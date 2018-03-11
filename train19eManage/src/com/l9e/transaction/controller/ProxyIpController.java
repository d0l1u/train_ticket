package com.l9e.transaction.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.l9e.common.BaseController;
import com.l9e.transaction.qo.ProxyIpQO;
import com.l9e.transaction.service.ProxyIpService;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.ProxyIpVo;
import com.l9e.util.PageUtil;

/**
 * @ClassName: ProxyIpController
 * @Description: 代理ip
 * @author: taokai
 * @date: 2017年6月26日 下午4:30:33
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
@Controller
@RequestMapping("/ip")
public class ProxyIpController extends BaseController {

	private static final Logger LOG = Logger.getLogger(ProxyIpController.class);
	
	@Resource
	private ProxyIpService proxyIpService;
	
	@RequestMapping("/gotoProxyIpPage.do")
	public String gotoProxyIpPage(HttpServletRequest request, HttpServletResponse response) {

		return "redirect:/ip/queryProxyIpList.do";
	}
	
	@RequestMapping("/gotoAddNewProxyIp.do")
	public String gotoAddNewProxyIp(HttpServletRequest request, HttpServletResponse response) {
		return "proxyIp/addNewProxyIp"; 
	}
	
	
	@RequestMapping("/addNewProxyIp.do")
	@ResponseBody
	public void addNewProxyIp(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String orderId = request.getParameter("orderId");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String port = request.getParameter("port");
		String endTime = request.getParameter("endTime");
		String proxyIpList = request.getParameter("proxyIpList");
		LOG.info("orderId:"+orderId);
		LOG.info("username:"+username);
		LOG.info("password:"+password);
		LOG.info("port:"+port);
		LOG.info("endTime:"+endTime);
		LOG.info("proxyIpList:"+proxyIpList);
		if(StringUtils.isBlank(orderId) || orderId.replaceAll("\\s", "").equals("")){
			throw new RuntimeException("订单号为空");
		}
		if(StringUtils.isBlank(username) || username.replaceAll("\\s", "").equals("")){
			throw new RuntimeException("账户名为空");
		}
		if(StringUtils.isBlank(password) || password.replaceAll("\\s", "").equals("")){
			throw new RuntimeException("密码为空");
		}
		if(StringUtils.isBlank(port) || port.replaceAll("\\s", "").equals("") || ! port.replaceAll("\\s", "").matches("[0-9]+")){
			throw new RuntimeException("端口为空或者不合法");
		}
		if(StringUtils.isBlank(proxyIpList) || proxyIpList.replaceAll("\\s", "").equals("")){
			throw new RuntimeException("IP为空");
		}
		
		String[] ipArr = proxyIpList.split("\n");
		List<String> ipList = Arrays.asList(ipArr);
		LOG.info("ipList-size:"+ipList.size());
		for (int i = 0; i < ipList.size(); i++) {
			String ip = ipList.get(i).replaceAll("\\s", "");
			if(StringUtils.isBlank(ip)){
				ipList.remove(i);
				i--;
			}else{
				ipList.set(i, ip);
			}
		}
		
		ProxyIpVo ipvo = new ProxyIpVo();
		ipvo.setCreateTime(new Date());
		ipvo.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
		ipvo.setOrderId(orderId);
		ipvo.setPort(Integer.valueOf(port));
		ipvo.setIpList(ipList);
		ipvo.setUsername(username);
		ipvo.setPassword(password);
		proxyIpService.addProxyIps(ipvo);
	}
	
	@RequestMapping("/queryProxyIpList.do")
	public String queryProxyIpList(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String orderId = request.getParameter("orderId"); 
		String ip = request.getParameter("ip"); 
		String beginCreateTime = request.getParameter("beginCreateTime"); 
		String endCreateTime = request.getParameter("endCreateTime"); 
		String beginEndTime = request.getParameter("beginEndTime"); 
		String endEndTime = request.getParameter("endEndTime"); 
		String isShow = request.getParameter("isShow"); 
		String queryCheckAllFlag = request.getParameter("queryCheckAllFlag"); 
//		LOG.info("orderId:"+orderId);
//		LOG.info("ip:"+ip);
//		LOG.info("beginCreateTime:"+beginCreateTime);
//		LOG.info("endCreateTime:"+endCreateTime);
//		LOG.info("beginEndTime:"+beginEndTime);
//		LOG.info("endEndTime:"+endEndTime);
//		LOG.info("isShow:"+isShow);
		
		
		
		if(StringUtils.isNotBlank(isShow)){
			ProxyIpQO proxyIpQo = new ProxyIpQO();
			
			if(StringUtils.isNotBlank(queryCheckAllFlag) && !queryCheckAllFlag.equals("8") && !queryCheckAllFlag.equals("9")){
				Integer status = Integer.valueOf(queryCheckAllFlag);
				LOG.info("#### 查询状态:"+status);
				proxyIpQo.setStatus(status);
			}else{
				LOG.info("查询全部...");
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(StringUtils.isNotBlank(beginCreateTime)){
				proxyIpQo.setBeginCreateTime(dateFormat.parse(beginCreateTime));
			}
			if(StringUtils.isNotBlank(endCreateTime)){
				proxyIpQo.setEndCreateTime(dateFormat.parse(endCreateTime));
			}
			if(StringUtils.isNotBlank(beginEndTime)){
				proxyIpQo.setBeginEndTime(dateFormat.parse(beginEndTime));
			}
			if(StringUtils.isNotBlank(endEndTime)){
				proxyIpQo.setEndEndTime(dateFormat.parse(endEndTime));
			}
			proxyIpQo.setIp(ip);
			proxyIpQo.setOrderId(orderId);
			
			//查询
			/*************************分页条件***************************/
			Integer totalCount = proxyIpService.queryTotalCount(proxyIpQo);
			LOG.info("totalCount:"+totalCount); 
			
			//分页
			PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
			int firstResultIndex = page.getFirstResultIndex();//每页开始的序号
			int everyPageRecordCount = page.getEveryPageRecordCount();//每页记录数
			LOG.info("firstResultIndex:"+firstResultIndex);
			LOG.info("everyPageRecordCount:"+everyPageRecordCount);
			proxyIpQo.setBeginIndex(firstResultIndex);
			proxyIpQo.setEndIndex(everyPageRecordCount);
			
			List<ProxyIpVo> proxyIpList = proxyIpService.queryListProxyIp(proxyIpQo);
			
			LOG.info("proxyIpList-size:"+proxyIpList.size()); 
			request.setAttribute("proxyIpList", proxyIpList); 
		}else{
			PageVo page = PageUtil.getInstance().paging(request, 15, 0);
		}
		
		request.setAttribute("orderId", orderId);
		request.setAttribute("ip", ip);
		request.setAttribute("beginCreateTime", beginCreateTime);
		request.setAttribute("endCreateTime", endCreateTime);
		request.setAttribute("beginEndTime", beginEndTime);
		request.setAttribute("endEndTime", endEndTime);
		request.setAttribute("isShow", isShow); 
		request.setAttribute("queryCheckAllFlag", queryCheckAllFlag); 
		return "proxyIp/proxyIpList"; 
	}
	
	
	@RequestMapping("/batchDelete.do")
	public void batchDelete(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String parameter = getRequestPostStr(request);
		JSONObject json = JSONObject.parseObject(parameter);
		List<Integer> linkedList = json.getObject("autoIds", List.class);
		if(linkedList == null || linkedList.isEmpty()){
			LOG.info("autoIds为空"); 
			return;
		}
		Integer result = proxyIpService.deleteListByIds(linkedList);
		LOG.info("删除结果："+result);
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String parameter = request.getParameter("autoId");
		LinkedList<Integer> linkedList = new LinkedList<Integer>();
		linkedList.add(Integer.valueOf(parameter));
		Integer result = proxyIpService.deleteListByIds(linkedList);
		LOG.info("删除结果："+result);
		return "redirect:/ip/queryProxyIpList.do?isShow=1";
	}
	
	
	@RequestMapping("/batchRenew.do")
	public void batchRenew(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String parameter = getRequestPostStr(request);
		JSONObject json = JSONObject.parseObject(parameter);
		List<Integer> idList = json.getObject("autoIds", List.class);
		if(idList == null || idList.isEmpty()){
			LOG.info("autoIds为空"); 
			return;
		}
		Date endTime = json.getDate("endTime");
		if(endTime == null){
			LOG.info("到期时间为空"); 
			return;
		}
		ProxyIpVo ipVo = new ProxyIpVo();
		ipVo.setEndTime(endTime);
		Integer result = proxyIpService.updateListByIds(idList,ipVo);
		LOG.info("续费结果："+result);
	}
	
	@RequestMapping("/batchEnable.do")
	public void batchEnable(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String parameter = getRequestPostStr(request);
		JSONObject json = JSONObject.parseObject(parameter);
		List<Integer> idList = json.getObject("autoIds", List.class);
		if(idList == null || idList.isEmpty()){
			LOG.info("autoIds为空"); 
			return;
		}
		Integer status = json.getInteger("status");
		if(status == null){
			LOG.info("状态为空"); 
			return;
		}
		ProxyIpVo ipVo = new ProxyIpVo();
		ipVo.setStatus(status);
		Integer result = proxyIpService.updateListByIds(idList,ipVo);
		LOG.info("启用结果："+result);
	}
	
	@RequestMapping("/batchDisable.do")
	public void batchDisable(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String parameter = getRequestPostStr(request);
		JSONObject json = JSONObject.parseObject(parameter);
		List<Integer> idList = json.getObject("autoIds", List.class);
		if(idList == null || idList.isEmpty()){
			LOG.info("autoIds为空"); 
			return;
		}
		Integer status = json.getInteger("status");
		if(status == null){
			LOG.info("状态为空"); 
			return;
		}
		ProxyIpVo ipVo = new ProxyIpVo();
		ipVo.setStatus(status);
		Integer result = proxyIpService.updateListByIds(idList,ipVo);
		LOG.info("禁用结果："+result);
	}
	
	
	@RequestMapping("/disable.do")
	public String disable(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String parameter = request.getParameter("autoId");
		LinkedList<Integer> idList = new LinkedList<Integer>();
		idList.add(Integer.valueOf(parameter));
		ProxyIpVo ipVo = new ProxyIpVo();
		ipVo.setStatus(0);
		Integer result = proxyIpService.updateListByIds(idList,ipVo);
		LOG.info("禁用结果："+result);
		return "redirect:/ip/queryProxyIpList.do?isShow=1";
	}
	
	@RequestMapping("/enable.do")
	public String enable(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String parameter = request.getParameter("autoId");
		LinkedList<Integer> idList = new LinkedList<Integer>();
		idList.add(Integer.valueOf(parameter));
		ProxyIpVo ipVo = new ProxyIpVo();
		ipVo.setStatus(1); 
		Integer result = proxyIpService.updateListByIds(idList,ipVo);
		LOG.info("启用结果："+result);
		return "redirect:/ip/queryProxyIpList.do?isShow=1";
	}
	
	
	
}
 