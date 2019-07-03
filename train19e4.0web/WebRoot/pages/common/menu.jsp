<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<% if (request.getProtocol().compareTo("HTTP/1.0")==0) 
response.setHeader("Pragma","no-cache"); 
if (request.getProtocol().compareTo("HTTP/1.1")==0) 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires",0); %>

<%!
	String getParam(HttpServletRequest request, String param) {
		return request.getParameter(param) == null ? "" : request.getParameter(
				param).toString().trim();
	}

	boolean isMyMenu(HttpServletRequest request, String menuId) {
		if (menuId.equals(getParam(request, "menuId"))) {
			return true;
		} else {
			return false;
		}
	}
%>
<%@include file="/pages/common/height.jsp"%>
<!--左边内容 start-->
<div id="new_left">
<!--侧边栏 start-->
<script type="text/javascript">
$().ready(function(e){
	$(".sidebar_con li").hover(function(){
			var myindex=$(this).index()+1;
		
			$(this).find("div").addClass("current_cat"+myindex);
			$(this).siblings().each(function(index, element) {
			   $(this).find("div").removeClass(); 
			});
		
			$(this).find("span").addClass("gcurrent");
			$(this).siblings().each(function(index, element) {
				$(this).find("span").removeClass(); 
        });},function(){
		
			var myindex=$(this).index()+1;
			var mymum=$(this).index()
			$(this).find("div").removeClass("current_cat"+myindex).addClass("cat"+mymum);
			$(this).siblings().each(function(index, element) {
           		$(this).find("div").removeClass(); 
        });
				$(this).find("span").removeClass("gcurrent")
		}
		);	
	//以上实现鼠标移上和移出li的样式改变	
	
	
	//以下是鼠标移走之后恢复最初当前页面选项
		var onc_id = $(".current_onclick").attr("id");
		if(onc_id){
			var idx = $(".current_onclick").attr("id").substring(12);
			//alert(idx);
			var n = parseInt(idx)-1;
			$(".sidebar_con").mouseleave(function(){
			$(".sidebar_con li").eq(n).find("div").addClass("current_cat"+idx);
			$(".sidebar_con li").eq(n).find("div").addClass("current_onclick");
			$(".sidebar_con li").eq(n).siblings().removeClass();
			$(".sidebar_con li").eq(n).find("span").addClass("gcurrent").siblings().removeClass();
			});
		}
			
	});

//$(".current_cat1").onclick(function(){
//	alert("aa");
	//var id = $(".current_choose").attr("id");
//});	
function toHref(id, url){
	var index = id.substring(12);
	var oldId = $(".current_onclick").attr("id");
	$("#"+oldId).removeClass("current_onclick");
	$("#"+id).addClass("current_onclick");
	//alert(oldId+"   "+id);
	window.location = url;
}
 var objWin; 
