package com.nineteen.test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Testyanzm {
	
	public static void main(String[] args) throws AWTException, InterruptedException {
		testf();
	}
	public static void testf() throws AWTException, InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\\\Program Files\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe");
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
        driver.get("http://coupon.m.jd.com/coupons/show.action?key=bd0742853fc74088a4983a78e5f9784c&roleId=2897233&to=train.m.jd.com");
		Actions action = new Actions(driver); 
		
		action.contextClick(driver.findElement(By.id("imgCode"))).build().perform();
		action.contextClick();// 鼠标右键在当前停留的位置做单击操作 
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_DOWN);
	      
	    Thread.sleep(1000);
	      
	    robot.keyPress(KeyEvent.VK_DOWN);
	      
	    Thread.sleep(1000);
//	      
//	     robot.keyPress(KeyEvent.VK_DOWN);
//	      
//	     Thread.sleep(1000);
//	      
//	     robot.keyPress(KeyEvent.VK_DOWN);
//	      
//	     Thread.sleep(1000);
	      
	    // robot.keyPress(KeyEvent.VK_DOWN);
	      
	     //Thread.sleep(1000);
	      
	     // This is to release the down key, before this enter will not work
	      
//	     robot.keyRelease(KeyEvent.VK_DOWN);
//	      
//	     Thread.sleep(1000);
	      
	    robot.keyPress(KeyEvent.VK_ENTER);
//		Runtime rn = Runtime.getRuntime();
//		Process p = null;
//		try {			
//			p = rn.exec("\"D:/jd.exe\"");
//		} catch (Exception e) {
//			System.out.println("Error exec!");
//		}
//		
		try {
			Runtime.getRuntime().exec("D:\\test.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
