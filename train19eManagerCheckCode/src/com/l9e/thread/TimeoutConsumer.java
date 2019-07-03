package com.l9e.thread;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.cqz.tc.TcCodeUtil;
import com.l9e.common.TrainConsts;
import com.l9e.producerConsumer.distinct.DistinctConsumer;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.Picture;
import com.l9e.transaction.vo.QunarCodeReturn;
import com.l9e.transaction.vo.TcCodeReturn;
import com.l9e.util.Base64Util;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

//消费者：消费qunar渠道的（qunar_list）
public class TimeoutConsumer extends DistinctConsumer<Picture> {
	
	private Logger logger=Logger.getLogger(this.getClass());

	@Override
	public boolean consume(Picture picture, RobotCodeService robotService, Map<String,String> map) {
		if("qunar".equals(picture.getChannel())){
			logger.info("qunar pic pic_root url:"+map.get("pic_root"));
			//获取待打码图片路径
			try {
				//转化为二进制流
//				byte[] byteArr = imageToByte(map.get("sys_root")+picture.getPic_filename());
//				System.setProperty("http.proxySet", "true");  
//				System.setProperty("http.proxyHost", "127.0.0.1");
//				System.setProperty("http.proxyPort", "8888");
				//向qunar发起打码请求
//				String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
//				logger.info("byteArr-length:"+byteArr.length);
				String agent_code = map.get("agent_code1");
				String secretKey = map.get("secretKey1");
				if(picture.getOrder_id().contains(map.get("agent_code2"))){
					agent_code = map.get("agent_code2");
					secretKey = map.get("secretKey2");
				}
				String rand_url = map.get("rand_url");
				/*if("33".equals(picture.getQunar_code())){//am后台打码
					rand_url = map.get("rand_url_am");
				}*/
				
				String image = Base64Util.imageToBase64(map.get("pic_root")+picture.getPic_filename());
				String hmac = DigestUtils.md5Hex(secretKey+agent_code+image).toLowerCase();
				
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("agentCode", agent_code);
				paramMap.put("image", image);
				paramMap.put("hmac", hmac);
				//向qunar上传验证码图片
				String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				String jsonStr = HttpUtil.sendByPost(rand_url, params, "utf-8");
				logger.info("【上传验证码图片返回】"+jsonStr);
				
				ObjectMapper mapper = new ObjectMapper();
				QunarCodeReturn rs = mapper.readValue(jsonStr, QunarCodeReturn.class);
				
				if(rs.isRet()){//请求是否成功，true成功，false失败
					Map<String, String> data = rs.getData();
					String state = data.get("state");
					if("1".equals(state)){//state：0请求受理失败 state：1请求受理成功
						String yzm_id = data.get("globalId");//globalId：该验证码全局的，在后面的两步中使用
						if (yzm_id != null) {
							// 更新本地图片 占用状态
							picture.setOpt_ren("qunar");
							picture.setUpdate_status(TrainConsts.UPDATE_SUCCESS);
							picture.setShyzmid(yzm_id);
							robotService.updateUploadStatus(picture);
						}
					}else{
						// 还原图片占用状态
						picture.setUser_pic_status(Integer.parseInt(TrainConsts.USER_PIC_OUT));
						robotService.updateCodeUserPicStatus(picture);
						logger.error("qunar打码 上传图片获取结果失败! " + picture.getPic_id());
					}
				}else{
					logger.error("qunar返回打码异常原因为："+jsonStr);
					map.put("fail_reason", rs.getErrcode()+":"+rs.getErrmsg());
					map.put("status", "00");
					map.put("pic_id", picture.getPic_id());
					logger.info(map.toString());
					robotService.updatePicturesMap(map);
				}
				
			} catch (Exception e) {
				logger.error("find picturelist dberror:",e);
				Map<String,String> map_fail = new HashMap<String,String>();
				map_fail.put("status", "00");
				map_fail.put("pic_id", picture.getPic_id());
				robotService.updatePicturesMap(map_fail);
			}
		}else if("tongcheng".equals(picture.getChannel())){
			logger.info("[tongcheng consumer] pic sys_root url:"+map.get("sys_root"));
			//获取待打码图片路径
			try {
				String image = Base64Util.imageToBase64(map.get("pic_root")+picture.getPic_filename());
//				String image = TcBase64.file2Base64(map.get("pic_root")+picture.getPic_filename());
				String jsonStr = TcCodeUtil.captchaDecode(TcCodeUtil.U_NAME, TcCodeUtil.KEY, image);
				logger.info("【同程打码返回】"+jsonStr);
				//处理打码结果
				if(jsonStr!=null && StringUtils.isNotEmpty(jsonStr)){
					updateRancodeResult(robotService, picture, jsonStr);
				}else{
					//{"cid":"a52c241e-4065-4453-80e4-d824f0ac0174","ret":1,"res":"34","desc":"处理成功"}
					JSONObject json = new JSONObject();
					json.put("cid", "");
					json.put("ret", 1);
					json.put("res", "36");
					json.put("desc", "处理成功");
					updateRancodeResult(robotService, picture, json.toString());
				}
				
			} catch (Exception e) {
				logger.error("find picturelist dberror:",e);
				Map<String,String> map_fail = new HashMap<String,String>();
				map_fail.put("status", "00");
				map_fail.put("pic_id", picture.getPic_id());
				robotService.updatePicturesMap(map_fail);
			}
		}
		return true;
	}
	
	
	public void updateRancodeResult(RobotCodeService robotService,Picture picture,String result){
		String finishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String, String> map = new HashMap<String, String>();
//		{"cid":"30cf176c-40ab-737c-80a7-2a7ed74886fb","ret":1,"res":"23","desc":" 处理成功"}
		JSONObject json =JSONObject.fromObject(result);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			TcCodeReturn rs = mapper.readValue(result, TcCodeReturn.class);
			if(rs.getRet()==1){//1 表示得到结果；-1 表示无法处理；-2 数据格式错误，请确认
				logger.info("tongcheng打码结果返回成功,pic_id="+picture.getPic_id());
				map.put("user_pic_status", "0");
				map.put("verify_code", rs.getRes());//打码结果
				map.put("opt_ren", "tongcheng");
				map.put("finish_time", finishTime);
				map.put("status", "11");
				map.put("pic_id", picture.getPic_id());
				map.put("shyzmid", rs.getCid());
				map.put("update_status", "11");
				logger.info(map.toString());
				int num = robotService.updatePicturesMap(map);
				logger.info("tongcheng更新数据条数："+num);
			}else if(rs.getRet()==-1){//{"ret":-1,"desc":"系统无法处理"}
				logger.info("tongcheng系统无法处理,pic_id="+picture.getPic_id()+"随机验证码值24");
				map.put("user_pic_status", "0");
				map.put("verify_code", "3");//打码结果
				map.put("opt_ren", "tongcheng");
				map.put("finish_time", finishTime);
				map.put("status", "11");
				map.put("pic_id", picture.getPic_id());
				map.put("shyzmid", rs.getCid());
				map.put("update_status", "11");
				logger.info(map.toString());
				int num = robotService.updatePicturesMap(map);
				logger.info("tongcheng更新数据条数："+num);
			}else{
				logger.error("tongcheng返回打码异常,pic_id="+picture.getPic_id()+",原因为："+result);
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
	
	@Override
	public String getObjectKeyId(Picture t) {
		return t.getPic_id();
	}


}