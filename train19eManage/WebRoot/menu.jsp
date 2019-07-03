<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%
	Calendar theCa = Calendar.getInstance();
	theCa.setTime(new Date());
	theCa.add(theCa.DATE, 0);
	Date date = theCa.getTime();
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String querydate = df.format(date);
%>
<html>
    <head>
       <title>火车票管理后台</title>
       <link rel="stylesheet" href="/css/menuStyle.css" type="text/css" />
       <style type="text/css">
           .menutitle {
              cursor:pointer;
           }
           .submenu {
              cursor:pointer;
           }
       </style>
       <script type="text/javascript">
           if (document.getElementById){ //DynamicDrive.com change
              document.write('<style type="text/css">\n')
              document.write('.submenu{display: none;}\n')
              document.write('</style>\n')
           }
           function SwitchMenu(obj){
              if(document.getElementById){
                  var el = document.getElementById(obj);
                  var ar = document.getElementById("masterdiv").getElementsByTagName("div"); //DynamicDrive.com change
                  if(el.style.display != "block"){ //DynamicDrive.com change
                     for (var i=0; i<ar.length; i++){
                         if (ar[i].className=="submenu") //DynamicDrive.com change
                            ar[i].style.display = "none";
                     }
                     el.style.display = "block";
                  }else{
                     el.style.display = "none";
                  }
              }
           }
           //选中变色
           function init(){
			var a=document.links;
				for(var i=0;i<a.length;i++){
					if(i!=0){
						a[i].style.color="";
						a[i].onclick=change;
					}
				}
			}
			function change(){
			var a=document.links;
				for(var i=0;i<a.length;i++){
					if(i!=0){
						a[i].style.color="";
						a[i].onclick=change;
					}
				}
			this.style.color="#f60";
			//console.log(" 颜色  ");
			}
			window.onload=init;
       </script>
       <script type="text/javascript" src="/js/jquery.js"></script>
    </head>
    <body>
       <div style="text-align:center;border:1px solid #dadada;width:120px;margin:5px auto;padding:4px 0;background-color:#2c99ff;">
       <span style="font-size:12px;padding-right:20px;color:#CED8F6">您好！${sessionScope.loginUserVo.real_name}</span><br />
       <a href="/login/logOutUser.do" style="color:#fff;font-size:12px;" 
           target="mainConent">退出系统</a>
       </div>
           <div class="side oz">
        <div class="side-nav oz mb10" id="masterdiv">
            <h2>火车票管理</h2>
            <ul class="parent-nav">
            <%
            	LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
            			.getAttribute("loginUserVo");
            	if ("2".equals(loginUserVo.getUser_level())
            			|| "1".equals(loginUserVo.getUser_level())) {
            %>
                <li class="parent-li">
                       <span class="span3">常用功能</span>
                       <ul>
                               <li><a href="/acquire/queryAcquirePage.do" target="mainConent">&gt;&nbsp;&nbsp;出票管理</a></li>
                                <li><a href="/allRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                                <li><a href="/elongAlter/queryAlterTicketPage.do" target="mainConent">&gt;&nbsp;&nbsp;改签管理</a></li>
                               <li><a href="/allInsurance/queryInsurancePage.do" target="mainConent">&gt;&nbsp;&nbsp;保险管理</a></li>
                               <li><a href="/allComplain/queryComplainPage.do" target="mainConent">&gt;&nbsp;&nbsp;投诉管理</a></li>
                               <li><a href="/rob/queryRobPage.do" target="mainConent">&gt;&nbsp;&nbsp;抢票管理</a></li>
                              <!-- <li><a href="/jdRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;京东退票管理</a></li>
                            
                               <li><a href="/questionOrder/gototrain_question_order.do?question_status=11&question_status=33" target="mainConent">&gt;&nbsp;&nbsp;问题订单</a></li>
                          	   <li><a href="/oldOrder/queryOldOrderPage.do" target="mainConent">&gt;&nbsp;&nbsp;历史订单</a></li>
                          	   <li><a href="/manual/queryManualPage.do" target="mainConent">&gt;&nbsp;&nbsp;人工出票</a></li>
                               -->
                       </ul>
                </li>
                
                  <li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub20')">
                     <span class="span1">&nbsp;<font color="red">京东出票</font></span>
                  </div>
                     <div class="submenu" id="sub20">
                         <ul>
                             <li><a href="/jdRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;京东退票</a></li>
                             <li><a href="/jdCard/queryJDCardPage.do" target="mainConent">&gt;&nbsp;&nbsp;京东预付卡</a></li>
                           </ul>
                     </div>
                    
                </li>
                
                <li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub14')">
                     <span class="span14">&nbsp;<font color="red">携程出票</font></span>
                  </div>
                     <div class="submenu" id="sub14">
                         <ul>
                             <li><a href="/ctrip/queryCtripPage.do" target="mainConent">&gt;&nbsp;&nbsp;携程出票</a></li>
                             <li><a href="/ctripAccount/queryCtripAccountPage.do" target="mainConent">&gt;&nbsp;&nbsp;携程账号</a></li>
                           </ul>
                     </div>
                    
                </li>
 						 <%
 						 	if ("2".equals(loginUserVo.getUser_level())) {
 						 %>
  
  			 <li class="parent-li">
	  			  		<div class="menutitle" onclick="SwitchMenu('sub13')">
	                     <span class="span13">&nbsp;数据分析</span>
	                  </div>
	                     <div class="submenu" id="sub13">
	                         <ul>
	                           <li><a href="/compete/queryCompetePage.do" target="mainConent">&gt;&nbsp;&nbsp;竞价跟踪</a></li>
	                           <li><a href="/hcStat/queryHcStatPage.do" target="mainConent">&gt;&nbsp;&nbsp;出票统计</a></li>
	                           <!-- 
	                           <li><a href="/acquire/queryAcquireXlPage.do" target="mainConent">&gt;&nbsp;&nbsp;出票效率</a></li>
	                            <li><a href="/channeltj/queryChanneltjPage.do" target="mainConent">&gt;&nbsp;&nbsp;渠道统计</a></li>
	                     	 	<li><a href="/acquire/queryAcquireFailPage.do" target="mainConent">&gt;&nbsp;&nbsp;失败订单</a></li> 
	                            -->
	                            <li><a href="/outTicketTj/queryOutTicketTjPage.do?type=pc&create_time=<%=querydate%>" target="mainConent">&gt;&nbsp;&nbsp;效率统计</a></li>
	                            <li><a href="/accounttj/queryAccounttjPage.do" target="mainConent">&gt;&nbsp;&nbsp;账号统计</a></li>
	                            <li><a href="/failtj/queryFailtjPage.do" target="mainConent">&gt;&nbsp;&nbsp;失败统计</a></li>
	                            <li><a href="/execptiontj/queryExecptiontjPage.do" target="mainConent">&gt;&nbsp;&nbsp;异常统计</a></li>
	                         </ul>
                    	 </div>
                </li>
                		 <%
                		 	}
                		 %>
                <li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub2')">
                     <span class="span1">&nbsp;19e管理</span>
                  </div>
                     <div class="submenu" id="sub2">
                         <ul>
                             <li><a href="/booking/queryBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                             <li><a href="/refundTicket/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                             <li><a href="/notice/queryNoticePage.do" target="mainConent">&gt;&nbsp;&nbsp;公告管理</a></li>
                             <li><a href="/joinUs/queryJoinUsPage.do" target="mainConent">&gt;&nbsp;&nbsp;加盟列表</a></li>
                             <li><a href="/refundStation/queryRefundStationPage.do" target="mainConent">&gt;&nbsp;&nbsp;数据管理</a></li>
                             <%
                             	if ("2".equals(loginUserVo.getUser_level())
                             				|| "lujuan".endsWith(loginUserVo.getUser_name())) {
                             %>
                             <li><a href="/opterStat/queryOpterStatPage.do" target="mainConent">&gt;&nbsp;&nbsp;考核统计</a></li>
                             <li><a href="/userStat/queryUserStatListPage.do" target="mainConent">&gt;&nbsp;&nbsp;用户统计</a></li>
                             <li><a href="/system/querySystemPage.do" target="mainConent">&gt;&nbsp;&nbsp;开通管理</a></li>
                             <%
                             	}
                             %>
                             
                             <li><a href="/file/queryFileList.do" target="mainConent">&gt;&nbsp;&nbsp;退款查询</a></li>
                             
							<%
								if ("2".equals(loginUserVo.getUser_level())) {
							%>
							<li>
								<a href="/psOrder/queryPsorderPage.do" target="mainConent">&gt;&nbsp;&nbsp;配送上门</a>
							</li>
							<li>
								<a href="/manualfind/queryManualPage.do" target="mainConent">&gt;&nbsp;&nbsp;人工查询</a>
							</li>
							<%
								}
							%>
							</ul>
                     </div>
                    
                </li>
                <li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub3')";>
                     <span class="span1">&nbsp;去哪管理</span>
                  </div>
                     <div class="submenu" id="sub3"> 
                          <ul>
                               <li><a href="/qunarbook/queryAllBookList.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                               <li><a href="/qunarrefund/queryRefundTicketPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                           </ul>
                     </div>
                </li>
                <li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub10')";>
                     <span class="span1">&nbsp;代理管理</span>
                  </div>
                     <div class="submenu" id="sub10"> 
                          <ul>
                               <li><a href="/elongBook/queryAllBookList.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                               <li><a href="/elongRefund/queryRefundTicketPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                                <li><a href="/elongAlter/queryAlterTicketPage.do" target="mainConent">&gt;&nbsp;&nbsp;改签管理</a></li>
                                <%
                                	if ("2".equals(loginUserVo.getUser_level())) {
                                %>
                              	<li><a href="/elongBook/queryAllBookList.do?abnormal=1" target="mainConent">&gt;&nbsp;&nbsp;异常管理</a></li>
                               <li><a href="/elongBook/queryAllBookList.do?chexiao=1" target="mainConent">&gt;&nbsp;&nbsp;撤销管理</a></li>
                          	   <%
                          	   	}
                          	   %>
                              <li><a href="/elongStation/queryElongStationPage.do" target="mainConent">&gt;&nbsp;&nbsp;数据管理</a></li>
                              <li><a href="/elongExcel/queryExcelList.do" target="mainConent">&gt;&nbsp;&nbsp;表格下载</a></li>
                              <li><a href="/elongExcel/tcAddCheckPage.do" target="mainConent">&gt;&nbsp;&nbsp;同程对账</a></li>
                           </ul>
                     </div>
                </li>
                
                 <li class="parent-li">
                 <div class="menutitle" onclick="SwitchMenu('sub15')">
                     <span class="span1">&nbsp;高铁管家</span>
                  </div>
                     <div class="submenu" id="sub15"> 
                         <ul>
                         <li><a href="/gtBooking/queryGtBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                         <li><a href="/gtRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                         <li><a href="/gtStation/queryGtStationPage.do" target="mainConent">&gt;&nbsp;&nbsp;数据管理</a></li>
                         <li><a href="/gtBooking/queryGTNOCallBackOrder.do" target="mainConent">&gt;&nbsp;&nbsp;回调订单</a></li>
                         </ul>
                     </div>
              </li>
               <li class="parent-li">
                 <div class="menutitle" onclick="SwitchMenu('sub16')">
                     <span class="span1">&nbsp;美团管理</span>
                  </div>
                     <div class="submenu" id="sub16"> 
                         <ul>
                        <li><a href="/meituanBook/queryAllBookList.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                        <li><a href="/meituanRefund/queryRefundTicketPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                        <li><a href="/meituanStation/queryMeituanStationPage.do" target="mainConent">&gt;&nbsp;&nbsp;数据管理</a></li>
                         </ul>
                     </div>
              </li>
               <li class="parent-li">
                 <div class="menutitle" onclick="SwitchMenu('sub17')">
                     <span class="span1">&nbsp;携程管理</span>
                  </div>
                     <div class="submenu" id="sub17"> 
                         <ul>
                        <li><a href="/xcBooking/queryXcBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                        <li><a href="/xcRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                        <li><a href="/xcStation/queryXcStationPage.do" target="mainConent">&gt;&nbsp;&nbsp;数据管理</a></li>
                         </ul>
                     </div>
              </li>
               <li class="parent-li">
                 <div class="menutitle" onclick="SwitchMenu('sub18')">
                     <span class="span1">&nbsp;途牛管理</span>
                  </div>
                     <div class="submenu" id="sub18"> 
                         <ul>
                        <li><a href="/tuniuBooking/queryTuniuBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                        <li><a href="/tuniuRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                       	<li><a href="/tuniuUrgeRefund/queryUrgeRefundPage.do" target="mainConent">&gt;&nbsp;催退款管理</a></li>
                       	<li><a href="/tuniuStation/queryTuniuStationPage.do" target="mainConent">&gt;&nbsp;&nbsp;数据管理</a></li>
                         </ul>
                     </div>
              </li>
                <li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub4')";>
                     <span class="span1">&nbsp;内嵌管理</span>
                  </div>
                     <div class="submenu" id="sub4"> 
                          <ul>
                               <li><a href="/innerBooking/queryInnerBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                               <li><a href="/innerRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                               <li><a href="/innerNotice/queryNoticePage.do" target="mainConent">&gt;&nbsp;&nbsp;公告管理</a></li>
                           	
                           </ul>
                     </div>
                </li>
                
              <li class="parent-li">
                 <div class="menutitle" onclick="SwitchMenu('sub8')">
                     <span class="span1">&nbsp;对外商户</span>
                  </div>
                     <div class="submenu" id="sub8"> 
                         <ul>
                         <li><a href="/extBooking/queryExtBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                         <li><a href="/extRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                          <li><a href="/extNotice/queryNoticePage.do" target="mainConent">&gt;&nbsp;&nbsp;公告管理</a></li>
                         <%
                         	if ("2".equals(loginUserVo.getUser_level())) {
                         %>
                         <li><a href="/extSetting/queryExtSettingList.do" target="mainConent">&gt;&nbsp;&nbsp;商户管理</a></li>
                         <%
                         	}
                         %>
                         </ul>
                     </div>
              </li>
              <li class="parent-li">
                 <div class="menutitle" onclick="SwitchMenu('sub9')">
                     <span class="span1">&nbsp;B2C管理</span>
                  </div>
                     <div class="submenu" id="sub9"> 
                         <ul>
                         <li><a href="/appbooking/queryAppBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                         <li><a href="/appRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                         <li><a href="/appNotice/queryNoticePage.do" target="mainConent">&gt;&nbsp;&nbsp;消息管理</a></li>
                         <%
                         	if ("2".equals(loginUserVo.getUser_level())) {
                         %>
                         <li><a href="/appUser/queryAppUserPage.do" target="mainConent">&gt;&nbsp;&nbsp;用户管理</a></li>
                        
                         <%
                                                 	}
                                                 %>
                         </ul>
                     </div>
              </li>
          
                <li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub11')";>
                     <span class="span1">&nbsp;资源管理</span>
                  </div>
                     <div class="submenu" id="sub11">                
                         <ul>
                            <%
                            	if ("2".equals(loginUserVo.getUser_level())) {
                            %>
                            <li><a href="/checkPrice/queryCheckPricePage.do" target="mainConent">&gt;&nbsp;&nbsp;对账管理<font color ="red">new</font></a></li>
                            <li><a href="/login/loginUserPage.do" target="mainConent">&gt;&nbsp;&nbsp;用户管理</a></li>
                            <!-- 
                            <li><a href="/data/dataMaintain.do" target="mainConent">&gt;&nbsp;&nbsp;数据维护</a></li>
                             -->
                            <li><a href="/account/queryAccountPage.do" target="mainConent">&gt;&nbsp;&nbsp;账号管理</a></li>
                            <li><a href="/mail/queryMailPage.do" target="mainConent">&gt;&nbsp;&nbsp;邮箱管理</a></li>
                            <%
                            	}
                            %>
                            <li><a href="/register/queryRegisterPage.do" target="mainConent">&gt;&nbsp;&nbsp;注册管理</a></li>
                            <li><a href="/ticketPrice/queryTicketPricePage.do" target="mainConent">&gt;&nbsp;&nbsp;票价管理</a></li>
                         <!--     <li><a href="/worker/queryWorkerPage.do" target="mainConent">&gt;&nbsp;&nbsp;资源管理</a></li>-->
                             <%
                             	if ("1".equals(loginUserVo.getUser_level())) {
                             %>
                             <li><a href="/login/loginUserPage.do" target="mainConent">&gt;&nbsp;&nbsp;用户管理</a></li>
                               <%
                               	}
                               %>
                            <%
                            	if ("2".equals(loginUserVo.getUser_level())) {
                            %>
                            <!--   <li><a href="/robotSet/queryRobotSetting.do" target="mainConent">&gt;&nbsp;&nbsp;机器管理</a></li>-->
                            <li><a href="/robotSetNew/gotoRobotSetPage.do" target="mainConent">&gt;&nbsp;&nbsp;机器管理<font color ="red">new</font></a></li>
                             <%
                             	}
                             %>
                             
                              <li><a href="/ip/gotoProxyIpPage.do" target="mainConent">&gt;&nbsp;&nbsp;代理IP管理</a></li>
                           </ul>
                     </div>
                </li>
                <%
                	if ("2".equals(loginUserVo.getUser_level())) {
                %>
                  <li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub12')";>
                     <span class="span1">&nbsp;系统设置</span>
                  </div>
                     <div class="submenu" id="sub12">                
                         <ul>
                        	<li><a href="/trainSetting/trainSettingPage.do" target="mainConent">&gt;&nbsp;&nbsp;系统管理</a></li>
                        <!-- 
                         <li><a href="/trainSystemSetting/getSystemSetting.do" target="mainConent">&gt;&nbsp;&nbsp;系统管理</a></li>
                         -->	
                        	  <li><a href="/qunarsetting/getQunarSetting.do" target="mainConent">&gt;&nbsp;&nbsp;去哪系统</a></li>
                        	   <li><a href="/elongSetting/getElongSetting.do" target="mainConent">&gt;&nbsp;&nbsp;代理系统</a></li>
                            <li><a href="/systemSetting/getSystemSetting.do" target="mainConent">&gt;&nbsp;&nbsp;19e系统</a></li>
                            <li><a href="/innerSystemSetting/getSystemSetting.do" target="mainConent">&gt;&nbsp;&nbsp;内嵌系统</a></li>
                            <li><a href="/extSystemSetting/getSystemSetting.do" target="mainConent">&gt;&nbsp;&nbsp;商户系统</a></li>
                            <li><a href="/appSystemSetting/getSystemSetting.do" target="mainConent">&gt;&nbsp;&nbsp;B2C系统</a></li>
                           
                        </ul>
                     </div>
                </li>
                 <%
                 	}
                 %>
                
            <%
                            	}
                            	if ("0".equals(loginUserVo.getUser_level())) {
                            %>
                  <li class="parent-li">
                         <span class="span3">系统管理</span>
                         <ul>
                                <li><a href="/booking/queryBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                                <li><a href="/refundTicket/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                                <li><a href="/acquire/queryAcquirePage.do" target="mainConent">&gt;&nbsp;&nbsp;出票管理</a></li>
                                <!-- <li><a href="/orderStat/queryOrderStatPage.do" target="mainConent">&gt;&nbsp;&nbsp;出票统计</a></li> -->
                                 
                              <!--   <li><a href="/hcStat/queryHcStatPage.do" target="mainConent">&gt;&nbsp;&nbsp;出票统计</a></li>   -->
                                <%
                                	if ("0".equals(loginUserVo.getUser_level())) {
                                %>
                                
                             <li><a href="/allComplain/queryComplainPage.do" target="mainConent">&gt;&nbsp;&nbsp;投诉管理</a></li> 
                                <li><a href="/login/loginUserPage.do" target="mainConent">&gt;&nbsp;&nbsp;用户管理</a></li>
                                
                                <%
                                                                	}
                                                                %>
                                <li><a href="/file/queryFileList.do" target="mainConent">&gt;&nbsp;&nbsp;退款查询</a></li>
                                <li><a href="/acquire/queryAcquireFailPage.do" target="mainConent">&gt;&nbsp;&nbsp;失败订单</a></li>
                                 
                         </ul>
                   </li>
                   

             <li class="parent-li">
             	<div class="menutitle" onclick="SwitchMenu('sub1')">
               		<span class="span1">去哪管理</span>
               	</div>
               	<div class="submenu" id="sub1"> 
                    <ul>
                    	<li><a href="/qunarbook/queryAllBookList.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                        <li><a href="/qunarrefund/queryRefundTicketPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                    </ul>
                </div>
             </li>
                
            <!--         
			<li class="parent-li">
            	<div class="menutitle" onclick="SwitchMenu('sub2')">
               		<span class="span1">cmpay管理</span>
               	</div>
               	<div class="submenu" id="sub2">
                	<ul>
                		<li><a href="/cmbooking/queryCmBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                		<li><a href="/cMRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                		<li><a href="/cmpayInsurance/queryInsurancePage.do" target="mainConent">&gt;&nbsp;&nbsp;保险管理</a></li>
               			<li><a href="/cMNotice/queryNoticePage.do" target="mainConent">&gt;&nbsp;&nbsp;公告管理</a></li>
                  		<li><a href="/cMComplain/queryComplainPage.do" target="mainConent">&gt;&nbsp;&nbsp;投诉管理</a></li>
                  	</ul>
                 </div>
              </li>
           
			<li class="parent-li">
            	<div class="menutitle" onclick="SwitchMenu('sub3')">
               		<span class="span1">19pay管理</span>
               	</div>
               	<div class="submenu" id="sub3">
                    <ul>
                  		<li><a href="/yjpaybooking/queryYjPayBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                        <li><a href="/yjPayRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                        <li><a href="/yjPayNotice/queryNoticePage.do" target="mainConent">&gt;&nbsp;&nbsp;公告管理</a></li>
                      <li><a href="/yjPayComplain/queryComplainPage.do" target="mainConent">&gt;&nbsp;&nbsp;投诉管理</a></li>
                        <li><a href="/yjPayReturnNotify/queryReturnNotifyPage.do" target="mainConent">&gt;&nbsp;&nbsp;出票结果</a></li>
                  	</ul>
                </div>
			</li>
			
			<li class="parent-li">
            	<div class="menutitle" onclick="SwitchMenu('sub4')">
               		<span class="span1">建行管理</span>
               	</div>
               	<span class="submenu" id="sub4">
                    <ul>
                  		 <li><a href="/cbcpayBooking/queryCbcpayBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                         <li><a href="/cbcpayRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                         <li><a href="/cbcpayNotice/queryNoticePage.do" target="mainConent">&gt;&nbsp;&nbsp;公告管理</a></li>
                        <li><a href="/cbcpayComplain/queryComplainPage.do" target="mainConent">&gt;&nbsp;&nbsp;投诉管理</a></li> 
                  	</ul>
                </span>
			</li>    -->
			<li class="parent-li">
            	<div class="menutitle" onclick="SwitchMenu('sub5')">
               		<span class="span1">对外商户</span>
               	</div>
               	<div class="submenu" id="sub5">
                    <ul>
                        <li><a href="/extBooking/queryExtBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                        <li><a href="/extRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                  	</ul>
                </div>
			</li>
			<li class="parent-li">
            	<div class="menutitle" onclick="SwitchMenu('sub6')">
               		<span class="span1">B2C管理</span>
               	</div>
               	<div class="submenu" id="sub6">
                    <ul>
                         <li><a href="/appbooking/queryAppBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                         <li><a href="/appRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                      <!--     <li><a href="/appComplain/queryComplainPage.do" target="mainConent">&gt;&nbsp;&nbsp;投诉管理</a></li>-->
                         <li><a href="/appNotice/queryNoticePage.do" target="mainConent">&gt;&nbsp;&nbsp;消息管理</a></li>
                  	</ul>
                </div>
			</li>
			<li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub7')";>
                     <span class="span1">&nbsp;艺龙管理</span>
                  </div>
                     <div class="submenu" id="sub7"> 
                          <ul>
                               <li><a href="/elongBook/queryAllBookList.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                               <li><a href="/elongRefund/queryRefundTicketPage.do" target="mainConent">&gt;&nbsp;&nbsp;退票管理</a></li>
                           </ul>
                     </div>
            </li>
                  <%
                  	}
                  	if ("1.1".equals(loginUserVo.getUser_level())) {
                  %>
                  <li class="parent-li">
                       <span class="span3">订单管理</span>
                       <ul>
                         <li><a href="/booking/queryBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;预订管理</a></li>
                         <li><a href="/hcStat/queryHcStatPage.do" target="mainConent">&gt;&nbsp;&nbsp;出票统计</a></li>
                         <li><a href="/userStat/showAllPrivateUserStatBar.do" target="mainConent">&gt;&nbsp;&nbsp;用户统计</a></li>
                       </ul>
                   </li>
                   <li class="parent-li">
                       <span class="span3">系统管理</span>
                       <ul>
                         <li><a href="/notice/queryNoticePage.do" target="mainConent">&gt;&nbsp;&nbsp;公告管理</a></li>
                         <li><a href="/joinUs/queryJoinUsPage.do" target="mainConent">&gt;&nbsp;&nbsp;加盟列表</a></li>
                         <li><a href="/allComplain/queryComplainPage.do" target="mainConent">&gt;&nbsp;&nbsp;投诉管理</a></li>
                       <li><a href="/login/loginUserPage.do" target="mainConent">&gt;&nbsp;&nbsp;登录用户管理</a></li>
                         
                       </ul>
                   </li>
                  <%
                  	}
                  %>
                  <%
                  	if ("1.2".equals(loginUserVo.getUser_level())) {
                  %>
                            
                   <li class="parent-li">
                       <span class="span3">考核管理</span>
                       <ul>
                         <li><a href="/acquire/queryAcquirePage.do" target="mainConent">&gt;&nbsp;&nbsp;出票管理</a></li>
                         <li><a href="/opterStat/queryOpterStatPage.do" target="mainConent">&gt;&nbsp;&nbsp;考核统计</a></li>
                       </ul>
                   </li>
                   <li class="parent-li">
                       <span class="span3">预订管理</span>
                       <ul>
						 <li><a href="/booking/queryBookPage.do" target="mainConent">&gt;&nbsp;&nbsp;19e预订</a></li>                       
						</ul>
                   </li>
                   <li class="parent-li">
                       <span class="span3">退票管理</span>
                       <ul>
                         <li><a href="/refundTicket/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;19e退票</a></li>
                       <!-- <li><a href="/cMRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;cmpay退票</a></li>
                        <li><a href="/cbcpayRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;建行退票</a></li>
                         <li><a href="/yjPayRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;19pay退票</a></li>  --> 
                         <li><a href="/qunarrefund/queryRefundTicketPage.do" target="mainConent">&gt;&nbsp;&nbsp;去哪退票</a></li>
                         <li><a href="/appRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;B2C退票</a></li>
                         <li><a href="/extRefund/queryRefundPage.do" target="mainConent">&gt;&nbsp;&nbsp;商户退票</a></li>
                         <li><a href="/elongRefund/queryRefundTicketPage.do" target="mainConent">&gt;&nbsp;&nbsp;艺龙退票</a></li>
                       </ul>
                   </li>
                 <%
                 	}
                 %>
                 <%
                 	String username = loginUserVo.getUser_name();
                 	if (username.equals("zhouyan") || username.equals("wangxiuxiu")
                 			|| username.equals("xuli") || username.equals("guona")
                 			|| username.equals("liuyi")) {
                 %> 
				<!-- 
                 <li class="parent-li">
                  <div class="menutitle" onclick="SwitchMenu('sub19')";>
                     <span class="span1">&nbsp;对账管理</span>
                  </div>
                     <div class="submenu" id="sub19"> 
                          <ul>
                           		<li><a href="/balance/queryBalanceAccountList.do" target="mainConent" >&gt;&nbsp;&nbsp;支付宝表</a></li>
                               <li><a href="/balance/queryfileList.do" target="mainConent" >&gt;&nbsp;&nbsp;订单匹配</a></li>
                              </ul>
                     </div>
            	</li>
				 -->
            	<%
            		}
            	%>
            	 <%
            	 	if ("77".equals(loginUserVo.getUser_level())
            	 			|| "78".equals(loginUserVo.getUser_level())) {
            	 %>
            	<li class="parent-li">
                       <span class="span3">常用功能</span>
                       <ul>
                               <li><a href="/manual/queryManualPage.do" target="mainConent">&gt;&nbsp;&nbsp;出票管理</a></li>
                       </ul>
                </li>
 				<%
 					}
 				%>	
 				
 				 <%
	 				 				 	if ("p1".equals(loginUserVo.getUser_level())
	 				 				 			|| "p2".equals(loginUserVo.getUser_level())) {
	 				 				 %>
            	<li class="parent-li">
                       <span class="span3">常用功能</span>
                       <ul>
                               <li><a href="/psOrder/queryPsorderPage.do" target="mainConent">&gt;&nbsp;&nbsp;出票管理</a></li>
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
