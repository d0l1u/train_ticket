package com.l9e.transaction.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.thread.StackData;
import com.l9e.thread.StackTcData;
import com.l9e.transaction.dao.RobotCodeDao;
import com.l9e.transaction.mq.MqService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.CodeVo;
import com.l9e.transaction.vo.OnlineMessage;
import com.l9e.transaction.vo.Partner;
import com.l9e.transaction.vo.Picture;
import com.l9e.transaction.vo.PlayCardResult;
import com.l9e.transaction.vo.UserInfo;
import com.l9e.util.DateUtil;
@Service("robotService")
public class RobotCodeServiceImpl implements RobotCodeService{
	private static final Logger logger = 
		Logger.getLogger(RobotCodeServiceImpl.class);
	@Resource
	RobotCodeDao robotDao;
	
	@Resource
	private MqService mqService;
	
	@Override
	public UserInfo queryUserInfo(String username) {
		return robotDao.queryUserInfo(username);
	}

	@Override
	public PlayCardResult queryUserPlayCardTotal(String username) {
		PlayCardResult playNum = new PlayCardResult();
		playNum.setTotal_num("0");
		playNum.setRight_num("0");
		playNum.setError_num("0");
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", username);
		map.put("create_time", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1) + " 01:00:00");
		playNum.setTotal_num(robotDao.queryPlayCardTotalNum(map));
		playNum.setRight_num(robotDao.queryPlayCardRightNum(map));
		playNum.setError_num(robotDao.queryPlayCardErrorNum(map));
		playNum.setOver_num(robotDao.queryPlayCardOverNum(map));
		return playNum;
	}

	@Override
	public void updateUserInfo(Map<String,Object> map) {
		robotDao.updateUserInfo(map);
	}

	@Override
	public void savePicture(Picture picture) {
		robotDao.savePicture(picture);
	}

	@Override
	public Picture getPicture(String picId) {
		return robotDao.getPicture(picId);
	}

	@Override
	public void updatePictures(Picture picture) {
		robotDao.updatePictures(picture);
	}

	@Override
	public void updateAndFindUserStatus(String username) {
		UserInfo userInfo = robotDao.queryUserInfo(username);
		if(userInfo.getLogin_status() == 2 || userInfo.getLogin_status() == 3 || userInfo.getLogin_status() == 0){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("status", 1);
			map.put("login_status", "1");
			map.put("username", username);
			robotDao.updateUserInfo(map);
		}
	}

	@Override
	public Picture updateAndFindPictureStatus(String depart, String code_weight_switch) {
		/*String tc_play_code = robotDao.querySystemValue("tc_play_code");
		if("6".equals(depart) && ("00".equals(tc_play_code)||"22".equals(tc_play_code)||"44".equals(tc_play_code))){
			//单例获取同城图片池
			StackTcData data = StackTcData.getInstance();
			//logger.info("========>>> lave wait play code size:"+data.getQueueSize());
			Picture picture = null;
			String pic_id = data.getPicId();
			if(null==pic_id || "".equals(pic_id)){
				return null;
			}
//			logger.info("========>>> find the picture id:"+pic_id);
			picture = robotDao.queryPictureForUpdate(pic_id);
				
			return picture;
		}*/
		//单例获取同一对象
		//StackData data = StackData.getInstance();
		//logger.info("========>>> lave wait play code size:"+data.getQueueSize());
		Picture picture = null;
		//5:打码团队1    00:打码团队2   4:打码团队3    99:打码团队其他
		TextMessage textMessage = null;
		if("0".equals(code_weight_switch)){//打码权重关闭
			textMessage = (TextMessage) mqService.receive(TrainConsts.ROBOT_CODENO);
		}else{
			if("5".equals(depart)){
				textMessage = (TextMessage) mqService.receive(TrainConsts.ROBOT_CODE);
			}else if("00".equals(depart)){
				textMessage = (TextMessage) mqService.receive(TrainConsts.ROBOT_CODE02);
			}else if("4".equals(depart)){
				textMessage = (TextMessage) mqService.receive(TrainConsts.ROBOT_CODE03);
			}else{
				textMessage = (TextMessage) mqService.receive(TrainConsts.ROBOT_CODE04);
			}
		}
		
		
		String pic_id=null;
		try {
			if(textMessage!=null){
				pic_id = textMessage.getText();
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(null==pic_id){
			return null;
		}
		logger.info("========>>> find the picture id:"+pic_id);
		picture = robotDao.queryPictureForUpdate(pic_id);
		logger.info("========>>> mq receive the pic_id:"+pic_id + ", pic_filename:" + picture);
		//批量更新码为处理状态 
		robotDao.updatePicturesStatus(pic_id);
		/*Picture picture = null;
		Map<String,Object> map = new HashMap<String,Object>();
		String channel = robotDao.querySystemValue("qunar_play_code");
		map.put("channel", channel);
		map.put("limit", 5);
		List<String> list = robotDao.queryPictureIdList(map);
		for(String pic_id:list){
			int re_num = robotDao.updatePicturesStatusHasReturn(pic_id);
			if(re_num==1){
				picture = robotDao.queryPictureForUpdate(pic_id);
				break;
			}
		}*/
			
		return picture;
	}

	@Override
	public int findResultStatus(String username) {
		return robotDao.findResultStatus(username);
	}

	@Override
	public OnlineMessage getOnlineMessage() {
		String value = robotDao.querySystemValue("qunar_play_code");
		Map<String,String> map = new HashMap<String,String>();
		map.put("channel", value);
		OnlineMessage manager = new OnlineMessage();
		manager.setOnline_num("0");
		manager.setUncode_num("0");
		manager.setOnline_num(String.valueOf(robotDao.getOnlineUserNum()));
		manager.setUncode_num(String.valueOf(robotDao.getOnlinePictureNum(map)));
		return manager;
	}

	@Override
	public Map<String, String> findByPicName(String pic_filename) {
		return robotDao.findByPicName(pic_filename);
	}

	@Override
	public Partner selectPartnerById(String partnerId) {
		return robotDao.selectPartnerById(partnerId);
	}

	@Override
	public void updateResultStatusById(Map<String, String> map) {
		robotDao.updateResultStatusById(map);
	}

	@Override
	public Map<String, String> querySysInfo() {
		return robotDao.querySysInfo();
	}

	@Override
	public int updatePicturesMap(Map<String, String> map) {
		return robotDao.updatePicturesMap(map);
	}

	@Override
	public String queryOrderInfoChannel(String orderId) {
		return robotDao.queryOrderInfoChannel(orderId);
	}

	@Override
	public List<Picture> queryQunarPictureList(String channel) {
		return robotDao.queryQunarPictureList(channel);
	}

	@Override
	public void updatePicturesStatus(String pic_id) {
		robotDao.updatePicturesStatus(pic_id);
	}


	@Override
	public void insertToHistoryLogin(Map<String, String> param) {
		robotDao.insertToHistoryLogin(param);
	}

	@Override
	public String querySystemValue(String key) {
		return robotDao.querySystemValue(key);
	}

	@Override
	public int queryLoginCountById(Map<String, String> param) {
		return robotDao.queryLoginCountById(param);
	}

	@Override
	public void insertNoteLogin(Map<String, String> param) {
		robotDao.insertNoteLogin(param);
	}

	@Override
	public int selectNoteLogin(Map<String, String> param) {
		return robotDao.selectNoteLogin(param);
	}

	@Override
	public void updateNoteLogin(Map<String, String> param) {
		robotDao.updateNoteLogin(param);
	}



	@Override
	public void updatePicturesChannel(Map<String, String> map) {
		robotDao.updatePicturesChannel(map);
	}

	@Override
	public List<Picture> queryQunarPictureListSuccess(Map<String, Integer> map) {
		return robotDao.queryQunarPictureListSuccess(map);
	}

	@Override
	public List<String> queryPictureIdList(Map<String, Object> map) {
		return robotDao.queryPictureIdList(map);
	}

	@Override
	public int updatePicturesStatusHasReturn(String picId) {
		return robotDao.updatePicturesStatusHasReturn(picId);
	}

	@Override
	public List<String> queryChannelPictureIdList(Map<String, Object> map) {
		return robotDao.queryChannelPictureIdList(map);
	}

	@Override
	public void updateUserInfoLoginTime(String username) {
		robotDao.updateUserInfoLoginTime(username);
	}

	@Override
	public String querySystemValueOpen(String key) {
		return robotDao.querySystemValueOpen(key);
	}

	@Override
	public String queryUserCodeTimeLimit(String username) {
		return robotDao.queryUserCodeTimeLimit(username);
	}

	@Override
	public void updateUploadStatus(Picture picture) {
		robotDao.updateUploadStatus(picture);
	}

	@Override
	public void updateCodeUserPicStatus(Picture picture) {
		robotDao.updateCodeUserPicStatus(picture);
	}

	@Override
	public List<CodeVo> queryFeedBackResultList(Map<String, String> info) {
		return robotDao.queryFeedBackResultList(info);
	}

	//查询系统配置的验证码超时时间
	@Override
	public String queryVerifiCodeTimeOut(String settingName) {
		// TODO Auto-generated method stub
		return robotDao.selectVerifiCodeTimeOut(settingName);
	}
}
