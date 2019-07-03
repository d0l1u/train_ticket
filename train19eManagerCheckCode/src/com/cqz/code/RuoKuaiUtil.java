package com.cqz.code;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class RuoKuaiUtil {
	private static final Logger logger = Logger.getLogger(RuoKuaiUtil.class);
	
	public static final String RK_NAME = "hcpkuyou";//若快账号
	public static final String RK_PWD = "hcpkuyou1";//若快密码
	public static final String RK_TYPEID = "6005";//若快题目类型:必须原始尺寸图片，返回坐标，格式为(x,y)，18图、8图通用类型，但会降低识别率
	public static final String RK_TYPEID_8 = "6113";//8图：返回1-8数字 
	public static final String RK_TYPEID_18 = "6115";//18图：返回1-18数字（会有，隔开） 
	public static final String RK_TIMEOUT = "30000";//若快超时时间
	public static final String RK_SOFTID = "47929";//若快软件ID
	public static final String RK_SOFTKEY = "31f8b15d5ad14ad994eb26ed4125372c";//若快软件key
	
	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "192.168.65.126");
		System.setProperty("http.proxyPort", "3128");
		String path = "d:/code/PIC1512040911401007.GIF";
		String result = getResultByPost(path, "18");
		System.out.println(result);
	}
	
	/**
	 * 上传题目图片返回结果
	 * @param username 用户名
	 * @param password 密码
	 * @param typeid 题目类型
	 * @param timeout 任务超时时间
	 * @param softid 软件ID
	 * @param softkey 软件KEY
	 * @param filePath 题目截图或原始图二进制数据路径
	 * @throws IOException
	 * {"Result":"243,116","Id":"194df09c-4b39-4288-aaef-54850b535fb2"}
	 */
	public static String getResultByPost(String filePath, String pic_type) {
		String result = "";
		String type_id = RK_TYPEID;
		if("8".equals(pic_type)){
			type_id = RK_TYPEID_8;
		}else if("18".equals(pic_type)){
			type_id = RK_TYPEID_18;
		}
		String param = String.format(
						"username=%s&password=%s&typeid=%s&timeout=%s&softid=%s&softkey=%s",
						RK_NAME, RK_PWD, type_id, RK_TIMEOUT, RK_SOFTID, RK_SOFTKEY);
		try {
			File f = new File(filePath);
			if (null != f) {
				int size = (int) f.length();
				byte[] data = new byte[size];
				FileInputStream fis = new FileInputStream(f);
				fis.read(data, 0, size);
				if (null != fis)
					fis.close();

				if (data.length > 0)
					result = RuoKuaiUtil.httpPostImage(
							"http://api.ruokuai.com/create.json", param, data);
			}
		} catch (Exception e) {
			logger.info("[ruokuai] upload pic get result exception " + e);
			e.printStackTrace();
		}

		return result;
	}
	
	//报错
	public static String sendError(String id) {
		// TODO Auto-generated method stub
		String ret = "";
		String param = String.format(
				"username=%s&password=%s&softid=%s&softkey=%s&id=%s", RK_NAME,
				RK_PWD, RK_SOFTID, RK_SOFTKEY, id);
		try {
			ret = httpRequestData("http://api.ruokuai.com/reporterror.json",
					param);
		} catch (Exception e) {
			logger.info("[ruokuai] send error pic exception " + e);
			e.printStackTrace();
		}
		return ret;
	}
	
	private static String httpRequestData(String url, String param)
			throws IOException {
		// TODO Auto-generated method stub
		URL u;
		HttpURLConnection con = null;
		OutputStreamWriter osw;
		StringBuffer buffer = new StringBuffer();

		u = new URL(url);
		con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
		osw.write(param);
		osw.flush();
		osw.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con
				.getInputStream(), "UTF-8"));
		String temp;
		while ((temp = br.readLine()) != null) {
			buffer.append(temp);
			buffer.append("\n");
		}

		return buffer.toString();
	}

	public static String Post() {
		String ret = null;
		try {
			URL u = new URL("http://api.ruokuai.com/info.json");

			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.addRequestProperty("encoding", "UTF-8");
			con.setRequestMethod("POST");
			// 写入
			OutputStream out = con.getOutputStream();
			OutputStreamWriter ow = new OutputStreamWriter(out);// 将要写入流中的字符编码成字节
			BufferedWriter bw = new BufferedWriter(ow);// 写入字符输出流
			bw.write("username=a123785&password=123456");
			bw.flush();// 刷新该流的缓冲

			// 读取
			InputStream in = con.getInputStream();
			InputStreamReader iread = new InputStreamReader(in);// 读取字节并将其解码为字符
			BufferedReader read = new BufferedReader(iread);// 从字符输入流中读取文本

			StringBuffer buf = new StringBuffer();
			String lin = null;
			// 逐行读取
			while (((lin = read.readLine()) != null)) {
				buf.append(lin);
			}
			ret = buf.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// DOM
		try {
			//获取DocumentBuilderFactory的新实例。
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// 使用当前配置的参数创建一个新的DocumentBuilder实例。
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(ret)));
			// 允许直接访问文档的文档元素的子节点。
			Element root = doc.getDocumentElement();
			// 返回具有带给定值的 ID 属性的Element。
			NodeList h = root.getElementsByTagName("Score");
			// 节点文本内容
			System.out.println(h.item(0).getTextContent());

			// System.out.println("aa"+his);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 字符串MD5加密
	 * @param s 原始字符串
	 * @return 加密后字符串
	 */
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询用户信息
	 */
	public static String getInFo(String username, String password) {
		String ret = "";
		String param = String.format("username=%s&password=%s", username, password);
		try {
			ret = httpRequestData("http://api.ruokuai.com/info.json", param);
		} catch (Exception e) {
			ret = "未知问题";
		}
		return ret;
	}

	/**
	 * 答题
	 * @param url 请求URL，不带参数 如：http://api.ruokuai.com/register.json
	 * @param param 请求参数，如：username=test&password=1
	 * @param data 图片二进制流
	 * @return 平台返回结果XML样式
	 * @throws IOException
	 */
	public static String httpPostImage(String url, String param, byte[] data)
			throws IOException {
		long time = (new Date()).getTime();
		URL u = null;
		HttpURLConnection con = null;
		String boundary = "----------" + MD5(String.valueOf(time));
		String boundarybytesString = "\r\n--" + boundary + "\r\n";
		OutputStream out = null;

		u = new URL(url);

		con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("POST");
		// con.setReadTimeout(95000);
		con.setConnectTimeout(95000); // 此值与timeout参数相关，如果timeout参数是90秒，这里就是95000，建议多5秒
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(true);
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

		out = con.getOutputStream();

		for (String paramValue : param.split("[&]")) {
			out.write(boundarybytesString.getBytes("UTF-8"));
			String paramString = "Content-Disposition: form-data; name=\""
					+ paramValue.split("[=]")[0] + "\"\r\n\r\n"
					+ paramValue.split("[=]")[1];
			out.write(paramString.getBytes("UTF-8"));
		}
		out.write(boundarybytesString.getBytes("UTF-8"));

		String paramString = "Content-Disposition: form-data; name=\"image\"; filename=\""
				+ "sample.gif" + "\"\r\nContent-Type: image/gif\r\n\r\n";
		out.write(paramString.getBytes("UTF-8"));

		out.write(data);

		String tailer = "\r\n--" + boundary + "--\r\n";
		out.write(tailer.getBytes("UTF-8"));

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(con
				.getInputStream(), "UTF-8"));
		String temp;
		while ((temp = br.readLine()) != null) {
			buffer.append(temp);
			buffer.append("\n");
		}
		return buffer.toString();
	}

	

	/**
	 * URL验证
	 * 
	 * @param softId
	 * @param softKey
	 * @param typeid
	 * @param username
	 * @param password
	 * @param url
	 * @return
	 */
	public static String createByUrl(String softId, String softKey,
			String typeid, String username, String password, String url) {
		// TODO Auto-generated method stub
		String param = String.format(
						"username=%s&password=%s&typeid=%s&timeout=%s&softid=%s&softkey=%s",
						username, password, typeid, "90", softId, softKey);
		ByteArrayOutputStream baos = null;
		String ret;

		try {
			URL u = new URL(url);
			BufferedImage image = ImageIO.read(u);

			baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			byte[] data = baos.toByteArray();
			baos.close();
			ret = httpPostImage("http://api.ruokuai.com/create.json", param, data);
		} catch (Exception e) {
			// TODO: handle exception
			return "未知错误";
		}

		return ret;
	}

	

	/**
	 * 注册
	 * 
	 * @param username
	 * @param password
	 * @param email
	 * @return
	 */

	public static String zhuce(String username, String password, String email) {
		// TODO Auto-generated method stub
		String ret = "";
		String param = String.format("username=%s&password=%s&email=%s",
				username, password, email);
		try {
			ret = httpRequestData("http://api.ruokuai.com/register.json", param);
		} catch (Exception e) {
			// TODO: handle exception
			return "未知错误";
		}
		return ret;
	}

	/**
	 * 充值
	 * 
	 * @param username
	 * @param kid
	 * @param key
	 * @return
	 */

	public static String chongzhi(String username, String kid, String key) {
		String ret = "";
		String param = String.format("username=%s&password=%s&id=%s", username,
				key, kid);
		try {
			ret = httpRequestData("http://api.ruokuai.com/recharge.json", param);
		} catch (Exception e) {
			// TODO: handle exception
			return "未知错误";
		}

		return ret;
	}
}
