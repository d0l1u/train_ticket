package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.common.Consts;
import com.l9e.transaction.service.ElongOrderGoBackService;
import com.l9e.transaction.service.ElongOrderService;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.util.HttpUtil;
import com.l9e.util.elong.ElongJsonUtil;
import com.l9e.util.elong.ElongMd5Util;
import com.l9e.util.elong.ElongUrlFormatUtil;
/**
 *	elong订单回库操作
 * 	@author liuyi02
 *
 */
@Component("elongOrderGoBackJob")
public class ElongOrderGoBackJob {
	@Resource
	private ElongOrderGoBackService elongOrderGoBackService;
	
	private static final Logger logger = Logger.getLogger(ElongOrderGoBackJob.class);
	/**
	 *	订单结果通知
	 */
	public void goBack(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		//数据库查询艺龙可通知出票结果的数据
		//logger.info("艺龙订单回库处理");
		list=elongOrderGoBackService.getOrderGoBackIdList();
		int size=list.size();
		if(list.size()>0){
			for(Map<String,Object> map : list){
				this.sendOne(map);
			}
		}
	}
	private void sendOne(Map<String,Object> map) {
		String orderId=map.get("order_id")+"";
		int notify_num =Integer.parseInt(map.get("notify_num")+"");
		String merchantCode=Consts.ELONG_MERCHANTCODE;
		String sign_key =Consts.ELONG_SIGNKEY;
		String order_return=Consts.ELONG_ORDERRETURN_URL;
		try {
			Map<String,String> params=new HashMap<String,String>();
			params.put("merchantCode", merchantCode);
			params.put("orderIds", orderId);
			String sign=ElongMd5Util.md5_32(params, sign_key, "utf-8");
			params.put("sign",sign);
			
			String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
			String result=HttpUtil.sendByPost(order_return, sendParams, "utf-8");
			logger.info("通知艺龙订单回库-"+orderId+",通知返回结果"+result);
			JSONObject json=JSONObject.fromObject(result);
			String retCode=ElongJsonUtil.getString(json.get("retcode"));
			String retDesc=ElongJsonUtil.getString(json.get("retdesc"));
			if(retCode.equals("200")){
				//成功以后删除数据
				elongOrderGoBackService.deleteAllOrderInfo(orderId);
				elongOrderGoBackService.doLogAndNotifyUpdate("SUCCESS",notify_num,orderId,"retCode:"+retCode+",retDesc:"+retDesc);
			}else{
				logger.info("通知艺龙订单回库-失败"+result);
				elongOrderGoBackService.doLogAndNotifyUpdate("FAIL",notify_num,orderId,"retCode:"+retCode+",retDesc:"+retDesc);
			}
		} catch (Exception e) {
			elongOrderGoBackService.doLogAndNotifyUpdate("EXCEPTION",notify_num,orderId,"");
			logger.info("通知艺龙订单回库-异常"+e);
			e.printStackTrace();
		}
	}
}
