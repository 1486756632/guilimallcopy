webpackJsonp([2,18,72],{"6SSK":function(t,n,e){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var a=e("8i7I"),r=e("Ui6l"),i=e("iINV"),o={components:{Category:a.default,AddOrUpdate:r.default,RelationUpdate:i.default},props:{},data:function(){return{catId:0,dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,relationVisible:!1}},activated:function(){this.getDataList()},methods:{relationHandle:function(t){var n=this;this.relationVisible=!0,this.$nextTick(function(){n.$refs.relationUpdate.init(t)})},treenodeclick:function(t,n,e){3==n.level&&(this.catId=t.catId,this.getDataList())},getAllDataList:function(){this.catId=0,this.getDataList()},getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/product/attrgroup/list/"+this.catId),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(n){var e=n.data;e&&0===e.code?(t.dataList=e.page.list,t.totalPage=e.page.totalCount):(t.dataList=[],t.totalPage=0),console.log("222222",t.dataList,t.totalPage,t.dataForm.key),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var n=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){n.$refs.addOrUpdate.init(t)})},deleteHandle:function(t){var n=this,e=t?[t]:this.dataListSelections.map(function(t){return t.attrGroupId});this.$confirm("确定对[id="+e.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){n.$http({url:n.$http.adornUrl("/product/attrgroup/delete"),method:"post",data:n.$http.adornData(e,!1)}).then(function(t){var e=t.data;e&&0===e.code?n.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){n.getDataList()}}):n.$message.error(e.msg)})})}}},l={render:function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("el-row",{attrs:{gutter:20}},[e("el-col",{attrs:{span:6}},[e("category",{on:{"tree-node-click":t.treenodeclick}})],1),t._v(" "),e("el-col",{attrs:{span:18}},[e("div",{staticClass:"mod-config"},[e("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(n){if(!("button"in n)&&t._k(n.keyCode,"enter",13,n.key,"Enter"))return null;t.getDataList()}}},[e("el-form-item",[e("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:t.dataForm.key,callback:function(n){t.$set(t.dataForm,"key",n)},expression:"dataForm.key"}})],1),t._v(" "),e("el-form-item",[e("el-button",{on:{click:function(n){t.getDataList()}}},[t._v("查询")]),t._v(" "),e("el-button",{attrs:{type:"success"},on:{click:function(n){t.getAllDataList()}}},[t._v("查询全部")]),t._v(" "),t.isAuth("product:attrgroup:save")?e("el-button",{attrs:{type:"primary"},on:{click:function(n){t.addOrUpdateHandle()}}},[t._v("新增")]):t._e(),t._v(" "),t.isAuth("product:attrgroup:delete")?e("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(n){t.deleteHandle()}}},[t._v("批量删除")]):t._e()],1)],1),t._v(" "),e("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[e("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),e("el-table-column",{attrs:{prop:"attrGroupId","header-align":"center",align:"center",label:"分组id"}}),t._v(" "),e("el-table-column",{attrs:{prop:"attrGroupName","header-align":"center",align:"center",label:"组名"}}),t._v(" "),e("el-table-column",{attrs:{prop:"sort","header-align":"center",align:"center",label:"排序"}}),t._v(" "),e("el-table-column",{attrs:{prop:"descript","header-align":"center",align:"center",label:"描述"}}),t._v(" "),e("el-table-column",{attrs:{prop:"icon","header-align":"center",align:"center",label:"组图标"}}),t._v(" "),e("el-table-column",{attrs:{prop:"catelogId","header-align":"center",align:"center",label:"所属分类id"}}),t._v(" "),e("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(n){return[e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.relationHandle(n.row.attrGroupId)}}},[t._v("关联")]),t._v(" "),e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.addOrUpdateHandle(n.row.attrGroupId)}}},[t._v("修改")]),t._v(" "),e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.deleteHandle(n.row.attrGroupId)}}},[t._v("删除")])]}}])})],1),t._v(" "),e("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?e("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.relationVisible?e("relation-update",{ref:"relationUpdate",on:{refreshData:t.getDataList}}):t._e()],1)])],1)},staticRenderFns:[]};var s=e("VU/8")(o,l,!1,function(t){e("pKej")},"data-v-5dcc0a36",null);n.default=s.exports},Ui6l:function(t,n,e){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var a={data:function(){return{props:{value:"catId",label:"name",children:"children"},visible:!1,categorys:[],catelogPath:[],dataForm:{attrGroupId:0,attrGroupName:"",sort:"",descript:"",icon:"",catelogId:0},dataRule:{attrGroupName:[{required:!0,message:"组名不能为空",trigger:"blur"}],sort:[{required:!0,message:"排序不能为空",trigger:"blur"}],descript:[{required:!0,message:"描述不能为空",trigger:"blur"}],icon:[{required:!0,message:"组图标不能为空",trigger:"blur"}],catelogId:[{required:!0,message:"所属分类id不能为空",trigger:"blur"}]}}},components:{CategoryCascader:e("2uKH").default},methods:{dialogClose:function(){this.catelogPath=[]},getCategorys:function(){var t=this;this.$http({url:this.$http.adornUrl("/product/category/list/tree"),method:"get"}).then(function(n){var e=n.data;t.categorys=e.data})},init:function(t){var n=this;this.dataForm.attrGroupId=t||0,this.visible=!0,this.$nextTick(function(){n.$refs.dataForm.resetFields(),n.dataForm.attrGroupId&&n.$http({url:n.$http.adornUrl("/product/attrgroup/info/"+n.dataForm.attrGroupId),method:"get",params:n.$http.adornParams()}).then(function(t){var e=t.data;e&&0===e.code&&(n.dataForm.attrGroupName=e.attrGroup.attrGroupName,n.dataForm.sort=e.attrGroup.sort,n.dataForm.descript=e.attrGroup.descript,n.dataForm.icon=e.attrGroup.icon,n.dataForm.catelogId=e.attrGroup.catelogId,n.catelogPath=e.attrGroup.catelogPath)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(n){n&&t.$http({url:t.$http.adornUrl("/product/attrgroup/"+(t.dataForm.attrGroupId?"update":"save")),method:"post",data:t.$http.adornData({attrGroupId:t.dataForm.attrGroupId||void 0,attrGroupName:t.dataForm.attrGroupName,sort:t.dataForm.sort,descript:t.dataForm.descript,icon:t.dataForm.icon,catelogId:t.catelogPath[t.catelogPath.length-1]})}).then(function(n){var e=n.data;e&&0===e.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(e.msg)})})}},created:function(){this.getCategorys()}},r={render:function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(n){t.visible=n},closed:t.dialogClose}},[e("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"120px"},nativeOn:{keyup:function(n){if(!("button"in n)&&t._k(n.keyCode,"enter",13,n.key,"Enter"))return null;t.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"组名",prop:"attrGroupName"}},[e("el-input",{attrs:{placeholder:"组名"},model:{value:t.dataForm.attrGroupName,callback:function(n){t.$set(t.dataForm,"attrGroupName",n)},expression:"dataForm.attrGroupName"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"排序",prop:"sort"}},[e("el-input",{attrs:{placeholder:"排序"},model:{value:t.dataForm.sort,callback:function(n){t.$set(t.dataForm,"sort",n)},expression:"dataForm.sort"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"描述",prop:"descript"}},[e("el-input",{attrs:{placeholder:"描述"},model:{value:t.dataForm.descript,callback:function(n){t.$set(t.dataForm,"descript",n)},expression:"dataForm.descript"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"组图标",prop:"icon"}},[e("el-input",{attrs:{placeholder:"组图标"},model:{value:t.dataForm.icon,callback:function(n){t.$set(t.dataForm,"icon",n)},expression:"dataForm.icon"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"所属分类",prop:"catelogId"}},[e("category-cascader",{attrs:{catelogPath:t.catelogPath},on:{"update:catelogPath":function(n){t.catelogPath=n}}})],1)],1),t._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(n){t.visible=!1}}},[t._v("取消")]),t._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(n){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},i=e("VU/8")(a,r,!1,null,null,null);n.default=i.exports},Z4pE:function(t,n,e){(t.exports=e("FZ+f")(!1)).push([t.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",""])},iINV:function(t,n,e){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var a={components:{},props:{},data:function(){return{attrGroupId:0,visible:!1,innerVisible:!1,relationAttrs:[],dataListSelections:[],dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,innerdataListSelections:[]}},computed:{},watch:{},methods:{selectionChangeHandle:function(t){this.dataListSelections=t},innerSelectionChangeHandle:function(t){this.innerdataListSelections=t},addRelation:function(){this.getDataList(),this.innerVisible=!0},batchDeleteRelation:function(t){var n=this,e=[];this.dataListSelections.forEach(function(t){e.push({attrId:t.attrId,attrGroupId:n.attrGroupId})}),this.$http({url:this.$http.adornUrl("/product/attrgroup/attr/relation/delete"),method:"post",data:this.$http.adornData(e,!1)}).then(function(t){var e=t.data;0==e.code?(n.$message({type:"success",message:"删除成功"}),n.init(n.attrGroupId)):n.$message({type:"error",message:e.msg})})},relationRemove:function(t){var n=this,e=[];e.push({attrId:t,attrGroupId:this.attrGroupId}),this.$http({url:this.$http.adornUrl("/product/attrgroup/attr/relation/delete"),method:"post",data:this.$http.adornData(e,!1)}).then(function(t){var e=t.data;0==e.code?(n.$message({type:"success",message:"删除成功"}),n.init(n.attrGroupId)):n.$message({type:"error",message:e.msg})})},submitAddRealtion:function(){var t=this;if(this.innerVisible=!1,console.log("准备新增的数据",this.innerdataListSelections),this.innerdataListSelections.length>0){var n=[];this.innerdataListSelections.forEach(function(e){n.push({attrId:e.attrId,attrGroupId:t.attrGroupId})}),this.$http({url:this.$http.adornUrl("/product/attrgroup/attr/relation"),method:"post",data:this.$http.adornData(n,!1)}).then(function(n){0==n.data.code&&t.$message({type:"success",message:"新增关联成功"}),t.$emit("refreshData"),t.init(t.attrGroupId)})}},init:function(t){var n=this;this.attrGroupId=t||0,this.visible=!0,this.$http({url:this.$http.adornUrl("/product/attrgroup/"+this.attrGroupId+"/attr/relation"),method:"get",params:this.$http.adornParams({})}).then(function(t){var e=t.data;n.relationAttrs=e.data})},dialogClose:function(){},getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/product/attrgroup/"+this.attrGroupId+"/noattr/relation"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(n){var e=n.data;e&&0===e.code?(t.dataList=e.page.list,t.totalPage=e.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()}}},r={render:function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("div",[e("el-dialog",{attrs:{"close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(n){t.visible=n},closed:t.dialogClose}},[e("el-dialog",{attrs:{width:"40%",title:"选择属性",visible:t.innerVisible,"append-to-body":""},on:{"update:visible":function(n){t.innerVisible=n}}},[e("div",[e("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(n){if(!("button"in n)&&t._k(n.keyCode,"enter",13,n.key,"Enter"))return null;t.getDataList()}}},[e("el-form-item",[e("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:t.dataForm.key,callback:function(n){t.$set(t.dataForm,"key",n)},expression:"dataForm.key"}})],1),t._v(" "),e("el-form-item",[e("el-button",{on:{click:function(n){t.getDataList()}}},[t._v("查询")])],1)],1),t._v(" "),e("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.innerSelectionChangeHandle}},[e("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center"}}),t._v(" "),e("el-table-column",{attrs:{prop:"attrId","header-align":"center",align:"center",label:"属性id"}}),t._v(" "),e("el-table-column",{attrs:{prop:"attrName","header-align":"center",align:"center",label:"属性名"}}),t._v(" "),e("el-table-column",{attrs:{prop:"icon","header-align":"center",align:"center",label:"属性图标"}}),t._v(" "),e("el-table-column",{attrs:{prop:"valueSelect","header-align":"center",align:"center",label:"可选值列表"}})],1),t._v(" "),e("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}})],1),t._v(" "),e("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(n){t.innerVisible=!1}}},[t._v("取 消")]),t._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:t.submitAddRealtion}},[t._v("确认新增")])],1)]),t._v(" "),e("el-row",[e("el-col",{attrs:{span:24}},[e("el-button",{attrs:{type:"primary"},on:{click:t.addRelation}},[t._v("新建关联")]),t._v(" "),e("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:t.batchDeleteRelation}},[t._v("批量删除")]),t._v(" "),e("el-table",{staticStyle:{width:"100%"},attrs:{data:t.relationAttrs,border:""},on:{"selection-change":t.selectionChangeHandle}},[e("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),e("el-table-column",{attrs:{prop:"attrId",label:"#"}}),t._v(" "),e("el-table-column",{attrs:{prop:"attrName",label:"属性名"}}),t._v(" "),e("el-table-column",{attrs:{prop:"valueSelect",label:"可选值"},scopedSlots:t._u([{key:"default",fn:function(n){return[e("el-tooltip",{attrs:{placement:"top"}},[e("div",{attrs:{slot:"content"},slot:"content"},t._l(n.row.valueSelect.split(";"),function(n,a){return e("span",{key:a},[t._v("\n                    "+t._s(n)+"\n                    "),e("br")])})),t._v(" "),e("el-tag",[t._v(t._s(n.row.valueSelect.split(";")[0]+" ..."))])],1)]}}])}),t._v(" "),e("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(n){return[e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.relationRemove(n.row.attrId)}}},[t._v("移除")])]}}])})],1)],1)],1)],1)],1)},staticRenderFns:[]};var i=e("VU/8")(a,r,!1,function(t){e("wzli")},"data-v-5cb55c46",null);n.default=i.exports},"jK+0":function(t,n,e){(t.exports=e("FZ+f")(!1)).push([t.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",""])},pKej:function(t,n,e){var a=e("jK+0");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);e("rjj0")("45154444",a,!0)},wzli:function(t,n,e){var a=e("Z4pE");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);e("rjj0")("dfd3c5d8",a,!0)}});