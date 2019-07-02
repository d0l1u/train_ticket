package com.kuyou.train.service.impl;

import com.kuyou.train.dao.ChangeCpMapper;
import com.kuyou.train.entity.po.ChangeCpPo;
import com.kuyou.train.service.ChangeCpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ChangeCpServiceImpl
 *
 * @author taokai3
 * @date 2018/11/2
 */
@Slf4j
@Service
public class ChangeCpServiceImpl implements ChangeCpService {

    @Resource
    private ChangeCpMapper changeCpMapper;


    @Override
    public List<ChangeCpPo> selectByChangeId(Integer changeId) {
        return changeCpMapper.selectByChangeId(changeId);
    }


    @Override
    public ChangeCpPo selectSequenceByNewCpId(String cpId) {
        return changeCpMapper.selectSequenceByNewCpId(cpId);
    }

    @Override
    public ChangeCpPo selectByNewCpId(Integer changeId, String newCpId) {
        return changeCpMapper.selectByNewCpId(changeId, newCpId);
    }

    @Override
    public List<ChangeCpPo> selectByOrderId(String orderId) {
        return changeCpMapper.selectByOrderId(orderId);
    }

    @Override
    public List<ChangeCpPo> selectByMyOrderId(String myOrderId) {
        return changeCpMapper.selectByMyOrderId(myOrderId);
    }

    @Override
    public int updateByCpId(ChangeCpPo changeCpPo, String cpId) {
        int result = changeCpMapper.updateByCpId(changeCpPo, cpId);
        log.info("ChangeCpService.updateByCpId 更新结果:{}", result);
        return result;
    }

    @Override
    public int updateByNewCpId(ChangeCpPo changeCpPo, String newCpId) {
        int result = changeCpMapper.updateByNewCpId(changeCpPo, newCpId);
        log.info("ChangeCpService.updateByNewCpId 更新结果:{}", result);
        return result;
    }

    @Override
    public int updateByChangeIdAndSeq(ChangeCpPo changeCpPo, Integer changeId, String oldTicketNo) {
        int result = changeCpMapper.updateByChangeIdAndSeq(changeCpPo, changeId, oldTicketNo);
        log.info("ChangeCpService.updateByChangeIdAndSeq 更新结果:{}", result);
        return result;
    }

    @Override
    public List<String> selectSeqByChangeId(Integer changeId) {
        return changeCpMapper.selectSeqByChangeId(changeId);
    }
}
