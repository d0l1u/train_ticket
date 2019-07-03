package com.cqz.tc;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.util.ConfigUtil;
@Deprecated
public class TcCodeUtil {
	private static final Logger logger = Logger.getLogger(TcCodeUtil.class);
	
	public static final String CODE_URL = ConfigUtil.getValue("code_url");
	public static final String REPORT_ERROR_URL = ConfigUtil.getValue("report_error_url");
	public static final String U_NAME = "19e_x8Fd3";
	public static final String KEY = "7Jw23Q2LqPb4RHxkz9tyN6bMbA3qK33M";
	public static List<Long> times = new ArrayList<Long>();

	public static void main(String[] args) {
		TcBase64 base64 = new TcBase64();
		File f = new File("d:/code");
		for (File target : f.listFiles()) {

			logger.info(target.getName());
			String result = base64.file2Base64(target);
			result.replaceAll("\\r\\n", "");

			try {
				captchaDecode(U_NAME, KEY, result);
				// String cid = "dcd5990b-40a5-9ae2-80cd-8d83210ac6de";
				// String desc = "测试反馈";
				// captchaReportError(U_NAME, KEY, cid, desc);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		logger.info("验证码识别数量：" + times.size());
		long sum = 0;
		for (Long l : times) {
			sum += l;
		}
		long avg = sum / times.size();
		logger.info("平均耗时：" + avg);

	}

	
	//同程验证码请求信息
	public static String captchaDecode(String uname, String key, String base64)
			throws JsonGenerationException, JsonMappingException, IOException {
		String cid = "";
		// String timestamp = DateUtil.dateToString(new Date(),
		// DateUtil.DATE_FMT2);
		long timestamp = System.currentTimeMillis() / 1000L;
		String signature = TcMd5Util.md5_32(
				uname + cid + timestamp
				+ TcMd5Util.md5_32(timestamp + key, "UTF-8")
				.toLowerCase(), "UTF-8").toLowerCase();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uname", uname);
		param.put("timestamp", timestamp);
		param.put("signature", signature);
		param.put("image", base64);

		String postJson = "";
		ObjectMapper mapper = new ObjectMapper();
		postJson = mapper.writeValueAsString(param);
		postJson = URLEncoder.encode(postJson, "UTF-8");

		// logger.info(postJson);
		long start = System.currentTimeMillis();
		String result = TcHttpUtil.sendByPost(CODE_URL, "data=" + postJson, "UTF-8", 6000, 6000);
		
		long end = System.currentTimeMillis();

//		logger.info(result);
		times.add((end - start));
//		logger.info("耗时：" + (end - start));
		return result;
	}

	//同程验证码识别反馈信息
	public static String captchaReportError(String uname, String key, String cid,
			String desc) throws JsonGenerationException, JsonMappingException,
			IOException {
		long timestamp = System.currentTimeMillis() / 1000L;
		String signature = TcMd5Util.md5_32(uname
						+ cid
						+ timestamp
						+ TcMd5Util.md5_32(timestamp + key, "UTF-8")
								.toLowerCase(), "UTF-8").toLowerCase();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uname", uname);
		param.put("timestamp", timestamp);
		param.put("signature", signature);
		param.put("cid", cid);
		param.put("desc", desc);

		String postJson = "";
		ObjectMapper mapper = new ObjectMapper();
		postJson = mapper.writeValueAsString(param);
		postJson = URLEncoder.encode(postJson, "UTF-8");

		// logger.info(postJson);
		long start = System.currentTimeMillis();
		String result = TcHttpUtil.sendByPost(REPORT_ERROR_URL, "data="
				+ postJson, "UTF-8", 30000, 30000);
		long end = System.currentTimeMillis();

//		logger.info(result);
//		logger.info("耗时：" + (end - start));
		return result;
	}
}
