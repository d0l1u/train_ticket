package com.kuyou.train.consumer;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.common.enums.*;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.util.BigDecimalUtil;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.common.util.IpUtil;
import com.kuyou.train.entity.req.BookReq;
import com.kuyou.train.entity.req.BookTicketReq;
import com.kuyou.train.entity.resp.BookResp;
import com.kuyou.train.entity.resp.ChangeResp;
import com.kuyou.train.http.cookie.CacheCookieJar;
import com.kuyou.train.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import javax.management.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BookConsumer
 *
 * @author taokai3
 * @date 2018/11/27
 */
@Slf4j
@Component("bookConsumer")
public class BookConsumer {

	@Resource
	private JedisClient orderJedisClient;

	@Resource
	private JedisClient ipJedisClient;

	@Resource
	private BookService bookService;

	@MDCLog
	@Scheduled(cron = "* * 5-23 * * ?")
	public void consumer() {
		long begin = System.currentTimeMillis();
		//判断本机IP是否优质
		Set<Tuple> zrange = ipJedisClient.zrangeWithScores(KeyEnum.IP_CHECK.getValue(), 0, -1);
		List<Tuple> tupleList = zrange.stream().collect(Collectors.toList());

		Double score = null;
		int index = 0;
		Double flagScore = null;
		int total = tupleList.size();
		for (int i = 0; i < tupleList.size(); i++) {
			Tuple tuple = tupleList.get(i);
			if(total/3 == i){
				flagScore = tuple.getScore();
			}
			if (tuple.getElement().equals(IpUtil.getInnetIp())) {
				index = i;
				score = tuple.getScore();
				break;
			}
		}

		//乘因
		int factor = 100;
		//排名分割
		int top = total / 2;
		log.info("total:{}, 本机排名:{}, 打分:{}, top:{}", total, index, score, top);
		//超过200毫秒，统一设置成200毫秒
		if(score > 200){
			score = new Double(200);
		}

		//3分之2 超过 50 毫秒，睡眠因子调整为50
		if(flagScore.intValue() > 50){
			factor = 50;
		}

		//如果在 < top 50%, 可以使用，不进行睡眠
		//如果在 > top 50%, 判断是否存在挤压订单，如果不存在，则进行睡眠
		//如果存在挤压订单，睡眠时间减半
		if (index > top && score > 50) {
			//获取订单挤压数量
			Long llen = orderJedisClient.llen(KeyEnum.BOOK_REQ.getValue());
			log.info("当前队列积压数量:{}", llen);
			try {
				if (llen > 10) {
					Thread.sleep((score.intValue() * factor)/2);
				} else {
					Thread.sleep(score.intValue() * factor);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String rpopResult = orderJedisClient.rpop(KeyEnum.BOOK_REQ.getValue());
		if (StringUtils.isBlank(rpopResult)) {
			return;
		}
		log.info("rpopResult:{}", rpopResult);

		BookResp bookResp = BookResp.builder().success(false).build();
		try {
			//将19e参数转成12306参数
			BookReq bookReq = JSON.parseObject(rpopResult, BookReq.class);
			List<BookTicketReq> tickets = bookReq.getTickets().stream().map(ticket -> {
				String cardType = ticket.getCardType();
				String ticketType = ticket.getTicketType();
				String seatType = ticket.getSeatType();
				ticket.setCardType(CardTypeEnum.getByKy(cardType).getKyfw());
				ticket.setTicketType(PassengerTypeEnum.getByKy(ticketType).getKyfw());
				ticket.setSeatType(SeatTypeEnum.getByKy(seatType).getKyfw());
				return ticket;
			}).collect(Collectors.toList());
			bookReq.setTickets(tickets);
			bookResp.setOrderId(bookReq.getOrderId());
			//开始占位
			bookResp = bookService.book(bookReq);
		} catch (TrainException e) {
			//解析错误信息
			String message = e.getMessage();
			bookResp.setMessage(message);
		} catch (Exception e) {
			//系统异常
			String simpleName = e.getClass().getSimpleName();
			bookResp.setMessage(String.format("预订占位发生异常:%s", simpleName));
			log.info("系统异常", e);
		} finally {
			new CacheCookieJar().clearCookie();
			bookResp.setRobotIp(IpUtil.getInnetIp());
			log.info("预订占位结果:{}", bookResp.toString());
			//推送到redis
			Long lpush = orderJedisClient.lpush(KeyEnum.BOOK_RESP.getValue(), bookResp.toString());
			log.info("预订占位结果LPUSH:{}", lpush);

			BigDecimal time = new BigDecimal(System.currentTimeMillis()).subtract(new BigDecimal(begin));
			log.info("预订占位耗时:{} s", BigDecimalUtil.divideTime(time));

			//统计
			String format = DateUtil.format(new Date());
			String key = KeyEnum.IP_ORDER.getValue() + format;
			ipJedisClient.zincrby(key, 1, IpUtil.getInnetIp());
			ipJedisClient.expire(key, 60 * 60 *24 * 3);
		}
	}
}
