package com.l9e.common;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.l9e.util.MemcachedUtil;

/**
 * 车票预订 
 * @author zhangjun
 *
 */
public class ExternalBase extends BaseController {
	
	protected static final Logger logger = Logger.getLogger(ExternalBase.class);
	
	@Value("#{propertiesReader[real_name_verify_url]}")
	protected String real_name_verify_url;//实名认证
	
	@Value("#{propertiesReader[soukd_query_left_ticket_url]}")
	protected String soukd_query_left_ticket_url;//soukd查询地址
	
	protected static Random random = new Random();
	
	/**
	 * 获取系统接口频道
	 * @param provinceId
	 * @return
	 */
	protected String getSysInterfaceChannel(String key){
		String channel = null;//1:12306接口 2:SOUKD接口,3:新接口
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			channel = commonService.querySysSettingByKey(key);
			MemcachedUtil.getInstance().setAttribute(key, channel, 60*1000);
			
		}else{
			channel = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return channel;
	}
	
	//余票阀值控制
	protected String replaceNumVal(String str,String limit_num){
		if(!StringUtils.isEmpty(str)){
			if(!"-".equals(str) && Integer.parseInt(str)<=Integer.parseInt(limit_num)){
				str = "-";
			}
		}
		return str;
	}
}
