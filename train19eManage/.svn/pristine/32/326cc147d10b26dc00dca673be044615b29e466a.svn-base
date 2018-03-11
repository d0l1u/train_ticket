<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>催退票管理页面</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />

<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/ajaxfileupload.js"></script>
<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript">
	
<%PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();%>
			
 
   
			
	//全选notify
	function selectAllrefund_status() {
		var checklist = document.getElementsByName("urge_status");
		if (document.getElementById("all_refund_status").checked) {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 1;
			}
		} else {
			for (var j = 0; j < checklist.length; j++) {
				checklist[j].checked = 0;
			}
		}
	}

	//全选操作
	function checkChnRetRuleAll() {
		var refund_urge_id = document.getElementsByName("refund_urge_id");
		var tag1 = document.getElementById("tag1").value;
		if (tag1 == 0) {
			for (var i = 0; i < refund_urge_id.length; i++) {
				refund_urge_id[i].checked = true;
			}
			document.getElementById("tag1").value = 1;
		} else if (tag1 == 1) {
			for (var i = 0; i < refund_urge_id.length; i++) {
				refund_urge_id[i].checked = false;
			}
			document.getElementById("tag1").value = 0;
		}
	}
	//批量修改 状态为其它
	function updateRefundUrge(urge_status) {

		//urge_status: 11处理中 22退款成功 33 退款失败 44其他

		var str = "暂未核实到退款的车票";
		var remark = "暂未核实到退款";
		var urgeRefundIdStr = "";
		var urgeRefundIdNum = 0;
		$("input[name='refund_urge_id']:checkbox:checked").each(function() { //遍历被选中CheckBox元素的集合 得到Value值
			var refund_urge_id = $(this).val();
			urgeRefundIdStr += refund_urge_id + ",";
			urgeRefundIdNum++;
		});
		if (urgeRefundIdNum == 0) {
			alert("请选择需要" + str + "!");
			return false;
		}

		urgeRefundIdStr = urgeRefundIdStr.substring(0,
				urgeRefundIdStr.length - 1);
		var url = "/tuniuUrgeRefund/updateUrgeRefundList.do?urgeRefundIdStr="
				+ urgeRefundIdStr + "&urge_status=" + urge_status + "&remark="
				+ encodeURIComponent(remark) + "&version=" + new Date();

		if (confirm("确认批量" + str + "吗 ？")) {
			$.get(url, function(data) {
				alert(data);
				$("form:first").submit();
			});
		}
	}

	function exportExcelRefundUrge() {

		$("form:first").attr("action",
				"/tuniuUrgeRefund/excelExportForUrgeRefund.do");
		$("form:first").submit();
		$("form:first").attr("action",
				"/tuniuUrgeRefund/queryUrgeRefundList.do");
	}

	function importExcel() {
		var file = $.trim($("#excelFile").val());
		var fileType = (file.substring(file.lastIndexOf(".") + 1, file.length))
				.toLowerCase();
		if (file == "") {
			alert("请选择'.xls'或'.xlsx'格式的文件！");
			$("#excelFile").focus();
			return;
		} else if (fileType != "xls" && fileType != "xlsx") {
			alert("请选择'.xls'或'.xlsx'格式的文件！");
			$("#excelFile").focus();
			return;
		}
		var flag = false;
		if (confirm("确定上传？")) {
			$.ajaxFileUpload({
				url : '/tuniuUrgeRefund/ajaxUpload.do?file=' + file,
				secureuri : false,
				fileElementId : 'excelFile',//file标签的id
				dataType : 'json',//返回数据的类型  
				//data:{add_type:"${add_type }",ticket_type:"${ticket_type }"},//一同上传的数据
				success : function(data, status) {
					alert(data.name + ",提示：" + data.msg + ",成功个数："
							+ data.succ_num + ",失败个数：" + data.fail_num);
					flag = true;
				},
				error : function(data, status, e) {
					alert(data.name + ",提示：" + data.msg + ",成功个数："
							+ data.succ_num + ",失败个数：" + data.fail_num);
					flag = true;
				}
			});
		   
			if (flag) {
				$("form:first").submit();
			}

		} else {
			return;
		}

	}


