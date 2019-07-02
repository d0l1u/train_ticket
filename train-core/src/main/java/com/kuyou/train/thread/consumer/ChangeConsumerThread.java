package com.kuyou.train.thread.consumer;

import com.kuyou.train.common.constant.ChangeType;
import com.kuyou.train.common.status.ChangeNotifyStatus;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.entity.dto.TrainTimeDto;
import com.kuyou.train.entity.po.ChangeCpPo;
import com.kuyou.train.entity.po.ChangePo;
import com.kuyou.train.entity.resp.ChangeOrderResp;
import com.kuyou.train.entity.resp.ChangeOrderTicketResp;
import com.kuyou.train.thread.BaseThread;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * ChangeConsumerThread
 *
 * @author taokai3
 * @date 2018/11/7
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ChangeConsumerThread extends BaseThread<ChangeOrderResp> {

    private ChangeOrderResp changeOrderResp;

    @Override
    public void execute() {
        Integer changeId = changeOrderResp.getChangeId();
        String orderId = changeOrderResp.getOrderId();
        String sequence = changeOrderResp.getSequence();
        String code = changeOrderResp.getCode();
        String message = changeOrderResp.getMessage();
        String robotIp = changeOrderResp.getRobotIp();

        //查询原单结果
        ChangePo changePo = changeService.selectByChangeId(changeId);
        String status = changePo.getChangeStatus();
        if (!ChangeStatus.CHANGE_ING.equals(status) && !ChangeStatus.CHANGE_MANAUL.equals(status)) {
            log.info("重复结果，不做处理");
            historyService.insertChangeLog(orderId, changeId, "重复结果，不做处理");
            return;
        }
        //message 匹配具体的错误信息 TODO

        log.info("changeId:{}, orderId:{}, sequence:{}, robotIp:{}", changeId, orderId, sequence, robotIp);
        log.info("code:{}, message:{}", code, message);

        if (!"0000".equals(code)) {
            fail(message);
        } else {
            success();
        }
    }

    private void success() {
        Integer changeId = changeOrderResp.getChangeId();
        String orderId = changeOrderResp.getOrderId();

        //查询信息
        ChangePo changePo = changeService.selectByChangeId(changeId);

        Boolean hasSeat = changePo.getHasseat();
        hasSeat = hasSeat == null ? false : hasSeat;
        //1、不改到无座0、允许改到无座票
        log.info("改签是否接受无座票:{} [true:不改到无座票, false:允许改到无座票]", hasSeat);

        List<ChangeOrderTicketResp> tickets = changeOrderResp.getTickets();
        boolean wuZuo = false;
        //判断是否预订无座
        for (ChangeOrderTicketResp ticket : tickets) {
            String seatName = ticket.getSeatName();
            if (StringUtils.isBlank(seatName) || seatName.contains("无")) {
                if (hasSeat) {
                    wuZuo = true;
                }
            }
            //更新乘客信息
            changeCpService.updateByNewCpId(new ChangeCpPo(ticket, changeId), ticket.getCpId());
        }

        String trainCode = changePo.getChangeTrainNo();
        Date fromTime = changeOrderResp.getFromTime();
        Date fromDate = changeOrderResp.getFromDate();
        Date toTime = changeOrderResp.getToTime();
        String fromStationName = changeOrderResp.getFromStationName();
        String toStationName = changeOrderResp.getToStationName();
        TrainTimeDto trainTimeDto = trainTime(trainCode, fromDate, fromTime, toTime, fromStationName, toStationName);
        toTime = trainTimeDto.getToTime();
        fromTime = trainTimeDto.getFromTime();
        String robotIp = changeOrderResp.getRobotIp();

        ChangePo updatePo = new ChangePo();
        int changeType = changeOrderResp.getChangeType();
        log.info("改签类型:{} [1:高->低，0:平价，-1:低->高]", changeType);
        //1:平改 2:高改低 3:低改高
        switch (changeType) {
            case 1:
                historyService.insertChangeLog(orderId, changeId, robotIp + ":高->低");
                changeType = ChangeType.GAO_DI;
                break;
            case -1:
                historyService.insertChangeLog(orderId, changeId, robotIp + ":低->高");
                changeType = ChangeType.DI_GAO;
                //低改高，更新新票金额
                updatePo.setChangeReceiveMoney(changeOrderResp.getTotalPrice());
                break;
            default:
                historyService.insertChangeLog(orderId, changeId, robotIp + ":平价改签");
                changeType = ChangeType.PING_JIA;
                break;
        }

        updatePo.setChangeFromTime(fromTime);
        updatePo.setChangeToTime(toTime);
        updatePo.setBookTicketTime(new Date());
        updatePo.setAlterPayType(changeType);
        updatePo.setPayLimitTime(changeOrderResp.getPayLimitTime());
        updatePo.setChangeStatus(ChangeStatus.CHANGE_PAY);
        updatePo.setFromCity(fromStationName);
        updatePo.setFromStationCode(changeOrderResp.getFromStationCode());
        updatePo.setToCity(toStationName);
        updatePo.setToStationCode(changeOrderResp.getToStationCode());

        if (wuZuo) {
            log.info("不接受无座，转为人工");
            updatePo.setChangeStatus(ChangeStatus.CHANGE_MANAUL);
            //更新订单信息
            changeService.updateByChangeId(updatePo, changeId);
            //插入日志
            historyService.insertChangeLog(orderId, changeId, "不接受无座");
        } else {
            updatePo.setChangeNotifyCount(0);
            updatePo.setChangeNotifyTime(new Date());
            updatePo.setChangeNotifyStatus(ChangeNotifyStatus.NOTIFY_WAIT);

            historyService.insertChangeLog(orderId, changeId, "改签成功");
            //更新订单信息
            changeService.updateByChangeId(updatePo, changeId);

        }
    }

    private void fail(String message) {
        Integer changeId = changeOrderResp.getChangeId();
        String orderId = changeOrderResp.getOrderId();
        //更新订单状态为人工处理，打印操作日志
        changeService.updateStatusById(changeId, ChangeStatus.CHANGE_MANAUL);

        //插入操作日志
        historyService.insertChangeLog(orderId, changeId, message);
    }
}
