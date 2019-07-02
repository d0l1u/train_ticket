package com.l9e.weixin.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.l9e.util.MemcachedUtil;
import com.l9e.weixin.pojo.AccessToken;
import com.l9e.weixin.pojo.Menu;

/**
 * 公众平台通用接口工具类
 * 
 * @author liuyq
 * @date 2013-08-09
 */
public class WeChatUtil {

	// @Value("#{propertiesReader[appid]}")
	public final static String APPID;

	// @Value("#{propertiesReader[]}")
	public final static String SECRET;
	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String ACCESS_TOKEN_URL;
	// 菜单创建（POST） 限100（次/天）
	public final static String MENU_CREATE_URL;
	// 预定车票首页url
	public static String TRAINHOMEURL;
	//菜单删除url
	public final static String MENU_DELETE_URL; 
	public static String ACCESSTOKENFILE = "accessTokenFile.txt";

	private static final Logger logger = Logger.getLogger(WeChatUtil.class);
	static {
		Properties props = new Properties();
		String url = WeChatUtil.class.getClassLoader().getResource(
				"config.properties").getPath();
		// System.out.println(url);
		// url = url.substring(6);
		String empUrl = url.replace("%20", " ");// 如果你的文件路径中包含空格，是必定会报错的
		logger.info("The path of config.properties is " + empUrl);
		InputStream in = null;
		// in = WeChatUtil.class.getResourceAsStream("/config.properties");

		try {
			in = new BufferedInputStream(new FileInputStream(empUrl));
			props.load(in);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		MENU_DELETE_URL = (String) props.get("menu_delete_url");
		APPID = (String) props.get("appid");
		SECRET = (String) props.get("secret");
		ACCESS_TOKEN_URL = (String) props.get("access_token_url") + "&appid="
				+ APPID + "&secret=" + SECRET;
		MENU_CREATE_URL = (String) props.get("menu_create_url");
		TRAINHOMEURL = (String) props.get("train_home_url");
		// System.out.println(APPID);
	}

	private static Logger log = Logger.getLogger(WeChatUtil.class);

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		accessToken = (AccessToken) MemcachedUtil.getInstance().getAttribute("weixin_access_token");
		if (accessToken != null) {
			return accessToken;
		}
		logger.info("the url of getting access_token is " + ACCESS_TOKEN_URL);

		String requestUrl = ACCESS_TOKEN_URL;// .replace("APPID",
		// appid).replace(
		// "APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
				MemcachedUtil.getInstance().setAttribute("weixin_access_token", accessToken, jsonObject.getInt("expires_in")* 980);
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				log.error("获取token失败 errcode:" + jsonObject.getInt("errcode")
						+ ",  errmsg:" + jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败 errcode:" + jsonObject.getInt("errcode")
						+ ",  errmsg:" + jsonObject.getString("errmsg"));
			}
		}

		return result;
	}
	
	public static int deleteMenu(String accessToken) {
		int result = 0;
		
		// 拼装删除菜单的url
		String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = httpRequest(url, "get", "");

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败 errcode:" + jsonObject.getInt("errcode")
						+ ",  errmsg:" + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}

	public static boolean saveToFile(AccessToken accessToken) {
		File tokenFile = new File(ACCESSTOKENFILE);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(tokenFile));
			bw.write(accessToken.getToken()
					+ String.valueOf(accessToken.getExpiresIn())
					+ String.valueOf(new Date().getTime()));
			bw.close();
		} catch (IOException e) {
			logger.info("写token文件时发生异常！");
			return false;
		}
		return true;
	}

	/*
	 * 从文件中读取access_token
	 */
	public static AccessToken readFromFile() {
		AccessToken accessToken = null;
		File tokenFile = new File(ACCESSTOKENFILE);
		if (!tokenFile.exists()) {
			logger.info("the token file does not exists!");
			return null;
		}
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(tokenFile));
			String tempString = null;
			int line = 1;
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				sb.append(tempString);
				line++;
			}
		} catch (FileNotFoundException e) {
			logger.info("the token file does not exists!");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		String accessTokenString = sb.toString().trim();
		String[] elements = accessTokenString.split(",");
		String token = elements[0];
		String expires = elements[1];
		String time = elements[2];
		if (token == null || token.length() == 0 || expires == null
				|| expires.length() == 0) {
			return null;
		}
		accessToken.setToken(token);
		long saveTime = Long.parseLong(time);
		Date curTime = new Date();
		int expiresTime = Integer.parseInt(expires);
		long currentTime = curTime.getTime();
		if (saveTime + expiresTime * 1000 >= currentTime) {
			return null;
		}
		accessToken.setExpiresIn(expiresTime);
		return accessToken;
	}
}