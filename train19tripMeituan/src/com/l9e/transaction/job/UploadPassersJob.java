package com.l9e.transaction.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.l9e.transaction.service.OrderService;
import com.l9e.util.FileUtil;
/**
 * 上传常用联系人的txt文件到美团的服务器
 */
@Component("uploadPassersJob")
public class UploadPassersJob {
	private static final Logger logger = Logger.getLogger(UploadPassersJob.class);
	
//	private static String path = "/data/train19tripMeituan/meituan.csv";//245测试
	private static String path = "/data/train_meituan/meituan.csv";//线上
	
	@Resource
	private OrderService orderService;

	//分页查询出数据，放入一个总的csv文件，将cp_pass_whitelist表的数据一下传给meituan
	public void uploadTxt(){
		if(queryPassersData()){
			postFileUpload();
		}
	}
	
	// POST文件上传至美团服务器
	public static boolean postFileUpload() {
		String logPre = "[全量白名单上传csv]";
		try {
			File f = new File(path);
//			String url = "http://test.i.meituan.com/uts/train/agentPassengerFileUpload/106/19E";//测试地址
			String url = "http://i.meituan.com/uts/train/agentPassengerFileUpload/106/19E";//线上地址
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			FileBody fb = new FileBody(f);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file1", fb);//file1为请求后台的File upload;属性
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				logger.info(logPre+"upload csv to meituan success!");
				return true;
			}else{
				logger.info(logPre+"upload csv to meituan fail! statusCode=" + statusCode);
				return false;
			}
		} catch (IOException e) {
			logger.info(logPre+"upload csv to meituan exception! e=" + e);
			e.printStackTrace();
			return false;
		}
	}
	
	
	//跑数据--常用乘车人信息，生成csv文件
	private boolean queryPassersData() {
		String logPre = "[全量白名单生成csv]";
		logger.info(logPre+"query cp_pass_whitelist data load start~~~");
		FileUtil.removeFile(path);//删除以前的文件
		//String querydate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		OutputStreamWriter pw = null;//定义一个流
		int count = orderService.queryPassersCount();//查询cp_pass_whitelist的总数
		if(count>0){
			try { 
				//确认流的输出文件和编码格式，此过程创建了“test.txt”实例
				pw = new OutputStreamWriter(new FileOutputStream(path, true),"UTF-8");
				 
				int size = 500000;//每次查询的数量
				int pageCount = count%size==0 ? count/size : count/size+1;
				for(int m=0; m<pageCount; m++){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("index", size*m);
					map.put("size", size);
					
					//分页查询，防止溢出
					//证件类型（1  身份证）身份证号码  姓名  添加时间（如没有，可填0）
					List<Map<String, String>> passList = orderService.queryPassersList(map);
					for(Map<String, String> aa : passList){
						String content = aa.get("passer");
						pw.write(content+"\r\n");          
					}
				}
				pw.flush();
				pw.close();
			}catch (Exception e) {
				logger.error(logPre+"[create meituan csv error]"+e); 
			}
		}else{
			return false;
		}
			
		logger.info(logPre+"query cp_pass_whitelist data load end~~~");
		return true;
	}
	
	
	//跑数据--常用乘车人信息，生成txt文件
	private void queryPassersDataCSV() {
		String logPre = "[全量白名单]";
		logger.info(logPre+"job start~~~");
		
		int count = orderService.queryPassersCount();//查询cp_pass_whitelist的总数
		if(count>0){
			try { 
				int size = 500000;//每次查询的数量
				int pageCount = count%size==0 ? count/size : count/size+1;
				for(int m=0; m<pageCount; m++){
					FileUtil.removeFile(path);//删除以前的文件
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("index", size*m);
					map.put("size", size);
					
					//分页查询，防止溢出
					//证件类型（1  身份证）身份证号码  姓名  添加时间（如没有，可填0）
					List<Map<String, String>> passList = orderService.queryPassersList(map);
					//生成csv文件
					logger.info(logPre+"[create csv Start]index:"+size*m); 
					this.createTxt(path, passList);
					logger.info(logPre+"[create csv End]index:"+size*m); 
					//上传文件
					logger.info(logPre+"[upload csv to meituan Start]index:"+size*m); 
					postFileUpload();
					logger.info(logPre+"[upload csv to meituan End]index:"+size*m); 
				}
			}catch (Exception e) {
				logger.error(logPre+"[create meituan csv error]"+e); 
			}
		}
			
		logger.info(logPre+"job end~~~");
	}
	
	
	//写入txt格式文件
	private void createTxt(String path, List<Map<String, String>> list){
		logger.info("create meituan txt start~~~");
		try { 
			 OutputStreamWriter pw = null;//定义一个流
			 //确认流的输出文件和编码格式，此过程创建了“test.txt”实例
			 pw = new OutputStreamWriter(new FileOutputStream(path, true),"UTF-8");
			 for(Map<String, String> aa:list){
				String content = aa.get("passer");
				pw.write(content+"\r\n");          
			 }
			 pw.close();
		}catch (Exception e) {
			logger.error("[create meituan txt error]"+e); 
		}
		logger.info("create meituan txt end~~~");
	}
	
}
