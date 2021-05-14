package com.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;


/**
 * ∂¡»°xml –Œ≥…List<ItemInfo>
 *
 *
 *
 */
public class ItemsReader extends AbstractXmlLoader<ItemInfo> {

//                private static final String PATH="items1.xml";

    private List<ItemInfo> iInfos;


    public List<ItemInfo> getiInfos(String fileName) throws DocumentException, IOException {
        if (iInfos == null) {
            File xml = new File(fileName);
            iInfos = load(xml);
        }
        return iInfos;
    }

    @Override
    protected ItemInfo create(Element element) {
        // TODO Auto-generated method stub
        ItemInfo iInfo = new ItemInfo();
        String name = element.attributeValue("name");
        iInfo.setName(name);
        String address = element.elementText("Address");
        iInfo.setAddress(address);
        String dataType = element.elementText("DataType");
        iInfo.setDataType(dataType);
        String typeLength = element.elementText("TypeLength");
        iInfo.setTypeLength(Integer.parseInt(typeLength));
        String authority = element.elementText("Authority");
        iInfo.setAuthority(authority);
        return iInfo;
    }

    @Override
    protected String getRootName() {
        // TODO Auto-generated method stub
        return "Items";
    }

    @Override
    protected String getSubName() {
        // TODO Auto-generated method stub
        return "Item";
    }

    public String getAddress(String name) {
        for (ItemInfo info : iInfos) {
            if (info.getName().equals(name)) {
                return info.getAddress();
            }
        }
        return null;
    }

}
