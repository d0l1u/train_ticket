import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.ProxyEntity;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Main {
	public static Logger logger = Logger.getLogger(Main.class);
	public static String STARTTIME;
	static {
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/config.properties");
		try {
			prop.load(in);
//			STARTTIME = prop.getProperty("start_time").trim();
			logger.info("任务执行开始时间为" + new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		Runnable runnable = new Runnable() {
//			public int thread;
//			public void run() {
//				
//				logger.info("开始查询携程的车次信息start~~~");
//				new CtripTrainNumber().updateTrainNumber(0,thread);
//				logger.info("结束查询携程的车次信息end!!!");
//				
//				logger.info("开始查询携程的途经站信息start~~~");
//				new CtripMidway().updateMidwayOfLccc();
//				logger.info("结束查询携程的途经站信息end!!!");
//				
//				
//				/*logger.info("开始更新车站名称start~~~");
//				new ZmManager().updateStation();
//				logger.info("结束更新车站名称end!!!");
//				
//				logger.info("开始更新车次信息start~~~"); 
//				new LcccManager().updateTrain();
//				logger.info("结束更新车次信息end!!!");
//				
//				logger.info("开始更新途经站信息start~~~");
//				new MidwayManager().updateMidwayOfLccc();
//				logger.info("结束更新途经站信息end!!!");*/
//			}
//		};
//		ScheduledExecutorService service = Executors
//				.newSingleThreadScheduledExecutor();
//		long oneDay = 24 * 60 * 60 * 1000;
//		long initDelay = getTimeMillis(STARTTIME) - System.currentTimeMillis();
//		initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
//
//		service.schedule AtFixedRate(runnable, initDelay, oneDay,
//				TimeUnit.MILLISECONDS);
		ArrayList list = new ArrayList();
//		list.add(new ProxyEntity("115.29.231.162",808,"wxg", "wxg")); 
//		list.add(new ProxyEntity("121.43.111.96",808,"wxg", "wxg")); 
//		list.add(new ProxyEntity("139.196.179.187",808,"wxg", "wxg")); 
//		list.add(new ProxyEntity("139.196.179.118",808,"wxg", "wxg")); 
//		list.add(new ProxyEntity("120.55.187.23",808,"wxg", "wxg")); 
//		list.add(new ProxyEntity("120.55.185.63",808,"wxg", "wxg")); 
		list.add(new ProxyEntity("121.40.108.163",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("121.41.86.165",808,"hcpkuyou19e", "hcpkuyou19e")); 
		list.add(new ProxyEntity("121.40.170.167",808,"hcpkuyou19e", "hcpkuyou19e")); 
		list.add(new ProxyEntity("123.57.217.202",808,"hcpkuyou19e", "hcpkuyou19e")); 
		list.add(new ProxyEntity("123.57.162.121 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("123.57.79.151 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		
		list.add(new ProxyEntity("103.37.158.149 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("43.241.225.179 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("103.37.156.194 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("43.241.226.60 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("103.37.158.29 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("43.241.224.41 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("43.241.225.200 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("103.37.156.22 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("43.241.227.42 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		list.add(new ProxyEntity("103.37.157.111 ",808,"hcpkuyou19e", "hcpkuyou19e"));
		
		
		
		
		for (int i = 1; i <= list.size(); i++) {
			logger.info("开启线程"+i);
			CtripTrainNumberThread ctnThread = new CtripTrainNumberThread(i);
			if(list.size()>0){
				ctnThread.number = list.size();
				ctnThread.entity = (ProxyEntity)list.get(i-1);
			}				
			ctnThread.start();
		}

	}

	private static long getTimeMillis(String time) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " "
					+ time);
			return curDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

}