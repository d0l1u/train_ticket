package com.l9e.transaction.job;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CtripAccountService;
import com.l9e.transaction.vo.CtripAcc;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;



/**
 * 定时更新携程账号礼品卡余额
 * @author wangsf01
 *
 */
@Component("ctripAccountUpdateJob")
@Deprecated
public class CtripAccountUpdateJob {
	
	private static final Logger logger = Logger.getLogger(CtripAccountUpdateJob.class);
	
	@Resource
	private CtripAccountService ctripAccountService;
	
	@Value("#{propertiesReader[queryCtripCardBalance]}")
	private  String queryCtripCardBalance;//查询携程礼品卡余额的请求url
	
	public void updateCtripAcc(){
		logger.info("ctripAccountUpdateJob start~~~");
		List<CtripAcc> ctripAccList = null;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		//为了统计所以的携程账号余额，把状态放开
		//queryMap.put("ctripStatus", CtripAcc.STATUS_WORKING);
		
		//查询携程账号列表
		ctripAccList = ctripAccountService.queryCtripAccountList(queryMap);
		logger.info("ctripAccountUpdateJob 查询得到的ctripAccList为: "+ ctripAccList);
		
		if (ctripAccList.size() > 0) {
			CtripAcc ctripAccount = null;
			Integer ctripID = null;
			String ctripName = null;
			String ctripPassword = null;
			String cookie = null;
			String cid = null;
			String auth = null;
			String sauth = null;

			for (int i = 0; i < ctripAccList.size(); i++) {
				ctripAccount = ctripAccList.get(i);
				logger.info("ctripAccountUpdateJob 循环遍历得到的携程账号实体为: "+ ctripAccount);
				
				if (null != ctripAccount) {
					ctripID = ctripAccount.getCtripId();//携程账号ID
					ctripName = ctripAccount.getCtripName();//携程账号名
					ctripPassword = ctripAccount.getCtripPassword();//携程账号密码
					cookie = ctripAccount.getCookie();//cookie缓存值
					cid = ctripAccount.getCid();//携程wap端帐号cid
					auth = ctripAccount.getAuth();//携程wap端帐号auth
					sauth = ctripAccount.getSauth();//携程wap端帐号sauth

					
					//为请求查询余额接口封装参数
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("dealType", "ctripApp");
					paramMap.put("ScriptPath", "amountCtrip");
					paramMap.put("SessionID", String.valueOf(System.currentTimeMillis()));
					paramMap.put("Timeout", "10000");
					paramMap.put("ParamCount", "1");

					StringBuffer sb = new StringBuffer();
					sb.append(ctripID + "|");
					sb.append(ctripName + "|");
					sb.append(ctripPassword + "|");
					sb.append(cookie + "|");
					sb.append(cid + "|");
					sb.append(auth + "|");
					sb.append(sauth + "|");
					paramMap.put("Param1", sb.toString());
					
					try {
						String param = UrlFormatUtil.createUrl("", paramMap);
						logger.info("ctripAccountUpdateJob 请求参数param为: "+ param);
						
						//查询账号礼品卡余额接口   
//						String url = "http://43.241.227.136:8091/RunScript";
//						String reqResult = HttpUtil.sendByPost(url, param,"UTF-8"); //post请求方式测试不通过
						//String url = "http://103.37.157.78:8091/RunScript"+"?"+param;
						logger.info("ctripAccountUpdateJob 查询礼品卡余额的请求url为: "+ queryCtripCardBalance);
						String url = queryCtripCardBalance+"?"+param;	
						String reqResult = HttpUtil.sendByGet(url,"UTF-8","30000","30000");
						logger.info("ctripAccountUpdateJob 请求返回结果reqResult为: "+ reqResult);

						// success(是否请求成功)|ctripID(携程账号ID)|balance(余额)|msg(返回的提示信息)
						if (!StringUtils.isEmpty(reqResult)) {
							String[] result = reqResult.split("\\|");
							logger.info("ctripAccountUpdateJob 请求返回结果转换为数组后为: "+ result);
											
							if ("success".equals(result[0])) {
															
								// 如果查询余额成功，则更新余额和状态
								Map<String, Object> updateMap = new HashMap<String, Object>();		
								updateMap.put("optStatus",CtripAcc.OPT_STATUS_0);// 0-空闲   1-使用中
								updateMap.put("balance", Double.parseDouble(result[2]));// 余额
								updateMap.put("resultType",0);// 查询返回结果类型：0：正常   1：账号异常    2：查询超时  
								updateMap.put("ctripId", ctripID);

								ctripAccountService.modifyCtripAccount(updateMap);
								logger.info("ctripAccountUpdateJob 更新账号状态和余额成功!");
								
							} else{
								
								String msg = URLDecoder.decode(result[3],"UTF-8");//请求接口返回的提示信息  解码
								logger.info("ctripAccountUpdateJob 请求返回结果转换为数组后,获取的msg解码后为: "+ msg);
								
								if (msg.contains("账号异常")) {
									// 如果返回的提示信息中包含 账号异常，则更新result_type为1
									Map<String, Object> updateMap = new HashMap<String, Object>();
									updateMap.put("resultType", 1);// 查询返回结果类型：0：正常   1：账号异常   2：查询超时
									updateMap.put("ctripId", ctripID);

									ctripAccountService.modifyCtripAccount(updateMap);
									logger.info("ctripAccountUpdateJob 账号异常，携程账号表更新result_type为1 !");
								} else if (msg.contains("查询超时")) {
									// 如果返回的提示信息中包含 查询超时，则更新result_type为2
									Map<String, Object> updateMap = new HashMap<String, Object>();
									updateMap.put("resultType", 2);// 查询返回结果类型：0：正常   1：账号异常  2：查询超时
									updateMap.put("ctripId", ctripID);

									ctripAccountService.modifyCtripAccount(updateMap);
									logger.info("ctripAccountUpdateJob 查询超时，携程账号表更新result_type为2 !");
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				try {
					// 休眠4秒后继续下一个余额查询更新
					Thread.sleep(4 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		logger.info("ctripAccountUpdateJob end~~~");
	}

}
