package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.InnerNoticeService;
import com.l9e.transaction.vo.InnerBookVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.NoticeVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
/**
 * Inner公告管理
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/innerNotice")
public class InnerNoticeController extends BaseController {
private static final Logger logger = Logger.getLogger(InnerNoticeController.class);
	
	@Resource
	private InnerNoticeService innerNoticeService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticePage.do")
	public String queryNoticePage(HttpServletRequest request,
			HttpServletResponse response){
		return "redirect:/innerNotice/queryNoticeList.do";
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticeList.do")
	public String queryNoticeList(HttpServletRequest request,HttpServletResponse response){
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int totalCount = innerNoticeService.queryNoticeListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String, Object>> noticeList = innerNoticeService.queryNoticeList(paramMap);
		request.setAttribute("noticeStatusMap", NoticeVo.getNoticeStatusMap());
		request.setAttribute("inner_channel", InnerBookVo.getChannel());
		request.setAttribute("noticeList", noticeList);
		request.setAttribute("isShowList", 1);
		return "innerNotice/innerNoticeList";
	}
	
	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNotice.do")
	public String queryNotice(String notice_id, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> notice = innerNoticeService.queryNotice(notice_id);
		request.setAttribute("notice", notice);
		request.setAttribute("noticeStatusMap", NoticeVo.getNoticeStatusMap());
		request.setAttribute("inner_channel", InnerBookVo.getChannel());
		return "innerNotice/innerNoticeInfo";
	}
	
	
	/**
	 * 进入更新页面
	 * @param Notice
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updatePreNotice.do")
	public String preUpdateNotice(String notice_id, HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> notice = innerNoticeService.queryNotice(notice_id);
		request.setAttribute("noticeStatusMap", NoticeVo.getNoticeStatusMap());
		request.setAttribute("inner_channel", InnerBookVo.getChannel());
		request.setAttribute("notice", notice);
		return "innerNotice/innerNoticeModify";
	}
	
	
	/**
	 * 更新通知信息
	 * @param Notice
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateNotice.do")
	public String updateNotice(NoticeVo notice, HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		//List<String> areanoList = this.getParamToList(request, "area_no");
		//if(areanoList!=null && areanoList.size()>0){
		//	notice.setProvinces(areanoList.toString());
		//}
		String opt_ren =loginUserVo.getReal_name();
		notice.setOpt_ren(opt_ren);
		innerNoticeService.updateNotice(notice);
		return "redirect:/innerNotice/queryNoticeList.do";
	}
	
	/**
	 * 进入增加页面
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addPreNotice.do")
	public String preAddNotice(Map<String, Object> params, HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("noticeStatusMap", NoticeVo.getNoticeStatusMap());
		request.setAttribute("inner_channel", InnerBookVo.getChannel());
		return "innerNotice/innerNoticeAdd";
	}
	
	
	/**
	 * 删除通知信息
	 * @param Notice
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteNotice.do")
	public String deleteNotice(NoticeVo notice, HttpServletRequest request,HttpServletResponse response){
		innerNoticeService.deleteNotice(notice);
		return "redirect:/innerNotice/queryNoticeList.do";
	}
	
	/**
	 * 添加通知信息
	 * @param Notice
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addNotice.do")
	public String addNotice(NoticeVo notice,HttpServletRequest request,HttpServletResponse response){
		//List<String> areanoList = this.getParamToList(request, "area_no");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		//notice.setProvinces(areanoList.toString());
		String opt_ren = loginUserVo.getReal_name();
		notice.setOpt_ren(opt_ren);
		innerNoticeService.insertNotice(notice);
		return "redirect:/innerNotice/queryNoticeList.do";
	}
}
