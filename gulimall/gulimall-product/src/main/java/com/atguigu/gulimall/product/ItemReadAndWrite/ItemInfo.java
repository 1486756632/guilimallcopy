package com.atguigu.gulimall.product.ItemReadAndWrite;
/**
 * Item������pojo
 *
 */
public class ItemInfo {
//������
private String name ;
//������ַ
private String address;
//��������
private String dataType;
//��������
private Integer typeLength;
//��дȨ��
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
