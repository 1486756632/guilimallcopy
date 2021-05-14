package com.atguigu.gulimall.product.ItemReadAndWrite;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 抽象读取xml
 *
 * @author jacob
 *
 * @param <T>
 */
public abstract class AbstractXmlLoader<T> {
	/**
	 * 读取xml 实体对象集合
	 *
	 * @param file
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public List<T> load(File file) throws DocumentException, IOException{
		List<T> loads = new ArrayList<T>();
		SAXReader reader = new SAXReader();
		reader.setEncoding("GBK");
		Document doc;
		doc = reader.read(file);
		Element root = doc.getRootElement();
		List<Element> elements = root.elements(getSubName());
		for(Element element:elements) {
			loads.add(create(element));
		}
		return loads;
	}

	/**
	 * 创建单个对象
	 *
	 * @param element
	 * @return
	 */
	protected abstract T create(Element element) throws IOException;

	/**
	 * 获取根节点
	 *
	 * @return
	 */
	protected abstract String getRootName();

	/**
	 * 获取对象开始节点
	 *
	 * @return
	 */
	protected abstract String getSubName();
}
