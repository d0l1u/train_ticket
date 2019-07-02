package com.kuyou.train.service.impl;

import com.google.common.collect.Lists;
import com.kuyou.train.dao.ChangeMapper;
import com.kuyou.train.entity.po.ChangePo;
import com.kuyou.train.service.ChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ChangeServiceImpl
 *
 * @author taokai3
 * @date 2018/11/2
 */
@Slf4j
@Service
public class ChangeServiceImpl implements ChangeService {

    @Resource
    private ChangeMapper changeMapper;

    @Override
    public ChangePo selectByChangeId(Integer changeId) {
        return changeMapper.selectByChangeId(changeId);
    }

    @Override
    public List<ChangePo> selectByOrderId(String orderId) {
        return changeMapper.selectByOrderId(orderId);
    }

    @Override
    public List<ChangePo> selectByMyOrderId(String myOrderId) {
        return changeMapper.selectByMyOrderId(myOrderId);
    }


    @Override
    public List<ChangePo> selectByStatus(String status, Integer limit) {
        return selectByStatus(Lists.newArrayList(status), limit);
    }

    @Override
    public List<ChangePo> selectByStatus(List<String> statusList, Integer limit) {
        if (limit == null) {
            limit = 10;
        }
        return changeMapper.selectByStatus(statusList, limit);
    }

    @Override
    public int updateStatusPre(Integer changeId, String preStatus, String targetStatus) {
        int result = changeMapper.updateStatusPre(changeId, preStatus, targetStatus);
        log.info("ChangeService.updateStatusPre 更新结果:{}", result);
        return result;
    }

    @Override
    public int updateStatus(Integer changeId, String status, String reason) {
        int result = changeMapper.updateStatus(changeId, status, reason);
        log.info("ChangeService.updateStatus 更新结果:{}", result);
        return result;
    }

    @Override
    public List<ChangePo> selectStuck(String status, ArrayList<String> supplierList, Date stuckMinutes) {
        return changeMapper.selectStuck(status, supplierList, stuckMinutes);
    }

    @Override
    public int updateStatusById(Integer changeId, String status) {
        return changeMapper.updateStatusById(changeId, status);
    }

    @Override
    public int updateByChangeId(ChangePo changePo, Integer changeId) {
        int result = changeMapper.updateByChangeId(changePo, changeId);
        log.info("ChangeService.updateByChangeId 更新结果:{}", result);
        return result;
    }

}
