package com.atguigu.gulimall.product.ItemReadAndWrite;

import java.util.*;

import at.bachmann.sys.common.smi.*;
import at.bachmann.sys.common.util.*;
import at.bachmann.util.*;
import at.bachmann.vis.dacc.*;
import at.bachmann.vis.dacc.value.*;


/**
 * 用于单机控制的风机数据类
 *
 * @author Jeffrey
 * @version 1.0
 */
public class BachmannWindTurbineNode {

    private Project project;
    private String name;
    private final String dsName;


    public BachmannWindTurbineNode(String name, String addr) throws Exception {
        project = new Project(name);
        this.name = name;
        dsName = "wnt" + ((int) (Math.random() * 100));
        DataSource datasource = null;
        datasource = project.createDataSource(
                "at.bachmann.vis.dacc.proxy.m1.M1DataSource", dsName,
                addr, "TCP");
        datasource.setParameter("VHDSESSIONNAME", "WT" + (new Date().getTime()));
        datasource.setParameter("RECONNECTION", "on");
        datasource.setParameter("VHDUPDATERATE", "1000");
        this.addItems();
    }

    /**
     * 搜索风场风机，返回Map<name, address>
     *
     * @return Map
     * @throws Exception
     */
    public static Map<String, String> searchBachmannWindTurbines() throws
            Exception {
        Map<String, String> map = new HashMap<String, String>();
        M1TargetFinder m1 = new M1TargetFinder();
        Vector m1Vector = null;
        m1Vector = m1.searchM1Targets(100);
        RpcBroadcastReply rpcBroadcastReply = null;
        for (int i = 0; i < m1Vector.size(); i++) {
            rpcBroadcastReply = (RpcBroadcastReply) m1Vector.elementAt(i);
            map.put(rpcBroadcastReply.getTargetName(),
                    rpcBroadcastReply.getIPAddress());
        }
        return map;
    }


    public Item getItem(String name) {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        return group.getItem(name);
    }

