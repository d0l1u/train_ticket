package com.l9e.util;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * bean与XML互转（基于Xstream注解）
 * @author zhangjun
 *
 */
public class XmlUtil{
	
	/**
	 * obj序列化成XML
	 * @param obj
	 * @param encode
	 * @return
	 */
	public static String toXml(Object obj, String encode){
		encode = StringUtils.isEmpty(encode) ? "UTF-8" : encode;
		XStream xstream=new XStream(new DomDriver(encode)); //指定编码解析器,直接用jaxp dom来解释
		
		//如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
		xstream.processAnnotations(obj.getClass()); 
		return xstream.toXML(obj);
	}
	
	/**
	 * XML反序列化为bean
	 * @param <T>
	 * @param xmlStr
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T>T toBean(String xmlStr,Class<T> cls){
		XStream xstream=new XStream(new DomDriver());
		//如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
		xstream.processAnnotations(cls);
		T obj=(T)xstream.fromXML(xmlStr);
		return obj;			
	} 
	
	/**
	 * 
	 * xml字符串转为document对象
	 * @param xmlConent
	 * @return
	 * @throws DocumentException
	 */
	public static Document xmlFromString(String xmlConent)
	throws DocumentException {
     return DocumentHelper.parseText(xmlConent);
	}
	

	/**
	 * 装换成xml格式的数据
	 * @param desc
	 * @return
	 */
	public static String zhuanXml(String desc){
		String filecontent="<?xml version=\"1.0\" encoding=\"utf-8\"?><product><synopsis><![CDATA["+desc+"]]></synopsis><desc><![CDATA["+desc+"]]></desc></product>";
		return filecontent;
	}
	/**
	 * 将一个数组转换成xml格式的数据
	 * @param desc
	 * @return
	 */
	public static String zhuanXml2(String[] desc){
		String filecontent="<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
								"<product>" +
									"<synopsis><![CDATA["+desc[0]+"]]></synopsis>" +
									"<desc><![CDATA["+desc[1]+"]]></desc>" +
								"</product>";
		return filecontent;
	}
	
	/**
	 * 解析xml 并且获取数据
	 * @param file
	 * @param element
	 * @return
	 * @throws DocumentException 
	 */
	public static String getTxt(File file,String element) {
		String str =null;
		try {
			SAXReader reader = new SAXReader(); 
			Document doc = reader.read(file); 
			Element root = doc.getRootElement(); //获取根节点
			str = root.elementText(element);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		return str;
	}
	/**
	 * 解析xml 并且获取数据
	 * @param file
	 * @param element
	 * @return
	 * @throws DocumentException 
	 */
	public static String[] getTxt2(File file,String[] element) {
		String[] str =null;
		try {
			SAXReader reader = new SAXReader(); 
			if(file.exists()){
				Document doc = reader.read(file); 
				Element root = doc.getRootElement(); //获取根节点
				String s1= root.elementText(element[0]);
				String s2= root.elementText(element[1]);
				str=new String[]{s1,s2};
			}else{
				str=new String[]{"",""};
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			
		}
		return str;
	}

	public static String zhuanProductXml(String[] strings) {
		String filecontent="<?xml version=\"1.0\" encoding=\"utf-8\" ?> " +
		"<product>" +
			"<notice><![CDATA["+strings[0]+"]]></notice>" +
			"<info><![CDATA["+strings[1]+"]]></info>" +
		"</product>";
		return filecontent;
	}
	
	   /**
     * 获取node值
     * Description：
     * @param node
     * @return
     */
	public static String getValue(Node node){
		if(node==null){
			return null;
		}
		return node.getText();
		
	}
}
