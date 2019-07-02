package com.l9e.common;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.l9e.transaction.dao.RedisDao;
import com.l9e.transaction.mq.MqService;
import com.l9e.transaction.service.impl.SysSettingServiceImpl;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.WorkIdNum;

public abstract class MqOrderBase {
	

	static Logger logger = Logger.getLogger(MqOrderBase.class.getClass());

	public abstract String channel();//设置渠道
	
	public abstract Integer sec();//设置执行时间
	
	public abstract Integer waitsec();//设置等待时间
	
	public abstract Integer getNum();//获取消息的数量
	
	public abstract Integer goNum();//机器人预订的数量
	
	public abstract Integer sqltime();//执行时间
	

	private Timer timer;


	@Resource
	private RedisDao redisDao;
	
	@Autowired
	private MqService mqService;
	

	@PostConstruct
	public void init() {
		this.setCoustomer(sec(),channel(),waitsec(),getNum(),goNum(),sqltime());
	}

	/*
	 * 开始获取账号
	 */
	public void setCoustomer(int sec,final String channel,final int waitsec,final int getnum,final int gonum,final int sqltime) {
		
		redisDao.delVal(StrUtil.getOrderQueue(channel));
		
		logger.info("启动time程序");
		timer = new Timer();
		
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				
				/*竞争锁机制start*/
				boolean flag = redisDao.setNXVal(StrUtil.getOrderRedis(channel), "1", 0);
				logger.info("flag的值："+flag);
				long ttl = redisDao.getTTL(StrUtil.getOrderRedis(channel));
				if(!flag){
					logger.info("未获取到竞争锁："+channel+"锁是否已经解除："+ttl);
					if(ttl!=-2){
						logger.info("释放竞争锁");
						redisDao.delVal(StrUtil.getOrderRedis(channel));
					}
					return ;
				}else{
					if(ttl==-1){
						logger.info("设置自动解锁时间");
						redisDao.setExpire(StrUtil.getOrderRedis(channel),sqltime);
					}
				}	
				/*竞争锁机制end*/
				
				Integer count = (int) redisDao.getINDEVal(StrUtil.getOrderQueue(channel));
				logger.info(channel+":记录存入执行数量："+count);
				//Map<String,String> sendParam=null;
				
				
				SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
				
				/*消费开关限制start*/
				String train_fh_status="1";//默认开启状态
				if(null != MemcachedUtil.getInstance().getAttribute("train_fh_status")){
					train_fh_status = String.valueOf(MemcachedUtil.getInstance().getAttribute("train_fh_status"));
					logger.info(" train_fh_status:"+train_fh_status);
				}else{
					try {
						sysImpl.querySysVal("train_fh_status");
					} catch (Exception e) {
						e.printStackTrace();
					} 
					train_fh_status = sysImpl.getSysVal();
					MemcachedUtil.getInstance().setAttribute("train_fh_status",train_fh_status,30*1000);
				}
				if("0".equals(train_fh_status)){
					logger.info("train_fh 预订分发功能已关闭,不再消费mq订单");
					return	;
				}
				/*消费开关限制end*/
				
				/**读取处理订单数的配置**/
				String order_pool_num="";
				if(null != MemcachedUtil.getInstance().getAttribute("order_pool_num_"+channel)){
					order_pool_num = String.valueOf(MemcachedUtil.getInstance().getAttribute("order_pool_num_"+channel));
					logger.info("order_pool_num_:"+channel+":"+order_pool_num);
				}else{
					try {
						sysImpl.querySysVal("order_pool_num_"+channel);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					order_pool_num = sysImpl.getSysVal();
					MemcachedUtil.getInstance().setAttribute("order_pool_num_"+channel,order_pool_num,60*1000);
				}
				int pool_num=0;
				
				try {
					pool_num=Integer.parseInt(order_pool_num);
				} catch (Exception e1) {
					pool_num=gonum;
				}
				
				
				
			
				
				
				
				/**读取打码方式的配置**/
				String rand_code_type="";
				if(null!= MemcachedUtil.getInstance().getAttribute("rand_code_type")){
					rand_code_type = String.valueOf(MemcachedUtil.getInstance().getAttribute("rand_code_type"));
					
					logger.info("rand_code_type:"+rand_code_type);
				}else{
					try {
						sysImpl.querySysVal("rand_code_type");
					} catch (Exception e) {
						e.printStackTrace();
					} 
					rand_code_type = sysImpl.getSysVal();
					
					MemcachedUtil.getInstance().setAttribute("rand_code_type",rand_code_type,60*1000);
				}
				WorkIdNum.randCodeType=rand_code_type;
				
				
				logger.info("init order pool num "+channel+":"+pool_num+",rand_code_type:"+rand_code_type);
				if (count < pool_num) {
					logger.info("获取订单数量----------------------："+channel);
					try {
						for(int i =0 ;i<getnum ;i++){
							logger.info(channel+":开始获取消息");
							TextMessage textMessage =  (TextMessage) mqService.receive(StrUtil.getOrderQueue(channel));
							if(textMessage!=null){
								String param = textMessage.getText();
								logger.info("http start"+param);
								HttpPostUtil.sendAndRecive(Consts.ORDERURL, "param="+param+"&channel="+channel,200,1000);
							}else{
								break;
							}
							logger.info(channel+":消息获取结束");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else {
					try {
						Thread.sleep(waitsec);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					logger.info("处理中订单个数已满等待s时间："+waitsec);
				}
				
				}
		}, 3000, sec);
	}

}
