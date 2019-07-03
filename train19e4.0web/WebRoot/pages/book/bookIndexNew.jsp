<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%> 
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
<title>bookIndexNew</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/css/sreachbar.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/css/default.css" />
<link rel="stylesheet" href="/css/style.css?version=10010" type="text/css" />
<style>.dialog_p {TEXT-INDENT: 32px; FONT: 12px/22px Simsun; COLOR: #333;}
.yupiao{text-align:center;border:none;color:#fff;background-color:#ff6600;cursor:pointer;width:60px;height:20px;}

.yw{border-bottom: 1px dotted #f60;}
.rw{border-bottom: 1px dotted #f60;}
.yw_msg{border:1px solid #87B2D2;background-color:#EFEFEF; color:#555555; font-family:"Microsoft YaHei";width: 120px;height: 50px;position:absolute;display: none;}
</style>
<script type=text/javascript src="/js/jquery.js"></script>
<script type=text/javascript src="/js/My97DatePicker/WdatePicker.js"></script>
<script type=text/javascript src="/js/artDialog.js"></script>
<script type=text/javascript src="/js/train_station.js"></script>
<script type=text/javascript src="/js/warnWindow.js"></script>
<script type=text/javascript src="/js/newbanner aside.js"></script>
<script type="text/javascript" src="/js/train_station.js?version=10010"></script>
<script>
// 抢票controller入口
    function qiangpiaogo(obj,seatType){
		alert("2017-07-03--2017-07-06 抢票功能维护中!");
		return false;
    	var tr = $(obj).parents("tr");
        var travelTime =$("#travel_time").val() ;
        var trainCode = $(tr.find(".checi")[0]).text();
        var startCity = $(tr.find(".startname")[0]).text();
        var endCity = $(tr.find(".endname")[0]).text();
        var startTime = $(tr.find("td")[2]).children("span").text();
        var endTime = $(tr.find("td")[2]).text().substring(5);
        var costTime_str = $(tr.find("td")[3]).text();
        var defaultSelect = seatType;
        var seatMsg = $("#seatMsg_"+trainCode).val();
        costTime_str = costTime_str.replace("分","");
        var minuteCos = costTime_str.split("小时");
        var costTime = parseInt(minuteCos[0])*60+parseInt(minuteCos[1]);
        var is_buyable="11";
        if(is_buyable == "00"){
            alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
            return false;
        }
        var url="/rob/robTickect.jhtml?travelTime="+travelTime+"&trainCode="+trainCode+"&startCity="+startCity+"&endCity="+endCity+"&startTime="+startTime+"&endTime="+endTime+"&costTime="+costTime+"&seatMsg="+seatMsg+"&defaultSelect="+defaultSelect;
        window.location = encodeURI(url);
}
</script>
</head>
<body>
<div class="content shouy oz">
	<div class=slogan></div>
</div>
  <!--导航条 end-->
<div class="index_all">
<!--左边内容 start-->
	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
				</jsp:include>
<!--左边内容 end-->
<!--右边部分start-->
    	<div class="rightIndex_con oz">
            <div class="tip_term oz" style="width:785px;">
            	<p class="price_tip"></p>
            </div>
            <form id="trainForm" action="/buyTicket/queryByStation.jhtml" method="post" >
             <div class="ser_tab oz" style="margin-top:5px;">
            	<ul class="tab">
                </ul>
                <div class="tab_list oz">
                    <div class="shou_query">
		                <dl class="shou_query_oz">
		                	<dt>出发城市</dt>
		                    <dd><input class="text" type="text" id="fromZh" name="from_city" 
		                    		onfocus="showCity('fromZh')" onblur="hideCity()" value="${paramMap.from_city}" /></dd>
		                </dl>
		                <div class="swap" onclick="swapCity();"></div>
		                <dl class="shou_query_oz" style="margin-left:35px;">
		                	<dt>到达城市</dt>
		                    <dd><input class="text" type="text" id="toZh" name="to_city" 
		                    		onfocus="showCity('toZh')" onblur="hideCity()" value="${paramMap.to_city}" /></dd>
		                </dl>
		                <div style="position:absolute;left:646px;width:60px">
								普通:<input id="adult_type" name="t_type" type="radio" value="ADULT" <c:if test="${t_type ne '0X00' }">checked="checked"</c:if>>
								学生:<input id="stu_type" name="t_type" type="radio" value="0X00" <c:if test="${t_type eq '0X00' }">checked="checked"</c:if>>
						</div>
		                <div id="query" class="search_query search_hover" onmouseover="this.className='search_query search_link'" onmouseout="this.className='search_query search_hover'">查询</div>
		                <dl class="shou_query_oz">
		                	<dt>出发日期</dt>
		                    <dd><input class="text text2" type="text" id="travel_time" name="travel_time" 
		                    		value="${paramMap.travel_time}" readonly="readonly" onfocus="myfunction(this)" /></dd>
		                </dl>
		            </div>
                 </div>
            </div>

            <div class="ser_term oz">
                <h3>
                	<span style="color:#32B1F0; font-size:14px;  font-weight:bold;">${paramMap.from_city}</span>
                	<span class="nav_middle_bg">→</span>
                	<span style="color:#32B1F0; font-size:14px;  font-weight:bold;">${paramMap.to_city}</span>&nbsp;${paramMap.travel_time}（<c:choose>
                		<c:when test="${osnd ==null && unBookList==null && otVo!=null}">
                			<span class="amount">共<strong id="train_total">${fn:length(otVo.dataList)}</strong>个车次</span>
                		</c:when>
                		<c:when test="${osnd !=null && unBookList==null && otVo==null}">
                			<span class="amount">共<strong id="train_total">${fn:length(osnd.datajson)}</strong>个车次</span>  
               		 	</c:when>
               		 	<c:when test="${osnd ==null && unBookList!=null && otVo==null}">
               				<span class="amount">共<strong id="train_total">${fn:length(unBookList)}</strong>个车次</span>  
               		 	</c:when>
               		 	<c:otherwise>
               		 		<span class="amount">共<strong id="train_total">0</strong>个车次</span>
               		 	</c:otherwise>
                	</c:choose>）
                    
                </h3>
                
		<div class="choose_databox">
			<span id="front_jiantou" class="choose_jiantou" onclick="toForwordDay();"></span> 
 			<div class="choose_li">
                 <ul>
                 	<c:forEach items="${dayAndWeek}" var="list" varStatus="idx">
                 		<c:forEach items="${beforeAndAfter4Days}" var="daylist" >
                 			<c:choose>
                 			<c:when test="${fn:contains(list.day, daylist.days) }">
                 				<c:choose>
                 					<c:when test="${list.day eq travelDay}">
                 						<li class="current_choose show_li" id="li_${idx.index+1}"><span>${list.day }<input type="hidden" id="li_span_${idx.index+1}" value="${list.newday }" /></span> <span>${list.week }</span></li>
                 					</c:when>
                 					<c:otherwise>
                 						<li id="li_${idx.index+1}" class="show_li"><span>${list.day }<input type="hidden" id="li_span_${idx.index+1}" value="${list.newday }" /></span> <span>${list.week }</span></li>
                 					</c:otherwise>
                 				</c:choose>
                 			</c:when>
                 			<c:otherwise>
                 			</c:otherwise>
                 			</c:choose>
                 		</c:forEach>
                 		<c:if test="${fn:contains(nodays, list.day) }">
                 			<li class="hide_li" id="li_${idx.index+1}"><span>${list.day }<input type="hidden" id="li_span_${idx.index+1}" value="${list.newday }" /></span> <span>${list.week }</span></li>
                 		</c:if>
                 	</c:forEach>
				</ul>
			</div>
			<span id="end_jiantou" class="choose_jiantour" onclick="toNextDay();"></span> 
		</div>
                <script type="text/javascript" language="JavaScript">
	                $(document).ready(function () {  
	               	    var m=document.getElementById("book_day_num").value;
	               	    $(".yw").hover(function (e) {  
	               	     	   var top = e.pageY+5;
							   var left = e.pageX+5;
			                    var pos = {//定位popudiv，这里以在td右边显示为例子，左边跟上边的时候还要计算popudiv的尺寸 
			                       'top' : top + 'px',
							    'left': left+ 'px'
			                    } 
			                    $('#yw_msg').css(pos).show();//设置left，top，并显示出来 
	                    }, function () {  
	                    	 $('#yw_msg').hide();//鼠标离开，div隐藏 
	                  	});
	                   $(".rw").hover(function (e) {  
	               	     	   var top = e.pageY+5;
							   var left = e.pageX+5;
			                    var pos = {//定位popudiv，这里以在td右边显示为例子，左边跟上边的时候还要计算popudiv的尺寸 
			                       'top' : top + 'px',
							    'left': left+ 'px'
			                    } 
			                    $('#yw_msg').css(pos).show();//设置left，top，并显示出来 
	                    }, function () {  
	                    	 $('#yw_msg').hide();//鼠标离开，div隐藏 
	                  	});
	               	    
	               		$(".choose_li li").hover(function () {  
	                    	$(this).addClass("current_hover"); //鼠标悬浮：添加背景色 
	                    }, function () {  
	                    	$(this).removeClass("current_hover"); //鼠标离开：去掉背景色
	                  	});

	                  	if($("#li_1").is(":visible")){//第一个li显示，则不能再向前
		                  	$("#front_jiantou").removeClass("choose_jiantou");
		                  	$("#front_jiantou").addClass("choose_jiantouend");//不能选
		                }
	                  	if($("#li_"+parseInt(m)).is(":visible")){//最后一个li显示，则不能再向后
		                  	$("#end_jiantou").removeClass("choose_jiantour");
		                  	$("#end_jiantou").addClass("choose_jiantourend");//不能选
		                }
	               	});
	               	//向前翻
                	function toForwordDay(){
                		// 获取第一个显示show_li的ID
                		var id = $(".show_li").attr("id");// li_12
                		var index = id.substring(3);// 12
                		var m=document.getElementById("book_day_num").value;
                		if(index==1){//第一个li显示，则不能再向前
                			$("#front_jiantou").removeClass("choose_jiantou");
		                  	$("#front_jiantou").addClass("choose_jiantouend");//不能选
                    		return;
                    	}else{
                        	if($("#front_jiantou").hasClass("choose_jiantouend")){
                        		$("#front_jiantou").addClass("choose_jiantou");
    	                  		$("#front_jiantou").removeClass("choose_jiantouend");//不能选
                            }
                        }
                		                    	
                		$("#li_"+(parseInt(index)-1)).addClass("show_li");//前一个显示
                    	$("#li_"+(parseInt(index)-1)).removeClass("hide_li"); 
                    	
                    	$("#li_"+(parseInt(index)+8)).addClass("hide_li");//最后一个隐藏
                    	$("#li_"+(parseInt(index)+8)).removeClass("show_li");
                    	if($(".show_li").attr("id").substring(3)==1){//第一个li显示，则不能再向前
                			$("#front_jiantou").removeClass("choose_jiantou");
		                  	$("#front_jiantou").addClass("choose_jiantouend");//不能选
                    	}else if($(".show_li").attr("id").substring(3)<parseInt(m)-8){//第一个li显示，则不能再向前
                			$("#end_jiantou").addClass("choose_jiantour");
		                  	$("#end_jiantou").removeClass("choose_jiantourend");//不能选
                    	}
                    	addWidth();
                    }
                	//向后翻
                	function toNextDay(){
                		// 获取第一个显示show_li的ID
                		var id = $(".show_li").attr("id");// li_12
                		var index = id.substring(3);// 12
                		var m=document.getElementById("book_day_num").value;
                		//alert(index);
                		if(index==parseInt(m)-9){//最后一个li显示，则不能再向后
                			$("#end_jiantou").removeClass("choose_jiantour");
		                  	$("#end_jiantou").addClass("choose_jiantourend");//不能选
                    	}else if(index>=parseInt(m)-8){//最后一个li显示，则不能再向后
                			$("#end_jiantou").removeClass("choose_jiantour");
		                  	$("#end_jiantou").addClass("choose_jiantourend");//不能选
		                  	return;
                    	}else{
                    		if($("#end_jiantou").hasClass("choose_jiantourend")){
                        		$("#end_jiantou").addClass("choose_jiantour");
    	                  		$("#end_jiantou").removeClass("choose_jiantourend");//不能选
                            }
                        }    
                       // alert(index+"隐藏："+"#li_"+(parseInt(index)));                	
                		$("#li_"+(parseInt(index))).removeClass("show_li");//第一个隐藏
                    	$("#li_"+(parseInt(index))).addClass("hide_li"); 
                    	//alert("显示："+"#li_"+(parseInt(index)+9));
                    	$("#li_"+(parseInt(index)+9)).removeClass("hide_li");//最后一个+1显示
                    	$("#li_"+(parseInt(index)+9)).addClass("show_li");
                    	//$("#li_"+(parseInt(index)+9)).attr("onClick","show_li_click();");
                    	if($(".show_li").attr("id").substring(3)==2){//第一个li显示，则不能再向前
                        	//alert("1");
                			$("#front_jiantou").addClass("choose_jiantou");
		                  	$("#front_jiantou").removeClass("choose_jiantouend");//不能选
                    	}else if($(".show_li").attr("id").substring(3)<parseInt(m)-8){//第一个li显示，则不能再向前
                			$("#end_jiantou").addClass("choose_jiantour");
		                  	$("#end_jiantou").removeClass("choose_jiantourend");//不能选
                    	}
                    	addWidth();
                    }

            	    
                    function addWidth(){//当前样式不显示的时候，加宽li的宽度
                    	var id = $(".current_choose").attr("id");
                    	if($("#"+id).is(":hidden")){
                    		$(".show_li").addClass("addwidth");
                    	}else{
                    		$(".show_li").removeClass("addwidth");
                        }
                    }

                    
                </script>
                
                
                
                
                
                
                
                <div class="box oz">
                    <!--查询条件区域-->
                     <ul class="sel_checkbox oz" id="sel_checkbox">
                            <li id="trainType">
                                <strong>车型选择：</strong>
                                <label id="selectALLTrain" class="choose_all" for="sx_cb1">全选</label>
                                <!-- 
                                <input type="checkbox" id="trainType_GD" class="filter trainType" value="G/D" <c:if test="${fn:contains(trainType,'G') || fn:contains(trainType,'D') }">checked="checked"</c:if> /><label for="trainType_GD">G高铁/D动车</label>
                                <input type="checkbox" id="trainType_G" class="filter trainType" value="G" <c:if test="${fn:contains(trainType,'G') }">checked="checked"</c:if> /><label for="trainType_G">G高铁</label>
                                <input type="checkbox" id="trainType_C" class="filter trainType" value="C" <c:if test="${fn:contains(trainType,'C') }">checked="checked"</c:if> /><label for="trainType_C">C城际</label>
                                 -->
                                <input type="checkbox" id="trainType_GD" class="filter trainType" value="G/D" /><label for="trainType_GD">G高铁/D动车</label>
                                <input type="checkbox" id="trainType_G" class="filter trainType" value="G" /><label for="trainType_G">G高铁</label>
                                <input type="checkbox" id="trainType_C" class="filter trainType" value="C" /><label for="trainType_C">C城际</label>
                                <input type="checkbox" id="trainType_D" class="filter trainType" value="D"/><label for="trainType_D">D动车</label>
                                <input type="checkbox" id="trainType_Z" class="filter trainType" value="Z"/><label for="trainType_Z">Z专列</label>
                                <input type="checkbox" id="trainType_T" class="filter trainType" value="T"/><label for="trainType_T">T特快</label>
                                <input type="checkbox" id="trainType_K" class="filter trainType" value="K"/><label for="trainType_K">K快速</label>
                                <input type="checkbox" id="trainType_L" class="filter trainType" value="L/PT"/><label for="trainType_L">L临客|普快</label>
                                <!--<span id="sx_txt" class="xianshi_box">更多筛选条件</span>-->
                            </li>
                            <li id="fromTime">
                                <strong>发车时间：</strong>
                                <label id="selectALLTime" class="choose_all" for="sx_cb1">全选</label>
                                <span><input type="checkbox" id="fromTime_1" class="filter startTime" value="fzs"/><label for="fromTime_1">早上(00:00-06:00)</label></span>
                                <span><input type="checkbox" id="fromTime_2" class="filter startTime" value="fam"/><label for="fromTime_2">上午(06:00-12:00)</label></span>
                                <span><input type="checkbox" id="fromTime_3" class="filter startTime" value="fpm"/><label for="fromTime_3">下午(12:00-18:00)</label></span>
                                <span><input type="checkbox" id="fromTime_4" class="filter startTime" value="fws"/><label for="fromTime_4">晚上(18:00-24:00)</label></span>
                            </li>
                             <!-- 发车站选择 -->
                            <li id="fromStation">
                            </li>
                            <!-- 到站选择 -->
                            <li id="toStation" style="display:none">
                            </li>
                            <li id="toTime" style="display:none">
                                <strong>到达时间：</strong>
                                <label id="selectALLTo" class="choose_all" for="sx_cb1">全选</label>
                                <span><input type="checkbox" id="toTime_1" class="filter arriveTime" filter="t" value="tzs"><label for="toTime_1">早上(00:00-06:00)</label></span>
                                <span><input type="checkbox" id="toTime_2" class="filter arriveTime" filter="t" value="tam"><label for="toTime_2">上午(06:00-12:00)</label></span>
                                <span><input type="checkbox" id="toTime_3" class="filter arriveTime" filter="t" value="tpm"><label for="toTime_3">下午(12:00-18:00)</label></span>
                                <span><input type="checkbox" id="toTime_4" class="filter arriveTime" filter="t" value="tws"><label for="toTime_4">晚上(18:00-24:00)</label></span>
                            </li>
                            <!-- 可否预订 -->
                            <c:if test="${unBookList==null}">
	                            <li id="startAfter" style="display:none">
	                            	<strong>可否预订：</strong>
						            <label id="selectStartAfter" class="choose_all" for="sx_cb1">全选</label>
						            <span><input type="checkbox" id="ableBook" class="filter weatherBook" value="预 订" filter="o"><label for="ableBook">可以预订</label></span>
						            <span><input type="checkbox" id="unableBook" class="filter weatherBook" value="抢 票" filter="o"><label for="unableBook">抢  票</label></span>
	                            </li>
	                        </c:if>
                        </ul> 
                        <div class="down-arrow" id="ser-more"><a href="javascript:void(0);">更多筛选条件</a></div>
                </div> 
              </div>
              <div class="ser_result">  
                <table cellpadding="0" cellspacing="0">
                    <tr class="tit">
                        <th width="12%">车次/车型</th>
                        <th width="16%">发站/到站</th>
                        <th width="12%">发车/到达</th>
                        <th width="12%">运行时间</th>
                        <th style="width:34%;padding-left:25px;text-align:left;">参考票价[余票]</th>
                        <th width="14%"></th>
                        <input type="hidden" id="book_day_num" name="book_day_num" value="${book_day_num}"/>
                        
                    </tr>
                    <c:if test="${ otVo ==null && osnd==null && unBookList == null}">
	                    <tr>
	                    	<td colspan="6" style="font:bold 16px/60px 'microsoft yahei','宋体';color:#f60;">对不起，暂无您查询的车次信息！</td>
	                    </tr>
                    </c:if>
                    
                    <!-- 新接口展示页面 -->
                    <c:if test="${otVo ==null && unBookList==null && osnd!=null}">
	                    <c:if test="${osnd.datajson == null || fn:length(osnd.datajson)==0}">
		                    <tr>
		                    	<td colspan="6" style="font:bold 16px/60px 'microsoft yahei','宋体';color:#f60;">对不起，暂无您查询的车次信息！</td>
		                    </tr>
	                    </c:if>
	                    <div id="yw_msg" class="yw_msg">注：暂收下铺价格，出票后根据实际票价退还差价。可选座位。</div>
	                    <c:forEach items="${osnd.datajson}" var="list">
	                    	<c:if test="${!empty list.station_train_code}">
	                    		<tr class="trainBar" id="${list.train_no}_ticket_${fn:substring(list.station_train_code, 0, 1)}_${tn:fromTimeStr(list.start_time)}_${tn:toTimeStr(list.arrive_time)}" startTime="${list.start_time}">
			                        <td class="wayStastion">
			                            <strong class="checi" id="${list.train_no}">${list.station_train_code}</strong></br>
			                            <div class="down-arrow" value="${list.from_station_name}_${list.to_station_name}"><a href="javascript:void(0);">途经站</a></div>
			                        </td>
			                        <td class="adr">
			                        	<c:if test="${list.from_station_name eq list.start_station_name}">
			                        		<span id="${list.train_no}_${list.from_station_name}" class="startname start">${list.from_station_name}</span><br />
			                        	</c:if>
			                        	<c:if test="${list.from_station_name ne list.start_station_name}">
			                        		<span id="${list.train_no}_${list.from_station_name}" class="startname pass">${list.from_station_name}</span><br />
			                        	</c:if>
			                        	<c:if test="${list.to_station_name eq list.end_station_name}">
			                        		<span class="endname end" id="${list.train_no}_${list.to_station_name}">${list.to_station_name}</span>
			                        	</c:if>
			                        	<c:if test="${list.to_station_name ne list.end_station_name}">
			                        		<span class="endname pass" id="${list.train_no}_${list.to_station_name}">${list.to_station_name}</span>
			                        	</c:if>
			                        </td>
			                        <td class="fd"><span>${list.start_time}</span><br />${list.arrive_time}</td>
			                        <td>${tn:liShiTimeStr(list.lishi)}</td>
			                        <td class="seat">
			                        	<c:if test="${(list.wz ne '-' && list.wz_num ne '-') && (list.yz_num eq '-') && (list.ze_num eq '-') }">
			                        		<p>无座<span>￥${(list.yz eq '-' || empty list.yz)?list.ze:list.yz}
			                        		<c:choose>
					                        	<c:when test="${list.wz_num_show < 10 && list.wz_num_show != 0}">
					                        		<input type="button" value="仅剩${list.wz_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.wz_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
			                        		</span></p>
			                        	</c:if>
				                        <c:if test="${!empty list.yz && list.yz ne '-' }"><p>硬座<span>￥${list.yz}
					                        <c:choose>
					                        	<c:when test="${list.yz_num_show < 10 && list.yz_num_show != 0}">
					                        		<input type="button" value="仅剩${list.yz_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.yz_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.rz && list.rz ne '-'}"><p>软座<span>￥${list.rz} 
				                        	<c:choose>
					                        	<c:when test="${list.rz_num_show < 10 && list.rz_num_show != 0}">
					                        		<input type="button" value="仅剩${list.rz_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.rz_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.zy && list.zy ne '-'}"><p>一等座<span>￥${list.zy} 
				                        	<c:choose>
					                        	<c:when test="${list.zy_num_show < 10 && list.zy_num_show != 0}">
					                        		<input type="button" value="仅剩${list.zy_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.zy_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.ze && list.ze ne '-'}"><p>二等座<span>￥${list.ze} 
				                        	<c:choose>
					                        	<c:when test="${list.ze_num_show < 10 && list.ze_num_show != 0}">
					                        		<input type="button" value="仅剩${list.ze_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.ze_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.yws && list.yws ne '-'}"><p>硬卧<span class="yw"
				                         onmousemove="myshow()" onmouseout="myhide()">￥${list.ywx} 
				                        	<c:choose>
					                        	<c:when test="${list.yw_num_show < 10 && list.yw_num_show != 0}">
					                        		<input type="button" value="仅剩${list.yw_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.yw_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.rws && list.rws ne '-'}"><p>软卧<span class="rw">￥${list.rwx} 
				                        	<c:choose>
					                        	<c:when test="${list.rw_num_show < 10 && list.rw_num_show != 0}">
					                        		<input type="button" value="仅剩${list.rw_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.rw_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${list.gws ne '-'}"><p>高级软卧<span>￥${list.gws}/${list.gwx} 
				                        	<c:choose>
					                        	<c:when test="${list.gr_num_show < 10 && list.gr_num_show != 0}">
					                        		<input type="button" value="仅剩${list.gr_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.gr_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${list.tdz ne '-'}"><p>特等座<span>￥${list.tdz} 
				                        	<c:choose>
					                        	<c:when test="${list.tz_num_show < 10 && list.tz_num_show != 0}">
					                        		<input type="button" value="仅剩${list.tz_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.tz_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${list.swz ne '-'}"><p>商务座<span>￥${list.swz} 
				                        	<c:choose>
					                        	<c:when test="${list.swz_num_show < 10 && list.swz_num_show != 0}">
					                        		<input type="button" value="仅剩${list.swz_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.swz_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p>
				                        </c:if>
				                        <c:if test="${list.dws ne '-'}"><p>动卧<span>￥${list.dws} 
				                        	<c:choose>
					                        	<c:when test="${list.dw_num_show < 10 && list.dw_num_show != 0}">
					                        		<input type="button" value="仅剩${list.dw_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        		[${list.dw_num_show}]
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p>
				                        </c:if>
			                        </td>
			                        <input type="hidden" id="seatMsg_${list.station_train_code}" value="无座_${(list.yz eq '-' || empty list.yz)?list.ze:list.yz}_${list.wz_num},硬座_${list.yz}_${list.yz_num},软座_${list.rz}_${list.rz_num},一等座_${list.zy}_${list.zy_num},二等座_${list.ze}_${list.ze_num},硬卧_${list.ywx}_${list.yw_num},软卧_${list.rwx}_${list.rw_num},高级软卧_${list.gwx}_${list.gr_num},特等座_${list.tdz}_${list.tz_num},商务座_${list.swz}_${list.swz_num},动卧_${list.dwx}_${list.dw_num}"/>
			                        <c:choose>
			                        	<c:when test="${sessionScope.showJm_Gg eq '2'}">
			                        	<td>
			                        		<input type="button" class="btn2" value="区域未开通" />
			                        	</td>
			                        	</c:when>
			                        	<c:when test="${sessionScope.menuDisplay ne 'all'}">
			                        	<td>
			                        		<input type="button" class="btn2" value="未开通2" />
			                        	</td>
			                        	</c:when>
			                        	<c:otherwise>
			                        		<c:choose>
						                        <c:when test="${list.canBook eq '1'}">
						                        	<td >
						                        		<c:if test="${(list.wz ne '-' && list.wz_num ne '-')&& (list.yz_num eq '-') && (list.ze_num eq '-') }">
						                        			<p><input type="button" id="${list.train_no}_yz_ableBook"  class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','无座');"/></p>
						                        		</c:if>
						                        		<c:if test="${list.wz_num eq '-' && list.wz ne '-' && !empty list.wz}">
						                        			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'无座');" /></p>
						                        		</c:if>
						                        		<c:if test="${list.yz ne '-' && list.yz_num ne '-'}">
						                        			<p><input type="button" id="${list.train_no}_yz_ableBook"  class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','硬座');"/></p>
						                        		</c:if>
						                        		<c:if test="${list.yz_num eq '-' && list.yz ne '-' && !empty list.yz}">
						                        			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'硬座');" /></p>
						                        		</c:if>
								                        <c:if test="${list.rz ne '-' && list.rz_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_rz_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','软座');"/></p>
								                        </c:if>
								                        <c:if test="${list.rz_num eq '-' && list.rz ne '-' && !empty list.rz}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'软座');" /></p>
								                        </c:if>
								                        
								                        <c:if test="${list.zy ne '-' && list.zy_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_rz1_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','一等座');"/></p>
								                        </c:if>
								                        <c:if test="${list.zy_num eq '-' && list.zy ne '-' && !empty list.zy}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'一等座');" /></p>
								                        </c:if>
								                        <c:if test="${list.ze ne '-' && list.ze_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_rz2_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','二等座');"/></p>
								                        </c:if>
								                        <c:if test="${list.ze_num eq '-' && list.ze ne '-' && !empty list.ze}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'二等座');" /></p>
								                        </c:if>
								                        <c:if test="${list.yws ne '-' && list.yw_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_yws_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','硬卧');"/></p>
								                        </c:if>
								                        <c:if test="${list.yw_num eq '-' && list.yws ne '-' && !empty list.yws}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'硬卧');" /></p>
								                        </c:if>
														
								                        <c:if test="${list.rws ne '-' && list.rw_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_rws_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','软卧');"/></p>
								                        </c:if>
								                        <c:if test="${list.rw_num eq '-' && list.rws ne '-' && !empty list.rws}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'软卧');" /></p>
								                        </c:if>
								                        
								                        <c:if test="${list.gws ne '-' && list.gr_num ne '-'}">
								                        	<p><input type="button" id="${list.id}_gws_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','高级软卧');"/></p>
								                        </c:if>
								                        <c:if test="${list.gr_num eq '-' && list.gws ne '-' && !empty list.gws}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'高级软卧');" /></p>
								                        </c:if>
								                        
								                        <c:if test="${list.tdz ne '-' && (list.tz_num ne '-' && list.tz_num ne '无')}">
								                        	<p><input type="button" id="${list.train_no}_tdz_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','特等座');"/></p>
								                        </c:if>
								                        <c:if test="${(list.tz_num eq '-' || list.tz_num eq '无') && list.tdz ne '-' && !empty list.tdz}">
								                        <p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'特等座');" /></p>
								                        </c:if>
								                        
								                        <c:if test="${list.swz ne '-' && list.swz_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_swz_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','商务座');"/></p>
								                        </c:if>
								                        <c:if test="${list.swz_num eq '-' && list.swz ne '-' && !empty list.swz}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'商务座');" /></p>
								                        </c:if>
								                        <c:if test="${list.dws ne '-' && list.dw_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_dw_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
						                        			onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','动卧');"/></p>
								                        </c:if>
								                        <c:if test="${list.dw_num eq '-' && list.dws ne '-' && !empty list.dws}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'动卧');" /></p>
								                        </c:if>
								                    </td>
								               </c:when>
								               <c:otherwise>
								                	<td>
														<c:if test="${list.wz ne '-' && !empty list.wz}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'无座');" /></p>
								                		</c:if>
								                		<c:if test="${list.yz ne '-' && !empty list.yz}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票"  onclick="qiangpiaogo(this,'硬座');"/></p>
								                		</c:if>
								                		<c:if test="${list.rz ne '-' && !empty list.rz}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'软座');" /></p>
								                		</c:if>
								                		<c:if test="${list.zy ne '-' && !empty list.zy}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票"  onclick="qiangpiaogo(this,'一等座');"/></p>
								                		</c:if>
								                		<c:if test="${list.ze ne '-' && !empty list.ze}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票"  onclick="qiangpiaogo(this,'二等座');"/></p>
								                		</c:if>
								                		<c:if test="${list.yws ne '-' && !empty list.yws}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'硬卧');" /></p>
								                		</c:if>
								                		<c:if test="${list.rws ne '-' && !empty list.rws}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'软卧');"/></p>
								                		</c:if>
								                		<c:if test="${list.gws ne '-' && !empty list.gws}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'高级软卧');"/></p>
								                		</c:if>
								                		<c:if test="${list.tdz ne '-' && !empty list.tdz}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'特等座');"/></p>
								                		</c:if>
								                		<c:if test="${list.swz ne '-' && !empty list.swz}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'商务座');"/></p>
								                		</c:if>
								                		<c:if test="${list.dws ne '-' && !empty list.dws}">
								                			<p><input type="button"  class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,'动卧');"/></p>
								                		</c:if>
						                        	</td>
								               </c:otherwise>
								          </c:choose>
			                        	</c:otherwise>
			                        </c:choose>
			                    </tr>
			                    <tr>
			                    	<td colspan='6' style="padding:0;border-bottom:none;">
				                    	<div id="train_station_${list.train_no}" class="train_station" style="display:none">
				                    		<div class="station_tit oz">
									   		    <span class="zx">站序</span>
									            <span class="zm">站名</span>
									            <span class="rq">天数</span>
									            <span class="dzsj">到站时间</span>
									            <span class="fcsj">出发时间</span>
									            <span class="tlsj">停留时间</span>
									            <span class="lcs">里程数</span>
									        </div>
									        <div id="station_con_${list.train_no}" class="station_con">
								            	<table> 
								            		<tbody id="station_tbody_${list.train_no}">
								            		</tbody>
								            	</table>
								            </div>
								            <b class="station_icon"></b>
				                    	</div>
			                    	</td>
			                    </tr>
	                    	</c:if>
	                    </c:forEach>
	                 </c:if>
	                 <!-- 不可预订 -->
	                 <c:if test="${otVo ==null && osnd==null && unBookList!=null}">
	                    <c:forEach items="${unBookList}" var="list">
	                    	<c:if test="${!empty list.cc && !empty list.start_time}">
	                    		<tr class="trainBar" id="${list.xh}_ticket_${list.cc}_${tn:fromTimeStr(list.start_time)}_${tn:toTimeStr(list.arrive_time)}" startTime="${list.start_time}">
			                        <td class="wayStastion">
			                            <strong class="checi" id="${list.xh}">${list.cc}</strong><br/>
			                            <div class="down-arrow" value="${list.fz}_${list.dz}"><a href="javascript:void(0);">途经站</a></div>
			                        </td>
			                        <td class="adr">
			                        	<c:if test="${list.fz eq list.start_station_name}">
			                        		<span id="${list.xh}_${list.fz}" class="startname start">${list.fz}</span><br />
			                        	</c:if>
			                        	<c:if test="${list.fz ne list.start_station_name}">
			                        		<span id="${list.xh}_${list.fz}" class="startname pass">${list.fz}</span><br />
			                        	</c:if>
			                        	<c:if test="${list.dz eq list.end_station_name}">
			                        		<span class="endname end" id="${list.xh}_${list.dz}">${list.dz}</span>
			                        	</c:if>
			                        	<c:if test="${list.dz ne list.end_station_name}">
			                        		<span class="endname pass" id="${list.xh}_${list.dz}">${list.dz}</span>
			                        	</c:if>
			                        </td>
			                        <td class="fd"><span>${list.start_time}</span><br />${list.arrive_time}</td>
			                        <td>${list.lishi}</td>
			                        <td class="seat">
				                        <c:if test="${!empty list.yz && list.yz ne '0'}">硬座<span>￥${list.yz}</span><br /></c:if>
				                        <c:if test="${!empty list.rz && list.rz ne '0'}">软座<span>￥${list.rz}</span><br /></c:if>
				                        <c:if test="${!empty list.rz1 && list.rz1 ne '0'}">一等座<span>￥${list.rz1}</span><br /></c:if>
				                        <c:if test="${!empty list.rz2 && list.rz2 ne '0'}">二等座<span>￥${list.rz2}</span><br /></c:if>
				                        <c:if test="${!empty list.yws && list.yws ne '0'}">硬卧<span>￥${list.ywx}</span><br /></c:if>
				                        <c:if test="${!empty list.ywx && list.rws ne '0'}">软卧<span>￥${list.rwx}</span><br/></c:if>
				                        <c:if test="${!empty list.dws && list.dws ne '0'}">动卧<span>￥${list.dwx}</span><br/></c:if>
			                        </td>
			                        <td>
				                        <c:choose>
				                        	<c:when test="${sessionScope.showJm_Gg eq '2'}">
				                        		<input type="button" class="btn2" value="区域未开通" />
				                        	</c:when>
				                        	<c:when test="${sessionScope.menuDisplay ne 'all'}">
				                        		<input type="button" class="btn2" value="未开通1" />
				                        	</c:when>
				                        	<c:otherwise>
						                        		<input type="button" id="${list.xh}_unableBook" class="btn2 bookTicket" value="抢 票" onclick="qiangpiaogo(this,0);" />
				                        	</c:otherwise>
				                        </c:choose>
				                        
			                        </td>
			                    </tr>
			                    <tr>
			                    	<td colspan='6' style="padding:0;border-bottom:none;">
				                    	<div id="train_station_${list.xh}" class="train_station" style="display:none">
				                    		<div class="station_tit oz">
									   		    <span class="zx">站序</span>
									            <span class="zm">站名</span>
									            <span class="rq">天数</span>
									            <span class="dzsj">到站时间</span>
									            <span class="fcsj">出发时间</span>
									            <span class="tlsj">停留时间</span>
									            <span class="lcs">里程数</span>
									        </div>
									        <div id="station_con_${list.xh}" class="station_con">
								            	<table> 
								            		<tbody id="station_tbody_${list.xh}">
								            		</tbody>
								            	</table>
								            </div>
								            <b class="station_icon"></b>
				                    	</div>
			                    	</td>
			                    </tr>
	                    	</c:if>
	                    </c:forEach>
                    </c:if>
                </table>
              </div>
              </form>	
        </div>
        <!--左边内容 end-->

</div>
<!--右边部分end-->
<div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
<script type="text/javascript" language="JavaScript">
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
	 	//var arr = new Array(adddays(20), adddays(21), adddays(22), adddays(23), adddays(24), adddays(25), adddays(26), adddays(27), adddays(28), adddays(29));
		//WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+29}', specialDates:arr}); 
		var m=document.getElementById("book_day_num").value;
		 		//去除 加10天预约购票服务
		 		var i=parseInt(m);
		 		//var j=i-1+10;
		 		var j=i-1;
		 var arr = new Array(adddays(parseInt(i)), adddays(parseInt(i)+1), adddays(parseInt(i)+2), adddays(parseInt(i)+3), adddays(parseInt(i)+4), 
		 adddays(parseInt(i)+5), adddays(parseInt(i)+6), adddays(parseInt(i)+7), adddays(parseInt(i)+8), adddays(parseInt(i)+9));
		 var is_stu = $("#stu_type").attr("checked")
			if(is_stu){
				WdatePicker({
					doubleCalendar : true,
					dateFmt : 'yyyy-MM-dd',
					minDate : '%y-%M-%d',
					maxDate : '2017-02-26' //TODO 修改此处,改变 学生票可订阅时间区间
				});
				
			}else{
				WdatePicker({
					doubleCalendar : true,
					dateFmt : 'yyyy-MM-dd',
					minDate : '%y-%M-%d',
					maxDate : '%y-%M-{%d+' + j + '}'
				});
			}

	}
	function swapCity(){
		var fromCity = $("#fromZh").val().replace("出发城市", "");
		var toCity = $("#toZh").val().replace("到达城市", "");
		$("#fromZh").val(toCity);
		$("#toZh").val(fromCity);
	}
	$().ready(function(){
		$(".show_li").live('click',function(){//点击日期，查询车次
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
	    	var id = $(".current_choose").attr("id");
	    	$("#"+id).removeClass("current_choose");//去除当前样式
			$(this).attr("class","show_li current_choose");//当前样式
			var str = $.trim($(this).text());//11-18 周三
			//alert(str);
			if($.trim($("#fromZh").val())=="" || $.trim($("#fromZh").val())=="出发城市"){
				showErrMsg("fromZh", "110px", "请输入出发城市！");
				return;
			}else{
				hideErrMsg("fromZh");
			}
			if($.trim($("#toZh").val())=="" || $.trim($("#toZh").val())=="到达城市"){
				showErrMsg("toZh", "110px", "请输入到达城市！");
				return;
			}else{
				hideErrMsg("toZh");
			}
			var newId = $(".current_choose").attr("id").substring(3);
			var date = $("#li_span_"+newId).val();
			$("#travel_time").val(date);
			if($("#travel_time").val()==""){
				showErrMsg("travel_time", "110px", "请输入出发日期！");
				return;
			}else{
				hideErrMsg("travel_time");
			}
			
			$(".aui_titleBar").hide();
			$("form:first").submit();
			
	    });
	    
		var animateAuto = function(prop, speed, callback){  
		    var elem, height, width;  
		    return this.each(function(i, el){  
		        el = jQuery(el), elem = el.clone().css({"height":"auto","width":"auto"}).appendTo("body");  
		        height = elem.css("height"),  
		        width = elem.css("width"),  
		        elem.remove();  
		        if(prop === "height")  
		            el.animate({"height":height}, speed, callback);  
		        else if(prop === "width")  
		            el.animate({"width":width}, speed, callback);    
		        else if(prop === "both")  
		            el.animate({"width":width,"height":height}, speed, callback);  
		    });    
		} 

		
		//途经站
		$(".wayStastion").click(function(){
			var chufa = $(this).children("div").attr("class");
			var checi = $(this).children("strong").attr("id");
			if(chufa=="up-arrow"){
				$("#train_station_"+checi).hide();
				$(this).children("div").attr("class","down-arrow");
				$(this).parents("tr").children("td").attr("style","border-bottom:1px;");
				$(this).parents("tr").children("td").removeAttr("style");
			}else{
				$(".up-arrow").each(function(){
					$(this).attr("class","down-arrow");
				});
				$(this).children("div").attr("class","up-arrow");
				$(this).parents("tr").children("td").attr("style","border-bottom:0px;");
				var stationame = $(this).children("div").val().split("_");
				var fromName = stationame[0];
				var toName = stationame[1];
				if($("#dis_"+checi).val()!=undefined && $("#dis_"+checi).val()!= null){
					$(".train_station").each(function(){
						$(this).hide();
					});
					//$("#train_station_"+checi).fadeTo("slow",1);
					$("#train_station_"+checi).show();
				}else{
					$.ajax({
						url:"/station/subwayName.jhtml?checi="+$(this).children("strong").text(),
						type: "POST",
						cache: false,
						dataType: "json",
						async: true,
						success: function(data){
							if(data=="" || data == null){
								return false;
							}
							var size = data.length;
							for(var i=0; i<size; i++){
								$("#station_con_"+checi).append("<div id=\"dis_"+checi+"\" value=\""+checi+"\" style=\"display:none\"></div>");
								
								if(fromName==data[i].name || toName==data[i].name){
									$("#station_tbody_"+checi).append("<tr class=\"bgc\" id=\"tr_"+i+checi+"\"></tr>");
									$("#tr_"+i+checi).append("<td style=\"width:80px;\">"+data[i].stationno+"</td>"
											+"<td style=\"width:120px;\">"+data[i].name+"</td>"
											+"<td style=\"width:90px;\">"+data[i].costtime+"</td>"
											+"<td style=\"width:110px;\">"+data[i].arrtime+"</td>"
											+"<td style=\"width:110px;\">"+data[i].starttime+"</td>"
											+"<td style=\"width:110px;\">"+data[i].interval+"</td>"
											+"<td style=\"width:110px;\">"+data[i].distance+"</td>");
								}else{
									$("#station_tbody_"+checi).append("<tr id=\"tr_"+i+checi+"\"></tr>");
									$("#tr_"+i+checi).append("<td style=\"width:80px;\">"+data[i].stationno+"</td>"
											+"<td style=\"width:120px;\">"+data[i].name+"</td>"
											+"<td style=\"width:90px;\">"+data[i].costtime+"</td>"
											+"<td style=\"width:110px;\">"+data[i].arrtime+"</td>"
											+"<td style=\"width:110px;\">"+data[i].starttime+"</td>"
											+"<td style=\"width:110px;\">"+data[i].interval+"</td>"
											+"<td style=\"width:110px;\">"+data[i].distance+"</td>");
								}
							}
							$(".train_station").each(function(){
								$(this).hide();
							});
							//动态增加显示途经站区域高度
							$("#train_station_"+checi).show();
							var down = $("#station_con_"+checi).height();
							changeHeight(down+20);
						}
					});
				}
			}
		});
		
		//更多筛选条件
		$("#ser-more").click(function(){
			if($(this).attr("class")=="down-arrow"){
				$("#toStation").show();
				$("#toTime").show();
				$("#startAfter").show();
				$(this).attr("class","up-arrow");
			}else{
				$("#toStation").hide();
				$("#toTime").hide();
				$("#startAfter").hide();
				$(this).attr("class","down-arrow");
			}
		});
		/**筛选功能初始化数组*/
		var arrType = new Array();
		var arrTime = new Array();
		var toTime = new Array();
		var arrStation = new Array();
		var toStation = new Array();
		
		/**初始化发车站、到达车站信息**/
		var arrStationName = new Array();
		var toStationName = new Array();
		var count = $("#train_total").text();
		if(count!=0){
			/**发车站信息*/
			$(".startname").each(function(){
				var value = $(this).text();
				var bo = false;
				if(arrStationName.length==0){
					arrStationName.push(value);
				}
				for(var i=0; i<arrStationName.length; i++){
					if(value==arrStationName[i]){
						bo = true;
					}
				}
				if(!bo){
					arrStationName.push(value);
				}
			});
			if(arrStationName.length>=2){
				$("#fromStation").append("<strong>发站选择：</strong>");
				$("#fromStation").append("<label id=\"selectFromStation\" class=\"choose_all\" for=\"sx_cb1\">全选</label>");
				for(var i=0; i<arrStationName.length; i++){
					$("#fromStation").append("<span><input type=\"checkbox\" id=\"fromStation_"+arrStationName[i]+"\" class=\"filter fromStationName\" value=\""+arrStationName[i]+"\" filter=\"s\"><label for=\"fromStation_"+arrStationName[i]+"\">"+arrStationName[i]+"</label></span>");	
				}
			}else{
				$("#fromStation").remove();
			}

			/**到达站xinx*/
			$(".endname").each(function(){
				var value = $(this).text();
				var bo = false;
				if(toStationName.length==0){
					toStationName.push(value);
				}
				for(var i=0; i<toStationName.length; i++){
					if(value==toStationName[i]){
						bo = true;
					}
				}
				if(!bo){
					toStationName.push(value);
				}
			});
			if(toStationName.length>=2){
				$("#toStation").append("<strong>到站选择：</strong>");
				$("#toStation").append("<label id=\"selectToStation\" class=\"choose_all\" for=\"sx_cb1\">全选</label>");
				for(var i=0; i<toStationName.length; i++){
					$("#toStation").append("<span><input type=\"checkbox\" id=\"toStation_"+toStationName[i]+"\" class=\"filter toStationName\" value=\""+toStationName[i]+"\" filter=\"s\"><label for=\"toStation_"+toStationName[i]+"\">"+toStationName[i]+"</label></span>");	
				}
			}else{
				$("#toStation").remove();
			}
		}else{
			$("#fromStation").remove();
			$("#toStation").remove();
		}
		/**车次全选**/
		$("#selectALLTrain").click(function(){
			var len = arrType.length;
			if(len==8){
				$("li#trainType").find(":checkbox").attr("checked", false);
			}else{
				$("li#trainType").find(":checkbox").attr("checked", true);
			}
			
			totalChoose();
		});
		
		/**车次过滤器**/
		$(":checkbox.trainType").click(function(){
			totalChoose();
		});
		/**发车时间全选**/
		$("#selectALLTime").click(function(){
			var len = arrTime.length;
			if(len==4){
				$("li#fromTime").find(":checkbox").attr("checked", false);
			}else{
				$("li#fromTime").find(":checkbox").attr("checked", true);
			}
			
			totalChoose();
		});
		
		/**发车时间过滤器**/
		$(":checkbox.startTime").click(function(){
			totalChoose();
		});

		/**到站时间全选**/
		$("#selectALLTo").click(function(){
			var len = toTime.length;
			if(len==4){
				$("li#toTime").find(":checkbox").attr("checked", false);
			}else{
				$("li#toTime").find(":checkbox").attr("checked", true);
			}
			
			totalChoose();
		});
		
		/**到站时间过滤器**/
		$(":checkbox.arriveTime").click(function(){
			totalChoose();
		});
		
		/**发车站过滤器**/
		$(":checkbox.fromStationName").click(function(){
			totalChoose();
			
		});
		/**发车站全选**/
		$("#selectFromStation").click(function(){
			var len = arrStation.length;
			if(len==arrStationName.length){
				$("li#fromStation").find(":checkbox").attr("checked", false);
			}else{
				$("li#fromStation").find(":checkbox").attr("checked", true);
			}
			totalChoose();
		});
		///发车站筛选
		function fromStationChoose(){
			arrStation = new Array();
			$(":checkbox.fromStationName:checked").each(function(){
				var station = $(this).val();
				arrStation.push(station);
			});
			$(".startname").each(function(){
				var id = $(this).attr("id");
				var value = $("#"+id).text();
				var stationShow = false;
				for(var num=0; num<arrStation.length; num++){
					stationShow = true;
					if(value==arrStation[num]){
						stationShow = false;
						break;
					}
				}
				if(stationShow){
					$(this).parents("tr").hide();
				}
			});
		}

		/**到站过滤器**/
		$(":checkbox.toStationName").click(function(){
			totalChoose();
		});
		/**到站全选**/
		$("#selectToStation").click(function(){
			var len = toStation.length;
			if(len==toStationName.length){
				$("li#toStation").find(":checkbox").attr("checked", false);
			}else{
				$("li#toStation").find(":checkbox").attr("checked", true);
			}
			
			totalChoose();
		});
		///到站筛选
		function toStataionChoose(){
			toStation = new Array();
			$(":checkbox.toStationName:checked").each(function(){
				var station = $(this).val();
				toStation.push(station);
			});
			$(".endname").each(function(){
				var id = $(this).attr("id");
				var value = $("#"+id).text();
				var stationShow = false;
				for(var num=0; num<toStation.length; num++){
					stationShow = true;
					if(value==toStation[num]){
						stationShow = false;
						break;
					}
				}
				if(stationShow){
					$(this).parents("tr").hide();
				}
			});
		}

		/**可否预订全选**/
		$("#selectStartAfter").click(function(){
            var this_selectAfter = new Array();
            $(":checkbox.weatherBook:checked").each(function(){
                var station = $(this).val();
                this_selectAfter.push(station);
                
            });
            if(this_selectAfter.length==2){
                $("li#startAfter").find(":checkbox").attr("checked", false);
            }else{
                $("li#startAfter").find(":checkbox").attr("checked", true);
            }
			totalChoose();
		});
		/**可否预订过滤器**/
		$(":checkbox.weatherBook").click(function(){
			totalChoose();
		});
		///可否预订筛选
		function selectAfterChoose(){
            var selectAfter = new Array();
            $(":checkbox.weatherBook:checked").each(function(){
                var station = $(this).val();
                selectAfter.push(station);
                
            });
            if(selectAfter.length==2||selectAfter.length==0){
                for (var i =yuding.length - 1; i >= 0; i--) {
                   $(yuding[i]).show();
                }
                for (var i =qiangpiao.length - 1; i >= 0; i--) {
                   $(qiangpiao[i]).show();
                }
            }else{
                if(selectAfter[0]=="预 订"){
                    for (var i =yuding.length - 1; i >= 0; i--) {
                        $(yuding[i]).show();
                    }
                    for (var i =qiangpiao.length - 1; i >= 0; i--) {
                        $(qiangpiao[i]).hide();
                    }
                }else{
                    for (var i =qiangpiao.length - 1; i >= 0; i--) {
                        $(qiangpiao[i]).show();
                    }
                    for (var i =yuding.length - 1; i >= 0; i--) {
                        $(yuding[i]).hide();
                    }

                }
            }
            $("td.seat").each(function(){
                if($(this).children(":visible").length==0){
                    $(this).parents("tr").hide();
                }
            });
			
        }
        var yuding;
        var qiangpiao;
        $().ready(function(){
             yuding= new Array();
             qiangpiao= new Array();
             $(".bookTicket").each(function(){
                var id = $(this).attr("id");
                var value = $(this).val();
                var td_this =  $(this).parent().parent()[0];
                var tr_this = $(this).parents("tr")[0];
                var seat_td = $(tr_this).children("td.seat")[0];
                var p = $(this).parent()[0];
                var p_index ;
                $(td_this).children("p").each(function(index,element){
                        if (p==element) {
                            p_index = index;
                            return false;
                        }
                });

                if(value == "预 订"){
                    yuding.push($(seat_td).children("p:eq("+p_index+")"));
                    yuding.push($(this).parents("p"));
                }else if (value="抢 票") {
                    qiangpiao.push($(seat_td).children("p:eq("+p_index+")"));
                    qiangpiao.push($(this).parents("p"));
                }

            });


        });
        
		
		
		//车次、发车时间、到站时间筛选
		function filterChoose(){
			arrTime = new Array();
			toTime = new Array();
			arrType = new Array();
			var count = $(":checkbox.filter:checked").length;
			if(count==0){
				$(".trainBar").show();
			}else{
				$(".trainBar").hide();
			}
			//车次类型
			$(":checkbox.trainType:checked").each(function(){
				var type = $(this).val();
				arrType.push(type);
			});
			//发车时间
			$(":checkbox.startTime:checked").each(function(){
				var time = $(this).val();
				arrTime.push(time);
			});

			if($("#ser-more").attr("class")=="up-arrow"){
				//到站时间
				$(":checkbox.arriveTime:checked").each(function(){
					var time = $(this).val();
					toTime.push(time);
				});
			}
			$(".trainBar").each(function(){
				var id = $(this).attr("id");
				var typeShow = true;
				for(var num=0; num<arrType.length; num++){
					typeShow = false;
					if((arrType[num].split("/").length>1)){
						if(arrType[num].split("/")[1]=='PT'){
							var patrn=/((ticket\_)[0-9])/;
							if(id.indexOf("ticket_"+arrType[num].split("/")[0])>0 || id.match(patrn)){
								typeShow = true;
								break;
							}
						}else{
							if(id.indexOf("ticket_"+arrType[num].split("/")[0])>0 || id.indexOf("ticket_"+arrType[num].split("/")[1])>0){
								typeShow = true;
								break;
							}
						}
					}else{
						if(id.indexOf("ticket_"+arrType[num])>0){
							typeShow = true;
							break;
						}
					}
				}
				var timeShow = true;
				for(var num=0; num<arrTime.length; num++){
					timeShow = false;
					if(id.indexOf(arrTime[num])>0){
						timeShow = true;
						break;
					}
				}

				var toTimeShow = true;
				for(var num=0; num<toTime.length; num++){
					toTimeShow = false;
					
					if(id.indexOf(toTime[num])>0){
						toTimeShow = true;
						break;
					}
				}
				if(typeShow&&timeShow&&toTimeShow){
					$("#"+id).show();
				}
			});
		}
		function totalChoose(){
			filterChoose();
			fromStationChoose();
			if($("#ser-more").attr("class")=="up-arrow"){
				toStataionChoose();
				selectAfterChoose();
			}
			$(".train_station").each(function(){
				$(this).hide();
			});
			$(".trainBar:visible").each(function(){
				var chufa = $(this).children(".wayStastion").children("div").attr("class");
				var checi = $(this).children(".wayStastion").children("strong").attr("id");
				if(chufa=="up-arrow"){
					//$("#train_station_"+checi).fadeTo("slow",1);
					$("#train_station_"+checi).show();
				}
			});
			$("#train_total").text($(".trainBar:visible").length);
		}
		/**筛选功能结束*/
		
		$("#query").click(function(){
			
			if($.trim($("#fromZh").val())=="" || $.trim($("#fromZh").val())=="出发城市"){
				showErrMsg("fromZh", "110px", "请输入出发城市！");
				return;
			}else{
				hideErrMsg("fromZh");
			}
			if($.trim($("#toZh").val())=="" || $.trim($("#toZh").val())=="到达城市"){
				showErrMsg("toZh", "110px", "请输入到达城市！");
				return;
			}else{
				hideErrMsg("toZh");
			}
			if($("#travel_time").val()==""){
				showErrMsg("travel_time", "110px", "请输入出发日期！");
				return;
			}else{
				hideErrMsg("travel_time");
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
		});
		
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
	});
	
	//进入下单页面
	function gotoBookOrder(travelTime, trainCode, startCity, endCity, startTime, endTime, costTime, defaultSelect){
		var is_buyable="${is_buyable}";
		var seatMsg = $("#seatMsg_"+trainCode).val();
		if(is_buyable == "00"){
			alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
			return false;
		}
		var url="/buyTicket/gotoBookOrder.jhtml?travelTime="+travelTime+"&trainCode="+trainCode+"&startCity="+startCity+"&endCity="+endCity+"&startTime="+startTime+"&endTime="+endTime+"&costTime="+costTime+"&seatMsg="+seatMsg+"&defaultSelect="+defaultSelect;
		window.location = encodeURI(url);
	}
	//切换出发和到达城市
	function swapCity(){
		var fromCity = $("#fromZh").val().replace("出发城市", "");
		var toCity = $("#toZh").val().replace("到达城市", "");
		$("#fromZh").val(toCity);
		$("#toZh").val(fromCity);
	}
	
</script>

</body>
</html>
