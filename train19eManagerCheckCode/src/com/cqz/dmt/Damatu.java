package com.cqz.dmt;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.log4j.Logger;

import com.cqz.dm.DMV2;
import com.cqz.utils.CodeGenerator;
import com.cqz.utils.DebugOut;
import com.cqz.utils.GetMacAddress;
import com.cqz.utils.JsonUtil;
import com.cqz.utils.MD5FileUtil;
import com.l9e.common.TrainConsts;
import com.l9e.util.MD5Util;
import com.l9e.util.MemcachedUtil;
@Deprecated
public class Damatu {
	//打码兔相关配置信息
	/**
	 * 
	 * 开发者账号信息
	 * */
	public static String username="huocpiao";
	public static String pwd="huocpiao";
	
	public static String appID="37623";
	public static String key="e6772a93b5130c4d0d3e5fca74228f3b";
	public static String sname="12306";
	public static String encinfo="";
	//encinfo
	/*账号：kuyou123456
	密码：kuyou19e
	用户账号
	*
	*/
	public static String yh_username="kuyou123456";
	public static String yh_pwd="kuyou19e";
	
	/** 授权信息 */
	public static String AUTH ="";
	
	/** 验证码类型ID */
	public static final String TYPE ="287";
	
	/** 验证码长度 */
	public static final String LEN ="";
	
	/** 超时时间 */
	public static final String TIMEOUT ="30";
	
	
	/** 通过URL打码*/
	public static final String cookie="";
	public static final String referer="";
	
	
	
	private static final Logger logger = Logger.getLogger(CodeGenerator.class);
	public static void main(String[] args) throws Exception
	{
		System.setProperty("http.proxyHost", "192.168.65.126");
		System.setProperty("http.proxyPort", "3128");
		init();
		
		
		login();
		File f = new File("D:\\Workspace\\train19eManageCheckCode\\WebRoot\\upload\\2015-03-17\\PIC1503171301135583.GIF");
		//File f=new File("");
		long start=System.currentTimeMillis();
	
		getCode(postFileUpload(f));
		System.out.println(System.currentTimeMillis()-start);
	}

	/**打码兔   接口    */
	
	
	//代理初始化
//	private static void setDlConfig(HttpClient client ){
//		client.getHostConfiguration().setProxy("192.168.65.126", 3128); 
//	}
	
	
	
	//预授权方法
	public static void init() throws Exception{
		/*功能URL：http://api.dama2.com:7788/app/login
			输入参数：appID、sname(可选参数)、encinfo
			返回数据：ret，desc，auth*/
		HttpClient client = new HttpClient();
//		setDlConfig(client);
		String result = "";
		GetMethod getMethod;
		getMethod=new GetMethod("http://api.dama2.com:7788/app/preauth");
		client.executeMethod(getMethod);
		//返回数据：ret，desc，auth(预授信息，仅用于计算加密信息)
		result = new String(getMethod.getResponseBody(), "UTF-8");
		logger.info("预授信息:" + result);
		Map<String, String> rm = JsonUtil.getMapFromJson(result);
		if(rm.get("ret").equals("0")){//调用成功，可保存返回的auth作为以后请求的参数
			AUTH=rm.get("auth");
//			logger.info("auth:"+AUTH);
		}else{
			logger.info("预授权出错:"+rm.get("ret"));
		}
		
	}
	
