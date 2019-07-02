package com.kuyou.train.consumer;

import com.kuyou.train.common.enums.KeyEnum;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PingJob
 *
 * @author taokai3
 * @date 2019/1/8
 */
@Slf4j
@Component("pingJob")
public class PingJob {

	private static final Pattern WIN_PATTERN = Pattern.compile("时间=([\\d\\.]+)ms", Pattern.CASE_INSENSITIVE);
	private static final Pattern LINUX_PATTERN = Pattern.compile("time=([\\d\\.]+) ms", Pattern.CASE_INSENSITIVE);

	@Resource
	private JedisClient ipJedisClient;

	@MDCLog
	@Scheduled(cron = "0 0/1 5-23 * * ?")
	public void job() {
		String innetIp = IpUtil.getInnetIp();
		double ping = ping("kyfw.12306.cn");
		log.info("{} 12306 ping:{}", innetIp, ping);
		ipJedisClient.zadd(KeyEnum.IP_CHECK.getValue(), ping, innetIp);
	}

	private double ping(String ipAddress) {
		String osName = System.getProperty("os.name");
		String pingCommand = "ping " + ipAddress ;
		log.info("当前系统:{}", osName);
		if(StringUtils.isNotBlank(osName) && osName.toLowerCase().contains("linux")){
			pingCommand = "ping -c 4 " + ipAddress ;
		}

		BufferedReader in = null;
		try {
			// 执行命令并获取输出
			Process process = Runtime.getRuntime().exec(pingCommand);
			if (process == null) {
				return 999;
			}
			in = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
			BigDecimal total = new BigDecimal(0);
			String line;
			// 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
			while ((line = in.readLine()) != null) {
				if(StringUtils.isBlank(line)){
					continue;
				}
				log.info("ping line:{}", line);
				total = total.add(getCheckResult(line));
			}

			if(total.intValue() == 0){
				return 999;
			}

			//除以4
			return total.divide(new BigDecimal(4), 2, RoundingMode.HALF_UP).doubleValue();
		} catch (Exception e) {
			log.info("ping kyfw Exception", e);
			return 999;
		} finally {
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				log.info("close IOException",e);
			}
		}
	}

	/**
	 * 若line含有=18ms TTL=16字样,说明已经ping通,返回时间,否則返回0.
	 * @param line
	 * @return
	 */
	private BigDecimal getCheckResult(String line) {
		Matcher matcher;
		String osName = System.getProperty("os.name");
		if(StringUtils.isNotBlank(osName) && osName.toLowerCase().contains("linux")){
			matcher = LINUX_PATTERN.matcher(line);
		}else{
			matcher = WIN_PATTERN.matcher(line);
		}
		if (matcher.find()) {
			return new BigDecimal(matcher.group(1));
		}else{
			return new BigDecimal(0);
		}
	}
}
