package com.bj.inventory.service.impl;

import com.bj.dto.mq.StockDetailTo;
import com.bj.dto.mq.StockLockedTo;
import com.bj.exceptionCode.NotStockException;
import com.bj.inventory.entity.PurchaseDetailEntity;
import com.bj.inventory.entity.WareOrderTaskDetailEntity;
import com.bj.inventory.entity.WareOrderTaskEntity;
import com.bj.inventory.feign.OrderFeignService;
import com.bj.inventory.feign.ProductFeignService;
import com.bj.inventory.service.WareOrderTaskDetailService;
import com.bj.inventory.service.WareOrderTaskService;
import com.bj.inventory.vo.OrderItemVo;
import com.bj.inventory.vo.SkuStockVo;
import com.bj.inventory.vo.WareSkuLockVo;
import com.bj.utils.R;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;

import com.bj.inventory.dao.WareSkuDao;
import com.bj.inventory.entity.WareSkuEntity;
import com.bj.inventory.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Slf4j
@RabbitListener(queues = "stock.release.stock.queue")
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Resource
    private WareSkuDao wareSkuDao;
    @Resource
    private ProductFeignService productFeignService;
    @Autowired
    private WareOrderTaskService orderTaskService;
    @Autowired
    private WareOrderTaskDetailService orderTaskDetailService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderFeignService orderFeignService;

    @Value("${myRabbitmq.MQConfig.eventExchange}")
    private String eventExchange;

    @Value("${myRabbitmq.MQConfig.routingKey}")
    private String routingKey;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByContidion(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<WareSkuEntity>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            wrapper.eq("sku_id", skuId);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq("ware_id", wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void addStock(List<PurchaseDetailEntity> detailEntities) {
        List<WareSkuEntity> addList = new ArrayList<>();
        List<WareSkuEntity> updateList = new ArrayList<>();
        for (PurchaseDetailEntity item : detailEntities) {
            WareSkuEntity wareSkuEntity = baseMapper.selectOne(new QueryWrapper<WareSkuEntity>()
                    .eq("sku_id", item.getSkuId())
                    .eq("ware_id", item.getWareId()));
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(item.getSkuId());
            skuEntity.setWareId(item.getWareId());
            skuEntity.setStockLocked(0);
            R r = productFeignService.skuInfo(item.getSkuId());
            String code = String.valueOf(r.get("code"));
            if("0".equalsIgnoreCase(code)){
                Map<String,Object> data =(Map<String, Object>) r.get("skuInfo");
                if(data!=null){
                    String skuName = (String) data.get("skuName");
                    skuEntity.setSkuName(skuName);
                }
            }
            if (wareSkuEntity == null) {
                skuEntity.setStock(item.getSkuNum());
                addList.add(skuEntity);
            } else {
                skuEntity.setStock(wareSkuEntity.getStock() + item.getSkuNum());
                updateList.add(skuEntity);
            }
        }
        if (!CollectionUtils.isEmpty(addList)) {
            this.saveBatch(addList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            wareSkuDao.updateBatch(updateList);
        }
    }

    @Override
    public R hasStockBySku(List<Long> skuIds) {
        List<SkuStockVo> stockVos = skuIds.stream().map(skuId -> {
            SkuStockVo skuStockVo = new SkuStockVo();
            Long count = wareSkuDao.getSkuStock(skuId);
            skuStockVo.setHasStock(count==null?false:count > 0);
            skuStockVo.setSkuId(skuId);
            return skuStockVo;
        }).collect(Collectors.toList());

        return R.ok().put("data",stockVos);
    }

    @Transactional(rollbackFor = NotStockException.class)
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {
        // 当定库存之前先保存订单 以便后来消息撤回
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        orderTaskService.save(taskEntity);
        // [理论上]1. 按照下单的收获地址 找到一个就近仓库, 锁定库存
        // [实际上]1. 找到每一个商品在那个一个仓库有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock hasStock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            hasStock.setSkuId(skuId);
            // 查询这两个商品在哪有库存
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            hasStock.setWareId(wareIds);
            hasStock.setNum(item.getCount());
            return hasStock;
        }).collect(Collectors.toList());

        for (SkuWareHasStock hasStock : collect) {
            Boolean skuStocked = true;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if(wareIds == null || wareIds.size() == 0){
                // 没有任何仓库有这个库存
                throw new NotStockException(skuId.toString());
            }
            // 如果每一个商品都锁定成功 将当前商品锁定了几件的工作单记录发送给MQ
            // 如果锁定失败 前面保存的工作单信息回滚了
            for (Long wareId : wareIds) {
                // 成功就返回 1 失败返回0
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if(count == 1){
                    // TODO 告诉MQ库存锁定成功 一个订单锁定成功 消息队列就会有一个消息
                    WareOrderTaskDetailEntity detailEntity = new WareOrderTaskDetailEntity(null,skuId,"",hasStock.getNum() ,taskEntity.getId(),wareId,1);
                    orderTaskDetailService.save(detailEntity);
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setId(taskEntity.getId());
                    StockDetailTo detailTo = new StockDetailTo();
                    BeanUtils.copyProperties(detailEntity, detailTo);
                    // 防止回滚以后找不到数据 把详细信息页
                    stockLockedTo.setDetailTo(detailTo);

                    rabbitTemplate.convertAndSend(eventExchange, routingKey ,stockLockedTo);
                    skuStocked = false;
                    break;
                }
                // 当前仓库锁定失败 重试下一个仓库
            }
            if(skuStocked){
                // 当前商品在所有仓库都没锁柱
                throw new NotStockException(skuId.toString());
            }
        }
        // 3.全部锁定成功
        return true;
    }
    @Data
    class SkuWareHasStock{

        private Long skuId;

        private List<Long> wareId;

        private Integer num;
    }
}