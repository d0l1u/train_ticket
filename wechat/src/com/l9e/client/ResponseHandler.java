package com.l9e.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.l9e.weixin.util.MD5Util;
import com.l9e.weixin.util.Sha11Util;
import com.l9e.weixin.util.TenpayUtil;
import com.l9e.weixin.util.XMLUtil;

/**
 * 微信支付服务器签名支付请求应答类 api说明： getKey()/setKey(),获取/设置密钥
 * getParameter()/setParameter(),获取/设置参数值 getAllParameters(),获取所有参数
 * isTenpaySign(),是否财付通签名,true:是 false:否 getDebugInfo(),获取debug信息
 */
public class ResponseHandler {
	private static final Logger logger = Logger
			.getLogger(ResponseHandler.class);
	private String appkey = null;

	/** 密钥 */
	private String key;

	/** 应答的参数 */
	private SortedMap<String, String> parameters;

	/** debug信息 */
	private String debugInfo;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private String uriEncoding;

	private Hashtable<String, String> xmlMap;

	private String k;

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getAppkey() {
		return appkey;
	}

	/**
	 * 构造函数
	 * 
	 * @param request
	 * @param response
	 */
	public ResponseHandler(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;

		this.key = "";
		this.parameters = new TreeMap<String, String>();
		this.debugInfo = "";

		this.uriEncoding = "";
		this.xmlMap = new Hashtable<String, String>();
		Map<String, Object> m = this.request.getParameterMap();
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String k = (String) it.next();
			String v = ((String[]) m.get(k))[0];
			this.setParameter(k, v);
		}
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = request.getReader();
			String line;

