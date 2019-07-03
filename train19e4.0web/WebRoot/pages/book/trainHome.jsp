<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>车票预订页</title>
		<meta content="text/html; charset=utf-8" http-equiv=Content-Type />
		<link rel="stylesheet" href="/css/sreachbar.css" type="text/css" />
		<link rel="stylesheet" href="/css/default.css" type="text/css" />
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<style>
			.dialog_p { TEXT-INDENT: 32px; FONT: 12px/ 22px Simsun; COLOR: #333}
			.dialog_pr {  FONT: 12px/ 22px Simsun; COLOR: #333 ;margin:10px auto; width:600px; height:auto; display:block;}
			#query{ display:inline-block;  }
			#query{_zoom:1;*display:inline;} 
		</style>
		<script type=text/javascript src="/js/jquery.js"></script>
		<script type=text/javascript src="/js/My97DatePicker/WdatePicker.js"></script>
		<script type=text/javascript src="/js/artDialog.js"></script>
		<script type=text/javascript src="/js/train_station.js?version=1008"></script>
		<script type=text/javascript src="/js/warnWindow.js"></script>
		<script type="text/javascript" src="/js/layer/layer.js"></script>
		<script type=text/javascript>
 var otime = null;
 var $startMove={
 	_getStyle:function(obj,name){
 		if(obj.currentStyle){
 			return obj.currentStyle[name];
 		}else{
 			return getComputedStyle(obj,false)[name];
 		}
 	},
 	_getScroll:function(obj,json,fnEnd){
 		clearInterval(obj.otime);
 		obj.otime=setInterval(function(){
 			var ostop = true;
 			for(var att in json){
 				var curr = 0;
 				curr = parseInt($startMove._getStyle(obj,att));
 				var speed = (json[att]-curr)/7;
 				speed=speed >0 ? Math.ceil(speed): Math.floor(speed);
 				if(curr!=json[att]){
 					ostop =false;
 				}
 				obj.style[att] = (curr+speed)+'px';
 				
 			}
 			if(ostop){
 				clearInterval(obj.otime);
 				if(fnEnd)fnEnd();
 			}
 		},30)
 	}
 }
 window.onload=function(){
		var stime = null;
	 	var mBox = document.getElementById('star_shop');
	 	var stop = true;
	 	var oUl = mBox.getElementsByTagName('ul')[0];
	 	var ali = oUl.getElementsByTagName('li');
	 	oUl.innerHTML+=oUl.innerHTML
	 	var aliWidth = parseInt($startMove._getStyle(ali[0],'width'))+27;
	 	oUl.style.width=(aliWidth*ali.length+aliWidth)+'px';
	 	var i = 0;
	 	var sum =parseInt(ali.length)/2;
	 	oUl.style.left=0;
	 	function getNext(){
	 		if(stop){
			 	if(i + 1 > sum ){
			 		oUl.style.left=0;
			 		i=0;
			 	}
			 	stop=false;
				$startMove._getScroll(oUl,{left:-(aliWidth*(i+=1))},function(){
					stop=true;
				})
			}
		}
		function getPrev(){
			if(stop){
				if(i - 1 < 0  ){
			 		oUl.style.left=-(aliWidth*sum)+'px';
			 		i=sum;
			 	}
			 	stop=false;
				$startMove._getScroll(oUl,{left:-(aliWidth*(i-=1))},function(){
					stop=true;
				})
			}
		}
		stime=setInterval(getNext,5000)
		mBox.onmouseover=function(){
			clearInterval(stime);
		}
		mBox.onmouseout=function(){
			stime=setInterval(getNext,5000)
		}
	}
 	$(function(){
 		var count="0";
 		if(count=="1"){
			openLayer('inndiv','<img src="/images/warn.jpg" alt="" ></img>',467,171);
			setTimeout('closeLayer()',4200);
			//startOpen();
 		}
 	});
 	//获取n天后的日期
	function adddays(n) {   
   		var newdate=new Date();   
   		var newtimems=newdate.getTime()+(n*24*60*60*1000);   
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
 		var m=document.getElementById("book_day_num").value;
 		var i=parseInt(m);
 		//去除 加10天预约购票服务
 		//var j=i-1+10;
 		var j=i-1;
 	 	//var arr = new Array(adddays(i), adddays(i+1), adddays(i+2), adddays(i+3), adddays(i+4), adddays(i+5), adddays(i+6), adddays(i+7), adddays(i+8), adddays(i+9));
 		//WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+29}', specialDates:arr}); 
 		 var arr = new Array(adddays(parseInt(m)), adddays(parseInt(m)+1), adddays(parseInt(m)+2), adddays(parseInt(m)+3), adddays(parseInt(m)+4), 
		 adddays(parseInt(m)+5), adddays(parseInt(m)+6), adddays(parseInt(m)+7), adddays(parseInt(m)+8), adddays(parseInt(m)+9));
		WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+'+j+'}'}); 
 	}

	//暂停订购通知
 	function booktype(){
		var sys_weather_book=$("#sys_weather_book").val();
 		if(sys_weather_book=="0"){
	 		var s="";
			var theDate=new Date();
			s += theDate.getYear()+"年  ";                         // 获取年份。
			s += (theDate.getMonth() + 1) + "月  ";            // 获取月份。
			s += theDate.getDate() +"日  ";             // 获取日。
			var dialog = art.dialog({
				lock: true
				,fixed: true
				,width: '650px'
				,height: '220px'
				,left: '250px'
				,top: '100px'
				,title: '通知'
				,okVal: '我知道了'
				,content: '<p style="font:bold 14px/30px Simsun;margin-left: 35%;">火车票暂时停止订购通知</p>'
						+'<p class="dialog_pr" >尊敬的代理商：<br/></p>'
					    +'<p class="dialog_pr" >&nbsp;&nbsp;&nbsp;&nbsp;您好，由于铁路系统故障，19e平台火车票业务暂时停止购票。我们正在努力解决，恢复订购后，我们会第一时间公告通知。给您带来的不便敬请谅解！<br/></p>'
					    +'<p class="dialog_pr"><br/></p>'
					    +'<p class="dialog_pr" style="text-align:right;"> 火车票业务组<br/></p>'
					    +'<p class="dialog_pr" style="text-align:right;">'+s+'  <br/></p>'
				,ok: function(){
					return true;
				}
			});
		}
 	 	}

	//赠送保险通知
 	function alertInsurance(){
		var sys_weather_book=$("#sys_weather_book").val();
		var alert_insurance=$("#alert_insurance").val();
 		if(sys_weather_book!="0" && alert_insurance == "1"){
			var dialog = art.dialog({
				lock: true
				,fixed: true
				,width: '650px'
				,height: '220px'
				,left: '250px'
				,top: '100px'
				,title: '赠险通知'
				,okVal: '我知道了'
				,content: '<p style="font:bold 14px/30px Simsun;margin-left: 35%;">火车票下单赠送免费保险通知</p>'
						+'<p class="dialog_pr" >尊敬的代理商：<br/></p>'
					    +'<p class="dialog_pr" >&nbsp;&nbsp;&nbsp;&nbsp;您好，目前19e开展免费送保险活动，只要乘客年龄在25-50周岁之间，下单即为您免费赠送高保额达<font color="red"><b>25万</b></font>的保险，足足保障您60天的安全出行，这样的好事，还等什么，让我们为您的出行保驾护航！<a href="javascript:tjInsurance();" style="color:blue;">查看详情</a>。<br/></p>'
					    +'<p class="dialog_pr" >&nbsp;&nbsp;各位代理赶快为乘客的出行领取一份保障吧！领取成功与否以手机下发<font color="red"><b>短信</b></font>为准，谢谢！<br/></p>'
					    +'<p class="dialog_pr"><br/></p>'
					    +'<p class="dialog_pr" >&nbsp;&nbsp;本活动最终解释权归19e所有<br/></p>'
					    +'<p class="dialog_pr"><br/></p>'
					    +'<p class="dialog_pr" style="text-align:right;"> 火车票业务组<br/></p>'
					    +'<p class="dialog_pr" style="text-align:right;"> 2015年10月13日  <br/></p>'
				,ok: function(){
					return true;
				}
			});
		}
 	 	}
 	 	
	function tjInsurance(){
		$.ajax({
			url:"/tjInsurance/clickNumAdd.jhtml?type=11&version="+new Date(),
			type: "POST",
			cache: false,
			dataType: "text",
			async: true,
			success: function(data){
			if(data != "success"){
				return;
			}else{
			window.open('http://www.ilovepingan.com/qi/activities/index.action?act=wangyi_edm1-2&channel=gudadianshang1509');
			}
		 }
		});
	}
</script>
	</head>
	<body >
		<div id="inndiv"></div>
		<!-- 
		<div class="content shouy oz">
			
		</div> -->
		<div class="index_all">
			<!--左边内容 start-->
				<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="bookHome" />
				</jsp:include>
			<!--左边内容 end-->
			<!--右边部分start-->
			<div id="new_right">
				<div class="idx_ban_fl left" id="idx_ban_fl" style="cursor:pointer;">
					<ul class="Slides">
						<li>
							<img src="/images/index_banner8.jpg" width="790" height="315" onclick="javascript:tjInsurance();" />
						</li> 
						<li>
							<img src="/images/index_banner4.jpg" width="790" height="315" onclick="window.open('http://image30.19360.cn/resources/page/20150112143636/hcpx/index.html')" />
						</li> 
						<li>
							<img src="/images/index_banner2.jpg" width="790" height="315" onclick="window.open('http://image30.19360.cn/resources/page/20141028111415/hcp/index.html')" />
						</li>
					<!--	
						<li>
							<img src="/images/index_banner6.jpg" width="790" height="315" onclick="window.open('http://image30.19360.cn/resources/page/20150528175432/111/index.html')" />
						</li> 
						<li>
							<img src="/images/index_banner1.jpg" width="790" height="315" onclick="window.open('http://image30.19360.cn/resources/page/20141107165144/hcp/index.html')" />
						</li>
						<li>
							<img src="/images/index_banner3.jpg" width="790" height="315" onclick="window.open('http://image30.19360.cn/resources/page/20140516140214/sjyyxj/index.html')" />
						</li>  
					-->
					</ul>
					<ol class="SlideTriggers">
						<li class="on">1</li>
						<li>2</li>
						<li>3</li>
					</ol>
				</div>
			
				<script type="text/javascript">
          $('#idx_ban_fl ol  li').mouseover(function(e){
                   $(this).addClass('on').siblings().removeClass();
                   var myindex=$('#idx_ban_fl ol li').index(this);
                   //var num=-(myindex*mywt);
                   $('#idx_ban_fl .Slides li').eq(myindex).fadeIn().siblings().hide(); 
                   mynum=myindex+1;
         });
         var mynum=1;
         function autoplay(){
                   if(mynum==3)
                   {
                    mynum=0;
                   }
                   $('#idx_ban_fl ol  li').eq(mynum).addClass('on').siblings().removeClass();
                   //$('#idx_ban_fl .Slides').animate({left:-(mynum*mywt)});
				   $('#idx_ban_fl .Slides li').eq(mynum).fadeIn().siblings().hide();
                   mynum++;
         }
         var timer=setInterval(autoplay,5000);
         $('#idx_ban_fl').hover(function(e){
                   clearInterval(timer);
         },function(){
               timer=setInterval(autoplay,5000);
         })

 
 </script>


				<!--搜索框模块-->
				<div class="block-content trainIndex_box_shadow trainIndex_shadow">
					<div id="changeTrainSearchCity" class="changeIcon" onclick="swapCity();"
						style="display: block;"></div>
					<div class="search-title">
						<div class="shouy_ser">
							<h3>
								<span class="titimg fl"></span>
								<span>国内火车票</span>
							</h3>
						</div>
					</div>
			<form id="trainForm" action="/buyTicket/queryByStation.jhtml" 
            	method="post" >
                <input name="from" class="from" type="hidden" />
                <input name="to" class="to" type="hidden" />
                <input type="hidden" id="book_day_num" name="book_day_num" value="${book_day_num}"/>
                <input type="hidden" id="sys_weather_book" name="sys_weather_book" value="${sys_weather_book}"/>
                <input type="hidden" id="alert_insurance" name="alert_insurance" value="${alert_insurance}"/>
             		<div id="search_info" class="search_info clearfix">
						<dl id="leaveCity" class="list">
							<dt class="list_tit">
								<span class="tips1">出发城市 </span>
							</dt>
							<dd>
								<input class="input06" type="text" id="fromZh" name="from_city" 
									value="出发城市" onfocus="showCity('fromZh')" onblur="hideCity()" />
							</dd>
						</dl>
						<dl id="arriveCity" class="list">
							<dt class="list_tit">
								到达城市
							</dt>
							<dd>
                    			<input class="input06" type="text" id="toZh" name="to_city" 
                    				onfocus="showCity('toZh')" onblur="hideCity()" value="到达城市" />
							</dd>
						</dl>
						<dl id="leaveDate" class="list">
							<dt class="list_tit">
								出发日期
							</dt>
							<dd>
                    			<input class="input06" type="text" id="travel_time" name="travel_time" 
                    				value="${paramMap.travel_time}" readonly="readonly" onfocus="myfunction(this)" />
							</dd>
						</dl>
						<input type="hidden" name="travel_day" value="${paramMap.travel_day}"/>
						<dl id="onlyGCD" class="list" style="margin-bottom: 0;">
							<dd class="dtd">
								<label>
									<input id="checkGcd" name="checkGcd" class="chk fl" type="checkbox" value="on"></input>
									<span class="fl fsize14" style="padding-left: 4px;">
										只要GC高铁/D动车 </span>
								</label>
							</dd>
						</dl>
						<input type="hidden" name="travel_day" value="${paramMap.travel_day}"/>
						<dl class="list01" style="margin-bottom: 0;">
							<dd class="dtd">
								<input type="button" class="submitSearch btn01"  id="query" value="查&nbsp;&nbsp;&nbsp;&nbsp;询" />
							</dd>
						</dl>
					</div>
				</form>
				</div>
			

				<!--中间图标start-->
				<div class=tools-box>
					<ul>
					<li class="tool-01">
						<a href="/query/queryOrderList.jhtml">
							<span></span>
							<p>订单查询</p>
						</a>
					</li>
					<li class="tool-02">
						<a href="/query/queryOrderList.jhtml?selectType=2">
							<span></span>
							<p>我要退票</p>
						</a>
					</li>
					<li class="last-li tool-06">
						<a href="/pages/guide/newGouPiao.jsp">
							<span></span>
							<p>如何订票</p>
						</a>
					</li>
					<li class="tool-04">
						<a href="/pages/guide/newQuPiao.jsp">
							<span></span>
							<p>如何取票</p>
						</a>
					</li>
					<li class="tool-03">
						<a href="/pages/guide/newHuoQuLiRun.jsp">
							<span></span>
							<p>如何返利</p>
						</a>
					</li>
					<li class="tool-05">
						<a href="/pages/guide/questionGouPiao.jsp?color=1">
							<span></span>
							<p>为什么实名认证</p>
						</a>
					</li>
				</ul>
				</div>
				<!--中间图标end-->


				<!--中奖感言分享start-->
				<h3 class="winnersshare">
					<span>中奖感言分享</span>
				</h3>

				<div class="star-shop-con oz" id="star_shop">
                <div class="winningDiv1">
                	<ul >
                		<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/13931075930.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：139****5930<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：邯郸-无锡<span style="margin-left:28px;">￥141.5元</span></p>
			        			<p >获奖感言：火车票业务这个免单活动非常好，而且自从开通火车票业务后，订票方便了不少，带动了店里的其他业务，希望19e越做越好。</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/15813448898.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：158****8898<span style="margin-left:38px;">银牌用户</span></p>
			        			<p >免费订购：天津-聊城<span style="margin-left:28px;">￥62.5元</span></p>
			        			<p >获奖感言：非常感谢19e给我们带来的便利，火车票业务更是给我们带来了很多客源，带动了店里的其他业务，希望19e越做越好。</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/15250113018.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：152****3018<span style="margin-left:38px;">银牌用户</span></p>
			        			<p >免费订购：苏州-南京<span style="margin-left:28px;">￥99.5元</span></p>
			        			<p >获奖感言：感谢19e提供了一个这么好的平台，不仅方便了附近的居民，也带动了我店里的其他业务，提高了收益。</p>
			        			</div>
			        	</div>
			        	</li>
			        	
                		<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/18956179299.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：189****9299<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：亳州-辛集<span style="margin-left:28px;">￥150元</span></p>
			        			<p >获奖感言：火车票业务这个免单活动非常好，让我们代理商获得了实实在在的实惠，同时火车票也带动了我店里其他业务营业额的增长，祝火车票业务越办越好！</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/13201791168.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：132****1168<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：民权-西安<span style="margin-left:28px;">￥80元</span></p>
			        			<p >获奖感言：自19e火车票开展免单活动以来，一直希望自己也能中奖，没想到真中了，很意外也很开心，希望火车票业务多举办些类似的活动，我会一直支持19e的。</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/13866665803.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：138****5803<span style="margin-left:38px;">银牌用户</span></p>
			        			<p >免费订购：合肥-兰考<span style="margin-left:28px;">￥61元</span></p>
			        			<p >获奖感言：感谢19e提供了一个这么好的平台，不仅方便了附近居民，也带动了我店里的其他业务，我代表附近的居民感谢19e，希望19e越办越好。</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			        		<div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/13174476723.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：131****6723<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：长春-延吉<span style="margin-left:28px;">￥66.0元</span></p>
			        			<p >获奖感言：在得知自己中奖后很意外，自从开通了火车票业务，来我这购票的住户越来越多，也希望19e火车票能越办越好</p>
			        			</div>
			        		</div>
			        	</li>
			        	
			        	<li >
			        		<div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/18796877866.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：187****7866<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：昆山-三门峡<span style="margin-left:28px;">￥148.5元</span></p>
			        			<p >获奖感言：被通知中奖后我很意外也很高兴，非常感谢19e给我们提供了这样一个真心为老百姓服务的平台。</p>
			        			</div>
			        		</div>
			        	</li>
			        	
			        	<li >
			        		<div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/18717188158.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：187****8158<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：武昌-长沙<span style="margin-left:42px;">￥53.5元</span></p>
			        			<p >获奖感言：在得知自己第二次中奖后特别高兴，比买彩票好，挣钱的同时还能中奖，让我挣的比以前多多啦，希望以后多些这样的活动，我会积极参加。</p>
			        			</div>
			        		</div>
			        	</li>
			        	<li >
			        		<div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/13856987575.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：138****7575<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：合肥-随州<span style="margin-left:42px;">￥75.0元</span></p>
			        			<p >获奖感言：感谢19e火车票业务给我这个获奖名额，通过火车票业务的开通，使我的营业额显著提高。</p>
			        			</div>
			        		</div>
			        	</li>
			            <li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/13057566628.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：130****6628<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：南京-邹城<span style="margin-left:28px;">￥138.0元</span></p>
			        			<p >获奖感言：感谢火车票业务给予这样一个中奖机会，火车票业务的开通，带动了其他业务的增长，使我受益颇丰。</p>
			        			</div>
			        	</div>
			        	</li>
			            <li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/13522665818.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：135****5818<span style="margin-left:38px;">银牌用户</span></p>
			        			<p >免费订购：北京西-济源<span style="margin-left:28px;">￥98.0元</span></p>
			        			<p >获奖感言：在得知自己中奖后很意外，也很高兴，火车票业务不但方便了自己的出行，同时也方便了周边住户，现在来我这购票的住户越来越多。</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/18956080225.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：189****0225<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：郑州-邯郸<span style="margin-left:28px;">￥81.0元</span></p>
			        			<p >获奖感言：感谢火车票业务给予我这个获奖名额，自从开通了火车票业务,店里的人流量和营业额大幅度增长，来我店里买票的人越来越多。</p>
			        			</div>
			        	</div>
			        	</li>
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/15936934088.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：159****4088<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：合肥-桐城<span style="margin-left:28px;">￥102.0元</span></p>
			        			<p >获奖感言：有幸获奖，感谢19e给我这个获奖名额。火车票业务的开通也带动了我店里的其他业务营业额的增长，希望火车票业务能越办越好</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/15505519198.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：155****9198<span style="margin-left:38px;">银牌用户</span></p>
			        			<p >免费订购：合肥-桐城<span style="margin-left:28px;">￥18.5元</span></p>
			        			<p >获奖感言：被通知中奖后我很开心，感谢19e火车票业务能大幅度提升我的营业额，也希望能多举办一些这样的活动。</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/13862073176.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：138****3176<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：苏州-阜阳<span style="margin-left:28px;">￥86元</span></p>
			        			<p >获奖感言：得知中奖后特别开心。希望所有的代理商都有机会获奖。现在我们小区周边居民买票很方便，感谢19e全体员工的辛劳和不断的创新。</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/15575578666.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：155****8666<span style="margin-left:38px;">金牌用户</span></p>
			        			<p >免费订购：衡阳东-韶关<span style="margin-left:28px;">￥149.5元</span></p>
			        			<p >获奖感言：被通知中奖后心情非常激动。感谢19e给予我这个获奖名额。同时也祝19e的加盟店越来越多，大家生意越做越红火！</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        	<li >
			            <div class="winningDiv2">
			        			<div class="winningDiv3">
			        				<img src="/images/13395692233.jpg" alt="" />
			        			</div>
			        			<div class="winningDiv4">
			        			<p >代理商：133****2233<span style="margin-left:38px;">银牌用户</span></p>
			        			<p >免费订购：合肥-长兴南<span style="margin-left:28px;">￥46.5元</span></p>
			        			<p >获奖感言：感谢19e提供这么好的平台，让我的小店慢慢的火爆了起来，得知自己获奖了，真是特别意外，特别惊喜。也希望19e火车票业务能做的更好！</p>
			        			</div>
			        	</div>
			        	</li>
			        	
			        </ul>
                </div>
            </div>
			<!--中奖感言分享end-->
			<!--海报下载start-->
			<div class="ques oz" style="MARGIN-TOP: 19px; border:1px solid #dfeae6;">
	           	<h3 class="winnersshare" style="border-top:0; border-left:0; border-right:0;">
				    <span> 海报下载</span>
				</h3>
                <div class="content stuffload oz">
			        <ul class="mb10 oz">
			        	<li>
			            	<h2>海报1<a style="color:#fff;margin-left:150px;text-decoration:underline;" href="/download/haibao_1.rar" >下载</a></h2>
			        		<p class="img"><img src="/images/stuff1.gif" alt="" /></p>
			        	</li>
			            <li>
			            	<h2>海报2<a style="color:#fff;margin-left:150px;text-decoration:underline;" href="/download/haibao_2.rar" >下载</a></h2>
			        		<p class="img"><img src="/images/stuff2.gif" alt="" /></p>
			        	</li>
			            <li>
			            	<h2>海报3<a style="color:#fff;margin-left:150px;text-decoration:underline;" href="/download/haibao_3.rar" >下载</a></h2>
			        		<p class="img"><img src="/images/stuff3.gif" alt="" /></p>
			        	</li>
			        </ul>
			       <p style="margin-left:210px;color:#ff8e17;">说明：以上物料的规格均为  高576mm-宽427mm-出血3mm-300dpi</p>
			       <br/>
    			</div>
            </div>
    	<!--海报下载end-->
    	</div>
</div>
<!--右边部分end-->
<input type="hidden" id="isAgentLogin" value="${isAgentLogin }" />
<input type="hidden" id="is_newLogin" value="${is_newLogin }" />

<div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
<script type="text/javascript" language="JavaScript">
	$().ready(function(){
		booktype();alertInsurance();
		var $startMove={
			_getStyle:function(obj,name){
				if(obj.currentStyle){
					return obj.currentStyle[name];
				}else{
					return getComputedStyle(obj,false)[name];
				}
			},
			_getScroll:function(obj,json,fnEnd){
				clearInterval(obj.otime);
				obj.otime=setInterval(function(){
					var ostop = true;
					for(var att in json){
						var curr = 0;
						curr = parseInt($startMove._getStyle(obj,att));
						var speed = (json[att]-curr)/7;
						speed=speed >0 ? Math.ceil(speed) : Math.floor(speed);
						if(curr!=json[att]){
							ostop =false;
						}
						obj.style[att] = (curr+speed)+'px';
						
					}
					if(ostop){
						clearInterval(obj.otime);
						if(fnEnd)fnEnd();
					}
				},30)
			}
		}
		
		var isAgentLogin = $.trim($("#isAgentLogin").val());
		var is_newLogin = $.trim($("#is_newLogin").val());
		if(isAgentLogin=='0' || is_newLogin=='0'){
			//SVIP服务的弹框
			var dialog = art.dialog({
				lock: true
				,fixed: true
				,width: '700px'
				,height: '300px'
				,left: '120px'
				,top: '50px'
				,title: 'SVIP专享权限说明'
				,okVal: '我知道了'
				,content: '<p style="font:bold 14px/30px Simsun;">尊敬的代理商，您好！</p>'
					    +'<p class="dialog_p">火车票业务为了回馈代理商及用户，特别开通SVIP服务功能。<br/></p>'
					    +'<p class="dialog_p" style="color:red;">乘客在购买火车票时代理商可推荐选择SVIP服务，选择该服务每笔订单增加5元服务费，其中当即返给代理商2元。<br/><br/></p>'
					    +'<p class="dialog_p" style="font-weight:bold;">选择SVIP服务乘客可以得到以下专享服务：</p>'
					    +'<p class="dialog_p">1)乘客可以享受提前30天预约购买火车票（待距离发车前20天铁路系统开放购买之后短信通知用户是否购票成功）；</p>'
					    +'<p class="dialog_p">2)可享受购票失败短信通知服务；</p>'
					    +'<p class="dialog_p">3)乘客可以在发车前4小时以外拨打客服热线400-688-2666申请退票服务；</p>'
					    +'<p class="dialog_p">4)免费赠送价值10万元交通意外保险。<br/><br/></p>'
					    +'<p class="dialog_p" style="font-weight:bold;">选择SVIP服务代理商可以得到以下专享优惠：</p>'
					    +'<p class="dialog_p">1)每笔SVIP订单以坐扣的形式当即返还2元；</p>'
					    +'<p class="dialog_p">2)每周对购买20元保险和选择SVIP服务的订单进行抽奖，抽中的代理商该笔订单免费。<br/><br/></p>'
				,ok: function(){
					return true;
				}
			});
		}
		
		/*
		window.onload=function(){
			var stime = null;
		 	var mBox = document.getElementById('star_shop');
		 	var prevBtn = document.getElementById('prev');
		 	var nextBtn = document.getElementById('next');
		 	var stop = true;
		 	var oUl = mBox.getElementsByTagName('ul')[0];
		 	var ali = oUl.getElementsByTagName('li');
		 	oUl.innerHTML+=oUl.innerHTML
		 	var aliWidth = parseInt($startMove._getStyle(ali[0],'width'))+8;
		 	oUl.style.width=(aliWidth*ali.length+aliWidth)+'px';
		 	var i = 0;
		 	var sum =parseInt(ali.length)/2;
		 	oUl.style.left=0;
		 	function getNext(){
		 		if(stop){
				 	if(i + 1 > sum ){
				 		oUl.style.left=0;
				 		i=0;
				 	}
				 	stop=false;
					$startMove._getScroll(oUl,{left:-(aliWidth*(i+=1))},function(){
						stop=true;
					})
				}
			}
			function getPrev(){
				if(stop){
					if(i - 1 < 0  ){
				 		oUl.style.left=-(aliWidth*sum)+'px';
				 		i=sum;
				 	}
				 	stop=false;
					$startMove._getScroll(oUl,{left:-(aliWidth*(i-=1)),height:86},function(){
						stop=true;
					})
				}
			 	
			}
			stime=setInterval(getNext,5000)
			mBox.onmouseover=function(){
				clearInterval(stime);
			}
			mBox.onmouseout=function(){
				stime=setInterval(getNext,5000)
			}
			nextBtn.onclick=function(){
			 	clearInterval(stime);
			 	getNext();
			}
			prevBtn.onclick=function(){
			 	clearInterval(stime);
			 	getPrev();
			}

		}**/

		
		$("#query").click(function(){
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

			var isAgentLogin='';
			isAgentLogin=${isAgentLogin}
			//判断是不是第一次登陆
			//if(isAgentLogin == '0'){
				//获取该用户是不是已经实名
				var isCheckUserInfo='';
				isCheckUserInfo=${isCheck};
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
		});
		
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
		
		function showErrMsg(id, _width, msg){
			$("#"+id+"_errMsg").remove();
			var offset = $("#"+id).offset();
			$obj=$("#tip").clone()
				.attr("id", id+"_errMsg")
				.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width}).appendTo("body");
			$obj.find(".errMsg").text(msg).end().show();
		}
		
		function hideErrMsg(id){
			$("#"+id+"_errMsg").remove();
		}
	});

	function openNewActivityPage(){
		$('.window_opacity').hide();
		window.open('http://image30.19360.cn/resources/page/20140331150530/hcpmd/index.html');
	}

	function swapCity(){
		var fromCity = $("#fromZh").val().replace("出发城市", "");
		var toCity = $("#toZh").val().replace("到达城市", "");
		$("#fromZh").val(toCity);
		$("#toZh").val(fromCity);
	}

	function openStep2(){
		$('.step1').hide();
		$('.step2').show();
	}
	function openStep3(){
		$('.step2').hide();
		$('.step3').show();
	}
	function openStep4(){
		$('.step3').hide();
		$('.contact_guide').show();
	}
	function openStepConcat(){
		$('.contact_guide').hide();
		$('.step4').show();
	}
	function openStep5(){
		$('.step4').hide();
		$('.step1').show();
	}
	function closeAllShow(){
		$('.window_opacity').hide();
	}
</script>

</body>
</html>
