<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我要加盟-资料填写页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
</head>

<body>
	<div class="content oz">
	<div class="index_all">
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="join" />
				</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz">
	    	 <div class="agent_pass oz">
	            	<ul class="agent_pass_ul">
	                	<li class="current current1"><a href="/joinUs/joinIndex.jhtml">我要开通</a></li>
	                	<li class="current1"><a href="/joinUs/realNameAuth.jhtml">实名认证</a></li>
	                </ul>
	         </div>
	         <div class="special mb10" style="margin-top:10px;">
            	<a href="#"><img src="/images/special_756x122.jpg" alt="开通专题" /></a>
            </div>
            <!--提醒通告模块 start-->
            <div class="notice_weak_box oz">
            	<div class="weak_box">
                	<h3>开通要求</h3>
                    <ul>
                        <li>同意<a href="javascript:void(0);" onclick="window.open('/pages/guide/daiGou.jsp')">《火车票线下代购服务协议》</a>中的条款</li>
                    	<li>100米之内不允许有车票售卖点</li>
                        <li>愿意接受19e分公司的业务监督和指导</li>
                        <li>承诺不收取除购买款外任何多余的手续费</li>
                    </ul>
                </div>
                <!-- 
                <div class="notice oz">
		  			<jsp:include flush="true" page="/notice/queryNoticeList.jhtml">
		  				<jsp:param name="" value=""/>
					</jsp:include>
                </div>
                 -->
            </div>
            <!--提醒通告模块 end-->
     
         	<!--我要加盟-tab start-->
            <div class="join_us oz">
            	  <div class="flow_step">
                	<p class="flow_pic1"></p>
                	<ul class="oz">
                      <li class="step1 current">填写基本资料</li>
                      <li class="step2">实名认证</li>
                      <li class="step3">正在审核</li>
                      <li class="step4">业务试用</li>
                      <li class="step5">审核通过</li>
                    </ul>
                </div>
              	<form action="/joinUs/addJoinInfo.jhtml" method="post">
              	<div class="reg_tip oz">
                	<p>为了保证网点的服务质量，火车票业务采用开通申请的方式，请按照右边的文本框提示填入您准确的信息，点击“下一步”我们根据收到您提供的信息跟当地分公司人员沟通，对网点进行审核。</p>
					<p>开通审核通过会以平台和电话的方式进行通知。</p>
					<p>具体开通情况，请咨询当地分公司。</p>
					<p style="color:red;">注意：订购成功后平台会给用户发送取票短信，短信中会包括您店铺的名称，这样可以提高用户对您店铺的售票的信任度，请您对“店铺简称”文本框填入的内容慎重对待，谢谢！</p>
					<p>例如：如果店铺简称填入是步步高超市，下发短信内容：“【步步高超市】E888888888，提醒您成功订购07月26日13:50（福州—福州南）D6237次2车14D号席位，张XX、李XX请持二代身份证乘车或换票乘车。【19易】”</p>
                </div>
                <table class="user_mes user_mes_r">
                	<tr>
                    	<th>店铺名称：</th>
                    	<td width="160">${agentName}</td>
                    	<th width="60"></th>
                    	<td align="left"></td>
                    </tr>
                    <tr>
                    	<th>店铺简称：</th>
                    	<td colspan="3"><input type="text" class="text" style="color:#aaa" id="shop_short_name" name="shop_short_name" 
                    		value="定制短信(左侧说明)" onfocus="if(this.value=='定制短信(左侧说明)'){this.value=''};this.style.color='#333';" 
                    		onblur="if(this.value==''||this.value=='定制短信(左侧说明)'){this.value='定制短信(左侧说明)';this.style.color='#aaa';}" maxlength="10" /></td>
                    </tr>
                    <tr>
                    	<th><span>*</span>您的姓名：</th>
                    	<td colspan="3"><input type="text" class="text" style="color:#aaa" id="user_name" name="user_name" 
                    		value="请填写真实姓名" onfocus="if(this.value=='请填写真实姓名'){this.value=''};this.style.color='#333';" 
                    		onblur="if(this.value==''||this.value=='请填写真实姓名'){this.value='请填写真实姓名';this.style.color='#aaa';}" /></td>
                    </tr>
                    <tr>
                    	<th><span>*</span>联系手机：</th>
                    	<td colspan="3"><input type="text" class="text" style="color:#aaa" id="user_phone" name="user_phone" 
                    		value="物料配送联系方式" onfocus="if(this.value=='物料配送联系方式'){this.value=''};this.style.color='#333';" 
                    		onblur="if(this.value==''||this.value=='物料配送联系方式'){this.value='物料配送联系方式';this.style.color='#aaa';}" /></td>
                    </tr>
                    <tr>
                    	<th><span>*</span>QQ：</th>
                    	<td colspan="3"><input type="text" class="text" style="color:#aaa" id="user_qq" name="user_qq"  
                    		value="网上服务联系方式" onfocus="if(this.value=='网上服务联系方式'){this.value=''};this.style.color='#333';" 
                    		onblur="if(this.value==''||this.value=='网上服务联系方式'){this.value='网上服务联系方式';this.style.color='#aaa';}" /></td>
                    </tr>
                    <tr>
                    	<th><span>*</span>所属区域：</th>
                    	<td colspan="3">
                            <select name="district_id" id="district_id">
                            	<option value="" selected="selected">请选择</option>
                            	<c:if test="${fn:length(districtList)==0}">
                            		<option value="111111">其他</option>
                            	</c:if>
                            	<c:forEach items="${districtList}" var="district">
                            		<option value="${district.area_no}">${district.area_name}</option>
                            	</c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr class="website_kinds">
                    	<th><span>*</span>店铺类型：</th>
                    	<td colspan="3">
                    		<ul >
                    			<li><input type="radio" value="0" name="shop_type" id="radio_sj" /><label for="radio_sj">手机充值店</label></li>
                    			<li><input type="radio" value="1" name="shop_type" id="radio_flower" /><label for="radio_flower">鲜花礼品店</label></li>
                    			<li><input type="radio" value="2" name="shop_type" id="radio_small_shop" /><label for="radio_small_shop">小型超市</label></li>
                    			<li><input type="radio" value="3" name="shop_type" id="radio_big_shop" /><label for="radio_big_shop">大型超市</label></li>
                    			<li><input type="radio" value="4" name="shop_type" id="radio_wine" /><label for="radio_wine">烟酒店</label></li>
                    			<li><input type="radio" value="5" name="shop_type" id="radio_paper" /><label for="radio_paper">报刊亭</label></li>
                    			<li><input type="radio" value="6" name="shop_type" id="radio_tickets" /><label for="radio_tickets">票务代售点</label></li>
                    			<li><input type="radio" value="7" name="shop_type" id="radio_lottery" /><label for="radio_lottery">彩票代售点</label></li>
                    			<li><input type="radio" value="8" name="shop_type" id="radio_travel" /><label for="radio_travel">旅行社</label></li>
                    			<li><input type="radio" value="10" name="shop_type" id="radio_net" /><label for="radio_net">网吧</label></li>
                    			<li><input type="radio" value="9" name="shop_type" id="radio_other" /><label for="radio_other">其他</label></li>
                    		</ul>
                        </td>
                    </tr>
                    
                    <tr>
                    	<th><span>*</span>详细地址：</th>
                    	<td colspan="3"><input type="text" id="user_address" name="user_address" class="text addr" /></td>
                    </tr>
                    <tr>
                    	<td colspan="4" class="xieyi">
                    		<input type="checkbox" id="read_chk" /><span><label for="read_chk">已阅读并同意</label><a href="javascript:void(0);" onclick="window.open('/pages/guide/daiGou.jsp')">19e火车票线下代购服务协议</a></span>
                    	</td>
                    </tr>
                </table>
                <div class="spacer"></div>
                <p>
                	<input type="button" class="btn step"  value="下一步" />
                </p>
                </form>
            </div>
            <!--我要加盟-tab end-->
        	
        </div>
        <!--左边内容 end-->
    </div>
    </div>
    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
