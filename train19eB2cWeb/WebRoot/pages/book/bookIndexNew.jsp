<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>19trip旅行</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="/css/sreachbar.css" />
<link rel="stylesheet" type="text/css" href="/css/default.css" />
<link rel="stylesheet" type="text/css" href="/css/reset-min.css" />
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<script type=text/javascript src="/js/My97DatePicker/WdatePicker.js"></script>
<script type=text/javascript src="/js/train_station.js?version=1008"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/js/dialog.js"></script>
<style type="text/css">
.condi_table tr td{padding:5px 0px 5px 0px;}
.condi_table tr td{border-top:1px solid #ddd;}
.condi_table{border-collapse: separate;}
/****订单弹出框*****/
* html,* html body{background-image:url(about:blank);background-attachment:fixed;} 
* html .ie6fixedTL{position:absolute;left:expression(eval(document.documentElement.scrollLeft));top:expression(eval(document.documentElement.scrollTop))} 
* html .ie6fixedBR{position:absolute;left:expression(eval(document.documentElement.scrollLeft+document.documentElement.clientWidth-this.offsetWidth)-(parseInt(this.currentStyle.marginLeft,10)||0)-(parseInt(this.currentStyle.marginRight,10)||0));top:expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||0)-(parseInt(this.currentStyle.marginBottom,10)||0)))} 
</style>


</head>
<body>
<!--以下是头部logo部分start -->
<jsp:include flush="true" page="/pages/common/headerNav.jsp">
	<jsp:param name="menuId" value="hcp" />
</jsp:include>
<!--以下是头部logo部分end -->

<!--以下是头部搜索车次部分inquiry部分 -->
<div class="position">
<h3>列车查询</h3>
</div>
<form id="trainForm" action="/buyTicket/queryByStation.jhtml" method="post">
	<input type="hidden" id="isLogin" name="isLogin" value="${isLogin }" />
<div id="inquiry">
          <dl>
            <dt>出发城市</dt>
            <dd>
              <input  type="text" id="fromZh" name="from_city" onfocus="showCity('fromZh')" onblur="hideCity()" value="${paramMap.from_city}" />
            </dd>
          </dl>
          <div class="huan" onclick="swapCity();"></div>
          <dl>
            <dt>到达城市</dt>
            <dd>
              <input type="text" id="toZh" name="to_city" onfocus="showCity('toZh')" onblur="hideCity()" value="${paramMap.to_city}" />
            </dd>
          </dl>
          <dl>
            <dt>出发日期</dt>
            <dd>
              <input type="text" id="travel_time" name="travel_time" value="${paramMap.travel_time}" readonly="readonly" onfocus="myfunction(this)" />
            </dd>
          </dl> 
           <div class="btn1" style="float:left; " id="query">查&nbsp;&nbsp;询</div>
        </div>

<!--以下是搜日期部分 -->
<div class="search_deta">	
	<h2> 
      	<span>${paramMap.from_city}</span> 
        <span>→</span> 
        <span>${paramMap.to_city}</span>
        <span class="ts">${paramMap.travel_time}（<c:choose>
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
      	</span>
	</h2>
</div> 


<!--以下是左右选择日期部分 start -->
<div class="date_tab_box">
    <div class="cal_left choose_jiantou" id="front_jiantou" onclick="toForwordDay();"></div>
    <ul class="cal_box">  
    	<c:forEach items="${dayAndWeek}" var="list" varStatus="idx">
        	<c:forEach items="${beforeAndAfter4Days}" var="daylist" >
                 <c:choose>
                 	<c:when test="${fn:contains(list.day, daylist.days) }">
                 		<c:choose>
                 			<c:when test="${list.day eq travelDay}">
                 				<li class="cal_item cal_cur show_li" id="li_${idx.index+1}"><span>${list.day }<input type="hidden" id="li_span_${idx.index+1}" value="${list.newday }" /></span> <span>${list.week }</span></li>
               				</c:when>
              				<c:otherwise>
                 				<li id="li_${idx.index+1}" class="cal_item show_li"><span>${list.day }<input type="hidden" id="li_span_${idx.index+1}" value="${list.newday }" /></span> <span>${list.week }</span></li>
                			</c:otherwise>
                 		</c:choose>
                 	</c:when>
                 	<c:otherwise>
                 	</c:otherwise>
                 </c:choose>
           </c:forEach>
           <c:if test="${fn:contains(nodays, list.day) }">
              <li class="cal_item hide_li" id="li_${idx.index+1}"><span>${list.day }<input type="hidden" id="li_span_${idx.index+1}" value="${list.newday }" /></span> <span>${list.week }</span></li>
           </c:if>
        </c:forEach>
    </ul>
    <div class="cal_left choose_jiantour" id="end_jiantou" onclick="toNextDay();"></div>
