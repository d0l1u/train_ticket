package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.OrderService;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.MobileMsgUtil;

/**
 * Common
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController{
	protected static final Logger logger = Logger.getLogger(CommonController.class);
	

}
