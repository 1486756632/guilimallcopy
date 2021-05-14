webpackJsonp([84],{l3Go:function(e,a,r){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var t={data:function(){return{visible:!1,dataForm:{id:0,levelId:"",username:"",password:"",nickname:"",mobile:"",email:"",header:"",gender:"",birth:"",city:"",job:"",sign:"",sourceType:"",integration:"",growth:"",status:"",createTime:""},dataRule:{levelId:[{required:!0,message:"会员等级id不能为空",trigger:"blur"}],username:[{required:!0,message:"用户名不能为空",trigger:"blur"}],password:[{required:!0,message:"密码不能为空",trigger:"blur"}],nickname:[{required:!0,message:"昵称不能为空",trigger:"blur"}],mobile:[{required:!0,message:"手机号码不能为空",trigger:"blur"}],email:[{required:!0,message:"邮箱不能为空",trigger:"blur"}],header:[{required:!0,message:"头像不能为空",trigger:"blur"}],gender:[{required:!0,message:"性别不能为空",trigger:"blur"}],birth:[{required:!0,message:"生日不能为空",trigger:"blur"}],city:[{required:!0,message:"所在城市不能为空",trigger:"blur"}],job:[{required:!0,message:"职业不能为空",trigger:"blur"}],sign:[{required:!0,message:"个性签名不能为空",trigger:"blur"}],sourceType:[{required:!0,message:"用户来源不能为空",trigger:"blur"}],integration:[{required:!0,message:"积分不能为空",trigger:"blur"}],growth:[{required:!0,message:"成长值不能为空",trigger:"blur"}],status:[{required:!0,message:"启用状态不能为空",trigger:"blur"}],createTime:[{required:!0,message:"注册时间不能为空",trigger:"blur"}]}}},methods:{init:function(e){var a=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){a.$refs.dataForm.resetFields(),a.dataForm.id&&a.$http({url:a.$http.adornUrl("/member/member/info/"+a.dataForm.id),method:"get",params:a.$http.adornParams()}).then(function(e){var r=e.data;r&&0===r.code&&(a.dataForm.levelId=r.member.levelId,a.dataForm.username=r.member.username,a.dataForm.password=r.member.password,a.dataForm.nickname=r.member.nickname,a.dataForm.mobile=r.member.mobile,a.dataForm.email=r.member.email,a.dataForm.header=r.member.header,a.dataForm.gender=r.member.gender,a.dataForm.birth=r.member.birth,a.dataForm.city=r.member.city,a.dataForm.job=r.member.job,a.dataForm.sign=r.member.sign,a.dataForm.sourceType=r.member.sourceType,a.dataForm.integration=r.member.integration,a.dataForm.growth=r.member.growth,a.dataForm.status=r.member.status,a.dataForm.createTime=r.member.createTime)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(a){a&&e.$http({url:e.$http.adornUrl("/member/member/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,levelId:e.dataForm.levelId,username:e.dataForm.username,password:e.dataForm.password,nickname:e.dataForm.nickname,mobile:e.dataForm.mobile,email:e.dataForm.email,header:e.dataForm.header,gender:e.dataForm.gender,birth:e.dataForm.birth,city:e.dataForm.city,job:e.dataForm.job,sign:e.dataForm.sign,sourceType:e.dataForm.sourceType,integration:e.dataForm.integration,growth:e.dataForm.growth,status:e.dataForm.status,createTime:e.dataForm.createTime})}).then(function(a){var r=a.data;r&&0===r.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(r.msg)})})}}},o={render:function(){var e=this,a=e.$createElement,r=e._self._c||a;return r("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(a){e.visible=a}}},[r("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"120px"},nativeOn:{keyup:function(a){if(!("button"in a)&&e._k(a.keyCode,"enter",13,a.key,"Enter"))return null;e.dataFormSubmit()}}},[r("el-form-item",{attrs:{label:"会员等级id",prop:"levelId"}},[r("el-input",{attrs:{placeholder:"会员等级id"},model:{value:e.dataForm.levelId,callback:function(a){e.$set(e.dataForm,"levelId",a)},expression:"dataForm.levelId"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"用户名",prop:"username"}},[r("el-input",{attrs:{placeholder:"用户名"},model:{value:e.dataForm.username,callback:function(a){e.$set(e.dataForm,"username",a)},expression:"dataForm.username"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"密码",prop:"password"}},[r("el-input",{attrs:{placeholder:"密码"},model:{value:e.dataForm.password,callback:function(a){e.$set(e.dataForm,"password",a)},expression:"dataForm.password"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"昵称",prop:"nickname"}},[r("el-input",{attrs:{placeholder:"昵称"},model:{value:e.dataForm.nickname,callback:function(a){e.$set(e.dataForm,"nickname",a)},expression:"dataForm.nickname"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"手机号码",prop:"mobile"}},[r("el-input",{attrs:{placeholder:"手机号码"},model:{value:e.dataForm.mobile,callback:function(a){e.$set(e.dataForm,"mobile",a)},expression:"dataForm.mobile"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"邮箱",prop:"email"}},[r("el-input",{attrs:{placeholder:"邮箱"},model:{value:e.dataForm.email,callback:function(a){e.$set(e.dataForm,"email",a)},expression:"dataForm.email"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"头像",prop:"header"}},[r("el-input",{attrs:{placeholder:"头像"},model:{value:e.dataForm.header,callback:function(a){e.$set(e.dataForm,"header",a)},expression:"dataForm.header"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"性别",prop:"gender"}},[r("el-input",{attrs:{placeholder:"性别"},model:{value:e.dataForm.gender,callback:function(a){e.$set(e.dataForm,"gender",a)},expression:"dataForm.gender"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"生日",prop:"birth"}},[r("el-input",{attrs:{placeholder:"生日"},model:{value:e.dataForm.birth,callback:function(a){e.$set(e.dataForm,"birth",a)},expression:"dataForm.birth"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"所在城市",prop:"city"}},[r("el-input",{attrs:{placeholder:"所在城市"},model:{value:e.dataForm.city,callback:function(a){e.$set(e.dataForm,"city",a)},expression:"dataForm.city"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"职业",prop:"job"}},[r("el-input",{attrs:{placeholder:"职业"},model:{value:e.dataForm.job,callback:function(a){e.$set(e.dataForm,"job",a)},expression:"dataForm.job"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"个性签名",prop:"sign"}},[r("el-input",{attrs:{placeholder:"个性签名"},model:{value:e.dataForm.sign,callback:function(a){e.$set(e.dataForm,"sign",a)},expression:"dataForm.sign"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"用户来源",prop:"sourceType"}},[r("el-input",{attrs:{placeholder:"用户来源"},model:{value:e.dataForm.sourceType,callback:function(a){e.$set(e.dataForm,"sourceType",a)},expression:"dataForm.sourceType"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"积分",prop:"integration"}},[r("el-input",{attrs:{placeholder:"积分"},model:{value:e.dataForm.integration,callback:function(a){e.$set(e.dataForm,"integration",a)},expression:"dataForm.integration"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"成长值",prop:"growth"}},[r("el-input",{attrs:{placeholder:"成长值"},model:{value:e.dataForm.growth,callback:function(a){e.$set(e.dataForm,"growth",a)},expression:"dataForm.growth"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"启用状态",prop:"status"}},[r("el-input",{attrs:{placeholder:"启用状态"},model:{value:e.dataForm.status,callback:function(a){e.$set(e.dataForm,"status",a)},expression:"dataForm.status"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"注册时间",prop:"createTime"}},[r("el-input",{attrs:{placeholder:"注册时间"},model:{value:e.dataForm.createTime,callback:function(a){e.$set(e.dataForm,"createTime",a)},expression:"dataForm.createTime"}})],1)],1),e._v(" "),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(a){e.visible=!1}}},[e._v("取消")]),e._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(a){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},l=r("VU/8")(t,o,!1,null,null,null);a.default=l.exports}});