</div>
                <script type="text/javascript" language="JavaScript">
	                $(document).ready(function () {  
	                  	if($("#li_1").is(":visible")){//第一个li显示，则不能再向前
		                  	$("#front_jiantou").removeClass("choose_jiantou");
		                  	$("#front_jiantou").addClass("choose_jiantouend");//不能选
		                }
	                  	if($("#li_60").is(":visible")){//最后一个li显示，则不能再向后
		                  	$("#end_jiantou").removeClass("choose_jiantour");
		                  	$("#end_jiantou").addClass("choose_jiantourend");//不能选
		                }
	               	});
	               	//向前翻
                	function toForwordDay(){
                		// 获取第一个显示show_li的ID
                		var id = $(".show_li").attr("id");// li_12
                		var index = id.substring(3);// 12
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
                    	}else if($(".show_li").attr("id").substring(3)<52){//第一个li显示，则不能再向前
                			$("#end_jiantou").addClass("choose_jiantour");
		                  	$("#end_jiantou").removeClass("choose_jiantourend");//不能选
                    	}
                    }
                	//向后翻
                	function toNextDay(){
                		// 获取第一个显示show_li的ID
                		var id = $(".show_li").attr("id");// li_12
                		var index = id.substring(3);// 12
                		if(index==51){//最后一个li显示，则不能再向后
                			$("#end_jiantou").removeClass("choose_jiantour");
		                  	$("#end_jiantou").addClass("choose_jiantourend");//不能选
                    	}else if(index>=52){//最后一个li显示，则不能再向后
                			$("#end_jiantou").removeClass("choose_jiantour");
		                  	$("#end_jiantou").addClass("choose_jiantourend");//不能选
		                  	return;
                    	}else{
                    		if($("#end_jiantou").hasClass("choose_jiantourend")){
                        		$("#end_jiantou").addClass("choose_jiantour");
    	                  		$("#end_jiantou").removeClass("choose_jiantourend");//不能选
                            }
                        }    
                		$("#li_"+(parseInt(index))).removeClass("show_li");//第一个隐藏
                    	$("#li_"+(parseInt(index))).addClass("hide_li"); 
                    	$("#li_"+(parseInt(index)+9)).removeClass("hide_li");//最后一个+1显示
                    	$("#li_"+(parseInt(index)+9)).addClass("show_li");
                    	if($(".show_li").attr("id").substring(3)==2){//第一个li显示，则不能再向前
                			$("#front_jiantou").addClass("choose_jiantou");
		                  	$("#front_jiantou").removeClass("choose_jiantouend");//不能选
                    	}else if($(".show_li").attr("id").substring(3)<52){//第一个li显示，则不能再向前
                			$("#end_jiantou").addClass("choose_jiantour");
		                  	$("#end_jiantou").removeClass("choose_jiantourend");//不能选
                    	}
                    }
                </script>
<!--以下是左右选择日期部分 end -->                
                

