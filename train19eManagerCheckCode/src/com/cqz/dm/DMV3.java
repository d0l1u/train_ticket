 package com.cqz.dm;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.log4j.Logger;

import com.cqz.utils.CodeGenerator;
import com.cqz.utils.DebugOut;
import com.cqz.utils.GetMacAddress;
import com.cqz.utils.JsonUtil;
import com.cqz.utils.MD5FileUtil;
import com.l9e.common.TrainConsts;
import com.l9e.util.MD5Util;

/**
 * 
 * @author cqz qq:6199401 email:cheqinzho@qq.com
 * 
 */
@Deprecated
public class DMV3
{
	/** 主机 */
	static final String	GETDOMAINARRY	= "http://dama3.hyslt.com";
	/** 测试接口验证码路径 */
	static final String	IMGPATH			= "D:/1.jpg";
	/** 用户名 */
	static final String	USERNAME		= TrainConsts.USERNAME;
	/** 密码 */
	static final String	PASSWORD		= TrainConsts.PASSWORD;
	/** 最大请求次数 */
	static final int	TIMES			= 10;

	/** 设置编码 */
	static final String	CODE			= "GBK2312";

	
	private static final Logger logger = Logger.getLogger(CodeGenerator.class);
	public static void main(String[] args) throws Exception
	{

		String domain = getDoMainArryAndValidUser();

		File f = new File(java.net.URLDecoder.decode(DMV3.class.getResource("1.jpg").getFile()));
		String fileName = MD5FileUtil.getFileMD5String(f) + ".jpg";
		String yzm_id = add(domain, f);
		Thread.currentThread().sleep(3000L);
		for (int i = 0; i < TIMES; i++)
		{
			DebugOut.println("getResult()=" + getResult(domain, yzm_id));

			// =============== 验证码报错API start ===============
			// yzm_error(domain, yzm_id);
			// =============== 验证码报错API end ===============
			Thread.sleep(1000L);
		}

	}

	// 获取服务器
	@SuppressWarnings("unchecked")
	public static String getDoMainArryAndValidUser() throws Exception
	{
		HttpClient client = new HttpClient();
		String result = "";
		PostMethod postMethod;
		postMethod = new PostMethod(GETDOMAINARRY + "/api.php?mod=yzm&act=server");
		postMethod.addParameter("user_name", USERNAME);
		postMethod.addParameter("user_pw", getBASE64(PASSWORD));
		postMethod.addParameter("mac", CodeGenerator.getStrMac());
		postMethod.addParameter("key", CodeGenerator.getKey());
		postMethod.addParameter("submit", "%CC%ED+%BC%D3");
		
		logger.info("获取服务user_name:"+USERNAME+",user_pw:"+getBASE64(PASSWORD)+",mac:"+CodeGenerator.getStrMac()
				+",key:"+CodeGenerator.getKey()
		);
		client.executeMethod(postMethod);
		result = new String(postMethod.getResponseBody(), "gbk");
		
		
		logger.info("获取服务请求结果:"+result);
		DebugOut.println("getDoMainArryAndValidUser:" + result);
		Map<String, String> rm = JsonUtil.getMapFromJson(result);
		return (String) rm.get("data");

	}

	// 上传验证码
	@SuppressWarnings("deprecation")
	public static String add(String domain, File f) throws Exception
	{

		HttpClient client = new HttpClient();
		String result = "";
		MultipartPostMethod filePost = new MultipartPostMethod("http://" + domain + "/api.php?mod=yzm&act=add");
		
		System.out.println("file:"+f);
		
		FilePart cbFile = new FilePart("upload", f);
		cbFile.setContentType("image/*");
		//System.out.println("image/*,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
		//cbFile.setContentType("image/gif");
		filePost.addParameter("mac", CodeGenerator.getStrMac());
		filePost.addParameter("key", CodeGenerator.getKey());
		filePost.addParameter("user_name", USERNAME);
		filePost.addParameter("user_pw", getBASE64(PASSWORD));

		filePost.addParameter("yzmtype_mark", "0");
		filePost.addParameter("yzm_minlen", "4");
		filePost.addParameter("yzm_maxlen", "4");
		// todo 待开发者自己填写
		// filePost.addParameter("zztool_token", "xxx");

		String fileName = MD5FileUtil.getFileMD5String(f) + ".GIF";
		logger.info("fileName:"+fileName);
		filePost.addParameter("filename", fileName);
		filePost.addPart(cbFile);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		DebugOut.println("result:" + result);
		
		
		logger.info("上传图片获取结果:"+result);
		Map<String, Integer> rm = JsonUtil.getMapFromJson(result);
		return (Integer) rm.get("data") + "";
	}

