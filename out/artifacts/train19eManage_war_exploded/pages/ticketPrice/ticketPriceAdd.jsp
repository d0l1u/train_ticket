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
		    	if($.trim($("#checi").val())==""){
		    		$("#checi").focus();
		    			alert("车次不能为空！");
		    			return;
		    	}
		    	if($.trim($("#fazhan").val())==""){
		    		$("#fazhan").focus();
		    			alert("发站不能为空！");
		    			return;
		    	}
		    	if($.trim($("#daozhan").val())==""){
		    		$("#daozhan").focus();
		    			alert("到站不能为空！");
		    			return;
		    	}
		    	if($.trim($("#yz").val())==""){
		    		$("#yz").focus();
		    			alert("硬座不能为空！");
		    			return;
		    	}
		    	if($.trim($("#rz").val())==""){
		    		$("#rz").focus();
		    			alert("软座不能为空！");
		    			return;
		    	}
		    	if($.trim($("#yws").val())==""){
		    		$("#yws").focus();
		    			alert("硬卧上不能为空！");
		    			return;
		    	}
		    	if($.trim($("#ywz").val())==""){
		    		$("#ywz").focus();
		    			alert("硬卧中不能为空！");
		    			return;
		    	}
		    	if($.trim($("#ywx").val())==""){
		    		$("#ywx").focus();
		    			alert("硬卧下不能为空！");
		    			return;
		    	}
		    	if($.trim($("#rws").val())==""){
		    		$("#rws").focus();
		    			alert("软卧上不能为空！");
		    			return;
		    	}
		    	if($.trim($("#rwx").val())==""){
		    		$("#rwx").focus();
		    			alert("软卧下不能为空！");
		    			return;
		    	}
		    	if($.trim($("#rz1").val())==""){
		    		$("#rz1").focus();
		    			alert("一等座不能为空！");
		    			return;
		    	}if($.trim($("#rz2").val())==""){
		    		$("#rz2").focus();
	    			alert("二等座不能为空！");
	    			return;
	    		}
				$("#addForm").submit();
			}
			function onblurCheci(){
				$("#cc_info").text("");
				var checi = $.trim( $("#checi").val() );
				if(checi==""){
					$("#cc_info").text("车次不能为空").css("color","#f00");
				}
			}
			function onblurFazhan(){
				$("#fz_info").text("");
				var fazhan = $.trim( $("#fazhan").val() );
				if(fazhan==""){
					$("#fz_info").text("发站不能为空").css("color","#f00");
				}
			}
			function onblurDaozhan(){
				var checi = $.trim( $("#checi").val() );
				var fazhan = $.trim( $("#fazhan").val() );
				var daozhan = $.trim( $("#daozhan").val() );
				fazhan = encodeURI(fazhan);
				fazhan = encodeURI(fazhan);
				daozhan = encodeURI(daozhan);
				daozhan = encodeURI(daozhan);
				$("#checi_info").text("");
				if(daozhan==""){
					$("#checi_info").text("到站不能为空").css("color","#f00");
				}else{
					var url = "/ticketPrice/queryTicketPriceAdd.do?cc="+checi+"&fz="+fazhan+"&dz="+daozhan;
					$.get(url,function(data){
						if(data == "no"){
							$("#checi_info").text("该车次已经存在该发站和到站，请重新输入").css("color","#f00");
							$("#btnModify").attr("disabled", true);
						}
					});
				}
			}
			//硬座
		    function onblurYz(){
		    	var yz_money = $.trim($("#yz").val());
				$("#yz_info").text("");
				var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if(yz_money==""){
					$("#yz_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( (reg.test(yz_money))==false ){
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
				if(rz_money==""){
					$("#rz_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( (reg.test(rz_money))==false ){
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
				if(yws_money==""){
					$("#yws_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( (reg.test(yws_money))==false ){
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
				if(ywz_money==""){
					$("#ywz_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( ywz_money!="" && (reg.test(ywz_money))==false ){
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
				if(money==""){
					$("#ywx_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if((reg.test(money))==false ){
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
				if(money==""){
					$("#rws_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( (reg.test(money))==false ){
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
				if(yz_money==""){
					$("#rwx_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( yz_money!="" && (reg.test(yz_money))==false ){
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
				if(yz_money==""){
					$("#rz1_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if((reg.test(yz_money))==false ){
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
				if(yz_money==""){
					$("#rz2_info").text("不能为空，若无此席位，请输入0").css("color","#f00");
				}else if( yz_money!="" && (reg.test(yz_money))==false ){
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
	<form action="/ticketPrice/addTicketPrice.do" method="post" name="addForm" id="addForm">
        <br/><br/><br/><br/>
   		<input type="hidden" name="cc" value="${cc }"/>
        <input type="hidden" name="fz" value="${fz }"/>
        <input type="hidden" name="dz" value="${dz }"/>
        <ul class="order_num oz">
        	<li>车&nbsp;&nbsp;次：&nbsp;&nbsp;<input type="text" name="checi" id="checi"  onblur="onblurCheci()"/><span id="cc_info"></span></li>
        	<li>发&nbsp;&nbsp;站：&nbsp;&nbsp;<input type="text" name="fazhan" id="fazhan"  onblur="onblurFazhan()"/><span id="fz_info"></span></li>
        	<li>到&nbsp;&nbsp;站：&nbsp;&nbsp;<input type="text" name="daozhan" id="daozhan" onblur="onblurDaozhan()"/><span id="checi_info"></span></li>
        	<li>硬&nbsp;&nbsp;座：&nbsp;&nbsp;<input type="text" name="yz" id="yz" onblur="onblurYz()"/><span id="yz_info"></span></li>
        	<li>软&nbsp;&nbsp;座：&nbsp;&nbsp;<input type="text" name="rz" id="rz" onblur="onblurRz()"/><span id="rz_info"></span></li>
        	<li>硬卧上：&nbsp;&nbsp;<input type="text" name="yws" id="yws" onblur="onblurYws()"/><span id="yws_info"></span></li>
        	<li>硬卧中：&nbsp;&nbsp;<input type="text" name="ywz" id="ywz" onblur="onblurYwz()"/><span id="ywz_info"></span></li>
        	<li>硬卧下：&nbsp;&nbsp;<input type="text" name="ywx" id="ywx" onblur="onblurYwx()"/><span id="ywx_info"></span></li>
        	<li>软卧上：&nbsp;&nbsp;<input type="text" name="rws" id="rws" onblur="onblurRws()"/><span id="rws_info"></span></li>
        	<li>软卧下：&nbsp;&nbsp;<input type="text" name="rwx" id="rwx" onblur="onblurRwx()"/><span id="rwx_info"></span></li>
        	<li>一等座：&nbsp;&nbsp;<input type="text" name="rz1" id="rz1" onblur="onblurRz1()"/><span id="rz1_info"></span></li>
        	<li>二等座：&nbsp;&nbsp;<input type="text" name="rz2" id="rz2" onblur="onblurRz2()"/><span id="rz2_info"></span></li>
        	<li>商务座：&nbsp;&nbsp;<input type="text" name="swz" id="swz" onblur="onblurSwz()"/><span id="swz_info"></span></li>
        	<li>特等座：&nbsp;&nbsp;<input type="text" name="tdz" id="tdz" onblur="onblurTdz()"/><span id="tdz_info"></span></li>
        	<li>高级软卧上：&nbsp;&nbsp;<input type="text" name="gws" id="gws" onblur="onblurGws()"/><span id="gws_info"></span></li>
        	<li>高级软卧下：&nbsp;&nbsp;<input type="text" name="gwx" id="gwx" onblur="onblurGwx()"/><span id="gwx_info"></span></li>
        	<li>动卧上：&nbsp;&nbsp;<input type="text" name="dws" id="dws"  onblur="onblurDws()"/><span id="dws_info"></span></li>
            <li>动卧下：&nbsp;&nbsp;<input type="text" name="dwx" id="dwx"  onblur="onblurDwx()"/><span id="dwx_info"></span></li>
        </ul>	
         <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/>
         <input type="button" value="返 回" class="btn btn_normal" onclick="javascript:history.back(-1);" /></p>
        </form>
	</div>
	</body>
</html>