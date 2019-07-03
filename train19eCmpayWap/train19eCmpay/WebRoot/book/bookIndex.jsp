<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车票预订页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/train_station.js?version=1008"></script>
</head>

<body>
	<div class="content oz">
		<!--导航条 start-->
	    <div class="main_nav">
        	<ul class="oz">
            	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--左边内容 start-->
    	<div class="left_con oz">
            <div class="tip_term oz" style="width:756px;margin:10px auto 0;">
            	<p class="price_tip">温馨提醒：火车票订票时间为早7:00-晚23:00，23:00之后的订单将在次日早7:00之后处理。<span 
            		style="color:#0081cc;cursor:pointer;line-height:24px;" 
            		onclick="javascript:window.location='http://ehcp.19e.cn/notice/queryNoticeInfo_no.jhtml?noticeId=57'">详情请点击查看</span></p>
            </div>
            <form id="trainForm" action="/buyTicket/queryByStation.jhtml" 
            	method="post" >
             <div class="ser_tab oz" style="margin-top:5px;">
                <ul class="tab">
                	<s></s>
                </ul>
                <div class="tab_list oz">
                    <div class="shou_query">
		                <dl class="oz">
		                	<dt>出发城市</dt>
		                    <dd><input class="text" type="text" id="fromZh" name="from_city" 
		                    		onfocus="showCity('fromZh')" onblur="hideCity()" value="${paramMap.from_city}" /></dd>
		                </dl>
		                <div class="swap" onclick="swapCity();"></div>
		                <dl class="oz">
		                	<dt>到达城市</dt>
		                    <dd><input class="text" type="text" id="toZh" name="to_city" 
		                    		onfocus="showCity('toZh')" onblur="hideCity()" value="${paramMap.to_city}" /></dd>
		                </dl>
		                <div id="query" class="search_query search_hover" onmouseover="this.className='search_query search_link'" onmouseout="this.className='search_query search_hover'"></div>
		                <dl class="oz">
		                	<dt>出发日期</dt>
		                    <dd><input class="text text2" type="text" id="travel_time" name="travel_time" 
		                    		value="${paramMap.travel_time}" readonly="readonly" 
							  		onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+18}'})" /></dd>
		                </dl>
		            </div>
                    <div class="notice oz">
			  			<jsp:include flush="true" page="/notice/queryNoticeList.jhtml">
			  				<jsp:param name="" value=""/>
						</jsp:include>
                    </div>
                 </div>
            </div>

            <div class="ser_term oz">
            	
                <h2>
                	<span class="nav_left_bg"></span>
                	<span class="nav_mid_bg"><span class="amount">共<strong id="train_total">${fn:length(otVo.dataList)}</strong>列</span><strong>${paramMap.from_city}</strong>到<strong>${paramMap.to_city}</strong>的列车</span>
                	<span class="nav_right_bg"></span>
                    
                </h2>
                <div class="box oz">
                     <!--查询条件区域-->
                     <ul class="sel_checkbox oz" id="sel_checkbox">
                            <li id="trainType">
                                <strong>车型选择：</strong>
                                <label id="selectALLTrain" class="choose_all" for="sx_cb1">全选</label>
                                <input type="checkbox" id="trainType_GD" class="filter trainType" value="G/D"/><label for="trainType_GD">G高铁/D动车</label>
                                <input type="checkbox" id="trainType_G" class="filter trainType" value="G"/><label for="trainType_G">G高铁</label>
                                <input type="checkbox" id="trainType_C" class="filter trainType" value="C"/><label for="trainType_C">C城际</label>
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
                            <li id="startAfter" style="display:none">
                            	<strong>可否预订：</strong>
					            <label id="selectStartAfter" class="choose_all" for="sx_cb1">全选</label>
					            <span><input type="checkbox" id="ableBook" class="filter weatherBook" value="预 订" filter="o"><label for="ableBook">可以预订</label></span>
					            <span><input type="checkbox" id="unableBook" class="filter weatherBook" value="不可预订" filter="o"><label for="unableBook">不可预订</label></span>
                            </li>
                        </ul>
                        <div class="down-arrow" id="ser-more"><a href="javascript:void(0);">更多筛选条件</a></div> 
                </div> 
              </div>
              <div class="ser_result">  
                <table>
                    <tr class="tit">
                        <th width="12%">车次/车型</th>
                        
                        <th width="16%">发站/到站</th>
                        <th width="12%">发车/到达</th>
                        <th width="12%">运行时间</th>
                        <th style="padding-left:40px;text-align:left;">参考票价[余票]</th>
                        <th width="18%"></th>
                    </tr>
                    <c:if test="${otVo.dataList == null || fn:length(otVo.dataList)==0}">
	                    <tr>
	                    	<td colspan="6" style="font:bold 16px/60px 'microsoft yahei','宋体';color:#f60;">对不起，暂无您查询的车次信息！</td>
	                    </tr>
                    </c:if>
                    <c:forEach items="${otVo.dataList}" var="list">
		                    <tr id="${list.id}_ticket_${fn:substring(list.trainCode, 0, 1)}_${tn:fromTimeStr(list.startTime)}_${tn:toTimeStr(list.endTime)}" class="trainBar" startTime="${list.startTime}">
		                        <td class="wayStastion">
		                            <strong class="checi" id="checi_${list.id}">${list.trainCode}</strong></br>
		                            <div class="down-arrow" value="${list.startCity}_${list.endCity}"><a href="javascript:void(0);">途经站</a></div>
		                        </td>
		                        <td class="adr">
		                        	<span id="${list.id}_${list.startCity}" class="startname start">${list.startCity}</span><br/>
		                        	<span class="endname end" id="${list.id}_${list.endCity}">${list.endCity}</span>
		                        </td>
		                        <td class="fd"><span>${list.startTime}</span><br />${list.endTime}</td>
		                        <td>${list.costTime}</td>
		                        <td class="seat">
			                        <c:if test="${list.yz ne '-'}">硬座<span>￥${list.yz} [${list.yz_yp_show}]</span><br /></c:if>
			                        <c:if test="${list.rz ne '-'}">软座<span>￥${list.rz} [${list.rz_yp_show}]</span><br /></c:if>
			                        <c:if test="${list.rz1 ne '-'}">一等座<span>￥${list.rz1} [${list.rz1_yp_show}]</span><br /></c:if>
			                        <c:if test="${list.rz2 ne '-'}">二等座<span>￥${list.rz2} [${list.rz2_yp_show}]</span><br /></c:if>
			                        <c:if test="${list.yws ne '-'}">硬卧<span>￥${list.yws}/${list.ywz}/${list.ywx} [${list.yw_yp_show}]</span><br /></c:if>
			                        <c:if test="${list.rws ne '-'}">软卧<span>￥${list.rws}/${list.rwx} [${list.rw_yp_show}]</span><br/></c:if>
			                        <c:if test="${list.gws ne '-'}">高级软卧<span>￥${list.gws}/${list.gwx} [${list.gw_yp_show}]</span><br/></c:if>
			                        <c:if test="${!empty list.tdz && list.tdz ne '-'}">特等座<span>￥${list.tdz} [${list.tdz_yp_show}]</span><br/></c:if>
			                        <c:if test="${!empty list.swz && list.swz ne '-'}">商务座<span>￥${list.swz} [${list.swz_yp_show}]</span><br/></c:if>
		                        </td>
		                        <td>
			                        <c:choose>
			                        	<c:when test="${sessionScope.showJm_Gg eq '2'}">
			                        		<input type="button" class="btn2" value="区域未开通" />
			                        	</c:when>
			                        	<c:when test="${sessionScope.menuDisplay ne 'all'}">
			                        		<input type="button" class="btn2" value="未开通" />
			                        	</c:when>
			                        	<c:otherwise>
			                        		<c:choose>
					                        	<c:when test="${list.canBook eq '1'}">
					                        		<input type="button" id="${list.id}_ableBook" class="btn1 bookTicket btn_link" value="预 订" onmouseover="this.className='btn1 bookTicket btn_hover'" onmouseout="this.className='btn1 bookTicket btn_link'"
					                        			onclick="gotoBookOrder('${otVo.sdate}','${list.trainCode}','${list.startCity}','${list.endCity}','${list.startTime}','${list.endTime}','${list.costTime}','无座_${list.yz eq '-'?list.rz2:list.yz}_${list.wz_yp},硬座_${list.yz}_${list.yz_yp},软座_${list.rz}_${list.rz_yp},一等座_${list.rz1}_${list.rz1_yp},二等座_${list.rz2}_${list.rz2_yp},硬卧_${list.ywx}_${list.yw_yp},软卧_${list.rwx}_${list.rw_yp},高级软卧_${list.gwx}_${list.gw_yp},特等座_${list.tdz}_${list.tdz_yp},商务座_${list.swz}_${list.swz_yp}');"/>
					                        	</c:when>
					                        	<c:otherwise>
					                        		<input type="button" id="${list.id}_unableBook" class="btn2 bookTicket" value="不可预订" />
					                        	</c:otherwise>
					                        </c:choose>
			                        	</c:otherwise>
			                        </c:choose>
		                        </td>
		                    </tr>
		                    <tr>
		                    	<td colspan='6' style="padding:0;border-bottom:none;">
			                    	<div id="train_station_checi_${list.id}" class="train_station" style="display:none">
			                    		<div class="station_tit oz">
								   		    <span class="zx">站序</span>
								            <span class="zm">站名</span>
								            <span class="rq">天数</span>
								            <span class="dzsj">到站时间</span>
								            <span class="fcsj">出发时间</span>
								            <span class="tlsj">停留时间</span>
								            <span class="lcs">里程数</span>
								        </div>
								        <div id="station_con_checi_${list.id}" class="station_con">
							            	<table> 
							            		<tbody id="station_tbody_checi_${list.id}">
							            		</tbody>
							            	</table>
							            </div>
							            <b class="station_icon"></b>
			                    	</div>
		                    	</td>
		                    </tr>
                    </c:forEach>
                </table>
              </div>
              </form>	
        </div>
        <!--左边内容 end-->
        
        <!--右边内容 start-->
        <%@ include file="/pages/common/right.jsp"%>
    	<!--右边内容 end-->
    </div>
   	<div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
