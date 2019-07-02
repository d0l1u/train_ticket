package com.nineteen.mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nineteen.vo.AlipayUser;

public interface CountMapper {
	//查询航旅版
	@Select("SELECT a.card_no,TIME_TO_SEC (TIMEDIFF(NOW(),a.create_time)) AS times FROM cp_cardinfo a WHERE  TIME_TO_SEC (TIMEDIFF(NOW(),a.create_time)) >2500 AND a.card_no LIKE '%huochepiao19e__@163.com' AND a.card_ext='1'  ORDER BY times DESC LIMIT 0, 1")
	List<AlipayUser> getAirInfo();
	//更新航旅版时间
	@Update("UPDATE cp_cardinfo SET create_time = NOW() where card_no=#{alipay_account} and card_ext='1' ")
	int updateAir(Map<String, String> map);
	//查询账号
	@Select("SELECT a.card_no,TIME_TO_SEC (TIMEDIFF(NOW(),a.create_time)) AS times FROM cp_cardinfo_wudong a WHERE  TIME_TO_SEC (TIMEDIFF(NOW(),a.create_time)) >2500 AND card_no LIKE '%huochepiaokuyou__@19e.com.cn'  ORDER BY times DESC LIMIT 0, 1")
	List<AlipayUser> getInfo();
	//更新账号余额
	@Update("UPDATE cp_cardinfo_wudong SET card_remain=#{card_remain},create_time = NOW() where card_no=#{card_no}")
	int updateBalance(Map<String, String> map);
	//更新时间 当出现验证码情况下
	@Update("UPDATE cp_cardinfo SET create_time = NOW() where card_no=#{card_no}")
	int updateDate(Map<String, String> map);
	//测试查询
	@Select("select count(1) from cp_cardinfo where card_id =#{card_id}")
	int query(Map<String, Object> map);
	//查询支付宝验证码
	@Select("SELECT a.card_no,a.verification_code FROM cp_alipaycode_info a WHERE a.card_no =#{card_no} ORDER BY a.create_time DESC LIMIT 1")
	List queryAlpayCode(Map<String, String> map); 
	//查询支付宝账单是否下载
	@Select("SELECT COUNT(1) FROM cp_alipaybill_info a WHERE a.alipay_account=#{alipay_account} AND a.download_time=#{download_time} AND a.is_download='1'")
	int queryAlipayBill(Map<String,String> map);
	//插入支付宝下载账单
	@Insert("INSERT INTO cp_alipaybill_info(alipay_account,download_time,is_download,create_time,download_path) VALUE(#{alipay_account},#{download_time},#{is_download},NOW(),#{download_path})")
	int insertAlipayBill(Map<String,String> map);
	//查询当月中日历前缀差值
	@Select("SELECT a.delta FROM cp_ailipaytime_info a WHERE a.month=#{month}")
	String queryCalendar(Map<String,String> map);
	
}
