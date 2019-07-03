package com.l9e.transaction.job;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cqz.dm.DMV3;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.DMService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.CodeVo;

/**
 * 联众打码 错误打码 上传
 * 
 * */
@Component("lZErrorSentJob")
public class LZErrorSentJob {
	private static final Logger logger = Logger.getLogger(LZErrorSentJob.class);
	@Resource
	private DMService dMService;

	
	@Resource
	private RobotCodeService robotService;
	public void errorUpload(){
		//logger.info("上传错误job");
		Map<String,String> info=robotService.querySysInfo();
		String code_channel=info.get("code_channel");
		if(TrainConsts.CODE_LZ.equals(code_channel)||TrainConsts.CODE_LZANDRG.equals(code_channel)){
			try {
				info.put("opt_ren", TrainConsts.OPTREN_NAME);
				List<CodeVo> list =
					dMService.queryErrorCodeList(info);
					if(list==null||list.size()<=0){
						return;
					}
					/**联众 上报错误验证码 开始*/
					String domain = DMV3.getDoMainArryAndValidUser();
					for(CodeVo vo:list){
						/*String yzmresult = DMV2.yzmErrorUpload(domain,vo.getShyzmid());
						JSONObject json=JSONObject.fromObject(yzmresult);
						String result=json.get("result")==null?"":json.get("result").toString();*/
						
						String resultStr=DMV3.yzm_error(domain, vo.getShyzmid());
						JSONObject json=JSONObject.fromObject(resultStr);
						if("true".equals(json.getString("result"))){
							//上报验证码成功
							vo.setBack_status(TrainConsts.BACK_SUCCESS);
							dMService.updateErrorInfo(vo);
							logger.info("上报验证码 错误 成功,data:"+json.getString("data"));
						}else{
							logger.info("验证码"+vo.getShyzmid()+"验证错误反馈商户失败"+"data:"+json.getString("data"));
							vo.setBack_status(TrainConsts.BACK_SYS.equals(vo.getBack_status())?TrainConsts.BACK_AGIN:
								TrainConsts.BACK_AGIN.equals(vo.getBack_status())?TrainConsts.BACK_FAILL:TrainConsts.BACK_AGIN);
							dMService.updateErrorInfo(vo);
						}
					}
			} catch (Exception e) {
				logger.error("联众打码 反馈验证码验证错误 异常"+e);
				e.printStackTrace();
			}
		}
	}
}
