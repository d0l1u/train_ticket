<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String color="";
if(request.getParameter("color")==null){
	color="0";
}else{
	color=request.getParameter("color");
}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>火车票业务常见问题</title>
<link rel="stylesheet" href="/css/style.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
</head>

<body>
<div class="content oz">
<div class="index_all">
				
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
				<jsp:param name="menuId" value="guide" />
			</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start--> 
    	<div class="infoinput-right oz">
  			<div class="help_type oz">
            	<ul class="help_type_ul">
	                <li class="current1"><a href="/pages/guide/newKaiTong.jsp">新手指南</a></li>
	                <li class="current current1"><a href="/pages/guide/questionGouPiao.jsp">常见问题</a></li>
	                <li class="current1"><a href="/pages/guide/bxShuoMing.jsp">相关规章</a></li>
	                <li class="current1" style=" width:196px;"><a href="/pages/guide/tuiPiaoGuiDing.jsp">业务常识</a></li>
	           </ul>
  		</div>  
  		  <!--内容 start-->
  <div class="bread_nav mb10"> 
  	<ul class="bread_nav_ul">
        <li ><a style="color:#32B1F0;" href="questionGouPiao.jsp">购票问题</a></li>
        <li ><a href="questionTuiPiao.jsp">退票问题</a></li>
        <li ><a href="questionBX.jsp">保险问题</a></li>
        <li ><a href="questionYushou.jsp">火车票预售期</a></li>
     </ul>
  </div>   
  <!--内容 start-->
<div class="yew_r_con oz">
    <h2>火车票业务常见问题</h2>
    <div class="con_con">
      <h5>购票问题</h5>
      <ul>
        <li>
        <p>1.火车票业务受理时间范围是多少？<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;火车票业务受理时间为7:00-23:00，在此时间段内您可进行订票、退票、查询订单等各项操作，23：00之后的订单将在次日早晨7：00之后处理。若因23:00之后下单未处理而造成的无法乘车等损失，平台不承担相应责任。</p>
        </li>
        
        <li>
        <%
        	if("1".equals(color)){
        %>
        <p ><span style="background-color:#87CEEB;font-size:16px;">2.为什么要实名认证？</span><br/>
        <%
        	}else{
        %>
        <p >2.为什么要实名认证？<br/>
           <%
        	}
           %>
        &nbsp;&nbsp;&nbsp;&nbsp;根据<a onclick="window.open('http://www.12306.cn/mormhweb/zxdt/201402/t20140223_1435.html')" href="#">《铁路互联网购票身份核验须知》</a>的规定，自3月1日起铁路系统已经开始对注册用户进行实名认证，为了保证系统正规和合法的运营，方便周边用户的出行，所以需要代理商提供未在12306绑定的实名身份证信息进行绑定，用于火车票代购服务，我们将在24小时之内通知代理商是否绑定成功。后期系统将对未实名认证的代理商停止提供火车票服务。</p>
        </li>
        
        <li>
        <p >3.购票时需要额外收取手续费用吗？<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;本网站坚决执行铁路系统的规定，禁止收取手续费。</p>
        </li>
        
        <li>
        <p >4.订票的时候，信息填写错误，怎么办?<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;首先请查看订单状态，如果订单状态显示“支付成功/出票中”，则第一时间联系客服人员，询问是否可以取消订单。如果显示“出票成功”，只能由您主动提交退票申请。</p>
        </li>
        
        <li>
        <p >5.如何确定购票成功？<br/>
	        &nbsp;&nbsp;&nbsp;&nbsp;①	进入【我的订单】查看订单状态。当订单状态显示为“出票成功”时，则表示该订单已经购票成功。<br />
			   &nbsp;&nbsp;&nbsp;&nbsp;②	若出票成功，系统会根据用户在购票时填写的手机号码自动下发短信，告知购票成功。
	        </p>
        </li>
        
        <li>
        <p >6.出票失败原因显示身份信息待核验是什么意思？<br/>
	        &nbsp;&nbsp;&nbsp;&nbsp;“待核验”指持二代居民身份证乘车人身份信息未经国家身份认证权威部门核验，需持二代居民身份证原件到火车站售票窗口或铁路代售点查询办理身份信息核验。通过核验后，方可继续网购火车票。</p>
        </li>
        
        <li>
        <p >7.在线支付票款有时间限制吗？<br/>
	     &nbsp;&nbsp;&nbsp;&nbsp;在线支付时需要立即支付，否则转入其他页面时，该订单会自动取消，不能重新支付。 </p>
        </li>
        
        <li>
        <p >8.网购火车票一张身份证最多可以购买多少火车票？一笔订单是否有购票张数限制？<br/>
	     &nbsp;&nbsp;&nbsp;&nbsp;一张有效身份证件同一乘车日期同一车次只能购买一张车票，但使用同行成年人的有效身份证件信息为乘车儿童购买儿童票的除外。一张订单可以同时购买五张车票。 </p>
        </li>
        
        <li>
        <p >9.我在网站查询有票，但预订时就没有票了怎么回事？<br/>
	     &nbsp;&nbsp;&nbsp;&nbsp;余票信息会随时更新，会出现多人抢票的情况，此种情况建议您提早购票。 </p>
        </li>
        
        <li>
        <p >10.支付成功，为什么收不到购票成功的短信通知？<br/>
	     &nbsp;&nbsp;&nbsp;&nbsp;①	耐心等待，可能订单是正在处理中，请进入【我的订单】中查看订单的状态，是否购票成功。<br />
			 &nbsp;&nbsp;&nbsp;&nbsp;②	如果订单状态显示“出票成功”但未收到短信通知，可能和目前短信服务商的低到达率和及时率所致。
 		</p>
        </li>
        
        <li>
        <p >11.卧铺票如何选择下铺？<br/>
	     &nbsp;&nbsp;&nbsp;&nbsp;卧铺铺位随机出票，暂收下铺价格，若出上中铺，根据实际票价退还差价。若想为老人和孕妇买卧铺，建议去火车站或代售点买下铺。 </p>
        </li>
        
        <li>
        <p >12.购买儿童票时需要注意哪些问题？<br/>
	     &nbsp;&nbsp;&nbsp;&nbsp;儿童票订票步骤和成人票是一样的，但有以下几点要求，请注意：<br />
	     &nbsp;&nbsp;&nbsp;&nbsp; ①	 儿童原则上不能单独乘车，儿童票须跟成人票一起购买；<br />
		 &nbsp;&nbsp;&nbsp;&nbsp; ②	乘车儿童没有有效身份证件的，可使用同行成年人的有效身份证件信息进行购票；乘车儿童有有效身份证件的，可填写儿童有效身份证件信息：如儿童未办理居民身份证，而使用居民户口簿上的身份证号码购买儿童票的，可凭其户口簿原件领取车票；<br />
		 &nbsp;&nbsp;&nbsp;&nbsp; ③	儿童身高为1.2-1.5米可购买儿童票，超过1.5米须购买成人票。一名成年乘客可以免费携带一名身高不足1.2米的儿童。身高不足1.2米的儿童超过一名时，一名儿童免费，其他儿童需购买儿童票；<br />
		 &nbsp;&nbsp;&nbsp;&nbsp; ④	儿童票暂收成人票价，出票后根据实际票价再返差价，差价后返至代理商19e账户中。
	      </p>
        </li>
        
      </ul>
    </div>
   </div>
   <!--内容 end--> 
 </div>
 </div>
</div>
</body>
</html>
