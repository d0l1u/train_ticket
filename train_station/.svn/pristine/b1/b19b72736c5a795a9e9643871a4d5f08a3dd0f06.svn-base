package com.speed.train;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.l9e.util.HttpUtil;

public class Huochepiaolicheng {
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		/*System.getProperties().setProperty("http.proxyHost", "192.168.65.126");
		System.getProperties().setProperty("http.proxyPort", "3128");*/
		
		String url = "http://search.huochepiao.com/checi/D1";
//		String txtChuFa=URLEncoder.encode("北京","GBK");
//		String txtDaoDa=URLEncoder.encode("哈尔滨","GBK");
//		url = url+"?chuFa="+txtChuFa+"&daoDa="+txtDaoDa+"&Submit=%C0%EF%B3%CC%B2%E9%D1%AF";
		
		String backHtml = HttpUtil.sendByGet(url, "GBK", "300000", "300000");
//		System.out.println("backHtml"+backHtml);
		String indexPoint = "<tr bgcolor=\"#ffffff\" onMouseOver=\"this.bgColor='#eeeeff';\" onMouseOut=\"this.bgColor='#ffffff';\">";
		if(backHtml!=null && backHtml.indexOf(indexPoint)>0){
			String[] arrayStr = backHtml.split(indexPoint);
			Map<String,String> lichengMap = new HashMap<String,String>();
			for(int i=1;i<arrayStr.length ;i++){
				String str = arrayStr[i];
				if(str!=null && !"".equals(str)){
					System.out.println(str);
					String tempStr = str.substring(str.indexOf("a href"),str.indexOf("</a>"));
					String key = tempStr.substring(tempStr.indexOf(">")+1);
					String[] centerStr = str.split("<td align=\"center\">");
					String value = centerStr[8].substring(0,centerStr[8].indexOf("<"));
					lichengMap.put(key, value);
				}		
			}
			System.out.println(lichengMap.toString());
		}	
	}
}	
