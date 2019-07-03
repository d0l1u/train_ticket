package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.LoginService;
import com.l9e.transaction.service.TjPicService;
import com.l9e.transaction.vo.CodeRateVo;
import com.l9e.util.DateUtil;

@Component("tjCodeRateJob")
public class TjCodeRateJob {
	private static final Logger logger = Logger.getLogger(TjCodeRateJob.class);

	@Resource
	private TjPicService tjPicService;

	@Resource
	private LoginService loginService;
	
	public void queryCodeRate() {
		Date now = new Date();
		String datePre = DateUtil.dateToString(now, "yyyyMMdd");		
		Date begin1 = DateUtil.stringToDate(datePre + "064500", "yyyyMMddHHmmss");//6:45
		Date end1 = DateUtil.stringToDate(datePre + "231500", "yyyyMMddHHmmss");//23:15
		if(now.before(begin1) || now.after(end1)){
			logger.info("tjCodeRateJob自动执行JOB，23:15~6:45之间，不执行");
			return;
		}
		
//		String begin = "2015-05-18 14";
//		String end = "2015-05-18 15";
		logger.info("tjCodeRateJob自动执行JOB，开始往tj_code_rate表中添加数据");
		
		String begin = DateUtil.dateToString(now,DateUtil.DATE_FMT4);
		String end = DateUtil.dateToString(DateUtil.dateAddHour(now, 1),DateUtil.DATE_FMT4);
		
		String day_stat = DateUtil.dateToString(now,DateUtil.DATE_FMT1);
		String hour_stat = DateUtil.dateToString(now, "HH");
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("begin", begin);
		param.put("end", end);
		
		List<CodeRateVo> nameList = tjPicService.queryCodeUserList(param);
		
		for(CodeRateVo codeVo : nameList){
			String username = codeVo.getUsername();//用户名
			//String code_count = codeVo.getCode_count();//打码总数
			String code_get_time = codeVo.getCode_get_time();//拉码平均时长
			String code_time = codeVo.getCode_time();//打码平均时长
			
			Map<String,String> map=new HashMap<String,String>();
			map.put("begin", begin);
			map.put("end", end);
			map.put("day_stat", day_stat);
			map.put("hour_stat", hour_stat);
			map.put("username", username);
			map.put("code_get_time", code_get_time);
			map.put("code_time", code_time);
			
			String code_count = tjPicService.queryCodeCount(map);//打码总数
			String code_success = tjPicService.queryCodeSuccess(map);//打码成功数
			String code_fail = tjPicService.queryCodeFail(map);//打码失败数
			String code_over = tjPicService.queryCodeOver(map);//打码超时数
			
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");  
			String code_success_rate = "";//打码成功率
			if(Integer.parseInt(code_count) != 0){
				code_success_rate =df.format(Double.parseDouble(code_success)/Double.parseDouble(code_count)*100)+"%".toString();
			}else{
				code_success_rate = "0.00%";
			}
			
			String department = loginService.queryAdminDepartment(username);
			
			map.put("code_count", code_count);
			map.put("code_success", code_success);
			map.put("code_fail", code_fail);
			map.put("code_over", code_over);
			map.put("code_success_rate", code_success_rate);
			map.put("department", department);
			System.out.println(map);
			int todayCount = tjPicService.queryCodeHourCount(map);
			if (todayCount == 0) {
				tjPicService.addTotj_code_rate(map);
			} else {
				tjPicService.updateTotj_code_rate(map);
			}
		}
		logger.info("tjCodeRateJob自动执行JOB完毕");
	}

}
