package com.l9e.train.supplier;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.l9e.train.supplier.po.OrderRefund;

public class RefundUtil {

	private Logger logger = Logger.getLogger(this.getClass());
	private String verifycode;
	private Map<String, String> key;

	public String verifyCode() {
		return verifycode;
	}

	public RefundUtil() {
		// TODO Auto-generated constructor stub
		key = new HashMap<String, String>();
		// name errorcode
		key.put("orderid", "0011");
		key.put("cpid", "0012");
		key.put("trainno", "0013");
		key.put("fromstation", "0014");
		key.put("arrivestation", "0015");
		key.put("fromtime", "0016");
		key.put("traveltime", "0017");
		key.put("buymoney", "0018");
		key.put("refundmoney", "0019");
		key.put("username", "0021");
		key.put("tickettype", "0020");
		key.put("userids", "0022");
		key.put("seattype", "0023");
		key.put("seatno", "0024");
		key.put("outticketbillno", "0025");
		key.put("outtickettime", "0026");
		key.put("altertrainno", "0027");
		key.put("alterseattype", "0028");
		key.put("altertraveltime", "0029");
		key.put("channel", "0030");
		key.put("backurl", "0031");
		key.put("refundtype", "0032");
	}

	public OrderRefund verifyRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Enumeration<String> names = request.getParameterNames();

		OrderRefund orderRefund = new OrderRefund();

		while (names.hasMoreElements()) {

			String nameStr = (String) names.nextElement();
			String valueStr = request.getParameter(nameStr);

			logger.info("name=" + nameStr + " value=" + valueStr);

			try {
				BeanUtils.setProperty(orderRefund, nameStr, valueStr);

			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				logger.warn("BeanUtils set value error " + e);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				logger.warn("BeanUtils set value error " + e);
			} finally {

			}
		}

		return orderRefund;
	}
}
