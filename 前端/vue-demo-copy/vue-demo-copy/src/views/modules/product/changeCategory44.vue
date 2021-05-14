<template>
  <div class="mod-config">
<el-select
    v-model="searchJobType"
    multiple
    collapse-tags
    @change="changeSelect"
    placeholder="请选择类型查询">
    <el-option v-for="(type,ind) in typeList"
      :key=ind
      :label="type.label"
      :value="type.value">
    </el-option>
</el-select>
<el-cascader-multi v-model="checkList" :data="data1" > </el-cascader-multi>
  
  <!-- <el-table :data="tableData" stripe style="width: 100%" class="box-table">
  <el-table-column type="index" width="50"></el-table-column>
  <el-table-column v-for="(item,key,val, index) in tableData[0]" :key="index">
    <template slot="header">{{key}}</template>
    <template slot-scope="scope">{{tableData[scope.$index][key]}}</template>
  </el-table-column>
</el-table> -->
  <div class="table" id="out-table">
   <el-table
            :data="tableData"
			border
            style="width: 100%"  >
        <!-- <el-table-column  v-for="item in tableHead"  :property="item.property" width="180" :key="item.label"> -->
          <el-table-column
    v-for="item in tableHead"
    :key="item.property"
    :label="item.label"
    :prop="item.property"
    :width="180"
   
  >
	<!-- :data="item.label" -->
<!-- show-overflow-tooltip
    :min-width="item.minWidth" -->

            <template slot-scope="scope">
                <el-input  v-model="scope.row[scope.column.property]" ></el-input>
            </template>
        </el-table-column>

        <el-table-column label="操作">
            <template slot-scope="scope">
                <el-button
                        size="mini"
                        @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                <el-button
                        size="mini"
                        type="danger"
                        @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
  <button @click="exportExcel">点击导出</button>
</div>
  


	<el-button class="btn-upload" type="primary" @click="handleUpdate">上传参数excel</el-button>
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


  
</template>

<script>
// 引入导出Excel表格依赖
import FileSaver from "file-saver";
import XLSX from "xlsx";


 export default {
    created () {
      this.selectData()
    },
    data () {
        return {
          dialogVisible: false,
          data1:[],
          searchJobType: ['ALL_SELECT','TSINPUT', '01', '02', '03', '04', '05', '07', '08', '09', '11', '12'],
          oldSearchJobType: [],

tableHead:[
                  {
                      label:'日期',
                      property:'date'
                  },{
                      label:'姓名',
                      property:'name'
                  },{
                      label:'地址',
                      property:'address'
                  }
              ],
                // 数据值
                tableData: [{
                    date: '2016-05-02',
                    name: '王小虎6666',
                    address: '上海市普陀区金沙江路 1518 弄'
                }, {
                    date: '2016-05-04',
                    name: '王小虎45',
                    address: '上海市普陀区金沙江路 1517 弄'
                }, {
                    date: '2016-05-01',
                    name: '王小虎333',
                    address: '上海市普陀区金沙江路 1519 弄'
                }, {
                    date: '2016-05-03',
                    name: '王小虎222',
                    address: '上海市普陀区金沙江路 1516 弄'
                }],






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
      },
      selectData() {
      this.$http({
        url: this.$http.adornUrl("/product/wntparameter/listParameter"),
        method: "get",
        // data: this.$http.adornData({brandId:this.brandId,catelogId:this.catelogPath[this.catelogPath.length-1]}, false)
      }).then(({ data }) => {
                console.log("成功获取到菜单数据...", data.data);
        this.data1 = data.data;
      });
      
    },
exportExcel() {
/* 从表生成工作簿对象 */
var wb = XLSX.utils.table_to_book(document.querySelector("#out-table"));
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
			}


}}
</script>
<style scoped="scoped">
	.btn-upload {
		top: 110px;
		right: 40px;
		position: fixed;
		z-index: 100;
		border-radius: 30px;
		box-shadow: 0 2px 12px 0 rgba(91, 156, 255, 0.9)
	}
	
	.el-upload {
		margin: 80px;
	}
</style>