package com.l9e.common;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.l9e.transaction.service.CommonService;

public class BaseController {
	
	@Resource
	protected CommonService commonService;
	
	/**
	 * 获取列车类型中文名称（eg 高铁 动车..）
	 * @param train_no
	 * @return
	 */
	public String getTrainTypeCn(String train_no){
		/*G 高铁 D 动车 K 快车 T 特快 Z 直达 C 城际
		数字 普快 L 临客 Y 旅游 剩下的就写其他*/
		String type = "";
		if(!StringUtils.isEmpty(train_no)){
			String pre = train_no.substring(0, 1);
			if("G".equalsIgnoreCase(pre)){
				type = "高铁";
			}else if("D".equalsIgnoreCase(pre)){
				type = "动车";
			}else if("K".equalsIgnoreCase(pre)){
				type = "快车";
			}else if("T".equalsIgnoreCase(pre)){
				type = "特快";
			}else if("Z".equalsIgnoreCase(pre)){
				type = "直达";
			}else if("C".equalsIgnoreCase(pre)){
				type = "城际";
			}else if("L".equalsIgnoreCase(pre)){
				type = "临客";
			}else if("Y".equalsIgnoreCase(pre)){
				type = "旅游";
			}else if(pre.charAt(0) >= '0' && pre.charAt(0) <= '9'){
				type = "普快";
			}else{
				type = "其他";
			}
		}
		return type;
	}
	
	/**
	 * request.getParameter
	 * @param param
	 * @return
	 */
	public String getParam(HttpServletRequest request, String param){
		return request.getParameter(param) == null ? "" : request.getParameter(param).toString().trim();
	}
	
	/**
	 * null转空
	 * @param str
	 * @return
	 */
	public String nullToEmpty(String str){
		if(str==null || "".equals(str.trim()) || "null".equals(str.trim())){
			return "";
		}else{
			return str.trim();
		}
	}
	
	/**
	 * 空转0
	 * @param str
	 * @return
	 */
	public String emptyToZeroStr(String str){
		if(str==null || "".equals(str.trim()) || "null".equals(str.trim())){
			return "0";
		}else{
			return str.trim();
		}
	}
	
	/**
	 * 空转0
	 * @param str
	 * @return
	 */
	public int emptyToZero(String str){
		if(str==null || "".equals(str.trim()) || "null".equals(str.trim())){
			return 0;
		}else{
			return Integer.parseInt(str.trim());
		}
	}
	
	/**
	 * 值写入response
	 * @param response
	 * @param StatusStr
	 */
	public void write2Response(HttpServletResponse response, String StatusStr){
		try {
			response.getWriter().write(StatusStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
}
