<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车票预订页</title>
<link rel="stylesheet" href="/css/style.css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/trendsHeight.js"></script>
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/train_station.js?version=1008"></script>
<script type="text/javascript" src="/js/warnWindow.js"></script>
<!--[if IE 6]>
	<script language="javascript" src="/js/png.js"></script>
	<script type="text/javascript">
	   EvPNG.fix('*');
	</script>
  <![endif]--> 
 <script type="text/javascript">
 	$(function(){
 		var count="${suitTotalCount}";
 		if(count=="1"){
			openLayer('inndiv','<img src="/images/warn.jpg" alt="" ></img>',467,171);
			setTimeout('closeLayer()',4200);
			//startOpen();
 		}
 	});
 		
</script>
</head>
<body>
<div id="inndiv">
</div>
	<div class="content shouy oz">
		<!--导航条 start-->
	    <div class="main_nav">
        	<ul class="oz">
            	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
				</jsp:include>
            </ul>
        </div>
        <!--导航条 end-->
    	<!--左边内容 start-->
    	<div class="left_con oz">
            <div class="tip_term oz" style="margin:10px auto 0;">
            	<p class="price_tip">
            		<span >温馨提醒：火车票订票时间为早7:00-晚23:00，23:00之后的订单将在次日早7:00之后处理。
            		</span>
            		<span style="padding-left:100px;">业务提供方：19旅行</span>
            	</p>
            </div>
            <form id="trainForm" action="/chunqiu/buyTicket/queryByStation.jhtml" 
            	method="post" >
                <input name="from" class="from" type="hidden" />
                <input name="to" class="to" type="hidden" />
            <div class="shouy_ser mb10">
            	<h3></h3>
                <dl class="oz">
                	<dt>出发城市</dt>
                    <dd><input class="text" type="text" id="fromZh" name="from_station" 
                    		onfocus="showCity('fromZh')" onblur="hideCity()" value="出发城市" /></dd>
                </dl>
                <div class="swap" onclick="swapCity();"></div>
                <dl class="oz">
                	<dt>到达城市</dt>
                    <dd><input class="text" type="text" id="toZh" name="arrive_station" 
                    		onfocus="showCity('toZh')" onblur="hideCity()" value="到达城市" /></dd>
                </dl>
                 <dl class="oz">
                	<dt>出发日期</dt>
                    <dd><input class="text text2" type="text" id="travel_time" name="travel_time" 
                    		value="${paramMap.travel_time}" readonly="readonly" 
					  		onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+${book_day_num }}'})" /></dd>
                </dl>
                <input type="hidden" name="travel_day" value="${paramMap.travel_day}"/>
                <p style="cursor: pointer;"><img src="../../images/chunqiu_search.jpg" id="query" onclick="javascript:queryTrainInfo('','');"></img></p>
            </div>
            </form>
            <div class="shouy_r mb10">
           		<div class="ggao oz">
                	<h2><a href="/chunqiu/notice/queryNoticeAllList.jhtml">更多<b>>></b></a>公告</h2>
                    <div class="ggao_con">
						<ul>
							<c:if test="${noticeList == null}">
								<li>
							    	<span class="notice_time">
							    	</span>
							    	<span class="list">
							    		暂无公告！
							        </span>
							    </li>
							</c:if>
							<c:if test="${noticeList != null}">
								<c:forEach items="${noticeList}" var="item" varStatus="idx">
								<c:if test="${idx.index<5}">
								    <li>
								    	<span class="notice_time">
								    	<fmt:formatDate value="${item.pub_time}" pattern="yyyy/MM/dd" />
								    	</span>
								    	<span class="list">
								    	<a href="/chunqiu/notice/queryNoticeInfo_no.jhtml?noticeId=${item.notice_id}"
								    			title="${item.notice_name}">${item.notice_name}</a>
								        </span>
								    </li>
							    </c:if>
							    </c:forEach>
							   </c:if>
						</ul>
                    </div>
            	</div>
                <div class="contact">
                	<img src="/images/contactNew.jpg" alt="as" name="sd" width="393" height="83" usemap="#sdMap" border="0"  />
                </div>
            </div>
            <div class="spacer"></div>
