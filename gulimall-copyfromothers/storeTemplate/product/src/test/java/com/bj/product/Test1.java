package com.bj.product;

import com.bj.product.dao.AttrAttrgroupRelationDao;
import com.bj.product.dao.BrandDao;
import com.bj.product.dao.CategoryBrandRelationDao;
import com.bj.product.entity.BrandEntity;
import com.bj.product.service.BrandService;
import com.bj.product.service.CategoryService;
import com.bj.product.vo.Catelog2Vo;
import com.bj.product.vo.SpuItemAttrGroup;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Test1
 * @Description TODO
 * @Author 13011
 * @Date 2020/8/2 22:28
 * @Version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test1 {
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private AttrAttrgroupRelationDao brandRelationDao;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void test() {
        /*List<Long> catelogPath = categoryService.getCatelogPath(225l);
        System.out.println(catelogPath);*/
        String catlogJson = redisTemplate.opsForValue().get("catlogJson");
        System.out.println(catlogJson);
    }

    @Test
    public void test2() {
//        List<BrandEntity> brandEntities = brandDao.test();
//        System.out.println(brandEntities);
        Map<String, List<Catelog2Vo>> catlog = categoryService.getCatlog();
        System.out.println(catlog.toString());

    }
    @Test
    public void test5() {
//        List<BrandEntity> brandEntities = brandDao.test();
//        System.out.println(brandEntities);
        List<SpuItemAttrGroup> attrs = brandRelationDao.getAttrBySpuIdAndCatId(8l, 225l);
        System.out.println(attrs.toString());
    }

    @Test
    public void test3() {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream() ) { // ?????????pdf
            OutputStream os2=new FileOutputStream("C:\\Users\\13011\\Desktop\\out3.pdf");
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            //???????????????????????????????????? ????????????   ???????????????pdf????????? ???????????????####
            builder.useFont(new FSSupplier<InputStream>() {
                @Override
                public InputStream supply() {
                    try {
                        //?????? ????????????
                        return new FileInputStream("L:\\sourcehansanscnregular_downcc\\SourceHanSansCN-Regular.ttf");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }, "SourceHanSansCN-Regular", 400, BaseRendererBuilder.FontStyle.NORMAL, true); //??????????????? ??????????????????????????????????????????html?????????
            //??????????????????html??????  ??????????????? ????????????????????? ????????????????????? ????????????????????????
            builder.withHtmlContent("<html >\n" +
                    "<head>\n" +
                    "    <title>Document</title>\n" +
                    "    <style>\n" +
                    "        *{\n" +
                    "        font-family: 'SourceHanSansCN-Regular';\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "\n" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    "    <div style=\"text-align: center; color:red;\">??????</div>\n" +
                    "  <p>?????????????????? ??????</p>  \n" +
                    "  <p>?????????</p> \n" +
                    "</body>\n" +
                    "</html>", null);
            builder.toStream(os);
            builder.run();
            byte[] bytes = os.toByteArray();
            for (byte b : bytes) {
                System.out.println(b);
                os2.write(b);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

