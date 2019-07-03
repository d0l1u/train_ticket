package com.l9e.transaction.dao;

import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLogVO;
import com.l9e.transaction.vo.ChangePassengerInfo;

import java.util.List;
import java.util.Map;

public interface ChangeDao {
    /**
     * 根据改签特征值查询改签信息
     *
     * @param reqtoken
     * @return
     */
    public ChangeInfo selectChangeInfoByReqtoken(String reqtoken);

    /**
     * 改签车票信息
     *
     * @param
     * @return
     */
    public List<ChangePassengerInfo> selectChangeCp(Map<String, Object> map);

    /**
     * 增加改签信息
     *
     * @param changeInfo
     * @return
     */
    public int insertChangeInfo(ChangeInfo changeInfo);

    /**
     * 增加改签用户信息
     *
     * @param passenger
     * @return
     */
    public void insertChangeCp(ChangePassengerInfo passenger);

    /**
     * 增加改签日志
     *
     * @param log
     * @return
     */
    public void insertChangeLog(ChangeLogVO log);

    /**
     * 更新改签信息
     *
     * @param changeInfo
     * @return
     */
    public int updateChangeInfo(ChangeInfo changeInfo);

    /**
     * 更新改签车票信息
     *
     * @param changePassengerInfo
     * @return
     */
    public int updateChangeCp(ChangePassengerInfo changePassengerInfo);


    /*********回调********/
    /**
     * 查询待通知的改签信息
     */
    public List<ChangeInfo> selectNoticeChangeInfo(String merchantId);

    /**
     * 根据订单号查询车票id集合
     *
     * @param orderId
     * @return
     */
    public List<String> getCpListByOrderId(String orderId);

    /**
     * 查询新票的改签车票信息
     *
     * @param param
     * @return
     */
    public Map<String, Object> queryChangeCpInfo(Map<String, Object> param);

    /**
     * 根据改签的车次，查询改签到达站的时间
     *
     * @param map
     * @return
     */
    public Map<String, String> querySinfo(Map<String, String> map);

    /**
     * 根据改签id,reqtoken ,订单号查询改签检票口数据
     *
     * @param paramMap
     * @return
     */
    Map<String, Object> selectChangeTicketEntrance(Map<String, Object> paramMap);
}
