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
import com.l9e.transaction.service.ExtNoticeService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.ExtBookVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.NoticeVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
/**
 * ext公告管理
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/extNotice")
public class ExtNoticeController extends BaseController {
private static final Logger logger = Logger.getLogger(ExtNoticeController.class);
	
	@Resource
	private ExtNoticeService extNoticeService;
	@Resource
	private ExtRefundService extRefundService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticePage.do")
	public String queryNoticePage(HttpServletRequest request,
			HttpServletResponse response){
		return "redirect:/extNotice/queryNoticeList.do";
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
		int totalCount = extNoticeService.queryNoticeListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		
		List<Map<String, Object>> noticeList = extNoticeService.queryNoticeList(paramMap);
		
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		List<NoticeVo> ext_channelList=extNoticeService.queryNoticeChannelList(paramMap);	
		Map<String, String> merchantMap =  ExtBookVo.getChannel();
		
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		
		for(int i=0;i<ext_channelList.size();i++){
			String ext_channel=ext_channelList.get(i).getExt_channel();
			if(ext_channel!=null){
			String notice_id =ext_channelList.get(i).getNotice_id();
			String a[] = ext_channel.split(",");
			List<String> list=new ArrayList<String>();
			for(int j=0;j<a.length;j++){
				list.add(a[j]);
			}
			
			//System.out.println(list);
			request.setAttribute("ext_channelStr"+notice_id, notice_id+list.toString());
			}
		}
		
		request.setAttribute("noticeStatusMap", NoticeVo.getNoticeStatusMap());
		request.setAttribute("ext_channel", merchantMap);
		request.setAttribute("noticeList", noticeList);
		request.setAttribute("isShowList", 1);
		return "extNotice/extNoticeList";
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
		Map<String, Object> notice = extNoticeService.queryNotice(notice_id);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("notice_id", notice_id);
		
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap =  ExtBookVo.getChannel();
		
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
	//	request.setAttribute("merchantMap", merchantMap);
		
		List<NoticeVo> ext_channelList=extNoticeService.queryNoticeChannelList(paramMap);		
			String ext_channel=ext_channelList.get(0).getExt_channel();
			if(ext_channel!=null){
			String a[] = ext_channel.split(",");
			List<String> list=new ArrayList<String>();
			for(int j=0;j<a.length;j++){
				list.add(a[j]);
			}
			request.setAttribute("ext_channelStr",list.toString());
//			System.out.println("!!!!!!!!!!!"+list.toString());
			}
		request.setAttribute("notice", notice);
		request.setAttribute("noticeStatusMap", NoticeVo.getNoticeStatusMap());
		request.setAttribute("ext_channel", merchantMap);
		return "extNotice/extNoticeInfo";
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
		Map<String, Object> notice = extNoticeService.queryNotice(notice_id);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("notice_id", notice_id);
		List<NoticeVo> ext_channelList=extNoticeService.queryNoticeChannelList(paramMap);	
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap =  ExtBookVo.getChannel();
		
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
	//	request.setAttribute("merchantMap", merchantMap);
		
		if(ext_channelList!=null){
			
		
			String ext_channel=ext_channelList.get(0).getExt_channel();
			if(ext_channel!=null){
			String a[] = ext_channel.split(",");
			List<String> list=new ArrayList<String>();
			for(int j=0;j<a.length;j++){
				list.add(a[j]);
			}
			request.setAttribute("ext_channelStr",list.toString());
//			System.out.println("!!!!!!!!!!!"+list.toString());
			}
			request.setAttribute("ext_channel", merchantMap);
		}
		request.setAttribute("noticeStatusMap", NoticeVo.getNoticeStatusMap());
		request.setAttribute("notice", notice);
		return "extNotice/extNoticeModify";
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
		List<String> ext_channelList = this.getParamToList(request, "ext_channel");
		//String notice_id=this.getParam(request, "notice_id");
		String ext_channels = "";
		for(int i=0;i<ext_channelList.size();i++){
			ext_channels += (ext_channelList.get(i)+",");
		}
		if(ext_channels.length()!=0){
			ext_channels = ext_channels.substring(0, ext_channels.length()-1);
		}
		notice.setExt_channel(ext_channels.toString());
		String opt_ren =loginUserVo.getReal_name();
		notice.setOpt_ren(opt_ren);
		extNoticeService.updateNotice(notice);
		return "redirect:/extNotice/queryNoticeList.do";
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
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap =  ExtBookVo.getChannel();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
	//	request.setAttribute("merchantMap", merchantMap);
		
		request.setAttribute("noticeStatusMap", NoticeVo.getNoticeStatusMap());
		request.setAttribute("ext_channel", merchantMap);
		return "extNotice/extNoticeAdd";
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
		extNoticeService.deleteNotice(notice);
		return "redirect:/extNotice/queryNoticeList.do";
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
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		List<String> ext_channelList = this.getParamToList(request, "ext_channel");
		String ext_channels = "";
		for(int i=0;i<ext_channelList.size();i++){
			ext_channels += (ext_channelList.get(i)+",");
		}
		if(ext_channels.length()!=0){
			ext_channels = ext_channels.substring(0, ext_channels.length()-1);
		}
		notice.setExt_channel(ext_channels.toString());
		
		String opt_ren = loginUserVo.getReal_name();
		notice.setOpt_ren(opt_ren);
		extNoticeService.insertNotice(notice);
		return "redirect:/extNotice/queryNoticeList.do";
	}
}
