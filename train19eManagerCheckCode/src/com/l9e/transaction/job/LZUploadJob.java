package com.l9e.transaction.job;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cqz.dm.DMV3;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.DMService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.CodeVo;
/**
 * 联众打码 上传图片job  并且获取结果job
 * 
 * */
@Component("lZUploadJob")
public class LZUploadJob {
	private static final Logger logger = Logger.getLogger(LZUploadJob.class);
	@Resource
	private DMService dMService;

	@Resource
	private RobotCodeService robotService;
	/*public void upload(){
		//logger.info("联众打码 上传图片job");
		Map<String,String> info=robotService.querySysInfo();
		String code_channel=info.get("code_channel");
		if(TrainConsts.CODE_LZ.equals(code_channel)||TrainConsts.CODE_LZANDRG.equals(code_channel)){
			List<CodeVo> list =
				dMService.updateQuerySendCodeList(info);
			if(list==null){
				return;
			}
			try {
				*//**联众 上传图片开始*//*
				String domain = DMV3.getDoMainArryAndValidUser();
				String path=LZUploadJob.class.getClassLoader().getResource("").getPath();
				for(CodeVo vo:list){
					String readPath=path.substring(0,path.indexOf("WEB-INF")-1)+vo.getPic_filename();
					try {
						logger.info("readPath"+readPath);
						File f = new File(readPath);
						
						String fileName = MD5FileUtil.getFileMD5String(f) + ".GIF";
						//String fileName = MD5FileUtil.getFileMD5String(f) + type.toLowerCase();
						String yzm_id =DMV3.add(domain, f);
						if(yzm_id!=null){
							//更新本地图片 占用状态
							vo.setOpt_ren(TrainConsts.OPTREN_NAME);
							vo.setUpdate_status(TrainConsts.UPDATE_SUCCESS);
							vo.setShyzmid(yzm_id);
							dMService.updateUploadStatus(vo);
							logger.info("上传图片"+fileName+"成功,联众返回yzm_id:"+yzm_id);
						}else{
							//还原图片占用状态
							vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
							dMService.updateCodeUserPicStatus(vo);
							logger.error("联众打码 上传图片失败! ");
						}
					} catch (Exception e) {
						logger.info("图片"+vo.getPic_id()+"上传异常"+e);
						//还原图片占用状态
						vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
						dMService.updateCodeUserPicStatus(vo);
						
						e.printStackTrace();
					}
				}
			}catch (Exception e) {
				logger.info("联众打码 上传图片异常");
				for(CodeVo vo:list){
					vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
					dMService.updateCodeUserPicStatus(vo);
				}
				e.printStackTrace();
			}
		}
	}*/
	
	public void upload(){
		//logger.info("联众打码 上传图片job");
		Map<String,String> info=robotService.querySysInfo();
		String code_channel=info.get("code_channel");
		if(TrainConsts.CODE_LZ.equals(code_channel)||TrainConsts.CODE_LZANDRG.equals(code_channel)){
			List<CodeVo> list =
				dMService.updateQuerySendCodeList(info);
			if(list==null){
				return;
			}
			try {
				//**联众 上传图片开始*//*
				//String domain = DMV3.getDoMainArryAndValidUser();
				String path=LZUploadJob.class.getClassLoader().getResource("").getPath();
				for(CodeVo vo:list){
					UploadPC t=new UploadPC(vo,path);
					t.start();
				}
			}catch (Exception e) {
				logger.info("联众打码 上传图片异常");
				for(CodeVo vo:list){
					vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
					dMService.updateCodeUserPicStatus(vo);
				}
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	class UploadPC extends Thread{
		 private CodeVo vo;
		 private String path; 
		 public UploadPC(CodeVo vo,String path)
		    {
		        this.vo = vo;
		        this.path = path;
		    }
		    public void run(){
		    	String readPath=path.substring(0,path.indexOf("WEB-INF")-1)+vo.getPic_filename();
				try {
					File f = new File(readPath);
					String threadName=this.getName();
					long start =System.currentTimeMillis();
					Map<String,String> map=DMV3.uploadGetResult(f);
					logger.info(threadName+" catch "+vo.getPic_id()+" startTime:"+start);
					
					Thread.sleep(new Random().nextInt(1000));
					if(map!=null){
						String yzm_id=map.get("id");
						String verify_code=map.get("val");
						//更新本地图片 占用状态
						vo.setOpt_ren(TrainConsts.OPTREN_NAME);
						vo.setUpdate_status(TrainConsts.UPDATE_SUCCESS);
						vo.setShyzmid(yzm_id);
						vo.setVerify_code(verify_code);
						dMService.updateUploadStatus(vo);
						long end =System.currentTimeMillis();
						logger.info(threadName+"-StartTime:"+start+",endTime:"+end+",picId:"+vo.getPic_id()+"上传图片成功,联众返回yzm_id:"+yzm_id+"code:"+verify_code+",耗时"+(end-start)+"ms");
						this.stop();
					}else{
						//还原图片占用状态
						vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
						dMService.updateCodeUserPicStatus(vo);
						logger.error("联众打码 上传图片获取结果失败! "+vo.getPic_id());
						this.stop();
					}
				} catch (Exception e) {
					logger.info("图片"+vo.getPic_id()+"上传异常"+e);
					//还原图片占用状态
					vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
					dMService.updateCodeUserPicStatus(vo);
					e.printStackTrace();
					this.stop();//不存在锁的问题 所有无关紧要
				}
			}
	}
	
	
	
	
	/*String readPath=path.substring(0,path.indexOf("WEB-INF")-1)+vo.getPic_filename();
	try {
		logger.info("readPath"+readPath);
		File f = new File(readPath);
		
		long start =System.currentTimeMillis();
		Map<String,String> map=DMV3.uploadGetResult(f);
		
		if(map!=null){
			String yzm_id=map.get("id");
			String verify_code=map.get("val");
			//更新本地图片 占用状态
			vo.setOpt_ren(TrainConsts.OPTREN_NAME);
			vo.setUpdate_status(TrainConsts.UPDATE_SUCCESS);
			vo.setShyzmid(yzm_id);
			vo.setVerify_code(verify_code);
			dMService.updateUploadStatus(vo);
			logger.info(vo.getPic_id()+"上传图片成功,联众返回yzm_id:"+yzm_id+"code:"+verify_code+",耗时"+(System.currentTimeMillis()-start)+"ms");
		}else{
			//还原图片占用状态
			vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
			dMService.updateCodeUserPicStatus(vo);
			logger.error("联众打码 上传图片获取结果失败! "+vo.getPic_id());
		}
	} catch (Exception e) {
		logger.info("图片"+vo.getPic_id()+"上传异常"+e);
		//还原图片占用状态
		vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
		dMService.updateCodeUserPicStatus(vo);
		
		e.printStackTrace();
	}*/
	
	
	
	public static void main(String[] args) {
		try {
			String domain = DMV3.getDoMainArryAndValidUser();
			System.out.println(domain);
			
			
			System.out.println(new Random().nextInt(1000));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
