package com.kuyou.train.web;

import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.service.CallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HthyCallbackController
 *
 * @author taokai3
 * @date 2018/11/5
 */
@Deprecated
@Slf4j
@Controller
@RequestMapping("/hthy-callback")
public class HthyCallbackController extends BaseController {

    @Resource
    private CallBackService callBackService;


    @MDCLog
    @RequestMapping(value = "/bookOrder")
    public void bookOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("-------------【预订占位回调】-------------");
        String dataJson = getData(request);
        callBackService.bookOrder(dataJson);
        //解析结果
        writerResponse(response);
    }

    @MDCLog
    @RequestMapping(value = "/bookPay")
    public void bookPay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("-------------【预订支付回调】-------------");
        String data = getPost(request);
        data = keyValue2Json(data);
        callBackService.bookPay(data);
        //解析结果
        writerResponse(response);
    }


    @MDCLog
    @RequestMapping(value = "/refund")
    public void refund(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("-------------【退票回调】-------------");
        String dataJson = getData(request);
        callBackService.refund(dataJson);
        //解析结果
        writerResponse(response);
    }

    @MDCLog
    @RequestMapping(value = "/changeOrder")
    public void changeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("-------------【改签占位回调】-------------");
        String backJson = getBackJson(request);
        callBackService.changeOrder(backJson);
        //解析结果
        writerResponse(response);
    }

    @MDCLog
    @RequestMapping(value = "/changePay")
    public void changePay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("-------------【改签支付回调】-------------");
        String backJson = getBackJson(request);
        callBackService.changePay(backJson);
        //解析结果
        writerResponse(response);
    }

}
