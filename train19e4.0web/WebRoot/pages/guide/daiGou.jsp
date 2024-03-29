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
	                <li class="current1"><a href="/pages/guide/questionGouPiao.jsp">常见问题</a></li>
	                <li class="current current1"><a href="/pages/guide/bxShuoMing.jsp">相关规章</a></li>
	                <li class="current1" style=" width:196px;"><a href="/pages/guide/tuiPiaoGuiDing.jsp">业务常识</a></li>
	           </ul>
  		</div>  
  		  <!--内容 start-->
  <div class="bread_nav mb10"> 
  	<ul class="bread_nav_ul">
        <li ><a href="bxShuoMing.jsp">保险说明</a></li>
         <li ><a  style="color:#32B1F0;" href="daiGou.jsp">火车票线下代购协议</a></li>
        <li ><a href="zhuCe.jsp">委托注册多个代购账户协议</a></li>
         <li ><a href="chengnuoPage.jsp"> 平台购票承诺书</a></li>
     </ul>
  </div>   
  <!--内容 start-->
<div class="yew_r_con oz">
    <h2>火车票业务相关章程</h2>
    <div class="con_con">
       <h5>火车票线下代购服务协议</h5>
            <ul>
              <li>
                <h3>一、协议的完善和修改</h3>
              </li>
              <li><p>北京一九易电子商务有限公司（简称：一九易）有权在必要时根据互联网的发展和中华人民共和国有关法律、法规的变化，不断地完善服务质量并依此修改本协议的条款，修改后的协议条款将在相关页面上提示修改的内容。代理商如果没有提出疑问，则被视为接受修改后的协议条款。</p></li>
              <li>
                <h3>二、服务内容</h3>
              </li>
              <li><p>一九易是将有资质的火车票代售点和互联网提供的火车票服务信息汇集于平台，为一九易代理商提供互联网信息查询服务，协助代理商周边的用户通过互联网或火车票代售点联系并预订相关服务项目。在涉及到具体服务的过程中的问题，但我们会将尽力协助代理商或者用户与相关服务提供商进行协商，不能协商解决的，用户自己可以向消费者协会投诉或通过法律途径解决。</p></li>
              <li>
                <h3>三、服务风险提示</h3>
              </li>
              <li><p>一九易的火车票代购服务提供给代理商周边的用户在预售期以内的火车票代购服务，由于火车票的购买不可预见性，本公司并不保证代购系统一定可以完全满足代理商周边用户的需求，公司会尽量协调相关资源提供给代理商周边用户优质的服务。</p></li>
              <li>
                <h3>四、关于服务中涉及的款项问题的说明</h3>
              </li>
              <li><p>在使用业务期间发生的支付、退款和补款过程，均在本公司提供的账户系统下操作完成。</p></li>
              <li><p><span>关于支付：</span>代理商通过平台进行的每一次交易，均按账户系统规定流程与机制处理完成，且在交易结束后会产生一个交易号，代理商通过交易号可以查询该次交易的详情信息。</p></li>
              <li><p><span>关于补款：</span>火车票存在多种坐席及车次可以满足用户的需求，所以存在购票过程中会产生差价的问题。其中差价的处理本着多退少补的原则，当代理商使用了一九易所提供的相关服务，就表示该用户接受此项原则，并能及时地进行补款操作以保证服务的正常进行。对未能及时补款造成的后果由代理商承担。</p></li>
              <li><p><span>关于退款：</span>一九易只负责退款到代理商账号的操作，未出票成功的订单公司系统会24小时内可退至代理商账户中；出票成功后的退款需要在15个工作日内抛去5%的手续费的金额退还给代理商的账户中。 对退款过程中代理商是否把款退还给用户或者产生的延时代理商自己负责。</p></li>
              <li><p><span> 关于保险：</span>在购票过程中，代理商可以帮助用户选择是否需要购买交通意外险。收费标准：20元/份，保额20万；具体价格以平台为准；每位乘车人最多可购买5份。最后在订票成功以后，一九易会以短信形式发送保单号和订单号到用户在平台上预留的手机上，保险的真实性可以根据保险单号到相关网站上进行验证。</p></li>
           	 <li><h3>五、免责条款</h3></li>
              <li><p>1、一九易火车票业务禁止代理商向用户收取票面价格以外的服务费用，如果因此类费用发生纠纷和一九易公司没有任何关系。</p></li>
              <li><p>2、一九易禁止代理商在未征得用户允许的情况向用户出售火车保险，如果因此发生的服务纠纷和一九易公司没有任何关系。</p></li>
              <li><p>3、在系统服务期间，一九易将有权根据需要，随时对产品的服务内容进行更新或删除，且无需另行通知或取得代理商的同意。</p></li>
              <li><p>4、一九易提供的火车票信息、列车时刻信息、客票余额信息、座位信息、票价等信息全部来自互联网，在代购的火车票的车次、票价、始发时间、到站时间等以实际为准，本公司对此等信息的准确性、完整性、合法性或真实性均不承担责任。
              </p></li>
              <li><p>5、一九易提供的是火车票代购服务，您接受本协议代表您已经同意我们使用您所填写的乘客信息进行代购，包括但不仅限于授权我们使用您的乘客信息创建12306帐号，同时您必须遵守12306的购票规定的服务条款（<a style="color:blue;font-size:12px;"  onclick="window.open('https://kyfw.12306.cn/otn/regist/rule')" href="#" >https://kyfw.12306.cn/otn/regist/rule</a>），对于您违反《火车票线下代购服务协议》
					 所引起的争议和法律问题，由您自行承担。
              </p></li>
               <li><h3>六、其他</h3></li>
              <li><p>1.如本协议的任何条款被视作无效或无法执行，则上述条款可被分离，其余部分则仍具有法律效力。<br/>&nbsp;&nbsp;&nbsp;&nbsp;2.此版本为一九易所有并享有一切解释权利。</p></li>
      </ul>
    </div>
   </div>
   <!--内容 end--> 
 </div>
 </div>
</div>
</body>
</html>
