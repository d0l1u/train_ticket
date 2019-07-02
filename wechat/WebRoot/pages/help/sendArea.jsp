<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>配送范围页面</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" href="/css/style.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
</head>

<body>
	<div class="content oz">
    	<!--导航条 start-->
    	<div class="main_nav">
        	<ul class="oz">
        	    <jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="help" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--内容 start-->
        <div class="bread_nav mb10">
        	您所在的位置：<a href="#">业务介绍</a> > <span>购票常识</span>
        	
        </div>
        <div class="yew_l_con oz">
        	<div class="left_nav">
            	<h2>业务介绍</h2>
            	<ul>
                    <li><a href="protocol.jsp">代购服务协议</a></li>
             		<li><a href="trueFalse.jsp">车票真伪识别</a></li>
                    <li><a href="refund.jsp">退票规定</a></li>
                    <li class="current"><a href="sendArea.jsp">配送范围</a></li>
                    <li><a href="bookPeriod.jsp">车票预定期</a></li>
                    <!--  <li><a href="bxProtocol.jsp">购票保险协议</a></li>-->
                </ul>
            </div>
        </div>
        <div class="yew_r_con oz"><!-- InstanceBeginEditable name="EditRegion3" -->
          <h2>配送范围</h2>
          <div class="con_con">
            <ul>
              <li>
                <h3 class="h3_c">同城件配送范围</h3>
              </li>
              <li>
              	<table>
                	<tr>
                		<th>序号</th>
                		<th>配送类别</th>
                		<th>配送标准</th>
                		<th>配送范围</th>
                		<th>票到时间</th>
                	</tr>
                    <tr>
                		<td>1</td>
                		<td>同城件</td>
                		<td>20元/件</td>
                		<td>同城</td>
                		<td class="cp_time"><strong>出票后1-2日</strong>送到</td>
                	</tr>
                </table>
              </li>
              <li><span>同城配送：凡是始发地与火车票送达目的地为同一城市，则认定为同城配送。</span></li>
              <li><h3>配送区域说明 ：</h3></li>
              <li>
1)党政机关，部队，军事基地不派送；<br />2)风景旅游区，度假地不派送。<br />3)以上所有地区的偏远县区，镇以及村组不派;<br />4)根据车票的不同，请您预先记住出发时间，由于乘车前的行程我们无法估计，请您协调好时间，提前做好出行安排，不延误您的行程。</li>
              <li>
              <li class="line"></li>
                <h3 class="h3_c">异地件配送范围</h3>
              </li>
              <li>
              	<table>
                	<tr>
                		<th>序号</th>
                		<th>配送类别</th>
                		<th>配送标准</th>
                		<th>配送范围</th>
                		<th>票到时间</th>
                	</tr>
                    <tr>
                		<td>1</td>
                		<td>同城件</td>
                		<td>20元/件</td>
                		<td>同城</td>
                		<td class="cp_time"><strong>出票后1-2日</strong>送到</td>
                	</tr>
                </table>
                </li>
              <li><span>异地配送：凡是始发地与火车票送达地点不在同一城市的，则认定为异地配送。</span></li>
              <li>
              	<h3>配送区域说明 ：</h3>
              </li>
              <li>
                1)党政机关，部队，军事基地不派送；<br /> 
                2)风景旅游区，度假地不派送；<br />
                3)以上所有地区的偏远县区，镇以及村组不派送；<br />
                4)根据车票的不同，请您预先记住出发时间，由于乘车前的行程我们无法估计，请您协调好时间，提前做好出行安排，不延误您的行程。
			</li>
            </ul>
          </div>
        <!-- InstanceEndEditable --></div>
        <!--内容 end-->
    </div>
	<!--业务所有标注 start-->
    <div class="business-provider">
    	<p>业务提供方：19旅行</p>
    </div>
    <!--业务所有标注 end-->
</body>
<!-- InstanceEnd --></html>
