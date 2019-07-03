package com.l9e.train.supplier;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.l9e.train.supplier.po.Order;


public class OrderUtil {

	private Logger logger=Logger.getLogger(this.getClass());
	private String verifycode;
	private Map<String, String> key;
	
	
	public String verifyCode(){
		
		return verifycode;
	}
	
	public OrderUtil() {
		// TODO Auto-generated constructor stub
		key = new HashMap<String, String>();
		//name errorcode
		key.put("orderid", "0011");
		key.put("ordername", "0012");
		key.put("paymoney", "0013");
		key.put("trainno", "0014");
		key.put("fromcity", "0015");
		key.put("tocity", "0016");
		key.put("fromtime", "0017");
		key.put("totime", "0018");
		key.put("traveltime", "0019");
		key.put("seattype", "0021");
		key.put("seattrains", "0020");
		key.put("outtickettype", "0022");
		key.put("channel", "0023");
		key.put("extseattype", "0024");
		key.put("backurl", "0025");
		key.put("ext", "0026");
		key.put("level", "0027");
		key.put("ispay", "0028");
		key.put("manualorder", "0029");
		key.put("waitfororder", "0030");
		verifycode = "0000";
	}

	public Order verifyRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Enumeration<String> names =  request.getParameterNames();
		
		Order supOrder = new Order();
		
		
		while (names.hasMoreElements()) {
			
			String  nameStr= (String) names.nextElement();
			String valueStr = request.getParameter(nameStr);
			
			logger.info("name="+nameStr+" value="+valueStr);
			
			
			
			try {
				
				BeanUtils.setProperty(supOrder, nameStr, valueStr);
				
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				logger.warn("BeanUtils set value error "+e);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				logger.warn("BeanUtils set value error "+e);
			}finally{
				
			}
			
		}		
		
		return supOrder;
	}
}
