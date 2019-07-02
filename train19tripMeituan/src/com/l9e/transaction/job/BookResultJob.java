package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.common.Consts;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBNoticeVo;

/**
 * 占座结果通知----火车票出票前代理商回调美团校验
 * 针对mt_allchannel_notify 通知表
 * */
@Component("bookResultJob")
public class BookResultJob {
	private static Logger logger=Logger.getLogger(BookResultJob.class);
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
		param.put("channel", Consts.CHANNEL_MEITUAN);
		List<DBNoticeVo> list=noticeService.selectBookResultList(param);
//		logger.info(list+"******************"+Consts.QUERY_TICKET);
		
		List<DBNoticeVo> sendList=new ArrayList<DBNoticeVo>();
		if(list.size()>0){
			long start =System.currentTimeMillis();
			for(DBNoticeVo vo:list){
				int count=noticeService.updateStartBookResultNotice(vo);
				if(count>0){
					logger.info("JOB先预订后支付,"+vo.getOrder_id()+",占座结果通知,开始");
					sendList.add(vo);
				}else{
					logger.info("JOB先预订后支付,"+vo.getOrder_id()+",占座结果通知,锁定");
				}
			}
			logger.info("JOB先预订后支付,通知列表数据更新耗时:"+(System.currentTimeMillis()-start)+"ms");
		}
		if(sendList.size()>0){
			for(DBNoticeVo vo:sendList){
				long start =System.currentTimeMillis();
				orderService.addBookResultNotice(vo);
				logger.info("JOB先预订后支付,:"+vo.getOrder_id()+"通知耗时"+(System.currentTimeMillis()-start)+"ms");
				/*try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		}
	}
	
}
