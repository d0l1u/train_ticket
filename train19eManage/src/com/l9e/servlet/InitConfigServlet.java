package com.l9e.servlet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.BookService;


@Component(value="initConfigServlet")
public class InitConfigServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(InitConfigServlet.class);
	@Resource
	private BookService BookService;
	
	@SuppressWarnings("unused")
	@Value("#{propertiesReader[no]}")
	private String no;//省市区id
	@SuppressWarnings("unused")
	@Value("#{propertiesReader[name]}")
	private String name;//省市区name
	
	public static Map<String,String> PROMAP = new LinkedHashMap<String, String>();//存放省id和name的map
	public static Map<String,String> CITYMAP = new LinkedHashMap<String, String>();//存放市id和name的map
	public static Map<String,String> REGIONMAP = new LinkedHashMap<String, String>();//存放区id和name的map
	@Override
	public void init() throws ServletException {
	//临时测试代理
	//System.getProperties().setProperty("http.proxyHost", "192.168.63.238");
	//System.getProperties().setProperty("http.proxyPort", "6789" );
		System.getProperties().setProperty("http.proxyHost", "127.0.0.1");
		System.getProperties().setProperty("http.proxyPort", "8080" );
		logger.info("===============init system config begin=====================");
		
		logger.info("load province city region...");
		List<Map<String,String>> list = BookService.queryNoAndName();
		for(int i=0;i<list.size();i++){
			Map<String,String> map = list.get(i);   
			String pno = map.get("pno");//省id
			String pname =  map.get("pname");//省name
			String cno = map.get("cno");//市id
			String cname =  map.get("cname");//市name
			String qno = map.get("qno");//区id
			String qname =  map.get("qname");//区name
			PROMAP.put(pno, pname);//存放省id和name的map
			CITYMAP.put(cno, cname);//存放市id和name的map
			REGIONMAP.put(qno, qname);//存放区id和name的map
		}
		logger.info("province city region has been loaded...");
//		logger.info("省id和name"+PROMAP);
//		logger.info("市id和name"+CITYMAP);
//		logger.info("区id和name"+REGIONMAP);
//		System.out.println(PROMAP);
//		System.out.println(CITYMAP);
//		System.out.println(REGIONMAP);
		super.init();
		logger.info("===============init system config end=======================");
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	

}
