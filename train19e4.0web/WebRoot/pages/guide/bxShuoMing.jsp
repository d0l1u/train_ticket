<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String color="";
if(request.getParameter("color")==null){
	color="0";
}else{
	color=request.getParameter("color");
}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>火车票业务常见问题</title>
<link rel="stylesheet" href="/css/style.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
</head>

<body>
<div class="content oz">
<div class="index_all">
				
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
				<jsp:param name="menuId" value="guide" />
			</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start--> 
    	<div class="infoinput-right oz">
  			<div class="help_type oz">
            	<ul class="help_type_ul">
	                <li class="current1"><a href="/pages/guide/newKaiTong.jsp">新手指南</a></li>
	                <li class="current1"><a href="/pages/guide/questionGouPiao.jsp">常见问题</a></li>
	                <li class="current current1"><a href="/pages/guide/bxShuoMing.jsp">相关规章</a></li>
	                <li class="current1" style=" width:196px;"><a href="/pages/guide/tuiPiaoGuiDing.jsp">业务常识</a></li>
	           </ul>
  		</div>  
  		  <!--内容 start-->
  <div class="bread_nav mb10"> 
  	<ul class="bread_nav_ul">
        <li ><a style="color:#32B1F0;" href="bxShuoMing.jsp">保险说明</a></li>
        <li ><a href="daiGou.jsp">火车票线下代购协议</a></li>
        <li ><a href="zhuCe.jsp">委托注册多个代购账户协议</a></li>
         <li ><a href="chengnuoPage.jsp"> 平台购票承诺书</a></li>
     </ul>
  </div>   
  <!--内容 start-->
