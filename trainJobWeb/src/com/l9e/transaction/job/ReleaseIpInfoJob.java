package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.IpInfoReleaseService;
import com.l9e.transaction.vo.IpInfoRelease;
import com.l9e.util.HttpUtil;



/**
 * 定时释放美团云不用的IP
 * @author wangsf01
 *
 */
@Component("releaseIpInfoJob")
public class ReleaseIpInfoJob {
	
	private static final Logger logger = Logger.getLogger(ReleaseIpInfoJob.class);
	
	@Resource
	private IpInfoReleaseService ipInfoReleaseService;
	
	@Value("#{propertiesReader[mtyunReleaseAddress]}")
	private String mtyunReleaseAddress;//美团云释放IP接口地址
	
	public void releaseIpInfo() {
		logger.info("releaseIpInfoJob start~~~");
		List<IpInfoRelease> ipInfoReleaseList = null;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("releaseStatus", IpInfoRelease.RELEASE_PREPARE);
		// 查询出所有要释放的IP列表
		ipInfoReleaseList = ipInfoReleaseService.queryIpInfoReleaseList(queryMap);
		logger.info("releaseIpInfoJob 查询得到的ipInfoReleaseList为: "+ ipInfoReleaseList);

		if (ipInfoReleaseList.size() > 0) {
			IpInfoRelease ipInfoRelease = null;
			Integer ipId = null;
			String ipExt = "";
			Integer releaseNum = null;

			for (int i = 0; i < ipInfoReleaseList.size(); i++) {
				ipInfoRelease = ipInfoReleaseList.get(i);
				logger.info("releaseIpInfoJob 循环遍历得到的待释放的IP实体为: "+ ipInfoRelease);

				if (null != ipInfoRelease) {
					ipId = ipInfoRelease.getIpId(); // 主键
					ipExt = ipInfoRelease.getIpExt();// IP地址
					releaseNum = ipInfoRelease.getReleaseNum();// 本IP释放的次数

					// 先判断释放次数是否大于3，如果大于3，则本条IP信息的释放状态改为2（释放失败），否则调用释放IP接口做具体的处理
					if (releaseNum > 3) {
						Map<String, Object> updateMap = new HashMap<String, Object>();
						updateMap.put("releaseStatus",IpInfoRelease.RELEASE_FAILURE); // 释放失败
						updateMap.put("optionTime", "now()");
						updateMap.put("ipId", ipId);
						ipInfoReleaseService.modifyIpInfoRelease(updateMap);
						logger.info("releaseIpInfoJob 本IP释放次数已经达到上限,释放IP失败,更新cp_ipinfo_release表中本IP的释放状态为2！");
					} else {

						// 先把待释放IP的状态更新为释放中：3
						Map<String, Object> updateMap = new HashMap<String, Object>();
						updateMap.put("releaseStatus",IpInfoRelease.RELEASE_UNDERWAY); // 释放中
						updateMap.put("optionTime", "now()");
						updateMap.put("ipId", ipId);
						ipInfoReleaseService.modifyIpInfoRelease(updateMap);
						logger.info("releaseIpInfoJob 更新cp_ipinfo_release表的IP释放状态为3成功！");

						// 开始调用释放IP接口
						String releaseResult = "";// 调用释放IP接口的返回结果值
						String releaseIpInfoResult = HttpUtil.sendByPost(mtyunReleaseAddress, "commond="
										+ "ReleaseAddress" + "&allocationId="
										+ ipExt, "utf-8");
						logger.info("releaseIpInfoJob 调用释放IP接口的返回结果为: "+ releaseIpInfoResult);

						if (!StringUtils.isEmpty(releaseIpInfoResult)) {
							JSONObject releaseResultObj = JSONObject.fromObject(releaseIpInfoResult);
							releaseResult = String.valueOf(releaseResultObj.getBoolean("return"));//调用释放IP接口的返回结果值
						}
						logger.info("releaseIpInfoJob 调用释放IP接口的返回结果的值为: "+ releaseResult);

						if (releaseResult.equals("true")) {
							//如果IP释放成功，则修改状态为1（释放成功），释放次数加1
							releaseNum++;
							Map<String, Object> releaseSuccessMap = new HashMap<String, Object>();
							releaseSuccessMap.put("releaseStatus",IpInfoRelease.RELEASE_SUCCESS); // 释放成功
							releaseSuccessMap.put("optionTime", "now()");
							releaseSuccessMap.put("releaseNum", releaseNum);
							releaseSuccessMap.put("ipId", ipId);
							ipInfoReleaseService.modifyIpInfoRelease(releaseSuccessMap);
							logger.info("releaseIpInfoJob IP释放成功，并更新cp_ipinfo_release表的状态为1！");
						} else {
							//如果IP释放失败，则修改状态为3（释放中），释放次数加1，释放次数累计达到3次则改状态为2（失败）
							releaseNum++;
							Map<String, Object> releaseSuccessMap = new HashMap<String, Object>();
							releaseSuccessMap.put("releaseStatus",IpInfoRelease.RELEASE_UNDERWAY); // 释放中
							releaseSuccessMap.put("optionTime", "now()");
							releaseSuccessMap.put("releaseNum", releaseNum);
							releaseSuccessMap.put("ipId", ipId);
							ipInfoReleaseService.modifyIpInfoRelease(releaseSuccessMap);
							logger.info("releaseIpInfoJob IP释放失败，并更新cp_ipinfo_release表的状态为3！");

						}

					}

				}
				try {
					// 休眠1秒后继续下一个IP释放
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		logger.info("releaseIpInfoJob end~~~");
	}

}
