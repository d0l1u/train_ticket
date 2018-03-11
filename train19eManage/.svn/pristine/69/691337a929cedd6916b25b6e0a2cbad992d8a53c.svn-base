package com.l9e.common;

import javax.annotation.Resource;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

public class BaseDao extends SqlMapClientDaoSupport {
	
	@Resource(name = "sqlMapClient")
    public void setSqlMapClientForAutowire(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }
	
	
	
	@Resource(name= "sqlMapClientTemplate")
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	public SqlMapClientTemplate getSqlMapClient2(){
		return this.sqlMapClientTemplate;
	}
	
	/**
	 * 查询数据条数
	 * @param sqlId
	 * @param vo
	 * @return
	 */
	public int getTotalRows(String sqlId, Object vo) {
		Integer retrows=(Integer)getSqlMapClientTemplate().queryForObject(sqlId, vo);
		return retrows==null ? 0 : retrows;
	}
}
