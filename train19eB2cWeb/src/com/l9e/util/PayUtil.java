package com.l9e.util;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
/**
 * 接口参数工具类
 * @author yangchao
 *
 */
public class PayUtil {

	/**
	 * 传入接口字符串，和单个参数名,返回参数名对应的值
	 * @param res
	 * @param param
	 * @return
	 */
	public static String getValue(String res,String param){
		if(StringUtils.isEmpty(res)){
			return null;
		}
		if(StringUtils.isEmpty(param)){
			return null;
		}
		Map<String,String>  paramMap=new HashMap<String,String>();
		String value[]=res.split("&");
		if(value!=null && value.length>0){
			for(int i=0;i<value.length;i++){
				String one[]=value[i].split("=");
				if(one.length==2){
					paramMap.put(one[0], one[1]);
				}else{
					paramMap.put(one[0], "");
				}
			}
		}
		return paramMap.get(param);
	}
	
	
	/**
	 * 传入接口字符串，返回map,map的key和value对应参数名和参数的值
	 * @param res
	 * @return
	 */
	public static Map<String,String> getValueMap(String res){
		Map<String,String>  paramMap=new HashMap<String,String>();
		if(StringUtils.isEmpty(res)){
			return null;
		}else{
			String value[]=res.split("&");
			if(value!=null && value.length>0){
				for(int i=0;i<value.length;i++){
					String one[]=value[i].split("=");
					if(one.length==2){
						paramMap.put(one[0], one[1]);
					}else{
						paramMap.put(one[0], "");
					}
				}
			}
			
		}
		return paramMap;
	}
}
