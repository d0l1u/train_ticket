package com.l9e.util;

import java.io.File;
import java.io.FileWriter;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

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
    * doc2XmlFile 
    * 将Document对象保存为一个xml文件到本地 
    * @return true:保存成功  flase:失败 
    * @param filename 保存的文件名 
    * @param document 需要保存的document对象 
    */    
   public static boolean doc2XmlFile(Document document,String filename) 
   { 
      boolean flag = true; 
      try 
      { 
            // 将document中的内容写入文件中 
            //默认为UTF-8格式，指定为"GB2312" 
            OutputFormat format = OutputFormat.createPrettyPrint(); 
            format.setEncoding("UTF-8"); 
            XMLWriter writer = new XMLWriter(new FileWriter(new File(filename)),format); 
            writer.write(document); 
            writer.close();             
        }catch(Exception ex) 
        { 
            flag = false; 
            ex.printStackTrace(); 
        } 
        return flag;       
   }
}
