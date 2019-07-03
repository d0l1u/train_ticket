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
import com.l9e.transaction.service.ManualfindService;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;

/**
 * 支付完成直接出票成功需人工查询
 * @author zhangjc02
 *
 */
@Controller
@RequestMapping("/manualfind")
public class ManualFindController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(ManualOrderController.class);
	@Resource
	private ManualfindService manualfindService;
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryManualPage.do")
	public String queryAcquirePage(HttpServletRequest request,
			HttpServletResponse response){
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		int totalCount = manualfindService.queryManualfindCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, String>> findList = manualfindService.queryManualfindList(paramMap);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("manualList", findList);
		request.setAttribute("isShowList", 1);
		return "manualfind/manualfindList";
	}

}
