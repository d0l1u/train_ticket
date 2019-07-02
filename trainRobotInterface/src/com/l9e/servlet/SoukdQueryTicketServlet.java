package com.l9e.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.l9e.common.BaseServlet;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;

/**
 * 查询余票主控路口
 * 
 * */
public class SoukdQueryTicketServlet extends HttpServlet{
	private static final long serialVersionUID = 3264299111876887524L;
	
	private static Logger logger=Logger.getLogger(SoukdQueryTicketServlet.class);
	//private int selectNum; 单类型机器人分配数
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}
	
	@Override
	public void init() throws ServletException {
	}

	/**查询余票入口*/
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		/**分发参数*/
		String channel=req.getParameter("channel");
		/**业务参数*/
		String from_station =req.getParameter("from_station");
		String arrive_station =req.getParameter("arrive_station");
		String travel_time =req.getParameter("travel_time");
		//查询余票
		String check_spare_num = req.getParameter("check_spare_num");
		logger.info("channel["+channel+"]Thread:"+Thread.currentThread().getName()+"start queryTicket["+from_station+"/"+arrive_station+":"+travel_time+"]");
		Map<String,String> param=new HashMap<String, String>();
		param.put("from_station", from_station);
		param.put("arrive_station", arrive_station);
		param.put("travel_time", travel_time);
		param.put("method", "DGTrain");
		String key = getFileName(param);
		param.put("channel", channel);
		Object memcache = MemcachedUtil.getInstance().getAttribute(key);
		if("check_spare_num".equals(check_spare_num)){
			memcache = null;
		}
		if(null == memcache){
			String xmlStr = queryLeftTicketSoukd(param);
			write2Response(resp,xmlStr);
////			if(xmlStr == null){
////				write2Response(resp,"error");
////			}
////			try{
//////				if(xmlStr.contains("\"return_code\":\"000\"")){
////					/**线程分布任务可做*/
////					String prePath = req.getSession().getServletContext().getRealPath("/files");
////					long startFile=System.currentTimeMillis();
////					String fileDir = prePath + "/" + travel_time;
////					String fileName = Md5Encrypt.md5(key, "gbk") + ".txt";
////					String filePath = fileDir + "/" + fileName;
////					logger.info("filePath="+filePath);
////					//创建文件保存接口返回数据
////					FileUtil.removeFile(filePath);
////					boolean isSucess = FileUtil.createFile(fileDir, fileName, xmlStr, "UTF-8");
////					//文件生成成功则把文件名写入Memcache
////					if(isSucess){
////						MemcachedUtil.getInstance().setAttribute(key, filePath, 5*60*1000);
////						logger.info("文件缓存耗时ms"+(System.currentTimeMillis()-startFile));
////					}
//////				}
////				//返回查询结果
////				write2Response(resp,xmlStr);
//			}catch(Exception e){
//				logger.error("保存查询信息为缓存文件异常！",e);
//				write2Response(resp,"error");
//			}
		}else{
			logger.info("进入文件缓存");
			long start = System.currentTimeMillis();
			String filePath = (String) memcache;
			String fileContent = FileUtil.readFile(filePath, "UTF-8");
			logger.info((System.currentTimeMillis() - start)+ "ms"+"读文件"+filePath+"耗时");
			write2Response(resp,fileContent);
		}
		
		
	}
	
	//第三方接口
	public String queryLeftTicketSoukd(Map<String,String> param){
		long start = System.currentTimeMillis();
		String url = BaseServlet.getSoukdUrl(param, BaseServlet.SOUKD_URL);
//		//临时测试代理
//	    System.getProperties().setProperty("http.proxyHost", "192.168.63.238");
//	    System.getProperties().setProperty("http.proxyPort", "6789" );
		String xmlStr = "";
		try{
			xmlStr = HttpUtil.sendByGet(url, BaseServlet.SOUKD_CHARSET, "30000", "30000");//调用接口
			logger.info("sokud接口返回数据："+xmlStr);
			logger.info("<火车票查询>调用soukd接口成功查询"
					+param.get("from_station")+"/"+param.get("arrive_station")
					+"("+param.get("travel_time")+")的列车信息，耗时" + (System.currentTimeMillis() - start)+ "ms");
		}catch(Exception e){//没有查询到数据
			logger.error("解析SOUKD接口返回数据异常", e);
			return null;
		}
		return xmlStr;
	}
	/**
	 * 获取文件名(eg:all_北京_上海_2013-5-22)
	 * @return
	 */
	protected String getFileName(Map<String, String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("center_").append(map.get("from_station"))
		  .append("_")
		  .append(map.get("arrive_station"))
		  .append("_")
		  .append(map.get("travel_time"))
		  .append(map.get("method"));
		return sb.toString();
	}
	
	/**
	 * 值写入response
	 * @param response
	 * @param StatusStr
	 */
	public void write2Response(HttpServletResponse response, String StatusStr){
		try {
			response.getWriter().write(StatusStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
