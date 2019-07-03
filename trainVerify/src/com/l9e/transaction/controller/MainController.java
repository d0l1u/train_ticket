package com.l9e.transaction.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.util.MD5;
import com.l9e.common.BaseController;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.CountService;
import com.l9e.transaction.service.IpInfoService;
import com.l9e.transaction.service.RedisService;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.CardBankVo;
import com.l9e.transaction.vo.CardInfoVo;
import com.l9e.transaction.vo.Count;
import com.l9e.transaction.vo.DeviceWeight;
import com.l9e.transaction.vo.IpInfo;
import com.l9e.transaction.vo.PassWhiteListVo;
import com.l9e.transaction.vo.Passenger;
import com.l9e.transaction.vo.ReturnData;
import com.l9e.transaction.vo.ReturnUser;
import com.l9e.transaction.vo.ReturnVo;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PostRequestUtil;
import com.l9e.util.RobotResultUtil;
import com.l9e.util.UrlFormatUtil;
import com.l9e.util.hcpjar.util.StrUtil;

@Controller
@RequestMapping("/l9helper")
public class MainController extends BaseController {

	private static final Logger logger = Logger.getLogger(MainController.class);
	private static final Random WEIGHT_RANDOM = new Random();

	@Resource
	private CommonService commonService;

	@Resource
	private AccountService accountService;

	@Resource
	private WorkerService workerService;

	@Resource
	private CountService countService;

	@Resource
	private RedisService redisService;

	@Resource
	private IpInfoService ipInfoService;

	/**
	 * 接口主入口
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/interface.do")
	public void query(HttpServletRequest request, HttpServletResponse response) {
		String command = this.getParam(request, "command");

		if ("verify".equalsIgnoreCase(command)) {// 验证实名信息接口
			this.realnameVerify(request, response);
		}
		if ("verifyAccount".equalsIgnoreCase(command)) {// 12306账号核验[老的]
			this.accountVerify(request, response);
		}
		if ("verifyAccountTuniu".equalsIgnoreCase(command)) {// 途牛12306账号核验[新加的]
			this.accountVerifyTuniu(request, response);
		}
		if ("queryAccountUse".equalsIgnoreCase(command)) {// 账号下查询常用联系人
			this.queryAccountUse(response, request);
		}
		if ("deleteContact".equalsIgnoreCase(command)) { // 删除常用联系人
			this.deleteContact(request, response);
		}
		if ("add_contact".equals(command)) {// 添加常用联系人
			this.add_contact(request, response);
		}
		if ("active".equalsIgnoreCase(command)) {
			this.updateAccountActive(request, response);
		}

		if ("updateWorker".equalsIgnoreCase(command)) {
			this.updateWorker(request, response);
		}
		if ("modifyPass".equalsIgnoreCase(command)) {
			this.updatePassword(request, response);
		}
		if ("count".equalsIgnoreCase(command)) {
			this.countRequest(request, response);
		}
		if ("verifyCode".equals(command)) {// 查询验证码（机器人发请求）
			this.queryVerifyCode(request, response);
		}
		if ("deleteQueue".equals(command)) {// 支付验证码
			this.deleteQueue(request, response);
		}
		if ("verifyBankCode".equals(command)) {// 查询银行卡验证码（机器人发请求）
			this.verifyBankCode(request, response);
		}

		// 更新支付宝账户余额
		if ("alipayBalance".equals(command)) {

			String card_no = this.getParam(request, "card_no");// 支付宝账号
			String card_remain = this.getParam(request, "card_remain");// 余额
			logger.info("更新支付宝账户余额  card_no:" + card_no + " ,余额 :" + card_remain);
			Map<String, String> map = new HashMap<String, String>();

			map.put("card_no", card_no);
			map.put("card_remain", card_remain);
			commonService.updateBalance(map);
		}

		if ("addAlipayCheckCode".equals(command)) {
			this.addAlipayCheckCode(request, response);
		}

	}

	/**
	 * @param request
	 * @param response
	 *  途牛账号验证接口
	 */
	private void accountVerifyTuniu(HttpServletRequest request,
			HttpServletResponse response) {
		UUID uuid=UUID.randomUUID();
		String data = this.getParam(request, "data");
		logger.info("accountVerifyTuniu传入参数:"+data);
		int count = commonService.queryHeyanWorkNum(5);//机器管理下的【核验机器】
		Map<String, Object> map_worker = new HashMap<String, Object>();
		int num = CreateIDUtil.getNextNum(count);
		map_worker.put("limit_num", num - 1);
		map_worker.put("worker_type", 5);	
		Worker worker = commonService.queryRandomWorker(map_worker);
		//http://127.0.0.1:18010/robot/verify/account/pc/{账号}/{密码}
		String ip=worker.getWorkerExt();//机器IP
		JSONArray array = JSONArray.fromObject(data);
		JSONObject obj=array.getJSONObject(0);
		String  username=obj.getString("username").replace(" ","");
		String  pass=obj.getString("pass").replace(" ","");
		StringBuffer url=new StringBuffer();
		url.append("http://").append(ip).append(":18010/robot/verify/account/pc/").append(username).append("/").append(pass);
		logger.info(uuid+",请求帐号核验url" + url);
		String returnStr="";
		try {
			returnStr = HttpUtil.sendByGet(url.toString(), "UTF-8", "60000", "60000");
		} catch (Exception e1) {
			logger.info(uuid+"请求帐号核验异常",e1);
		}
		logger.info(uuid+"请求帐号核验返回:" + returnStr);
		
		String retValue="";
		String retInfo="";
		String result_str="";
		if(StringUtils.isNotEmpty(returnStr)) {
			JSONObject obj3=new JSONObject();
			JSONArray  arr1=new JSONArray();
			try {
				JSONObject obj1=JSONObject.fromObject(returnStr);
				String status=obj1.getString("status");
				String message=obj1.getString("message");
				JSONObject obj2=new JSONObject();
					
				if ("0000".equals(status)) {
					obj2.put("is_receive", "Y");
					obj2.put("user_status", "1");
					obj3.put("retInfo", obj2);
					obj3.put("robotNum", 0);
					obj3.put("accId", "");
					obj3.put("retValue", "success");
					obj3.put("contacts", arr1);
					result_str=RobotResultUtil.ResultChange(obj3);
				}else{
					
					if (StringUtils.isNotEmpty(message)) {
						retValue="failure";
						retInfo="$$$"+message;
					}else {
						retValue="failure";
						retInfo="";
					}
					
					obj3.put("robotNum", 0);
					obj3.put("accId", "");
					obj3.put("retValue", retValue);
					obj3.put("retInfo", retInfo);
					obj3.put("contacts", arr1);
					
					result_str=RobotResultUtil.ResultChange(obj3);
				}
				
			} catch (Exception e) {
				logger.info(uuid+"解析结果：",e);
				retValue="failure";
				retInfo="";
				
				obj3.put("robotNum", 0);
				obj3.put("accId", "");
				obj3.put("retValue", retValue);
				obj3.put("retInfo", retInfo);
				obj3.put("contacts", arr1);
			}
			
			result_str=RobotResultUtil.ResultChange(obj3);
			
		}else {
			logger.info(uuid+"返回结果为空,由途牛接口处理");
		}
		
		logger.info(uuid+"处理后返回的结果："+result_str);	
		printJson(response, result_str);
			
	}

