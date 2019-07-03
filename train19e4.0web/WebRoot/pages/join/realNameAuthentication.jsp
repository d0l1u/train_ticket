<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%     
	String path = request.getContextPath();     
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";     
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我要加盟-实名认证页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/idCard.js"></script>
<script type="text/javascript" src="/js/jquery.form.js"></script>
</head>

<body>
	<div class="content oz">
		<div class="index_all">
		<!--左边内容 start-->
		<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
				</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz">
	         <div class="join_us oz">
	         		<c:choose>
	         		<c:when test="${agentVo.estate eq '33' || agentVo.estate eq '44'}">
	         		<div class="agent_pass oz">
			            	<ul class="agent_pass_ul">
			            		<li class="current1"><a href="/joinUs/agentInfoIndex.jhtml">个人信息</a></li>
			                	<li class="current current1"><a href="/joinUs/realNameAuth.jhtml">实名认证</a></li>
			                </ul>
		                </div>
	         		</c:when>
	         		<c:when test="${agentVo.estate eq '11' || agentVo.estate eq '22'}">
	         		 <div class="agent_pass oz">
		            	<ul class="agent_pass_ul">
		            		<li class="current1"><a href="/joinUs/agentInfoIndex.jhtml">个人信息</a></li>
			                <li class="current1"><a href="/joinUs/joinIndex.jhtml">我要开通</a></li>
		                	<li class="current current1"><a href="/joinUs/realNameAuth.jhtml">实名认证</a></li>
		                </ul>
	                </div>
	         		</c:when>
	         		<c:otherwise>
		         	 <div class="agent_pass oz">
		            	<ul class="agent_pass_ul">
			                <li class="current1"><a href="/joinUs/joinIndex.jhtml">我要开通</a></li>
		                	<li class="current current1"><a href="/joinUs/realNameAuth.jhtml">实名认证</a></li>
		                </ul>
	                </div>
	         		</c:otherwise>
	         		</c:choose>
	         		
	                
	                <div class="notice_weak_box oz">
		            	<div class="weak_box" style="width:98%;padding:10px 10px 10px 0;">
		            		<h3 style="text-align:center;">实名认证说明</h3>
		                    <ul>
		                        <li style="padding-left:0;text-indent:2em;line-height:22px;background:none;">根据<a onclick="window.open('http://www.12306.cn/mormhweb/zxdt/201402/t20140223_1435.html')" href="#">《铁路互联网购票身份核验须知》</a>的规定，自2014年3月1日起铁路系统已经开始对注册用户进行实名认证，为了保证系统正规和合法的运营，方便周边用户的出行，所以需要代理商提供未在12306绑定的实名身份证信息进行绑定（请使用本人或已经授权的亲属身份信息，不要使用乘客信息用于实名认证），提交申请后系统会自动审核绑定。</li>
		                     <!--  <li style="padding-left:0;text-indent:2em;line-height:22px;background:none;">活动详情：<a onclick="window.open('http://image30.19360.cn/resources/page/20140331150530/hcpmd/index.html')" href="#">订火车票免单—周周来袭</a></li>      -->   
		              </ul>
		                </div>
	            	</div>
	            
	             <!--身份证实名 start-->
               <form action="/joinUs/submitRegistInfo.jhtml" method="post" id="personForm">
               	<div class="id-verify oz">
                    <div class="id-verify-result">
                    	<input type="hidden" id="registNum" value="${fn:length(registList)}"/>
                    	<h3><span class="serial">序号</span><span class="name">姓名</span><span class="id">身份证号</span><span class="tel">联系人电话</span><span class="state2">状态</span><span class="operate2">操作</span></h3>
                        <c:forEach varStatus="status" items="${registList}" var="regist">
					       <c:choose>
					         <c:when test="${regist.regist_status eq '22'}">
		                        <ul class="oz person_data" id="person_data_${regist.regist_id}">
		                        	<li class="serial">${status.index + 1}</li>
		                            <li class="name">${regist.user_name}</li>
		                            <li class="id">${regist.ids_card}</li>
		                            <li class="tel">&nbsp;${regist.user_phone}</li>
			                        <li class="state2" style="color:blue;">已通过</li>
		                        </ul>
	                        </c:when>
	                       	<c:when test="${regist.regist_status eq '33'}">
	                       	 	 <ul class="oz person_data" id="person_data_${regist.regist_id}">
	                       	 		<li class="serial">${status.index + 1}</li>
		                            <li class="name">${regist.user_name}</li>
		                            <li class="id">${regist.ids_card}</li>
		                            <li class="tel">&nbsp;${regist.user_phone}</li>
	                        		<li class="state2" style="color:red;">未通过</li>
	                        		<li class="operate2">
	                        		<a class="update_person_content" href="#">【修改】</a>
	                        		<a href="#" id="delete_${regist.regist_id}" class="delete_person">【删除】</a>
	                        		</li>
	                        		<br/>
	                        	 <c:if test="${regist.fail_reason eq '1'}">
	                       	 		<li id="deleteFail_${regist.regist_id}" style="display:inline;margin-left:110px;line-height:22px;color:#ff8e17;">失败原因：实名制身份信息有误！</li>
	                        	 </c:if>
	                        	  <c:if test="${regist.fail_reason eq '2'}">
	                       	 		<li id="deleteFail_${regist.regist_id}" style="margin-left:110px;line-height:22px;color:#ff8e17;">失败原因：您提交的身份信息无法实名，请更换其他身份信息！</li>
	                        	 </c:if>
	                        	  <c:if test="${regist.fail_reason eq '6'}">
	                       	 		<li id="deleteFail_${regist.regist_id}" style="margin-left:110px;line-height:22px;color:#ff8e17;">失败原因：您所提交的身份信息已被用户车站取回，请更换其他身份信息，谢谢！</li>
	                        	 </c:if>
	                        	</ul>
			                </c:when>
			                <c:when test="${regist.regist_status eq '55'}">
	                       	 	 <ul class="oz person_data" id="person_data_${regist.regist_id}">
	                       	 		<li class="serial">${status.index + 1}</li>
		                            <li class="name">${regist.user_name}</li>
		                            <li class="id">${regist.ids_card}</li>
		                            <li class="tel">&nbsp;${regist.user_phone}</li>
	                        		<li class="state2">待核验</li>
	                        		<li class="operate2">
	                        		<a href="#" id="delete_${regist.regist_id}" class="delete_personWait">【删除】</a>
	                        		</li>
	                        		<br/>
	                        	   <input type="hidden" id="alreadyIdsCard_${regist.regist_id}" name="alreadyIdsCard" value="${regist.ids_card}"/>
	                        	   <input type="hidden" id="alreadyUserName_${regist.regist_id}" name="alreadyUserName" value="${regist.user_name}"/>
	                       	 		<li id="deleteFail_${regist.regist_id}" style="display:inline;margin-left:110px;"><p style="margin: 0;line-height:22px;color:#ff8e17;">该联系人身份信息待核验,需本人持有效证件去车站窗口实名认证后方可购票！</p></li>
	                       	 		<li style="float:right;margin-right:30px;_margin-right:20px;"><a href="#" id="${regist.regist_id}" class="alreadyAuthenticate" style="line-height:22px;">【我已窗口认证】</a></li>
	                       	 	</ul>
			                </c:when>
			                <c:otherwise>
			                 <ul class="oz person_data" id="person_data_${regist.regist_id}">
	                       	 		<li class="serial">${status.index + 1}</li>
		                            <li class="name">${regist.user_name}</li>
		                            <li class="id">${regist.ids_card}</li>
		                            <li class="tel">&nbsp;${regist.user_phone}</li>
	                        		<li class="state2">审核中</li>
	                        	 </ul>
	                        </c:otherwise>
	                       </c:choose> 
                        </c:forEach>
                    </div>
                    <div class="add-id" id="add_person_all">
                    	<div id="add_person_content">
	                    	<div class="add-id-con oz" style="display:none;">
	                    		<input name="registInfoList_source.regist_id" id="regist_id_index_source" type="hidden" class="text" />
	                        	<label>姓名：</label><input name="registInfoList_source.user_name" id="user_name_index_source" type="text" class="text" onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
	                            <label>身份证号：</label><input name="registInfoList_source.ids_card" id="ids_card_index_source" type="text" class="text" onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
	                            <label>联系人电话：</label><input name="registInfoList_source.user_phone" id="user_phone_index_source" type="text" class="text" onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
	                        </div>
                        </div>
                    	<div id="add_info_show" class="add-mes oz"><label>您还可以添加<i id="left_person_num"></i>个用户</label><input type="button" id="add_adult" class="btn" value="添加" /></div>
                    </div>
                    <div class="submit-id oz">
                    	<div class="protocol oz">
                        	<input type="checkbox" id="weather_agree" style="margin:0;"/>
                            <label for="weather_agree">我已阅读并同意
           						<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.open('<%=basePath%>pages/guide/zhuCe.jsp');">《委托注册多个代购账户协议》</span>                 
                            </label>
                        </div>
                        <div class="operate-btn oz" style="margin-left:60px;">
                        	<input type="submit" class="btn submit" value="提交" />
                        </div>
                    </div>
              	  </div>
                </form>
             </div>
            <!--我要加盟-tab end-->
            
             <!--身份证实名 start-->
             <c:choose>
             <c:when test="${numMap.passNum eq 1 }">
              <div class="vip-mes-wrap mb10" style="margin-bottom:60px;">
	           		<div class="vip-mark bronze-mark" ></div>
	              	<p>恭喜您，实名认证审核通过，您现在是铜牌用户，实名认证通过2人以上，<br/>
						等级可提升为“银牌用户”，升级为银牌用户后可参与银牌专享活动！</p>
	           	</div>
             </c:when>
             <c:when test="${numMap.passNum eq 2 or numMap.passNum eq 3 or numMap.passNum eq 4}">
             <div class="vip-mes-wrap mb10" style="margin-bottom:60px;">
	           		<div class="vip-mark silver-mark"></div>
	              	<p>您现在是银牌用户，实名认证通过5人，等级可提升为“金牌用户”，<br/>
						升级为金牌用户后可参与金牌专享活动，更多惊喜等着您！</p>
	           	</div>
             </c:when>
             <c:when test="${numMap.passNum ge 5}">
             	<div class="vip-mes-wrap mb10" style="margin-bottom:60px;">
           			<div class="vip-mark gold-mark"></div>
              		<p>恭喜您，等级已提升至“金牌用户”，<br/>将会享受到“金牌用户”专属活动，敬请期待！</p>
           		</div>
             </c:when>
             <c:when test="${numMap.passNum eq 0  and numMap.noNum ge 1 }">
             <div class="vip-mes-wrap mb10">
           			<div class="vip-mark novip-mark"></div>
              		<p>您提交的实名制信息审核没有通过，请更换实名认证信息后重新提交！</p>
           		</div>
             </c:when>
             <c:when test="${numMap.passNum eq 0 and  numMap.noNum eq 0 and numMap.waitNum  ge 1}">
             <div class="vip-mes-wrap mb10">
              		<p style="padding-left:0;">您提交的实名制信息正在审核中，请耐心等待，<br/>审核通过后等级将变为“铜牌用户”！</p>
           		</div>
             </c:when>
             <c:otherwise>
               <div class="vip-mes-wrap mb10" style="display:none;">
              		<p style="padding-left:0;">您还未开始实名，请填写实名信息开始实名！</p>
           		</div>
             </c:otherwise>
             </c:choose>
           	<!-- 身份证实名end -->
        </div>
        <!--右边内容 end-->
    </div>
    </div>
    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
