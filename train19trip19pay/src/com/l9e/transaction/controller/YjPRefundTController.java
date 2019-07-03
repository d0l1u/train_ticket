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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
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
import com.l9e.util.EmappSignService;

/**
 * 车票退订
 * @author yangchao
 *
 */
@Controller
@RequestMapping("/refund")
public class YjPRefundTController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(YjPRefundTController.class);
	
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[appInitKey]}")
	private String appInitKey;//验签静态key
	
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
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
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
			MultipartFile upfile = request.getFile("upfile_" + cp_id);
			if(!upfile.isEmpty()){
				String fileSuffix = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));
	
				if(".jpg".equalsIgnoreCase(fileSuffix) || ".png".equalsIgnoreCase(fileSuffix)
						|| ".jpeg".equalsIgnoreCase(fileSuffix) || ".bmp".equalsIgnoreCase(fileSuffix)){
					String fileName = CreateIDUtil.createID("IMG") + fileSuffix;
					String prePath = request.getSession().getServletContext().getRealPath("/upload");
					String date = DateUtil.dateToString(new Date(), "yyyyMMdd");
					prePath = prePath + "/" + date;
			        File targetFile = new File(prePath, fileName);  
			        if(!targetFile.exists()){
			        	targetFile.mkdirs();
			        }
					upfile.transferTo(targetFile);
					refund_purl = request.getScheme() + "://"
						+ request.getServerName() + ":" + request.getServerPort()
						+ "/upload/" + date + "/" + fileName;
					logger.info("退款小票" + refund_purl);
				}
			}
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
		
		return "redirect:/query/queryOrderList.jhtml";
	}
	
	
	
	
	/**
	 * 接收19pay发送的退款结果通知请求
	* @param request
	 * @param response
	 */
	@RequestMapping("/refundToTrain_no.jhtml")
	public void refundToTrain(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.info("接收19pay的退款结果通知请求!");
		Map<String,String> resultMap=new HashMap<String,String>();
		String service=this.getParam(request, "service");
		String sign_type=this.getParam(request, "sign_type");
		String timestamp=this.getParam(request, "timestamp");
		String partner_id=this.getParam(request, "partner_id");
		String sign=this.getParam(request, "sign");
		String order_id=this.getParam(request, "order_id");
		String refund_seq=this.getParam(request, "refund_seq");	//train退款请求流水号
		String plat_refund_seq=this.getParam(request, "plat_refund_seq");	//19pay退款流水号，可以去19pay查询退款状态 plat_refund_seq
		String refund_status=this.getParam(request, "refund_status");	//SUCCESS退款成功
		String actual_refund_money=this.getParam(request, "refund_money");//退款成功金额，实际退款金额 
		String order_left_money=this.getParam(request, "order_left_money"); //订单扣除退款后剩余金额
		String refund_time=this.getParam(request, "refund_finish_time");	//退款完成时间 refund_time
		String plat_order_id=this.getParam(request, "plat_order_id");
		String remark=this.getParam(request, "remark");
		
		logger.info("订单号："+order_id);
		logger.info("金额："+actual_refund_money);
		logger.info("train退款流水号："+refund_seq);
		logger.info("19pay退款流水号："+plat_refund_seq);
		logger.info("退款完成时间："+refund_time);

		
		resultMap.put("service", service);
		resultMap.put("sign_type", sign_type);
		resultMap.put("timestamp", timestamp);
		resultMap.put("partner_id", partner_id);
		resultMap.put("refund_seq", refund_seq);
		resultMap.put("plat_refund_seq",plat_refund_seq);
		resultMap.put("order_id", order_id);
		resultMap.put("plat_order_id", plat_order_id);
		resultMap.put("refund_status", refund_status);
		resultMap.put("refund_money", actual_refund_money);
		resultMap.put("order_left_money", order_left_money);
		resultMap.put("refund_finish_time", refund_time);
		resultMap.put("remark", remark);
		
		Map<String,String> refundReMap=new HashMap<String,String>();
		refundReMap.put("order_id", order_id);
		refundReMap.put("refund_seq", refund_seq);
		refundReMap.put("plat_refund_seq", plat_refund_seq);
		refundReMap.put("actual_refund_money", actual_refund_money);
		refundReMap.put("refund_time", refund_time);
		refundReMap.put("refund_status", TrainConsts.REFUND_STREAM_REFUND_FINISH);

		//开始验证签名
		boolean flag=false;
		flag=EmappSignService.checkReqSign(resultMap, sign, appInitKey);
		logger.info("签名报文："+sign);
		int update=0;
		if(flag){
			logger.info("订单号："+order_id+" 退款结果通知请求验签成功！");
			if("SUCCESS".equals(refund_status)){
				if(StringUtils.isEmpty(plat_refund_seq)){
					logger.info("19pay退款流水号为空！ 订单号："+order_id);
					write2Response(response,"N");
				}else{
					try {
						update=orderService.updateRefundStreamInfo(refundReMap);
						if(update==1){
							write2Response(response,"Y");
							logger.info("已更新19pay退款交易流水号、实际退款金额及退款状态！");
						}else{
							write2Response(response,"N");
							logger.info("更新19pay退款交易流水号、实际退款金额及退款状态失败！");
						}
					} catch (Exception e) {
						write2Response(response,"N");
						logger.error("订单号："+order_id+"--异步反馈，更新19pay退款交易流水号失败！", e);
					}
				}
			}else{
				write2Response(response,"N");
				logger.info("订单号："+order_id+"--接收19pay退款通知请求状态不是SUCCESS！");
			}
		}else{
			try {
				logger.info("订单号："+order_id+" 退款结果通知请求验签失败！");
				write2Response(response,"N");
			} catch (Exception e) {
				logger.info("订单号："+order_id+" 退款验签失败后，输出异常！");
				return;
			}
		}
	}
	
}
