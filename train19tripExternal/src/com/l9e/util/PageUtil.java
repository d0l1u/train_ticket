package com.l9e.util;

import javax.servlet.http.HttpServletRequest;

import com.l9e.transaction.vo.PageVo;

/**
 * 分页
 * @author zhangjun
 *
 */
public class PageUtil {
	
	private static PageUtil pageUtil = null;
	
	private PageUtil(){}
	
	public static synchronized PageUtil getInstance(){
		if(pageUtil == null){
			return new PageUtil();
		}else{
			return pageUtil;
		}
	}
	
	/**
	 * 分页 eg: PageUtil.getInstance.paging(request, 15, 200);
	 * @param request
	 * @param pageSize 每页记录数
	 * @param totalCount 总记录数
	 */
	public PageVo paging(HttpServletRequest request, int pageSize, int totalCount){
		String strPageIndex = request.getParameter("pageIndex");
		int nPageIndex=1;
		if(strPageIndex != null && strPageIndex.trim().length() != 0)
			nPageIndex = Integer.parseInt(strPageIndex);
		PageVo page = new PageVo(nPageIndex, pageSize, totalCount);
		request.setAttribute("pageBean", page);	
		
		return page;
	}
}
