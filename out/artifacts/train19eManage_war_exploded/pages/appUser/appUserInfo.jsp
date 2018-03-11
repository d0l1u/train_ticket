<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>自提订单明细页</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/layer/layer.js"></script>
<script language="javascript" src="/js/mylayer.js"></script>
<script type="text/javascript">
</script>

</head>

<body>
	<div class="content1 oz">
    	<!--左边内容 start-->
    	<div class="left_con oz">
         	<div class="pub_order_mes oz mb10_all">
            	<h4>用户信息</h4>
                <div class="pub_con">
                	<table class="pub_table" style="margin:20px 20px;">
                    	<tr>
                        	<td width="234">姓名：<span>${userInfo.user_name}</span></td>
                            <td>账号：<span>${userInfo.user_phone }</span>
                            </td>
                        </tr>
                        <tr>
                         	<td>邮箱：<span>${userInfo.user_email }&nbsp;</span></td>
                        	<td>状态：<span>${weatherAble[userInfo.weather_able ] }</span></td>
                        </tr>
                        <tr>
                        	<td>积分：<span>${userInfo.score_num}</span></td>
                        	<td>推荐人个数：<span>${referee_account_num}</span></td>
                        </tr>
                        <tr>
                        	<td>手机型号：<span>${userInfo.phone_pattern}</span></td>
                        	<td>登陆次数：<span>${userInfo.login_num}</span></td>
                        </tr>
                        <tr>
                        	<td>创建时间：<span>${userInfo.create_time}</span></td>
                        	<td>最后登录时间：<span>${userInfo.last_login_time}</span></td>
                        </tr>
                        <tr>
							<td>12306账号：<span>${userInfo.name12306 }</span></td>   
							<td>12306密码：<span>${userInfo.pwd12306 }</span></td>    
                        </tr>
                    </table>
                </div>
            </div>
            <div class="pub_order_mes  oz mb10_all">
            	<h4>常用联系人</h4>
                <div class="pub_con">
            	<form name="queryFrm" action="/appUser/queryAppUserInfo.do?user_id=${userInfo.user_id }" method="post">
                <div class="book_manage" style="width:660px;height:100%;">
                <c:if test="${totalCount > 0}">
                	<table style="width:100%;height:100%;text-align:center;font-size:12px;border:#dadada 1px solid;">
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								姓名
							</th>
							<th>
								身份证号码
							</th>
							<th>
								乘客类型
							</th>
							<th>
								核验状态
							</th>
							<th>
								创建时间
							</th>
						</tr>
						<c:forEach var="list" items="${linkerList}" varStatus="idx">
						<tr style="background: #BEE0FC;">
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.link_name}
							</td>
							<td>
								${list.ids_card}
							</td>
							<td>
								${ticketType[list.passenger_type]}
							</td>
							<td>
								${verifyStatus[list.verify_status] }
							</td>
							<td>
								${list.create_time}
							</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					</c:if>
					<c:if test="${totalCount == 0}">
                	<table style="width:100%;height:100%;text-align:center;font-size:12px;border:#dadada 1px solid;">
						<tr style="background: #EAEAEA;">
							<td>没有常用联系人</td>
						</tr>
					</table>
					</c:if>
					</div>
                </form>
                </div>
            </div>
            <div class="pub_debook_mes  oz mb10_all">	
               <p>
	               <input type="button" value="返 回" class="btn" onclick="backPage();"/>
			   </p>
        	</div>

        <!--左边内容 end-->
    </div>
</body>
</html>
