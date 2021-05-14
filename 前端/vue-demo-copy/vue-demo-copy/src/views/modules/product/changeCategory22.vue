<template>
<el-table
  v-loading="loading"
  :data="orgTableData"
  :header-cell-style="{'background':'#f5f7fa','color':'#666'}"
  stripe
  border
>
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
</el-table>
</template>

<script>
    export default {
        name:'ruleTable',
        data() {
            return {
                // 表头
                
tableColumns: [
    { key: 'orgName', name: '办案单位', width: 250 },
    { key: 'caseCnt', name: '案件数（件）', minWidth: 120 },
    { key: 'remarkCnt', name: '批注（个）', minWidth: 120 },
    { key: 'evidenceAuditCnt', name: '添加审查结论（次）', minWidth: 150 },
        { key: 'orgName1', name: '办案单位', width: 250 },
    { key: 'caseCnt1', name: '案件数（件）', minWidth: 120 },
    { key: 'remarkCnt1', name: '批注（个）', minWidth: 120 },
    { key: 'evidenceAuditCnt1', name: '添加审查结论（次）', minWidth: 150 }
]
            }
        },
        methods: {
            handleEdit(index, row) {
                console.log(index, row);
            },
            handleDelete(index, row) {
                console.log(index, row);
            },
            change(){
                this.tableHead = example2;
            }
        }
    }
</script>