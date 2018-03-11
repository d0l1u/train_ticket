package com.speed.train;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.l9e.util.HttpUtil;

public class Huochepiaolicheng1 {
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		
		/*System.getProperties().setProperty("http.proxyHost", "192.168.65.126");
		System.getProperties().setProperty("http.proxyPort", "3128");*/
		
		String url = "http://search.huochepiao.com/juli/";
		String txtChuFa=URLEncoder.encode("北京","GBK");
		String txtDaoDa=URLEncoder.encode("天津","GBK");
		url = url+"?chuFa="+txtChuFa+"&daoDa="+txtDaoDa+"&Submit=%C0%EF%B3%CC%B2%E9%D1%AF";
		
		String backHtml = HttpUtil.sendByGet(url, "GBK", "300000", "300000");
		String indexPoint = "<div align=\"center\"><h1>";
		if(backHtml!=null && backHtml.indexOf(indexPoint)>0){
			String lichengStr = backHtml.substring(backHtml.indexOf(indexPoint)+24);
			String licheng = lichengStr.substring(0,lichengStr.indexOf("公里"));
			System.out.println(licheng);
		}
	}
}	