<%--             <div class="ban mb10 oz">--%>
<%--            	<!--  <img src="/images/ban716x90.gif" width="756" height="90" alt="" onclick="window.open('http://image30.19360.cn/resources/page/20131129173335/hcp/index.html')" style="cursor: hand;"/>-->--%>
<%--            	<img src="/images/ban_cmpay.jpg" width="780" height="90" alt="" />--%>
<%--            </div>--%>
            <!--高铁模块 start-->
            <div class="box-mod">
                <div class="box_con_mod">
			        <div class="gt_wrap oz" id="gt_wrap">
			        	<dl>
			            	<dt>
			                	<h3>热门高铁</h3>
			                	<c:forEach var="list" items="${highTrainList}" varStatus="idx">
			                		<c:if test="${idx.index == 0}">
			                			<span class="current"><a href="javascript:void(0);">${list.key}</a></span>
		            				</c:if>
		            				<c:if test="${idx.index != 0}">
			                			<span><a href="javascript:void(0);">${list.key}</a></span>
		            				</c:if>
			                	</c:forEach>
			                </dt>
			                <c:forEach var="trainInfo" items="${highTrainList}" varStatus="no">
			                	<c:if test="${no.index == 0}">
			                		<dd class="current">
					                	 <ul class="fl">
					                		<c:forEach var="train" items="${trainInfo.value}" varStatus="num">
					                			<c:if test="${num.index <= 4}">
					                				<li>
							                        	<label class="checi"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');" >${train.train_code}</a></label>
							                            <c:if test="${train.end_station eq train.arrive_station}">
															<label class="fromto_city"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');"><span class="start">${train.from_station}</span>
							                            	到&nbsp;&nbsp;<span class="end">${train.arrive_station}</span></a></label>
							                            	<label class="price">${train.price}</label>
														</c:if>
														<c:if test="${train.end_station ne train.arrive_station}">
							                            	<label class="fromto_city"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');"><span class="start">${train.from_station}</span>
							                            	到&nbsp;&nbsp;<span class="pass">${train.arrive_station}</span></a></label>
							                            	<label class="price">${train.price}</label>
							                            </c:if>
						                        	</li>
					                			</c:if>
					                		</c:forEach>
						                </ul>
		                				<ul class="fr">
					                		<c:forEach var="train" items="${trainInfo.value}" varStatus="num">
					                			<c:if test="${num.index > 4}">
					                				<li>
							                        	<label class="checi"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');">${train.train_code}</a></label>
						                            	<c:if test="${train.end_station eq train.arrive_station}">
															<label class="fromto_city"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');"><span class="start">${train.from_station}</span>
							                            	到&nbsp;&nbsp;<span class="end">${train.arrive_station}</span></a></label>
														</c:if>
														<c:if test="${train.end_station ne train.arrive_station}">
							                            	<label class="fromto_city"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');"><span class="start">${train.from_station}</span>
							                            	到&nbsp;&nbsp;<span class="pass">${train.arrive_station}</span></a></label>
							                            </c:if>
							                            <label class="price">${train.price}</label>
						                        	</li>
					                			</c:if>
					                		</c:forEach>
					                    </ul>
				                    </dd>
			                	</c:if>
			                	<c:if test="${no.index != 0}">
			                		<dd >
						                <ul class="fl">
					                		<c:forEach var="train" items="${trainInfo.value}" varStatus="num">
					                			<c:if test="${num.index <= 4}">
					                				<li>
							                        	<label class="checi"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');">${train.train_code}</a></label>
							                            <c:if test="${train.end_station eq train.arrive_station}">
															<label class="fromto_city"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');"><span class="start">${train.from_station}</span>
							                            	到&nbsp;&nbsp;<span class="end">${train.arrive_station}</span></a></label>
														</c:if>
														<c:if test="${train.end_station ne train.arrive_station}">
							                            	<label class="fromto_city"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');"><span class="start">${train.from_station}</span>
							                            	到&nbsp;&nbsp;<span class="pass">${train.arrive_station}</span></a></label>
							                            </c:if>
							                            <label class="price">${train.price}</label>
						                        	</li>
					                			</c:if>
					                		</c:forEach>
						                </ul>
		                				<ul class="fr">
					                		<c:forEach var="train" items="${trainInfo.value}" varStatus="num">
					                			<c:if test="${num.index > 4}">
					                				<li>
							                        	<label class="checi"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');">${train.train_code}</a></label>
							                            <c:if test="${train.end_station eq train.arrive_station}">
															<label class="fromto_city"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');"><span class="start">${train.from_station}</span>
							                            	到&nbsp;&nbsp;<span class="end">${train.arrive_station}</span></a></label>
														</c:if>
														<c:if test="${train.end_station ne train.arrive_station}">
							                            	<label class="fromto_city"><a href="javascript:queryTrainInfo('${train.from_station}','${train.arrive_station}');"><span class="start">${train.from_station}</span>
							                            	到&nbsp;&nbsp;<span class="pass">${train.arrive_station}</span></a></label>
							                            </c:if>
							                            <label class="price">${train.price}</label>
						                        	</li>
					                			</c:if>
					                		</c:forEach>
					                    </ul>
				                    </dd>
			                	</c:if>
			                </c:forEach>
			            </dl>
			        </div>
		          </div>
           </div>
        <!--高铁模块 end-->
        </div>
        <!--左边内容 end-->
    </div>
    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
<script type="text/javascript" language="JavaScript">
	$().ready(function(){
		/*高铁tab选项卡 start*/
		$("#gt_wrap dt span").mouseover(function(){
			$(this).addClass('current').siblings().removeClass('current');
			$(this).parents('#gt_wrap').find('dd:eq('+($(this).index()-1)+')').addClass('current').siblings().removeClass('current');
		})
		/*高铁tab选项卡 end*/
		
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

	function queryTrainInfo(from_station,arrive_station){
			var isValid=true;
			if(from_station!=null && from_station!=""){
				$("#fromZh").val(from_station);
			}else{
				if($.trim($("#fromZh").val())=="" || $.trim($("#fromZh").val())=="出发城市"){
					showErrMsg("fromZh", "110px", "请输入出发城市！");
					isValid=false;
				}else{
					hideErrMsg("fromZh");
				}
			}

			if(arrive_station!=null&&arrive_station!=""){
				$("#toZh").val(arrive_station);
			}else{
				if($.trim($("#toZh").val())=="" || $.trim($("#toZh").val())=="到达城市"){
					showErrMsg("toZh", "110px", "请输入到达城市！");
					isValid=false;
				}else{
					hideErrMsg("toZh");
				}
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
	function swapCity(){
		var fromCity = $("#fromZh").val().replace("出发城市", "");
		var toCity = $("#toZh").val().replace("到达城市", "");
		$("#fromZh").val(toCity);
		$("#toZh").val(fromCity);
	}
</script>
</body>
</html>
