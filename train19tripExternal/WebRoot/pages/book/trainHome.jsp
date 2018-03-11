<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车票预订页</title>
<link rel="stylesheet" href="/css/style.css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
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
            <form id="trainForm" action="/foreign/queryRemainTicket.jhtml" method="post" > 
            <!-- <form id="trainForm" action="/mobileTicket/queryRemainTicket.jhtml" method="post" >-->
            	
                <input name="from" class="from" type="hidden" />
                <input name="to" class="to" type="hidden" />
            <div class="shouy_ser mb10">
            	<h3>火车票查询</h3>
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
					  		onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+18}'})" /></dd>
                </dl>
                <input type="hidden" name="travel_day" value="${paramMap.travel_day}"/>
                <p><input type="button" class="btn" id="query" value="搜 索" /></p>
            </div>
            </form>
            <div class="shouy_r mb10">
           		<div class="ggao oz">
                	<h2><a href="/notice/queryNoticeAllList.jhtml">更多<b>>></b></a>公告</h2>
                    <div class="ggao_con">
						<ul>
							<c:forEach items="${noticeList}" var="item" varStatus="idx">
							<c:if test="${idx.index<10}">
							    <li>
							    	<span class="notice_time">
							    	<fmt:formatDate value="${item.pub_time}" pattern="yyyy/MM/dd" />
							    	</span>
							    	<span class="list">
							    	<a href="/notice/queryNoticeInfo_no.jhtml?noticeId=${item.notice_id}"
							    			title="${item.notice_name}">${item.notice_name}</a>
							        </span>
							    </li>
						    </c:if>
						    </c:forEach>
						</ul>
                    </div>
            	</div>
            	<!-- 
            	<ul class="ticketstyle">
                	<li class="zg">
                    	<h5>正规</h5>
                        <p>代售点提供代购服务</p>
                    </li>
                    <li class="ss">
                    	<h5>实时</h5>
                    	<p>查询火车票余票信息</p>
                    </li>
                    <li class="aq">
                    	<h5>安全</h5>
                    	<p>先确认后支付票款</p>
                    </li>
                    <li class="kj">
                    	<h5>快捷</h5>
                    	<p>一流配送服务快捷及时</p>
                    </li>
                </ul>
                 -->
                <div class="contact">
                	<img src="/images/contact.jpg" alt="as" name="sd" width="436" height="83" usemap="#sdMap" border="0"  />
                    <map name="sdMap" id="sdMap">
                      <area shape="rect" coords="306,38,423,64" href="tencent://message/?uin=800001919" />
                    </map>
                </div>
                
                <!--  <ul class="contact oz">
                    <li>
                    	<h6 class="kf">加盟联系QQ</h6>
                        <a href="tencent://message/?uin=930812452"><p>930812452</p></a>
                    </li>
                	<li>
                    	<h6 class="tel">客服联系电话</h6>
                        <p>400-698-6666</p>
                    </li>
                    <li>
                    	<h6 class="kf">合作联系QQ</h6>
                        <a href="tencent://message/?uin=930812452"><p>2803964633</p></a>
                    </li>
                    <li>
                    	<h6 class="tel">票务商联系电话</h6>
                        <p>01057386868-6366</p>
                    </li>
                </ul>-->
            </div>
            <div class="spacer"></div>
            <div class="ban mb10 oz">
            	<img src="/images/ban716x90.gif" width="756" height="90" alt="" onclick="window.open('http://image30.19360.cn/resources/page/20131129173335/hcp/index.html')" style="cursor: hand;"/>
            </div>
            <!--常见问题 start-->
             <div class="ques oz">
            	<h3>常见问题</h3>
                <div class="gp_flow oz">
                    <p>
                    	<span class="s1"><b>1</b>申请开通</span><span class="s2"><b>2</b>查询列车时刻</span><span class="s3"><b>3</b>选择车次/坐席/下单</span><span class="s4"><b>4</b>付款</span><span class="s5"><b>5</b>自取配送</span><span class="s6"><b>6</b>车站自取/快递签收</span>
                    </p>
                </div>
                <div class="gp_weak oz">
                	<h5 style="font-size: 20px;">业务提示</h5>
                	 
                    <p style="margin-top: 10px;">为了帮助老百姓享受通过互联网购买火车票的便捷，结合19e网点覆盖面广和便利性等特点，我们推出火车票订购业务，此业务<font style="font-size: 18px;">免费开通</font>，除票面金额外，不会收取任何多余的手续费：</p>

                	<dl>
                    	<dt><a style="font-size: 15px;">1.为什么申请开通？</a><c:if test="${sessionScope.showJm_Gg!=null && sessionScope.showJm_Gg eq '1'}"><a href="/joinUs/joinIndex.jhtml" style="color: red;">(点击这里免费开通)</a></c:if></dt>
                        <dd>
                        	&nbsp;&nbsp;&nbsp;&nbsp;为了保证火车票业务代理网点的服务质量，避免乱收费的问题，我们采用代理商开通审核的方式，通过各省分公司对开通的代理商进行业务监督、指导、售前和售后服务；提交开通审核后我们会在5个工作日内对网点进行审核标准如下：
                       		<ul>
                            	<li>同意<a href="javascript:void(0);" onclick="window.open('/pages/common/protocolPop.jsp')">《火车票线下代购服务协议》</a>中的条款</li>
                            	<li>100米之内不允许有车票售卖点</li>
                                <li>愿意接受19e分公司的业务监督和指导</li>
                                <li>承诺不收取除购买款外任何多余的手续费</li>
                            </ul>
                        </dd> 
                    </dl>
                    <div style="border-bottom:1px dashed #dadada;border-top:0;margin:5px;"></div>
                    <dl>
                    	<dt><a style="font-size: 15px;">2.怎么获取火车票？</a></dt>
                        <dd>&nbsp;&nbsp;目前有两种取票方式： 
                       		<ul>
                            	<li style="float:left;width:380px;">
                            		电子票：
                            		<ul>
                            			<li>方式：自提取票 </li>
                            			<li>说明：拿购票人的身份证到火车站的取票点进行取票</li>
                            			<li>特点：出行时间自由掌握，火车站取票方便快捷</li>
                            		</ul>
                           		</li>
	                            <li style="float:left;">
	                           		配送票：
	                           		<ul>
	                           			<li>方式：配送取票 </li>
	                           			<li>说明：利用快递的方式配送到客户指定的地点</li>
	                           			<li>特点：方便省心，快递送票上门</li>
	                           		</ul>
	                            </li>
                            </ul>
                        </dd> 
                        <dd>&nbsp;&nbsp;注意：配送票会产生一定的配送费用，我们根据不同地区进行配送费用的收取。</dd>
                    </dl>
                    <div style="border-bottom:1px dashed #dadada;border-top:0;margin:5px;"></div>
                    <dl>
                    	<dt><a style="font-size: 15px;">3.利润怎么获取？</a></dt>
                        <dd style="text-indent:2em;">可以推荐用户购买其它19e产品，例如保险费等。</dd> 
                    </dl>
                	<div style="border-bottom:1px dashed #dadada;border-top:0;margin:5px;"></div>
                </div>
                <div class="gp_ex oz">
                	<h5 style="font-size: 20px;">销售举例</h5>
                    <p>郭先生从19e小店定了2张北京到乌海的K43软卧火车票：</p>
                    <ul class="oz">
                    	<li class="bb">
                        	<dl>
                            	<dt>车票：</dt>
                            	<dd>价格398.5元/张，总计797元</dd>
                            </dl>
                        </li>
                        <li class="bb">
                        	<dl>
                        		<dt>保险：</dt>
                        		<dd>可选保额20万的交通轨道保险</dd>
                        	</dl>
                        </li>
                        <li>
                        	<dl>
                        		<dt>收郭先生现金：</dt>
                        		<dd>金额：797元（车票总计）</dd>
                        	</dl>
                        </li>
                         <li>
                        	<dl>
                        		<dt>19e平台支付：</dt>
                        		<dd>金额：797元（车票总计）</dd>
                        	</dl>
                        </li>
                    </ul>
                    
                </div>
            </div>
            <!--常见问题 end-->

        </div>
        <!--左边内容 end-->
        
        <!--右边内容 start-->
        <%@ include file="/pages/common/right.jsp"%>
    	<!--右边内容 end-->
    </div>
    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
<script type="text/javascript" language="JavaScript">
	$().ready(function(){
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
			$obj=$("#tip").clone()
				.attr("id", id+"_errMsg")
				.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width}).appendTo("body");
			$obj.find(".errMsg").text(msg).end().show();
		}
		
		function hideErrMsg(id){
			$("#"+id+"_errMsg").remove();
		}
	});
	function swapCity(){
		var fromCity = $("#fromZh").val().replace("出发城市", "");
		var toCity = $("#toZh").val().replace("到达城市", "");
		$("#fromZh").val(toCity);
		$("#toZh").val(fromCity);
	}
</script>
</body>
</html>
