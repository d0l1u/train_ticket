package com.l9e.util.elong;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.l9e.transaction.vo.ElongNoticeVo;
import com.l9e.transaction.vo.ElongOrderInfoCp;

/**针对elong 的json处理*/
public class ElongJsonUtil {
	public static String getString(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	public static String getJaonStr(List<ElongOrderInfoCp> list,String orderId){
		JSONObject cpInfo=new JSONObject();
		//cpInfo.put("ticketNo", elongNoticeVo.getOut_ticket_billno());
		cpInfo.put("orderId", orderId);
		String out_ticket_billno="";
		JSONArray arr=new JSONArray();
		for(ElongOrderInfoCp cp:list){
			JSONObject json=new JSONObject();
			json.put("orderItemId", cp.getCp_id());
			json.put("seatType", cp.getElong_seat_type());
			json.put("seatNo", cp.getSeat_no());
			json.put("price", cp.getPay_money());
			json.put("passengerName", cp.getUser_name());
			json.put("certNo", cp.getUser_ids());
			json.put("ticketType", cp.getElong_ticket_type());
			out_ticket_billno=cp.getOut_ticket_billno();
			arr.add(json);
		}
		cpInfo.put("ticketNo", out_ticket_billno);
		cpInfo.put("tickets", arr);
		return cpInfo.toString();
	}
}
