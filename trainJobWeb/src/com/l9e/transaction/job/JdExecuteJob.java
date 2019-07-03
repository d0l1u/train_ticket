package com.l9e.transaction.job;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.CouponService;
import com.l9e.transaction.service.PrepaidCardService;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Coupon;
import com.l9e.transaction.vo.PrepaidCard;
import com.l9e.util.HttpUtil;

/**
 * <p>
 * Title", "Demo.java
 * </p>
 * <p>
 * Description", "TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年2月24日
 */
// @Component
@Deprecated
public class JdExecuteJob {

	private Logger logger = Logger.getLogger(JdExecuteJob.class);

	@Resource
	private PrepaidCardService prepaidCardService;

	@Resource
	private AccountService accountService;

	@Resource
	private CouponService couponService;

	/**
	 * 查询 2给月更新日期没变 余额为0的预付卡,禁用
	 */
	@Scheduled(cron = "0 0/3 * * * ?")
	public void updateCardStatus() {
		logger.info("------JD-updateCardStatus-Begin-----");
		prepaidCardService.jdUpdateCardStatus();
		logger.info("------JD-updateCardStatus-end-----");
	}

	/**
	 * 更新京东预付卡余额
	 */
	@Scheduled(cron = "0/50 * * * * ?")
	public void updateCardBalance() {
		try {
			logger.info("------JD-QueryCardBalance-Begin-----");

			PrepaidCard card = prepaidCardService.jdSelectCard();
			logger.info("card::" + card);
			if (card == null) {
				return;
			}
			String cardNo = card.getCardNo();
			String cardPwd = card.getCardPwd();
			logger.info("card_no:" + cardNo);
			logger.info("card_pwd:" + cardPwd);
			// card.setCardMoney("100.00");
			// prepaidCardService.jdUpdateCardMoney(card);
			String url = "http://59.110.172.182:9000/queryCardInfo";
			Map<String, String> map = new HashMap<String, String>();
			map.put("cardNo", cardNo);
			map.put("cardPwd", cardPwd);
			String params = new ObjectMapper().writeValueAsString(map);
			logger.info("query-url:" + url);

			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			PrintWriter pw = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			pw.print(params);
			// flush输出流的缓冲
			pw.flush();
			// 建立实际的连接
			conn.connect();
			// 定义BufferedReader输入流来读取URL的响应
			InputStream inputStream = conn.getInputStream();
			byte[] b = new byte[1024];
			int i = 0;

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while ((i = inputStream.read(b, 0, 1024)) > 0) {
				out.write(b, 0, i);
			}
			byte[] bytes = out.toByteArray();
			String result = new String(bytes);
			// {"status": true, "data": {"balance": "0.00", "expireDate": ""}}
			result = URLDecoder.decode(result, "utf-8");
			logger.info("query-result:" + result);
			if (!result.startsWith("{")) {
				logger.info("预付卡余额查询结果异常,page:" + result);
			} else {
				ObjectMapper mapper = new ObjectMapper();
				HashMap<String, Object> res = mapper.readValue(result, HashMap.class);
				boolean status = (Boolean) res.get("status");
				if (!status) {
					logger.info("查询结果不为True");
				} else {
					res = (HashMap) res.get("data");
					String balance = (String) res.get("balance");
					if (StringUtils.isBlank(balance)) {
						logger.info("查询余额为空");
					} else {
						logger.info("更新预付卡余额");
						card.setCardMoney(balance);
						prepaidCardService.jdUpdateCardMoney(card);
					}
				}
			}
		} catch (Exception e) {
			logger.info("[系统异常]-更新京东预付卡余额", e);
		} finally {
			logger.info("------JD-QueryCardBalance-End-----");
		}
	}

	/**
	 * 查询帐号所属优惠劵
	 */
	@Scheduled(cron = "0 * * * * ?")
	public void selectCoupon() {
		logger.info("------JD-selectCoupon-Begin-----");
		try {
			Account account = accountService.selectJdAccount("88");
			if (account == null) {
				logger.info("获取京东账号结果为空");
				return;
			}
			logger.info("调用服务,获取账号内的优惠劵");
			String username = account.getAccUsername();
			String password = account.getAccPassword();
			Integer accountId = Integer.valueOf(account.getAccId());
			String url = "http://219.143.36.224:18200/selenium/jd/queryCoupon/" + username + "/" + password;
			String result = HttpUtil.sendByGet(url, "utf-8", null, null);
			logger.info("服务返回结果:" + result);

			JSONObject json = new JSONObject().parseObject(result);
			String status = json.getString("status");
			String message = json.getString("message");
			if (status.equals("1") || status.equals("2")) {
				logger.info("封停京东账号");
				accountService.updateJdAccount(accountId, "99");
				return;
			}
			JSONArray array = json.getJSONArray("couponList");
			List<Coupon> list = new ArrayList<Coupon>();
			if (array.size() == 0) {
				logger.info("优惠劵为空,封停京东账号");
				accountService.updateJdAccount(accountId, "99");
				return;
			}
			for (int i = 0; i < array.size(); i++) {
				json = array.getJSONObject(i);
				Coupon c = new Coupon();
				c.setAccountId(accountId);
				c.setChannal("44");
				c.setCouponNo(json.getString("couponNo"));
				c.setPrice(json.getDouble("price"));
				c.setLimitPrice(json.getDouble("limitPrice"));
				c.setGetTime(json.getDate("getTime"));
				c.setEndTime(json.getDate("endTime"));
				list.add(c);
			}

			logger.info("插入或者更新优惠劵");
			couponService.insertCoupons(list);
			logger.info("启用京东账号");
			accountService.updateJdAccount(accountId, "00");
		} catch (Exception e) {
			logger.info("[系统异常]-查询京东优惠劵流程", e);
		} finally {
			logger.info("------JD-selectCoupon-end-----");
		}
	}

	/**
	 * 查询过期礼品卡,封停帐号
	 */
	@Scheduled(cron = "0/10 * * * * ?")
	public void expiredCoupon() {
		logger.info("------JD-expiredCoupon-Begin-----");

		logger.info("禁用过期优惠劵");
		couponService.disableExpiredCoupons();

		logger.info("查询京东帐号下面所有优惠劵过期的帐号");
		List<Integer> list = couponService.selectExpiredCouponsOfJdAccount("44");
		if (list == null || list.size() == 0) {
			logger.info("没有符合条件的帐号");
			return;
		} else {
			logger.info("可禁用帐号的数量:" + list.size());
		}

		for (int i = 0; i < list.size(); i++) {
			Integer accountId = list.get(i);
			logger.info("禁用京东帐号:" + accountId);
			accountService.disableJdAccount(accountId, "优惠劵使用完毕或者过期");
		}

		logger.info("------JD-expiredCoupon-end-----");
	}

}
