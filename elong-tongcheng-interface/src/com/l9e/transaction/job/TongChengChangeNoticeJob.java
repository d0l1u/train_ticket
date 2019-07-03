package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.TongChengChangeService;
import com.l9e.transaction.vo.DBChangeInfo;

/**
 * 改签异步回调job
 * @author licheng
 *
 */
@Component("tongChengChangeNoticeJob")
public class TongChengChangeNoticeJob {
	
	private static final Logger logger = Logger.getLogger(TongChengChangeNoticeJob.class);
	
	@Resource
	private TongChengChangeService tongChengChangeService;
	
	private Timer timer;
	
//	@PostConstruct
	public void timerNotice() {
		if (timer != null) {
			timer.cancel();
		}
		
		timer = new Timer();
		final int time=20 * 1000;
		
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					logger.info("同程改签通知timer运行... ...");
					tongChengChangeNotice();
				} catch (Exception e) {
					logger.error("同程改签通知timer异常" + e);
				}
			}
		}, 5 * 1000, time);
	}

	/**
	 * 改签结果通知
	 */
	public void tongChengChangeNotice() {
		/*获取回调通知列表*/
		
		List<DBChangeInfo> preNotifyList = tongChengChangeService.getNotifyList();
		List<Integer> notifyIdList = new ArrayList<Integer>();
		
		
		if(preNotifyList != null && preNotifyList.size() != 0) {
			//更新通知状态为开始通知,可避免其他job重复拉取记录
			for(DBChangeInfo changeInfo : preNotifyList) {
				int count = tongChengChangeService.updateNotifyBegin(changeInfo);
				if(count > 0) {
					notifyIdList.add(changeInfo.getChange_id());
					logger.info("同城改签回调,"+changeInfo.getOrder_id()+"准备开始");
				} else {
					logger.info("同城改签回调,"+changeInfo.getOrder_id()+"锁定");
				}
			}
		}
		
		for(Integer change_id : notifyIdList) {
			long start = System.currentTimeMillis();
			tongChengChangeService.notifyChange(change_id);
			logger.info("同城异步改签回调,change_id : " + change_id + "结束,耗时" + (System.currentTimeMillis() - start) + "ms");
		}
	}
	
//	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:context/applicationContext.xml");
//		TongChengChangeNoticeJob job = (TongChengChangeNoticeJob) context.getBean("tongChengChangeNoticeJob");
//		job.tongChengChangeNotice();
//	}
	
}
