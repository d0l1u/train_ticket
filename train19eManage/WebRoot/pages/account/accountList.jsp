<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>账号管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
			<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
	</head>
	<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	
	function submit(url){
		if(confirm("是否提交？")){
			location.href = url; 
		}
	}

	function xiugai(acc_id){
		var url="/account/updatePreAccount.do?acc_id="+acc_id;
		showlayer('修改 ',url,'1200px','800px')
		}
	
	
	
	function selectCity(){
		
		var url = "/account/queryGetCity.do?provinceid="+$("#at_province_id").val();
		
		$.get(url,function(data,status){
		    $("#at_city_id").empty(); 
	    	$("#at_city_id").append("<option value='' selected='selected'>请选择</option>");
		    var obj = eval(data);
			$(obj).each(function(index){
				var val = obj[index];
				$("#at_city_id").append("<option value='"+val.area_no+"'>"+val.area_name+"</option>");
			});
		  });
		
	}
	function shifang(acc_id,acc_username){
		if(confirm("确定释放账号吗？")){
		//	var url = '/account/updateShifangAccount.do';
			var  url = "/account/updateShifangAccount.do?version="+new Date();
			$.post(url,{acc_status:33,acc_id:acc_id,acc_username:acc_username},function(data){
				if(data=="yes"){

					$("form:first").attr("action","/account/queryAccountList.do?pageIndex="+<%=pageIndex%>);
					$("form:first").submit();
					//window.location="/account/queryAccountList.do?pageIndex="+<%=pageIndex%>;
				}else{
					alert("释放账号失败");
				}
				});
			return true;
		}
		return false;
		}

	

	function qiyong(acc_id,acc_username){
		//var url = '/account/updateQiYongAccount.do';
		var url = "/account/updateQiYongAccount.do?version="+new Date();
		$.post(url,{acc_status:33,acc_id:acc_id,acc_username:acc_username},function(data){
			if(data=="yes"){
				$("form:first").attr("action","/account/queryAccountList.do?pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
				//window.location="/account/queryAccountList.do?pageIndex="+<%=pageIndex%>;
			}else{
				alert("启用账号失败");
			}
		});
	}
	
	function shanchu(acc_id,acc_username){
		if (confirm("确定要删除吗？")){				
			//var url = '/account/updateDeleteAccount.do';
			var url = "/account/updateDeleteAccount.do?version="+new Date();
			$.post(url,{acc_id:acc_id,acc_username:acc_username},function(data){
				if(data=="yes"){
					$("form:first").attr("action","/account/queryAccountList.do?pageIndex="+<%=pageIndex%>);
					$("form:first").submit();
					//window.location="/account/queryAccountList.do?pageIndex="+<%=pageIndex%>;
				}else{
					alert("删除账号失败");
				}
			});		
		}else{		
			return false;
		}
	}

	function addRegisterAccount(){
		var register_num= $("#register_num").val();
		var choose_source= $("#choose_source").val();
		var choose_channel= $("#choose_channel").val();
				
		//if(register_num == "" || choose_source == "" || choose_channel== "")
		//alert(choose_channel);
		if(register_num=="" || choose_source=="000" || choose_channel=="000" )
		{
			alert("请填写完整！");
			return false;
		}
		if(parseInt(register_num) < 1 || parseInt(register_num) > 500){
			alert("请输入1-500间的数据");
			return false;
		}
		window.location="/account/queryRegister.do?register_num=" + register_num + "&choose_source=" + choose_source+"&choose_channel=" + choose_channel;
	}
	
	function startRegisterAccount(){
		var register_num= $("#start_register_num").val();
		var choose_source= $("#start_choose_source").val();
		var choose_channel= $("#start_choose_channel").val();
				
		//if(register_num == "" || choose_source == "" || choose_channel== "")
		//alert(choose_channel);
		if(register_num=="" || choose_source=="000" || choose_channel=="000" )
		{
			alert("请填写完整！");
			return false;
		}
		if(parseInt(register_num) < 1 || parseInt(register_num) > 500){
			alert("请输入1-500间的数据");
			return false;
		}
		window.location="/account/startQueryRegister.do?register_num=" + register_num + "&choose_source=" + choose_source+"&choose_channel=" + choose_channel;
	}


	  function div() {
	    var cheLength = document.getElementsByName("acc_status");
	    for(var i=0; i<cheLength.length; i++) {
	       //账号停用展示停用原因
	       if(cheLength[i].checked && cheLength[i].value == '22'){
	          document.getElementById('divcon').style.display = "block";
	 		 // return;
	       }
	       else if(!cheLength[i].checked && cheLength[i].value == '22'){
	          document.getElementById('divcon').style.display = "none";
	       }
	    }
	  }
	  
	  function divs(){
		  var cheLength = document.getElementsByName("acc_status");
		  for(var i=0; i<cheLength.length; i++){
			  if(cheLength[i].checked && cheLength[i].value == '33'){
				  document.getElementById('divs').style.display = "none";
		 		  return;
			  }
			  else if(!cheLength[i].checked && cheLength[i].value == '33'){
		          document.getElementById('divs').style.display = "none";
		      }
		  }
	  }


	//全选操作
	  function checkChnRetRuleAll(){
	      var acc_id = document.getElementsByName("acc_id");
	      var tag1 = document.getElementById("tag1").value;
	      if(tag1 == 0 ){
	         for(var i = 0 ; i < acc_id.length ; i++){
	             acc_id[i].checked = true ;
	         }
	         document.getElementById("tag1").value = 1;
	      }else if(tag1 == 1){
	         for(var i = 0 ; i < acc_id.length ; i++){
	             acc_id[i].checked = false ;
	         }
	         document.getElementById("tag1").value = 0;
	      }
	  }
	  
	  
	  function selectAllContact(){
		var checklist = document.getElementsByName("contact_num");
		if(document.getElementById("controlAllContact").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
	  
	  
	//批量操作启用,停用和删除
	function batchStop(type){
		var batchStopReason= $("#batchStopReason").val();
		var account_source= $("#account_source").val();
		//alert($("#batchStopReason").val());
	  	document.getElementById("batchStoptype").value = type; 
	  	var aa = document.getElementsByName("acc_id");
	  	var ii = 0;
	  	for (var i=0; i<aa.length; i++){
		  	if(aa[i].checked==true){
			  	ii++;
			}
		}
		if(ii==0){
			alert("请选中一条账号!");
			return false;
		}
		var batchStopnum = aa.length;
		var info = "";
		if(type == 4){
			info = "确认将所选的"+ii+"条账号停用吗?";
			if(confirm(info)){
				document.queryFrm.action="/account/updateAbatch.do?batchStopReason="+batchStopReason+"&account_source"+ account_source +"&pageIndex="+<%=pageIndex%> +"&version="+new Date();
				document.queryFrm.submit();
				
			}
		}
	}
	function selectAllChannel(){
		var checklist = document.getElementsByName("channel");
		if(document.getElementById("controlAllChannel").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
	
		
	function exportExcel() {
			$("form:first").attr("action","/account/exportexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/account/queryAccountList.do");
		}
</script>
	<body onload="div();divs();"><div></div>
		<div class="book_manage oz">
			<form  name="queryFrm" action="/account/queryAccountList.do" method="post">
				<ul class="ser oz">
					<li> 
						省 ：
						<select name="at_province_id" id="at_province_id" style="width:170px;"
							onchange="selectCity();">
							<c:forEach items="${province }" var="p">
								<option value="${p.area_no }"
									<c:if test="${at_province_id eq p.area_no }">selected="selected"</c:if>>
									${p.area_name}
								</option>
							</c:forEach>
						</select>
					</li>
					<li>
						市：
						<select name="at_city_id" id="at_city_id" style="width:170px;">
							<c:choose>
								<c:when test="${empty city }">
									<option value="" selected="selected">
										请选择
									</option>
								</c:when>
								<c:otherwise>
									<c:if test="${empty at_city_id}">
										<option value="" selected="selected">
											请选择
										</option>
									</c:if>
									<c:forEach items="${city }" var="p">
										<option value="${p.area_no }"
											<c:if test="${at_city_id eq p.area_no }">selected="selected"</c:if>>
											${p.area_name}
										</option>

									</c:forEach>
								</c:otherwise>
							</c:choose>
						</select>
					</li>
					<li>
						账号名称：
						<input type="text" class="text" name="acc_username" value="${acc_username }" />
					</li>
				</ul>
				<ul class="ser oz">
					<li>
							开始时间：
							<input type="text" class="text" name="begin_info_time"
								readonly="readonly" value="${begin_info_time }" style="width:146px;"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						</li>
						<li>
							结束时间：
							<input type="text" class="text" name="end_info_time"
								readonly="readonly" value="${end_info_time }" style="width:140px;"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
						<li>
							邮箱查询：
							<input type="text" class="text" name="acc_mail" value="${acc_mail }" />
						</li>
						
				</ul>
				<ul class="order_num oz" style="margin-top: 4px;">
					
					<li>
						<label style="float:left;"> 渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
						<table style="width:870px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
							<input type="checkbox" onclick="selectAllChannel()" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel"/><label for="controlAllChannel">&nbsp;全部&nbsp;</label>
							<c:forEach items="${channel_types }" var="s" varStatus="index">
									<input type="checkbox" id="channel${index.count }"
										name="channel" value="${s.key }"
										<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
									<label for="channel${index.count }">
										${s.value }
									</label>
							</c:forEach>
							
						</td></tr></table>
					<!-- 
						渠道：
						<c:forEach items="${channel_types }" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="channel${index.count }"
									name="channel" value="${s.key }"
									<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
								<label for="channel${index.count }">
									${s.value }
								</label>
							</li>
						</c:forEach>
					 -->
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 5px;">

					<li>
						状态：</li>
					<!-- <input type="checkbox"><span>全部</span></input> -->	
						<c:forEach items="${accountStatus }" var="s" varStatus="index">
						<c:choose>
							<c:when test="${s.key eq '22'}">
								<li>
									<input type="checkbox" id="acc_status${index.count }"
										name="acc_status" onclick="div()" value="${s.key }"
										<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
									<label for="acc_status${index.count }">
										${s.value }
									</label>
								</li>
							</c:when>
							<c:when test="${s.key eq '33'}">
								<li>
									<input type="checkbox" id="acc_status${index.count }"
										name="acc_status" onclick="divs()" value="${s.key }"
										<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
									<label for="acc_status${index.count }">
										${s.value }
									</label>
								</li>
							</c:when>
							<c:otherwise>
							<li>
								<input type="checkbox" id="acc_status${index.count }"
										name="acc_status"  value="${s.key }"
										<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
									<label for="acc_status${index.count }">
										${s.value }
									</label>
								</li>
							</c:otherwise>
						</c:choose>
						</c:forEach>
						<li>
							<input type="checkbox" id="active_time" name="active_time" value="active_time" value="1"
							<c:if test="${active_time ne '' }">checked="checked"</c:if> />
							<label for="active_time">
								已登录
							</label>
						</li>
					
				</ul>
				

				<div id="divcon" style="display:none">
					<ul class="order_num oz" style="margin-top: 5px;">

					<li>
						停用原因：</li>
						<c:forEach items="${stopReason}" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="stop_reason${index.count}" name="stop_reason"  value="${s.key}"
									<c:if test="${fn:contains(stop_reasonStr,s.key)}"> checked="checked"</c:if> />
								<label for="stop_reason${index.count}">${s.value}</label>
							</li>
						</c:forEach>	
					
				</ul>
				</div>
				
					<ul class="order_num oz" style="margin-top: 5px;">

					<li>
						来源：</li>
						<c:forEach items="${account_source}" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="account_source${index.count }" name="account_source" value="${s.key }"
									<c:if test="${fn:contains(sourceSrt, s.key ) }">checked="checked"</c:if> />
								<label for="account_source${index.count }">
									${s.value }
								</label>
							</li>
						</c:forEach>
					
				</ul>
					
					<ul class="order_num oz" style="margin-top: 10px;">

					<li>
						启用状态：&nbsp;&nbsp;</li>
							<li>
								<input type="checkbox" id="is_alive_1" name="is_alive_1" value="is_alive_1"
									<c:if test="${is_alive_1 ne '' }">checked="checked"</c:if> />
								<label for="is_alive_1">
									启用
								</label>
							</li>
							<li>
								<input type="checkbox" id="is_alive_0" name="is_alive_0" value="is_alive_0"
									<c:if test="${is_alive_0 ne '' }">checked="checked"</c:if> />
								<label for="is_alive">
									未启用
								</label>
							</li>
				</ul>
					<ul class="order_num oz" style="margin-top: 5px;">

						<li>
						邮箱类型：&nbsp;&nbsp;
						</li>
							<li>
								<input type="checkbox" id="mail_qita" name="mail_qita" value="mail_qita"
									<c:if test="${mail_qita ne '' }">checked="checked"</c:if> />
								<label for="mail_qita">
									其他
								</label>
							</li>
							<li>
								<input type="checkbox" id="mail_163" name="mail_163" value="mail_163"
									<c:if test="${mail_163 ne '' }">checked="checked"</c:if> />
								<label for="mail_163">
									163
								</label>
							</li>
							<li>
								<input type="checkbox" id="mail_19trip" name="mail_19trip" value="mail_19trip"
									<c:if test="${mail_19trip ne '' }">checked="checked"</c:if> />
								<label for="mail_19trip">
									19trip
								</label>
							</li>
							
				</ul>
				<ul class="order_num oz" style="margin-top: 5px;margin-bottom: 13px;">

					<li>
						联系人个数：</li>
							<li>
								<input type="checkbox" onclick="selectAllContact()" name="controlAllContact" style="controlAllContact" 
								id="controlAllContact"/>
								<label for="contact_num">
									全部
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="00"
									<c:if test="${fn:contains(contact_num , '00') }">checked="checked"</c:if> />
								<label for="contact_num">
									0个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="10"
									<c:if test="${fn:contains(contact_num , '10') }">checked="checked"</c:if> />
								<label for="contact_num">
									10个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="20"
									<c:if test="${fn:contains(contact_num , '20') }">checked="checked"</c:if> />
								<label for="contact_num">
									20个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="30"
									<c:if test="${fn:contains(contact_num , '30') }">checked="checked"</c:if> />
								<label for="contact_num">
									30个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="40"
									<c:if test="${fn:contains(contact_num , '40') }">checked="checked"</c:if> />
								<label for="contact_num">
									40个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="50"
									<c:if test="${fn:contains(contact_num , '50') }">checked="checked"</c:if> />
								<label for="contact_num">
									50个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="60"
									<c:if test="${fn:contains(contact_num , '60') }">checked="checked"</c:if> />
								<label for="contact_num">
									60个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="70"
									<c:if test="${fn:contains(contact_num , '70') }">checked="checked"</c:if> />
								<label for="contact_num">
									70个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="80"
									<c:if test="${fn:contains(contact_num , '80' )}">checked="checked"</c:if> />
								<label for="contact_num">
									80个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="90"
									<c:if test="${fn:contains(contact_num , '90' )}">checked="checked"</c:if> />
								<label for="contact_num">
									90个
								</label>
							</li>
							<li>
								<input type="checkbox" id="contact_num" name="contact_num" value="100"
									<c:if test="${fn:contains(contact_num , '100') }">checked="checked"</c:if> />
								<label for="contact_num">
									100个
								</label>
							</li>
				</ul>
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" />
					<input type="button" value="添 加"
						onclick="location.href = '/account/addPreAccount.do'" class="btn" />
					<%		if ("2".equals(loginUserVo.getUser_level()) ) {%>
					<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
					<%} %>
				</p>
				<div class="pub_debook_mes  oz mb10_all" style="margin-left: 800px;margin-top: -50px;margin-bottom: 20px;">
					<span style="font-size:14px;">申请增加<input value="" id="register_num" name="register_num" style="color:#f60;width:20px;"/>个账号，
					<select name="choose_source" style="width: 72px;" id="choose_source">
							<option value="000">选择来源</option>
							<option value="1">19e</option>
							<option value="2">批量导入</option>
							<option value="3">人工添加</option>
							<option value="4">其他</option>
					</select>
					<select name="choose_channel" style="width: 72px;" id="choose_channel">
							<option value="000">选择渠道</option>
							<c:forEach var="list" items="${channel_types}" varStatus="idx">
			        			<option value="${list.key }" >${list.value }</option>
							</c:forEach>
					</select>
						<input type="button" value="确定" onclick="addRegisterAccount();" /></span>	
						
				<div id="divs" style="display:none">
			      				<select name="batchStopReason" style="width: 100px;" id="batchStopReason">
										<option value="2" >取消订单过多</option>
										<option value="1" >账号被封</option>
										<option value="3" >联系人达上限</option>
										<option value="4" >未实名制</option>
								</select>
			    				<input type="button" value="批量停用" onclick="javascript:batchStop(4);" />
			    		</div> 	
				</div>
				<div class="pub_debook_mes  oz mb10_all" style="margin-left: 800px;margin-top: -20px;margin-bottom: 2px;">
					<span style="font-size:14px;">申请启用<input value="" id="start_register_num" name="start_register_num" style="color:#f60;width:20px;"/>个账号，
					<select name="start_choose_source" style="width: 72px;" id="start_choose_source">
							<option value="000">选择来源</option>
							<option value="1">19e</option>
							<option value="2">批量导入</option>
							<option value="3">人工添加</option>
							<option value="4">其他</option>
					</select>
					<select name="start_choose_channel" style="width: 72px;" id="start_choose_channel">
							<option value="000">选择渠道</option>
							<c:forEach var="list" items="${channel_types}" varStatus="idx">
			        			<option value="${list.key }" >${list.value }</option>
							</c:forEach>
					</select>
						<input type="button" value="启用" onclick="startRegisterAccount();" /></span>	
				</div>
				<br/>
					<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th style="width:40px;">全选 <br/><input type="checkbox" id="checkChnRetRulAll" name="checkChnRetRulAll" onclick="checkChnRetRuleAll()" /></th>
							<th width="8">
								序号
							</th>
							<!-- <th>账号名称</th> -->
							<th>
								所在省份
							</th>
							<th>
								账号来源
							</th>
							<th>
								登陆名
							</th>
							<th>
								登陆密码
							</th>
							<th>
								邮箱
							</th>
							<th>
								邮箱密码
							</th>
							<th>
								账号状态
							</th>
							<th width="65">
								创建时间
							</th>
							<th width="65">
								操作时间
							</th>
							<th width="30">
								停用天数
							</th>
							<th>
								联系人
							</th>
							<th>
								订单号
							</th>
							<th>
								渠道
							</th>
							<th>
								停用原因
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${accountList}" varStatus="idx">
						<script type="text/javascript">
							function stop(stop_id,acc_id,acc_username,obj){
								//alert(stop_id);
								if( $("#stop_reason_"+stop_id).val()== "000" ){
									alert("请选择停用原因！");
									$("#stop_reason").focus();
									return false;
								}else{
								
								var stop_reason = $("#stop_reason_"+stop_id).val();
								//alert("stop_reason"+stop_reason);
								var url = "/account/updateStopAccount.do?version="+new Date();
								
								$.post(url,{acc_status:22,acc_id:acc_id,acc_username:acc_username,stop_reason:stop_reason},function(data){
									if(data=="yes"){

										$("form:first").attr("action","/account/queryAccountList.do?pageIndex="+<%=pageIndex%>);
										$("form:first").submit();
										//window.location="/account/queryAccountList.do?pageIndex="+<%=pageIndex%>;
									}else{
										alert("停用账号失败");
									}
								});
								}
							}
							</script>
							<c:choose>
								<c:when test="${fn:contains('00,66', list.acc_status )}">
									<tr style="background: #BEE0FC;">
								</c:when>
								<c:otherwise>
									<tr>
								</c:otherwise>
							</c:choose>
							<td><input type="checkbox" id="acc_id" name="acc_id" value="${list.acc_id }"/></td>
							<td>
								${idx.index+1}
							</td>
							<!--<td>${list.acc_name}</td> -->
							<td>
								${list.province_name}
							</td>
							<td>
								${account_source[list.account_source]}
							</td>
							<td>
								${list.acc_username}
							</td>
							<td>
								${list.acc_password}
							</td>
							<td>
								${list.acc_mail}
							</td>
							<td>
								${list.pwd}
							</td>
							<td>
								${accountStatus[list.acc_status]}
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.option_time, ' ')}
								<br />
								${fn:substringAfter(list.option_time, ' ')}
							</td>
							<td>
								<c:if test="${list.acc_status eq '22' and list.stop_reason eq '3'}">
									${list.offdays }
								</c:if>
							</td>
							<td>
								${list.contact_num}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${channel_types[list.channel] }
							</td>
							<td>
								<c:if test="${list.acc_status eq '33' }">
									<select name="stop_reason" style="width: 100px;" id="stop_reason_${idx.index}">
										<option value="000"> </option>
										<option value="2" >取消订单过多</option>
										<option value="1" >账号被封</option>
										<option value="3" >联系人达上限</option>
										<option value="4" >未实名制</option>
										<option value="5" >已达订购上限</option>
										<option value="6" >用户取回</option>
										<option value="7" >手机核验</option>
									</select>
								</c:if>
								<c:if test="${list.acc_status eq '22' }">
									<c:if test="${list.stop_reason eq '1' }">账号被封</c:if>
								 	<c:if test="${list.stop_reason eq '2' }">取消订单过多</c:if>
								 	<c:if test="${list.stop_reason eq '3' }">联系人达上限</c:if>
								 	<c:if test="${list.stop_reason eq '4' }">未实名制</c:if>
								 	<c:if test="${list.stop_reason eq '5' }">已达订购上限</c:if>
								 	<c:if test="${list.stop_reason eq '6' }">用户取回</c:if>
								 	<c:if test="${list.stop_reason eq '7' }">手机核验</c:if>
								</c:if>  
							</td>
							<td>
								${list.opt_person }
							</td>
							<td>
								<span> <c:if test="${list.acc_status eq '33' }">
										<a href="javaScript:void(0)" onclick="xiugai('${list.acc_id }');" >修改</a> 
										
										<a href="javascript:void(0)" onclick="stop(${idx.index},'${list.acc_id}','${list.acc_username }',this)">停用</a>
									</c:if> <c:if test="${list.acc_status eq '22' }">
										<a href="javascript:void(0)" onclick="shanchu('${list.acc_id}','${list.acc_username }')">删除</a>
										<a href="javaScript:void(0)" onclick="xiugai('${list.acc_id }');" >修改</a> 
										<a href="javascript:void(0)" onclick="qiyong('${list.acc_id}','${list.acc_username }')">启用</a>
									</c:if> <c:if test="${list.acc_status eq '00' }">
										<a href="javascript:void(0)" onclick="shifang('${list.acc_id}','${list.acc_username }')">释放</a>
										
									</c:if> <c:if test="${list.acc_status eq '55' }">
									<a href="javascript:void(0)" onclick="qiyong('${list.acc_id}','${list.acc_username }')">启用</a>
									</c:if> <c:if test="${list.acc_status eq '66' }">
										<a href="javascript:void(0)" onclick="shifang('${list.acc_id}','${list.acc_username }')">释放</a>
										
									</c:if> </span>
							</td>
							</tr>
							
						</c:forEach>
					</table>
					
				<!-- 	<table border="1" cellspacing="1" cellpadding="0" width="100%">
			      		<tr align="center" style="height:25px;font-weight: bold" bgcolor="#ffffff" >
			    			<td colspan="14"  style="text-align:left;">
			    			<input type="button" value="批量删除" onclick="javascript:batchStop(1);"  style="width:80px;margin:4px 10px;"  />
			    				<input type="button" id="updateBtn" value="批量修改" onclick="javascript:updateMore();"  style="width:80px;margin:4px 10px;"  />
			    				<input type="button" value="批量启用" onclick="javascript:batchStop(3);"  style="width:80px;margin:4px 10px;"  />		
			    				<input type="button" value="批量停用" onclick="javascript:batchStop(4);"  style="width:80px;margin:4px 10px;"  />
			    				<select name="batchStopReason" style="width: 100px;" id="batchStopReason">
										<option value="2" >取消订单过多</option>
										<option value="1" >账号被封</option>
										<option value="3" >联系人达上限</option>
										<option value="4" >未实名制</option>
								</select>
			    				<input type="hidden" id="batchStoptype" name="batchStoptype">
			    				<input type="hidden" id="tag1" name="tag1" value="0">
			    			</td>
			    		</tr>
			    	</table> -->
			    	<input type="hidden" id="batchStoptype" name="batchStoptype">
			    	<input type="hidden" id="tag1" name="tag1" value="0">
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
				<br />
				<p></p>
			</form>
		</div>
	</body>
</html>
