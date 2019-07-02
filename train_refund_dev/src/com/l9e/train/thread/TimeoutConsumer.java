package com.l9e.train.thread;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.channel.request.IRequest;
import com.l9e.train.channel.request.RequestFactory;
import com.l9e.train.po.DeviceWeight;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.ResultCP;
import com.l9e.train.producerConsumer.distinct.DistinctConsumer;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;


public class TimeoutConsumer extends DistinctConsumer<OrderCP> {

	private Logger logger=Logger.getLogger(this.getClass());
	private static final Random WEIGHT_RANDOM = new Random();

	@Override
	public boolean consume(OrderCP orderbill) {
		TrainServiceImpl service = new TrainServiceImpl();
		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		//start 记录日志
		String optlog =null;
		ResultCP resultCp = null;
		
		String weight = deviceWeight(service);
		
		try {
			//start 选择账号、处理人员和处理类
			logger.info("select request="+orderbill.getOrderId()+" start, weight="+weight);
			IRequest request  = new RequestFactory().select(orderbill);
			logger.info("select request="+orderbill.getOrderId()+" end requestImpl:"+request);
			if(request == null){
				service.refundIsManual(orderbill,null);
				logger.error(orderbill.getOrderId()+" not worker");
				service.insertHistory(orderbill.getOrderId(),orderbill.getCpId(), "没有空闲退票机器人。");
				return false;
			}
			//end
		
			//start 利用处理类和其它信息进行订单的处理
			logger.info("request="+orderbill.getOrderId()+","+orderbill.getCpId()+" start");
			//判断该订单内是否存在别的车票号是06“正在机器退票”状态的，若有则return，若无再发起请求
//			service.ishaveOtherCp(orderbill);
//			if(StringUtils.isEmpty(orderbill.getIsRefunding()) || Integer.parseInt(orderbill.getIsRefunding())>0){
//				logger.info("订单号："+orderbill.getOrderId()+"，车票号："+orderbill.getCpId()+"存在其他正在机器退票的车票号");
//				return false;
//			}
			resultCp = request.request(orderbill, weight);
			String auto_refund_success = "0";//退款自动机器审核完成 0关闭 1开启
			try {
				sysImpl.querySysVal("auto_refund_success");
				if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
					auto_refund_success = sysImpl.getSysVal();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try{
				if (StringUtils.equals(resultCp.getRetValue(), ResultCP.SUCCESS)) {// 订单退票成功
					if("1".equals(auto_refund_success)){
						optlog = resultCp.getWorker().getWorkerName()+"，退票成功，直接退款完成！["+resultCp.getErrorInfo()+"]";
						logger.info(orderbill.getOrderId()+" refund is success!");
						service.refundAutoSuccess(orderbill, resultCp);
					}else{
						optlog = resultCp.getWorker().getWorkerName()+"，退票成功，进入审核退票流程！["+resultCp.getErrorInfo()+"]";
						logger.info(orderbill.getOrderId()+" refund is success!");
						service.refundIsSuccess(orderbill, resultCp);
					}
				}else if (StringUtils.equals(resultCp.getRetValue(), ResultCP.OVER)) {// 订单退票完成
					
					optlog = resultCp.getWorker().getWorkerName()+"，审核退票完成，退票成功！["+resultCp.getErrorInfo()+"]";
					logger.info(orderbill.getOrderId()+" refund is success!");
					service.refundIsSuccess(orderbill, resultCp);
					
				} else if (StringUtils.equals(resultCp.getRetValue(), ResultCP.FAILURE)) {
					
					optlog = resultCp.getWorker().getWorkerName()+"，退票失败！["+resultCp.getErrorInfo()+"]";
					logger.info(orderbill.getOrderId()+" refund is failure!");
					service.refundFailIsManual(orderbill,resultCp);
					
				} else if (StringUtils.equals(resultCp.getRetValue(), ResultCP.MANUAL)) {
					if(resultCp.getErrorInfo().contains("已改签")){
						//查询是否自带账号
						service.ishaveOwnAccount(orderbill);
						logger.info("++++++++++++errorinfo:"+resultCp.getErrorInfo()+"*****getAccount_from_way:"+("1".equals(orderbill.getAccount_from_way()))+", auto_refund_success:"+("1".equals(auto_refund_success)));
						if("1".equals(orderbill.getAccount_from_way()) && "1".equals(auto_refund_success)){
							optlog = resultCp.getWorker().getWorkerName()+"，12306自带账号，拒绝退票！["+resultCp.getErrorInfo()+"]";
							logger.info(orderbill.getOrderId()+" refund is refuse!"+"["+optlog+"]");
							//拒绝退票，并自动通知
							orderbill.setOur_remark("12306上不能退票，已改签！ ");
							orderbill.setRefuse_reason("99");
							service.refundIsRefuse(orderbill,resultCp);
						}else{
							optlog = resultCp.getWorker().getWorkerName()+"，退票人工！["+resultCp.getErrorInfo()+"]";
							logger.info(orderbill.getOrderId()+" refund is manal!"+"["+optlog+"]");
							service.refundIsManual(orderbill,resultCp);
						}
					}else{
						optlog = resultCp.getWorker().getWorkerName()+"，退票人工！["+resultCp.getErrorInfo()+"]";
						logger.info(orderbill.getOrderId()+" refund is manal!");
						service.refundIsManual(orderbill,resultCp);
					}
					
					
				}else if (StringUtils.equals(resultCp.getRetValue(), ResultCP.RESEND)) {
					
					optlog = resultCp.getWorker().getWorkerName()+"，退票重发！["+resultCp.getErrorInfo()+"]";
					logger.info(orderbill.getOrderId()+" refund is resend!");
					service.refundIsResend(orderbill,resultCp.getCpId());
				}else{// 异常
					optlog = resultCp.getWorker().getWorkerName()+"，退票异常！";
					logger.info(orderbill.getOrderId()+" refund is manal!");
					service.refundIsManual(orderbill,resultCp);
				}
				//end
				//start 记录日志
				service.insertHistory(orderbill.getOrderId(),resultCp.getCpId(), optlog);	
				logger.info("modify orderbill cpid="+orderbill.getOrderId()+" status:"+resultCp.getRetValue()+" end!");
				//end
			}catch(Exception el){
				logger.warn("exception!!:"+el);
				optlog = resultCp.getWorker().getWorkerName()+"，退票异常！";
				logger.info(orderbill.getOrderId()+" refund is manal!");
				service.refundIsManual(orderbill,resultCp);
			}
				
		} catch (Exception e) {
			logger.error("exception!!:",e);
		}
		return true;
	}
	@Override
	public String getObjectKeyId(OrderCP t) {
		// TODO Auto-generated method stub
		return t.getOrderId();
	}


	/**
	 * 权重
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	private String deviceWeight(TrainServiceImpl service) {
		String logPre = "[refund权重]";
		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		
		/*PC&APP 模式权重分配*/
		String defaultWeightMode = DeviceWeight.WEIGHT_PC;
		
		
		String pcWeightValue = "100";//pc权重
		String appWeightValue = "0";//app权重
		try {
			sysImpl.querySysVal("refund_" + DeviceWeight.WEIGHT_PC);
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				pcWeightValue = sysImpl.getSysVal();
			}
			
			sysImpl.querySysVal("refund_" + DeviceWeight.WEIGHT_APP);
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				appWeightValue = sysImpl.getSysVal();
			}

			
			logger.info(logPre+"权重系统设置pc_weight--" + pcWeightValue + ", app_weight--" + appWeightValue);
			/*设置权重置*/
			List<DeviceWeight> modeCategories = new ArrayList<DeviceWeight>();//放各个权重的集合
			
			DeviceWeight pcMode = new DeviceWeight(DeviceWeight.WEIGHT_PC, Integer.parseInt(pcWeightValue));//pc权重
			DeviceWeight appMode = new DeviceWeight(DeviceWeight.WEIGHT_APP, Integer.parseInt(appWeightValue));//app权重
			
			modeCategories.add(pcMode);
			modeCategories.add(appMode);
			
			int count = 0;
			for(DeviceWeight category : modeCategories) {
				count += category.getWeight();
			}
			logger.info(logPre+"权重最大边界 count : " + count);
			Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0, weightSum)
			//logger.info(logPre+"权重随机值 nchannel : " + nchannel);
			Integer mchannel = 0;
			for (DeviceWeight weightCategory : modeCategories) {
				if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
					defaultWeightMode = weightCategory.getCategory();
					break;
				}
				mchannel += weightCategory.getWeight();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		logger.info(logPre+"权重随机值: " + defaultWeightMode);
		return defaultWeightMode;

	}
}