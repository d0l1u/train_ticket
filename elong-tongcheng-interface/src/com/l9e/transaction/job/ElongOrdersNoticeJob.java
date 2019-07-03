package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.ElongNoticeService;
import com.l9e.transaction.vo.ElongNoticeVo;
import com.l9e.transaction.vo.ElongOrderNoticeVo;
/**
 *	elong订单结果通知job
 * 	@author liuyi02
 *
 */
@Component("elongOrdersNoticeJob")
public class ElongOrdersNoticeJob {
	@Resource
	private ElongNoticeService elongNoticeService;
	
	private static final Logger logger = Logger.getLogger(ElongOrdersNoticeJob.class);
	
	private ElongOrderNoticeVo orderNoticeVo=null;
	/**
	 *	订单结果通知
	 */
	public void ordersStatusNotice(){
		List<ElongOrderNoticeVo> list=new ArrayList<ElongOrderNoticeVo>();
		//数据库查询艺龙可通知出票结果的数据
		list=elongNoticeService.getOrderNoticesList();
		int size=list.size();
		
		List<ElongOrderNoticeVo> sendList=new ArrayList<ElongOrderNoticeVo>();
		if(list.size()>0){
			/*for(int i=0;i<size;i++){
				ElongOrderNoticeVo orderNoticeVo=list.get(i);
				int num=
					orderNoticeVo.getOut_notify_num();
				if(num==0){
					elongNoticeService.updateNoticeBegin(orderNoticeVo);
				}else{
					elongNoticeService.updateNoticeTime(orderNoticeVo);
				}
				
			}*/
			for(int i=0;i<size;i++){
				ElongOrderNoticeVo orderNoticeVo=list.get(i);
				int count=elongNoticeService.updateNoticeBegin(orderNoticeVo);
				if(count>0){
					//long start =System.currentTimeMillis();
					//elongNoticeService.addSendOrdersNotice(orderNoticeVo);
					sendList.add(orderNoticeVo);
					logger.info("艺龙先支付,"+orderNoticeVo.getOrder_id()+"通知出票结果,订单开始");
				}else{
					logger.info("艺龙先支付,"+orderNoticeVo.getOrder_id()+"通知出票结果,订单锁定");
				}
			}
		}
		
		
		
		if(sendList.size()>0){
			for(ElongOrderNoticeVo vo:sendList){
				long start =System.currentTimeMillis();
				elongNoticeService.addSendOrdersNotice(vo);
				logger.info("艺龙先支付,"+vo.getOrder_id()+"通知出票结果,耗时"+(System.currentTimeMillis()-start)+"ms");
			}
		}
	}
	
	
	
	
}