function toOpen(url){
            //判断是否打开 
            if (objWin == null || objWin.closed) { 
                objWin = window.open(url); 
            } else { 
                objWin.location.replace(url); 
            } 
}
</script>

		<h3 class="sidebar_tit"  onclick="toHref(this.id, '/buyTicket/bookIndex.jhtml');" style="cursor: pointer;">
			火车票业务
		</h3>
		<!--登录状态 start-->
		<c:choose>
   		<c:when test="${numMap.passNum ge 1 }">
	   	   <c:if test="${numMap.passNum le 4 }">
	   		 <div class="vip-state oz">
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
		              	  <p class="vip-mes"><label>姓名：</label><span  style="color: #555555">${numMap.passName} </span></p>
		              	  
		              	  <c:choose>
		              	  	<c:when test="${status eq 'passed'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">已开通</span></p>
		              	  	</c:when>
		              	 	<c:when test="${status eq 'nopass'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未通过</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'waiting'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">审核中</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'nozhuce'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未开通</span></p>
		              	  	</c:when>
		              	  </c:choose>
		              	  
		                  <p class="vip-mes"><label>实名：</label><span class="state">已通过</span></p>
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
	   		 <div class="vip-state oz">
	   			<a class="box-a" href="/joinUs/realNameAuth.jhtml">
		      	<dl>
		          	<dt>
		              	<div class="vip-icon gold-icon">
		              	</div>
		              </dt>
		              <dd>
		              	  <p class="vip-mes"><label>姓名：</label><span  style="color: #555555">${numMap.passName} </span></p>
		                    
		              	  <c:choose>
		              	  	<c:when test="${status eq 'passed'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">已开通</span></p>
		              	  	</c:when>
		              	 	<c:when test="${status eq 'nopass'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未通过</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'waiting'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">审核中</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'nozhuce'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未开通</span></p>
		              	  	</c:when>
		              	  </c:choose>
		                  <p class="vip-mes"><label>实名：</label><span class="state">已通过</span></p>
		                  <p class="vip-mes"><label>等级：</label><span>金牌用户</span></p>
		              </dd>
		          </dl> 
		        </a>  
		    </div>
		  </c:if>
   		</c:when>
   	
   		<c:when test="${numMap.passNum eq 0 and numMap.noNum ge 1}">
	   	 <div class="vip-state oz">
	   	 	<a class="box-a" href="/joinUs/realNameAuth.jhtml">
		      	<dl class="oz">
		      		<dt>
		              	<div class="vip-icon nolevel-icon"></div>
		              	<p class="operate update" style="text-align:center; width:63px; height:25px;line-height:25px;"><span>重新认证</span></p>
		              </dt>
		              <dd>
		              	<p class="vip-mes"><label>姓名：</label><span  style="color: #555555">${numMap.noName}</span></p>
		                   
		              	  <c:choose>
		              	  	<c:when test="${status eq 'passed'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">已开通</span></p>
		              	  	</c:when>
		              	 	<c:when test="${status eq 'nopass'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未通过</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'waiting'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">审核中</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'nozhuce'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未开通</span></p>
		              	  	</c:when>
		              	  </c:choose>
		                  <p class="vip-mes"><label>实名：</label><span class="state">未通过</span></p>
		                  <p class="vip-mes"><label>等级：</label><span>无</span></p>
		              </dd>
		          </dl>   
		     </a>
		 </div>
   		</c:when>
   	
   	    <c:when test="${numMap.waitNum ge 1 }">
   		 <div class="vip-state oz">
   		 	<a class="box-a" href="/joinUs/realNameAuth.jhtml">
		      	<dl>
		          	<dt>
		              	<div class="vip-icon nolevel-icon">
		              	</div>
		              <p class="operate update" style="text-align:center; width:63px; height:25px;line-height:25px;"><span>获取等级</span></p>
		              </dt>
		              <dd>
		              	<p class="vip-mes"><label>姓名：</label><span style="color: #555555">${numMap.waitName} </span></p>
		                   
		              	  <c:choose>
		              	  	<c:when test="${status eq 'passed'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">已开通</span></p>
		              	  	</c:when>
		              	 	<c:when test="${status eq 'nopass'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未通过</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'waiting'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">审核中</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'nozhuce'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未开通</span></p>
		              	  	</c:when>
		              	  </c:choose>
		                  <p class="vip-mes"><label>实名：</label><span class="state">审核中</span></p>
		                  <p class="vip-mes"><label>等级：</label><span>暂未获得</span></p>
		              </dd>
		          </dl>
		    </a>   
	    </div>
   	  </c:when>
   	  <c:otherwise>
   	   <div class="vip-state oz">
	   	 	<a class="box-a" href="/joinUs/realNameAuth.jhtml">
		      	<dl class="oz">
		      		<dt>
		              	<div class="vip-icon nolevel-icon"></div>
		              	 <p class="operate update" style="text-align:center; width:63px; height:25px;line-height:25px;"><span>立即认证</span></p>
		              </dt>
		              <dd>
		              	<p class="vip-mes"><label>姓名：</label><span  style="color: #555555">未填写</span></p>
		                  
		              	  <c:choose>
		              	  	<c:when test="${status eq 'passed'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">已开通</span></p>
		              	  	</c:when>
		              	 	<c:when test="${status eq 'nopass'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未通过</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'waiting'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">审核中</span></p>
		              	  	</c:when>
		              	  	<c:when test="${status eq 'nozhuce'}">
		              	  	  <p class="vip-mes"><label>状态：</label><span style="color:#555555">未开通</span></p>
		              	  	</c:when>
		              	  </c:choose>
		                  <p class="vip-mes"><label>实名：</label><span class="state">未认证</span></p>
		                  <p class="vip-mes"><label>等级：</label><span>无</span></p>
		              </dd>
		          </dl>   
		     </a>
		 </div>
   	  </c:otherwise>
   	</c:choose>
		<!--登录状态 end-->
	<div id="sidebar">	
		
