<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.l9e.common.LoginUserInfo"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<script type="text/javascript">
$().ready(function(){
	/*****以下是导航客户服务鼠标移上显示电话事件*******/
	$(".serveli").hover(function(){
		$(this).addClass("serveli_on");
		$(".serveli_div").css("display","block");
	},function(){
		$(this).removeClass("serveli_on");
		$(".serveli_div").css("display","none");
	});	
});
//退出登录
function loginOut(){
	var url = "/login/loginOutUser.jhtml";
	$.post(url,function(data){
    	if(data=="success"){
    		window.location.reload(); //页面重新加载
        }
  	});
}
</script>

<div id="header">
	<div class="logo" onclick="window.location='/buyTicket/bookIndex.jhtml'"><img src="/images/logo.jpg" width="257" height="59" /></div>
    <ul class="headnav">
    <%
		LoginUserInfo loginUser = (LoginUserInfo) request.getSession().getAttribute("loginUser");
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
	%>
		<li><a href="/login/toUserLogin.jhtml">登录</a></li>
    	<li><a href="/login/toUserRegister.jhtml">注册</a></li>
	<%		
		}else{
			String user_id = loginUser.getUser_id();
			String user_name = loginUser.getUser_name();
			String user_phone = loginUser.getUser_phone();
	%>
		<li><a href="javascript:void(0);">您好：<%=user_phone %>，欢迎使用<span>19旅行</span>！</a><a href="javascript:void(0);" onclick="loginOut();">退出</a></li>
	<%
		}
	%>
    	
    	<li><a href="/login/toUserApp.jhtml">手机客户端</a></li>
    	<!-- <li><a href="#">帮助中心</a></li> -->
        <li class="serveli"><a href="javascript:void(0);">客户服务</a></li>
        <div class="serveli_div"  style="display:none;">
    		<h4>400-688-2666&nbsp;&nbsp;<b>转</b>2<b>号键</b></h4>
   		</div>
    </ul>
</div>
