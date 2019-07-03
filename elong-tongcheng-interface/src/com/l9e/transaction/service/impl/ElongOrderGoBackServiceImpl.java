package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.l9e.common.Consts;
import com.l9e.transaction.dao.ElongOrderDao;
import com.l9e.transaction.dao.ElongOrderGoBackDao;
import com.l9e.transaction.service.ElongOrderGoBackService;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.util.HttpUtil;
import com.l9e.util.elong.ElongJsonUtil;
import com.l9e.util.elong.ElongMd5Util;
import com.l9e.util.elong.ElongUrlFormatUtil;
@Service("elongOrderGoBackService")
public class ElongOrderGoBackServiceImpl implements ElongOrderGoBackService{

	private static final Logger logger = Logger.getLogger(ElongOrderGoBackServiceImpl.class);
	@Resource
	private  ElongOrderGoBackDao  ElongOrderGoBackDao;
	
	
	@Resource
	private ElongOrderDao elongOrderDao;
	@Override
	public List<Map<String,Object>> getOrderGoBackIdList() {
		return ElongOrderGoBackDao.getOrderGoBackIdList();
	}


	@Override
	/**事务限制*/
	public void deleteAllOrderInfo(String orderId) {
		ElongOrderGoBackDao.insertAllElongCpInfo(orderId);//备份数据
		ElongOrderGoBackDao.insertElongOrderInfo(orderId);//备份数据
		
		ElongOrderGoBackDao.deleteAllElongCpInfo(orderId);
		ElongOrderGoBackDao.deleteAllElongNotifyInfo(orderId);
		ElongOrderGoBackDao.deleteAllElongOrderInfo(orderId);
		
		ElongOrderGoBackDao.deleteAllSysCpInfo(orderId);
		ElongOrderGoBackDao.deleteAllSysNotifyInfo(orderId);
		ElongOrderGoBackDao.deleteAllSysOrderInfo(orderId);
	}


	@Override
	public void updateGoBackNotify(Map<String,String> param) {
		ElongOrderGoBackDao.updateGoBackNotify(param);
	}


	@Override
	public void doLogAndNotifyUpdate(String resutlStr, int notifyNum,String order_id,String message) {
		ElongOrderLogsVo log=new ElongOrderLogsVo();
		log.setOpt_person("elong_app");
		log.setOrder_id(order_id);
		
		Map<String,String> notifyMap=new HashMap<String,String>();
		notifyMap.put("order_id", order_id);
		notifyMap.put("notify_num", (notifyNum+1)+"");
		if("SUCCESS".equals(resutlStr)){
			log.setContent("通知艺龙订单回库_通知成功["+message+"]");
			notifyMap.put("notify_status", Consts.NOTICE_OVER);
			notifyMap.put("notify_finish_time", "success");
		}else if("FAIL".equals(resutlStr)){
			log.setContent("通知艺龙订单回库_通知失败╮(╯_╰)╭["+message+"]");
			notifyMap.put("notify_status", (notifyNum==Consts.GOBACK_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING);
		}else{
			log.setContent("通知艺龙订单回库_通知异常╮(╯_╰)╭["+message+"]");
			notifyMap.put("notify_status", (notifyNum==Consts.GOBACK_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING);
		}
		
		if("FAIL".equals(resutlStr)||"EXCEPTION".equals(resutlStr)){
			if(notifyNum==Consts.GOBACK_NOTICE_NUM-1){
				/**更新订单状态为  撤销失败状态*/
				ElongOrderGoBackDao.updateGoBackOrderInfoStatusFail(order_id);
			}
		}
		//更新通知表
		ElongOrderGoBackDao.updateGoBackNotify(notifyMap);
		//插入日志
		elongOrderDao.insertElongOrderLogs(log);
		
	}

}
