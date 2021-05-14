<template>
  <div class="mod-config">
    <div class="el_selectAndCascaderMulti">
<el-select
class="sameDiv"
    style="width:20%"
    v-model="searchJobType"
    multiple
    collapse-tags
    @change="changeSelect"
    placeholder="请选择风机">
    <el-option v-for="(type,ind) in typeList"
      :key=ind
      :label="type.label"
      :value="type.value">
    </el-option>
</el-select>
<el-cascader-multi class="sameDiv" style="width:20%;margin-left:20px" ref="myCascader"  collapse-tags v-model="checkList" :data="data1" change="showChange"> </el-cascader-multi>

<el-button @click="showSearch" type="primary"  display="inline-block" class="btn_search">查询</el-button>
<el-button @click="showCell" type="primary"  display="inline-block" >点击导出</el-button>
	<el-button display="inline-block" type="primary" @click="handleUpdate">上传参数excel</el-button>
		<el-dialog
			title="提示"
			:visible.sync="dialogVisible"
			width="30%"
			>
			<span>
				<el-upload class="upload-demo"
					ref="upload"
					drag 
					action="http://localhost:88/api/product/wntparameter/fileUpload" 
					multiple
					:auto-upload="false"
					:limit="5"
					:on-success="handleFilUploadSuccess"
					:on-remove="handleRemove"
					>
					<i class="el-icon-upload"></i>
					<div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
					<div class="el-upload__tip" slot="tip">只能上传 Excel 文件，且不超过500kb</div>
				</el-upload>
			</span>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="handleUpload">确 定</el-button>
			</span>
		</el-dialog>

    </div>
    

  
  <!-- <el-table :data="tableData" stripe style="width: 100%" class="box-table">
  <el-table-column type="index" width="50"></el-table-column>
  <el-table-column v-for="(item,key,val, index) in tableData[0]" :key="index">
    <template slot="header">{{key}}</template>
    <template slot-scope="scope">{{tableData[scope.$index][key]}}</template>
  </el-table-column>
</el-table> -->
  
         <div class="out_table">
           



          <el-table
            border
            :data="items"
            class="tb-edit"
            style="width: 100%"
            highlight-current-row
          >
            
     <el-table-column
          v-for="it in xmls"
          :key="it.id"
          :label="it.name"
          :prop="it.code"
          width="100"
          :show-overflow-tooltip="true"
          align="right"
        >
          <template scope="scope">
            <span
              v-if="it.editeFlag&&scope.row[it.code].editing"
              style="display:inline-block;width:100%;height:100%;"
            >
              <el-input
                ref="inputRef"
                v-model="scope.row[it.code].value"
                placeholder="请输入内容"
                @change="closeEdit(scope.row,it)"
                @blur="closeEdit(scope.row,it)"
              />
            </span>
            <span
              v-if="!scope.row[it.code].editing"
              style="display:inline-block;width:100%;height:100%;"
              @click="handleEdit(scope.row,it)"
            >{{scope.row[it.code].value}}</span>
          </template>
        </el-table-column>

          </el-table>
          
        </div>

  </div>
  
</template>

