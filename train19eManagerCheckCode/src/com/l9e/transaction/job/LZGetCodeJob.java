package com.l9e.transaction.job;

import java.io.File;
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
 * 联众打码 获取请求结果
 * 
 * */
@Component("lZGetCodeJob")
public class LZGetCodeJob {
	private static final Logger logger = Logger.getLogger(LZGetCodeJob.class);
	@Resource
	private DMService dMService;

	@Resource
	private RobotCodeService robotService;
	public void getCode(){
		//logger.info("获取 code job");
		Map<String,String> info=robotService.querySysInfo();
		String code_channel=info.get("code_channel");
		try {
			if(TrainConsts.CODE_LZ.equals(code_channel)||TrainConsts.CODE_LZANDRG.equals(code_channel)){
				info.put("opt_ren", TrainConsts.OPTREN_NAME);
				List<CodeVo> list =dMService.queryCatchCodeList(info);
				
				if(list==null||list.size()==0){
					//logger.info("没有可以获取code的验证码数据");
					return ;
				}
				//String path=LZGetCodeJob.class.getClassLoader().getResource("").getPath();
				String domain = DMV3.getDoMainArryAndValidUser();
				for(CodeVo vo:list){
					String resultStr=DMV3.getResult(domain, vo.getShyzmid());
				/*	String readPath=path.substring(0,path.indexOf("WEB-INF")-1)+vo.getPic_filename();
					String type=vo.getPic_filename().substring(vo.getPic_filename().lastIndexOf("."), vo.getPic_filename().length());
					File f = new File(readPath);
					String fileName = MD5FileUtil.getFileMD5String(f) + ".jpg";
					
					String result = DMV2.getResult(domain, fileName);
					DMV3.getResult(domain, yzm_id);*/
					logger.info("获取联众图片"+vo.getShyzmid()+"结果result:"+resultStr);
					try {
						if(resultStr==null||resultStr.replaceAll(" ", "").length()==0){
							logger.error("联众打码 获取请求结果null result:"+resultStr);
							continue;
						}
						JSONObject json=JSONObject.fromObject(resultStr);
						if(json.getString("result").equals("true")){
							vo.setVerify_code(json.getString("data"));
							logger.info("联众打码 结果成功返回 code:["+vo.getVerify_code()+"],yzmid:"+vo.getShyzmid());
							dMService.updateCodeInfo(vo);
						}else if (json.getString("result").equals("false")) {
							logger.info("联众打码 获取Error:"+json.getString("data"));
							//DMV2.updateState(domain, fileName);
						}else{
							logger.info("联众打码 获取结果失败");
							
						}
					} catch (Exception e) {
						logger.error("联众打码 获取请求结果异常"+e);
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	class UploadPC extends Thread{
		 private CodeVo vo;
		    public UploadPC(CodeVo vo)
		    {
		        this.vo = vo;
		    }
		    public void run()
		    {
		       
		    }

	}
}
