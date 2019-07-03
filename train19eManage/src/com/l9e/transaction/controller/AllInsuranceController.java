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
import com.l9e.transaction.service.AllInsuranceService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.InsuranceVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
@Controller
@RequestMapping("/allInsurance")
public class AllInsuranceController extends BaseController {
	private static final Logger logger = Logger.getLogger(AllInsuranceController.class);
	
	@Resource
	private AllInsuranceService allInsuranceService;
	@Resource
	private ExtRefundService extRefundService;

	//进入查询界面
	@RequestMapping("/queryInsurancePage.do")
	public String queryInsurancePage(HttpServletRequest request,
			HttpServletResponse response){
		request.setAttribute("insueance_statuses", InsuranceVo.getInsurance_Status());
		request.setAttribute("insueance_bx_channel", InsuranceVo.getBx_Channel());
		List<String> sort = this.getParamToList(request, "sort");
		String controlAllChannel = this.getParam(request, "controlAllChannel");//全选
		String selectAllBxchannel = this.getParam(request, "selectAllBxchannel");//全选
		
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getErrorInfoChannels();
		Map<String, String> merchant_map = new HashMap<String, String>();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
			merchant_map.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		/*************************request绑定***************************/
		request.setAttribute("merchantList", merchantMap);
		request.setAttribute("channel_types", merchantMap);
		request.setAttribute("sort_channel", AccountVo.getBxSortChannels());//类别：11公司、22商户、33内嵌、44B2C 
		request.setAttribute("company_channel", AccountVo.getCompanyChannels());//11公司  (19e、19pay)
		request.setAttribute("merchant_channel", merchant_map);//22商户
		request.setAttribute("inner_channel", AccountVo.getInnerChannels());//33内嵌(cmpay、ccb)
		request.setAttribute("b2c_channel", AccountVo.getB2cChannels());//44B2C(app、weixin)
		request.setAttribute("sort", sort);
		request.setAttribute("controlAllChannel", controlAllChannel);
		request.setAttribute("selectAllBxchannel", selectAllBxchannel);
		return "redirect:/allInsurance/queryInsuranceList.do?insueance_status=0";
	}
	
	/******查询列表******/
	@RequestMapping("/queryInsuranceList.do")
	public String queryInsuranceList(HttpServletRequest request, HttpServletResponse response){
		/*************************查询条件***************************/
		String order_id = this.getParam(request, "order_id");
		String telephone = this.getParam(request, "telephone");
		String begin_create_time = this.getParam(request, "begin_create_time");
		String end_create_time = this.getParam(request, "end_create_time");
		String bx_code = this.getParam(request, "bx_code");
		String fail_reason = this.getParam(request, "fail_reason");//失败原因
		List<String>insueance_status = this.getParamToList(request, "insueance_status");
		List<String> insueance_bx_channel = this.getParamToList(request, "insueance_bx_channel");
		List<String>channelList = this.getParamToList(request, "channel");
		List<String> channel = new ArrayList<String>(channelList);
		if(channel.contains("30101612")){
			channel.add("301016");
			channel.add("30101601");
			channel.add("30101602");
		}
		List<String> sort = this.getParamToList(request, "sort");
		String controlAllChannel = this.getParam(request, "controlAllChannel");//全选
		String selectAllBxchannel = this.getParam(request, "selectAllBxchannel");//全选
		/*************************创建Map***************************/
		Map<String,Object>query_Map = new HashMap<String,Object>();
		query_Map.put("order_id", order_id);
		query_Map.put("telephone", telephone);
		query_Map.put("begin_create_time", begin_create_time);
		query_Map.put("end_create_time", end_create_time);
		query_Map.put("bx_code", bx_code);
		query_Map.put("fail_reason", fail_reason);
		query_Map.put("bx_status", insueance_status);
		query_Map.put("bx_channel", insueance_bx_channel);
		query_Map.put("channel", channel);
		//query_Map.put("order_status", AcquireVo.OUT_SUCCESS);
		/*************************分页条件***************************/
		int totalCount = allInsuranceService.queryInsuranceListCount(query_Map);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		query_Map.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		query_Map.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/*************************执行查询***************************/
		List<Map<String,Object>>insuranceList = allInsuranceService.queryInsuranceList(query_Map);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		Map<String, String> merchant_map = AccountVo.getErrorInfoChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
			merchant_map.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		/*************************request绑定***************************/
		request.setAttribute("merchantList", merchantMap);
		request.setAttribute("channel_types", merchant_map);
		request.setAttribute("isShowList", 1);
		request.setAttribute("order_id", order_id);
		request.setAttribute("telephone", telephone);
		request.setAttribute("begin_create_time", begin_create_time);
		request.setAttribute("end_create_time", end_create_time);
		request.setAttribute("bx_code", bx_code);
		request.setAttribute("fail_reason", fail_reason);
		request.setAttribute("insuranceList", insuranceList);
		request.setAttribute("insueance_statuses", InsuranceVo.getInsurance_Status());
		request.setAttribute("insueance_bx_channel", InsuranceVo.getBx_Channel());
		request.setAttribute("insueance_Str", insueance_status.toString());
		request.setAttribute("bx_channel_Str", insueance_bx_channel.toString());
		request.setAttribute("channelStr", channel.toString());
		
		request.setAttribute("sort_channel", AccountVo.getBxSortChannels());//类别：11公司、22商户、33内嵌、44B2C
		request.setAttribute("company_channel", AccountVo.getCompanyChannels());//11公司  (19e、19pay)
		request.setAttribute("merchant_channel", merchant_map);//22商户
		request.setAttribute("inner_channel", AccountVo.getInnerChannels());//33内嵌(cmpay、ccb)
		request.setAttribute("b2c_channel", AccountVo.getB2cChannels());//44B2C(app、weixin)
		request.setAttribute("sort", sort);
		request.setAttribute("controlAllChannel", controlAllChannel);
		request.setAttribute("selectAllBxchannel", selectAllBxchannel);
		return "allInsurance/allInsuranceList";
	}
	
	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("queryInsuranceInfo.do")
	public String queryInsuranceInfo(HttpServletRequest request,HttpServletResponse response){
		/*********************查询条件************************/
		String bx_id = this.getParam(request, "bx_id");
		String order_id = this.getParam(request, "order_id");
		/*********************Map************************/
		Map<String,Object>query_Map = new HashMap<String,Object>();
		query_Map.put("bx_id", bx_id);
		query_Map.put("order_id", order_id);
		/*********************执行service************************/
		List<Map<String,Object>>insuranceInfoList = allInsuranceService.queryInsuranceInfo(query_Map);
		Map<String,Object>insuranceInfo = insuranceInfoList.get(0);
		Map<String, String> orderInfo = allInsuranceService.queryAllBookOrderInfo(order_id); //查询预订订单信息
		List<Map<String,Object>>log_List = allInsuranceService.queryLog(order_id);
		/*********************request绑定************************/
		request.setAttribute("insuranceInfo", insuranceInfo);
		request.setAttribute("log_List", log_List);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("seattype", BookVo.getSeattype());
		request.setAttribute("bx_status", InsuranceVo.getInsurance_Status());
		request.setAttribute("bx_channel", InsuranceVo.getBx_Channel());
		request.setAttribute("bookStatus", AcquireVo.getAcquireStatus());
		
		return "allInsurance/allInsuranceInfo";
	}
	
