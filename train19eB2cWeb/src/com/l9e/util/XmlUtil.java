package com.l9e.util;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * bean与XML互转（基于Xstream注解）
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
}
