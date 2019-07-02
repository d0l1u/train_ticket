package com.kuyou.train.service;

import com.kuyou.train.entity.dto.HthySyncDto;
import com.kuyou.train.entity.dto.TicketInfoDto;
import com.kuyou.train.entity.po.ServicePo;
import com.kuyou.train.entity.req.BookOrderReq;
import com.kuyou.train.entity.req.ChangeOrderReq;
import com.kuyou.train.entity.req.RefundReq;
import com.kuyou.train.entity.req.TicketReq;

/**
 * HthyService
 *
 * @author taokai3
 * @date 2018/11/5
 */
public interface HthyService {

    /**
     * 预订取消
     *
     * @param orderPo
     * @return
     */
    String bookCancel(ServicePo orderPo);

    /**
     * 改签占位
     * @param changeDto
     */
    void changeOrder(ChangeOrderReq changeDto);

    /**
     * 退票
     *
     * @param refundReq
     * @return
     */
    HthySyncDto refund(RefundReq refundReq);

    /**
     * 改签取消
     *
     * @param servicePo
     * @return
     */
    String changeCancel(ServicePo servicePo);

    /**
     * 查询余票
     * @param ticketReq
     * @return
     */
    TicketInfoDto queryTicket(TicketReq ticketReq);

    /**
     * 调用航天华有
     *
     * @param bookOrderReq
     */
    void bookOrder(BookOrderReq bookOrderReq);
}