	private void deleteQueue(HttpServletRequest request, HttpServletResponse response) {
		String account = this.getParam(request, "account");// 支付宝账号
		//redis-----queue
		String queueKey = getAlipayQueue(account);
//		 Long lLen = redisService.LLEN(queueKey);
//		 String verify_code = "";
//		 if(lLen > 0) {
		redisService.delVal(queueKey);
		logger.info("[redis queue]delVal key:" + queueKey);
//		 }else{
//		 	logger.info("[redis queue]delVal key:"+queueKey+", 为空lLen:"+lLen);
//		 }

		write2Response(response, "success");
	}

	private void queryVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		String account = this.getParam(request, "account");// 支付宝账号
//		// 从redis中取值----缓存
//		String verify_code = (String)redisService.getVal("alipay_"+account);
//		logger.info("[redis]获取key:"+"alipay_"+account+",
//		value:"+verify_code);

		//redis-----queue
		String queueKey = getAlipayQueue(account);
		Long lLen = redisService.LLEN(queueKey);
		String verify_code = "";
		if (lLen > 0) {
			verify_code = (String) redisService.RPOP(queueKey);
			logger.info("[redis queue]RPOP key:" + queueKey + ", value:" + verify_code);
		} else {
			logger.info("[redis queue]RPOP key:" + queueKey + ", lLen:" + lLen);
		}