	// 作者分成接口
	@SuppressWarnings("deprecation")
	public static String add4Dev(String domain, File f) throws Exception
	{

		HttpClient client = new HttpClient();
		String result = "";

		MultipartPostMethod filePost = new MultipartPostMethod("http://" + domain + "/api.php?mod=yzm&act=add");
		FilePart cbFile = new FilePart("upload", f);
		cbFile.setContentType("image/pjpeg");
		filePost.addParameter("mac", CodeGenerator.getStrMac());
		filePost.addParameter("key", CodeGenerator.getKey());
		filePost.addParameter("user_name", USERNAME);
		filePost.addParameter("user_pw", getBASE64(PASSWORD));

		filePost.addParameter("yzmtype_mark", "0");
		filePost.addParameter("yzm_minlen", "4");
		filePost.addParameter("yzm_maxlen", "4");
		// todo 待开发者自己填写
		// filePost.addParameter("zztool_token", "xxx");


		String fileName = MD5FileUtil.getFileMD5String(f) + ".jpg";
		filePost.addParameter("filename", fileName);
		filePost.addPart(cbFile);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		DebugOut.println("result:" + result);
		Map<String, Integer> rm = JsonUtil.getMapFromJson(result);
		return (Integer) rm.get("data") + "";
	}

	// 查询点数
	@SuppressWarnings("deprecation")
	public static String point(String domain) throws Exception
	{
		HttpClient client = new HttpClient();
		String result = "";
		MultipartPostMethod filePost = new MultipartPostMethod("http://" + domain + "/api.php?mod=yzm&act=point");
		filePost.addParameter("mac", CodeGenerator.getStrMac());
		filePost.addParameter("key", CodeGenerator.getKey());
		filePost.addParameter("user_name", USERNAME);
		filePost.addParameter("user_pw", getBASE64(PASSWORD));
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		return result;
	}

	// 获取结果
	@SuppressWarnings("deprecation")
	public static String getResult(String domain, String yzm_id) throws Exception
	{
		HttpClient client = new HttpClient();
		String result = "";
		MultipartPostMethod filePost = new MultipartPostMethod("http://" + domain + "/api.php?mod=yzm&act=result");
		filePost.addParameter("mac", CodeGenerator.getStrMac());
		filePost.addParameter("key", CodeGenerator.getKey());
		filePost.addParameter("yzm_id", yzm_id);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		DebugOut.println("getResult:" + result);
		return result;
	}

	// 报错
	@SuppressWarnings("deprecation")
	public static String yzm_error(String domain, String yzm_id) throws Exception
	{
		HttpClient client = new HttpClient();
		String result = "";
		MultipartPostMethod filePost = new MultipartPostMethod("http://" + domain + "/api.php?mod=dmuser&act=yzm_error");
		filePost.addParameter("mac", CodeGenerator.getStrMac());
		filePost.addParameter("key", CodeGenerator.getKey());
		filePost.addParameter("yzm_id", yzm_id);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		DebugOut.println("yzm_error:" + result);
		return result;
	}

