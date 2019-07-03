package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.Tj_Hc_orderInfo_Today_Service;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.util.CreateIDUtil;

/**
 * 统计客服有效操作数流程
 * @author liht
 *
 */
@Component("tj_Opter_Job")
public class Tj_Opter_Job {
	private static final Logger logger = Logger.getLogger(Tj_Opter_Job.class);
	@Resource
	private Tj_OpterService tj_OpterService;
	@Resource
	Tj_Hc_orderInfo_Today_Service tj_Hc_orderInfo_Today_Service;
	
	public void tj_OptToTable(){
		logger.info("tj_Opter_Job开始执行");
		List<String> opter_List = tj_OpterService.queryAllOpter();//查询所有的普通用户（启用）
		//获取当前时间的前一天 格式为yyyy-MM-dd
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);    //得到前一天
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String create_time = df.format(date);
		//得到create_time的前一天
		calendar.add(Calendar.DATE, -1);
		Date datePre = calendar.getTime();
		String pre_time =df.format(datePre);
		//循环除去机器人名称，循环所有操作人
		logger.info("create_time:"+create_time+"_pre_time:"+pre_time);
		logger.info("opter_List.size():"+opter_List.size());
		for(int j=0;j<opter_List.size();j++){
			String opter = opter_List.get(j).toString();
			logger.info("opter:opter_List.get(j):"+j);
			Map<String,Object> query_Map = new HashMap<String,Object>();
			query_Map.put("create_time", create_time);
			query_Map.put("pre_time", pre_time);
			query_Map.put("opter", opter);
			List<Map<String,String>> order_List = tj_OpterService.queryOrder_List(query_Map);//当天操作人操作的订单--去重
			int out_ticket_total=order_List.size();//本天本人出票次数（订单数）
			int refund_total_19e = tj_OpterService.queryRefund_total(query_Map);//19e退款总数
			int refund_total_qunar = tj_OpterService.queryRefund_totalQunar(query_Map);//去哪退款总数
			int refund_total_ext = tj_OpterService.queryRefund_totalExt(query_Map);//对外商户退款总数
			int refund_total_app = tj_OpterService.queryRefund_totalApp(query_Map);//app退款总数
			int refund_total_inner=tj_OpterService.queryRefund_totalInner(query_Map);//内嵌退款总数
			int refund_total_elong =tj_OpterService.queryRefund_totalElong(query_Map);//艺龙退款总数
			int refund_total_tongcheng =tj_OpterService.queryRefund_totaltongcheng(query_Map);//同程退款总数
			
			List<Map<String, String>> merchantIdList =  tj_Hc_orderInfo_Today_Service.queryMerchantId();
			List<String> merchantId_list1 = new ArrayList<String>();//渠道
			List<String> merchantId_list2 = new ArrayList<String>();//渠道
			List<String> merchantId_list3 = new ArrayList<String>();//渠道
			List<String> merchantId_list4 = new ArrayList<String>();//渠道
			List<String> merchantId_list5 = new ArrayList<String>();//渠道
			List<String> merchantId_list6 = new ArrayList<String>();//渠道
			List<String> merchantId_list7 = new ArrayList<String>();//渠道
			merchantId_list1.add("19e");
			merchantId_list2.add("qunar");
			
			merchantId_list3.add("cmpay");
			merchantId_list3.add("cmwap");
			merchantId_list3.add("19pay");
			merchantId_list3.add("ccb");
			merchantId_list3.add("chq");
			
			merchantId_list4.add("web");
			merchantId_list4.add("app");
			merchantId_list4.add("weixin");
			
			merchantId_list5.add("elong");
			merchantId_list6.add("tongcheng");
			
			for(int i=0; i<merchantIdList.size(); i++){
				String merchant_id = merchantIdList.get(i).get("merchant_id").toString();
					merchantId_list7.add(merchant_id);
			}
			query_Map.put("channel", merchantId_list1);
			int refund_19enew = tj_OpterService.queryRefundnew(query_Map);
			query_Map.put("channel", merchantId_list2);
			int refund_qunarnew = tj_OpterService.queryRefundnew(query_Map);
			query_Map.put("channel", merchantId_list7);
			int refund_extnew = tj_OpterService.queryRefundnew(query_Map);
			query_Map.put("channel", merchantId_list4);
			int refund_appnew = tj_OpterService.queryRefundnew(query_Map);
			query_Map.put("channel", merchantId_list3);
			int refund_innernew = tj_OpterService.queryRefundnew(query_Map);
			query_Map.put("channel", merchantId_list5);
			int refund_elongnew = tj_OpterService.queryRefundnew(query_Map);
			query_Map.put("channel", merchantId_list6);
			int refund_tongchengnew = tj_OpterService.queryRefundnew(query_Map);
			
			refund_total_19e +=refund_19enew;
			refund_total_qunar +=refund_qunarnew;
			refund_total_ext +=refund_extnew;
			refund_total_app +=refund_appnew;
			refund_total_inner +=refund_innernew;
			refund_total_elong +=refund_elongnew;
			refund_total_tongcheng +=refund_tongchengnew;
			
			int refund_total = refund_total_19e + refund_total_qunar  + refund_total_ext + refund_total_app+refund_total_inner+refund_total_elong+ refund_total_tongcheng;//查询本人退款的次数（退款数）
			Map<String,Object> add_Map = new HashMap<String,Object>();
			add_Map.put("tj_id", CreateIDUtil.createID("TJ"));
			add_Map.put("tj_time", create_time);
			add_Map.put("opt_person", opter);
			add_Map.put("out_ticket_total", out_ticket_total);//订单数
			add_Map.put("refund_total", refund_total);//退款数
			add_Map.put("refund_total_19e", refund_total_19e);
			add_Map.put("refund_total_qunar", refund_total_qunar);
			add_Map.put("refund_total_ext", refund_total_ext);
			add_Map.put("refund_total_app", refund_total_app);
			add_Map.put("refund_total_inner", refund_total_inner);
			add_Map.put("refund_total_elong", refund_total_elong);
			add_Map.put("refund_total_tongcheng", refund_total_tongcheng);
			logger.info("操作人"+opter
					+"订单数"+ out_ticket_total
					+"退款数"+ refund_total
					+"19e退款总数"+refund_total_19e 
					+"去哪退款总数"+ refund_total_qunar
					+"商户退款总数"+ refund_total_ext 
					+"app退款总数"+ refund_total_app
					+"内嵌退款总数"+ refund_total_inner
					+"艺龙退款总数"+ refund_total_elong
					+"同程退款总数"+ refund_total_tongcheng
					);
			try{
				tj_OpterService.addStatToTj_Opter(add_Map);
			}catch(Exception e){
				e.printStackTrace();
				logger.error("添加数据失败，"+"opter:"+opter+"order_time:"+create_time);
			}
		}
	}

}
