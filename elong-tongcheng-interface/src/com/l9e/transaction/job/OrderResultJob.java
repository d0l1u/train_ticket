package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBRemedyNoticeVo;


/**
 * 订单结果通知
 * @author liuyi02
 * 针对elong_allchannel_notify 通知表
 * */
@Component("orderResultJob")
public class OrderResultJob {
	private static final Logger logger = Logger.getLogger(OrderResultJob.class);
	@Resource
	private NoticeService noticeService;
	@Resource
	private OrderService orderService;
	
	/**
	 * 同城渠道的出票结果通知
	 * */
	public void sendTc(){
		Map<String,String> param=new HashMap<String,String>();
		param.put("num", "20");
		//param.put("channel", Consts.CHANNEL_TONGCHENG);
		List<DBNoticeVo> list=noticeService.selectOrderResultList(param);
		
		List<DBNoticeVo> sendList=new ArrayList<DBNoticeVo>();
		
		if(list.size()>0){
			for(DBNoticeVo vo:list){
				int count=noticeService.updateStartOrderResultNotice(vo);
				if(count>0){
					logger.info("JOB先预定后支付,"+vo.getOrder_id()+",出票结果通知,开始");
					sendList.add(vo);
					//orderService.addOrderResultNotice(vo);
				}else{
					logger.info("JOB先预定后支付,"+vo.getOrder_id()+",出票结果通知,锁定");
				}
			}
		}
		
		if(sendList.size()>0){
			for(DBNoticeVo vo:sendList){
				long start =System.currentTimeMillis();
				orderService.addOrderResultNotice(vo);
				logger.info("JOB先预定后支付,"+vo.getOrder_id()+",通知耗时"+(System.currentTimeMillis()-start)+"ms");
			}
		}
	}
	
	/**
	 * 同城渠道的补单结果通知
	 * */
	public void sendRemedyTc(){
		List<DBRemedyNoticeVo> list=noticeService.selectOrderRemedyList();
		if(list.size()>0){
			for(DBRemedyNoticeVo vo:list){
				orderService.addOrderRemedyNotice(vo);
			}
		}
	}
	
	
}
