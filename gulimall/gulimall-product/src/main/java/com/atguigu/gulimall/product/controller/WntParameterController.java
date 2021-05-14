package com.atguigu.gulimall.product.controller;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.product.ItemReadAndWrite.BachmannWindTurbineNode;
import com.atguigu.gulimall.product.entity.WntIpEntity;
import com.atguigu.gulimall.product.entity.WntParameterEntity;
import com.atguigu.gulimall.product.service.WntIpService;
import com.atguigu.gulimall.product.service.WntParameterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * spu信息
 *
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 22:50:32
 */
@RestController
@RequestMapping("product/wntparameter")
public class WntParameterController {
    @Autowired
    private WntParameterService wntParameterService;

    @Autowired
    private WntIpService wntIpService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private List<BachmannWindTurbineNode> listWnt = new ArrayList<>();

    private String[] strArrPara;

    List<Integer> listPra = new ArrayList<>();
    List<Integer> listIp = new ArrayList<>();

    @RequestMapping(value="/testtt")
    public  R testtt(){
        return R.ok();
    }

    /**
     * 根据前端返回的风机、参数，生成表格数据返回前端
     * ipAndWntPara格式如下  {"t":1619510117428,"ipAndWntPara":"1002,2001,3018&3,2,1"}
     *
     * @param ipAndWntPara
     * @throws IOException
     */
    @RequestMapping(value = "/showSelectPara")
    public void showSelectPara(@RequestBody String ipAndWntPara) throws IOException {
        System.out.println("ipAndWntPara   " + ipAndWntPara);
        String[] strArr = ipAndWntPara.split("ipAndWntPara");
        String[] strParaAndIp = strArr[1].substring(3, strArr[1].length() - 2).split("&");
        String[] arrPara = strParaAndIp[0].split(",");
        String[] arrIp = strParaAndIp[1].split(",");
        if (arrPara.length > 0) {
            for (String para : arrPara) {
                listPra.add(Integer.parseInt(para));
            }
        }
        if (arrIp.length > 0) {
            //前端如果风机全选，那么直接写值999
            if (arrIp[0].equals("ALL_SELECT")) {
                listIp.add(999);
                return;
            }
            for (String ip : arrIp) {
                listIp.add(Integer.parseInt(ip));
            }
        }
    }

    @GetMapping("/getTablePara")
    public R getTablePara() {
        List<WntParameterEntity> listSearchPara = null;
        if (listPra.size() < 1) {
            return R.error(407, "请先选择风机、参数");
        } else {
            listSearchPara = wntParameterService.list(new QueryWrapper<WntParameterEntity>().in(true, "third_node_id", listPra));
        }
        List<WntIpEntity> listSearchIp = null;
        if (listIp.size() < 1) {
            return R.error(407, "请先选择风机、参数");
        } else if (listIp.contains(999)) {
            listSearchIp = wntIpService.list(new QueryWrapper<WntIpEntity>().isNotNull("id"));
        } else {
            listSearchIp = wntIpService.list(new QueryWrapper<WntIpEntity>().in(true, "id", listIp));
        }

        List<HashMap<String, Object>> listHead = new ArrayList<>();
        HashMap<String, Object> mapHead = new LinkedHashMap<>();
        mapHead.put("id", 1);
        mapHead.put("name", "风机名");
        mapHead.put("code", 1);
        mapHead.put("editeFlag", false);
        listHead.add(mapHead);
        for (int i = 0; i < listSearchPara.size(); i++) {
            mapHead = new LinkedHashMap<>();
            mapHead.put("id", listSearchPara.get(i).getThirdNodeId());
            mapHead.put("name", listSearchPara.get(i).getThirdNode());
            mapHead.put("code", listSearchPara.get(i).getThirdNodeId());
            mapHead.put("editeFlag", true);
            listHead.add(mapHead);
        }

        List<HashMap<String, Object>> listTablePara = new ArrayList<>();

        for (int i = 0; i < listSearchIp.size(); i++) {
            HashMap<String, Object> mapTablePara = new LinkedHashMap<>();
            HashMap<String, Object> mapJsonPara = new LinkedHashMap<>();
            mapTablePara.put("num", i + 1);

            mapJsonPara.put("value", listSearchIp.get(i).getWntName());
            mapJsonPara.put("editing", false);
            mapTablePara.put("1", mapJsonPara);
            //读取每个 风机的每个参数值，连接风机后，方可使用
            for (int j = 0; j < listSearchPara.size(); j++) {
                mapJsonPara = new LinkedHashMap<>();
                //伪代码
//                mapJsonPara.put("value",listWnt.read(listSearchPara.get(j).getParameterName()))
                mapJsonPara.put("value", 222);
                mapJsonPara.put("editing", false);
                mapTablePara.put(String.valueOf(listSearchPara.get(j).getThirdNodeId()), mapJsonPara);
            }
            listTablePara.add(mapTablePara);
        }


        listPra.clear();
        listIp.clear();

        return R.ok().put("dataHead", listHead).put("dataPara", listTablePara);
    }


