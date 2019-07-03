package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cqz.tc.TcCodeUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.DMService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.CodeVo;

/**
 * tongcheng打码 错误打码 上传
 * 
 * */
@Component("tcErrorSentJob")
public class TcErrorSentJob {
	private static final Logger logger = Logger.getLogger(TcErrorSentJob.class);
	
	@Resource
	private DMService dMService;

	@Resource
	private RobotCodeService robotService;

	public void errorTcReport() {
//		logger.info("tongcheng上传错误job");
		//系统设置--是否分离同城打码     是否进行同程打码 00：同程打码；11：19e打码
		String tongcheng_code_weight = robotService.querySystemValue("tongcheng_code_weight");
		
		if (!"0".equals(tongcheng_code_weight)) {
			Map<String, String> info = new HashMap<String, String>();
			try {
				info.put("opt_ren", "tongcheng");
				List<CodeVo> list = dMService.queryErrorCodeList(info);
				if (list == null || list.size() <= 0) {
					return;
				}
				/** tongcheng 上报错误验证码 开始 */
				for (CodeVo vo : list) {
					if(vo.getShyzmid()!=null){
						String resultStr = TcCodeUtil.captchaReportError(TcCodeUtil.U_NAME, TcCodeUtil.KEY, vo.getShyzmid(), "");
//						JSONObject json = JSONObject.fromObject(resultStr);
						if ("OK".equals(resultStr.toUpperCase())) {
							// 上报验证码成功
							vo.setBack_status(TrainConsts.BACK_SUCCESS);
							dMService.updateErrorInfo(vo);
//							logger.info("上报验证码 错误 成功,data:" + json.getString("data"));
						} else {
							logger.info("tongcheng验证码" + vo.getShyzmid() + "验证错误反馈tongcheng失败");
							vo.setBack_status(TrainConsts.BACK_SYS.equals(vo
											.getBack_status()) ? TrainConsts.BACK_AGIN
											: TrainConsts.BACK_AGIN.equals(vo
													.getBack_status()) ? TrainConsts.BACK_FAILL
													: TrainConsts.BACK_AGIN);
							dMService.updateErrorInfo(vo);
						}
					}
				}
			} catch (Exception e) {
				logger.error("tongcheng打码 反馈验证码验证错误 异常" + e);
				e.printStackTrace();
			}
		}
	}
}
