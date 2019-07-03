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
import com.l9e.transaction.service.BookService;
import com.l9e.transaction.service.InsuranceService;
import com.l9e.transaction.service.LoginService;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.InsuranceVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;
	/**
	 * 保险管理
	 * @author liht
	 *
	 */

	@Controller
	@RequestMapping("/insurance")
	public class InsuranceController extends BaseController{
		private static final Logger logger = 
			Logger.getLogger(InsuranceController.class);
		
		@Resource
		private InsuranceService insuranceService;
		@Resource
		private BookService bookService ;
		/**
		 * 进入查询页面
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/queryInsurancePage.do")
		public String queryInsurancePage(HttpServletRequest request,
				HttpServletResponse response){
			request.setAttribute("insueance_statuses", InsuranceVo.getInsurance_Status());
			//获得系统当前时间
			//String now = DateUtil.nowDate();
			//request.setAttribute("now", now);//绑定当前时间
			request.setAttribute("orderSource", BookVo.getOrderSource());
			return "insurance/insuranceList";
		}
		
		/**
		 * 查询列表
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/queryInsuranceList.do")
		public String queryInsuranceList(HttpServletRequest request,
				HttpServletResponse response){
			//获得系统当前时间
			//String now = DateUtil.nowDate();
			/*************************查询条件***************************/
			String order_id = this.getParam(request, "order_id");
			String telephone = this.getParam(request, "telephone");
			String begin_create_time = this.getParam(request, "begin_create_time");
			String end_create_time = this.getParam(request, "end_create_time");
			String bx_code = this.getParam(request, "bx_code");
			List<String>insueance_status = this.getParamToList(request, "insueance_status");
			List<String> order_sourceList = this.getParamToList(request, "order_source");
			
			/*************************创建Map***************************/
			Map<String,Object>query_Map = new HashMap<String,Object>();
			query_Map.put("order_id", order_id);
			query_Map.put("telephone", telephone);
			query_Map.put("begin_create_time", begin_create_time);
			query_Map.put("end_create_time", end_create_time);
			query_Map.put("bx_code", bx_code);
			query_Map.put("bx_status", insueance_status);
			query_Map.put("order_status", BookVo.OUT_SUCCESS);
			query_Map.put("order_source", order_sourceList);
			/*************************分页条件***************************/
			int totalCount = insuranceService.queryInsuranceListCount(query_Map);//总条数	
			PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
			query_Map.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			query_Map.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			
			/*************************执行查询***************************/
			List<Map<String,Object>>insuranceList = insuranceService.queryInsuranceList(query_Map);
			/*************************request绑定***************************/
			request.setAttribute("isShowList", 1);
			request.setAttribute("order_id", order_id);
			request.setAttribute("telephone", telephone);
			request.setAttribute("begin_create_time", begin_create_time);
			request.setAttribute("end_create_time", end_create_time);
//			//如果begin_create_time为空，则显示当前系统时间
//			if(begin_create_time.equals("")){
//				request.setAttribute("begin_create_time", now);
//			}else{
//				request.setAttribute("begin_create_time", begin_create_time);
//			}
//			//如果end_create_time为空，则显示当前系统时间
//			if(end_create_time.equals("")){
//				request.setAttribute("end_create_time", now);
//			}else{
//				request.setAttribute("end_create_time", end_create_time);
//			}
			
			request.setAttribute("bx_code", bx_code);
			request.setAttribute("insuranceList", insuranceList);
			request.setAttribute("insueance_statuses", InsuranceVo.getInsurance_Status());
			request.setAttribute("insueance_Str", insueance_status.toString());
			request.setAttribute("orderSource", BookVo.getOrderSource());
			request.setAttribute("orderSourceStr", order_sourceList.toString());
			return "insurance/insuranceList";
		}
		
		/**
		 * 查询明细
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("queryInsuranceInfo.do")
		public String queryInsuranceInfo(HttpServletRequest request,
				HttpServletResponse response){
			/*********************查询条件************************/
			String bx_id = this.getParam(request, "bx_id");
			String order_id = this.getParam(request, "order_id");
			/*********************Map************************/
			Map<String,Object>query_Map = new HashMap<String,Object>();
			query_Map.put("bx_id", bx_id);
			query_Map.put("order_id", order_id);
			/*********************执行service************************/
			List<Map<String,Object>>insuranceInfoList = insuranceService.queryInsuranceInfo(query_Map);
			Map<String,Object>insuranceInfo = insuranceInfoList.get(0);
			Map<String, String> orderInfo = bookService.queryBookOrderInfo(order_id);
			List<Map<String,Object>>log_List = insuranceService.queryLog(order_id);
			/*********************request绑定************************/
			request.setAttribute("insuranceInfo", insuranceInfo);
			request.setAttribute("bookStatus", BookVo.getBookStatus());
			request.setAttribute("log_List", log_List);
			request.setAttribute("orderInfo", orderInfo);
			request.setAttribute("seattype", BookVo.getSeattype());
			request.setAttribute("bx_status", InsuranceVo.getInsurance_Status());
			return "insurance/insuranceInfo";
		}
		
		/**
		 * 重新投保
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("updateInsuranceStatusSendAgain.do")
		public String updateInsuranceStatusSendAgain(HttpServletRequest request,
				HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

			/*********************查询条件************************/
			String order_id = this.getParam(request, "order_id");
			String bx_id = this.getParam(request, "bx_id");
			String bx_code = this.getParam(request, "bx_code");
			String opt_person =loginUserVo.getReal_name();
			String log = opt_person+"点击了重新投保!保单号："+bx_code;
			/*********************Map************************/
			Map<String,Object>update_Map = new HashMap<String,Object>();
			Map<String,Object>log_Map = new HashMap<String,Object>();
			update_Map.put("order_id", order_id);
			update_Map.put("bx_id", bx_id);
			update_Map.put("bx_status", InsuranceVo.NOT_SENT);
			log_Map.put("opt_person", opt_person);
			log_Map.put("order_id", order_id);
			log_Map.put("log", log);
			/*********************执行service************************/
			insuranceService.updateInsuranceStatusSendAgain(update_Map);
			insuranceService.addLog(log_Map);
			
			return "redirect:/insurance/queryInsuranceList.do";
		}
		
		@RequestMapping("updateInsuranceStatusNeedCancel.do")
		public String updateInsuranceStatusNeedCancel(HttpServletRequest request,
				HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

			/*********************查询条件************************/
			String order_id = this.getParam(request, "order_id");
			String bx_id = this.getParam(request, "bx_id");
			String bx_code = this.getParam(request, "bx_code");
			String opt_person = loginUserVo.getReal_name();
			String log = opt_person+"点击了退保!保单号："+bx_code;
			/*********************Map************************/
			Map<String,Object>update_Map = new HashMap<String,Object>();
			Map<String,Object>log_Map = new HashMap<String,Object>();
			update_Map.put("order_id", order_id);
			update_Map.put("bx_id", bx_id);
			update_Map.put("bx_status", InsuranceVo.NEED_CANCEL);
			log_Map.put("opt_person", opt_person);
			log_Map.put("order_id", order_id);
			log_Map.put("log", log);
			/*********************执行service************************/
			insuranceService.updateInsuranceStatusNeedCancel(update_Map);
			insuranceService.addLog(log_Map);
			
			return "redirect:/insurance/queryInsuranceList.do";
			
		}
}	
