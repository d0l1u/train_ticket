<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css"/>
<link rel="stylesheet" href="/css/sreachbar.css" type="text/css"/>
<link rel="stylesheet" href="/css/travel.css" type="text/css"/>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/dialog.js"></script>
<script type="text/javascript" src="/js/idCard.js"></script>
<style type="text/css">
/****订单弹出框*****/
#drawBill{ position:fixed;_position: absolute;_top: expression(documentElement.scrollTop + 340 + "px");
	 background:#fff; width:400px; height:280px; border:1px solid #86CBFF; top:50%; left:50%; 
	 margin: -200px 0 0 -250px; overflow:hidden; z-index:999999; font-size:14px; color:#000; display:none;}
* html,* html body{background-image:url(about:blank);background-attachment:fixed} 
* html .ie6fixedTL{position:absolute;left:expression(eval(document.documentElement.scrollLeft));top:expression(eval(document.documentElement.scrollTop))} 
* html .ie6fixedBR{position:absolute;left:expression(eval(document.documentElement.scrollLeft+document.documentElement.clientWidth-this.offsetWidth)-(parseInt(this.currentStyle.marginLeft,10)||0)-(parseInt(this.currentStyle.marginRight,10)||0));top:expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||0)-(parseInt(this.currentStyle.marginBottom,10)||0)))} 
</style>

<script type="text/javascript">
//全局变量
var linkName = false;
var idsCard = false;
var linkPhone = false;

//验证姓名是否有效
function checkName(){
	var link_name = $.trim($("#link_name").val());
    var pat=new RegExp("[^a-zA-Z\_\u4e00-\u9fa5]","i"); 
    if(link_name==""){
    	$("#link_name_info").html("<span class='no_span'></span>请输入乘客姓名");
    	linkName = false;
    }else if(pat.test(link_name)==true){
        $("#link_name_info").html("<span class='no_span'></span>请输入正确的姓名");
        linkName = false;
    }else{
        var check = /^[A-Za-z]+/;
        var checkCName = /^[\u4e00-\u9fa5]+/;
        if(check.test(link_name)){
            if(link_name.length<3){
            	$("#link_name_info").html("<span class='no_span'></span>请输入正确的姓名");
            	linkName = false;
            }else{
            	$("#link_name_info").html("<span class='yes_span'></span>乘客姓名输入正确");
            	linkName = true;
            }
        }else if(checkCName.test(link_name)){
            if(link_name.length<2){
            	$("#link_name_info").html("<span class='no_span'></span>请输入正确的姓名");
            	linkName = false;
            }else{
            	$("#link_name_info").html("<span class='yes_span'></span>乘客姓名输入正确");
            	linkName = true;
            }
        }
    }
}

//验证身份证是否有效
function valiIdCard(){
	var ids_card = $.trim($("#ids_card").val());
	if(ids_card==""){
    	$("#ids_card_info").html("<span class='no_span'></span>请输入乘客证件号码");
    	idsCard = false;
    }else if($("#ids_type").val()=='2'){//二代身份证
    	if(ids_card.length!=18){
   			$("#ids_card_info").html("<span class='no_span'></span>请输入正确的证件号码");
   			idsCard = false;
   		}
   		var checkFlag = new clsIDCard(ids_card);
   		if(!checkFlag.IsValid()){
   			$("#ids_card_info").html("<span class='no_span'></span>请输入正确的证件号码");
   			idsCard = false;
   		}else{
   			$("#ids_card_info").html("<span class='yes_span'></span>证件号码正确");
   			idsCard = true;
   		}	
    }
}

//验证手机号码是否正确
function checkPhone(){
	var link_phone = $.trim($("#link_phone").val());
	if(link_phone==""){
    	//$("#link_phone_info").html("<span class='no_span'></span>请输入手机号码");
    	linkPhone = true;
    }else{
        var reg = /^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g;
    	if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test(link_phone)){
    		$("#link_phone_info").html("<span class='no_span'></span>请输入正确的手机号码");
    		linkPhone = false;
        }else{
        	$("#link_phone_info").html("<span class='yes_span'></span>手机号码填写正确");
        	linkPhone = true;
        }
    }
}