<script type="text/javascript" language="JavaScript">
	$().ready(function(){
		var show = false;
		$(".person_data").each(function(){
			var status = $(this).children(".state2").text();
			if(status == "未通过"){
				show = true;
				return true;
			}
		});
		if($("#registNum").val()>=5 && !show){
			$("#add_person_all").hide();
			$(".submit-id").hide();
		}else{
			$("#add_person_all").show();
			$(".submit-id").show();
		}
		
		if($("#registNum").val()>=5){
			$("#add_info_show").hide();
		}else{
			var num = 5 - Number($("#registNum").val());
			$("#left_person_num").text(num);
			$("#add_info_show").show();
		}
		
		//添加联系人
		$("#add_adult").click(function(){
			var count = $(".add-id-con:visible").length;
			var replaceStr = "registInfoList["+count+"]";
			var html = $(".add-id-con").html().replace(/index_source/g, count+1);
			html = html.replace(/registInfoList_source/g, replaceStr);
			$("#add_person_content").append($("<div class=\"add-id-con\">" + html + "</div>").css("background-color","")).show();
			var num = $("#registNum").val();
			var totalNum = Number(count)+Number(num);
			if(totalNum>=5){
				$("#add_info_show").hide();
			}
			var result = 5 - totalNum;
			$("#left_person_num").text(result);
		}).trigger("click");

		/**提交开始**/
		//消息框	
		var ajax_dialog = "";
		
		var options = {
			   target: '#output',          //把服务器返回的内容放入id为output的元素中    
			   beforeSubmit: showRequest,
			   success: showResponse,      //提交后的回调函数
			   //url: url,                 //默认是form的action， 如果申明，则会覆盖
			   dataType : 'json',
			   timeout: 5000,               //限制请求的时间，当请求大于3秒后，跳出请求
			   failure : function(xhr,msg){ 
					ajax_dialog.close();
					dialogAlter("处理超时请重新提交"); 
					return false;
			   } 
				   
		}
		
		function showResponse(responseText, statusText){
			var result = responseText.result;
			/**
			if(result=="SUCCESS"){
				ajax_dialog.content('用户信息提交成功！');
			}else{
				ajax_dialog.content('用户信息提交失败！');
				ajax_dialog.title("提示");
				ajax_dialog.button({name:'确认',focus: true,callback: function(){}});
				return false;
			}
			sleep(2000);//睡眠2秒，跳转到查询首页
			**/
			if(result=="SUCCESS"){
				window.location.href="/joinUs/realNameAuth.jhtml";
			}
		};
		//睡眠程序，毫秒为单位
		function sleep(numberMillis) {
			 var now = new Date();
			 var exitTime = now.getTime() + numberMillis; 
			 while (true) { 
				 now = new Date(); 
			 	 if (now.getTime() > exitTime) 
				  return;
		 	}
		}
			
		function showRequest(){
			if($("#weather_agree").attr("checked")!='checked'){
				showErrMsg("weather_agree", "150px", "请阅读并勾选相关协议！");
				return false;
			}
			var count = $(".person_data").length;
			var index = 1;
			var result = true;
			$(".add-id-con:visible").each(function(){
				var user_name = $.trim($("#user_name_"+index).val());
				var user_phone = $.trim($("#user_phone_"+index).val());
				var ids_card = $.trim($("#ids_card_"+index).val());

				if(user_name=="" && user_phone=="" && ids_card==""){
					return true;
				}else{
					if(count == 5){
						var regist_id = $.trim($("#regist_id_1").val());
						if(regist_id == '' || regist_id == undefined){
							dialogAlter("最多添加申请五个实名认证用户！");	
							result = false;
							return false;			
						}
					}
					if(user_name==""){
						showErrMsg("user_name_"+index, "150px", "请输入姓名！");
						result = false;
						return false;
					}else if(!checkName(user_name)){
						showErrMsg("user_name_"+index, "150px", "请填写正确的姓名！");
						result = false;
						return false;
					}
					if(ids_card =="" || !valiIdCard(ids_card)){
						showErrMsg("ids_card_"+index, "200px", "请输入正确的18位二代身份证号！");
						result = false;
						return false;
					}
					if(user_phone == "" || !/^1(3[\d]|4[57]|5[\d]|8[\d]|70)\d{8}$/g.test(user_phone)){
						showErrMsg("user_phone_"+index, "150px", "请输入正确的手机号！");
						result = false;
						return false;
					}
				}
				$(".person_data").each(function(){
					var status = $(this).children(".state2").text();
					if(status == "未通过"){
						return true;
					}
					var name = $(this).children(".name").text();
					var card = $(this).children(".id").text();
					var phone = $(this).children(".tel").text();
					
					if(card==ids_card){
						dialogAlter("证件号"+card+"已经存在！");
						result = false;
						return false;
					}
				});
				if(result==false){
					return false;
				}
				index += 1;
			});
			/**
			if(result){
				ajax_dialog =art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '30%',
				    title: 'Loading...',
				    icon: "/images/loading.gif",
				    content: '信息正在提交中！'
				});
			}
			*/
			if(result){
				if(confirm("是否提交用户信息")){
					return result;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		
		$('#personForm').submit(function() { 
			$(this).ajaxSubmit(options); 
	        return false; 
	    }); 
		/**提交结束**/
		
		
		
		
		/**我已窗口认证开始 **/
		$(".alreadyAuthenticate").click(function(){
			var regist_id = $(this).attr("id");
			if(confirm("您是否已去火车站窗口认证？")){
				var ids_card=$("#alreadyIdsCard_"+regist_id).val();
			 	var user_name=$("#alreadyUserName_"+regist_id).val();
                var dataStr='';
                dataStr=user_name+"&"+ids_card;
				$.ajax({
					url:"/userIdsCardInfo/alreadyAuthenticate.jhtml",
					type: "POST",
					cache: false,
					data: {'data':dataStr}, 
					success: function(data){
						if(data=="SUCCESS"){
							location.reload();
						}
					}
				});
			}
		});
		/**我已窗口认证结束 **/
		
		
		/**修改按钮**/
		$(".update_person_content").click(function(){
			var regist_id = $(this).parent("li").parent("ul").attr("id");
			regist_id = regist_id.split("_")[2];
			var ids_card = $(this).parent("li").siblings(".id").text();
			var user_name = $(this).parent("li").siblings(".name").text();
			var tel = $(this).parent("li").siblings(".tel").text();
			var count = $(".add-id-con:visible").length;
			var replaceStr = "registInfoList["+count+"]";
			var result = true;
			
			//联系人信息存在空行则填充最近空行，若不存在空行，则添加一行并填充选中联系人信息
			for(var i=1; i<=5; i++){
				if($("#user_name_"+i).val()==user_name && $("#ids_card_"+i).val()==ids_card){
					dialogAlter("该联系人信息已经填充到提交内容区域里！");
					return false;
				}
				//已经存在五个实名认证用户的修改逻辑
				var count = $(".person_data").length;
				if(count == 5){
					$("#regist_id_1").val(regist_id);
					$("#user_name_1").val(user_name);
					$("#ids_card_1").val(ids_card);
					$("#user_phone_1").val(tel);
					result = false;
					break;
				}
				if($("#user_name_"+i).val()=='' && $("#ids_card_"+i).val()=='' && $("#user_phone_"+i).val()==''){
					$("#regist_id_"+i).val(regist_id);
					$("#user_name_"+i).val(user_name);
					$("#ids_card_"+i).val(ids_card);
					$("#user_phone_"+i).val(tel);
					result = false;
					break;
				}
			}
			if(result){
				var num = count+1;
				var html = $(".add-id-con").html().replace(/index_source/g, count+1);
				html = html.replace(/registInfoList_source/g, replaceStr);
				$("#add_person_content").append($("<div class=\"add-id-con\">" + html + "</div>").css("background-color","")).show();
				$("#regist_id_"+num).val(regist_id);
				$("#user_name_"+num).val(user_name);
				$("#ids_card_"+num).val(ids_card);
				$("#user_phone_"+num).val(tel);
			}
		});
		/**删除按钮**/
		$(".delete_person").click(function(){
			if(confirm("确定要删除该用户信息吗？")){
				var regist_str = $(this).attr("id");
				var card = $(this).parent("li").siblings(".id").text();
				var name = $(this).parent("li").siblings(".name").text();
				var user_tel = $(this).parent("li").siblings(".tel").text();
				var index = 1;
				$(".add-id-con:visible").each(function(){
					var user_name = $.trim($("#user_name_"+index).val());
					var user_phone = $.trim($("#user_phone_"+index).val());
					var ids_card = $.trim($("#ids_card_"+index).val());
					if(card==ids_card && user_tel==user_phone && name==user_name){
						$("#user_name_"+index).val("");
						$("#user_phone_"+index).val("");
						$("#ids_card_"+index).val("");
					}
					index +=1;
				});
				regist_id = regist_str.split("_")[1];
				var regist = $(this).parent("li").parent("ul").attr("id");
				$.ajax({
					url:"/joinUs/deleteRegistInfo.jhtml?regist_id="+regist_id,
					type: "POST",
					cache: false,
					success: function(data){
						if(data=="SUCCESS"){
							/**
							var dialog =art.dialog({
								lock: true,
								fixed: true,
								left: '50%',
								top: '30%',
							    title: '提示',
							    content: '用户信息删除成功！'
							});
							setTimeout(function(){
								dialog.close();
								},
								500
							); 
							*/
							//提交人员内容区域显示
							$("#add_person_all").show();
							$(".submit-id").show();
							//页面行删除
							$("#"+regist).remove();
							$("#deleteFail"+regist_id).remove();
							//还可以添加人数
							var left_num = Number($("#left_person_num").text())+1;
							$("#left_person_num").text(left_num);
							$("#add_info_show").show();

							//重新设置序号
							$(".person_data").each(function(index){
								$(this).children(".serial").text(index+1);
							});
						}
					}
				});
			}
			/**return false;
			var confirm = art.dialog.confirm('您确认删除该用户信息吗？',function(){
	            delPersonInfo(id);
	    		},
	    		function(){}
    		);
    		**/
		});

		/**待核验删除按钮**/
		$(".delete_personWait").click(function(){
			if(confirm("若您已通知联系人去火车站办理实名认证，请不要删除该联系人。您是否确认删除该联系人？")){
				var regist_str = $(this).attr("id");
				var card = $(this).parent("li").siblings(".id").text();
				var name = $(this).parent("li").siblings(".name").text();
				var user_tel = $(this).parent("li").siblings(".tel").text();
				var index = 1;
				$(".add-id-con:visible").each(function(){
					var user_name = $.trim($("#user_name_"+index).val());
					var user_phone = $.trim($("#user_phone_"+index).val());
					var ids_card = $.trim($("#ids_card_"+index).val());
					if(card==ids_card && user_tel==user_phone && name==user_name){
						$("#user_name_"+index).val("");
						$("#user_phone_"+index).val("");
						$("#ids_card_"+index).val("");
					}
					index +=1;
				});
				regist_id = regist_str.split("_")[1];
				var regist = $(this).parent("li").parent("ul").attr("id");
				$.ajax({
					url:"/joinUs/deleteRegistInfo.jhtml?regist_id="+regist_id,
					type: "POST",
					cache: false,
					success: function(data){
						if(data=="SUCCESS"){
							/**
							var dialog =art.dialog({
								lock: true,
								fixed: true,
								left: '50%',
								top: '30%',
							    title: '提示',
							    content: '用户信息删除成功！'
							});
							setTimeout(function(){
								dialog.close();
								},
								500
							); 
							*/
							//提交人员内容区域显示
							$("#add_person_all").show();
							$(".submit-id").show();
							//页面行删除
							$("#"+regist).remove();
							$("#deleteFail"+regist_id).remove();
							//还可以添加人数
							var left_num = Number($("#left_person_num").text())+1;
							$("#left_person_num").text(left_num);
							$("#add_info_show").show();

							//重新设置序号
							$(".person_data").each(function(index){
								$(this).children(".serial").text(index+1);
							});
						}
					}
				});
			}
			/**return false;
			var confirm = art.dialog.confirm('您确认删除该用户信息吗？',function(){
	            delPersonInfo(id);
	    		},
	    		function(){}
    		);
    		**/
		});
		
		/**删除按钮
		$(".delete_person").click(function(){
				art.dialog.confirm('您确认删除该常用乘客信息？',function(){
		            delCookiePaxInfo(id);
	    		},function(){});


			var regist_str = $(this).attr("id");
			var card = $(this).parent("li").siblings(".id").text();
			var name = $(this).parent("li").siblings(".name").text();
			var user_tel = $(this).parent("li").siblings(".tel").text();
			var index = 1;
			$(".add-id-con:visible").each(function(){
				var user_name = $.trim($("#user_name_"+index).val());
				var user_phone = $.trim($("#user_phone_"+index).val());
				var ids_card = $.trim($("#ids_card_"+index).val());
				if(card==ids_card && user_tel==user_phone && name==user_name){
					$("#user_name_"+index).val("");
					$("#user_phone_"+index).val("");
					$("#ids_card_"+index).val("");
				}
				index +=1;
			});
			regist_id = regist_str.split("_")[1];
			var regist = $(this).parent("li").parent("ul").attr("id");
			$.ajax({
				url:"/joinUs/deleteRegistInfo.jhtml?regist_id="+regist_id,
				type: "POST",
				cache: false,
				success: function(data){
					if(data=="SUCCESS"){
						var dialog =art.dialog({
							lock: true,
							fixed: true,
							left: '50%',
							top: '30%',
						    title: '提示',
						    content: '用户信息删除成功！'
						});
						setTimeout(function(){
							dialog.close();
							},
							500
						); 
							
						//提交人员内容区域显示
						$("#add_person_all").show();
						$(".submit-id").show();
						//页面行删除
						$("#"+regist).remove();
						//还可以添加人数
						var left_num = Number($("#left_person_num").text())+1;
						$("#left_person_num").text(left_num);
						$("#add_info_show").show();

						//重新设置序号
						$(".person_data").each(function(index){
							$(this).children(".serial").text(index+1);
						});
					}
				}
			});
		});
		**/
		function showErrMsg(id, _width, msg){
			$("#"+id+"_errMsg").remove();
			var offset = $("#"+id).offset();
			$obj=$("#tip").clone().attr("id", id+"_errMsg")
				.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width}).appendTo("body");
			$obj.find(".errMsg").text(msg).end().show();
			setTimeout(function(){
				$("#"+id+"_errMsg").remove();},
				1000
			); 
		}
		
		//验证身份证是否有效
		function valiIdCard(idCard){
			if(idCard.length!=18){
				return false;
			}
			var checkFlag = new clsIDCard(idCard);
			if(!checkFlag.IsValid()){
				return false;
			}else{
				return true;
			}
		}
		
		//通用弹出信息为str的消息弹框
		function dialogAlter(str){
			//消息框
			var dialog = art.dialog({
				lock: true,
				fixed: true,
				left: '50%',
				top: '30%',
			    title: '提示',
			    okVal: '确认',
			    icon: "/images/warning.png",
			    content: str,
			    ok: function(){}
			});
			return;
		}
		
		
	//验证姓名是否有效
	function checkName(val){
	    var pat=new RegExp("[^a-zA-Z\_\u4e00-\u9fa5]","i"); 
	    //var forbidArr  = new Array('成人','成人票','学生票','一张');
	    if(pat.test(val)==true){
	        return false; 
	    }else{
	        var check = /^[A-Za-z]+/;
	        if(check.test(val)){
	            if(val.length<3){
	                return false;
	            }
	        }
	        var checkCName = /^[\u4e00-\u9fa5]+/;
	        if(checkCName.test(val)){
	            if(val.length<2){
	                return false;
	            }
	        }
	        /**
	        if($.inArray(val.trim(),forbidArr)>=0){
	            return false;
	        }**/
	        return true;
	    }
	}   
	});
</script>
</body>
</html>
