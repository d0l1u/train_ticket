package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.dao.PhonePlatDao;
import com.l9e.transaction.vo.Phone;
import com.l9e.util.PhoneUtil;

@Controller
@RequestMapping("/main")
public class MainController extends BaseController {

	private static final Logger logger = Logger.getLogger(MainController.class);

	@Resource
	private PhonePlatDao phonePlatDao;

	/**
	 * 接口主入口
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/insertPhone.do")
	public void insert(HttpServletRequest request, HttpServletResponse response) {
		PhoneUtil util = new PhoneUtil();
		Phone supOrder=util.verifyRequest(request);
		logger.info("insert phone:"+supOrder.getTelephone()+"==="+supOrder.getContent());
		Map<String,String> map = new HashMap<String,String>();
		map.put("phone", supOrder.getTelephone());
		map.put("phone_name", supOrder.getPhone_name());
		if("11".equals(supOrder.getMsg_type())){
			int num =phonePlatDao.queryPhoneByPhone(map);
			if(num>=1){
				return;
			}
		}else if("22".equals(supOrder.getMsg_type())){
			int num =phonePlatDao.queryPhoneNumHour(map);
			if(num>=1){
				return;
			}
		}else if("33".equals(supOrder.getMsg_type())){
			int num =phonePlatDao.queryPhoneNumDay(map);
			if(num>=1){
				return;
			}
		}
		phonePlatDao.addPhone(supOrder);
	}
}