<!-----查询条件区域condition-------------->
<div id="book_allcon">
<div id="condition">
        <ul class="condi_ul" id="sel_checkbox">
        	<li id="trainType">
            	<strong>车型选择：</strong><label id="selectALLTrain" class="choose_all" for="sx_cb1">全选</label>
                <input type="checkbox" id="trainType_GD" class="filter trainType" value="G/D" /><label for="trainType_GD">G高铁/D动车</label>
                <input type="checkbox" id="trainType_G" class="filter trainType" value="G" /><label for="trainType_G">G高铁</label>
                <input type="checkbox" id="trainType_C" class="filter trainType" value="C" /><label for="trainType_C">C城际</label>
                <input type="checkbox" id="trainType_D" class="filter trainType" value="D"/><label for="trainType_D">D动车</label>
                <input type="checkbox" id="trainType_Z" class="filter trainType" value="Z"/><label for="trainType_Z">Z专列</label>
                <input type="checkbox" id="trainType_T" class="filter trainType" value="T"/><label for="trainType_T">T特快</label>
                <input type="checkbox" id="trainType_K" class="filter trainType" value="K"/><label for="trainType_K">K快速</label>
                <input type="checkbox" id="trainType_L" class="filter trainType" value="L/PT"/><label for="trainType_L">L临客|普快</label>
            </li>
         	<li id="fromTime">
                 <strong>发车时间：</strong><label id="selectALLTime" class="choose_all" for="sx_cb1">全选</label>
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
                <strong>到达时间：</strong><label id="selectALLTo" class="choose_all" for="sx_cb1">全选</label>
                <span><input type="checkbox" id="toTime_1" class="filter arriveTime" filter="t" value="tzs"/><label for="toTime_1">早上(00:00-06:00)</label></span>
                <span><input type="checkbox" id="toTime_2" class="filter arriveTime" filter="t" value="tam"/><label for="toTime_2">上午(06:00-12:00)</label></span>
                <span><input type="checkbox" id="toTime_3" class="filter arriveTime" filter="t" value="tpm"/><label for="toTime_3">下午(12:00-18:00)</label></span>
                <span><input type="checkbox" id="toTime_4" class="filter arriveTime" filter="t" value="tws"/><label for="toTime_4">晚上(18:00-24:00)</label></span>
            </li>
            <!-- 是否始发
	        <li id="startYesOrNo">
	            <strong>是否始发：</strong><label id="selectStartYesOrNo" class="choose_all" for="sx_cb1">全选</label>
					<span><input type="checkbox" id="startYes" class="filter weatherStart" value="start" filter="o"/><label for="startYes">始发</label></span>
					<span><input type="checkbox" id="startNo" class="filter weatherStart" value="startno" filter="o"/><label for="startNo">路过</label></span>
	        </li>-->
            <!-- 可否预订 -->
            <c:if test="${unBookList==null}">
	        <li id="startAfter" style="display:none">
	            <strong>余票显示：</strong><label id="selectStartAfter" class="choose_all" for="sx_cb1">全选</label>
					<span><input type="checkbox" id="ableBook" class="filter weatherBook" value="预 订" filter="o"/><label for="ableBook">仅显示有票</label></span>
					<!-- 
					<span><input type="checkbox" id="unableBook" class="filter weatherBook" value="" filter="o"/><label for="unableBook">不可预订</label></span>
					 -->
	        </li>
	        </c:if>
        </ul> 
		<div class="down-arrow" id="ser-more"><a href="javascript:void(0);">更多筛选条件&nbsp;&nbsp;&nbsp;</a></div>
    <table class="condi_table" cellpadding="0" cellspacing="0" width="800px">
    <tr class="tit">
	      <th width="14%" style="text-align:left; padding-left:12px;">车次</th>
	      <th width="20%" style="text-align:left">出发/到达车站</th>
	      <th width="20%" style="text-align:left">出发/到达时间</th>
	      <th width="15%" style="text-align:left">运行时间 <!-- <span class="time"></span> --></th>
	      <th style="width:26%; text-align:left">参考票价</th>
	      <th width="12%" style="text-align:left"></th>
    </tr>
    <c:if test="${ otVo ==null && osnd==null && unBookList == null}">
	                    <tr>
	                    	<td colspan="6" style="font:bold 16px/60px 'microsoft yahei','宋体';color:#f60; width:800px; text-align:center;">对不起，暂无您查询的车次信息！</td>
	                    </tr>
                    </c:if>
    
    
    <!-- 新接口展示页面 -->
                    <c:if test="${otVo ==null && unBookList==null && osnd!=null}">
	                    <c:if test="${osnd.datajson == null || fn:length(osnd.datajson)==0}">
		                    <tr>
		                    	<td colspan="6" style="font:bold 16px/60px 'microsoft yahei','宋体';color:#f60; width:800px; text-align:center;"">对不起，暂无您查询的车次信息！</td>
		                    </tr>
	                    </c:if>
	                    <c:forEach items="${osnd.datajson}" var="list">
	                    	<c:if test="${!empty list.station_train_code}">
	                    		<tr class="trainBar" id="${list.train_no}_ticket_${fn:substring(list.station_train_code, 0, 1)}_${tn:fromTimeStr(list.start_time)}_${tn:toTimeStr(list.arrive_time)}" startTime="${list.start_time}">
			                        <td class="wayStastion">
								      	<span class="checi" id="${list.train_no}">${list.station_train_code}</span><br /> 
								      	<div style="float:left;" class="down-arrow" value="${list.from_station_name}_${list.to_station_name}"><a href="javascript:void(0);">经停站&nbsp;&nbsp;&nbsp;</a></div>
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
			                        <td class="fd"><span class="sp">${list.start_time}</span><br />${list.arrive_time}</td>
			                        <td style="vertical-align:middle;">${tn:liShiTimeStr(list.lishi)}</td>
			                        <td class="money seat">
			                        	<c:if test="${(list.wz ne '-' && list.wz_num ne '-') && (list.yz_num eq '-') && (list.ze_num eq '-') }">
			                        		<p>无座<span>￥${(list.yz eq '-' || empty list.yz)?list.ze:list.yz}
			                        		<c:choose>
					                        	<c:when test="${list.wz_num_show < 10 && list.wz_num_show != 0}">
					                        		<input type="button" value="仅剩${list.wz_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
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
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.rz && list.rz ne '-'}"><p>软座<span>￥${list.rz} 
				                        	<c:choose>
					                        	<c:when test="${list.rz_num_show < 10 && list.rz_num_show != 0}">
					                        		<input type="button" value="仅剩${list.rz_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.zy && list.zy ne '-'}"><p>一等座<span>￥${list.zy} 
				                        	<c:choose>
					                        	<c:when test="${list.zy_num_show < 10 && list.zy_num_show != 0}">
					                        		<input type="button" value="仅剩${list.zy_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.ze && list.ze ne '-'}"><p>二等座<span>￥${list.ze} 
				                        	<c:choose>
					                        	<c:when test="${list.ze_num_show < 10 && list.ze_num_show != 0}">
					                        		<input type="button" value="仅剩${list.ze_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.yws && list.yws ne '-'}"><p>硬卧<span>￥${list.ywx} 
				                        	<c:choose>
					                        	<c:when test="${list.yw_num_show < 10 && list.yw_num_show != 0}">
					                        		<input type="button" value="仅剩${list.yw_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${!empty list.rws && list.rws ne '-'}"><p>软卧<span>￥${list.rwx} 
				                        	<c:choose>
					                        	<c:when test="${list.rw_num_show < 10 && list.rw_num_show != 0}">
					                        		<input type="button" value="仅剩${list.rw_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${list.gws ne '-'}"><p>高级软卧<span>￥${list.gwx} 
				                        	<c:choose>
					                        	<c:when test="${list.gr_num_show < 10 && list.gr_num_show != 0}">
					                        		<input type="button" value="仅剩${list.gr_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${list.tdz ne '-'}"><p>特等座<span>￥${list.tdz} 
				                        	<c:choose>
					                        	<c:when test="${list.tz_num_show < 10 && list.tz_num_show != 0}">
					                        		<input type="button" value="仅剩${list.tz_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
				                        <c:if test="${list.swz ne '-'}"><p>商务座<span>￥${list.swz} 
				                        	<c:choose>
					                        	<c:when test="${list.swz_num_show < 10 && list.swz_num_show != 0}">
					                        		<input type="button" value="仅剩${list.swz_num_show}张" class="yupiao" />
					                        	</c:when>
					                        	<c:otherwise>
					                        	</c:otherwise>
					                        </c:choose>
				                        </span></p></c:if>
			                        </td>
			                        		<c:choose>
						                        <c:when test="${list.canBook eq '1'}">
						                        	<td >
						                        		<input type="hidden" id="seatMsg_${list.station_train_code}" value="无座_${(list.yz eq '-' || empty list.yz)?list.ze:list.yz}_${list.wz_num},硬座_${list.yz}_${list.yz_num},软座_${list.rz}_${list.rz_num},一等座_${list.zy}_${list.zy_num},二等座_${list.ze}_${list.ze_num},硬卧_${list.ywx}_${list.yw_num},软卧_${list.rwx}_${list.rw_num},高级软卧_${list.gwx}_${list.gr_num},特等座_${list.tdz}_${list.tz_num},商务座_${list.swz}_${list.swz_num}"/>
						                        		<c:if test="${(list.wz ne '-' && list.wz_num ne '-')&& (list.yz_num eq '-') && (list.ze_num eq '-') }">
						                        			<p><input type="button" id="${list.train_no}_yz_ableBook"  class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_yz_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','无座');"/></p>
						                        		</c:if>
						                        		<c:if test="${list.wz_num eq '-' && (list.ze ne '-' || list.yz ne '-') && !empty list.wz}">
						                        			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
						                        		</c:if>
						                        		<c:if test="${list.yz ne '-' && list.yz_num ne '-'}">
						                        			<p><input type="button" id="${list.train_no}_yz_ableBook"  class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_yz_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','硬座');"/></p>
						                        		</c:if>
						                        		<c:if test="${list.yz_num eq '-' && list.yz ne '-' && !empty list.yz}">
						                        			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
						                        		</c:if>
								                        <c:if test="${list.rz ne '-' && list.rz_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_rz_ableBook" class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_rz_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','软座');"/></p>
								                        </c:if>
								                        <c:if test="${list.rz_num eq '-' && list.rz ne '-' && !empty list.rz}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                        </c:if>
								                        
								                        <c:if test="${list.zy ne '-' && list.zy_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_rz1_ableBook" class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_rz1_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','一等座');"/></p>
								                        </c:if>
								                        <c:if test="${list.zy_num eq '-' && list.zy ne '-' && !empty list.zy}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                        </c:if>
								                        <c:if test="${list.ze ne '-' && list.ze_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_rz2_ableBook" class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_rz2_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','二等座');"/></p>
								                        </c:if>
								                        <c:if test="${list.ze_num eq '-' && list.ze ne '-' && !empty list.ze}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                        </c:if>
								                        <c:if test="${list.yws ne '-' && list.yw_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_yws_ableBook" class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_yws_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','硬卧');"/></p>
								                        </c:if>
								                        <c:if test="${list.yw_num eq '-' && list.yws ne '-' && !empty list.yws}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                        </c:if>
														
								                        <c:if test="${list.rws ne '-' && list.rw_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_rws_ableBook" class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_rws_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','软卧');"/></p>
								                        </c:if>
								                        <c:if test="${list.rw_num eq '-' && list.rws ne '-' && !empty list.rws}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                        </c:if>
								                        
								                        <c:if test="${list.gws ne '-' && list.gr_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_gws_ableBook" class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_gws_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','高级软卧');"/></p>
								                        </c:if>
								                        <c:if test="${list.gr_num eq '-' && list.gws ne '-' && !empty list.gws}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                        </c:if>
								                        
								                        <c:if test="${list.tdz ne '-' && (list.tz_num ne '-' && list.tz_num ne '无')}">
								                        	<p><input type="button" id="${list.train_no}_tdz_ableBook" class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_tdz_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','特等座');"/></p>
								                        </c:if>
								                        <c:if test="${(list.tz_num eq '-' || list.tz_num eq '无') && list.tdz ne '-' && !empty list.tdz}"><p><input type="button"  class="btn2 bookTicket" value="预 订" /></p></c:if>
								                        
								                        <c:if test="${list.swz ne '-' && list.swz_num ne '-'}">
								                        	<p><input type="button" id="${list.train_no}_swz_ableBook" class="btn3" value="预 订" 
						                        			onclick="gotoBookOrder('${list.train_no}_swz_ableBook','${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishiValue}','商务座');"/></p>
								                        </c:if>
								                        <c:if test="${list.swz_num eq '-' && list.swz ne '-' && !empty list.swz}">
								                        	<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                        </c:if>
								                    </td>
								               </c:when>
								               <c:otherwise>
								                	<td>
								                		<c:if test="${(list.wz_num ne '-' && list.yz ne '-' && !empty list.yz ) || (list.wz_num ne '-' && (list.ze ne '-' && !empty list.ze))}">
								                			<p><input type="button"  class="btn2 bookTicket" value="111" /></p>
								                		</c:if>
								                		<c:if test="${list.yz ne '-' && !empty list.yz}">
								                			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                		</c:if>
								                		<c:if test="${list.rz ne '-' && !empty list.rz}">
								                			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                		</c:if>
								                		<c:if test="${list.zy ne '-' && !empty list.zy}">
								                			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                		</c:if>
								                		<c:if test="${list.ze ne '-' && !empty list.ze}">
								                			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                		</c:if>
								                		<c:if test="${list.yws ne '-' && !empty list.yws}">
								                			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                		</c:if>
								                		<c:if test="${list.rws ne '-' && !empty list.rws}">
								                			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                		</c:if>
								                		<c:if test="${list.gws ne '-' && !empty list.gws}">
								                			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                		</c:if>
								                		<c:if test="${list.tdz ne '-' && !empty list.tdz}">
								                			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                		</c:if>
								                		<c:if test="${list.swz ne '-' && !empty list.swz}">
								                			<p><input type="button"  class="btn2 bookTicket" value="预 订" /></p>
								                		</c:if>
						                        	</td>
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
	                 <!--  -->
	                 <c:if test="${otVo ==null && osnd==null && unBookList!=null}">
	                    <c:forEach items="${unBookList}" var="list">
	                    	<c:if test="${!empty list.cc && !empty list.start_time}">
	                    		<tr class="trainBar" id="${list.xh}_ticket_${list.cc}_${tn:fromTimeStr(list.start_time)}_${tn:toTimeStr(list.arrive_time)}" startTime="${list.start_time}">
			                        <td class="wayStastion">
			                            <strong class="checi" id="${list.xh}">${list.cc}</strong><br/>
			                            <div style="float:left;" class="down-arrow" value="${list.fz}_${list.dz}"><a href="javascript:void(0);">经停站&nbsp;&nbsp;&nbsp;</a></div>
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
				                        <c:if test="${!empty list.yws && list.yws ne '0'}">硬卧<span>￥${list.yws}/${list.ywz}/${list.ywx}</span><br /></c:if>
				                        <c:if test="${!empty list.ywx && list.rws ne '0'}">软卧<span>￥${list.rws}/${list.rwx}</span><br/></c:if>
			                        </td>
			                        <td>
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

