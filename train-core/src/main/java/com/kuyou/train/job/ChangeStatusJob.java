package com.kuyou.train.job;

import com.google.common.collect.Lists;
import com.kuyou.train.common.constant.SupplierCode;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.entity.po.ChangeCpPo;
import com.kuyou.train.entity.po.ChangePo;
import com.kuyou.train.entity.po.OrderCpPo;
import com.kuyou.train.entity.po.OrderPo;
import com.kuyou.train.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * ChangeStatusJob：修改改签单的状态，11->10-12
 *
 * @author taokai3
 * @date 2018/12/10
 */
@Slf4j
@Component("changeStatusJob")
public class ChangeStatusJob {

    @Resource
    private ChangeService changeService;

    @Resource
    private OrderService orderService;

    @Resource
    private ChangeCpService changeCpService;

    @Resource
    private HistoryService historyService;

    @Resource
    private OrderCpService orderCpService;

    @MDCLog
    @Scheduled(cron = "* * 5-23 * * ?")
    public void updateStatus() {
        log.info("********* 开始修改改签状态 *********");
        //获取状态为 的改签订单
        List<ChangePo> changes = changeService.selectByStatus(Lists.newArrayList(ChangeStatus.CHANGE_INIT), 1);
        if (changes.isEmpty()) {
            log.info("待修改改签单 changePos 为空，不做处理");
            return;
        }
        ChangePo change = changes.get(0);
        Integer changeId = change.getChangeId();
        String orderId = change.getOrderId();
        log.info("改签单OrderId:{}, ChangeId:{}", orderId, changeId);

        //查询原单信息
        OrderPo orderPo = orderService.selectByOrderId(orderId);
        String supplierType = orderPo.getSupplierType();
        supplierType = StringUtils.isBlank(supplierType) ? SupplierCode.KYFW : supplierType;
        log.info("supplierType:{}{}", supplierType, SupplierCode.MESSAGE);

        change.setSupplierType(supplierType);
        change.setMyOrderId(orderPo.getMyOrderId());
        change.setSupplierOrderId(orderPo.getSupplierOrderId());

        //判断是否是高铁 重提提交
        List<ChangeCpPo> changeCpPos = changeCpService.selectByChangeId(changeId);
        if (CollectionUtils.isEmpty(changeCpPos)) {
            log.info("根据ChangeId查询出来的passenger为空，该单可能不是最新的订单");
            change.setChangeStatus(ChangeStatus.CHANGE_FAIL);
            int updateResult = changeService.updateByChangeId(change, changeId);
            log.info("改签单信息修改结果:{}", updateResult);
            historyService.insertChangeLog(orderId, changeId, "不是最新的改签订单，置为失败");
            return;
        }

        //修改乘客的 取票号
        for (ChangeCpPo changeCp : changeCpPos) {
            String cpId = changeCp.getCpId();
            //根据cpid查询原票信息
            OrderCpPo orderCp = orderCpService.selectByCpId(cpId);
            changeCp.setOldSubSequence(orderCp.getSubOutticketBillno());
            //更新乘客信息
            changeCpService.updateByNewCpId(changeCp, changeCp.getNewCpId());
        }

        //更新改签单的状态
        change.setChangeStatus(ChangeStatus.CHANGE_WAIT);
        changeService.updateByChangeId(change, changeId);
    }
}
