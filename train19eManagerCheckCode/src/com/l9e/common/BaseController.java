package com.l9e.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
	
	/**
	 * request.getParameter
	 * @param param
	 * @return
	 */
	public String getParam(HttpServletRequest request, String param){
		return request.getParameter(param) == null ? "" : request.getParameter(param).toString().trim();
	}
	
	/**
	 * request.getParameterValues
	 * @param request
	 * @param param
	 * @return
	 */
	public List<String> getParamToList(HttpServletRequest request, String param){
		String[] array = request.getParameterValues(param);
		return array == null ? new ArrayList<String>() : Arrays.asList(array);
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
	public void writeN2Response(HttpServletResponse response, String StatusStr){
		try {
			response.getWriter().write(StatusStr);
			response.getWriter().flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
}
