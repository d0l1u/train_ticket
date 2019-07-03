 package com.l9e.transaction.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.csvreader.CsvReader;
import com.l9e.transaction.service.CheckPriceService;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.FtpUtil;
import com.l9e.util.RarUtils;


/**
 * 自动上传csv数据
 * @author kongxm
 *
 */
@Component("UploadCsvJob")
public class UploadCsvJob {
	private static final Logger logger = Logger.getLogger(UploadCsvJob.class);
	@Resource
	private CheckPriceService checkPriceService;
	private String ipAddr;
	@Value("#{propertiesReader[ipAddr]}")
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	private String userName;
	@Value("#{propertiesReader[userName]}")
	public void setUserName(String userName) {
		this.userName = userName;
	}
	private String pwd;
	@Value("#{propertiesReader[pwd]}")
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	private String port;
	@Value("#{propertiesReader[port]}")
	public void setPort(String port) {
		this.port = port;
	}
	//获取下载好的支付宝文件
	public void uploadFileToLocal(){
		String date = DateUtil.dateToString(DateUtil.dateAddDays(new Date(),-1), "yyyy-MM-dd");
        try {
        	logger.info("连接ftp,地址"+ipAddr+"用户名"+userName+"密码"+pwd+"端口"+port);
      	  	Integer portInt = Integer.parseInt(port);
        	Boolean connectResult = FtpUtil.connectFtp(ipAddr,userName,pwd,portInt);
      	    if(connectResult){
                FtpUtil.startDown( "/data/train/upRar/"+date+"/", date);//下载ftp文件测试
      	    }else{
      	    	
      	    }
        } catch (Exception e) {
			e.printStackTrace();
		}
     
	}
	
