<%@ page language="java" contentType="text/html; charset=utf-8" %>
<% 

//调试模式
 boolean DEBUG_ = true;
 String PARTNER		= "1219762701" ;	//财付通商户号
 String PARTNER_KEY	= "02dc5bc202057b6b71e6486af3cafb65";	//财付通密钥
 String APP_ID		= "wxd728b7c2edc62fc2";	//appid
 String APP_SECRET	= "8d9874ce5c0e8750353b6c72073c361b";	//appsecret
 String APP_KEY		= "BA4M2PqJfNsHn6mfLXfLGAnJQHPNnj09dB3Zq5M7H5FjGQOvTUtU5VtvwFpwMz5wn8IqLFe5G8Nudx8NlJEu71apiSsxQE52Fyf7u1OJ254TxFqpUEKir4CeeCvRU14D";	//paysignkey 128位字符串(非appkey)
 String NOTIFY_URL	= "http://114.247.40.69:55399/wepay/receiveWepayNotify.jhtml";  //支付完成后的回调处理页面,*替换成notify_url.asp所在路径
 String LOGING_DIR	= "/logs/wechat/train.log";  //日志保存路径
%>
