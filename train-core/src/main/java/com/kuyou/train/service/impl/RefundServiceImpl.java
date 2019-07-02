package com.kuyou.train.service.impl;

import com.kuyou.train.dao.RefundMapper;
import com.kuyou.train.entity.po.RefundPo;
import com.kuyou.train.entity.po.UnderRefundPo;
import com.kuyou.train.service.RefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * RefundServiceImpl
 *
 * @author taokai3
 * @date 2018/11/9
 */
@Slf4j
@Service
public class RefundServiceImpl implements RefundService {

    @Resource
    private RefundMapper refundMapper;

    @Override
    public int insertUnderRefund(List<UnderRefundPo> refundList) {
        int result = refundMapper.insertUnderRefund(refundList);
        log.info("RefundService.insertUnderRefund 线下退票插入结果:{}", result);
        return result;
    }

    @Override
    public RefundPo selectRefundByOrderIdAndSeq(String orderId, String subSequence) {
        return refundMapper.selectRefundByOrderIdAndSeq(orderId, subSequence);
    }

    @Override
    public RefundPo selectRefundByMyOrderIdAndSeq(String orderId, String subSequence) {
        return refundMapper.selectRefundByMyOrderIdAndSeq(orderId, subSequence);
    }

    @Override
    public int updateStatus(String orderId, String cpId, String status) {
        int result = refundMapper.updateStatus(orderId, cpId, status);
        log.info("RefundService.updateStatus 更新结果:{}", result);
        return result;
    }

    @Override
    public int updateRefund(RefundPo refundPo, String orderId, String cpId) {
        int result = refundMapper.updateRefund(refundPo, orderId, cpId);
        log.info("RefundService.updateRefund 更新结果:{}", result);
        return result;
    }

    @Override
    public List<RefundPo> selectWaitRefund(String orderStatus, int limit) {
        return refundMapper.selectWaitRefund(orderStatus, limit);
    }

    @Override
    public int updateStatusPre(String orderId, String cpId, String status, String preStatus) {
        int result = refundMapper.updateStatusPre(orderId, cpId, status, preStatus);
        log.info("RefundService.updateStatusPre 更新结果:{}", result);
        return result;
    }

    @Override
    public List<RefundPo> selectStuck(String status, Date time) {
        return refundMapper.selectStuck(status, time);
    }
}
