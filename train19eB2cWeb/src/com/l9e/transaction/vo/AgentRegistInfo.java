package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.List;

public class AgentRegistInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<AgentRegistVo> registInfoList;

	public List<AgentRegistVo> getRegistInfoList() {
		return registInfoList;
	}

	public void setRegistInfoList(List<AgentRegistVo> registInfoList) {
		this.registInfoList = registInfoList;
	}
}