			while ((line = in.readLine()) != null) {
				sb.append(line.trim());
			}
		} catch (Exception e) {
			logger.error("读取支付结果通知xml发生异常：" + e);
		}
		try {
			logger.info("支付结果通知xml ::: " + sb.toString());
			this.doParse(sb.toString());
			logger.info("支付结果通知xml ::: " + this.xmlMap.toString());
		} catch (JDOMException e) {
			logger.error("解析支付结果通知xml发生异常：" + e);
		} catch (IOException e) {
			logger.error("解析支付结果通知xml发生异常：" + e);
		}
	}

	/**
	 * 获取密钥
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 设置密钥
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取参数值
	 * 
	 * @param parameter
	 *            参数名称
	 * @return String
	 */
	public String getParameter(String parameter) {
		String s = (String) this.parameters.get(parameter);
		return (null == s) ? "" : s;
	}

	/**
	 * 设置参数值
	 * 
	 * @param parameter
	 *            参数名称
	 * @param parameterValue
	 *            参数值
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if (null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}

	/**
	 * 返回所有的参数
	 * 
	 * @return SortedMap
	 */
	public SortedMap<String, String> getAllParameters() {
		return this.parameters;
	}

	public void doParse(String xmlContent) throws JDOMException, IOException {
		this.xmlMap.clear();
		// 解析xml,得到map
		Map<String, String> m = XMLUtil.doXMLParse(xmlContent);

		// 设置参数
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			xmlMap.put(k, v);
		}
	}

	/**
	 * 是否财付通签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * 
	 * @return boolean
	 */
	public boolean isValidSign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}

		sb.append("key=" + this.getKey());

		// 算出摘要
		String enc = TenpayUtil.getCharacterEncoding(this.request,
				this.response);
		logger.info("签名参数：" + sb.toString() + "，签名格式:" + enc);
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();

		String ValidSign = this.getParameter("sign").toLowerCase();
		logger.info("sign：" + sign + " ::::: validSign" + ValidSign);
		// debug信息
		this.setDebugInfo(sb.toString() + " => sign:" + sign + " ValidSign:"
				+ ValidSign);
		logger.info("ValidSign.equals(sign):::" + ValidSign.equals(sign));
		return ValidSign.equals(sign);
	}

	/**
	 * 判断微信签名
	 */
	public boolean isWXsign() {
		logger.info("开始判断微信签名");
		StringBuffer sb = new StringBuffer();
		String keys = "";
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		SortedMap<String, String> signMap = new TreeMap<String, String>();
		Set es = this.xmlMap.entrySet();
		Iterator it = es.iterator();
		logger.info("判断微信签名：" + xmlMap.toString());
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			logger.info("判断微信签名k:" + k + "   , v: " + v);
			if (!k.equals("SignMethod") && !k.equals("AppSignature")) {
				signParams.put(k.toLowerCase(), v);
				// sb.append(k + "=" + v + "&");
			}
		}
		signParams.put("appkey", this.appkey);
		// ArrayList akeys = new ArrayList();
		// akeys.sort();

		Set<String> key = signParams.keySet();
		System.out.println("before sort:::" + key.toString());
		List<String> keyList = new ArrayList<String>(key);
		Collections.sort(keyList, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		// it = es.iterator();
		sb = new StringBuffer();
		logger.info("key:::" + key.toString());
		for (int i = 0; i < keyList.size(); i++) {
			String s = keyList.get(i);
			logger.info(s + "=" + signParams.get(s));
			if (sb.length() == 0) {
				sb.append(s.toLowerCase() + "=" + signParams.get(s));
			} else {
				sb.append("&" + s.toLowerCase() + "=" + signParams.get(s));
			}
		}

		logger.info("【判断微信签名】签名之前的字符串为：" + sb.toString());
		String sign = Sha11Util.getSha1(sb.toString()).toString().toLowerCase();

		this.setDebugInfo(sb.toString() + " => SHA1 sign:" + sign);

		return sign.equals(xmlMap.get("AppSignature"));

	}

	
	// 判断微信维权签名
	public boolean isWXsignfeedback() {

		StringBuffer sb = new StringBuffer();
		Hashtable signMap = new Hashtable();
		Set es = this.xmlMap.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!k.equals("SignMethod") && !k.equals("AppSignature")) {

				sb.append(k + "=" + v + "&");
			}
		}
		signMap.put("appkey", this.appkey);

		// ArrayList akeys = new ArrayList();
		// akeys.Sort();
		while (it.hasNext()) {
			String v = k;
			if (sb.length() == 0) {
				sb.append(k.toLowerCase() + "=" + v);
			} else {
				sb.append("&" + k.toLowerCase() + "=" + v);
			}
		}

		String sign = Sha11Util.getSha1(sb.toString()).toString().toLowerCase();

		this.setDebugInfo(sb.toString() + " => SHA1 sign:" + sign);

		return sign.equals(xmlMap.get("AppSignature"));
	}

	/**
	 * 返回处理结果给财付通服务器。
	 * 
	 * @param msg
	 *            Success or fail
	 * @throws IOException
	 */
	public void sendToCFT(String msg) throws IOException {
		String strHtml = msg;
		PrintWriter out = this.getHttpServletResponse().getWriter();
		out.println(strHtml);
		out.flush();
		out.close();

	}

	/**
	 * 获取uri编码
	 * 
	 * @return String
	 */
	public String getUriEncoding() {
		return uriEncoding;
	}

	/**
	 * 设置uri编码
	 * 
	 * @param uriEncoding
	 * @throws UnsupportedEncodingException
	 */
	public void setUriEncoding(String uriEncoding)
			throws UnsupportedEncodingException {
		if (!"".equals(uriEncoding.trim())) {
			this.uriEncoding = uriEncoding;

			// 编码转换
			String enc = TenpayUtil.getCharacterEncoding(request, response);
			Iterator it = this.parameters.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = this.getParameter(k);
				v = new String(v.getBytes(uriEncoding.trim()), enc);
				this.setParameter(k, v);
			}
		}
	}

	/**
	 * 获取debug信息
	 */
	public String getDebugInfo() {
		return debugInfo;
	}

	/**
	 * 设置debug信息
	 */
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}

	protected HttpServletRequest getHttpServletRequest() {
		return this.request;
	}

	protected HttpServletResponse getHttpServletResponse() {
		return this.response;
	}

}