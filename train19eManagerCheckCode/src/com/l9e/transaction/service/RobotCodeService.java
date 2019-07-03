package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.CodeVo;
import com.l9e.transaction.vo.OnlineMessage;
import com.l9e.transaction.vo.Partner;
import com.l9e.transaction.vo.Picture;
import com.l9e.transaction.vo.PlayCardResult;
import com.l9e.transaction.vo.UserInfo;


public interface RobotCodeService {
	UserInfo queryUserInfo(String username);
	
	PlayCardResult queryUserPlayCardTotal(String username);
	
	void updateUserInfo(Map<String,Object> map);
	
	void savePicture(Picture picture);
	
	Picture getPicture(String pic_id);
	
	void updatePictures(Picture picture);
	
	void updatePicturesStatus(String pic_id);
	
	void updateAndFindUserStatus(String username);
	
	Picture updateAndFindPictureStatus(String depart, String code_weight_switch);
	
	int findResultStatus(String username);
	
	OnlineMessage getOnlineMessage();
	
	Map<String,String> findByPicName(String pic_filename);
	
	Partner selectPartnerById(String partner_id);
	
	void updateResultStatusById(Map<String,String> map);

	Map<String, String> querySysInfo();

	int updatePicturesMap(Map<String,String> map);
	
	String queryOrderInfoChannel(String order_id);
	
	List<Picture> queryQunarPictureList(String channel);


	int queryLoginCountById(Map<String, String> param);

	void insertToHistoryLogin(Map<String, String> param);

	void insertNoteLogin(Map<String, String> param);

	int selectNoteLogin(Map<String, String> param);

	void updateNoteLogin(Map<String, String> param);

	String querySystemValue(String key);
	
	void updatePicturesChannel(Map<String,String> map);
	
	List<Picture> queryQunarPictureListSuccess(Map<String,Integer> map);
	
	List<String> queryPictureIdList(Map<String,Object> map);
	
	List<String> queryChannelPictureIdList(Map<String,Object> map);
	
	int updatePicturesStatusHasReturn(String pic_id);
	
	void updateUserInfoLoginTime(String username);

	String querySystemValueOpen(String string);

	String queryUserCodeTimeLimit(String username);

	void updateUploadStatus(Picture picture);

	void updateCodeUserPicStatus(Picture picture);

	List<CodeVo> queryFeedBackResultList(Map<String, String> info);
	
	//查询系统配置的验证码超时时间
	String queryVerifiCodeTimeOut(String settingName);

}
