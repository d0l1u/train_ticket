package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.common.TuniuConstant;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.service.TuniuWhitePassService;
import com.l9e.transaction.vo.TuniuWhitePass;
import com.l9e.util.DateUtil;
import com.l9e.util.EncryptUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.JacksonUtil;
import com.l9e.util.MemcachedUtil;
@Component("tuniuIdenValiJob")
public class TuniuIdenValiJob {
	private static final Logger logger = Logger.getLogger(TuniuIdenValiJob.class);
	@Resource
	private TuniuWhitePassService tuniuWhitePassService;
	@Resource
	private TuniuCommonService tuniuCommonService;
	/**
	 * 身份核验信息上传--全量
	 */
	public void indentityValidationUpload(){
		Date begin = new Date();
		int num=20;
		int lastId = 0;
		if(MemcachedUtil.getInstance().getAttribute("tuniuWhitelistId")!=null){
			lastId = (Integer)MemcachedUtil.getInstance().getAttribute("tuniuWhitelistId");
		}
		List<TuniuWhitePass> list = tuniuWhitePassService.getWhitePassList(lastId, num);
		
		logger.info("途牛通知白名单全量,记录数为:"+list.size());
	
		if(list.size()>0){
			String requestUrl = TuniuConstant.idenValiUploadUrl;
			Map<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("number", list.size());
			dataMap.put("vendorOrderId", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2));
			List<Map<String,Object>> pass =new ArrayList<Map<String,Object>>();
			int whitelistId = lastId;
			for(TuniuWhitePass whitePass:list){
				Map<String,Object> whitePassMap =  new HashMap<String,Object>();
				whitelistId = whitePass.getId();
				whitePassMap.put("idCard", whitePass.getCert_no());
				whitePassMap.put("userName", whitePass.getContact_name());
				whitePassMap.put("type", getIdCardType(whitePass.getCert_type()));
				whitePassMap.put("idCardName", getIdCardName(getIdCardType(whitePass.getCert_type())));
				whitePassMap.put("status", 1);
				pass.add(whitePassMap);
			}
			dataMap.put("idCardList", pass);
			try {
				Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("account", TuniuConstant.account);
				String timestamp = DateUtil.dateToString(new Date(),TuniuConstant.TIMESTAMP_FORMAT);
				resultMap.put("timestamp", timestamp);
				resultMap.put("returnCode", "231000");
				String data = "";
				logger.info("全量白名单,json->"+JacksonUtil.generateJson(dataMap));
				if (!StringUtils.isEmpty(dataMap.toString())) {
					data = EncryptUtil.encode(JacksonUtil.generateJson(dataMap), TuniuConstant.signKey, "UTF-8");
					resultMap.put("data", data);
				}
				/* 签名 */
				Map<String, String> params = new HashMap<String, String>();
				params.put("account", TuniuConstant.account);
				params.put("timestamp", timestamp);
				params.put("returnCode", "231000");
				params.put("data", data);
				String sign = tuniuCommonService.generateSign(params,TuniuConstant.signKey);
				resultMap.put("sign", sign);
				logger.info("post请求地址: " + requestUrl + ", 参数: " + resultMap.toString());
				String result = HttpUtil.sendByPost(requestUrl, JacksonUtil.generateJson(resultMap), "UTF-8");
				logger.info( lastId+"-"+ whitelistId+",请求返回结果,result:" + result);
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.info("途牛身份核验信息上传，请求异常，" + requestUrl);
			}
			logger.info("途牛身份核验信息上传"+dataMap.get("vendorOrderId")+",上传到"+whitelistId);
			Date end = new Date();
			logger.info("途牛身份核验信息上传，耗时"+(end.getTime()-begin.getTime()));
			MemcachedUtil.getInstance().setAttribute("tuniuWhitelistId", whitelistId);
		}
		
		
	}
	/**
	 * 证件类型名称
	 * @param passporttypeseid
	 * @return
	 */
	public String getIdCardName(String cert_type) {
		if(cert_type == null || "".equals(cert_type)) {
			return "";
		}
		String passporttypeseidname = "";
		if(cert_type.equals("2")) {
			passporttypeseidname = "一代身份证";
		} else if(cert_type.equals("C")) {
			passporttypeseidname = "港澳通行证";
		} else if(cert_type.equals("B")) {
			passporttypeseidname = "护照";
		} else if(cert_type.equals("1")) {
			passporttypeseidname = "二代身份证";
		} else if(cert_type.equals("G")) {
			passporttypeseidname = "台湾通行证";
		}else  if(cert_type.equals("H")) {
			passporttypeseidname = "外国人居留证";
		}
			
		return passporttypeseidname;
	}
	
	
	/**
	 * 证件类型编号,转换
	 * @param cert_type
	 * @return
	 */
	public String getIdCardType(String cert_type) {
		
		if(cert_type == null || "".equals(cert_type)) {
			return "";
		}
		String passporttypeseid = "";
		if(cert_type.equals("2")) {
			passporttypeseid = "1";
		} else if(cert_type.equals("C")) {
			passporttypeseid = "C";
		} else if(cert_type.equals("B")) {
			passporttypeseid = "B";
		} else if(cert_type.equals("1")) {
			passporttypeseid = "2";
		} else if(cert_type.equals("G")) {
			passporttypeseid = "G";
		}else if(cert_type.equals("H")) {
			passporttypeseid = "H";
		}
		
		return passporttypeseid;
	}
	

}