	/**
	 * 重新投保
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateInsuranceStatusSendAgain.do")
	public void updateInsuranceStatusSendAgain(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		/*********************查询条件************************/
		String order_id = this.getParam(request, "order_id");
		String bx_id = this.getParam(request, "bx_id");
		String bx_code = this.getParam(request, "bx_code");
		String opt_person =loginUserVo.getReal_name();
		String log = opt_person+"点击了重新投保!保单号："+bx_id;
		/*********************Map************************/
		Map<String,Object>update_Map = new HashMap<String,Object>();
		Map<String,Object>log_Map = new HashMap<String,Object>();
		update_Map.put("order_id", order_id);
		update_Map.put("bx_id", bx_id);
		update_Map.put("bx_code", bx_code);
		update_Map.put("bx_status", InsuranceVo.NOT_SENT);
		log_Map.put("opt_person", opt_person);
		log_Map.put("order_id", order_id);
		log_Map.put("log", log);
		log_Map.put("bx_code", bx_code);
		/*********************执行service************************/
		allInsuranceService.addLog(log_Map);
		allInsuranceService.updateInsuranceStatusSendAgain(request,response, update_Map);

	}
	
	@RequestMapping("updateInsuranceStatusNeedCancel.do")
	public void updateInsuranceStatusNeedCancel(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		/*********************查询条件************************/
		String order_id = this.getParam(request, "order_id");
		String bx_id = this.getParam(request, "bx_id");
		String bx_code = this.getParam(request, "bx_code");
		String opt_person = loginUserVo.getReal_name();
		String log = opt_person+"点击了退保!保单号："+bx_id;
		/*********************Map************************/
		Map<String,Object>update_Map = new HashMap<String,Object>();
		Map<String,Object>log_Map = new HashMap<String,Object>();
		update_Map.put("order_id", order_id);
		update_Map.put("bx_id", bx_id);
		update_Map.put("bx_code", bx_code);
		update_Map.put("bx_status", InsuranceVo.NEED_CANCEL);
		
		log_Map.put("opt_person", opt_person);
		log_Map.put("order_id", order_id);
		log_Map.put("log", log);
		/*********************执行service************************/
		allInsuranceService.updateInsuranceStatusNeedCancel(request,response, update_Map);
		allInsuranceService.addLog(log_Map);
		logger.info(log);
		
	}

	/**
	 * 重新投保
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("plUpdateAgain.do")
	public String plUpdateAgain(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		/*********************查询条件************************/
		String opt_person =loginUserVo.getReal_name();
		logger.info(opt_person+"点击了批量重新投保!");
		allInsuranceService.plUpdateAgain();
		return "redirect:/allInsurance/queryInsuranceList.do?insueance_status=0";
	}
}
