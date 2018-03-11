package com.l9e.transaction.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.AcquireService;
import com.l9e.transaction.service.BookService;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;

/**
 * 账号管理
 * @author liht
 *
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(CommonController.class);
	

	@Resource
	private CommonService commonService;
	
	
	@RequestMapping("/queryGetCity.do")
	@ResponseBody
	public String getCity(String provinceid,HttpServletRequest request,HttpServletResponse response){
	
		
		List<AreaVo> list = commonService.getCity(provinceid);
		
		
		ObjectMapper map = new ObjectMapper();
		try {
			map.writeValue(response.getOutputStream(), list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	@RequestMapping("/queryGetArea.do")
	@ResponseBody
	public String getArea(String cityid,HttpServletRequest request,HttpServletResponse response){
	
		
		List<AreaVo> list = commonService.getArea(cityid);
		
		
		ObjectMapper map = new ObjectMapper();
		try {
			map.writeValue(response.getOutputStream(), list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
}
