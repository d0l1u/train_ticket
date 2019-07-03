package com.l9e.transaction.job;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
 * qunar打码 获取请求结果
 * 
 * */
@Component("qunarGetCodeJob")
public class QunarGetCodeJob {
	private static final Logger logger = Logger.getLogger(QunarGetCodeJob.class);
	
	@Resource
	private DMService dMService;
	@Resource
	private RobotCodeService robotService;

	@Value("#{propertiesReader[qunarQueryResultUrl]}")
	private String qunarQueryResultUrl;
	@Value("#{propertiesReader[agent_code1]}")
	private String agent_code1;
	@Value("#{propertiesReader[agent_code2]}")
	private String agent_code2;
	@Value("#{propertiesReader[secretKey1]}")
	private String secretKey1;
	@Value("#{propertiesReader[secretKey2]}")
	private String secretKey2;
	
	public void getCode() {
//		logger.info("qunar获取 code job");
		Map<String, String> info = robotService.querySysInfo();
		String code_channel = info.get("code_channel");
		try {
			info.put("opt_ren", "qunar");
			List<CodeVo> list = dMService.queryCatchCodeList(info);

			if (list == null || list.size() == 0) {
				// logger.info("没有可以获取code的验证码数据");
				return;
			}
			for (CodeVo vo : list) {
				String globalId = vo.getShyzmid();
				String agent_code = agent_code1;
				String secretKey = secretKey1;
				if(vo.getOrder_id().contains(agent_code2)){
					agent_code = agent_code2;
					secretKey = secretKey2;
				}
				String hmac = DigestUtils.md5Hex(secretKey+agent_code+globalId).toLowerCase();
				
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("globalId", globalId);
				paramMap.put("agentCode", agent_code);
				paramMap.put("hmac", hmac);
				//向qunar查询验证码结果
				String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				String jsonStr = HttpUtil.sendByPost(qunarQueryResultUrl, params, "utf-8");
				logger.info("【获取验证码图片结果返回】"+jsonStr);
				
				ObjectMapper mapper = new ObjectMapper();
				QunarCodeReturn rs = mapper.readValue(jsonStr, QunarCodeReturn.class);
				
				if(rs.isRet()){//请求是否成功，true成功，false失败
					Map<String, String> data = rs.getData();
					String state = data.get("state");
					if("1".equals(state)){//state：0请求受理失败 state：1请求受理成功
						String result = data.get("result");//result：state为1时，返回的验证码结果
						String finishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						vo.setFinish_time(finishTime);
						vo.setVerify_code(result);
//							logger.info("qunar打码 结果成功返回 code:[" + vo.getVerify_code() + "],yzmid:" + vo.getShyzmid());
						dMService.updateCodeInfo(vo);
					}else if("-1".equals(state)){//State：-1请求处理失败，请重新上传图片
						Map<String,String> map = new HashMap<String,String>();
						map.put("fail_reason", data.get("errMsg"));
						map.put("status", "00");
						map.put("pic_id", vo.getPic_id());
						logger.info(map.toString());
						robotService.updatePicturesMap(map);
					}
				}else{
					logger.error("qunar返回打码异常原因为："+jsonStr);
					Map<String,String> map = new HashMap<String,String>();
					map.put("fail_reason", rs.getErrcode()+":"+rs.getErrmsg());
					map.put("status", "00");
					map.put("pic_id", vo.getPic_id());
					logger.info(map.toString());
					robotService.updatePicturesMap(map);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
