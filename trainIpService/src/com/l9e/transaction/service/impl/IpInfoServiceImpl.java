package com.l9e.transaction.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.IpInfoDao;
import com.l9e.transaction.dao.RedisDao;
import com.l9e.transaction.service.IpInfoService;
import com.l9e.transaction.vo.IpInfo;
import com.l9e.util.ConfigUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.UrlFormatUtil;

@Service("ipInfoService")
public class IpInfoServiceImpl implements IpInfoService{
	private static final Logger logger = Logger.getLogger(IpInfoServiceImpl.class);
	@Resource
	private IpInfoDao ipInfoDao;
	
	@Resource
	private RedisDao redisDao;
	


	/**
	 * 准备IP
	 * @param type IP类型 
	 */
	@Override
	public void setIpInfo(Integer type) {
		// TODO Auto-generated method stub
        logger.info("开始存入IP地址");
		
		if(type == IpInfo.TYPE_CTRIP) {
			setCtripIpInfo();
		} else if(type == IpInfo.TYPE_BOOKIP) {
			setBookIpInfo();
		}
	}
	
	/**
	 * 查询多个携程预定代理IP，并放入队列中
	 */
	public void setCtripIpInfo(){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ipType", IpInfo.TYPE_CTRIP);
		params.put("ipStatus", IpInfo.STATUS_FREE);
		params.put("limit", 10);
		List<IpInfo> ctripIpInfos = ipInfoDao.selectIpList(params);
		ctripIPlpush(IpInfo.TYPE_CTRIP, ctripIpInfos);
			
	}
	