<div id="warn">
	<p><h2>火车票小助手提示</h2><br/>
	1.自2014年12月1日起，铁路互联网售票、电话订票的预售期由20天延长至60天。具体为：12月1日，预售期由现行20天延长至30天；12月2日至12月6日，预售期每天再比上一日延长6天；12月7日开始发售2015年春运第一天（2015年2月4日）的车票。其他售票方式按错后2天的原则同步延长。<br/><br/>
	2.在线退票处理时间是7:00-23:00。<br/><br/>
	3.线下退票非工作时间或者已取票的乘客，如需退票，请凭购票有效证件在发车前到火车站退票窗口办理退票。<br/><br/>
	4.退票费用根据铁路局退票规定：<br/>
	①开车前15天（不含）以上退票的，不收取退票费<br/>
	②开车前48小时以上（不含）不足15天按票价的5%<br/>
	③开车前24小时以上（不含）不足48小时按票价的10%<br/>
	④开车前不足24小时按20%计<br/><br/>
	5.车票改签需持有效证件原件和火车票去始发地火车站或取票地火车站窗口办理。
</p>
</div>
</div>
</form>




<div id="all_land" class="all_land" style="display:none;">
	<div class="left_land">
     	<div class="land_tit">
        	<ul>
    			<li class="onland">登录</li>
    			<li>注册</li>
        	</ul>
    	</div>
    	<form id="land_form" action="/login/userLogin.jhtml" method="post">
	        <div class="land_con" id="land_con">
	        	<div class="login_div">
	        		<dl>
	        			<dt></dt>
	        			<dd>
	                	<input type="text" id="user_phone" name="user_phone" value="请输入手机号码" onfocus="if(this.value=='请输入手机号码'){this.value=''};" 
    						onblur="if(this.value==''||this.value=='请输入手机号码'){this.value='请输入手机号码';}" />
	                	</dd>
	        		</dl>
	        		<p id="user_phone_verify"></p>
	        	</div>
	        	
	        	<div class="login_div">
		        	<dl>
		        		<dt class="lock"></dt>
		        		<dd>
		                <input type="text" id="user_password_text" name="user_password_text" value="6-20位数字、字母和符号" onfocus="changeLogin(this);" />
	    				<input type="password" id="user_password" name="user_password" value="" style="display:none;"
	    					onblur="changeLogin(this);" onkeydown="if (event.keyCode==13) submitLoginForm();" />
		                </dd>
		        	</dl>
		        	<p id="user_password_verify"></p>
	            </div>
	            
	            <div class="free_login">
		            <input id="rmbUser" type="checkbox" />两周内免登录
		            <a href="/login/forgetPwd.jhtml">忘记密码？</a>        
	            </div>
	            <div class="btn4" onclick="submitLoginForm();">登&nbsp;&nbsp;&nbsp;陆</div>
	        </div>
        </form>
