package com.l9e.transaction.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.TrainNewStationVo;

@Controller
@RequestMapping("/chunqiu/station")
public class QueryStationInfoController {
	private static final Logger logger = Logger.getLogger(QueryStationInfoController.class);
	
	@Resource
	private QueryTicketService ticketService;
	
	@RequestMapping("/subwayName.jhtml")
	public String queryWayStation(HttpServletRequest request, HttpServletResponse response){
		logger.info("获取途径车站信息！");
		String checi = request.getParameter("checi");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			checi = ticketService.queryTheCheciForStation(checi);
			List<TrainNewStationVo> list = ticketService.queryWayStationInfo(checi);
			
			if(list==null||list.size()==0){
				out.write("");
				return null;
			}
			for(TrainNewStationVo tnv: list){
				tnv.reSetInterval();
			}
			JSONArray jsonArray = JSONArray.fromObject(list);  
			out.write(jsonArray.toString());
		}catch(Exception e){
			out.write("");
			logger.error("途径车站解析失败！",e);
		}
		return null;
	}
}
