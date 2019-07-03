package com.l9e.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookInfo;

/**
 * 针对hcp对外接口
 * 参数校验工具类
 * @author liuyi02
 * **/
public class ParamCheckUtil {
	
	public static String DATA_REGEX="^[0-9]{4}-[0-1][0-9]-[0-3][0-9]$";//乘车日期，如2014-01-12
	
	public static String TIME_REGEX="^[0-2][0-9]:[0-6][0-9]$";//乘车日期，如14:02
	
	public static String ZHONGWEN_REGEX="^[\u4e00-\u9fa5]{1,10}$";//车站中文名匹配
	
	public static String TIMESTAMP_REGEX="^[0-9]{14}$";//时间戳校验
	
	private static String[] PARAM_STATUS={"0","1"};//order_level、bx_invoice、sms_notify、wz_ext状态码 
	
	private static String[] SEAT_TYPE={"0","1","2","3","4","5","6","7","8","9"};//seat_type状态码 
	
	private static String[] IDS_TYPE={"0","1","2","3","4","5"};//seat_type状态码 
	
	public static String PRICE_TYPE="^[0-9]{1,9}(.[0-9]{0,2})?$";//价格匹配 000.00 
	/**检测未通过正则匹配规则*/
	public static boolean isNotCheck(String param,String regex){
		 Pattern pattern = Pattern.compile(regex); 
         Matcher match = pattern.matcher(param);                
         return !match.matches(); 
	}
	
	
	/**检测通过正则匹配规则*/
	private static boolean isCheck(String param,String regex){
		 Pattern pattern = Pattern.compile(regex); 
         Matcher match = pattern.matcher(param);                
         return match.matches(); 
	}
	
	/**检测下单接口参数格式正确性*/
	public static boolean createOrderParamCheck(BookInfo bookInfo){
		if(isInStatusArray(bookInfo.getOrder_level(),PARAM_STATUS)
				&&isInStatusArray(bookInfo.getSms_notify(),PARAM_STATUS)
				&&isInStatusArray(bookInfo.getWz_ext(),PARAM_STATUS)
				&&isInStatusArray(bookInfo.getSeat_type(), SEAT_TYPE)
//				&&isInStatusArray(bookInfo.getBx_invoice(), PARAM_STATUS)
				&&isCheck(bookInfo.getArrive_station(), ZHONGWEN_REGEX)
				&&isCheck(bookInfo.getFrom_station(), ZHONGWEN_REGEX)
				&&isCheck(bookInfo.getArrive_time(), TIME_REGEX)
				&&isCheck(bookInfo.getFrom_time(), TIME_REGEX)
				&&isCheck(bookInfo.getTravel_time(), DATA_REGEX)
				&&isCheck(bookInfo.getTicket_price(), PRICE_TYPE)
				&&isCheck(bookInfo.getSum_amount(), PRICE_TYPE)
		){
			return false;
		}
		return true;//检测不通过
	}
	/**检测下单接口订单内乘客信息正确性*/
	public static boolean bookDetailInfoCheck(List<BookDetailInfo> book_detail_list,String order_level){
		int size=book_detail_list.size();
		for(int i=0;i<size;i++){
			BookDetailInfo bookDetailInfo=book_detail_list.get(i);
			if(isEmpty(bookDetailInfo.getBx())||
					isEmpty(bookDetailInfo.getIds_type())||
					isEmpty(bookDetailInfo.getTicket_type())||
					isEmpty(bookDetailInfo.getUser_ids())||
					isEmpty(bookDetailInfo.getUser_name())){
				return true;// 参数为空
			}
			if(!isInStatusArray(bookDetailInfo.getBx(),PARAM_STATUS)||
					!isInStatusArray(bookDetailInfo.getTicket_type(),PARAM_STATUS)||
					!isInStatusArray(bookDetailInfo.getIds_type(),IDS_TYPE)){
				return true;// 不合法的状态码
			}
			if(!order_level.equals(bookDetailInfo.getBx())){
				return true;//订单等级 和订单内乘客保险信息不一致
			}
		}
		return false;
	}
	
	/**检测下单接口参数是否为null*/
	public static boolean createOrderParamIsEmpty(BookInfo bookInfo){
		if(isEmpty(bookInfo.getMerchant_order_id())||
				isEmpty(bookInfo.getOrder_level())||
				isEmpty(bookInfo.getOrder_result_url())||
				isEmpty(bookInfo.getArrive_station())||
				isEmpty(bookInfo.getArrive_time())||
//				isEmpty(bookInfo.getBx_invoice())||
				isEmpty(bookInfo.getFrom_station())||
				isEmpty(bookInfo.getFrom_time())||
				isEmpty(bookInfo.getSeat_type())||
				isEmpty(bookInfo.getSms_notify())||
				isEmpty(bookInfo.getSum_amount())||
				isEmpty(bookInfo.getTicket_price())||
				isEmpty(bookInfo.getTrain_code())||
				isEmpty(bookInfo.getTravel_time())||
				isEmpty(bookInfo.getWz_ext())||bookInfo.getBook_detail_list()==null||bookInfo.getBook_detail_list().size()<=0){
			return true;
		}else{
			if("1".equals(bookInfo.getBx_invoice())){
				if(isEmpty(bookInfo.getBx_invoice_address())||
						isEmpty(bookInfo.getBx_invoice_phone())||
						isEmpty(bookInfo.getBx_invoice_receiver())||
						isEmpty(bookInfo.getBx_invoice_zipcode())){
					return true;
				}
			}
			if("1".equals(bookInfo.getSms_notify())){
				if(isEmpty(bookInfo.getLink_name())||
						isEmpty(bookInfo.getLink_phone())){
					return true;
				}
			}
			return false;
		}
	}
	/**检验 null和空字符串*/
	private static boolean isEmpty(String str){
		if(str==null||str.equals("")){
			return true;
		}else{
			return false;
		}
	}
	/**检验合法状态码*/
	private static boolean isInStatusArray(String param,String[] status){
		for(int i=0;i<status.length;i++){
			//param.trim().equals(status[i])
			if(param.equals(status[i])){
				return true;
			}
		}
		return false;
	}
	
	/**测试*/
	public static void main(String[] args) {
	}
}
