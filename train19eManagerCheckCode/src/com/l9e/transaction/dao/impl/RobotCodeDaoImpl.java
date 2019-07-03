package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RobotCodeDao;
import com.l9e.transaction.vo.CodeVo;
import com.l9e.transaction.vo.Partner;
import com.l9e.transaction.vo.Picture;
import com.l9e.transaction.vo.UserInfo;
@Repository("robotDao")
public class RobotCodeDaoImpl extends BaseDao implements RobotCodeDao{

	@Override
	public UserInfo queryUserInfo(String username) {
		return (UserInfo)this.getSqlMapClientTemplate().queryForObject("robot.queryUserInfo", username);
	}

	@Override
	public String queryPlayCardErrorNum(Map<String,String> map) {
		return (String)this.getSqlMapClientTemplate().queryForObject("robot.queryPlayCardErrorNum", map);
	}

	@Override
	public String queryPlayCardRightNum(Map<String,String> map) {
		return (String)this.getSqlMapClientTemplate().queryForObject("robot.queryPlayCardRightNum", map);
	}

	@Override
	public String queryPlayCardTotalNum(Map<String,String> map) {
		return (String)this.getSqlMapClientTemplate().queryForObject("robot.queryPlayCardTotalNum", map);
	}

	@Override
	public void updateUserInfo(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("robot.updateUserInfo", map);
	}

	@Override
	public void savePicture(Picture picture) {
		this.getSqlMapClientTemplate().insert("robot.savePicture", picture);
	}

	@Override
	public Picture getPicture(String pic_id) {
		return (Picture)this.getSqlMapClientTemplate().queryForObject("robot.getPicture", pic_id);
	}

	@Override
	public void updatePictures(Picture picture) {
		this.getSqlMapClientTemplate().update("robot.updatePictures", picture);
	}

	@Override
	public Picture queryPictureForUpdate(String pic_id) {
		return (Picture)this.getSqlMapClientTemplate().queryForObject("robot.queryPictureForUpdate",pic_id);
	}

	@Override
	public int updatePicturesStatus(String pic_id) {
		return (Integer)this.getSqlMapClientTemplate().update("robot.updatePicturesStatus", pic_id);
	}

	@Override
	public int findResultStatus(String username) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("robot.findResultStatus",username);
	}

	@Override
	public int getOnlineUserNum() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("robot.getOnlineUserNum");
	}

	@Override
	public int getOnlinePictureNum(Map<String,String> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("robot.getOnlinePictureNum",map);
	}
	

	@Override
	public Map<String, String> findByPicName(String pic_filename) {
		return (Map<String, String>)this.getSqlMapClientTemplate().queryForObject("robot.findByPicName",pic_filename);
	}

	@Override
	public Partner selectPartnerById(String partner_id) {
		return (Partner)this.getSqlMapClientTemplate().queryForObject("robot.selectPartnerById",partner_id);
	}

	@Override
	public void updateResultStatusById(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("robot.updateResultStatusById", map);
	}

	@Override
	public Map<String, String> querySysInfo() {
		return (Map<String, String>)this.getSqlMapClientTemplate().queryForObject("robot.querySysInfo");
	}

	@Override
	public int updatePicturesMap(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update("robot.updatePicturesMap", map);
	}

	@Override
	public String queryOrderInfoChannel(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("robot.queryOrderInfoChannel",order_id);
	}

	@Override
	public List<Picture> queryQunarPictureList(String channel) {
		return (List<Picture>)this.getSqlMapClientTemplate().queryForList("robot.queryQunarPictureList",channel);
	}


	@Override
	public void insertToHistoryLogin(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("robot.insertToHistoryLogin",param);
	}

	@Override
	public int queryLoginCountById(Map<String, String> param) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("robot.queryLoginCountById",param);
	}

	@Override
	public void insertNoteLogin(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("robot.insertNoteLogin",param);
	}

	@Override
	public int selectNoteLogin(Map<String, String> param) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("robot.selectNoteLogin",param);
	}

	@Override
	public void updateNoteLogin(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("robot.updateNoteLogin",param);
	}


	@Override
	public String querySystemValue(String key) {
		return (String) this.getSqlMapClientTemplate().queryForObject("robot.querySystemValue",key);
	}

	@Override
	public void updatePicturesChannel(Map<String,String> map) {
		this.getSqlMapClientTemplate().update("robot.updatePicturesChannel", map);
	}

	@Override
	public List<Picture> queryQunarPictureListSuccess(Map<String, Integer> map) {
		return this.getSqlMapClientTemplate().queryForList("robot.queryQunarPictureListSuccess",map);
	}

	@Override
	public List<String> queryPictureIdList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("robot.queryPictureIdList",map);
	}

	@Override
	public int updatePicturesStatusHasReturn(String pic_id) {
		return (Integer) this.getSqlMapClientTemplate().update("robot.updatePicturesStatusHasReturn",pic_id);
	}

	@Override
	public List<String> queryChannelPictureIdList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("robot.queryChannelPictureIdList",map);
	}

	@Override
	public void updateUserInfoLoginTime(String username) {
		this.getSqlMapClientTemplate().update("robot.updateUserInfoLoginTime", username);
	}

	@Override
	public String querySystemValueOpen(String key) {
		return (String) this.getSqlMapClientTemplate().queryForObject("robot.querySystemValueOpen", key);
	}

	@Override
	public String queryPlayCardOverNum(Map<String, String> map) {
		return (String)this.getSqlMapClientTemplate().queryForObject("robot.queryPlayCardOverNum", map);
	}

	@Override
	public String queryUserCodeTimeLimit(String username) {
		return (String) this.getSqlMapClientTemplate().queryForObject("robot.queryUserCodeTimeLimit", username);
	}

	@Override
	public void updateUploadStatus(Picture picture) {
		this.getSqlMapClientTemplate().update("robot.updateUploadStatus", picture);
	}

	@Override
	public void updateCodeUserPicStatus(Picture picture) {
		this.getSqlMapClientTemplate().update("robot.updateCodeUserPicStatus", picture);
	}

	@Override
	public List<CodeVo> queryFeedBackResultList(Map<String, String> info) {
		return this.getSqlMapClientTemplate().queryForList("robot.queryFeedBackResultList", info);
	}

	//查询系统配置的验证码超时时间
	@Override
	public String selectVerifiCodeTimeOut(String settingName) {
		// TODO Auto-generated method stub
		return (String)this.getSqlMapClientTemplate().queryForObject("robot.queryVerifiCodeTimeOut",settingName);
	}

}