<script type="text/javascript" language="JavaScript">
	$().ready(function(){
		$(":button.step").click(function(){
			var isVaild = true;
			$(":input[type='text'],select").each(function(){
				if(checkEmpty($(this).attr("id"), $(this).parents("tr").find("th").text())){
					isVaild = false;
					return false;
				}
			});
			if(isVaild && $(".website_kinds").find(":input:radio:checked").length==0){
				showErrMsg("radio_sj", "120px", "请选择店铺类型！");
				isVaild = false;
			}
			if(isVaild && !$("#read_chk").attr("checked")){
				showErrMsg("read_chk", "160px", "请勾选是否同意代购协议！");
				isVaild = false;
			}
			if(isVaild){
				$("form:first").submit();
			}
		});
		
		$(":input[type='text'],select").blur(function(){
			if($.trim($(this).val())!=""){
				hideErrMsg($(this).attr("id"));
			}
		});
		
		$("#read_chk").click(function(){
			if($("#read_chk").attr("checked")){
				hideErrMsg("read_chk");
			}
		});
		
		$(".website_kinds").find(":input:radio").click(function(){
			if( $(".website_kinds").find(":input:radio:checked").length>0){
				hideErrMsg("radio_sj");
			}
		})
		
		function checkEmpty(id, msg){
			if($.trim($("#"+id).val())=="" || ($("#"+id).css("color").toLowerCase()=="#aaa" && id!="shop_short_name")
				|| ($("#"+id).css("color").toLowerCase()=="#aaaaaa" && id!="shop_short_name")){
				showErrMsg(id, "120px", "请输入"+msg.replace("：", "！").replace("*",""));
				return true;
			}else if(id=='user_phone' && !/^1(3[\d]|4[57]|5[\d]|8[\d]|70)\d{8}$/g.test($.trim($("#user_phone").val()))){
				showErrMsg(id, "150px", "请输入正确的手机号！");
				return true;
			}else if(id=='shop_short_name' && $.trim($("#"+id).val())!='定制短信(左侧说明)' && countByteLength($.trim($("#"+id).val()),2)>10){
				showErrMsg(id, "170px", "店铺简称不能超过5个字！");
				return true;
			}else{
				hideErrMsg(id);
				return false;
			}
		}
		
		function showErrMsg(id, _width, msg){
			$("#"+id+"_errMsg").remove();
			var offset = $("#"+id).offset();
			$obj=$("#tip").clone().attr("id", id+"_errMsg")
				.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width}).appendTo("body");
			$obj.find(".errMsg").text(msg).end().show();
		}
		
		function hideErrMsg(id){
			$("#"+id+"_errMsg").remove();
		}
		
		function countByteLength(str, cnCharByteLen) {
			var byteLen = 0; 
			for (var i=0; i<str.length; i++){
				if ((/[\x00-\xff]/g).test(str.charAt(i)))
				byteLen += 1;
				else byteLen += cnCharByteLen;
			} 
			return byteLen;
		}
	});
</script>
</body>
</html>