		// String verify_code = workerService.queryVerifyCode(account);
		// verify_code = verify_code==null?"":verify_code;
		if (verify_code == null) {
			verify_code = "";
			write2Response(response, verify_code);
		} else {
			write2Response(response, verify_code);
			// redisService.delVal("alipay_"+account);//从redis中删除该值
			// logger.info("[redis]删除key:"+"alipay_"+account);

		}
		logger.info("查询验证码（机器人发请求）, account=" + account + ", verify_code=" + verify_code);

	}

	private void verifyBankCode(HttpServletRequest request, HttpServletResponse response) {
		String account = this.getParam(request, "account");// 银行卡账号
		// redis-----queue
		String queueKey = getBankQueue(account);
		Long lLen = redisService.LLEN(queueKey);
		String verify_code = "";
		if (lLen > 0) {
			verify_code = (String) redisService.RPOP(queueKey);
			logger.info("[redis queue]RPOP key:" + queueKey + ", value:" + verify_code);
		} else {
			logger.info("[redis queue]RPOP key:" + queueKey + ", lLen:" + lLen);
		}

		// String verify_code = workerService.queryVerifyCode(account);
		// verify_code = verify_code==null?"":verify_code;
		if (verify_code == null) {
			verify_code = "";
			write2Response(response, verify_code);
		} else {
			write2Response(response, verify_code);
			// redisService.delVal("alipay_"+account);//从redis中删除该值
			// logger.info("[redis]删除key:"+"alipay_"+account);

		}
		logger.info("查询验证码（机器人发请求）, account=" + account + ", verify_code=" + verify_code);

	}

	private void countRequest(HttpServletRequest request, HttpServletResponse response) {
		String result = request.getParameter("result");
		logger.info("统计信息为：：" + result);
		JSONObject resultJson = JSONObject.fromObject(result);
		Count count = new Count();
		count.setChannel(resultJson.getString("channel"));
		count.setCode(resultJson.getString("code"));
		count.setIp(request.getRemoteAddr());
		count.setSource(resultJson.getString("source"));
		count.setMessage(resultJson.getString("message"));
		count.setType(resultJson.getString("type"));
		countService.insertIntoCount(count);
	}

	private void updatePassword(HttpServletRequest request, HttpServletResponse response) {
		String result = request.getParameter("result");
		logger.info("更改密码结果：：" + result);
		JSONObject resultJson = JSONObject.fromObject(result);
		Account account = new Account();
		account.setAccId(resultJson.getString("acc_id"));
		account.setAccUsername(resultJson.getString("acc_username"));
		account.setAccPassword(resultJson.getString("acc_password"));
		account.setOldPass(resultJson.getString("acc_oldPass"));
		String resultCode = resultJson.getString("ret_code");
		String resultMsg = resultJson.getString("ret_msg");
		if ("0".equalsIgnoreCase(resultCode) && account.getAccId() != null && account.getAccId().length() > 0) {
			account.setModifyStatus("03");
			accountService.updateModifyStatus(account);
		}
		if ("1".equalsIgnoreCase(resultCode) && account.getAccId() != null && account.getAccId().length() > 0) {
			account.setModifyStatus("04");
			accountService.updateModifyStatus(account);
		}
	}

	/**
	 * 实名认证
	 * 
	 * @param request
	 * @param response
	 */
	public void realnameVerify(HttpServletRequest request, HttpServletResponse response) {
		String data = this.getParam(request, "passengers");
		String channel = this.getParam(request, "channel");// 渠道

		// 解析乘客数据cert_no cert_type user_name
		// [{"cert_no":"320324197810066225","cert_type":"1","user_name":"徐静"}]
		List<Passenger> userList = new ArrayList<Passenger>();
		JSONArray passengers = JSONArray.fromObject(data);
		logger.info("实名认证，传入的乘客信息为：" + passengers);
		Passenger passenger = null;

		JSONObject jsobj = null;
		for (int i = 0; i < passengers.size(); i++) {
			passenger = new Passenger();
			jsobj = passengers.getJSONObject(i);
			passenger.setCert_no(jsobj.getString("cert_no"));
			passenger.setCert_type(jsobj.getString("cert_type"));
			passenger.setUser_name(jsobj.getString("user_name"));
			passenger.setUser_type("0");// 默认成人
			passenger.setCheck_status(Passenger.CHECK_PASSED);// 初始化默认通过核验
			userList.add(passenger);
		}
		// memcache校验
		boolean mRet = memcacheVerify(userList);
		logger.info("实名认证，memcache校验结果mRet为：" + mRet);
		if (mRet) {
			logger.info("Memcache:核验用户信息完成！");
			write2Response(response, getReturnJson(userList));
			return;
		}

		// 数据库校验 add by wangsf
		// 把userList中的Cert_No提取出来，放到String串中。
		Account account = null;
		Integer accountId = null;// 账号ID
		Integer machingNum = null;// 匹配到的总次数
		boolean dRet = false;// 数据库中是否匹配到
		if (userList != null && userList.size() > 0) {

			// 把订单下面的多个身份证号用","分隔，拼成一个全新的字符串。
			StringBuilder passportNos = new StringBuilder();
			String passportNo = null;
			for (int i = 0; i < userList.size(); i++) {
				passportNo = userList.get(i).getCert_no();
				passportNos.append(passportNo);
				passportNos.append(",");
			}
			logger.info("传入的身份证号为：" + passportNos.toString());
			// 调用AccountService中的接口，获取匹配历史账号的ID和匹配次数的集合
			account = accountService.queryAccountMappingNum(passportNos.toString());
			logger.info("实名认证,白名单匹配到的历史账号为：" + account);

			if (account != null) {
				accountId = account.getId();
				logger.info("账号ID为：" + accountId);
				machingNum = account.getContact();
				logger.info("账号ID匹配到历史账号的次数为：" + machingNum);
				logger.info("传入的身份证号个数为：" + userList.size());
				// 如果匹配的次数和传入的身份证号个数一致，则直接返回，不在走机器人校验
				if (machingNum.equals(userList.size())) {
					dRet = true;
					// 数据库中存在，则保存到memcache中
					addUsersToMemache(userList);
				}

			}

		}

		// boolean dRet = dbVerify(userList);
		// 如果dRet为true的话，则不进行机器人校验
		if (dRet) {
			logger.info("DB:核验用户信息完成！");
			write2Response(response, getReturnJson(userList));
			return;
		} else {

			// 机器人校验
			boolean rRet = robotVerify(userList, channel, accountId);
			logger.info("Robot:核验用户信息完成！rRet为：" + rRet);
			logger.info("Robot:核验用户信息完成！返回结果为：" + getReturnJson(userList));
			write2Response(response, getReturnJson(userList));
		}
	}

	/**
	 * 处理账户状态
	 * 
	 * @param request
	 * @param response
	 */
	public void updateAccountActive(HttpServletRequest request, HttpServletResponse response) {
		String accId = request.getParameter("acc_id");
		String status = request.getParameter("status");
		String contact_num = request.getParameter("contact_num");
		String accUserName = request.getParameter("acc_username");
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("acc_id", accId);
		updateMap.put("status", status);
		updateMap.put("contact_num", contact_num);
		updateMap.put("acc_username", accUserName);
		if (contact_num != null && contact_num.length() > 0) {
			logger.info("帐号：" + accId + "的常用联系人个数为：" + contact_num);
		}
		if ("02".equals(status)) {
			if (accId != null && accId.length() > 0) {
				logger.info("帐号：" + accId + "已唤醒！");
			}
			if (accUserName != null && accUserName.length() > 0) {
				logger.info("帐号：" + accUserName + "已唤醒！");
			}
		} else if ("03".equals(status)) {
			if (accId != null && accId.length() > 0) {
				logger.info("帐号：" + accId + "未唤醒！");
			}
			if (accUserName != null && accUserName.length() > 0) {
				logger.info("帐号：" + accUserName + "未唤醒！");
			}
		}
		if (accId == null || accId.isEmpty()) {
			if (accUserName != null && !accUserName.isEmpty()) {
				Map<String, String> accountMap = accountService.queryAccountByName(updateMap);
				if (accountMap == null) {
					logger.info("数据库内不存在该账号！");
					return;
				}
			} else {
				logger.info("账户名和账号id都为空，不予处理！");
				return;
			}
		}
		accountService.updateAccountActive(updateMap);
	}

	/**
	 * 更新机器人信息
	 * 
	 * @param request
	 * @param response
	 */
	private void updateWorker(HttpServletRequest request, HttpServletResponse response) {
		String workerId = request.getParameter("worker_id");
		String spareThread = request.getParameter("spare_thread");
		String scriptVersion = request.getParameter("script_version");
		logger.info("机器人id为" + workerId.toString() + ", 空闲进程数为" + spareThread.toString() + ", 脚本版本为" + scriptVersion);
		if (workerId == null || workerId.isEmpty()) {
			logger.info("机器人id为空，无法更新");
			return;
		}
		int spareThreadInt = Integer.parseInt(spareThread);
		if (spareThreadInt < 0 || spareThreadInt > 20) {
			logger.error("机器人获取空闲线程数发生错误，不予更新");
			return;
		}
		Map<String, Object> workerMap = new HashMap<String, Object>();

		workerMap.put("worker_id", Integer.parseInt(workerId));
		workerMap.put("spare_thread", Integer.parseInt(spareThread));
		if (scriptVersion != null && !"".equalsIgnoreCase(scriptVersion.trim())) {
			workerMap.put("script_version", Integer.parseInt(scriptVersion));
		}
		logger.info("更新机器人参数为:" + workerMap);

		workerService.updateWorker(workerMap);

	}

	/**
	 * 通过memcache缓存校验
	 * 
	 * @param userList
	 * @return
	 */
	private boolean memcacheVerify(List<Passenger> userList) {
		boolean isFind = true;
		String key = null;
		for (Passenger user : userList) {
			key = "Verify_" + user.getUser_name() + "_" + user.getCert_no();
			logger.info("通过memcache缓存校验,key为：" + key);
			if (null != MemcachedUtil.getInstance().getAttribute(key)) {
				String cacheValue = (String) MemcachedUtil.getInstance().getAttribute(key);
				logger.info("Verify_" + user.getUser_name() + "_" + user.getCert_no() + "结果：" + cacheValue);
				String[] cacheValueArr = cacheValue.split("\\|");
				/**
				 * 因为直接调用memcache校验通过时，核验未通过状态下没有区分信息冒用和核验不通过2种情况,
				 * 所以把核验返回信息也放到value里以便区分
				 */
				if (cacheValueArr.length > 1) {
					user.setMessage(cacheValueArr[1]);
				}
				user.setCheck_status(cacheValueArr[0]);
				// user.setCheck_status((String) MemcachedUtil.getInstance()
				// .getAttribute(key));
				logger.info("memcache缓存校验通过！");
			} else {
				isFind = false;
				logger.info("memcache缓存校验不通过！");
				break;
			}
		}
		return isFind;
	}

	/**
	 * 通过数据库已有数据核验
	 * 
	 * @return
	 */
	private boolean dbVerify(List<Passenger> userList) {

		boolean isExist = accountService.queryAccoutsDBExist(userList);
		if (isExist) {// 数据库中存在，则保存到memcache中
			addUsersToMemache(userList);
		}
		return isExist;
	}

	/**
	 * 通过机器人核验
	 * 
	 * @param userlist
	 * @throws Exception
	 */
	private boolean robotVerify(List<Passenger> userList, String channel, Integer id) {

		// 机器人校验
		Account account = null;
		// 调用AccountService接口获取account add by wangsf
		logger.info("channel" + channel);
		account = accountService.getAccountById(id);

		// account如果为空，则从队列中按渠道获取账号
		if (account == null) {
			String channel19e = "19e"; // 写死
			account = lockAccount(channel19e, userList.size());// 获取账号
			if (account == null) {
				account = lockAccount(channel19e, userList.size());// 获取账号
				if (account == null) {
					return false;
				}
			}

		}

		// 因为接口那边和本工程的实体类属性字段不一致，有几个字段在后面会用到，因此在此做赋值操作
		if (account != null) {
			String accId = account.getId().toString();// 账号ID
			String accUsername = account.getUsername();
			String accPassword = account.getPassword();
			String accStatus = account.getStatus();
			String contactNum = account.getContact().toString();

			account.setAccId(accId);
			logger.info("账号ID为：" + accId);
			account.setAccUsername(accUsername);
			logger.info("用户名为：" + accUsername);
			account.setAccPassword(accPassword);
			account.setAccStatus(accStatus);
			account.setContactNum(contactNum);
		}

		boolean isReqExp = false;// 请求是否异常

		try {

			// 生成一个随机的16位的字符串(设备号）
			String deviceNo = StrUtil.getRandomString(16);
			// 打码方式：0、手动打码 1、机器识别
			String randCodeType = commonService.querySysSettingByKey("rand_code_type");
			// 动态库或黑盒选择 0：动态库 1：黑盒
			String dynamicDepotOrBlackBox = commonService.querySysSettingByKey("dynamicDepot_or_blackBox");

			String weight = deviceWeight(commonService);
			Map<String, String> maps = new HashMap<String, String>();

			maps.put("commond", "trainAudit");// java版脚本核验

			// 添加代理IP开关
			String proxyIPSwitch = commonService.querySysSettingByKey("proxyIP_switch");// 代理IP开关：
																						// 0：代理IP打开
																						// 1：代理IP关闭

			if ("0".equals(proxyIPSwitch)) {
				IpInfo ipInfo = ipInfoService.getIpInfoByType(IpInfo.TYPE_BOOKIP);
				logger.info("根据IP类型获取的代理IP对象为：" + ipInfo);

				if (ipInfo != null) {
					maps.put("proxyIP", ipInfo.getIpExt()); // 代理IP
					logger.info("根据IP类型获取的代理IP地址为：" + ipInfo.getIpExt());
				}
			}

			maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
			maps.put("Sync", "1");// 0异步请求/1同步请求
			maps.put("Timeout", "25000");
			maps.put("ParamCount", String.valueOf(userList.size() + 1));

			if (DeviceWeight.WEIGHT_APP.equals(weight)) {
				maps.put("ScriptPath", "appAddLinkers.lua");
				maps.put("Param1", account.getAccUsername() + "|" + MD5.getMd5(account.getAccPassword()) + "|"
						+ randCodeType + "|" + deviceNo + "|" + dynamicDepotOrBlackBox);
			} else {
				maps.put("ScriptPath", "addLinkers.lua");
				maps.put("Param1", account.getAccUsername() + "|" + account.getAccPassword() + "|" + randCodeType + "|"
						+ deviceNo + "|" + dynamicDepotOrBlackBox);
			}
			Passenger user = null;
			for (int i = 0; i < userList.size(); i++) {
				user = userList.get(i);
				maps.put("Param" + (i + 2), user.getUser_name() + "|" + user.getUser_type() + "|" + user.getCert_type()
						+ "|" + user.getCert_no());
			}
			logger.info("maps=" + maps);
			String params = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");

			int count = commonService.queryHeyanWorkNum(CreateIDUtil.HEYAN);
			Map<String, Object> map_worker = new HashMap<String, Object>();
			int num = CreateIDUtil.getNextNum(count);
			map_worker.put("limit_num", num - 1);
			map_worker.put("worker_type", CreateIDUtil.HEYAN);
			Worker worker = commonService.queryRandomWorker(map_worker);

			logger.info(
					" num :" + num + "current worker is " + worker.workerName + ",worker_ext is " + worker.workerExt);

			String reqResult = PostRequestUtil.getPostRes("UTF-8", worker.getWorkerExt(), params);

			reqResult = reqResult.replace("[\"{", "[{");
			reqResult = reqResult.replace("}\"]", "}]");
			reqResult = reqResult.replace("\\", "");

			logger.info("req robot return Value:" + reqResult);

			if (null == reqResult) {
				logger.error("post request Error retValue:" + reqResult);
				isReqExp = true;
			} else if (reqResult.equals(PostRequestUtil.TIME_OUT)
					|| reqResult.equals(PostRequestUtil.CONNECT_ERROR) | reqResult.equals(PostRequestUtil.URL_ERROR)) {// 请求错误
				logger.error("post request Error retValue:" + reqResult);
				isReqExp = true;
			} else if (reqResult.contains("ErrorCode\":9")) {// 机器人处理超时
				logger.error("robot request Error retValue:" + reqResult);
				isReqExp = true;
			} else if (reqResult.contains("ErrorCode\":7")) {// 机器人处理超时
				logger.error("robot request Error retValue:" + reqResult);
				isReqExp = true;
			} else {
				// 解析结果，将数据缓存到Memcache，同时将通过的数据保存到数据库
				ReturnVo retObj = null;
				ObjectMapper map = new ObjectMapper();
				retObj = map.readValue(reqResult, ReturnVo.class);
				// retValue:{"ErrorCode":0,"ErrorInfo":[{"loginId":"qiufm030411","contacts":[{"idType":"1","ticketType":"1","name":"张俊","id":"320481198702116616","message":"","status":"exist"}],"password":"123456a"}]}

				ReturnData contact = null;
				List<ReturnUser> reUsers = null;

				if (!new Integer(0).equals(retObj.getErrorCode())) {// 机器人返回码，0代表成功
					logger.error("robot return code is not zero:" + reqResult);
					isReqExp = true;
				} else if (retObj.getErrorInfo() == null || retObj.getErrorInfo().size() == 0) {
					logger.error("robot return data package is empty:" + reqResult);
					isReqExp = true;
				} else {
					contact = retObj.getErrorInfo().get(0);
					reUsers = contact.getContacts();
					if (reUsers == null || reUsers.size() == 0) {
						logger.error("contact is Empty:" + reqResult);
						isReqExp = true;
					}
					// 设置审核状态
					String check_status = null;// 审核状态
					List<Passenger> container = new ArrayList<Passenger>();
					for (Passenger p : userList) {
						for (ReturnUser reUser : reUsers) {
							check_status = reUser.getStatus();
							p.setMessage(reUser.getMessage());

							if (null == reUser.getId() || null == reUser.getName()) {

								String verifyControlChannel = commonService
										.querySysSettingByKey("verify_null_control_channel");
								logger.info(p.getCert_no() + ":核验结果用户信息不完整~verifyNullControlChannel: "
										+ verifyControlChannel);
								logger.info(p.getCert_no() + ": 核验结果用户信息不完整，channel:" + channel);

								if (verifyControlChannel != null && verifyControlChannel.indexOf(channel) > -1) {
									// 当核验返回结果中没有用户名这些信息时，直接核验不通过
									logger.info(p.getCert_no() + ": 核验结果用户信息不完整，核验不通过:" + reqResult);
									p.setCheck_status(Passenger.CHECK_NOTPASSED);
								}
							} else {

								if (p.getCert_no().equals(reUser.getId())
										&& p.getUser_name().equals(reUser.getName())) {
									String verifyNullControlChannel = commonService
											.querySysSettingByKey("verify_null_control_channel");
									logger.info(
											p.getCert_no() + ":verifyNullControlChannel: " + verifyNullControlChannel);
									if (StringUtils.isEmpty(check_status)) {
										if (verifyNullControlChannel != null
												&& verifyNullControlChannel.indexOf(channel) > -1) {
											logger.info(p.getCert_no() + ":当核验返回状态为空时，status is empty,控制为失败");
											p.setCheck_status(Passenger.CHECK_NOTPASSED);
										} else {
											logger.info(p.getCert_no() + "~" + p.getUser_name()
													+ " status is empty,default passed!");
											p.setCheck_status(Passenger.CHECK_PASSED);
										}
									} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_FAILUE)) {
										logger.info(p.getCert_no() + "~" + p.getUser_name()
												+ " is failure,default passed!");
										p.setCheck_status(Passenger.CHECK_NOTPASSED);// 添加失败
										// 冒用算未通过审核
										container.add(p);
									} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_EXIST)) {
										logger.info(p.getCert_no() + "~" + p.getUser_name() + " is passed!");
										p.setCheck_status(Passenger.CHECK_PASSED);// 通过核验
										container.add(p);

									} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_CHECKING)) {
										logger.info(p.getCert_no() + "~" + p.getUser_name() + " is checking!");
										p.setCheck_status(Passenger.CHECKING);// 核验中
										container.add(p);
									} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_NOPASS)) {
										logger.info(p.getCert_no() + "~" + p.getUser_name() + " is not passed!");
										p.setCheck_status(Passenger.CHECK_NOTPASSED);// 未通过核验
										container.add(p);
									} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_LIMIT)) {
										logger.info(p.getCert_no() + "~" + account.getAccUsername() + " is full,"
												+ p.getUser_name() + " default passed!");
										int count_ac = accountService.updateLimitAccountStop(account);
										if (count_ac == 1) {
											logger.info(account.getAccUsername() + " is full, stop account!");
										}
										if (verifyNullControlChannel != null
												&& verifyNullControlChannel.indexOf(channel) > -1) {
											logger.info(p.getCert_no() + "~" + "当核验返回状态为空时，status is empty,控制为失败");
											p.setCheck_status(Passenger.CHECK_NOTPASSED);
										} else {
											p.setCheck_status(Passenger.CHECK_PASSED);// 常用联系人达上限
										}
									} else {
										logger.info(p.getCert_no() + "~" + account.getAccUsername()
												+ " verify status is unkown:" + check_status + " default passed!");
										if (verifyNullControlChannel != null
												&& verifyNullControlChannel.indexOf(channel) > -1) {
											logger.info(p.getCert_no() + "~" + "当核验返回状态为空时，status is empty,控制为失败");
											p.setCheck_status(Passenger.CHECK_NOTPASSED);
										} else {
											p.setCheck_status(Passenger.CHECK_PASSED);
										}
									}
									break;
								}
							}
						}
					}
					// 将乘客信息置入Memcache
					addUsersToMemache(container);

					// 将通过审核的乘客保存到数据库
					addPassedUserToDB(container, account);

				}
			}
		} catch (Exception e) {
			logger.error("robotVerify exception", e);
			isReqExp = true;

		}
		if (isReqExp) {
			releaseAccount(account);// 释放账号
			String verifyNullControlChannel = commonService.querySysSettingByKey("verify_null_control_channel");
			logger.info("verifyNullControlChannel" + verifyNullControlChannel);
			logger.info("核验不通过的情况下，channel为：" + channel);
			if (verifyNullControlChannel != null && verifyNullControlChannel.indexOf(channel) > -1) {
				for (Passenger p : userList) {
					p.setCheck_status(Passenger.CHECK_NOTPASSED);// 核验不通过
					logger.info(p.getCert_no() + "：核验不通过的情况下，Check_status为：" + p.getCheck_status());
				}
			}
			return true;
		} else {
			releaseAccount(account);// 释放账号
			logger.info("robotVerify success!");
			return false;
		}
	}

	/**
	 * 获取返回JSON
	 * 
	 * @param userList
	 * @return
	 */
	private String getReturnJson(List<Passenger> userList) {
		return JSONArray.fromObject(userList).toString();
	}

	/**
	 * 获取账号
	 * 
	 * @return
	 */
	Map<String, Object> paramMap = new HashMap<String, Object>();

	private Account lockAccount(String channel, Integer num) {
		// paramMap.put("acc_status", Account.FREE);
		// paramMap.put("channel", channel);
		// paramMap.put("num", num);
		// Account account = accountService.queryAccountForVerify(paramMap);

		// 改造成从队列中按渠道获取账号
		Account account = accountService.getChannelAccount(channel, num);

		if (account == null) {
			// 取账号联系人个数小于10的12306账号
			account = accountService.getChannelAccount(channel, 0);
		}

		if (account != null) {
			logger.info("锁定账号成功，accUsername=" + account.getAccUsername());
		} else {
			logger.error("锁定账号失败！");
		}
		return account;
	}

	/**
	 * 释放账号
	 * 
	 * @param account
	 */
	private void releaseAccount(Account account) {
		if (account != null) {
			int count = accountService.updateAccoutFree(account);
			if (count == 0) {
				logger.error("释放账号失败，accUsername=" + account.getAccUsername());
			} else {
				logger.info("释放账号成功，accUsername=" + account.getAccUsername());
			}
		} else {
			logger.info("释放账号失败，传入的account为空！");
		}
	}

	/**
	 * 将审核结果保存到memcache 缓存10分钟
	 */
	private void addUsersToMemache(List<Passenger> users) {
		String key = null;
		for (Passenger user : users) {
			key = "Verify_" + user.getUser_name() + "_" + user.getCert_no();
			logger.info("将审核结果保存到memcache,key为：" + key);

			/**
			 * 因为直接调用memcache校验通过时，核验未通过状态下没有区分信息冒用和核验不通过2种情况,
			 * 所以把核验返回信息也放到value里以便区分
			 */
			MemcachedUtil.getInstance().setAttribute(key, user.getCheck_status() + "|" + user.getMessage(),
					10 * 60 * 1000);
			logger.info("将审核结果保存到memcache缓存中成功！");
		}
	}

	/**
	 * 将通过审核的用户信息保存到数据库
	 * 
	 * @param users
	 */
	private void addPassedUserToDB(List<Passenger> users, Account account) {
		List<PassWhiteListVo> afs = new ArrayList<PassWhiteListVo>();
		Set<String> container = new HashSet<String>();
		PassWhiteListVo pwo = null;
		for (Passenger p : users) {
			pwo = new PassWhiteListVo();
			if (StringUtils.isNotEmpty(p.getCheck_status()) && Passenger.CHECK_PASSED.equals(p.getCheck_status())) {
				if (!container.contains(p.getCert_no())) {
					container.add(p.getCert_no());
					pwo.setAcc_id(Integer.parseInt(account.getAccId()));
					pwo.setCert_no(p.getCert_no());
					pwo.setContact_name(p.getUser_name());
					logger.info("account.getAccUsername()" + account.getAccUsername());
					logger.info("account.getAccStatus()" + account.getAccStatus());
					pwo.setAcc_status(account.getAccUsername());
					pwo.setCert_type(p.getCert_type());
					afs.add(pwo);
				}
			}
		}
		accountService.addUserToPassWhiteList(afs);
	}

	public static void main(String[] args) throws Exception {
		//// Set<String> container = new HashSet<String>();
		//// String a = new String("123");
		//// container.add(a);
		//// String b = new String("123");
		//// if (container.contains(b)) {
		//// System.out.println("yes");
		//// } else {
		//// System.out.println("no");
		//// }
		//// if (a == b) {
		//// System.out.println("equal");
		//// } else {
		//// System.out.println("not equal");
		//// }
		// String randCodeType = "0";
		//
		// Map<String, String> maps = new HashMap<String, String>();
		// maps.put("ScriptPath", "appAddLinkers.lua");
		// maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
		// maps.put("Sync", "1");// 0异步请求/1同步请求
		// maps.put("Timeout", "25000");
		// maps.put("ParamCount", "2");
		// maps.put("Param1", "JTH1998082823" + "|"
		// + MD5.getMd5("JTH600476") + "|" + randCodeType);
		//
		// List<Passenger> userList = new ArrayList<Passenger>();
		// Passenger passenger = new Passenger();
		// passenger.setCert_no("371521199110107576");
		// passenger.setCert_type("2");
		// passenger.setUser_name("毛瑞文");
		// passenger.setUser_type("0");
		// userList.add(passenger);
		// Passenger user = null;
		// for (int i = 0; i < userList.size(); i++) {
		// user = userList.get(i);
		// maps.put("Param" + (i + 2), user.getUser_name() + "|"
		// + user.getUser_type() + "|" + user.getCert_type() + "|"
		// + user.getCert_no());
		// }
		// logger.info("maps=" + maps);
		// String params = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
		// System.out.println(params);
	}

	/**
	 * 权重
	 * 
	 * @throws DatabaseException
	 * @throws RepeatException
	 */
	private String deviceWeight(CommonService service) {
		/* PC&APP 模式权重分配 */
		String defaultWeightMode = DeviceWeight.WEIGHT_PC;

		try {
			String pcWeightValue = service.querySysSettingByKey("device_checkLinkers_" + DeviceWeight.WEIGHT_PC);// pc权重
			String appWeightValue = service.querySysSettingByKey("device_checkLinkers_" + DeviceWeight.WEIGHT_APP);// app权重

			logger.info("权重系统设置pc_weight ,PC端: " + pcWeightValue + "app_weight ,移动端: " + appWeightValue);
			/* 设置权重置 */
			List<DeviceWeight> modeCategories = new ArrayList<DeviceWeight>();// 放各个权重的集合

			DeviceWeight pcMode = new DeviceWeight(DeviceWeight.WEIGHT_PC, Integer.parseInt(pcWeightValue));// pc权重
			DeviceWeight appMode = new DeviceWeight(DeviceWeight.WEIGHT_APP, Integer.parseInt(appWeightValue));// app权重

			modeCategories.add(pcMode);
			modeCategories.add(appMode);

			int count = 0;
			for (DeviceWeight category : modeCategories) {
				count += category.getWeight();
			}
			if(count == 0){
				//new Random().nextInt(0)回报异常java.lang.IllegalArgumentException: n must be positive
				count = 1;
			}
			logger.info("权重最大边界 count : " + count);
			Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0, weightSum]
			logger.info("权重随机值 nchannel : " + nchannel);
			Integer mchannel = 0;
			for (DeviceWeight weightCategory : modeCategories) {
				if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
					defaultWeightMode = weightCategory.getCategory();
					break;
				}
				mchannel += weightCategory.getWeight();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return defaultWeightMode;

	}

	/**
	 * 账号核验
	 * 
	 * @param args
	 * @throws IOException
	 */
	public void accountVerify(HttpServletRequest request, HttpServletResponse response) {
		String data = this.getParam(request, "data");
		// 打码方式：0、手动打码 1、机器识别
		String randCodeType = commonService.querySysSettingByKey("rand_code_type");
		JSONArray array = JSONArray.fromObject(data);
		String SessionID = String.valueOf(System.currentTimeMillis());
		String Timeout = "1200000";
		int count = commonService.queryHeyanWorkNum(CreateIDUtil.HEYAN);
		Map<String, Object> map_worker = new HashMap<String, Object>();
		int num = CreateIDUtil.getNextNum(count);
		map_worker.put("limit_num", num - 1);
		map_worker.put("worker_type", CreateIDUtil.HEYAN);
		Worker worker = commonService.queryRandomWorker(map_worker);
		String workerId = worker.getWorkerId();
		String ParamCount = String.valueOf(array.size() + 1);
		String uri = worker.getWorkerExt() + "?ScriptPath=appCheckAccount.lua";
		for (int i = 0; i < array.size(); i++) {
			Map map = (Map) array.get(i);
			String username = map.get("username").toString();
			System.out.println(" username  " + username);
			String pass = map.get("pass").toString();
			pass = Md5Encrypt.md5(pass, "UTF-8");
			String Param1 = username + "|" + pass + "||" + randCodeType + "|" + workerId;
			String url = uri + "&SessionID=" + SessionID + "&Timeout=" + Timeout + "&ParamCount=" + ParamCount
					+ "&Param1=" + Param1 + "&commond=appCheckAccount";
			logger.info("请求帐号核验url" + url);
			String returnStr = HttpUtil.sendByGet(url, "UTF-8", "1800000", "1800000");
			logger.info("请求帐号核验返回" + returnStr);
			printJson(response, returnStr);
		}
	}

	/**
	 * 查询账号下联系人
	 * 
	 * @param args
	 */
	public void queryAccountUse(HttpServletResponse response, HttpServletRequest request) {
		String data = this.getParam(request, "data");
		logger.info("查询账号联系人,参数：" + data);
		// 打码方式：0、手动打码 1、机器识别
		String randCodeType = commonService.querySysSettingByKey("rand_code_type");
		JSONArray array = JSONArray.fromObject(data);
		String SessionID = String.valueOf(System.currentTimeMillis());
		String Timeout = "1200000";
		int count = commonService.queryHeyanWorkNum(CreateIDUtil.HEYAN);
		Map<String, Object> map_worker = new HashMap<String, Object>();
		int num = CreateIDUtil.getNextNum(count);
		map_worker.put("limit_num", num - 1);
		map_worker.put("worker_type", CreateIDUtil.HEYAN);
		Worker worker = commonService.queryRandomWorker(map_worker);
		String workerId = worker.getWorkerId();
		String ParamCount = String.valueOf(array.size() + 1);
		// String uri =
		// worker.getWorkerExt()+"?ScriptPath=appQueryPassenger.lua";
		String uri = "http://139.196.26.235:18090/RunScript?ScriptPath=appQueryPassenger.lua";

		for (int i = 0; i < array.size(); i++) {
			Map map = (Map) array.get(i);
			String username = map.get("trainAccount").toString();
			String pass = map.get("pass").toString();
			pass = Md5Encrypt.md5(pass, "UTF-8");
			String Param1 = username + "|" + pass + "||" + randCodeType + "|" + workerId;
			String url = uri + "&SessionID=" + SessionID + "&Timeout=" + Timeout + "&ParamCount=" + ParamCount
					+ "&Param1=" + Param1;
			logger.info("查询账号常用联系人地址:" + url);
			String returnStr = HttpUtil.sendByGet(url, "UTF-8", "1800000", "1800000");
			logger.info("查询账号常用联系人结果:" + returnStr);
			printJson(response, returnStr);
		}
	}

	/**
	 * 删除常用联系人
	 * 
	 * @param args
	 */
	public void deleteContact(HttpServletRequest request, HttpServletResponse response) {
		String data = this.getParam(request, "data");
		// 打码方式：0、手动打码 1、机器识别
		String randCodeType = commonService.querySysSettingByKey("rand_code_type");
		JSONArray array = JSONArray.fromObject(data);
		String SessionID = String.valueOf(System.currentTimeMillis());
		String Timeout = "1200000";
		int count = commonService.queryHeyanWorkNum(CreateIDUtil.HEYAN);
		Map<String, Object> map_worker = new HashMap<String, Object>();
		int num = CreateIDUtil.getNextNum(count);
		map_worker.put("limit_num", num - 1);
		map_worker.put("worker_type", CreateIDUtil.HEYAN);
		Worker worker = commonService.queryRandomWorker(map_worker);
		String workerId = worker.getWorkerId();
		String ParamCount = String.valueOf(array.size() + 1);
		String uri = worker.getWorkerExt() + "?ScriptPath=delePassenger.lua";
		for (int i = 0; i < array.size(); i++) {
			Map map = (Map) array.get(i);
			String username = map.get("trainAccount").toString();
			String pass = map.get("pass").toString();
			String identy = map.get("identy").toString();
			pass = Md5Encrypt.md5(pass, "UTF-8");
			String Param1 = username + "|" + pass + "||" + identy + "|" + randCodeType + "|" + workerId;
			String url = uri + "&SessionID=" + SessionID + "&Timeout=" + Timeout + "&ParamCount=" + ParamCount
					+ "&Param1=" + Param1;
			String returnStr = HttpUtil.sendByGet(url, "UTF-8", "1800000", "1800000");
			printJson(response, returnStr);
		}
	}

	// 添加常用联系人
	public void add_contact(HttpServletRequest request, HttpServletResponse response) {
		String data = this.getParam(request, "data");
		String channel = this.getParam(request, "channel");// 渠道

		List<Passenger> userList = new ArrayList<Passenger>();
		JSONArray passengers = JSONArray.fromObject(data);
		Passenger passenger = null;

		JSONObject jsobj = null;
		Account account = new Account();
		for (int i = 0; i < passengers.size(); i++) {
			passenger = new Passenger();

			jsobj = passengers.getJSONObject(i);
			passenger.setCert_no(jsobj.getString("cert_no"));
			passenger.setCert_type(jsobj.getString("cert_type"));
			passenger.setUser_name(jsobj.getString("user_name"));
			passenger.setUser_type(jsobj.getString("contact_type"));
			passenger.setCheck_status("0");// 初始化默认通过核验
			account.setAccUsername(jsobj.getString("trainAccount"));
			account.setAccPassword(jsobj.getString("pass"));
			userList.add(passenger);
		}
		// 机器人校验
		boolean rRet = addrobot(userList, channel, account);
		logger.info("Robot:核验用户信息完成！");
		write2Response(response, getReturnJson(userList));
	}

	// 机器人添加联系人
	public boolean addrobot(List<Passenger> userList, String channel, Account account) {
		// Account account = lockAccount(channel, userList.size());// 获取账号
		// if (account == null) {
		// account = lockAccount(channel, userList.size());// 获取账号
		// if(account == null){
		// return false;
		// }
		// }
		boolean isReqExp = false;// 请求是否异常

		try {
			// 打码方式：0、手动打码 1、机器识别
			String randCodeType = commonService.querySysSettingByKey("rand_code_type");

			Map<String, String> maps = new HashMap<String, String>();
			maps.put("ScriptPath", "addLinkers.lua");
			maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
			maps.put("Sync", "1");// 0异步请求/1同步请求
			maps.put("Timeout", "25000");
			maps.put("ParamCount", String.valueOf(userList.size() + 1));
			maps.put("Param1", account.getAccUsername() + "|" + account.getAccPassword() + "|" + randCodeType);

			Passenger user = null;
			for (int i = 0; i < userList.size(); i++) {
				user = userList.get(i);
				maps.put("Param" + (i + 2), user.getUser_name() + "|" + user.getUser_type() + "|" + user.getCert_type()
						+ "|" + user.getCert_no());
			}
			logger.info("maps=" + maps);
			String params = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");

			int count = commonService.queryHeyanWorkNum(CreateIDUtil.HEYAN);
			Map<String, Object> map_worker = new HashMap<String, Object>();
			int num = CreateIDUtil.getNextNum(count);
			map_worker.put("limit_num", num - 1);
			map_worker.put("worker_type", CreateIDUtil.HEYAN);
			Worker worker = commonService.queryRandomWorker(map_worker);

			logger.info(
					" num :" + num + "current worker is " + worker.workerName + ",worker_ext is " + worker.workerExt);

			String reqResult = PostRequestUtil.getPostRes("UTF-8", worker.getWorkerExt(), params);

			reqResult = reqResult.replace("[\"{", "[{");
			reqResult = reqResult.replace("}\"]", "}]");
			reqResult = reqResult.replace("\\", "");

			logger.info("req robot return Value:" + reqResult);

			if (null == reqResult) {
				logger.error("post request Error retValue:" + reqResult);
				isReqExp = true;
			} else if (reqResult.equals(PostRequestUtil.TIME_OUT)
					|| reqResult.equals(PostRequestUtil.CONNECT_ERROR) | reqResult.equals(PostRequestUtil.URL_ERROR)) {// 请求错误
				logger.error("post request Error retValue:" + reqResult);
				isReqExp = true;
			} else if (reqResult.contains("ErrorCode\":9")) {// 机器人处理超时
				logger.error("robot request Error retValue:" + reqResult);
				isReqExp = true;
			} else if (reqResult.contains("ErrorCode\":7")) {// 机器人处理超时
				logger.error("robot request Error retValue:" + reqResult);
				isReqExp = true;
			} else {
				// 解析结果，将数据缓存到Memcache，同时将通过的数据保存到数据库
				ReturnVo retObj = null;
				ObjectMapper map = new ObjectMapper();
				retObj = map.readValue(reqResult, ReturnVo.class);
				// retValue:{"ErrorCode":0,"ErrorInfo":[{"loginId":"qiufm030411","contacts":[{"idType":"1","ticketType":"1","name":"张俊","id":"320481198702116616","message":"","status":"exist"}],"password":"123456a"}]}

				ReturnData contact = null;
				List<ReturnUser> reUsers = null;

				if (!new Integer(0).equals(retObj.getErrorCode())) {// 机器人返回码，0代表成功
					logger.error("robot return code is not zero:" + reqResult);
					isReqExp = true;
				} else if (retObj.getErrorInfo() == null || retObj.getErrorInfo().size() == 0) {
					logger.error("robot return data package is empty:" + reqResult);
					isReqExp = true;
				} else {
					contact = retObj.getErrorInfo().get(0);
					reUsers = contact.getContacts();
					if (reUsers == null || reUsers.size() == 0) {
						logger.error("contact is Empty:" + reqResult);
						isReqExp = true;
					}
					// 设置审核状态
					String check_status = null;// 审核状态
					List<Passenger> container = new ArrayList<Passenger>();
					for (Passenger p : userList) {
						for (ReturnUser reUser : reUsers) {
							check_status = reUser.getStatus();
							if (p.getCert_no().equals(reUser.getId()) && p.getUser_name().equals(reUser.getName())) {
								if (StringUtils.isEmpty(check_status)) {
									logger.info(p.getUser_name() + " status is empty,default passed!");
									p.setCheck_status(Passenger.CHECK_PASSED);

								} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_FAILUE)) {
									logger.info(p.getUser_name() + " is failure,default passed!");
									p.setCheck_status(Passenger.CHECK_NOTPASSED);// 添加失败
									// 算未通过审核

								} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_EXIST)) {
									logger.info(p.getUser_name() + " is passed!");
									p.setCheck_status(Passenger.CHECK_PASSED);// 通过核验
									container.add(p);

								} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_CHECKING)) {
									logger.info(p.getUser_name() + " is checking!");
									p.setCheck_status(Passenger.CHECKING);// 核验中
									container.add(p);
								} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_NOPASS)) {
									logger.info(p.getUser_name() + " is not passed!");
									p.setCheck_status(Passenger.CHECK_NOTPASSED);// 未通过核验
									container.add(p);
								} else if (check_status.equalsIgnoreCase(ReturnUser.STATUS_LIMIT)) {
									logger.info(account.getAccUsername() + " is full," + p.getUser_name()
											+ " default passed!");
									int count_ac = accountService.updateLimitAccountStop(account);
									if (count_ac == 1) {
										logger.info(account.getAccUsername() + " is full, stop account!");
									}

									p.setCheck_status(Passenger.CHECK_PASSED);// 常用联系人达上限
								} else {
									logger.info(account.getAccUsername() + " add status is unkown:" + check_status
											+ " default passed!");
									p.setCheck_status(Passenger.CHECK_PASSED);
								}
								break;
							}
						}
					}
					// 将乘客信息置入Memcache
					addUsersToMemache(container);

					// 将通过审核的乘客保存到数据库
					// addPassedUserToDB(container, account);

				}
			}
		} catch (Exception e) {
			logger.error("addrobot exception", e);
			isReqExp = true;
		}
		if (isReqExp) {
			// releaseAccount(account);// 释放账号
			logger.info("addrobot occurs some exception, default all passed!");
			return true;
		} else {
			//
			logger.info("addrobot success!");
			return false;
		}
	}

	public void addAlipayCheckCode(HttpServletRequest request, HttpServletResponse response) {
		String data = this.getParam(request, "data");
		logger.info("传入的参数解码前" + data);
		if (data.length() > 5000) {
			write2Response(response, "fail");
			return;
		}
		try {
			if (data != null && !"".equals(data)) {
				data = URLDecoder.decode(data, "utf-8");
				logger.info("传入的参数解码后" + data);
				String[] dataArray = data.split("@#@");
				if (dataArray.length > 0) {
					for (String dataStr : dataArray) {
						String comNo = dataStr.split(",")[0];
						if ("COM18".equalsIgnoreCase(comNo)) {
							redisService.RPUSH("AIRLINE_MESSAGE", dataStr);
							logger.info("[COM18]机票的验证码:" + dataStr + "，存入redis成功");
						} else if (dataStr.indexOf("正在尝试登录支付宝") >= 0) {

							String alipayCheckCode = data.substring(data.indexOf("校验码") + 4, data.indexOf("校验码") + 10);
							logger.info("支付宝登录验证码:" + alipayCheckCode);
							redisService.RPUSH("AlipayLoginCode", alipayCheckCode);
						} else if (dataStr.indexOf("校验码") > -1 && dataStr.indexOf("COM") == 0) {

							// COM28,2016-09-29
							// 12:54:32,106980095188,15201005154
							// 10,【支付宝】你的支付宝正在发生24.50元的交易，校验码：872566，打死都不能告诉别人哦！唯一热线95188
							if (dataStr.indexOf("尝试登录支付宝") > -1) {
								String alipayCheckCode = data.substring(data.indexOf("校验码") + 4,
										data.indexOf("校验码") + 10);
								logger.info("alipayCheckCode:" + alipayCheckCode);
								CardInfoVo cardInfoVo = workerService.queryCardInfoVoByComNo(comNo);
								if (cardInfoVo != null && cardInfoVo.getCard_no() != null) {
									Map insertMap = new HashMap();
									insertMap.put("worker_id", cardInfoVo.getWorker_id());
									insertMap.put("card_id", cardInfoVo.getCard_id());
									insertMap.put("card_no", cardInfoVo.getCard_no());
									insertMap.put("verification_code", alipayCheckCode);
									workerService.insertAlipayCodeInfo(insertMap);
								}
							} else {
								String alipayCheckCodeTemp = dataStr.substring(dataStr.indexOf("校验码") + 3,
										dataStr.indexOf("校验码") + 10);
								String alipayCheckCode = "";
								if (alipayCheckCodeTemp.indexOf("：") > -1) {
									alipayCheckCode = alipayCheckCodeTemp.substring(1, 7);
								} else {
									alipayCheckCode = alipayCheckCodeTemp.substring(0, 6);
								}
								logger.info("alipayCheckCode:" + alipayCheckCode);
								CardInfoVo cardInfoVo = workerService.queryCardInfoVoByComNo(comNo);
								if (cardInfoVo != null && cardInfoVo.getCard_no() != null) {
									// 放入redis
									// redisService.setVal("alipay_"+cardInfoVo.getCard_no(),
									// alipayCheckCode, 15);//15s
									// logger.info("[redis]设置key:"+"alipay_"+cardInfoVo.getCard_no()+",
									// value:"+alipayCheckCode);
									String queueKey = getAlipayQueue(cardInfoVo.getCard_no());
									redisService.LPUSH(queueKey, alipayCheckCode);
									logger.info("[redis queue]LPUSH key:" + queueKey + ", value:" + alipayCheckCode);
									// 存库
									Map insertMap = new HashMap();
									insertMap.put("worker_id", cardInfoVo.getWorker_id());
									insertMap.put("card_id", cardInfoVo.getCard_id());
									insertMap.put("card_no", cardInfoVo.getCard_no());
									insertMap.put("verification_code", alipayCheckCode);
									workerService.insertWorkerCodeInfo(insertMap);
								}
							}
						} else if (dataStr.indexOf("建设银行") > -1 && dataStr.indexOf("COM") == 0
								&& dataStr.indexOf("验证码") > -1) {
							String ccbCode = dataStr.substring(dataStr.indexOf("验证码") + 3, dataStr.indexOf("验证码") + 9);
							logger.info("ccbCode:" + ccbCode + ",com:" + comNo);
							Map<String, String> map = new HashMap<String, String>();
							map.put("comNo", comNo);
							map.put("bank_type", "11");// 银行类型 00、中国银行 11、建设银行
														// 22、中铁银通卡 33、支付宝
														// 44、招商银行
							CardBankVo cardBank = workerService.queryCardBank(map);
							logger.info("ccb-CardNo:" + cardBank.getCard_no());
							if (cardBank != null && cardBank.getCard_no() != null) {
								// 放入redis
								String queueKey = getBankQueue(cardBank.getCard_no());
								redisService.LPUSH(queueKey, ccbCode);
								logger.info("[redis queue]LPUSH key:" + queueKey + ", value:" + ccbCode);
								// 存库
								Map insertMap = new HashMap();
								insertMap.put("worker_id", cardBank.getWorker_id());
								insertMap.put("card_id", cardBank.getCard_id());
								insertMap.put("card_no", cardBank.getCard_no());
								insertMap.put("verification_code", ccbCode);
								workerService.insertWorkerCodeInfo(insertMap);
							}
						} else if (dataStr.indexOf("招商银行") > -1 && dataStr.indexOf("COM") == 0
								&& dataStr.indexOf("验证码") > -1) {
							String cmbCode = dataStr.substring(dataStr.indexOf("验证码") + 4, dataStr.indexOf("验证码") + 10);
							logger.info("cmbCode:" + cmbCode + ",com:" + comNo);
							Map<String, String> map = new HashMap<String, String>();
							map.put("comNo", comNo);
							map.put("bank_type", "44");// 银行类型 00、中国银行 11、建设银行
														// 22、中铁银通卡 33、支付宝
														// 44、招商银行
							CardBankVo cardBank = workerService.queryCardBank(map);
							logger.info("ccb-CardNo:" + cardBank.getCard_no());
							if (cardBank != null && cardBank.getCard_no() != null) {
								// 放入redis
								String queueKey = getBankQueue(cardBank.getCard_no());
								redisService.LPUSH(queueKey, cmbCode);
								logger.info("[redis queue]LPUSH key:" + queueKey + ", value:" + cmbCode);
								// 存库
								Map insertMap = new HashMap();
								insertMap.put("worker_id", cardBank.getWorker_id());
								insertMap.put("card_id", cardBank.getCard_id());
								insertMap.put("card_no", cardBank.getCard_no());
								insertMap.put("verification_code", cmbCode);
								workerService.insertWorkerCodeInfo(insertMap);
							}
						}
					}
				}

				write2Response(response, "success");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			write2Response(response, "fail");
		}
	}
}
