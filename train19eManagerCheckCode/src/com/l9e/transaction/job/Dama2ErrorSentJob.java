package com.l9e.transaction.job;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cqz.dmt.Damatu;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.DMService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.CodeVo;

/**
 * 打码兔打码 错误打码 上传
 * 
 * */
@Component("dama2ErrorSentJob")
public class Dama2ErrorSentJob {
	private static final Logger logger = Logger.getLogger(Dama2ErrorSentJob.class);
	
	@Resource
	private DMService dMService;

	@Resource
	private RobotCodeService robotService;

	public void errorReport() {
//		logger.info("打码兔上传错误job");
		Map<String, String> info = robotService.querySysInfo();
		String code_channel = info.get("code_channel");
//		if (TrainConsts.CODE_DAMATU.equals(code_channel) || TrainConsts.CODE_DAMATUANDRG.equals(code_channel)) {
			try {
				info.put("opt_ren", TrainConsts.DAMATU_NAME);
				List<CodeVo> list = dMService.queryErrorCodeList(info);
				if (list == null || list.size() <= 0) {
					return;
				}
				/** 打码兔 上报错误验证码 开始 */
				for (CodeVo vo : list) {
					String resultStr = Damatu.reportError(vo.getShyzmid());
//					JSONObject json = JSONObject.fromObject(resultStr);
					if ("true".equals(resultStr)) {
						// 上报验证码成功
						vo.setBack_status(TrainConsts.BACK_SUCCESS);
						dMService.updateErrorInfo(vo);
//						logger.info("上报验证码 错误 成功,data:" + json.getString("data"));
					} else {
						logger.info("验证码" + vo.getShyzmid() + "验证错误反馈商户失败");
						vo.setBack_status(TrainConsts.BACK_SYS.equals(vo
										.getBack_status()) ? TrainConsts.BACK_AGIN
										: TrainConsts.BACK_AGIN.equals(vo
												.getBack_status()) ? TrainConsts.BACK_FAILL
												: TrainConsts.BACK_AGIN);
						dMService.updateErrorInfo(vo);
					}
				}
			} catch (Exception e) {
				logger.error("打码兔打码 反馈验证码验证错误 异常" + e);
				e.printStackTrace();
			}
//		}
	}
}
