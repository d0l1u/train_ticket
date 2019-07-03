package com.l9e.util;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
	   
	   
	   /**
	     * @description 将xml字符串转换成map
	     * @param xml
	     * @return Map
	     */
	    public static Map readStringXmlOut(String xml) {
	        Map map = new HashMap();
	        Document doc = null;
	        try {
	            doc = DocumentHelper.parseText(xml); // 将字符串转为XML

	            Element rootElt = doc.getRootElement(); // 获取根节点

	            System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称


	            Iterator iter = rootElt.elementIterator("head"); // 获取根节点下的子节点head

	            // 遍历head节点

	            while (iter.hasNext()) {

	                Element recordEle = (Element) iter.next();
	                String title = recordEle.elementTextTrim("title"); // 拿到head节点下的子节点title值

	                System.out.println("title:" + title);
	                map.put("title", title);

	                Iterator iters = recordEle.elementIterator("script"); // 获取子节点head下的子节点script


	                // 遍历Header节点下的Response节点

	                while (iters.hasNext()) {

	                    Element itemEle = (Element) iters.next();

	                    String username = itemEle.elementTextTrim("username"); // 拿到head下的子节点script下的字节点username的值

	                    String password = itemEle.elementTextTrim("password");

	                    System.out.println("username:" + username);
	                    System.out.println("password:" + password);
	                    map.put("username", username);
	                    map.put("password", password);

	                }
	            }

	            Iterator iterss = rootElt.elementIterator("body"); ///获取根节点下的子节点body

	            // 遍历body节点

	            while (iterss.hasNext()) {
	                Element recordEless = (Element) iterss.next();
	                String result = recordEless.elementTextTrim("result"); // 拿到body节点下的子节点result值

	                System.out.println("result:" + result);

	                Iterator itersElIterator = recordEless.elementIterator("form"); // 获取子节点body下的子节点form

	                // 遍历Header节点下的Response节点

	                while (itersElIterator.hasNext()) {

	                    Element itemEle = (Element) itersElIterator.next();

	                    String banlce = itemEle.elementTextTrim("banlce"); // 拿到body下的子节点form下的字节点banlce的值

	                    String subID = itemEle.elementTextTrim("subID");

	                    System.out.println("banlce:" + banlce);
	                    System.out.println("subID:" + subID);
	                    map.put("result", result);
	                    map.put("banlce", banlce);
	                    map.put("subID", subID);
	                }
	            }
	        } catch (DocumentException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return map;
	    }
}
