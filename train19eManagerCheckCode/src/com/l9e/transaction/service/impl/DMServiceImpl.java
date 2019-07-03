package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.DMDao;
import com.l9e.transaction.service.DMService;
import com.l9e.transaction.vo.CodeVo;
@Service("dMService")
public class DMServiceImpl implements DMService{
	@Resource
	private DMDao dMDao;

	@Override
	public Map<String, String> queryCodeBusinesInfo() {
		return dMDao.queryCodeBusinesInfo();
	}


	@Override
	public List<CodeVo> queryCatchCodeList(Map<String, String> info) {
		return dMDao.queryCatchCodeList(info);
	}

	@Override
	public List<CodeVo> updateQuerySendCodeList(Map<String, String> info) {
		List<CodeVo> list=dMDao.querySendCodeList(info);
		
		List<CodeVo> returnList=new ArrayList<CodeVo>();
		if(list.size()>0){
			for(CodeVo vo:list){
				vo.setUser_pic_status(TrainConsts.USER_PIC_ON);
				int count=dMDao.updateCodeUserPicStatus(vo);
				if(count>0){
					returnList.add(vo);
				}
			}
			return returnList;
		}else{
			return null;
		}
	}


	@Override
	public void updateCodeInfo(CodeVo vo) {
		dMDao.updateCodeInfo(vo);
	}




	@Override
	public void updateErrorInfo(CodeVo vo) {
		dMDao.updateErrorInfo(vo);
	}


	@Override
	public void updateCodeUserPicStatus(CodeVo vo) {
		dMDao.updateCodeUserPicStatus(vo);
	}


	@Override
	public void updateUploadStatus(CodeVo vo) {
		dMDao.updateUploadStatus(vo);
	}


	@Override
	public List<CodeVo> queryErrorCodeList(Map<String, String> info) {
		return dMDao.queryErrorCodeList(info);
	}
}
