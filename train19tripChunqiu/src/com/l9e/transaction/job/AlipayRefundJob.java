package com.l9e.transaction.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alipay.services.AlipayService;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.TradeService;
import com.l9e.transaction.vo.TradeVo;
import com.l9e.util.XmlUtil;

/*
 * 支付宝退款请求
 * @author lihaichao
 */
@Component("alipayRefundJob")
public class AlipayRefundJob {
	private static final Logger logger = Logger
			.getLogger(AlipayRefundJob.class);

	@Resource
	private OrderService orderService;
	@Resource
	private TradeService tradeService;

	private SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	public void alipayRefund() {
		logger.info("【支付宝退款定时任务】开始退款， 现在时间为" + sdf.format(new Date()));
		Map<String, Object> batchNoMap = new HashMap<String, Object>();
		batchNoMap.put("trade_status", TradeVo.WAIT_REFUND);
		batchNoMap.put("trade_type", TradeVo.EXPENSES);
		List<String> batchNoList = tradeService
				.queryDistinctBatchNo(batchNoMap);
		logger.info("【支付宝退款定时任务】退款批次号为：" + batchNoList);
		for (String batchNo : batchNoList) {
			Map<String, String> refundMap = new HashMap<String, String>();
			Map<String, Object> tradeMap = new HashMap<String, Object>();
			tradeMap.put("batch_no", batchNo);
			tradeMap.put("trade_type", TradeVo.EXPENSES);
			tradeMap.put("trade_status", TradeVo.WAIT_REFUND);
			List<TradeVo> tradeList = tradeService.queryTrade(tradeMap);
			StringBuffer sb = new StringBuffer();
			int batch_num = 0; // 退款总笔数
			for (TradeVo trade : tradeList) {
				refundMap.put("order_id", trade.getOrder_id());
				refundMap.put("refund_seq", trade.getTrade_seq());
				refundMap.put("eop_refund_seq", trade.getTrade_id());
				sb.append(trade.getTrade_no()).append("^")
						.append(trade.getTrade_fee()).append("^")
						.append(trade.getRemark()).append("#");
				batch_num++;
				if (batch_num > 100) {
					break;
				}
			}
			
			logger.info("【支付宝退款定时任务】支付宝共有" + batch_num + "笔退款");
			String refundDetailData = sb.toString(); // 单笔数据集
			logger.info("【支付宝退款定时任务】退款数据集为：" + sb.toString());
			String refund_date = sdf.format(new Date());// 退款时间
			refundDetailData = refundDetailData.substring(0, refundDetailData.length() - 1);
			// 把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("batch_no", batchNo);
			sParaTemp.put("refund_date", refund_date);
			sParaTemp.put("batch_num", String.valueOf(batch_num));
			sParaTemp.put("detail_data", refundDetailData);
			String sHtmlText = "";

			try {
				// 构造函数，生成请求URL
				sHtmlText = AlipayService
						.refund_fastpay_by_platform_nopwd(sParaTemp);
				logger.info("【支付宝退款定时任务】批次" + batchNo + "的退款请求返回结果为：" + sHtmlText);
			} catch (Exception e) {
				logger.error("【支付宝退款定时任务】生成请求URL发生异常:" + e.getCause());
				e.printStackTrace();
			}
			if (sHtmlText.length() > 0) {
				Map<String, Object> resultMap = XmlUtil.Dom2Map(sHtmlText);
				if (resultMap != null) {
					if("T".equalsIgnoreCase((String)resultMap.get("is_success"))) {
						for (TradeVo trade : tradeList) {
							trade.setTrade_status(TradeVo.REFUND_SUCCESS);
							trade.setOperate_person("AlipayRefundJob");
							int rows = tradeService.updateTrade(trade);
							logger.info("【支付宝退款定时任务】更新批次为" + batchNo + ", 支付流水为" + trade.getTrade_no() + ", 订单号为" + trade.getOrder_id() + "为成功受影响的行数为" + rows);
							
							refundMap.put("refund_status", TrainConsts.REFUND_STREAM_REFUND_FINISH);
							orderService.updateRefundSStatus(refundMap);
						}
					}else if ("F".equalsIgnoreCase((String)resultMap.get("is_success"))) {
						for(TradeVo trade : tradeList) {
							trade.setTrade_status(TradeVo.REFUND_FAILURE);
							trade.setFail_reason((String)resultMap.get("error"));
							//trade.setRemark(AlipayErrorCode.REFUND_ERROR_CODE_MAP.get("error"));
							trade.setOperate_person("AlipayRefundJob");
							int rows = tradeService.updateTrade(trade);
							logger.info("【支付宝退款定时任务】更新批次为" + batchNo + ", 支付流水为" + trade.getTrade_no() + ", 订单号为" + trade.getOrder_id() + "为失败受影响的行数为" + rows);
							
							refundMap.put("refund_status", TrainConsts.REFUND_STREAM_ALIPAY_FAIL);
							orderService.updateRefundSStatus(refundMap);
						}
					}else if("P".equalsIgnoreCase((String)resultMap.get("is_success"))) {
						for(TradeVo trade : tradeList) {
							trade.setTrade_status(TradeVo.ALIPAY_REFUNDING);
							trade.setOperate_person("AlipayRefundJob");
							int rows = tradeService.updateTrade(trade);
							logger.info("【支付宝退款定时任务】更新批次为" + batchNo + ", 支付流水为" + trade.getTrade_no() + ", 订单号为" + trade.getOrder_id() + "为失败受影响的行数为" + rows);
							refundMap.put("refund_status", TrainConsts.REFUND_STREAM_ALIPAY_REFUNDING);
							orderService.updateRefundSStatus(refundMap);
						}
					}else {
						logger.error("【支付宝退款定时任务】退款请求返回数据不符合文档中的信息，is_success为" + (String)resultMap.get("is_success"));
					}
				}
			} else {
				logger.error("【支付宝退款定时任务】请求阿里云退款结果为空！");
			}
		}
	}

}
