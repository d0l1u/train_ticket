<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
		<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
		<link rel="stylesheet" href="/css/sreachbar.css" type="text/css"/>
		<link rel="stylesheet" href="/css/default.css" type="text/css" />
		<link rel="stylesheet" href="/css/style.css" type="text/css"/>
		<script type=text/javascript src="/js/jquery.js"></script>
		<script type=text/javascript src="/js/My97DatePicker/WdatePicker.js"></script>
		<script type=text/javascript src="/js/artDialog.js"></script>
		<script type=text/javascript src="/js/train_station.js?version=1008"></script>
		<script type=text/javascript src="/js/warnWindow.js"></script>
		<script type="text/javascript" src="/js/layer/layer.js"></script>
		
		<script type="text/javascript">
			//获取n天后的日期
			function adddays(n) {   
		   		var newdate = new Date();   
		   		var newtimems = newdate.getTime()+(n*24*60*60*1000);   
		   		newdate.setTime(newtimems);   
		   	 	var month = newdate.getMonth()+1;
		   	 	month<10 ? month='0'+month : month=month;
		   	 	var day = newdate.getDate();
		   	 	day<10 ? day='0'+day : day=day;
		   	 	//只得到年月日的   
		   	 	var newDay = newdate.getFullYear()+"-"+month+"-"+day;
		   	 	return newDay;
		  	}  
		  	//出发日期--日历控件
		 	function myfunction(element){
		 	 	//var arr = new Array(adddays(20), adddays(21), adddays(22), adddays(23), adddays(24), adddays(25), adddays(26), adddays(27), adddays(28), adddays(29));
		 		//WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+29}', specialDates:arr}); 
		 		WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+59}'}); 
		 	}
		 	//交换“出发城市和到达城市”
			function swapCity(){
				var isValid=true;
				if($.trim($("#fromZh").val())=="" || $.trim($("#fromZh").val())=="出发城市"){
					showErrMsg("fromZh", "110px", "请输入出发城市！");
					isValid=false;
				}else{
					hideErrMsg("fromZh");
				}
				if($.trim($("#toZh").val())=="" || $.trim($("#toZh").val())=="到达城市"){
					showErrMsg("toZh", "110px", "请输入到达城市！");
					isValid=false;
				}else{
					hideErrMsg("toZh");
				}
				if(isValid==false){
					return;
				}
				var fromCity = $("#fromZh").val().replace("出发城市", "");
				var toCity = $("#toZh").val().replace("到达城市", "");
				$("#fromZh").val(toCity);
				$("#toZh").val(fromCity);
			}
			//点击“查询”按钮
			function queryTrain(){
				var isValid=true;
				if($.trim($("#fromZh").val())=="" || $.trim($("#fromZh").val())=="出发城市"){
					showErrMsg("fromZh", "110px", "请输入出发城市！");
					isValid=false;
				}else{
					hideErrMsg("fromZh");
				}
				if($.trim($("#toZh").val())=="" || $.trim($("#toZh").val())=="到达城市"){
					showErrMsg("toZh", "110px", "请输入到达城市！");
					isValid=false;
				}else{
					hideErrMsg("toZh");
				}
				if($("#travel_time").val()==""){
					showErrMsg("travel_time", "110px", "请输入出发日期！");
					isValid=false;
				}else{
					hideErrMsg("travel_time");
				}
				if(isValid==false){
					return;
				}
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '250px',
					title: 'Loading...',
					icon: "/images/loading.gif",
					content: '正在查询，请稍候！'
				});
				$(".aui_titleBar").hide();
				$("form:first").submit();
			}
			//热门城市的热门线路搜索
			function queryTrainInfo(from_station,arrive_station){
				var isValid=true;
				if(from_station!=null && from_station!=""){
					$("#fromZh").val(from_station);
				}	
				if(arrive_station!=null&&arrive_station!=""){
					$("#toZh").val(arrive_station);
				}
				if($("#travel_time").val()==""){
					$("#travel_time").val(adddays(2));
				}
				
				//消息框	
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '250px',
				    title: 'Loading...',
				    icon: "/images/loading.gif",
				    content: '正在查询，请稍候！'
				});
				$(".aui_titleBar").hide();
				$("form:first").submit();
	
			}
			//如果点击继续购买，则调用此函数
			function continueSub(){
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '250px',
				    title: 'Loading...',
				    icon: "/images/loading.gif",
				   	content: '正在查询，请稍候！'
				});
				$(".aui_titleBar").hide();
				$("form:first").submit();
			}
			//显示错误信息
			function showErrMsg(id, _width, msg){
				$("#"+id+"_errMsg").remove();
				var offset = $("#"+id).offset();
				$obj=$("#tip").clone().attr("id", id+"_errMsg")
					.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width}).appendTo("body");
				$obj.find(".errMsg").text(msg).end().show();
			}
			//隐藏错误信息
			function hideErrMsg(id){
				$("#"+id+"_errMsg").remove();
			}
			//选择热门城市
			function selectHotCity(element){
				$(element).addClass('current').siblings().removeClass('current');
				//alert($(element).parents('#hot_city').find('#hot_way').find('div:eq('+($(element).index()-1)+')').html());
				$(element).parents('#hot_city').find('#hot_way').find('div:eq('+($(element).index()-1)+')').addClass('hot_con').siblings().removeClass('hot_con');
			}

			//热门城市的线路选择
			$().ready(function(){
				$("#hot_city dt span").mouseover(function(){
					$(this).addClass('current').siblings().removeClass('current');
					$(this).parents('#hot_city').find('div:eq('+($(this).index())+')').addClass('current').siblings().removeClass('current');
				})
			});	
			function toHref(id, url){
				var index = id.substring(12);
				var oldId = $(".current_onclick").attr("id");
				$("#"+oldId).removeClass("current_onclick");
				$("#"+id).addClass("current_onclick");
				//alert(oldId+"   "+id);
				window.location = url;
			}
		</script>
