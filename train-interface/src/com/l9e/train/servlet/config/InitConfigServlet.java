/**
 * 
 */
package com.l9e.train.servlet.config;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.unlun.commons.database.DBBean;
import com.unlun.commons.res.Config;
import com.unlun.commons.res.Resource;



/**
 * @author salesforce.g
 *
 */
public class InitConfigServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	public void init(){
		try{
			logger.info("START | 加载配置文件");
			
			
			String databasePath = this.getClass().getClassLoader().getResource("database.properties").getPath();
            //String database=this.getInitParameter("database");
            Config.setConfigResource(databasePath);
            
            logger.info("END   | 加载配置文件");
            
            //创建数据库连接池0
            /*logger.info("START | 创建连接池");
            
          
            DBBean dbbean = new DBBean();  
    		
    		Resource.put("dbbean", dbbean);*/

    		//System.out.println("END | 创建连接池");
		}catch(Exception e){
			logger.info("初始化配置文件失败");
			e.printStackTrace();
		}
	}
}
