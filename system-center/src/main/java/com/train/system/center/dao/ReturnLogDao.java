package com.train.system.center.dao;

import com.train.system.center.entity.ReturnLog;

import java.util.List;

/**
 * ReturnLogDao
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface ReturnLogDao {

    List<ReturnLog> queryList();
}
