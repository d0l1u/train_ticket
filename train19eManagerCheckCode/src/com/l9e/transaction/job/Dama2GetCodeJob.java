package com.l9e.transaction.job;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cqz.dmt.Damatu;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.DMService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.CodeVo;

/**
 * 打码兔打码 获取请求结果
 * 
 * */
@Component("dama2GetCodeJob")
public class Dama2GetCodeJob {
	private static final Logger logger = Logger.getLogger(Dama2GetCodeJob.class);
	
	@Resource
	private DMService dMService;

	@Resource
	private RobotCodeService robotService;

	public void getCode() {
//		logger.info("打码兔获取 code job");
		Map<String, String> info = robotService.querySysInfo();
		String code_channel = info.get("code_channel");
		try {
//			if (TrainConsts.CODE_DAMATU.equals(code_channel) || TrainConsts.CODE_DAMATUANDRG.equals(code_channel)) {
				info.put("opt_ren", TrainConsts.DAMATU_NAME);
				List<CodeVo> list = dMService.queryCatchCodeList(info);

				if (list == null || list.size() == 0) {
					// logger.info("没有可以获取code的验证码数据");
					return;
				}
				for (CodeVo vo : list) {
					String resultStr = Damatu.getCode(vo.getShyzmid());
					String finishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					vo.setFinish_time(finishTime);
					resultStr = axisY(resultStr);
					logger.info("获取打码兔图片" + vo.getShyzmid() + "结果result:" + resultStr);
					try {
						if (resultStr == null || resultStr.replaceAll(" ", "").length() == 0) {
							logger.error("打码兔打码 获取请求结果null result:" + resultStr);
							continue;
						}
						vo.setVerify_code(resultStr);
						logger.info("打码兔打码 结果成功返回 code:[" + vo.getVerify_code() + "],yzmid:" + vo.getShyzmid());
						dMService.updateCodeInfo(vo);
//						JSONObject json = JSONObject.fromObject(resultStr);
//						if (json.getString("result").equals("true")) {
//							vo.setVerify_code(json.getString("data"));
//							logger.info("打码兔打码 结果成功返回 code:[" + vo.getVerify_code() + "],yzmid:" + vo.getShyzmid());
//							dMService.updateCodeInfo(vo);
//						} else if (json.getString("result").equals("false")) {
//							logger.info("打码兔打码 获取Error:" + json.getString("data"));
//						} else {
//							logger.info("打码兔打码 获取结果失败");
//
//						}
					} catch (Exception e) {
						logger.error("打码兔打码 获取请求结果异常" + e);
						e.printStackTrace();
					}
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class UploadPC extends Thread {
		private CodeVo vo;

		public UploadPC(CodeVo vo) {
			this.vo = vo;
		}

		public void run() {

		}

	}
	
	public static String axisY(String resultStr){//纵坐标减30
		String[] arr = resultStr.split("\\|");
		for(int i=0; i<arr.length; i++){
			String s = arr[i];
			String[] a = s.split(",");
			a[1] = (Integer.parseInt(a[1])-30)+"";
			arr[i] = a[0]+","+a[1];
		}
		resultStr = Arrays.toString(arr).substring(1, Arrays.toString(arr).length()-1);
		resultStr = resultStr.replaceAll(" ", "");
		return resultStr;
	}
	
	public static void main(String[] args) {
		String resultStr = "195,156|258,94|195,71";
		System.out.println(axisY(resultStr));
	}
}
