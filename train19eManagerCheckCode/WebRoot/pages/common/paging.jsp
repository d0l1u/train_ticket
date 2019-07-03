<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/css/page.css" rel="stylesheet" type="text/css" />
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%
	PageVo pageObject = (PageVo) request
			.getAttribute("pageBean");
	int pageIndex = pageObject.getPageIndex();
	int totalPageCount = pageObject.getTotalPageCount();

	if (pageObject.getTotalPageCount() <= 0) {
%>
<table width="70%" height="25px;" border="0" align="center"
	cellpadding="0" cellspacing="0"
	style="margin-top: 1px; margin-left: auto; margin-right: auto">
	<tr>
		<td align="center">
			<font color="#ff0000"><strong>没有可显示的项目</strong> </font>
		</td>
	</tr>

</table>
<%
	} else {
%>
<!-- pageIndex是指当前页号，本页也是处理当前业务的分页jsp -->
<input type='hidden' name='pageIndex' />
<script type="text/javascript">
 
    var curPageIndex=<%=pageIndex%>;
    var totalPageCount=<%=totalPageCount%>;
 
    function gotoPageIndex(pageIndex) 
    {
    	if(document.forms){
       		if(document.forms.item(0) != null && document.forms.item(0).pageIndex && document.forms.item(0).method == 'post'){
       			document.forms.item(0).pageIndex.value = pageIndex;
       			document.forms.item(0).submit();
       			return;
       		}
        }
       var url=location.href;
       var n = url.lastIndexOf("?");//lastIndexOf方法是从右向左执行的
       var newUrl;
       if(n==(-1))
       {
          newUrl=url + "?pageIndex=" + pageIndex;
          window.location.href =newUrl;
          return;
        }
          
       var query = url.substring(n+1);//截取？号后面的内容
       var pairs = query.split("&");//将query按&来分割成部分
       var newQuery = "";
       
       for(var i=0; i<pairs.length; i++) 
       {
          var pos = pairs[i].indexOf("=");//找到=位置
        
          if(pos==-1) continue;
        
          var argname = pairs[i].substring(0,pos);//截取两者之间的字符
          
          if(argname=="pageIndex") continue;
        
          newQuery = newQuery + pairs[i] + "&";
       }
       var newurl=url.substring(0,n+1) + newQuery + "pageIndex=" + pageIndex;
       if(document.getElementById("orderBeginDate").value!="" && query.indexOf("orderBeginDate")==-1){
       		newurl += "&orderBeginDate="+document.getElementById("orderBeginDate").value;
       }
       if(document.getElementById("orderEndDate").value!="" && query.indexOf("orderEndDate")==-1){
       		newurl += "&orderEndDate="+document.getElementById("orderEndDate").value;
       }  
       if(document.getElementById("status").value!==""&&query.indexOf("status")==-1){
       		newurl += "&status="+document.getElementById("status"		).value;
       }  
       window.location.href =newurl;   
  }
  
  function gotoPageFirst()
  {
     gotoPageIndex(1);
  }
  
  
  function gotoPageLast()
  {
     
     gotoPageIndex(totalPageCount);
  }
  
  function gotoPagePrev()
  {
      
     <%
        if(pageObject.isHasPrevPage())
        {
     %>
            var prevIndex=curPageIndex-1;
            gotoPageIndex(prevIndex);
      <%
        }
      %>
  }
  
  function gotoPageNext()
  {
     <%
        if(pageObject.isHasNextPage())
        {
     %>
            var nextIndex=curPageIndex+1;
            gotoPageIndex(nextIndex);
      <%
        }
      %>
  }
  
  function gotoButtonClicked()
  {
       var strIndex=document.getElementById("gotoLabel").value;
       var nIndex=parseInt(strIndex);
       
       if(isNaN(nIndex))
       {
          alert("输入跳转到的页号必须是数字!");
       }
       else if(nIndex <1)
       {
         alert("输入跳转到的页号必须大于或等于1!");
       }
       else if(nIndex >totalPageCount)
       {
          alert("输入跳转到的页号超出总页数!");
       }
       else
       {
           gotoPageIndex(nIndex);
       }
       
  }
 </script>
<div class="turn2pageWrap">
	<div class="turn2page">
		<ul>
			<li>
				共计<%=totalPageCount%>页
			</li>
			<li>
				<a href="javascript:gotoPageFirst();">首页</a> 
			</li>
			<li>
				<a href="javascript:gotoPagePrev();">上一页</a>
			</li>
			<%
				int start = ((pageIndex - 1) / 4) * 4 + 1;
					int end = ((pageIndex + 3) / 4) * 4;
					if (end > totalPageCount)
						end = totalPageCount;

					if (start >= 5) {
			%>
			<li>
				<a href="javascript:gotoPageIndex(<%=start - 1%>);">...</a>
			</li>

			<%
				}

					for (int i = start; i <= end; i++) {
			%>
			<li>
				<a href="javascript:gotoPageIndex(<%=i%>);" <% if(i==pageIndex){%>
					class="currentPage" <%}%>><%=i%></a>
			</li>
			<%
				}

					if (end < totalPageCount) {
			%>
			<li>
				<a href="javascript:gotoPageIndex(<%=end + 1%>);">...</a>
			</li>
			<%
				}
			%>
			<li>
				<a href="javascript:gotoPageNext();">下一页</a>
			</li>
			<li>
				<a href="javascript:gotoPageLast();">末页</a>
			</li>
		</ul>
	</div>
	<div class="turn2pageInput">
		<span class="pr5">快速跳转</span>
		<input id="gotoLabel" name="gotoLabel" type="text" class="" />
		<a href="javascript:gotoButtonClicked();"><img
				src="/images/btnTurn2Page.gif" title="点击跳转" /></a>
	</div>
</div>
<%
	}
%>

