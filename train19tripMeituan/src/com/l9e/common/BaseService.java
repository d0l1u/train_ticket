package com.l9e.common;

import javax.servlet.http.HttpServletRequest;

public class BaseService {
	public String getParam(HttpServletRequest request,String paramName){
		return request.getParameter(paramName)==null?"": request.getParameter(paramName);
	}
}
