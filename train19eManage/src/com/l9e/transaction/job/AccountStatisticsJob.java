package com.l9e.transaction.job;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.AccounttjService;
import com.l9e.transaction.vo.AccountStatistics;

/**
 * <p>
 * Title: Job.java
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年3月25日
 */
@Component
@SuppressWarnings("all")
public class AccountStatisticsJob {

	@Resource
	private AccounttjService accounttjService;

	private static final Logger logger = Logger.getLogger(AccountStatisticsJob.class);

	@Scheduled(cron = "0 0 1 * * ?")
	public void statisticsAccountInfo() {
		Date date = new Date();
		date.setDate(date.getDate() - 1);
		AccountStatistics statistics = new AccountStatistics();
		statistics.setStatisticsDate(date);
		BigDecimal multiply = new BigDecimal(100);
		// 查询新增帐号
		String format = new SimpleDateFormat("yyyy-MM-dd").format(date);
		Integer addAccount = accounttjService.queryNewaddAccountTotal(date);
		logger.info(format + "-新增帐号-addAccount:" + addAccount);
		statistics.setAddAccount(addAccount);

		// 查询白名单总数
		Integer whiteList = accounttjService.queryWhiteListTotal();
		logger.info(format + "-白名单总数-whiteList:" + whiteList);
		statistics.setWhiteList(whiteList);

		// 查询剩余联系人坑位
		Integer surplusPassenger = accounttjService.querySurplusPassengerTotal();
		logger.info(format + "-剩余联系人坑位-surplusPassenger:" + surplusPassenger);
		statistics.setSurplusPassenger(surplusPassenger);

		// 查询总票数
		Integer ticketSum = accounttjService.queryTicketTotal(date);
		logger.info(format + "-总票数-ticketSum:" + ticketSum);
		statistics.setTicketSum(ticketSum);

		// 查询当日白名单添加总数
		Integer addWhiteListTotal = accounttjService.queryAddWhiteListTotal(date);
		logger.info("当日白名单添加总数-addWhiteListTotal:" + addWhiteListTotal);
		statistics.setAddWhiteList(addWhiteListTotal);

		// 查询白名单匹配成功数
		Integer matchWhiteListTotal = accounttjService.queryMatchWhiteListTotal(date);
		logger.info(format + "-白名单匹配成功数-matchWhiteListTotal:" + matchWhiteListTotal);

		// 计算白名单匹配率
		Double whiteListRate = 0D;
		if (!matchWhiteListTotal.equals(0)) {

			BigDecimal ticketTotal = new BigDecimal(ticketSum).setScale(4);
			BigDecimal whiteTotal = new BigDecimal(matchWhiteListTotal).setScale(4);
			whiteListRate = whiteTotal.divide(ticketTotal, BigDecimal.ROUND_HALF_UP).setScale(4).multiply(multiply)
					.doubleValue();
		}
		logger.info(format + "-白名单匹配成功率-whiteListRate:" + whiteListRate);
		statistics.setWhiteListRate(whiteListRate);

		// 查询帐号停用总数
		Integer accountStopTotal = accounttjService.queryAccountStopTotal(date);
		logger.info(format + "-帐号停用总数-accountStopTotal:" + accountStopTotal);

		// 帐号停用-联系人达上线
		Integer upperLimit = accounttjService.queryAccountOfUpperlimit(date);
		logger.info(format + "-帐号停用(联系人达上线 )-upperLimit:" + upperLimit);
		statistics.setUpperLimit(upperLimit);

		// 帐号停用-用户信息待核验
		Integer checkUser = accounttjService.queryAccountOfCheckUser(date);
		logger.info(format + "-帐号停用(用户信息待核验)-checkUser:" + checkUser);
		statistics.setUserInfo(checkUser);

		// 帐号停用-手机待核验
		Integer checkPhone = accounttjService.queryAccountOfCheckPhone(date);
		logger.info(format + "-帐号停用(手机待核验)-checkPhone:" + checkPhone);
		statistics.setPhoneVerifi(checkPhone);

		// 帐号停用-其他
		Integer other = accountStopTotal - upperLimit - checkUser - checkPhone;
		logger.info(format + "-帐号停用(其他)-other:" + other);
		statistics.setOtherSum(other);

		Double xRate = 0D;
		// 查询一人单
		Integer one = accounttjService.queryTicketX(date, 1);
		if (!one.equals(0)) {
			xRate = new BigDecimal(one).setScale(6)
					.divide(new BigDecimal(ticketSum).setScale(6), BigDecimal.ROUND_HALF_UP).setScale(6)
					.multiply(multiply).doubleValue();
		}
		logger.info(format + "-一人单数量:" + one + "; one-xRate:" + xRate);
		statistics.setX1Rate(xRate);
		xRate = 0D;

		// 查询二人单
		Integer two = accounttjService.queryTicketX(date, 2);
		if (!two.equals(0)) {
			xRate = new BigDecimal(two).setScale(6)
					.divide(new BigDecimal(ticketSum).setScale(6), BigDecimal.ROUND_HALF_UP).setScale(6)
					.multiply(multiply).doubleValue();
		}
		logger.info(format + "-二人单数量:" + two + "; two-xRate:" + xRate);
		statistics.setX2Rate(xRate);
		xRate = 0D;

		// 查询三人单
		Integer threed = accounttjService.queryTicketX(date, 3);
		if (!threed.equals(0)) {
			xRate = new BigDecimal(threed).setScale(6)
					.divide(new BigDecimal(ticketSum).setScale(6), BigDecimal.ROUND_HALF_UP).setScale(6)
					.multiply(multiply).doubleValue();
		}
		logger.info(format + "-三人单数量:" + threed + "; threed-xRate:" + xRate);
		statistics.setX3Rate(xRate);
		xRate = 0D;

		// 查询四人单
		Integer four = accounttjService.queryTicketX(date, 4);
		if (!four.equals(0)) {
			xRate = new BigDecimal(four).setScale(6)
					.divide(new BigDecimal(ticketSum).setScale(6), BigDecimal.ROUND_HALF_UP).setScale(6)
					.multiply(multiply).doubleValue();
		}
		logger.info(format + "-四人单数量:" + four + "; four-xRate:" + xRate);
		statistics.setX4Rate(xRate);
		xRate = 0D;

		// 查询五人单
		Integer five = accounttjService.queryTicketX(date, 5);
		if (!five.equals(0)) {
			xRate = new BigDecimal(five).setScale(6)
					.divide(new BigDecimal(ticketSum).setScale(6), BigDecimal.ROUND_HALF_UP).setScale(6)
					.multiply(multiply).doubleValue();
		}
		logger.info(format + "-五人单数量:" + five + "; five-xRate:" + xRate);
		statistics.setX5Rate(xRate);

		// 将数据插入统计表
		accounttjService.insertStatistics(statistics);
	}

	public static void main(String[] args) {
		Double xRate = 0D;
		// 查询一人单
		Integer one = 22382;
		Integer ticketSum = 42706;
		if (!one.equals(0)) {
			// xRate = new BigDecimal(one).setScale(6)
			// .divide(new BigDecimal(ticketSum).setScale(6),
			// BigDecimal.ROUND_HALF_UP).setScale(6).doubleValue()*100;
			System.err.println(new BigDecimal(one).setScale(6)
					.divide(new BigDecimal(ticketSum).setScale(6), BigDecimal.ROUND_HALF_UP).setScale(6)
					.multiply(new BigDecimal(100)).setScale(6).doubleValue());
		}
		System.err.println(xRate);
	}
}
