webpackJsonp([58],{"7zAI":function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{visible:!1,wareList:[],dataForm:{id:0,skuId:"",wareId:"",stock:0,skuName:"",stockLocked:0},dataRule:{skuId:[{required:!0,message:"sku_id不能为空",trigger:"blur"}],wareId:[{required:!0,message:"仓库id不能为空",trigger:"blur"}],stock:[{required:!0,message:"库存数不能为空",trigger:"blur"}],skuName:[{required:!0,message:"sku_name不能为空",trigger:"blur"}]}}},created:function(){this.getWares()},methods:{getWares:function(){var t=this;this.$http({url:this.$http.adornUrl("/ware/wareinfo/list"),method:"get",params:this.$http.adornParams({page:1,limit:500})}).then(function(a){var e=a.data;t.wareList=e.page.list})},init:function(t){var a=this;this.dataForm.id=t||0,this.visible=!0,this.$nextTick(function(){a.$refs.dataForm.resetFields(),a.dataForm.id&&a.$http({url:a.$http.adornUrl("/ware/waresku/info/"+a.dataForm.id),method:"get",params:a.$http.adornParams()}).then(function(t){var e=t.data;e&&0===e.code&&(a.dataForm.skuId=e.wareSku.skuId,a.dataForm.wareId=e.wareSku.wareId,a.dataForm.stock=e.wareSku.stock,a.dataForm.skuName=e.wareSku.skuName,a.dataForm.stockLocked=e.wareSku.stockLocked)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(a){a&&t.$http({url:t.$http.adornUrl("/ware/waresku/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,skuId:t.dataForm.skuId,wareId:t.dataForm.wareId,stock:t.dataForm.stock,skuName:t.dataForm.skuName,stockLocked:t.dataForm.stockLocked})}).then(function(a){var e=a.data;e&&0===e.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(e.msg)})})}}},o={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(a){t.visible=a}}},[e("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"120px"},nativeOn:{keyup:function(a){if(!("button"in a)&&t._k(a.keyCode,"enter",13,a.key,"Enter"))return null;t.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"sku_id",prop:"skuId"}},[e("el-input",{attrs:{placeholder:"sku_id"},model:{value:t.dataForm.skuId,callback:function(a){t.$set(t.dataForm,"skuId",a)},expression:"dataForm.skuId"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"仓库",prop:"wareId"}},[e("el-select",{attrs:{placeholder:"请选择仓库",clearable:""},model:{value:t.dataForm.wareId,callback:function(a){t.$set(t.dataForm,"wareId",a)},expression:"dataForm.wareId"}},t._l(t.wareList,function(t){return e("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"库存数",prop:"stock"}},[e("el-input",{attrs:{placeholder:"库存数"},model:{value:t.dataForm.stock,callback:function(a){t.$set(t.dataForm,"stock",a)},expression:"dataForm.stock"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"sku_name",prop:"skuName"}},[e("el-input",{attrs:{placeholder:"sku_name"},model:{value:t.dataForm.skuName,callback:function(a){t.$set(t.dataForm,"skuName",a)},expression:"dataForm.skuName"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"锁定库存",prop:"stockLocked"}},[e("el-input",{attrs:{placeholder:"锁定库存"},model:{value:t.dataForm.stockLocked,callback:function(a){t.$set(t.dataForm,"stockLocked",a)},expression:"dataForm.stockLocked"}})],1)],1),t._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(a){t.visible=!1}}},[t._v("取消")]),t._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(a){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},s=e("VU/8")(r,o,!1,null,null,null);a.default=s.exports}});