    @RequestMapping("/listWntIpId")
    public R listWntIpId() {
        List<Map<String, Object>> page = wntIpService.listMaps();
        List<String> listId = new ArrayList<>();
        listId.add("ALL_SELECT");

        for (Map<String, Object> map : page) {
            listId.add(String.valueOf(map.get("id")));
            System.out.println("map.get(\"wnt_id\")  " + map.get("id"));
        }
        return R.ok().put("data", listId);
    }

    @RequestMapping("/listWntIpName")
    public R listWntIpName() {
        List<Map<String, Object>> page = wntIpService.listMaps();
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> hashMapAll = new LinkedHashMap<>();
        hashMapAll.put("value", "ALL_SELECT");
        hashMapAll.put("label", "全部");
        list.add(hashMapAll);
        for (Map<String, Object> map : page) {
            HashMap<String, Object> hashMap = new LinkedHashMap<>();
            hashMap.put("value", map.get("id"));
            hashMap.put("label", map.get("wnt_name"));
//            System.out.println("map.get(\"wnt_id\")  " + map.get("id") + "   " + map.get("wnt_name"));
            list.add(hashMap);
        }
        return R.ok().put("data", list);
    }


    /**
     * 风机参数列表
     */
    @RequestMapping("/listParameter")
    //@RequiresPermissions("product:spuinfo:list")
    public R list() {


        List<WntParameterEntity> page = wntParameterService.list(new QueryWrapper<WntParameterEntity>().isNotNull("first_Node_Id"));
        List<Integer> listFirstNodeId = new ArrayList<>();
        listFirstNodeId.add(0);
        List<Integer> listSecondNodeId = new ArrayList<>();
        listSecondNodeId.add(90);
        List listFirst = new ArrayList();
        List listSecond = null;
        List listThird = null;
        Map<String, Object> mapFirst = null;
        for (WntParameterEntity wntParameterEntity : page) {


            if (!listFirstNodeId.contains(wntParameterEntity.getFirstNodeId())) {
                listFirstNodeId.add(wntParameterEntity.getFirstNodeId());
                listSecondNodeId.add(Integer.parseInt(wntParameterEntity.getFirstNodeId() + "" + wntParameterEntity.getSecondNodeId()));
                mapFirst = new LinkedHashMap<String, Object>();
                Map<String, Object> mapSecond = new LinkedHashMap<String, Object>();
                listSecond = new ArrayList();
                listThird = new ArrayList();
                System.out.println("mapSecond1   " + mapSecond.hashCode());

                mapFirst.put("value", wntParameterEntity.getFirstNodeId());
                mapFirst.put("label", wntParameterEntity.getFirstNode());

                mapSecond.put("value", wntParameterEntity.getSecondNodeId());
                mapSecond.put("label", wntParameterEntity.getSecondNode());

                Map<String, Object> mapThird = new LinkedHashMap<String, Object>();
                mapThird.put("value", wntParameterEntity.getThirdNodeId());
                mapThird.put("label", wntParameterEntity.getThirdNode());
                listThird.add(mapThird);

                mapSecond.put("children", listThird);
                listSecond.add(mapSecond);
                mapFirst.put("children", listSecond);

                listFirst.add(mapFirst);

            } else {
                if (!listSecondNodeId.contains(Integer.parseInt(wntParameterEntity.getFirstNodeId() + "" + wntParameterEntity.getSecondNodeId()))) {
                    //二级目录ID的判断逻辑，应该是 一级id + 二级id
                    listSecondNodeId.add(Integer.parseInt(wntParameterEntity.getFirstNodeId() + "" + wntParameterEntity.getSecondNodeId()));
                    Map<String, Object> mapSecond = new LinkedHashMap<String, Object>();
                    listThird = new ArrayList();
                    System.out.println("mapSecond2   " + mapSecond.hashCode());

                    Map<String, Object> mapThird = new LinkedHashMap<String, Object>();
                    mapThird.put("value", wntParameterEntity.getThirdNodeId());
                    mapThird.put("label", wntParameterEntity.getThirdNode());
                    listThird.add(mapThird);

                    mapSecond.put("value", wntParameterEntity.getSecondNodeId());
                    mapSecond.put("label", wntParameterEntity.getSecondNode());
                    mapSecond.put("children", listThird);
                    listSecond.add(mapSecond);
                    System.out.println("listFirstsize2222222222222" + listFirst.size());

                } else {
                    Map<String, Object> mapThird = new LinkedHashMap<String, Object>();
                    mapThird.put("value", wntParameterEntity.getThirdNodeId());
                    mapThird.put("label", wntParameterEntity.getThirdNode());
                    listThird.add(mapThird);
                }
            }


        }
        return R.ok().put("data", listFirst);
    }

