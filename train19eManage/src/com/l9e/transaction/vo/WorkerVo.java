package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class WorkerVo {

    private static Map<String, String> WORKERSTATUS = new LinkedHashMap<String, String>();
    private static Map<Integer, String> WORKERTYPE = new LinkedHashMap<Integer, String>();
    private static Map<String, String> STOPREASON = new LinkedHashMap<String, String>();
    private static Map<Integer, String> WORKERREGION = new LinkedHashMap<Integer, String>();
    private static Map<Integer, String> WORKERVENDOR = new LinkedHashMap<Integer, String>();
    private static Map<Integer, String> WORKERLANGUAGE = new LinkedHashMap<Integer, String>();

    static {

        STOPREASON.put("11", "需重启");
        STOPREASON.put("22", "IP被封");
        STOPREASON.put("33", "余额不足");
        STOPREASON.put("44", "其他");
        STOPREASON.put("55", "人工");

        WORKERSTATUS.put("00", "空闲");
        WORKERSTATUS.put("01", "队列");
        WORKERSTATUS.put("11", "启用");
        WORKERSTATUS.put("22", "停用");
        WORKERSTATUS.put("33", "备用");
        WORKERSTATUS.put("99", "人工");

        WORKERTYPE.put(new Integer(1), "预订");
        WORKERTYPE.put(new Integer(2), "人工");
        WORKERTYPE.put(new Integer(3), "支付");
        WORKERTYPE.put(new Integer(5), "核验");
        WORKERTYPE.put(new Integer(6), "取消");
        WORKERTYPE.put(new Integer(7), "改签");
        WORKERTYPE.put(new Integer(8), "退票");
        WORKERTYPE.put(new Integer(9), "余票");
        WORKERTYPE.put(new Integer(10), "认证");
        WORKERTYPE.put(new Integer(11), "票价");
        WORKERTYPE.put(new Integer(13), "删除");
        WORKERTYPE.put(new Integer(14), "注册");
        WORKERTYPE.put(new Integer(15), "激活");
        WORKERTYPE.put(new Integer(21), "携程");


        WORKERVENDOR.put(new Integer(1), "阿里云");
        WORKERVENDOR.put(new Integer(2), "美团云");
        WORKERVENDOR.put(new Integer(3), "百度云");
        WORKERVENDOR.put(new Integer(4), "腾讯云");
        WORKERVENDOR.put(new Integer(5), "金山云");
        WORKERVENDOR.put(new Integer(6), "华为云");
        WORKERVENDOR.put(new Integer(7), "亚马逊云");
        WORKERVENDOR.put(new Integer(8), "微软云");
        WORKERVENDOR.put(new Integer(9), "京东云");

        WORKERREGION.put(new Integer(1), "A华北1(青岛)");
        WORKERREGION.put(new Integer(2), "A华北2(北京)");
        WORKERREGION.put(new Integer(3), "A华东1(杭州)");
        WORKERREGION.put(new Integer(4), "A华东2(上海)");
        WORKERREGION.put(new Integer(5), "A华南1(深圳)");
        WORKERREGION.put(new Integer(6), "M华北1");
        //WORKERREGION.put(new Integer(7), "M华北1(B北京4)");
        WORKERREGION.put(new Integer(8), "M华东1");
        WORKERREGION.put(new Integer(9), "B华北(北京)");
        WORKERREGION.put(new Integer(10), "B华东(苏州)");
        WORKERREGION.put(new Integer(11), "B华南(广州)");
        WORKERREGION.put(new Integer(12), "T(华北)北京");
        WORKERREGION.put(new Integer(13), "T(华东)上海");
        WORKERREGION.put(new Integer(14), "T(华南)广州");
        WORKERREGION.put(new Integer(15), "T(西南)成都");

        WORKERREGION.put(new Integer(16), "J(华北)北京");
        WORKERREGION.put(new Integer(17), "J(华南)广州");
        WORKERREGION.put(new Integer(18), "J(华东)宿迁");
        WORKERREGION.put(new Integer(19), "J(华东)上海");

        WORKERLANGUAGE.put(new Integer(1), "LUA");
        WORKERLANGUAGE.put(new Integer(2), "JAVA");
        WORKERLANGUAGE.put(new Integer(3), "PYTHON");


    }

    String worker_name;
    String worker_type;
    String worker_priority;
    String order_num;
    String max_order_num;
    String worker_ext;
    String public_ip;
    String worker_status;
    String worker_id;
    String opt_name;
    String robot_id;
    String robot_con_timeout;
    String robot_read_timeout;
    String spare_thread;
    String stop_reason;
    String proxy_status;
    String app_valid;
    String pay_device_type;
    String worker_region;
    String worker_vendor;
    String worker_describe;
    String worker_language_type;

    public static Map<String, String> getWorkerStatus() {
        //00、正在下单；22、账号停用；33、账号空闲
        return WORKERSTATUS;
    }

    public static Map<String, String> getStopReason() {
//		机器人停用原因11 机器人需要重启22 机器人IP被封33 该支付机器人余额不足 44 其他
        return STOPREASON;
    }

    public static Map<Integer, String> getWorkerType() {
        // 1、预定机器人 2、预定人工 3、支付机器人 4、支付人工  5、查询机器人 6、人工 7、取消机器人
        return WORKERTYPE;
    }

    public static Map<Integer, String> getWorkerRegion() {
        //  1.华北1(青岛)2.华北2(北京)3.华东1(杭州)4.华东2(上海)5.华南1(深圳)
        return WORKERREGION;
    }

    public static Map<Integer, String> getWorkerVendor() {
        //	1.阿里云 2.美团云 3.百度云 4.腾讯云 5.金山云 6.华为云 7.亚马逊云 8.微软云 9.京东云
        return WORKERVENDOR;
    }

    public static Map<Integer, String> getWorkerLanguage() {
        //机器脚本语言类型： 1：lua脚本 2：java脚本
        return WORKERLANGUAGE;
    }

    public String getWorker_name() {
        return worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getWorker_type() {
        return worker_type;
    }

    public void setWorker_type(String worker_type) {
        this.worker_type = worker_type;
    }

    public String getWorker_priority() {
        return worker_priority;
    }

    public void setWorker_priority(String worker_priority) {
        this.worker_priority = worker_priority;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getMax_order_num() {
        return max_order_num;
    }

    public void setMax_order_num(String max_order_num) {
        this.max_order_num = max_order_num;
    }

    public String getWorker_ext() {
        return worker_ext;
    }

    public void setWorker_ext(String worker_ext) {
        this.worker_ext = worker_ext;
    }

    public String getWorker_status() {
        return worker_status;
    }

    public void setWorker_status(String worker_status) {
        this.worker_status = worker_status;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public String getOpt_name() {
        return opt_name;
    }

    public void setOpt_name(String optName) {
        opt_name = optName;
    }

    public String getRobot_id() {
        return robot_id;
    }

    public void setRobot_id(String robotId) {
        robot_id = robotId;
    }

    public String getRobot_con_timeout() {
        return robot_con_timeout;
    }

    public void setRobot_con_timeout(String robotConTimeout) {
        robot_con_timeout = robotConTimeout;
    }

    public String getRobot_read_timeout() {
        return robot_read_timeout;
    }

    public void setRobot_read_timeout(String robotReadTimeout) {
        robot_read_timeout = robotReadTimeout;
    }

    public String getSpare_thread() {
        return spare_thread;
    }

    public void setSpare_thread(String spareThread) {
        spare_thread = spareThread;
    }

    public String getStop_reason() {
        return stop_reason;
    }

    public void setStop_reason(String stopReason) {
        stop_reason = stopReason;
    }

    public String getProxy_status() {
        return proxy_status;
    }

    public void setProxy_status(String proxyStatus) {
        proxy_status = proxyStatus;
    }

    public String getApp_valid() {
        return app_valid;
    }

    public void setApp_valid(String app_valid) {
        this.app_valid = app_valid;
    }

    public String getPay_device_type() {
        return pay_device_type;
    }

    public void setPay_device_type(String pay_device_type) {
        this.pay_device_type = pay_device_type;
    }

    public String getWorker_region() {
        return worker_region;
    }

    public void setWorker_region(String worker_region) {
        this.worker_region = worker_region;
    }

    public String getWorker_vendor() {
        return worker_vendor;
    }

    public void setWorker_vendor(String worker_vendor) {
        this.worker_vendor = worker_vendor;
    }

    public String getWorker_describe() {
        return worker_describe;
    }

    public void setWorker_describe(String worker_describe) {
        this.worker_describe = worker_describe;
    }

    public String getWorker_language_type() {
        return worker_language_type;
    }

    public void setWorker_language_type(String worker_language_type) {
        this.worker_language_type = worker_language_type;
    }

    public String getPublic_ip() {
        return public_ip;
    }

    public void setPublic_ip(String public_ip) {
        this.public_ip = public_ip;
    }

}