</head>
<body>
<!--以下是头部logo部分start -->
<jsp:include flush="true" page="/pages/common/headerNav.jsp">
	<jsp:param name="menuId" value="hcp" />
</jsp:include>
<!--以下是头部logo部分end -->


<!--以下是banner部分 -->

<script type="text/javascript">
$().ready(function(){
	$(".banner_all ol li").mouseover(function(e) {/*给圆点绑定鼠标移上事件，鼠标移上之后，对应的图片显示，以及圆点改变样式*/
        var myindex=$(this).index();
		$(this).addClass("current_ol").siblings().removeClass();
		$(".banner_all ul li").eq(myindex).fadeIn().siblings().hide();
    });
	
	var mymum=0;

	function autoplay (){/*定时器函数，改变图片的显示和ol里li的样式*/
		if(mymum>2){mymum=0}
		$(".banner_all ul li").eq(mymum).fadeIn().siblings().hide(); 
		
		$(".banner_all ol li").eq(mymum).addClass("current_ol").siblings().removeClass();
		mymum++;
	}
		var timer = setInterval(autoplay,2000);
	$(".banner_all ul li").hover( function(){/*给圆点绑定事件，鼠标移上之后定时器停止。鼠标离开之后图片继续轮播*/
		clearInterval(timer);
		},function(){
		timer = setInterval(autoplay,2000);
		})
});
</script>
<div id="banner">
	<div class="banner_all">
		<ul>
    		<li><img src="/images/banner1.jpg" width="2000" height="350" /></li>
    
    	</ul>
    	<!--<ol>
   		  <li class="current_ol"></li>
    		<li></li>
    		<li></li>
    	</ol>
 -->
	</div>


<!--搜索框模块-->
	<div class="sreachbarbg">
    <div class="block-content trainIndex_box_shadow trainIndex_shadow">
    	<div id="changeTrainSearchCity" class="changeIcon" style="display: block;" onclick="swapCity();"></div>
        <div class="search-title">
	        <div class="shouy_ser">
	            <h3>国内火车票</h3>
	        </div>
        </div>
        <form id="trainForm" action="/buyTicket/queryByStation.jhtml" method="post" >
			<input name="from" class="from" type="hidden" />
            <input name="to" class="to" type="hidden" />
        <div id="search_info" class="search_info clearfix">
            <dl id="leaveCity" class="list">
                <dt class="list_tit">
                    <span class="tips1">出发城市</span>
                </dt>
                <dd>
                    <input class="input06" type="text" id="fromZh" name="from_city" value="出发城市" onfocus="showCity('fromZh');" onblur="hideCity();" />
                </dd>
            </dl>
            <dl id="arriveCity" class="list">
                <dt class="list_tit"> 到达城市</dt>
                <dd>
                    <input class="input06" type="text" id="toZh" name="to_city" onfocus="showCity('toZh')" onblur="hideCity()" value="到达城市" />
                </dd>
            </dl>
            <dl id="leaveDate" class="list">
                <dt class="list_tit">出发日期</dt>
                <dd>
                    <input class="input06" type="text" id="travel_time" name="travel_time" value="${paramMap.travel_time}" readonly="readonly" onfocus="myfunction(this);" />
                </dd>
            </dl>
            <input type="hidden" name="travel_day" value="${paramMap.travel_day}"/>
            <dl id="onlyGCD" class="list" style="margin-bottom:0;">
                <dd class="dtd">
                    <label>
                        <input id="checkGcd" name="checkGcd" class="chk fl" type="checkbox" value="on"></input>
                        <span class="fl fsize14" style="padding-left: 4px;">只要GC高铁/D动车</span>
                    </label>
                </dd>
            </dl>
            <dl class="list" style="margin-bottom:0;margin-left:10px;">
                <dd class="dtd">
	                <input id="query" class="submitSearch btn01" type="button" style="display: inline-block;" 
	                	value="查&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;询" onclick="queryTrain();"></input>
                </dd>
            </dl>
        </div>
        </form>
	</div>
	</div>
