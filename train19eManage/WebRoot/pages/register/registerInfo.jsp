<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>注册信息明细页</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script type="text/javascript">

		function fail(regist_id,obj){
			if($(obj).attr("id")=="register_fail"&& $("#fail_reason").val()== 000){
				alert("请选择失败原因！");
				$("#fail_reason").focus();
				return;
			}
			var fail_reason = $("#fail_reason").val();
			//onclick="location.href = '/register/updateRegisterFail.do?regist_id=${registerInfo.regist_id}'"
			$("form:first").attr("action","/register/updateRegisterFail.do?regist_id="+regist_id+"&fail_reason="+fail_reason);
			$("form:first").submit();
		}

		function save12306Register(regist_id,account_name1,account_pwd1){
			//location.href = '/register/save12306Register.do?regist_id=${registerInfo.regist_id}'
			var account_name = $("#account_name").val();
			var account_pwd = $("#account_pwd").val();
			var url = "/register/save12306Register.do?version="+new Date();
			$.post(url,
					{regist_id:regist_id,
					 account_name:account_name,
					 account_pwd:account_pwd,
					 account_name1:account_name1,
					 account_pwd1:account_pwd1,
					 },
			function(data){
				if(data == "success"){
					alert("更新成功！");
				}else{
					alert("更新失败，请您重试！");
				}
			});
		}
		</script>
	</head>

	<body>
		<div class="content1 oz">
    		<div class="left_con oz">
    			<form action="/acquire/updateRegisterInfo.do" method="post" id="updateForm">
        	 	<div class="pub_order_mes oz mb10_all">
            		<h4>基本信息</h4>
                	<div class="pub_con">
                		<table class="pub_table">
                    		<tr>
                     		   	<td class="pub_yuliu" rowspan="6"></td>
                     		   	<td width="234">姓名：<span>${registerInfo.user_name}</span></td>
                         	    <td>当前状态：<span>${regist_status[registerInfo.regist_status] }</span></td>
                        	</tr>
                        	<tr>
                        		<td>身份证号：<span>${registerInfo.ids_card}</span></td>
                        		<td>电话号码：<span>${registerInfo.user_phone}</span></td>
                        	</tr>
                        	<tr>
                        		<td>代理商账号：<span>${registerInfo.user_id}</span></td>
                        		<td>创建时间：<span>${registerInfo.create_time}</span></td>
                        	</tr>
                        	<tr>
                        		<td>12306账号：<span>
                        		<c:choose>
                        			<c:when test="${isShow eq '0'}">
                        				<input type="text" name="account_name" id="account_name" value="${registerInfo.account_name}" />
                        			</c:when>
                        			<c:otherwise>
                        				${registerInfo.account_name}
                        			</c:otherwise>
                        		</c:choose>
                        		</span></td>
                        		<td>12306密码：<span>
                        		<c:choose>
                        			<c:when test="${isShow eq '0'}">
                        				<input type="text" name="account_pwd" id="account_pwd" value="${registerInfo.account_pwd}" />
                        			</c:when>
                        			<c:otherwise>
                        				${registerInfo.account_pwd}
                        			</c:otherwise>
                        		</c:choose>
                        		</span></td>
                        	</tr>
                        	<tr>
                        		<td>12306注册邮箱：<span>${registerInfo.mail}</span></td>
                        		<td>
                        		<c:if test="${isShow eq '0'}">
                        			<input type="button" value="更新" id="register_success" onclick="save12306Register('${registerInfo.regist_id}','${registerInfo.account_name}','${registerInfo.account_pwd}')"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#17C9B9;cursor:pointer;width:70px;height:30px;"/>
								</c:if>
                        		</td>
                        	</tr>
                    	</table>
                	</div>
            	</div>
            	
           		<div class="pub_order_mes  oz mb10_all">
            		<h4>失败原因</h4>
               		<div class="pub_con">
                		<table class="pub_table">
                    		<tr>
                        		<td class="pub_yuliu" rowspan="6"></td>
                        	</tr>
                        	<tr>
                        		<td>
									失败原因：
									<span><c:if test="${registerInfo.fail_reason eq '1'}">实名制身份信息有误</c:if>
										<c:if test="${registerInfo.fail_reason eq '2'}">身份信息已使用</c:if>
										<c:if test="${registerInfo.fail_reason eq '6'}">用户取回</c:if> </span>
								</td>
							</tr>
                        	<tr>
                        		<td>人工注册说明：<span>${registerInfo.description}</span></td>
                        	</tr>
                    	</table>
                	</div>
            	</div>
            	<c:if test="${isShow eq '0'}">
            	<div class="pub_order_mes oz mb10_all">
            		<h4>人工处理</h4>
                	<div class="pub_con">
                		<table class="pub_table">
                			<tr>
                        		<td class="pub_yuliu" rowspan="6"></td>
                        	</tr>
                			<tr>
                				<td>
									<span style="float:left;line-height:20px;padding-right:12px;">失败原因:</span>
									<select name="fail_reason" id="fail_reason" style="float:left;">
										<option value="000">请选择</option>
										<c:choose>
											<c:when test="${registerInfo.regist_status eq '44'}">
												<option value="1">实名制身份信息有误</option>
												<option value="2">身份信息已使用</option>
											</c:when>
											<c:when test="${registerInfo.regist_status eq '55'}">
												<option value="1">实名制身份信息有误</option>
												<option value="2">身份信息已使用</option>
											</c:when>
										</c:choose>
									</select>
								</td>
                			</tr>
                		
                		</table>
                	</div>
                </div>
                </c:if>
            	<div class="pub_debook_mes  oz mb10_all">	
			   		<c:if test="${isShow eq '0'}">
			   			<p>
							<input type="button" value="注册成功" id="register_success" onclick="location.href = '/register/updateRegisterSuccess.do?regist_id=${registerInfo.regist_id}'"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:green;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="注册失败" id="register_fail" onclick="fail('${registerInfo.regist_id}',this);"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#BA55D3;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="需要核验" onclick="location.href = '/register/updateRegisterCheck.do?regist_id=${registerInfo.regist_id}&regist_status=55'" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#87CEFA;cursor:pointer;width:100px;height:30px;"/>	
							<input type="button" value="邮箱激活" onclick="location.href = '/register/updateRegisterCheck.do?regist_id=${registerInfo.regist_id}&regist_status=88'" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#F57026;cursor:pointer;width:100px;height:30px;"/>	
						</p>
			   		</c:if>
			   		<p>
	              		<input type="button" value="返 回" class="btn btn_normal" onclick="javascript:history.back(-1);"/>
			   		</p>
        		</div>
        		</form>
       		</div>
        </div>
	</body>
</html>
