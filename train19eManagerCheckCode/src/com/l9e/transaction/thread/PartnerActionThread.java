package com.l9e.transaction.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.cqz.code.JudgePicType;
import com.cqz.code.RuoKuaiUtil;
import com.cqz.code.YunsuUtil;
import com.cqz.tc.TcCodeUtil;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.Picture;
import com.l9e.transaction.vo.TcCodeReturn;
import com.l9e.util.Base64Util;


public class PartnerActionThread implements Runnable {

	Picture picture;

	private String pic_root;

	RobotCodeService robotService;

	private static final Logger logger = Logger
			.getLogger(PartnerActionThread.class);

	public PartnerActionThread(Picture pic,String pic_root,RobotCodeService robotService) {
		this.picture = pic;
		this.pic_root = pic_root;
		this.robotService = robotService;
	}

	@Override
	public void run() {
		if ("tongcheng".equals(picture.getChannel())) {// 同程打码
		// logger.info("[tongcheng]getTcCode："+picture.getPic_id()+" success！");
			getTcCode(picture);
		} else if ("ruokuai".equals(picture.getChannel())) {// 若快打码
			getRkCode(picture);
		} else if ("yunsu".equals(picture.getChannel())) {// 云速打码
			getYsCode(picture);
		}
	}

	// 获取yunsu打码的结果---同步
	public void getYsCode(Picture picture) {
		logger.info("[yunsu] pic sys_root url:" + pic_root);
		// 获取待打码图片路径
		try {
			String pic_type = JudgePicType.checkImage(pic_root
					+ picture.getPic_filename());
			String jsonStr = YunsuUtil.createByPost(pic_root
					+ picture.getPic_filename(), pic_type);
			logger.info(pic_type + "图【yunsu打码返回】" + jsonStr);
			// 处理打码结果
			if (jsonStr != null && StringUtils.isNotEmpty(jsonStr)) {
				updateRkcodeResult(picture, jsonStr, pic_type);
			} else {
				// {"Result":"243,116","Id":"194df09c-4b39-4288-aaef-54850b535fb2"}
				JSONObject json = new JSONObject();
				json.put("Id", "");
				json.put("Result", "243,116");
				updateRkcodeResult(picture, json.toString(), pic_type);
			}
		} catch (Exception e) {
			logger.error("find picturelist dberror:", e);
			Map<String, String> map_fail = new HashMap<String, String>();
			map_fail.put("status", "00");
			map_fail.put("pic_id", picture.getPic_id());
			robotService.updatePicturesMap(map_fail);
		}
	}

	// 获取ruokuai打码的结果---同步
	public void getRkCode(Picture picture) {
		logger.info("[ruokuai] pic sys_root url:" + pic_root);
		// 获取待打码图片路径
		try {
			// 判断该图片是8图还是18图
			String pic_type = JudgePicType.checkImage(pic_root
					+ picture.getPic_filename());
			String jsonStr = RuoKuaiUtil.getResultByPost(pic_root
					+ picture.getPic_filename(), pic_type);
			logger.info(pic_type + "图【ruokuai打码返回】" + jsonStr);
			// 处理打码结果
			if (jsonStr != null && StringUtils.isNotEmpty(jsonStr)) {
				updateRkcodeResult(picture, jsonStr, pic_type);
			} else {
				// {"Result":"243,116","Id":"194df09c-4b39-4288-aaef-54850b535fb2"}
				JSONObject json = new JSONObject();
				json.put("Id", "");
				json.put("Result", "243,116");
				updateRkcodeResult(picture, json.toString(), pic_type);
			}
		} catch (Exception e) {
			logger.error("find picturelist dberror:", e);
			Map<String, String> map_fail = new HashMap<String, String>();
			map_fail.put("status", "00");
			map_fail.put("pic_id", picture.getPic_id());
			robotService.updatePicturesMap(map_fail);
		}
	}

	// 获取同程打码的结果---同步
	public void getTcCode(Picture picture) {
		logger.info("[tongcheng consumer] pic sys_root url:" + pic_root);
		// 获取待打码图片路径
		try {
			String image = Base64Util.imageToBase64(pic_root
					+ picture.getPic_filename());
			// String image =
			// TcBase64.file2Base64(map.get("pic_root")+picture.getPic_filename());
			String jsonStr = TcCodeUtil.captchaDecode(TcCodeUtil.U_NAME,
					TcCodeUtil.KEY, image);
			logger.info("【同程打码返回】" + jsonStr);
			// 处理打码结果
			if (jsonStr != null && StringUtils.isNotEmpty(jsonStr)) {
				updateRancodeResult(picture, jsonStr);
			} else {
				// {"cid":"a52c241e-4065-4453-80e4-d824f0ac0174","ret":1,"res":"34","desc":"处理成功"}
				JSONObject json = new JSONObject();
				json.put("cid", "");
				json.put("ret", 1);
				json.put("res", "3");
				json.put("desc", "处理成功");
				updateRancodeResult(picture, json.toString());
			}
		} catch (Exception e) {
			logger.error("find picturelist dberror:", e);
			Map<String, String> map_fail = new HashMap<String, String>();
			map_fail.put("status", "00");
			map_fail.put("pic_id", picture.getPic_id());
			robotService.updatePicturesMap(map_fail);
		}
	}

