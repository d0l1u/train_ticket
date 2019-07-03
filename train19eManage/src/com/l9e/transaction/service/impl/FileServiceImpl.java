package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.FileDao;
import com.l9e.transaction.service.FileService;
@Service("fileService")
public class FileServiceImpl implements FileService {
	@Resource
	private FileDao fileDao;

	@Override
	public void insertFile(Map<String, String> paramMap) {
		fileDao.insertFile(paramMap);
		
	}

	@Override
	public int queryFileCount(Map<String, Object> paramMap) {
		return fileDao.queryFileCount(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryFileList(Map<String, Object> paramMap) {
		return fileDao.queryFileList(paramMap);
	}

	@Override
	public String queryFilepath(String id) {
		return fileDao.queryFilepath(id);
	}

	@Override
	public void deleteFile(String id) {
		fileDao.deleteFile(id);
	}
	
}
