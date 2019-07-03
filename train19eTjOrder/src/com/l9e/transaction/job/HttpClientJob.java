package com.l9e.transaction.job;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.util.MD5;
import com.speed.esalDemo.generation.OrderServiceImplServiceStub;
import com.speed.esalDemo.generation.OrderServiceImplServiceStub.CreateOrderByPnr;
import com.speed.esalDemo.generation.OrderServiceImplServiceStub.CreateOrderByPnrE;
import com.speed.esalDemo.generation.OrderServiceImplServiceStub.CreateOrderByPnrResponseE;
import com.speed.esalDemo.generation.OrderServiceImplServiceStub.OrderByPnrRequest;

/**
 * 
 * @author kongxm
 * 
 */
@Component("HttpClientJob")
@Deprecated
public class HttpClientJob {
	
	private static final Logger logger = Logger.getLogger(HttpClientJob.class);
	
	private Timer timer;

	@PostConstruct
	public void timerRegist() {
		
		if (timer != null) {
			timer.cancel();
		}
		
		timer = new Timer();
		final int time=2 * 1000;
		
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					logger.info("timer每隔【"+time/1000+"s】执行一次");
					doPost();
				} catch (Exception e) {
					logger.error("timer异常" + e);
				}
			}
		}, 5 * 1000, time);
	}
	
	public void doPost(){
		try {
			String url = "http://192.168.63.158:18099/services/OrderService";
			OrderServiceImplServiceStub stub = new OrderServiceImplServiceStub(url);
			OrderByPnrRequest request = new OrderByPnrRequest();
			request.setAppcode(19);
			request.setDiscount("3.2");                                 //政策查询接口返回的Rate对象中的backrate字段
			request.setIs_change_pnr("1");
			request.setNotified_url("http://xxx.xxx.xx");            //接收票号回传的地址
			request.setOut_orderid("abc");                           //中航联系统订单号
			request.setPnr("JQDJ3F");
			String pnr_content = "RTJQDJ3F   "+ 
				"1.颜振辉  JQDJ3F "+
				"2.  CA1527 E   SA11JAN  TSNSHA HK1   1740 1940          E ---- "+  
				"3.TL/1257/06JAN/PEK262            "+ 
				"4.SSR FOID CA HK1 NI00430419/P1 "+  
				"5.OSI CA CTCT "+
				"6.RMK CA/MW6NVC "+ 
				"7.PEK262 "+
				">PAT:A           "+
				"01 E FARE:CNY340.00 TAX:CNY50.00 YQ:CNY120.00  TOTAL:510.00 "+
				"SFC:01";
			request.setPnr_content(pnr_content);
			request.setPnr_type("2");
			request.setUsername("13520910803");
			request.setRate_id("66095014");                                 //政策查询接口返回的Rate对象中的rate_id字段
			StringBuffer strBuff = new StringBuffer();
			strBuff.append("OrderByPnrRequest");
			strBuff.append(request.getPnr());
			strBuff.append(request.getPnr_type());
			strBuff.append(request.getUsername());
			strBuff.append(request.getRate_id());
			strBuff.append(request.getIs_change_pnr());
			strBuff.append(request.getDiscount());
			strBuff.append(request.getAppcode());
			strBuff.append("[3*[9TjUj6k#T[Z&jSkU&0[##UY[*Zl[");
			String sign = MD5.getMd5(strBuff.toString());
			request.setSign(sign);
			CreateOrderByPnr createOrderByPnr = new CreateOrderByPnr();
			createOrderByPnr.setArg0(request);
			CreateOrderByPnrE createOrderByPnrE = new CreateOrderByPnrE();
			createOrderByPnrE.setCreateOrderByPnr(createOrderByPnr);
			CreateOrderByPnrResponseE  createOrderByPnrResponseE  = stub.createOrderByPnr(createOrderByPnrE);
			System.out.println(createOrderByPnrResponseE.getCreateOrderByPnrResponse().get_return().getCode());
			System.out.println(createOrderByPnrResponseE.getCreateOrderByPnrResponse().get_return().getDesc());
			System.out.println(createOrderByPnrResponseE.getCreateOrderByPnrResponse().get_return().getOrder_id());
			System.out.println(createOrderByPnrResponseE.getCreateOrderByPnrResponse().get_return().getTotal_price());
		} catch (AxisFault e) {
			e.printStackTrace();
		}catch (RemoteException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		HttpClientJob job = new HttpClientJob();
		job.timerRegist();
	}
	
}
