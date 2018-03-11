package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.l9e.common.BaseController;
import com.l9e.transaction.service.TuniuRefundService;
import com.l9e.transaction.service.TuniuUrgeRefundService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.TuniuBookVo;
import com.l9e.transaction.vo.TuniuRefundVo;
import com.l9e.transaction.vo.TuniuUrgeRefund;
import com.l9e.util.ExcelUtil;
import com.l9e.util.ImportExcelUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.sun.org.apache.xml.internal.security.Init;

@Controller
@RequestMapping("/tuniuUrgeRefund")
public class TuniuUrgeRefundController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(TuniuUrgeRefundController.class);

	@Resource
	private TuniuRefundService tuniuRefundService;

	@Resource
	private TuniuUrgeRefundService tuniuUrgeRefundService;

	/**
	 * 进入查询页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryUrgeRefundPage.do")
	public String queryRefundPage(HttpServletRequest request,
			HttpServletResponse response) {
		return "redirect:/tuniuUrgeRefund/queryUrgeRefundList.do?urge_status=11";
	}

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryUrgeRefundList.do")
	public String queryRefundTicket(HttpServletRequest request,
			HttpServletResponse response) {

		/****************** 查询条件 ********************/
		String order_id = this.getParam(request, "order_id");// 订单ID
		String begin_create_time = this.getParam(request, "begin_create_time");// 开始时间
		String end_create_time = this.getParam(request, "end_create_time");// 结束时间
		List<String> urge_status = this.getParamToList(request, "urge_status");// 催退款状态
		/****************** 查询Map ********************/
		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (order_id.trim().length() > 0) {
			paramMap.put("order_id", order_id);
		} else {
			paramMap.put("begin_create_time", begin_create_time);// 开始时间
			paramMap.put("end_create_time", end_create_time);// 结束时间
			paramMap.put("urge_status", urge_status);
		}
		/****************** 分页条件开始 ********************/
		int totalCount = tuniuUrgeRefundService.queryUrgeRefundCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数

		/****************** 查询开始 ********************/
		List<TuniuUrgeRefund> urgeRefundList = tuniuUrgeRefundService
				.queryUrgeRefundList(paramMap);

		/****************** 封装返回值 ********************/
		request.setAttribute("urgeRefundList", urgeRefundList);
		if (order_id.trim().length() > 0) {
			request.setAttribute("order_id", order_id);
		} else {
			request.setAttribute("urge_statusStr", urge_status.toString());
			request.setAttribute("begin_create_time", begin_create_time);
			request.setAttribute("end_create_time", end_create_time);
		}
		request.setAttribute("urge_status_list",
				TuniuUrgeRefund.getUrgeStatusList());
		return "tuniuUrgeRefund/tuniuUrgeRefundList";
	}

	/**
	 * 查询明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryUrgeRefundInfo.do")
	public String queryUrgeRefundInfo(HttpServletRequest request,
			HttpServletResponse response) {
		/****************** 查询条件 ********************/
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		/********************* 创建容器 *********************/
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("order_id", order_id);
		param.put("cp_id", cp_id);
		/****************** 查询开始 ********************/
		TuniuUrgeRefund urgeRefundInfo = tuniuUrgeRefundService
				.queryUrgeRefundInfo(param);
		Map<String, String> orderInfo = tuniuRefundService
				.queryBookOrderInfo(order_id);
		List<Map<String, String>> cpList = tuniuRefundService
				.queryBookOrderInfoCp(order_id);

		List<Map<String, Object>> history = tuniuRefundService
				.queryHistroyByCpId(cp_id);
		/****************** request绑定 ********************/
		request.setAttribute("urgeRefundInfo", urgeRefundInfo);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("cpList", cpList);
		request.setAttribute("history", history);
		request.setAttribute("cp_id", cp_id);
		request.setAttribute("order_id", order_id);
		request.setAttribute("refund_statuses",
				TuniuRefundVo.getRefund_Status());
		request.setAttribute("refund_types", TuniuRefundVo.getRefund_Types());
		request.setAttribute("order_statuses", TuniuBookVo.getBookStatus());
		request.setAttribute("ticket_types", TuniuRefundVo.getTicket_Types());
		request.setAttribute("ids_types", TuniuRefundVo.getIdstype());
		request.setAttribute("seat_types", TuniuBookVo.getSeatType());
		request.setAttribute("urge_status_list",
				TuniuUrgeRefund.getUrgeStatusList());

		return "tuniuUrgeRefund/tuniuUrgeRefundInfo";
	}

	@RequestMapping("/toUpdateUrgeRefundInfo.do")
	public String toUpdateUrgeRefundInfo(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("urge_refund_id",
				this.getParam(request, "urge_refund_id"));
		request.setAttribute("order_id", this.getParam(request, "order_id"));
		request.setAttribute("cp_id", this.getParam(request, "cp_id"));
		return "tuniuUrgeRefund/tuniuUrgeRefundUpdate";
	}

	/**
	 * 更新催退款信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateUrgeRefundInfo.do")
	@ResponseBody
	public int updateUrgeRefundInfo(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();// 当前登录人
		String urge_refund_id = this.getParam(request, "urge_refund_id");
		String refund_money = this.getParam(request, "refund_money");
		String urge_status = this.getParam(request, "urge_status");
		String remark = this.getParam(request, "remark");
		logger.info(opt_person + "更新催退款信息，urge_refund_id:" + urge_refund_id);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("urge_refund_id", new Integer(urge_refund_id));
		param.put("refund_money", refund_money);
		param.put("urge_status", urge_status);
		if (urge_status.equals("22")) {
			String order_id = this.getParam(request, "order_id");
			String cp_id = this.getParam(request, "cp_id");
			Map<String, Object> refundParam = new HashMap<String, Object>();
			refundParam.put("order_id", order_id);
			refundParam.put("cp_id", cp_id);
			refundParam.put("refund_status", "11");// 退款完成
			String refund_time = tuniuRefundService
					.queryRefundTime(refundParam);
			if (refund_time == null || refund_time.equals("")) {
				return -1;
			}
			param.put("refund_time", refund_time);
		}
		param.put("remark", remark);
		param.put("opt_person", opt_person);
		int result = tuniuUrgeRefundService.updateUrgeRefundInfo(param);
		return result;

	}

	/**
	 * 更新多个催退款信息，未核实到退款信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateUrgeRefundList.do")
	@ResponseBody
	public void updateRefundUrgeList(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();// 当前登录人
		String urgeRefundIdStr = this.getParam(request, "urgeRefundIdStr");

		String urge_status = this.getParam(request, "urge_status");
		String remark = this.getParam(request, "remark");
		logger.info(opt_person + "更新催退款信息，urgeRefundIdStr:" + urgeRefundIdStr);
		String[] refundStr = urgeRefundIdStr.split(",");
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (String str : refundStr) {
			String[] temp = str.split("\\|");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", temp[0]);
			map.put("cp_id", temp[1]);
			listMap.add(map);
		}

		int fail = 0;// 失败个数
		int succ = 0;// 成功个数

		for (Map<String, Object> param : listMap) {
			param.put("remark", remark);
			param.put("urge_status", urge_status);
			param.put("opt_person", opt_person);
			int count = 0;
			try {
				count = tuniuUrgeRefundService.updateUrgeRefundByOrderId(param);
			} catch (Exception e) {
				logger.info("******", e);
			}
			if (count == 1) {
				succ++;
			} else {
				fail++;
			}
		}

		String result = "成功：" + succ + "个,失败：" + fail + "个";

		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("excelExportForUrgeRefund.do")
	public void excelExportForUrgeRefund(HttpServletRequest request,
			HttpServletResponse response) {

		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();// 得到操作人的姓名
		logger.info(opt_name + "点击了催退款管理导出******");

		/****************** 查询条件 ********************/
		String order_id = this.getParam(request, "order_id");// 订单ID
		String begin_create_time = this.getParam(request, "begin_create_time");// 开始时间
		String end_create_time = this.getParam(request, "end_create_time");// 结束时间
		List<String> urge_StatusList = this.getParamToList(request,
				"urge_status"); // 退款状态
		String opt_person = this.getParam(request, "opt_person");

		/****************** 查询Map ********************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (order_id.trim().length() > 0) {
			paramMap.put("order_id", order_id);
		} else {
			paramMap.put("begin_create_time", begin_create_time);// 开始时间
			paramMap.put("end_create_time", end_create_time);// 结束时间
			paramMap.put("opt_person", opt_person);
			paramMap.put("urge_status", urge_StatusList);
		}
		/****************** 分页条件开始 ********************/
		int totalCount = tuniuUrgeRefundService.queryUrgeRefundCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", 0);// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount() * totalCount);// 每页显示的条数

		/****************** 查询开始 ********************/
		List<TuniuUrgeRefund> urgeRefundList = tuniuUrgeRefundService
				.queryUrgeRefundListForExcel(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (TuniuUrgeRefund m : urgeRefundList) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(String.valueOf(m.getUrge_refund_id()));// 主键
			linkedList.add(m.getOrder_id());// 订单号
			linkedList.add(m.getCp_id());// 车票单号
			linkedList.add(TuniuUrgeRefund.URGEREFUNDSTATUS.get(m
					.getUrge_status()));// 催退款状态
			linkedList.add(m.getOut_ticket_billno());// 12306出票单号
			linkedList.add(m.getRefund_money());// 退款金额
			linkedList.add(m.getRefund_time());// 退款时间
			linkedList.add(m.getRemark());// 备注
			linkedList.add(m.getFail_reason());// 失败原因
			linkedList.add(m.getOpt_time());// 最后操作时间
			linkedList.add(m.getCreate_time());// 创建时间
			linkedList.add(m.getOpt_person());// 操作人
			list.add(linkedList);
		}

		String title = "催退款订单导出明细";
		String date = createDate(begin_create_time, end_create_time);
		String filename = "催退款订单导出表格.xls";
		String[] secondTitles = { "序号", "主键", "订单号", "车票单号", "催退款状态",
				"12306出票单号", "退款金额", "退款时间", "备注", "失败原因", "最后操作时间", "创建时间",
				"操作人" };

		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);

	}

	private String createDate(String begin_info_time, String end_info_time) {
		String date = "日期：";
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		if (begin_info_time.equals(end_info_time)
				|| begin_info_time == end_info_time) {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				date += ss.format(new Date());
			} else {
				date += begin_info_time;
			}
		} else {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += ss.format(new Date()) + "之前";
				} else {
					date += end_info_time + "之前";
				}
			} else {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += begin_info_time + "-------" + ss.format(new Date());
				} else {
					date += begin_info_time + "-------" + end_info_time;
				}
			}
		}
		return date;
	}

	@ResponseBody
	@RequestMapping(value = "ajaxUpload.do", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void ajaxUploadExcel(HttpServletRequest request,
			HttpServletResponse response) {
		String msg = "";
		String name="";
		int succ=0;
		int fail=0;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			System.out.println("通过 jquery.form.js 提供的ajax方式上传文件！");
			InputStream in = null;
			List<List<Object>> listob = null;
			List<TuniuUrgeRefund> listRefunds=new ArrayList<TuniuUrgeRefund>();
			MultipartFile file = multipartRequest.getFile("excelFile");
			name=file.getOriginalFilename();
			System.out.println(name);
			if (file.isEmpty()) {
				throw new Exception("文件不存在！");
			}
			Map<String, String> urgeMap = new HashMap<String, String>();
			urgeMap.put("处理中", "11");
			urgeMap.put("已退款", "22");
			urgeMap.put("退款失败", "33");
			urgeMap.put("其他", "44");
			in = file.getInputStream();
			listob = new ImportExcelUtil().getBankListByExcel(in,
					file.getOriginalFilename());
			// 该处可调用service相应方法进行数据保存到数据库中，现只对数据输出
			for (int i = 0 + 3; i < listob.size(); i++) {
				List<Object> lo = listob.get(i);
				TuniuUrgeRefund vo = new TuniuUrgeRefund();
				vo.setUrge_refund_id(Integer.parseInt(String.valueOf(lo.get(1)).replace(" ", "")));
				vo.setOrder_id(String.valueOf(lo.get(2)).replace(" ", ""));
				vo.setCp_id(String.valueOf(lo.get(3)).replace(" ", ""));
				vo.setUrge_status(urgeMap.get(String.valueOf(lo.get(4))).replace(" ", ""));
				vo.setOut_ticket_billno(String.valueOf(lo.get(5)).replace(" ", ""));
				vo.setRefund_money(String.valueOf(lo.get(6)).replace(" ", ""));
				vo.setRefund_time(String.valueOf(lo.get(7)).replace(" ", ""));
				vo.setRemark(String.valueOf(lo.get(8)).replace(" ", ""));
				vo.setFail_reason(String.valueOf(lo.get(9)).replace(" ", ""));
				vo.setCreate_time(String.valueOf(lo.get(11)).replace(" ", ""));
				vo.setOpt_person(String.valueOf(lo.get(12)).replace(" ", ""));
				listRefunds.add(vo);
			}
			
			//更新数据库
			for (TuniuUrgeRefund tuniuUrgeRefund : listRefunds) {
				int effect_num=tuniuUrgeRefundService.updateUrgeRefund(tuniuUrgeRefund);
				if(effect_num==1) {
					succ++;
				}else {
					fail++;
				}
				
			}
			
			
			
		} catch (Exception e) {
			logger.info("****", e);
			msg = "上传数据处理异常";
		}
		msg = "文件导入成功！";
		PrintWriter out = null;
		response.setCharacterEncoding("utf-8");// 防止ajax接受到的中文信息乱码
		try {
			out = response.getWriter();
		} catch (IOException e) {
			logger.info("***", e);
		}
		
		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("name", name);
		resultMap.put("msg", msg);
		resultMap.put("succ_num", succ);
		resultMap.put("fail_num", fail);
		String result_str=JSON.toJSONString(resultMap);
		out.print(result_str);
		out.flush();
		out.close();
	}

}
