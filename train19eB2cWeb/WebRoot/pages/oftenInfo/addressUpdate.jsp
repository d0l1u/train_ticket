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
<script type="text/javascript" src="/js/address.js"></script>
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
$().ready(function(){
	var province = $.trim($("#province_val").val());
	var city = $.trim($("#city_val").val());
	var district = $.trim($("#district_val").val());
	addressInit('province', 'city', 'district', province, city, district);//省市区三级联动
});
//全局变量
var linkName = false;//收件人姓名
var addressName = false;//详细地址
var zipCode = false;//邮政编码
var addresseePhone = false;//手机号码

//验证收件人姓名是否有效
function checkName(){
	var addressee = $.trim($("#addressee").val());
    var pat=new RegExp("[^a-zA-Z\_\u4e00-\u9fa5]","i"); 
    if(addressee==""){
    	$("#addressee_info").html("<span class='no_span'></span>请输入收件人姓名");
    	linkName = false;
    }else if(pat.test(addressee)==true){
        $("#addressee_info").html("<span class='no_span'></span>请输入正确的收件人姓名");
        linkName = false;
    }else{
        var check = /^[A-Za-z]+/;
        var checkCName = /^[\u4e00-\u9fa5]+/;
        if(check.test(addressee)){
            if(addressee.length<3){
            	$("#addressee_info").html("<span class='no_span'></span>请输入正确的收件人姓名");
            	linkName = false;
            }else{
            	//$("#addressee_info").html("<span class='yes_span'></span>收件人姓名输入正确");
            	linkName = true;
            }
        }else if(checkCName.test(addressee)){
            if(addressee.length<2){
            	$("#addressee_info").html("<span class='no_span'></span>请输入正确的收件人姓名");
            	linkName = false;
            }else{
            	//$("#addressee_info").html("<span class='yes_span'></span>收件人姓名输入正确");
            	linkName = true;
            }
        }
    }
}

//验证收货详细地址
function checkAddressName(){
	var address_name = $.trim($("#address_name").val());
	if(address_name=="" || address_name=="请填写详细地址，不需要重复填写省市区/县"){
    	$("#address_name_info").html("<span class='no_span'></span>请输入详细地址");
    	addressName = false;
    }else{
    	//$("#address_name_info").html("<span class='yes_span'></span>详细地址填写正确");
    	addressName = true;
    }
}

//验证邮政编码
function checkZipcode(){
	var zip_code = $.trim($("#zip_code").val());
	var re = /^[0-9]{6}$/;
	if(zip_code==""){
		$("#zip_code_info").html("<span class='no_span'></span>请输入邮编");
		zipCode = false;
	}else if(!re.test(zip_code)){
		$("#zip_code_info").html("<span class='no_span'></span>请输入正确的邮编");
		zipCode = false;
	}else{
		//$("#zip_code_info").html("<span class='yes_span'></span>邮编填写正确");
		zipCode = true;
	}
}

//验证手机号码是否正确
function checkPhone(){
	var addressee_phone = $.trim($("#addressee_phone").val());
	if(addressee_phone==""){
    	$("#addressee_phone_info").html("<span class='no_span'></span>请输入手机号码");
    	addresseePhone = false;
    }else{
        var reg = /^1(3[d]|4[57]|5[d]|8[d]|70|78)d{8}$/g;
    	if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test(addressee_phone)){
    		$("#addressee_phone_info").html("<span class='no_span'></span>请输入正确的手机号码");
    		addresseePhone = false;
        }else{
        	//$("#addressee_phone_info").html("<span class='yes_span'></span>手机号码填写正确");
        	addresseePhone = true;
        }
    }
}

function saveAddress(){
	checkName();
	checkAddressName();
	checkZipcode();
	checkPhone();
	if(linkName == true && addressName == true && zipCode == true && addresseePhone == true){
		var address_id = $.trim($("#address_id").val());
		var addressee = $.trim($("#addressee").val());
		var province = $.trim($("#province").val());
		var city = $.trim($("#city").val());
		var district = $.trim($("#district").val());
		var address_name = $.trim($("#address_name").val());
		var zip_code = $.trim($("#zip_code").val());
		var addressee_phone = $("#addressee_phone").val();
		
		var url = "/login/updateAddress.jhtml?address_id="+address_id+"&addressee="+addressee+"&address_name="+address_name+"&addressee_phone="+addressee_phone+"&zip_code="+zip_code+"&province="+province+"&city="+city+"&district="+district+"&version="+new Date();
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
				<jsp:param name="menuId" value="postAddress" />
			</jsp:include>
	<!--左边内容 end-->
    
    
    <!--右边内容 start-->  
	<div class="right_con">
	<form id="trainForm" action="/login/updateAddress.jhtml" method="post">
		<input type="hidden" id="address_id" name="address_id" value="${addressMap.address_id }" />
		<ul class="MyOrder">
    		<li>编辑地址</li>
    	</ul>
        <table class="MyInmatable">
	        <tr>
		        <td class="right"><b>*</b>&nbsp;收件人姓名：</td>
		        <td><input type="text" id="addressee" name="addressee" class="text" onblur="checkName();" value="${addressMap.addressee }" /><span id="addressee_info"></span></td>
	        </tr>
        
	        <tr>
	        <td class="right"><b>*</b>&nbsp;所在地区：</td>
		        <td>
		        	<select id="province" name="province" class="address"></select>
					<select id="city" name="city" class="address"></select>
					<select id="district" name="district" class="address"></select>
					<input type="hidden" id="province_val" name="province_val" value="${addressMap.province }" />
					<input type="hidden" id="city_val" name="city_val" value="${addressMap.city }" />
					<input type="hidden" id="district_val" name="district_val" value="${addressMap.district }" />
		        </td>
	        </tr>

	        <tr>
		        <td class="right"><b>*</b>&nbsp;详细地址：</td>
		        <td><input type="text" id="address_name" name="address_name" class="text changads" value="${addressMap.address_name }"
		        		onfocus="if(this.value=='请填写详细地址，不需要重复填写省市区/县'){this.value=''};this.style.color='#333';" 
            			onblur="checkAddressName(); if(this.value==''||this.value=='请填写详细地址，不需要重复填写省市区/县'){this.value='请填写详细地址，不需要重复填写省市区/县';this.style.color='#aaa';}"/>
            		<span id="address_name_info"></span></td>
	        </tr>

	        <tr>
		        <td class="right"><b>*</b>&nbsp;邮政编码：</td>
		        <td><input type="text" id="zip_code" name="zip_code" class="text" value="${addressMap.zip_code }" onblur="checkZipcode();"/><span id="zip_code_info"></span></td>
	        </tr>
	        
	        <tr>
		        <td class="right"><b>*</b>&nbsp;手机号码：</td>
		        <td><input type="text" id="addressee_phone" name="addressee_phone" value="${addressMap.addressee_phone }" class="text" onblur="checkPhone();"/><span id="addressee_phone_info"></span></td>
	        </tr>
       
		</table>
        <input type="button" class="btn13 AddP_btn" onclick="saveAddress();" value="保&nbsp;&nbsp;存" />
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
                <p>修改常用邮寄地址成功！</p>
            </dd>
		</dl>
		<input type="hidden" id="land_off" />
		<input type="hidden" id="land_on" />
		<button type="button" class="btn13" onclick="window.location='/login/queryAddress.jhtml'">确&nbsp;&nbsp;定</button>
    </div>
<!-- 密码修改成功弹框end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->
</body>
</html>
