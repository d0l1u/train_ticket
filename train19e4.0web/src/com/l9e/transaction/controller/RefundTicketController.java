package com.l9e.transaction.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;

/**
 * 车票退订
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/refund")
public class RefundTicketController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(RefundTicketController.class);
	
	
	@Resource
	private OrderService orderService;
	
	
	@RequestMapping("/refunding.jhtml")
	public String refunding(RefundVo refund, MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException{
		logger.info("[代理商主动发起退款]order_id="+refund.getOrder_id());
		//9点-21点可以
		Date currentDate = new Date();
		String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "090000", "yyyyMMddHHmmss");//9:00
		Date end = DateUtil.stringToDate(datePre + "210000", "yyyyMMddHHmmss");//21:00
		if(currentDate.before(begin) || currentDate.after(end)){
			logger.info("不受理该退款，发起时间为："+DateUtil.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss"));
			String errMsg = "<span style='line-height:40px;'>由于火车票业务新上线，每日临时受理退款时间为早9:00～晚21:00，请见谅。<br />如有特殊情况请先到车站退票，第二日持车站退款小票到代理商处退款。</span>";
			errMsg = URLEncoder.encode(errMsg, "UTF-8");
			return "redirect:/common/goToErrPage.jhtml?errMsg="+errMsg;
		}
		String tipUrl = null;//小票地址
		//上传小票
		MultipartFile upfile = request.getFile("upfile");
		if(!upfile.isEmpty()){
			String fileSuffix = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));

			if(".jpg".equalsIgnoreCase(fileSuffix)
					|| ".png".equalsIgnoreCase(fileSuffix)){
				String fileName = CreateIDUtil.createID("IMG") + fileSuffix;
				String prePath = request.getSession().getServletContext().getRealPath("/upload");
				String date = DateUtil.dateToString(new Date(), "yyyyMMdd");
				prePath = prePath + "/" + date;
		        File targetFile = new File(prePath, fileName);  
		        if(!targetFile.exists()){
		        	targetFile.mkdirs();
		        }
				upfile.transferTo(targetFile);
				tipUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ "/upload/" + date + "/" + fileName;
			}
		}
		String old_refund_status = this.getParam(request, "old_refund_status");//原来的退款状态
		refund.setRefund_status(TrainConsts.REFUND_PRE_REFUND);//准备退款
		refund.setRefund_purl(tipUrl);
		orderService.refunding(refund, old_refund_status);
		
		return "redirect:/query/queryOrderList.jhtml";
	}
	
	/**
	 * 退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/refund.jhtml")
	public String refund(MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		/**
		 * token防止重复提交
		 */
		String token = this.getParam(request, "token");
		if(null==request.getSession().getAttribute("sessionToken") 
				|| !token.equals((String)request.getSession().getAttribute("sessionToken"))){
			logger.info("[token校验]退款发生重复提交，请求被拒绝，重定向到订单查询页面");
			return "redirect:/query/queryOrderList.jhtml";
		}else{
			request.getSession().removeAttribute("sessionToken");
		}
		String order_id = this.getParam(request, "order_id");
		logger.info("[代理商主动发起退款]order_id="+order_id);
		//9点-21点可以
		Date currentDate = new Date();
		String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "070000", "yyyyMMddHHmmss");//7:00
		Date end = DateUtil.stringToDate(datePre + "224500", "yyyyMMddHHmmss");//23:00
		if(currentDate.before(begin) || currentDate.after(end)){
			logger.info("不受理该退款，发起时间为："+DateUtil.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss"));
			String errMsg = "<span style='line-height:40px;'>每日受理退票时间为早7:00～晚23:00，请见谅。<br />如有特殊情况请先到车站退票，第二日持车站退款小票到代理商处退款。</span>";
			errMsg = URLEncoder.encode(errMsg, "UTF-8");
			return "redirect:/common/goToErrPage.jhtml?errMsg="+errMsg;
		}
		
		String cp_id_str = this.getParam(request, "cp_id_str");
		List<Map<String, String>> refundList = new ArrayList<Map<String, String>>();
		Map<String, String> refundMap = null;
		for(String cp_id : cp_id_str.split(",")){
			logger.info("[代理商主动发起退款]cp_id="+cp_id);
			refundMap = new HashMap<String, String>();
			
			String refund_purl = null;//小票地址
			//上传小票
//			MultipartFile upfile = request.getFile("upfile_" + cp_id);
//			if(!upfile.isEmpty()){
//				String fileSuffix = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));
//	
//				if(".jpg".equalsIgnoreCase(fileSuffix) || ".png".equalsIgnoreCase(fileSuffix)
//						|| ".jpeg".equalsIgnoreCase(fileSuffix) || ".bmp".equalsIgnoreCase(fileSuffix)){
//					String fileName = CreateIDUtil.createID("IMG") + fileSuffix;
//					String prePath = request.getSession().getServletContext().getRealPath("/upload");
//					String date = DateUtil.dateToString(new Date(), "yyyyMMdd");
//					prePath = prePath + "/" + date;
//			        File targetFile = new File(prePath, fileName);  
//			        if(!targetFile.exists()){
//			        	targetFile.mkdirs();
//			        }
//					upfile.transferTo(targetFile);
//					refund_purl = request.getScheme() + "://"
//						+ request.getServerName() + ":" + request.getServerPort()
//						+ "/upload/" + date + "/" + fileName;
//					logger.info("退款小票" + refund_purl);
//				}
//			}
			refundMap.put("refund_type", TrainConsts.REFUND_TYPE_1);//用户退款
			refundMap.put("order_id", order_id);
			refundMap.put("cp_id", cp_id);
			refundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
			refundMap.put("refund_money", this.getParam(request, "refund_money_" + cp_id));//退款金额
			refundMap.put("refund_purl", refund_purl);
			refundMap.put("user_remark", this.getParam(request, "user_remark_" + cp_id));
			refundMap.put("refund_status", TrainConsts.REFUND_STREAM_PRE_REFUND);//准备退款
			refundMap.put("old_refund_status", this.getParam(request, "refund_status_" + cp_id));//旧的退款状态
			refundMap.put("refund_percent", this.getParam(request, "refund_percent_" + cp_id));
			refundList.add(refundMap);
		}
		orderService.addRefundStream(refundList, order_id);
		
		return "redirect:/query/queryOrderList.jhtml?selectType=4";
	}
	
}
