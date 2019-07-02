package com.kuyou.train.service;

import com.google.common.collect.Lists;
import com.kuyou.train.common.code.Message;
import com.kuyou.train.common.enums.OrderStatusEnum;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.util.BigDecimalUtil;
import com.kuyou.train.common.util.RegexUtil;
import com.kuyou.train.entity.dto.CompleteOrderDto;
import com.kuyou.train.entity.dto.RefundInfoDto;
import com.kuyou.train.entity.kyfw.refund.RefundInfo;
import com.kuyou.train.entity.req.RefundReq;
import com.kuyou.train.entity.resp.RefundResp;
import com.kuyou.train.kyfw.impl.LoginApiRobot;
import com.kuyou.train.kyfw.impl.QueryOrderApiRobot;
import com.kuyou.train.kyfw.impl.RefundApiRobot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * RefundService
 *
 * @author taokai3
 * @date 2018/11/22
 */
@Slf4j
@Service
public class RefundService {

    private static final Pattern TRADE_NO = Pattern.compile("业务流水号:([0-9a-zA-Z]+)");

    @Resource
    private LoginApiRobot loginApiRobot;

    @Resource
    private QueryOrderApiRobot queryOrderApiRobot;

    @Resource
    private RefundApiRobot refundApiRobot;

    /**
     * @param refundReq
     * @return
     */
    public RefundResp refund(RefundReq refundReq) throws IOException {
        String orderId = refundReq.getOrderId();
        String cpId = refundReq.getCpId();
        String sequence = refundReq.getSequence();
        String subSequence = refundReq.getSubSequence();
        String name = refundReq.getName();
        RefundResp refundResp = RefundResp.builder().success(true).orderId(orderId).cpId(cpId).build();

        log.info("退票订单orderId:{}, sequence:{}", orderId, sequence);
        log.info("退票乘客name:{}, cpId:{}, subSequence:{}", name, cpId, subSequence);

        String username = refundReq.getUsername();
        String password = refundReq.getPassword();

        //检查登录状态，如果redis存在登录cookie，则判断cookie是否失效，如果失效，则重新登录
        loginApiRobot.checkLoginStatus(username, password);

        //查询已完成订单
        List<CompleteOrderDto> dtos = queryOrderApiRobot.queryNotTravelOrder(sequence, Lists.newArrayList(subSequence));
        CompleteOrderDto orderDto = dtos.get(0);
        String statusName = orderDto.getStatusName();
        OrderStatusEnum statusEnum = orderDto.getStatusEnum();
        String status = orderDto.getStatus();
        log.info("车票状态:{}-{}", status, statusName);
        if (OrderStatusEnum.REFUND_FINISH.equals(statusEnum)) {
            String tradeNo = RegexUtil.matcher(statusName, TRADE_NO);
            if (StringUtils.isBlank(tradeNo)) {
                //如果 退款流水号为空，则抛出异常
                throw new TrainException(String.format(Message.REFUND_MANUAL, "退款流水号为空"));
            }

            //已退票, 查询退票详情
            RefundInfo refundInfo = refundApiRobot.queryRefundInfo(tradeNo, orderDto.getFromDate());
            if (refundInfo.getTransStatus() != 1) {
                //查询退款信息失败，则抛出异常
                throw new TrainException(String.format(Message.REFUND_MANUAL, "查询退款信息失败"));
            }
            refundResp.setRefundMoney(BigDecimalUtil.dividePrice(refundInfo.getTransAmount()));
            refundResp.setRefundNo(refundInfo.getTradeNo());
        } else if (OrderStatusEnum.PAY_FINISH.equals(statusEnum) || OrderStatusEnum.CHANGE_TICKET.equals(statusEnum) ||
                OrderStatusEnum.ALTER_TICKET.equals(statusEnum)) {
            //获取退票手续费等信息
            RefundInfoDto refundInfoDto = refundApiRobot.returnTicketAffirm(orderDto);
            refundResp.setRefundMoney(refundInfoDto.getReturnPrice());
            refundResp.setFee(refundInfoDto.getReturnRate());
            //进行退票
            refundApiRobot.returnTicket();

            //获取退票流水号
            String tradeNo = refundApiRobot.returnTicketRedirect();
            if (StringUtils.isBlank(tradeNo)) {
                log.info("获取支付流水号为空，进行反查退票结果");
                orderDto = queryOrderApiRobot.queryNotTravelOrder(sequence, Lists.newArrayList(subSequence)).get(0);
                if (OrderStatusEnum.REFUND_FINISH.equals(orderDto.getStatusEnum())) {
                    //退票成功
                    tradeNo = RegexUtil.matcher(statusName, TRADE_NO);
                } else {
                    //退票失败
                    throw new TrainException(String.format(Message.REFUND_MANUAL, "退票失败，请重试"));
                }
            }
            refundResp.setRefundNo(tradeNo);
        } else {
            //其他状态
            refundResp.setSuccess(false);
            refundResp.setMessage(statusName);
        }
        return refundResp;
    }
}
