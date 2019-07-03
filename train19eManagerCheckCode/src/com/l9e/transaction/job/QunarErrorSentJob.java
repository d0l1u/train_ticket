package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cqz.dmt.Damatu;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.DMService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.CodeVo;
import com.l9e.transaction.vo.QunarCodeReturn;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * qunar返回验证码结果
 * 
 * */
@Component("qunarErrorSentJob")
public class QunarErrorSentJob {
	private static final Logger logger = Logger.getLogger(QunarErrorSentJob.class);
	
	@Resource
	private DMService dMService;
	@Resource
	private RobotCodeService robotService;
	
	@Value("#{propertiesReader[qunarFeedbackUrl]}")
	private String qunarFeedbackUrl;
	@Value("#{propertiesReader[agent_code1]}")
	private String agent_code1;
	@Value("#{propertiesReader[agent_code2]}")
	private String agent_code2;
	@Value("#{propertiesReader[secretKey1]}")
	private String secretKey1;
	@Value("#{propertiesReader[secretKey2]}")
	private String secretKey2;

	public void errorReport() {
//		logger.info("qunar上传错误job");
		Map<String, String> info = robotService.querySysInfo();
		String code_channel = info.get("code_channel");
		try {
			info.put("opt_ren", "qunar");
//			List<CodeVo> list = dMService.queryErrorCodeList(info);
			List<CodeVo> list = robotService.queryFeedBackResultList(info);
			if (list == null || list.size() <= 0) {
				return;
			}
			/** qunar 上报错误验证码 开始 */
			for (CodeVo vo : list) {
				String globalId = vo.getShyzmid();
				String agent_code = agent_code1;
				String secretKey = secretKey1;
				if(vo.getOrder_id().contains(agent_code2)){
					agent_code = agent_code2;
					secretKey = secretKey2;
				}
				String isRight = "true";
				if("0".equals(vo.getResult_status())){//0 验证失败， 1验证成功
					isRight = "false";
				}else if("1".equals(vo.getResult_status())){
					isRight = "true";
				}
				String hmac = DigestUtils.md5Hex(secretKey+agent_code+globalId+isRight).toLowerCase();
				
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("globalId", globalId);
				paramMap.put("agentCode", agent_code);
				paramMap.put("isRight", isRight);
				paramMap.put("hmac", hmac);
				//向qunar查询验证码结果
				String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				String jsonStr = HttpUtil.sendByPost(qunarFeedbackUrl, params, "utf-8");
				logger.info("【报错验证码图片结果返回】"+jsonStr);
				
				ObjectMapper mapper = new ObjectMapper();
				QunarCodeReturn rs = mapper.readValue(jsonStr, QunarCodeReturn.class);
				
				if(rs.isRet()){//请求是否成功，true成功，false失败
					// 上报验证码成功
					vo.setBack_status(TrainConsts.BACK_SUCCESS);
					dMService.updateErrorInfo(vo);
				}else{
					logger.info("验证码" + vo.getShyzmid() + "验证错误反馈商户失败："+rs.getErrcode()+"/"+rs.getErrmsg());
					vo.setBack_status(TrainConsts.BACK_SYS.equals(vo.getBack_status()) ? TrainConsts.BACK_AGIN
									: TrainConsts.BACK_AGIN.equals(vo.getBack_status()) ? TrainConsts.BACK_FAILL
											: TrainConsts.BACK_AGIN);
					dMService.updateErrorInfo(vo);
				}
				
			}
		} catch (Exception e) {
			logger.error("qunar打码反馈验证码验证错误 异常" + e);
			e.printStackTrace();
		}
	}
}
