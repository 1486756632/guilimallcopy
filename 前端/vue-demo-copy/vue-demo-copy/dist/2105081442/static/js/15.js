webpackJsonp([15],{ShSq:function(n,t,e){(n.exports=e("FZ+f")(!1)).push([n.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",""])},k4HQ:function(n,t,e){var a=e("ShSq");"string"==typeof a&&(a=[[n.i,a,""]]),a.locals&&(n.exports=a.locals);e("rjj0")("770554fb",a,!0)},sd0f:function(n,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={components:{},props:{},data:function(){return{pCid:[],draggable:!1,updateNodes:[],maxLevel:0,title:"",dialogType:"",category:{name:"",parentCid:0,catLevel:0,showStatus:1,sort:0,productUnit:"",icon:"",catId:null},dialogVisible:!1,menus:[],expandedKey:[],defaultProps:{children:"children",label:"name"}}},computed:{},watch:{},methods:{getMenus:function(){var n=this;this.$http({url:this.$http.adornUrl("/product/category/list/tree"),method:"get"}).then(function(t){var e=t.data;console.log("成功获取到菜单数据...",e.data),n.menus=e.data})},batchDelete:function(){var n=this,t=[],e=this.$refs.menuTree.getCheckedNodes();console.log("被选中的元素",e);for(var a=0;a<e.length;a++)t.push(e[a].catId);this.$confirm("是否批量删除【"+t+"】菜单?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){n.$http({url:n.$http.adornUrl("/product/category/delete"),method:"post",data:n.$http.adornData(t,!1)}).then(function(t){t.data;n.$message({message:"菜单批量删除成功",type:"success"}),n.getMenus()})}).catch(function(){})},batchSave:function(){var n=this;this.$http({url:this.$http.adornUrl("/product/category/update/sort"),method:"post",data:this.$http.adornData(this.updateNodes,!1)}).then(function(t){t.data;n.$message({message:"菜单顺序等修改成功",type:"success"}),n.getMenus(),n.expandedKey=n.pCid,n.updateNodes=[],n.maxLevel=0})},handleDrop:function(n,t,e,a){console.log("handleDrop: ",n,t,e);var o=0,d=null;"before"==e||"after"==e?(o=void 0==t.parent.data.catId?0:t.parent.data.catId,d=t.parent.childNodes):(o=t.data.catId,d=t.childNodes),this.pCid.push(o);for(var i=0;i<d.length;i++)if(d[i].data.catId==n.data.catId){var s=n.level;d[i].level!=n.level&&(s=d[i].level,this.updateChildNodeLevel(d[i])),this.updateNodes.push({catId:d[i].data.catId,sort:i,parentCid:o,catLevel:s})}else this.updateNodes.push({catId:d[i].data.catId,sort:i});console.log("updateNodes",this.updateNodes)},updateChildNodeLevel:function(n){if(n.childNodes.length>0)for(var t=0;t<n.childNodes.length;t++){var e=n.childNodes[t].data;this.updateNodes.push({catId:e.catId,catLevel:n.childNodes[t].level}),this.updateChildNodeLevel(n.childNodes[t])}},allowDrop:function(n,t,e){console.log("allowDrop:",n,t,e),this.countNodeLevel(n);var a=Math.abs(this.maxLevel-n.level)+1;return console.log("深度：",a),"inner"==e?a+t.level<=3:a+t.parent.level<=3},countNodeLevel:function(n){if(null!=n.childNodes&&n.childNodes.length>0)for(var t=0;t<n.childNodes.length;t++)n.childNodes[t].level>this.maxLevel&&(this.maxLevel=n.childNodes[t].level),this.countNodeLevel(n.childNodes[t])},edit:function(n){var t=this;console.log("要修改的数据",n),this.dialogType="edit",this.title="修改分类",this.dialogVisible=!0,this.$http({url:this.$http.adornUrl("/product/category/info/"+n.catId),method:"get"}).then(function(n){var e=n.data;console.log("要回显的数据",e),t.category.name=e.data.name,t.category.catId=e.data.catId,t.category.icon=e.data.icon,t.category.productUnit=e.data.productUnit,t.category.parentCid=e.data.parentCid,t.category.catLevel=e.data.catLevel,t.category.sort=e.data.sort,t.category.showStatus=e.data.showStatus})},append:function(n){console.log("append",n),this.dialogType="add",this.title="添加分类",this.dialogVisible=!0,this.category.parentCid=n.catId,this.category.catLevel=1*n.catLevel+1,this.category.catId=null,this.category.name="",this.category.icon="",this.category.productUnit="",this.category.sort=0,this.category.showStatus=1},submitData:function(){"add"==this.dialogType&&this.addCategory(),"edit"==this.dialogType&&this.editCategory()},editCategory:function(){var n=this,t=this.category,e=t.catId,a=t.name,o=t.icon,d=t.productUnit;this.$http({url:this.$http.adornUrl("/product/category/update"),method:"post",data:this.$http.adornData({catId:e,name:a,icon:o,productUnit:d},!1)}).then(function(t){t.data;n.$message({message:"菜单修改成功",type:"success"}),n.dialogVisible=!1,n.getMenus(),n.expandedKey=[n.category.parentCid]})},addCategory:function(){var n=this;console.log("提交的三级分类数据",this.category),this.$http({url:this.$http.adornUrl("/product/category/save"),method:"post",data:this.$http.adornData(this.category,!1)}).then(function(t){t.data;n.$message({message:"菜单保存成功",type:"success"}),n.dialogVisible=!1,n.getMenus(),n.expandedKey=[n.category.parentCid]})},remove:function(n,t){var e=this,a=[t.catId];this.$confirm("是否删除【"+t.name+"】菜单?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/product/category/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){t.data;e.$message({message:"菜单删除成功",type:"success"}),e.getMenus(),e.expandedKey=[n.parent.data.catId]})}).catch(function(){}),console.log("remove",n,t)}},created:function(){this.getMenus()},mounted:function(){},beforeCreate:function(){},beforeMount:function(){},beforeUpdate:function(){},updated:function(){},beforeDestroy:function(){},destroyed:function(){},activated:function(){}},o={render:function(){var n=this,t=n.$createElement,e=n._self._c||t;return e("div",[e("el-switch",{attrs:{"active-text":"开启拖拽","inactive-text":"关闭拖拽"},model:{value:n.draggable,callback:function(t){n.draggable=t},expression:"draggable"}}),n._v(" "),n.draggable?e("el-button",{on:{click:n.batchSave}},[n._v("批量保存")]):n._e(),n._v(" "),e("el-button",{attrs:{type:"danger"},on:{click:n.batchDelete}},[n._v("批量删除")]),n._v(" "),e("el-tree",{ref:"menuTree",attrs:{data:n.menus,props:n.defaultProps,"expand-on-click-node":!1,"show-checkbox":"","node-key":"catId","default-expanded-keys":n.expandedKey,draggable:n.draggable,"allow-drop":n.allowDrop},on:{"node-drop":n.handleDrop},scopedSlots:n._u([{key:"default",fn:function(t){var a=t.node,o=t.data;return e("span",{staticClass:"custom-tree-node"},[e("span",[n._v(n._s(a.label))]),n._v(" "),e("span",[a.level<=2?e("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(){return n.append(o)}}},[n._v("Append")]):n._e(),n._v(" "),e("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(t){n.edit(o)}}},[n._v("edit")]),n._v(" "),0==a.childNodes.length?e("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(){return n.remove(a,o)}}},[n._v("Delete")]):n._e()],1)])}}])}),n._v(" "),e("el-dialog",{attrs:{title:n.title,visible:n.dialogVisible,width:"30%","close-on-click-modal":!1},on:{"update:visible":function(t){n.dialogVisible=t}}},[e("el-form",{attrs:{model:n.category}},[e("el-form-item",{attrs:{label:"分类名称"}},[e("el-input",{attrs:{autocomplete:"off"},model:{value:n.category.name,callback:function(t){n.$set(n.category,"name",t)},expression:"category.name"}})],1),n._v(" "),e("el-form-item",{attrs:{label:"图标"}},[e("el-input",{attrs:{autocomplete:"off"},model:{value:n.category.icon,callback:function(t){n.$set(n.category,"icon",t)},expression:"category.icon"}})],1),n._v(" "),e("el-form-item",{attrs:{label:"计量单位"}},[e("el-input",{attrs:{autocomplete:"off"},model:{value:n.category.productUnit,callback:function(t){n.$set(n.category,"productUnit",t)},expression:"category.productUnit"}})],1)],1),n._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(t){n.dialogVisible=!1}}},[n._v("取 消")]),n._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:n.submitData}},[n._v("确 定")])],1)],1)],1)},staticRenderFns:[]};var d=e("VU/8")(a,o,!1,function(n){e("k4HQ")},"data-v-401746d2",null);t.default=d.exports}});