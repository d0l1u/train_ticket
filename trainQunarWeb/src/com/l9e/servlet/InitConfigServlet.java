package com.l9e.servlet;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.transaction.vo.InterAccount;
import com.l9e.transaction.vo.SysConfig;

@Component(value="initConfigServlet")
public class InitConfigServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(InitConfigServlet.class);
	
	@Value("#{propertiesReader[merchantCode]}")
	private String merchantCode;//qunar代理商id
	
	@Value("#{propertiesReader[md5Key]}")
	private String md5Key;//验签key
	
	@Override
	public void init() throws ServletException {
	//临时测试代理
	System.setProperty("http.proxyHost", "192.168.65.126");
	System.setProperty("http.proxyPort", "3128");
	
//	System.getProperties().setProperty("http.proxyHost", "192.168.63.238");
//	System.getProperties().setProperty("http.proxyPort", "6789" );
		logger.info("===============init system config begin=====================");
		
		logger.info("load merchant account...");
		String[] merchantCodes = merchantCode.split(",");
		String[] md5Keys = md5Key.split(",");
		Set<String> container = new HashSet<String>();
		
		if(merchantCodes == null || md5Keys == null || merchantCodes.length != md5Keys.length){
			logger.error("init system config fail,caused by merchantCode or md5Key match error!");
			logger.error("system will shutdown...");
			System.exit(0);
		}else{
			InterAccount account = null;
			for(int i=0;i<merchantCodes.length;i++){
				if(container.contains(merchantCodes[i])){
					logger.error("init system config fail,merchantCodes exsit same!");
					logger.error("system will shutdown...");
					System.exit(0);
				}else{
					container.add(merchantCodes[i]);
				}
				logger.info("merchantCode"+(i+1)+"="+merchantCodes[i]);
				account = new InterAccount();
				String key = "qunar"+(i+1);
				account.setName(key);
				account.setMerchantCode(merchantCodes[i]);
				account.setMd5Key(md5Keys[i]);
				SysConfig.accountContainer.add(account);
			}
		}
		logger.info("merchant account size="+SysConfig.accountContainer.size());
		super.init();
		logger.info("===============init system config end=======================");
	}

	@Override
	public void destroy() {
		SysConfig.accountContainer.clear();
		super.destroy();
	}
	
}
