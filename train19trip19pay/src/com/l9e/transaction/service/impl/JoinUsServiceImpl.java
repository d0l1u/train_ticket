package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.JoinUsDao;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.vo.AgentVo;
import com.l9e.transaction.vo.ProductVo;

@Service("joinUsService")
public class JoinUsServiceImpl implements JoinUsService {
	
	@Resource
	private JoinUsDao joinUsDao;
	
	@Resource
	private CommonDao commonDao;

	public AgentVo queryAgentInfo(String agentId) {
		return joinUsDao.queryAgentInfo(agentId);
	}

	public AgentVo queryAgentInfoById(String userId){
		return joinUsDao.queryAgentInfoById(userId);
	}
	public void addAgentInfo(AgentVo agentVo) {
		joinUsDao.addAgentInfo(agentVo);
	}

	public List<Map<String, String>> queryDistrictList(String cityId) {
		return joinUsDao.queryDistrictList(cityId);
	}

	public void addJmOrder(Map<String, Object> jmMap) {
		joinUsDao.addJmOrder(jmMap);
	}

	public void updateJmOrderEopInfo(Map<String, String> eopInfo) {
		commonDao.updateOrderEop(eopInfo);
		
		joinUsDao.updateJmOrderEopInfo(eopInfo);
		
		//更新代理商审核状态(用于支付环节，其他环节则跳过)
		if(!StringUtils.isEmpty(eopInfo.get("order_status")) 
				&& "11".equals(eopInfo.get("order_status"))){//支付成功 则更新代理商审核状态为等待审核
			ProductVo productVo = new ProductVo();
			productVo.setProduct_id(eopInfo.get("product_id"));
			ProductVo productInfo = commonDao.queryProductInfoList(productVo).get(0);
			eopInfo.put("user_level", productInfo.getLevel());
			if(!StringUtils.isEmpty(eopInfo.get("asp_order_type")) 
					&& "jm".equals(eopInfo.get("asp_order_type"))){//首次加盟订单修改等待审核状态
				eopInfo.put("estate", TrainConsts.AGENT_ESTATE_WAITING);//等待审核
			}
			
			joinUsDao.updateAgentJmInfo(eopInfo);
		}
		
	}

	public List<Map<String, String>> queryTimedJmRefundList() {
		return joinUsDao.queryTimedJmRefundList();
	}

	public void updateJmOrderStatus(Map<String, String> map) {
		joinUsDao.updateJmOrderEopInfo(map);
	}

	public Map<String, String> queryJmOrderInfo(Map<String, String> map) {
		return joinUsDao.queryJmOrderInfo(map);
	}
	
	/**
	 * 被动续费
	 */
	public void updateAgentJmSysXf(Map<String, String> map) {
		joinUsDao.updateJmOrderEopInfo(map);
		//更新代理商状态（需要续费->审核通过 用于续费，其他环节跳过）
		joinUsDao.updateAgentJmSysXf(map);
	}
	
	/**
	 * 主动续费
	 */
	public void updateAgentJmHumXf(Map<String, String> map) {
		joinUsDao.updateJmOrderEopInfo(map);
		//更新代理商产品有效期
		joinUsDao.updateAgentJmHumXf(map);
	}
}