</div>
<!--以下是订单详情order情部分 -->
<div id="order_all">
	<dl>
		<dt><img src="/images/search_icon.gif" width="65" height="60" /></dt>
		<dd>
        	<h3>订单查询</h3>
            <p>快速查询订单状态</p>
        </dd>
	</dl>
	<dl>
	  <dt><img src="/images/qupiao.gif" width="65" height="60" /></dt>
		<dd>
       	  <h3>如何取票</h3>
            <p>查看线下取票方式</p>
        </dd>
    </dl>
    <dl>
		<dt><img src="/images/out_icon.gif" width="65" height="60" /></dt>
		<dd>
        	<h3>我要退票</h3>
            <p>快速退票为您解忧</p>
        </dd>
	</dl>
    <dl>
		<dt><img src="/images/news_icon.gif" width="65" height="60" /></dt>
		<dd>
        	<h3>铁路资讯</h3>
            <p>了解最新铁路信息</p>
        </dd>
	</dl>
</div>

<!--以下是热门城市部分 -->
<div class="hot_citybg">
<div id="hot_city" class="hot_city">
<dl>
	<dt class="hot_tit">
    	<h3>热门城市</h3>
        <p class="city">
        	<c:forEach var="list" items="${highTrainList}" varStatus="idx">
				<c:if test="${idx.index == 0}">
					<span class="current"><a href="javascript:void(0);">${list.key}</a></span>
		        </c:if>
		        <c:if test="${idx.index != 0}">
			        <span><a href="javascript:void(0);">${list.key}</a></span>
		        </c:if>
			</c:forEach>
        </p>
    </dt>
    </dl>
    <c:forEach var="trainInfo" items="${highTrainList}" varStatus="no">
		<c:if test="${no.index == 0}">
	    	<div class="hot_con current">
				<c:forEach var="train" items="${trainInfo.value}" varStatus="num">
				<dl>
				    <dt><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');">${train.from_station} →  ${train.arrive_station}</a></dt>
	    			<dd><b>¥${train.price}元</b>起</dd> 
	    		</dl>
    			</c:forEach>  
    		</div>
		</c:if>
		<c:if test="${no.index != 0}">
			<div class="hot_con">
			    <c:forEach var="train" items="${trainInfo.value}" varStatus="num">
			    <dl>
				    <dt><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');">${train.from_station} → ${train.arrive_station}</a></dt>
	    			<dd><b>¥${train.price}元</b>起</dd> 
	    		</dl>
    			</c:forEach>
    		</div>
		</c:if>
	</c:forEach>
</div>
</div>

<!--以下是我们为您提供部分 -->
<div id="offer">
	<div class="offer_in">
    	<h3>我们为您提供</h3>
        <div class="offer_con">
		    <dl>
		        <dt><img src="/images/booking.jpg" width="65" height="60" /></dt>
		        <dd>
        	    <p><b>提前预售</b><br />可提前30天预售火车票</p>
                </dd>
	        </dl> 	
		    <dl>
		        <dt><img src="/images/tecket.jpg" width="65" height="60" /></dt>
		        <dd>
        	    <p><b>上车补票</b><br />帮您先上车，余站后补票</p>
                </dd>
	         </dl> 	
		     <dl>
		        <dt><img src="/images/qupiao_w.jpg" width="65" height="60" /></dt>
		        <dd>
        	    <p><b>取票快捷</b><br />任一火车站或代售点取票</p>
                </dd>
	         </dl> 	
		     <dl>
		        <dt><img src="/images/outw_icon.jpg" width="65" height="60" /></dt>
		        <dd>
        	    <p><b>随时退票</b><br />发车前3小时多渠道退票</p>
                </dd>
	         </dl> 	
        </div>
    </div>
</div>

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

<div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>

</body>
</html>