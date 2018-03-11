package com.l9e.transaction.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;

/**
 * 退订管理
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/refund")
public class RefundController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(RefundController.class);
}
