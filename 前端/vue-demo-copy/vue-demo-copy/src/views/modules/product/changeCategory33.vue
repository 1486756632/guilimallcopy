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
  
  <el-table :data="tableData" stripe style="width: 100%" class="box-table">

<el-table-column
    prop="number"
    label="序号"
    width="60"
    type="index"
    align="center"
  />
  <el-table-column
    v-for="item in tableColumns"
    :key="item.key"
    :label="item.name"
    :prop="item.key"
    show-overflow-tooltip
    :min-width="item.minWidth"
    :width="item.width"
    align="center"
  >
  </el-table-column>


  <el-table-column type="index" width="50"></el-table-column>
  <el-table-column v-for="(item,key,val, index) in tableData[0]" :key="index">
    <template slot="header">{{key}}</template>
    <template slot-scope="scope">{{tableData[scope.$index][key]}}</template>
  </el-table-column>
</el-table>
  </div>


  
</template>

<script>
 export default {
    created () {
      this.selectData()
    },
    data () {
        return {
          data1:[],
          tableColumns: [
    { key: 'orgName', name: '办案单位', width: 250 },
    { key: 'caseCnt', name: '案件数（件）', minWidth: 120 },
    { key: 'remarkCnt', name: '批注（个）', minWidth: 120 },
    { key: 'evidenceAuditCnt', name: '添加审查结论（次）', minWidth: 150 },
        { key: 'orgName1', name: '办案单位', width: 250 },
    { key: 'caseCnt1', name: '案件数（件）', minWidth: 120 },
    { key: 'remarkCnt1', name: '批注（个）', minWidth: 120 },
    { key: 'evidenceAuditCnt1', name: '添加审查结论（次）', minWidth: 150 }
],
          searchJobType: ['ALL_SELECT','TSINPUT', '01', '02', '03', '04', '05', '07', '08', '09', '11', '12'],
          oldSearchJobType: [],
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
      
    }
}}
</script>