    @PostMapping("/fileUpload")
    public R fileUploads(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {

        // 得到格式化后的日期

        String format = sdf.format(new Date());
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        // 时间 和 日期拼接
        String newFileName = format + "-" + fileName.trim();

        String filePath = "";
        System.out.println("wenjian chadu   " + file.getSize());
        if (System.getProperties().getProperty("os.name").contains("Windows")) {
            filePath = "D:\\ParameterChange\\";
        } else {
            filePath = "/usr/local/ParameterChange/";
        }
        System.out.println("文件名  " + file.getName());
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        System.out.println("1111");
        // 得到文件保存的位置以及新文件名
        File dest = new File(filePath + newFileName);
        System.out.println("22222" + dest.toString());
        try {
            // 上传的文件被保存了
            file.transferTo(dest);
            System.out.println("文件绝对路径   " + dest.getAbsolutePath() + "   " + dest.getAbsolutePath());
            if (dest.exists()) {
                parseExcel(dest.getAbsolutePath());
            }
            // 打印日志
            // 自定义返回的统一的 JSON 格式的数据，可以直接返回这个字符串也是可以的。
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 待完成 —— 文件类型校验工作
        return R.error();

    }

    @RequestMapping("/downContextExcel")
    public void downContextExcel(HttpServletResponse response) throws IOException {
//        strArrPara = new String[]{"A/Aa1,B/Bb1,C/Cc1", "A/Aa2,A/Aa2,B/Bb2,C/Cc2", "A/Aa3,A/Aa3,B/Bb3,C/Cc3"};
        if (strArrPara != null && strArrPara.length > 0) {
            export(strArrPara, response);
        }
        strArrPara = null;
//        return R.ok();
    }

    @PostMapping(value = "/uploadContext")
    public void selectValue(@RequestBody String strContext) throws IOException {
        System.out.println(strContext);
        String[] strS = strContext.split(":");
        String strPara = strS[strS.length - 1];
        strPara = strPara.substring(1, strPara.length() - 2);//strPara    "A/Aa1,B/Bb1,C/Cc1;A/Aa2,A/Aa2,B/Bb2,C/Cc2;A/Aa3,A/Aa3,B/Bb3,C/Cc3"
        String[] strW = strPara.split(";");
        strArrPara = strW;
        for (String str : strArrPara)
            System.out.println("str    " + str);
    }

    //生成模板
    // 前端参数格式  A/Aa1,B/Bb1,C/Cc1;A/Aa2,A/Aa2,B/Bb2,C/Cc2;A/Aa3,A/Aa3,B/Bb3,C/Cc3
    public void export(String[] strW, HttpServletResponse response) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        String sheetName = "风机参数导出";
        HSSFSheet sheet = workbook.createSheet(sheetName);
        List<HSSFRow> listRow = new ArrayList<HSSFRow>();
        HSSFRow rowHead = sheet.createRow(0);
        List<String> listHead = new ArrayList<>();//存表头参数

        int sheetColCount = strW[0].split(",").length;
        for (int i = 0; i < strW.length; i++) {
            HSSFRow row = sheet.createRow(i + 1);
            String[] strCell = strW[i].split(",");
            for (int j = 0; j < strCell.length; j++) {
                String strHead = strCell[j].split("/")[0];
                String strValue = strCell[j].split("/")[1];
                if (!listHead.contains(strHead)) {
                    listHead.add(strHead);
                    rowHead.createCell(j).setCellValue(strHead);
                }
                System.out.println("listHead.indexOf(strHead)  " + strHead + "      " + listHead.indexOf(strHead));
                row.createCell(listHead.indexOf(strHead)).setCellValue(strValue);

            }


        }
        System.out.println("aaaaaa  " + listHead.contains("A"));
        for (int i = 0; i <= sheetColCount; i++) {
            sheet.setColumnWidth(i, 5000);
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(sheetName + ".xls", "utf-8"));
        //将数据写入
        //将文件保存到指定的位置
        try {
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseExcel(String localUrl) throws Exception {
        HSSFWorkbook wb = null;
//Excel文件
        try {
            wb = new HSSFWorkbook(new FileInputStream(ResourceUtils.getFile(localUrl)));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Excel工作表
        HSSFSheet sheet = wb.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();//单元格行数
        boolean statusRead = false;//excel有内容，才会进行读取
        List<String> listHead = new ArrayList<>();//存表头参数

        if (rowCount > 0) {
            statusRead = true;
        }
        HSSFRow titleRow = sheet.getRow(0);
        int row0Index = 1; //从第二列开始,因为第一个单元是  风机名
        while (statusRead) {
            //读取表头每个单元格
            String strCell = "";
            try {
                strCell = titleRow.getCell(row0Index).getStringCellValue().trim();
                if (strCell != null && strCell.length() > 0) {
                    listHead.add(strCell);
                    row0Index++;
                } else {
                    statusRead = false;
                }
            } catch (Exception ex) {
                statusRead = false;
            }

        }
        HashMap<String, String> hashPra = initPara(listHead);
        List<String> listWntName = new ArrayList<>();
        //获取每一列的风机名，并且根据风机名生成 风机名、ip对应关系
        for (int i = 1; i <= rowCount; i++) {
            HSSFRow contentRow = sheet.getRow(i);
            listWntName.add(contentRow.getCell(0).toString().trim());
        }
        HashMap<String, BachmannWindTurbineNode> hashNode = new HashMap<>();

        HashMap<String, String> hashIp = initIp(listWntName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Set<String> setName = hashIp.keySet();
                for (String name : setName) {
                    //涉及到频繁读取 文件，应该用 工厂模式
                    BachmannWindTurbineNode bwtNode = null;
                    try {
                        bwtNode = new BachmannWindTurbineNode("name", hashIp.get(name));
                        bwtNode.start();
                        hashNode.put(name, bwtNode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();
        Thread.sleep(5000);
        for (int i = 1; i <= rowCount; i++) {
            HSSFRow contentRow = sheet.getRow(i);
            String wntName = contentRow.getCell(0).toString().trim();
            BachmannWindTurbineNode bwtNode = hashNode.get(wntName);
            for (int j = 1; j < row0Index; j++) {
                String itemName = hashPra.get(listHead.get(j - 1));
                String itemValue = contentRow.getCell(j).toString().trim();
                System.out.println("读取excel   " + listHead.get(j) + "   值是  " + contentRow.getCell(j).toString().trim());
                bwtNode.writeItem(itemName,itemValue,"");
            }
        }

    }

    private void writeWntValue(String itemName, String writeValue, BachmannWindTurbineNode bwtNode) {

    }

    private HashMap initPara(List<String> listHead) {

        List<WntParameterEntity> listSearchPara = null;
        listSearchPara = wntParameterService.list(new QueryWrapper<WntParameterEntity>().in("third_node", listHead));
        HashMap<String, String> mapHead = new HashMap<>();
        //将数据库中查询的数据，整理为 参数名、参数地址的键值对
        for (WntParameterEntity wpe : listSearchPara) {
            mapHead.put(wpe.getThirdNode(), wpe.getParameterName());
        }
        return mapHead;
    }

    private HashMap initIp(List<String> listWntName) {

        List<WntIpEntity> listSearchIp = null;
        listSearchIp = wntIpService.list(new QueryWrapper<WntIpEntity>().in("wnt_name", listWntName));
        HashMap<String, String> mapIp = new HashMap<>();
        for (WntIpEntity wie : listSearchIp) {
            mapIp.put(wie.getWntName(), wie.getWntIp());
        }
        return mapIp;
    }

}
