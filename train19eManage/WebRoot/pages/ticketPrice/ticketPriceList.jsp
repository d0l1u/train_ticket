<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>票价管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
	    function submitForm(){
			if( ($.trim($("#fazhan").val())=="")&&($.trim($("#daozhan").val())=="") ){
			//$("#fazhan").focus();
				alert("发站或到站不能都为空！");
				return;
			}
			$("#myform").submit();
		}
	    function fancheng(){
			var fazhan = document.getElementById("fazhan").value;
			var daozhan = document.getElementById("daozhan").value;
			//alert("fazhan:"+fazhan+"  daozhan:"+daozhan);
			document.getElementById("fazhan").value = daozhan;
			document.getElementById("daozhan").value = fazhan;
		}
		
		function deletePrice(xh,cc,fz,dz){
			var checi = document.getElementById("checi").value;
			var fazhan = document.getElementById("fazhan").value;
			var daozhan = document.getElementById("daozhan").value;
			
			if(confirm("确定删除吗？")){
			$("#myform").attr("action","/ticketPrice/toDeleteTicketPrice.do?xh="+xh+"&cc="+cc+"&fz="+fz+"&dz="+dz
			+"&checi="+checi+"&fazhan="+fazhan+"&daozhan="+daozhan
			);
			$("#myform").submit();
			}	
		}
	    //查看操作日志
		function opt_rizhi(){
			var url="/ticketPrice/queryTicketPriceLogList.do";
			showlayer('操作日志',url,'950px','600px')
		}
		//修改提交(处理get请求中文乱码)
		function cp_edit(xh,cc,fz,dz) {
			fz = encodeURI(encodeURI(fz));
			dz = encodeURI(encodeURI(dz));
			var href = "/ticketPrice/toUpdateTicketPrice.do?xh=" + xh + "&cc=" + cc + "&fz=" + fz + "&dz=" + dz;
			window.location.href = href;
			return false;
		}
	    </script>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/ticketPrice/queryTicketPricePageList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							车次
							<input type="text" class="text" name="cc" id="checi" value="${cc }" />
						</li>
						<li>
							发站
							<input type="text" class="text" name="fz" id="fazhan" value="${fz }" />
						</li>
						<li>
						 	到站
							<input type="text" class="text" name="dz" id="daozhan" value="${dz }" />
		       			</li>
		       			<li>
		       				<a href="javascript:fancheng();">返程</a>
		       			</li>
					</ul>
				</div>
				<br />
				<br />
				<p>
					<input type="button" value="查 询" class="btn" id="btnSubmit" onclick="submitForm();" />
					<input type="button" value="添 加" class="btn" id="addTicket" onclick="location.href = '/ticketPrice/toAddTicketPrice.do?cc=${cc}&fz=${fz}&dz=${dz}'" /> 
					<input type="button" value="操作日志" class="btn" id="rizhi" onclick="opt_rizhi()" />
				<!-- 		<input type="button" value="添 加" class="btn" id="addTicket" onclick="location.href = '/ticketPrice/toAddTicketPrice.do'" />     -->
				</p>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
					<!-- 		<th>
								票价ID
							</th>   -->
							<th>
								车次
							</th>
							<th>
								发站
							</th>
							<th>
								到站
							</th>
							<th>
								硬座
							</th>
							<th>
								软座
							</th>
							<th>
								硬卧上
							</th>
							<th>
								硬卧中
							</th>
							<th>
								硬卧下
							</th>
							<th>
								软卧上
							</th>
							<th>
								软卧下
							</th>
							<th>
								一等座
							</th>
							<th>
								二等座
							</th>
							<th>
								商务座
							</th>
							<th>
								特等座
							</th>
							<th>
								高级软卧上
							</th>
							<th>
								高级软卧下
							</th>
							<th>
								动卧上
							</th>
							<th>
								动卧下
							</th>
							<th>
								操作
							</th>
						</tr>
						
						<c:forEach var="list" items="${ticketPriceList}" varStatus="idx">
						<tr style="background:#E0F3ED; ">
							<td>
								${idx.index+1}
							</td>
					<!-- 		<td>
								${list.xh }
							</td>    -->
							<td>
								${list.cc}
							</td>	
							<td>
								${list.fz }
							</td>
							<td>
								${list.dz }
							</td>
							<td>
								${list.yz }
							</td>
							<td>
								${list.rz }
							</td>
							<td>
								${list.yws }
							</td>
							<td>
								${list.ywz }
							</td>
							<td>
								${list.ywx }
							</td>
							<td>
								${list.rws }
							</td>
							<td>
								${list.rwx }
							</td>
							<td>
								${list.rz1 }
							</td>
							<td>
								${list.rz2 }
							</td>
							<td>
								${list.swz }
							</td>
							<td>
								${list.tdz }
							</td>
							<td>
								${list.gws }
							</td>
							<td>
								${list.gwx }
							</td>
							<td>
								${list.dws}
							</td>
							<td>
								${list.dwx}
							</td>
							<td>
								<span>
									<a href="/ticketPrice/toUpdateTicketPrice.do?xh=${list.xh }&cc=${list.cc}&fz=${list.fz}&dz=${list.dz}" onclick="return cp_edit('${list.xh}','${list.cc}','${list.fz }','${list.dz }');">修改</a>
									<a href="javascript:void(0);" onclick="deletePrice('${list.xh }','${list.cc}','${list.fz}','${list.dz}');">删除</a>
								</span>
							</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
			</form>
		</div>
	</body>
</html>