function savePassenger(){
	checkName();
	valiIdCard();
	checkPhone();
	if(linkName == true && idsCard == true && linkPhone == true){
		var link_name = $.trim($("#link_name").val());
		var ids_card = $.trim($("#ids_card").val());
		var link_phone = $.trim($("#link_phone").val());
		var ids_type = $("#ids_type").val();
		var passenger_type = $("#passenger_type").val();
		
		var url = "/login/addPassenger.jhtml?link_name="+link_name+"&ids_card="+ids_card+"&link_phone="+link_phone+"&ids_type="+ids_type+"&passenger_type="+passenger_type+"&version="+new Date();
		$.post(encodeURI(url),function(data){
	    	if(data == 'success'){
	    		var dialog = new popup("land_on","drawBill","land_off");
				$("#land_on").click();
		    }
	  	});
	}
}
</script>
</head>

<body>
<!--以下是头部logo部分start -->
<jsp:include flush="true" page="/pages/common/headerNav.jsp">
	<jsp:param name="menuId" value="lx" />
</jsp:include>
<!--以下是头部logo部分end -->


<!--以下是我的旅行正文内容travel_con部分start -->
<div class="travel_con">
	<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menuLeft.jsp">
				<jsp:param name="menuId" value="passInfo" />
			</jsp:include>
	<!--左边内容 end-->
    
    
    <!--右边内容 start-->  
	<div class="right_con">
	<form id="trainForm" action="/login/addPassenger.jhtml" method="post">
		<ul class="MyOrder">
    		<li>添加新乘客</li>
    	</ul>
        <table class="MyInmatable">
        <tr>
	        <td class="right"><b>*</b>&nbsp;姓名：</td>
	        <td><input type="text" class="text" id="link_name" name="link_name" onblur="checkName();" /></td>
	        <td id="link_name_info"></td>
        </tr>
        <tr>
	        <td class="right"><b>*</b>&nbsp;乘客类型：</td>
	        <td>
	        	<select id="passenger_type" name="passenger_type">
		        	<option value="0">成人</option>
                   	<option value="1">儿童</option>
	        	</select>
	        </td>
	        <td></td>
	    </tr>
        <!-- 
        <tr>
        <td class="right">姓别：</td>
        <td><input type="radio"  name="11"/>&nbsp;男&nbsp;&nbsp;&nbsp;
        	<input type="radio" name="11" />&nbsp;女</td>
        </tr>
        <tr>
        <td class="right">国家/地区：</td>
        <td><input type="text"  class="text"/></td>
        </tr>
		 -->
        <tr>
	        <td class="right"><b>*</b>&nbsp;证件类型：</td>
	        <td><select id="ids_type" name="ids_type">
	        		 <option value="2">二代身份证</option>
                     <option value="3">港澳通行证</option>
                     <option value="4">台湾通行证</option>
                     <option value="5">护照</option>
	        	</select>
	        </td>
	        <td></td>
        </tr>
        <tr>
	        <td class="right"><b>*</b>&nbsp;证件号码：</td>
	        <td><input type="text" class="text" id="ids_card" name="ids_card" onblur="valiIdCard();" /></td>
	        <td id="ids_card_info"></td>
        </tr>
        <tr>
	        <td class="right">手机号码：</td>
	        <td><input type="text" class="text" id="link_phone" name="link_phone" onblur="checkPhone();" /></td>
	        <td id="link_phone_info"></td>
        </tr>
		</table>
        <!-- 
        <button type="button" class="btn13 AddP_btn">保&nbsp;&nbsp;存</button>
        <button type="button" class="btn6 AddP_btn" style=" margin-left:40px;">取&nbsp;&nbsp;消</button>
         -->
        <input type="button" class="btn13 AddP_btn" onclick="savePassenger();" value="保&nbsp;&nbsp;存" />
        <input type="button" class="btn13 AddP_btn" style=" margin-left:40px;" onclick="javascript:history.back(-1);" value="取&nbsp;&nbsp;消" />
        <br/><br/><br/><br/>
    </form>
	</div>
  	<!--右边内容 end-->
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- 密码修改成功弹框start -->
  	<div class="password" id="drawBill">
    	<dl>
			<dt>
				<strong></strong>
			</dt>
			<dd>
                <p>恭喜您，新增乘客成功！</p>
            </dd>
		</dl>
		<input type="hidden" id="land_off" />
		<input type="hidden" id="land_on" />
		<button type="button" class="btn13" onclick="window.location='/login/queryPassenger.jhtml'">确&nbsp;&nbsp;定</button>
    </div>
<!-- 密码修改成功弹框end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->
</body>
</html>