<script type="text/javascript">
//登录处的js
    $(document).ready(function() {  
		if ($.cookie("rmbUser") == "true") {
			$("#rmbUser").attr("checked", true);  
    	    $("#user_phone").val($.cookie("user_phone"));  
    	    $("#user_password").val($.cookie("user_password")); 
    	    if($.trim($("#user_password").val())!=""){
    			document.getElementById('user_password').style.display = "block";
    			document.getElementById('user_password_text').style.display = "none";
            } 
		}
    }); 
    //密码框
    function changeLogin(obj){ 
        if($("#user_password").is(":visible") && $.trim($("#user_password").val())!=""){
            return;
        } 
  		obj.style.display = "none";  
  		if(obj.type=="text")  {    
  			document.getElementById('user_password').style.display = "block";    
  			document.getElementById('user_password').focus();//加上    
  			if(this.value==''||this.value=='请输入密码'){this.value='请输入密码';}
  		}else{     
  			document.getElementById('user_password_text').style.display = "block";  
  			if(this.value=='请输入密码'){this.value='';}   
  		}  
 	} 
 	//提交登录  
	function submitLoginForm(){
		var user_phone = $.trim($("#user_phone").val());  
		var user_password = $.trim($("#user_password").val()); 
		var sessionVal = "no"; 
		//保存用户信息    两星期
		if($("#rmbUser").attr("checked") == "checked") {  
			$.cookie("rmbUser", "true", {expires : 14});
			$.cookie("user_phone", user_phone, {expires : 14});
			$.cookie("user_password", user_password, {expires : 14}); 
			sessionVal = "yes";
		} else {
		    $.cookie("rmbUser", "false");  
		    $.cookie("user_phone", null);  
		    $.cookie("user_password", null);  
		}
		//校验手机号码和登录密码  
		if(user_phone=="" || user_phone=="请输入手机号码"){
			$("#user_phone").focus();
			$("#user_phone_verify").addClass("login_div_p");
			$("#user_phone_verify").html("<span></span>手机号码不能为空！");
			return false;
		}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test(user_phone)){
			$("#user_phone").focus();
			$("#user_phone_verify").addClass("login_div_p");
			$("#user_phone_verify").html("<span></span>请填写正确的手机号！");
			return false;
		}else{
			$("#user_phone_verify").removeClass("login_div_p");
			$("#user_phone_verify").text("");
		}
		if(user_password=="" || user_password=="请输入密码"){
			$("#user_password").focus();
			$("#user_password_verify").addClass("login_div_p");
			$("#user_password_verify").html("<span></span>登录密码不能为空！");
			return;
		}else{
			$("#user_password_verify").removeClass("login_div_p");
			$("#user_password_verify").text("");
		}
		//数据库校验用户的账号和密码是否存在
		if(user_phone!="" && user_phone!="请输入手机号码" && user_password!="" && user_password!="请输入密码"){
			var url = "/login/userLogin.jhtml?user_phone="+user_phone+"&user_password="+user_password+"&sessionVal="+sessionVal+"&version="+new Date();
			$.post(url,function(data){
				if(data == "yes"){
					noLogin();//继续购票流程
					//window.location="/index.jsp?version="+new Date();
					$("#user_password_verify").removeClass("login_div_p");
					$("#user_password_verify").html("");
				}else{
					$("#user_password_verify").addClass("login_div_p");
					$("#user_password_verify").html("<span></span>用户名或密码错误！");
				}
			});
		}
	}