	// 将 s 进行 BASE64 编码
	public static String getBASE64(String s)
	{
		if (s == null) return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	public static String getTranCode(String str)
	{
		if (System.getProperty("os.name").contains("Windows"))
		{
			byte[] data;
			try
			{
				data = str.getBytes("ISO8859-1");
				return new String(data, "GBK");
			} catch (UnsupportedEncodingException e)
			{
				return str;
			}
		} else
		{
			return str;
		}

	}

	public static String getTranCode(String str, String code)
	{
		if (System.getProperty("os.name").contains("Windows"))
		{
			byte[] data;
			try
			{
				data = str.getBytes("ISO8859-1");
				return new String(data, code);
			} catch (UnsupportedEncodingException e)
			{
				return str;
			}
		} else
		{
			return str;
		}

	}
	
	
	/**
	 * 联众上传验证码并获取验证码结果  接口
	 * @author liuyi02
	 * */
	@SuppressWarnings("deprecation")
	public static Map<String,String> uploadGetResult(File f) throws Exception
	{

		HttpClient client = new HttpClient();
		String result = "";
		MultipartPostMethod filePost = new MultipartPostMethod("http://bbb4.hyslt.com/api.php?mod=php&act=upload");
		
		FilePart cbFile = new FilePart("upload", f);
		cbFile.setContentType("image/*");
		filePost.addParameter("user_name", USERNAME);
		filePost.addParameter("user_pw", PASSWORD);

		filePost.addParameter("yzmtype_mark", "0");
		filePost.addParameter("yzm_minlen", "4");
		filePost.addParameter("yzm_maxlen", "4");
		// todo 待开发者自己填写
		// filePost.addParameter("zztool_token", "xxx");

		filePost.addPart(cbFile);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		DebugOut.println("result:" + result);
		Map<String,String> map=null;
		try {
			JSONObject json=JSONObject.fromObject(result);
			if("true".equals(json.get("result").toString())){
				logger.info("data:"+json.getString("data"));
				JSONObject data=JSONObject.fromObject(json.getString("data"));
				map=new HashMap<String,String>();
				map.put("id", data.getString("id"));
				map.put("val",data.getString("val"));
			}
			logger.info("result:"+result);
		} catch (Exception e) {
			logger.info("联众打码异常result:"+result);
			map=null;
			e.printStackTrace();
		}
		return map;
	}
	
	
	
	
	/**
	 * 联众上传错误验证码 接口
	 * @author liuyi02
	 * *//*
	@SuppressWarnings("deprecation")
	public static Map<String,String> uploadErrorResult() throws Exception
	{
		HttpClient client = new HttpClient();
		String result = "";
		MultipartPostMethod filePost = new MultipartPostMethod("http://bbb4.hyslt.com/api.php?mod=php&act=upload");
		
		FilePart cbFile = new FilePart("upload", f);
		cbFile.setContentType("image/*");
		filePost.addParameter("user_name", USERNAME);
		filePost.addParameter("user_pw", PASSWORD);

		filePost.addParameter("yzmtype_mark", "0");
		filePost.addParameter("yzm_minlen", "4");
		filePost.addParameter("yzm_maxlen", "4");
		// todo 待开发者自己填写
		// filePost.addParameter("zztool_token", "xxx");

		filePost.addPart(cbFile);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		DebugOut.println("result:" + result);
		Map<String,String> map=null;
		try {
			JSONObject json=JSONObject.fromObject(result);
			if("true".equals(json.get("result").toString())){
				logger.info("data:"+json.getString("data"));
				JSONObject data=JSONObject.fromObject(json.getString("data"));
				map=new HashMap<String,String>();
				map.put("id", data.getString("id"));
				map.put("val",data.getString("val"));
			}
			logger.info("result:"+result);
		} catch (Exception e) {
			logger.info("联众打码异常result:"+result);
			map=null;
			e.printStackTrace();
		}
		return map;
	}
	*/
	
	
	
	public static String	UU_USERNAME	= "UserName not API-USER Name";			//UU用户名
	public static String	UU_PASSWORD	= "UserPassword";						//UU密码
	public static String	DLLPATH		= "lib\\UUWiseHelper";					//DLL
	//public static String	IMGPATH		= "img\\test.png";
	public static int		SOFTID		= 2097;									//软件ID 获取方式：http://dll.uuwise.com/index.php?n=ApiDoc.GetSoftIDandKEY
	public static String	SOFTKEY		= "b7ee76f547e34516bc30f6eb6c67c7db";	//软件KEY 获取方式：http://dll.uuwise.com/index.php?n=ApiDoc.GetSoftIDandKEY
	public static String	DLLVerifyKey="32F1C86B-E64C-4EAF-8BC1-C142570008BC";	//校验API文件是否被篡改，实际上此值不参与传输，关系软件安全，高手请实现复杂的方法来隐藏此值，防止反编译,获取方式也是在后台获取软件ID和KEY一个地方
	
	
	
	
	/**
	 * 优优云登入请求
	 * @author liuyi02
	 * @throws IOException 
	 * @throws HttpException 
	 * */
	@SuppressWarnings("deprecation")
	public static void uu_login() throws HttpException, IOException{
		//File f=new File();
		HttpClient client = new HttpClient();
		String result = "";
		MultipartPostMethod filePost = new MultipartPostMethod("http://login.abc.com/Upload/UULogin.aspx");
		
		//所有请求的通用header：
		/*request.Headers.Add("SID",软件id);                                            
		request.Headers.Add("HASH",md5(软件id+软件key.ToUpper()));					//32位MD5加密小写
		request.Headers.Add("UUVersion","1.0.0.1");                    
		request.Headers.Add("UID",UserID);											//没有登录之前，UserID就用100。登录成功后，服务器会返回UserID，之后的请求就用服务器返回的UserID		      
		request.Headers.Add("User-Agent", MD5(软件key.ToUpper() + UserID));	*/
		/**初始用户id 100*/
		String UIDINIT="100";
		/**通用header*/
		filePost.addRequestHeader("SID",SOFTID+"");
		filePost.addRequestHeader("HASH",SOFTID+SOFTKEY.toUpperCase());
		filePost.addRequestHeader("UUVersion", "1.0.0.1");
		filePost.addRequestHeader("UID", UIDINIT);
		filePost.addRequestHeader("User-Agent",MD5Util.myMd5(SOFTKEY.toUpperCase()+UIDINIT, "UTF-8"));
		/**登入特别header*/
		/*	request.Headers.Add("KEY",MD5(软件key.ToUpper()+UserName.ToUpper())+MAC);    */				//MAC把特殊符号去掉，纯粹字母数字
		/*request.Headers.Add("UUKEY", MD5(UserName.ToUpper() + MAC + 软件key.ToUpper()));*/
		filePost.addRequestHeader("KEY",MD5Util.myMd5(SOFTKEY.toUpperCase()+UU_USERNAME.toUpperCase()+GetMacAddress.getUUYUnixMACAddress(),"UTF-8"));
		filePost.addRequestHeader("UUKEY",MD5Util.myMd5(UU_USERNAME.toUpperCase()+GetMacAddress.getUUYUnixMACAddress()+SOFTKEY.toUpperCase(),"UTF-8"));
		
		
		filePost.addParameter("U", UU_USERNAME);
		filePost.addParameter("p", MD5Util.myMd5(UU_PASSWORD,"UTF-8"));
		//FilePart cbFile = new FilePart("upload", f);
		//cbFile.setContentType("image/*");
		/*filePost.addParameter("user_name", USERNAME);
		filePost.addParameter("user_pw", PASSWORD);

		filePost.addParameter("yzmtype_mark", "0");
		filePost.addParameter("yzm_minlen", "4");
		filePost.addParameter("yzm_maxlen", "4");*/
		
		
		
		
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		DebugOut.println("result:" + result);
	/*	Map<String,String> map=null;
		try {
			JSONObject json=JSONObject.fromObject(result);
			if("true".equals(json.get("result").toString())){
				logger.info("data:"+json.getString("data"));
				JSONObject data=JSONObject.fromObject(json.getString("data"));
				map=new HashMap<String,String>();
				map.put("id", data.getString("id"));
				map.put("val",data.getString("val"));
			}
			logger.info("result:"+result);
		} catch (Exception e) {
			logger.info("联众打码异常result:"+result);
			map=null;
			e.printStackTrace();
		}
		return map;
	*/
	}
	
	
}
