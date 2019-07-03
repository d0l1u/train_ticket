package com.cqz.dm;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqz.utils.CodeGenerator;
import com.cqz.utils.MD5FileUtil;
import com.l9e.common.TrainConsts;

/**
 * 
 * @author cqz qq:6199401 email:cheqinzho@qq.com
 * 
 */
@Deprecated
public class DMV2 {
	//static final String GETDOMAINARRY = "http://dama3.hyslt.com/lz_yzmphp_DLL/GetDoMainArry.php";
	
	/**备用域名*/
	static final String GETDOMAINARRY = "http://bbb4.hyslt.com/lz_yzmphp_DLL/GetDoMainArry.php";
	
	static final String IMGPATH = "D:/1.jpg";
	/*static final String USERNAME = "yanrian1989101";
	static final String PASSWORD = "1989101";*/
	
	static final String USERNAME = TrainConsts.USERNAME;//"zhangshumei";
	static final String PASSWORD = TrainConsts.PASSWORD;//"zhangshumei2010";
	
	static final int TIMES = 10; // 请求次数
	static final String CODE = "GBK2312";


	public static void main(String[] args) throws Exception {

		String domain = getDoMainArryAndValidUser();
		
		File f = new File(java.net.URLDecoder.decode(DMV2.class.getResource("1.jpg").getFile()));
		// File f = new File(IMGPATH);
		String fileName = MD5FileUtil.getFileMD5String(f) + ".jpg";
		System.out.println("upload=" + upload(domain, f));
		System.out.println("insertipData()=" + insertipData(domain, fileName));
		Thread.currentThread().sleep(3000L);
		for (int i = 0; i < TIMES; i++) {
			String result = getResult(domain, fileName);
		
			System.out.println("getResult()=" + getResult(domain, fileName));
			
			//===============   验证码报错API start   ===============
			String yzmid = result.split("|")[2];			
			String yzmresult = yzmErrorUpload(domain,yzmid);
			System.out.println("yzmErrorUpload:"+yzmresult);
			//===============   验证码报错API end     ===============
			
			if (result.indexOf("Error:TimeOut") > 0) {
				updateState(domain, fileName);
				break;
			}
			Thread.sleep(1000L);
		}

	}
	
	

