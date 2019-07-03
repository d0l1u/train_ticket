package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.util.DateUtil;
import com.l9e.util.DbContextHolder;
import com.l9e.util.SwitchUtils;

/**
 * 根据cp_match表来统计白名单匹配数据，加入tj_match表
 */
@Component("tj_Match_Job")
public class Tj_Match_Job {

	private static final Logger logger = Logger.getLogger(Tj_Match_Job.class);
	
	@Resource
	Tj_OpterService tj_OpterService;
	
	public void queryMatchJob() {
		String logPre = "[tj_match]";
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String tj_date = df.format(date);//当前时间
		logger.info(logPre+"自动执行JOB start，统计时间为："+tj_date);
		
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//查询传入证件总数param_count、匹配证件总数match_count、channel、tj_date
		paramMap.put("tj_date", tj_date);
		List<Map<String, Object>> matchList = tj_OpterService.queryMatchList(paramMap);
		
		Map<String, Object> addMap = null;
		for(Map<String, Object> matchMap : matchList){
			addMap = new HashMap<String, Object>();
			addMap.put("tj_date", tj_date);//统计日期
			addMap.put("create_time", "now()");//创建时间
			addMap.put("channel", matchMap.get("channel"));//渠道
			int query_count = Integer.parseInt(matchMap.get("query_count").toString());
			int param_sum = Integer.parseInt(matchMap.get("param_sum").toString());
			int match_sum = Integer.parseInt(matchMap.get("match_sum").toString());
			addMap.put("query_count", query_count);//该渠道总查询次数
			addMap.put("param_sum", param_sum);//传入证件总数
			addMap.put("match_sum", match_sum);//匹配证件总数
			
			String match_ratio = null;//匹配证件比率(数据库字段)
			double match_ratio_bl;
			if (param_sum != 0) {
				match_ratio_bl = ((double) match_sum / (double) param_sum);
				match_ratio = SwitchUtils.format_perCent(match_ratio_bl);// doble 转换为 String类型的“**.**%”
			} else {
				match_ratio = "0.00%"; // 出票成功率
			}
			addMap.put("match_ratio", match_ratio);//匹配证件比率
			
			
			paramMap.put("channel", matchMap.get("channel"));
			/**************** 00完全匹配 ****************/
			paramMap.put("match_status", "00");//00完全匹配
			Map<String, Object> allMap = tj_OpterService.queryStatusMatch(paramMap);
			int all_count = Integer.parseInt(allMap.get("query_count").toString());
			int all_sum = Integer.parseInt(allMap.get("match_sum").toString());
			String all_ratio = null, all_qratio = null;
			double all_ratio_bl, all_qratio_bl;
			if (param_sum != 0) {
				all_ratio_bl = ((double) all_sum / (double) param_sum);
				all_ratio = SwitchUtils.format_perCent(all_ratio_bl);// doble 转换为 String类型的“**.**%”
			} else {
				all_ratio = "0.00%"; // 出票成功率
			}
			if (query_count != 0) {
				all_qratio_bl = ((double) all_count / (double) query_count);
				all_qratio = SwitchUtils.format_perCent(all_qratio_bl);// doble 转换为 String类型的“**.**%”
			} else {
				all_qratio = "0.00%"; // 出票成功率
			}
			addMap.put("all_count", all_count);//完全匹配总查询次数
			addMap.put("all_qratio", all_qratio);//完全匹配总查询次数比率
			addMap.put("all_sum", all_sum);//完全匹配证件总数
			addMap.put("all_ratio", all_ratio);//完全匹配证件比率

			
			/**************** 11部分匹配(可添加剩余证件) ****************/
			paramMap.put("match_status", "11");//11部分匹配(可添加剩余证件)
			Map<String, Object> partOkMap = tj_OpterService.queryStatusMatch(paramMap);
			int partOk_count = Integer.parseInt(partOkMap.get("query_count").toString());
			int partOk_sum = Integer.parseInt(partOkMap.get("match_sum").toString());
			String partOk_ratio = null, partOk_qratio = null;
			double partOk_ratio_bl, partOk_qratio_bl;
			if (param_sum != 0) {
				partOk_ratio_bl = ((double) partOk_sum / (double) param_sum);
				partOk_ratio = SwitchUtils.format_perCent(partOk_ratio_bl);// doble 转换为 String类型的“**.**%”
			} else {
				partOk_ratio = "0.00%"; 
			}
			if (query_count != 0) {
				partOk_qratio_bl = ((double) partOk_count / (double) query_count);
				partOk_qratio = SwitchUtils.format_perCent(partOk_qratio_bl);// doble 转换为 String类型的“**.**%”
			} else {
				partOk_qratio = "0.00%"; 
			}
			addMap.put("partOk_count", partOk_count);//部分匹配（可添加剩余证件）总查询次数
			addMap.put("partOk_qratio", partOk_qratio);//部分匹配（可添加剩余证件）总查询次数比率
			addMap.put("partOk_sum", partOk_sum);//部分匹配（可添加剩余证件）证件总数
			addMap.put("partOk_ratio", partOk_ratio);//部分匹配（可添加剩余证件）比率
			
			
			/**************** 22部分匹配(不可添加剩余证件)  ****************/
			paramMap.put("match_status", "22");//22部分匹配(不可添加剩余证件) 
			Map<String, Object> partNoMap = tj_OpterService.queryStatusMatch(paramMap);
			int partNo_count = Integer.parseInt(partNoMap.get("query_count").toString());
			int partNo_sum = Integer.parseInt(partNoMap.get("match_sum").toString());
			String partNo_ratio = null, partNo_qratio = null;
			double partNo_ratio_bl, partNo_qratio_bl;
			if (param_sum != 0) {
				partNo_ratio_bl = ((double) partNo_sum / (double) param_sum);
				partNo_ratio = SwitchUtils.format_perCent(partNo_ratio_bl);// doble 转换为 String类型的“**.**%”
			} else {
				partNo_ratio = "0.00%"; 
			}
			if (query_count != 0) {
				partNo_qratio_bl = ((double) partNo_count / (double) query_count);
				partNo_qratio = SwitchUtils.format_perCent(partNo_qratio_bl);// doble 转换为 String类型的“**.**%”
			} else {
				partNo_qratio = "0.00%"; 
			}
			addMap.put("partNo_count", partNo_count);//部分匹配（不可添加剩余证件）总查询次数
			addMap.put("partNo_qratio", partNo_qratio);//部分匹配（不可添加剩余证件）总查询次数比率
			addMap.put("partNo_sum", partNo_sum);//部分匹配（不可添加剩余证件）证件总数
			addMap.put("partNo_ratio", partNo_ratio);//部分匹配（不可添加剩余证件）比率
			
			
			/**************** 33不匹配  ****************/
			paramMap.put("match_status", "33");//33不匹配
			Map<String, Object> unmatchMap = tj_OpterService.queryStatusMatch(paramMap);
			int unmatch_count = Integer.parseInt(unmatchMap.get("query_count").toString());
			int unmatch_sum = Integer.parseInt(unmatchMap.get("match_sum").toString());
			String unmatch_ratio = null, unmatch_qratio = null;
			double unmatch_ratio_bl, unmatch_qratio_bl;
			if (param_sum != 0) {
				unmatch_ratio_bl = ((double) unmatch_sum / (double) param_sum);
				unmatch_ratio = SwitchUtils.format_perCent(unmatch_ratio_bl);// doble 转换为 String类型的“**.**%”
			} else {
				unmatch_ratio = "0.00%"; 
			}
			if (query_count != 0) {
				unmatch_qratio_bl = ((double) unmatch_count / (double) query_count);
				unmatch_qratio = SwitchUtils.format_perCent(unmatch_qratio_bl);// doble 转换为 String类型的“**.**%”
			} else {
				unmatch_qratio = "0.00%"; 
			}
			addMap.put("unmatch_count", unmatch_count);//不匹配证件总查询次数
			addMap.put("unmatch_qratio", unmatch_qratio);//不匹配总查询次数比率
			addMap.put("unmatch_sum", unmatch_sum);//不匹配证件总数
			addMap.put("unmatch_ratio", unmatch_ratio);//不匹配证件比率
			
			
			DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
			tj_OpterService.insertTjMatch(addMap);
			logger.info(logPre+"插入1条数据成功");
		}
		
		logger.info(logPre+"自动执行JOB end~~~");
	}
}