	public void updateRkcodeResult(Picture picture, String result,
			String pic_type) {
		String finishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		Map<String, String> map = new HashMap<String, String>();
		try {
			// 正确：{"Result":"243,116","Id":"194df09c-4b39-4288-aaef-54850b535fb2"}
			// 错误：{"Error":"错误提示信息","Error_Code":"错误代码（详情见错误代码表）","Request":""}
			JSONObject json = JSONObject.fromObject(result);
			if (result.contains("Result")) {// 1 表示得到结果；-1 表示无法处理；-2 数据格式错误，请确认
				logger.info(picture.getChannel() + "打码结果返回成功,pic_id="
						+ picture.getPic_id());
				map.put("user_pic_status", "0");
				String verify_code = json.getString("Result");
				String verify_code_coord = "";//坐标位置
				if ("18".equals(pic_type)) {// 返回1-18数字（会有，隔开）
					//verify_code = get18Code(verify_code);
					verify_code_coord = JudgePicType.get18Code(verify_code);
				}else{
					verify_code_coord = JudgePicType.getCode(verify_code);
				}
				
				map.put("verify_code", verify_code);// 打码结果
				map.put("verify_code_coord", verify_code_coord);// 打码结果坐标位置
				map.put("opt_ren", picture.getChannel());
				map.put("finish_time", finishTime);
				map.put("status", "11");
				map.put("pic_id", picture.getPic_id());
				map.put("shyzmid", json.getString("Id"));
				map.put("update_status", "11");
				map.put("start_time", "now()");
				logger.info("update verify_code:"+map.toString());
				int num = robotService.updatePicturesMap(map);
				logger.info(picture.getChannel() + "更新数据条数：" + num);
			} else {
				logger.error(picture.getChannel() + "返回打码异常,pic_id="
						+ picture.getPic_id() + ",原因为："
						+ json.getString("Error_Code") + "/"
						+ json.getString("Error"));
				map.put("fail_reason", json.getString("Error_Code"));
				map.put("status", "00");
				map.put("pic_id", picture.getPic_id());
				logger.info(map.toString());
				robotService.updatePicturesMap(map);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public void updateRancodeResult(Picture picture, String result) {
		String finishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		Map<String, String> map = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			// {"cid":"30cf176c-40ab-737c-80a7-2a7ed74886fb","ret":1,"res":"23","desc":" 处理成功"}
			TcCodeReturn rs = mapper.readValue(result, TcCodeReturn.class);
			if (rs.getRet() == 1) {// 1 表示得到结果；-1 表示无法处理；-2 数据格式错误，请确认
				logger.info("tongcheng打码结果返回成功,pic_id=" + picture.getPic_id());
				map.put("user_pic_status", "0");
				map.put("verify_code", rs.getRes());// 打码结果
				map.put("opt_ren", "tongcheng");
				map.put("finish_time", finishTime);
				map.put("status", "11");
				map.put("pic_id", picture.getPic_id());
				map.put("shyzmid", rs.getCid());
				map.put("update_status", "11");
				map.put("start_time", "now()");
				logger.info(map.toString());
				int num = robotService.updatePicturesMap(map);
				logger.info("tongcheng更新数据条数：" + num);
			} else if (rs.getRet() == -1) {// {"ret":-1,"desc":"系统无法处理"}
				logger.info("tongcheng系统无法处理,pic_id=" + picture.getPic_id()
						+ "随机验证码值24");
				map.put("user_pic_status", "0");
				map.put("verify_code", "3");// 打码结果
				map.put("opt_ren", "tongcheng");
				map.put("finish_time", finishTime);
				map.put("status", "11");
				map.put("pic_id", picture.getPic_id());
				map.put("shyzmid", rs.getCid());
				map.put("update_status", "11");
				map.put("start_time", "now()");
				logger.info(map.toString());
				int num = robotService.updatePicturesMap(map);
				logger.info("tongcheng更新数据条数：" + num);
			} else {
				logger.error("tongcheng返回打码异常,pic_id=" + picture.getPic_id()
						+ ",原因为：" + result);
				map.put("fail_reason", rs.getDesc());
				map.put("status", "00");
				map.put("pic_id", picture.getPic_id());
				logger.info(map.toString());
				robotService.updatePicturesMap(map);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
};
