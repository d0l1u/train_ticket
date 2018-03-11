

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.ProxyEntity;


public class CtripTrainNumberThread extends Thread{	
	public static Logger logger = Logger.getLogger(CtripTrainNumberThread.class);
	public CtripTrainNumberThread(int num){
		super();
		this.threadNum = num;
	}
	private int threadNum;
	public ProxyEntity entity;
	public int number;
	
	@Override
	public void run(){
		logger.info("线程:"+threadNum+"开始查询携程的车次信息start~~~");
		CtripTrainNumber ctn = new CtripTrainNumber();
		ctn.entity = this.entity;
		ctn.getCtripTrainNumber(threadNum,number);
		logger.info("线程:"+threadNum+"结束查询携程的车次信息end!!!");
//		
//		logger.info("线程:"+threadNum+"开始查询携程的途经站信息start~~~");
//		new CtripMidway().updateMidwayOfLccc();
//		logger.info("线程:"+threadNum+"结束查询携程的途经站信息end!!!");
		
	}
}
