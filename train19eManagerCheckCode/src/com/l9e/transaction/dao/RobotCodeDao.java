package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.CodeVo;
import com.l9e.transaction.vo.Partner;
import com.l9e.transaction.vo.Picture;
import com.l9e.transaction.vo.UserInfo;


public interface RobotCodeDao {
	UserInfo queryUserInfo(String username);
	
	String queryPlayCardTotalNum(Map<String,String> map);
	
	String queryPlayCardRightNum(Map<String,String> map);
	
	String queryPlayCardErrorNum(Map<String,String> map);
	
	void updateUserInfo(Map<String,Object> map);
	
	void savePicture(Picture picture);
	
	Picture getPicture(String pic_id);
	
	void updatePictures(Picture picture);
	
	Picture queryPictureForUpdate(String pic_id);
	
	int updatePicturesStatus(String pic_id);
	
	int findResultStatus(String username);
	
	int getOnlineUserNum();
	
	int getOnlinePictureNum(Map<String,String> map);
	
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

	String querySystemValueOpen(String key);

	String queryPlayCardOverNum(Map<String, String> map);

	String queryUserCodeTimeLimit(String username);

	void updateUploadStatus(Picture picture);

	void updateCodeUserPicStatus(Picture picture);

	List<CodeVo> queryFeedBackResultList(Map<String, String> info);
	
	//查询系统配置的验证码超时时间
	String selectVerifiCodeTimeOut(String settingName);
}
