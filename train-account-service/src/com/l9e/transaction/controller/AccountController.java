package com.l9e.transaction.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.exception.AccountException;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Result;
import com.l9e.util.JacksonUtil;

/**
 * 账号服务接口
 * 
 * @author licheng
 * 
 */
@Controller
@RequestMapping("/accountN")
public class AccountController extends BaseController {

	private static final Logger logger = Logger.getLogger(AccountController.class);

	@Resource
	private AccountService accountService;

	/**
	 * @Title: getChannelAccount
	 * @Description: 根据渠道获取账号
	 * @author: taokai
	 * @date: 2017年8月23日 下午4:27:15
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getChannelAccount")
	public void getChannelAccount(HttpServletRequest request, HttpServletResponse response) {
		logger.info("getChannelAccount...");
		String channel = getParam(request, "channel");
		String passengerSize = getParam(request, "passengerSize");// 账号要添加的联系人个数
		logger.info("channel:" + channel + "; passengerSize:" + passengerSize);

		Result result = new Result();
		try {
			if (StringUtils.isEmpty(channel)) {
				logger.info("请求获取账号没有传入渠道channel参数");
				throw new AccountException("未传入渠道参数");
			}

			if (StringUtils.isEmpty(passengerSize)) {
				logger.info("请求获取账号没有传入账号要添加的联系人个数参数");
				throw new AccountException("未传入账号要添加的联系人个数参数");
			}
			int size = Integer.valueOf(passengerSize);
			logger.info("[#####]------------------");
			logger.info("[#####]passengerSize:" + size);
			Account account = accountService.getAccount(channel, size);
			if (account == null) {
				if (size == 0) {
					logger.info("[#####]获取10>人账号为空，账号不足");
				} else if (size >= 5) {
					logger.info("[#####]10人账号查询结果为空,获取10>人账号");
					account = accountService.getAccount(channel, 0);
				} else {
					size = size + 1;
					while (size <= 5 && account == null) {
						logger.info("[#####]再次查询" + (15 - size) + "人账号");
						try {
							account = accountService.getAccount(channel, size);
						} catch (Exception e) {
							logger.info("getAccount Exception", e);
						} finally {
							size = size + 1;
						}
					}
				}
			}
			logger.info("[#####]account:" + account);
			if (account == null) {
				//
				
				logger.info("没有空闲账号, channel : " + channel);
				throw new AccountException("没有空闲账号");
			}

			if (account.getStatus() == null || !account.getStatus().equals(Account.STATUS_QUEUE)) {
				logger.info("账号状态异常,accountId : " + account.getId() + ", " + account.getUsername() + ", 异常状态 : "
						+ account.getStatus());
				throw new AccountException("账号状态异常");
			}

			account.setStatus(Account.STATUS_PLACING_ORDER);
			accountService.updateAccount(account);
			result.setData(account);
		} catch (AccountException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("请求获取账号异常", e);
			result.setMsg("系统异常");
			result.setSuccess(false);
		}
		String string = "";
		try {
			string = JacksonUtil.generateJson(result);
		} catch (IOException e) {
			logger.info("【JacksonUtil IOException】", e);
		}
		printJson(response, string);

	}

	/**
	 * @Title: getOrderAccount
	 * @Description: 获取重发时正在使用的账号 accountId : 账号id
	 * @author: taokai
	 * @date: 2017年8月23日 下午4:33:07
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getOrderAccount")
	public void getOrderAccount(HttpServletRequest request, HttpServletResponse response) {
		logger.info("getOrderAccount...");
		String idStr = getParam(request, "id");
		Result result = new Result();
		logger.info("account id:" + idStr);
		try {
			if (StringUtils.isEmpty(idStr)) {
				logger.info("请求获取账号没有传入账号id参数");
				throw new AccountException("账号id为空");
			}

			Integer id = null;

			try {
				id = Integer.valueOf(idStr);
			} catch (NumberFormatException e) {
				logger.info("accountId参数非数字主键，转换异常:" + idStr);
				throw new AccountException("账号id参数异常");
			}

			Account account = accountService.queryAccountById(id);
			if (account == null) {
				logger.info("获取账号失败，accountId : " + idStr);
				throw new AccountException("账号获取失败");
			}
			result.setData(account);
		} catch (AccountException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("请求获取账号异常 ", e);
			result.setMsg("系统异常");
			result.setSuccess(false);
		}

		String string = "";
		try {
			string = JacksonUtil.generateJson(result);
		} catch (IOException e) {
			logger.info("【JacksonUtil IOException】", e);
		}
		printJson(response, string);
	}

	/**
	 * 
	 * 停用12306账号 accountId : 账号id reason : 停用原因
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/stopAccount")
	public void stopAccount(HttpServletRequest request, HttpServletResponse response) {
		logger.info("stopAccount...");
		String idStr = getParam(request, "id");
		String reason = getParam(request, "reason");
		logger.info("id:" + idStr + "; reason:" + reason);

		Result result = new Result();
		try {
			Integer id = null;
			try {
				id = Integer.valueOf(idStr);
			} catch (NumberFormatException e) {
				logger.info("accountId参数非数字主键，转换异常:" + idStr);
				throw new AccountException("账号id参数异常");
			}

			Account stopAccount = accountService.stopAccount(id, reason);
			if (stopAccount == null) {
				logger.info("停用账号失败, accountId : " + idStr);
				throw new AccountException("停用账号失败");
			}

			logger.info("停用账号成功, accountId : " + idStr);

		} catch (AccountException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("停用账号异常", e);
			result.setMsg("系统异常");
			result.setSuccess(false);
		}

		String string = "";
		try {
			string = JacksonUtil.generateJson(result);
		} catch (IOException e) {
			logger.info("【JacksonUtil IOException】", e);
		}
		printJson(response, string);
	}

	/**
	 * 找到匹配度最高的账号 passportNo : 乘客证件号码集合 add by wangsf
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/filterAccount")
	public void filterAccount(HttpServletRequest request, HttpServletResponse response) {

		logger.info("filterAccount...");

		String passportNo = getParam(request, "passportNo");
		String channel = getParam(request, "channel");// 新传参数
		// 把入参分隔后放入List中
		List<String> paramList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(passportNo, ",");
		while (st.hasMoreTokens()) {
			paramList.add(st.nextToken());
		}

		logger.info("匹配适用账号, passportNo : " + passportNo);
		logger.info("入参列表, paramList : " + paramList);
		Result result = new Result();

		try {
			// 得到匹配度最高的历史账号信息
			Account account = accountService.filterAccount(paramList, channel);
			if (account == null) {
				logger.info("passportNo : " + passportNo + " 没有匹配到历史账号");
				throw new AccountException("没有匹配到历史账号");
			}

			// 不判断账号状态，直接拿来下单2016-04-13
			// if (account.getStatus().equals(Account.STATUS_PLACING_ORDER)) {
			// logger.info("passportNo : " + passportNo
			// + " 匹配账号正在下单, account : " + account.getUsername());
			// throw new AccountException("账号正在下单，12306账号名："
			// + account.getUsername());
			// }

			account.setStatus(Account.STATUS_PLACING_ORDER);
			accountService.updateAccount(account);
			result.setData(account);
		} catch (AccountException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("匹配历史账号异常", e);
			result.setMsg("系统异常");
			result.setSuccess(false);
		}

		String string = "";
		try {
			string = JacksonUtil.generateJson(result);
		} catch (IOException e) {
			logger.info("【JacksonUtil IOException】", e);
		}
		printJson(response, string);
	}

	/**
	 * 修改账号：含自操作
	 * 
	 * 
	 * @param request
	 * @param response
	 * @param option
	 */
	@RequestMapping("/modifyAccount")
	public void modifyAccount(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * account json: { "id" : 1245184, "username" : "JYR1972123030",
		 * "password" : "JYR340538", "status" : "33", "channel" : "19e",
		 * "contact" : 2, "bookNum" : 0, "orderId" : "EXHC160107120316625" }
		 */
		logger.info("modifyAccount...");
		String accountJson = getParam(request, "account");

		logger.info("修改账号信息，12306账号json串, accountJson : " + accountJson);

		Result result = new Result();

		try {
			if (StringUtils.isEmpty(accountJson)) {
				logger.info("修改账号接口传入账号json串为空");
				throw new AccountException("账号参数为空");
			}
			Account account = null;

			try {
				account = JacksonUtil.readJson(accountJson, Account.class);
			} catch (Exception e) {
				logger.info("账号json串解析异常，e : " + e.getMessage());
				throw new AccountException("账号参数解析异常");
			}

			if (account != null) {
				accountService.updateAccount(account);
				logger.info("账号修改成功, account : " + account);
			} else {
				logger.info("账号信息有误，不作修改");
			}
		} catch (AccountException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("修改12306账号异常", e);
			result.setMsg("系统异常");
			result.setSuccess(false);
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			logger.info("printJson异常", e);
		}
	}

