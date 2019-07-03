package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.OrderService;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.MobileMsgUtil;

/**
 * Common
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/chunqiu/common")
public class CommonController extends BaseController{
	protected static final Logger logger = Logger.getLogger(CommonController.class);
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
	@Resource
	private OrderService orderService;
	
	@RequestMapping("/goToErrPage.jhtml")
	public String goToErrPage(HttpServletRequest request,
			HttpServletResponse response){
		String errMsg = "";
		try {
			errMsg = URLDecoder.decode(this.getParam(request, "errMsg"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL解码异常", e);
		}
		request.setAttribute("errMsg", errMsg);
		return "common/error";
	}
	
	@RequestMapping("/sendPhoneMsg_no.jhtml")
	public String sendPhoneMsg(HttpServletRequest request,
			HttpServletResponse response){
		try {
			String key = this.getParam(request, "key");
			String phone = this.getParam(request, "phone");
			String content = URLDecoder.decode(this.getParam(request, "content"), "UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter w = response.getWriter();
			if("19ehcp".equals(key)){
				mobileMsgUtil.send(phone, content);
				w.write("1,正在发送短信");
			}else{
				w.write("-1,密钥错误");
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
/*	@RequestMapping("/hisRefund_no.jhtml")
	public String hisRefund(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		logger.info("===================开始执行！========================");
		String key = "TUI_KUAN_HIS_9";
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			MemcachedUtil.getInstance().setAttribute(key, "TUI_KUAN_HIS", 100*60*1000);
		}else{
			logger.info("历史数据重复处理，已经拒绝");
			return null;
		}
		logger.info("开始处理hc_orderinfo_refund数据");
		List<Map<String, String>> orderRefundList = orderService.queryOrderRefundForHis();
		List<Map<String, String>> refundList = new ArrayList<Map<String, String>>();
		int a=0,b=0;
		logger.info("hc_orderinfo_refund,size=" + orderRefundList.size());
		Map<String, String> map = null; 
		for(Map<String, String> refund : orderRefundList){
			map = new HashMap<String, String>();
			map.put("order_id", refund.get("order_id"));
			logger.info("order_id="+refund.get("order_id"));
			map.put("eop_order_id", refund.get("eop_order_id"));
			
			if("44".equals(refund.get("order_status"))){
				map.put("refund_type", "1");
			}else if("45".equals(refund.get("order_status"))){
				map.put("refund_type", "3");
			}
			if(StringUtils.isEmpty(refund.get("refund_seq"))){
				map.put("refund_seq", CreateIDUtil.createID("TK"));
			}else{
				map.put("refund_seq", refund.get("refund_seq"));
			}
			map.put("eop_refund_seq", refund.get("eop_refund_seq"));
			map.put("refund_money", refund.get("refund_money"));
			map.put("create_time", refund.get("create_time"));
			map.put("refund_time", refund.get("refund_time"));
			map.put("refund_purl", refund.get("refund_purl"));
			map.put("our_remark", refund.get("refund_memo"));
			map.put("refund_plan_time", refund.get("refund_plan_time"));
			map.put("refund_12306_seq", refund.get("refund_12306_seq"));
			if("55".equals(refund.get("refund_status"))){
				map.put("refund_status", "00");
			}else if("66".equals(refund.get("refund_status"))){
				map.put("refund_status", "11");
			}else if("67".equals(refund.get("refund_status"))){
				map.put("refund_status", "33");
			}else if("77".equals(refund.get("refund_status"))){
				map.put("refund_status", "44");
			}else if("88".equals(refund.get("refund_status"))){
				map.put("refund_status", "55");
			}
			refundList.add(map);
			a++;
		}
		
		
		logger.info("开始处理hc_orderinfo_differ数据");
		List<Map<String, String>> differList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> orderDifferList = orderService.queryOrderDifferForHis();
		logger.info("hc_orderinfo_differ,size=" + orderDifferList.size());
		for(Map<String, String> differ : orderDifferList){
			map = new HashMap<String, String>();
			map.put("order_id", differ.get("order_id"));
			map.put("refund_type", "2");
			if(StringUtils.isEmpty(differ.get("refund_seq"))){
				map.put("refund_seq", CreateIDUtil.createID("TK"));
			}else{
				map.put("refund_seq", differ.get("refund_seq"));
			}
			map.put("eop_order_id", differ.get("eop_order_id"));
			map.put("eop_refund_seq", differ.get("eop_refund_seq"));
			map.put("refund_money", differ.get("refund_money"));
			map.put("refund_status", differ.get("differ_status"));
			map.put("refund_time", differ.get("refund_time"));
			map.put("opt_person", differ.get("opt_person"));
			map.put("create_time", differ.get("create_time"));
			differList.add(map);
			b++;
		}
		
		orderService.addOrderRefundHis(refundList, differList);
		logger.info("hc_orderinfo_refund共"+a+"条数据");
		logger.info("hc_orderinfo_differ共"+b+"条数据");
		logger.info("===================执行完毕！========================");
		response.getWriter().write("ok");
		response.getWriter().close();
		//
		return null;
	}*/

}