<script>
// 引入导出Excel表格依赖
import FileSaver from "file-saver";
import XLSX from "xlsx";
import less from 'less'

 export default {
    created () {
      this.selectData()
    },
    data () {
        return {


          
          dataShow:[],
          dialogVisible: false,
          data1:[],
          searchJobType: ['ALL_SELECT','TSINPUT', '01', '02', '03', '04', '05', '07', '08', '09', '11', '12'],
          oldSearchJobType: [],

xmls : [
      { id: 1, name: "A", code: "aaa", editeFlag: true },
      { id: 2, name: "B", code: "bbb", editeFlag: false },//定义第二列不能编辑
      { id: 3, name: "C", code: "ccc", editeFlag: true }
    ],
    items : [
      {
        id: 11,
        xm: "A资金",
        num: 1,
        aaa: {
          value: "Aa1",
          editing: false//定义数据是否在编辑状态
        },
        bbb: {
          value: "Bb1",
          editing: false
        },
        ccc: {
          value: "Cc1",
          editing: false
        }
      },
      {
        id: 12,
        xm: "B资金",
        num: 2,
        aaa: {
          value: "Aa2",
          editing: false
        },
        bbb: {
          value: "Bb2",
          editing: false
        },
        ccc: {
          value: "Cc2",
          editing: false
        }
      },
      {
        id: 13,
        xm: "C资金",
        num: 3,
        aaa: {
          value: "Aa3",
          editing: false
        },
        bbb: {
          value: "Bb3",
          editing: false
        },
        ccc: {
          value: "Cc3",
          editing: false
        }
      }
    ],












          typeList: [
            {value: 'ALL_SELECT', label: '全部'},
            {value: 'TSINPUT', label: '时序数据采集任务'},
            {value: '01', label: 'RDBMS → HIVE全量'},
            {value: '02', label: 'RDBMS → HDFS全量'},
            {value: '03', label: 'RDBMS → HBASE全量'},
            {value: '04', label: 'HDFS → RDBMS'},
            {value: '05', label: 'HIVE  → RDBMS'},
            {value: '07', label: 'RDBMS → HIVE增量'},
            {value: '08', label: 'RDBMS → HBASE增量'},
            {value: '09', label: '文件 → RDBMS'},
            {value: '11', label: '数据对象 → 数据对象'},
            {value: '12', label: 'FTP服务器 → 文件系统'},
        ],
        data: [{
          value: 1,
          label: '东南',
          children: [{
            value: 2,
            label: '上海',
            children: [
              { value: 3, label: '普陀' },
              { value: 4, label: '黄埔' },
              { value: 5, label: '徐汇' }
            ]
          }, {
            value: 7,
            label: '江苏',
            children: [
              { value: 8, label: '南京' },
              { value: 9, label: '苏州' },
              { value: 10, label: '无锡' }
            ]
          }, {
            value: 12,
            label: '浙江',
            children: [
              { value: 13, label: '杭州' },
              { value: 14, label: '宁波' },
              { value: 15, label: '嘉兴' }
            ]
          }]
        }, {
          value: 2,
          label: '西北',
          children: [{
            value: 1,
            label: '陕西',
            children: [
              { value: 1, label: '西安' },
              { value: 2, label: '延安' }
            ]
          }, {
            value: 2,
            label: '新疆维吾尔族自治区',
            children: [
              { value: 1, label: '乌鲁木齐' },
              { value: 2, label: '克拉玛依' }
            ]
          }]
        }],
           checkList: [],
           tableData: [{
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄',sex: '女'
        }, {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄',sex: '女'
        }, {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄',sex: '女'
        }, {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄',sex: '女'
        }, {
          date: '2016-05-08',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄',sex: '女'
        }, {
          date: '2016-05-06',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄',sex: '女'
        }, {
          date: '2016-05-07',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄',sex: '女'
        }]
      }
    },
    methods:{

 showChange(){
        console.log("show嫦娥  ")
    },

        changeSelect(val) {
        const allValues = [];
        // 保留所有值
        for (const item of this.typeList) {
          allValues.push(item.value)
        }
        // 用来储存上一次的值，可以进行对比
        const oldVal = this.oldSearchJobType.length === 1 ? this.oldSearchJobType[0] : [];
        // 若是全部选择
        if (val.includes('ALL_SELECT')) this.searchJobType = allValues;
        // 取消全部选中 上次有 当前没有 表示取消全选
        if (oldVal.includes('ALL_SELECT') && !val.includes('ALL_SELECT')) this.searchJobType = [];
        // 点击非全部选中 需要排除全部选中 以及 当前点击的选项
        // 新老数据都有全部选中
        if (oldVal.includes('ALL_SELECT') && val.includes('ALL_SELECT')) {
          const index = val.indexOf('ALL_SELECT');
          val.splice(index, 1); // 排除全选选项
          this.searchJobType = val
        }
        // 全选未选 但是其他选项全部选上 则全选选上 上次和当前 都没有全选
        if (!oldVal.includes('ALL_SELECT') && !val.includes('ALL_SELECT')) {
          if (val.length === allValues.length - 1) this.searchJobType = ['ALL_SELECT'].concat(val)
        }
        // 储存当前最后的结果 作为下次的老数据
        this.oldSearchJobType[0] = this.searchJobType;

        console.log("输出选择结果     "+this.oldSearchJobType)
      },
      selectData() {

      //初始化风机参数
      this.$http({
        url: this.$http.adornUrl("/product/wntparameter/listParameter"),
        method: "get",
        // data: this.$http.adornData({brandId:this.brandId,catelogId:this.catelogPath[this.catelogPath.length-1]}, false)
      }).then(({ data }) => {
                console.log("成功获取到菜单数据...", data.data);
        this.data1 = data.data;
      });

      //初始化风机ip
      this.$http({
        url: this.$http.adornUrl("/product/wntparameter/listWntIpId"),
        method: "get",
      }).then(({ data }) => {
                console.log("成功获取到风机参数ip1111111=...", data.data);
        this.searchJobType = data.data;
      });

      //初始化风机ip
      this.$http({
        url: this.$http.adornUrl("/product/wntparameter/listWntIpName"),
        method: "get",
      }).then(({ data }) => {
                console.log("成功获取到风机参数ip2222...", data.data);
        this.typeList = data.data;
      });
      
    },

handleRemove(file,fileList) {
				console.log(file,fileList);
			},
			submitUpload() {
				this.$refs.upload.submit();
			},
			// 文件上传成功时的函数
			handleFilUploadSuccess (res,file,fileList) {
				console.log(res,file,fileList)
				this.$message.success("上传成功")
			},
			handleUpdate () {
				this.dialogVisible = true;
			},
			// 处理文件上传的函数
			handleUpload () {
				// console.log(res,file)
				this.submitUpload()
				this.dialogVisible = false
			},
       handleEdit(index, row) {
      var sum = 0
      this.tableHead.forEach(element => {
        sum +=Number(this.tableData[index][element.item]) 
      });
      row.total =sum
    },
    exportExcel() {
/* 从表生成工作簿对象 */
var wb = XLSX.utils.table_to_book(document.querySelector("#out_table"));
/* 获取二进制字符串作为输出 */
var wbout = XLSX.write(wb, {
bookType: "xlsx",
bookSST: true,
type: "array"
});
try {
FileSaver.saveAs(
//Blob 对象表示一个不可变、原始数据的类文件对象。
//Blob 表示的不一定是JavaScript原生格式的数据。
//File 接口基于Blob，继承了 blob 的功能并将其扩展使其支持用户系统上的文件。
//返回一个新创建的 Blob 对象，其内容由参数中给定的数组串联组成。
new Blob([wbout], { type: "application/octet-stream" }),
//设置导出文件名称
"sheetjs.xlsx"
);
} catch (e) {
if (typeof console !== "undefined") console.log(e, wbout);
}
return wbout;
},
handleEdit(row, it) {
      //遍历数组改变editeFlag
      if (it.editeFlag) {
        row[it.code].editing = true;
        this.$nextTick(function() {
          //DOM 更新了
          console.log(this.$refs.inputRef);
          this.$refs.inputRef[0].focus();
        });
      }
    },
    closeEdit(row, it) {
      row[it.code].editing = false;
    },
showSearch(val){
// this.dataShow = this.$refs['myCascader'].currentLabels;    //注意2： 获取label值
//  this.dataShow = this.$refs['myCascader'].inputValue;
// this.dataShow=this.$refs["myCascader"].getCheckedNodes()[0].label;
// this.$refs['myCascader'].currentLabels
        //  console.log("labelValue 1111   "+this.$refs["myCascader"].getCheckedNodes()[0].pathLabels); // 注意3： 最终结果是个一维数组对象
        
// var thsAreaCode=this.$refs["myCascader"].currentLabels;
//          console.log("labelValue 1111   "+thsAreaCode[0]); // 注意3： 最终结果是个一维数组对象


// var thsAreaCode = this.$refs['myCascader'].currentLabels    //注意2： 获取label值
// 			alert(thsAreaCode)  // 注意3： 最终结果是个一维数组对象
            //   let nodesObj = this.$refs['myCascader'];
            console.log("nodesObj    "+this.$refs['myCascader'].getCheckedNodes()[0]);
},  


    showCell(){
      var tableSelect = document.getElementById("el-table__body");
 var arrBody=document.getElementsByTagName("table")[1].getElementsByTagName("span");//表体
  var arrHead=document.getElementsByTagName("table")[0].getElementsByTagName("div");//表头 
console.log("测试输出单元格内容    " +arrBody.length+"   "+arrBody[1].innerHTML);
var headSize=arrHead.length;
var strContext="";
for(var i = 0; i < arrBody.length; i++) {
  if(strContext.length>0 && i%headSize==0){
    strContext=strContext+";"+arrHead[i%headSize].innerHTML+"/"+arrBody[i].innerHTML+",";
  }
  strContext=strContext+arrHead[i%headSize].innerHTML+"/"+arrBody[i].innerHTML+",";

}
console.log("测试循环输出单元格内容 11111111111111   " +strContext);
strContext=strContext.substring(0,strContext.length-1)
strContext=strContext.replace(",;",";");
strContext=strContext.replace(",;",";");
console.log("测试循环输出单元格内容    " +strContext);
 this.$http({
              url: this.$http.adornUrl('/product/wntparameter/uploadContext'),
              method: 'post',
              data: this.$http.adornData({
              strContext
              })
            })
//  this.$http({
//               url: this.$http.adornUrl('/product/wntparameter/selectValue'),
//               method: 'get',
//               responseType: 'blob'
//             }).then(({ data }) => {
        
// const link = document.createElement('a');
//         let blob = new Blob([data.data], {type: 'application/vnd.ms-excel'});
//         link.style.display = 'none';
//         link.href = URL.createObjectURL(blob);
//         let num = '';
//         for (let i = 0; i < 10; i++) {
//           num += Math.ceil(Math.random() * 10)
//         }
//         link.setAttribute('download', 1234 + '.xls');
//         document.body.appendChild(link);
//         link.click();
//         document.body.removeChild(link)

//       });
    var url = 'http://localhost:88/api/product/wntparameter/downContextExcel'
    window.open(url)
    },
   


}}
</script>
<style lang="less">

.btn_search{
  margin-top: 100pxx;
  margin-right: 5px;
  width: 150px;
}
.btn_upload{
  margin-right: 5px;
  width: 150px;
}
.selectWnt{
  parding-top: 100px;
  parding-left: 100px;
  width: 150px;
}
.selectPara{
  parding-top: 100px;
  parding-left: 100px;
  width: 150px;
}
.sameDiv{
  margin-top:20px;
  font-size:16px;
  font-weight:900;
  display:inline-block;
}
.btn_search{
  margin-left:450px;
}
</style>