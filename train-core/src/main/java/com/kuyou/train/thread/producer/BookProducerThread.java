package com.kuyou.train.thread.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.constant.SupplierCode;
import com.kuyou.train.common.enums.AccountWayEnum;
import com.kuyou.train.common.enums.SettingEnum;
import com.kuyou.train.common.status.AccountStatus;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.common.util.HttpUtil;
import com.kuyou.train.common.util.WeightUtil;
import com.kuyou.train.entity.po.*;
import com.kuyou.train.entity.req.BookOrderReq;
import com.kuyou.train.entity.req.BookOrderTicketReq;
import com.kuyou.train.thread.BaseThread;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * BookProducerThread
 *
 * @author taokai3
 * @date 2018/12/25
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BookProducerThread extends BaseThread<OrderPo> {

    private static final String filterAccountUrl = "http://10.16.22.31:19020/accountN/filterAccount";
    private static final String getAccountQueue = "http://10.16.22.31:19020/accountN/getChannelAccount";

    private OrderPo order;

    @Override
    public void execute() {
        String orderId = order.getOrderId();
        String myOrderId = order.getMyOrderId();
        log.info("预订订单信息 orderId:{}, myOrderId:{}", orderId, myOrderId);

        String supplierType = order.getSupplierType();
        String channel = order.getChannel();
        Boolean canSwitch = order.getCanSwitchSupplier();
        log.info("channel:{}, canSwitch:{}, supplierType:{}{}", channel, canSwitch, supplierType, SupplierCode.MESSAGE);

        //查询乘客信息
        List<OrderCpPo> cps = orderCpService.selectByOrderId(orderId);

        //组装参数
        BookOrderReq bookOrderReq = buildParameter(cps);

        if (canSwitch == null || canSwitch) {
            //获取权重，重新分配
            supplierType = getWeight();
            if (SupplierCode.HTHY.equals(supplierType)) {
                //航天华有出票
                hthyBook(bookOrderReq);
            } else {
                //机器人出票
                cralwerBook(bookOrderReq);
            }
        } else {
            //找到指定的供货商
            if (SupplierCode.HTHY.equals(supplierType)) {
                //航天华有出票
                hthyBook(bookOrderReq);
            } else {
                //机器人出票
                cralwerBook(bookOrderReq);
            }
        }
    }


    /**
     * 机器人出票
     *
     * @param bookOrderReq
     */
    private void cralwerBook(BookOrderReq bookOrderReq) {
        String orderId = order.getOrderId();
        Integer accountId = order.getAccountId();
        Integer accountFromWay = order.getAccountFromWay();

        //获取账号
        AccountPo account = getAccount(bookOrderReq.getTickets(), accountFromWay, accountId);
        if (account == null) {
            log.info("没有可用账号");
            historyService.insertBookLog(orderId, "没有可用账号");
            OrderPo updateOrder = new OrderPo();
            updateOrder.setOrderStatus(OrderStatus.BOOK_MANUAL);
            updateOrder.setReturnOptlog("sd");
            orderService.updateByOrderId(updateOrder, orderId, OrderStatus.BOOK_ING);
            return;
        } else {
            bookOrderReq.setUsername(account.getUsername());
            bookOrderReq.setPassword(account.getPassword());
            String name = AccountWayEnum.getByVaule(accountFromWay).getName();
            log.info("分配账号 {}:{}, 账号来源:{}", account.getUsername(), account.getPassword(), name);
            historyService.insertBookLog(orderId, String.format("分配账号 %s:%s, 账号来源:%s", account.getUsername(), account.getPassword(), name));
            int update = accountService.updateStatusByIds(AccountStatus.WORK_ING, Lists.newArrayList(account.getAccountId()));
            log.info("账号‘使用中’跟新结果:{}", update);
        }

        OrderPo updateOrder = new OrderPo();
        updateOrder.setCanSwitchSupplier(false);
        updateOrder.setAccountId(account.getAccountId());
        orderService.updateByOrderId(updateOrder, orderId, OrderStatus.BOOK_ING);

        //将数据推送到redis
        Long lpush = orderJedisClient.lpush(KeyConstant.BOOK_REQ, JSON.toJSONString(bookOrderReq));
        log.info("预订占位订单推送结果:{}", lpush);
    }

    /**
     * 航天华有出票
     *
     * @param bookOrderReq
     */
    private void hthyBook(BookOrderReq bookOrderReq) {
        String orderId = bookOrderReq.getOrderId();
        //判断是否是自有账号
        Integer accountFromWay = order.getAccountFromWay();
        Integer accountId = order.getAccountId();
        if (AccountWayEnum.KUYOU.getValue().equals(accountFromWay)) {
            log.info("航天华有出票，自有账号重置为空");
            OrderPo updateOrder = new OrderPo();
            updateOrder.setAccountId(0);
            orderService.updateByOrderId(updateOrder, orderId, OrderStatus.BOOK_ING);
            bookOrderReq.setUsername("");
            bookOrderReq.setPassword("");
        } else if (accountId != null && !accountId.equals(0)) {
            AccountPo accountPo = accountService.selectByAccountId(accountId);
            bookOrderReq.setUsername(accountPo.getUsername());
            bookOrderReq.setPassword(accountPo.getPassword());
        }

        //更新取消
        OrderPo updateOrder = new OrderPo();
        updateOrder.setSupplierType(SupplierCode.HTHY);
        orderService.updateByOrderId(updateOrder, orderId, OrderStatus.BOOK_ING);

        hthyService.bookOrder(bookOrderReq);
    }

    /**
     * 获取账号
     *
     * @param cps
     * @param accountFromWay
     * @param accountId
     * @return
     */
    private AccountPo getAccount(List<BookOrderTicketReq> cps, Integer accountFromWay, Integer accountId) {
        log.info("accountId:{}, accountFromWay:{}{}", accountId, accountFromWay, AccountWayEnum.message);

        AccountPo accountPo = null;
        if (AccountWayEnum.KUYOU.getValue().equals(accountFromWay) && (accountId == null || accountId.equals(0))) {
            //提取idcardNo
            StringBuffer cardList = new StringBuffer();
            for (BookOrderTicketReq cp : cps) {
                String certNo = cp.getCardNo();
                certNo = certNo.toUpperCase().replace("\\s", "");
                cardList.append(certNo).append(",");
            }
            log.info("根据常旅白名单获取账号cardList:{}", cardList);

            //调用服务
            String url = filterAccountUrl + "?passportNo=" + cardList;

            String response = new HttpUtil().doHttpGet(url, 1000 * 10);
            log.info("获取账号结果:{}", response);
            if (StringUtils.isNotBlank(response)) {
                JSONObject json = JSONObject.parseObject(response);
                Boolean success = json.getBoolean("success");
                if (success) {
                    accountPo = new AccountPo();
                    JSONObject dataJson = json.getJSONObject("data");
                    accountPo.setUsername(dataJson.getString("username"));
                    accountPo.setPassword(dataJson.getString("password"));
                    accountPo.setAccountId(dataJson.getInteger("id"));
                    return accountPo;
                }
            }

            //从对垒中获取
            int index = 0;
            url = getAccountQueue + "?channel=19e&passengerSize=" + cps.size();
            while (index++ < 3) {
                log.info("第 {} 次获取账号4Queue", index);
                try {

                    response = new HttpUtil().doHttpGet(url, 1000 * 10);
                    log.info("index-{}-获取账号Queue结果:{}", index, response);
                    if (StringUtils.isBlank(response)) {
                        Thread.sleep(500);
                        continue;
                    }
                    JSONObject json = JSONObject.parseObject(response);
                    Boolean success = json.getBoolean("success");
                    if (success) {
                        accountPo = new AccountPo();
                        JSONObject dataJson = json.getJSONObject("data");
                        accountPo.setUsername(dataJson.getString("username"));
                        accountPo.setPassword(dataJson.getString("password"));
                        accountPo.setAccountId(dataJson.getInteger("id"));
                        break;
                    }
                } catch (Exception e) {
                    log.info("{}-获取账号Queue异常", index, e);
                }
            }
            if (accountPo != null) {
                return accountPo;
            }

            index = 0;
            url = getAccountQueue + "?channel=19e&passengerSize=0";
            while (index++ < 3) {
                log.info("第 {} 次获取账号0Size", index);
                try {

                    response = new HttpUtil().doHttpGet(url, 1000 * 10);
                    log.info("index-{}-获取账号QSize结果:{}", index, response);
                    if (StringUtils.isBlank(response)) {
                        Thread.sleep(500);
                        continue;
                    }
                    JSONObject json = JSONObject.parseObject(response);
                    Boolean success = json.getBoolean("success");
                    if (success) {
                        accountPo = new AccountPo();
                        JSONObject dataJson = json.getJSONObject("data");
                        accountPo.setUsername(dataJson.getString("username"));
                        accountPo.setPassword(dataJson.getString("password"));
                        accountPo.setAccountId(dataJson.getInteger("id"));
                        break;
                    }
                } catch (Exception e) {
                    log.info("{}-获取账号Size异常", index, e);
                }
            }
        } else {
            accountPo = accountService.selectByAccountId(accountId);
        }
        return accountPo;
    }

    /**
     * 分配权重
     *
     * @return
     */
    private String getWeight() {
        //获取pc权重
        SettingPo crawler = settingService.selectByName(SettingEnum.CRALWER_WEIGHT);
        SettingPo hthy = settingService.selectByName(SettingEnum.HTHY_WEIGHT);
        Integer crawlerWeight = 1;
        if (StringUtils.isNotBlank(crawler.getValue())) {
            crawlerWeight = Integer.valueOf(crawler.getValue());
        }
        Integer hthyWeight = 0;
        if (StringUtils.isNotBlank(hthy.getValue())) {
            hthyWeight = Integer.valueOf(hthy.getValue());
        }
        log.info("机器人权重:{}", crawlerWeight);
        log.info("航天华有权重:{}", hthyWeight);

        if (crawlerWeight.equals(0) && hthyWeight.equals(0)) {
            crawlerWeight = 1;
        }

        // 获取航天华有权重
        WeightUtil.WeightCategory crawlerWc = new WeightUtil.WeightCategory(SupplierCode.KYFW, crawlerWeight);
        WeightUtil.WeightCategory hthyWc = new WeightUtil.WeightCategory(SupplierCode.HTHY, hthyWeight);
        String weight = new WeightUtil().randomWeight(crawlerWc, hthyWc);
        log.info("出票权重分配结果:{}", weight);
        return weight;
    }

    /**
     * 构造参数
     *
     * @param cps
     * @return
     */
    private BookOrderReq buildParameter(List<OrderCpPo> cps) {
        //无座判断
        // 0#9,0
        // 8#无
        boolean acceptNoSeat = true;
        String extraSeatType = order.getExtSeattype();
        String seatType = order.getSeatType().toString();
        log.info("本次预订坐席:{}, 备选坐席:{}", seatType, extraSeatType);
        extraSeatType = StringUtils.isBlank(extraSeatType) ? "" : extraSeatType.split("#")[1];
        String extra = extraSeatType.split(",")[0];
        if (!"9".equals(extra) && !"无".equals(extra) && !"9".equals(seatType)) {
            acceptNoSeat = false;
        }

        //车站获取
        //车站获取
        String fromStationName = order.getFromCity();
        String toStationName = order.getToCity();
        String fromStationCode = order.getFrom3c();
        String toStationCode = order.getTo3c();

        if (StringUtils.isBlank(fromStationName)) {
            fromStationName = dataJedisClient.hget(KeyConstant.STATION_CODE2NAME, fromStationCode);
        } else if (StringUtils.isBlank(fromStationCode)) {
            fromStationCode = dataJedisClient.hget(KeyConstant.STATION_NAME2CODE, fromStationName);
        }

        if (StringUtils.isBlank(toStationName)) {
            toStationName = dataJedisClient.hget(KeyConstant.STATION_CODE2NAME, toStationCode);
        } else if (StringUtils.isBlank(toStationCode)) {
            toStationCode = dataJedisClient.hget(KeyConstant.STATION_NAME2CODE, toStationName);
        }
        log.info("from:{}-{}, to:{}-{}", fromStationName, fromStationCode, toStationName, toStationCode);

        Date travelTime = order.getTravelTime();
        if (travelTime == null) {
            travelTime = order.getFromTime();
        }
        //组装参数
        BookOrderReq bookOrderReq = new BookOrderReq();
        bookOrderReq.setOrderId(order.getOrderId());
        bookOrderReq.setMyOrderId(order.getMyOrderId());
        bookOrderReq.setTrainCode(order.getTrainNo());
        bookOrderReq.setFromDate(travelTime);
        bookOrderReq.setFromStationName(fromStationName);
        bookOrderReq.setFromStationCode(fromStationCode);
        bookOrderReq.setToStationName(toStationName);
        bookOrderReq.setToStationCode(toStationCode);
        bookOrderReq.setAcceptNoSeat(acceptNoSeat);
        bookOrderReq.setChooseSeats(order.getChooseSeats());

        //组装乘客
        List<BookOrderTicketReq> tickets = Lists.newArrayList();
        for (OrderCpPo cp : cps) {
            BookOrderTicketReq.BookOrderTicketReqBuilder reqBuilder = BookOrderTicketReq.builder();
            reqBuilder.cpId(cp.getCpId()).name(cp.getUserName()).cardType(cp.getCertType().toString());
            reqBuilder.ticketType(cp.getTicketType().toString()).seatType(seatType);
            reqBuilder.bornDate(cp.getBorthDate()).cardNo(cp.getCertNo()).validDateEnd(cp.getValidDateEnd());

            //学生票信息
            OrderStudentPo studentPo = orderCpService.selectStudent(cp.getCpId());
            if (studentPo != null) {
                reqBuilder.provinceName(studentPo.getProvinceName()).provinceCode(studentPo.getProvinceCode());
                reqBuilder.schoolName(studentPo.getSchoolName()).schoolCode(studentPo.getSchoolCode());
                reqBuilder.studentNo(studentPo.getStudentNo()).system(studentPo.getSystem())
                        .enterYear(studentPo.getEnterYear());
                reqBuilder.stuFromStationName(studentPo.getStuFromStationName())
                        .stuFromStationCode(studentPo.getStuFromStationCode());
                reqBuilder.stuToStationName(studentPo.getStuToStationName())
                        .stuToStationCode(studentPo.getStuToStationName());
            }
            tickets.add(reqBuilder.build());
        }
        bookOrderReq.setTickets(tickets);
        return bookOrderReq;
    }
}
