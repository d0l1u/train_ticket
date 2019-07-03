package com.l9e.util;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

public class HttpPostUtil {
	
	public static String sendAndRecive(String url,String params) throws Exception{
		
		HttpClientParams hcp = new HttpClientParams();
		hcp.setContentCharset("UTF-8");
		hcp.setSoTimeout(120000);
		HttpClient hc = new HttpClient();
		hc.setParams(hcp);
		
		PostMethod pm = new PostMethod(url);
		pm.addParameter("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		
		String[] aParam = params.split("&");
	    if (aParam.length == 0) {
	      return null;
	    }
	    int z = 0;
	    for (int i = 0; i < aParam.length; i++) {
	      z = aParam[i].indexOf('=');
	      if (z != -1) {
	        pm.addParameter(aParam[i].substring(0, z++), aParam[i].substring(z));
	      }

	    }

	    String repMsg = "";
	    try {
	      hc.executeMethod(pm);
	      repMsg = pm.getResponseBodyAsString();
	    } finally {
	      pm.releaseConnection();
	      pm = null;
	      hc = null;
	    }
//	    System.out.println(repMsg);
	    return repMsg;
	}
}