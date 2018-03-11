<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>辛增竞价页</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.validate.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.metadata.js"></script>
<script type="text/javascript">	

	function submitForm(){
		$("#updateForm").submit();
	}

</script>
<style type="text/css">
table{width: 800px;line-height: 40px;}
table th,td{font-size:17px;font-family:"宋体";}
</style>
</head>

<body>
	<div style="margin-top:100px;width:1000px;background-color: #dadada;">
	<div style="margin-left: 50px;">
		<form action="/compete/updateCompete.do?compete_id=${compete_id }" method="post" name="updateForm" id="updateForm">
		<c:forEach var="compete" items="${compete}" varStatus="idx">
	        <table>
	        	<tr>
	        		<td><b>竞价规则明细</b></td>
	       		</tr>
	        	<tr><td>竞价日期:
	        		${compete.compete_date }
	        		</td>
	        		<td>竞价时段:
	        		${compete.compete_time }
	        		</td>
	        	</tr>
	        	<tr>
	        		<td>竞价渠道:
	        			${competeChannel[compete.compete_channel]}
	        		</td>
	        	</tr>
	        	<tr>
	        		<td><font color="#2C99FF">19旅行</font></td>
	        	</tr>
	        	<tr>
	        		<td>CDG竞价:
	        		${compete.compete_money_1 }
	        		</td>
	        		<td>非CDG竞价:
	        		${compete.compete_money_un_1 }
	        		</td>
	        	</tr>
	           	<tr>
	        		<td>CDG排名:
	        		${compete.compete_ranking_1 }
	        		</td>
	        		<td>非CDG排名:
	        		${compete.compete_ranking_un_1 }
	        		</td>
	        	</tr>
	        	<tr>
	        		<td><font color="#2C99FF">九九商旅</font></td>
	        	</tr>
	        	<tr>
	        		<td>CDG竞价:
	        		${compete.compete_money_2 }
	        		</td>
	        		<td>非CDG竞价:
	        		${compete.compete_money_un_2 }
	        		</td>
	        	</tr>
	           	<tr>
	        		<td>CDG排名:
	        		${compete.compete_ranking_2 }
	        		</td>
	        		<td>非CDG排名:
	        		${compete.compete_ranking_un_2 }
	        		</td>
	        	</tr>
	        	<tr>
	        		<td colspan="3">CDG前五名:
	        		${compete.compete_top }
	        		</td>
	        	</tr>
	        	<tr>
	        		<td colspan="3">非CDG前五名:
	        		${compete.compete_top_un }
	        		</td>
	        	</tr>
       		</table>
       		</c:forEach>
         </form>
     </div>
        <div class="book_manage" style="margin-left: 30%;">
        <p>  <!--<input type="button" value="修改" class="btn" id="btnSubmit" onclick="submitForm()"/>-->
           <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/></p>
        </div>
   
	</div>
</body>
</html>
