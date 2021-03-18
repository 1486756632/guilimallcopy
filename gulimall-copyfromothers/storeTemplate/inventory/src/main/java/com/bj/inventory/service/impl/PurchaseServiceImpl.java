package com.bj.inventory.service.impl;

import com.bj.constant.WareConstant;
import com.bj.inventory.entity.PurchaseDetailEntity;
import com.bj.inventory.service.PurchaseDetailService;
import com.bj.inventory.service.WareSkuService;
import com.bj.inventory.vo.MergeVo;
import com.bj.inventory.vo.PurchaseFinshedVo;
import com.bj.inventory.vo.PurchaseItemVo;
import com.bj.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;

import com.bj.inventory.dao.PurchaseDao;
import com.bj.inventory.entity.PurchaseEntity;
import com.bj.inventory.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    private PurchaseDetailService purchaseDetailService;
    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<PurchaseEntity>();
        //查询未被领取的采购单，只有没有领取的采购单才能被合并
        wrapper.eq("status", 0).or().eq("status", 1);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public R merge(MergeVo mergeVo) {
        if (CollectionUtils.isEmpty(mergeVo.getItems())) {
            return R.error("请选择采购需求");
        }
        List<Long> ids = mergeVo.getItems();
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(ids);
        //过滤出未领取采购需求
        List<Long> detailIds = purchaseDetailEntities.stream().filter(item -> {
            if (item.getStatus().equals(WareConstant.CREATE.getCode()) ||
                    item.getStatus().equals(WareConstant.ASSIGNED.getCode())
            ) {
                return true;
            } else {
                return false;
            }
        }).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(detailIds)) {
            return R.error("请选择至少一个未领取的采购需求");
        }
        if (mergeVo.getPurchaseId() == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.CREATE.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            Long id = purchaseEntity.getId();
            mergeVo.setPurchaseId(id);
        } else {
            Long purchaseId = mergeVo.getPurchaseId();
            PurchaseEntity purchase = this.getById(purchaseId);
            if (!purchase.getStatus().equals(WareConstant.CREATE.getCode())
                    && !purchase.getStatus().equals(WareConstant.ASSIGNED.getCode())
            ) {
                return R.error("不允许合并到已领取的采购单");
            }
        }
        List<PurchaseDetailEntity> collect = detailIds.stream().map(id -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(id);
            purchaseDetailEntity.setPurchaseId(mergeVo.getPurchaseId());
            purchaseDetailEntity.setStatus(WareConstant.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(mergeVo.getPurchaseId());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
        return R.ok();
    }

    @Transactional
    @Override
    public R receive(List<Long> ids) {
        //1.确认当前采购单是否是未领取的
        List<PurchaseEntity> purchaseEntities = this.listByIds(ids);
        List<PurchaseEntity> collect = purchaseEntities.stream().filter(item -> {
            if (item.getStatus().equals(WareConstant.CREATE.getCode()) ||
                    item.getStatus().equals(WareConstant.ASSIGNED.getCode())
            ) {
                return true;
            } else {
                return false;
            }
        }).map(item -> {
            item.setUpdateTime(new Date());
            item.setStatus(WareConstant.ORDERING.getCode());
            return item;
        }).collect(Collectors.toList());
        //2.改变采购单的状态
        this.updateBatchById(collect);
        //3.改变采购需求单的状态
        List<Long> purIds = collect.stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        List<PurchaseDetailEntity> purchaseDetailList = purchaseDetailService.listBypurIds(purIds);
        List<PurchaseDetailEntity> detailEntities = purchaseDetailList.stream().map(item -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item.getId());
            detailEntity.setStatus(WareConstant.ORDERING.getCode());
            return detailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(detailEntities);
        return R.ok();
    }

    @Transactional
    @Override
    public R finshed(PurchaseFinshedVo finshedVo) {
        Long purchaseId = finshedVo.getPurchaseId();
        PurchaseEntity purchaseEntity = this.getById(purchaseId);
        if (purchaseEntity == null) {
            throw new RuntimeException("采购单不存在");
        }
        //1.改变采购项状态
        List<PurchaseItemVo> items = finshedVo.getItems();
        Boolean flag = true;
        List<Long> detailIds = new ArrayList<>();//已完成需求项
        List<PurchaseDetailEntity> detailList = new ArrayList<>();
        for (PurchaseItemVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item.getItemId());
            if (item.getStatus().equals(WareConstant.ERROR.getCode())) {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.FINISHED.getCode());
                detailIds.add(detailEntity.getId());
            }
            detailList.add(detailEntity);
        }
        purchaseDetailService.updateBatchById(detailList);
        //2改变采购单状态
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setId(purchaseId);
        if (flag) {
            purchase.setStatus(WareConstant.FINISHED.getCode());
        } else {
            purchase.setStatus(WareConstant.ERROR.getCode());
        }
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchase);
        //3.将成功采购的进行入库
        List<PurchaseDetailEntity> detailEntities = purchaseDetailService.listByIds(detailIds);
        wareSkuService.addStock(detailEntities);
        // TODO:   将未成功采购的进行入库
        return R.ok();
    }

}