	/**
	 * 释放账号至空闲状态
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/release")
	public void releaseAccount(HttpServletRequest request, HttpServletResponse response) {

		String id = getParam(request, "id");

		Result result = new Result();

		try {
			if (StringUtils.isEmpty(id)) {
				logger.info("释放账号接口传入账号id为空");
				throw new AccountException("账号id为空");
			}

			Account account = null;

			Integer accountId = null;

			try {
				accountId = Integer.valueOf(id);
			} catch (Exception e) {
				logger.info("账号id转换异常,id : " + id);
				throw new AccountException("id参数异常");
			}

			account = accountService.queryAccountById(accountId);

			if (account == null) {
				logger.info("账号不存在，终止释放操作, id : " + id);
				throw new AccountException("账号不存在");
			}

			if (account.getStatus() != null && account.getStatus().equals(Account.STATUS_STOP)) {
				logger.info("账号已停用，无法释放. id : " + id);
				throw new AccountException("账号已停用");
			}

			account.setStatus(Account.STATUS_FREE);
			accountService.updateAccount(account);
		} catch (AccountException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("释放12306账号异常", e);
			result.setMsg("系统异常");
			result.setSuccess(false);
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			logger.info("printJson异常", e);
		}
	}

	/**
	 * 白名单更改账号表信息 add by wangsf
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping("/modifyAccountInfo")
	public void modifyAccountInfo(HttpServletRequest request, HttpServletResponse response) {
		// 账号ID--参数中获取
		String idStr = getParam(request, "acc_id");
		// 账号核验状态--参数中获取
		String realCheckStatusStr = getParam(request, "real_check_status");
		// 手机核验状态--参数中获取
		String realReceiveStr = getParam(request, "real_receive");
		// 真实联系人个数--参数中获取
		String realContactNumStr = getParam(request, "real_contact_num");

		// 账号ID
		Integer id = null;
		if (!("".equals(idStr) || null == idStr)) {
			id = Integer.valueOf(idStr);
		}
		// 账号核验状态
		Byte realCheckStatus = null;
		if (!("".equals(realCheckStatusStr) || null == realCheckStatusStr)) {
			realCheckStatus = Byte.valueOf(realCheckStatusStr);
		}
		// 手机核验状态
		String realReceive = "";
		if (!("".equals(realReceiveStr) || null == realReceiveStr)) {
			realReceive = realReceiveStr;
		}
		// 真实联系人个数
		Integer realContactNum = null;
		if (!("".equals(realContactNumStr) || null == realContactNumStr)) {
			realContactNum = Integer.valueOf(realContactNumStr);
		}
		// 账号状态
		String accStatus = "";
		// 常用联系人个数 和真实联系人个数一致
		Integer contactNum = realContactNum;

		// 更新成功是否的提示信息，默认在账号状态不变的情况下，提示这个！
		String point = "账号状态不变,更新成功!";
		try {
			if (2 == realCheckStatus || 6 == realCheckStatus) {
				accStatus = "22";
				point = "账号状态变更为22,更新成功!";
			} else if ((1 == realCheckStatus || 5 == realCheckStatus) && "X".equals(realReceive)) {
				accStatus = "77";
				point = "账号状态变更为77,更新成功!";
			}

			// 在此执行更新操作
			accountService.updateAccountInfo(id, realCheckStatus, realReceive, realContactNum, accStatus, contactNum);

		} catch (Exception e) {
			logger.info("白名单更改账号表信息异常", e);
			point = "更新失败!";
		}

		try {
			printJson(response, point);
		} catch (Exception e) {
			logger.info("printJson异常", e);
		}
	}

	/**
	 * 获取重白名单表匹配到历史账号的ID和次数的集合 add by wangsf
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getAccountMappingNum")
	public void getAccountMappingNum(HttpServletRequest request, HttpServletResponse response) {

		String passportNo = getParam(request, "passportNo");
		// 把入参分隔后放入List中
		List<String> paramList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(passportNo, ",");
		while (st.hasMoreTokens()) {
			paramList.add(st.nextToken());
		}

		logger.info("匹配适用账号, passportNo : " + passportNo);
		logger.info("入参列表, paramList : " + paramList);

		Result result = new Result();
		try {
			// 白名单表匹配到历史账号的ID和次数
			Account account = accountService.queryAccountMappingNum(paramList);
			if (account == null) {
				logger.info("passportNo : " + passportNo + " 没有匹配到历史账号");
				throw new AccountException("没有匹配到历史账号");
			}
			result.setData(account);
		} catch (AccountException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("请求获取账号异常", e);
			result.setMsg("系统异常");
			result.setSuccess(false);
			e.printStackTrace();
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			logger.info("printJson异常", e);
		}
	}

	/**
	 * 对错误信息的处理 add by wangsf
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/handleBindAccErrorCode")
	public void handleBindAccErrorCode(HttpServletRequest request, HttpServletResponse response) {

		String passportNo = getParam(request, "passportNo");// 传入的乘客证件号
		String userName = getParam(request, "userName");// 12306账号名
		String pass = getParam(request, "pass");// 12306密码

		// String passportNo = "341202199702150518,232103194009271227";//
		// 传入的乘客证件号
		// String userName = "wangxue19860701";// 12306账号名
		// String pass = "123456a";// 12306密码
		// 把入参分隔后放入List中
		List<String> paramList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(passportNo, ",");
		while (st.hasMoreTokens()) {
			paramList.add(st.nextToken());
		}

		logger.info("对自带12306账号的处理, 传入的passportNo : " + passportNo);
		logger.info("对自带12306账号的处理, paramList为 : " + paramList);
		Result result = new Result();

		try {
			// 调用对自带12306账号订单的错误信息处理的接口
			String errorCode = accountService.bindAccountErrorCode(userName, pass, paramList);
			logger.info("对自带12306账号的处理, errorCode为 : " + errorCode);
			if ("0".equals(errorCode)) {
				logger.info("errorCode : " + errorCode + " 脚本执行异常！");
				throw new AccountException("脚本执行异常");
			}
			result.setData(errorCode);
		} catch (AccountException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("脚本执行异常", e);
			result.setMsg("脚本执行异常");
			result.setSuccess(false);
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			logger.info("printJson异常", e);
		}
	}

	/**
	 * 
	 * 自带账号订单，实时更新账号密码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/modifyAccountPwd")
	public void modifyAccountPwd(HttpServletRequest request, HttpServletResponse response) {

		String userName = getParam(request, "userName");
		String userPassWord = getParam(request, "userPassWord");

		logger.info("开始更新自带账号订单的账号密码，userName : " + userName + ", userPassWord : " + userPassWord);

		Result result = new Result();
		try {

			accountService.updateAccountPwd(userName, userPassWord);
		} catch (Exception e) {
			logger.info("更新账号密码异常", e);
			result.setMsg("系统异常");
			result.setSuccess(false);
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			logger.info("printJson异常", e);
		}
	}

}
