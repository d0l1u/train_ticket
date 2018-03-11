package com.l9e.transaction.controller;

import java.util.ArrayList;
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
import com.l9e.transaction.service.AppNoticeService;
import com.l9e.transaction.vo.AppNoticeVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;

@Controller
@RequestMapping("/appNotice")
public class AppNoticeController extends BaseController {
private static final Logger logger = Logger.getLogger(AppNoticeController.class);
	
	@Resource
	private AppNoticeService appNoticeService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticePage.do")
	public String queryNoticePage(HttpServletRequest request,
			HttpServletResponse response){
		return "redirect:/appNotice/queryNoticeList.do";
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
		int totalCount = appNoticeService.queryNoticeListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String, Object>> noticeList = appNoticeService.queryNoticeList(paramMap);
		request.setAttribute("noticeStatusMap", AppNoticeVo.getNoticeStatusMap());
		request.setAttribute("noticeSystemMap", AppNoticeVo.getNoticeSystemMap());
		request.setAttribute("noticeList", noticeList);
		request.setAttribute("isShowList", 1);
		return "appNotice/appNoticeList";
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
		Map<String, Object> notice = appNoticeService.queryNotice(notice_id);
		List<String> user_phoneList = appNoticeService.queryUserPhone(notice_id);
//		List<String> list = new ArrayList<String>();String user_phone = null;
//		for(int i=0;i<user_phoneList.size();i++){
//			user_phone = user_phoneList.get(i).get("user_phone");
//			list.add(user_phone);
//		}
		request.setAttribute("notice", notice);
		request.setAttribute("user_phone", user_phoneList);
		request.setAttribute("noticeStatusMap", AppNoticeVo.getNoticeStatusMap());
		request.setAttribute("noticeSystemMap", AppNoticeVo.getNoticeSystemMap());
		return "appNotice/appNoticeInfo";
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
		Map<String, Object> notice = appNoticeService.queryNotice(notice_id);
		//request.setAttribute("province", appNoticeService.getProvince());
		request.setAttribute("noticeStatusMap", AppNoticeVo.getNoticeStatusMap());
		request.setAttribute("noticeSystemMap", AppNoticeVo.getNoticeSystemMap());
		request.setAttribute("notice", notice);
		return "appNotice/appNoticeModify";
	}
	
	
	/**
	 * 更新通知信息
	 * @param Notice
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateNotice.do")
	public String updateNotice(AppNoticeVo notice, HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		//List<String> areanoList = this.getParamToList(request, "area_no");
		//if(areanoList!=null && areanoList.size()>0){
		//	notice.setProvinces(areanoList.toString());
		//}
		String opt_ren =loginUserVo.getReal_name();
		notice.setOpt_ren(opt_ren);
		appNoticeService.updateNotice(notice);
		return "redirect:/appNotice/queryNoticeList.do";
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
		request.setAttribute("noticeStatusMap", AppNoticeVo.getNoticeStatusMap());
		request.setAttribute("noticeSystemMap", AppNoticeVo.getNoticeSystemMap());
		//request.setAttribute("province", appNoticeService.getProvince());
		return "appNotice/appNoticeAdd";
	}
	
	
	/**
	 * 删除通知信息
	 * @param Notice
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteNotice.do")
	public String deleteNotice(AppNoticeVo notice, HttpServletRequest request,HttpServletResponse response){
		appNoticeService.deleteNotice(notice);
		return "redirect:/appNotice/queryNoticeList.do";
	}
	
	/**
	 * 添加通知信息
	 * @param Notice
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addNotice.do")
	public String addNotice(AppNoticeVo notice,HttpServletRequest request,HttpServletResponse response){
		//List<String> areanoList = this.getParamToList(request, "area_no");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		//notice.setProvinces(areanoList.toString());
		String opt_ren = loginUserVo.getReal_name();
		notice.setOpt_ren(opt_ren);
		appNoticeService.insertNotice(notice);
		String user_phones = notice.getUser_phone();
		String notice_id = appNoticeService.queryNoticeId(notice);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("notice_id", notice_id);
		System.out.println(notice.getNotice_system());
		if(notice.getNotice_system()=="33" || notice.getNotice_system().equals("33")){
			String[] user_phone_arr = user_phones.split(",");
			for(int i=0; i<user_phone_arr.length; i++){
				String user_phone = user_phone_arr[i];
				if(StringUtil.isNotEmpty(user_phone)){
					paramMap.put("user_phone", user_phone);
					paramMap.put("status", "00");//00：未接收；11：已接受
					appNoticeService.addNoticeUser(paramMap);
				}
			}
		}
		return "redirect:/appNotice/queryNoticeList.do";
	}
}