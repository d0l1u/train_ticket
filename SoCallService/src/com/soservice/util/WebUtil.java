package com.soservice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.soservice.controller.SoCallController;

/**
 * IP地址
 * 
 * @author zhangyou
 * 
 */
public class WebUtil {
	/**
	 * 日志
	 */
	private static Logger log = Logger.getLogger(SoCallController.class);
	/**
     * 获取客户端IP地址.<br>
     * 支持多级反向代理
     * 
     * @param request
     *            HttpServletRequest
     * @return 客户端真实IP地址
     */
    public static String getRemoteAddr(final HttpServletRequest request) {
        try{
            String remoteAddr = request.getHeader("X-Forwarded-For");
            // 如果通过多级反向代理，X-Forwarded-For的值不止一个，而是一串用逗号分隔的IP值，此时取X-Forwarded-For中第一个非unknown的有效IP字符串
            if (isEffective(remoteAddr) && (remoteAddr.indexOf(",") > -1)) {
                String[] array = remoteAddr.split(",");
                for (String element : array) {
                    if (isEffective(element)) {
                        remoteAddr = element;
                        break;
                    }
                }
            }
            if (!isEffective(remoteAddr)) {
                remoteAddr = request.getHeader("X-Real-IP");
            }
            if (!isEffective(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
            return remoteAddr;
        }catch(Exception e){
            log.error("get romote ip error,error message:"+e.getMessage());
            return "";
        }
    }
     
    /**
     * 获取客户端源端口
     * @param request
     * @return
     */
    public static int getRemotePort(final HttpServletRequest request){
        try{
            String port = request.getHeader("remote-port");
            if(!"".equals(StringUtil.nullToEmpty(port ))) {
                try{
                    return Integer.parseInt(port);
                }catch(NumberFormatException ex){
                    log.error("convert port to long error , port: "+port);
                    return 0;
                }
            }else{
                return request.getRemotePort();
            }       
        }catch(Exception e){
            log.error("get romote port error,error message:"+e.getMessage());
            return 0;
        }
    }
    /**
     * 远程地址是否有效.
     * 
     * @param remoteAddr
     *            远程地址
     * @return true代表远程地址有效，false代表远程地址无效
     */
    private static boolean isEffective(final String remoteAddr) {
        boolean isEffective = false;
        if ((null != remoteAddr) && (!"".equals(remoteAddr.trim()))
                && (!"unknown".equalsIgnoreCase(remoteAddr.trim()))) {
            isEffective = true;
        }
        return isEffective;
    }
	/**
	 * 读取XML
	 * @param req
	 * @return
	 */
    public static   String  readerXml(HttpServletRequest req){
    	String requestXml = "";
    	BufferedReader br = null;
		// 读取请求的xml报文
		try {
			br = new BufferedReader(new InputStreamReader(req.getInputStream(),
					"utf-8"));
			StringBuilder sb = new StringBuilder();
			while ((requestXml = br.readLine()) != null) {
				sb.append(requestXml);
			}
			requestXml = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭流
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return  requestXml;
    }

}
