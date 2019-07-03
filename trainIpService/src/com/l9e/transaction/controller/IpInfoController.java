package com.l9e.transaction.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.exception.IpInfoException;
import com.l9e.transaction.service.IpInfoService;
import com.l9e.transaction.vo.IpInfo;
import com.l9e.transaction.vo.Result;
import com.l9e.util.JacksonUtil;


/**
 * IP服务接口
 * 
 * @author wangsf
 * 
 */
@Controller
@RequestMapping("/ipInfo")
public class IpInfoController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(IpInfoController.class);
	
	@Resource
	private IpInfoService ipInfoService;
	
	/**
	 * 根据ip类型获取某个具体的IP
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getIpInfo")
	public void getIpInfo(HttpServletRequest request,
			HttpServletResponse response) {

		String type = getParam(request, "type");

		Result result = new Result();
		try {
			if (StringUtils.isEmpty(type)) {
				logger.info("请求获取IP没有传入类型type参数");
				throw new IpInfoException("未传入类型参数");
			}

			Integer ipType = null;
			try {
				ipType = Integer.valueOf(type);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("IP类型参数异常 type : " + type);
				throw new IpInfoException("类型异常");
			}

			IpInfo ipInfo = ipInfoService.getIpInfo(ipType);

			if (ipInfo == null) {
				logger.info("没有可用IP");
				throw new IpInfoException("没有可用IP");
			}

			logger.info("从队列中获取IP,ipInfo : " + ipInfo);

			if (ipInfo.getIpStatus() != null
					&& ipInfo.getIpStatus().equals(IpInfo.STATUS_STOP)) {
				logger.info("IP已被封停，无法使用,ipInfo : " + ipInfo.getIpId());
				throw new IpInfoException("IP已封停");
			}

			if (ipType != null
					&& (ipType == IpInfo.TYPE_CTRIP || ipType == IpInfo.TYPE_BOOKIP)) {
				if (ipInfo.getIpStatus() == null
						|| !ipInfo.getIpStatus().equals(IpInfo.STATUS_FREE)) {
					logger.info("IP状态异常,ipInfo" + ipInfo.getIpId() + ", "
							+ ipInfo.getIpExt() + ", 异常状态 : "
							+ ipInfo.getIpStatus());
					throw new IpInfoException("IP状态异常");
				}
				ipInfo.setIpStatus(null);
			}

			ipInfoService.updateIpInfo(ipInfo);

			result.setData(ipInfo);
		} catch (IpInfoException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("请求获取IP异常，e : " + e.getMessage());
			result.setMsg("系统异常");
			result.setSuccess(false);
			e.printStackTrace();
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据ip主键获取某个具体的IP
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getIpInfoById")
	public void getIpInfoById(HttpServletRequest request,
			HttpServletResponse response) {

		String idStr = getParam(request, "ipId");

		Result result = new Result();

		try {
			if (StringUtils.isEmpty(idStr)) {
				logger.info("请求获取IP没有传入类型id参数");
				throw new IpInfoException("没有传入id参数");
			}

			Integer id = null;
			try {
				id = Integer.valueOf(idStr);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("ipid参数异常 type : " + idStr);
				throw new IpInfoException("id异常");
			}

			IpInfo ipInfo = ipInfoService.queryIpInfoById(id);
			if (ipInfo == null) {
				logger.info("没有可用IP");
				throw new IpInfoException("没有可用IP");
			}
			
			if(ipInfo.getIpStatus() != null && 
					ipInfo.getIpStatus().equals(IpInfo.STATUS_STOP)){
				logger.info("IP已被封停，无法使用,ipInfo : " + ipInfo.getIpId());
				throw new IpInfoException("IP已封停");
			}

			result.setData(ipInfo);
		} catch (IpInfoException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("请求获取IP异常，e : " + e.getMessage());
			result.setMsg("系统异常");
			result.setSuccess(false);
			e.printStackTrace();
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
