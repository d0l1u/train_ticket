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
	                <li class="current1"><a href="/pages/guide/bxShuoMing.jsp">相关规章</a></li>
	                <li class="current current1" style=" width:196px;"><a href="/pages/guide/tuiPiaoGuiDing.jsp">业务常识</a></li>
	           </ul>
  		</div>  
  		  <!--内容 start-->
  <div class="bread_nav mb10"> 
  	<ul class="bread_nav_ul">
        <li ><a href="tuiPiaoGuiDing.jsp">退票规定</a></li>
        <li ><a href="gaiQianGuiDing.jsp">改签规定</a></li>
        <li ><a style="color:#32B1F0;" href="trainYesNo.jsp">火车票真伪识别</a></li>
     </ul>
  </div>   
  <!--内容 start-->
  <div class="yew_r_con oz">
    <h2>火车票业务业务常识</h2>
    <div class="con_con" >
       <h5>火车票真伪识别</h5>
             <ul >
              <li style="font-size:14px;">现在，用户买到的大都是新版火车票，那么新版车票都有哪些变化，如何辨别火车票的真伪呢？</li>
              <li style="font-size:14px;">第一，新版车票下方的一维码已经变成一个二维的防伪图案了；<br />
                第二，原来的“全价”字样改为符号、￥(读元)的字样，也就是英文字母Y加一横；<br />
                第三，原来的“限乘当日当次车、三日内有效”由两行变为一行。另外，拿到票后您还可以先逆光照照，看看车票上有没有铁路的路徽水印。实用小贴士(新旧两版火车票)在新版火车票的二维的防伪图案、￥、3日内有效处加红圈。</li>
              <li class="li_img" style="font-size:14px;"><img src="/images/yp_show.jpg" width="479" height="254" alt="样票图解" /></li>
              <li>
                <h3 style="font-size:14px;">新版火车票七大特点：</h3>
              </li>
              <li style="font-size:14px;">1)新版车票车次放在始发站和终点站之间；<br />
                2)新版车票将旧版车票的“全价”字样改为“￥”；<br />
                3)新版车票站名下方加印有拼音；<br />
                4)新版票种增加了“红色旅游”，在票面上显示“红”字；<br />
                5)优质优价空调车执行折扣价时，在票面上显示“折”字；<br />
                6)团体票优惠时，票面会打印“团优”字样；<br />
                7)新版车票背面印有《铁路旅客乘车须知》内容。</li>
              <li>
                <h3 style="font-size:14px;">图解说明</h3>
              </li>
              <li style="font-size:14px;">1）最左上角的红色字体为票号，表明对于某个售票窗口或终端连续售票的编号，由字母和6位数字组成。有时字母前方还有数字，一般表示售票窗口编号。这个是一票一号，出现票号相同的几率几乎为零。</li>
              <li style="font-size:14px;">2）始发站和终到站：行程400公里以内为魏碑体，400公里以上为黑体。</li>
              <li style="font-size:14px;">3）票价后面的车型部分。新票省略了很多，比如旧版票的“硬座普快”，新版直接为“硬座”。</li>
              <li style="font-size:14px;">4）新版火车票最后一行的数字为21位数字，取消了以前车票的随机码和里程数。</li>
              <li>
                <h3 style="font-size:14px;">重点</h3>
              </li>
              <li style="font-size:14px;">新版火车票最后一行数字的意义可以分为五个部分来理解：</li>
              <li style="font-size:14px;">新版火车票第一部分：也就是前5位是发售车站代码；<br />
                新版火车票第二部分：6，7位为售票方式码，具体内容为：00–09 车站发售；10–19 预约预订； 20–29 代售； 30–39 自动售票； 40–49 备用； 90–98 管理；99 技术维护。<br />
                新版火车票第三部分：8–10位为售票窗编码（非第**售票窗口），售票窗口码编码范围001–255。新版火车票售票窗口码具体内容为：1–200 售票、退票、预约预订窗口； 201–255 管理窗口。（小技巧：对于售票方式码为 20–29 的，窗口号通常为代售处编号。）<br />
                新版火车票第四部分：11–14位表示财收结帐日期（非买票日期），MMDD格式，4位数字。为收入管理的计算日期，通常在每天下午交班后改为第二天日期。也有部分铁路局错后一天。大体可以认为是购票时间。<br />
                新版火车票第五部分：15～21 位是车票号码，除了I和O以外的大写英文字母 + 6位数字，计7位，票号编码范围A000001 – Z100000。规定每一票卷由1000张票底卷成，票面左上方印有票号。由于部分铁路局还会在左上角的票号前面加印窗口号，票号部分应与客票左上角印刷的票号中字母开始的部分一致。这个也是一票一号。<br />
                通过新版火车票最后一行数字的意义判断新版火车票的真假，有心的人一定要牢记！</li>
              <li>
                <h3 style="font-size:14px;">快速辨别火车票真假方法集锦</h3>
              </li>
              <li style="font-size:14px;"><strong>看</strong>：仔细辨认或稍稍转换角度就可以看见在车票的底纹图案上印有“中国铁路”和“CR”（英文“中国铁路”的缩语）等防伪隐形文字及字符。看票面文字、数字是否清晰。真票的金额小数点后面有两个“00”，如果金额小数点后面仅有一个数字“0”，那一定是假票。重点看“到站名、票价、车次”等处，有无涂改、挖补的痕迹。
              </li>
              <li style="font-size:14px;"><strong>摸</strong>：真票手感平顺、光滑；假票手感粗糙有凸出感，尤其是“整版假票”，是用高清晰度彩色打印机打印出来的，这种假票上的油墨多，用手一摸，油墨就粘到手上了。
              </li>
              <li style="font-size:14px;"><strong>照</strong>：用阳光或强的灯光照射新版火车票，观察车票上有无铁路的路徽水印。
              </li>
              <li style="font-size:14px;"><strong>搓</strong>：把车票卷起来搓，真车票放开就会恢复原状，假车票搓后则很难再舒展开。
              </li>
              <li style="font-size:14px;"><strong>比</strong>：拿真火车票来比较。不管是到哪里的火车票，只要是真票，纸张的质量、字迹都差不多，假票的纸张则五花八门，一对比即能分辨真假。对编码也是一个有效的办法：火车票左边最上方的一组编码（如142T082331），在票面最下方的条形编码数字组中，在第二组对应显示上述编码中从字母开始的内容，即T082331。</li>
              <li>
                <h3 style="font-size:14px;">假火车票分类</h3>
              </li>
              <li style="font-size:14px;">假火车票分为“挖补”假火车票和全假火车票两种。</li>
              <li style="font-size:14px;">专家提供了几种识别假票的方法。一种是用放大镜细看票面的数字部分，如发现某些数字与其它字迹不相同或是有涂改过的痕迹，极有可能是假票；如果金额小数点后面仅有一个数字“0”，那一定是假票；票面被挖补处因在制假过程中被刮去一部分纸纤维，所以在阳光直射下略有些发白；此外，用放大镜或在灯光照射下如果看到票面上的车站名、票价、车次等处有裂缝，那也多半是假票。以上方法较适合于“挖补假票”的识别。
              </li>
              <li style="font-size:14px;">对于“整版假票”，首先可以通过手感识别，真票纸质较好，手感平顺、光滑，假票纸质厚薄不一，手感粗糙。同时，真票票面油墨在充足光线的照射下有柔和的光泽，且能看到防伪水印，而假票票面“中国铁路”标志以及背面的水印较为模糊，且手轻揉票面及数字时，油墨会沾在手上。</li>
              <li style="font-size:14px;"><span style="font-size:14px;">识别方法1：</span>挖补的假火车票，一般是利用短途的有效车票或过期的废票作为“票本”，通过挖补手段，将短途车票变成长途车票。识别挖补车票要抓住车票的始发站和终点站、开车时间和票价这几个要点来仔细辨别。挖补的假车票，是经过挑挖、粘补和颜色修改，即使伪造技术再高，只要对着强光、假车票的真面目就会暴露无遗。因为挖补的车票在制作过程中被刮去部分纸纤维，在光照下挖补处显得略有发白，与周围颜色不统一。票面挖补处与周围纸纤维不是整体，挖补处与票面间有细小裂缝。
              </li>
              <li style="font-size:14px;"><span style="font-size:14px;">识别方法2：</span>全假火车票，一般是利用高性能的彩色复印机复印出来的。对于这种车票，主要从手感、颜色、纸质来识别。 由于假车票用的蜡纸多，故手感较滑，而真车票手摸有凹凸感。假车票的颜色比真的车票鲜艳，并有反光的现象，用力捏手上就染有颜色，因为假车票容易脱色。假票面的“中国铁路”标志以及背面的水印较为模糊，真的火车票票面在充足的光线下，能看到清楚的防伪水印。</li>
            </ul>
    </div>
   </div>
   <!--内容 end--> 
 </div>
 </div>
</div>
</body>
</html>