	/**
	 * 查询多个预定代理IP，并放入队列中
	 */
	public void setBookIpInfo(){ 
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ipType", IpInfo.TYPE_BOOKIP); 
		params.put("ipStatus", IpInfo.STATUS_FREE);
		params.put("limit", 10);
		List<IpInfo> bookIpInfos = ipInfoDao.selectIpList(params);
		bookLpush(IpInfo.TYPE_BOOKIP, bookIpInfos);

	}
	
		
	/**
	 * 预定代理IP主键进入队列
	 * @param type
	 * @param ipInfos
	 */
	private void bookLpush(Integer type, List<IpInfo> ipInfos) {
		try {
			if(ipInfos != null && ipInfos.size() > 0) {
				for(IpInfo ipInfo : ipInfos) {
					
					//在此先判断各个IP的下单次数是否达上限
					Integer limitNum = ipInfo.getRequestNum();
					logger.info("本台机器的下单次数为: " + limitNum);
					String ifSupportChange = ipInfo.getIfSupportChange(); //机器是否支持切换新旧IP功能： 0: 支持   1: 不支持  只有美团云机器支持这一功能。
					logger.info("本台机器是否支持切换IP的标识为: " + ifSupportChange);
					String ipExt = ipInfo.getIpExt(); //192.168.1.1:808:wxg:wxg
					String[] ipExtArr=ipExt.split("\\:");
					String currentIP = ""; //最终放入队列的IP
					String oldIP = ipExtArr[0];//旧IP
					logger.info("[机器上原先的预定IP]为: " + oldIP);
					
					if( limitNum > 20){
						
						//如果本机器的下单次数已达上限,则判断这台机器是否支持切换IP 0-支持  1-不支持
						if("0".equals(ifSupportChange)){
							//进行美团云切换IP操作
							Date currentDate=new Date();
							String formatStr = "yyyyMMddHHmmss";
							String currentDateStr=DateUtil.dateToString(currentDate, formatStr);
							String ipName = "l9eip"+currentDateStr;//浮动IP的自定义名称  唯一
							
							String newIP = changeMtyunIp(ipName,oldIP);//美团云切换IP后返回的新IP
							logger.info("[切换后的新美团云预定IP]为: " + newIP);
							
							currentIP = newIP;
							
							//切换IP以后，更新新IP到数据库，并把下单次数设为0
							StringBuffer ipStr = new StringBuffer();
							ipStr.append(currentIP).append(":").append("808").append(":").append("wxg")
									.append(":").append("wxg");
							limitNum = 0;//换成新IP后，需要把下单次数改为0
							
							//IpInfo changeIP =new IpInfo();
							ipInfo.setOptionTime("optionTime");
							ipInfo.setRequestNum(limitNum);
							ipInfo.setIpExt(ipStr.toString());
							ipInfoDao.updateIpInfo(ipInfo);
							logger.info("当前美团云机器下单次数达上限时,新购IP,执行新旧IP切换操作，并把新IP更新到表中！");
							
							//往cp_ipinfo_log表插入一条日志记录
							String msg = "当前美团云机器下单次数已达上限，新购美团云IP,执行新旧IP切换操作，并更新IP。";
							Map<String, Object> insertMap = new HashMap<String, Object>();
							insertMap.put("old_ip", oldIP);
							insertMap.put("new_ip", currentIP);
							insertMap.put("content", msg);
							insertMap.put("create_time", "now()");
							ipInfoDao.insertIpInfoLog(insertMap);
							logger.info("当前美团云机器下单次数达上限时,新购IP,执行新旧IP切换操作，并往日志表中插入一条记录！");	
						}else if("1".equals(ifSupportChange)){
							continue;  //如果不支持切换IP，则结束本次循环，执行下次循环
						}
					
					}else{
						currentIP = oldIP;
					}
					
					logger.info("最终的预定代理IP为: " + currentIP);
					//在此先进行代理IP授权核验，如果核验成功，则放入队列，否则不放
					StringBuffer ipStr = new StringBuffer();
					ipStr.append(currentIP).append(":").append("808").append(":").append("wxg")
							.append(":").append("wxg");
					logger.info("[拼接后要进行授权核验的美团云IP]为: " + ipStr.toString());
					
					//授权核验
					String verifyResult = verifyProxyIp(ipStr.toString());
					logger.info("[代理IP授权核验的结果]为: " + verifyResult);
					
					if (!StringUtils.isEmpty(verifyResult)) {

						if ("success".equals(verifyResult)) {
							// 如果授权核验通过，则放入队列
							//IpInfo passIP =new IpInfo();
							limitNum++;//订单数加1
							ipInfo.setOptionTime("optionTime");
							ipInfo.setIpExt(ipStr.toString());
							ipInfo.setRequestNum(limitNum);
							ipInfoDao.updateIpInfo(ipInfo);//先更新在放入队列
							redisDao.LPUSH(StrUtil.getIpInfoQueue(type), ipInfo.getIpId());
							logger.info("最终的预定代理IP授权核验通过,更新对应的IP记录，放入队列成功！");
							
							//往cp_ipinfo_log表插入一条日志记录
							String msg = "最终的预定代理IP，授权核验通过，放入队列成功。";
							Map<String, Object> insertMap = new HashMap<String, Object>();
							insertMap.put("old_ip", oldIP);
							insertMap.put("new_ip", currentIP);
							insertMap.put("content", msg);
							insertMap.put("create_time", "now()");
							ipInfoDao.insertIpInfoLog(insertMap);
							logger.info("最终的预定代理IP，授权核验通过，放入队列成功,并往日志表中插入一条记录！");
						}else{
							// 如果授权核验没有通过，则不放入队列
							//IpInfo noPassIP =new IpInfo();
							ipInfo.setOptionTime("optionTime");
							ipInfo.setIpExt(ipStr.toString());
							ipInfoDao.updateIpInfo(ipInfo);	
							logger.info("最终的预定代理IP授权核验不通过,更新对应的IP记录,放入队列失败！");
							
							//往cp_ipinfo_log表插入一条日志记录
							String msg = "最终的预定代理IP，授权核验没有通过，放入队列失败。";
							Map<String, Object> insertMap = new HashMap<String, Object>();
							insertMap.put("old_ip", oldIP);
							insertMap.put("new_ip", currentIP);
							insertMap.put("content", msg);
							insertMap.put("create_time", "now()");
							ipInfoDao.insertIpInfoLog(insertMap);
							logger.info("最终的预定代理IP，授权核验没有通过，放入队列失败,并往日志表中插入一条记录！");
						}

					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 携程代理IP主键进入队列   
	 * @param type
	 * @param ipInfos
	 */
	private void ctripIPlpush(Integer type, List<IpInfo> ipInfos) {
		try {

			if(ipInfos != null && ipInfos.size() > 0) {
				for(IpInfo ipInfo : ipInfos) {
				
						// 在此先进行代理IP授权核验，如果核验成功，则放入队列，否则不放
						String verifyResult = verifyCtripProxyIp(ipInfo.getIpExt());
						logger.info("[IP授权核验的结果]为: " + verifyResult);

						if (!StringUtils.isEmpty(verifyResult)) {

							if ("success".equals(verifyResult)) {
								// 如果授权核验通过，则放入队列
								ipInfo.setOptionTime("optionTime");
								ipInfoDao.updateIpInfo(ipInfo);// 先更新在放入队列
								redisDao.LPUSH(StrUtil.getIpInfoQueue(type),ipInfo.getIpId());
								logger.info("当前IP授权核验成功,更新当前IP记录，并放入队列！");
							} else {
								// 如果授权核验没有通过，则不放入队列
								ipInfo.setOptionTime("optionTime");
								ipInfoDao.updateIpInfo(ipInfo);
								logger.info("当前IP授权核验不成功,更新当前IP记录！");
								
							}

						}

					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据指定类型获取一个队列中IP
	 * @param type IP类型 
	 * @return
	 */
	@Override
	public IpInfo getIpInfo(Integer type) {
		// TODO Auto-generated method stub
		logger.info("开始获取IP: " + type);
		String key = StrUtil.getIpInfoQueue(type);
		Long lLen = redisDao.LLEN(key);
		IpInfo ipInfo = null;
		try {
			if(lLen > 0) {
				//清空缓存使用
//				for(int i=0;i<lLen;i++){
//					Object obj = redisDao.RPOP(key);
//				}
				Object obj = redisDao.RPOP(key);
				if(obj != null) {
					Integer ipId = (Integer) obj;
					ipInfo = queryIpInfoById(ipId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ipInfo;
	}

	/**
	 * 从库中查询某个具体IP地址
	 * @param ipId IP-id
	 * @return
	 */
	@Override
	public IpInfo queryIpInfoById(Integer ipId) {
		// TODO Auto-generated method stub
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ipId", ipId);
		return ipInfoDao.selectOneIp(params);
	}

	/**
	 * 修改IP信息
	 * @param ipInfo IP实体
	 */
	@Override
	public void updateIpInfo(IpInfo ipInfo) {
		// TODO Auto-generated method stub
		ipInfoDao.updateIpInfo(ipInfo);
	}

	
	/**
	 * 切换美团云IP，把旧IP换成新购IP
	 * @param ipName 浮动IP的自定义名称  唯一(以字母开头，仅包含字母、数字或中划线的3-40个字符)
	 * @param oldIP 机器本来的IP
	 * @return 切换后的新IP
	 */
	@Override
	public String changeMtyunIp(String ipName, String oldIP) {
		// TODO Auto-generated method stub
		String newIP = "";// 新IP
		try {
			// 新购IP
			String buyNewIpResult = HttpUtil.sendByPost(ConfigUtil
					.getProperty("mtyunAllocateAddress"), "commond="
					+ "AllocateAddress" + "&ip_name=" + ipName, "utf-8");
			logger.info("[新购浮动IP的JSON串为]" + buyNewIpResult);

			if (!StringUtils.isEmpty(buyNewIpResult)) {
				JSONObject buyNewIpObject = JSONObject.fromObject(buyNewIpResult);
				newIP = buyNewIpObject.getString("publicIp");
			}

			// 切换IP
			if (newIP != null && !"".equals(newIP)) {
				String changeIpResult = HttpUtil.sendByPost(ConfigUtil
						.getProperty("mtyunReplaceAddress"), "commond="
						+ "ReplaceAddress" + "&allocationId=" + oldIP
						+ "&newAllocationId=" + newIP, "utf-8");
				logger.info("[切换云产品上绑定的浮动IP]返回结果为: " + changeIpResult);
			}

			// 释放旧IP 中间间隔太短会报异常，另写一个定时任务来释放IP
//			String releaseIpResult = HttpUtil.sendByPost(ConfigUtil
//					.getProperty("mtyunReleaseAddress"), "commond="
//					+ "ReleaseAddress" + "&allocationId=" + oldIP, "utf-8");
//			logger.info("[释放浮动IP]返回结果为: " + releaseIpResult);
			
			//往cp_ipinfo_release表插入一条待释放IP(旧IP)的记录，用定时job定时释放该旧IP
			Map<String, Object> insertMap = new HashMap<String, Object>();
			insertMap.put("ip_ext", oldIP);
			insertMap.put("release_status", "0"); //0：待释放  1：释放成功  2：释放失败  3：释放中
			insertMap.put("create_time", "now()");
			ipInfoDao.insertIpInfoRelease(insertMap);
			logger.info("往cp_ipinfo_release表插入一条待释放IP的记录！");

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("美团云切换后的新IP为: " + newIP);
		return newIP;
	}

	/**
	 * 12306--IP授权核验接口
	 * @param proxyIP 代理IP
	 * @return 授权核验返回的结果
	 */
	@Override
	public String verifyProxyIp(String proxyIP) {
		// TODO Auto-generated method stub
		
		//调IP授权核验接口
		String deviceNo=StrUtil.getRandomString(16);
		
		Map<String, String> maps = new HashMap<String,String>();
		maps.put("commond", "checkIP");
		maps.put("ipExt", proxyIP);
		maps.put("deviceNo", deviceNo);
	
		String param = UrlFormatUtil.createUrl("", maps,"UTF-8");
		logger.info("调用12306-代理IP授权核验接口    params为: "+param);
		
		String url = ConfigUtil.getProperty("checkIPUrl");
		logger.info("12306-代理IP授权核验接口URL为: "+url);
		
		String reqResult = HttpUtil.sendByPost(url, param, "UTF-8");
		logger.info("调用12306-代理IP授权核验接口，请求的返回结果 reqResult为: "+reqResult);
		
		return reqResult;
	}
	
	/**
	 * 携程--IP授权核验接口
	 * @param proxyIP 代理IP
	 * @return 授权核验返回的结果
	 */
	@Override
	public String verifyCtripProxyIp(String proxyIP) {
		// TODO Auto-generated method stub
		
		//调IP授权核验接口
		
		Map<String, String> maps = new HashMap<String,String>();
		maps.put("commond", "checkCtripIP");
		maps.put("ipExt", proxyIP);
	
		String param = UrlFormatUtil.createUrl("", maps,"UTF-8");
		logger.info("调用携程代理IP授权核验接口    params为: "+param);
		
		String url = ConfigUtil.getProperty("checkCtripIPUrl");
		logger.info("携程代理IP授权核验接口URL为: "+url);
		
		String reqResult = HttpUtil.sendByPost(url, param, "UTF-8");
		logger.info("调用携程代理IP授权核验接口，请求的返回结果 reqResult为: "+reqResult);
		
		return reqResult;
	}
	
	public static void main(String[] args){
		Map<String, String> maps = new HashMap<String,String>();
		maps.put("commond", "checkCtripIP");
		maps.put("ipExt", "112.74.22.184:808:wxg:wxg");
	
		String param = UrlFormatUtil.createUrl("", maps,"UTF-8");
		System.out.println("调用携程代理IP授权核验接口    params为: "+param);
		
		String url = ConfigUtil.getProperty("checkCtripIPUrl");
		System.out.println("携程代理IP授权核验接口URL为: "+url);
		
		String reqResult = HttpUtil.sendByPost(url, param, "UTF-8");
		System.out.println("结果:"+reqResult);
	}

}
