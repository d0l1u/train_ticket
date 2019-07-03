package com.l9e.transaction.vo;
/**
 * 订单日志表
 * */
public class ElongOrderLogsVo {
	private String log_id ;  
	private String order_id  ;    
	private String content ;      // 操作日志      
	private String create_time ;  // 记录时间      
	private String opt_person;   // 操作人         
	private String order_time ;
	public ElongOrderLogsVo(){
		
	}
	public ElongOrderLogsVo(String order_id,String content,String opt_person){
		this.order_id=order_id;
		this.content=content;
		this.opt_person=opt_person;
	}
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String logId) {
		log_id = logId;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getOpt_person() {
		return opt_person;
	}
	public void setOpt_person(String optPerson) {
		opt_person = optPerson;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String orderTime) {
		order_time = orderTime;
	}


}