	//解析文件。保存到数据库
	public void uploadCsv() throws IOException{
		UploadCsvJob f = new UploadCsvJob();
		String date = DateUtil.dateToString(DateUtil.dateAddDays(new Date(),-1), "yyyy-MM-dd");
		String prePath="/data/train/upRar/"+date;
		String backupPath="/data/train/backupRar/"+date;
		File targetFile = new File(backupPath);
		if(!targetFile.exists()){
        	targetFile.mkdirs();
        }
		File path = new File(prePath);
		if(path.exists() && path.isDirectory()){
			//获取文件夹里文件
			List<File> ll = f.getFiles(new File(prePath));
			if(ll!=null && ll.size()>0){
				for (File ff : ll) {
					List<String> fileRarName = new ArrayList<String>();
					String fileName=ff.getName();//文件名
					String fileSuffix = fileName.substring(fileName.lastIndexOf("."),ff.getName().length());//后缀名
					String fileNameWithoutSuffix =fileName .substring(0, fileName.lastIndexOf("."));//不带后缀名的文件名
					if(".rar".equalsIgnoreCase(fileSuffix)  || ".zip".equalsIgnoreCase(fileSuffix)){
						String newPath = prePath + "/" + fileName;
						String backupFilePath=backupPath+"/"+fileName;
						//进行解压缩
						try {
							copyFile(newPath, backupFilePath);//压缩包备份
							RarUtils.deCompress(newPath, prePath, fileRarName);//对压缩包解压缩
							delFile(newPath);//删除临时文件夹下的压缩包
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("解压文件异常");
						}
						for(int j = 0; j < fileRarName.size(); j++) {
							String fileInRarName = fileRarName.get(j);
							String newFilePath=prePath+"/"+fileInRarName;
							File file=new File(newFilePath); 
							if (!file.exists()) {
					            throw new FileNotFoundException("文件不存在：" + prePath);
					        }
					        try {
					            if(newFilePath.endsWith("csv")){
					            	ArrayList<String[]> csvList = new ArrayList<String[]>();  
					            	LinkedList<Map<String, Object>> paramList = new LinkedList<Map<String, Object>>();  
					            	try {  
					      			  	CsvReader reader = new CsvReader(newFilePath, ',', Charset.forName("GBK"));  
					      			  	//逐行读入除表头的数据         
					      			  	while(reader.readRecord()){ 
					      		           csvList.add(reader.getValues());  
					      		        }                
					      			  	reader.close();  
					      		        // 读取数据行
					      	            String check_seq=CreateIDUtil.createID("CH");
					      	            for(int row=5; row<csvList.size()-4; row++){ 
					      		           String alipay_type = csvList.get(row)[10];//业务类型
						  		           if("在线支付".equals(alipay_type)){
						  		        	   alipay_type = "11";
						  		           }else if("交易退款".equals(alipay_type)){
						  		        		alipay_type = "22";
						  		           }else{
						  		        		alipay_type = "";
						  		           }
						  		          
						  		           String bank_pay_seq = csvList.get(row)[2];//商户订单号
					  		               bank_pay_seq=bank_pay_seq.replace("\t", "").trim();
							  		       if(!StringUtils.isEmpty(alipay_type)){
							  		    	   if((!bank_pay_seq.equals(""))&&bank_pay_seq!=null){
							  		    		   String pay_time=csvList.get(row)[4];//时间
							  		               String pay_money=csvList.get(row)[7];
							  		               double abs_money = Math.abs(Double.parseDouble(pay_money));//支出金额（出票）绝对值
							  		               String refund_money=csvList.get(row)[6];//收入金额（退款）
							  		               String opt_person="韩翠梅";//操作人
							  		               String alipay_id="酷游"+""+fileNameWithoutSuffix;//支付宝账号
						  		               
							  		               Map<String,Object> paramMap = new HashMap<String,Object>();
						  		            	   paramMap.put("bank_pay_seq", bank_pay_seq);//支付宝流水号
						  		            	   paramMap.put("pay_time", pay_time);//时间
						  		            	   paramMap.put("refund_money", refund_money);//收入金额（退款）
						  		            	   paramMap.put("pay_money", abs_money);//支出金额（出票）
						  		            	   paramMap.put("alipay_type", alipay_type);//类型
						  		            	   paramMap.put("alipay_id", alipay_id);//支付宝账号
						  		            	   paramMap.put("opt_person", opt_person);//操作人
						  		            	   paramMap.put("check_seq", check_seq);//单次上传标识
						  		            	   paramMap.put("create_time", "now()");//上传时间		
						  		            	   
						  		            	   paramList.add(paramMap);
					  		            		
							  		               if(row==csvList.size()-5){
								            			Map<String,Object> paMap = new HashMap<String,Object>();
								            			String account_balance=csvList.get(row)[8];//账户余额
								            			alipay_id="huochepiaokuyou"+""+fileNameWithoutSuffix+""+"@19e.com.cn";
								            			
								            			paMap.put("pay_time", pay_time);
								            			paMap.put("alipay_id", alipay_id);
								            			paMap.put("account_balance", account_balance);
								            			paMap.put("create_time", "now()");
								            			try {
									            			checkPriceService.addAlipayBalance(paMap);
									            		} catch (Exception e) {
									            			logger.error("插入addAlipayBalance失败");
									            		}
								            		 }
						  		                 }
						      		         } 
					      	            }
		  		            		 try {
				            			checkPriceService.addAlipayInfoList(paramList);
				            		 } catch (Exception e) {
				            			logger.error("插入addAlipayInfo失败bank_pay_seq");
				            		 }
					      		  }catch (Exception e) {  
					      			  e.printStackTrace();  
					      		  }  
						      	}
					            logger.info("addAlipayInfo批量导入完成");
					        } catch (Exception e) {
					        	logger.error("addAlipayInfo批量导入异常");
					            e.printStackTrace();
					        }
					        if(file.exists()){
					       		if(file.getParentFile().isDirectory()){
					          		file.delete();
					          		logger.info("addAlipayInfo删除文件："+file);
					          		file.getParentFile().delete();
					          		logger.info("addAlipayInfo删除文件目录："+file.getParentFile());
					          	}
					        }
						}
					}else if(".csv".equalsIgnoreCase(fileSuffix)){
						String newPath = prePath + "/" + fileName;
						String backupFilePath=backupPath+"/"+fileName;
						copyFile(newPath, backupFilePath);//压缩包备份
						File file = new File(newPath);
		            	ArrayList<String[]> csvList = new ArrayList<String[]>();  
		            	LinkedList<Map<String, Object>> paramList = new LinkedList<Map<String, Object>>();  
		            	if(file.exists()){
		            		try {
			            		CsvReader reader = new CsvReader(newPath, ',', Charset.forName("GBK"));  
			      			  	//逐行读入除表头的数据         
			      			  	while(reader.readRecord()){ 
			      		           csvList.add(reader.getValues());  
			      		        }                
			      			  	reader.close();  
			      		        // 读取数据行
			      	            String check_seq=CreateIDUtil.createID("CH");
			      	            for(int row=5; row<csvList.size()-4; row++){ 
			      		           String alipay_type = csvList.get(row)[9];//业务类型
				  		           if("余额购票".equals(alipay_type)){
				  		        	   alipay_type = "11";//出票
				  		           }else if("余额退款".equals(alipay_type)){
				  		        		alipay_type = "22";//退票
				  		           }else{
				  		        		alipay_type = "";//忽略其他类型
				  		           }
				  		          
				  		           String bank_pay_seq = csvList.get(row)[2];//商户订单号
			  		               bank_pay_seq=bank_pay_seq.replace("\t", "").trim();
					  		       if(!StringUtils.isEmpty(alipay_type)&& StringUtils.isNotEmpty(bank_pay_seq)){
				  		    		   String pay_time=csvList.get(row)[3];//时间
				  		               String pay_money=StringUtils.isNotEmpty(csvList.get(row)[6])?csvList.get(row)[6]:"0";//支出金额
				  		               double abs_money = Math.abs(Double.parseDouble(pay_money));//支出金额（出票）绝对值
				  		               String refund_money=StringUtils.isNotEmpty(csvList.get(row)[5])?csvList.get(row)[5]:"0";//收入金额（退款）
				  		               String opt_person="韩翠梅";//操作人
				  		               String alipay_id="19e"+""+fileNameWithoutSuffix;//支付宝账号
			  		               
				  		               Map<String,Object> paramMap = new HashMap<String,Object>();
			  		            	   paramMap.put("bank_pay_seq", bank_pay_seq);//支付宝流水号
			  		            	   paramMap.put("pay_time", pay_time);//时间
			  		            	   paramMap.put("refund_money", refund_money);//收入金额（退款）
			  		            	   paramMap.put("pay_money", abs_money);//支出金额（出票）
			  		            	   paramMap.put("alipay_type", alipay_type);//类型
			  		            	   paramMap.put("alipay_id", alipay_id);//支付宝账号
			  		            	   paramMap.put("opt_person", opt_person);//操作人
			  		            	   paramMap.put("check_seq", check_seq);//单次上传标识
			  		            	   paramMap.put("create_time", "now()");//上传时间		
			  		            	   
			  		            	   paramList.add(paramMap);
				            		
				  		               if(row==csvList.size()-5){
					            			Map<String,Object> paMap = new HashMap<String,Object>();
					            			String account_balance=csvList.get(row)[7];//账户余额
					            			alipay_id="huochepiao19e"+""+fileNameWithoutSuffix+""+"@163.com";
					            			paMap.put("pay_time", pay_time);
					            			paMap.put("alipay_id", alipay_id);
					            			paMap.put("account_balance", account_balance);
					            			paMap.put("create_time", "now()");
					            			try {
						            			checkPriceService.addAlipayBalance(paMap);
						            		} catch (Exception e) {
						            			logger.error("插入addAlipayBalance失败,alipay_id"+alipay_id);
						            		}
					            		 }
				      		          } 
			      	              }
				      	       try {
				      	    	   checkPriceService.addAlipayInfoList(paramList);
				      	       }catch (Exception e) {
				      	    	   logger.error("插入addAlipayInfo失败bank_pay_seq");
				      	       }
		            		}catch (Exception e) {  
							  logger.info("解析csv文件发生异常,文件路径"+newPath);
							  e.printStackTrace();
		            		}  
		            	}else{
		            		 throw new FileNotFoundException("文件不存在：" + prePath);
		            	}
		            	if(file.exists()){
				       		if(file.getParentFile().isDirectory()){
				          		file.delete();
				          		logger.info("addAlipayInfo删除文件："+file);
				          		file.getParentFile().delete();
				          		logger.info("addAlipayInfo删除文件目录："+file.getParentFile());
				          	}
				        }
					}
				}
			}else{
				logger.info("没有未处理的支付宝文件");
			}
		}else{
			logger.info("文件不存在或文件路径不是目录");
		}
	}
	//获取文件
	 public List<File> getFiles(File fileDir) {
	        List<File> lfile = new ArrayList<File>();
	        File[] fs = fileDir.listFiles();
	        for (File f : fs) {
	            if (f.isFile()) {
	                    lfile.add(f);
	            } else {
	                List<File> ftemps = getFiles(f);
	                lfile.addAll(ftemps);
	            }
	        }
	        return lfile;
	    }
	 //拷贝文件
	 public void copyFile(String oldPath, String newPath) { 
	       try { 
	           int bytesum = 0; 
	           int byteread = 0; 
	           File oldfile = new File(oldPath); 
	           if (oldfile.exists()) { //文件存在时 
		           InputStream inStream = new FileInputStream(oldPath); //读入原文件 
		           FileOutputStream fs = new FileOutputStream(newPath); 
		           byte[] buffer = new byte[8192]; 
		           int length; 
		           while ( (byteread = inStream.read(buffer)) != -1) { 
		               bytesum += byteread; //字节数 文件大小 
		               fs.write(buffer, 0, byteread); 
		           } 
		           inStream.close(); 
		       } 
	   } 
	   catch (Exception e) { 
		   logger.info("复制单个文件操作出错");
	           e.printStackTrace();
	       }
	   }
	 //删除文件
	 public void delFile(String filePathAndName) { 
	       try { 
	           String filePath = filePathAndName; 
	           filePath = filePath.toString(); 
	           java.io.File myDelFile = new java.io.File(filePath); 
	           myDelFile.delete();
	       } 
	       catch (Exception e) { 
	    	   logger.info("删除文件操作出错");
	           e.printStackTrace();
	
	       }
	
	   }
	 
}
