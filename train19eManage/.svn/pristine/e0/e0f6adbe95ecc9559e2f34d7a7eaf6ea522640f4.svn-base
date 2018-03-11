package com.l9e.transaction.dao.impl;


	import java.util.List;
	import java.util.Map;

	import org.springframework.stereotype.Repository;

	import com.l9e.common.BaseDao;
	import com.l9e.transaction.dao.ExtNoticeDao;
	import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.NoticeVo;
	@Repository("extNoticeDao")
	public class ExtNoticeDaoImpl extends BaseDao implements ExtNoticeDao {

		public List<Map<String, Object>> queryNoticeList(
				Map<String, Object> paramMap) {
			return this.getSqlMapClientTemplate().queryForList("extNotice.queryNoticeList", paramMap);
		}

		@SuppressWarnings("unchecked")
		public int queryNoticeListCount(Map<String, Object> paramMap) {
			return getTotalRows("extNotice.queryNoticeListCount", paramMap);
		}

		@SuppressWarnings("unchecked")
		public Map<String, Object> queryNotice(String notice_id) {
			return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("extNotice.queryNotice", notice_id);
		}

		@SuppressWarnings("unchecked")
		public void updateNotice(NoticeVo  Notice) {
			this.getSqlMapClientTemplate().update("extNotice.updateNotice", Notice);
		}

		@SuppressWarnings("unchecked")
		public void insertNotice(NoticeVo  Notice) {
			this.getSqlMapClientTemplate().insert("extNotice.insertNotice", Notice);
		}

		@SuppressWarnings("unchecked")
		public void deleteNotice(NoticeVo Notice) {
			this.getSqlMapClientTemplate().delete("extNotice.deleteNotice", Notice);
		}

		@SuppressWarnings("unchecked")
		public List<AreaVo> getProvince() {
			return this.getSqlMapClientTemplate().queryForList("extNotice.getProvince");
		}

		@SuppressWarnings("unchecked")
		public List<AreaVo> getCity(String provinceid) {
			return this.getSqlMapClientTemplate().queryForList("extNotice.getCity", provinceid);
		}

		@SuppressWarnings("unchecked")
		public List<AreaVo> getArea(String cityid) {
			return this.getSqlMapClientTemplate().queryForList("extNotice.getArea", cityid);
		}

		@Override
		public List<NoticeVo> queryNoticeChannelList(
				Map<String, Object> paramMap) {
			return this.getSqlMapClientTemplate().queryForList("extNotice.queryNoticeChannelList", paramMap);
		}


	}
