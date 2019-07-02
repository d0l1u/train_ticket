package com.nineteen.job;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.nineteen.util.Utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import com.nineteen.service.Alipay_balanceService;
import com.nineteen.service.RedisService;
import com.nineteen.util.CaptchaOCRUtil;
import com.nineteen.util.HttpUtil;
import com.nineteen.util.ImageRequest;
import com.nineteen.util.UrlFormatUtil;

import com.nineteen.vo.AlipayUser;

public class Alipay_balanceJob extends QuartzJobBase{
	//日志־
	private static final Logger logger = Logger
	.getLogger(Alipay_balanceJob.class);
	//注入
	@Autowired  
    public Alipay_balanceService alipayBalanceService;
	@Autowired
	public RedisService redisService;
	//获取配置文件参数
	@Value("#{config}")
	private Properties config;
	@Override
	public void execute2(JobExecutionContext jobExecutionContext) {
		// TODO Auto-generated method stub
		//开始时间
		Long start = System.currentTimeMillis();
		List<AlipayUser> alipayUser = alipayBalanceService.getInfo();
		logger.info(" 检验是否有账户在设置的时间内进行操作 : "+alipayUser.size());
		//浏览器路径
		String path = (String)config.get("path");
		//浏览器驱动
		String path_driver = config.get("path_driver").toString();
		//支付宝登录地址
		String alipay_login = config.get("alipay_login").toString();
		for (int i = 0; i < alipayUser.size(); i++) {
			
				
			
			AlipayUser user = alipayUser.get(i); 
			//支付宝账号
			String alipay_account = user.getCard_no();
			// 设置 chrome 的路径  
	        System.setProperty("webdriver.chrome.driver",path);  
	        // 创建一个 ChromeDriver 的接口，用于连接 Chrome  
	        @SuppressWarnings("deprecation")  
	        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(  
	                        new File(path_driver)).usingAnyFreePort().build();  
	        try {
				service.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        // 创建一个 Chrome 的浏览器实例  
	        WebDriver driver = new RemoteWebDriver(service.getUrl(),  
	                DesiredCapabilities.chrome());  
//	     	 获取登录页面
	        driver.get(alipay_login);
//				获取用户名输入框
	        driver.findElement(By.id("J-input-user")).clear();
	        driver.findElement(By.id("J-input-user")).sendKeys("huochepiaokuyou05@19e.com.cn");//alipay_account
//	        driver.findElement(By.id("J-input-user")).sendKeys("huochepiaokuyou13@19e.com.cn");
//		        休息500ms，否则，速度太快，会将密码内容填充到用户名输入框中
	        try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		        获取密码输入框
	        driver.findElement(By.id("password_rsainput")).clear();
	        driver.findElement(By.id("password_rsainput")).sendKeys("hcm234789");//hcm234789
//		        休息2秒等待用户输入验证码
	        try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        driver.findElement(By.id("J-login-btn")).click();
//	        try {
	        //当出现验证码时，则在处理
	        try {
	        	 Thread.sleep(2000);
    		    //一旦当前页面URL不是登录页面URL，就说明浏览器已经进行了跳转
                if (!driver.getCurrentUrl().equals(alipay_login)) {
//                	driver.findElement(By.className("personal-login")).click();
   	        	 	logger.info(" 支付宝账号登录: "+alipay_account);
                }else{
                	logger.info(" 账户登录异常,可能需要验证码输入:"+alipay_account);
                	//获取用户名输入框
    		        driver.findElement(By.id("J-input-user")).clear();
    		        driver.findElement(By.id("J-input-user")).sendKeys(alipay_account);
//    		        休息500ms，否则，速度太快，会将密码内容填充到用户名输入框中
    		        try {
    					Thread.sleep(500);
    				} catch (InterruptedException e2) {
    					// TODO Auto-generated catch block
    					e2.printStackTrace();
    				}
//    		        获取密码输入框
    		        driver.findElement(By.id("password_rsainput")).clear();
    		        driver.findElement(By.id("password_rsainput")).sendKeys("hcm234789");
//    		        String randCode = driver.findElement(By.xpath("//*[@id='J-checkcode']")).getAttribute("class");
//    				System.out.println(" 是否出现验证码  "+randCode);
    		        String img_path = config.get("img_path").toString();
    		      //验证码地址
    	        	String str =driver.findElement(By.cssSelector("img.ui-checkcode-img")).getAttribute("src");
    	        	
    	        	
    	        	//请求地址获取图片
    	        	try {
    					String img_url = ImageRequest.Image(str, img_path);
    					logger.info(" 存储验证码位置:"+img_url);
    				//调用打码接口 返回
    					String checkcode = CaptchaOCRUtil.ocrUtil(img_url);
    					logger.info(" 打码接口返回验证码:"+checkcode);
    				// 获取验证码输入框
        		        driver.findElement(By.id("J-input-checkcode")).clear();
        		        driver.findElement(By.id("J-input-checkcode")).sendKeys(checkcode);
        		        Thread.sleep(800);
    				} catch (Exception e2) {
    					// TODO Auto-generated catch block
    					e2.printStackTrace();
    				}
    				 driver.findElement(By.id("J-login-btn")).click();
    				 try {
 						Thread.sleep(2000);
 					} catch (InterruptedException e1) {
 						// TODO Auto-generated catch block
 						e1.printStackTrace();
 					}
                }
			} catch (Exception e) {
				// TODO: handle exception
				logger.info("可能需要验证码");
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Map<String,String> map = new HashMap<String, String>();
				map.put("card_no", alipay_account);
				List list =alipayBalanceService.queryAlpayCode(map);
				Map codeMap = (Map)list.get(0);
				String verification_code = codeMap.get("verification_code").toString();
				driver.findElement(By.id("riskackcode")).clear();
		        driver.findElement(By.id("riskackcode")).sendKeys(verification_code);
		        driver.findElement(By.id("J-submit")).click();
//				
		        String balance = driver.findElement(By.xpath("//*[@id=\"J_balanceBaby\"]/div[3]/div[1]/span/em")).getText();
		        logger.info(" 支付宝余额:"+balance);
		        Map<String,String> mapBalance = new HashMap<String, String>();
		        mapBalance.put("card_remain", balance.replace(",", ""));
		        mapBalance.put("card_no", alipay_account);
				int flag = alipayBalanceService.updateBalance(mapBalance);
				if(flag==1){
					logger.info(" 更新成功!"+alipay_account);
				}else{
					logger.info("更新操作失败"+alipay_account);
				}
		        logger.info("当前时间:"+Utils.getHour());
				if(Utils.getHour()>=11 && Utils.getHour()<=14){
					
					Map<String,String> map1 = new HashMap<String, String>();
					map1.put("alipay_account", alipay_account);
					map1.put("download_time", Utils.currentDate(-1));
					
					int size = alipayBalanceService.queryAlipayBill(map1);
					logger.info(alipay_account+" 前一天该账单是否已下载 "+size);
					if(size==0){
						WebElement accountWeb = driver.findElement(By.xpath("//*[@id=\"J-myapp-content\"]/div[1]/div[2]/div/div[2]/ul/li[3]"));
				        accountWeb.click();
				        try {
				        	System.err.println("正在睡眠");
							Thread.sleep(10000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						int num = Integer.parseInt(Utils.currentDay());
						
						Map<String,String> mapDate = new HashMap<String, String>();
						String month = Utils.currentMonth();
						mapDate.put("month", month);
						String delta = alipayBalanceService.queryCalendar(mapDate);
						logger.info("日历差量:"+delta);
						int dt = Integer.parseInt(delta);
						
						int total = num +dt+7-1;
						logger.info("div定位到:"+total+"");
						String ph=config.getProperty("download");
//						
						JavascriptExecutor js = (JavascriptExecutor) driver;
						js.executeScript("arguments[0].click();",driver.findElement(By.xpath("//*[@id=\"billDownload\"]/div/div[2]/div["+total+"]/div[2]/span/i")));
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						File file=new File(ph);
						File[] tempList = file.listFiles();
						//每天创建一个目录
						String newPath = config.getProperty("dirPath")+Utils.currentDate(-1);
						File dir = new File(newPath);  
						if(!dir.exists()){
							dir.mkdir();
						}
						 for (int j = 0; j < tempList.length; j++) {
						   if (tempList[j].isFile()) {
						   //读取某个文件夹下的所有文件
							   String paths = file.list()[i];
								//读取某个文件夹下的所有文件
							 if(paths.contains("zip")){
//								 newPath = newPath+"\\"+alipay_account+"账单"+Utils.currentDate(-1)+".zip";
								 newPath = newPath+"\\"+alipay_account.substring(alipay_account.indexOf("you")+3, alipay_account.indexOf("@"))+".zip";
								 tempList[i].renameTo(new File(newPath));
								 break;
							    }else if(paths.contains("csv")){
							    	continue;
							    }else{
							    	continue;
							    }
						   }
						 }
						 Utils.delAllFile(ph); //删除完里面所有内容
						 map.put("is_download", "1");
						 map.put("download_path", newPath);
						 alipayBalanceService.insertAlipayBill(map);
					}
				}
				
		        driver.quit();  
		        // 关闭 ChromeDriver 接口  
		        service.stop();
				logger.info(" 关闭浏览器登录 "+alipay_account);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			logger.info("跳转支付地址:"+driver.getCurrentUrl());
			
			if(driver.getCurrentUrl().equals("https://authzth.alipay.com/login/checkSecurity.htm")){
				logger.info("短信验证码");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String code ="";
				while(true){
					 code = (String) redisService.RPOP("AlipayLoginCode");
					 if(code!=null&&!code.equals("")){
						 break;
					 }
					 try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				driver.findElement(By.xpath("//*[@id=\"riskackcode\"]")).sendKeys(code);
				driver.findElement(By.xpath("//*[@id=\"J-submit\"]/input")).click();
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		//不出现异常
			String balance = "";
			try {
				balance = driver.findElement(By.xpath("//*[@id=\"J_balanceBaby\"]/div[3]/div[1]/span/em")).getText();
		        logger.info(" 支付宝余额:"+balance);
			} catch (Exception e) {
				// TODO: handle exception
				
				balance = driver.findElement(By.xpath("//*[@id=\"J_balanceBaby\"]/div[2]/div[1]/span/em")).getText();
			}
			
			
	        Map<String,String> mapBalance = new HashMap<String, String>();
	        mapBalance.put("card_remain", balance.replace(",", ""));
	        mapBalance.put("card_no", alipay_account);
			int flag = alipayBalanceService.updateBalance(mapBalance);
			if(flag==1){
				logger.info(" 更新成功!"+alipay_account);
			}else{
				logger.info("更新操作失败"+alipay_account);
			}
			logger.info("当前时间:"+Utils.getHour());
			//当天早上7点下载账单
			if(Utils.getHour()>=11 && Utils.getHour()<=14){
				
				Map<String,String> map = new HashMap<String, String>();
				map.put("alipay_account", alipay_account);
				map.put("download_time", Utils.currentDate(-1));
				
				int size = alipayBalanceService.queryAlipayBill(map);
				logger.info(alipay_account+" 前一天该账单是否已下载 "+size);
				if(size==0){
					WebElement accountWeb = driver.findElement(By.xpath("//*[@id=\"J-myapp-content\"]/div[1]/div[2]/div/div[2]/ul/li[3]"));
			        accountWeb.click();
			        try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Map<String,String> mapDate = new HashMap<String, String>();
					int num = Integer.parseInt(Utils.currentDay());
					
					if(num==1){
						String month = Utils.currentMonth(-1);
						mapDate.put("month", month);
						int dys = Integer.parseInt(month);
						num = Utils.dy(dys);
					}else{
						String month = Utils.currentMonth();
						mapDate.put("month", month);
					}
					
					String delta = alipayBalanceService.queryCalendar(mapDate);
					logger.info("日历差量:"+delta);
					int dt = Integer.parseInt(delta);
					
					int total = num +dt+7-1;
					logger.info("div定位到:"+total+"");
					String ph=config.getProperty("download");
//					Utils.delAllFile(ph); //删除完里面所有内容
					//每天创建一个目录
					String newPath = config.getProperty("dirPath")+Utils.currentDate(-1);
					File dir = new File(newPath);  
					if(!dir.exists()){
						dir.mkdir();
					}
					try {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].click();",driver.findElement(By.xpath("//*[@id=\"billDownload\"]/div/div[2]/div["+total+"]/div[2]/span/i")));
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					File file=new File(ph);
					File[] tempList = file.listFiles();
					System.err.println(" 默认下载路径 "+tempList.length);
					 for (int j = 0; j < tempList.length; j++) {
					   if (tempList[j].isFile()) {
					   //读取某个文件夹下的所有文件
						   String paths = file.list()[i];
							//读取某个文件夹下的所有文件
						 if(paths.contains("zip")){
//							 newPath = newPath+"\\"+alipay_account+"账单"+Utils.currentDate(-1)+".zip";
							 newPath = newPath+"\\"+alipay_account.substring(alipay_account.indexOf("you")+3, alipay_account.indexOf("@"))+".zip";
							 tempList[i].renameTo(new File(newPath));
							 break;
						    }else if(paths.contains("csv")){
						    	continue;
						    }else{
						    	continue;
						    }
					   }
					 }
					
					} catch (Exception e) {
						// TODO: handle exception
						logger.info("下载账单异常");
					}
					 map.put("is_download", "1");
					 map.put("download_path", newPath);
					 alipayBalanceService.insertAlipayBill(map);
				}
			}
			
//			} catch (Exception e) {
//				// TODO: handle exception
//				logger.info("当前时间:"+Utils.getHour()+",捕获总异常:"+e.getMessage());
//				
////				Map<String,String> mapBalance = new HashMap<String, String>();
////		        mapBalance.put("card_remain", "");
////		        mapBalance.put("card_no", alipay_account);
////				int flag = alipayBalanceService.updateBalance(mapBalance);
////				if(flag==1){
////					logger.info(" 更新成功!"+alipay_account);
////				}else{
////					logger.info("更新操作失败"+alipay_account);
////				}
//				
//			}
			driver.quit();  
		    // 关闭 ChromeDriver 接口  
		    service.stop();
		    
		    Runtime runTime = Runtime.getRuntime();

		    //如果使用了ie浏览器和driver     
//		    runTime.exec("tskill iexplore");
//		    runTime.exec("tskill IEDriverServer");
		    //chrome浏览器和driver
		    try {
				runTime.exec("Taskkill /IM chrome.exe");
				runTime.exec("Taskkill /IM chromedriver.exe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			logger.info(" 关闭浏览器登录 "+alipay_account);
			long end = System.currentTimeMillis();
			logger.info("账号为:"+alipay_account+" 总费时 "+(end-start)/1000+" 秒");
		}
	}
	public static void main(String[] args) {
		
		System.setProperty("webdriver.chrome.driver","C:\\\\Program Files\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe");  
        // 创建一个 ChromeDriver 的接口，用于连接 Chrome  
        @SuppressWarnings("deprecation")  
        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(  
                        new File("d:\\\\chromedriver.exe")).usingAnyFreePort().build();  
        try {
			service.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        // 创建一个 Chrome 的浏览器实例  
        WebDriver driver = new RemoteWebDriver(service.getUrl(),  
                DesiredCapabilities.chrome());  
//     	 获取登录页面
        driver.get("https://auth.alipay.com/login/index.htm");
//			获取用户名输入框
        driver.findElement(By.id("J-input-user")).clear();
        driver.findElement(By.id("J-input-user")).sendKeys("huochepiaokuyou22@19e.com.cn");
//       driver.findElement(By.id("J-input-user")).sendKeys("huochepiaokuyou13@19e.com.cn");
//	        休息500ms，否则，速度太快，会将密码内容填充到用户名输入框中
        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	        获取密码输入框
        driver.findElement(By.id("password_rsainput")).clear();
        driver.findElement(By.id("password_rsainput")).sendKeys("hcm234789");//hcm234789
//	        休息2秒等待用户输入验证码
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}	
