package com.l9e.transaction.controller;

import java.io.IOException;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.exception.WorkerException;
import com.l9e.transaction.service.PayCardService;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.PayCard;
import com.l9e.transaction.vo.Result;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.JacksonUtil;

/**
 * 机器人服务接口
 * 
 * @author licheng
 * 
 */
@Controller
@RequestMapping("/worker")
public class WorkerController extends BaseController {

	private static final Logger logger = Logger.getLogger(WorkerController.class);

	@Resource
	private WorkerService workerService;

	@Resource
	private PayCardService payCardService;

	/**
	 * 获取机器人 type : 类型
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getWorker")
	public void getWorker(HttpServletRequest request, HttpServletResponse response) {
		String millis = String.valueOf(System.currentTimeMillis());
		String logid = "[" + millis.substring(millis.length() - 4) + new Random().nextInt(99) + "] ";
		Result result = new Result();
		try {
			String typeStr = getParam(request, "type");
			logger.info(logid + "Get Worker Type:" + typeStr);

			if (StringUtils.isEmpty(typeStr)) {
				logger.info(logid + "请求获取机器人没有传入类型type参数");
				throw new WorkerException("未传入类型参数");
			}
			Integer workerType = Integer.valueOf(typeStr);

			Worker worker = workerService.getWorker(workerType, logid);
			if (worker == null) {
				logger.info(logid + "没有可用机器人");
				throw new WorkerException("没有可用机器人");
			}

			String workerStatus = worker.getWorkerStatus();
			Integer workerId = worker.getWorkerId();

			if (StringUtils.isNotBlank(workerStatus) && workerStatus.equals(Worker.STATUS_STOP)) {
				logger.info(logid + "机器人已被封停，无法使用:" + workerId);
				throw new WorkerException("机器人已封停");
			}
			if (workerType == Worker.TYPE_BOOK) {
				if (worker.getWorkerStatus() == null || !worker.getWorkerStatus().equals(Worker.STATUS_FREE)) {
					logger.info("机器状态异常,worker" + worker.getWorkerId() + ", " + worker.getWorkerName() + ", 异常状态 : " + worker.getWorkerStatus());
					throw new WorkerException("机器人状态异常");
				}
				// worker.setWorkerStatus(Worker.STATUS_FREE);
				worker.setWorkerStatus(null);
			} else if (workerType == Worker.TYPE_PAY) {
				worker.setWorkerStatus(null);
			} else if (workerType == Worker.TYPE_VERIFY) {
				worker.setWorkerStatus(null);
			} else if (workerType == Worker.TYPE_REGIST) {
				worker.setWorkerStatus(null);
			}

			worker.setWorkNum(worker.getWorkNum() + 1);// 使用次数+1
			worker.setWorkTime("workTime");// 更新使用时间
			worker.setSingleOrder(1);// 有订单下单

			workerService.updateWorker(worker);
			result.setData(worker);
		} catch (WorkerException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info(logid + "【getWorker() Exception】" + e.getMessage(), e);
			result.setMsg("系统异常");
			result.setSuccess(false);
		}
		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			logger.info(logid + "【getWorker() printJson Exception】", e);
		}
	}

	/**
	 * 获取机器人 id : 机器人主键
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getWorkerById")
	public void getWorkerById(HttpServletRequest request, HttpServletResponse response) {

		String idStr = getParam(request, "workerId");

		Result result = new Result();

		try {
			if (StringUtils.isEmpty(idStr)) {
				logger.info("请求获取机器人没有传入类型id参数");
				throw new WorkerException("没有传入id参数");
			}

			Integer id = null;
			try {
				id = Integer.valueOf(idStr);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("机器人id参数异常 type : " + idStr);
				throw new WorkerException("id异常");
			}

			Worker worker = workerService.queryWorkerById(id);
			if (worker == null) {
				logger.info("没有可用机器人");
				throw new WorkerException("没有可用机器人");
			}

			if (worker.getWorkerStatus() != null && worker.getWorkerStatus().equals(Worker.STATUS_STOP)) {
				logger.info("机器人已被封停，无法使用,worker : " + worker.getWorkerId());
				throw new WorkerException("机器人已封停");
			}

			result.setData(worker);
		} catch (WorkerException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("请求获取机器人异常，e : " + e.getMessage());
			result.setMsg("系统异常");
			result.setSuccess(false);
			e.printStackTrace();
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 停用机器人并启用备用机器人
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/stopAndStartPreparedWorker")
	public void stopAndStartPreparedWorker(HttpServletRequest request, HttpServletResponse response) {

		String workerIdStr = getParam(request, "workerId");
		String reason = getParam(request, "reason");

		logger.info("停用机器人，workerId : " + workerIdStr + ", reason : " + reason);

		Result result = new Result();
		try {
			Integer workerId = null;
			try {
				workerId = Integer.valueOf(workerIdStr);
			} catch (NumberFormatException e) {
				logger.info("workerId参数非数字主键，转换异常:" + workerIdStr);
				throw new WorkerException("机器人id参数异常");
			}

			Worker stopWorker = workerService.stopWorker(workerId, reason);
			if (stopWorker == null) {
				logger.info("停用机器人失败, workerId : " + workerIdStr);
				throw new WorkerException("停用机器人失败");
			}

			Worker preparedWorker = workerService.startPreparedWorker(stopWorker.getWorkerType());
			if (preparedWorker == null) {
				logger.info("启用备用机器人失败,类型：" + stopWorker.getWorkerType());
				result.setMsg("备用机器人启用失败");
			} else if (preparedWorker.getWorkerType() != null && preparedWorker.getWorkerType() == Worker.TYPE_PAY) {
				PayCard payCard = payCardService.getCardByWorkerId(stopWorker.getWorkerId());
				if (payCard == null) {
					logger.info("获取停用支付机器人绑定支付账号信息失败,workerId: " + workerIdStr);
					result.setMsg("停用支付机器人，支付账号信息获取失败");
				} else {
					payCard.setWorkerId(preparedWorker.getWorkerId());
					payCardService.updatePayCard(payCard);
				}
			}

		} catch (WorkerException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("停用并启用备用机器人异常，e : " + e.getMessage());
			result.setMsg("系统异常");
			result.setSuccess(false);
			e.printStackTrace();
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改机器人：分子操作
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateWorker/{option}")
	public void updateWorker(HttpServletRequest request, HttpServletResponse response, @PathVariable String option) {

		Result result = new Result();

		try {
			if (StringUtils.isEmpty(option)) {
				logger.info("未指定具体操作");
				throw new WorkerException("未指定具体操作");
			} else if (option.equalsIgnoreCase("release")) {
				releaseWorker(result, request, response);
			} else if (option.equalsIgnoreCase("preparedSign")) {
				// preparedSignWorker(result, request, response);
			} else {
				logger.info("不支持的操作，option : " + option);
				throw new WorkerException("不支持的操作");
			}
		} catch (WorkerException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("修改机器人异常，e : " + e.getMessage());
			result.setMsg("系统异常");
			result.setSuccess(false);
			e.printStackTrace();
		}

		try {
			result.setMsg("机器释放成功！");
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放机器人 id : 机器人id
	 * 
	 * @param result
	 * @param request
	 * @param response
	 */
	private void releaseWorker(Result result, HttpServletRequest request, HttpServletResponse response) throws WorkerException {

		String idStr = getParam(request, "workerId");

		if (StringUtils.isEmpty(idStr)) {
			logger.info("释放机器人，缺少机器人id");
			throw new WorkerException("没有传入id参数");
		}

		Integer id = null;

		try {
			id = Integer.valueOf(idStr);
		} catch (NumberFormatException e) {
			logger.info("释放机器人，id转换异常，workerId : " + idStr);
			throw new WorkerException("id参数转换异常");
		}

		Worker worker = workerService.queryWorkerById(id);
		if (worker == null) {
			logger.info("没有机器人信息，无法释放, workerId : " + idStr);
			throw new WorkerException("没有机器人信息");
		}

		if (Worker.STATUS_STOP.equalsIgnoreCase(worker.getWorkerStatus())) {
			logger.info("机器人已停用，不作释放操作, workerId : " + idStr);
			throw new WorkerException("机器人已停用");
		}

		worker.setWorkerStatus(Worker.STATUS_FREE);
		worker.setSingleOrder(0);
		worker.setWorkerLock(0);// 机器解锁
		workerService.updateWorker(worker);
	}

	/**
	 * 随机获取一个java脚本机器人
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getOneRandJavaWorker")
	public void getOneRandJavaWorker(HttpServletRequest request, HttpServletResponse response) {

		Result result = new Result();

		try {

			Worker worker = workerService.queryOneJavaWorker();
			if (worker == null) {
				logger.info("没有可用的java脚本机器人");
				throw new WorkerException("没有可用的java脚本机器人");
			}

			if (worker.getWorkerStatus() != null && worker.getWorkerStatus().equals(Worker.STATUS_STOP)) {
				logger.info("java脚本机器人已被封停，无法使用,worker : " + worker.getWorkerId());
				throw new WorkerException("java脚本机器人已封停");
			}

			worker.setWorkNum(worker.getWorkNum() + 1);// 使用次数+1
			worker.setWorkTime("workTime");// 更新使用时间
			worker.setSingleOrder(1);// 有订单下单

			workerService.updateWorker(worker);

			result.setData(worker);
		} catch (WorkerException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("请求获取java脚本机器人异常，e : " + e.getMessage());
			result.setMsg("系统异常");
			result.setSuccess(false);
			e.printStackTrace();
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
