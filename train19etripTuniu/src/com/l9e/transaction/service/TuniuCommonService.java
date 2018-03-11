package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Station;

/**
 * 途牛基本功能接口
 * @author licheng
 *
 */
public interface TuniuCommonService {
	
	/*通知状态*/
	/**
	 * 准备通知
	 */
	String NOTIFY_PREPARED = "00";
	/**
	 * 开始通知
	 */
	String NOTIFY_START = "11";
	/**
	 * 完成通知
	 */
	String NOTIFY_SUCCESS = "22";
	/**
	 * 通知失败
	 */
	String NOTIFY_FAILURE = "33";
	
	/**
	 * 改签开始通知
	 */
	String CHANGE_NOTIFY_START = "111";
	/**
	 * 改签完成通知
	 */
	String CHANGE_NOTIFY_SUCCESS = "222";
	/**
	 *改签 通知失败
	 */
	String CHANGE_NOTIFY_FAILURE = "333";
	
	/**
	 * 参数异常
	 */
	String RETURN_CODE_PARAM_ERROR = "231008";
	/**
	 * 请求成功
	 */
	String RETURN_CODE_SUCCESS = "231000";
	/**
	 * 时间戳异常
	 */
	String RETURN_CODE_TIMESTAMP_ERROR = "231006";
	/**
	 * 签名异常
	 */
	String RETURN_CODE_SIGNATURE_ERROR = "231007";
	/**
	 * 请求用户不存在
	 */
	String RETURN_CODE_USER_NOT_EXISTS = "231001";
	/**
	 * 接口不存在
	 */
	String RETURN_CODE_INTER_NOT_EXISTS = "231009";
	/**
	 * 未知异常
	 */
	String RETURN_CODE_UNKNOWN_ERROR = "231099";
	/**
	 * 没有该接口访问权限
	 */
	String RETURN_CODE_NO_PERMISSION = "231002";

	/**
	 * 检查时间戳
	 * @param timestamp
	 * @return
	 */
	boolean validateTimestamp(String timestamp);
	/**
	 * 生成签名
	 * @param dataBase64
	 * @param key
	 * @return
	 */
	String generateSign(Map<String, String> params, String signKey);
	/**
	 * 根据返回code获取errorMsg
	 * @param code
	 * @return
	 */
	String getErrorMessage(String code);
	
	/**
	 * 转换车票类型
	 * @param ticketType
	 * @return
	 */
	String sysTicketType(String ticketType);
	/**
	 * 转换证件类型
	 * @param idsType
	 * @return
	 */
	String sysIdsType(String idsType);
	/**
	 * 转换坐席类型
	 * @param seatType
	 * @return
	 */
	String sysSeatType(String seatType);
	
	/**
	 * 途牛车票类型名称
	 * @param tuniuTicketTypeId
	 * @return
	 */
	String tuniuTicketName(String tuniuTicketTypeId);
	/**
	 * 途牛坐席类型名称
	 * @param tuniuSeatTypeId
	 * @return
	 */
	String tuniuSeatName(String tuniuSeatTypeId);
	/**
	 * 途牛证件类型名称
	 * @param tuniuIdsTypeId
	 * @return
	 */
	String tuniuIdsName(String tuniuIdsTypeId);
	
	/**
	 * 查询经停站
	 * @param trainCode
	 * @return
	 */
	List<Station> queryStations(String trainCode); 
	
}
