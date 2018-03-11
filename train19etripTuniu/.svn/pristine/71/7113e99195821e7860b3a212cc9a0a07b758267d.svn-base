package com.l9e.common;

import javax.annotation.Resource;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

public class BaseDao extends SqlMapClientDaoSupport {
	
	@Resource(name = "sqlMapClient")
    public void setSqlMapClientForAutowire(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }
	
}
