package com.kuyou.train.web;

import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.entity.req.CancelReq;
import com.kuyou.train.entity.req.PayReq;
import com.kuyou.train.entity.req.RefundReq;
import com.kuyou.train.entity.resp.CancelResp;
import com.kuyou.train.entity.resp.PayResp;
import com.kuyou.train.entity.resp.RefundResp;
import com.kuyou.train.http.cookie.CacheCookieJar;
import com.kuyou.train.service.CancelService;
import com.kuyou.train.service.PayService;
import com.kuyou.train.service.RefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * TrainController
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Slf4j
@Controller
public class TrainController {

    @Resource
    private RefundService refundService;

    @Resource
    private CancelService cancelService;

    @Resource
    private PayService payService;

    @MDCLog
    @RequestMapping(value = "/refund")
    @ResponseBody
    public RefundResp refund(@RequestBody RefundReq refundReq) {
        log.info("退票参数:{}", refundReq);
        RefundResp refundResp = RefundResp.builder().success(false).orderId(refundReq.getOrderId()).cpId(refundReq.getCpId()).build();
        try {
            refundResp = refundService.refund(refundReq);
        } catch (TrainException e) {
            log.info("退票失败，message:{}", e.getMessage());
            refundResp.setMessage(e.getMessage());
        } catch (Exception e) {
            String simpleName = e.getClass().getSimpleName();
            log.info("退票异常:{}", simpleName, e);
            refundResp.setMessage("系统异常:" + e.getClass().getSimpleName());
        } finally {
            new CacheCookieJar().clearCookie();
        }
        log.info("退票结果:{}", refundResp);
        return refundResp;
    }


    @MDCLog
    @RequestMapping(value = "/cancel")
    @ResponseBody
    public CancelResp cancel(@RequestBody CancelReq cancelReq) {
        log.info("取消参数:{}", cancelReq);
        CancelResp cancelResp = CancelResp.builder().bookFlag(cancelReq.isBookFlag()).build();
        try {
            cancelResp = cancelService.cancel(cancelReq);
        } catch (TrainException e) {
            log.info("取消失败，message:{}", e.getMessage());
            cancelResp.setMessage(e.getMessage());
        } catch (Exception e) {
            String simpleName = e.getClass().getSimpleName();
            log.info("取消异常:{}", simpleName, e);
            cancelResp.setMessage("系统异常:" + e.getClass().getSimpleName());
        } finally {
            new CacheCookieJar().clearCookie();
        }
        log.info("取消结果:{}", cancelResp);
        return cancelResp;
    }

    @MDCLog
    @RequestMapping(value = "/pay")
    @ResponseBody
    public RefundResp pay(@RequestBody PayReq payReq) {
        log.info("支付参数:{}", payReq);
        PayResp payResp = PayResp.builder().orderId(payReq.getOrderId()).changeId(payReq.getChangeId()).success(false).build();
        try {
            //TODO
            payService.pay(payReq);
            throw  new TrainException("TODO");
        } catch (TrainException e) {
            log.info("支付失败，message:{}", e.getMessage());
            payResp.setMessage(e.getMessage());
        } catch (Exception e) {
            String simpleName = e.getClass().getSimpleName();
            log.info("支付异常:{}", simpleName, e);
            payResp.setMessage("系统异常:" + e.getClass().getSimpleName());
        } finally {
            new CacheCookieJar().clearCookie();
        }
        log.info("支付结果:{}", payResp);
        return null;
    }
}
