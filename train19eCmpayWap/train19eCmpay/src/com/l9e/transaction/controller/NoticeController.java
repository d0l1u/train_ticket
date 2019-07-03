package com.l9e.transaction.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;

/**
 * 公告
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {
	protected static final Logger logger = Logger.getLogger(NoticeController.class);
	
	/**
	 * 查询公告列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticeList.jhtml")
	public String queryNoticeList(HttpServletRequest request, 
			HttpServletResponse response){
		List<Map<String, String>> noticeList = commonService.queryNoticeList();
		request.setAttribute("noticeList", noticeList);
		return "common/noticeList";
	}
	
	/**
	 * 查询全部公告
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticeAllList.jhtml")
	public String queryNoticeAllList(HttpServletRequest request, 
			HttpServletResponse response){
		List<Map<String, String>> noticeList = commonService.queryNoticeAllList();
		request.setAttribute("noticeList", noticeList);
		return "common/noticeQuery";
	}
	
	/**
	 * 查询公告内容
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticeInfo_no.jhtml")
	public String queryNoticeInfo(HttpServletRequest request, 
			HttpServletResponse response){
		String noticeId = this.getParam(request, "noticeId");
		Map<String, String> noticeInfo = commonService.queryNoticeInfo(noticeId);
		request.setAttribute("noticeInfo", noticeInfo);
		return "common/noticeInfo";
	}

}
