package com.nineteen.job;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.nineteen.service.Alipay_balanceService;
import com.nineteen.util.CaptchaOCRUtil;
import com.nineteen.util.ImageRequest;
import com.nineteen.util.Utils;
import com.nineteen.vo.AlipayUser;


public class Alipay_airTravelJob extends QuartzJobBase {

	//日志־
	private static final Logger logger = Logger
	.getLogger(Alipay_airTravelJob.class);
	//获取配置文件参数
	@Value("#{config}")
	private Properties config;
	
	@Autowired  
    public Alipay_balanceService alipayBalanceService;
	@Override
	public void execute2(JobExecutionContext jobExecutionContext) {
		// TODO Auto-generated method stub
		//开始时间
		Long start = System.currentTimeMillis();
		List<AlipayUser> alipayUser = alipayBalanceService.getAirInfo();
		
		logger.info(" 检验是否有账户在设置的时间内进行操作 : "+alipayUser.size());
		
		//浏览器路径
		String path = (String)config.get("path");
		//浏览器驱动
		String path_driver = config.get("path_driver").toString();
		//支付宝登录地址
		String alipay_login = config.get("alipay_login").toString();
		
		for (int i = 0; i < alipayUser.size(); i++) {
			AlipayUser a = alipayUser.get(i); 
			//支付宝账号
			String alipay_account =  a.getCard_no();
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
//			navigation.to(alipay_login);
	        driver.get(alipay_login);
//				获取用户名输入框
	        driver.findElement(By.id("J-input-user")).clear();
	        driver.findElement(By.id("J-input-user")).sendKeys(alipay_account);
//		        休息500ms，否则，速度太快，会将密码内容填充到用户名输入框中
	        try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		        获取密码输入框
	        driver.findElement(By.id("password_rsainput")).clear();
	        driver.findElement(By.id("password_rsainput")).sendKeys("han234789");//hcm234789
//		        休息2秒等待用户输入验证码
	        try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        driver.findElement(By.id("J-login-btn")).click();
	        
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
//    		        driver.findElement(By.id("J-input-user")).sendKeys("huochepiao19e20@163.com");
//    		        休息500ms，否则，速度太快，会将密码内容填充到用户名输入框中
    		        try {
    					Thread.sleep(500);
    				} catch (InterruptedException e2) {
    					// TODO Auto-generated catch block
    					e2.printStackTrace();
    				}
//    		        获取密码输入框
    		        driver.findElement(By.id("password_rsainput")).clear();
    		        driver.findElement(By.id("password_rsainput")).sendKeys("han234789");
//    		     
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
				
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				WebElement accountWeb = driver.findElement(By.xpath("//*[@id=\"J_mainNavUl\"]/li[3]/a/em"));
		        accountWeb.click();
		        try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//当天早上5点下载账单
			if(Utils.getHour()>=5 && Utils.getHour()<=8){
				Map<String,String> map = new HashMap<String, String>();
				map.put("alipay_account", alipay_account);
				alipayBalanceService.updateAir(map);
				map.put("download_time", Utils.currentDate(-1));
				
				int size = alipayBalanceService.queryAlipayBill(map);
				logger.info(alipay_account+" 前一天该账单是否已下载 "+size);
				if(size==0){
				
				driver.findElement(By.xpath("//*[@id=\"sidebar\"]/div/div/ul/li[2]/a/span")).click();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				driver.findElement(By.xpath("//*[@id=\"dayCountDownTab\"]/span")).click();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				driver.findElement(By.xpath("//*[@id=\"JSFormSearch\"]")).click();
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String param = driver.findElement(By.xpath("//*[@id=\"JSServiceTable\"]/tbody/tr[1]/td[2]")).getText();
				param = param.substring(8, 10);
				String beforeDay =Utils.currentDay(-1);
				String newPath = config.getProperty("dirPath")+Utils.currentDate(-1);
				logger.info("支付宝最后生成账单日期为:"+param);
				if(param.equals(beforeDay)){
					driver.findElement(By.xpath("//*[@id=\"JSServiceTable\"]/tbody/tr[1]/td[3]/a")).click();
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					String ph=config.getProperty("download");
					File file=new File(ph);
					File[] tempList = file.listFiles();
					System.err.println(" 默认下载路径 "+tempList.length);
					//每天创建一个目录
					
					File dir = new File(newPath);  
					if(!dir.exists()){
						dir.mkdir();
					}
					 for (int j = 0; j < tempList.length; j++) {
						 String paths = file.list()[i];
						//读取某个文件夹下的所有文件
						 if(paths.contains("zip")){
							 continue;
						    }else if(paths.contains("csv")){
//						    	newPath = newPath+"\\"+alipay_account+"航旅账单"+Utils.currentDate(-1)+".csv";
						    	String ps = newPath+"\\"+alipay_account.substring(alipay_account.indexOf("19e")+3, alipay_account.indexOf("@"))+".csv";
						    	System.out.println("ps账单目录:"+ps);
							    tempList[i].renameTo(new File(ps));
							 break;
						    }else{
						    	continue;
						    }
					 }
					 map.put("is_download", "1");
					 map.put("download_path", newPath);
					 alipayBalanceService.insertAlipayBill(map);
				}
				}
			}
//			
			driver.quit();  
			service.stop();
		    // 关闭 ChromeDriver 接口  
			 Runtime runTime = Runtime.getRuntime();

			    //如果使用了ie浏览器和driver     
//			    runTime.exec("tskill iexplore");
//			    runTime.exec("tskill IEDriverServer");
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

}
