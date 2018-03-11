package com.l9e.transaction.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.common.BaseController;
import com.l9e.common.TuniuConstant;
import com.l9e.transaction.component.TicketQueryResult;
import com.l9e.transaction.exception.TuniuCommonException;
import com.l9e.transaction.service.TuniuChangeService;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.service.TuniuOrderService;
import com.l9e.transaction.service.TuniuRefundService;
import com.l9e.transaction.service.impl.TuniuChangeServiceImpl;
import com.l9e.transaction.service.impl.TuniuOrderServiceImpl;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Station;
import com.l9e.transaction.vo.SynchronousOutput;
import com.l9e.transaction.vo.TuniuContact;
import com.l9e.transaction.vo.TuniuDeleteContact;
import com.l9e.transaction.vo.TuniuIdentityValidate;
import com.l9e.transaction.vo.TuniuInput;
import com.l9e.transaction.vo.TuniuParameter;
import com.l9e.transaction.vo.TuniuResult;
import com.l9e.transaction.vo.model.Parameter;
import com.l9e.transaction.vo.model.Result;
import com.l9e.util.Base64Util;
import com.l9e.util.ConfigUtil;
import com.l9e.util.EncryptUtil;
import com.l9e.util.HttpCheckAccountUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.JacksonUtil;
import com.l9e.util.UrlFormatUtil;
import com.l9e.util.ut;

/**
 * 途牛火车票业务接口
 * 
 * @author licheng
 * 
 */