<div class="yew_r_con oz">
    <h2>火车票业务相关章程</h2>
    <div class="con_con">
     <h5>“合众人寿网销火车意外险传世1号”保险说明</h5>
      <ul>
         <li>
          <h3>&nbsp;&nbsp;&nbsp;基本信息：</h3>
        </li>
        <li>
        <p> 保险名称：合众人寿网销火车意外险传世1号<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;保险金额：火车意外伤害65万元<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;保险费：20元<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;保险生效时间： 单次火车（仅限当日当次），可当天投保当天生效（投保时间必须早于保险责任生效时间2个小时），或投保时所指定的生效时间，可提前30天投保。<br/> 
			&nbsp;&nbsp;&nbsp;&nbsp;限购份数：1份/人 <br/>
			&nbsp;&nbsp;&nbsp;&nbsp;保障年龄：18-65周岁 <br/>
			&nbsp;&nbsp;&nbsp;&nbsp;投保人与被保险人关系：仅限本人<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;证件类型：身份证、护照、军人证<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;受益人：法定<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;退保规则: 保险责任正式开始之前可全额退保<br/>
        </p>
        </li>
        
        <li>
          <h3>&nbsp;&nbsp;&nbsp;其他保险说明：</h3>
        </li>
        
        <li>
        <p>1.保险期间：为被保险人在本保险合同载明的保险责任生效当日当次以乘客身份乘坐合法商业运营的客运火车，并遵守承运人关于安全乘坐的规定，自持有效车票检票进站时起至被保险人抵达车票载明的终点检票出站时止。 </p>
        <p>2.保险责任：<br />
           &nbsp;&nbsp;&nbsp;&nbsp;（1）在火车意外伤害保险责任有效期间内，被保险人因火车事故遭受意外伤害，并自事故发生之日起180天内以该事故为直接且单独的原因导致被保险人发生本主合同所附“人身保险伤残评定标准（行业标准）”所列伤残程度之一者，我们将按该伤残等级所对应的给付比例乘以电子保险单上载明的该类交通工具的保险金额给付交通工具意外伤残保险金。如治疗仍未结束的，按事故发生之日起第180天的身体情况进行伤残鉴定，并据此给付交通工具意外伤残保险金。被保险人因同一交通事故造成两处或两处以上伤残的，应对各处伤残程度分别进行评定，如几处伤残程度等级不同，我们按最重的伤残程度等级所对应的给付比例给付该类交通工具意外伤残保险金；如两处或两处以上伤残程度等级相同且为最重的伤残程度等级，伤残程度等级在原评定基础上晋升一级，但最高晋升至第一级。在交通工具意外伤害保险责任有效期间内，如被保险人因多次同类交通工具交通事故造成伤残，后次交通事故导致的伤残包含以前交通事故导致的伤残，且后次交通事故导致的伤残对应更严重伤残程度等级的，本公司按后次伤残程度等级相对应的给付比例给付该类交通工具意外伤残保险金，但以前伤残已给付的该类交通工具意外伤残保险金（除另有约定外，投保前已患或因责任免除事项所致伤残视为已给付交通工具意外伤残保险金）应予以扣除。每次评定时，对被保险人同一部位和性质的伤残，不应采用《人身保险伤残评定标准（行业标准）》条文两条及以上或者同一条文两次及以上进行评定。
           <br />
            &nbsp;&nbsp;&nbsp;&nbsp;（2） 在火车意外伤害保险责任有效期间内，被保险人因火车事故遭受意外伤害，并自事故发生之日起180天内以该事故为直接且单独的原因身故的，我们按电子保险单上载明的该类交通工具的保险金额给付交通工具意外身故保险金，但已给付的该类交通工具意外伤残保险金将予以扣除，本主合同终止。在本主合同保险期间内，不论被保险人一次或多次遭受某类交通工具意外伤害事故，我们按以上规定给付交通工具意外伤残保险金和交通工具意外身故保险金，但对于某类交通工具累计给付的各项保险金数额之和以电子保险单上载明的该类交通工具对应的保险金额为限。
       </p>
       <p>
      	   3.保险责任的免除：因下列情形之一导致被保险人身故或残疾的，我们不承担给付保险金的责任：（1）投保人对被保险人的故意杀害、故意伤害；（2）被保险人故意犯罪或者抗拒依法采取的刑事强制措施；（3）被保险人主动吸食或注射毒品；（4）被保险人酒后驾驶，无合法有效驾驶证驾驶或驾驶无有效行驶证的机动车；（5）战争、军事冲突、暴乱或武装叛乱；（6）核爆炸、核辐射或核污染；（7）被保险人从事探险活动、特技表演、赛车等高风险运动；（8）被保险人因医疗事故、药物过敏或精神疾患（依照世界卫生组织《疾病和有关健康问题的国际统计分类》（ICD-10）确定）导致的伤害；（9）被保险人未遵医嘱，私自使用药物，但按使用说明的规定使用非处方药不在此限；（10）被保险人醉酒、自杀（但被保险人自杀时为无民事行为能力人的除外）或故意自伤；（11）被保险人非因意外事故被人民法院宣告死亡；（12）被保险人违反有关管理部门安全驾驶或承运部门安全乘坐相关规定；（13）被保险人乘坐从事非法营运的交通工具；（14）被保险人非法搭乘商业运营的民航班机、轨道交通、轮船、或者客运汽车的。发生上述第（1）条情形导致被保险人身故的，本主合同终止。发生上述其他情形导致被保险人身故的，本主合同终止，我们向您退还保险单的现金价值。发生上述情形之一导致被保险人伤残的，我们不承担给付保险金的责任，本主合同在约定的保险期间内继续有效。
       </p>
       <p>
       	   <font>4.保险责任特别约定：(1)被保险人改乘等效车次，保险合同继续有效，保险期间自被保险人乘等效车次进站时始，至被保险人抵达目的站走出所乘等效车次出站时止。(2)本保险责任生效后办理退保保单现金价值为零，且本保险责任生效后不得做任何形式的保单变更。(3)保险事故须是被保险人所乘火车发生的交通事故所导致，否则不承担保险金给付责任；保险事故须是被保险人所乘等效车次火车发生的交通事故所导致，且可以出具显示该等效车票与原车票具有关联性的有效证明材料，否则不承担保险金给付责任。</font>
       </p>
       <p>
      	   5.如发生合同约定的保险事故，需在十日内通知本公司，报案电话：95515。
       </p>
       <p>
       	   6.您可根据保险单号及投保人姓名登陆以下网址<a onclick="window.open('http://www.unionlife.com.cn/tab147/')" href="#" >http://www.unionlife.com.cn/tab147/</a>进行保单验真，您也可根据保险单号登陆以下网址<a onclick="window.open('http://www.unionlife.com.cn/tab144/')" href="#">http://www.unionlife.com.cn/tab144/</a>查询保单相关信息。您可凭保单号登陆以下网址<a onclick="window.open('http://www.unionlife.com.cn/tab570/')" href="#" >http://www.unionlife.com.cn/tab570/</a>下载相关保险电子凭证。
       </p>
       <p>7.本保险未尽事宜，以<a onclick="window.open('http://www.unionlife.com.cn/Portals/0/hzsjasjt2013.pdf')" href="#">《合众世纪安顺交通工具意外伤害保险（2013修订）条款》</a>为准。
        </p>
       <p>8.您（投保人）同意在中国法律允许或要求的范围内，授权承保公司将个人信息及保单信息提供给北京意外险信息平台和北京健康保险信息平台以做合理利用。如果您投保时填写了手机号码，我们将为您提供免费的投保短信提示。（仅限北京地区投保客户）
       </p>
       <p>9.您在购买了一年期及以下主险为意外险的产品或产品组合保单后可到北京人身意外伤害保险信息平台查询相关保单信息（查询网址：<a onclick="window.open('http://www.biabii.org.cn')" href="#">http://www.biabii.org.cn</a>，仅限北京地区投保客户）。</p>
        </li>
      </ul>
      
    </div>
   </div>
   <!--内容 end--> 
 </div>
 </div>
</div>
</body>
</html>