	//登入
	public static void login() throws Exception{
		/*功能URL：http://api.dama2.com:7788/app/login
			输入参数：appID、sname(可选参数)、encinfo
			返回数据：ret，desc，auth*/
		HttpClient client = new HttpClient();
//		setDlConfig(client);
		String result = "";
		PostMethod postMethod;
		postMethod = new PostMethod("http://api.dama2.com:7788/app/login");
		postMethod.addParameter("appID",appID);
		//postMethod.addParameter("sname",sname);
		String encinfo=DamatuMd5Util.jm(AUTH, yh_username, yh_pwd, key);
		postMethod.addParameter("encinfo",encinfo);
		/*public static String yh_username="kuyou123456";
		public static String yh_pwd="kuyou19e";*/
		
		/** 授权信息 */
		client.executeMethod(postMethod);
		result = new String(postMethod.getResponseBody(), "UTF-8");
		
		logger.info("login result:"+result);
		Map<String, String> rm = JsonUtil.getMapFromJson(result);
		String desc=rm.get("desc");
//		logger.info("login:"+desc);
		if(rm.get("ret").equals("0")){//调用成功，可保存返回的auth作为以后请求的参数
			AUTH=rm.get("auth");
			MemcachedUtil.getInstance().setAttribute("DAMATU_AUTH", AUTH, 9*60*1000);
		}
	}
	
	
	//POST文件打码
	public static String postFileUpload(File f) throws Exception{
		/*功能URL：http://api.dama2.com:7788/app/decode
		输入参数：auth、type、len(可选)、timeout（可选）、文件数据
		返回数据：ret，desc，aut，id(用于查询结果和报告结果)*/
		HttpClient client = new HttpClient();
//		setDlConfig(client);
		String result = "";
		MultipartPostMethod filePost = new MultipartPostMethod("http://api.dama2.com:7788/app/decode");
		
		FilePart cbFile = new FilePart("upload", f);
		cbFile.setContentType("image/*");
		if(MemcachedUtil.getInstance().getAttribute("DAMATU_AUTH")==null || MemcachedUtil.getInstance().getAttribute("DAMATU_AUTH").equals("")){
			init();
			login();
			logger.info("预授权、登录打码兔");
		}
		filePost.addParameter("auth", AUTH);
		//filePost.addParameter("len", PASSWORD);
		filePost.addParameter("timeout", TIMEOUT);
		filePost.addParameter("type", TYPE);

		filePost.addPart(cbFile);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		//result = getTranCode(filePost.getResponseBodyAsString(), "UTF-8");
		result=new String(filePost.getResponseBody(), "UTF-8");
//		DebugOut.println("result:" + result);
		Map<String,String> map=null;
		try {
			logger.info("postFileUpload:"+result);
			Map<String, String> rm = JsonUtil.getMapFromJson(result);
			String id=rm.get("id");
//			logger.info("postFileUpload succsess:"+id);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("打码兔打码异常result:"+result);
			return null;
		}
	}
	

	//查询打码结果
	public static String getCode(String id) throws Exception{
	/*	功能URL：http://api.dama2.com:7788/app/getResult
			输入参数：auth，id
			返回数据：ret，desc，result，cookie, auth*/
		String code=null;
		HttpClient client = new HttpClient();
//		setDlConfig(client);
		String result = "";
		PostMethod postMethod;
		postMethod = new PostMethod("http://api.dama2.com:7788/app/getResult");
		if(MemcachedUtil.getInstance().getAttribute("DAMATU_AUTH")==null || MemcachedUtil.getInstance().getAttribute("DAMATU_AUTH").equals("")){
			init();
			login();
			logger.info("预授权、登录打码兔");
		}
		postMethod.addParameter("auth",AUTH);
		postMethod.addParameter("id",id);
		
		/** 授权信息 */
		client.executeMethod(postMethod);
		result = new String(postMethod.getResponseBody(), "UTF-8");
		
		logger.info("getCode result:"+result);
		Map<String, String> rm = JsonUtil.getMapFromJson(result);
		String ret=rm.get("ret");
		if("0".equals(ret)){
			code=rm.get("result");
		}
//		logger.info("getCode code:"+code);
		return code;
	}
	
	
	//报告结果
	public static String reportError(String id) throws Exception{
	/*	功能URL：http://api.dama2.com:7788/app/getResult
			输入参数：auth，id
			返回数据：ret，desc，result，cookie, auth*/
		String code=null;
		HttpClient client = new HttpClient();
//		setDlConfig(client);
		String result = "";
		PostMethod postMethod;
		postMethod = new PostMethod("http://api.dama2.com:7788/app/reportError");
		
		if(MemcachedUtil.getInstance().getAttribute("DAMATU_AUTH")==null || MemcachedUtil.getInstance().getAttribute("DAMATU_AUTH").equals("")){
			init();
			login();
			logger.info("预授权、登录打码兔");
		}
		
		postMethod.addParameter("auth",AUTH);
		postMethod.addParameter("id",id);
		
		/** 授权信息 */
		client.executeMethod(postMethod);
		result = new String(postMethod.getResponseBody(), "UTF-8");
		
//		logger.info("reportError pic_id:"+ id +", result:"+result);
		Map<String, String> rm = JsonUtil.getMapFromJson(result);
		String ret=rm.get("ret");
		if("0".equals(ret)){
			AUTH=rm.get("auth");
			return "true";
		}else{
			logger.info("reportError pic_id:"+ id +", result:"+result + ", ret:" + rm.get("ret") + ", desc:" + rm.get("desc"));
			return "false";
		}
	}
	
	
	
	
}