</script>
        <form id="enroll_form" action="/login/userRegister.jhtml" method="post">
        <div class="enroll" id="enroll" style="display:none;" >
        <div class="login_div">
        	<dl>
        		<dt></dt>
        		<dd>
                <input type="text" id="user_phone_reg" name="user_phone_reg" value="请输入手机号码" onfocus="if(this.value=='请输入手机号码'){this.value=''};" 
    					onblur="if(this.value==''||this.value=='请输入手机号码'){this.value='请输入手机号码';}  user_phone_reg_info();" />
                </dd>
        	</dl>
        	<p id="user_phone_reg_info"></p>
        </div>
        <div class="login_div">
        	<dl>
        		<dt class="lock"></dt>
        		<dd>
                <input type="text" id="user_password_reg_text" name="user_password_reg_text" value="6-20位数字、字母和符号" onfocus="changeRegOne(this);" />
    			<input type="password" id="user_password_reg" name="user_password_reg" value="" style="display:none;" onblur="userPasswordInfo(); changeRegOne(this);" />
                </dd>
        	</dl>
        	<p id="user_password_reg_info"></p>
        </div>
        <div class="login_div">
        	<dl>
        		<dt class="lock"></dt>
        		<dd>
        		<input type="text" id="user_password_ok_text" name="user_password_ok_text" value="请再次输入密码" onfocus="changeRegTwo(this);" />
    			<input type="password" id="user_password_ok" name="user_password_ok" value="" style="display:none;" onblur="changeRegTwo(this);passIsSame();" />
                </dd>
        	</dl>
        	<p id="user_password_ok_info"></p>
        </div>
        <div class="login_div">
			<div class="land_test">
                <input type="text" id="check_code" name="check_code" value="请输入验证码" onfocus="if(this.value=='请输入验证码'){this.value=''};" 
    					onblur="if(this.value==''||this.value=='请输入验证码'){this.value='请输入验证码';}  check_code_info();" 
    					onkeydown="if (event.keyCode==13) submitRegisterForm();" />				
                <img src="/imageServlet" width="100" height="40" alt="" title="点击更换" onclick="javascript:refresh(this);" />
                <!-- 
                <span class="test_tu"></span>
                <a href="#">换一张</a> -->
            </div>
        	<p id="check_code_info"></p>
        </div>
        
        <div class="btn4" style="margin:10px auto;" onclick="submitRegisterForm();">立&nbsp;&nbsp;即&nbsp;&nbsp;注&nbsp;&nbsp;册</div>
        </div>
        </form>
