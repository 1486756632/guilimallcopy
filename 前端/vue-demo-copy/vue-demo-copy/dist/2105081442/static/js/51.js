webpackJsonp([51,97],{"+mjr":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:a("Pg0H").default},activated:function(){this.getDataList()},methods:{getDataList:function(){var e=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/coupon/homeadv/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(t){var a=t.data;a&&0===a.code?(e.dataList=a.page.list,e.totalPage=a.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},addOrUpdateHandle:function(e){var t=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){t.$refs.addOrUpdate.init(e)})},deleteHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(e?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/coupon/homeadv/delete"),method:"post",data:t.$http.adornData(a,!1)}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})})}}},l={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key,"Enter"))return null;e.getDataList()}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:e.dataForm.key,callback:function(t){e.$set(e.dataForm,"key",t)},expression:"dataForm.key"}})],1),e._v(" "),a("el-form-item",[a("el-button",{on:{click:function(t){e.getDataList()}}},[e._v("查询")]),e._v(" "),e.isAuth("coupon:homeadv:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.addOrUpdateHandle()}}},[e._v("新增")]):e._e(),e._v(" "),e.isAuth("coupon:homeadv:delete")?a("el-button",{attrs:{type:"danger",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.deleteHandle()}}},[e._v("批量删除")]):e._e()],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),e._v(" "),a("el-table-column",{attrs:{prop:"id","header-align":"center",align:"center",label:"id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"name","header-align":"center",align:"center",label:"名字"}}),e._v(" "),a("el-table-column",{attrs:{prop:"pic","header-align":"center",align:"center",label:"图片地址"}}),e._v(" "),a("el-table-column",{attrs:{prop:"startTime","header-align":"center",align:"center",label:"开始时间"}}),e._v(" "),a("el-table-column",{attrs:{prop:"endTime","header-align":"center",align:"center",label:"结束时间"}}),e._v(" "),a("el-table-column",{attrs:{prop:"status","header-align":"center",align:"center",label:"状态"}}),e._v(" "),a("el-table-column",{attrs:{prop:"clickCount","header-align":"center",align:"center",label:"点击数"}}),e._v(" "),a("el-table-column",{attrs:{prop:"url","header-align":"center",align:"center",label:"广告详情连接地址"}}),e._v(" "),a("el-table-column",{attrs:{prop:"note","header-align":"center",align:"center",label:"备注"}}),e._v(" "),a("el-table-column",{attrs:{prop:"sort","header-align":"center",align:"center",label:"排序"}}),e._v(" "),a("el-table-column",{attrs:{prop:"publisherId","header-align":"center",align:"center",label:"发布者"}}),e._v(" "),a("el-table-column",{attrs:{prop:"authId","header-align":"center",align:"center",label:"审核者"}}),e._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.addOrUpdateHandle(t.row.id)}}},[e._v("修改")]),e._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.deleteHandle(t.row.id)}}},[e._v("删除")])]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]},n=a("VU/8")(r,l,!1,null,null,null);t.default=n.exports},Pg0H:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,name:"",pic:"",startTime:"",endTime:"",status:"",clickCount:"",url:"",note:"",sort:"",publisherId:"",authId:""},dataRule:{name:[{required:!0,message:"名字不能为空",trigger:"blur"}],pic:[{required:!0,message:"图片地址不能为空",trigger:"blur"}],startTime:[{required:!0,message:"开始时间不能为空",trigger:"blur"}],endTime:[{required:!0,message:"结束时间不能为空",trigger:"blur"}],status:[{required:!0,message:"状态不能为空",trigger:"blur"}],clickCount:[{required:!0,message:"点击数不能为空",trigger:"blur"}],url:[{required:!0,message:"广告详情连接地址不能为空",trigger:"blur"}],note:[{required:!0,message:"备注不能为空",trigger:"blur"}],sort:[{required:!0,message:"排序不能为空",trigger:"blur"}],publisherId:[{required:!0,message:"发布者不能为空",trigger:"blur"}],authId:[{required:!0,message:"审核者不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/coupon/homeadv/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataForm.name=a.homeAdv.name,t.dataForm.pic=a.homeAdv.pic,t.dataForm.startTime=a.homeAdv.startTime,t.dataForm.endTime=a.homeAdv.endTime,t.dataForm.status=a.homeAdv.status,t.dataForm.clickCount=a.homeAdv.clickCount,t.dataForm.url=a.homeAdv.url,t.dataForm.note=a.homeAdv.note,t.dataForm.sort=a.homeAdv.sort,t.dataForm.publisherId=a.homeAdv.publisherId,t.dataForm.authId=a.homeAdv.authId)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/coupon/homeadv/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,name:e.dataForm.name,pic:e.dataForm.pic,startTime:e.dataForm.startTime,endTime:e.dataForm.endTime,status:e.dataForm.status,clickCount:e.dataForm.clickCount,url:e.dataForm.url,note:e.dataForm.note,sort:e.dataForm.sort,publisherId:e.dataForm.publisherId,authId:e.dataForm.authId})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},l={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"120px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key,"Enter"))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"名字",prop:"name"}},[a("el-input",{attrs:{placeholder:"名字"},model:{value:e.dataForm.name,callback:function(t){e.$set(e.dataForm,"name",t)},expression:"dataForm.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"图片地址",prop:"pic"}},[a("el-input",{attrs:{placeholder:"图片地址"},model:{value:e.dataForm.pic,callback:function(t){e.$set(e.dataForm,"pic",t)},expression:"dataForm.pic"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"开始时间",prop:"startTime"}},[a("el-input",{attrs:{placeholder:"开始时间"},model:{value:e.dataForm.startTime,callback:function(t){e.$set(e.dataForm,"startTime",t)},expression:"dataForm.startTime"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"结束时间",prop:"endTime"}},[a("el-input",{attrs:{placeholder:"结束时间"},model:{value:e.dataForm.endTime,callback:function(t){e.$set(e.dataForm,"endTime",t)},expression:"dataForm.endTime"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"状态",prop:"status"}},[a("el-input",{attrs:{placeholder:"状态"},model:{value:e.dataForm.status,callback:function(t){e.$set(e.dataForm,"status",t)},expression:"dataForm.status"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"点击数",prop:"clickCount"}},[a("el-input",{attrs:{placeholder:"点击数"},model:{value:e.dataForm.clickCount,callback:function(t){e.$set(e.dataForm,"clickCount",t)},expression:"dataForm.clickCount"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"广告详情连接地址",prop:"url"}},[a("el-input",{attrs:{placeholder:"广告详情连接地址"},model:{value:e.dataForm.url,callback:function(t){e.$set(e.dataForm,"url",t)},expression:"dataForm.url"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"备注",prop:"note"}},[a("el-input",{attrs:{placeholder:"备注"},model:{value:e.dataForm.note,callback:function(t){e.$set(e.dataForm,"note",t)},expression:"dataForm.note"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"排序",prop:"sort"}},[a("el-input",{attrs:{placeholder:"排序"},model:{value:e.dataForm.sort,callback:function(t){e.$set(e.dataForm,"sort",t)},expression:"dataForm.sort"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"发布者",prop:"publisherId"}},[a("el-input",{attrs:{placeholder:"发布者"},model:{value:e.dataForm.publisherId,callback:function(t){e.$set(e.dataForm,"publisherId",t)},expression:"dataForm.publisherId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"审核者",prop:"authId"}},[a("el-input",{attrs:{placeholder:"审核者"},model:{value:e.dataForm.authId,callback:function(t){e.$set(e.dataForm,"authId",t)},expression:"dataForm.authId"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},n=a("VU/8")(r,l,!1,null,null,null);t.default=n.exports}});