</script>
</head>
<body>
	<div></div>
	<div class="book_manage oz" id="mainDiv">
		<form action="/tuniuUrgeRefund/queryUrgeRefundList.do" method="post"
			name="myform" id="myform">
			<div style="border: 0px solid #00CC00; margin: 10px;">

				<ul class="order_num oz" style="margin-top: 30px;">
					<li>订单号：&nbsp;&nbsp;&nbsp; <input type="text" class="text"
						name="order_id" value="${order_id }" />
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>开始时间：&nbsp; <input type="text" class="text"
						name="begin_create_time" readonly="readonly"
						value="${begin_create_time }"
						onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>结束时间：&nbsp;&nbsp; <input type="text" class="text"
						name="end_create_time" readonly="readonly"
						value="${end_create_time}"
						onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
				</ul>
				<ul class="order_state oz" style="margin-top: 10px;">
					<li>&nbsp;&nbsp;催退款状态：</li>
					<li>
					<input type="checkbox" onclick="selectAllrefund_status();"
						id="all_refund_status" /> <label for="">全部</label> <c:forEach
							items="${urge_status_list }" var="s" varStatus="index">
							<input type="checkbox" id="urge_status_list${index.count }"
								name="urge_status" value="${s.key }"
								<c:if test="${fn:contains(urge_statusStr, s.key ) }">checked="checked"</c:if> />
							<label for="urge_status_list${index.count }"> ${s.value }&nbsp;&nbsp;&nbsp;&nbsp;</label>
						</c:forEach>
						</li>
				</ul>

			</div>
			<p>
				<input type="submit" value="查 询" class="btn" /> <input
					type="button" value="批量未核退款" onclick="updateRefundUrge('44');"
					class="btn" style="width: 106px;height: 35px;" /> <input
					type="button" value="导出Excel" onclick="exportExcelRefundUrge();"
					class="btn" style="width: 106px;height: 35px;" /> <input
					type="button" value="批量导入" class="btn" id="import"
					onclick="importExcel();" /> 选择文件：<input type="file"
					name="excelFile" id="excelFile" /><br />
				<br />
				<br />
			</p>
		
			<table>
				<tr style="background: #f0f0f0;">
					<th style="width:30px;">全选 <br /> <input type="checkbox"
						id="checkChnRetRulAll" name="checkChnRetRulAll"
						onclick="checkChnRetRuleAll()" /></th>
					<th>序号</th>
					<th>订单号</th>
					<th>车票ID</th>
					<th>催退款状态</th>
					<th>退款金额</th>
					<th>备注</th>
					<th>创建时间</th>
					<th>操作时间</th>
					<th>操作人</th>
					<th>操作</th>
				</tr>
				<c:forEach var="list" items="${urgeRefundList}" varStatus="idx">
					<tr>
						<td><input type="checkbox" id="refund_urge_id"
							name="refund_urge_id" value="${list.order_id }|${list.cp_id}" /></td>
						<td>${idx.index+1}</td>
						<td>${list.order_id }</td>
						<td>${list.cp_id}</td>
						<td>${urge_status_list[list.urge_status] }</td>
						<td>${list.refund_money }</td>
						<td>${list.remark}</td>
						<td>${fn:substringBefore(list.create_time, ' ')}<br />
							${fn:substringAfter(list.create_time, ' ')}
						</td>
						<td>${fn:substringBefore(list.opt_time, ' ')}<br />
							${fn:substringAfter(list.opt_time, ' ')}
						</td>
						<td>${list.opt_person }</td>
						<td><span> <a
								href="/tuniuUrgeRefund/queryUrgeRefundInfo.do?order_id=${list.order_id }&cp_id=${list.cp_id }">明细</a>
						</span></td>
					</tr>
				</c:forEach>
			</table>
			<jsp:include page="/pages/common/paging.jsp" />
			<input type="hidden" id="tag1" name="tag1" value="0" />

		</form>
	</div>
	
</body>
</html>