<script type="text/javascript">
//注册处的js
	//定义全局变量
	var userPhone = false;//手机号码
	var userPassword = false;//用户密码
	var userPwdAgain = false;//确认密码
	var userCode = false;//验证码
	//密码框
    function changeRegOne(obj){ 
        if($("#user_password_reg").is(":visible") && $.trim($("#user_password_reg").val())!=""){
            return;
        } 
  		obj.style.display = "none";  
  		if(obj.type=="text")  {    
  			document.getElementById('user_password_reg').style.display = "block";    
  			document.getElementById('user_password_reg').focus();//加上    
  			if(this.value==''||this.value=='6-20位数字、字母和符号'){this.value='6-20位数字、字母和符号';}
  		}else{     
  			document.getElementById('user_password_reg_text').style.display = "block";  
  			if(this.value=='6-20位数字、字母和符号'){this.value='';}   
  		}  
 	}
  	//确认密码框
    function changeRegTwo(obj){ 
        if($("#user_password_ok").is(":visible") && $.trim($("#user_password_ok").val())!=""){
            return;
        } 
  		obj.style.display = "none";  
  		if(obj.type=="text")  {    
  			document.getElementById('user_password_ok').style.display = "block";    
  			document.getElementById('user_password_ok').focus();//加上    
  			if(this.value==''||this.value=='请再次输入密码'){this.value='请再次输入密码';}
  		}else{
  			document.getElementById('user_password_ok_text').style.display = "block";  
  			if(this.value=='请再次输入密码'){this.value='';}   
  		}  
 	}
  	//判断两次输入的密码是否一致
    function passIsSame(){
     	var user_password_reg = $.trim($("#user_password_reg").val());
     	var user_password_ok = $.trim($("#user_password_ok").val());
     	if(user_password_reg=="" || user_password_reg=="6-20位数字、字母和符号"){
     		$("#user_password_reg_info").addClass("login_div_p");
     		$("#user_password_reg_info").html("<span></span>密码不能为空");
     		userPassword = false;
     	}
     	if(user_password_ok=="" || user_password_ok=="请再次输入密码"){
     		$("#user_password_ok_info").addClass("login_div_p");
     		$("#user_password_ok_info").html("<span></span>确认密码不能为空");
     		userPwdAgain = false;
     	}
     	if(user_password_reg!="" && user_password_reg!="6-20位数字、字母和符号" && user_password_ok!="" && user_password_ok!="请再次输入密码"){
     		if(user_password_ok != user_password_reg){
     			$("#user_password_ok_info").addClass("login_div_p");
     	 		$("#user_password_ok_info").html("<span></span>两次输入密码不一致，请重新输入");
     	 		$("#user_password_ok").val("");
     	 	 	$("#user_password_ok").focus();
     	 	 	userPwdAgain = false;
     	 	}else{
     	 		$("#user_password_ok_info").removeClass("login_div_p");
     	 		$("#user_password_ok_info").html("");
     	 		userPwdAgain = true;
     	 	}
     	}
    }
 	//校验密码的长度
 	function userPasswordInfo(){
 		var pwd = $.trim($("#user_password_reg").val());
 		if (pwd == null || pwd == '' || pwd=='6-20位数字、字母和符号') {
 			$("#user_password_reg_info").html("<span></span>密码不能为空");
 			userPassword = false;
 		} else{ 
 			if(pwd.length>5 && pwd.length<21){
 				$("#user_password_reg_info").removeClass("login_div_p");
 				$("#user_password_reg_info").html("");
 				userPassword = true;
 			}else{
 				$("#user_password_reg_info").addClass("login_div_p");
 				$("#user_password_reg_info").html("<span></span>密码为6-20位数字、字母和符号");
 				userPassword = false;
 			}
 		}
 	}
 	//校验手机号码
 	function user_phone_reg_info(){
 		var user_phone_reg = $.trim($("#user_phone_reg").val()); 
 		if(user_phone_reg=="" || user_phone_reg=="请输入正确的手机号方便登录和找回密码"){
 			$("#user_phone_reg_info").addClass("login_div_p");
 			$("#user_phone_reg_info").html("<span></span>手机号码不能为空");
 			userPhone = false;
 			return;
 		}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test(user_phone_reg)){
 			$("#user_phone_reg_info").addClass("login_div_p");
 			$("#user_phone_reg_info").html("<span></span>请填写正确的手机号码");
 			userPhone = false;
 			return;
 		}else{//看手机号码是否已经注册过
 			var url = "/login/userPhoneCanUse.jhtml?user_phone="+user_phone_reg+"&version="+new Date();
 			$.post(url,function(data){
 				if(data == "no"){
 					$("#user_phone_reg_info").addClass("login_div_p");
 					$("#user_phone_reg_info").html("<span></span>该手机号码已经注册过");
 					userPhone = false;
 					return;
 				}else{
 					$("#user_phone_reg_info").removeClass("login_div_p");
 					$("#user_phone_reg_info").html("");
 					userPhone = true;
 				}
 			});
 		}
 	}
 	//校验验证码
 	function check_code_info(){
 		var check_code = $.trim( document.getElementById("check_code").value );
 		if(check_code=="" || check_code=="请输入验证码"){
 			$("#check_code_info").addClass("login_div_p");
 			$("#check_code_info").html("<span></span>验证码不能为空");
 			userCode = false;
 		}else{
 			var url = "/login/checkCode.jhtml?check_code="+check_code+"&version="+new Date();
 			$.post(url,function(data){
 				if(data == "yes"){
 					$("#check_code_info").removeClass("login_div_p");
 					$("#check_code_info").html("");
 					userCode = true;
 				}else if(data == "checkCodeNo"){
 					$("#check_code_info").addClass("login_div_p");
 					$("#check_code_info").html("<span></span>验证码输入有误");
 					userCode = false;
 				}
 			});
 		}
 	}
 	//提交注册
    function submitRegisterForm(){
    	user_phone_reg_info();
    	userPasswordInfo();
    	passIsSame();
    	check_code_info();
    	
		var user_phone_reg = $.trim($("#user_phone_reg").val());  
		var user_password_reg = $.trim($("#user_password_reg").val());
 	 	var user_password_ok = $.trim($("#user_password_ok").val());
 	 	var check_code = $.trim($("#check_code").val());
		//数据库校验用户的账号和密码是否存在
		if(userPhone == true && userPassword == true && userPwdAgain == true && userCode == true){
			var url = "/login/userRegister.jhtml?user_phone="+user_phone_reg+"&user_password="+user_password_reg+"&check_code="+check_code+"&version="+new Date();
			$.post(url,function(data){
				if(data == "yes"){
					noLogin();//继续购票流程
					//window.location="/index.jsp?version="+new Date();
				}else if(data == "checkCodeNo"){
					$("#check_code_info").addClass("login_div_p");
 					$("#check_code_info").html("<span></span>验证码输入有误");
 					userCode = false;
				}
			});
		}
	}
