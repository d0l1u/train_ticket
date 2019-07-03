<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="right_con oz">

   	<c:choose>
   		<c:when test="${numMap.passNum ge 1 }">
	   	   <c:if test="${numMap.passNum le 4 }">
	   		 <div class="vip-state oz mb10">
	   		    <a class="box-a" href="/joinUs/realNameAuth.jhtml">
		      	 <dl>
		          	<dt>
		          	     <c:if test="${numMap.passNum eq '1' }">
		                 	<div class="vip-icon bronze-icon"></div>
		                  </c:if>
		                  <c:if test="${numMap.passNum ge 2 }">
		                  	<div class="vip-icon silver-icon"></div>
		                  </c:if>
		              <p class="operate update" style="text-align:center; width:63px; height:25px;line-height:25px;"><span>升级</span></p>
		              </dt>
		              <dd>
		              	  <p><label>姓名：</label><span>${numMap.passName} </span></p>
		                  <p class="vip-mes"><label>状态：</label><span class="state">已通过</span></p>
		                  <c:if test="${numMap.passNum eq 1 }">
		                 	 <p class="vip-mes"><label>等级：</label><span>铜牌用户</span></p>
		                  </c:if>
		                  <c:if test="${numMap.passNum ge 2 }">
		                  	<p class="vip-mes"><label>等级：</label><span>银牌用户</span></p>
		                  </c:if>
		              </dd>
		          </dl>  
		        </a> 
		    </div>
		  </c:if>
		  <c:if test="${numMap.passNum ge 5 }">
	   		 <div class="vip-state oz mb10">
	   			<a class="box-a" href="/joinUs/realNameAuth.jhtml">
		      	<dl>
		          	<dt>
		              	<div class="vip-icon gold-icon">
		              	</div>
		              </dt>
		              <dd>
		              	  <p><label>姓名：</label><span>${numMap.passName} </span></p>
		                  <p class="vip-mes"><label>状态：</label><span class="state">已通过</span></p>
		                  <p class="vip-mes"><label>等级：</label><span>金牌用户</span></p>
		              </dd>
		          </dl> 
		        </a>  
		    </div>
		  </c:if>
   		</c:when>
   	
   		<c:when test="${numMap.passNum eq 0 and numMap.noNum ge 1}">
	   	 <div class="vip-state oz mb10">
	   	 	<a class="box-a" href="/joinUs/realNameAuth.jhtml">
		      	<dl class="oz">
		      		<dt>
		              	<div class="vip-icon nolevel-icon"></div>
		              	<p class="operate update" style="text-align:center; width:63px; height:25px;line-height:25px;"><span>重新认证</span></p>
		              </dt>
		              <dd>
		              	<p><label>姓名：</label><span>${numMap.noName}</span></p>
		                  <p class="vip-mes"><label>状态：</label><span class="span">未通过</span></p>
		                  <p class="vip-mes"><label>等级：</label><span>无</span></p>
		              </dd>
		          </dl>   
		     </a>
		 </div>
   		</c:when>
   	
   	    <c:when test="${numMap.waitNum ge 1 }">
   		 <div class="vip-state oz mb10">
   		 	<a class="box-a" href="/joinUs/realNameAuth.jhtml">
		      	<dl>
		          	<dt>
		              	<div class="vip-icon nolevel-icon">
		              	
		              	</div>
		              <p class="operate update" style="text-align:center; width:63px; height:25px;line-height:25px;"><span>获取等级</span></p>
		              </dt>
		              <dd>
		              	<p><label>姓名：</label><span>${numMap.waitName} </span></p>
		                  <p class="vip-mes"><label>状态：</label><span class="span">审核中</span></p>
		                  <p class="vip-mes"><label>称号：</label><span>暂未获得</span></p>
		              </dd>
		          </dl>
		    </a>   
	    </div>
   	  </c:when>
   	  <c:otherwise>
   	   <div class="vip-state oz mb10">
	   	 	<a class="box-a" href="/joinUs/realNameAuth.jhtml">
		      	<dl class="oz">
		      		<dt>
		              	<div class="vip-icon nolevel-icon"></div>
		              	 <p class="operate update" style="text-align:center; width:63px; height:25px;line-height:25px;"><span>立即认证</span></p>
		              </dt>
		              <dd>
		              	<p><label>姓名：</label><span>未填写</span></p>
		                  <p class="vip-mes"><label>状态：</label><span class="span">未认证</span></p>
		                  <p class="vip-mes"><label>等级：</label><span>无</span></p>
		              </dd>
		          </dl>   
		     </a>
		 </div>
   	  </c:otherwise>
   	</c:choose>
   	

    
    <!--  
    <div class="ban_exam oz mb10">
	   	<a href="http://www.diaochaquan.cn/08/front/showSv.jsp?SVC=A77BD34248F8765A" target="_blank;">
	   		<img src="/images/exam.gif"  />
		</a>
   	</div>
    -->
    <!-- 
	<c:if test="${sessionScope.showJm_Gg!=null && sessionScope.showJm_Gg eq '1'}">
    	<div class="ban3 oz mb10"><a href="/pages/guide/business.jsp">火车票开通</a></div>
    </c:if>
     -->
     <!-- 调查 -->
	<%--<div class="activity oz mb10">
		<h2>
			<span class="nav_left_bg"></span>
			<span class="nav_mid_bg">问卷调查<s></s></span>
			<span class="nav_right_bg"></span>
		</h2>
		<div class="act_content">
			<ul>
			<li>
				<a class="pic_a" title="参与酒店需求调查投票" href="#" onclick="window.open('http://www.diaochaquan.cn/s/3BN6R')" class="img-a">
					<img width="50" height="50" src="/images/hotel_vote.gif">
				</a>
				<a title="参与酒店需求调查投票" href="#" onclick="window.open('http://www.diaochaquan.cn/s/3BN6R')" class="txt-a" >
						酒店需求调查
				</a>
				<!-- 
				<p>
					<br/>
					<a title="参与度假产品调查投票" href="#" onclick="window.open('http://www.diaochaquan.cn/s/3BN14')" class="txt-a" style="float:none;font-size:14px;line-height:25px;" >
						度假产品调查
					</a>
				</p>
				 -->
			</li>
			<li>
				<a class="pic_a" title="参与度假产品调查投票" href="#" onclick="window.open('http://www.diaochaquan.cn/s/3BN14')" class="img-a">
					<img width="50" height="50" src="/images/travel_vote.gif">
				</a>
				<a title="参与度假产品调查投票" href="#" onclick="window.open('http://www.diaochaquan.cn/s/3BN14')" class="txt-a" >
						度假产品调查
				</a>
			</li>
		</ul>
		</div>
    </div>
    
    --%><!-- 活动 -->
	<div class="activity oz mb10">
		<h2>
			<span class="nav_left_bg"></span>
			<span class="nav_mid_bg">精彩活动</span>
			<span class="nav_right_bg"></span>
		</h2>
		<div class="act_content">
			<ul>
			<li class="bottom_dashed">
				<!-- <a title="SVIP功能盛大开启" href="/pages/activity/gold_sliver_activity.jsp" class="img-a" target="blank">-->
				<a class="pic_a" title="SVIP功能盛大开启" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20140516135403/hcp/index.html')" class="img-a" >
					<img width="50" height="50" src="/images/svip_activity.gif">
				</a>
				<a title="SVIP功能盛大开启" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20141028111415/hcp/index.html')" class="txt-a" >
					SVIP功能盛大开启
				</a>
			</li>
			<li class="bottom_dashed">
				<!-- <a title="SVIP和VIP免单专享" href="/pages/activity/gold_sliver_activity.jsp" class="img-a" target="blank">-->
				<a class="pic_a" title="SVIP和VIP免单专享" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20140516135403/hcp/index.html')" class="img-a" >
					<img width="50" height="50" src="/images/svip_vip_activity.gif">
				</a>
				<a title="SVIP和VIP免单专享" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20141107165144/hcp/index.html')" class="txt-a" >
					SVIP和VIP免单专享
				</a>
			</li>
			<!--	<li class="bottom_dashed">-->
				<!-- <a title="金牌银牌专享活动" href="/pages/activity/gold_sliver_activity.jsp" class="img-a" target="blank">-->
			<!--		<a class="pic_a" title="金牌银牌专享活动" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20140516135403/hcp/index.html')" class="img-a" >
					<img width="50" height="50" src="/images/gold_sliver_activity.gif">
				</a>
				<a title="金牌银牌专享活动" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20140516135403/hcp/index.html')" class="txt-a" >
					金牌银牌免单专享
				</a>
			</li>-->
			<li class="bottom_dashed">
				<a class="pic_a" title="晒经验赢现金活动" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20140516140214/sjyyxj/index.html')" class="img-a">
					<img width="50" height="50" src="/images/sjy_activity.gif">
				</a>
				<a title="晒经验赢现金活动" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20140516140214/sjyyxj/index.html')" class="txt-a">
					晒经验赢现金活动
				</a>
			</li>
		<!--	<li class="bottom_dashed">
				<a class="pic_a" title="火车票返利专题-山西" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20140422165029/hcpfl/index.html')" class="img-a">
					<img width="50" height="50" src="/images/shanxi_activity.jpg">
				</a>
				<a title="火车票返利专题-山西" href="#" onclick="window.open('http://image30.19360.cn/resources/page/20140422165029/hcpfl/index.html')" class="txt-a">
					山西返利专场
				</a>
			</li>-->
		</ul>
		</div>
    </div>
    
    <!-- 中奖信息 -->
  	<jsp:include flush="true" page="/userIdsCardInfo/allAgentWinningInfo.jhtml">
		<jsp:param name="" value="" />
	</jsp:include>
   
    <!-- 近期订单 
  	<jsp:include flush="true" page="/query/queryLastestOrderList.jhtml">
		<jsp:param name="" value="" />
	</jsp:include>
	-->
	
	<!-- 电子保单验真 -->
	<div class="ban_bx_check oz mb10"><a href="http://www.unionlife.com.cn/tab147/" target="_blank;">电子保单验真</a></div>
    <!-- 禁止收取手续费
    <div class="ban2 oz mb10">
    </div>
     -->
    <!--  <span><a href="/complain/complainIndex.jhtml">投诉<b>-</b>建议</a></span>  -->
    <!-- 
	<div class="buy_info oz mb10">
		<h2>
			<span class="nav_left_bg"></span>
			<span class="nav_mid_bg">购买信息</span>
			<span class="nav_right_bg"></span>
		</h2>
		<jsp:useBean id="now" class="java.util.Date" />
		
		<marquee direction="up" scrollamount="2" height="185">
			<ul class="oz">
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;赵**成功购买了绅坊到上海虹桥二等座2张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;安**成功购买了长沙南到深圳北二等座2张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;赵**成功购买了绅坊到上海虹桥二等座2张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;柏**成功购买了广州到北京西硬卧下3张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;许**成功购买了呼和浩特东到重庆北硬卧下1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;周**成功购买了杭州到临海二等座2张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;孙**成功购买了泰州到北京硬卧下2张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;王**成功购买了淄博到青岛一等座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;季**成功购买了杭州到苍南一等座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;孙**成功购买了徐州东到天津西二等座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;黄**成功购买了杭州到上海虹桥二等座2张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;宾**成功购买了青岛到北京南二等座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;王**成功购买了长沙南到衡山西二等座2张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;常**成功购买了西安到昆明硬卧下5张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;卞**成功购买了青岛到北京南二等座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;沙**成功购买了昆明到贵阳硬卧下1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;史**成功购买了重庆北到成都二等座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;熊**成功购买了武昌到南昌二等座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;佘**成功购买了广州南到深圳北二等座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;蔡**成功购买了兰州到太原硬卧1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;王**成功购买了开封到梁山硬座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;何**成功购买了深圳到上饶软卧下1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;屈**成功购买了天津到临沂硬座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;蒋**成功购买了拉萨到那曲硬座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;许**成功购买了呼和浩特东到重庆北硬卧下4张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;曹**成功购买了合肥到温岭二等座3张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;高**成功购买了上海到哈尔滨硬座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;何**成功购买了义乌到秦皇岛硬卧1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;熊**成功购买了上海虹桥到麻城北二等座1张</li>
				<li><span><fmt:formatDate value="${now}" pattern="MM-dd" /></span>&nbsp;&nbsp;&nbsp;段**成功购买了三门峡到石家庄硬座1张</li>
			</ul>
		</marquee>
	</div>
	 -->
	<!--  
	<div class="user_level oz mb10">
		<h2>
			<span class="nav_left_bg"></span>
			<span class="nav_mid_bg">用户级别</span>
			<span class="nav_right_bg"></span>
		</h2>
		<ul class="oz">
			<li>
				<dl>
					<dt>
						普通用户：
					</dt>
					<dd class="name">
						张三
					</dd>
				</dl>
				<dl>
					<dt>
						北京-上海
					</dt>
					<dd class="profit">
						利润：
						<span>10</span>元
					</dd>
				</dl>
			</li>
			<li>
				<dl>
					<dt>
						普通用户：
					</dt>
					<dd class="name">
						张三
					</dd>
				</dl>
				<dl>
					<dt>
						北京-上海
					</dt>
					<dd class="profit">
						利润：
						<span>10</span>元
					</dd>
				</dl>
			</li>
			<li class="li_last">
				<dl>
					<dt>
						普通用户：
					</dt>
					<dd class="name">
						张三
					</dd>
				</dl>
				<dl>
					<dt>
						北京-上海
					</dt>
					<dd class="profit">
						利润：
						<span>10</span>元
					</dd>
				</dl>
			</li>
		</ul>
	</div>
	-->
	<!--  
    <div class="exper mb10">
		<h2>
			<span class="nav_left_bg"></span>
			<span class="nav_mid_bg"><strong><a href="javascript:void(0);" onclick="window.open('/pages/common/stuffLoad.jsp')">更多 >></a></strong>物料下载</span>
			<span class="nav_right_bg"></span>
		</h2>
		<div class="ban1 oz mb10">
     	<a href="javascript:void(0);" onclick="window.open('/pages/common/stuffLoad.jsp')"><img src="/images/ban1.jpg"  alt="" /></a>
     	</div>
	</div>
	
	<div class="exper mb10">
		<h2>
			<span class="nav_left_bg"></span>
			<span class="nav_mid_bg"><strong><a href="/pages/help/protocol.jsp">更多 >></a></strong>业务介绍</span>
			<span class="nav_right_bg"></span>
		</h2>
		<ul class="oz">
           	<li><a href="/pages/help/protocol.jsp">火车票线下代购服务协议</a></li>
        	<li><a href="/pages/help/trueFalse.jsp">新版火车票真伪识别</a></li>
            <li><a href="/pages/help/realName.jsp">实名制购票流程</a></li>
        	<li><a href="/pages/help/changeDate.jsp">改签规定</a></li>
            <li><a href="/pages/help/refund.jsp">退票规定</a></li>
            <li><a href="/pages/help/sendArea.jsp">配送范围</a></li>
            <li><a href="/pages/help/bookPeriod.jsp">车票预定期</a></li>
            <li><a href="javascript:void(0);" onclick="window.open('http://www.unionlife.com.cn/tab674/')">购票保险协议</a></li>
		</ul>
	</div>
	
	-->
</div>