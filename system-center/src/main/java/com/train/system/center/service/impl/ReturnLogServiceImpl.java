package com.train.system.center.service.impl;

import com.train.system.center.dao.ReturnLogDao;
import com.train.system.center.entity.ReturnLog;
import com.train.system.center.service.ReturnLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ReturnLogServiceImpl
 *
 * @author taokai3
 * @date 2018/6/25
 */
@Service("returnLogService")
public class ReturnLogServiceImpl implements ReturnLogService {

    @Resource
    private ReturnLogDao returnLogDao;

    @Override
    public List<ReturnLog> queryList() {
        return returnLogDao.queryList();
    }
}
