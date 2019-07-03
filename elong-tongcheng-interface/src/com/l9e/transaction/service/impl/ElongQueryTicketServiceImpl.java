package com.l9e.transaction.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseService;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.ElongQueryTicketService;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.ParamCheckUtil;
@Service("elongQueryTicketService")
public class ElongQueryTicketServiceImpl extends BaseService implements ElongQueryTicketService{
	private static final Logger logger=Logger.getLogger(ElongQueryTicketServiceImpl.class);
	@Resource
	private CommonService commonService;
	@Override
	public void queryTicket(HttpServletRequest request,
			HttpServletResponse response) {
	}
}
