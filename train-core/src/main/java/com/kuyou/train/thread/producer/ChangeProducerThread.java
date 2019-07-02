package com.kuyou.train.thread.producer;

import com.google.common.collect.Lists;
import com.kuyou.train.common.constant.ChangeMessage;
import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.constant.SupplierCode;
import com.kuyou.train.common.enums.SeatType;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.po.AccountPo;
import com.kuyou.train.entity.po.ChangeCpPo;
import com.kuyou.train.entity.po.ChangePo;
import com.kuyou.train.entity.po.OrderCpPo;
import com.kuyou.train.entity.req.ChangeOrderReq;
import com.kuyou.train.entity.req.ChangeOrderTicketReq;
import com.kuyou.train.thread.BaseThread;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ChangeThread
 *
 * @author taokai3
 * @date 2018/11/2
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ChangeProducerThread extends BaseThread<ChangePo> {

    private ChangePo changePo;

    @Override
    public void execute() {
        Integer changeId = changePo.getChangeId();
        String orderId = changePo.getOrderId();
        String myOrderId = changePo.getMyOrderId();
        log.info("改签订单信息changeId:{}, orderId:{}, myOrderId:{}", changeId, orderId, myOrderId);

        //根据changeId 查询改签订单信息
        List<ChangeCpPo> changeCpPos = changeCpService.selectByChangeId(changeId);
        if (changeCpPos.isEmpty()) {
            log.info("该订单不是最新的订单，无票失败处理");
            historyService.insertChangeLog(orderId, changeId, "不是最新的订单，无票失败");
            changeService.updateStatus(changeId, ChangeStatus.CHANGE_FAIL, ChangeMessage.WU_PIAO);
            return;
        }

        //时间判断
        if (changePo.getCreateTime().before(DateUtil.add(-5, TimeUnit.DAYS))) {
            log.info("5天前的订单，默认转成无票失败,changeId:{}, orderId:{}", changeId, orderId);
            historyService.insertChangeLog(orderId, changeId, "3天前的订单，默认转成无票失败");
            changeService.updateStatus(changeId, ChangeStatus.CHANGE_FAIL, ChangeMessage.WU_PIAO);
            return;
        }

        //判断原车是否已经发车
        Date fromTime = changePo.getFromTime();
        if (fromTime != null && fromTime.before(new Date())) {
            historyService.insertChangeLog(orderId, changeId, "原车已过发车时间");
            //人工处理
            log.info("原车已过发车时间,转人工处理");
            changeService.updateStatusPre(changeId, ChangeStatus.CHANGE_ING, ChangeStatus.CHANGE_MANAUL);
            return;
        }

        for (ChangeCpPo changeCpPo : changeCpPos) {
            //判断取票号是否存在
            String oldSubSequence = changeCpPo.getOldSubSequence();
            if (StringUtils.isNotBlank(oldSubSequence)) {
                continue;
            }

            //如果不存在，通过cpId查询预订占位乘客，获取取票号
            String cpId = changeCpPo.getCpId();
            String newCpId = changeCpPo.getNewCpId();
            OrderCpPo orderCpPo = orderCpService.selectByCpId(cpId);
            oldSubSequence = orderCpPo.getSubOutticketBillno();
            log.info("cpId:{}, newCpId:{}, oldSubSequence:{}", cpId, newCpId, oldSubSequence);
            changeCpPo.setOldSubSequence(oldSubSequence);

            //根据 new_cp_id 更新乘客车票号
            changeCpService.updateByNewCpId(changeCpPo, newCpId);
        }

        //判断供货商来源
        String supplierOrderId = changePo.getSupplierOrderId();
        String supplierType = changePo.getSupplierType();
        log.info("supplierOrderId:{}, supplierType:{}{}", supplierOrderId, supplierType, SupplierCode.MESSAGE);

        //获取账号密码
        String accountId = changePo.getAccountId();

        AccountPo accountPo = null;
        if (StringUtils.isNotBlank(accountId)) {
            log.info("根据accountId:{}查询账号密码", accountId);
            accountPo = accountService.selectByAccountId(Integer.valueOf(accountId));
        }

        //判断是否可以批量改签
        boolean canBatch = true;
        for (ChangeCpPo changeCpPo : changeCpPos) {
            String changeSeatType = changeCpPo.getChangeSeatType();
            String seatType = changeCpPo.getSeatType();

            //判断是否是卧铺
            if (SeatType.WO_PU_LIST.contains(changeSeatType) || SeatType.WO_PU_LIST.contains(seatType)) {
                canBatch = false;
                break;
            }
        }

        Boolean isChangeTo = changePo.getIschangeto() == null ? false : changePo.getIschangeto();
        //组装订单参数
        ChangeOrderReq changeReq = new ChangeOrderReq();
        if (accountPo != null) {
            changeReq.setUsername(accountPo.getUsername());
            changeReq.setPassword(accountPo.getPassword());
        }
        changeReq.setChangeId(changePo.getChangeId());
        changeReq.setOrderId(orderId);
        changeReq.setMyOrderId(changePo.getMyOrderId());
        changeReq.setSupplierOrderId(changePo.getSupplierOrderId());
        changeReq.setSequence(changePo.getOutTicketBillno());
        changeReq.setTrainCode(changePo.getChangeTrainNo());
        changeReq.setFromDate(changePo.getChangeTravelTime());

        //车站获取
        String fromStationName = changePo.getFromCity();
        String toStationName = changePo.getToCity();
        String fromStationCode = dataJedisClient.hget(KeyConstant.STATION_NAME2CODE, fromStationName);
        String toStationCode = dataJedisClient.hget(KeyConstant.STATION_NAME2CODE, toStationName);

        //改签信息
        changeReq.setFromStationName(fromStationName);
        changeReq.setToStationName(toStationName);
        changeReq.setFromStationCode(fromStationCode);
        changeReq.setToStationCode(toStationCode);
        changeReq.setChooseSeats(changePo.getChooseseats());
        changeReq.setIsChangeTo(isChangeTo);

        List<ChangeOrderTicketReq> changeTicketDtos = Lists.newArrayList();
        for (ChangeCpPo changeCpPo : changeCpPos) {
            String cpId = changeCpPo.getNewCpId();
            ChangeOrderTicketReq changeTicketDto = new ChangeOrderTicketReq(changeCpPo);
            changeTicketDto.setName(changeCpPo.getUserName());
            //非航天华有，并且不可以批量改签
            if (!canBatch && !SupplierCode.HTHY.equals(supplierType)) {
                log.info("newCpId:{}, 进行单独isChangeTo:{}[false:改签, true:变更到站], data:{}", cpId, isChangeTo, changeReq);
                historyService.insertChangeLog(orderId, changeId, String.format("进行单独%s", isChangeTo ? "变更到站" : "改签"));
                changeReq.setTickets(Lists.newArrayList(changeTicketDto));
                //推送到redis
                orderJedisClient.lpush(KeyConstant.CHANGE_REQ, changeReq.toString());
            } else {
                changeTicketDtos.add(changeTicketDto);
            }
        }
        if (changeTicketDtos.isEmpty()) {
            return;
        }
        changeReq.setTickets(changeTicketDtos);

        log.info("进行批量isChangeTo:{}[false:改签, true:变更到站]", isChangeTo);
        if (SupplierCode.HTHY.equals(supplierType)) {
            historyService.insertChangeLog(orderId, changeId, String.format("航天华有进行批量%s", isChangeTo ? "变更到站" : "改签"));
            hthyService.changeOrder(changeReq);
        } else {
            historyService.insertChangeLog(orderId, changeId, String.format("进行批量%s", isChangeTo ? "变更到站" : "改签"));
            //推送到redis
            orderJedisClient.lpush(KeyConstant.CHANGE_REQ, changeReq.toString());
            log.info("等待机器人改签占位结果");
        }

    }

}