<c:choose>
	<c:when test="${menuDisplay eq 'all'}">
	<ul class="sidebar_con" id="all_menu">
		<li style="border-left: 4px #92d14f solid;" >
			<div id="currentMenu_1" <% if (isMyMenu(request, "book") || isMyMenu(request, "bookHome") ) { %> class="current_cat1 current_onclick" <% }else{ %>class="cat1"<%} %> 
				onclick="toHref(this.id, '/buyTicket/bookIndex.jhtml');"> 
				<a href="#">车票预订</a> 
				<span <% if (isMyMenu(request, "book") || isMyMenu(request, "bookHome")) { %> class="gcurrent" <% } %>></span> 
			</div>
		</li>
		<li style="border-left: 4px #a04ed1 solid;" >
			<div id="currentMenu_2" <% if (isMyMenu(request, "query")) { %> class="current_cat2 current_onclick" <% }else{ %> class="cat2" <%}%> 
				onclick="toHref(this.id, '/query/queryOrderList.jhtml');" > 
				<a href="#">我的订单</a> 
				<span <% if (isMyMenu(request, "query")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li style="border-left: 4px #a04ed1 solid;" >
			<div id="currentMenu_2" <% if (isMyMenu(request, "query")) { %> class="current_cat2 current_onclick" <% }else{ %> class="cat2" <%}%> 
				onclick="toHref(this.id, '/rob/robOrderList.jhtml');" > 
				<a href="#">抢票订单</a> 
				<span <% if (isMyMenu(request, "query")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li  style="border-left: 4px #fca0ab solid;" >
			<div id="currentMenu_3" <% if (isMyMenu(request, "saleReport")) { %> class="current_cat3 current_onclick" <% }else{ %>class="cat3"<%} %> 
				onclick="toHref(this.id, '/query/querySaleReport.jhtml');" > 
				<a href="#">销售报表</a> 
				<span <% if (isMyMenu(request, "saleReport")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<!--	<li  style="border-left: 4px #c3a9aa solid;" >
			<div id="currentMenu_4" <% //if (isMyMenu(request, "join")) { %> class="current_cat4 current_onclick" <% //}//else{ %>class="cat4"<%//} %> 
				onclick="toHref(this.id, '/joinUs/agentInfoIndex.jhtml');"  > 
				<a href="#">个人中心</a> 
				<span <% //if (isMyMenu(request, "join")) { %> class="gcurrent" <%// } %>></span>
			</div>
		</li>  -->
		<li style="border-left: 4px #ffc001 solid;" >
			<div id="currentMenu_4" <% if (isMyMenu(request, "complain")) { %> class="current_cat4 current_onclick" <% }else{ %> class="cat4"<%} %> 
				onclick="toHref(this.id, '/complain/complainIndex.jhtml');"  > 
				<a href="#">投诉建议</a> 
				<span <% if (isMyMenu(request, "complain")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li style="border-left: 4px solid #f15f62;" >
			<div id="currentMenu_5" <% if (isMyMenu(request, "guide")) { %> class="current_cat5 current_onclick" <% }else{ %>class="cat5" <%} %> 
				onclick="toHref(this.id, '/pages/guide/newKaiTong.jsp');" > 
				<a href="#">常见问题</a> 
				<span <% if (isMyMenu(request, "guide")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li  style="border-left: 4px #c3a9aa solid;" >
			<div id="currentMenu_6" <% if (isMyMenu(request, "game")) { %> class="current_cat6 current_onclick" <% }else{ %>class="cat6"<%} %> 
				onclick="toOpen( '/game/playGame.jhtml');"  > 
				<a href="#">活动专区</a> 
				<span <% if (isMyMenu(request, "game")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>  
	</ul>
	</c:when>
	<c:when test="${menuDisplay eq 'bookAndJoin'}">
	<ul class="sidebar_con" id="bookAndJoin">
		<li style="border-left: 4px #92d14f solid;" >
			<div id="currentMenu_1" <% if (isMyMenu(request, "book") || isMyMenu(request, "bookHome")) { %> class="current_cat1 current_onclick" <% }else{ %> class="cat1"<%}%> 
				onclick="toHref(this.id, '/buyTicket/bookIndex.jhtml');" > 
				<a href="#">车票预订</a> 
				<span <% if (isMyMenu(request, "book") || isMyMenu(request, "bookHome")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li style="border-left: 4px #c3a9aa solid;" >
			<div id="currentMenu_2" <% if (isMyMenu(request, "join")) { %> class="current_cat2 current_onclick" <% }else{ %> class="cat2"<%} %> 
				onclick="toHref(this.id, '/joinUs/agentInfoIndex.jhtml');"  > 
				<a href="#">个人中心</a> 
				<span <% if (isMyMenu(request, "join")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li style="border-left: 4px #ffc001 solid;">
			<div id="currentMenu_3" <% if (isMyMenu(request, "complain")) { %> class="current_cat3 current_onclick" <% }else{ %>class="cat3"<%} %> 
				onclick="toHref(this.id, '/complain/complainIndex.jhtml');" > 
				<a href="#">投诉建议</a> 
				<span <% if (isMyMenu(request, "complain")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li style="border-left: 4px solid #f15f62;" >
			<div  id="currentMenu_4" <% if (isMyMenu(request, "guide")) { %> class="current_cat4 current_onclick" <% }else{ %>class="cat4"<%} %> 
				onclick="toHref(this.id, '/pages/guide/newKaiTong.jsp');"  > 
				<a href="#">常见问题</a> 
				<span <% if (isMyMenu(request, "guide")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
	</ul>
	</c:when>
	<c:when test="${menuDisplay eq 'bookOnly'}">
	<ul class="sidebar_con" id="bookOnly">
		<li style="border-left: 4px #92d14f solid;" >
			<div id="currentMenu_1"  <% if (isMyMenu(request, "book") || isMyMenu(request, "bookHome")) { %> class="current_cat1 current_onclick" <% }else{ %> class="cat1"<%}%> 
				onclick="toHref(this.id, '/buyTicket/bookIndex.jhtml');"  > 
				<a href="#">车票预订</a> 
				<span <% if (isMyMenu(request, "book") || isMyMenu(request, "bookHome")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li style="border-left: 4px #ffc001 solid;">
			<div id="currentMenu_2" <% if (isMyMenu(request, "complain")) { %> class="current_cat2 current_onclick" <% }else{ %>class="cat2"<%} %> 
				onclick="toHref(this.id, '/complain/complainIndex.jhtml');"  > 
				<a href="#">投诉建议</a> 
				<span <% if (isMyMenu(request, "complain")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li style="border-left: 4px solid #f15f62;" >
			<div id="currentMenu_3" <% if (isMyMenu(request, "guide")) { %> class="current_cat3 current_onclick" <% }else{ %>class="cat3"<%} %> 
				onclick="toHref(this.id, '/pages/guide/newKaiTong.jsp');"  > 
				<a href="#">常见问题</a> 
				<span <% if (isMyMenu(request, "guide")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
	</ul>
	</c:when>
	<c:when test="${menuDisplay eq 'shiji'}">
	<ul class="sidebar_con" id="shiji">
		<li style="border-left: 4px #a04ed1 solid;">
			<div id="currentMenu_1" <% if (isMyMenu(request, "extquery")) { %> class="current_cat1 current_onclick" <% }else{ %> class="cat1"<%} %> 
				onclick="toHref(this.id, '/extShiji/queryExtShijiOrderList.jhtml');"  > 
				<a href="#">订单查询</a> 
				<span <% if (isMyMenu(request, "extquery")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li style="border-left: 4px #fca0ab solid;" >
			<div id="currentMenu_2" <% if (isMyMenu(request, "extaccount")) { %> class="current_cat2 current_onclick" <% }else{ %> class="cat2"<%} %> 
				onclick="toHref(this.id, '/extShiji/queryExtAccountOrder.jhtml');"  > 
				<a href="#">对账管理</a> 
				<span <% if (isMyMenu(request, "extaccount")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
		<li style="border-left: 4px #c3a9aa solid;" >
			<div id="currentMenu_3"  <% if (isMyMenu(request, "extagent")) { %> class="current_cat3 current_onclick" <% }else{ %>class="cat3"<%} %> 
				onclick="toHref(this.id, '/extShiji/agentExtShijiInfoIndex.jhtml');" > 
				<a href="#">商户信息</a> 
				<span <% if (isMyMenu(request, "extagent")) { %> class="gcurrent" <% } %>></span>
			</div>
		</li>
	</ul>
	</c:when>
</c:choose>
		
		<!-- 
			<li style="border-left: 4px #92d14f solid;">
				<div class="current_cat1"> <a href="/buyTicket/bookIndex.jhtml">车票预订</a> <span class="gcurrent"></span> </div>
			</li>
			<li style="border-left: 4px #a04ed1 solid;"> 
				<div class="cat2"> <a href="/query/queryOrderList.jhtml">我的订单</a> <span></span> </div>
			</li>
			<li style="border-left: 4px #fca0ab solid;">
				<div class="cat3"> <a href="/query/querySaleReport.jhtml">销售报表</a> <span></span> </div>
			</li>
			<li style="border-left: 4px #c3a9aa solid;">
				<div class="cat4"> <a href="/joinUs/agentInfoIndex.jhtml">个人中心</a> <span></span> </div>
			</li>
			<li style="border-left: 4px #ffc001 solid;">
				<div class="cat5"> <a href="/complain/complainIndex.jhtml">投诉建议</a> <span></span> </div>
			</li>
			<li style="border-left: 4px solid #f15f62;">
				<div class="cat6"> <a href="/pages/guide/newKaiTong.jsp">常见问题</a> <span></span> </div>
			</li>
		 -->
	</div>
	<!--侧边栏 end-->

<% if (!isMyMenu(request, "extquery") && !isMyMenu(request, "extaccount") && !isMyMenu(request, "extagent")) { %>
	<!--公告 start-->
	<jsp:include flush="true" page="/notice/queryNoticeList.jhtml">
		<jsp:param name="" value=""/>
	</jsp:include>
	<!--公告 end-->

	<!--联系方式 start-->
	<div id="new_contact">
		<h3>
			<span>联系方式</span>
		</h3>
		<div class="new_contact">
			<span>客服热线</span>
			<b>7:00-23:00</b>
			<a href="#">400-688-2666</a>
		</div>
	</div>
	<!--联系方式 end-->
<%}else{ %>
<!-- 对外商户公告start -->
<jsp:include flush="true" page="/extShiji/queryNoticeList.jhtml">
	<jsp:param name="" value=""/>
</jsp:include>
<!-- 对外商户公告end -->

<!-- 对外商户业务常识start -->
<div id="new_ggao" class="exper mb10">
	<h2><span>业务常识</span></h2>
	<div class=nggao_con>
		<ul class="oz" style="border:0;">
		    <li><a href="/pages/guide/extTuiPiaoGuiDing.jsp">退票规定</a></li>
	        <li><a href="/pages/guide/extGaiQianGuiDing.jsp">改签规定</a></li>
	        <li><a href="/pages/guide/extTrainYesNo.jsp">火车票真伪识别</a></li>
		</ul>
	</div>
</div>
<!-- 对外商户业务常识end -->
<%} %>

<% if (isMyMenu(request, "bookHome")) { %>
	<!--中奖信息 start-->
	<jsp:include flush="true" page="/userIdsCardInfo/allAgentWinningInfo.jhtml">
		<jsp:param name="" value="" />
	</jsp:include>
	<!--中奖信息 end-->

	<!--电子保单验真start-->
	<div class="ban_bx_check oz mb10">
		<a href="http://www.unionlife.com.cn/tab147/" target=_blank;><img
				src="/images/bx_check.jpg" width="165" height="86" />
		</a>
	</div>
	<!--电子保单验真end-->

	<!--禁止乱收手续费start-->
	<div class="poundage">
	</div>
	<!--禁止乱收手续费end-->
<%} %>


</div>
