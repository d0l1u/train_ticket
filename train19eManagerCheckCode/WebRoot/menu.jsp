<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate">
		<meta HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
		<meta http-equiv="Expires" CONTENT="-1">
		<title>验证码服务系统</title>
		<link rel="stylesheet" href="/css/menuStyle.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">
		<%
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String department = loginUserVo.getDepartment();
			String logined = loginUserVo.getLogined();
		%>
			$().ready(function(){
				$(".side-nav a").click(function(){
					$(".side-nav li").removeClass("current");
					$(this).parent("li").addClass("current");
				});
			<%//if(loginUserVo.getLogined().equals("0")){%>//第一次登录系统，修改密码
			<%
				if (loginUserVo.getLogined().equals("0") && "4".equals(loginUserVo.getUser_level())) {//超级管理员
			%>
					document.getElementById("updateUserPwd4").click();//默认进入修改密码页面
			<%
				}
				if (loginUserVo.getLogined().equals("0") && "1".equals(loginUserVo.getUser_level())) {//普通用户
			%>
					document.getElementById("updateUserPwd1").click();//默认进入修改密码页面
			<%
				}
				if (loginUserVo.getLogined().equals("0") && "2".equals(loginUserVo.getUser_level())) {//部门管理员
			%>
					document.getElementById("updateUserPwd2").click();//默认进入修改密码页面
			<%
				}
			%>

			
			<%//}else{%>
				<%
					if (loginUserVo.getLogined().equals("1") && "4".equals(loginUserVo.getUser_level())) {//超级管理员
				%>
						document.getElementById("queryAdminPage").click();//默认进入系统显示管理员首页
				<%
					}
					if (loginUserVo.getLogined().equals("1") && "1".equals(loginUserVo.getUser_level())) {//普通用户
				%>
						document.getElementById("queryByUserInfo").click();//默认进入系统显示管理员首页
				<%
					}
					if (loginUserVo.getLogined().equals("1") && "2".equals(loginUserVo.getUser_level())) {//部门管理员
				%>
						document.getElementById("queryByDepart").click();//默认进入系统显示部门管理员首页
				<%
					}
				%>
			<%//}%>
			
			
			})
		</script>
	</head>
	<body>
		<div class="side oz"><br/>
        <div class="side-nav oz mb10" >
        	<div style="text-align:center;border:1px solid #dadada;width:120px;margin:5px auto;padding:4px 0;background-color:#2c99ff;">
				<span style="font-size:12px;padding-right:20px;color:#CED8F6;line-height:20px;">您好，${sessionScope.loginUserVo.real_name}！<br />
					<!-- 
					本月收入<span style="color:#f60;font-weight:bold;font-family:arial;font-size:14px;line-height:20px;">${sessionScope.loginUserVo.monthMoney/100}</span>元<br />
					 -->
				</span>
				<a href="javascript:if(confirm('确实要退出系统吗?'))location='/loginManager/logOutUser.do'" style="color:#fff;font-size:12px;" target="mainConent">退出系统</a>
			</div>
            <!-- <h2>验证码系统</h2>   -->
            <ul class="parent-nav">
            <%
				if ("4".equals(loginUserVo.getUser_level())) {//超级管理员
			%>
                <li class="parent-li">
                    <span class="span1">数据统计</span>
                    <ul>
                    	<li><a href="/loginManager/queryAdminPage.do" id="queryAdminPage" target="mainConent">>&nbsp;&nbsp;打码总况</a></li>
                    	<li><a href="/loginManager/queryCurrentPage.do" target="mainConent">>&nbsp;&nbsp;当前打码</a></li>
                        <li><a href="/picture/queryPicturePageList.do" target="mainConent">>&nbsp;&nbsp;打码统计</a></li>
                    	<li><a href="/loginManager/queryByDay.do" target="mainConent">>&nbsp;&nbsp;按天查询</a></li>
                    	<li><a href="/loginManager/queryByAdmin.do" target="mainConent">>&nbsp;&nbsp;打码用户</a></li>
                    	<li><a href="/loginManager/toqueryCodeRate.do" target="mainConent">>&nbsp;&nbsp;打码效率</a></li>
                    </ul>
                </li>
                <li class="parent-li">
                	<span class="span1">用户管理</span>
                    <ul>
                    	<!-- <li><a href="/web/webCodePage.do" target="mainConent">>&nbsp;&nbsp;页面打码</a></li> -->
                        <li><a href="/loginManager/queryAdminUserList.do" target="mainConent">>&nbsp;&nbsp;查询用户</a></li>
                        <li><a href="/loginManager/toAddAdminPage.do" target="mainConent">>&nbsp;&nbsp;添加用户</a></li>
                        <li><a href="/user/queryUserInfo.do?opt_ren=<%=loginUserVo.getUsername() %>" id="updateUserPwd4" target="mainConent">>&nbsp;&nbsp;修改密码</a></li>
                    </ul>
                </li>
                <li class="parent-li">
                	<span class="span1">打码图表</span>
                    <ul>
                        <li><a href="/jfree/show15dayCodePicture.do" target="mainConent">>&nbsp;&nbsp;15日图表</a></li>
                        <li><a href="/jfree/showCodeSuccessPicture.do" target="mainConent">>&nbsp;&nbsp;小时图表</a></li>
                    </ul>
                </li>
                 <li class="parent-li">
                	<span class="span1">系统设置</span>
                    <ul>
                        <li><a href="/loginManager/systemSetting.do" target="mainConent">>&nbsp;&nbsp;权重设置</a></li>
                    </ul>
                </li>
			<%
				}
				if ("1".equals(loginUserVo.getUser_level())) {//普通用户
			%>
					<li class="parent-li">
						<span class="span1">个人打码</span>
			            <ul>
			            	<li><a href="/loginManager/queryByAdminInfo.do?opt_ren=<%=loginUserVo.getUsername() %>" id="queryByUserInfo" target="mainConent">>&nbsp;&nbsp;打码总况</a></li>
							<li><a href="/user/dayUser.do?opt_ren=<%=loginUserVo.getUsername() %>" target="mainConent">>&nbsp;&nbsp;按天查询</a></li>
						</ul>
					</li>
					<li class="parent-li">
	                	<span class="span1">打码图表</span>
	                    <ul>
	                        <li><a href="/jfree/showPeruserCodePicture.do?opt_ren=<%=loginUserVo.getUsername() %>" target="mainConent">>15日图表</a></li>
	                        <li><a href="/jfree/showUserCodeSuccess.do?opt_ren=<%=loginUserVo.getUsername() %>" target="mainConent">>小时图表</a></li>
	                    </ul>
                	</li>
                	<li class="parent-li">
	                	<span class="span1">用户管理</span>
	                    <ul>
	                    	<!-- <li><a href="/web/webCodePage.do" target="mainConent">>&nbsp;&nbsp;页面打码</a></li> -->
	                        <li><a href="/user/queryUserInfo.do?opt_ren=<%=loginUserVo.getUsername() %>" id="updateUserPwd1" target="mainConent">>&nbsp;&nbsp;修改密码</a></li>
	                    </ul>
                	</li>
			<%
				}
				if ("2".equals(loginUserVo.getUser_level())) {//部门管理员
			%>
				<li class="parent-li">
                    <span class="span1">数据统计</span>
                    <ul>
                    	<li><a href="/depart/queryDepartAdminPage.do?department=<%=department %>" id="queryByDepart" target="mainConent">>&nbsp;&nbsp;打码总况</a></li>
                    	<li><a href="/depart/queryDepartCurrentPage.do?department=<%=department %>" target="mainConent">>&nbsp;&nbsp;当前打码</a></li>
                        <li><a href="/depart/queryDepartPicturePageList.do?department=<%=department %>" target="mainConent">>&nbsp;&nbsp;打码统计</a></li>
                    	<li><a href="/depart/queryDepartByDay.do?department=<%=department %>" target="mainConent">>&nbsp;&nbsp;按天查询</a></li>
                    	<li><a href="/depart/queryDepartByAdmin.do?department=<%=department %>" target="mainConent">>&nbsp;&nbsp;打码用户</a></li>
                    </ul>
                </li>
                <li class="parent-li">
                	<span class="span1">用户管理</span>
                    <ul>
                    	<!-- <li><a href="/web/webCodePage.do" target="mainConent">>&nbsp;&nbsp;页面打码</a></li> -->
                        <li><a href="/depart/queryDepartAdminUserList.do?department=<%=department %>" target="mainConent">>&nbsp;&nbsp;查询用户</a></li>
                        <li><a href="/depart/toAddDepartAdminPage.do?department=<%=department %>" target="mainConent">>&nbsp;&nbsp;添加用户</a></li>
                        <li><a href="/user/queryUserInfo.do?opt_ren=<%=loginUserVo.getUsername() %>" id="updateUserPwd2" target="mainConent">>&nbsp;&nbsp;修改密码</a></li>
                    </ul>
                </li>
                <li class="parent-li">
                	<span class="span1">打码图表</span>
                    <ul>
                        <li><a href="/jfree/show15dayDepartCodePicture.do?department=<%=department %>" target="mainConent">>15日图表</a></li>
                        <li><a href="/jfree/showDepartCodeSuccessPicture.do?department=<%=department %>" target="mainConent">>小时图表</a></li>
                    </ul>
                </li>
					
			<%
				} 
			%>
	 			</ul>
	        </div>
	    </div>
	</body>
</html>