<script type="text/javascript" language="JavaScript">
	$().ready(function(){
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
	
	//进入下单页面
	function gotoBookOrder(travelTime, trainCode, startCity, endCity, startTime, endTime, costTime, seatMsg){
		var is_buyable="${is_buyable}";
		if(is_buyable == "00"){
			alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
			return false;
		}
		var url="/buyTicket/gotoBookOrder.jhtml?travelTime="+travelTime+"&trainCode="+trainCode+"&startCity="+startCity+"&endCity="+endCity+"&startTime="+startTime+"&endTime="+endTime+"&costTime="+costTime+"&seatMsg="+seatMsg;
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
<script type="text/javascript" language="JavaScript">
$().ready(function(){
		var top = ""; 
		var left = "";
		
		//途经站
		$(".wayStastion").click(function(){
			var chufa = $(this).children("div").attr("class");
			var checi = $(this).children("strong").attr("id");
			if(chufa=="up-arrow"){
				$("#train_station_"+checi).hide();
				$(this).children("div").attr("class","down-arrow");
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
				top = $(this).offset().top+$(this).height();
				left = $(this).offset().left+$(this).width();
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
							
							//$("#train_station_"+checi).slideDown(1000);
							$("#train_station_"+checi).show();
						}
					});
				}
				
			}
		});
});
</script>
</body>
</html>