</script>
    </div>
	<div class="right_land">
    	<span class="land_off" id="land_off"></span>
    	<div class="btn5 noland" onclick="noLogin();">不&nbsp;登&nbsp;陆，直&nbsp;接&nbsp;预&nbsp;订&lt;&lt;</div>
    </div>
</div>

               
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
		WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+59}'}); 
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
	    	var id = $(".cal_cur").attr("id");
	    	$("#"+id).removeClass("cal_cur");//去除当前样式
			$(this).attr("class","cal_item show_li cal_cur");//当前样式
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
			var newId = $(".cal_cur").attr("id").substring(3);
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
			var checi = $(this).children("span").attr("id");
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
						url:"/station/subwayName.jhtml?checi="+$(this).children("span").text(),
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
							//changeHeight(down+20);
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
		var selectAfter = new Array();
		
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
					$("#fromStation").append("<span>&nbsp;<input type=\"checkbox\" id=\"fromStation_"+arrStationName[i]+"\" class=\"filter fromStationName\" value=\""+arrStationName[i]+"\" filter=\"s\"><label for=\"fromStation_"+arrStationName[i]+"\">"+arrStationName[i]+"</label></span>");	
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
					$("#toStation").append("<span>&nbsp;<input type=\"checkbox\" id=\"toStation_"+toStationName[i]+"\" class=\"filter toStationName\" value=\""+toStationName[i]+"\" filter=\"s\"><label for=\"toStation_"+toStationName[i]+"\">"+toStationName[i]+"</label></span>");	
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
			var len = selectAfter.length;
			if(len==2){
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
			selectAfter = new Array();
			$(":checkbox.weatherBook:checked").each(function(){
				var station = $(this).val();
				selectAfter.push(station);
			});
			$(".bookTicket").each(function(){
				var id = $(this).attr("id");
				var value = $("#"+id).val();
				var bookShow = false;
				for(var num=0; num<selectAfter.length; num++){
					bookShow = true;
					if(value==selectAfter[num]){
						bookShow = false;
						break;
					}
				}
				if(bookShow){
					$(this).parents("tr").hide();
				}
			});
		}
		
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

	//登陆注册页面的控制
	$(".land_tit ul li").click(function(){
		$(".land_tit ul li").each(function(){
			$(this).attr("class","");
		});  
		$(this).attr("class","onland");
		if($.trim($(this).text())=='登录'){
			$("#land_con").css('display','block');
			$("#enroll").css('display','none');
		}else{
			$("#enroll").css('display','block');
			$("#land_con").css('display','none');
		}
	});
	//刷新验证码
	function refresh(obj){
		obj.src = "/imageServlet?"+Math.random();   
	}
	//不登陆直接预定，继续预定流程
	function noLogin(){
		//$("#all_land").css('display','none');
		var bookInfo = $("#bookInfo").val();
		var arr = bookInfo.split("--");
		//alert(arr[7].substring(1,(parseInt(arr[7].length,10)-2)));
		var url="/buyTicket/gotoBookOrder.jhtml?travelTime="+arr[0]+"&trainCode="+arr[1]+"&startCity="+arr[2]+"&endCity="+arr[3]+"&startTime="+arr[4]+"&endTime="+arr[5]+"&costTime="+arr[6]+"&seatMsg="+arr[7].substring(1,arr[7].length-1)+"&defaultSelect="+arr[8];
		window.location = encodeURI(url);
	}
	//进入下单页面
	function gotoBookOrder(id, travelTime, trainCode, startCity, endCity, startTime, endTime, costTime, defaultSelect){
		var dialog = new popup(id,"all_land","land_off");
		var is_buyable="${is_buyable}";
		var seatMsg = $("#seatMsg_"+trainCode).val();
		if(is_buyable == "00"){
			alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
			return false;
		}
		var bookInfo = travelTime+"--"+trainCode+"--"+startCity+"--"+endCity+"--"+startTime+"--"+endTime+"--"+costTime+"--["+seatMsg+"]--"+defaultSelect;
		$("#bookInfo").val(bookInfo); 
		var isLogin = $("#isLogin").val();
		if(isLogin=='yes'){//判断用户是否已经登录
			var url="/buyTicket/gotoBookOrder.jhtml?travelTime="+travelTime+"&trainCode="+trainCode+"&startCity="+startCity+"&endCity="+endCity+"&startTime="+startTime+"&endTime="+endTime+"&costTime="+costTime+"&seatMsg="+seatMsg+"&defaultSelect="+defaultSelect;
			window.location = encodeURI(url);
		}else{
			$("#"+id).click(); 
		}
	}
</script>
<input type="hidden" id="bookInfo" value=""  />
<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 


</body>
</html>
