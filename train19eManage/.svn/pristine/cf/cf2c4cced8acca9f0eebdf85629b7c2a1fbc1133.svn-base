package com.l9e.transaction.dao.impl;


	import java.util.List;
	import java.util.Map;

	import org.springframework.stereotype.Repository;

	import com.l9e.common.BaseDao;
	import com.l9e.transaction.dao.InnerNoticeDao;
	import com.l9e.transaction.vo.AreaVo;
	import com.l9e.transaction.vo.NoticeVo;
	@Repository("innerNoticeDao")
	public class InnerNoticeDaoImpl extends BaseDao implements InnerNoticeDao {

		public List<Map<String, Object>> queryNoticeList(
				Map<String, Object> paramMap) {
			return this.getSqlMapClientTemplate().queryForList("innerNotice.queryNoticeList", paramMap);
		}

		@SuppressWarnings("unchecked")
		public int queryNoticeListCount(Map<String, Object> paramMap) {
			return getTotalRows("innerNotice.queryNoticeListCount", paramMap);
		}

		@SuppressWarnings("unchecked")
		public Map<String, Object> queryNotice(String notice_id) {
			return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("innerNotice.queryNotice", notice_id);
		}

		@SuppressWarnings("unchecked")
		public void updateNotice(NoticeVo  Notice) {
			this.getSqlMapClientTemplate().update("innerNotice.updateNotice", Notice);
		}

		@SuppressWarnings("unchecked")
		public void insertNotice(NoticeVo  Notice) {
			this.getSqlMapClientTemplate().insert("innerNotice.insertNotice", Notice);
		}

		@SuppressWarnings("unchecked")
		public void deleteNotice(NoticeVo Notice) {
			this.getSqlMapClientTemplate().delete("innerNotice.deleteNotice", Notice);
		}

		@SuppressWarnings("unchecked")
		public List<AreaVo> getProvince() {
			return this.getSqlMapClientTemplate().queryForList("innerNotice.getProvince");
		}

		@SuppressWarnings("unchecked")
		public List<AreaVo> getCity(String provinceid) {
			return this.getSqlMapClientTemplate().queryForList("innerNotice.getCity", provinceid);
		}

		@SuppressWarnings("unchecked")
		public List<AreaVo> getArea(String cityid) {
			return this.getSqlMapClientTemplate().queryForList("innerNotice.getArea", cityid);
		}


	}
