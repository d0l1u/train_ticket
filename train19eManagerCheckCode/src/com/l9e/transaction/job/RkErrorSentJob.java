package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cqz.code.RuoKuaiUtil;
import com.cqz.code.YunsuUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.DMService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.CodeVo;

/**
 * ruokuai打码 错误打码 上传
 * 
 * */
@Component("rkErrorSentJob")
public class RkErrorSentJob {
	private static final Logger logger = Logger.getLogger(RkErrorSentJob.class);
	
	@Resource
	private DMService dMService;

	@Resource
	private RobotCodeService robotService;

	public void errorRkReport() {
		logger.info("[ruokuai/yunsu] report error code job start");
		//系统设置--是否分离ruokuai打码     是否进行若快打码 0：ruokuai不打码
		String code_rk_weight = robotService.querySystemValue("code_book_rk_weight");
		String code_ys_weight = robotService.querySystemValue("code_book_ys_weight");
		if (Integer.parseInt(code_rk_weight)>0) {
			Map<String, String> info = new HashMap<String, String>();
			try {
				info.put("opt_ren", "ruokuai");
				List<CodeVo> list = dMService.queryErrorCodeList(info);
				if (list != null && list.size() > 0) {
					/** ruokuai 上报错误验证码 开始 */
					for (CodeVo vo : list) {
						if(vo.getShyzmid()!=null){
							String resultStr = RuoKuaiUtil.sendError(vo.getShyzmid());
//							JSONObject json = JSONObject.fromObject(resultStr);
							//正确：{"Result":"报错成功/报错成功2"}
							//错误：{"Error":"错误提示信息","Error_Code":"错误代码（详情见错误代码表）","Request":""}
							if (resultStr.contains("Result")) {
								// 上报验证码成功
								vo.setBack_status(TrainConsts.BACK_SUCCESS);
								dMService.updateErrorInfo(vo);
								logger.info("[ruokuai]上报验证码 错误 成功,resultStr:" + resultStr);
							} else {
								logger.info("[ruokuai]" + vo.getShyzmid() + "验证错误反馈ruokuai失败,resultStr="+resultStr);
								vo.setBack_status(TrainConsts.BACK_SYS.equals(vo
									.getBack_status()) ? TrainConsts.BACK_AGIN: TrainConsts.BACK_AGIN.equals(vo
									.getBack_status()) ? TrainConsts.BACK_FAILL: TrainConsts.BACK_AGIN);
								dMService.updateErrorInfo(vo);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("[ruokuai]反馈验证码验证错误 异常" + e);
				e.printStackTrace();
			}
		}
		
		
		if (Integer.parseInt(code_ys_weight)>0) {
			Map<String, String> info = new HashMap<String, String>();
			try {
				info.put("opt_ren", "yunsu");
				List<CodeVo> list = dMService.queryErrorCodeList(info);
				if (list != null && list.size() > 0) {
					/** yunsu 上报错误验证码 开始 */
					for (CodeVo vo : list) {
						if(vo.getShyzmid()!=null){
							String resultStr = YunsuUtil.report(vo.getShyzmid());
//							JSONObject json = JSONObject.fromObject(resultStr);
							//正确：{"Result":"报错成功/报错成功2"}{"Result":"报告成功2."}
							//错误：{"Error":"错误提示信息","Error_Code":"错误代码（详情见错误代码表）","Request":""}
							if (resultStr.contains("Result")) {
								// 上报验证码成功
								vo.setBack_status(TrainConsts.BACK_SUCCESS);
								dMService.updateErrorInfo(vo);
								logger.info("[yunsu]上报验证码 错误 成功,resultStr:" + resultStr);
							} else {
								logger.info("[yunsu]" + vo.getShyzmid() + "错误反馈yunsu失败,resultStr="+resultStr);
								vo.setBack_status(TrainConsts.BACK_SYS.equals(vo
									.getBack_status()) ? TrainConsts.BACK_AGIN: TrainConsts.BACK_AGIN.equals(vo
									.getBack_status()) ? TrainConsts.BACK_FAILL: TrainConsts.BACK_AGIN);
								dMService.updateErrorInfo(vo);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("[yunsu]反馈验证码验证错误 异常" + e);
				e.printStackTrace();
			}
		}
	}
}