    public void addItems() throws Exception {
        DataSource datasource = project.getDataSource(dsName);
        Group group = null;
        group = datasource.createGroup("GroupAll",
                "at.bachmann.vis.dacc.proxy.m1.VhdPollingChangesMode");
        group.setParameter("POLLINGRATE", "1000");
        try {
            this.addItemsToGroup(group);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Enumeration enumeration = group.getItems();
        while (enumeration.hasMoreElements()) {
            Item item1 = (Item) enumeration.nextElement();
            debugProperties(item1);
        }
    }


    protected void addItemsToGroup(Group group) {
        ItemsReader reader = new ItemsReader();
        List<ItemInfo> infos1 = null;
        String name=null;
        try {
            infos1 = reader.getiInfos("ini/bachmann/items1.5.xml");
        } catch (Exception ex1) {
        }
        for (ItemInfo info : infos1) {
            name=info.getName();
            try {
                group.createItem(info.getName(), info.getAddress(),
                        DataType.getDataType(info.getDataType()),
                        1, info.getTypeLength(), info.getAuthority(), null);
            } catch (ItemException ex2) {
            } catch (InstantiationException ex2) {
            }
        }
    }

    /**
     * 连接风机并激活数据刷新
     *
     * @throws AuthenticationException
     * @throws CommunicationException
     * @throws IOException
     */
    public void start() throws Exception {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        datasource.connect();
        group.setActive(true);
    }

    /**
     * 中止数据刷新并断开风机
     *
     * @throws CommunicationException
     * @throws IOException
     */
    public void stop() throws Exception {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        group.setActive(false);
        datasource.disconnect();
    }

    /**
     * 注销风机，在程序退出时使用
     */
    public void destroy() {
        this.removeAllItemValueListeners();
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        try {
            group.removeAllItems();
        } catch (Exception ex) {
        }
        try {
            datasource.removeAllGroups();
        } catch (Exception ex1) {
        }
        datasource.removeAllDataSourceListeners();
        project.removeAllDataSources();
        group = null;
        datasource = null;
        project = null;
    }

    private void debugProperties(IPropertyCapable ipropertycapable) {
        Enumeration enumeration = ipropertycapable.getPropertyNames();
        String s = ipropertycapable.getClass().getName();
        String s1;
        String s2;
        for (; enumeration.hasMoreElements();) {
            s1 = (String) enumeration.nextElement();
            s2 = ipropertycapable.getProperty(s1);
        }
    }

    /**
     * 读取风机变量
     *
     * @param name String
     * @return String
     */
    public String getValue(String name) {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Item item = group.getItem(name);
        if (item != null) {
            String ret = null;
            try {
//                ret = item.read(ReadMode.FROM_CACHE).toString();
                ret = item.read(ReadMode.FROM_DEVICE).toString();
            } catch (Exception ex) {
            } finally {
                return ret;
            }
        }else{
            return null;
        }
    }

    /**
     * 获取版本
     *
     * @return float
     */
    public float getSwVer(){
        String mSwVer = getValue("mSwVer");
        if (mSwVer != null && mSwVer.length() >= 5) {
            return Float.parseFloat(mSwVer.substring(0, 4));
        }else{
            return 0;
        }
    }

    private Map<String, String> nullMap = new HashMap<String, String>();

    public String getDescription(String name) {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Item item = group.getItem(name);
        try {
            datasource.setParameter("VHDSESSIONNAME", "test");
        } catch (Exception ex1) {
        }
        try {
            datasource.setParameter("RECONNECTION", "on");
        } catch (InvalidParamValueException ex2) {
        } catch (InvalidParamException ex2) {
        }
        String ret = "null";
        try {
            ret = item.read(ReadMode.FROM_CACHE).getDescription();
        } catch (Exception ex) {
        } finally {
            return ret;
        }
    }

    public String getItemStatus(String name) {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Item item = group.getItem(name);
        return item.getQuality().toString();
    }

    public String getDataType(String name) {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Item item = group.getItem(name);
        return item.getDataType().toString();
    }

    public String getAccessRights(String name) {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Item item = group.getItem(name);
        return item.getAccessRights();
    }

    public String getSourceName(String name) {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Item item = group.getItem(name);
        return item.getSourceName();
    }

    public String getRawValueAsString(String name) {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Item item = group.getItem(name);
        String ret = "null";
        try {
            ret = item.read(ReadMode.FROM_CACHE).getRawValueAsString();
        } catch (Exception ex) {
        } finally {
            return ret;
        }
    }

    public int getItemCount() {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        return group.getItemsArr().length;
    }

    /**
     * 写入风机变量
     *
     * @param name String
     * @param value String
     * @param groupName String
     * @throws Exception
     */
    public void writeItem(String name, String value, String groupName) {
        Item item = this.project.getDataSource(dsName).getGroup("GroupAll").
                getItem(name);
        if (item != null) {
            try {
                item.write(WriteMode.TO_DEVICE, value);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            } catch (ValueException ex) {
            } catch (ItemException ex) {
                ex.printStackTrace();
            } catch (CommunicationException ex) {
                ex.getStackTrace();
            }
        } else {
        }
    }

    /**
     * 添加数据源监听者，监听与风机的连接
     *
     * @param listener DataSourceListener
     */
    public void addDataSourceListener(DataSourceListener listener) {
        DataSource datasource = project.getDataSource(dsName);
        datasource.addDataSourceListener(listener);
    }

    public void removeAllDataSourceListeners() {
        DataSource datasource = project.getDataSource(dsName);
        datasource.removeAllDataSourceListeners();
    }

    /**
     * 添加数据监听者
     *
     * @param listener ItemValueListener
     */
    public void addItemValueListener(ItemValueListener listener) {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Enumeration enumeration = group.getItems();
        while (enumeration.hasMoreElements()) {
            Item item = (Item) enumeration.nextElement();
            for (int i = 0; i < item.getArrayLength(); i++) {
                item.addItemValueListener(listener, i);
            }
        }
    }

    public void removeAllItemValueListeners() {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Enumeration enumeration = group.getItems();
        while (enumeration.hasMoreElements()) {
            Item item = (Item) enumeration.nextElement();
            item.removeAllItemValueListeners();
        }
    }

    public String[] getAllItemNames() {
        DataSource datasource = project.getDataSource(dsName);
        Group group = datasource.getGroup("GroupAll");
        Item[] items = group.getItemsArr();
        String[] ret = new String[items.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = items[i].getName();
        }
        return ret;
    }

    public Project getProject() {
        return project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatusCode() {
        int code = -255;
        try {
            code = Integer.parseInt(this.getValue("CodeInfo"));
        } catch (NumberFormatException ex) {
        } finally {
            return code;
        }
    }
}
