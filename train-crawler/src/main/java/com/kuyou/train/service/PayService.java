package com.kuyou.train.service;

import com.kuyou.train.common.enums.OrderStatusEnum;
import com.kuyou.train.entity.dto.CompleteOrderDto;
import com.kuyou.train.entity.dto.NoCompleteOrderDto;
import com.kuyou.train.entity.req.PayReq;
import com.kuyou.train.kyfw.impl.LoginApiRobot;
import com.kuyou.train.kyfw.impl.OrderApiRobot;
import com.kuyou.train.kyfw.impl.QueryOrderApiRobot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * PayService
 *
 * @author taokai3
 * @date 2019/1/11
 */
@Slf4j
@Service
public class PayService {

	@Resource
	private LoginApiRobot loginApiRobot;

	@Resource
	private QueryOrderApiRobot queryOrderApiRobot;

	@Resource
	private OrderApiRobot orderApiRobot;

	public void pay(PayReq payReq) throws IOException {
		Integer changeId = payReq.getChangeId();
		String orderId = payReq.getOrderId();
		String username = payReq.getUsername();
		String password = payReq.getPassword();
		String sequence = payReq.getSequence();
		List<String> subSequences = payReq.getSubSequences();

		//检查登录状态，如果redis存在登录cookie，则判断cookie是否失效，如果失效，则重新登录
		loginApiRobot.checkLoginStatus(username, password);

		//查询待支付订单
		NoCompleteOrderDto noOrderDto = queryOrderApiRobot.queryWaitPay(sequence, subSequences);
		if(noOrderDto == null){
			//反查已完成订单
			List<CompleteOrderDto> orderDtos = queryOrderApiRobot.queryNotTravelOrder(sequence, subSequences);
			for (CompleteOrderDto dto : orderDtos){
				OrderStatusEnum status = dto.getStatusEnum();
				if(!status.equals(OrderStatusEnum.WAIT_PAY) && !status.equals(OrderStatusEnum.CHANGE_WAIT_PAY)
						&& !status.equals(OrderStatusEnum.ALTER_WAIT_PAY)){


				}
			}
		}
	}
}
