package com.l9e.transaction.job;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cqz.dmt.Damatu;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.DMService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.CodeVo;
import com.l9e.util.MemcachedUtil;

/**
 * 打码兔打码 上传图片job 并且获取结果job
 * 
 * */
@Component("dama2UploadJob")
public class Dama2UploadJob {
	private static final Logger logger = Logger.getLogger(Dama2UploadJob.class);

	@Resource
	private DMService dMService;

	@Resource
	private RobotCodeService robotService;

	public void upload() {
		
		Map<String, String> info = robotService.querySysInfo();
		String code_channel = info.get("code_channel");
		if (TrainConsts.CODE_DAMATU.equals(code_channel)
				|| TrainConsts.CODE_DAMATUANDRG.equals(code_channel)) {
			List<CodeVo> list = dMService.updateQuerySendCodeList(info);
			if (list == null) {
				return;
			}
			logger.info("打码兔打码 上传图片job-------------"+list);
			try {
				// **打码兔 上传图片开始*//*
				// String domain = DMV3.getDoMainArryAndValidUser();
				String path = Dama2UploadJob.class.getClassLoader()
						.getResource("").getPath();
				for (CodeVo vo : list) {
					UploadPC t = new UploadPC(vo, path);
					t.start();
				}
			} catch (Exception e) {
				logger.info("打码兔打码 上传图片异常");
				for (CodeVo vo : list) {
					vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
					dMService.updateCodeUserPicStatus(vo);
				}
				e.printStackTrace();
			}
		}
	}

	class UploadPC extends Thread {
		private CodeVo vo;
		private String path;

		public UploadPC(CodeVo vo, String path) {
			this.vo = vo;
			this.path = path;
		}

		public void run() {
			String readPath = path.substring(0, path.indexOf("WEB-INF") - 1) + vo.getPic_filename();
//			logger.info("---------readPath:"+readPath);
			try {
				File f = new File(readPath);
				String threadName = this.getName();
				long start = System.currentTimeMillis();
				String yzm_id = Damatu.postFileUpload(f);
				logger.info(threadName + " catch " + vo.getPic_id() + " startTime:" + start);

				Thread.sleep(new Random().nextInt(1000));
				if (yzm_id != null) {
//					String verify_code = map.get("val");
					// 更新本地图片 占用状态
					vo.setOpt_ren(TrainConsts.DAMATU_NAME);
					vo.setUpdate_status(TrainConsts.UPDATE_SUCCESS);
					vo.setShyzmid(yzm_id);
//					vo.setVerify_code(verify_code);
					dMService.updateUploadStatus(vo);
					long end = System.currentTimeMillis();
					logger.info(threadName + "-StartTime:" + start
							+ ",endTime:" + end + ",picId:" + vo.getPic_id()
							+ "上传图片成功,打码兔返回yzm_id:" + yzm_id 
//							+ "code:"+ verify_code 
							+ ",耗时" + (end - start) + "ms");
					this.stop();
				} else {
					// 还原图片占用状态
					vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
					dMService.updateCodeUserPicStatus(vo);
					logger.error("打码兔打码 上传图片获取结果失败! " + vo.getPic_id());
					this.stop();
				}
			} catch (Exception e) {
				logger.info("图片" + vo.getPic_id() + "上传异常" + e);
				// 还原图片占用状态
				vo.setUser_pic_status(TrainConsts.USER_PIC_OUT);
				dMService.updateCodeUserPicStatus(vo);
				e.printStackTrace();
				this.stop();// 不存在锁的问题 所有无关紧要
			}
		}
	}

}
