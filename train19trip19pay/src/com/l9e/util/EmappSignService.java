package com.l9e.util;

/**
 * 
 * <p>应用系统动态密钥安全操作类</p>
 *
 * @author WeiJun
 * @version V2.0
 * 2009-09-04
 *
 */

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jiexun.iface.util.StringUtil;


public class EmappSignService {
	private static final Logger logger = Logger.getLogger(EmappSignService.class);
	
	/**
	 * 对接口请求进行验证
	 * 
	 * @param hm
	 * @param hmac
	 *            商家请求的摘要
	 * @param appInitKey
	 *            商家静态KEY
	 * @return true 验证成功 false 验证失败
	 */
	public static boolean checkReqSign(Map<String, String> hm, String hmac,
			String appInitKey) {
		try {
			if (hm.isEmpty()) {
				return false;
			}

			Object[] key = hm.keySet().toArray();
			Arrays.sort(key);
			StringBuffer sBuf = new StringBuffer();
			for (int i = 0; i < key.length; i++) {
				sBuf.append(key[i] + "=" + hm.get(key[i]) + "&");
			}
			String sString = sBuf.substring(0, sBuf.length() - 1);// .toString();
			String signString = KeyedDigestMD5.getKeyedDigest(sString,
					appInitKey);
			if (signString != null && signString.equals(hmac)) {
				// 校验成功
				return true;
			} else {
				/**
				 * 兼容通过utf加密的签名
				 */
				signString = KeyedDigestMD5.getKeyedDigestUTF8(sString,
						appInitKey);
				if (signString != null && signString.equals(hmac)) {
					return true;
				}
				logger.error("验证的加密串失败|" + sString + "|key=" + appInitKey
						+ "|hmac=" + signString);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 对接口响应参数进行加密生成
	 * 
	 * @param resParam
	 * @param appInitKey
	 *            商家静态KEY
	 * @return 接口输出串
	 */
	public static String creatResSign(String[][] resParam, String appInitKey) {
		try {
			// 对参数名按照ASCII升序排序
			Arrays.sort(resParam, new Comparator<Object>() {
				public int compare(final Object o1, final Object o2) {
					return ((String[]) o1)[0].compareTo(((String[]) o2)[0]);
				}
			});

			StringBuffer res = new StringBuffer(128);

			String errcode = "11111";
			for (int m = 0; m < resParam.length; m++) {
				res.append(resParam[m][0] + "=" + (resParam[m][1]==null?"":resParam[m][1]) + "&");
				if ("errcode".equals(resParam[m][0])) {
					errcode = resParam[m][1];
				}
			}
			// String sStr = res.toString();
			String rStr = res.substring(0, res.length() - 1);

			// 分类记录日志
			if ("0".equals(errcode)) {
				logger.info("接口返回串|:0" + rStr);
			} else if ("11111".equals(errcode)) {
				logger.error("接口返回串|1:" + rStr);
			} else {
				logger.warn("接口返回串|" + rStr);
			}

			if (StringUtil.isEmpty(appInitKey)) {
				return rStr + "&hmac="
						+ KeyedDigestMD5.getKeyedDigest(rStr, "");
			}

			return rStr + "&hmac="
					+ KeyedDigestMD5.getKeyedDigest(rStr, appInitKey);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 输入二维数组，生成参数串，生成参数串
	 * 
	 * @param resParam
	 * @param appInitKey
	 *            商家静态KEY
	 * @return 接口输出串
	 */
	public static String creatResParam(String[][] resParam) {
		try {
			// 对参数名按照ASCII升序排序
			Arrays.sort(resParam, new Comparator<Object>() {
				public int compare(final Object o1, final Object o2) {
					return ((String[]) o1)[0].compareTo(((String[]) o2)[0]);
				}
			});

			StringBuffer res = new StringBuffer(128);

			for (int m = 0; m < resParam.length; m++) {
				res.append(resParam[m][0] + "=" + (resParam[m][1]==null?"":resParam[m][1]) + "&");
			}
			// String sStr = res.toString();
			String rStr = res.substring(0, res.length() - 1);
			return rStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static void main(String[] args) {
		//url参数串
		String[][] num=new String[6][7];//定义一个String类型的2维数组
		String appInitKey="nihao";
		num[0][0]="extend1";
		num[0][1]="nihao";
		num[1][0]="extend2";
		num[1][1]="wohao";
		num[2][0]="mxdate";
		num[2][1]="2014-05-07 15:01:30";
		num[3][0]="mxorderid";
		num[3][1]="pay201402155893458";
		num[4][0]="orderid";
		num[4][1]="pay2014568745842";
		num[5][0]="status";
		num[5][1]="11";
		System.out.println("最后返回的信息为:"+EmappSignService.creatResSign(num, appInitKey));
		
		//md5加密
		StringBuffer signData=new StringBuffer();		
		signData.append("extend1="+"nihao")
				.append("&extend2="+"wohao")
				.append("&mxdate="+"2014-05-07 15:01:30")
				.append("&mxorderid="+"pay201402155893458")
				.append("&orderid="+"pay2014568745842")
				.append("&status="+"11");
		System.out.println(KeyedDigestMD5.getKeyedDigest(signData.toString(), "nihao"));
		
		
		//测试验证签名
		Map<String,String> map=new HashMap<String,String>();
		map=PayUtil.getValueMap(signData.toString());
		
		
		boolean flag=false;
		flag=EmappSignService.checkReqSign(map, KeyedDigestMD5.getKeyedDigest(signData.toString(), "nihao"), "nihao");
		System.out.println(flag);
	}

}
