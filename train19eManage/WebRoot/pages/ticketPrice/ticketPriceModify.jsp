<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>票价修改页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript">
		    function submitForm(){
				$("#updateForm").submit();
			}
			//硬座
		    function onblurYz(){
		    	var yz_money = $.trim($("#yz").val());
				$("#yz_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if( (reg.test(yz_money))==false ){
					$("#yz_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
		  	//软座
		    function onblurRz(){
		    	var rz_money = $.trim($("#rz").val());
				$("#rz_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if( (reg.test(rz_money))==false ){
					$("#rz_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
			//硬卧上
		    function onblurYws(){
		    	var yws_money = $.trim($("#yws").val());
				$("#yws_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if( (reg.test(yws_money))==false ){
					$("#yws_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
			//硬卧中
		    function onblurYwz(){
		    	var ywz_money = $.trim($("#ywz").val());
				$("#ywz_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if( (reg.test(ywz_money))==false ){
					$("#ywz_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
			//硬卧下
		    function onblurYwx(){
		    	var money = $.trim($("#ywx").val());
				$("#ywx_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if( (reg.test(money))==false ){
					$("#ywx_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
			//软卧上
		    function onblurRws(){
		    	var money = $.trim($("#rws").val());
				$("#rws_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if( (reg.test(money))==false ){
					$("#rws_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
		    //软卧下
		    function onblurRwx(){
		    	var yz_money = $.trim($("#rwx").val());
				$("#rwx_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if( (reg.test(yz_money))==false ){
					$("#rwx_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
			//一等座
		    function onblurRz1(){
		    	var yz_money = $.trim($("#rz1").val());
				$("#rz1_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if( (reg.test(yz_money))==false ){
					$("#rz1_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
			//二等座
		    function onblurRz2(){
		    	var yz_money = $.trim($("#rz2").val());
				$("#rz2_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if( (reg.test(yz_money))==false ){
					$("#rz2_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
			
		    //商务座
		    function onblurSwz(){
		    	var yz_money = $.trim($("#swz").val());
				$("#swz_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if(yz_money==""){
					$("#swz_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( yz_money!="" && (reg.test(yz_money))==false ){
					$("#swz_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
		  
		    //特等座
		    function onblurTdz(){
		    	var yz_money = $.trim($("#tdz").val());
				$("#tdz_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if(yz_money==""){
					$("#tdz_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( yz_money!="" && (reg.test(yz_money))==false ){
					$("#tdz_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
		    //高级软卧上座
		    function onblurGws(){
		    	var yz_money = $.trim($("#gws").val());
				$("#gws_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if(yz_money==""){
					$("#gws_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( yz_money!="" && (reg.test(yz_money))==false ){
					$("#gws_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
		    //高级软卧下
		    function onblurGwx(){
		    	var yz_money = $.trim($("#gwx").val());
				$("#gwx_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if(yz_money==""){
					$("#gwx_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( yz_money!="" && (reg.test(yz_money))==false ){
					$("#gwx_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
		    //动卧上
		    function onblurDws(){
		    	var yz_money = $.trim($("#dws").val());
				$("#dws_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if(yz_money==""){
					$("#dws_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( yz_money!="" && (reg.test(yz_money))==false ){
					$("#dws_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
		    //动卧下
		    function onblurDwx(){
		    	var yz_money = $.trim($("#dwx").val());
				$("#dwx_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if(yz_moxainey==""){
					$("#dwx_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( yz_money!="" && (reg.test(yz_money))==false ){
					$("#dwx_info").text("格式不正确，请输入有效的金额").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#btnModify").attr("disabled", false);
				} 
			}
	    </script>
	</head>
	<body>
		<div class="book_manage account_manage oz">
	<form action="/ticketPrice/updateTicketPrice.do" method="post" name="updateForm" id="updateForm">
        <br/><br/><br/><br/>
        <input type="hidden" name="xh" value="${ticket.xh }"/>
        <input type="hidden" name="cc" value="${cc }"/>
        <input type="hidden" name="fz" value="${fz }"/>
        <input type="hidden" name="dz" value="${dz }"/>
        <ul class="order_num oz">
        	<li>车&nbsp;&nbsp;次：&nbsp;&nbsp;<input type="text" name="new_cc" id="new_cc" value="${ticket.cc }" onblur=""/><span id="new_cc_info"></span></li>
        	<li>发&nbsp;&nbsp;站：&nbsp;&nbsp;<input type="text" name="new_fz" id="new_fz" value="${ticket.fz }" onblur=""/><span id="new_fz_info"></span></li>
        	<li>到&nbsp;&nbsp;站：&nbsp;&nbsp;<input type="text" name="new_dz" id="new_dz" value="${ticket.dz }" onblur=""/><span id="new_dz_info"></span></li>
        	<li>硬&nbsp;&nbsp;座：&nbsp;&nbsp;<input type="text" name="yz" id="yz" value="${ticket.yz }" onblur="onblurYz()"/><span id="yz_info"></span></li>
        	<li>软&nbsp;&nbsp;座：&nbsp;&nbsp;<input type="text" name="rz" id="rz" value="${ticket.rz }" onblur="onblurRz()"/><span id="rz_info"></span></li>
        	<li>硬卧上：&nbsp;&nbsp;<input type="text" name="yws" id="yws" value="${ticket.yws }" onblur="onblurYws()"/><span id="yws_info"></span></li>
        	<li>硬卧中：&nbsp;&nbsp;<input type="text" name="ywz" id="ywz" value="${ticket.ywz }" onblur="onblurYwz()"/><span id="ywz_info"></span></li>
        	<li>硬卧下：&nbsp;&nbsp;<input type="text" name="ywx" id="ywx" value="${ticket.ywx }" onblur="onblurYwx()"/><span id="ywx_info"></span></li>
        	<li>软卧上：&nbsp;&nbsp;<input type="text" name="rws" id="rws" value="${ticket.rws }" onblur="onblurRws()"/><span id="rws_info"></span></li>
        	<li>软卧下：&nbsp;&nbsp;<input type="text" name="rwx" id="rwx" value="${ticket.rwx }" onblur="onblurRwx()"/><span id="rwx_info"></span></li>
        	<li>一等座：&nbsp;&nbsp;<input type="text" name="rz1" id="rz1" value="${ticket.rz1 }" onblur="onblurRz1()"/><span id="rz1_info"></span></li>
        	<li>二等座：&nbsp;&nbsp;<input type="text" name="rz2" id="rz2" value="${ticket.rz2 }" onblur="onblurRz2()"/><span id="rz2_info"></span></li>
        	<li>商务座：&nbsp;&nbsp;<input type="text" name="swz" id="swz" value="${ticket.swz }"  onblur="onblurSwz()"/><span id="swz_info"></span></li>
            <li>特等座：&nbsp;&nbsp;<input type="text" name="tdz" id="tdz" value="${ticket.tdz }"  onblur="onblurTdz()"/><span id="tdz_info"></span></li>
        <li>高级软卧上：&nbsp;&nbsp;<input type="text" name="gws" id="gws" value="${ticket.gws }"  onblur="onblurGws()"/><span id="gws_info"></span></li>
        <li>高级软卧下：&nbsp;&nbsp;<input type="text" name="gwx" id="gwx" value="${ticket.gwx }"  onblur="onblurGwx()"/><span id="gwx_info"></span></li>
         <li>动卧上：&nbsp;&nbsp;<input type="text" name="dws" id="dws" value="${ticket.dws }"  onblur="onblurDws()"/><span id="dws_info"></span></li>
         <li>动卧下：&nbsp;&nbsp;<input type="text" name="dwx" id="dwx" value="${ticket.dwx }"  onblur="onblurDwx()"/><span id="dwx_info"></span></li>
        </ul>	
         <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/>
         <input type="button" value="返 回" class="btn btn_normal" onclick="javascript:history.back(-1);" /></p>
        </form>
	</div>
	</body>
</html>