	/**
	 * m
	 * 
	 * @param domain
	 *            分压域名
	 * @param f
	 *            文件
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String upload(String domain, File f) throws Exception {

		String code = "";
		HttpClient client = new HttpClient();
		String result = "";
		GetMethod getMethod;

		MultipartPostMethod filePost = new MultipartPostMethod("http://" + domain + "/lz_yzmphp_DLL/upload2.php");
		FilePart cbFile = new FilePart("upfile", f);
		cbFile.setContentType("image/pjpeg");
		filePost.addParameter("account", CodeGenerator.getStrMac());
		filePost.addParameter("key", CodeGenerator.getKey());
		filePost.addParameter("user", USERNAME);
		String fileName = MD5FileUtil.getFileMD5String(f) + ".jpg";
		filePost.addParameter("filename", fileName);
		filePost.addPart(cbFile);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		//System.out.println("filePost.getResponseBodyAsString()"+filePost.getResponseBodyAsString());
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		if ("上传失败".equals(result)) {
			throw new Exception(result);
		}
		return result;
	}
	
	/**
	 *  验证码错误上报
	 * @param yzm_id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String yzmErrorUpload(String domain,String yzm_id) throws Exception {
		String result = "";
		HttpClient client = new HttpClient();
		MultipartPostMethod filePost = new MultipartPostMethod("http://" + domain + "/api.php?mod=dmuser&act=yzm_error");
		filePost.addParameter("mac", CodeGenerator.getStrMac());
		filePost.addParameter("key", CodeGenerator.getKey());
		filePost.addParameter("yzm_id", yzm_id);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.executeMethod(filePost);
		result = getTranCode(filePost.getResponseBodyAsString(), CODE);
		if ("上传失败".equals(result)) {
			throw new Exception(result);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public static String getDoMainArryAndValidUser() throws Exception {
		HttpClient client = new HttpClient();
		String result = "";
		PostMethod postMethod;
		postMethod = new PostMethod(GETDOMAINARRY);
		postMethod.addParameter("user", USERNAME);
		postMethod.addParameter("pass", PASSWORD);
		postMethod.addParameter("account", CodeGenerator.getStrMac());
		postMethod.addParameter("key", CodeGenerator.getKey());
		postMethod.addParameter("submit", "%CC%ED+%BC%D3");
		client.executeMethod(postMethod);
		System.out.println(postMethod.getResponseCharSet());
		result = getTranCode(postMethod.getResponseBodyAsString(), "GBK");
		if (result.indexOf("ok") != -1) {
			result = result.substring(3, result.length());
		} else {
			throw new Exception(result);
		}
		return result;

	}

	public static String updateState(String domain, String fileName) throws Exception {

		HttpClient client = new HttpClient();
		String result = "";
		PostMethod postMethod;
		postMethod = new PostMethod("http://www.hyslt.com/lz_yzmphp_client/UpdateState.php");
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GB2312");
		postMethod.addParameter("imagepath", "http://" + domain + "/imagenew/" + USERNAME + "/" + fileName);
		postMethod.addParameter("status", "超时无效");
		postMethod.addParameter("account", CodeGenerator.getStrMac());
		postMethod.addParameter("key", CodeGenerator.getKey());
		postMethod.addParameter("submit", "%CC%ED+%BC%D3");
		client.executeMethod(postMethod);
		result = getTranCode(postMethod.getResponseBodyAsString(), CODE);
		return result;

	}

	public static String insertipData(String domain, String fileName) throws Exception {

		HttpClient client = new HttpClient();
		String result = "";
		PostMethod postMethod;
		postMethod = new PostMethod("http://" + domain + "/lz_yzmphp_DLL/InsertipData.php");

		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GB2312");
		postMethod.addParameter("user", USERNAME);
		postMethod.addParameter("pass", PASSWORD);
		postMethod.addParameter("status", "未识别");
		postMethod.addParameter("imagepath", "http://" + domain + "/imagenew/" + USERNAME + "/" + fileName);
		postMethod.addParameter("yzmtype", "0");
		postMethod.addParameter("yzmlength", "0");
		postMethod.addParameter("lenmax", "0");
		postMethod.addParameter("account", CodeGenerator.getStrMac());
		postMethod.addParameter("key", CodeGenerator.getKey());
		postMethod.addParameter("submit", "%CC%ED+%BC%D3");
		client.executeMethod(postMethod);
		// System.out.println(postMethod.getResponseCharSet());
		result = getTranCode(postMethod.getResponseBodyAsString(), CODE);
		return result;

	}

	public static String getResult(String domain, String fileName) throws Exception {

		HttpClient client = new HttpClient();
		String result = "";
		PostMethod postMethod;
		postMethod = new PostMethod("http://" + domain + "/lz_yzmphp_DLL/GetResult.php");
		postMethod.addParameter("imagepath", "http://" + domain + "/imagenew/" + USERNAME + "/" + fileName);
		postMethod.addParameter("server", domain);
		postMethod.addParameter("account", CodeGenerator.getStrMac());
		postMethod.addParameter("key", CodeGenerator.getKey());
		postMethod.addParameter("submit", "%CC%ED+%BC%D3");
		client.executeMethod(postMethod);
		//System.out.println("result----:"+postMethod.getResponseBodyAsString());
		result = getTranCode(postMethod.getResponseBodyAsString(), CODE);
		return result;
	}

	public static String getTranCode(String str) {
		if (System.getProperty("os.name").contains("Windows")) {
			byte[] data;
			try {
				data = str.getBytes("ISO8859-1");
				return new String(data, "GBK");
			} catch (UnsupportedEncodingException e) {
				return str;
			}
		} else {
			return str;
		}

	}

	public static String getTranCode(String str, String code) {
		if (System.getProperty("os.name").contains("Windows")) {
			byte[] data;
			try {
				data = str.getBytes("ISO8859-1");
				return new String(data, code);
			} catch (UnsupportedEncodingException e) {
				return str;
			}
		} else {
			return str;
		}

	}
}