@Controller
@RequestMapping("/")
public class TuniuController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(TuniuController.class);

	/**
	 * 途牛通用业务对象
	 */
	@Resource
	private TuniuCommonService tuniuCommonService;
	/**
	 * 途牛订单业务对象
	 */
	@Resource
	private TuniuOrderService tuniuOrderService;
	/**
	 * 途牛退票业务对象
	 */
	@Resource
	private TuniuRefundService tuniuRefundService;
	
	@Resource
	private TuniuChangeService tuniuChangeService;
	
	@Value("#{config}")
	private Properties config;

	/**
	 * 12306账号验证
	 * @throws IOException
	 */
	@RequestMapping(value = "trainAccount/{method}", method = { RequestMethod.POST })
	public void tuniuTrain_Account(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String method)
			throws IOException {
		SynchronousOutput synOutput = new SynchronousOutput();
		if ("validate".equals(method)) {
			if(ut.getHour()>23 || ut.getHour()<6){
				synOutput.setSuccess(false);
				synOutput.setReturnCode("1005");
				Map data = new HashMap();
				data.put("isPass", 1);
				synOutput.setData(data);
				synOutput.setErrorMsg("6:00-23::00之外12306不能提供服务");
			}else{
				// 接收Post传递参数
				String paramStr = recieveParameterUTF8(request, response);
				logger.info("validate途牛传入参数: " + paramStr);
				try {
					TuniuInput input = JacksonUtil.readJson(paramStr,
							TuniuInput.class);
					if (input == null)
						throw new TuniuCommonException(
								TuniuCommonService.RETURN_CODE_PARAM_ERROR);
					/* 通用参数检查 */
					validateCommonParameter(input, TuniuConstant.signKey);
					/* 传入参数 */
					String dataBase64 = input.getData();
					logger.info("validate途牛业务参数密文data :" + dataBase64);
					String data1 = EncryptUtil.decode(dataBase64,
							TuniuConstant.signKey, "UTF-8");
		//			String data1 = Base64Util.getFromBASE64(dataBase64);
					logger.info("validate途牛传入业务参数：" + data1);
					String sign = input.getSign();// 签名
					String timestamp = input.getTimestamp();// 时间戳
					String account = input.getAccount();// 账号
					
					JSONObject json = JSON.parseObject(data1);
					String pass = json.getString("pass");// 密码
					String trainAccount = json.getString("trainAccount");// 12306账号
					Account account_12306 = new Account();
					account_12306.setUsername(trainAccount);
					account_12306.setPassword(pass);
					if (null == account_12306)
						throw new TuniuCommonException(
								TuniuCommonService.RETURN_CODE_PARAM_ERROR);
					
					List list = new ArrayList();
					list.add(account_12306);
					/* 12306账号验证 */
					String flag = validateAccount(account_12306);
					if ("1".equals(flag)) {
						synOutput.setReturnCode("231000");
						synOutput.setErrorMsg("");
						Map data = new HashMap();
						data.put("isPass", 0);
						synOutput.setData(data);
						logger.info(trainAccount+"账号核验"+"信息："+synOutput.getData());
						printJson(response, JacksonUtil.generateJson(synOutput));
					} else if("0".equals(flag)){
						synOutput.setSuccess(false);
						synOutput.setReturnCode("901");
						synOutput.setErrorMsg("");
						Map data = new HashMap();
						data.put("isPass", 1);
						synOutput.setData(data);
						logger.info(trainAccount+"账号核验"+"信息："+synOutput.getData());
						printJson(response, JacksonUtil.generateJson(synOutput));
					}else{
						synOutput.setSuccess(false);
						synOutput.setReturnCode("901");
						if(flag!=null && flag.length()>3){
							synOutput.setErrorMsg(flag.substring(3, flag.length()));
						}else{
							synOutput.setErrorMsg("");
						}
						Map data = new HashMap();
						data.put("isPass", 1);
						synOutput.setData(data);
						logger.info(trainAccount+"账号核验"+"返回错误信息："+synOutput.getErrorMsg());
						printJson(response, JacksonUtil.generateJson(synOutput));
					}
				}catch (Exception e) {
					String errorMsg = e.getMessage();
					synOutput.setSuccess(true);
					synOutput.setReturnCode("231000");
					synOutput.setErrorMsg("");
					Map data = new HashMap();
					data.put("isPass", 0);
					synOutput.setData(data);
					logger.info("账号核验发生异常: " + errorMsg);
					printJson(response, JacksonUtil.generateJson(synOutput));

				}
			}
			
		} else {
			synOutput.setSuccess(false);
			synOutput.setReturnCode("404");
			synOutput.setErrorMsg("账号验证地址有误!");
		}
		printJson(response, JacksonUtil.generateJson(synOutput));
	}

	/**
	 * 查询常用联系人
	 * 
	 * @throws IOException
	 * @throws TuniuCommonException
	 */
	@RequestMapping(value = "trainAccount/contact/{method}", method = { RequestMethod.POST })
	public void tuniuTrain_contact(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String method)
			throws IOException{
		SynchronousOutput synOutput = new SynchronousOutput();
		try {
			if ("query".equals(method)) {
			
				// 接收Post传递参数
				String paramStr = recieveParameterUTF8(request, response);
				logger.info("query途牛传入参数: " + paramStr);
				TuniuInput input = JacksonUtil.readJson(paramStr,
						TuniuInput.class);
				if (input == null)
					throw new TuniuCommonException(
							TuniuCommonService.RETURN_CODE_PARAM_ERROR);
				
				/* 传入参数 */
				String dataBase64 = input.getData();
				logger.info("query途牛业务参数密文data :" + dataBase64);
				String data1 = EncryptUtil.decode(dataBase64,
						TuniuConstant.signKey, "UTF-8");
				logger.info("query途牛传入业务参数：" + data1);
	//			//转对象
				JSONObject obj = JSON.parseObject(data1);
				
				List userList = new ArrayList();
//				// 取出12306账号
					String trainAccount = (String) obj.get("trainAccount");
					String pass = (String) obj.get("pass");
					//账号 密码放入对象
					Account account_12306 = new Account();
					account_12306.setUsername(trainAccount);
					account_12306.setPassword(pass);
					if (null == account_12306)
						throw new TuniuCommonException(
								TuniuCommonService.RETURN_CODE_PARAM_ERROR);
					String flag = queryAccountUse(account_12306);
					if ("error".equals(flag)) {
						logger.info("查询失败");
						synOutput.setSuccess(false);
						synOutput.setReturnCode("1001");
						synOutput.setErrorMsg("查询失败");
						synOutput.setData(null);
					} else {
						logger.info("查询成功");
						logger.info("查询结果"+flag);
						JSONObject jb = JSON.parseObject(flag);
						String info =jb.getString("info");
						JSONArray arrayflag = JSON.parseArray(info);
						for (int j = 0; j < arrayflag.size(); j++) {
							Map maps = (Map)arrayflag.get(j);
							Integer id= (Integer)maps.get("id");//序列号
							String name = maps.get("name").toString(); //姓名
							Integer isUserSelf = (Integer)maps.get("isUserSelf");//是否是账号本人 0是 1不是
//							Integer sex = (Integer)maps.get("sex") ;//性别 0 男 1 女
							String birthday = maps.get("birthday").toString();//生日
							String country = maps.get("country").toString(); //国家 CN中国
							Integer identyType = (Integer) maps.get("identyType");//证件类型
							String identy = maps.get("identy").toString();//证件号码
							Integer personType = (Integer)maps.get("personType"); //旅客类型 0 成人 1儿童
							String phone = maps.get("phone").toString();//手机
							String tel = maps.get("tel").toString(); //固话
							String email = maps.get("email").toString();//邮件
							String address = maps.get("address").toString();//地址
							Integer checkStatus = (Integer)maps.get("checkStatus");//身份是否校验通过 0通过1不通过
							Integer sex = (Integer)maps.get("sex");//性别
							String addTime = maps.get("addTime").toString();//添加时间
							//学生信息
							String provinceName=maps.get("provinceName").toString();//学校所在省份名称
							String schoolName=maps.get("schoolName").toString();//学校名称
							String department=maps.get("department").toString();//院系
							String className=maps.get("className").toString();//班级
							String studentNo=maps.get("studentNo").toString();//学号
							String schoolSystem=maps.get("schoolSystem").toString();//学制
							String enterYear=maps.get("enterYear").toString();//入学年份
							String preferenceNum=maps.get("preferenceNum").toString();//优惠卡号制	
							String preferenceFromStationName=maps.get("preferenceFromStationName").toString();//优惠区间起始地名称
							String preferenceToStationName=maps.get("preferenceToStationName").toString();//优惠区间到达地名称
							
							
							
							Map<String,Object> jsonMap = new HashMap<String,Object>();
							//组合
							jsonMap.put("id", id);
							jsonMap.put("name", name);
							jsonMap.put("isUserSelf", isUserSelf);
							jsonMap.put("birthday", birthday); 
							jsonMap.put("country", country);
							jsonMap.put("identyType", identyType);
							jsonMap.put("identy", identy);
							jsonMap.put("personType", personType-1);
							jsonMap.put("phone", phone);
							jsonMap.put("tel", tel);
							jsonMap.put("email", email);
							jsonMap.put("address", address);
							jsonMap.put("checkStatus", checkStatus);
							jsonMap.put("sex", sex);
							jsonMap.put("addTime", addTime);
							
							jsonMap.put("provinceName", provinceName);
							jsonMap.put("schoolName", schoolName);
							jsonMap.put("department", department);
							jsonMap.put("className", className);
							jsonMap.put("studentNo", studentNo);
							jsonMap.put("schoolSystem", schoolSystem);
							jsonMap.put("enterYear", enterYear);
							jsonMap.put("preferenceNum", preferenceNum);
							jsonMap.put("preferenceFromStationName", preferenceFromStationName);
							jsonMap.put("preferenceToStationName", preferenceToStationName);
										
							userList.add(jsonMap);
							
						}
						logger.info("查询常旅返回结果"+userList);
						
						// 设置返回参数
						synOutput.setReturnCode("231000");
						synOutput.setErrorMsg("");
						String str = JacksonUtil.generateJson(userList);
						logger.info("查询常旅返回结果json："+str);
							   str = URLDecoder.decode(str,"UTF-8");
						String code = EncryptUtil.encode(str, TuniuConstant.signKey, "UTF-8");
						synOutput.setData(code);
					}
//				}
				printJson(response, JacksonUtil.generateJson(synOutput));
			
				// 途牛增加和修改常用联系人
			} else if ("saveOrUpdate".equals(method)) {
	
				// 接收Post传递参数
				String paramStr = recieveParameterUTF8(request, response);
				logger.info("saveOrUpdate途牛传入参数: " + paramStr);
				TuniuInput input = JacksonUtil.readJson(paramStr,
						TuniuInput.class);
				if (input == null)
					throw new TuniuCommonException(
							TuniuCommonService.RETURN_CODE_PARAM_ERROR);
				/* 传入参数 */
				String dataBase64 = input.getData();
				logger.info("saveOrUpdate途牛业务参数密文data :" + dataBase64);
				String data1 = EncryptUtil.decode(dataBase64,
						TuniuConstant.signKey, "UTF-8");
				logger.info("saveOrUpdate途牛传入业务参数：" + data1);
				String timestamp = input.getTimestamp();// 时间戳
				String account = input.getAccount();// 账号
				// 传入参数认证
				TuniuInput tuniuInput = new TuniuInput();
				tuniuInput.setAccount(account);
				tuniuInput.setTimestamp(timestamp);
				
					JSONObject jsonObject = JSON.parseObject(data1);// 两次解析
					String trainAccount = jsonObject.getString("trainAccount");// 账号
					String pass = jsonObject.getString("pass");// 密码
					
					String contacts = jsonObject.getString("contacts");
					JSONArray array = JSONArray.parseArray(contacts);
					for(int i=0;i<array.size();i++){
						String contact = array.getString(i);
						TuniuContact t = JacksonUtil.readJson(contact, TuniuContact.class);
					
					// 账号验证
					Account account_12306 = new Account();
					account_12306.setUsername(trainAccount);
					account_12306.setPassword(pass);
					String saveUpdateContact = saveUpdateContact(account_12306, t);// 0:新增失败;1:更新失败;2:操作成功
					if (null != saveUpdateContact) {
						if ("0".equals(saveUpdateContact)) {
							synOutput.setReturnCode("1101");
							synOutput.setErrorMsg("新增失败");
							break;
						} else if ("1".equals(saveUpdateContact)) {
							synOutput.setReturnCode("1102");
							synOutput.setErrorMsg("更新失败");
							break;
						} else {
							synOutput.setReturnCode("231000");
							synOutput.setErrorMsg("");
							List dataList = new ArrayList();
							synOutput.setData(dataList);
						}
					} else {
						// synOutput.setReturnCode("");
						synOutput.setErrorMsg("查询更新失败");
					}
				}
				printJson(response, JacksonUtil.generateJson(synOutput));
			} else if ("delete".equals(method)) {
	
				String paramStr = recieveParameterUTF8(request, response);
				logger.info("delete途牛传入参数: " + paramStr);
				TuniuInput input = JacksonUtil.readJson(paramStr,
						TuniuInput.class);
				String timestamp = input.getTimestamp();// 时间戳
				String account = input.getAccount();// 账号
				
				/* 传入参数 */
				String dataBase64 = input.getData();
				logger.info("delete途牛业务参数密文data :" + dataBase64);
				String data1 = EncryptUtil.decode(dataBase64,
						TuniuConstant.signKey, "UTF-8");
				logger.info("delete途牛传入业务参数：" + data1);
	//			//取出串
				String str = JSON.toJSONString(data1).replace("\\", "").replace("\"{", "{").replace("}\"", "}");
				System.out.println("str "+str);
				if (null == input)
					throw new TuniuCommonException(
							TuniuCommonService.RETURN_CODE_PARAM_ERROR);
				/* 通用参数检查 */
	//			validateCommonParameter(tuniuInput, TuniuConstant.signKey);
				TuniuDeleteContact tuniuDeleteContact = JacksonUtil.readJson(str,
						TuniuDeleteContact.class);
				
				boolean deleteContact = deleteContact(tuniuDeleteContact);
				if (deleteContact) {
					System.out.println(" 删除成功");
					synOutput.setErrorMsg("");
					synOutput.setReturnCode("231000");
					List list = new ArrayList();
					synOutput.setData(list);
					printJson(response, JacksonUtil.generateJson(synOutput));
				} else {
					synOutput.setErrorMsg("删除失败");
					synOutput.setReturnCode("1201");
					printJson(response, JacksonUtil.generateJson(synOutput));
				}
			} else {
				synOutput.setReturnCode("404");
				synOutput.setErrorMsg("请求地址有误");
				printJson(response, JacksonUtil.generateJson(synOutput));
			}
		} catch (TuniuCommonException e) {
			logger.info("途牛常旅查询异常: " + e.getMessage());
			logger.info("途牛常旅查询异常: " ,e);
			String errorMsg = tuniuCommonService.getErrorMessage(e
					.getMessage());
			synOutput.setSuccess(false);
			synOutput.setReturnCode(e.getMessage());
			synOutput.setErrorMsg(errorMsg);
			printJson(response, JacksonUtil.generateJson(synOutput));
		}catch(Exception e){
			logger.info("途牛常旅查询异常: " + e.getMessage());
			logger.info("途牛常旅查询异常: ",e);
			synOutput.setSuccess(false);
			synOutput.setReturnCode("231099");
			synOutput.setErrorMsg("操作异常");
			printJson(response, JacksonUtil.generateJson(synOutput));
		}
		
	}
	

	/**
	 * 途牛火车票业务入口
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("train/{method}")
	public void tuniuTrain(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String method) {
		/* 同步输出对象 */
		SynchronousOutput synOutput = new SynchronousOutput();
	
		try {
			String paramStr = recieveParameterUTF8(request, response);
			logger.info("途牛传入参数: " + paramStr);
			try {
				// 身份证验证
				if ("validate".equals(method)) {
					tuniuVerify(response, synOutput, paramStr);// 身份证验证,未使用DES加密
				}else{
					TuniuInput input = JacksonUtil.readJson(paramStr,TuniuInput.class);
					if (input == null)
						throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
					/* 通用参数检查 */
					validateCommonParameter(input, TuniuConstant.signKey);
					/* 传入参数 */
					String dataBase64 = input.getData();
					logger.info("途牛业务参数密文data :" + dataBase64);
	
					String data = EncryptUtil.decode(dataBase64, TuniuConstant.signKey, "UTF-8");
					logger.info("途牛传入业务参数：" + data);
	
					Parameter parameter = new TuniuParameter(data);
					/* 业务接口 */
					if ("search".equals(method)) {//【查询】
						tuniuTrainSearch(parameter, synOutput);
					} else if ("book".equals(method)) {// 途牛【预订下单】接口目前只支持异步
						tuniuTrainBook(parameter, synOutput);
					} else if ("confirm".equals(method)) {// 途牛【确认出票】接口目前只支持异步
						tuniuTrainConfirm(parameter, synOutput);
					} else if ("cancel".equals(method)) {// 途牛【取消】订单接口目前只支持异步
						tuniuTrainCancel(parameter, synOutput);
					} else if ("return".equals(method)) {// 途牛线上【退票 】接口目前只支持异步
						tuniuTrainReturn(parameter, synOutput);
					} else if ("queryStations".equals(method)) {//【途经站】
						tuniuTrainQueryStations(parameter, synOutput);
					} else if ("validate".equals(method)) {//【身份信息验证】
						tuniuTrainValidate(parameter, synOutput);
					}else if("refundNotice".equals(method)){
					    queryRefundNotice(parameter, synOutput);//催退款通知
					}else if ("".equals(method)) {
						logger.info("method参数为空");
					}else{
						throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_INTER_NOT_EXISTS);
					}
				}
			} catch (TuniuCommonException e) {
				String errorMsg = tuniuCommonService.getErrorMessage(e.getMessage());
				synOutput.setSuccess(false);
				synOutput.setReturnCode(e.getMessage());
				synOutput.setErrorMsg(errorMsg);
				logger.info("tuniu_error_msg: " + errorMsg);
			}
			/* 同步结果输出 */
			printJson(response, JacksonUtil.generateJson(synOutput));

		} catch (Exception e) {
			e.printStackTrace();
			String code = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
			String errorMsg = tuniuCommonService.getErrorMessage(code);
			synOutput.setSuccess(false);
			synOutput.setReturnCode(code);
			synOutput.setErrorMsg(errorMsg);
			logger.info("tuniu_unknown_error : " + e.getMessage());
			try {
				printJson(response, JacksonUtil.generateJson(synOutput));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
	
	/**
	 * @param request
	 * @param response
	 * 【途牛推送超时订单】
	 */
	@RequestMapping("train/pushOrderStatus")
	public void tuniuTrain_PushOrderStatus(HttpServletRequest request,
			    HttpServletResponse response) {
		
		try{
			
			String paramStr = recieveParameterUTF8(request, response);
			logger.info("途牛传入参数: " + paramStr);
			TuniuInput input = JacksonUtil.readJson(paramStr,TuniuInput.class);
	
			if (input == null){
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
		    
			/* 通用参数检查 */
			validateCommonParameter(input, TuniuConstant.signKey);
			/* 传入参数 */
			String dataBase64 = input.getData();
			logger.info("途牛业务参数密文data :" + dataBase64);
			String data = EncryptUtil.decode(dataBase64, TuniuConstant.signKey, "UTF-8");
			logger.info("途牛传入业务参数：" + data);
			Parameter parameter = new TuniuParameter(data);
			this.tuniuPushOrderStatus(parameter, null);
			
			printJson(response, "success");
			
		}catch(Exception e){
			/* 同步结果输出 */
			logger.info("处理出现异常："+e.getMessage(),e);
			printJson(response, "failture");
		}
		
	}
	

	
	/**
	 *  查看账号联系人状态
	 * @param response
	 * @param synOutput
	 * @param paramStr
	 * @throws IOException
	 * @throws TuniuCommonException
	 */
	private void tuniuVerify(HttpServletResponse response,
			SynchronousOutput synOutput, String paramStr) throws IOException,
			TuniuCommonException {
		TuniuInput input = JacksonUtil.readJson(paramStr,TuniuInput.class);
		if (input == null)
			throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
		/* 通用参数检查 */
		validateCommonParameter(input, TuniuConstant.signKey);
		/* 传入参数 */
		String dataBase64 = input.getData();
		logger.info("身份证验证途牛业务参数密文data :" + dataBase64);
//					String data1 = EncryptUtil.decode(dataBase64,
//							TuniuConstant.signKey, "UTF-8");
		String data1 = Base64Util.getFromBASE64(dataBase64);
		logger.info("身份证验证途牛传入业务参数：" + data1);
		String sign = input.getSign();// 签名
		String timestamp = input.getTimestamp();// 时间戳
		String account = input.getAccount();// 账号
//					System.out.println(" 签名："+sign+" 时间戳: "+timestamp+" 账号: "+account);
		JSONArray list = JSON.parseArray(data1);
		List listData = new ArrayList();
		TuniuIdentityValidate tiv = new TuniuIdentityValidate();
		for (int i = 0; i < list.size(); i++) {
			Map dataMap = (Map) list.get(i);
			String identityCard = (String) dataMap.get("identityCard");
			String identityType =  (String) dataMap.get("identityType");
			String name = (String) dataMap.get("name");
			tiv.setName(name);
			tiv.setIdentityCard(identityCard);
			tiv.setIdentityType(identityType);
			if (null == tiv)
				throw new TuniuCommonException(
						TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			String tnv = identity(tiv);
			if ("0".equals(tnv)) {
				synOutput.setReturnCode("231000");
				synOutput.setErrorMsg("");
				Map data = new HashMap();
				data.put("identityCard", tiv.getIdentityCard());
				data.put("name", tiv.getName());
				data.put("identityType", tiv.getIdentityType());
				data.put("isPass", 1);
				data.put("error ", "审核通过");
				listData.add(data);
				synOutput.setData(listData);
			} else if ("1".equals(tnv)) {
				synOutput.setReturnCode("231000");
				synOutput.setErrorMsg("");
				Map data = new HashMap();
				data.put("identityCard", tiv.getIdentityCard());
				data.put("name", tiv.getName());
				data.put("identityType", tiv.getIdentityType());
				 data.put("isPass", 0);
				data.put("error", "乘客:"+tiv.getName()+",需待确认");
				listData.add(data);
				synOutput.setData(listData);
			} else if("2".equals(tnv)){
				synOutput.setReturnCode("231000");
				synOutput.setErrorMsg("");
				Map data = new HashMap();
				data.put("identityCard", tiv.getIdentityCard());
				data.put("name", tiv.getName());
				data.put("identityType", tiv.getIdentityType());
				data.put("isPass", 2);
				data.put("error", "乘客:"+tiv.getName()+",身份证未通过核验");
				listData.add(data);
				synOutput.setData(listData);
			}else if("3".equals(tnv)){
				synOutput.setReturnCode("231000");
				synOutput.setErrorMsg("");
				Map data = new HashMap();
				data.put("identityCard", tiv.getIdentityCard());
				data.put("name", tiv.getName());
				data.put("identityType", tiv.getIdentityType());
				data.put("isPass", 3);
				data.put("error", tiv.getName()+"的身份信息涉嫌被他人冒用，为了保障个人信息安全，请您持本人和金勇华的身份证件原件(未成年人可持户口本)到就近办理客运售票业务的铁路车站完成身份核验，通过后即可在网上办理购票业务，谢谢。");
				listData.add(data);
				synOutput.setData(listData);
			}else{
				synOutput.setReturnCode("901");
				synOutput.setErrorMsg("");
				Map data = new HashMap();
				data.put("identityCard", tiv.getIdentityCard());
				data.put("name", tiv.getName());
				data.put("identityType", tiv.getIdentityType());
				data.put("isPass", 4);
				data.put("error", "乘客:"+tiv.getName()+",验证时系统异常");
				listData.add(data);
				synOutput.setData(listData);
			}
		}
		
	}

	/**
	 * @param parameter
	 * @param synOutput
	 * 途牛推送超时订单
	 * @throws TuniuCommonException 
	 */
	public void tuniuPushOrderStatus(Parameter parameter,
			SynchronousOutput synOutput) throws TuniuCommonException {
		tuniuOrderService.tuniuPushOrderStatus(parameter,synOutput);
	}

	/**
	 * 余票查询
	 * 
	 * @param parameter
	 * @param synOutput
	 * @param response
	 */
	private void tuniuTrainSearch(Parameter parameter,SynchronousOutput synOutput) {

		String trainDate = parameter.getString("trainDate");
		String fromStation = parameter.getString("fromStation");
		String toStation = parameter.getString("toStation");
		String trainCode = parameter.getString("trainCode");

		if (StringUtils.isEmpty(trainDate) || StringUtils.isEmpty(fromStation)
				|| StringUtils.isEmpty(toStation)) {
			synOutput.setReturnCode("202");
			synOutput.setErrorMsg("查询失败");
			synOutput.setSuccess(false);
			return;
		}

		Map<String, String> paramMap = new HashMap<String, String>();
		String url = TuniuConstant.QUERY_TICKET;
		paramMap.put("from_station", fromStation);
		paramMap.put("arrive_station", toStation);
		paramMap.put("travel_time", trainDate);
		paramMap.put("train_code", trainCode);
		String params = UrlFormatUtil.createUrl("", paramMap);

		logger.info("途牛发起余票查询 : " + paramMap.toString() + "url" + url);
		String result = HttpUtil.sendByPost(url, params, "UTF-8");

		logger.info("途牛发起余票查询result : " + result);
		if (StringUtils.isEmpty(result)
				|| result.equalsIgnoreCase("STATION_ERROR")
				|| result.equalsIgnoreCase("ERROR")) {
			synOutput.setReturnCode("201");
			synOutput.setErrorMsg("没有符合条件的车次信息");
			synOutput.setSuccess(false);
		} else {
			TicketQueryResult tqr = new TicketQueryResult();
			Object data = tqr.ticketQueryJson(result);
			synOutput.setReturnCode("231000");
			synOutput.setErrorMsg("请求成功");
			synOutput.setSuccess(true);
			synOutput.setData(data);
		}
	}

	/**
	 * 预订下单(异步)
	 * 
	 * @param parameter
	 * @param synOutput
	 * @param response
	 * @throws IOException
	 * @throws TuniuCommonException
	 */
	private void tuniuTrainBook(Parameter parameter,SynchronousOutput synOutput) throws TuniuCommonException {

		String orderId = parameter.getString("orderId");//订单号
		String callBackUrl = parameter.getString("callBackUrl");//回调地址
		
		if (StringUtils.isEmpty(orderId)|| StringUtils.isEmpty(callBackUrl))
		{
			logger.info("途牛预定下单参数错误，订单号或回调地址为空");
			throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
	
		}
		
		Result result = tuniuOrderService.trainBook(parameter);
		wrapSynOutput(result, synOutput);

	}

	/**
	 * 确认出票
	 * 
	 * @param parameter
	 * @param synOutput
	 * @param response
	 * @throws IOException
	 * @throws TuniuCommonException
	 */
	private void tuniuTrainConfirm(Parameter parameter,SynchronousOutput synOutput) throws TuniuCommonException {

		String orderId = parameter.getString("orderId");
		String callBackUrl = parameter.getString("callBackUrl");
		if (StringUtils.isEmpty(orderId)
				|| StringUtils.isEmpty(callBackUrl))
			throw new TuniuCommonException(
					TuniuCommonService.RETURN_CODE_PARAM_ERROR);
		
		Result result = tuniuOrderService.trainConfirm(parameter);
		wrapSynOutput(result, synOutput);
		
	}

	/**
	 * 取消订单
	 * 
	 * @param parameter
	 * @param synOutput
	 * @param response
	 * @throws IOException
	 * @throws TuniuCommonException
	 */
	private void tuniuTrainCancel(Parameter parameter,SynchronousOutput synOutput) throws TuniuCommonException {

		String orderId = parameter.getString("orderId");
		String callBackUrl = parameter.getString("callBackUrl");
		if (StringUtils.isEmpty(orderId)
				|| StringUtils.isEmpty(callBackUrl))
			throw new TuniuCommonException(
					TuniuCommonService.RETURN_CODE_PARAM_ERROR);
		
		Result result = tuniuOrderService.trainCancel(parameter);
		wrapSynOutput(result, synOutput);
	}

	/**
	 * 线上退票
	 * 
	 * @param parameter
	 * @param synOutput
	 * @param response
	 * @throws IOException
	 * @throws TuniuCommonException
	 */
	private void tuniuTrainReturn(Parameter parameter,SynchronousOutput synOutput) throws TuniuCommonException {

		String orderId = parameter.getString("orderId");
		String callBackUrl = parameter.getString("callBackUrl");
		if (StringUtils.isEmpty(orderId)
				|| StringUtils.isEmpty(callBackUrl))
			throw new TuniuCommonException(
					TuniuCommonService.RETURN_CODE_PARAM_ERROR);
		
		Result result = tuniuRefundService.trainRefund(parameter);
		wrapSynOutput(result, synOutput);
	}

	/**
	 * 查询经停站
	 * 
	 * @param parameter
	 * @param synOutput
	 * @param response
	 */
	private void tuniuTrainQueryStations(Parameter parameter,SynchronousOutput synOutput) {

		String trainDate = parameter.getString("trainDate");
		String trainCode = parameter.getString("trainCode");
		if (StringUtils.isEmpty(trainCode) || StringUtils.isEmpty(trainDate)) {
			synOutput.setReturnCode("801");
			synOutput.setErrorMsg("查询失败");
			synOutput.setSuccess(false);
			return;
		}

		try {
			List<Station> stations = tuniuCommonService.queryStations(trainCode);
			Map<String, Object> trainInfo = new HashMap<String, Object>();
			trainInfo.put("trainDate", trainDate);
			trainInfo.put("trainCode", trainCode);
			trainInfo.put("stations", stations);
		
			if (stations == null || stations.size() == 0) {
				synOutput.setReturnCode("802");
				synOutput.setErrorMsg("无此车次");
				synOutput.setSuccess(false);
			} else {
				synOutput.setReturnCode("231000");
				synOutput.setErrorMsg("请求成功");
				synOutput.setSuccess(true);
				synOutput.setData(trainInfo);
			}
		} catch (Exception e) {
			synOutput.setReturnCode("801");
			synOutput.setErrorMsg("查询失败");
			synOutput.setSuccess(false);
		}
	}

	/**
	 * 身份证验证接口
	 * 
	 * @param parameter
	 * @param synOutput
	 * @param response
	 */
	private void tuniuTrainValidate(Parameter parameter,SynchronousOutput synOutput) {

		String code = TuniuCommonService.RETURN_CODE_NO_PERMISSION;
		String errorMsg = tuniuCommonService.getErrorMessage(code);
		synOutput.setSuccess(false);
		synOutput.setReturnCode(code);
		synOutput.setErrorMsg(errorMsg);
		logger.info("暂不支持身份证验证接口");
	}

	/**
	 * 通用参数检查
	 * 
	 * @param input
	 * @param signkey
	 * @throws TuniuCommonException
	 */
	private void validateCommonParameter(TuniuInput input, String signkey)throws TuniuCommonException {

		/* 测试start */
		// return;
		/* end */
		if (isEmpty(input.getAccount()) || isEmpty(input.getTimestamp())
				|| isEmpty(input.getSign()))
			throw new TuniuCommonException(
					TuniuCommonService.RETURN_CODE_PARAM_ERROR);

		if (!input.getAccount().equals(TuniuConstant.account))
			throw new TuniuCommonException(
					TuniuCommonService.RETURN_CODE_USER_NOT_EXISTS);

		if (!tuniuCommonService.validateTimestamp(input.getTimestamp()))
			throw new TuniuCommonException(
					TuniuCommonService.RETURN_CODE_TIMESTAMP_ERROR);

		/* 验签参数 */
		Map<String, String> params = new HashMap<String, String>();
		params.put("account", input.getAccount());
		params.put("timestamp", input.getTimestamp());
		params.put("data", input.getData());
		params.put("returnCode",input.getReturnCode());
		String validateSign = tuniuCommonService.generateSign(params, signkey);
		if (!input.getSign().equals(validateSign))
			throw new TuniuCommonException(
					TuniuCommonService.RETURN_CODE_SIGNATURE_ERROR);
	}
	
	/**
	 * 处理同步输出内容
	 * @param result
	 * @param synOutput
	 */
	private void wrapSynOutput(Result result, SynchronousOutput synOutput) {
		String code = synOutput.getReturnCode();
		if(result == null) {
			code = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
		} else {
			logger.info("code:"+result.getCode());
			code = result.getCode();
			synOutput.setData(result.resultMap());//出参
		}
		
		String errorMsg = tuniuCommonService.getErrorMessage(code);
		synOutput.setReturnCode(code);
		if(code!=null&&!code.equals(TuniuCommonService.RETURN_CODE_SUCCESS)&&!code.equals(TuniuOrderService.RETURN_CODE_CONFIRM_ORDER_SUCCESS)&&!code.equals(TuniuOrderService.RETURN_CODE_CANCEL_SUCCESS)&&!code.equals(TuniuRefundService.RETURN_CODE_REFUND_SUCCESS)) {
			synOutput.setSuccess(false);
		}
		synOutput.setErrorMsg(errorMsg);
	}
	/**
	 * 12306登录账号验证
	 */
	public String validateAccount(Account account) throws Exception{
		Map params = new HashMap();
		String url = (String) config.get("identityURL");
		params.put("command", "verifyAccountTuniu");//新的接口，陶凯的帐号核验机器
		params.put("channel", "19e");
		String username = account.getUsername();
		String pass = account.getPassword();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", username);
		jsonObject.put("pass", pass);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonObject);
		params.put("data", jsonArray.toString());
		String uri = UrlFormatUtil.createUrl("", params, "utf-8");
		String result = HttpCheckAccountUtil.sendByPost(url, uri, "UTF-8");
		int count=0;
		while(result.contains("登录初始化失败")||result.contains("登录失败")){
			 count++;
			 result = HttpCheckAccountUtil.sendByPost(url, uri, "UTF-8");
			 if(count>3) break;
		}
		JSONObject strObject = JSON.parseObject(result);
		String flag = strObject.getString("ErrorInfo").replace("\\", "").replace("[\"{", "[{").replace("}\"]", "}]");
		logger.info(username+"账号核验返回结果"+flag);
		if (flag.startsWith("脚本")) {
			return "1";
		} else {
			JSONArray array = JSON
					.parseArray(flag);
			for (int i = 0; i < array.size(); i++) {
				Map maps = (Map) array.get(i);
				String results = maps.get("retValue").toString();
				String retInfo = maps.get("retInfo").toString();
				logger.info("results："+results); 
				if ("success".equals(results)) {
					return "1";
				}
				return retInfo;
			}
		}
		return "0";

	}

	/**
	 * 12306账号下的常用联系人
	 */
	public String queryAccountUse(Account account) {

		Map params = new HashMap();
		//获取配置文件中的内部接口地址
		String url = (String) config.get("identityURL");
		params.put("command", "queryAccountUse");
		params.put("channel", "19e");
		String username = account.getUsername();
		String pass = account.getPassword();
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("trainAccount", username);
		jsonObject.put("pass", pass);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonObject);
		params.put("data", jsonArray.toString());
		//参数拼接整合
		String uri = UrlFormatUtil.createUrl("", params, "utf-8");
		//post请求
		String result = HttpUtil.sendByPost(url, uri, "UTF-8");
	//	String result="{\"ErrorCode\":0,\"ErrorInfo\":[\"{\\\"retInfo\\\":[{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"421083196807071238\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2015-01-26\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"陈又阳\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1968-07-07\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"130706198410200315\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2014-10-21\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"贾亮\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1984-10-20\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"150428198810032512\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2014-10-29\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"李海龙\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1988-10-03\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"371327198502160045\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2014-10-29\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"刘玉花\\\",\\\"sex\\\":\\\"1\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1985-02-16\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"422425196808291215\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2014-12-03\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"孙传虎\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1968-08-29\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"422426196107271920\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2015-09-29\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"沈凤元\\\",\\\"sex\\\":\\\"1\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1961-07-27\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"422425197608107131\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"13873149848\\\",\\\"addTime\\\":\\\"2016-09-22\\\",\\\"tel\\\":\\\"13873149848\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"王泽浩\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1976-08-10\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"422426196205111912\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"15073149538\\\",\\\"addTime\\\":\\\"2013-12-16\\\",\\\"tel\\\":\\\"15073149538\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"杨代银\\\",\\\"sex\\\":\\\"1\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1962-05-11\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"0\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"421083198708261916\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"18229933193\\\",\\\"addTime\\\":\\\"2013-09-14\\\",\\\"tel\\\":\\\"18229933193\\\",\\\"email\\\":\\\"171389959@qq.com\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"杨海洋\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1987-08-26\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"422403198112091910\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2015-04-02\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"杨涛\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1981-12-09\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"422425196710121277\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2015-04-02\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"易贤海\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1967-10-12\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"420683198607114249\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"13667232429\\\",\\\"addTime\\\":\\\"2013-08-23\\\",\\\"tel\\\":\\\"13667232429\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"赵姣\\\",\\\"sex\\\":\\\"1\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1986-07-11\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"431226197109110033\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2017-02-19\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"张金龙\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1971-09-11\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"421023198910057197\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2016-02-25\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"张文\\\",\\\"sex\\\":\\\"0\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1989-10-05\\\"},{\\\"personType\\\":\\\"1\\\",\\\"preferenceNum\\\":\\\"\\\",\\\"schoolName\\\":\\\"\\\",\\\"isUserSelf\\\":\\\"1\\\",\\\"checkStatus\\\":\\\"0\\\",\\\"identy\\\":\\\"120104198201096067\\\",\\\"preferenceToStationName\\\":\\\"\\\",\\\"schoolSystem\\\":\\\"\\\",\\\"department\\\":\\\"\\\",\\\"identyType\\\":\\\"2\\\",\\\"studentNo\\\":\\\"\\\",\\\"country\\\":\\\"CN\\\",\\\"phone\\\":\\\"\\\",\\\"addTime\\\":\\\"2015-12-15\\\",\\\"tel\\\":\\\"\\\",\\\"email\\\":\\\"\\\",\\\"provinceName\\\":\\\"\\\",\\\"enterYear\\\":\\\"\\\",\\\"name\\\":\\\"张雅囡\\\",\\\"sex\\\":\\\"1\\\",\\\"preferenceFromStationName\\\":\\\"\\\",\\\"className\\\":\\\"\\\",\\\"address\\\":\\\"\\\",\\\"birthday\\\":\\\"1982-01-09\\\"}],\\\"robotNum\\\":14,\\\"retValue\\\":\\\"success\\\",\\\"accId\\\":\\\"\\\"}\"]}";
		logger.info("途牛常旅查询地址"+url);
		logger.info("途牛常旅查询返回结果"+result);
		//{"ErrorCode":0,"ErrorInfo":["{\"retInfo\":[{\"isUserSelf\":\"0\",\"identyType\":\"1\",\"name\":\"%E4%BD%99%E5%AD%A6%E6%96%87\",\"phone\":\"\",\"identy\":\"342623199009216552\",\"checkStatus\":\"0\",\"personType\":\"1\"}],\"robotNum\":14,\"retValue\":\"success\",\"accId\":\"\"}"]}
		//解析返回参数
		JSONObject strObject = JSON.parseObject(result);
		//对返回参数替换
		String flag = strObject.getString("ErrorInfo").replace("\\", "").replace("[\"{", "[{").replace("}\"]", "}]");
		Map objMap = new HashMap();
		//判断出现异常时返回登录失败
		if (flag.startsWith("脚本")) {
			logger.info(" 脚本异常 ");
			return "error";
		} else {
			//解析返回参数List
			JSONArray arrayObj = JSON.parseArray(flag);
			for (int i = 0; i < arrayObj.size(); i++) {
				Map map = (Map)arrayObj.get(i);
				String retValue = (String)map.get("retValue");
				if("success".equals(retValue)){
					JSONArray Array = JSON.parseArray(map.get("retInfo").toString());
					List list = new ArrayList();
					for (int j = 0; j < Array.size(); j++) {
						Map userMaps = (Map) Array.get(j);
						//ID
						Integer id = j+1;
						//身份证号
						String identy = userMaps.get("identy").toString();
						//姓名
						String name = userMaps.get("name").toString();
						//是否是账号本人
						String isUserSelf = (String) userMaps.get("isUserSelf");
						//证件类型
						String identyType = (String) userMaps.get("identyType");
						//旅客类型
						String personType = (String) userMaps.get("personType");
						//手机
						String phone = userMaps.get("phone").toString();
						//身份是否校验通过 0通过1不通过
						String checkStatus = (String) userMaps.get("checkStatus");
						Integer status = Integer.valueOf(checkStatus);
						Integer isu = Integer.valueOf(isUserSelf);
						Integer pers = Integer.valueOf(personType);
						Integer ide = Integer.valueOf(identyType);
						//性别
						String sex = (String) userMaps.get("sex");
						Integer sexInter = Integer.valueOf(sex);
						//添加时间
						String addTime = (String) userMaps.get("addTime");
						//生日
						String birthday = (String) userMaps.get("birthday");
						//国家
						String country =  (String) userMaps.get("country");
						//电话
						String tel =  (String) userMaps.get("tel");
						//邮件
						String email =  (String) userMaps.get("email");
						//地址
						String address =  (String) userMaps.get("address");
						
						String provinceName=(String) userMaps.get("provinceName");
						String schoolName=(String) userMaps.get("schoolName");
						String department=(String) userMaps.get("department");
						String className=(String) userMaps.get("className");
						String studentNo=(String) userMaps.get("studentNo");
						String schoolSystem=(String) userMaps.get("schoolSystem");
						String enterYear=(String) userMaps.get("enterYear");
						String preferenceNum=(String) userMaps.get("preferenceNum");
						String preferenceToStationName=(String) userMaps.get("preferenceToStationName");
						String preferenceFromStationName=(String) userMaps.get("preferenceFromStationName");
		
						Map jsonMap = new HashMap();
						
						//组合
						jsonMap.put("id", id);
						jsonMap.put("name", name);
						jsonMap.put("isUserSelf", isu);
						jsonMap.put("sex", sexInter);
						jsonMap.put("birthday", birthday);
						jsonMap.put("country", country);
						jsonMap.put("identyType", ide);
						jsonMap.put("identy", identy);
						jsonMap.put("personType", pers);
						jsonMap.put("phone", phone);
						jsonMap.put("tel", tel);
						jsonMap.put("email", email);
						jsonMap.put("address","");
						jsonMap.put("checkStatus", status);
						jsonMap.put("addTime", addTime);
						
						jsonMap.put("provinceName", provinceName);
						jsonMap.put("schoolName", schoolName);
						jsonMap.put("department", department);
						jsonMap.put("className", className);
						jsonMap.put("studentNo", studentNo);
						jsonMap.put("schoolSystem", schoolSystem);
						jsonMap.put("enterYear", enterYear);
						jsonMap.put("preferenceNum", preferenceNum);
						jsonMap.put("preferenceFromStationName", preferenceFromStationName);
						jsonMap.put("preferenceToStationName", preferenceToStationName);

						
						list.add(jsonMap);
					}
					String objs = JSON.toJSONString(list);
					objMap.put("info", objs);
					objMap.put("flag", "success");
					String obj = JSON.toJSONString(objMap);
					return obj;
					
				}else{
					return "error";
				}
			}
		}
		return "error";
	}

	/**
	 * 添加更新常用联系人
	 */
	public String saveUpdateContact(Account account, TuniuContact tuniuContact) {
		//途牛账号
		String tuniu_account = account.getUsername();
		//途牛密码
		String tuniu_pwd = account.getPassword();
		//途牛增加联系人
		//身份证号
		String contact_identy = tuniuContact.getIdenty();
		//联系人
		String contact_name = tuniuContact.getName();
		//联系人类型
		int contact_type = tuniuContact.getPersonType();
		//身份类型
		String contact_identy_type = tuniuContact.getIdentyType();
		//序列号
		int id = tuniuContact.getId();
		if(id>0){
			return "0";
		}else{
		Map params = new HashMap();
		//获取配置文件中的内部接口地址
		String url = (String) config.get("identityURL");
		params.put("command", "add_contact");
		params.put("channel", "19e");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("trainAccount", tuniu_account);
		jsonObject.put("pass", tuniu_pwd);
		jsonObject.put("cert_no", contact_identy);
		jsonObject.put("user_name", contact_name);
		jsonObject.put("contact_type", contact_type);
		jsonObject.put("cert_type", contact_identy_type);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonObject);
		params.put("data", jsonArray.toString());
		//参数拼接整合
		String uri = UrlFormatUtil.createUrl("", params, "utf-8");
		//post请求
		String result = HttpUtil.sendByPost(url, uri, "UTF-8");
//		String result = "[{\"cert_no\":\"510623199407301613\",\"cert_type\":\"2\",\"check_status\":\"12\",\"user_name\":\"郭鸿\",\"user_type\":\"0\"}]";
		JSONArray array = JSON.parseArray(result);
		String flag = "";
		for (int i = 0; i < array.size(); i++) {
			JSONObject job = JSON.parseObject(array.get(i).toString());
			flag = job.getString("check_status");
		}
		if("0".equals(flag)){
			return "2";
		} 
		//返回失败
		return "0";
		}
	}

	/**
	 * 删除常用联系人
	 */
	public boolean deleteContact(TuniuDeleteContact tuniuDeleteContact) {
		Map params = new HashMap();
		//获取配置文件中的内部接口地址
		String url = (String) config.get("identityURL");
		params.put("command", "add_contact");
		params.put("channel", "19e");
		String trainAccount = tuniuDeleteContact.getTrainAccount();
		String pass = tuniuDeleteContact.getPass();
		int ids = tuniuDeleteContact.getId();
		String identy = tuniuDeleteContact.getIdenty();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonObject.put("trainAccount", trainAccount);
		jsonObject.put("pass", pass);
		jsonObject.put("ids", ids);
		jsonObject.put("identy", identy);
		
		jsonArray.add(jsonObject);
		params.put("data", jsonArray.toString());
		//参数拼接整合
		String uri = UrlFormatUtil.createUrl("", params, "utf-8");
		//post请求
		String result = HttpUtil.sendByPost(url, uri, "UTF-8");
		//解析返回参数
		JSONObject strObject = JSON.parseObject(result);
		//对返回参数替换
		String flag = strObject.getString("ErrorInfo").replace("\\", "").replace("[\"{", "[{").replace("}\"]", "}]");
		
		if(flag.startsWith("脚本")){
			logger.info(" 脚本异常 ");
			return false;
		}else{
			JSONArray arrayObj = JSON.parseArray(flag);
			for (int i = 0; i < arrayObj.size(); i++) {
				Map map = (Map)arrayObj.get(i);
				String retValue = (String)map.get("retValue");
				if("success".equals(retValue)){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 身份证验证
	 */
	public String identity(TuniuIdentityValidate tuniuIdentityValidate) {
		// 身份证号
		String identityCard = tuniuIdentityValidate.getIdentityCard();
//		if(identityCard.length()==18){
//			System.out.println(identityCard.length()+" 二代身份证");
			// 姓名
			String name = tuniuIdentityValidate.getName();
			// 身份证类型
			String identityType = tuniuIdentityValidate.getIdentityType();
			String type = String.valueOf(identityType);
			if("1".equals(type)){
				type = "2";
			}else{
				type = "1";
			}
			Map params = new HashMap();
			String url = (String) config.get("identityURL");
			params.put("command", "verify");
			params.put("channel", "19e");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("cert_no", identityCard);
			jsonObject.put("cert_type", type);
			jsonObject.put("user_name", name);
	
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(jsonObject);
			params.put("passengers", jsonArray.toString());
			String uri = UrlFormatUtil.createUrl("", params, "utf-8");
			String result = HttpUtil.sendByPost(url, uri, "UTF-8");
			System.out.println(" result  "+result);
			logger.info(" result  "+result);
			JSONArray array = JSON.parseArray(result);
			String check_status = "";
			for (int i = 0; i < array.size(); i++) {
				Map map = (Map) array.get(i);
				check_status = map.get("check_status").toString();
				String message  = map.get("message").toString();
				if("2".equals(check_status)){
					if(message.indexOf("冒用")>0){
						check_status = "3";
					}
				}
			}
			return check_status;
//		}else{
//			return "2";
//		}
	}
	/**
	 * 改签接口
	 * @param request
	 * @param response
	 * @param method
	 */
	@RequestMapping("train/change/{method}")
	public void tuniuTrainChange(HttpServletRequest request,HttpServletResponse response, @PathVariable String method) {
		/* 同步输出对象 */
		SynchronousOutput synOutput = new SynchronousOutput();
		try{
			// 接收Post传递参数
			String paramStr = recieveParameterUTF8(request, response);
			TuniuInput input = JacksonUtil.readJson(paramStr,TuniuInput.class);
			if (input == null)
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			/* 通用参数检查 */
			validateCommonParameter(input, TuniuConstant.signKey);
			/* 传入参数 */
			String dataBase64 = input.getData();
			logger.info("途牛业务参数密文data :" + dataBase64);
	
			String data = EncryptUtil.decode(dataBase64, TuniuConstant.signKey, "UTF-8");
			logger.info("途牛传入业务参数：" + data);
	
			Parameter parameter = new TuniuParameter(data);
		
			if(method.equals("apply")){
				applyChange(parameter,synOutput);//请求改签
			}else if(method.equals("cancel")){
				cancelChange(parameter,synOutput);//取消改签
			}else if(method.equals("confirm")){
				confirmChange(parameter,synOutput);//确认改签
			}else{
				logger.info("非法请求接口");
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_INTER_NOT_EXISTS);

			}
		} catch (TuniuCommonException e) {
			String errorMsg = tuniuCommonService.getErrorMessage(e.getMessage());
			synOutput.setSuccess(false);
			synOutput.setReturnCode(e.getMessage());
			synOutput.setErrorMsg(errorMsg);
			logger.info("tuniu_error_msg: " + errorMsg);
		}
		catch (Exception e) {
			e.printStackTrace();
			String code = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
			String errorMsg = tuniuCommonService.getErrorMessage(code);
			synOutput.setSuccess(false);
			synOutput.setReturnCode(code);
			synOutput.setErrorMsg(errorMsg);
			logger.info("tuniu_unknown_error : " + e.getMessage());
		}
		finally{
			try {
				printJson(response, JacksonUtil.generateJson(synOutput));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	//请求改签
	private void applyChange(Parameter parameter,SynchronousOutput synOutput) throws TuniuCommonException {
		Result result = tuniuChangeService.addChange(parameter);
		wrapSynOutput(result, synOutput);
	}
	//取消改签
	private void cancelChange(Parameter parameter,SynchronousOutput synOutput) throws TuniuCommonException{
		Result result = tuniuChangeService.cancelChange(parameter);
		wrapSynOutput(result, synOutput);
	}
	//确认改签
	private void confirmChange(Parameter parameter,SynchronousOutput synOutput)throws TuniuCommonException{
		Result result = tuniuChangeService.confirmChange(parameter);
		wrapSynOutput(result, synOutput);
	}
	/**
	 * 获取订单信息中的车票状态信息  
	 * @param request
	 * @param response
	 * @param method
	 */
	@RequestMapping("trainTicket/searchStatusFrom12306")
	public void searchStatusFrom12306(HttpServletRequest request,HttpServletResponse response) {
		logger.info("途牛请求- 获取订单信息中的车票状态信息  ");
		/* 同步输出对象 */
		SynchronousOutput synOutput = new SynchronousOutput();
		try{
			// 接收Post传递参数
			String paramStr = recieveParameterUTF8(request, response);
			TuniuInput input = JacksonUtil.readJson(paramStr,TuniuInput.class);
			
			if (input == null)
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			/* 通用参数检查 */
			validateCommonParameter(input, TuniuConstant.signKey);
			/* 传入参数 */
			String dataBase64 = input.getData();
			logger.info("途牛获取订单信息中的车票状态信息 业务参数密文data :" + dataBase64);
	
			String data = EncryptUtil.decode(dataBase64, TuniuConstant.signKey, "UTF-8");
			logger.info("途牛获取订单信息中的车票状态信息 传入业务参数：" + data);
			
			Parameter parameter = new TuniuParameter(data);
			//获取订单信息中的车票状态信息
			Result result = tuniuOrderService.searchStatusFrom12306(parameter);
			wrapSynOutput(result, synOutput);
		} catch (TuniuCommonException e) {
			String errorMsg = tuniuCommonService.getErrorMessage(e.getMessage());
			synOutput.setSuccess(false);
			synOutput.setReturnCode(e.getMessage());
			synOutput.setErrorMsg(errorMsg);
			logger.info("tuniu_error_msg: " + errorMsg);
		}
		catch (Exception e) {
			e.printStackTrace();
			String code = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
			String errorMsg = tuniuCommonService.getErrorMessage(code);
			synOutput.setSuccess(false);
			synOutput.setReturnCode(code);
			synOutput.setErrorMsg(errorMsg);
			logger.info("tuniu_unknown_error : " + e.getMessage());
		}
		finally{
			try {
				printJson(response, JacksonUtil.generateJson(synOutput));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 催退款通知
	 * @param parameter
	 * @param synOutput
	 * @param response
	 * @throws IOException
	 * @throws TuniuCommonException
	 */
	private void queryRefundNotice(Parameter parameter,SynchronousOutput synOutput) throws TuniuCommonException {
		tuniuRefundService.queryRefundNotice(parameter,synOutput);
	}
	
	/*---------------------------dqh2015-11-23---------------------------------------------------
	
	/**
	 * 获取验证码
	 * @param request
	 * @param response
	 * @param method
	 */
	@RequestMapping("train/captcha/getCaptcha")
	public void getCaptcha(HttpServletRequest request,HttpServletResponse response) {
		logger.info("途牛请求- 获取12306的验证码  "); 
		/* 同步输出对象 */
		SynchronousOutput synOutput = new SynchronousOutput();
		try{
			// 接收Post传递参数
			String paramStr = recieveParameterUTF8(request, response);
			TuniuInput input = JacksonUtil.readJson(paramStr,TuniuInput.class);
			
			if (input == null)
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			/* 通用参数检查	 */
			validateCommonParameter(input, TuniuConstant.signKey);
			/* 传入参数 */
			String dataBase64 = input.getData();
//			logger.info("途牛获取12306的验证码业务参数密文data :" + dataBase64);
	
			String data = EncryptUtil.decode(dataBase64, TuniuConstant.signKey, "UTF-8");
			logger.info("途牛获取12306的验证码业务参数：" + data);
			
			Parameter parameter = new TuniuParameter(data);
			//获取订单信息中的车票状态信息
			String accountUrl = (String)config.get("accountUrl");
			String captchaResultUrl = (String)config.get("captchaResultUrl");
			String robotUrl = (String)config.get("robotUrl");
			String changeUrl = (String)config.get("changeUrl"); 
			Result result = new TuniuResult();
			if (parameter.getInt("businessType")==2){		
				result = tuniuOrderService.captchaTrainBook(parameter,accountUrl,captchaResultUrl,robotUrl,0);
				//未知异常，重发请求
				if (result.getCode().equals(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR)) { 
						result = tuniuOrderService.captchaTrainBook(parameter,accountUrl,captchaResultUrl,robotUrl,1); 
				}
				//重发继续失败，返回250012
				if (result.getCode().equals(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR)) 
					result.setCode("250012");
			}else if(parameter.getInt("businessType")==3){
				result = tuniuChangeService.addChangeCaptcha(parameter,captchaResultUrl,changeUrl); 
				if (result.getCode().equals(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR)) {
					result = tuniuChangeService.addChangeCaptcha(parameter,captchaResultUrl,changeUrl);
				}				
			}
			wrapSynOutput(result, synOutput); 
		} catch (TuniuCommonException e) {
			String errorMsg = tuniuCommonService.getErrorMessage(e.getMessage());
			synOutput.setSuccess(false);
			synOutput.setReturnCode(e.getMessage());
			synOutput.setErrorMsg(errorMsg);
			logger.info("tuniu_error_msg: " + errorMsg);
		}
		catch (Exception e) {
			e.printStackTrace();
			String code = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
			String errorMsg = tuniuCommonService.getErrorMessage(code);
			synOutput.setSuccess(false);
			synOutput.setReturnCode(code);
			synOutput.setErrorMsg(errorMsg);
			logger.info("tuniu_unknown_error : " + e.getMessage());
		}
		finally{
			try {
				logger.info("获取12306的验证码返回结果:"+JacksonUtil.generateJson(synOutput));
				printJson(response, JacksonUtil.generateJson(synOutput));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	} 
	
	/**
	 * 提交打码结果
	 * @param request
	 * @param response
	 * @param method
	 */
	@RequestMapping("train/captcha/submitCaptcha")
	public void submitCaptcha(HttpServletRequest request,HttpServletResponse response) {
		logger.info("途牛请求- 获取12306的验证码打码结果  "); 
		/* 同步输出对象 */
		SynchronousOutput synOutput = new SynchronousOutput();
		Result result = new TuniuResult();
		try{
			// 接收Post传递参数
			String paramStr = recieveParameterUTF8(request, response);
			TuniuInput input = JacksonUtil.readJson(paramStr,TuniuInput.class);
			
			if (input == null)
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			/* 通用参数检查 */
			validateCommonParameter(input, TuniuConstant.signKey);
			/* 传入参数 */
			String dataBase64 = input.getData();
			logger.info("途牛获取12306的验证码打码结果参数密文data :" + dataBase64);
	
			String data = EncryptUtil.decode(dataBase64, TuniuConstant.signKey, "UTF-8");
			logger.info("途牛获取12306的验证码打码结果参数：" + data);
			
			Parameter parameter = new TuniuParameter(data);
			//插入验证码打码坐标
			StringBuffer captchaBuffer = new StringBuffer();
			captchaBuffer.append("commond=").append("updateCaptcha")
			.append("&requestId=").append(parameter.getString("requestId"))
			.append("&randCode=").append(parameter.getString("randCode"))
			.append("&captchaType=").append(parameter.getString("captchaType"));
			String captchaResultUrl = (String)config.get("captchaResultUrl");
			logger.info("插入打码参数:"+captchaBuffer.toString());
			String updateCaptcha = HttpUtil.sendByPost(captchaResultUrl, captchaBuffer.toString(), "UTF-8");
			logger.info("插入打码返回:"+updateCaptcha); 
			if ("success".equals(updateCaptcha)) {
				//查询打码结果
				String captchaResult = "";
				StringBuffer captchaResultBuffer = new StringBuffer();
				captchaResultBuffer.append("commond=").append("resultCaptcha")
				.append("&requestId=").append(parameter.getString("requestId"))
				.append("&captchaType=").append(parameter.getString("captchaType"));
				logger.info("查询打码结果参数:"+captchaResultBuffer.toString());
				for (int i = 0; i < 20; i++) {
					captchaResult = HttpUtil.sendByPost(captchaResultUrl, captchaResultBuffer.toString(), "UTF-8"); 
//					logger.info("查询打码结果返回:"+captchaResult);
					if (!captchaResult.contains("fail")&&!captchaResult.contains("验证码不正确"))	
						break;
					else
						Thread.sleep(3000);
				}
				result.putData("requestId", parameter.getString("requestId"));
				logger.info("查询打码结果返回:"+captchaResult);
				if ("success".equals(captchaResult)) {
					result.putData("isFinish", true);//打码流程是否完成	
					result.putData("isCorrect",true);//验证码是否正确 				
				}else if (captchaResult.contains("message")) { 
					String[] resultList = captchaResult.split("\\|");
					if (captchaResult.contains("取消次数过多"))
						result.setCode("260010");
					else if (captchaResult.contains("未完成订单"))
						result.setCode("260011");
					result.putData("isFinish", false);//打码流程是否完成
					result.putData("isCorrect",false);//验证码是否正确
				}else if (captchaResult.contains("restart")) {
					//判断是否打码失败重新发送的验证码 
					String[] resultList = captchaResult.split("\\|");
					result.putData("captchaType", parameter.getString("captchaType"));//打码流程是否完成 
					result.putData("file",resultList[1]);
					result.putData("isFinish", false);//打码流程是否完成
					result.putData("isCorrect",false);//验证码是否正确
				} else{
					result.putData("isFinish", false);//打码流程是否完成	
					result.putData("isCorrect",false);//验证码是否正确 			
				}
				wrapSynOutput(result, synOutput);
			}
		} catch (TuniuCommonException e) {
			String errorMsg = tuniuCommonService.getErrorMessage(e.getMessage());
			synOutput.setSuccess(false);
			synOutput.setReturnCode(e.getMessage());
			synOutput.setErrorMsg(errorMsg);
			synOutput.setData(result.resultMap());
			logger.info("打码tuniu_error_msg: " + errorMsg);
		}
		catch (Exception e) {
			e.printStackTrace();
			String code = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
			String errorMsg = tuniuCommonService.getErrorMessage(code);
			synOutput.setSuccess(false);
			synOutput.setReturnCode(code);
			synOutput.setErrorMsg(errorMsg);
			synOutput.setData(result.resultMap());
			logger.info("打码tuniu_unknown_error : " + e.getMessage());
		}
		finally{
			try {
				logger.info("返回打码结果:"+JacksonUtil.generateJson(synOutput));
				printJson(response, JacksonUtil.generateJson(synOutput));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 提交打码结果
	 * @param request
	 * @param response
	 * @param method
	 */
	@RequestMapping("train/captcha/checkLogin")
	public void checkLogin(HttpServletRequest request,HttpServletResponse response) {
		
		logger.info("途牛请求- 获取12306用户登录核验"); 
		/* 同步输出对象 */
		SynchronousOutput synOutput = new SynchronousOutput();
		Result result = new TuniuResult();
		try{
			// 接收Post传递参数
			String paramStr = recieveParameterUTF8(request, response);
			TuniuInput input = JacksonUtil.readJson(paramStr,TuniuInput.class);
			
			if (input == null)
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			/* 通用参数检查 */
			validateCommonParameter(input, TuniuConstant.signKey);
			/* 传入参数 */
			String dataBase64 = input.getData();
			logger.info("途牛获取12306用户登录核验参数密文data :" + dataBase64);
	
			String data = EncryptUtil.decode(dataBase64, TuniuConstant.signKey, "UTF-8");
			logger.info("途牛获取12306用户登录核验参数：" + data);
			
			Parameter parameter = new TuniuParameter(data);
			 
			String checkUrl = (String)config.get("checkUrl");//访问lua机器人
			StringBuffer luaParams = new StringBuffer();  
			luaParams.append("ScriptPath=").append("appTuniuCheck.lua")
			.append("&commond=").append("trainOrder")
			.append("&SessionID=").append(String.valueOf(System.currentTimeMillis()))
			.append("&Timeout=").append("100000")
			.append("&ParamCount=").append(2)
			.append("&Param1=").append(URLEncoder.encode(parameter.getString("userName")+"|"+parameter.getString("userPassword"),"UTF-8"));
			logger.info("12306用户登录核验参数:"+luaParams.toString());
			String luaResult = HttpUtil.sendByGet(checkUrl+"?"+luaParams.toString(),"UTF-8", "30000", "30000");//请求机器人
//				HttpUtil.sendByGet(reqUrl, inputCharset, connectiontimeout, readtimeout)(checkUrl, luaParams.toString(), "UTF-8");
			logger.info("12306用户登录核验返回:"+luaResult);
			result.putData("requestId", parameter.getString("requestId"));		
			if (luaResult.contains("success")) {
				result.putData("isLogin", true);//打码流程是否完成	 	 
				wrapSynOutput(result, synOutput);
			}else {  
				result.setCode(null);
				result.putData("isLogin", false);//打码流程是否完成	 
				synOutput.setData(result.resultMap()); 
				synOutput.setSuccess(false);
				String a = luaResult.substring(luaResult.indexOf("retInfo")+12,luaResult.length());
				String msg = a.substring(0,a.indexOf("\"")-1); 
				synOutput.setErrorMsg(msg);
				if (luaResult.contains("不存在"))
					synOutput.setReturnCode("270001");	
				else if (luaResult.contains("密码输入错误"))
					synOutput.setReturnCode("270002");	 
				else if (luaResult.contains("锁定时间为20分钟"))
					synOutput.setReturnCode("270003");	   
			}
		} catch (TuniuCommonException e) {
			String errorMsg = tuniuCommonService.getErrorMessage(e.getMessage());
			synOutput.setSuccess(false);
			synOutput.setReturnCode(e.getMessage());
			synOutput.setErrorMsg(errorMsg);
			synOutput.setData(result.resultMap());
			logger.info("tuniu_error_msg: " + errorMsg);
		}
		catch (Exception e) {
			e.printStackTrace();
			String code = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
			String errorMsg = tuniuCommonService.getErrorMessage(code);
			synOutput.setSuccess(false);
			synOutput.setReturnCode(code);
			synOutput.setErrorMsg(errorMsg);
			synOutput.setData(result.resultMap());
			logger.info("tuniu_unknown_error : " + e.getMessage());
		}
		finally{
			try {
				logger.info("返回12306用户登录核验结果:"+JacksonUtil.generateJson(synOutput));
				printJson(response, JacksonUtil.generateJson(synOutput));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws TuniuCommonException {
//		String data = "{\"businessType\":3,\"requestId\":\"EC98365EF1414CE8B92DF2564E10A353\",\"userName\":\"15000332654\",\"userPassword\":\"luchao3417\",\"busData\":{\"orderId\":\"1005920222\",\"retailId\":2016,\"orderNumber\":\"E883613415\",\"changeCheCi\":\"6056\",\"changeDateTime\":\"2017-01-05\",\"changeZwCode\":\"1\",\"oldZwCode\":\"1\",\"hasSeat\":true,\"fromStationCode\":\"BTC\",\"fromStationName\":\"包头\",\"toStationCode\":\"BDC\",\"toStationName\":\"包头东\",\"userName\":\"15000332654\",\"userPassword\":\"luchao3417\",\"oldTicketInfos\":[{\"passengerId\":1780722,\"passengerName\":\"陆超\",\"passportNo\":\"310230198909231459\",\"passportTypeId\":\"1\",\"passportTypeName\":\"二代身份证\",\"piaoType\":\"1\"}]}}";
//		logger.info("途牛获取12306的验证码业务参数：" + data);
//		TuniuController tc = new TuniuController();
//		Parameter parameter = new TuniuParameter(data);
//		//获取订单信息中的车票状态信息  
//		String accountUrl ="http://10.22.16.33:18040/accountN/getChannelAccount";
//		String captchaResultUrl = "http://www.19trip.com/tuniuCaptcha/tuniu/captcha";
//		String robotUrl = "http://120.26.6.116:8091/RunScript";
//		String changeUrl ="http://43.241.225.165:8080/RunScript"; 
//		TuniuChangeService tuniuChangeService = new TuniuChangeServiceImpl();
//		Result result = new TuniuResult();
//		if (parameter.getInt("businessType")==2){		
//			result = tc.tuniuOrderService.captchaTrainBook(parameter,accountUrl,captchaResultUrl,robotUrl); 
//			if (result.getCode().equals(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR)) {
//				result = tc.tuniuOrderService.captchaTrainBook(parameter,accountUrl,captchaResultUrl,robotUrl); 
//			}
//		}else if(parameter.getInt("businessType")==3)		
//			result = tuniuChangeService.addChangeCaptcha(parameter,captchaResultUrl,changeUrl); 
////		wrapSynOutput(result, synOutput); 
//		System.out.println(result.toString());
		String data = "{\"businessType\":3,\"requestId\":\"8EFC9FEE61944E8A984CF897D229A894\",\"userName\":\"15000332654\",\"userPassword\":\"luchao3417\",\"busData\":{\"orderId\":\"1005605153\",\"retailId\":\"\",\"orderNumber\":\"E829017931\",\"oldZwCode\":\"1\",\"oldCheCi\":\"6056\",\"fromStationCode\":\"1175334\",\"fromStationName\":\"包头\",\"toStationCode\":\"1175333\",\"toStationName\":\"包头东\",\"userName\":\"15000332654\",\"userPassword\":\"luchao3417\",\"oldTicketInfos\":[{\"passengerId\":\"11541914\",\"passengerName\":\"陆超\",\"passportNo\":\"310230198909231459\",\"passportTypeId\":\"1\",\"passportTypeName\":\"二代身份证\",\"piaoType\":\"1\",\"provinceCode\":\"\",\"schoolCode\":\"\",\"schoolName\":\"\",\"studentNo\":\"\",\"schoolSystem\":\"\",\"enterYear\":\"\",\"preferenceFromStationName\":\"\",\"preferenceFromStationCode\":\"\",\"preferenceToStationName\":\"\",\"preferenceToStationCode\":\"\",\"oldTicketNo\":\"\"}],\"changeCheCi\":\"6056\",\"changeDateTime\":\"2017-01-09 08:10\",\"changeZwCode\":\"1\"}}";
		
		logger.info("途牛获取12306的验证码业务参数：" + data);
		TuniuController tc = new TuniuController();
		Parameter parameter = new TuniuParameter(data); 
		TuniuOrderService tuniuOrderService = new TuniuOrderServiceImpl();
		//获取订单信息中的车票状态信息  
		String accountUrl =ConfigUtil.getValue("accountUrl");
		String captchaResultUrl = ConfigUtil.getValue("captchaResultUrl");
		String robotUrl = ConfigUtil.getValue("robotUrl");
		String changeUrl = ConfigUtil.getValue("changeUrl"); 
		TuniuChangeService tuniuChangeService = new TuniuChangeServiceImpl();
		Result result = new TuniuResult();
		if (parameter.getInt("businessType")==2){		
			result = tuniuOrderService.captchaTrainBook(parameter,accountUrl,captchaResultUrl,robotUrl,1); 
//			if (result.getCode().equals(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR)) {
//				result = tuniuOrderService.captchaTrainBook(parameter,accountUrl,captchaResultUrl,robotUrl,1); 
//			}
		}else if(parameter.getInt("businessType")==3)		
			result = tuniuChangeService.addChangeCaptcha(parameter,captchaResultUrl,changeUrl); 
//		wrapSynOutput(result, synOutput); 
		System.out.println(result.toString());
	}
	 
}
