package com.l9e.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.l9e.transaction.vo.Phone;


public class PhoneUtil {
	private Logger logger=Logger.getLogger(this.getClass());
	
	public Phone verifyRequest(HttpServletRequest request) {
		Enumeration<String> names =  request.getParameterNames();
		
		Phone phone = new Phone();
		while (names.hasMoreElements()) {
			
			String  nameStr= (String) names.nextElement();
			String valueStr = request.getParameter(nameStr);
			
			logger.info("name="+nameStr+" value="+valueStr);
			
			try {
				BeanUtils.setProperty(phone, nameStr, valueStr);
			} catch (Exception e) {
				logger.warn("BeanUtils set value error "+e);
			}finally{
				
			}
			
		}		
		
		return phone;
	}
}
