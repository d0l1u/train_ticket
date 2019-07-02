package com.l9e.transaction.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.transaction.service.impl.TrainServiceImpl;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.PostRequestUtil;
import com.l9e.util.UrlFormatUtil;
import com.l9e.util.WorkIdNum;

public class ReadyToPayServlet extends HttpServlet{
	private static final Logger logger = LoggerFactory.getLogger(ReadyToPayServlet.class);
	
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("get--------------");
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		long servlet_start=System.currentTimeMillis();
		String channel = req.getParameter("channel");
		String order_id = req.getParameter("order_id");
		String accUsername = req.getParameter("accUsername");
		String accPassword = req.getParameter("accPassword");
		resp.getWriter().print("ok");
		resp.getWriter().flush();
		resp.getWriter().close();
		if(order_id==null||"".equals(order_id.trim())){
			logger.info("订单号为null");
			return;
		}
		
		logger.info("Start to login , for the pay"+order_id+"|"+accUsername+"|"+accPassword);
		if(accUsername!=null&&accPassword!=null&&order_id!=null){
			
			TrainServiceImpl dao = new TrainServiceImpl();
			try {
				dao.initPayAccountAndWorkerBy(order_id);
				Worker w=dao.getWorker();
				
				
				
				long start=System.currentTimeMillis();
				/*String acc_username=request.getParameter("acc_username");
				String acc_password=request.getParameter("acc_password");
				String worker_ext=request.getParameter("worker_ext");*/
				
				//String acc_username=request.getParameter("acc_username");
				//String acc_password=request.getParameter("acc_password");
				//String worker_ext=request.getParameter("worker_ext");
				//logger.info("start login servlet"+acc_username+"_"+acc_password+"_"+worker_ext);
				Map<String, String> maps = new HashMap<String,String>();
				maps.put("ScriptPath", "keepCookie.lua");//执行的脚本路径
				maps.put("SessionID", String.valueOf(System.currentTimeMillis()));//任务完成后，机器人回应该请求时携带
				maps.put("Timeout", "240000");//供货商价格
				maps.put("Sync", "0");//0异步请求/1同步请求
				maps.put("ParamCount","1");
				maps.put("Param1", accUsername+"|"+accPassword+"|"+WorkIdNum.randCodeType);
				
				String param_str="";
				try {
					param_str = UrlFormatUtil.createUrl("", maps, 1);
					logger.info("start post request params:"+param_str+" orderUrl:"+w.getWorkerExt());
					String reqResult=PostRequestUtil.getPostRes("UTF-8", w.getWorkerExt(), param_str,"500","1");
					logger.info("end post request reqResult:"+reqResult);
				} catch (Exception e) {
					logger.info("post request Exception:"+e);
					e.printStackTrace();
				}finally{
					logger.info("pay servlet 登入耗时:"+(System.currentTimeMillis()-start)+"ms");
				}
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
		}
	}
}
