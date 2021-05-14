package com.atguigu.gulimall.product.ItemReadAndWrite;
/**
 * Item配置项pojo
 *
 */
public class ItemInfo {
        //参数名
        private String name ;
        //参数地址
        private String address;
        //参数类型
        private String dataType;
        //参数长度
        private Integer typeLength;
        //读写权限
        private String authority;
        public String getName() {
                return name;
        }
        public void setName(String name) {
                this.name = name;
        }
        public String getAddress() {
                return address;
        }
        public void setAddress(String address) {
                this.address = address;
        }
        public String getDataType() {
                return dataType;
        }
        public void setDataType(String dataType) {
                this.dataType = dataType;
        }
        public Integer getTypeLength() {
                return typeLength;
        }
        public void setTypeLength(Integer typeLength) {
                this.typeLength = typeLength;
        }
        public String getAuthority() {
                return authority;
        }
        public void setAuthority(String authority) {
                this.authority = authority;
        }
}
