/*
SQLyog Ultimate v8.71 
MySQL - 5.5.28-29.2-log : Database - hcpiao
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`hcpiao` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `hcpiao`;

/*Table structure for table `account_logs` */

DROP TABLE IF EXISTS `account_logs`;

CREATE TABLE `account_logs` (
  `log_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `acc_username` varchar(20) DEFAULT NULL COMMENT '账号名称',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `opt_logs` varchar(50) DEFAULT NULL COMMENT '日志内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1616 DEFAULT CHARSET=utf8;

/*Table structure for table `account_statistics` */

DROP TABLE IF EXISTS `account_statistics`;

CREATE TABLE `account_statistics` (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `statistics_date` date NOT NULL COMMENT '统计日期',
  `add_account` int(11) DEFAULT '0' COMMENT '新增帐号',
  `white_list` int(11) DEFAULT '0' COMMENT '白名单总数',
  `surplus_passenger` int(11) DEFAULT '0' COMMENT '剩余联系人',
  `ticket_sum` int(11) DEFAULT '0' COMMENT '总票数',
  `white_list_rate` double(2,1) DEFAULT '0.0' COMMENT '白名单匹配率',
  `add_white_list` int(11) DEFAULT '0' COMMENT '新增白名单',
  `upper_limit` int(11) DEFAULT '0' COMMENT '联系人达上线',
  `user_info` int(11) DEFAULT '0' COMMENT '用户信息待核验',
  `phone_verifi` int(11) DEFAULT '0' COMMENT '手机待核验',
  `other_sum` int(11) DEFAULT '0' COMMENT '其他',
  `x1_rate` double(2,1) DEFAULT '0.0' COMMENT '一人占比',
  `x2_rate` double(2,1) DEFAULT '0.0' COMMENT '二人占比',
  `x3_rate` double(2,1) DEFAULT '0.0' COMMENT '三人占比',
  `x4_rate` double(2,1) DEFAULT '0.0' COMMENT '四人占比',
  `x5_rate` double(2,1) DEFAULT '0.0' COMMENT '五人占比',
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Table structure for table `account_status` */

DROP TABLE IF EXISTS `account_status`;

CREATE TABLE `account_status` (
  `code` char(2) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `alipay` */

DROP TABLE IF EXISTS `alipay`;

CREATE TABLE `alipay` (
  `order_no` varchar(100) DEFAULT NULL,
  `alipay_no` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `alipay_perday_account_balance` */

DROP TABLE IF EXISTS `alipay_perday_account_balance`;

CREATE TABLE `alipay_perday_account_balance` (
  `check_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_time` date DEFAULT NULL COMMENT '支付时间',
  `alipay_id` varchar(30) DEFAULT NULL COMMENT '支付宝账号',
  `account_balance` decimal(11,2) DEFAULT NULL COMMENT '账户余额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`check_id`),
  KEY `idx_balance_pay_time` (`pay_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000765 DEFAULT CHARSET=utf8 COMMENT='支付宝每日账户余额';

/*Table structure for table `app_complain` */

DROP TABLE IF EXISTS `app_complain`;

CREATE TABLE `app_complain` (
  `complain_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '投诉建议id',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户id',
  `question_type` varchar(2) DEFAULT NULL COMMENT '问题类别  00：订单问题 11：出票问题 22：业务建议 55：其他',
  `question` varchar(1000) DEFAULT NULL COMMENT '问题或建议',
  `answer` varchar(1000) DEFAULT NULL COMMENT '答复',
  `permission` varchar(2) DEFAULT NULL COMMENT '权限 0：全部可见 1：自己可见',
  `create_time` datetime DEFAULT NULL COMMENT '提问时间',
  `reply_time` datetime DEFAULT NULL COMMENT '答复时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`complain_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COMMENT='客户端投诉与建议';

/*Table structure for table `app_notice_userinfo` */

DROP TABLE IF EXISTS `app_notice_userinfo`;

CREATE TABLE `app_notice_userinfo` (
  `nu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `notice_id` int(11) DEFAULT NULL COMMENT '消息id',
  `user_phone` varchar(15) DEFAULT NULL COMMENT '用户手机号',
  `status` varchar(2) DEFAULT NULL COMMENT '00：未接收；11：已接受',
  PRIMARY KEY (`nu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COMMENT='用户消息关联表';

/*Table structure for table `app_noticeinfo` */

DROP TABLE IF EXISTS `app_noticeinfo`;

CREATE TABLE `app_noticeinfo` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `notice_name` varchar(45) DEFAULT NULL COMMENT '消息名称',
  `notice_status` varchar(10) DEFAULT NULL COMMENT '消息状态 00、未发布 11、已发布  22、已到期',
  `notice_content` varchar(8000) DEFAULT NULL COMMENT '消息内容',
  `notice_system` varchar(2) DEFAULT NULL COMMENT '消息发布对象： 00、全部 11、ios 22、android 33、个人 ',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '创建人',
  `pub_time` datetime DEFAULT NULL COMMENT '公告发布时间',
  `stop_time` datetime DEFAULT NULL COMMENT '公告结束时间',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='消息表';

/*Table structure for table `app_oftentraininfo` */

DROP TABLE IF EXISTS `app_oftentraininfo`;

CREATE TABLE `app_oftentraininfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_station` varchar(30) DEFAULT NULL,
  `arrive_station` varchar(30) DEFAULT NULL,
  `price` decimal(8,1) DEFAULT NULL,
  `station` varchar(30) DEFAULT NULL,
  `show_order` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

/*Table structure for table `app_order_payinfo` */

DROP TABLE IF EXISTS `app_order_payinfo`;

CREATE TABLE `app_order_payinfo` (
  `pay_id` varchar(30) NOT NULL COMMENT '提供接口商标识',
  `merchant_id` varchar(30) NOT NULL COMMENT '提供接口商户编号',
  `pc_id` varchar(10) DEFAULT NULL COMMENT '支付通道',
  `pm_id` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `sign_key` varchar(50) DEFAULT NULL COMMENT '密钥',
  `url_pay` varchar(100) DEFAULT NULL COMMENT '下单路径',
  `version_id` varchar(10) DEFAULT NULL COMMENT '版本号',
  `comment` varchar(100) DEFAULT NULL COMMENT '备注',
  `spare1` varchar(50) DEFAULT NULL COMMENT '备用字段1',
  `spare2` varchar(50) DEFAULT NULL COMMENT '备用字段1',
  PRIMARY KEY (`pay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='19trip提供下单支付接口商户信息';

/*Table structure for table `app_orderinfo` */

DROP TABLE IF EXISTS `app_orderinfo`;

CREATE TABLE `app_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,1) DEFAULT NULL COMMENT '支付价格(sum_amount)',
  `ticket_pay_money` decimal(11,1) DEFAULT NULL COMMENT '票价总额',
  `buy_money` decimal(11,1) DEFAULT NULL COMMENT '成本价格',
  `bx_pay_money` decimal(11,1) DEFAULT '0.0' COMMENT '保险总额',
  `order_status` varchar(3) DEFAULT NULL COMMENT '订单状态 00、未支付 01、准备预定 11、支付成功 21、超时未支付  22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成  99、已取消',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `refund_deadline_ignore` varchar(1) DEFAULT NULL COMMENT '1：无视截止时间（发车12小时后为退款截止时间）',
  `refund_total` decimal(11,1) DEFAULT NULL COMMENT '退款金额总计',
  `can_refund` varchar(1) DEFAULT NULL COMMENT '是否可以退款（默认可以退 0:不能退 1:能退）',
  `sms_notify` varchar(1) DEFAULT NULL COMMENT '是否短信通知  1：是；0:否',
  `ext_seat` varchar(30) DEFAULT NULL COMMENT '扩展坐席',
  `order_level` varchar(1) DEFAULT '0' COMMENT '订单级别：0：普通订单；1：VIP订单',
  `link_name` varchar(50) DEFAULT NULL COMMENT '短信通知联系人',
  `link_phone` varchar(15) DEFAULT NULL COMMENT '短信通知号码',
  `order_pro1` varchar(30) DEFAULT NULL COMMENT '备用字段1(退款url  refund_url)',
  `order_pro2` varchar(30) DEFAULT NULL COMMENT '备用字段2',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户表主键user_id',
  `pay_order_id` varchar(50) DEFAULT NULL COMMENT '支付订单号（支付接口使用）',
  `product_type` varchar(5) DEFAULT NULL COMMENT '产品类型别：1、有 0、没有（用二进制10000表示，第一位代表火车票，第二位代表飞机票， 第三位代表景点门票，第四位代表酒店，其它几位空闲）',
  `gps_info` varchar(50) DEFAULT NULL COMMENT '位置信息',
  `zfb_trade_no` varchar(50) DEFAULT NULL COMMENT '支付宝交易号、19pay支付流水号',
  `pay_type` varchar(2) DEFAULT '11' COMMENT '11、联动优势支付；22、支付宝支付; 33、微信支付；44、19pay支付',
  `channel` varchar(10) NOT NULL DEFAULT '' COMMENT '渠道ID',
  `notify_id` varchar(128) DEFAULT NULL COMMENT '微信支付结果通知id',
  `transaction_id` varchar(28) DEFAULT NULL COMMENT '微信交易号',
  `return_fee` varchar(10) DEFAULT NULL COMMENT '微信返回支付金额',
  `discount` varchar(10) DEFAULT NULL COMMENT '微信返回折扣券',
  `ticket_num` int(2) DEFAULT NULL COMMENT '车票数',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票订单信息表';

/*Table structure for table `app_orderinfo_bx` */

DROP TABLE IF EXISTS `app_orderinfo_bx`;

CREATE TABLE `app_orderinfo_bx` (
  `bx_id` varchar(50) NOT NULL COMMENT '保险单号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '车票ID',
  `from_name` varchar(45) DEFAULT NULL COMMENT '出发地址',
  `to_name` varchar(45) DEFAULT NULL COMMENT '到达地址',
  `ids_type` varchar(1) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户姓名',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '用户证件号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL,
  `telephone` varchar(15) DEFAULT NULL COMMENT '联系电话',
  `bx_status` int(2) DEFAULT NULL COMMENT '状态：0、未发送 1、正在发送 2、发送完成 3、正在撤销 4、撤销完成 5、发送失败 6、需要取消 7、取消失败',
  `bx_code` varchar(45) DEFAULT NULL COMMENT '保险单号',
  `bx_billno` varchar(45) DEFAULT NULL COMMENT '服务器端订单号',
  `pay_money` decimal(11,1) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,1) DEFAULT NULL COMMENT '进货金额',
  `product_id` varchar(15) DEFAULT NULL COMMENT '保险产品Id',
  `effect_date` varchar(20) DEFAULT NULL COMMENT '生效日期',
  `train_no` varchar(45) DEFAULT NULL COMMENT '车次',
  `bx_channel` varchar(1) DEFAULT NULL COMMENT '保险渠道: 1、快保   2、合众',
  PRIMARY KEY (`bx_id`),
  KEY `FK_Reference_app_idx` (`order_id`),
  CONSTRAINT `FK_Reference_app_idx` FOREIGN KEY (`order_id`) REFERENCES `app_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端保险单信息';

/*Table structure for table `app_orderinfo_bxfp` */

DROP TABLE IF EXISTS `app_orderinfo_bxfp`;

CREATE TABLE `app_orderinfo_bxfp` (
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '订单号',
  `fp_receiver` varchar(50) DEFAULT NULL COMMENT '收件人',
  `fp_phone` varchar(15) DEFAULT NULL COMMENT '联系电话',
  `fp_zip_code` varchar(10) DEFAULT NULL COMMENT '邮编',
  `fp_address` varchar(500) DEFAULT NULL COMMENT '收件地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端保险发票信息';

/*Table structure for table `app_orderinfo_cp` */

DROP TABLE IF EXISTS `app_orderinfo_cp`;

CREATE TABLE `app_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `from_station` varchar(30) DEFAULT NULL COMMENT '出发城市',
  `arrive_station` varchar(30) DEFAULT NULL COMMENT '到达城市',
  `from_time` varchar(10) DEFAULT NULL COMMENT '出发时间',
  `arrive_time` varchar(10) DEFAULT NULL COMMENT '到达时间',
  `travel_time` date DEFAULT NULL COMMENT '乘车日期',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(1) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `ids_type` int(1) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(20) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(15) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,1) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,1) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` int(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(3) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(5) DEFAULT NULL COMMENT '座位号',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '退款状态：00:无退票 11: 同意退票 22: 拒绝退票 33:退票完成 44:退票失败',
  `refund_fail_reason` varchar(300) DEFAULT NULL COMMENT '拒绝退款原因/退款失败原因',
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_app2_idx` (`order_id`),
  CONSTRAINT `FK_Reference_app2_idx` FOREIGN KEY (`order_id`) REFERENCES `app_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端火车票订购信息表';

/*Table structure for table `app_orderinfo_history` */

DROP TABLE IF EXISTS `app_orderinfo_history`;

CREATE TABLE `app_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表自增ID',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=477 DEFAULT CHARSET=utf8;

/*Table structure for table `app_orderinfo_refundnotify` */

DROP TABLE IF EXISTS `app_orderinfo_refundnotify`;

CREATE TABLE `app_orderinfo_refundnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `refund_type` varchar(2) DEFAULT '00' COMMENT '退款类型：00：用户申请退款；11：差额退款；22：出票失败退款',
  `refund_money` decimal(11,2) DEFAULT NULL COMMENT '退款金额',
  `refund_seq` varchar(45) DEFAULT NULL COMMENT '退款流水号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '通知状态：00、未通知 11、准备通知 22、开始通知 33、申请成功 44、申请失败',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `order_money` varchar(50) DEFAULT NULL COMMENT '订单金额',
  `notify_fail_reason` varchar(300) DEFAULT NULL COMMENT '通知失败原因',
  `order_create_time` varchar(30) DEFAULT NULL COMMENT '生成订单时间',
  `pay_type` varchar(2) DEFAULT NULL COMMENT '11、联动优势支付；22、支付宝支付; 33、微信支付；44、19pay支付',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='app退票退款通知表';

/*Table structure for table `app_orderinfo_refundstream` */

DROP TABLE IF EXISTS `app_orderinfo_refundstream`;

CREATE TABLE `app_orderinfo_refundstream` (
  `stream_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `refund_type` varchar(1) DEFAULT NULL COMMENT '退款类型：1、用户线上退票 2、改签差额退款 3、用户电话退票 4、差额退款 5、出票失败退款  6、改签单退款',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(30) DEFAULT NULL COMMENT '退款流水号(trip_no)',
  `refund_money` decimal(11,1) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,1) DEFAULT NULL COMMENT '实际退款金额',
  `alter_tickets_money` decimal(11,1) DEFAULT NULL COMMENT '改签差价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注',
  `our_remark` varchar(200) DEFAULT NULL COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(30) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refund_status` varchar(2) DEFAULT NULL COMMENT '退款状态：00、等待退票 11、开始退票 22、拒绝退票 33、退票完成 44、退票失败 55、微信退款处理中 ',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `refund_no` varchar(32) DEFAULT NULL COMMENT 'app退款流水号',
  `bank_username` varchar(30) DEFAULT NULL COMMENT '银行账户姓名',
  `bank_type` varchar(25) DEFAULT NULL COMMENT '乘客银行类型',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '乘客银行账号',
  `bank_openName` varchar(100) DEFAULT NULL COMMENT '开户行名称',
  `refund_fail_reason` varchar(200) DEFAULT NULL COMMENT '联动优势退款失败原因',
  `change_ticket_info` varchar(200) DEFAULT NULL COMMENT '改签明细',
  `wx_refund_id` varchar(28) DEFAULT NULL COMMENT '(微信)财付通退款流水号',
  `wx_refund_channel` varchar(2) DEFAULT NULL COMMENT '(微信)退款渠道，0：退到财付通， 1：退到银行',
  `wx_refund_fee` varchar(10) DEFAULT NULL COMMENT '(微信)退款总金额',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志',
  `refund_purl` varchar(100) DEFAULT NULL COMMENT '小票地址',
  `refund_amt` decimal(11,2) DEFAULT NULL COMMENT '联众退款金额',
  PRIMARY KEY (`stream_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8 COMMENT='移动端退款流水表';

/*Table structure for table `app_system_log` */

DROP TABLE IF EXISTS `app_system_log`;

CREATE TABLE `app_system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

/*Table structure for table `app_system_setting` */

DROP TABLE IF EXISTS `app_system_setting`;

CREATE TABLE `app_system_setting` (
  `setting_id` varchar(50) NOT NULL COMMENT '主键',
  `setting_name` varchar(50) DEFAULT NULL COMMENT '名称',
  `setting_value` varchar(2000) DEFAULT NULL COMMENT '内容',
  `setting_status` varchar(2) DEFAULT NULL COMMENT '状态 0:关闭 1:启用',
  `setting_desc` varchar(100) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`setting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户端系统设置表';

/*Table structure for table `app_user_linker` */

DROP TABLE IF EXISTS `app_user_linker`;

CREATE TABLE `app_user_linker` (
  `link_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '联系人ID',
  `link_name` varchar(45) NOT NULL COMMENT '真实姓名',
  `ids_type` varchar(1) NOT NULL DEFAULT '2' COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `ids_card` varchar(18) NOT NULL COMMENT '证件号码',
  `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '用户表user_id',
  `passenger_type` varchar(2) NOT NULL DEFAULT '0' COMMENT '乘客类型：0成人票 1儿童票',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `verify_status` varchar(2) DEFAULT NULL COMMENT '联系人身份核验状态：1已通过 2待核验 3未通过',
  `link_phone` varchar(20) DEFAULT NULL COMMENT '联系电话号码',
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=393 DEFAULT CHARSET=utf8 COMMENT='用户常用联系人表';

/*Table structure for table `app_user_mail_address` */

DROP TABLE IF EXISTS `app_user_mail_address`;

CREATE TABLE `app_user_mail_address` (
  `address_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '邮寄地址ID',
  `addressee_phone` varchar(13) NOT NULL COMMENT '收件人联系电话',
  `addressee` varchar(50) NOT NULL COMMENT '收件人',
  `zip_code` varchar(8) NOT NULL COMMENT '邮编',
  `address_name` varchar(500) NOT NULL COMMENT '地址',
  `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '用户表主键user_id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `province` varchar(10) DEFAULT NULL COMMENT '省',
  `city` varchar(10) DEFAULT NULL COMMENT '市',
  `district` varchar(10) DEFAULT NULL COMMENT '区、县',
  PRIMARY KEY (`address_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='用户常用邮寄地址';

/*Table structure for table `app_userinfo` */

DROP TABLE IF EXISTS `app_userinfo`;

CREATE TABLE `app_userinfo` (
  `user_id` varchar(45) NOT NULL COMMENT '用户ID',
  `user_phone` varchar(15) NOT NULL COMMENT '手机号（登陆账号）',
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户名',
  `user_password` varchar(18) DEFAULT NULL COMMENT '账号密码',
  `user_email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间（注册时间）',
  `user_source` varchar(30) NOT NULL COMMENT 'app:（手机客户端）APP、web:（网站）WEB、weixin：微信、baidu：百度',
  `weather_able` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否启用 1：启用 0：停用',
  `user_verify` varchar(2) NOT NULL DEFAULT '00' COMMENT '用户证件信息审核  00：正在审核  11：审核通过  22：审核未通过',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `login_ip` varchar(20) DEFAULT NULL COMMENT '登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `gps_info` varchar(50) DEFAULT NULL COMMENT '位置信息',
  `12306_name` varchar(50) DEFAULT NULL COMMENT '12306账号',
  `12306_pwd` varchar(32) DEFAULT NULL COMMENT '12306密码',
  `score_num` bigint(10) NOT NULL DEFAULT '0' COMMENT '用户积分',
  `referee_account` varchar(32) DEFAULT NULL COMMENT '推荐人账号',
  `verify_code` varchar(6) DEFAULT NULL COMMENT '注册验证码（六位数字）',
  `phone_pattern` varchar(30) DEFAULT NULL COMMENT '手机型号',
  `login_num` bigint(10) DEFAULT NULL COMMENT '登陆次数',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '操作人',
  `openID` varchar(50) DEFAULT NULL COMMENT '微信openID',
  `user_sex` varchar(1) DEFAULT NULL COMMENT '用户性别：0、男 1、女',
  `user_birth` varchar(12) DEFAULT NULL COMMENT '用户出生日期',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='app用户信息表';

/*Table structure for table `app_zm` */

DROP TABLE IF EXISTS `app_zm`;

CREATE TABLE `app_zm` (
  `zmhz` varchar(10) DEFAULT NULL,
  `py` varchar(5) DEFAULT NULL,
  `lh` varchar(3) DEFAULT NULL,
  `tcs` int(11) DEFAULT NULL,
  `qpy` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `check_price_alipayinfo` */

DROP TABLE IF EXISTS `check_price_alipayinfo`;

CREATE TABLE `check_price_alipayinfo` (
  `check_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bank_pay_seq` varchar(50) DEFAULT NULL COMMENT '支付宝流水',
  `pay_time` varchar(50) DEFAULT NULL COMMENT '支付时间',
  `refund_money` decimal(11,2) DEFAULT NULL COMMENT '收入金额（退款）',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支出金额（出票）',
  `alipay_type` varchar(2) DEFAULT NULL COMMENT '类型:11,余额购票;22余额退款',
  `alipay_id` varchar(50) DEFAULT NULL COMMENT '支付宝账号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `check_seq` varchar(50) DEFAULT NULL COMMENT '单次上传标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `add_status` varchar(2) DEFAULT '00' COMMENT '如果已经有该流水号则提示重复默认00不重复',
  PRIMARY KEY (`check_id`),
  KEY `index_create_time` (`create_time`),
  KEY `NewIndex1` (`bank_pay_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=2247896 DEFAULT CHARSET=utf8;

/*Table structure for table `check_price_alipayinfo_1` */

DROP TABLE IF EXISTS `check_price_alipayinfo_1`;

CREATE TABLE `check_price_alipayinfo_1` (
  `check_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bank_pay_seq` varchar(50) DEFAULT NULL COMMENT '支付宝流水',
  `pay_time` varchar(50) DEFAULT NULL COMMENT '支付时间',
  `refund_money` decimal(11,2) DEFAULT NULL COMMENT '收入金额（退款）',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支出金额（出票）',
  `alipay_type` varchar(2) DEFAULT NULL COMMENT '类型:11,余额购票;22余额退款',
  `alipay_id` varchar(50) DEFAULT NULL COMMENT '支付宝账号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `check_seq` varchar(50) DEFAULT NULL COMMENT '单次上传标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `add_status` varchar(2) DEFAULT '00' COMMENT '如果已经有该流水号则提示重复默认00不重复',
  PRIMARY KEY (`check_id`),
  KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3063217 DEFAULT CHARSET=utf8;

/*Table structure for table `check_price_alipayinfo_copy` */

DROP TABLE IF EXISTS `check_price_alipayinfo_copy`;

CREATE TABLE `check_price_alipayinfo_copy` (
  `check_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bank_pay_seq` varchar(50) DEFAULT NULL COMMENT '支付宝流水',
  `pay_time` varchar(50) DEFAULT NULL COMMENT '支付时间',
  `refund_money` decimal(11,2) DEFAULT NULL COMMENT '收入金额（退款）',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支出金额（出票）',
  `alipay_type` varchar(2) DEFAULT NULL COMMENT '类型:11,余额购票;22余额退款',
  `alipay_id` varchar(50) DEFAULT NULL COMMENT '支付宝账号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `check_seq` varchar(50) DEFAULT NULL COMMENT '单次上传标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `add_status` varchar(2) DEFAULT '00' COMMENT '如果已经有该流水号则提示重复默认00不重复',
  PRIMARY KEY (`check_id`),
  KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1531609 DEFAULT CHARSET=utf8;

/*Table structure for table `check_price_orderinfo` */

DROP TABLE IF EXISTS `check_price_orderinfo`;

CREATE TABLE `check_price_orderinfo` (
  `check_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `bank_pay_seq` varchar(50) DEFAULT NULL COMMENT '支付宝流水',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_price` decimal(11,3) DEFAULT NULL COMMENT '出票总额',
  `refund_price` decimal(11,3) DEFAULT NULL COMMENT '退票总额',
  `ticket_type` varchar(50) DEFAULT NULL COMMENT '类型:11出票 33改签',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `channel` varchar(50) DEFAULT NULL COMMENT '渠道',
  PRIMARY KEY (`check_id`),
  KEY `NewIndex1` (`bank_pay_seq`),
  KEY `NewIndex2` (`order_id`),
  KEY `NewIndex3` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=6173771 DEFAULT CHARSET=utf8;

/*Table structure for table `city` */

DROP TABLE IF EXISTS `city`;

CREATE TABLE `city` (
  `area_id` int(5) NOT NULL COMMENT '主键（无意义）',
  `area_no` varchar(6) DEFAULT NULL COMMENT '区域编号（国家行政编码）',
  `area_name` varchar(100) DEFAULT NULL COMMENT '区域名称',
  `area_shortname` varchar(4) DEFAULT NULL COMMENT '区域简称（针对省份）',
  `area_fullspell` varchar(100) DEFAULT NULL COMMENT '区域全拼（针对市）',
  `area_shortspell` varchar(32) DEFAULT NULL COMMENT '区域简拼（针对市）',
  `area_code` varchar(8) DEFAULT NULL COMMENT '区域区号（针对市）',
  `area_parentno` varchar(6) DEFAULT NULL COMMENT '父区域编号',
  `area_oldno` varchar(6) DEFAULT NULL COMMENT '19e3.0对应编号',
  `area_rank` int(11) DEFAULT NULL COMMENT '区域等级',
  `area_zipcode` varchar(10) DEFAULT NULL COMMENT '区域邮政编码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cm_orderinfo` */

DROP TABLE IF EXISTS `cm_orderinfo`;

CREATE TABLE `cm_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `ticket_pay_money` decimal(11,3) DEFAULT NULL COMMENT '票价总额',
  `bx_pay_money` decimal(11,3) DEFAULT '0.000' COMMENT '保险总额',
  `ps_pay_money` decimal(11,3) DEFAULT '0.000' COMMENT '配送总额',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 00、预下单 11、支付成功  22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、支付失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `cm_phone` varchar(50) DEFAULT NULL COMMENT 'cmpay用户账号',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `timeout` varchar(2) DEFAULT NULL COMMENT '超时重发（1:需要重发 2:重发完成）',
  `refund_deadline_ignore` varchar(2) DEFAULT NULL COMMENT '1：无视截止时间（发车12小时后为退款截止时间）',
  `refund_total` decimal(11,3) DEFAULT NULL COMMENT '退款金额总计',
  `can_refund` varchar(2) DEFAULT NULL COMMENT '是否可以退款（默认可以退 0:不能退 1:能退）',
  `ext_seat` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `pay_no` varchar(50) DEFAULT NULL COMMENT '支付平台交易流水号',
  `request_id` varchar(50) DEFAULT NULL COMMENT '订单流水号',
  `order_source` varchar(2) DEFAULT NULL COMMENT '订单来源：1、pc端， 2、mobile',
  PRIMARY KEY (`order_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_billno` (`out_ticket_billno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票订单信息表';

/*Table structure for table `cm_productinfo` */

DROP TABLE IF EXISTS `cm_productinfo`;

CREATE TABLE `cm_productinfo` (
  `product_id` varchar(15) NOT NULL COMMENT '产品id',
  `type` int(2) DEFAULT NULL COMMENT '产品类别 0:自有产品 1:保险',
  `name` varchar(32) DEFAULT NULL COMMENT '产品名称',
  `status` int(2) DEFAULT NULL COMMENT '产品状态 0:未上架 1:上架',
  `province_id` varchar(10) DEFAULT NULL COMMENT '所属省份id',
  `city_id` varchar(10) DEFAULT NULL COMMENT '所属城市id',
  `sale_type` varchar(2) DEFAULT NULL COMMENT '计费方式 0：元/件 1：元/月 2：元/年',
  `sale_price` decimal(11,3) DEFAULT NULL COMMENT '售价',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `describe` varchar(800) DEFAULT NULL COMMENT '产品描述',
  `level` varchar(2) DEFAULT NULL COMMENT '级别 0：普通 1：vip',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品表';

/*Table structure for table `coupon_info` */

DROP TABLE IF EXISTS `coupon_info`;

CREATE TABLE `coupon_info` (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `coupon_no` varchar(32) NOT NULL COMMENT '礼品卷编号',
  `status` int(2) DEFAULT '0' COMMENT '0初始化状态可用， 9已用  1：使用中',
  `get_time` datetime DEFAULT NULL COMMENT '领取时间',
  `end_time` datetime DEFAULT NULL COMMENT '到期时间',
  `price` decimal(10,0) DEFAULT NULL COMMENT '使用金额',
  `limit_price` decimal(10,0) DEFAULT NULL COMMENT '到达条件金额',
  `account_id` int(11) DEFAULT NULL COMMENT '账号ID，外键 冗余字段',
  `channal` varchar(2) DEFAULT NULL COMMENT '渠道44京东，',
  `is_lock` tinyint(2) DEFAULT '0',
  PRIMARY KEY (`auto_id`),
  UNIQUE KEY `coupon_no` (`coupon_no`),
  KEY `account_id` (`account_id`),
  KEY `channal` (`channal`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_account_refund_cancel_log` */

DROP TABLE IF EXISTS `cp_account_refund_cancel_log`;

CREATE TABLE `cp_account_refund_cancel_log` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `acc_name` varchar(45) DEFAULT NULL COMMENT '账号名',
  `order_id` varchar(45) DEFAULT NULL COMMENT '操作订单号',
  `cz_type` varchar(2) DEFAULT NULL COMMENT '操作类型11取消 22退票 33改签 44预订(暂不统计) ',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `opter` varchar(20) DEFAULT NULL COMMENT '操作人/系统',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_account_register` */

DROP TABLE IF EXISTS `cp_account_register`;

CREATE TABLE `cp_account_register` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `acc_id` varchar(50) DEFAULT NULL COMMENT '账号ID',
  `acc_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `acc_password` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `apply_acc_num` varchar(4) DEFAULT NULL COMMENT '申请新注册账号个数',
  `linkers` varchar(4) DEFAULT NULL COMMENT '联系人个数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `isused` varchar(2) DEFAULT NULL COMMENT '信息是否已使用：1已使用 2未使用',
  `channel` varchar(10) NOT NULL COMMENT '渠道：1、19e 2、qunar 3、cmpay 4、19pay ',
  PRIMARY KEY (`id`,`channel`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_accountinfo` */

DROP TABLE IF EXISTS `cp_accountinfo`;

CREATE TABLE `cp_accountinfo` (
  `acc_id` int(11) NOT NULL AUTO_INCREMENT,
  `acc_name` varchar(45) DEFAULT NULL COMMENT '账号名称',
  `acc_username` varchar(45) DEFAULT NULL COMMENT '登陆名',
  `acc_password` varchar(45) DEFAULT NULL COMMENT '登陆密码',
  `acc_status` varchar(45) DEFAULT NULL COMMENT '账号状态:00:正在下单; 01:队列中; 22:账号停用; 33:账号空闲; 44:临时停用; 55:核验用户信息流程; 66:占座中; 77:核验手机流程; 78:正在核验手机; 79:核验手机失败; 90:更新白名单流程',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `at_province_id` varchar(45) DEFAULT NULL COMMENT '所在省份',
  `at_city_id` varchar(45) DEFAULT NULL COMMENT '所在地区',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `stop_time` datetime DEFAULT NULL COMMENT '停用时间',
  `priority` int(2) NOT NULL DEFAULT '10' COMMENT '未下单天数',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道ID',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `acc_mail` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `stop_reason` varchar(2) DEFAULT NULL COMMENT '停用原因:1:账号被封; 2:取消订单过多; 3:联系人达上限; 4:账号冒用; 5:临时封停; 6:账号不存在; 7:账号密码错误; 8:手机未核验; 9:手机双向核验; 10:用户信息不完整; 11:身份信息待核验',
  `real_name` varchar(50) DEFAULT 'null' COMMENT '真实姓名',
  `id_card` varchar(21) DEFAULT NULL COMMENT '身份证号码',
  `contact_num` int(5) DEFAULT '0' COMMENT '常用联系人个数',
  `account_source` varchar(2) DEFAULT NULL COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `is_alive` int(2) DEFAULT NULL COMMENT '是否处于登录激活状态',
  `live_time` datetime DEFAULT NULL COMMENT '激活维持时间',
  `verify_time` datetime DEFAULT NULL COMMENT '核验时间',
  `delete_remark` varchar(300) DEFAULT NULL COMMENT '机器人删除常用联系人备注',
  `active_status` varchar(3) DEFAULT NULL COMMENT '01、正在唤醒 02、已唤醒 03、未唤醒',
  `active_time` datetime DEFAULT NULL COMMENT '唤醒时间',
  `delete_status` varchar(2) DEFAULT '00' COMMENT '00：等待删除；11：重新删除；22：删除中；33：删除成功；44：删除失败 55:该帐号失效',
  `modify_status` varchar(2) DEFAULT '00' COMMENT '00:等待更改  01：准备更改   02：正在更改   03：更改成功   04：更改失败',
  `old_pass` varchar(20) DEFAULT NULL COMMENT '老密码',
  `modify_time` datetime DEFAULT NULL COMMENT '更改密码操作时间',
  `book_num` int(2) DEFAULT '0' COMMENT '当天订票数',
  `actual_robot_host` varchar(10) DEFAULT NULL COMMENT '当前登录帐号的机器人host',
  `real_check_status` tinyint(4) DEFAULT '99' COMMENT '账号状态 1-核验通过 2-待核验 6-未通过 99-未确定',
  `real_receive` varchar(2) NOT NULL DEFAULT 'U' COMMENT '真实联系人个数',
  `real_contact_num` int(5) NOT NULL DEFAULT '99' COMMENT '真实联系人个数',
  `checkStatusTime` datetime DEFAULT NULL COMMENT '核验账号状态时间',
  `checkstatus` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0:等待更改  1：准备更改   2：正在更改   3：更改成功   4：更改失败',
  `temp_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`acc_id`),
  KEY `IND_cp_accountinfo_acc_username` (`acc_username`),
  KEY `IND_cp_accountinfo_active_time` (`active_time`),
  KEY `IND_cp_accountinfo_channel_acc_status` (`channel`,`acc_status`),
  KEY `IND_cp_accountinfo_acc_status` (`acc_status`),
  KEY `IND_cp_accountinfo_acc_status_stop_reason` (`acc_status`,`stop_reason`)
) ENGINE=InnoDB AUTO_INCREMENT=100604456 DEFAULT CHARSET=utf8 COMMENT='账号信息表';

/*Table structure for table `cp_accountinfo20150806` */

DROP TABLE IF EXISTS `cp_accountinfo20150806`;

CREATE TABLE `cp_accountinfo20150806` (
  `acc_id` int(11) NOT NULL AUTO_INCREMENT,
  `acc_name` varchar(45) DEFAULT NULL COMMENT '账号名称',
  `acc_username` varchar(45) DEFAULT NULL COMMENT '登陆名',
  `acc_password` varchar(45) DEFAULT NULL COMMENT '登陆密码',
  `acc_status` varchar(45) DEFAULT NULL COMMENT '账号状态：00、正在下单；22、账号停用；33、账号空闲 44、临时停用 55、人工启用 66、占座中',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `at_province_id` varchar(45) DEFAULT NULL COMMENT '所在省份',
  `at_city_id` varchar(45) DEFAULT NULL COMMENT '所在地区',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `stop_time` datetime DEFAULT NULL COMMENT '停用时间',
  `priority` int(11) DEFAULT '0' COMMENT '优先级',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道ID',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `acc_mail` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `stop_reason` varchar(2) DEFAULT NULL COMMENT '停用原因：1账号被封 2取消订单过多 3联系人达上限 4未实名制5 当日订购达上限 6用户取回',
  `can_switch` varchar(2) DEFAULT '1' COMMENT '是否可以切换账号 0：不能切换 1：可以切换',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `contact_num` int(5) DEFAULT NULL COMMENT '常用联系人个数',
  `account_source` varchar(2) DEFAULT NULL COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `is_alive` int(2) DEFAULT NULL COMMENT '是否处于唤醒状态(1:处于唤醒状态)',
  `live_time` datetime DEFAULT NULL COMMENT '激活发送时间',
  `verify_num` int(5) DEFAULT '0',
  `delete_remark` varchar(100) DEFAULT NULL COMMENT '机器人删除常用联系人备注',
  `verify_time` datetime DEFAULT NULL COMMENT '核验时间',
  `active_status` varchar(3) DEFAULT NULL COMMENT '01、正在唤醒 02、已唤醒  03、未唤醒  10、等待修改手机号  11、准备修改手机号  12、正在修改手机号  13、修改完成',
  `active_time` datetime DEFAULT NULL COMMENT '激活时间',
  `delete_status` varchar(2) DEFAULT '00' COMMENT '00：等待删除；11：重新删除；22：删除中；33：删除成功；44：删除失败 55:该帐号失效',
  `modify_status` varchar(3) DEFAULT '00' COMMENT '00:等待更改  01：准备更改   02：正在更改   03：更改成功   04：更改失败',
  `old_pass` varchar(30) DEFAULT NULL COMMENT '老密码',
  `modify_time` datetime DEFAULT NULL COMMENT '更改密码操作时间',
  `book_num` int(2) DEFAULT NULL COMMENT '当天订票数',
  PRIMARY KEY (`acc_id`,`real_name`),
  KEY `option_time_inidex` (`option_time`)
) ENGINE=InnoDB AUTO_INCREMENT=288229 DEFAULT CHARSET=utf8 COMMENT='账号信息表';

/*Table structure for table `cp_accountinfo_20170308` */

DROP TABLE IF EXISTS `cp_accountinfo_20170308`;

CREATE TABLE `cp_accountinfo_20170308` (
  `acc_id` int(11) DEFAULT NULL,
  `acc_name` varchar(135) DEFAULT NULL,
  `acc_username` varchar(135) DEFAULT NULL,
  `acc_password` varchar(135) DEFAULT NULL,
  `acc_status` varchar(135) DEFAULT NULL COMMENT '账号状态：00、正在下单；01、队列中 22、账号停用；33、账号空闲 44、临时停用 55、核验用户信息 66、占座中 77、核验手机 78、正在核验手机 79、核验手机失败',
  `create_time` datetime DEFAULT NULL,
  `option_time` datetime DEFAULT NULL,
  `at_province_id` varchar(135) DEFAULT NULL COMMENT '所在省份',
  `at_city_id` varchar(135) DEFAULT NULL COMMENT '所在地区',
  `order_id` varchar(135) DEFAULT NULL COMMENT '订单ID',
  `stop_time` datetime DEFAULT NULL COMMENT '停用时间',
  `priority` int(3) DEFAULT NULL COMMENT '未下单天数',
  `channel` varchar(135) DEFAULT NULL COMMENT '渠道ID',
  `opt_person` varchar(150) DEFAULT NULL COMMENT '操作人',
  `acc_mail` varchar(300) DEFAULT NULL,
  `stop_reason` varchar(6) DEFAULT NULL COMMENT '  停用原因：1账号被封 2取消订单过多 3联系人达上限 4未实名制 5临时封停 7、核验',
  `real_name` varchar(150) DEFAULT NULL,
  `id_card` varchar(63) DEFAULT NULL COMMENT '身份证号码',
  `contact_num` int(5) DEFAULT '0' COMMENT '常用联系人个数',
  `account_source` varchar(6) DEFAULT NULL COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `is_alive` int(2) DEFAULT NULL COMMENT '是否处于登录激活状态',
  `live_time` datetime DEFAULT NULL COMMENT '激活维持时间',
  `verify_time` datetime DEFAULT NULL COMMENT '核验时间',
  `delete_remark` varchar(900) DEFAULT NULL COMMENT '机器人删除常用联系人备注',
  `active_status` varchar(9) DEFAULT NULL COMMENT '01、正在唤醒 02、已唤醒 03、未唤醒',
  `active_time` datetime DEFAULT NULL COMMENT '唤醒时间',
  `delete_status` varchar(6) DEFAULT NULL COMMENT '00：等待删除；11：重新删除；22：删除中；33：删除成功；44：删除失败 55:该帐号失效',
  `modify_status` varchar(6) DEFAULT NULL COMMENT '00:等待更改  01：准备更改   02：正在更改   03：更改成功   04：更改失败',
  `old_pass` varchar(60) DEFAULT NULL COMMENT '老密码',
  `modify_time` datetime DEFAULT NULL COMMENT '更改密码操作时间',
  `book_num` int(2) DEFAULT '0' COMMENT '当天订票数',
  `actual_robot_host` varchar(30) DEFAULT NULL COMMENT '当前登录帐号的机器人host',
  `real_check_status` tinyint(4) DEFAULT NULL COMMENT '账号状态 1-核验通过 2-待核验 6-未通过 99-未确定',
  `real_receive` varchar(6) DEFAULT NULL,
  `real_contact_num` int(5) DEFAULT NULL COMMENT '真实联系人个数',
  `checkStatusTime` datetime DEFAULT NULL,
  `checkstatus` tinyint(4) DEFAULT NULL COMMENT '0:等待更改  1：准备更改   2：正在更改   3：更改成功   4：更改失败'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cp_accountinfo_bak` */

DROP TABLE IF EXISTS `cp_accountinfo_bak`;

CREATE TABLE `cp_accountinfo_bak` (
  `acc_id` int(11) NOT NULL AUTO_INCREMENT,
  `acc_name` varchar(45) DEFAULT NULL COMMENT '账号名称',
  `acc_username` varchar(45) DEFAULT NULL COMMENT '登陆名',
  `acc_password` varchar(45) DEFAULT NULL COMMENT '登陆密码',
  `acc_status` varchar(45) DEFAULT NULL COMMENT '账号状态：00、正在下单；22、账号停用；33、账号空闲 44、临时停用 55、人工启用 66、占座中 77、手机核验中',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `at_province_id` varchar(45) DEFAULT NULL COMMENT '所在省份',
  `at_city_id` varchar(45) DEFAULT NULL COMMENT '所在地区',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `stop_time` datetime DEFAULT NULL COMMENT '停用时间',
  `priority` int(11) DEFAULT '0' COMMENT '优先级',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道ID',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `acc_mail` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `stop_reason` varchar(2) DEFAULT NULL COMMENT '停用原因：1账号被封 2取消订单过多 3联系人达上限 4未实名制5 当日订购达上限 6用户取回',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `contact_num` int(5) DEFAULT NULL COMMENT '常用联系人个数',
  `account_source` varchar(2) DEFAULT NULL COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `is_alive` int(2) DEFAULT NULL COMMENT '是否处于唤醒状态(1:处于唤醒状态)',
  `live_time` datetime DEFAULT NULL COMMENT '激活发送时间',
  `verify_time` datetime DEFAULT '0000-00-00 00:00:00',
  `delete_remark` varchar(100) DEFAULT NULL COMMENT '机器人删除常用联系人备注',
  `active_status` varchar(3) DEFAULT NULL COMMENT '01、正在唤醒 02、已唤醒  03、未唤醒  10、等待修改手机号  11、准备修改手机号  12、正在修改手机号  13、修改完成',
  `active_time` datetime DEFAULT NULL COMMENT '激活时间',
  `delete_status` varchar(2) DEFAULT '00' COMMENT '00：等待删除；11：重新删除；22：删除中；33：删除成功；44：删除失败 55:该帐号失效',
  `modify_status` varchar(3) DEFAULT '00' COMMENT '00:等待更改  01：准备更改   02：正在更改   03：更改成功   04：更改失败',
  `old_pass` varchar(30) DEFAULT NULL COMMENT '老密码',
  `modify_time` datetime DEFAULT NULL COMMENT '更改密码操作时间',
  `book_num` int(2) DEFAULT NULL COMMENT '当天订票数',
  `actual_robot_host` varchar(10) DEFAULT NULL COMMENT '当前登录帐号的机器人host',
  `real_check_status` tinyint(4) DEFAULT '99' COMMENT '账号状态 1-核验通过 2-待核验 6-未通过 99-未确定',
  `real_receive` varchar(2) NOT NULL DEFAULT 'U' COMMENT '手机是否核验通过Y、通过X、未核验U、未知',
  `real_contact_num` int(5) DEFAULT NULL COMMENT '真实联系人个数',
  `checkStatusTime` datetime DEFAULT NULL COMMENT '核验账号状态时间',
  `checkstatus` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:等待更改  1：准备更改   2：正在更改   3：更改成功   4：更改失败',
  PRIMARY KEY (`acc_id`,`real_name`),
  KEY `option_time_inidex` (`option_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1540398 DEFAULT CHARSET=utf8 COMMENT='账号信息表';

/*Table structure for table `cp_accountinfo_check` */

DROP TABLE IF EXISTS `cp_accountinfo_check`;

CREATE TABLE `cp_accountinfo_check` (
  `check_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_id` int(11) NOT NULL COMMENT '账号主键',
  `account_username` varchar(45) NOT NULL COMMENT '12306账号名',
  `account_password` varchar(45) NOT NULL COMMENT '12306账号密码',
  `phone` varchar(15) DEFAULT NULL COMMENT '核验手机',
  `phone_code` varchar(10) DEFAULT NULL COMMENT '手机验证码',
  `channel` varchar(10) DEFAULT NULL COMMENT '核验渠道',
  `error_code` varchar(10) DEFAULT NULL COMMENT '错误码：0、核验完成 1、手机号已被占用 2、验证码错误 3、发起机器核验失败 4、12306处理错误',
  `check_status` varchar(10) DEFAULT '0' COMMENT '核验状态 : 0、待核验 1、开始核验 2、核验成功 3、核验失败 4、发起机器核验 5、核验待确认',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `check_time` datetime DEFAULT NULL COMMENT '核验日期',
  `user_id` varchar(50) DEFAULT NULL COMMENT '核验用户唯一标识',
  PRIMARY KEY (`check_id`),
  KEY `cp_accountinfo_check_account_id` (`account_id`),
  KEY `cp_accountinfo_check_account_username` (`account_username`),
  KEY `cp_accountinfo_check_channel` (`channel`),
  KEY `cp_accountinfo_check_phone` (`phone`),
  KEY `cp_accountinfo_check_check_status` (`check_status`),
  KEY `cp_accountinfo_check_create_time` (`create_time`),
  KEY `cp_accountinfo_check_check_time` (`check_time`),
  KEY `cp_accountinfo_check_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='账号核验信息表';

/*Table structure for table `cp_accountinfo_check_refund` */

DROP TABLE IF EXISTS `cp_accountinfo_check_refund`;

CREATE TABLE `cp_accountinfo_check_refund` (
  `check_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_id` int(11) NOT NULL COMMENT '账号主键',
  `account_username` varchar(45) NOT NULL COMMENT '12306账号名',
  `account_password` varchar(45) NOT NULL COMMENT '12306账号密码',
  `check_status` varchar(10) DEFAULT '0' COMMENT '核验状态 : 0、待核验 1、开始核验 2、核验成功 3、核验失败 4、发起机器核验 5、核验待确认',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `user_id` varchar(50) DEFAULT NULL COMMENT '核验用户唯一标识',
  PRIMARY KEY (`check_id`),
  KEY `cp_accountinfo_check_account_id` (`account_id`),
  KEY `cp_accountinfo_check_account_username` (`account_username`),
  KEY `cp_accountinfo_check_check_status` (`check_status`),
  KEY `cp_accountinfo_check_create_time` (`create_time`),
  KEY `cp_accountinfo_check_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='账号核验信息表（退款添加）';

/*Table structure for table `cp_accountinfo_copy` */

DROP TABLE IF EXISTS `cp_accountinfo_copy`;

CREATE TABLE `cp_accountinfo_copy` (
  `acc_id` int(11) NOT NULL AUTO_INCREMENT,
  `acc_name` varchar(45) DEFAULT NULL COMMENT '账号名称',
  `acc_username` varchar(45) DEFAULT NULL COMMENT '登陆名',
  `acc_password` varchar(45) DEFAULT NULL COMMENT '登陆密码',
  `acc_status` varchar(45) DEFAULT NULL COMMENT '账号状态：00、正在下单；22、账号停用；33、账号空闲 44、临时停用 55、人工启用 66、占座中 77、手机核验中',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `at_province_id` varchar(45) DEFAULT NULL COMMENT '所在省份',
  `at_city_id` varchar(45) DEFAULT NULL COMMENT '所在地区',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `stop_time` datetime DEFAULT NULL COMMENT '停用时间',
  `priority` int(11) DEFAULT '0' COMMENT '优先级',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道ID',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `acc_mail` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `stop_reason` varchar(2) DEFAULT NULL COMMENT '停用原因：1账号被封 2取消订单过多 3联系人达上限 4未实名制5 当日订购达上限 6用户取回',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `contact_num` int(5) DEFAULT NULL COMMENT '常用联系人个数',
  `account_source` varchar(2) DEFAULT NULL COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `is_alive` int(2) DEFAULT NULL COMMENT '是否处于唤醒状态(1:处于唤醒状态)',
  `live_time` datetime DEFAULT NULL COMMENT '激活发送时间',
  `verify_time` datetime DEFAULT '0000-00-00 00:00:00',
  `delete_remark` varchar(100) DEFAULT NULL COMMENT '机器人删除常用联系人备注',
  `active_status` varchar(3) DEFAULT NULL COMMENT '01、正在唤醒 02、已唤醒  03、未唤醒  10、等待修改手机号  11、准备修改手机号  12、正在修改手机号  13、修改完成',
  `active_time` datetime DEFAULT NULL COMMENT '激活时间',
  `delete_status` varchar(2) DEFAULT '00' COMMENT '00：等待删除；11：重新删除；22：删除中；33：删除成功；44：删除失败 55:该帐号失效',
  `modify_status` varchar(3) DEFAULT '00' COMMENT '00:等待更改  01：准备更改   02：正在更改   03：更改成功   04：更改失败',
  `old_pass` varchar(30) DEFAULT NULL COMMENT '老密码',
  `modify_time` datetime DEFAULT NULL COMMENT '更改密码操作时间',
  `book_num` int(2) DEFAULT NULL COMMENT '当天订票数',
  `actual_robot_host` varchar(10) DEFAULT NULL COMMENT '当前登录帐号的机器人host',
  `real_check_status` tinyint(4) DEFAULT '9' COMMENT '账号状态 0-已锁定  1-待手机核验  2-正常使用 9-未确定',
  `real_contact_num` int(5) DEFAULT NULL COMMENT '真实联系人个数',
  `checkStatusTime` datetime DEFAULT NULL COMMENT '核验账号状态时间',
  PRIMARY KEY (`acc_id`,`real_name`),
  KEY `option_time_inidex` (`option_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1265811 DEFAULT CHARSET=utf8 COMMENT='账号信息表';

/*Table structure for table `cp_accountinfo_filter` */

DROP TABLE IF EXISTS `cp_accountinfo_filter`;

CREATE TABLE `cp_accountinfo_filter` (
  `ids_card` varchar(45) NOT NULL COMMENT '证件号',
  `account_id` int(11) DEFAULT NULL COMMENT '关联账号id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `real_name` varchar(45) DEFAULT NULL COMMENT '真实姓名',
  PRIMARY KEY (`ids_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账号选择过滤表';

/*Table structure for table `cp_ailipaytime_info` */

DROP TABLE IF EXISTS `cp_ailipaytime_info`;

CREATE TABLE `cp_ailipaytime_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `month` varchar(10) DEFAULT NULL COMMENT '月份',
  `delta` int(10) DEFAULT NULL COMMENT '差量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_alipaybill_info` */

DROP TABLE IF EXISTS `cp_alipaybill_info`;

CREATE TABLE `cp_alipaybill_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alipay_account` varchar(50) DEFAULT NULL COMMENT '支付宝账号',
  `download_time` varchar(30) DEFAULT NULL COMMENT '下载时间',
  `is_download` char(2) DEFAULT '0' COMMENT '是否下载(0:没有下载,1:已下载)',
  `create_time` datetime DEFAULT NULL COMMENT '生成时间',
  `download_path` varchar(200) DEFAULT NULL COMMENT '下载存放路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_alipaycode_info` */

DROP TABLE IF EXISTS `cp_alipaycode_info`;

CREATE TABLE `cp_alipaycode_info` (
  `code_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `worker_id` int(11) DEFAULT NULL COMMENT '工人ID',
  `card_id` int(11) DEFAULT NULL COMMENT '支付宝账号表主键',
  `card_no` varchar(45) DEFAULT NULL COMMENT '支付宝账号',
  `verification_code` varchar(10) DEFAULT NULL COMMENT '验证码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`code_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='验证码';

/*Table structure for table `cp_balance_account` */

DROP TABLE IF EXISTS `cp_balance_account`;

CREATE TABLE `cp_balance_account` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '表序号',
  `stream_id` tinyint(10) DEFAULT NULL COMMENT '序号',
  `cw_seq` varchar(30) DEFAULT NULL COMMENT '财务流水号',
  `yw_seq` varchar(30) DEFAULT NULL COMMENT '业务流水号',
  `sh_order_id` varchar(30) DEFAULT NULL COMMENT '商户订单号',
  `sp_name` varchar(30) DEFAULT NULL COMMENT '商品名称',
  `sp_create_time` datetime DEFAULT NULL COMMENT '发生时间',
  `user_account` varchar(40) DEFAULT NULL COMMENT '对方账号',
  `in_money` decimal(11,3) DEFAULT NULL COMMENT '收入金额',
  `out_money` decimal(11,3) DEFAULT NULL COMMENT '支出金额',
  `surplus_money` decimal(11,3) DEFAULT NULL COMMENT '账户余额',
  `channel` varchar(10) DEFAULT NULL COMMENT '交易渠道',
  `business_status` varchar(10) DEFAULT NULL COMMENT '业务类型',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `our_account` varchar(30) DEFAULT NULL COMMENT '账户',
  `order_id` varchar(30) DEFAULT NULL COMMENT '系统订单号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `refund_result` varchar(2) DEFAULT '1' COMMENT '退款结果 1、未处理 2、匹配 3 未匹配',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88206 DEFAULT CHARSET=utf8 COMMENT='支付宝对账表';

/*Table structure for table `cp_card_bank` */

DROP TABLE IF EXISTS `cp_card_bank`;

CREATE TABLE `cp_card_bank` (
  `card_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '卡编号',
  `card_no` varchar(45) DEFAULT NULL COMMENT '卡号',
  `card_pwd` varchar(45) DEFAULT NULL COMMENT '密码',
  `pay_type` varchar(5) DEFAULT NULL COMMENT '支付类型：00、借记卡网银支付 11、借记卡快捷支付  22、中铁银通卡 33、信用卡快捷支付 44、信用卡网银支付 55、支付宝',
  `card_phone` varchar(45) DEFAULT NULL COMMENT '绑定手机号',
  `card_status` varchar(5) DEFAULT NULL COMMENT '卡状态 00：正在付款 11：等待付款 22：暂停付款',
  `card_remain` decimal(11,2) DEFAULT NULL COMMENT '余额',
  `bank_type` varchar(45) DEFAULT NULL COMMENT '银行类型 00、中国银行 11、建设银行 22、中铁银通卡 33、支付宝 44、招商银行',
  `card_ext` varchar(45) DEFAULT NULL COMMENT '卡扩展',
  `worker_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '余额更新时间',
  `com_no` varchar(8) DEFAULT NULL COMMENT '短信猫COM口号',
  `ids_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (`card_id`),
  KEY `index_com_no` (`com_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='银行卡信息';

/*Table structure for table `cp_cardinfo` */

DROP TABLE IF EXISTS `cp_cardinfo`;

CREATE TABLE `cp_cardinfo` (
  `card_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '卡编号',
  `card_no` varchar(45) DEFAULT NULL COMMENT '卡号',
  `card_pwd` varchar(45) DEFAULT NULL COMMENT '密码',
  `pay_type` varchar(5) DEFAULT NULL COMMENT '支付类型：00、借记卡网银支付 11、借记卡快捷支付  22、中铁银通卡 33、信用卡快捷支付 44、信用卡网银支付 55、支付宝',
  `card_phone` varchar(45) DEFAULT NULL COMMENT '绑定手机号',
  `card_status` varchar(5) DEFAULT NULL COMMENT '卡状态 00：正在付款 11：等待付款 22：暂停付款',
  `card_remain` decimal(11,2) DEFAULT NULL COMMENT '余额',
  `bank_type` varchar(45) DEFAULT NULL COMMENT '银行类型 00、中国银行 11、建设银行 22、中铁银通卡 33、支付宝',
  `card_ext` varchar(45) DEFAULT NULL COMMENT '卡扩展',
  `worker_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '余额更新时间',
  `com_no` varchar(8) DEFAULT NULL COMMENT '短信猫COM口号',
  PRIMARY KEY (`card_id`),
  KEY `index_com_no` (`com_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8 COMMENT='银行卡信息';

/*Table structure for table `cp_cardinfo20150806` */

DROP TABLE IF EXISTS `cp_cardinfo20150806`;

CREATE TABLE `cp_cardinfo20150806` (
  `card_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '卡编号',
  `card_no` varchar(45) DEFAULT NULL COMMENT '卡号',
  `card_pwd` varchar(45) DEFAULT NULL COMMENT '密码',
  `pay_type` varchar(5) DEFAULT NULL COMMENT '支付类型：00、借记卡网银支付 11、借记卡快捷支付  22、中铁银通卡 33、信用卡快捷支付 44、信用卡网银支付 55、支付宝',
  `card_phone` varchar(45) DEFAULT NULL COMMENT '绑定手机号',
  `card_status` varchar(5) DEFAULT NULL COMMENT '卡状态 00：正在付款 11：等待付款 22：暂停付款',
  `card_remain` decimal(11,2) DEFAULT NULL COMMENT '余额',
  `bank_type` varchar(45) DEFAULT NULL COMMENT '银行类型 00、中国银行 11、建设银行 22、中铁银通卡',
  `card_ext` varchar(45) DEFAULT NULL COMMENT '卡扩展',
  `worker_id` int(11) DEFAULT NULL COMMENT '机器人ID',
  PRIMARY KEY (`card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='银行卡信息';

/*Table structure for table `cp_cardinfo_test` */

DROP TABLE IF EXISTS `cp_cardinfo_test`;

CREATE TABLE `cp_cardinfo_test` (
  `card_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '卡编号',
  `card_no` varchar(45) DEFAULT NULL COMMENT '卡号',
  `card_pwd` varchar(45) DEFAULT NULL COMMENT '密码',
  `pay_type` varchar(5) DEFAULT NULL COMMENT '支付类型：00、借记卡网银支付 11、借记卡快捷支付  22、中铁银通卡 33、信用卡快捷支付 44、信用卡网银支付 55、支付宝',
  `card_phone` varchar(45) DEFAULT NULL COMMENT '绑定手机号',
  `card_status` varchar(5) DEFAULT NULL COMMENT '卡状态 00：正在付款 11：等待付款 22：暂停付款',
  `card_remain` decimal(11,2) DEFAULT NULL COMMENT '余额',
  `bank_type` varchar(45) DEFAULT NULL COMMENT '银行类型 00、中国银行 11、建设银行 22、中铁银通卡',
  `card_ext` varchar(45) DEFAULT NULL COMMENT '卡扩展',
  `worker_id` int(11) DEFAULT NULL COMMENT '机器人ID',
  PRIMARY KEY (`card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='银行卡信息';

/*Table structure for table `cp_cardinfo_wudong` */

DROP TABLE IF EXISTS `cp_cardinfo_wudong`;

CREATE TABLE `cp_cardinfo_wudong` (
  `card_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '卡编号',
  `card_no` varchar(45) DEFAULT NULL COMMENT '卡号',
  `card_pwd` varchar(45) DEFAULT NULL COMMENT '密码',
  `pay_type` varchar(5) DEFAULT NULL COMMENT '支付类型：00、借记卡网银支付 11、借记卡快捷支付  22、中铁银通卡 33、信用卡快捷支付 44、信用卡网银支付 55、支付宝',
  `card_phone` varchar(45) DEFAULT NULL COMMENT '绑定手机号',
  `card_status` varchar(5) DEFAULT NULL COMMENT '卡状态 00：正在付款 11：等待付款 22：暂停付款',
  `card_remain` decimal(11,2) DEFAULT NULL COMMENT '余额',
  `bank_type` varchar(45) DEFAULT NULL COMMENT '银行类型 00、中国银行 11、建设银行 22、中铁银通卡 33、支付宝',
  `card_ext` varchar(45) DEFAULT NULL COMMENT '卡扩展',
  `worker_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '余额更新时间',
  `com_no` varchar(8) DEFAULT NULL COMMENT '短信猫COM口号',
  PRIMARY KEY (`card_id`),
  UNIQUE KEY `index_com_no` (`com_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8 COMMENT='银行卡信息';

/*Table structure for table `cp_cardpayinfo` */

DROP TABLE IF EXISTS `cp_cardpayinfo`;

CREATE TABLE `cp_cardpayinfo` (
  `pay_id` varchar(45) NOT NULL,
  `pay_money` varchar(45) DEFAULT NULL COMMENT '支付金额',
  `pay_remain` varchar(45) DEFAULT NULL COMMENT '余额',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `card_no` varchar(45) DEFAULT NULL COMMENT '卡序列号',
  `bank_type` varchar(45) DEFAULT NULL COMMENT '银行类型 00、中国银行 11、建设银行 22、中铁银通卡\n',
  `pay_no` varchar(45) DEFAULT NULL COMMENT '支付流水号',
  PRIMARY KEY (`pay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cp_complain` */

DROP TABLE IF EXISTS `cp_complain`;

CREATE TABLE `cp_complain` (
  `complain_id` varchar(50) NOT NULL COMMENT '投诉建议id',
  `province_id` varchar(10) DEFAULT NULL COMMENT '所属省',
  `city_id` varchar(10) DEFAULT NULL COMMENT '所属城市',
  `district_id` varchar(10) DEFAULT NULL COMMENT '所属区、县id',
  `agent_id` varchar(50) DEFAULT NULL COMMENT '代理商id',
  `question_type` varchar(2) DEFAULT NULL COMMENT '问题类别  0：订单问题 1：加盟问题 2：配送问题 3：出票问题 4：业务建议 5：其他',
  `question` varchar(1000) DEFAULT NULL COMMENT '问题或建议',
  `answer` varchar(1000) DEFAULT NULL COMMENT '答复',
  `permission` varchar(2) DEFAULT NULL COMMENT '权限 0：全部可见 1：自己可见',
  `create_time` datetime DEFAULT NULL COMMENT '提问时间',
  `reply_time` datetime DEFAULT NULL COMMENT '答复时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  `eop_user` varchar(50) DEFAULT NULL COMMENT 'EOP用户昵称',
  `channel` varchar(15) DEFAULT NULL COMMENT '渠道：19e、cmpay、19pay、ccb、ext、b2c',
  `user_name` varchar(10) DEFAULT NULL COMMENT '代理商姓名',
  `user_phone` varchar(15) DEFAULT NULL COMMENT '代理商电话',
  `reply_season` varchar(10) DEFAULT '00' COMMENT '处理结果:00未处理，11已处理（默认00）',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户ID（建行、春秋）',
  PRIMARY KEY (`complain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投诉与建议';

/*Table structure for table `cp_complain_history` */

DROP TABLE IF EXISTS `cp_complain_history`;

CREATE TABLE `cp_complain_history` (
  `history_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `complain_id` varchar(50) DEFAULT NULL COMMENT '投诉建议id',
  `reply_time` datetime DEFAULT NULL COMMENT '答复时间',
  `reply_person` varchar(10) DEFAULT NULL COMMENT '答复人',
  `our_reply` varchar(1000) DEFAULT NULL COMMENT '答复内容',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_count` */

DROP TABLE IF EXISTS `cp_count`;

CREATE TABLE `cp_count` (
  `count_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '计数请求id',
  `source` varchar(20) DEFAULT NULL COMMENT '计数请求来源',
  `channel` varchar(10) DEFAULT NULL COMMENT '计数请求渠道',
  `type` varchar(3) DEFAULT NULL COMMENT '计数种类01、查询  02、核验乘客  03 改签  04 退票  05 短信',
  `ip` varchar(50) DEFAULT NULL COMMENT '请求ip',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `message` varchar(100) DEFAULT NULL COMMENT '计数信息',
  `code` varchar(3) DEFAULT NULL COMMENT '记录的状态代码00、一次请求 01、成功；11、核验接口关闭，默认成功  02、失败  03、发送数据不符合要求   04、系统错误导致失败  50、一次发送短信请求',
  UNIQUE KEY `count_id` (`count_id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_exception_config` */

DROP TABLE IF EXISTS `cp_exception_config`;

CREATE TABLE `cp_exception_config` (
  `config_id` varchar(10) NOT NULL COMMENT '异常ID',
  `config_name` varchar(30) NOT NULL DEFAULT '' COMMENT '异常关键字',
  `config_desc` varchar(100) NOT NULL DEFAULT '' COMMENT '异常关键字具体描述',
  `config_type` varchar(2) NOT NULL DEFAULT '11' COMMENT '异常类型：11预定 22支付',
  `config_status` varchar(2) NOT NULL DEFAULT '1' COMMENT '是否启用0：否。1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `config_person` varchar(30) DEFAULT '' COMMENT '操作人',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='异常信息配置表';

/*Table structure for table `cp_ipinfo` */

DROP TABLE IF EXISTS `cp_ipinfo`;

CREATE TABLE `cp_ipinfo` (
  `ip_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `ip_type` int(3) DEFAULT NULL COMMENT 'ip类型   1-携程待出票ip  2-预定代理ip  ',
  `ip_status` varchar(4) DEFAULT NULL COMMENT '状态：00、空闲  11、使用中  22、停用  33 备用',
  `ip_ext` varchar(30) DEFAULT NULL COMMENT 'ip地址',
  `request_num` int(3) DEFAULT '0' COMMENT '本IP下单成功的次数',
  `if_support_change` varchar(4) DEFAULT NULL COMMENT '机器是否支持切换新旧IP功能： 0: 支持   1: 不支持  只有美团云机器支持这一功能。',
  PRIMARY KEY (`ip_id`),
  KEY `idx_ipinfo_crttime` (`create_time`) USING BTREE,
  KEY `index_ipinfo_opttime` (`option_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_ipinfo_log` */

DROP TABLE IF EXISTS `cp_ipinfo_log`;

CREATE TABLE `cp_ipinfo_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键    操作日志ID',
  `old_ip` varchar(30) DEFAULT NULL COMMENT '机器原先的IP',
  `new_ip` varchar(30) DEFAULT NULL COMMENT '美团云切换IP后，机器上的新IP',
  `content` varchar(200) DEFAULT NULL COMMENT '具体的日志操作内容',
  `create_time` datetime DEFAULT NULL COMMENT '本条日志创建的时间',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_ipinfo_release` */

DROP TABLE IF EXISTS `cp_ipinfo_release`;

CREATE TABLE `cp_ipinfo_release` (
  `ip_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `ip_ext` varchar(30) DEFAULT NULL COMMENT 'ip地址',
  `release_status` varchar(4) DEFAULT NULL COMMENT '本ip的释放状态   0：待释放  1：释放成功  2：释放失败  3：释放中',
  `release_num` int(3) DEFAULT '0' COMMENT '本ip的释放次数',
  PRIMARY KEY (`ip_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_match` */

DROP TABLE IF EXISTS `cp_match`;

CREATE TABLE `cp_match` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `channel` varchar(15) NOT NULL COMMENT '来源渠道',
  `param_num` int(2) DEFAULT '0' COMMENT '传入证件总数',
  `param_cardno` varchar(120) DEFAULT '' COMMENT '传入的证件号码',
  `match_num` int(2) DEFAULT '0' COMMENT '匹配证件总数',
  `match_cardno` varchar(120) DEFAULT '' COMMENT '匹配的证件号码',
  `match_account_id` varchar(20) DEFAULT '' COMMENT '匹配的账号ID',
  `match_status` varchar(2) DEFAULT NULL COMMENT '匹配状态：00完全匹配 11部分匹配(可添加剩余证件) 22部分匹配(不可添加剩余证件) 33不匹配',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `NewIndex1` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo` */

DROP TABLE IF EXISTS `cp_orderinfo`;

CREATE TABLE `cp_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `order_status` varchar(10) DEFAULT NULL COMMENT '出票状态 00、开始出票 01、重发出票   11、正在预定 15、12306排队 22、预定失败 33、预定成功  44、预定人工 45、等待支付 55、开始支付 56. 重新支付61、人工支付 66、正在支付 77、支付失败 88、支付成功  81、支付核对 83、正在取消 77、取消失败 99、出票成功 10、出票失败 82、人工核对 85、开始取消 AA、补单',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` int(11) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(20) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `account_id` int(11) DEFAULT NULL COMMENT '账号ID',
  `worker_id` int(11) DEFAULT NULL COMMENT '处理人账号',
  `out_ticket_account` varchar(45) DEFAULT NULL COMMENT '出票账号',
  `bank_pay_seq` varchar(50) DEFAULT NULL COMMENT '银行支付流水号',
  `error_info` varchar(145) DEFAULT NULL COMMENT '错误信息：1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验 9、系统异常 11、超时未支付 12信息冒用 13排队人数过多，高铁管家要求失败 ;【qunar】错误信息：0、其他 1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符 4、车次数据与12306不一致 5、乘客信息错误 6、12306乘客身份信息核验失败',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道ID',
  `ext_seattype` varchar(200) DEFAULT NULL COMMENT '扩展坐席',
  `level` varchar(10) DEFAULT '0' COMMENT '订单级别:0、普通。1、10元VIP1。  2、20元VIP2。  5、SVIP 。 10 、联程',
  `pay_type` varchar(2) DEFAULT '0' COMMENT '支付方式：默认0机器支付，1是手动支付',
  `is_pay` varchar(2) DEFAULT '00' COMMENT '是否支付：00：已支付；11：未支付',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志：00 不识别码;11 预定超时;22 不识别码;33 实名制;44 提交过多;55 系统忙;66 脚本异常;77 预定超时;88 预定超时;99  排队中;01 无账号;02 无订单号;03 任务超时;04 支付超时;05 加载失败;06 查不到单;07 窜号;08 无票;09 无列车;',
  `pro_bak2` varchar(2) DEFAULT '00' COMMENT '00正常11 补充订单',
  `pay_limit_time` datetime DEFAULT NULL COMMENT '支付截止时间',
  `manual_order` varchar(2) DEFAULT '00' COMMENT '是否为人工出票：00普通出票模式 11 手工出票模式 22 携程出票模式（不买保险）33 携程出票模式（买保险） 44：京东出票模式',
  `wait_for_order` varchar(2) DEFAULT '00' COMMENT '12306系统异常是否继续出票 11：继续出票 00：不继续出票 （只针对同程渠道）',
  `device_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0--PC端预订  1-APP端预订',
  `from_3c` varchar(6) DEFAULT NULL,
  `account_from_way` int(2) DEFAULT '0' COMMENT '账号来源： 0：公司自有账号 ； 1：12306自带账号',
  `to_3c` varchar(6) DEFAULT NULL,
  `ctrip_bx_money` decimal(11,3) DEFAULT '0.000' COMMENT '携程保险金额',
  `is_click_button` varchar(2) DEFAULT '00' COMMENT '后台页面点击的是哪个按钮：   00：批量支付按钮  11：批量反支按钮',
  `seat_detail_type` varchar(6) DEFAULT NULL COMMENT '客户预选的卧铺位置',
  `choose_seats` varchar(10) DEFAULT NULL COMMENT '客户预选的座位号',
  `if_12306_cutover` tinyint(2) DEFAULT NULL COMMENT '是否切入12306出票   0：否  1：是',
  `choose_seat_type` varchar(10) DEFAULT NULL COMMENT '可以选座的坐席类型 如果有多个，中间用逗号分隔',
  `resend_identify` tinyint(2) DEFAULT NULL COMMENT '重发类型：   0：无变化重发   1：切换12306账号重发  2：切换京东账号重发  3：切换京东预付卡重发',
  `jdbook_resendnum` tinyint(2) DEFAULT '0' COMMENT '京东预定重发次数',
  `pingan_status` int(1) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_status_time` (`order_status`(2),`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票出票信息表';

/*Table structure for table `cp_orderinfo_12306` */

DROP TABLE IF EXISTS `cp_orderinfo_12306`;

CREATE TABLE `cp_orderinfo_12306` (
  `order_id` varchar(50) NOT NULL COMMENT '出票系统订单号',
  `tran_data` varchar(2000) DEFAULT NULL COMMENT '12306网站密钥1',
  `mer_sign_msg` varchar(1000) DEFAULT NULL COMMENT '12306网站密钥2',
  `app_id` varchar(45) DEFAULT NULL COMMENT '12306网站appId',
  `trans_type` varchar(45) DEFAULT NULL COMMENT '12306网站transType',
  `she_id` varchar(45) DEFAULT NULL COMMENT '12306网站订单号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付信息';

/*Table structure for table `cp_orderinfo_backup` */

DROP TABLE IF EXISTS `cp_orderinfo_backup`;

CREATE TABLE `cp_orderinfo_backup` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `order_status` varchar(10) DEFAULT NULL COMMENT '出票状态 00A、未审核 00、开始出票 01、重发出票   11、正在预定 22、预定失败 33、预定成功  44、人工预定 55、开始支付 61、人工支付 66、正在支付 77、取消失败 88、支付成功  81、支付核对 83、正在取消 99、出票成功 10、出票失败 82、人工查询 85、开始取消',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` int(11) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(20) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `account_id` int(11) DEFAULT NULL COMMENT '账号ID',
  `worker_id` int(11) DEFAULT NULL COMMENT '处理人账号',
  `out_ticket_account` varchar(45) DEFAULT NULL COMMENT '出票账号',
  `bank_pay_seq` varchar(50) DEFAULT NULL COMMENT '银行支付流水号',
  `error_info` varchar(145) DEFAULT NULL COMMENT '错误信息：1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验  【qunar】错误信息：0、其他 1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符 4、车次数据与12306不一致 5、乘客信息错误 6、12306乘客身份信息核验失败',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道ID',
  `ext_seattype` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `level` varchar(10) DEFAULT '0' COMMENT '订单级别',
  `pay_type` varchar(2) DEFAULT '0' COMMENT '支付方式：默认0机器支付，1是手动支付',
  `is_pay` varchar(2) DEFAULT '00' COMMENT '是否支付：00：已支付；11：未支付',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志',
  PRIMARY KEY (`order_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_status_time` (`order_status`(2),`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票出票信息表备份表';

/*Table structure for table `cp_orderinfo_bx` */

DROP TABLE IF EXISTS `cp_orderinfo_bx`;

CREATE TABLE `cp_orderinfo_bx` (
  `bx_id` varchar(50) NOT NULL COMMENT '保险号主键',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '车票ID',
  `from_name` varchar(45) DEFAULT NULL COMMENT '出发地址',
  `to_name` varchar(45) DEFAULT NULL COMMENT '到达地址',
  `ids_type` varchar(2) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户姓名',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '用户证件号',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL,
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `bx_status` int(2) DEFAULT NULL COMMENT '状态：0、未发送 1、正在发送 2、发送完成 3、正在撤销 4、撤销完成 5、发送失败 6、需要取消 7、取消失败',
  `bx_code` varchar(45) DEFAULT NULL COMMENT '保险单号',
  `bx_billno` varchar(45) DEFAULT NULL COMMENT '服务器端保险号',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '进货金额',
  `product_id` varchar(15) DEFAULT NULL COMMENT '产品ID',
  `effect_date` varchar(45) DEFAULT NULL COMMENT '生效日期',
  `train_no` varchar(45) DEFAULT NULL COMMENT '车次',
  `bx_channel` varchar(2) DEFAULT NULL COMMENT '保险公司渠道: 1、快保   2、合众  3、平安保险',
  `order_channel` varchar(30) DEFAULT NULL COMMENT '订单渠道：19e、19e前台; inner、内嵌；app、app客户端；ext、对外商户   ',
  `merchant_id` varchar(30) DEFAULT NULL COMMENT '商户编号',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '失败原因',
  `card_id` int(2) DEFAULT NULL COMMENT '支付账号',
  `trans_no` varchar(30) DEFAULT NULL COMMENT '支付流水号',
  `applyPolicyNo` varchar(30) DEFAULT NULL,
  `orderNo` varchar(30) DEFAULT NULL,
  `bx_type` varchar(2) DEFAULT NULL COMMENT '0、儿童；1、成人',
  PRIMARY KEY (`bx_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='整体保险单信息';

/*Table structure for table `cp_orderinfo_bx_history` */

DROP TABLE IF EXISTS `cp_orderinfo_bx_history`;

CREATE TABLE `cp_orderinfo_bx_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `order_time` datetime DEFAULT NULL COMMENT '退款创建时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `bx_code` varchar(45) DEFAULT NULL COMMENT '保险单号',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo_bx_test` */

DROP TABLE IF EXISTS `cp_orderinfo_bx_test`;

CREATE TABLE `cp_orderinfo_bx_test` (
  `bx_id` varchar(50) NOT NULL COMMENT '保险号主键',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '车票ID',
  `from_name` varchar(45) DEFAULT NULL COMMENT '出发地址',
  `to_name` varchar(45) DEFAULT NULL COMMENT '到达地址',
  `ids_type` varchar(2) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户姓名',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '用户证件号',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL,
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `bx_status` int(2) DEFAULT NULL COMMENT '状态：0、未发送 1、正在发送 2、发送完成 3、正在撤销 4、撤销完成 5、发送失败 6、需要取消 7、取消失败 8、准备发送',
  `bx_code` varchar(45) DEFAULT NULL COMMENT '保险单号',
  `bx_billno` varchar(45) DEFAULT NULL COMMENT '服务器端保险号',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '进货金额',
  `product_id` varchar(15) DEFAULT NULL COMMENT '产品ID',
  `effect_date` varchar(45) DEFAULT NULL COMMENT '生效日期',
  `train_no` varchar(45) DEFAULT NULL COMMENT '车次',
  `bx_channel` varchar(2) DEFAULT NULL COMMENT '保险公司渠道: 1、快保   2、合众',
  `order_channel` varchar(30) DEFAULT NULL COMMENT '订单渠道：19e、19e前台; inner、内嵌；app、app客户端；ext、对外商户   ',
  `merchant_id` varchar(30) DEFAULT NULL COMMENT '商户编号',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '失败原因',
  `bx_type` varchar(4) DEFAULT NULL COMMENT '0、儿童；1、成人',
  `trans_no` varchar(40) DEFAULT NULL COMMENT '支付交易流水号',
  `card_id` int(11) DEFAULT NULL,
  `applyPolicyNo` varchar(20) DEFAULT NULL,
  `orderNo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`bx_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='整体保险单信息';

/*Table structure for table `cp_orderinfo_bxfp` */

DROP TABLE IF EXISTS `cp_orderinfo_bxfp`;

CREATE TABLE `cp_orderinfo_bxfp` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `fp_receiver` varchar(50) DEFAULT NULL COMMENT '收件人',
  `fp_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `fp_zip_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `fp_address` varchar(300) DEFAULT NULL COMMENT '收件地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo_cp` */

DROP TABLE IF EXISTS `cp_orderinfo_cp`;

CREATE TABLE `cp_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `cert_type` int(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `cert_no` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` varchar(45) DEFAULT NULL COMMENT '支付价格',
  `buy_money` varchar(45) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `check_status` varchar(2) DEFAULT NULL COMMENT '12306身份核验状态 0、已通过 1、审核中 2、未通过',
  `ctrip_bx_price` varchar(20) DEFAULT NULL COMMENT '携程出票单张保险价钱',
  `is_white_list` tinyint(4) DEFAULT NULL COMMENT '是否属于白名单   1：否   2：是',
  `sub_outTicket_billno` varchar(32) DEFAULT NULL COMMENT '子订单号',
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_10_idx` (`order_id`),
  KEY `create_time_idx` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票出票信息表\r\n';

/*Table structure for table `cp_orderinfo_ct` */

DROP TABLE IF EXISTS `cp_orderinfo_ct`;

CREATE TABLE `cp_orderinfo_ct` (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `order_id` varchar(32) DEFAULT NULL COMMENT '订单ID',
  `train_num` varchar(10) DEFAULT NULL COMMENT '车次',
  `station_name` varchar(10) DEFAULT '' COMMENT '检票站名',
  `entrance` varchar(32) DEFAULT NULL COMMENT '检票口',
  PRIMARY KEY (`auto_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `cp_orderinfo_ct_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `cp_orderinfo` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo_find` */

DROP TABLE IF EXISTS `cp_orderinfo_find`;

CREATE TABLE `cp_orderinfo_find` (
  `find_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '未找到订单主键',
  `order_id` varchar(50) NOT NULL COMMENT '订单id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `find_status` varchar(2) NOT NULL COMMENT '查找状态:00、未查找 11、查找中 22、查找完成 33、未找到',
  PRIMARY KEY (`find_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_find_status` (`find_status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='支付成功未找到订单记录表';

/*Table structure for table `cp_orderinfo_history` */

DROP TABLE IF EXISTS `cp_orderinfo_history`;

CREATE TABLE `cp_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表ID',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `order_optlog` varchar(500) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`),
  KEY `Inx_opter_create_time` (`opter`,`create_time`),
  KEY `IND_cp_orderinfo_history_create_time` (`create_time`),
  KEY `IND_cp_orderinfo_history_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=58049200 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo_history_backup` */

DROP TABLE IF EXISTS `cp_orderinfo_history_backup`;

CREATE TABLE `cp_orderinfo_history_backup` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表ID',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`),
  KEY `FK_Reference_11_idx` (`order_id`),
  CONSTRAINT `FK_Reference_13` FOREIGN KEY (`order_id`) REFERENCES `cp_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21957 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo_mail` */

DROP TABLE IF EXISTS `cp_orderinfo_mail`;

CREATE TABLE `cp_orderinfo_mail` (
  `mail_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '邮箱id',
  `address` varchar(50) DEFAULT NULL COMMENT '邮箱地址',
  `pwd` varchar(50) DEFAULT NULL COMMENT '邮箱密码',
  `status` varchar(2) DEFAULT '0' COMMENT '状态：0、未使用 1、已使用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_name` varchar(45) DEFAULT NULL COMMENT '操作人',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`mail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6647 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo_modify_phone` */

DROP TABLE IF EXISTS `cp_orderinfo_modify_phone`;

CREATE TABLE `cp_orderinfo_modify_phone` (
  `phone_num` varchar(20) NOT NULL COMMENT '电话号码',
  `status` varchar(2) DEFAULT '0' COMMENT '00、获取成功 01、发送成功 02 获取验证码成功 22、验证成功 33手机已被验证 34其他错误',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '修改时间',
  `channel` varchar(10) DEFAULT NULL COMMENT '渠道',
  `code` varchar(10) DEFAULT NULL COMMENT '验证码',
  `regist_id` varchar(20) DEFAULT NULL COMMENT '注册账号id',
  `acc_id` varchar(20) DEFAULT NULL COMMENT '账号id',
  PRIMARY KEY (`phone_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo_notify` */

DROP TABLE IF EXISTS `cp_orderinfo_notify`;

CREATE TABLE `cp_orderinfo_notify` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `notify_num` int(5) DEFAULT NULL COMMENT '通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `notify_next_time` datetime DEFAULT NULL COMMENT '下次通知时间',
  `notify_status` int(5) DEFAULT NULL COMMENT '通知状态 0、未通知 1、等待通知 2、正在通知 3、通知成功 4、通知失败 5、重新通知',
  `notify_url` varchar(150) DEFAULT NULL COMMENT '通知地址',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `notify_type` int(5) DEFAULT '0' COMMENT '通知类别：0、预订成功通知 1、出票成功通知',
  PRIMARY KEY (`order_id`),
  CONSTRAINT `FK_Reference_11` FOREIGN KEY (`order_id`) REFERENCES `cp_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台通知表';

/*Table structure for table `cp_orderinfo_notify_0505` */

DROP TABLE IF EXISTS `cp_orderinfo_notify_0505`;

CREATE TABLE `cp_orderinfo_notify_0505` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `notify_num` int(5) DEFAULT NULL COMMENT '通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `notify_next_time` datetime DEFAULT NULL COMMENT '下次通知时间',
  `notify_status` int(5) DEFAULT NULL COMMENT '通知状态 0、未通知 1、等待通知 2、正在通知 3、通知成功 4、通知失败 5、重新通知',
  `notify_url` varchar(150) DEFAULT NULL COMMENT '通知地址',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `notify_type` int(2) DEFAULT '0' COMMENT '通知类别：0、预订成功通知 1、出票成功通知 2、取消预约通知',
  PRIMARY KEY (`id`),
  KEY `idx_notify_status` (`notify_status`)
) ENGINE=InnoDB AUTO_INCREMENT=3362388 DEFAULT CHARSET=utf8 COMMENT='出票结果通知表';

/*Table structure for table `cp_orderinfo_phone` */

DROP TABLE IF EXISTS `cp_orderinfo_phone`;

CREATE TABLE `cp_orderinfo_phone` (
  `phone_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '电话id',
  `phone_num` varchar(50) DEFAULT NULL COMMENT '电话号码',
  `status` varchar(2) DEFAULT '0' COMMENT '状态：0、未使用 1、已使用',
  `phone_total` int(5) DEFAULT '0' COMMENT '使用次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_name` varchar(45) DEFAULT NULL COMMENT '操作人',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `use_status` varchar(2) DEFAULT '00' COMMENT '00：多次使用；11：使用一次',
  PRIMARY KEY (`phone_id`)
) ENGINE=InnoDB AUTO_INCREMENT=195966 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo_ps` */

DROP TABLE IF EXISTS `cp_orderinfo_ps`;

CREATE TABLE `cp_orderinfo_ps` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `ps_billno` varchar(45) DEFAULT NULL COMMENT '配送单号',
  `ps_company` varchar(45) DEFAULT NULL COMMENT '配送公司',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '配送费用',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `ps_status` varchar(2) DEFAULT NULL COMMENT '配送状态：00、等待配送 11、正在配送 22、配送成功',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `link_name` varchar(45) DEFAULT NULL COMMENT '联系人姓名',
  `link_phone` varchar(45) DEFAULT NULL COMMENT '联系人电话',
  `link_address` varchar(125) DEFAULT NULL COMMENT '联系人地址',
  `link_mail` varchar(45) DEFAULT NULL COMMENT '联系人邮件\n',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配送单信息';

/*Table structure for table `cp_orderinfo_queue` */

DROP TABLE IF EXISTS `cp_orderinfo_queue`;

CREATE TABLE `cp_orderinfo_queue` (
  `order_id` varchar(50) NOT NULL COMMENT '订单id',
  `resend_num` int(11) DEFAULT '0' COMMENT '重发次数',
  `queue_status` varchar(10) DEFAULT '00' COMMENT '排队状态',
  `resend_time` datetime DEFAULT NULL COMMENT '重发时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`),
  KEY `cp_orderinfo_queue_queue_status` (`queue_status`),
  KEY `cp_orderinfo_queue_create_time` (`create_time`),
  KEY `cp_orderinfo_queue_resend_time` (`resend_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='排队订单表';

/*Table structure for table `cp_orderinfo_refund` */

DROP TABLE IF EXISTS `cp_orderinfo_refund`;

CREATE TABLE `cp_orderinfo_refund` (
  `refund_seq` varchar(32) DEFAULT NULL COMMENT '退款流水号',
  `order_id` varchar(32) NOT NULL COMMENT '订单号',
  `robot_id` varchar(50) DEFAULT NULL COMMENT ' 机器人id',
  `cp_id` varchar(32) NOT NULL COMMENT '车票号',
  `account_name` varchar(32) DEFAULT NULL COMMENT '12306帐号',
  `account_pwd` varchar(32) DEFAULT NULL COMMENT '12306帐号密码',
  `buy_money` decimal(11,1) DEFAULT '0.0' COMMENT '成本价格',
  `alter_diff_money` decimal(11,1) DEFAULT '0.0' COMMENT '改签差额',
  `alter_buy_money` decimal(11,1) DEFAULT '0.0' COMMENT '改签后成本价格',
  `refund_money` decimal(11,1) DEFAULT '0.0' COMMENT '退款金额',
  `refund_12306_money` decimal(11,1) DEFAULT '0.0' COMMENT '12306退款金额',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `order_status` varchar(2) DEFAULT NULL COMMENT '00：等待机器改签 02：开始机器改签 03：人工改签 04：等待机器退票 06：开始机器退票 07：退票人工 08：待审核退票 09：正在审核 11：退票完成 22：拒绝退票 33：审核退款完成',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_ren` varchar(30) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_billno` varchar(20) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(20) DEFAULT NULL COMMENT '车次',
  `alter_train_no` varchar(20) DEFAULT NULL COMMENT '改签后车次',
  `from_station` varchar(32) DEFAULT NULL COMMENT '出发城市',
  `arrive_station` varchar(32) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `alter_from_time` datetime DEFAULT NULL COMMENT '改签后发车时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `alter_travel_time` datetime DEFAULT NULL COMMENT '改签后乘车日期',
  `seat_type` int(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `alter_seat_type` int(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `alter_train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `alter_seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `ids_type` int(2) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(32) DEFAULT NULL COMMENT '证件号码',
  `error_info` varchar(145) DEFAULT NULL,
  `channel` varchar(20) DEFAULT NULL COMMENT '渠道ID',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '日志简略说明',
  `user_name` varchar(32) DEFAULT NULL COMMENT '乘客姓名',
  `refund_12306_seq` varchar(32) DEFAULT NULL COMMENT '12306退票流水号',
  `user_remark` varchar(150) DEFAULT NULL COMMENT '用户备注',
  `our_remark` varchar(150) DEFAULT NULL COMMENT '备注',
  `refuse_reason` varchar(2) DEFAULT NULL COMMENT '拒绝退票原因：1、已取票 2、已过时间 3、来电取消',
  `verify_time` datetime DEFAULT NULL COMMENT '审核退款时间',
  `pro_bak2` varchar(2) DEFAULT NULL COMMENT '备用字段2',
  `old_refund_seq` varchar(32) DEFAULT NULL COMMENT '原退款流水号',
  `level` varchar(10) DEFAULT '00' COMMENT '订单级别：00普通 11_1联程订单第一联 11_2联程订单第二联',
  `refund_type` varchar(2) DEFAULT NULL COMMENT '退票类型 11线上退票 55 改签退票',
  `alter_myself` tinyint(1) DEFAULT NULL COMMENT '是否是咱们自己改签的订单   0：否  1：是',
  PRIMARY KEY (`order_id`,`cp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票退票信息表';

/*Table structure for table `cp_orderinfo_refund_history` */

DROP TABLE IF EXISTS `cp_orderinfo_refund_history`;

CREATE TABLE `cp_orderinfo_refund_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表ID',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=520 DEFAULT CHARSET=utf8 COMMENT='退票操作日志';

/*Table structure for table `cp_orderinfo_refund_jd` */

DROP TABLE IF EXISTS `cp_orderinfo_refund_jd`;

CREATE TABLE `cp_orderinfo_refund_jd` (
  `order_id` varchar(32) NOT NULL COMMENT '订单号',
  `cp_id` varchar(32) NOT NULL COMMENT '车票号',
  `jd_order_id` varchar(50) DEFAULT NULL COMMENT '京东流水号，不是京东订单号',
  `jd_account_name` varchar(32) DEFAULT NULL COMMENT '京东帐号',
  `jd_account_pwd` varchar(32) DEFAULT NULL COMMENT '京东帐号密码',
  `buy_money` decimal(11,1) DEFAULT '0.0' COMMENT '成本价格',
  `refund_money` decimal(11,1) DEFAULT '0.0' COMMENT '退款金额',
  `refund_12306_money` decimal(11,1) DEFAULT '0.0' COMMENT '12306退款金额',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `order_status` varchar(2) DEFAULT NULL COMMENT '退票状态 00、开始退票  01、重发退票   02、正在退票   04：开始退票结果查询  05：退票结果查询重发 06：退票结果查询中  09：退票人工  10、退票失败   11、退票成功 ',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_ren` varchar(30) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_billno` varchar(20) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(20) DEFAULT NULL COMMENT '车次',
  `from_station` varchar(32) DEFAULT NULL COMMENT '出发城市',
  `arrive_station` varchar(32) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `cert_type` int(2) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `cert_no` varchar(32) DEFAULT NULL COMMENT '证件号码',
  `error_info` varchar(145) DEFAULT NULL,
  `channel` varchar(20) DEFAULT NULL COMMENT '渠道ID',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '日志简略说明',
  `user_name` varchar(32) DEFAULT NULL COMMENT '乘客姓名',
  `refund_12306_seq` varchar(32) DEFAULT NULL COMMENT '12306退票流水号',
  `refund_seq` varchar(32) DEFAULT NULL COMMENT '退款流水号',
  `user_remark` varchar(150) DEFAULT NULL COMMENT '用户备注',
  `our_remark` varchar(150) DEFAULT NULL COMMENT '备注',
  `old_refund_seq` varchar(32) DEFAULT NULL COMMENT '原退款流水号',
  `refund_type` varchar(2) DEFAULT NULL COMMENT '退票类型 11线上退票 22线下退票',
  `refund_request_num` tinyint(2) DEFAULT '0' COMMENT '京东退票请求重发次数',
  `query_refund_num` tinyint(2) DEFAULT '0' COMMENT '京东退票结果查询次数',
  `refuse_reason` varchar(2) DEFAULT NULL COMMENT '拒绝退票原因：1、已取票 2、已过时间 3、来电取消 4：账号被封 6：退款金额有损失  7：不可退票',
  PRIMARY KEY (`order_id`,`cp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票退票信息表';

/*Table structure for table `cp_orderinfo_refund_jd_history` */

DROP TABLE IF EXISTS `cp_orderinfo_refund_jd_history`;

CREATE TABLE `cp_orderinfo_refund_jd_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表ID',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=548 DEFAULT CHARSET=utf8 COMMENT='退票操作日志';

/*Table structure for table `cp_orderinfo_refund_notify` */

DROP TABLE IF EXISTS `cp_orderinfo_refund_notify`;

CREATE TABLE `cp_orderinfo_refund_notify` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `cp_id` varchar(50) NOT NULL DEFAULT '' COMMENT '车票号',
  `notify_num` int(5) DEFAULT NULL COMMENT '通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `notify_next_time` datetime DEFAULT NULL COMMENT '下次通知时间',
  `notify_status` int(5) DEFAULT NULL COMMENT '通知状态 0、未通知 1、等待通知 2、正在通知 3、通知成功 4、通知失败 5、重新通知',
  `notify_url` varchar(150) DEFAULT NULL COMMENT '通知地址',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `notify_type` int(5) DEFAULT '0' COMMENT '通知类别：0、改签成功通知 1、退票结果通知',
  PRIMARY KEY (`order_id`,`cp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台退票通知表';

/*Table structure for table `cp_orderinfo_regist` */

DROP TABLE IF EXISTS `cp_orderinfo_regist`;

CREATE TABLE `cp_orderinfo_regist` (
  `regist_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '注册id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `ids_card` varchar(50) DEFAULT NULL COMMENT '二代身份证号',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `mail` varchar(100) DEFAULT NULL COMMENT '12306注册邮箱',
  `order_id` varchar(50) DEFAULT NULL COMMENT '关联订单号（注册）',
  `regist_status` varchar(2) DEFAULT NULL COMMENT '状态 00、等待注册 11、注册中 22、注册成功 33、注册失败 44、开始核验 55、正在核验',
  `fail_reason` varchar(2) DEFAULT NULL COMMENT '注册失败原因：00、邮箱激活失败 11、12306账号激活失败 99、其他',
  `regist_time` datetime DEFAULT NULL COMMENT '注册开始时间',
  `regist_num` int(3) DEFAULT '0' COMMENT '注册次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `channel` varchar(25) DEFAULT NULL COMMENT '渠道',
  `switch_num` int(3) DEFAULT '0' COMMENT '切换用户注册身份证次数',
  PRIMARY KEY (`regist_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_orderinfo_student` */

DROP TABLE IF EXISTS `cp_orderinfo_student`;

CREATE TABLE `cp_orderinfo_student` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `province_name` varchar(10) DEFAULT NULL COMMENT '省份名称',
  `province_code` varchar(10) DEFAULT NULL COMMENT '省份编号',
  `school_code` varchar(10) DEFAULT NULL COMMENT '学校代号',
  `school_name` varchar(45) DEFAULT NULL COMMENT '学校名称',
  `student_no` varchar(45) DEFAULT NULL COMMENT '学号',
  `school_system` varchar(45) DEFAULT NULL COMMENT '学制',
  `enter_year` varchar(45) DEFAULT NULL COMMENT '入学年份：yyyy',
  `preference_from_station_name` varchar(45) DEFAULT NULL COMMENT '优惠区间起始地名称【选填】',
  `preference_from_station_code` varchar(10) DEFAULT NULL COMMENT '优惠区间起始地代号',
  `preference_to_station_name` varchar(10) DEFAULT NULL COMMENT '优惠区间到达地名称【选填】',
  `preference_to_station_code` varchar(10) DEFAULT NULL COMMENT '优惠区间到达地代号',
  `channel` varchar(10) DEFAULT NULL COMMENT '渠道',
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_10_idx` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生票支持信息\n';

/*Table structure for table `cp_pass_acc` */

DROP TABLE IF EXISTS `cp_pass_acc`;

CREATE TABLE `cp_pass_acc` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `acc_id` int(11) unsigned NOT NULL COMMENT '12306账号表主键',
  `acc_username` varchar(45) NOT NULL COMMENT '12306账号',
  `acc_password` varchar(45) NOT NULL COMMENT '12306账号密码',
  `worker_ext` varchar(100) NOT NULL COMMENT '机器人url',
  `use_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 00-待核验 11-核验中  22-核验成功 33-核验失败 44-3次核验均返回空',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `use_time` datetime DEFAULT NULL COMMENT '核验时间',
  `verify_num` int(2) DEFAULT NULL COMMENT '核验次数',
  PRIMARY KEY (`id`),
  KEY `idx_pass_accId` (`acc_id`) USING BTREE,
  KEY `idx_pass_accuser` (`acc_username`) USING BTREE,
  KEY `idx_pass_use_status` (`use_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='账号核验信息表';

/*Table structure for table `cp_pass_white_incre` */

DROP TABLE IF EXISTS `cp_pass_white_incre`;

CREATE TABLE `cp_pass_white_incre` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `contact_name` varchar(30) NOT NULL COMMENT '联系人姓名',
  `contact_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '联系人状态 0-已通过 1-待核验 2-未通过',
  `cert_no` varchar(30) NOT NULL COMMENT '身份证号',
  `cert_type` varchar(4) NOT NULL DEFAULT '2' COMMENT '证件类型1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通行证，B:护照 ',
  `person_type` int(2) DEFAULT '1' COMMENT '联系人类型：1成人 2儿童 3学生',
  `acc_username` varchar(45) NOT NULL COMMENT '12306账号',
  `acc_id` int(11) NOT NULL COMMENT '账号表主键',
  `acc_status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '账号状态 1失效 2可用 ',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_pass_contname` (`contact_name`) USING BTREE,
  KEY `idx_pass_accId` (`acc_id`) USING BTREE,
  KEY `idx_pass_accuser` (`acc_username`) USING BTREE,
  KEY `idx_pass_cert_no` (`cert_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COMMENT='增量的白名单信息表';

/*Table structure for table `cp_pass_whitelist` */

DROP TABLE IF EXISTS `cp_pass_whitelist`;

CREATE TABLE `cp_pass_whitelist` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `contact_name` varchar(30) NOT NULL COMMENT '联系人姓名',
  `contact_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '联系人状态 0-已通过 1-待核验 2-未通过',
  `cert_no` varchar(30) NOT NULL COMMENT '身份证号',
  `cert_type` varchar(4) NOT NULL DEFAULT '99' COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照  99、未确定   ',
  `person_type` tinyint(2) DEFAULT '1' COMMENT '联系人类型：0成人 1儿童 3学生',
  `acc_username` varchar(45) DEFAULT NULL COMMENT '12306账号',
  `acc_id` int(11) NOT NULL COMMENT '账号表主键',
  `acc_status` tinyint(4) NOT NULL DEFAULT '99' COMMENT '账号状态 0-已锁定  1-待手机核验  2-正常使用 99-未确定',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `count_num` int(11) DEFAULT '0',
  `last_match_time` datetime DEFAULT NULL,
  `can_del` int(2) DEFAULT '0',
  `delete_date` date DEFAULT NULL COMMENT '添加时间',
  `user_self` char(1) DEFAULT NULL,
  `temp_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_accid_certno_name` (`acc_id`,`cert_no`,`contact_name`) USING BTREE,
  KEY `idx_pass_contname` (`contact_name`) USING BTREE,
  KEY `idx_pass_accId` (`acc_id`) USING BTREE,
  KEY `idx_pass_accuser` (`acc_username`) USING BTREE,
  KEY `idx_pass_cert_no` (`cert_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29374692 DEFAULT CHARSET=utf8 COMMENT='账号白名单池表';

/*Table structure for table `cp_pass_whitelist_run_0307` */

DROP TABLE IF EXISTS `cp_pass_whitelist_run_0307`;

CREATE TABLE `cp_pass_whitelist_run_0307` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `contact_name` varchar(30) NOT NULL COMMENT '联系人姓名',
  `contact_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '联系人状态 0-已通过 1-待核验 2-未通过',
  `cert_no` varchar(30) NOT NULL COMMENT '身份证号',
  `cert_type` varchar(4) NOT NULL DEFAULT '2' COMMENT '证件类型2、一代身份证、1、二代身份证、C、港澳通行证、G、台湾通行证、B、护照  ',
  `person_type` int(2) DEFAULT '1' COMMENT '联系人类型：1成人 2儿童 3学生',
  `acc_username` varchar(45) NOT NULL COMMENT '12306账号',
  `acc_id` int(11) NOT NULL COMMENT '账号表主键',
  `acc_status` tinyint(4) NOT NULL DEFAULT '99' COMMENT '账号状态 0-已锁定  1-待手机核验  2-正常使用 99-未确定',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `person_type1` int(2) DEFAULT NULL COMMENT '联系人类型：1成人 2儿童 3学生',
  PRIMARY KEY (`id`),
  KEY `idx_pass_contname` (`contact_name`) USING BTREE,
  KEY `idx_pass_accId` (`acc_id`) USING BTREE,
  KEY `idx_pass_accuser` (`acc_username`) USING BTREE,
  KEY `idx_pass_cert_no` (`cert_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10184363 DEFAULT CHARSET=utf8 COMMENT='账号白名单池表';

/*Table structure for table `cp_proxy_info` */

DROP TABLE IF EXISTS `cp_proxy_info`;

CREATE TABLE `cp_proxy_info` (
  `proxy_id` int(11) NOT NULL AUTO_INCREMENT,
  `proxy_ip` varchar(45) DEFAULT NULL COMMENT '账号名称',
  `proxy_port` varchar(45) DEFAULT NULL COMMENT '登陆名',
  `proxy_status` varchar(45) DEFAULT NULL COMMENT '代理状态：00、未使用 11、已使用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `stop_reason` varchar(2) DEFAULT NULL COMMENT '停用原因：',
  `channel` varchar(15) DEFAULT NULL COMMENT '00、自己购买永久  11、接口请求 ',
  `active time` datetime DEFAULT NULL COMMENT '有效时间',
  PRIMARY KEY (`proxy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13847 DEFAULT CHARSET=utf8 COMMENT='代理表';

/*Table structure for table `cp_proxy_info_regist` */

DROP TABLE IF EXISTS `cp_proxy_info_regist`;

CREATE TABLE `cp_proxy_info_regist` (
  `proxy_id` int(11) NOT NULL AUTO_INCREMENT,
  `proxy_ip` varchar(45) DEFAULT NULL COMMENT '账号名称',
  `proxy_port` varchar(45) DEFAULT NULL COMMENT '登陆名',
  `proxy_status` varchar(45) DEFAULT NULL COMMENT '代理状态：00、未使用 11、已使用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `stop_reason` varchar(2) DEFAULT NULL COMMENT '停用原因：',
  `channel` varchar(15) DEFAULT NULL COMMENT '00、自己购买永久  11、接口请求 ',
  `active time` datetime DEFAULT NULL COMMENT '有效时间',
  PRIMARY KEY (`proxy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14510 DEFAULT CHARSET=utf8 COMMENT='代理表4注册';

/*Table structure for table `cp_proxy_pre` */

DROP TABLE IF EXISTS `cp_proxy_pre`;

CREATE TABLE `cp_proxy_pre` (
  `proxy_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '代理id',
  `proxy_ip` varchar(25) NOT NULL COMMENT 'ip地址',
  `proxy_port` varchar(6) NOT NULL COMMENT 'ip端口',
  `proxy_status` varchar(3) DEFAULT NULL COMMENT '代理状态 : 0、未检测 1、可用 2、不可用 3、已发送 4、正在检测',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `resp_time` int(20) DEFAULT NULL COMMENT '响应时间',
  PRIMARY KEY (`proxy_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_proxy_status` (`proxy_status`)
) ENGINE=InnoDB AUTO_INCREMENT=4501 DEFAULT CHARSET=utf8 COMMENT='代理';

/*Table structure for table `cp_proxy_pre_regist` */

DROP TABLE IF EXISTS `cp_proxy_pre_regist`;

CREATE TABLE `cp_proxy_pre_regist` (
  `proxy_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '代理id',
  `proxy_ip` varchar(25) NOT NULL COMMENT 'ip地址',
  `proxy_port` varchar(6) NOT NULL COMMENT 'ip端口',
  `proxy_status` varchar(3) DEFAULT NULL COMMENT '代理状态 : 0、未检测 1、可用 2、不可用 3、已发送 4、正在检测',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `resp_time` int(20) DEFAULT NULL COMMENT '响应时间',
  PRIMARY KEY (`proxy_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_proxy_status` (`proxy_status`)
) ENGINE=InnoDB AUTO_INCREMENT=10981 DEFAULT CHARSET=utf8 COMMENT='代理4注册';

/*Table structure for table `cp_statinfo` */

DROP TABLE IF EXISTS `cp_statinfo`;

CREATE TABLE `cp_statinfo` (
  `stat_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '统计表ID',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `channel` varchar(10) DEFAULT NULL COMMENT '渠道',
  `order_succeed` int(10) DEFAULT NULL COMMENT '订单成功',
  `order_defeated` int(10) DEFAULT NULL COMMENT '订单失败',
  `order_count` int(10) DEFAULT NULL COMMENT '总订单',
  `succeed_money` decimal(11,3) DEFAULT NULL COMMENT '成功金额',
  `defeated_money` decimal(11,3) DEFAULT NULL COMMENT '失败金额',
  `ticket_count` int(10) DEFAULT NULL COMMENT '票数',
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1231 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_tradeinfo` */

DROP TABLE IF EXISTS `cp_tradeinfo`;

CREATE TABLE `cp_tradeinfo` (
  `trade_id` varchar(30) NOT NULL,
  `order_id` varchar(30) NOT NULL COMMENT '订单号',
  `batch_no` varchar(40) DEFAULT NULL COMMENT '退款批次号',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '交易流水号',
  `trade_seq` varchar(40) DEFAULT NULL COMMENT '平台流水号',
  `trade_status` varchar(3) DEFAULT NULL COMMENT '交易状态 00、等待买家付款， 01、等待卖家收款， 02，交易成功，且可对该交易做操作， 03， 交易成功且结束，即不可再做任何操作， 04、交易关闭， 10、等待退款，11、正在退款，12、发起退款成功， 13、退款成功， 14， 退款失败， 15、 支付宝处理中或银行卡充退',
  `trade_fee` decimal(6,2) DEFAULT NULL COMMENT '交易金额',
  `trade_type` varchar(2) DEFAULT NULL COMMENT '交易类型 0、收入， 1、支出',
  `channel` varchar(2) DEFAULT NULL COMMENT '渠道，如qunar，elong, chunqiu',
  `trade_channel` varchar(20) DEFAULT NULL COMMENT '交易渠道,如alipay，ccb',
  `create_time` datetime DEFAULT NULL COMMENT '交易信息创建时间',
  `trade_time` datetime DEFAULT NULL,
  `buyer_id` varchar(30) DEFAULT NULL COMMENT '支付账号',
  `buyer_name` varchar(30) DEFAULT NULL COMMENT '支付账号名',
  `seller_id` varchar(30) DEFAULT NULL COMMENT '收款账号',
  `operate_time` datetime DEFAULT NULL,
  `operate_person` varchar(30) DEFAULT NULL,
  `fail_reason` varchar(255) DEFAULT NULL COMMENT '失败原因',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`trade_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cp_workercode_info` */

DROP TABLE IF EXISTS `cp_workercode_info`;

CREATE TABLE `cp_workercode_info` (
  `code_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `worker_id` int(11) DEFAULT NULL COMMENT '工人ID',
  `card_id` int(11) DEFAULT NULL COMMENT '支付宝账号表主键',
  `card_no` varchar(45) DEFAULT NULL COMMENT '支付宝账号',
  `verification_code` varchar(10) DEFAULT NULL COMMENT '验证码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`code_id`),
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1243826 DEFAULT CHARSET=utf8 COMMENT='验证码';

/*Table structure for table `cp_workerinfo` */

DROP TABLE IF EXISTS `cp_workerinfo`;

CREATE TABLE `cp_workerinfo` (
  `worker_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '工人ID',
  `worker_name` varchar(45) DEFAULT NULL COMMENT '创建名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `worker_type` int(11) DEFAULT NULL COMMENT '账号类型 1、预定机器人 2、人工  3、支付机器人 5、核验机器人6、取消机器人7、改签机器人、8退票机器人9、查询机器人 10、实名认证 11、查询票价机器人 12、保险 13、删除联系人 14、注册帐号 15、激活帐号  21：携程预订机器人  44：京东预定机器人',
  `worker_priority` int(11) DEFAULT NULL COMMENT '优先级',
  `order_num` int(11) DEFAULT '0' COMMENT '处理订单数',
  `worker_status` varchar(10) DEFAULT '22' COMMENT '工作状态：00、空闲  11、工作中  22、停用  33 备用   99 需要人工处理  01 在队列中 02 预登录',
  `worker_ext` varchar(150) DEFAULT NULL COMMENT '扩展字段',
  `max_order_num` int(11) DEFAULT '10' COMMENT '最大订单处理数',
  `opt_name` varchar(45) DEFAULT NULL COMMENT '操作人',
  `spare_thread` int(2) DEFAULT '0' COMMENT '机器人空闲进程数',
  `robot_id` varchar(50) DEFAULT NULL COMMENT '机器人ID',
  `robot_con_timeout` varchar(10) DEFAULT NULL COMMENT '连接超时时间',
  `robot_read_timeout` varchar(10) DEFAULT NULL COMMENT '连接读取时间',
  `public_ip` varchar(20) DEFAULT NULL COMMENT '机器人公网ip',
  `private_ip` varchar(20) DEFAULT NULL COMMENT '机器人内网ip',
  `stop_reason` varchar(10) DEFAULT NULL COMMENT '机器人停用原因11 机器人需要重启22 机器人IP被封33 该支付机器人余额不足 44 其他',
  `proxy_start_time` datetime DEFAULT NULL COMMENT '代理配置成功时间',
  `proxy_status` varchar(11) DEFAULT '22' COMMENT '00、代理重置中 11、代理重置完成',
  `work_time` datetime DEFAULT NULL COMMENT '上一次工作时间',
  `work_num` int(11) DEFAULT '0' COMMENT '机器人使用次数',
  `script_version` int(11) DEFAULT NULL COMMENT '脚本版本',
  `single_order` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否正在订单预订 0、否 1、是',
  `app_valid` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 默认，1 白名单',
  `worker_vendor` tinyint(4) DEFAULT NULL COMMENT '1.阿里云 2.美团云 3.百度云 4.腾讯云 5.金山云 6.华为云 7.亚马逊云 8.微软云 9.京东云',
  `worker_region` tinyint(4) DEFAULT NULL COMMENT '机器区域:1.华北1(青岛)2.华北2(北京)3.华东1(杭州)4.华东2(上海)5.华南1(深圳)',
  `worker_describe` varchar(20) DEFAULT NULL COMMENT '机器描述信息',
  `worker_language_type` int(4) DEFAULT '1' COMMENT '机器脚本语言类型： 1：lua脚本  2：java脚本',
  `worker_lock` tinyint(4) DEFAULT '0' COMMENT '当前机器是否锁定    0、否  1、是',
  `pay_device_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0--PC端支付  1-APP端支付',
  `worker_mtyun_usenum` int(4) DEFAULT '0' COMMENT '美团云机器使用次数',
  `worker_mtyun_identify` tinyint(4) DEFAULT '0' COMMENT '是否美团云机器标识    0：否   1：是',
  `worker_lock_2` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`worker_id`),
  KEY `work_time_INDEX` (`worker_type`,`worker_status`,`work_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1113148 DEFAULT CHARSET=utf8 COMMENT='工作人员表';

/*Table structure for table `cp_workerinfo_ali` */

DROP TABLE IF EXISTS `cp_workerinfo_ali`;

CREATE TABLE `cp_workerinfo_ali` (
  `id` varchar(255) DEFAULT NULL COMMENT '实例ID',
  `name` varchar(255) DEFAULT NULL COMMENT '实例名称\r\n',
  `region` varchar(255) DEFAULT NULL COMMENT '地域\r\n',
  `IP1` varchar(255) DEFAULT NULL COMMENT '公网IP\r\n',
  `IP2` varchar(255) DEFAULT NULL COMMENT '内网IP\r\n',
  `time1` varchar(255) DEFAULT NULL COMMENT '当前到期时间\r\n',
  `time2` varchar(255) DEFAULT NULL COMMENT '续费后到期时间\r\n',
  `time3` varchar(255) DEFAULT NULL COMMENT '时长\r\n',
  `amount` varchar(255) DEFAULT NULL COMMENT '费用\r\n',
  `batch_id` varchar(255) DEFAULT NULL COMMENT '批次',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `rownum` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`rownum`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_workerinfo_bak` */

DROP TABLE IF EXISTS `cp_workerinfo_bak`;

CREATE TABLE `cp_workerinfo_bak` (
  `worker_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '工人ID',
  `worker_name` varchar(45) DEFAULT NULL COMMENT '创建名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `worker_type` int(11) DEFAULT NULL COMMENT '账号类型 1、预定机器人 2、人工  3、支付机器人 5、核验机器人6、取消机器人7、改签机器人、8退票机器人9、查询机器人 10、实名认证 11、查询票价机器人 12、保险 13、删除联系人 14、注册帐号 15、激活帐号',
  `worker_priority` int(11) DEFAULT NULL COMMENT '优先级',
  `order_num` int(11) DEFAULT '0' COMMENT '处理订单数',
  `worker_status` varchar(10) DEFAULT '22' COMMENT '工作状态：00、空闲  11、工作中  22、停用  33 备用   99 需要人工处理  01 在队列中 02 预登录',
  `worker_ext` varchar(150) DEFAULT NULL COMMENT '扩展字段',
  `max_order_num` int(11) DEFAULT '10' COMMENT '最大订单处理数',
  `opt_name` varchar(45) DEFAULT NULL COMMENT '操作人',
  `spare_thread` int(2) DEFAULT '0' COMMENT '机器人空闲进程数',
  `robot_id` varchar(50) DEFAULT NULL COMMENT '机器人ID',
  `robot_con_timeout` varchar(10) DEFAULT NULL COMMENT '连接超时时间',
  `robot_read_timeout` varchar(10) DEFAULT NULL COMMENT '连接读取时间',
  `work_host` varchar(10) DEFAULT NULL COMMENT '机器人host',
  `stop_reason` varchar(10) DEFAULT NULL COMMENT '机器人停用原因11 机器人需要重启22 机器人IP被封33 该支付机器人余额不足 44 其他',
  `proxy_start_time` datetime DEFAULT NULL COMMENT '代理配置成功时间',
  `proxy_status` varchar(11) DEFAULT '22' COMMENT '00、代理重置中 11、代理重置完成',
  `work_time` datetime DEFAULT NULL COMMENT '上一次工作时间',
  `work_num` int(11) DEFAULT '0' COMMENT '机器人使用次数',
  `script_version` int(11) DEFAULT NULL COMMENT '脚本版本',
  `single_order` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否正在订单预订 0、否 1、是',
  `app_valid` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 默认，1 白名单',
  `worker_region` varchar(20) DEFAULT NULL COMMENT '机器区域 (北京，上海，深圳)',
  PRIMARY KEY (`worker_id`),
  KEY `work_time_INDEX` (`worker_type`,`worker_status`,`work_time`)
) ENGINE=InnoDB AUTO_INCREMENT=878 DEFAULT CHARSET=utf8 COMMENT='工作人员表';

/*Table structure for table `cp_workerinfo_copy` */

DROP TABLE IF EXISTS `cp_workerinfo_copy`;

CREATE TABLE `cp_workerinfo_copy` (
  `worker_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '工人ID',
  `worker_name` varchar(45) DEFAULT NULL COMMENT '创建名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `worker_type` int(11) DEFAULT NULL COMMENT '账号类型 1、预定机器人 2、人工  3、支付机器人 5、核验机器人6、取消机器人7、改签机器人、8退票机器人9、查询机器人 10、实名认证 11、查询票价机器人 12、保险 13、删除联系人 14、注册帐号 15、激活帐号  21,携程',
  `worker_priority` int(11) DEFAULT NULL COMMENT '优先级',
  `order_num` int(11) DEFAULT '0' COMMENT '处理订单数',
  `worker_status` varchar(10) DEFAULT '22' COMMENT '工作状态：00、空闲  11、工作中  22、停用  33 备用   99 需要人工处理  01 在队列中 02 预登录',
  `worker_ext` varchar(150) DEFAULT NULL COMMENT '扩展字段',
  `max_order_num` int(11) DEFAULT '10' COMMENT '最大订单处理数',
  `opt_name` varchar(45) DEFAULT NULL COMMENT '操作人',
  `spare_thread` int(2) DEFAULT '0' COMMENT '机器人空闲进程数',
  `robot_id` varchar(50) DEFAULT NULL COMMENT '机器人ID',
  `robot_con_timeout` varchar(10) DEFAULT NULL COMMENT '连接超时时间',
  `robot_read_timeout` varchar(10) DEFAULT NULL COMMENT '连接读取时间',
  `work_host` varchar(10) DEFAULT NULL COMMENT '机器人host',
  `stop_reason` varchar(10) DEFAULT NULL COMMENT '机器人停用原因11 机器人需要重启22 机器人IP被封33 该支付机器人余额不足 44 其他 55 ，人工',
  `proxy_start_time` datetime DEFAULT NULL COMMENT '代理配置成功时间',
  `proxy_status` varchar(11) DEFAULT '22' COMMENT '00、代理重置中 11、代理重置完成',
  `work_time` datetime DEFAULT NULL COMMENT '上一次工作时间',
  `work_num` int(11) DEFAULT '0' COMMENT '机器人使用次数',
  `script_version` int(11) DEFAULT NULL COMMENT '脚本版本',
  `single_order` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否正在订单预订 0、否 1、是',
  `app_valid` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 白名单，1 黑名单',
  `worker_region` varchar(20) DEFAULT NULL COMMENT '机器区域 (北京，上海，深圳)',
  `pay_device_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0--PC端支付  1-APP端支付',
  PRIMARY KEY (`worker_id`),
  KEY `work_time_INDEX` (`worker_type`,`worker_status`,`work_time`)
) ENGINE=InnoDB AUTO_INCREMENT=890 DEFAULT CHARSET=utf8 COMMENT='工作人员表';

/*Table structure for table `cp_workerinfo_copy1` */

DROP TABLE IF EXISTS `cp_workerinfo_copy1`;

CREATE TABLE `cp_workerinfo_copy1` (
  `worker_id` int(11) DEFAULT NULL,
  `worker_name` varchar(135) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `option_time` datetime DEFAULT NULL,
  `worker_type` int(11) DEFAULT NULL,
  `worker_priority` int(11) DEFAULT NULL,
  `order_num` int(11) DEFAULT NULL,
  `worker_status` varchar(30) DEFAULT NULL,
  `worker_ext` varchar(450) DEFAULT NULL,
  `max_order_num` int(11) DEFAULT NULL,
  `opt_name` varchar(135) DEFAULT NULL,
  `spare_thread` int(7) DEFAULT NULL,
  `robot_id` varchar(150) DEFAULT NULL,
  `robot_con_timeout` varchar(30) DEFAULT NULL,
  `robot_read_timeout` varchar(30) DEFAULT NULL,
  `work_host` varchar(30) DEFAULT NULL,
  `stop_reason` varchar(30) DEFAULT NULL,
  `proxy_start_time` datetime DEFAULT NULL,
  `proxy_status` varchar(33) DEFAULT NULL,
  `work_time` datetime DEFAULT NULL,
  `work_num` int(11) DEFAULT NULL,
  `script_version` int(11) DEFAULT NULL,
  `single_order` tinyint(4) DEFAULT NULL,
  `app_valid` tinyint(4) DEFAULT NULL,
  `worker_region` varchar(60) DEFAULT NULL,
  `worker_language_type` int(4) DEFAULT NULL,
  `pay_device_type` tinyint(4) DEFAULT NULL,
  `worker_lock` tinyint(4) DEFAULT NULL,
  `worker_mtyun_usenum` int(4) DEFAULT NULL,
  `worker_mtyun_identify` tinyint(4) DEFAULT NULL,
  `worker_lock_2` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cp_workerinfo_copy2` */

DROP TABLE IF EXISTS `cp_workerinfo_copy2`;

CREATE TABLE `cp_workerinfo_copy2` (
  `worker_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '工人ID',
  `worker_name` varchar(45) DEFAULT NULL COMMENT '创建名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `worker_type` int(11) DEFAULT NULL COMMENT '账号类型 1、预定机器人 2、人工  3、支付机器人 5、核验机器人6、取消机器人7、改签机器人、8退票机器人9、查询机器人 10、实名认证 11、查询票价机器人 12、保险 13、删除联系人 14、注册帐号 15、激活帐号',
  `worker_priority` int(11) DEFAULT NULL COMMENT '优先级',
  `order_num` int(11) DEFAULT '0' COMMENT '处理订单数',
  `worker_status` varchar(10) DEFAULT '22' COMMENT '工作状态：00、空闲  11、工作中  22、停用  33 备用   99 需要人工处理  01 在队列中 02 预登录',
  `worker_ext` varchar(150) DEFAULT NULL COMMENT '扩展字段',
  `max_order_num` int(11) DEFAULT '10' COMMENT '最大订单处理数',
  `opt_name` varchar(45) DEFAULT NULL COMMENT '操作人',
  `spare_thread` int(2) DEFAULT '0' COMMENT '机器人空闲进程数',
  `robot_id` varchar(50) DEFAULT NULL COMMENT '机器人ID',
  `robot_con_timeout` varchar(10) DEFAULT NULL COMMENT '连接超时时间',
  `robot_read_timeout` varchar(10) DEFAULT NULL COMMENT '连接读取时间',
  `work_host` varchar(10) DEFAULT NULL COMMENT '机器人host',
  `stop_reason` varchar(10) DEFAULT NULL COMMENT '机器人停用原因11 机器人需要重启22 机器人IP被封33 该支付机器人余额不足 44 其他',
  `proxy_start_time` datetime DEFAULT NULL COMMENT '代理配置成功时间',
  `proxy_status` varchar(11) DEFAULT '22' COMMENT '00、代理重置中 11、代理重置完成',
  `work_time` datetime DEFAULT NULL COMMENT '上一次工作时间',
  `work_num` int(11) DEFAULT '0' COMMENT '机器人使用次数',
  `script_version` int(11) DEFAULT NULL COMMENT '脚本版本',
  `single_order` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否正在订单预订 0、否 1、是',
  `app_valid` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 默认，1 白名单',
  `worker_region` varchar(20) DEFAULT NULL COMMENT '机器区域:1.华北1(青岛)2.华北2(北京)3.华东1(杭州)4.华东2(上海)5.华南1(深圳)',
  `worker_language_type` int(4) DEFAULT '1' COMMENT '机器脚本语言类型： 1：lua脚本  2：java脚本',
  `pay_device_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0--PC端支付  1-APP端支付',
  PRIMARY KEY (`worker_id`),
  KEY `work_time_INDEX` (`worker_type`,`worker_status`,`work_time`)
) ENGINE=InnoDB AUTO_INCREMENT=10128 DEFAULT CHARSET=utf8 COMMENT='工作人员表';

/*Table structure for table `cp_workerinfo_log` */

DROP TABLE IF EXISTS `cp_workerinfo_log`;

CREATE TABLE `cp_workerinfo_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` text COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `worker_id` varchar(50) DEFAULT NULL COMMENT '机器人ID',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2971 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_workerinfo_meit` */

DROP TABLE IF EXISTS `cp_workerinfo_meit`;

CREATE TABLE `cp_workerinfo_meit` (
  `rownum` int(11) NOT NULL AUTO_INCREMENT,
  `robot_name` varchar(255) DEFAULT NULL COMMENT '主机名',
  `status` varchar(255) DEFAULT NULL,
  `system_type` varchar(255) DEFAULT NULL COMMENT '系统',
  `CPU` varchar(255) DEFAULT NULL,
  `RAM` varchar(255) DEFAULT NULL,
  `system_pan` varchar(255) DEFAULT NULL COMMENT '系统盘',
  `data_pan` varchar(255) DEFAULT NULL,
  `cloud_pan` varchar(255) DEFAULT NULL,
  `ip_out` varchar(255) NOT NULL,
  `ip_inner` varchar(255) DEFAULT NULL,
  `kf_type` varchar(255) DEFAULT NULL COMMENT '计费方式',
  `money` varchar(255) NOT NULL COMMENT '原价',
  `robot_create_time` varchar(255) DEFAULT NULL COMMENT '机器购买时间',
  `gq_time` varchar(255) NOT NULL COMMENT '到期时间',
  `zdxufei` varchar(255) DEFAULT NULL COMMENT '自动续费',
  `user_name` varchar(255) DEFAULT NULL,
  `user_pwd` varchar(255) DEFAULT NULL,
  `group` varchar(255) DEFAULT NULL,
  `batch_id` varchar(255) DEFAULT NULL,
  `create_time` date DEFAULT NULL COMMENT '导入日期',
  PRIMARY KEY (`rownum`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

/*Table structure for table `cp_workerinfo_report` */

DROP TABLE IF EXISTS `cp_workerinfo_report`;

CREATE TABLE `cp_workerinfo_report` (
  `report_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `worker_id` int(11) NOT NULL COMMENT '机器人id',
  `worker_name` varchar(45) DEFAULT NULL COMMENT '机器人名称',
  `order_id` varchar(50) NOT NULL COMMENT '订单id',
  `request_time` datetime DEFAULT NULL COMMENT '发送请求时间',
  `release_time` datetime DEFAULT NULL COMMENT '结束释放时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_type` varchar(2) DEFAULT NULL COMMENT '操作类型：1、预订 2、支付',
  PRIMARY KEY (`report_id`),
  KEY `idx_cp_workerinfo_report_worker_id` (`worker_id`),
  KEY `idx_cp_workerinfo_report_order_id` (`order_id`),
  KEY `idx_cp_workerinfo_report_create_time` (`create_time`),
  KEY `idx_cp_workerinfo_report_opt_type` (`opt_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1863498 DEFAULT CHARSET=utf8 COMMENT='机器人信息统计表';

/*Table structure for table `cp_workerinfo_test` */

DROP TABLE IF EXISTS `cp_workerinfo_test`;

CREATE TABLE `cp_workerinfo_test` (
  `worker_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '工人ID',
  `worker_name` varchar(45) DEFAULT NULL COMMENT '创建名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `worker_type` int(11) DEFAULT NULL COMMENT '账号类型 1、预定机器人 2、人工  3、支付机器人 5、核验机器人  6、保险',
  `worker_priority` int(11) DEFAULT NULL COMMENT '优先级',
  `order_num` int(11) DEFAULT '0' COMMENT '处理订单数',
  `worker_status` varchar(10) DEFAULT '22' COMMENT '工作状态：00、工作中  11、空闲  22、停用 33、备用 ',
  `worker_ext` varchar(45) DEFAULT NULL COMMENT '扩展字段',
  `max_order_num` int(11) DEFAULT '10' COMMENT '最大订单处理数',
  `opt_name` varchar(45) DEFAULT NULL COMMENT '操作人',
  `spare_thread` int(2) DEFAULT NULL COMMENT '剩余空闲进程数',
  `robot_id` varchar(50) DEFAULT NULL COMMENT '机器人ID',
  PRIMARY KEY (`worker_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='工作人员表';

/*Table structure for table `cp_workerinfo_update` */

DROP TABLE IF EXISTS `cp_workerinfo_update`;

CREATE TABLE `cp_workerinfo_update` (
  `worker_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '工人ID',
  `worker_name` varchar(45) DEFAULT NULL COMMENT '创建名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `worker_type` int(11) DEFAULT NULL COMMENT '账号类型 1、预定机器人 2、人工  3、支付机器人 5、核验机器人6、取消机器人7、改签机器人、8退票机器人9、查询机器人 10、实名认证 11、查询票价机器人 12、保险 13、删除联系人 14、注册帐号 15、激活帐号  21,携程',
  `worker_priority` int(11) DEFAULT NULL COMMENT '优先级',
  `order_num` int(11) DEFAULT '0' COMMENT '处理订单数',
  `worker_status` varchar(10) DEFAULT '22' COMMENT '工作状态：00、空闲  11、工作中  22、停用  33 备用   99 需要人工处理  01 在队列中 02 预登录',
  `worker_ext` varchar(150) DEFAULT NULL COMMENT '扩展字段',
  `max_order_num` int(11) DEFAULT '10' COMMENT '最大订单处理数',
  `opt_name` varchar(45) DEFAULT NULL COMMENT '操作人',
  `spare_thread` int(2) DEFAULT '0' COMMENT '机器人空闲进程数',
  `robot_id` varchar(50) DEFAULT NULL COMMENT '机器人ID',
  `robot_con_timeout` varchar(10) DEFAULT NULL COMMENT '连接超时时间',
  `robot_read_timeout` varchar(10) DEFAULT NULL COMMENT '连接读取时间',
  `work_host` varchar(10) DEFAULT NULL COMMENT '机器人host',
  `stop_reason` varchar(10) DEFAULT NULL COMMENT '机器人停用原因11 机器人需要重启22 机器人IP被封33 该支付机器人余额不足 44 其他',
  `proxy_start_time` datetime DEFAULT NULL COMMENT '代理配置成功时间',
  `proxy_status` varchar(11) DEFAULT '22' COMMENT '00、代理重置中 11、代理重置完成',
  `work_time` datetime DEFAULT NULL COMMENT '上一次工作时间',
  `work_num` int(11) DEFAULT '0' COMMENT '机器人使用次数',
  `script_version` int(11) DEFAULT NULL COMMENT '脚本版本',
  `single_order` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否正在订单预订 0、否 1、是',
  `app_valid` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 白名单，1 黑名单',
  `worker_region` varchar(20) DEFAULT NULL COMMENT '机器区域 (北京，上海，深圳)',
  PRIMARY KEY (`worker_id`),
  KEY `work_time_INDEX` (`worker_type`,`worker_status`,`work_time`)
) ENGINE=InnoDB AUTO_INCREMENT=880 DEFAULT CHARSET=utf8 COMMENT='工作人员表';

/*Table structure for table `ctrip_accountinfo` */

DROP TABLE IF EXISTS `ctrip_accountinfo`;

CREATE TABLE `ctrip_accountinfo` (
  `ctrip_id` int(11) NOT NULL AUTO_INCREMENT,
  `ctrip_name` varchar(50) NOT NULL COMMENT '登陆名',
  `ctrip_password` varchar(25) DEFAULT NULL COMMENT '登陆密码',
  `ctrip_status` varchar(2) DEFAULT NULL COMMENT '账号状态：00启用 11停用 22-余额不足临时停用 33-携程不买保险订单达上限 44-注册成功绑定礼品卡成功，暂时停用 55-注册成功绑定礼品卡失败，暂时停用 66-注册成功绑定礼品卡成功，查询余额失败，暂时停用',
  `pay_password` varchar(25) DEFAULT NULL COMMENT '支付密码',
  `ctrip_username` varchar(10) DEFAULT NULL COMMENT '用户名',
  `ctrip_phone` varchar(20) DEFAULT NULL COMMENT '绑定手机号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `balance` double(10,2) DEFAULT '0.00' COMMENT '余额',
  `acc_degree` int(2) DEFAULT '9' COMMENT '帐号等级,根据充值确认1:充值100  2:充值500  3:充值1000  4:充值2000  5:充值5000',
  `order_succ_time` int(3) NOT NULL DEFAULT '0' COMMENT '下单成功次数',
  `opt_status` int(2) NOT NULL DEFAULT '0' COMMENT '0-空闲  1- 使用中',
  `opt_person` varchar(25) DEFAULT NULL COMMENT '操作人',
  `ctrip_bx` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否需要买保险：1需要 0不需要',
  `cookie` varchar(1000) DEFAULT NULL COMMENT '携程wap端帐号cookie',
  `cid` varchar(30) DEFAULT NULL COMMENT '携程wap端帐号cid',
  `auth` varchar(80) DEFAULT NULL COMMENT '携程wap端帐号auth',
  `sauth` varchar(80) DEFAULT NULL COMMENT '携程wap端帐号sauth',
  `card_id` varchar(50) DEFAULT NULL COMMENT '礼品卡id',
  `mail_id` varchar(15) DEFAULT NULL COMMENT '邮箱id',
  `result_type` int(2) DEFAULT '0' COMMENT '查询返回结果类型：0：正常   1：账号异常    2：查询超时  ',
  `open_day_count` int(1) DEFAULT NULL COMMENT '开支付密码-当天次数',
  `open_time` datetime DEFAULT NULL COMMENT '开支付密码-操作时间',
  `open_sum_count` int(10) DEFAULT NULL COMMENT '开支付密码-总次数',
  PRIMARY KEY (`ctrip_id`,`ctrip_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10001758 DEFAULT CHARSET=utf8 COMMENT='携程账号信息';

/*Table structure for table `ctrip_accountinfo_log` */

DROP TABLE IF EXISTS `ctrip_accountinfo_log`;

CREATE TABLE `ctrip_accountinfo_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1438 DEFAULT CHARSET=utf8;

/*Table structure for table `ctrip_accountinfo_temp` */

DROP TABLE IF EXISTS `ctrip_accountinfo_temp`;

CREATE TABLE `ctrip_accountinfo_temp` (
  `ctrip_id` int(11) NOT NULL AUTO_INCREMENT,
  `ctrip_name` varchar(50) NOT NULL COMMENT '登陆名',
  `ctrip_password` varchar(25) DEFAULT NULL COMMENT '登陆密码',
  `ctrip_status` varchar(2) DEFAULT NULL COMMENT '账号状态：00启用 11停用 22-余额不足临时停用 33-携程不买保险订单达上限 44-注册成功绑定礼品卡成功，暂时停用 55-注册成功绑定礼品卡失败，暂时停用 66-注册成功绑定礼品卡成功，查询余额失败，暂时停用',
  `pay_password` varchar(25) DEFAULT NULL COMMENT '支付密码',
  `ctrip_username` varchar(10) DEFAULT NULL COMMENT '用户名',
  `ctrip_phone` varchar(20) DEFAULT NULL COMMENT '绑定手机号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `balance` double(10,2) DEFAULT '0.00' COMMENT '余额',
  `acc_degree` int(2) DEFAULT '9' COMMENT '帐号等级,根据充值确认1:充值100  2:充值500  3:充值1000  4:充值2000  5:充值5000',
  `order_succ_time` int(3) NOT NULL DEFAULT '0' COMMENT '下单成功次数',
  `opt_status` int(2) NOT NULL DEFAULT '0' COMMENT '0-空闲  1- 使用中',
  `opt_person` varchar(25) DEFAULT NULL COMMENT '操作人',
  `ctrip_bx` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否需要买保险：1需要 0不需要',
  `cookie` varchar(1000) DEFAULT NULL COMMENT '携程wap端帐号cookie',
  `cid` varchar(30) DEFAULT NULL COMMENT '携程wap端帐号cid',
  `auth` varchar(80) DEFAULT NULL COMMENT '携程wap端帐号auth',
  `sauth` varchar(80) DEFAULT NULL COMMENT '携程wap端帐号sauth',
  `card_id` varchar(50) DEFAULT NULL COMMENT '礼品卡id',
  `mail_id` varchar(15) DEFAULT NULL COMMENT '邮箱id',
  `result_type` int(2) DEFAULT '0' COMMENT '查询返回结果类型：0：正常   1：账号异常    2：查询超时  ',
  `open_day_count` int(1) DEFAULT NULL COMMENT '开支付密码-当天次数',
  `open_time` datetime DEFAULT NULL COMMENT '开支付密码-操作时间',
  `open_sum_count` int(10) DEFAULT NULL COMMENT '开支付密码-总次数',
  PRIMARY KEY (`ctrip_id`,`ctrip_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='携程账号信息';

/*Table structure for table `ctrip_accountinfo_temp_copy` */

DROP TABLE IF EXISTS `ctrip_accountinfo_temp_copy`;

CREATE TABLE `ctrip_accountinfo_temp_copy` (
  `ctrip_id` int(11) NOT NULL AUTO_INCREMENT,
  `ctrip_name` varchar(50) NOT NULL COMMENT '登陆名',
  `ctrip_password` varchar(25) DEFAULT NULL COMMENT '登陆密码',
  `ctrip_status` varchar(2) DEFAULT NULL COMMENT '账号状态：00启用 11停用 22-余额不足临时停用 33-携程不买保险订单达上限 44-注册成功绑定礼品卡成功，暂时停用 55-注册成功绑定礼品卡失败，暂时停用 66-注册成功绑定礼品卡成功，查询余额失败，暂时停用',
  `pay_password` varchar(25) DEFAULT NULL COMMENT '支付密码',
  `ctrip_username` varchar(10) DEFAULT NULL COMMENT '用户名',
  `ctrip_phone` varchar(20) DEFAULT NULL COMMENT '绑定手机号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `balance` double(10,2) DEFAULT '0.00' COMMENT '余额',
  `acc_degree` int(2) DEFAULT '9' COMMENT '帐号等级,根据充值确认1:充值100  2:充值500  3:充值1000  4:充值2000  5:充值5000',
  `order_succ_time` int(3) NOT NULL DEFAULT '0' COMMENT '下单成功次数',
  `opt_status` int(2) NOT NULL DEFAULT '0' COMMENT '0-空闲  1- 使用中',
  `opt_person` varchar(25) DEFAULT NULL COMMENT '操作人',
  `ctrip_bx` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否需要买保险：1需要 0不需要',
  `cookie` varchar(1000) DEFAULT NULL COMMENT '携程wap端帐号cookie',
  `cid` varchar(30) DEFAULT NULL COMMENT '携程wap端帐号cid',
  `auth` varchar(80) DEFAULT NULL COMMENT '携程wap端帐号auth',
  `sauth` varchar(80) DEFAULT NULL COMMENT '携程wap端帐号sauth',
  `card_id` varchar(50) DEFAULT NULL COMMENT '礼品卡id',
  `mail_id` varchar(15) DEFAULT NULL COMMENT '邮箱id',
  `result_type` int(2) DEFAULT '0' COMMENT '查询返回结果类型：0：正常   1：账号异常    2：查询超时  ',
  `open_day_count` int(1) DEFAULT NULL COMMENT '开支付密码-当天次数',
  `open_time` datetime DEFAULT NULL COMMENT '开支付密码-操作时间',
  `open_sum_count` int(10) DEFAULT NULL COMMENT '开支付密码-总次数',
  PRIMARY KEY (`ctrip_id`,`ctrip_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10001757 DEFAULT CHARSET=utf8 COMMENT='携程账号信息';

/*Table structure for table `ctrip_amountarea_conf` */

DROP TABLE IF EXISTS `ctrip_amountarea_conf`;

CREATE TABLE `ctrip_amountarea_conf` (
  `id` int(6) NOT NULL,
  `limit_begin` double(10,2) DEFAULT NULL,
  `limit_end` double(10,2) DEFAULT NULL,
  `acc_degree` int(2) NOT NULL DEFAULT '1' COMMENT '帐号等级,根据充值确认1:充值100  2:充值500  3:充值1000  4:充值2000  5:充值5000',
  `card_amount` double(10,2) DEFAULT NULL COMMENT '礼品卡面额',
  `create_time` datetime DEFAULT NULL,
  `option_time` datetime DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='携程代出票匹配金额表';

/*Table structure for table `ctrip_cardinfo` */

DROP TABLE IF EXISTS `ctrip_cardinfo`;

CREATE TABLE `ctrip_cardinfo` (
  `card_id` int(8) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `ctrip_card_no` varchar(50) NOT NULL COMMENT '礼品卡账号',
  `ctrip_card_pwd` varchar(50) NOT NULL COMMENT '礼品卡密码',
  `ctrip_status` char(2) DEFAULT '00' COMMENT '礼品卡状态：00未使用 11使用中 22已使用 33不能使用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `ctrip_mailinfo` */

DROP TABLE IF EXISTS `ctrip_mailinfo`;

CREATE TABLE `ctrip_mailinfo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mail_account` varchar(60) DEFAULT NULL,
  `mail_pwd` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `mail_status` int(3) DEFAULT '0' COMMENT '0未使用 1取用中 2 已使用',
  `update_time` datetime DEFAULT NULL,
  `is_modify` int(2) DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ctrip_mail` (`mail_account`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1899 DEFAULT CHARSET=utf8;

/*Table structure for table `ctrip_mailinfo20160808` */

DROP TABLE IF EXISTS `ctrip_mailinfo20160808`;

CREATE TABLE `ctrip_mailinfo20160808` (
  `id` int(10) NOT NULL,
  `mail_account` varchar(60) DEFAULT NULL,
  `mail_pwd` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `mail_status` int(3) DEFAULT '0' COMMENT '0未使用 1取用中 2 已使用',
  `update_time` datetime DEFAULT NULL,
  `is_modify` int(2) DEFAULT NULL COMMENT '标识是否修改密码',
  `remark` varchar(50) DEFAULT NULL COMMENT '放原有密码',
  PRIMARY KEY (`id`),
  KEY `idx_ctrip_mail` (`mail_account`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ctrip_orderinfo` */

DROP TABLE IF EXISTS `ctrip_orderinfo`;

CREATE TABLE `ctrip_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '系统订单号',
  `ctrip_order_id` varchar(50) DEFAULT NULL COMMENT '携程订单号',
  `ctrip_id` int(11) DEFAULT NULL COMMENT '携程账号id',
  `go_status` varchar(2) DEFAULT NULL COMMENT '处理状态 00:初始状态 11:请求出票成功 22:请求出票失败 33:出票流程结束 44:切入12306出票 55:开始查询结果',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_ren` varchar(25) DEFAULT NULL COMMENT '操作人',
  `acc_id` int(11) DEFAULT NULL COMMENT '12306账号id',
  `find_time` datetime DEFAULT NULL COMMENT '查询开始时间',
  `query_num` int(11) DEFAULT '0' COMMENT '查询次数',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='携程订单信息表';

/*Table structure for table `ctrip_phonecode` */

DROP TABLE IF EXISTS `ctrip_phonecode`;

CREATE TABLE `ctrip_phonecode` (
  `code_id` int(11) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(20) DEFAULT NULL COMMENT '手机号',
  `phone_code` varchar(200) DEFAULT NULL COMMENT '验证码',
  `create_time` datetime DEFAULT NULL,
  `check_code` varchar(200) DEFAULT NULL COMMENT '完整短信',
  `code_type` varchar(10) DEFAULT NULL COMMENT '短信类型',
  PRIMARY KEY (`code_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `ctrip_phonenum` */

DROP TABLE IF EXISTS `ctrip_phonenum`;

CREATE TABLE `ctrip_phonenum` (
  `phone_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '手机号ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `phone_number` varchar(11) DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`phone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ctrip_price_payinfo` */

DROP TABLE IF EXISTS `ctrip_price_payinfo`;

CREATE TABLE `ctrip_price_payinfo` (
  `check_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ctrip_seq` varchar(50) DEFAULT NULL COMMENT '携程流水号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `order_time` varchar(50) DEFAULT NULL COMMENT '交易时间',
  `card_type` varchar(50) DEFAULT NULL COMMENT '礼品卡类型',
  `trans_type` varchar(50) DEFAULT NULL COMMENT '交易类型',
  `trans_money` decimal(11,2) DEFAULT NULL COMMENT '收支金额',
  `ctrip_type` varchar(50) DEFAULT NULL COMMENT '携程账单类型（11支出，22退还）',
  `ctrip_id` varchar(50) DEFAULT NULL COMMENT '携程账号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `check_seq` varchar(50) DEFAULT NULL COMMENT '单次上传标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `valid_time` varchar(50) DEFAULT NULL COMMENT '有效期',
  `ctrip_remark` varchar(300) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`check_id`),
  KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2247298 DEFAULT CHARSET=utf8 COMMENT='携程对账文件上传表';

/*Table structure for table `ctrip_register_phone` */

DROP TABLE IF EXISTS `ctrip_register_phone`;

CREATE TABLE `ctrip_register_phone` (
  `phone_id` int(11) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `phone_status` varchar(10) DEFAULT NULL COMMENT '0未使用,1使用中,2已使用',
  `phone_source` varchar(20) DEFAULT NULL COMMENT '手机号来源',
  PRIMARY KEY (`phone_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `elong_allchannel_notify` */

DROP TABLE IF EXISTS `elong_allchannel_notify`;

CREATE TABLE `elong_allchannel_notify` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_notify_status` varchar(2) DEFAULT NULL COMMENT '通知出票系统状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `cp_notify_num` int(11) DEFAULT '0' COMMENT '通知出票系统次数',
  `cp_notify_time` datetime DEFAULT NULL COMMENT '通知出票系统时间',
  `cp_notify_finish_time` datetime DEFAULT NULL COMMENT '通知出票系统完成时间',
  `book_notify_status` varchar(2) DEFAULT NULL COMMENT '预订结果通知状态',
  `book_notify_num` int(4) DEFAULT '0' COMMENT '预订结果通知次数',
  `book_notify_time` datetime DEFAULT NULL COMMENT '预订结果通知时间',
  `book_notify_url` varchar(100) DEFAULT NULL COMMENT '预订结果异步回调地址',
  `book_notify_finish_time` datetime DEFAULT NULL COMMENT '预订结果通知结束时间',
  `out_notify_status` varchar(2) DEFAULT NULL COMMENT '通知订单结果状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `out_notify_num` int(11) DEFAULT '0' COMMENT '通知订单结果次数',
  `out_notify_time` datetime DEFAULT NULL COMMENT '通知订单结果时间',
  `out_notify_finish_time` datetime DEFAULT NULL COMMENT '通知订单结果完成时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `channel` varchar(15) DEFAULT NULL COMMENT '渠道名称 elong、tongcheng',
  `out_notify_url` varchar(100) DEFAULT NULL COMMENT '通知订单结果回调地址',
  `cp_notify_level` int(4) DEFAULT NULL COMMENT '通知出票系统优先级 num越大优先级越高',
  PRIMARY KEY (`id`),
  KEY `idx_out_notify_status` (`out_notify_status`),
  KEY `idx_out_notify_num` (`out_notify_num`),
  KEY `idx_out_notify_time` (`out_notify_time`),
  KEY `IND_elong_allchannel_notify_book_notify_status_book_notify_num` (`book_notify_status`,`book_notify_num`)
) ENGINE=InnoDB AUTO_INCREMENT=13789 DEFAULT CHARSET=utf8 COMMENT='所有渠道订单通知表';

/*Table structure for table `elong_billinfo` */

DROP TABLE IF EXISTS `elong_billinfo`;

CREATE TABLE `elong_billinfo` (
  `bill_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '结算id',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306取票单号',
  `amount` decimal(11,3) DEFAULT NULL COMMENT '结算金额',
  `settlement_type` tinyint(2) DEFAULT NULL COMMENT '结算类型 1：出票成功；2：出票不成功；3：线上退票；4：线下退票；5：改签退票；6：赔偿退款；7：异常扣款；8：异常加款；9：手续费；10：改签扣款；11：线下改签；12：财务充值；13：期末余额；14：期初余额；',
  `quantity` tinyint(2) DEFAULT NULL COMMENT '票数',
  `trade_date` datetime DEFAULT NULL COMMENT '交易时间',
  `settlement_date` date DEFAULT NULL COMMENT '结算归属日期',
  `channel` varchar(25) DEFAULT '10' COMMENT '供应商 10、酷游',
  `account_balance` decimal(11,3) DEFAULT NULL COMMENT '余额',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态: 00、未通知 11、正在通知 22、通知完成 33、通知失败',
  `notify_num` tinyint(2) DEFAULT '0' COMMENT '通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(30) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`bill_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_notify_status` (`notify_status`),
  KEY `idx_notify_status_num` (`notify_status`,`notify_num`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=30023 DEFAULT CHARSET=utf8 COMMENT='同程结算信息表';

/*Table structure for table `elong_billinfo_logs` */

DROP TABLE IF EXISTS `elong_billinfo_logs`;

CREATE TABLE `elong_billinfo_logs` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `bill_id` int(11) NOT NULL COMMENT '结算记录id',
  `order_id` varchar(50) NOT NULL COMMENT '订单id',
  `content` varchar(500) DEFAULT NULL COMMENT '操作日志',
  `opt_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_bill_id` (`bill_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=60340 DEFAULT CHARSET=utf8 COMMENT='同程结算信息日志表';

/*Table structure for table `elong_change_cp` */

DROP TABLE IF EXISTS `elong_change_cp`;

CREATE TABLE `elong_change_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '旧车票ID',
  `new_cp_id` varchar(50) DEFAULT NULL COMMENT '新车票ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单ID',
  `change_id` int(11) NOT NULL COMMENT '改签ID',
  `buy_money` decimal(11,2) DEFAULT '0.00' COMMENT '成本价格',
  `fee` decimal(11,2) DEFAULT NULL COMMENT '改签手续费',
  `diffrate` decimal(3,2) DEFAULT '0.00' COMMENT '差额退款费率',
  `change_buy_money` decimal(11,1) DEFAULT '0.0' COMMENT '改签后成本价格',
  `tc_seat_type` varchar(2) DEFAULT NULL COMMENT '同城坐席类型',
  `seat_type` varchar(11) DEFAULT NULL COMMENT '坐席类型',
  `change_seat_type` varchar(11) DEFAULT NULL COMMENT '改签后坐席类型：',
  `tc_ticket_type` varchar(2) DEFAULT NULL COMMENT '同城车票类型',
  `ticket_type` varchar(2) DEFAULT NULL COMMENT '车票类型：0、成人票, 1、儿童票',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `change_train_box` varchar(10) DEFAULT NULL COMMENT '改签后车厢',
  `tc_change_seat_type` varchar(2) DEFAULT NULL COMMENT '同城改签后坐席类型',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `change_seat_no` varchar(20) DEFAULT NULL COMMENT '改签后座位号',
  `tc_ids_type` varchar(2) DEFAULT NULL COMMENT '同程证件类型',
  `tn_seat_type` varchar(2) DEFAULT NULL COMMENT '途牛坐席类型',
  `tn_ticket_type` varchar(2) DEFAULT NULL COMMENT '途牛车票类型',
  `tn_change_seat_type` varchar(2) DEFAULT NULL COMMENT '途牛改签后坐席类型',
  `tn_ids_type` varchar(2) DEFAULT NULL COMMENT '途牛证件类型',
  `ids_type` varchar(2) DEFAULT NULL COMMENT '证件类型:1、一代身份证 2、二代身份证 3、港澳通行证 4、台湾通行证 5、护照',
  `user_ids` varchar(32) DEFAULT NULL COMMENT '证件号码',
  `user_name` varchar(32) DEFAULT NULL COMMENT '乘客姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_changed` varchar(4) DEFAULT NULL COMMENT '是否改签过 Y、是  N。否',
  `mt_change_seat_type` varchar(2) DEFAULT NULL COMMENT '美团改签后座位类型： 美团座位类型 :1硬座 2硬卧上 3硬卧中 4硬卧下 5软座 6软卧上 7软卧中 8软卧下 9商务座 10观光座 11一等包座 12特等座 13一等座 14二等座 15高级软卧上 16高级软卧下 17无座 18一人软包 10动卧 21高级动卧 22包厢硬卧 ',
  PRIMARY KEY (`cp_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_change_id` (`change_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='同程改签车票表';

/*Table structure for table `elong_change_cp_0928` */

DROP TABLE IF EXISTS `elong_change_cp_0928`;

CREATE TABLE `elong_change_cp_0928` (
  `cp_id` varchar(50) NOT NULL COMMENT '旧车票ID',
  `new_cp_id` varchar(50) DEFAULT NULL COMMENT '新车票ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单ID',
  `change_id` int(11) NOT NULL COMMENT '改签ID',
  `buy_money` decimal(11,2) DEFAULT '0.00' COMMENT '成本价格',
  `change_buy_money` decimal(11,1) DEFAULT '0.0' COMMENT '改签后成本价格',
  `seat_type` varchar(11) DEFAULT NULL COMMENT '坐席类型',
  `change_seat_type` varchar(11) DEFAULT NULL COMMENT '改签后坐席类型：',
  `ticket_type` varchar(2) DEFAULT NULL COMMENT '车票类型：0、成人票, 1、儿童票',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `change_train_box` varchar(10) DEFAULT NULL COMMENT '改签后车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `change_seat_no` varchar(20) DEFAULT NULL COMMENT '改签后座位号',
  `ids_type` varchar(2) DEFAULT NULL COMMENT '证件类型:1、一代身份证 2、二代身份证 3、港澳通行证 4、台湾通行证 5、护照',
  `user_ids` varchar(32) DEFAULT NULL COMMENT '证件号码',
  `user_name` varchar(32) DEFAULT NULL COMMENT '乘客姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_changed` varchar(4) DEFAULT NULL COMMENT '是否改签过 Y、是  N。否',
  `tc_seat_type` varchar(2) DEFAULT NULL COMMENT '同程坐席类别',
  `tc_ticket_type` varchar(2) DEFAULT NULL COMMENT '同程车票类型',
  `tc_change_seat_type` varchar(2) DEFAULT NULL COMMENT '同程改签后坐席类别',
  `tc_ids_type` varchar(2) DEFAULT NULL COMMENT '同程证件类型',
  `tn_seat_type` varchar(2) DEFAULT NULL COMMENT '途牛坐席类型',
  `tn_ticket_type` varchar(2) DEFAULT NULL COMMENT '途牛车票类型',
  `tn_change_seat_type` varchar(2) DEFAULT NULL COMMENT '途牛改签后坐席类型',
  `tn_ids_type` varchar(2) DEFAULT NULL COMMENT '途牛证件类型',
  `mt_change_seat_type` varchar(2) DEFAULT NULL COMMENT '美团改签后座位类型： 美团座位类型 :1硬座 2硬卧上 3硬卧中 4硬卧下 5软座 6软卧上 7软卧中 8软卧下 9商务座 10观光座 11一等包座 12特等座 13一等座 14二等座 15高级软卧上 16高级软卧下 17无座 18一人软包 10动卧 21高级动卧 22包厢硬卧 ',
  `fee` decimal(11,2) DEFAULT NULL COMMENT '改签手续费',
  `diffrate` decimal(3,2) DEFAULT NULL COMMENT '差额退款费率',
  PRIMARY KEY (`cp_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_change_id` (`change_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='同程改签车票表';

/*Table structure for table `elong_change_logs` */

DROP TABLE IF EXISTS `elong_change_logs`;

CREATE TABLE `elong_change_logs` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) NOT NULL COMMENT '订单id',
  `change_id` int(11) DEFAULT NULL COMMENT '改签id',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '原车票id',
  `content` varchar(500) DEFAULT NULL COMMENT '操作日志',
  `opt_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_change_id` (`change_id`),
  KEY `idx_cp_id` (`cp_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=814 DEFAULT CHARSET=utf8 COMMENT='同程改签日志表';

/*Table structure for table `elong_excel` */

DROP TABLE IF EXISTS `elong_excel`;

CREATE TABLE `elong_excel` (
  `excel_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `excel_url` varchar(200) DEFAULT NULL COMMENT '下载地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `excel_name` varchar(50) DEFAULT NULL COMMENT '表格名称',
  `excel_channel` varchar(10) DEFAULT NULL COMMENT '渠道',
  `excel_type` varchar(5) DEFAULT NULL COMMENT '11艺龙预订22艺龙退款33同程',
  PRIMARY KEY (`excel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4546 DEFAULT CHARSET=utf8;

/*Table structure for table `elong_goback_notify` */

DROP TABLE IF EXISTS `elong_goback_notify`;

CREATE TABLE `elong_goback_notify` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '通知艺龙订单回库：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_num` int(11) DEFAULT '0' COMMENT '通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '开始通知时间',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COMMENT='艺龙火车票订单通知表';

/*Table structure for table `elong_orderinfo` */

DROP TABLE IF EXISTS `elong_orderinfo`;

CREATE TABLE `elong_orderinfo` (
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '订单号',
  `order_name` varchar(20) DEFAULT NULL COMMENT '订单名',
  `order_level` varchar(2) DEFAULT NULL COMMENT '订单等级',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `order_status` varchar(10) DEFAULT '00' COMMENT '订单状态: 00下单成功 11通知出票成功 22预订成功 33出票成功 44出票失败   51撤销中 52撤销失败 88 超时订单 24 取消成功',
  `notice_status` varchar(10) DEFAULT '00' COMMENT '通知状态:00、准备通知  11、开始通知  22、通知完成  33、通知失败',
  `order_time` datetime DEFAULT NULL COMMENT '用户下单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `out_fail_reason` varchar(2) DEFAULT NULL COMMENT '出票失败原因：1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符',
  `train_no` varchar(25) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_date` date DEFAULT NULL COMMENT '乘车时间',
  `elong_seat_type` varchar(2) DEFAULT NULL COMMENT 'elong座位类型 :0站票1硬座2软座3硬卧4软卧5高级软卧6一等软座7二等软座8商务座9一等座10二等座11特等座12观光座13特等软座14一人软包',
  `seat_type` varchar(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `passenger_reason` varchar(500) DEFAULT NULL COMMENT '乘客错误信息',
  `ext_field1` varchar(100) DEFAULT '1' COMMENT '备选1(备选坐席)',
  `ext_field2` varchar(100) DEFAULT NULL COMMENT '备选2',
  `ticket_num` int(2) DEFAULT NULL COMMENT '车票内订单数量',
  `channel` varchar(15) DEFAULT NULL COMMENT '订单渠道 elong 、tongcheng',
  `order_type` varchar(4) DEFAULT NULL COMMENT '订单类型  11、先预订后支付 22 先支付后预订',
  `pay_limit_time` datetime DEFAULT NULL COMMENT '支付限制时间',
  `wait_for_order` varchar(2) DEFAULT NULL COMMENT '12306异常是否继续等待出票 11：继续等待 00：不继续等待',
  `serialnumber` varchar(50) DEFAULT NULL COMMENT '订单流水号 ',
  `isChooseSeats` tinyint(1) DEFAULT NULL COMMENT '是否选座, 1:选, 0:非选',
  `chooseSeats` varchar(50) DEFAULT NULL COMMENT '选座信息(选座个数要和乘客数量一致) 例如：1F2F',
  PRIMARY KEY (`order_id`),
  KEY `IND_elong_orderinfo_create_time_order_status` (`create_time`,`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='艺龙火车票订单表';

/*Table structure for table `elong_orderinfo_change` */

DROP TABLE IF EXISTS `elong_orderinfo_change`;

CREATE TABLE `elong_orderinfo_change` (
  `change_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '改签ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单ID',
  `order_status` char(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `old_ticket_change_serial` varchar(35) DEFAULT NULL COMMENT '退还原票票款记录的同程资金变动流水号(改签新票款大于原票款)',
  `new_ticket_change_serial` varchar(35) DEFAULT NULL COMMENT '收取新票票款记录的同程资金变动流水号(改签新票款大于原票款)',
  `ticket_price_diff_change_serial` varchar(35) DEFAULT NULL COMMENT '退还票款差价记录的同程资金变动流水号(改签新票款小于原票款)',
  `change_diff_money` decimal(11,1) DEFAULT NULL COMMENT '改签差额(新票小于旧票)',
  `change_refund_money` decimal(11,1) DEFAULT NULL COMMENT '退还旧票金额(新票大于旧票)',
  `diffrate` decimal(11,2) DEFAULT '0.00' COMMENT '改签差额手续费率',
  `totalpricediff` decimal(11,2) DEFAULT '0.00' COMMENT '改签实际差额退款金额',
  `fee` decimal(11,2) DEFAULT '0.00' COMMENT '改签差额手续费',
  `change_receive_money` decimal(11,1) DEFAULT NULL COMMENT '收取新票金额(新票大于旧票)',
  `book_ticket_time` datetime DEFAULT NULL COMMENT '改签车票预订时间',
  `fail_msg` varchar(256) DEFAULT NULL COMMENT '预订失败原因 描述',
  `fail_reason` varchar(256) DEFAULT NULL COMMENT '预订失败原因 code',
  `train_no` varchar(20) DEFAULT NULL COMMENT '出发车次',
  `change_train_no` varchar(20) DEFAULT NULL COMMENT '改签后出发车次',
  `from_time` datetime DEFAULT NULL COMMENT '发车时间',
  `change_from_time` datetime DEFAULT NULL COMMENT '改签后发车时间',
  `change_to_time` datetime DEFAULT NULL COMMENT '改签后到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `bank_pay_seq` varchar(50) DEFAULT NULL COMMENT '银行支付流水号',
  `worker_id` int(11) DEFAULT NULL COMMENT '处理人账号',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `account_id` varchar(45) DEFAULT NULL COMMENT '出票账号id',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `change_status` varchar(2) DEFAULT NULL COMMENT '改签状态：11、改签预订 12、正在改签预订 13、人工改签预订 14、改签成功等待确认 15、改签预订失败 21、改签取消 22、正在取消 23、取消成功 24、取消失败 31、开始支付 32、正在支付 33、人工支付 34、支付成功 35、支付失败 36、补价支付 37、改签超时(途牛)',
  `isasync` varchar(2) DEFAULT 'N' COMMENT '是否异步改签',
  `callbackurl` varchar(256) DEFAULT NULL COMMENT '异步回调地址',
  `reqtoken` varchar(55) DEFAULT NULL COMMENT '请求特征值',
  `change_travel_time` datetime DEFAULT NULL COMMENT '改签后乘车日期',
  `change_notify_status` varchar(4) DEFAULT NULL COMMENT '回调状态:000、准备回调 111、开始回调 222、回调完成 333、回调失败',
  `change_notify_count` int(11) DEFAULT NULL COMMENT '回调次数',
  `change_notify_time` datetime DEFAULT NULL COMMENT '回调开始时间',
  `change_notify_finish_time` datetime DEFAULT NULL COMMENT '回调完成时间',
  `from_station_code` varchar(10) DEFAULT NULL,
  `to_station_code` varchar(10) DEFAULT NULL,
  `ischangeto` int(1) NOT NULL DEFAULT '0' COMMENT '1:变更到站 0：改签',
  `merchant_id` varchar(50) DEFAULT NULL COMMENT '合作商户编号',
  `hasSeat` int(1) DEFAULT NULL COMMENT '改签是否接受无座票  1、不改到无座票 0、允许改到无座票',
  `alter_pay_type` tinyint(1) DEFAULT NULL COMMENT '改签支付类型    1：平改   2：高改低   3：低改高',
  `mt_change_id` varchar(20) DEFAULT NULL COMMENT '美团改签id',
  `pay_limit_time` datetime DEFAULT NULL COMMENT '支付截止时间',
  `serialnumber` varchar(50) DEFAULT NULL COMMENT '同城订单流水号',
  `isChooseSeats` tinyint(1) DEFAULT '0' COMMENT '是否选座, 1:选, 0:非选',
  `chooseSeats` varchar(50) DEFAULT NULL COMMENT '选座信息(选座个数要和乘客数量一致) 例如：1F2F',
  PRIMARY KEY (`change_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_change_notify_status` (`change_notify_status`),
  KEY `idx_change_status` (`change_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `order_status` (`order_status`),
  CONSTRAINT `elong_orderinfo_change_ibfk_1` FOREIGN KEY (`order_status`) REFERENCES `order_status` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=487619 DEFAULT CHARSET=utf8 COMMENT='同程改签记录表';

/*Table structure for table `elong_orderinfo_cp` */

DROP TABLE IF EXISTS `elong_orderinfo_cp`;

CREATE TABLE `elong_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `elong_ticket_type` varchar(2) DEFAULT NULL COMMENT 'elong车票类型0儿童票1成人票2学生票',
  `ticket_type` varchar(2) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `elong_ids_type` varchar(2) DEFAULT NULL COMMENT 'elong证件类型:1身份证 C港澳通行证 G台湾通行证 B护照',
  `ids_type` varchar(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `elong_seat_type` varchar(2) DEFAULT NULL COMMENT '下单坐席类型 elong座位类型 :0站票1硬座2软座3硬卧4软卧5高级软卧6一等软座7二等软座8商务座9一等座10二等座11特等座12观光座13特等软座14一人软包   ',
  `seat_type` varchar(11) DEFAULT NULL COMMENT '实际坐席类型 座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '00:无退票，11:退票中 22：拒绝退票 33：退票成功，44：退票失败',
  `refund_fail_reason` varchar(200) DEFAULT NULL COMMENT '退款失败原因/拒绝退票原因',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306订单号',
  `alter_seat_type` varchar(2) DEFAULT NULL COMMENT '改签后坐席',
  `alter_train_box` varchar(5) DEFAULT NULL COMMENT '改签后车厢',
  `alter_seat_no` varchar(10) DEFAULT NULL COMMENT '改签后座位号',
  `alter_buy_money` decimal(11,3) DEFAULT NULL COMMENT '改签后成本价格',
  `alter_train_no` varchar(10) DEFAULT NULL COMMENT '改签后车次',
  `alter_money` decimal(11,3) DEFAULT NULL COMMENT '改签差额',
  `refund_12306_money` decimal(11,3) DEFAULT NULL COMMENT '12306退款金额',
  `out_passengerid` varchar(20) DEFAULT NULL COMMENT '同程车票序号',
  PRIMARY KEY (`cp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='elong火车票订购信息表';

/*Table structure for table `elong_orderinfo_logs` */

DROP TABLE IF EXISTS `elong_orderinfo_logs`;

CREATE TABLE `elong_orderinfo_logs` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `content` varchar(500) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `opt_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `order_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39936625 DEFAULT CHARSET=utf8 COMMENT='elong火车票订单日志表';

/*Table structure for table `elong_orderinfo_notify` */

DROP TABLE IF EXISTS `elong_orderinfo_notify`;

CREATE TABLE `elong_orderinfo_notify` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_notify_status` varchar(2) DEFAULT NULL COMMENT '通知出票系统状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `cp_notify_num` int(11) DEFAULT '0' COMMENT '通知出票系统次数',
  `cp_notify_time` datetime DEFAULT NULL COMMENT '开始通知出票系统时间',
  `cp_notify_finish_time` datetime DEFAULT NULL COMMENT '通知出票系统完成时间',
  `out_notify_status` varchar(2) DEFAULT NULL COMMENT '通知出票状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `out_notify_num` int(11) DEFAULT '0' COMMENT '通知出票次数',
  `out_notify_time` datetime DEFAULT NULL COMMENT '开始通知出票时间',
  `out_notify_finish_time` datetime DEFAULT NULL COMMENT '通知出票完成时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_out_notify_status` (`out_notify_status`),
  KEY `idx_out_notify_num` (`out_notify_num`),
  KEY `idx_out_notify_time` (`out_notify_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3921733 DEFAULT CHARSET=utf8 COMMENT='艺龙火车票订单通知表';

/*Table structure for table `elong_orderinfo_refundstream` */

DROP TABLE IF EXISTS `elong_orderinfo_refundstream`;

CREATE TABLE `elong_orderinfo_refundstream` (
  `stream_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号',
  `refund_money` decimal(11,3) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,3) DEFAULT NULL COMMENT '实际退款金额',
  `alter_tickets_money` decimal(11,3) DEFAULT NULL COMMENT '改签差价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注/出票失败原因',
  `our_remark` varchar(200) DEFAULT '' COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refuse_reason` varchar(2) DEFAULT NULL COMMENT '拒绝退款 原因：1、已取票2、已过时间 3、来电取消',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票   05：重新机器退票06：开始机器退票   07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款  55:预退票 72：生成线下退款 73：用户提交线下退款 71：线下退款通知中 81 车站退票',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` varchar(3) DEFAULT '0' COMMENT '通知次数',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败  ',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `change_ticket_info` varchar(200) DEFAULT NULL COMMENT '改签明细',
  `detail_refund` varchar(100) DEFAULT NULL COMMENT '12306详细退款金额',
  `detail_alter_tickets` varchar(100) DEFAULT NULL COMMENT '详细改签差额的金额',
  `refund_type` varchar(2) DEFAULT NULL COMMENT '退款类型 11、退票退款 22、线下退款（人工退款）33 车站退票 44 申请退款 55改签退票',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志',
  `channel` varchar(20) DEFAULT NULL COMMENT '退款渠道 elong tongcheng',
  `callbackurl` varchar(200) DEFAULT NULL COMMENT '退票结果回传地址',
  `reqtoken` varchar(50) DEFAULT NULL COMMENT '针对同城的请求特征 回置值',
  `user_time` datetime DEFAULT NULL COMMENT '用户退款时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`stream_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_refund_seq` (`refund_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=1710565 DEFAULT CHARSET=utf8 COMMENT='艺龙退款流水表';

/*Table structure for table `elong_orderinfo_returnnotify` */

DROP TABLE IF EXISTS `elong_orderinfo_returnnotify`;

CREATE TABLE `elong_orderinfo_returnnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `order_status` varchar(2) DEFAULT '33' COMMENT '订单状态 ：33、出票成功 44、出票失败',
  `beiyong1` varchar(45) DEFAULT NULL COMMENT '备用字段1',
  `beiyong2` varchar(45) DEFAULT NULL COMMENT '备用字段2',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='elong订单处理结果通知表';

/*Table structure for table `elong_passenger_change` */

DROP TABLE IF EXISTS `elong_passenger_change`;

CREATE TABLE `elong_passenger_change` (
  `pc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '常旅变更id',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `channel` varchar(25) DEFAULT 'tongcheng' COMMENT '渠道 tongcheng、同程',
  `account_name` varchar(45) NOT NULL COMMENT '账号名',
  `cp_infos` varchar(255) NOT NULL COMMENT '常旅变更信息 例：身份证号|姓名|操作编号 1、新增 2、删除 3、修改',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态: 00、未通知 11、正在通知 22、通知完成 33、通知失败',
  `notify_num` tinyint(2) DEFAULT '0' COMMENT '通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(30) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`pc_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_notify_status` (`notify_status`),
  KEY `idx_notify_status_num` (`notify_status`,`notify_num`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=7741 DEFAULT CHARSET=utf8 COMMENT='同程常旅信息变更通知表';

/*Table structure for table `elong_refundstation` */

DROP TABLE IF EXISTS `elong_refundstation`;

CREATE TABLE `elong_refundstation` (
  `station_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `order_status` varchar(20) DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8 COMMENT='手动导入订单表';

/*Table structure for table `elong_refundstation_tj` */

DROP TABLE IF EXISTS `elong_refundstation_tj`;

CREATE TABLE `elong_refundstation_tj` (
  `station_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `count` int(11) DEFAULT NULL COMMENT '总数',
  `success_num` int(11) DEFAULT NULL COMMENT '符合条件数',
  `fail_num` int(11) DEFAULT NULL COMMENT '已退款数',
  `again_num` int(11) DEFAULT NULL COMMENT '重复数据数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='手动导入订单统计表';

/*Table structure for table `elong_remedy_notify` */

DROP TABLE IF EXISTS `elong_remedy_notify`;

CREATE TABLE `elong_remedy_notify` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_num` int(11) DEFAULT '0' COMMENT '通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `create_time` datetime DEFAULT NULL COMMENT '通知条目创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='补单通知表';

/*Table structure for table `elong_system_log` */

DROP TABLE IF EXISTS `elong_system_log`;

CREATE TABLE `elong_system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;

/*Table structure for table `elong_system_setting` */

DROP TABLE IF EXISTS `elong_system_setting`;

CREATE TABLE `elong_system_setting` (
  `setting_id` varchar(150) DEFAULT NULL,
  `setting_name` varchar(150) DEFAULT NULL,
  `setting_value` varchar(6000) DEFAULT NULL,
  `setting_status` varchar(6) DEFAULT NULL COMMENT '状态 0:关闭 1:启用',
  `setting_desc` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ext_callbackurl` */

DROP TABLE IF EXISTS `ext_callbackurl`;

CREATE TABLE `ext_callbackurl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `merchant_id` varchar(10) NOT NULL COMMENT '商户编号',
  `refund_callbackurl` varchar(100) NOT NULL COMMENT '退票通知，回调地址,预先存入',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='商户车站退票，推送的回调地址';

/*Table structure for table `ext_fee_model` */

DROP TABLE IF EXISTS `ext_fee_model`;

CREATE TABLE `ext_fee_model` (
  `merchant_id` varchar(50) NOT NULL COMMENT '合作商户编号',
  `fee_type` varchar(20) DEFAULT NULL COMMENT '收费类型：order:订单收费；ticket：票数收费；echelon1：梯次1收费；echelon2：梯次2收费；percent：百分比收费',
  `ticket_fee` decimal(3,2) DEFAULT '0.00' COMMENT '单张票收费金额',
  `echelon_zeo` decimal(3,2) DEFAULT '0.00' COMMENT '第一梯次收费金额',
  `echelon_fir` varchar(10) DEFAULT NULL COMMENT '第二梯次收费格式： 1000（票数）: 0.8(手续费)',
  `echelon_sec` varchar(10) DEFAULT NULL COMMENT '第三梯次收费格式： 2000（票数）: 0.8(手续费)',
  `percent_fee` decimal(3,2) DEFAULT '0.00' COMMENT '收费百分比率',
  `order_fee` decimal(3,2) DEFAULT '0.00' COMMENT '订单收费金额',
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='19trip合作商户扣费模式信息';

/*Table structure for table `ext_merchant_log` */

DROP TABLE IF EXISTS `ext_merchant_log`;

CREATE TABLE `ext_merchant_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `merchant_id` varchar(50) DEFAULT NULL COMMENT '合作商户编号',
  `merchant_name` varchar(100) DEFAULT NULL COMMENT '合作商户名称',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8;

/*Table structure for table `ext_merchantinfo` */

DROP TABLE IF EXISTS `ext_merchantinfo`;

CREATE TABLE `ext_merchantinfo` (
  `merchant_id` varchar(50) NOT NULL COMMENT '合作商户编号',
  `sign_key` varchar(50) DEFAULT NULL COMMENT '合作商户密钥',
  `merchant_name` varchar(100) DEFAULT NULL COMMENT '合作商户名称',
  `merchant_version` varchar(10) DEFAULT NULL COMMENT '合作商户使用接口的版本号',
  `merchant_terminal` varchar(20) DEFAULT NULL COMMENT '合作商户请求终端',
  `pay_order_fee` decimal(3,3) DEFAULT '0.000' COMMENT '平台支付手续费',
  `pay_type` varchar(2) DEFAULT '11' COMMENT '支付方式：11、自动代扣；22、商户自行扣费; 33、支付宝代扣；44、支付宝链接',
  `check_left_ticket` varchar(2) DEFAULT '00' COMMENT '是否启用校验余票接口00：禁用；11：启用',
  `bx_10_fee` varchar(5) DEFAULT '0' COMMENT '10元保险扣费金额',
  `bx_20_fee` varchar(5) DEFAULT '0' COMMENT '20原保险扣费金额',
  `merchant_fee` varchar(15) DEFAULT '0' COMMENT '单张车票手续费',
  `bx_company` varchar(2) DEFAULT NULL COMMENT '保险单位：1、快保；2、合众',
  `sms_channel` varchar(2) DEFAULT NULL COMMENT '短信渠道：00、19e；11、鼎鑫移动；22、企信通；88、商户自发短信',
  `spare_ticket_amount` varchar(3) DEFAULT NULL COMMENT '余票阀值：当查询出的余票小于等于该值时，显示无票',
  `stop_buyTicket_time` varchar(4) DEFAULT NULL COMMENT '开车之前的停止购票时间（3~~6小时）',
  `merchant_stop_reason` varchar(200) DEFAULT NULL COMMENT '商户停用原因',
  `merchant_status` varchar(2) DEFAULT NULL COMMENT '商户状态：00、停用 11、启用',
  `verify_status` varchar(2) DEFAULT '00' COMMENT '验证状态：00、停用 11、启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(20) DEFAULT NULL COMMENT '操作人',
  `agent_id` varchar(50) DEFAULT NULL COMMENT '分配的代理商ID',
  `agent_name` varchar(15) DEFAULT NULL COMMENT '代理商登陆账号',
  `ticket_num` int(6) DEFAULT '0' COMMENT '购票张数（天）',
  `ticket_total_num` int(15) DEFAULT '0' COMMENT '购票总数',
  `verify_total_num` int(15) DEFAULT '0' COMMENT '验证总数',
  `book_limit_time` int(3) DEFAULT '20' COMMENT '默认限制时间20分钟',
  `mid_pay` varchar(2) DEFAULT '00' COMMENT '00、不可支付； 11、可以支付',
  `book_flow` varchar(2) DEFAULT '00' COMMENT '00:先支付后出票；11：先预定后支付',
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='19trip合作商户信息';

/*Table structure for table `ext_merchantinfo_1` */

DROP TABLE IF EXISTS `ext_merchantinfo_1`;

CREATE TABLE `ext_merchantinfo_1` (
  `merchant_id` varchar(150) DEFAULT NULL,
  `sign_key` varchar(150) DEFAULT NULL,
  `merchant_name` varchar(300) DEFAULT NULL,
  `merchant_version` varchar(30) DEFAULT NULL,
  `merchant_terminal` varchar(60) DEFAULT NULL,
  `pay_order_fee` decimal(5,0) DEFAULT NULL,
  `pay_type` varchar(6) DEFAULT NULL,
  `check_left_ticket` varchar(6) DEFAULT NULL,
  `bx_10_fee` varchar(15) DEFAULT NULL,
  `bx_20_fee` varchar(15) DEFAULT NULL,
  `merchant_fee` varchar(45) DEFAULT NULL,
  `bx_company` varchar(6) DEFAULT NULL,
  `sms_channel` varchar(6) DEFAULT NULL,
  `spare_ticket_amount` varchar(9) DEFAULT NULL,
  `stop_buyTicket_time` varchar(6) DEFAULT NULL,
  `merchant_stop_reason` varchar(600) DEFAULT NULL,
  `merchant_status` varchar(6) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `opt_time` datetime DEFAULT NULL,
  `opt_person` varchar(60) DEFAULT NULL,
  `agent_id` varchar(150) DEFAULT NULL,
  `agent_name` varchar(45) DEFAULT NULL,
  `ticket_num` int(6) DEFAULT NULL,
  `ticket_total_num` int(15) DEFAULT NULL,
  `book_limit_time` int(3) DEFAULT NULL,
  `mid_pay` varchar(6) DEFAULT NULL,
  `book_flow` varchar(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ext_noticeinfo` */

DROP TABLE IF EXISTS `ext_noticeinfo`;

CREATE TABLE `ext_noticeinfo` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `notice_name` varchar(45) DEFAULT NULL COMMENT '通知名称',
  `notice_status` varchar(10) DEFAULT NULL COMMENT '通知状态 00、未发布 11、已发布  22、已到期',
  `notice_content` varchar(8000) DEFAULT NULL COMMENT '通知内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pub_time` datetime DEFAULT NULL COMMENT '生效时间',
  `stop_time` datetime DEFAULT NULL COMMENT '到期时间',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '创建人',
  `ext_channel` varchar(200) DEFAULT NULL COMMENT '渠道',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='对外商户公告表';

/*Table structure for table `ext_orderinfo` */

DROP TABLE IF EXISTS `ext_orderinfo`;

CREATE TABLE `ext_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格(sum_amount)',
  `pay_money_show` decimal(11,2) DEFAULT NULL COMMENT '用户支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `ticket_pay_money` decimal(11,2) DEFAULT NULL COMMENT '票价总额',
  `bx_pay_money` decimal(11,2) DEFAULT '0.00' COMMENT '保险总额',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 00、未支付 11、支付成功 12、eop发货成功 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 ',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `from_station` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `arrive_station` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` varchar(10) DEFAULT NULL COMMENT '出发时间',
  `arrive_time` varchar(10) DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `refund_deadline_ignore` varchar(2) DEFAULT NULL COMMENT '1：无视截止时间（发车12小时后为退款截止时间）',
  `refund_total` decimal(11,2) DEFAULT NULL COMMENT '退款金额总计',
  `refund_status` varchar(20) DEFAULT 'NO_RFUND' COMMENT '退款状态：NO_RFUND:无退票 ；REFUNDING:退票中；PART_REFUND:部分退票完成；REFUND_FINISH:退款完成；REFUSE_REFUND:拒绝退款',
  `can_refund` varchar(2) DEFAULT NULL COMMENT '是否可以退款（默认可以退 0:不能退 1:能退）',
  `sms_notify` varchar(1) DEFAULT NULL COMMENT '是否短信通知  1：是；0:否',
  `ext_seat` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `merchant_order_id` varchar(50) DEFAULT '' COMMENT '合作商户订单id',
  `order_level` varchar(10) DEFAULT '' COMMENT '订单级别：0：普通订单；1：VIP订单',
  `merchant_id` varchar(50) DEFAULT '' COMMENT '合作商户编号',
  `link_name` varchar(100) DEFAULT '' COMMENT '短信通知联系人',
  `link_phone` varchar(20) DEFAULT '' COMMENT '短信通知号码',
  `order_result_url` varchar(200) DEFAULT '' COMMENT '订单处理结果通知合作商户url',
  `order_pro1` varchar(30) DEFAULT '' COMMENT '备用字段1',
  `order_pro2` varchar(30) DEFAULT '' COMMENT '备用字段2',
  `eop_order_id` varchar(45) DEFAULT NULL COMMENT 'EOP订单号',
  `nopwd_pay_url` varchar(200) DEFAULT NULL COMMENT 'EOP提供无密支付接口地址',
  `eop_pay_number` varchar(30) DEFAULT NULL COMMENT 'EOP支付流水号',
  `eop_refund_url` varchar(200) DEFAULT NULL COMMENT '退款接口URL',
  `send_notify_url` varchar(200) DEFAULT NULL COMMENT '通知eop发货地址',
  `pay_fail_reason` varchar(200) DEFAULT NULL COMMENT '支付失败原因',
  `dealer_id` varchar(50) DEFAULT NULL COMMENT '代理商ID',
  `pay_type` varchar(2) DEFAULT NULL COMMENT '支付类型：0 钱包 1 支付宝',
  `ticket_num` int(2) DEFAULT NULL COMMENT '订单内车票数量',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `isChooseSeats` tinyint(1) DEFAULT NULL COMMENT '是否选座, true:选, false:非选',
  `chooseSeats` varchar(50) DEFAULT NULL COMMENT '选座信息(选座个数要和乘客数量一致)',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票订单信息表';

/*Table structure for table `ext_orderinfo_alter` */

DROP TABLE IF EXISTS `ext_orderinfo_alter`;

CREATE TABLE `ext_orderinfo_alter` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '车票ID',
  `alter_train_code` varchar(25) DEFAULT NULL COMMENT '要改签的车次',
  `alter_train_type` varchar(10) DEFAULT NULL COMMENT '要改签的车次类型',
  `alter_seat_type` varchar(2) DEFAULT NULL COMMENT '要改签的座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `alter_train_time` varchar(2) DEFAULT NULL COMMENT '要改签的发车时间段：00：00:00:00-06:00:00 ；11：06:00:00-12:00:00；22：12:00:00-18:00:00；33：18:00:00-24:00:00',
  `alter_travel_time` datetime DEFAULT NULL COMMENT '要改签的发车日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `alter_status` varchar(2) DEFAULT NULL COMMENT '改签状态：00、等待改签；01、重新改签；02、正在改签；03：人工改签；11、改签失败；22：改签成功；',
  `from_station` varchar(45) DEFAULT NULL COMMENT '发车站',
  `arrive_station` varchar(45) DEFAULT NULL COMMENT '到达站',
  `old_train_code` varchar(20) DEFAULT NULL COMMENT '原车次',
  `old_seat_type` varchar(2) DEFAULT NULL COMMENT '原坐席：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `old_travel_time` datetime DEFAULT NULL COMMENT '原发车日期',
  `old_ticket_type` varchar(2) DEFAULT NULL COMMENT '原车票类型',
  `old_from_time` varchar(30) DEFAULT NULL COMMENT '原发车时间',
  `old_seat_no` varchar(5) DEFAULT NULL COMMENT '原座位号',
  `old_train_box` varchar(5) DEFAULT NULL COMMENT '原车厢号',
  `old_pay_money` decimal(11,3) DEFAULT NULL COMMENT '原支付金额',
  `new_train_code` varchar(20) DEFAULT NULL COMMENT '改签后的车次',
  `new_seat_type` varchar(2) DEFAULT NULL COMMENT '改签后的坐席：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `new_travel_time` datetime DEFAULT NULL COMMENT '改签后的发车日期',
  `new_from_time` varchar(30) DEFAULT NULL COMMENT '改签后的发车时间',
  `new_ticket_type` varchar(2) DEFAULT NULL COMMENT '改签后的车票类型',
  `new_seat_no` varchar(5) DEFAULT NULL COMMENT '改签后的座位号',
  `new_train_box` varchar(5) DEFAULT NULL COMMENT '改签后的车厢号',
  `new_pay_money` decimal(11,3) DEFAULT NULL COMMENT '改签后的支付金额',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户提交待改签表';

/*Table structure for table `ext_orderinfo_alternotify` */

DROP TABLE IF EXISTS `ext_orderinfo_alternotify`;

CREATE TABLE `ext_orderinfo_alternotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `notify_url` varchar(45) DEFAULT NULL COMMENT '改签异步通知url',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='改签处理结果通知表';

/*Table structure for table `ext_orderinfo_booknotify` */

DROP TABLE IF EXISTS `ext_orderinfo_booknotify`;

CREATE TABLE `ext_orderinfo_booknotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `merchant_id` varchar(50) DEFAULT NULL COMMENT '商户订单id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `notify_url` varchar(100) DEFAULT NULL COMMENT '异步通知商户地址',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2019 DEFAULT CHARSET=utf8 COMMENT='订单预订结果通知表';

/*Table structure for table `ext_orderinfo_bx` */

DROP TABLE IF EXISTS `ext_orderinfo_bx`;

CREATE TABLE `ext_orderinfo_bx` (
  `bx_id` varchar(50) NOT NULL COMMENT '保险单号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '车票ID',
  `from_name` varchar(45) DEFAULT NULL COMMENT '出发地址',
  `to_name` varchar(45) DEFAULT NULL COMMENT '到达地址',
  `ids_type` varchar(45) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户姓名',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '用户证件号',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL,
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `bx_status` int(11) DEFAULT NULL COMMENT '状态：0、未发送 1、正在发送 2、发送完成 3、正在撤销 4、撤销完成 5、发送失败 6、需要取消 7、取消失败',
  `bx_code` varchar(45) DEFAULT NULL COMMENT '保险单号',
  `bx_billno` varchar(45) DEFAULT NULL COMMENT '服务器端订单号',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '进货金额',
  `product_id` varchar(15) DEFAULT NULL COMMENT '保险产品Id',
  `effect_date` varchar(45) DEFAULT NULL COMMENT '生效日期',
  `train_no` varchar(45) DEFAULT NULL COMMENT '车次',
  `bx_channel` varchar(45) DEFAULT NULL COMMENT '保险渠道: 1、快保   2、合众',
  PRIMARY KEY (`bx_id`),
  KEY `FK_Reference_1_idx` (`order_id`),
  CONSTRAINT `FK_Reference_1_idx` FOREIGN KEY (`order_id`) REFERENCES `ext_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保险单信息';

/*Table structure for table `ext_orderinfo_bxfp` */

DROP TABLE IF EXISTS `ext_orderinfo_bxfp`;

CREATE TABLE `ext_orderinfo_bxfp` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `fp_receiver` varchar(50) DEFAULT NULL COMMENT '收件人',
  `fp_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `fp_zip_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `fp_address` varchar(300) DEFAULT NULL COMMENT '收件地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ext_orderinfo_cp` */

DROP TABLE IF EXISTS `ext_orderinfo_cp`;

CREATE TABLE `ext_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `ids_type` int(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` int(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '00:无退票，11:退票中 22：拒绝退票 33：退票成功，44：退票失败',
  `refund_fail_reason` varchar(200) DEFAULT NULL COMMENT '退款失败原因/拒绝退票原因',
  `alter_seat_type` varchar(2) DEFAULT NULL,
  `alter_train_box` varchar(5) DEFAULT NULL,
  `alter_seat_no` varchar(10) DEFAULT NULL,
  `alter_buy_money` decimal(11,3) DEFAULT NULL,
  `alter_train_no` varchar(10) DEFAULT NULL,
  `alter_money` decimal(11,3) DEFAULT NULL,
  `refund_12306_money` decimal(11,3) DEFAULT NULL,
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_2_idx` (`order_id`),
  CONSTRAINT `FK_Reference_2_idx` FOREIGN KEY (`order_id`) REFERENCES `ext_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票订购信息表';

/*Table structure for table `ext_orderinfo_eopandpaynotify` */

DROP TABLE IF EXISTS `ext_orderinfo_eopandpaynotify`;

CREATE TABLE `ext_orderinfo_eopandpaynotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `eop_order_id` varchar(50) DEFAULT NULL COMMENT 'eop订单id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `send_notify_url` varchar(200) DEFAULT NULL COMMENT '异步通知商户地址',
  `pay_money` varchar(10) DEFAULT NULL COMMENT '支付金额',
  `pay_type` varchar(2) DEFAULT '11' COMMENT '11:代扣；22：自行扣费',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=218 DEFAULT CHARSET=utf8 COMMENT='订单通知eop发货和出票系统支付表';

/*Table structure for table `ext_orderinfo_history` */

DROP TABLE IF EXISTS `ext_orderinfo_history`;

CREATE TABLE `ext_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '退款创建时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2166633 DEFAULT CHARSET=utf8;

/*Table structure for table `ext_orderinfo_paynotify` */

DROP TABLE IF EXISTS `ext_orderinfo_paynotify`;

CREATE TABLE `ext_orderinfo_paynotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '重新支付状态：00、等待支付 11、开始支付 22、支付完成 33、支付失败',
  `notify_time` datetime DEFAULT NULL COMMENT '开始支付时间',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `pay_type` varchar(22) DEFAULT NULL COMMENT '支付类型：11、代理商平台支付；22、支付宝支付',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '支付失败原因',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COMMENT='订单支付结果通知表';

/*Table structure for table `ext_orderinfo_payresultnotify` */

DROP TABLE IF EXISTS `ext_orderinfo_payresultnotify`;

CREATE TABLE `ext_orderinfo_payresultnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：11、开始通知 22、通知完成 44、通知失败',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `pay_status` varchar(2) DEFAULT NULL COMMENT '支付结果: 00、支付失败；11：支付成功',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '支付失败原因',
  `pay_number` varchar(32) DEFAULT NULL COMMENT '支付流水号',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=456 DEFAULT CHARSET=utf8 COMMENT='订单支付结果通知表';

/*Table structure for table `ext_orderinfo_ps` */

DROP TABLE IF EXISTS `ext_orderinfo_ps`;

CREATE TABLE `ext_orderinfo_ps` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `ps_billno` varchar(45) DEFAULT NULL COMMENT '配送单号',
  `ps_company` varchar(45) DEFAULT NULL COMMENT '配送公司',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '配送费用',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `ps_status` varchar(2) DEFAULT NULL COMMENT '配送状态：00、等待配送 11、正在配送 22、配送成功',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `link_name` varchar(45) DEFAULT NULL COMMENT '联系人姓名',
  `link_phone` varchar(45) DEFAULT NULL COMMENT '联系人电话',
  `link_address` varchar(125) DEFAULT NULL COMMENT '联系人地址',
  `link_mail` varchar(45) DEFAULT NULL COMMENT '联系人邮件\n',
  PRIMARY KEY (`order_id`),
  CONSTRAINT `FK_Reference_3_idx` FOREIGN KEY (`order_id`) REFERENCES `ext_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配送单信息';

/*Table structure for table `ext_orderinfo_refundeopnotify` */

DROP TABLE IF EXISTS `ext_orderinfo_refundeopnotify`;

CREATE TABLE `ext_orderinfo_refundeopnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `eop_order_id` varchar(50) DEFAULT NULL COMMENT '平台订单id',
  `refund_seq` varchar(45) DEFAULT NULL COMMENT '退款流水号(trip_no)',
  `refund_money` decimal(11,2) DEFAULT NULL COMMENT '退款金额',
  `refund_reason` varchar(500) DEFAULT NULL COMMENT '退款原因',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '通知状态：00、未通知 11、准备通知 22、开始通知 33、通知完成',
  `notify_url` varchar(200) DEFAULT NULL COMMENT '通知地址',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `withholding_type` varchar(2) DEFAULT NULL COMMENT '代扣方式:平台代扣、支付宝代扣',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=76933 DEFAULT CHARSET=utf8 COMMENT='平台退款通知表';

/*Table structure for table `ext_orderinfo_refundnotify` */

DROP TABLE IF EXISTS `ext_orderinfo_refundnotify`;

CREATE TABLE `ext_orderinfo_refundnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票号',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单id',
  `refund_seq` varchar(45) DEFAULT NULL COMMENT '退款流水号(trip_no)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '通知状态：00、未通知 11、准备通知 22、开始通知 33、通知完成  44、通知失败',
  `notify_url` varchar(200) DEFAULT NULL COMMENT '通知地址',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT 'eop退款流水号',
  `eop_refund_status` varchar(2) DEFAULT NULL COMMENT 'eop退款状态',
  `eop_notify_status` varchar(2) DEFAULT NULL COMMENT '通知eop退款通知状态：11、通知失败；22通知成功',
  `eop_refund_time` datetime DEFAULT NULL COMMENT 'EOP实际退款完成时间',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45239 DEFAULT CHARSET=utf8 COMMENT='退票退款通知表';

/*Table structure for table `ext_orderinfo_refundstream` */

DROP TABLE IF EXISTS `ext_orderinfo_refundstream`;

CREATE TABLE `ext_orderinfo_refundstream` (
  `stream_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单Id',
  `refund_type` varchar(1) DEFAULT NULL COMMENT '退款类型：1、用户线上退票 2、用户车站退票 3、改签单退款 4、差额退款 5、出票失败退款 6 线下退款',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号(trip_no)',
  `merchant_refund_seq` varchar(50) DEFAULT NULL COMMENT '商户退款流水号(request_id)',
  `refund_money` decimal(11,2) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,2) DEFAULT NULL COMMENT '实际退款金额',
  `alter_tickets_money` decimal(11,2) DEFAULT NULL COMMENT '改签差价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_purl` varchar(100) DEFAULT NULL COMMENT '小票地址',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注',
  `our_remark` varchar(200) DEFAULT NULL COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refund_status` varchar(2) DEFAULT NULL COMMENT '退款状态：00、等待退票 11、同意退票 22、拒绝退票 33、退票完成 44、退票失败  ',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道(merchant_id 合作商户编号)',
  `eop_refund_status` varchar(2) DEFAULT NULL COMMENT '平台退款状态：11、退款失败；22:、退款成功',
  `eop_refund_money` decimal(11,2) DEFAULT NULL COMMENT '平台实际退款金额',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT '平台退款流水号',
  `eop_refund_time` date DEFAULT NULL COMMENT '平台实际退款完成时间',
  `change_ticket_info` varchar(200) DEFAULT NULL COMMENT '改签明细',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志',
  `option_time` date DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`stream_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=130987 DEFAULT CHARSET=utf8 COMMENT='退款流水表';

/*Table structure for table `ext_orderinfo_returnnotify` */

DROP TABLE IF EXISTS `ext_orderinfo_returnnotify`;

CREATE TABLE `ext_orderinfo_returnnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `refund_type` varchar(10) DEFAULT NULL COMMENT '退款类型: 001、差额退款；002：出票失败退款',
  `refund_amount` varchar(10) DEFAULT '0' COMMENT '退款金额',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '出票失败原因',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=447062 DEFAULT CHARSET=utf8 COMMENT='订单处理结果通知表';

/*Table structure for table `ext_productinfo` */

DROP TABLE IF EXISTS `ext_productinfo`;

CREATE TABLE `ext_productinfo` (
  `product_id` varchar(15) NOT NULL COMMENT '产品id',
  `type` int(2) DEFAULT NULL COMMENT '产品类别 0:自有产品 1:保险',
  `name` varchar(32) DEFAULT NULL COMMENT '产品名称',
  `status` int(2) DEFAULT NULL COMMENT '产品状态 0:未上架 1:上架',
  `province_id` varchar(10) DEFAULT NULL COMMENT '所属省份id',
  `city_id` varchar(10) DEFAULT NULL COMMENT '所属城市id',
  `sale_type` varchar(2) DEFAULT NULL COMMENT '计费方式 0：元/件 1：元/月 2：元/年',
  `sale_price` decimal(11,3) DEFAULT NULL COMMENT '售价',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `describe` varchar(800) DEFAULT NULL COMMENT '产品描述',
  `level` varchar(2) DEFAULT NULL COMMENT '级别 0：普通 1：vip',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品表';

/*Table structure for table `ext_system_log` */

DROP TABLE IF EXISTS `ext_system_log`;

CREATE TABLE `ext_system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

/*Table structure for table `ext_system_setting` */

DROP TABLE IF EXISTS `ext_system_setting`;

CREATE TABLE `ext_system_setting` (
  `setting_id` varchar(50) NOT NULL COMMENT '主键',
  `setting_name` varchar(50) DEFAULT NULL COMMENT '名称',
  `setting_value` varchar(2000) DEFAULT NULL COMMENT '内容',
  `setting_status` varchar(2) DEFAULT NULL COMMENT '状态 0:关闭 1:启用',
  `setting_desc` varchar(100) DEFAULT NULL COMMENT '说明'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ext_zm` */

DROP TABLE IF EXISTS `ext_zm`;

CREATE TABLE `ext_zm` (
  `zmhz` varchar(10) DEFAULT NULL,
  `py` varchar(5) DEFAULT NULL,
  `lh` varchar(3) DEFAULT NULL,
  `tcs` int(11) DEFAULT NULL,
  `qpy` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `file` */

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `filename` varchar(200) DEFAULT NULL COMMENT '文件名',
  `filepath` varchar(200) DEFAULT NULL COMMENT '文件路径',
  `bill_time` date DEFAULT NULL COMMENT '账单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_name` varchar(200) DEFAULT NULL COMMENT '上传者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

/*Table structure for table `file_balance_account` */

DROP TABLE IF EXISTS `file_balance_account`;

CREATE TABLE `file_balance_account` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `filename` varchar(200) DEFAULT NULL COMMENT '文件名',
  `filepath` varchar(200) DEFAULT NULL COMMENT '文件路径',
  `bill_time` date DEFAULT NULL COMMENT '账单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_name` varchar(200) DEFAULT NULL COMMENT '上传者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

/*Table structure for table `gen_area` */

DROP TABLE IF EXISTS `gen_area`;

CREATE TABLE `gen_area` (
  `area_id` int(5) NOT NULL COMMENT '主键（无意义）',
  `area_no` varchar(6) DEFAULT NULL COMMENT '区域编号（国家行政编码）',
  `area_name` varchar(100) DEFAULT NULL COMMENT '区域名称',
  `area_shortname` varchar(4) DEFAULT NULL COMMENT '区域简称（针对省份）',
  `area_fullspell` varchar(100) DEFAULT NULL COMMENT '区域全拼（针对市）',
  `area_shortspell` varchar(32) DEFAULT NULL COMMENT '区域简拼（针对市）',
  `area_code` varchar(8) DEFAULT NULL COMMENT '区域区号（针对市）',
  `area_parentno` varchar(6) DEFAULT NULL COMMENT '父区域编号',
  `area_oldno` varchar(6) DEFAULT NULL COMMENT '19e3.0对应编号',
  `area_rank` int(11) DEFAULT NULL COMMENT '区域等级',
  `area_zipcode` varchar(10) DEFAULT NULL COMMENT '区域邮政编码',
  KEY `idx_area_no` (`area_no`),
  KEY `idx_area_parentno` (`area_parentno`),
  KEY `idx_area_rank` (`area_rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `grasp_train_list` */

DROP TABLE IF EXISTS `grasp_train_list`;

CREATE TABLE `grasp_train_list` (
  `train_no` varchar(45) NOT NULL COMMENT '列车ID',
  `station_train_code` varchar(45) NOT NULL COMMENT '列车号',
  `start_station_name` varchar(20) DEFAULT NULL COMMENT '始发站名',
  `end_station_name` varchar(20) DEFAULT NULL COMMENT '终点站名',
  `train_class` varchar(5) DEFAULT NULL COMMENT '列车类型分组',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_status` varchar(2) DEFAULT '00' COMMENT '更新途经站状态 00、未更新 11、更新成功 22、更新失败 ',
  `update_message` varchar(55) DEFAULT NULL COMMENT '更新返回信息',
  `update_time` datetime DEFAULT NULL COMMENT '更新操作时间',
  `update_error_code` varchar(2) DEFAULT NULL COMMENT '更新失败错误码 01车站编号错误 02查询异常 03解析异常 04程序异常',
  PRIMARY KEY (`station_train_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `gt_orderinfo` */

DROP TABLE IF EXISTS `gt_orderinfo`;

CREATE TABLE `gt_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格(sum_amount)',
  `pay_money_show` decimal(11,2) DEFAULT NULL COMMENT '用户支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `ticket_pay_money` decimal(11,2) DEFAULT NULL COMMENT '票价总额',
  `bx_pay_money` decimal(11,2) DEFAULT '0.00' COMMENT '保险总额',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 00、未支付 11、支付成功 12、eop发货成功 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 24、取消成功 80、超时未支付',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `from_station` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `arrive_station` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` varchar(10) DEFAULT NULL COMMENT '出发时间',
  `arrive_time` varchar(10) DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `refund_deadline_ignore` varchar(2) DEFAULT NULL COMMENT '1：无视截止时间（发车12小时后为退款截止时间）',
  `refund_total` decimal(11,2) DEFAULT NULL COMMENT '退款金额总计',
  `refund_status` varchar(20) DEFAULT 'NO_RFUND' COMMENT '退款状态：NO_RFUND:无退票 ；REFUNDING:退票中；PART_REFUND:部分退票完成；REFUND_FINISH:退款完成；REFUSE_REFUND:拒绝退款',
  `can_refund` varchar(2) DEFAULT NULL COMMENT '是否可以退款（默认可以退 0:不能退 1:能退）',
  `sms_notify` varchar(1) DEFAULT NULL COMMENT '是否短信通知  1：是；0:否',
  `ext_seat` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `merchant_order_id` varchar(50) DEFAULT '' COMMENT '合作商户订单id',
  `order_level` varchar(10) DEFAULT '' COMMENT '订单级别：0：普通订单；1：VIP订单',
  `merchant_id` varchar(50) DEFAULT '' COMMENT '合作商户编号',
  `link_name` varchar(100) DEFAULT '' COMMENT '短信通知联系人',
  `link_phone` varchar(20) DEFAULT '' COMMENT '短信通知号码',
  `order_result_url` varchar(200) DEFAULT '' COMMENT '订单处理结果通知合作商户url',
  `order_pro1` varchar(30) DEFAULT '' COMMENT '备用字段1',
  `order_pro2` varchar(30) DEFAULT '' COMMENT '备用字段2',
  `eop_order_id` varchar(45) DEFAULT NULL COMMENT 'EOP订单号',
  `nopwd_pay_url` varchar(200) DEFAULT NULL COMMENT 'EOP提供无密支付接口地址',
  `eop_pay_number` varchar(30) DEFAULT NULL COMMENT 'EOP支付流水号',
  `eop_refund_url` varchar(200) DEFAULT NULL COMMENT '退款接口URL',
  `send_notify_url` varchar(200) DEFAULT NULL COMMENT '通知eop发货地址',
  `pay_fail_reason` varchar(200) DEFAULT NULL COMMENT '支付失败原因',
  `dealer_id` varchar(50) DEFAULT NULL COMMENT '代理商ID',
  `pay_type` varchar(2) DEFAULT NULL COMMENT '支付类型：0 钱包 1 支付宝',
  `ticket_num` int(2) DEFAULT NULL COMMENT '订单内车票数量',
  `order_type` varchar(4) DEFAULT NULL COMMENT '11.先下单后付款,22.先支付后预定',
  `reqtoken` varchar(150) DEFAULT NULL COMMENT '请求token',
  `fail_reason` varchar(3) DEFAULT NULL COMMENT '占座失败原因:1.无票 2.身份证件已经实名制购票 3.票价和12306不符 4.乘车时间异常 5.证件错误 6.用户要求取消订单 7.未通过12306实名认证 8.身份信息核验 9.系统异常 11、超时未支付     ',
  `isChooseSeats` tinyint(1) DEFAULT NULL COMMENT '是否选座, 1:选, 0:非选',
  `chooseSeats` varchar(50) DEFAULT NULL COMMENT '选座信息(选座个数要和乘客数量一致)',
  PRIMARY KEY (`order_id`),
  KEY `index_gt_create_time` (`create_time`),
  KEY `index_gt_orderinfo_order_status` (`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='高铁管家预订信息表';

/*Table structure for table `gt_orderinfo_booknotify` */

DROP TABLE IF EXISTS `gt_orderinfo_booknotify`;

CREATE TABLE `gt_orderinfo_booknotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `merchant_id` varchar(50) DEFAULT NULL COMMENT '商户订单id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、未通知 11、准备通知 12、开始通知 22、通知完成 33、不用通知（预定期间已支付，直接出票成功后通知商户）',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `notify_url` varchar(100) DEFAULT NULL COMMENT '异步通知商户地址',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=310867 DEFAULT CHARSET=utf8 COMMENT='订单预订结果通知表';

/*Table structure for table `gt_orderinfo_cp` */

DROP TABLE IF EXISTS `gt_orderinfo_cp`;

CREATE TABLE `gt_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `ids_type` int(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` int(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '00:无退票，11:退票中 22：拒绝退票 33：退票成功，44：退票失败',
  `refund_fail_reason` varchar(200) DEFAULT NULL COMMENT '退款失败原因/拒绝退票原因',
  `alter_seat_type` varchar(2) DEFAULT NULL,
  `alter_train_box` varchar(5) DEFAULT NULL,
  `alter_seat_no` varchar(10) DEFAULT NULL,
  `alter_buy_money` decimal(11,3) DEFAULT NULL,
  `alter_train_no` varchar(10) DEFAULT NULL,
  `alter_money` decimal(11,3) DEFAULT NULL,
  `refund_12306_money` decimal(11,3) DEFAULT NULL,
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_gt_idx` (`order_id`),
  CONSTRAINT `FK_Reference_gt_idx` FOREIGN KEY (`order_id`) REFERENCES `gt_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='高铁管家乘客信息表';

/*Table structure for table `gt_orderinfo_eopandpaynotify` */

DROP TABLE IF EXISTS `gt_orderinfo_eopandpaynotify`;

CREATE TABLE `gt_orderinfo_eopandpaynotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `eop_order_id` varchar(50) DEFAULT NULL COMMENT 'eop订单id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `send_notify_url` varchar(100) DEFAULT NULL COMMENT '异步通知商户地址',
  `pay_money` varchar(10) DEFAULT NULL COMMENT '支付金额',
  `pay_type` varchar(2) DEFAULT '11' COMMENT '11:代扣；22：自行扣费',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=639862 DEFAULT CHARSET=utf8 COMMENT='订单通知eop发货和出票系统支付表';

/*Table structure for table `gt_orderinfo_history` */

DROP TABLE IF EXISTS `gt_orderinfo_history`;

CREATE TABLE `gt_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '退款创建时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`),
  KEY `NewIndex1` (`order_id`),
  KEY `NewIndex2` (`cp_id`),
  KEY `NewIndex3` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=121194 DEFAULT CHARSET=utf8;

/*Table structure for table `gt_orderinfo_refundnotify` */

DROP TABLE IF EXISTS `gt_orderinfo_refundnotify`;

CREATE TABLE `gt_orderinfo_refundnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票号',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单id',
  `refund_seq` varchar(45) DEFAULT NULL COMMENT '退款流水号(trip_no)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '通知状态：00、未通知 11、准备通知 22、开始通知 33、通知完成  44、通知失败',
  `notify_url` varchar(200) DEFAULT NULL COMMENT '通知地址',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT 'eop退款流水号',
  `eop_refund_status` varchar(2) DEFAULT NULL COMMENT 'eop退款状态',
  `eop_notify_status` varchar(2) DEFAULT NULL COMMENT '通知eop退款通知状态：11、通知失败；22通知成功',
  `eop_refund_time` datetime DEFAULT NULL COMMENT 'EOP实际退款完成时间',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=421 DEFAULT CHARSET=utf8 COMMENT='退票退款通知表';

/*Table structure for table `gt_orderinfo_refundstream` */

DROP TABLE IF EXISTS `gt_orderinfo_refundstream`;

CREATE TABLE `gt_orderinfo_refundstream` (
  `stream_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单Id',
  `refund_type` varchar(1) DEFAULT NULL COMMENT '退款类型：1、用户线上退票 2、用户车站退票 3、改签单退款 4、差额退款 5、出票失败退款',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号(trip_no)',
  `merchant_refund_seq` varchar(1000) DEFAULT NULL COMMENT '商户退款流水号(request_id)',
  `refund_money` decimal(11,2) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,2) DEFAULT NULL COMMENT '实际退款金额',
  `alter_tickets_money` decimal(11,2) DEFAULT NULL COMMENT '改签差价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_purl` varchar(100) DEFAULT NULL COMMENT '小票地址',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注',
  `our_remark` varchar(200) DEFAULT NULL COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refund_status` varchar(2) DEFAULT NULL COMMENT '退款状态：00、等待退票 11、同意退票 22、拒绝退票 33、退票完成 44、退票失败  ',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道(merchant_id 合作商户编号)',
  `eop_refund_status` varchar(2) DEFAULT NULL COMMENT '平台退款状态：11、退款失败；22:、退款成功',
  `eop_refund_money` decimal(11,2) DEFAULT NULL COMMENT '平台实际退款金额',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT '平台退款流水号',
  `eop_refund_time` date DEFAULT NULL COMMENT '平台实际退款完成时间',
  `change_ticket_info` varchar(200) DEFAULT NULL COMMENT '改签明细',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志',
  PRIMARY KEY (`stream_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `index_gt_refundstream_refund_status` (`refund_status`)
) ENGINE=InnoDB AUTO_INCREMENT=693 DEFAULT CHARSET=utf8 COMMENT='高铁管家退款表';

/*Table structure for table `gt_orderinfo_returnnotify` */

DROP TABLE IF EXISTS `gt_orderinfo_returnnotify`;

CREATE TABLE `gt_orderinfo_returnnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成  44：通知失败',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `refund_type` varchar(10) DEFAULT NULL COMMENT '退款类型: 001、差额退款；002：出票失败退款',
  `refund_amount` varchar(10) DEFAULT '0' COMMENT '退款金额',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '出票失败原因',
  `is_travel` varchar(2) DEFAULT '0' COMMENT '是否为不可退的旅游票，1 旅游票不可退，0 非旅游票可退。（默认 0）',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2255383 DEFAULT CHARSET=utf8 COMMENT='订单处理结果通知表';

/*Table structure for table `gt_refundstation` */

DROP TABLE IF EXISTS `gt_refundstation`;

CREATE TABLE `gt_refundstation` (
  `station_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `order_status` varchar(20) DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8 COMMENT='手动导入订单表';

/*Table structure for table `gt_refundstation_tj` */

DROP TABLE IF EXISTS `gt_refundstation_tj`;

CREATE TABLE `gt_refundstation_tj` (
  `station_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `count` int(11) DEFAULT NULL COMMENT '总数',
  `success_num` int(11) DEFAULT NULL COMMENT '符合条件数',
  `fail_num` int(11) DEFAULT NULL COMMENT '已退款数',
  `again_num` int(11) DEFAULT NULL COMMENT '重复数据数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='手动导入订单统计表';

/*Table structure for table `hc_agent_login` */

DROP TABLE IF EXISTS `hc_agent_login`;

CREATE TABLE `hc_agent_login` (
  `agent_login_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '注册id',
  `agent_id` varchar(50) DEFAULT NULL COMMENT '代理商id',
  `is_login` varchar(2) DEFAULT NULL COMMENT '是否已经显示引导页：0、是 1、否',
  `is_verify` varchar(2) DEFAULT NULL COMMENT '是否是第一次登陆改版后的新页面：0：是 1：否',
  `create_time` datetime DEFAULT NULL,
  `is_newLogin` varchar(2) DEFAULT '0' COMMENT '是否第一次登录最新改版后的页面：0第一次 1不是第一次',
  PRIMARY KEY (`agent_login_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_agent_orderdata` */

DROP TABLE IF EXISTS `hc_agent_orderdata`;

CREATE TABLE `hc_agent_orderdata` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `order_month` varchar(20) DEFAULT NULL COMMENT '订单年月',
  `order_num` varchar(20) DEFAULT NULL COMMENT '订单数量',
  `order_total_money` decimal(11,3) DEFAULT NULL COMMENT '支付金额',
  `bx_num` varchar(20) DEFAULT NULL COMMENT '保险个数',
  `bx_money` decimal(11,3) DEFAULT NULL COMMENT '保险利润',
  `winning_money` decimal(11,3) DEFAULT NULL COMMENT '活动奖励',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `dealer_name` varchar(50) DEFAULT NULL COMMENT '代理商姓名',
  `dealer_id` varchar(50) DEFAULT NULL COMMENT '代理商ID',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存放代理商的订单数据';

/*Table structure for table `hc_agent_winning` */

DROP TABLE IF EXISTS `hc_agent_winning`;

CREATE TABLE `hc_agent_winning` (
  `winning_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `agent_id` varchar(50) DEFAULT NULL COMMENT '中奖代理商id',
  `dealer_name` varchar(50) DEFAULT NULL COMMENT '中奖代理商姓名',
  `order_id` varchar(50) DEFAULT NULL COMMENT '中奖订单号',
  `winning_money` decimal(11,3) DEFAULT '0.000' COMMENT '中奖金额',
  `order_time` datetime DEFAULT NULL COMMENT '购票时间',
  `is_pay` varchar(2) DEFAULT NULL COMMENT '是否已打款给代理商:0、是，1、否',
  `pay_time` datetime DEFAULT NULL COMMENT '打款时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `agent_type` varchar(2) DEFAULT NULL COMMENT '中奖代理商类型：0、金牌；1、银牌；2、VIP；3、SVIP',
  `winning_type` varchar(2) DEFAULT NULL COMMENT '中奖类型(前面的为银牌，后面为金牌)：0、30元以下和50-90；1、30-60 和90-120 2、60-90和120-150',
  `extend` varchar(50) DEFAULT NULL COMMENT '0、抽奖金牌代理商人数为0；1、抽奖时银牌代理商人数为0；2、vip抽奖时订单数为0',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `seat_type` varchar(10) DEFAULT NULL COMMENT '座位类型',
  PRIMARY KEY (`winning_id`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_complain` */

DROP TABLE IF EXISTS `hc_complain`;

CREATE TABLE `hc_complain` (
  `complain_id` varchar(50) NOT NULL COMMENT '投诉建议id',
  `province_id` varchar(10) DEFAULT NULL COMMENT '所属省',
  `city_id` varchar(10) DEFAULT NULL COMMENT '所属城市',
  `district_id` varchar(10) DEFAULT NULL COMMENT '区 id',
  `agent_id` varchar(50) DEFAULT NULL COMMENT '代理商id',
  `question_type` varchar(2) DEFAULT NULL COMMENT '问题类别  0：订单问题 1：加盟问题 2：配送问题 3：出票问题 4：业务建议 5：其他',
  `question` varchar(1000) DEFAULT NULL COMMENT '问题或建议',
  `answer` varchar(1000) DEFAULT NULL COMMENT '答复',
  `permission` varchar(2) DEFAULT NULL COMMENT '权限 0：全部可见 1：自己可见',
  `create_time` datetime DEFAULT NULL COMMENT '提问时间',
  `reply_time` datetime DEFAULT NULL COMMENT '答复时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  `eop_user` varchar(50) DEFAULT NULL COMMENT 'EOP用户昵称',
  PRIMARY KEY (`complain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投诉与建议';

/*Table structure for table `hc_complain_history` */

DROP TABLE IF EXISTS `hc_complain_history`;

CREATE TABLE `hc_complain_history` (
  `history_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `complain_id` varchar(50) DEFAULT NULL COMMENT '投诉建议id',
  `reply_time` datetime DEFAULT NULL COMMENT '答复时间',
  `reply_person` varchar(10) DEFAULT NULL COMMENT '答复人',
  `our_reply` varchar(1000) DEFAULT NULL COMMENT '答复内容',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_cpinfo` */

DROP TABLE IF EXISTS `hc_cpinfo`;

CREATE TABLE `hc_cpinfo` (
  `cp_id` varchar(50) DEFAULT NULL,
  `order_id` varchar(50) DEFAULT NULL,
  `from_city_id` varchar(10) DEFAULT NULL,
  `to_city_id` varchar(10) DEFAULT NULL,
  `cp_status` int(11) DEFAULT NULL COMMENT '出票状态：00、开始预订 11、正在预订 22、人工预订 33、预订成功 44、预订失败 55、预订异常 66、正在支付 77、人工支付 88、支付成功 99、支付通知',
  `opter` varchar(50) DEFAULT NULL,
  `account_id` varchar(50) DEFAULT NULL,
  `pay_account` varchar(50) DEFAULT NULL,
  `pay_money` decimal(11,3) DEFAULT NULL,
  `gf_orderid` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出票管理信息表';

/*Table structure for table `hc_excel_log` */

DROP TABLE IF EXISTS `hc_excel_log`;

CREATE TABLE `hc_excel_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `opt_name` varchar(50) DEFAULT NULL COMMENT '操作人',
  `comtent` varchar(500) DEFAULT NULL COMMENT '操作内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=784 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_jm_order` */

DROP TABLE IF EXISTS `hc_jm_order`;

CREATE TABLE `hc_jm_order` (
  `order_id` varchar(50) NOT NULL COMMENT '加盟订单',
  `user_id` varchar(50) DEFAULT NULL COMMENT '代理商id',
  `product_id` varchar(50) DEFAULT NULL COMMENT '产品id',
  `order_status` int(11) DEFAULT NULL COMMENT '加盟状态 00、预下单 11、支付成功 12、EOP发货完成 22、支付失败 33、等待退款 34、EOP正在退款 44、退款成功 55、退款失败',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `asp_order_type` varchar(8) DEFAULT NULL COMMENT '订单类型（jm:加盟订单 jmxf_sys:主动续费 jmxf_hum:被动续费）',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户加盟信息表';

/*Table structure for table `hc_msn` */

DROP TABLE IF EXISTS `hc_msn`;

CREATE TABLE `hc_msn` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_name` varchar(25) DEFAULT NULL COMMENT '联系人',
  `user_phone` varchar(25) DEFAULT NULL COMMENT '联系手机',
  `content` varchar(1000) DEFAULT NULL COMMENT '短信内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `hc_noticeinfo` */

DROP TABLE IF EXISTS `hc_noticeinfo`;

CREATE TABLE `hc_noticeinfo` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `notice_name` varchar(45) DEFAULT NULL COMMENT '通知名称',
  `notice_status` varchar(10) DEFAULT NULL COMMENT '通知状态 00、未发布 11、已发布  22、已到期',
  `notice_content` varchar(8000) DEFAULT NULL COMMENT '通知内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pub_time` datetime DEFAULT NULL COMMENT '生效时间',
  `stop_time` datetime DEFAULT NULL COMMENT '到期时间',
  `provinces` varchar(500) DEFAULT NULL COMMENT '发布省份 各个省份用","隔开',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1153 DEFAULT CHARSET=utf8 COMMENT='通知表';

/*Table structure for table `hc_order_eop` */

DROP TABLE IF EXISTS `hc_order_eop`;

CREATE TABLE `hc_order_eop` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `eop_order_id` varchar(50) DEFAULT NULL COMMENT 'eop订单号',
  `send_notify_url` varchar(1000) DEFAULT NULL COMMENT '发货结果通知URL(ASP发货完成后通知EOP的地址)',
  `eop_refund_url` varchar(1000) DEFAULT NULL COMMENT 'ASP向EOP请求退款的地址',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT 'EOP提供的退款流水，ASP可以使用该流水号到EOP查询退款状态',
  `asp_refund_seq` varchar(50) DEFAULT NULL COMMENT 'ASP退款请求流水号',
  `pay_url` varchar(1000) DEFAULT NULL COMMENT 'EOP提供的订单支付页面地址，ASP需嵌入该支付页面',
  `query_result_url` varchar(1000) DEFAULT NULL COMMENT '支付结果查询接口、退款结果查询接口地址',
  `eop_refund_time` datetime DEFAULT NULL COMMENT 'eop退款时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='eop交互参数表';

/*Table structure for table `hc_orderinfo` */

DROP TABLE IF EXISTS `hc_orderinfo`;

CREATE TABLE `hc_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `at_province_id` varchar(10) DEFAULT NULL COMMENT '所属省',
  `at_city_id` varchar(10) DEFAULT NULL COMMENT '所属城市',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `ticket_pay_money` decimal(11,3) DEFAULT NULL COMMENT '票价总额',
  `bx_pay_money` decimal(11,3) DEFAULT '0.000' COMMENT '保险总额',
  `ps_pay_money` decimal(11,3) DEFAULT '0.000' COMMENT '配送总额',
  `server_pay_money` decimal(11,3) DEFAULT '0.000' COMMENT '技术服务费',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 00、预下单 11、支付成功 12、EOP发货  22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、支\r\n \r\n付失败 77、预约购票 78、取消预约 P1开始配送 P2配送成功 P3配送失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `dealer_name` varchar(50) DEFAULT NULL COMMENT '代理商姓名',
  `dealer_id` varchar(50) DEFAULT NULL COMMENT '代理商ID',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `timeout` varchar(2) DEFAULT NULL COMMENT '超时重发（1:需要重发 2:重发完成）',
  `refund_deadline_ignore` varchar(2) DEFAULT NULL COMMENT '1：无视截止时间（发车12小时后为退款截止时间）',
  `refund_total` decimal(11,3) DEFAULT NULL COMMENT '退款金额总计',
  `can_refund` varchar(2) DEFAULT NULL COMMENT '是否可以退款（默认可以退 0:不能退 1:能退）',
  `ext_seat` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `user_pay_money` decimal(11,3) DEFAULT NULL COMMENT '用户支付总金额',
  `ticket_num` int(2) DEFAULT NULL COMMENT '订单内的车票数量',
  `ticket_ps_type` varchar(2) DEFAULT NULL COMMENT '00车站自提 11 送票上门',
  PRIMARY KEY (`order_id`),
  KEY `idx_billno` (`out_ticket_billno`),
  KEY `idx_area` (`at_province_id`,`at_city_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_dealer_id` (`dealer_id`),
  KEY `idx_status_time` (`order_status`(2),`create_time`),
  KEY `IND_hc_orderinfo_dealer_id_order_status` (`dealer_id`,`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票订单信息表';

/*Table structure for table `hc_orderinfo_bx` */

DROP TABLE IF EXISTS `hc_orderinfo_bx`;

CREATE TABLE `hc_orderinfo_bx` (
  `bx_id` varchar(50) NOT NULL COMMENT '保险单号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '车票ID',
  `from_name` varchar(45) DEFAULT NULL COMMENT '出发地址',
  `to_name` varchar(45) DEFAULT NULL COMMENT '到达地址',
  `ids_type` varchar(45) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户姓名',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '用户证件号',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL,
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `bx_status` int(11) DEFAULT NULL COMMENT '状态：0、未发送 1、正在发送 2、发送完成 3、正在撤销 4、撤销完成 5、发送失败 6、需要取消 7、取消失败',
  `bx_code` varchar(45) DEFAULT NULL COMMENT '保险单号',
  `bx_billno` varchar(45) DEFAULT NULL COMMENT '服务器端订单号',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '进货金额',
  `product_id` varchar(15) DEFAULT NULL COMMENT '车次',
  `effect_date` varchar(45) DEFAULT NULL COMMENT '生效日期',
  `train_no` varchar(45) DEFAULT NULL COMMENT '车次',
  `bx_channel` varchar(45) DEFAULT NULL COMMENT '保险渠道: 1、快保   2、合众',
  `fail_reason` varchar(200) DEFAULT NULL COMMENT '失败原因',
  `source_channel` varchar(2) DEFAULT NULL COMMENT '来源渠道',
  PRIMARY KEY (`bx_id`),
  KEY `FK_hc_bx_idx` (`order_id`),
  CONSTRAINT `FK_hc_bx_idx` FOREIGN KEY (`order_id`) REFERENCES `hc_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保险单信息';

/*Table structure for table `hc_orderinfo_bxfp` */

DROP TABLE IF EXISTS `hc_orderinfo_bxfp`;

CREATE TABLE `hc_orderinfo_bxfp` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `fp_receiver` varchar(50) DEFAULT NULL COMMENT '收件人',
  `fp_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `fp_zip_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `fp_address` varchar(300) DEFAULT NULL COMMENT '收件地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_cp` */

DROP TABLE IF EXISTS `hc_orderinfo_cp`;

CREATE TABLE `hc_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `ids_type` int(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `alter_seat_type` varchar(2) DEFAULT NULL COMMENT '改签后坐席',
  `alter_train_box` varchar(5) DEFAULT NULL COMMENT '改签后车厢',
  `alter_seat_no` varchar(10) DEFAULT NULL COMMENT '改签后座位号',
  `alter_buy_money` decimal(11,3) DEFAULT NULL COMMENT '改签后成本价格',
  `alter_train_no` varchar(10) DEFAULT NULL COMMENT '改签后车次',
  `alter_money` decimal(11,3) DEFAULT NULL COMMENT '改签差额',
  `refund_12306_money` decimal(11,3) DEFAULT NULL COMMENT '12306退款金额',
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_2_idx` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票订购信息表';

/*Table structure for table `hc_orderinfo_history` */

DROP TABLE IF EXISTS `hc_orderinfo_history`;

CREATE TABLE `hc_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `order_time` datetime DEFAULT NULL COMMENT '退款创建时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1258 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_manual` */

DROP TABLE IF EXISTS `hc_orderinfo_manual`;

CREATE TABLE `hc_orderinfo_manual` (
  `manual_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '人工注册id',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `mail` varchar(100) DEFAULT NULL COMMENT '12306注册邮箱',
  `mail_pwd` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `regist_status` varchar(2) DEFAULT NULL COMMENT '状态 55、需要检查账户状态， 56、正在检查账户状态   57、检查成功 58、检查失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `is_output` varchar(2) DEFAULT '0' COMMENT '是否已经输出到账号表0：未输出 1：已输出',
  `account_source` varchar(2) DEFAULT '1' COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `channel` varchar(30) DEFAULT NULL,
  `error_info` varchar(255) DEFAULT NULL COMMENT '返回信息',
  PRIMARY KEY (`manual_id`),
  KEY `IND_hc_orderinfo_regist_regist_status` (`regist_status`),
  KEY `IND_hc_orderinfo_regist_regist_status_create_time` (`regist_status`,`create_time`),
  KEY `account_name` (`account_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1596192 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_manual_copy` */

DROP TABLE IF EXISTS `hc_orderinfo_manual_copy`;

CREATE TABLE `hc_orderinfo_manual_copy` (
  `manual_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '人工注册id',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `mail` varchar(100) DEFAULT NULL COMMENT '12306注册邮箱',
  `mail_pwd` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `regist_status` varchar(2) DEFAULT NULL COMMENT '状态 55、需要检查账户状态， 56、正在检查账户状态   57、检查成功 58、检查失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `is_output` varchar(2) DEFAULT '0' COMMENT '是否已经输出到账号表0：未输出 1：已输出',
  `account_source` varchar(2) DEFAULT '1' COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `error_info` varchar(255) DEFAULT NULL COMMENT '返回信息',
  PRIMARY KEY (`manual_id`),
  KEY `IND_hc_orderinfo_regist_regist_status` (`regist_status`),
  KEY `IND_hc_orderinfo_regist_regist_status_create_time` (`regist_status`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=12155 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_manual_copy1` */

DROP TABLE IF EXISTS `hc_orderinfo_manual_copy1`;

CREATE TABLE `hc_orderinfo_manual_copy1` (
  `manual_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '人工注册id',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `mail` varchar(100) DEFAULT NULL COMMENT '12306注册邮箱',
  `mail_pwd` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `regist_status` varchar(2) DEFAULT NULL COMMENT '状态 55、需要检查账户状态， 56、正在检查账户状态   57、检查成功 58、检查失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `is_output` varchar(2) DEFAULT '0' COMMENT '是否已经输出到账号表0：未输出 1：已输出',
  `account_source` varchar(2) DEFAULT '1' COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `error_info` varchar(255) DEFAULT NULL COMMENT '返回信息',
  PRIMARY KEY (`manual_id`),
  KEY `IND_hc_orderinfo_regist_regist_status` (`regist_status`),
  KEY `IND_hc_orderinfo_regist_regist_status_create_time` (`regist_status`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1208064 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_manual_copy2` */

DROP TABLE IF EXISTS `hc_orderinfo_manual_copy2`;

CREATE TABLE `hc_orderinfo_manual_copy2` (
  `manual_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '人工注册id',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `mail` varchar(100) DEFAULT NULL COMMENT '12306注册邮箱',
  `mail_pwd` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `regist_status` varchar(2) DEFAULT NULL COMMENT '状态 55、需要检查账户状态， 56、正在检查账户状态   57、检查成功 58、检查失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `is_output` varchar(2) DEFAULT '0' COMMENT '是否已经输出到账号表0：未输出 1：已输出',
  `account_source` varchar(2) DEFAULT '1' COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `channel` varchar(30) DEFAULT NULL,
  `error_info` varchar(255) DEFAULT NULL COMMENT '返回信息',
  PRIMARY KEY (`manual_id`),
  KEY `IND_hc_orderinfo_regist_regist_status` (`regist_status`),
  KEY `IND_hc_orderinfo_regist_regist_status_create_time` (`regist_status`,`create_time`),
  KEY `account_name` (`account_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1976946 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_manual_copy3` */

DROP TABLE IF EXISTS `hc_orderinfo_manual_copy3`;

CREATE TABLE `hc_orderinfo_manual_copy3` (
  `manual_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '人工注册id',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `mail` varchar(100) DEFAULT NULL COMMENT '12306注册邮箱',
  `mail_pwd` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `regist_status` varchar(2) DEFAULT NULL COMMENT '状态 55、需要检查账户状态， 56、正在检查账户状态   57、检查成功 58、检查失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `is_output` varchar(2) DEFAULT '0' COMMENT '是否已经输出到账号表0：未输出 1：已输出',
  `account_source` varchar(2) DEFAULT '1' COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `channel` varchar(30) DEFAULT NULL,
  `error_info` varchar(255) DEFAULT NULL COMMENT '返回信息',
  PRIMARY KEY (`manual_id`),
  KEY `IND_hc_orderinfo_regist_regist_status` (`regist_status`),
  KEY `IND_hc_orderinfo_regist_regist_status_create_time` (`regist_status`,`create_time`),
  KEY `account_name` (`account_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1991244 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_manual_copy_copy` */

DROP TABLE IF EXISTS `hc_orderinfo_manual_copy_copy`;

CREATE TABLE `hc_orderinfo_manual_copy_copy` (
  `manual_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '人工注册id',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `mail` varchar(100) DEFAULT NULL COMMENT '12306注册邮箱',
  `mail_pwd` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `regist_status` varchar(2) DEFAULT NULL COMMENT '状态 55、需要检查账户状态， 56、正在检查账户状态   57、检查成功 58、检查失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `is_output` varchar(2) DEFAULT '0' COMMENT '是否已经输出到账号表0：未输出 1：已输出',
  `account_source` varchar(2) DEFAULT '1' COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `error_info` varchar(255) DEFAULT NULL COMMENT '返回信息',
  PRIMARY KEY (`manual_id`),
  KEY `IND_hc_orderinfo_regist_regist_status` (`regist_status`),
  KEY `IND_hc_orderinfo_regist_regist_status_create_time` (`regist_status`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=78671 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_manutemp` */

DROP TABLE IF EXISTS `hc_orderinfo_manutemp`;

CREATE TABLE `hc_orderinfo_manutemp` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `acc_username` varchar(500) DEFAULT NULL,
  `acc_password` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `acc_username` (`acc_username`(255))
) ENGINE=InnoDB AUTO_INCREMENT=78233 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_opt` */

DROP TABLE IF EXISTS `hc_orderinfo_opt`;

CREATE TABLE `hc_orderinfo_opt` (
  `opt_id` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单操作记录';

/*Table structure for table `hc_orderinfo_ps` */

DROP TABLE IF EXISTS `hc_orderinfo_ps`;

CREATE TABLE `hc_orderinfo_ps` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `ps_billno` varchar(45) DEFAULT NULL COMMENT '配送单号',
  `ps_company` varchar(45) DEFAULT NULL COMMENT '配送公司',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '配送费用',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `ps_status` varchar(2) DEFAULT NULL COMMENT '配送状态：00、等待配送 11、正在配送 22、配送成功',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `link_name` varchar(45) DEFAULT NULL COMMENT '联系人姓名',
  `link_phone` varchar(45) DEFAULT NULL COMMENT '联系人电话',
  `link_address` varchar(125) DEFAULT NULL COMMENT '联系人地址',
  `link_mail` varchar(45) DEFAULT NULL COMMENT '联系人邮件\n',
  PRIMARY KEY (`order_id`),
  CONSTRAINT `FK_hc_ps_idx` FOREIGN KEY (`order_id`) REFERENCES `hc_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配送单信息';

/*Table structure for table `hc_orderinfo_pssm` */

DROP TABLE IF EXISTS `hc_orderinfo_pssm`;

CREATE TABLE `hc_orderinfo_pssm` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `ps_billno` varchar(45) DEFAULT NULL COMMENT '配送单号',
  `ps_company` varchar(45) DEFAULT NULL COMMENT '配送公司',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '配送费用',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `ps_status` varchar(2) DEFAULT '00' COMMENT '配送状态：00、等待出票 11、等待配送 22、正在配送 33、配送成功 44、配送失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `choose_seat_num` varchar(2) DEFAULT NULL COMMENT '选择购买下铺数',
  `choose_ext` varchar(2) DEFAULT '00' COMMENT '是否同意无下铺时购买中上铺00、不同意11、同意',
  `link_name_ps` varchar(20) DEFAULT NULL COMMENT '联系姓名',
  `link_phone_ps` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `province` varchar(10) DEFAULT NULL COMMENT '所在省份',
  `city` varchar(10) DEFAULT NULL COMMENT '所在城市',
  `district` varchar(10) DEFAULT NULL COMMENT '城市所在区',
  `ps_address` varchar(500) DEFAULT NULL COMMENT '详细地址',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='送票上门信息明细';

/*Table structure for table `hc_orderinfo_pssm_cp` */

DROP TABLE IF EXISTS `hc_orderinfo_pssm_cp`;

CREATE TABLE `hc_orderinfo_pssm_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `cert_type` int(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `cert_no` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` varchar(45) DEFAULT NULL COMMENT '支付价格',
  `buy_money` varchar(45) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `check_status` varchar(2) DEFAULT NULL COMMENT '12306身份核验状态 0、已通过 1、审核中 2、未通过',
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_10_idx` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='19e火车票送票上门乘客明细\n';

/*Table structure for table `hc_orderinfo_pssm_ticket` */

DROP TABLE IF EXISTS `hc_orderinfo_pssm_ticket`;

CREATE TABLE `hc_orderinfo_pssm_ticket` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `order_status` varchar(10) DEFAULT NULL COMMENT '出票状态  00、开始出票 11、出票成功 12、出票失败 21、正在配送 22、配送成功 23、配送失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(20) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `error_info` varchar(145) DEFAULT NULL COMMENT '错误信息',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `ext_seattype` varchar(2) DEFAULT NULL COMMENT '扩展坐席',
  `level` varchar(10) DEFAULT '0' COMMENT '订单级别:0、普通。1、10元VIP1。  2、20元VIP2。  5、SVIP ',
  `ps_fail_reason` varchar(200) DEFAULT NULL COMMENT '配送失败原因',
  PRIMARY KEY (`order_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_status_time` (`order_status`(2),`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='19e火车票送票上门信息表';

/*Table structure for table `hc_orderinfo_refundstream` */

DROP TABLE IF EXISTS `hc_orderinfo_refundstream`;

CREATE TABLE `hc_orderinfo_refundstream` (
  `stream_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `eop_order_id` varchar(50) DEFAULT NULL COMMENT 'eop订单id',
  `refund_type` varchar(45) DEFAULT NULL COMMENT '退款类型：1、用户退款 2、差额退款 3、出票失败退款 4、改签差额退款 5、改签单退款6、电话退票7、取消预约退款8、线下退款33、车站退票',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT 'eop退款流水号',
  `refund_money` decimal(11,3) DEFAULT NULL COMMENT '退款金额(代理商)',
  `actual_refund_money` decimal(11,3) DEFAULT NULL COMMENT '12306实际退款金额',
  `alter_tickets_money` decimal(11,3) DEFAULT NULL COMMENT '改签差价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_purl` varchar(100) DEFAULT NULL COMMENT '小票地址',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注/出票失败原因',
  `our_remark` varchar(200) DEFAULT NULL COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refund_status` varchar(2) DEFAULT NULL COMMENT '退款状态：00、准备退款 01、重新机器改签 02、开始机器改签 03、人工改签 04、等待机器退票 05、重新机器退票 06、开始机器退票 07、人工退票 11、等待退款 22、开始退款 33、EOP退款中 44、退款完成 55、拒绝退款 99、搁置订单81、车站退票',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT NULL COMMENT '通知次数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `change_ticket_info` varchar(200) DEFAULT NULL COMMENT '改签明细',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志: 00、已出票;11、系统异常;22、查询异常;33、任务超时;44、改签超时;55、未完成单;66、大于原价;77、任务超时;88、已改签;99、改签网址;01、非法席别;02、网络繁忙;03、继续支付;04、改签失败',
  PRIMARY KEY (`stream_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_refund_seq` (`refund_seq`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_plan_time` (`refund_plan_time`),
  KEY `Ind_order_id_create_time` (`order_id`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=418667 DEFAULT CHARSET=utf8 COMMENT='退款流水表';

/*Table structure for table `hc_orderinfo_refundstream_bak10_1` */

DROP TABLE IF EXISTS `hc_orderinfo_refundstream_bak10_1`;

CREATE TABLE `hc_orderinfo_refundstream_bak10_1` (
  `stream_id` int(11) NOT NULL DEFAULT '0' COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `eop_order_id` varchar(50) DEFAULT NULL COMMENT 'eop订单id',
  `refund_type` varchar(45) DEFAULT NULL COMMENT '退款类型：1、用户退款 2、差额退款 3、出票失败退款 4、改签差额退款 5、改签单退款',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT 'eop退款流水号',
  `refund_money` decimal(11,3) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,3) DEFAULT NULL COMMENT '实际退款金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_purl` varchar(100) DEFAULT NULL COMMENT '小票地址',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '用户方备注',
  `our_remark` varchar(200) DEFAULT NULL COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refund_status` varchar(2) DEFAULT NULL COMMENT '退款状态：00、准备退款 11、等待退款 22、开始退款 33、EOP退款中 44、退款完成 55、拒绝退款',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT NULL COMMENT '通知次数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_regist` */

DROP TABLE IF EXISTS `hc_orderinfo_regist`;

CREATE TABLE `hc_orderinfo_regist` (
  `regist_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '注册id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `ids_card` varchar(32) DEFAULT NULL COMMENT '二代身份证号',
  `regist_phone` varchar(32) DEFAULT NULL COMMENT '注册帐号电话',
  `user_phone` varchar(32) DEFAULT NULL COMMENT '联系电话',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `mail` varchar(100) DEFAULT NULL COMMENT '12306注册邮箱',
  `mail_pwd` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `user_id` varchar(50) DEFAULT NULL COMMENT '关联代理商id',
  `regist_status` varchar(2) DEFAULT NULL COMMENT '状态 00、等待注册 11、注册中 22、注册成功 33、注册失败 44、人工注册 55、需要检查账户状态， 56、正在检查账户状态   88、需要激活账户  89正在激活账户',
  `fail_reason` varchar(200) DEFAULT NULL COMMENT '注册失败原因：1、实名制身份信息有误 2、身份信息已使用 3、邮箱已被注册（身份信息待核验或者身份信息有误） ',
  `description` varchar(100) DEFAULT NULL COMMENT '人工注册说明',
  `regist_time` datetime DEFAULT NULL COMMENT '注册开始时间',
  `regist_num` int(3) DEFAULT '0' COMMENT '注册次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `account_id` int(11) DEFAULT NULL COMMENT '关联账号id(注册成功后填入)',
  `channel` varchar(25) DEFAULT NULL COMMENT '渠道',
  `is_output` varchar(2) DEFAULT '0' COMMENT '是否已经输出到账号表0：未输出 1：已输出',
  `account_source` varchar(2) DEFAULT '1' COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `mobile_status` varchar(2) DEFAULT '01' COMMENT '01等待修改 02 准备修改 03 正在修改 04 成功修改 05 修改失败',
  `mobile_time` datetime DEFAULT NULL COMMENT '手机修改操作时间',
  `mobile_reason` varchar(2) DEFAULT NULL COMMENT '手机修改失败错误码',
  `mobile_desp` varchar(100) DEFAULT NULL COMMENT '手机修改错误原因描述',
  PRIMARY KEY (`regist_id`),
  KEY `IND_hc_orderinfo_regist_user_id` (`user_id`),
  KEY `IND_hc_orderinfo_regist_regist_status` (`regist_status`),
  KEY `IND_hc_orderinfo_regist_regist_time` (`regist_time`),
  KEY `IND_hc_orderinfo_regist_regist_status_create_time` (`regist_status`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_regist_backup` */

DROP TABLE IF EXISTS `hc_orderinfo_regist_backup`;

CREATE TABLE `hc_orderinfo_regist_backup` (
  `regist_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '注册id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `ids_card` varchar(50) DEFAULT NULL COMMENT '二代身份证号',
  `user_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `account_name` varchar(50) DEFAULT NULL COMMENT '12306账号名',
  `account_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `mail` varchar(100) DEFAULT NULL COMMENT '12306注册邮箱',
  `mail_pwd` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `user_id` varchar(50) DEFAULT NULL COMMENT '关联代理商id',
  `regist_status` varchar(2) DEFAULT NULL COMMENT '状态 00、等待注册 11、注册中 22、注册成功 33、注册失败 44、人工注册 55、需要核验',
  `fail_reason` varchar(2) DEFAULT NULL COMMENT '注册失败原因：1、实名制身份信息有误 2、身份信息已使用  ',
  `description` varchar(100) DEFAULT NULL COMMENT '人工注册说明',
  `regist_time` datetime DEFAULT NULL COMMENT '注册开始时间',
  `regist_num` int(3) DEFAULT '0' COMMENT '注册次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `account_id` int(11) DEFAULT NULL COMMENT '关联账号id(注册成功后填入)',
  `channel` varchar(25) DEFAULT NULL COMMENT '渠道',
  `is_output` varchar(2) DEFAULT '0' COMMENT '是否已经输出到账号表0：未输出 1：已输出',
  `account_source` varchar(2) DEFAULT '1' COMMENT '账号来源：1、19e 2、批量导入 3、人工添加 4、其他',
  `regist_phone` varchar(50) DEFAULT NULL COMMENT '导入电话',
  PRIMARY KEY (`regist_id`)
) ENGINE=InnoDB AUTO_INCREMENT=252211 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_orderinfo_reserve_notice` */

DROP TABLE IF EXISTS `hc_orderinfo_reserve_notice`;

CREATE TABLE `hc_orderinfo_reserve_notice` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '请求放票时间状态：00、未请求 11、已请求放票时间 ',
  `from_time` datetime DEFAULT NULL COMMENT '发车时间',
  `begin_time` int(6) DEFAULT NULL COMMENT '放票时间点 HH:mm:ss 格式化字符串',
  `out_ticket_status` varchar(2) DEFAULT '00' COMMENT '请求预订状态：00未请求 11、已请求',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预约订票通知表';

/*Table structure for table `hc_productinfo` */

DROP TABLE IF EXISTS `hc_productinfo`;

CREATE TABLE `hc_productinfo` (
  `product_id` varchar(15) NOT NULL COMMENT '产品id',
  `type` int(2) DEFAULT NULL COMMENT '产品类别 0:自有产品 1:保险',
  `name` varchar(32) DEFAULT NULL COMMENT '产品名称',
  `status` int(2) DEFAULT NULL COMMENT '产品状态 0:未上架 1:上架',
  `province_id` varchar(10) DEFAULT NULL COMMENT '所属省份id',
  `city_id` varchar(10) DEFAULT NULL COMMENT '所属城市id',
  `sale_type` varchar(2) DEFAULT NULL COMMENT '计费方式 0：元/件 1：元/月 2：元/年',
  `sale_price` decimal(11,3) DEFAULT NULL COMMENT '售价',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `describe` varchar(800) DEFAULT NULL COMMENT '产品描述',
  `level` varchar(2) DEFAULT NULL COMMENT '级别 0：普通 1：vip',
  `coverage` decimal(10,0) DEFAULT NULL COMMENT '保额',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品表';

/*Table structure for table `hc_productinfo_test` */

DROP TABLE IF EXISTS `hc_productinfo_test`;

CREATE TABLE `hc_productinfo_test` (
  `product_id` varchar(15) NOT NULL COMMENT '产品id',
  `type` int(2) DEFAULT NULL COMMENT '产品类别 0:自有产品 1:保险',
  `name` varchar(32) DEFAULT NULL COMMENT '产品名称',
  `status` int(2) DEFAULT NULL COMMENT '产品状态 0:未上架 1:上架',
  `province_id` varchar(10) DEFAULT NULL COMMENT '所属省份id',
  `city_id` varchar(10) DEFAULT NULL COMMENT '所属城市id',
  `sale_type` varchar(2) DEFAULT NULL COMMENT '计费方式 0：元/件 1：元/月 2：元/年',
  `sale_price` decimal(11,3) DEFAULT NULL COMMENT '售价',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `describe` varchar(800) DEFAULT NULL COMMENT '产品描述',
  `level` varchar(2) DEFAULT NULL COMMENT '级别 0：普通 1：vip',
  `coverage` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品表';

/*Table structure for table `hc_refundstation` */

DROP TABLE IF EXISTS `hc_refundstation`;

CREATE TABLE `hc_refundstation` (
  `station_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `order_status` varchar(20) DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=610 DEFAULT CHARSET=utf8 COMMENT='手动导入订单表';

/*Table structure for table `hc_refundstation_tj` */

DROP TABLE IF EXISTS `hc_refundstation_tj`;

CREATE TABLE `hc_refundstation_tj` (
  `station_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `count` int(11) DEFAULT NULL COMMENT '总数',
  `success_num` int(11) DEFAULT NULL COMMENT '符合条件数',
  `fail_num` int(11) DEFAULT NULL COMMENT '已退款数',
  `again_num` int(11) DEFAULT NULL COMMENT '重复数据数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='手动导入订单统计表';

/*Table structure for table `hc_rob_orderinfo` */

DROP TABLE IF EXISTS `hc_rob_orderinfo`;

CREATE TABLE `hc_rob_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `ctrip_order_id` varchar(50) DEFAULT NULL COMMENT '抢票订单传给携程生成的订单号，咱们自己生成',
  `fromTo_zh` varchar(50) DEFAULT NULL COMMENT '出发到达站中文',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '客户支付总金额',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '12306官网票面总额',
  `order_status` varchar(10) DEFAULT NULL COMMENT '出票状态 00、开始出票 01、重发出票 10、出票失败 11、正在预定  33、请求抢票成功 44、预定人工 55、扣位成功（开始支付） 56、重新支付  61、人工支付 , 70.退款中 71 退款成功 85、开始取消 83、正在取消  84、取消重发 86、取消人工 87、取消失败  88、支付成功 99、出票成功 ',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` int(11) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(20) DEFAULT NULL COMMENT '车次',
  `train_no_accept` varchar(100) DEFAULT NULL COMMENT '"|"分割备用车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_time` varchar(20) DEFAULT NULL COMMENT '乘车日期',
  `seat_type` varchar(4) DEFAULT NULL COMMENT '座席类型：9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座    F:动卧  A:高级动卧  H:包厢硬卧',
  `seat_type_accept` varchar(30) DEFAULT NULL COMMENT '"|"分割备用座席类型：9、商务座 P、特等座 M、一等座  O、二等座 6、高级软卧 4、软卧  3、硬卧 2、 软座  1、硬座  0、无座  F:动卧  A:高级动卧   H:包厢硬卧',
  `account_id` varchar(50) DEFAULT '0' COMMENT '账号ID',
  `worker_id` int(11) DEFAULT NULL COMMENT '处理人账号',
  `out_ticket_account` varchar(45) DEFAULT NULL COMMENT '出票账号',
  `bank_pay_seq` varchar(50) DEFAULT NULL COMMENT '银行支付流水号',
  `error_info` varchar(145) DEFAULT '6' COMMENT '错误信息：1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验9。系统异常 12、信息冒用 13.排队人数过多高铁管家要求失败【qunar】错误信息：0、其他 1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符 4、车次数据与12306不一致 5、乘客信息错误 6、12306乘客身份信息核验失败 7、身份信息被冒用1',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道ID',
  `level` varchar(10) DEFAULT '0' COMMENT '订单级别',
  `pay_type` varchar(2) DEFAULT '0' COMMENT '支付方式：默认0机器支付，1是手动支付',
  `is_pay` varchar(2) DEFAULT NULL COMMENT '是否支付：00：已支付；11：未支付',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '简要错误信息',
  `pro_bak2` varchar(2) DEFAULT '00' COMMENT '00：正常 11：补单',
  `pay_limit_time` datetime DEFAULT NULL,
  `manual_order` varchar(2) DEFAULT NULL COMMENT '出票方式 11：人工出票 00：系统出票',
  `wait_for_order` varchar(2) DEFAULT NULL COMMENT '12306系统故障是否继续出票 11：继续出票 00：不继续出票 （目前只针对同程渠道）',
  `device_type` tinyint(4) DEFAULT '0' COMMENT '0--PC端预订  1-APP端预订',
  `from_3c` varchar(6) DEFAULT NULL COMMENT '出发三字码',
  `to_3c` varchar(6) DEFAULT NULL COMMENT ' 到达三字码',
  `account_from_way` int(2) DEFAULT '0' COMMENT '账号来源： 0：公司自有账号 ； 1：12306自带账号',
  `pay_money_ext_sum` decimal(11,3) DEFAULT NULL COMMENT '咱们把抢票订单传给携程出票时，携程收取咱们的抢票服务费总价（咱们给携程的服务费，乘客总人数*单人抢票服务费）',
  `buy_money_ext_sum` decimal(11,3) DEFAULT NULL COMMENT '抢票时咱们收取客户的抢票服务费总价（客户给咱们的服务费，乘客总人数*单人抢票服务费）',
  `final_train_no` varchar(20) DEFAULT NULL COMMENT '最终抢到的车次',
  `pay_serial_number` varchar(50) DEFAULT NULL COMMENT '支付序列号',
  `final_seat_type` varchar(4) DEFAULT NULL COMMENT '最终抢到的座席类型 座席类型：9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座    F:动卧  A:高级动卧  H:包厢硬卧',
  `leak_cut_offTime` int(11) DEFAULT NULL COMMENT '捡漏截止日期',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '订单通知联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '订单通知联系电话',
  `leak_cut_str` varchar(50) DEFAULT '' COMMENT '捡漏截止时间 date 字符串',
  `cancel_time` datetime DEFAULT NULL,
  `refund_time` datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `idx_jl_create_time` (`create_time`),
  KEY `IND_jl_orderinfo_out_ticket_billno` (`out_ticket_billno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='火车票抢票信息表';

/*Table structure for table `hc_rob_orderinfo_cp` */

DROP TABLE IF EXISTS `hc_rob_orderinfo_cp`;

CREATE TABLE `hc_rob_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型   1：成人票   2：儿童票   3:学生票',
  `cert_type` varchar(11) DEFAULT NULL COMMENT '证件类型 证件类型1、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `cert_no` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` varchar(45) DEFAULT NULL COMMENT '支付价格',
  `buy_money` varchar(45) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` varchar(100) DEFAULT NULL COMMENT '座席类型：9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座    F:动卧  A:高级动卧  H:包厢硬卧',
  `train_no` varchar(20) DEFAULT NULL COMMENT '车次',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `check_status` varchar(2) DEFAULT NULL COMMENT '12306身份核验状态 0、已通过 1、审核中 2、未通过',
  `pay_money_ext` varchar(11) DEFAULT NULL COMMENT '咱们把抢票订单传给携程出票时，携程收取咱们的单人抢票服务费（咱们给携程的服务费，单人的）',
  `buy_money_ext` varchar(11) DEFAULT NULL COMMENT '抢票时咱们收取客户的单人抢票服务费（客户给咱们的服务费，单人的）',
  `OrderTicketPrice` varchar(20) DEFAULT NULL COMMENT '最终出票价格',
  `OrderTicketSeat` varchar(20) DEFAULT NULL COMMENT '最终坐席类型',
  `refund_status` varchar(20) DEFAULT NULL COMMENT '退款状态(开始退票,退票成功,退票失败)',
  PRIMARY KEY (`cp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `hc_rob_orderinfo_history` */

DROP TABLE IF EXISTS `hc_rob_orderinfo_history`;

CREATE TABLE `hc_rob_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表ID',
  `order_id` varchar(45) DEFAULT NULL COMMENT '抢票订单号',
  `order_optlog` varchar(500) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `hc_rob_orderinfo_notify` */

DROP TABLE IF EXISTS `hc_rob_orderinfo_notify`;

CREATE TABLE `hc_rob_orderinfo_notify` (
  `task_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '任务表主键',
  `order_id` varchar(45) DEFAULT NULL COMMENT '抢票订单号',
  `task_type` varchar(10) DEFAULT NULL COMMENT '任务类型  1：预定任务  2：支付任务  3：取消任务 4：向订单系统发送通知',
  `task_num` int(10) DEFAULT NULL COMMENT '任务次数',
  `task_status` varchar(10) DEFAULT NULL COMMENT '任务状态 00：开始任务 11：正在任务  22：任务成功  33：任务失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `hc_statinfo` */

DROP TABLE IF EXISTS `hc_statinfo`;

CREATE TABLE `hc_statinfo` (
  `stat_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '统计表id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `out_ticket_succeed` int(10) DEFAULT NULL COMMENT '出票成功的个数',
  `out_ticket_defeated` int(10) DEFAULT NULL COMMENT '出票失败的个数',
  `preparative_count` int(10) DEFAULT NULL COMMENT '预下单的个数',
  `pay_defeated` int(10) DEFAULT NULL COMMENT '支付失败的个数',
  `refund_count` int(10) DEFAULT NULL COMMENT '退款完成的个数',
  `ticket_count` int(10) DEFAULT NULL COMMENT '出售的票数',
  `order_count` int(10) DEFAULT NULL COMMENT '总订单',
  `succeed_money` decimal(11,3) DEFAULT NULL COMMENT '成功金额',
  `defeated_money` decimal(11,3) DEFAULT NULL COMMENT '失败金额',
  `bx_count` int(10) DEFAULT NULL COMMENT '保险个数',
  `bx_countMoney` decimal(11,3) DEFAULT NULL COMMENT '保险总额',
  `succeed_odds` varchar(10) DEFAULT NULL COMMENT '成功转换率',
  `defeated_odds` varchar(10) DEFAULT NULL COMMENT '失败转换率',
  `succeed_cgl` varchar(10) DEFAULT NULL COMMENT '订单成功率',
  `succeed_sbl` varchar(10) DEFAULT NULL COMMENT '订单失败率',
  `activeAgent` int(10) DEFAULT NULL COMMENT '活跃用户数',
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4494 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_statinfo_province` */

DROP TABLE IF EXISTS `hc_statinfo_province`;

CREATE TABLE `hc_statinfo_province` (
  `stat_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表_id 自增',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `province_name` varchar(20) DEFAULT NULL COMMENT '省名',
  `province_id` int(11) DEFAULT NULL COMMENT '省id',
  `user_count` int(11) DEFAULT NULL COMMENT '用户总数',
  `activeAgent` int(11) DEFAULT NULL COMMENT '活跃用户数',
  `apply_count` int(11) DEFAULT NULL COMMENT '申请条数',
  `not_pass` int(11) DEFAULT NULL COMMENT '未通过条数',
  `wait_pass` int(11) DEFAULT NULL COMMENT '等待审核条数',
  `order_count` int(11) DEFAULT NULL COMMENT '订单数',
  `ticket_count` int(11) DEFAULT NULL COMMENT '票数',
  `bx_count` int(11) DEFAULT NULL COMMENT '保险个数',
  `bx_Money_Sum` decimal(11,3) DEFAULT NULL COMMENT '保险价钱',
  `succeed_count` int(11) DEFAULT NULL COMMENT '出票成功个数',
  `defeated_count` int(11) DEFAULT NULL COMMENT '出票失败个数',
  `want_outTicket` int(11) DEFAULT NULL COMMENT '预下单个数',
  `pay_fall` int(11) DEFAULT NULL COMMENT '支付失败个数',
  `succeed_money` decimal(11,3) DEFAULT NULL COMMENT '出票成功价钱',
  `defeated_money` decimal(11,3) DEFAULT NULL COMMENT '出票失败价钱',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `today_money` decimal(11,3) DEFAULT NULL COMMENT '当日交易成功金额',
  `mouth_count` int(11) DEFAULT NULL COMMENT '当月订单数',
  `last_mouth_count` int(11) DEFAULT NULL COMMENT '上月订单数',
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35334 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_supplier_orderinfo` */

DROP TABLE IF EXISTS `hc_supplier_orderinfo`;

CREATE TABLE `hc_supplier_orderinfo` (
  `order_id` varchar(45) NOT NULL COMMENT '供货商订单号',
  `order_name` varchar(45) DEFAULT NULL COMMENT '订单名称',
  `supplier_id` varchar(45) DEFAULT NULL COMMENT '供货商ID',
  `pay_money` varchar(45) DEFAULT NULL COMMENT '价格',
  `income_money` varchar(45) DEFAULT NULL COMMENT '收入',
  `order_status` varchar(10) DEFAULT NULL COMMENT '00、等待抢票  11、正在出票  22、出票完成 ',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='供货商订单表';

/*Table structure for table `hc_supplierinfo` */

DROP TABLE IF EXISTS `hc_supplierinfo`;

CREATE TABLE `hc_supplierinfo` (
  `supplier_id` int(11) NOT NULL,
  `supplier_name` varchar(45) DEFAULT NULL,
  `dealer_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='供货商信息表';

/*Table structure for table `hc_system_config` */

DROP TABLE IF EXISTS `hc_system_config`;

CREATE TABLE `hc_system_config` (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `province_id` varchar(10) DEFAULT NULL COMMENT '省份',
  `is_open` varchar(10) DEFAULT NULL COMMENT '开通状态：00、关闭 11、开通',
  `is_cost` varchar(10) DEFAULT NULL COMMENT '是否付费：00、不  11、是',
  `is_ps` varchar(10) DEFAULT NULL COMMENT '是否开通配送 00、否 11、是',
  `is_buyable` varchar(10) DEFAULT NULL COMMENT '是否可以购买 00、否 11、是',
  `rule_content` varchar(145) DEFAULT NULL COMMENT '查询规则',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '操作人\n',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='火车票系统配置';

/*Table structure for table `hc_system_log` */

DROP TABLE IF EXISTS `hc_system_log`;

CREATE TABLE `hc_system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=304 DEFAULT CHARSET=utf8;

/*Table structure for table `hc_system_setting` */

DROP TABLE IF EXISTS `hc_system_setting`;

CREATE TABLE `hc_system_setting` (
  `setting_id` varchar(50) NOT NULL COMMENT '主键',
  `setting_name` varchar(50) DEFAULT NULL COMMENT '名称',
  `setting_value` varchar(2000) DEFAULT NULL COMMENT '内容',
  `setting_status` varchar(2) DEFAULT NULL COMMENT '状态 0:关闭 1:启用',
  `setting_desc` varchar(100) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`setting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统设置表';

/*Table structure for table `hc_telephone` */

DROP TABLE IF EXISTS `hc_telephone`;

CREATE TABLE `hc_telephone` (
  `phone_number` varchar(15) NOT NULL COMMENT '手机号',
  `message` varchar(500) DEFAULT NULL COMMENT '短信内容',
  `src` varchar(15) DEFAULT NULL COMMENT '短息来源号码',
  `recieve_time` varchar(50) DEFAULT NULL COMMENT '接收时间',
  `phone_status` varchar(10) DEFAULT '00' COMMENT '手机号状态：00、未使用 11、被提取 22、发送短信 33、收到短信',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `channel` varchar(10) DEFAULT NULL COMMENT '手机号来源',
  PRIMARY KEY (`phone_number`),
  KEY `idx_hc_telephone_phone_status` (`phone_status`),
  KEY `idx_hc_telephone_create_time` (`create_time`),
  KEY `idx_hc_telephone_channel` (`channel`),
  KEY `idx_hc_telephone_recieve_time` (`recieve_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='核验手机号信息表';

/*Table structure for table `hc_train_base` */

DROP TABLE IF EXISTS `hc_train_base`;

CREATE TABLE `hc_train_base` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `train_no` varchar(25) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(25) DEFAULT NULL COMMENT '出发车站',
  `to_city` varchar(25) DEFAULT NULL COMMENT '到达车站',
  `from_time` time DEFAULT NULL COMMENT '出发时间',
  `to_time` time DEFAULT NULL COMMENT '到达时间',
  `begin_city` varchar(25) DEFAULT NULL COMMENT '始发站',
  `end_city` varchar(25) DEFAULT NULL COMMENT '终点站',
  `train_type` varchar(25) DEFAULT NULL COMMENT '列车类别',
  `time_cost` time DEFAULT NULL COMMENT '历时',
  `yz_price` varchar(50) DEFAULT NULL COMMENT '硬座票价',
  `rz_price` varchar(50) DEFAULT NULL COMMENT '软座票价',
  `yw_price` varchar(50) DEFAULT NULL COMMENT '硬卧票价(上/中/下)',
  `rw_price` varchar(50) DEFAULT NULL COMMENT '软卧票价(上/下)',
  `ydz_price` varchar(50) DEFAULT NULL COMMENT '一等座票价',
  `edz_price` varchar(50) DEFAULT NULL COMMENT '二等座票价',
  `gw_price` varchar(50) DEFAULT NULL COMMENT '高级软卧票价(上/下)',
  `swz_price` varchar(50) DEFAULT NULL COMMENT '商务座票价',
  `tdz_price` varchar(50) DEFAULT NULL COMMENT '特等座票价',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `modify_by` varchar(25) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_train_no` (`train_no`),
  KEY `idx_from_city` (`from_city`),
  KEY `idx_to_city` (`to_city`)
) ENGINE=InnoDB AUTO_INCREMENT=390478 DEFAULT CHARSET=utf8 COMMENT='列车基本信息';

/*Table structure for table `hc_userinfo` */

DROP TABLE IF EXISTS `hc_userinfo`;

CREATE TABLE `hc_userinfo` (
  `user_id` varchar(50) NOT NULL COMMENT '代理商id',
  `province_id` varchar(10) DEFAULT NULL COMMENT '省id',
  `city_id` varchar(10) DEFAULT NULL COMMENT '市id',
  `district_id` varchar(10) DEFAULT NULL COMMENT '区（县）id',
  `shop_type` varchar(2) DEFAULT NULL COMMENT '0手机充值店、1鲜花礼品店、2小型超市、3大型超市、4烟酒店、5报刊亭、6票务代售点、7彩票代售点、8旅行社、9其他 10网吧',
  `shop_name` varchar(50) DEFAULT NULL COMMENT '店铺名称',
  `shop_short_name` varchar(20) DEFAULT NULL COMMENT '店铺简称',
  `user_name` varchar(50) DEFAULT NULL COMMENT '代理商姓名',
  `user_phone` varchar(25) DEFAULT NULL COMMENT '联系电话',
  `user_qq` varchar(20) DEFAULT NULL COMMENT 'qq',
  `user_address` varchar(200) DEFAULT NULL COMMENT '配送地址',
  `user_level` varchar(2) DEFAULT NULL COMMENT '级别 0 普通用户 1 vip用户 2 免费用户 3 对外商户',
  `estate` varchar(2) DEFAULT NULL COMMENT '状态 00需要付费 01 绑定账号 11等待审核 22审核未通过 33审核通过 44需要续费',
  `auditing_time` datetime DEFAULT NULL COMMENT '审核通过时间',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `product_id` varchar(15) DEFAULT NULL COMMENT '当前购买产品id',
  `jm_order_id` varchar(50) DEFAULT NULL COMMENT '最近购买加盟订单号',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  `agent_grade` varchar(2) DEFAULT NULL COMMENT '代理商等级：11、金牌用户 22、银牌用户 33、铜牌用户 44、普通用户',
  `ext_channel` varchar(50) DEFAULT NULL COMMENT '对外商户渠道，合作商户编号',
  PRIMARY KEY (`user_id`),
  KEY `idx_province_id` (`province_id`),
  KEY `idx_city_id` (`city_id`),
  KEY `idx_estate` (`estate`),
  KEY `idx_apply_time` (`apply_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='加盟用户信息';

/*Table structure for table `hc_userinfo_check` */

DROP TABLE IF EXISTS `hc_userinfo_check`;

CREATE TABLE `hc_userinfo_check` (
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `ids_card` varchar(50) NOT NULL COMMENT '身份证号',
  `status` varchar(2) DEFAULT NULL COMMENT '状态：0、已通过 1、待审核 2、未通过',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_name`,`ids_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `hc_userinfo_link` */

DROP TABLE IF EXISTS `hc_userinfo_link`;

CREATE TABLE `hc_userinfo_link` (
  `link_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '常用联系人id',
  `user_id` varchar(50) NOT NULL COMMENT '代理商id',
  `link_name` varchar(50) NOT NULL COMMENT '联系人姓名',
  `ids_type` varchar(2) NOT NULL COMMENT '证件类型 ２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `user_ids` varchar(50) NOT NULL COMMENT '证件号码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `buy_num` bigint(20) DEFAULT NULL COMMENT '买票次数',
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8 COMMENT='12306账号常用联系人';

/*Table structure for table `hc_userinfo_linknotify` */

DROP TABLE IF EXISTS `hc_userinfo_linknotify`;

CREATE TABLE `hc_userinfo_linknotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '通知表id',
  `req_seq` varchar(50) DEFAULT NULL COMMENT '请求流水号',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、等待通知 11、开始通知 22、通知成功',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='添加常用联系人通知表';

/*Table structure for table `hc_userinfo_register` */

DROP TABLE IF EXISTS `hc_userinfo_register`;

CREATE TABLE `hc_userinfo_register` (
  `register_id` varchar(50) NOT NULL COMMENT '主键ID',
  `user_id` varchar(50) DEFAULT NULL COMMENT '代理商id',
  `user_name` varchar(100) DEFAULT NULL COMMENT '注册12306账号姓名',
  `is_registed` varchar(1) DEFAULT NULL COMMENT '是否注册过12306账号 0：未注册 1：注册过',
  `sex` varchar(1) DEFAULT NULL COMMENT '注册12306账号性别,1:男；0：女',
  `ids_card` varchar(25) DEFAULT NULL COMMENT '二代身份证号',
  `user_account` varchar(100) DEFAULT NULL COMMENT '12306账号',
  `user_password` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `status` varchar(2) DEFAULT NULL COMMENT '账号状态：00、等待注册 11、注册中 22、注册/认证失败 33、等待认证 44、认证中 55、认证通过 66、认证未通过',
  `fail_reason` varchar(2) DEFAULT NULL COMMENT '失败原因：1、邮箱激活失败 2、12306账号激活失败 3、用户注册身份信息有误 4、用户账号信息有误 5、身份证号已被占用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `req_num` int(3) DEFAULT '0' COMMENT '请求次数',
  `req_time` datetime DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`register_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代理商12306账号信息';

/*Table structure for table `hc_userorder_tj` */

DROP TABLE IF EXISTS `hc_userorder_tj`;

CREATE TABLE `hc_userorder_tj` (
  `user_id` varchar(50) NOT NULL COMMENT '代理商id',
  `pre_month_total` decimal(11,3) DEFAULT NULL COMMENT '上月总计',
  `this_month_total` decimal(11,3) DEFAULT NULL COMMENT '本月总计',
  `last_pay_money` decimal(11,3) DEFAULT NULL COMMENT '最后订购金额',
  `last_pay_time` datetime DEFAULT NULL COMMENT '最后订购时间',
  `modify_time` datetime DEFAULT NULL COMMENT '更新时间',
  `pay_pre_count` bigint(10) DEFAULT NULL COMMENT '上月订单数',
  `pay_now_count` bigint(10) DEFAULT NULL COMMENT '当月订单数',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户订单统计';

/*Table structure for table `id_user` */

DROP TABLE IF EXISTS `id_user`;

CREATE TABLE `id_user` (
  `ids_card` varchar(25) DEFAULT NULL,
  `user_name` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `inner_noticeinfo` */

DROP TABLE IF EXISTS `inner_noticeinfo`;

CREATE TABLE `inner_noticeinfo` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `notice_name` varchar(45) DEFAULT NULL COMMENT '通知名称',
  `notice_status` varchar(10) DEFAULT NULL COMMENT '通知状态 00、未发布 11、已发布  22、已到期',
  `notice_content` varchar(8000) DEFAULT NULL COMMENT '通知内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pub_time` datetime DEFAULT NULL COMMENT '生效时间',
  `stop_time` datetime DEFAULT NULL COMMENT '到期时间',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '创建人',
  `inner_channel` varchar(45) DEFAULT NULL COMMENT '内嵌渠道：cmpay、19pay、ccb',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='内嵌通知表';

/*Table structure for table `inner_oftentraininfo` */

DROP TABLE IF EXISTS `inner_oftentraininfo`;

CREATE TABLE `inner_oftentraininfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `train_code` varchar(19) DEFAULT '',
  `from_station` varchar(10) DEFAULT '',
  `arrive_station` varchar(10) DEFAULT '',
  `price` float DEFAULT '0',
  `start_station` varchar(10) DEFAULT '',
  `end_station` varchar(10) DEFAULT '',
  `station` varchar(10) DEFAULT '',
  `show_order` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='内嵌常用车站查询表';

/*Table structure for table `inner_orderinfo` */

DROP TABLE IF EXISTS `inner_orderinfo`;

CREATE TABLE `inner_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `ticket_pay_money` decimal(11,3) DEFAULT NULL COMMENT '票价总额',
  `bx_pay_money` decimal(11,3) DEFAULT '0.000' COMMENT '保险总额',
  `ps_pay_money` decimal(11,3) DEFAULT '0.000' COMMENT '配送总额',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 00、预下单 01、准备预定 11、支付成功  22、正在预订 33、预订成功 34、正在取消 44、出票成功 45、出票失败 88、订单完成 99、',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户账号',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `timeout` varchar(2) DEFAULT NULL COMMENT '超时重发（1:需要重发 2:重发完成）',
  `refund_deadline_ignore` varchar(2) DEFAULT NULL COMMENT '1：无视截止时间（发车12小时后为退款截止时间）',
  `refund_total` decimal(11,3) DEFAULT NULL COMMENT '退款金额总计',
  `can_refund` varchar(2) DEFAULT NULL COMMENT '是否可以退款（默认可以退 0:不能退 1:能退）',
  `ext_seat` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `pay_seq` varchar(50) DEFAULT NULL COMMENT '支付平台交易流水号',
  `order_source` varchar(2) DEFAULT NULL COMMENT 'cmpay_订单来源：1、pc端， 2、mobile',
  `inner_channel` varchar(45) DEFAULT NULL COMMENT '内嵌渠道：cmpay、19pay、ccb、chq',
  `itemNo` varchar(45) DEFAULT NULL COMMENT '建行缴费项目编号',
  `cityName` varchar(30) DEFAULT NULL COMMENT '建行城市名称编号',
  `plat_order_id` varchar(45) DEFAULT NULL COMMENT '19pay订单号/cmpay_订单流水号',
  `refund_url` varchar(100) DEFAULT NULL COMMENT '19pay退款地址',
  `ticket_url` varchar(100) DEFAULT NULL COMMENT '19pay出票结果通知地址',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `cm_phone` varchar(20) DEFAULT NULL COMMENT 'cmpay手机号',
  `pay_channel` varchar(10) DEFAULT NULL COMMENT '支付渠道',
  PRIMARY KEY (`order_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_billno` (`out_ticket_billno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内嵌火车票订单信息表';

/*Table structure for table `inner_orderinfo_cp` */

DROP TABLE IF EXISTS `inner_orderinfo_cp`;

CREATE TABLE `inner_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `ids_type` int(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_inner_2` (`order_id`),
  CONSTRAINT `FK_Reference_inner_2` FOREIGN KEY (`order_id`) REFERENCES `inner_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内嵌火车票订购信息表';

/*Table structure for table `inner_orderinfo_history` */

DROP TABLE IF EXISTS `inner_orderinfo_history`;

CREATE TABLE `inner_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '退款创建时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8 COMMENT='内嵌历史表';

/*Table structure for table `inner_orderinfo_refundstream` */

DROP TABLE IF EXISTS `inner_orderinfo_refundstream`;

CREATE TABLE `inner_orderinfo_refundstream` (
  `stream_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `refund_type` varchar(45) DEFAULT NULL COMMENT '退款类型：1、用户退款 2、差额退款 3、出票失败退款 4、改签差额退款 5、改签单退款 6、线下退款',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT 'cmpay返回的退款交易流水号',
  `refund_money` decimal(11,3) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,3) DEFAULT NULL COMMENT '实际退款金额',
  `alter_tickets_money` decimal(11,3) DEFAULT NULL COMMENT '改签差价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_purl` varchar(100) DEFAULT NULL COMMENT '小票地址',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注/出票失败原因',
  `our_remark` varchar(200) DEFAULT NULL COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refund_status` varchar(2) DEFAULT NULL COMMENT '退款状态：00、准备退款 11、等待退款 22、开始退款  44、退款完成 55、拒绝退款 66、线下退款审核 77、线下退款中',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT NULL COMMENT '通知次数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `change_ticket_info` varchar(200) DEFAULT NULL COMMENT '改签明细',
  `inner_channel` varchar(45) DEFAULT NULL COMMENT '内嵌渠道：cmpay、19pay、ccb、chq',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志',
  `pay_channel` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`stream_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_refund_type` (`refund_type`),
  KEY `idx_refund_seq` (`refund_seq`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_refund_status` (`refund_status`),
  KEY `idx_plan_status` (`refund_plan_time`),
  KEY `idx_notify_num` (`notify_num`)
) ENGINE=InnoDB AUTO_INCREMENT=8708 DEFAULT CHARSET=utf8 COMMENT='内嵌退款流水表';

/*Table structure for table `inner_orderinfo_returnnotify` */

DROP TABLE IF EXISTS `inner_orderinfo_returnnotify`;

CREATE TABLE `inner_orderinfo_returnnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '出票失败原因',
  `plat_order_id` varchar(50) DEFAULT NULL COMMENT '19pay订单号',
  `order_status` varchar(2) DEFAULT NULL COMMENT '订单状态: 33、预订成功 44、出票成功 45、出票失败 ',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内嵌表订单处理结果通知表';

/*Table structure for table `inner_productinfo` */

DROP TABLE IF EXISTS `inner_productinfo`;

CREATE TABLE `inner_productinfo` (
  `product_id` varchar(15) NOT NULL COMMENT '产品id',
  `type` int(2) DEFAULT NULL COMMENT '产品类别 0:自有产品 1:保险',
  `name` varchar(32) DEFAULT NULL COMMENT '产品名称',
  `status` int(2) DEFAULT NULL COMMENT '产品状态 0:未上架 1:上架',
  `province_id` varchar(10) DEFAULT NULL COMMENT '所属省份id',
  `city_id` varchar(10) DEFAULT NULL COMMENT '所属城市id',
  `sale_type` varchar(2) DEFAULT NULL COMMENT '计费方式 0：元/件 1：元/月 2：元/年',
  `sale_price` decimal(11,3) DEFAULT NULL COMMENT '售价',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `describe` varchar(800) DEFAULT NULL COMMENT '产品描述',
  `level` varchar(2) DEFAULT NULL COMMENT '级别 0：普通 1：vip',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内嵌产品表';

/*Table structure for table `inner_system_log` */

DROP TABLE IF EXISTS `inner_system_log`;

CREATE TABLE `inner_system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

/*Table structure for table `inner_system_setting` */

DROP TABLE IF EXISTS `inner_system_setting`;

CREATE TABLE `inner_system_setting` (
  `setting_id` varchar(50) NOT NULL COMMENT '主键',
  `setting_name` varchar(50) DEFAULT NULL COMMENT '名称',
  `setting_value` varchar(2000) DEFAULT NULL COMMENT '内容',
  `setting_status` varchar(2) DEFAULT NULL COMMENT '状态 0:关闭 1:启用',
  `setting_desc` varchar(100) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`setting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内嵌系统设置表';

/*Table structure for table `inner_userinfo_link` */

DROP TABLE IF EXISTS `inner_userinfo_link`;

CREATE TABLE `inner_userinfo_link` (
  `link_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '常用联系人id',
  `user_id` varchar(50) NOT NULL COMMENT '代理商id',
  `link_name` varchar(50) NOT NULL COMMENT '联系人姓名',
  `ids_type` varchar(2) NOT NULL COMMENT '证件类型 ２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `user_ids` varchar(50) NOT NULL COMMENT '证件号码',
  `channel` varchar(20) DEFAULT NULL COMMENT '渠道',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `buy_num` bigint(20) DEFAULT NULL COMMENT '买票次数',
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8 COMMENT='内嵌常用联系人';

/*Table structure for table `ip_account` */

DROP TABLE IF EXISTS `ip_account`;

CREATE TABLE `ip_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `acc_username` varchar(20) NOT NULL COMMENT '账号',
  `acc_password` varchar(20) NOT NULL COMMENT '密码',
  `status` varchar(2) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1356718 DEFAULT CHARSET=utf8;

/*Table structure for table `ip_addressinfo` */

DROP TABLE IF EXISTS `ip_addressinfo`;

CREATE TABLE `ip_addressinfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `merchant_id` varchar(20) NOT NULL DEFAULT '' COMMENT '渠道',
  `ip_address` varchar(20) NOT NULL DEFAULT '' COMMENT 'ip地址',
  `ip_status` varchar(2) DEFAULT NULL COMMENT 'ip状态：0启用、1停用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_person` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `jd_account` */

DROP TABLE IF EXISTS `jd_account`;

CREATE TABLE `jd_account` (
  `jd_id` int(32) NOT NULL AUTO_INCREMENT COMMENT '主键  京东账号ID',
  `account_name` varchar(20) DEFAULT NULL COMMENT '账号名',
  `account_pwd` varchar(20) DEFAULT NULL COMMENT '账号密码',
  `account_paypwd` varchar(20) DEFAULT NULL COMMENT '账号支付密码',
  `account_email` varchar(30) DEFAULT NULL COMMENT '账号邮箱',
  `account_emailpwd` varchar(30) DEFAULT NULL COMMENT '账号邮箱密码',
  `account_status` varchar(2) DEFAULT '88' COMMENT '账号状态:00空闲; 11使用中; 22领券成功; 33领券失败; 44消费成功; 55消费失败; 88待领劵; 99停用;',
  `account_remark` varchar(100) DEFAULT NULL COMMENT '账号备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`jd_id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;

/*Table structure for table `jd_card` */

DROP TABLE IF EXISTS `jd_card`;

CREATE TABLE `jd_card` (
  `card_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `card_no` varchar(64) NOT NULL COMMENT '预付卡账号',
  `card_pwd` varchar(16) NOT NULL COMMENT '预付卡密码',
  `card_money` varchar(16) NOT NULL COMMENT '预付卡余额',
  `card_amount` varchar(11) DEFAULT NULL COMMENT '预付卡面额',
  `card_status` varchar(2) DEFAULT NULL COMMENT '预付卡状态:  00：空闲  11：使用中  22：停用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `become_due_time` datetime DEFAULT NULL COMMENT '预付卡到期时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `update_status` int(1) DEFAULT NULL,
  `img_url` varchar(32) DEFAULT NULL COMMENT '余额截图路径',
  PRIMARY KEY (`card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;

/*Table structure for table `jd_card_bill` */

DROP TABLE IF EXISTS `jd_card_bill`;

CREATE TABLE `jd_card_bill` (
  `自增长ID` int(11) NOT NULL AUTO_INCREMENT,
  `预付卡ID` int(11) DEFAULT NULL,
  `订单号` varchar(32) DEFAULT NULL,
  `支付流水号` varchar(32) NOT NULL,
  PRIMARY KEY (`自增长ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `jd_orderinfo` */

DROP TABLE IF EXISTS `jd_orderinfo`;

CREATE TABLE `jd_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '系统订单号',
  `jd_order_id` varchar(50) DEFAULT NULL COMMENT '京东订单号',
  `jd_order_no` varchar(50) DEFAULT NULL COMMENT '京东流水号',
  `jd_id` int(11) DEFAULT NULL COMMENT '京东账号id',
  `go_status` varchar(2) DEFAULT NULL COMMENT '处理状态 00:初始状态 11:请求出票成功 22:请求出票失败 33:出票流程结束 44:切入12306出票 55:开始查询结果',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_ren` varchar(25) DEFAULT NULL COMMENT '操作人',
  `acc_id` int(11) DEFAULT NULL COMMENT '12306账号id',
  `find_time` datetime DEFAULT NULL COMMENT '查询开始时间',
  `query_num` int(11) DEFAULT '0' COMMENT '查询次数',
  `pay_money` varchar(255) DEFAULT NULL COMMENT '支付金额(12306官网支付金额)',
  `coupon_no` varchar(32) DEFAULT NULL COMMENT '礼品卷编号',
  `jd_pay_money` varchar(255) DEFAULT NULL COMMENT '京东实际支付的金额',
  `jd_coupon_money` varchar(255) DEFAULT NULL COMMENT '京东优惠券支付金额',
  `klt_order_no` varchar(32) DEFAULT NULL COMMENT '开联通流水号',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='京东订单信息表';

/*Table structure for table `jd_orderinfo_log` */

DROP TABLE IF EXISTS `jd_orderinfo_log`;

CREATE TABLE `jd_orderinfo_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `content` varchar(500) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='京东火车票订单日志表';

/*Table structure for table `jdpay_corr_order` */

DROP TABLE IF EXISTS `jdpay_corr_order`;

CREATE TABLE `jdpay_corr_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cp_orderId` varchar(60) DEFAULT NULL COMMENT '出票系统id',
  `jdpay_tradeNum` varchar(60) DEFAULT NULL COMMENT '传递给jd的交易流水号',
  `jd_user` varchar(100) DEFAULT NULL COMMENT '京东用户',
  `jd_orderId` varchar(60) DEFAULT NULL COMMENT '京东订单号',
  `jd_userPhone` varchar(20) DEFAULT NULL COMMENT '京东订单联系手机号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `jdpay_person` */

DROP TABLE IF EXISTS `jdpay_person`;

CREATE TABLE `jdpay_person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jd_user` varchar(60) DEFAULT NULL COMMENT '京东用户',
  `user_name` varchar(60) DEFAULT NULL COMMENT '用户名',
  `ticket_type` varchar(10) DEFAULT NULL COMMENT '车票类型0：成人票1：儿童票',
  `user_ids` varchar(20) DEFAULT NULL COMMENT '证件号码',
  `ids_type` varchar(10) DEFAULT NULL COMMENT '1、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `jl_ctrip_refund` */

DROP TABLE IF EXISTS `jl_ctrip_refund`;

CREATE TABLE `jl_ctrip_refund` (
  `ctrip_trade_no` varchar(50) NOT NULL COMMENT '携程唯一退款 单号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '抢票订单号',
  `refund_money` varchar(20) DEFAULT NULL COMMENT '本次退款金额',
  `ctrip_ref_time` varchar(30) DEFAULT NULL COMMENT '携程发起退款金额',
  `create_time` varchar(30) DEFAULT NULL COMMENT '入库时间',
  PRIMARY KEY (`ctrip_trade_no`),
  KEY `index_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `jl_merchant` */

DROP TABLE IF EXISTS `jl_merchant`;

CREATE TABLE `jl_merchant` (
  `merchant_id` varchar(50) NOT NULL COMMENT '商户ID',
  `merchant_name` varchar(50) DEFAULT NULL,
  `id_prefix` varchar(10) DEFAULT NULL COMMENT '在jl_orderinfo 表中的订单号前缀',
  `account_id` varchar(50) DEFAULT NULL COMMENT '19e平台账号',
  `account_name` varchar(64) DEFAULT NULL COMMENT '19e平台登录名',
  `access_key` varchar(64) DEFAULT NULL COMMENT '代理商抢票接入KEY(接口权限校验)',
  `contact_person` varchar(20) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `ip_addr` varchar(20) DEFAULT NULL COMMENT 'ip地址',
  `call_back_url` varchar(100) DEFAULT NULL COMMENT '消息通知异步回调地址',
  `succ_count` int(11) DEFAULT NULL COMMENT '出票成功总数',
  `cancel_count` int(11) DEFAULT NULL COMMENT '取消总数',
  `book_count` int(11) DEFAULT NULL COMMENT '预订总数',
  `return_count` int(11) DEFAULT NULL COMMENT '退票总数',
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `jl_merchant_notify` */

DROP TABLE IF EXISTS `jl_merchant_notify`;

CREATE TABLE `jl_merchant_notify` (
  `notify_id` varchar(50) NOT NULL,
  `merchant_id` varchar(50) DEFAULT NULL COMMENT '商户ID',
  `jl_order_id` varchar(50) DEFAULT NULL COMMENT '抢票订单号',
  `notify_data` varchar(1000) DEFAULT NULL COMMENT '通知JSON',
  `notify_status` varchar(1) DEFAULT NULL COMMENT '通知状态(0 失败 ,1 成功)',
  `notify_biz_type` varchar(20) DEFAULT NULL COMMENT '通知业务类型',
  `notify_times` tinyint(4) DEFAULT NULL COMMENT '通知次数',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `jl_orderinfo` */

DROP TABLE IF EXISTS `jl_orderinfo`;

CREATE TABLE `jl_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `ctrip_order_id` varchar(50) DEFAULT NULL COMMENT '抢票订单传给携程生成的订单号，咱们自己生成',
  `fromTo_zh` varchar(50) DEFAULT NULL COMMENT '出发到达站中文',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '客户支付金额',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '官网票面总额',
  `order_status` varchar(10) DEFAULT NULL COMMENT '出票状态 00、开始出票 01、重发出票 10、出票失败 11、正在预定  33、请求抢票成功 44、预定人工 55、扣位成功（开始支付） 56、重新支付  61、人工支付  85、开始取消 83、正在取消  84、取消重发 86、取消人工 87、取消失败  88、支付成功 99、出票成功 71 退款成功 72 退款人工',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` int(11) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(20) DEFAULT NULL COMMENT '车次',
  `train_no_accept` varchar(100) DEFAULT NULL COMMENT '"|"分割备用车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` varchar(4) DEFAULT NULL COMMENT '座席类型：9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座    F:动卧  A:高级动卧  H:包厢硬卧',
  `seat_type_accept` varchar(30) DEFAULT NULL COMMENT '"|"分割备用座席类型：9、商务座 P、特等座 M、一等座  O、二等座 6、高级软卧 4、软卧  3、硬卧 2、 软座  1、硬座  0、无座  F:动卧  A:高级动卧   H:包厢硬卧',
  `account_id` varchar(50) DEFAULT '0' COMMENT '账号ID',
  `worker_id` int(11) DEFAULT NULL COMMENT '处理人账号',
  `out_ticket_account` varchar(45) DEFAULT NULL COMMENT '出票账号',
  `bank_pay_seq` varchar(50) DEFAULT NULL COMMENT '银行支付流水号',
  `error_info` varchar(145) DEFAULT '6' COMMENT '错误信息：1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验9。系统异常 12、信息冒用 13.排队人数过多高铁管家要求失败【qunar】错误信息：0、其他 1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符 4、车次数据与12306不一致 5、乘客信息错误 6、12306乘客身份信息核验失败 7、身份信息被冒用1',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道ID',
  `level` varchar(10) DEFAULT '0' COMMENT '订单级别',
  `pay_type` varchar(2) DEFAULT '0' COMMENT '支付方式：默认0机器支付，1是手动支付',
  `is_pay` varchar(2) DEFAULT NULL COMMENT '是否支付：00：已支付；11：未支付',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '简要错误信息',
  `pro_bak2` varchar(2) DEFAULT '00' COMMENT '00：正常 11：补单',
  `pay_limit_time` datetime DEFAULT NULL,
  `manual_order` varchar(2) DEFAULT NULL COMMENT '出票方式 11：人工出票 00：系统出票',
  `wait_for_order` varchar(2) DEFAULT NULL COMMENT '12306系统故障是否继续出票 11：继续出票 00：不继续出票 （目前只针对同程渠道）',
  `device_type` tinyint(4) DEFAULT '0' COMMENT '0--PC端预订  1-APP端预订',
  `from_3c` varchar(6) DEFAULT NULL COMMENT '出发三字码',
  `to_3c` varchar(6) DEFAULT NULL COMMENT ' 到达三字码',
  `account_from_way` int(2) DEFAULT '0' COMMENT '账号来源： 0：公司自有账号 ； 1：12306自带账号',
  `pay_money_ext_sum` decimal(11,3) DEFAULT NULL COMMENT '咱们把抢票订单传给携程出票时，携程收取咱们的抢票服务费总价（咱们给携程的服务费，乘客总人数*单人抢票服务费）',
  `buy_money_ext_sum` decimal(11,3) DEFAULT NULL COMMENT '抢票时咱们收取客户的抢票服务费总价（客户给咱们的服务费，乘客总人数*单人抢票服务费）',
  `final_train_no` varchar(20) DEFAULT NULL COMMENT '最终抢到的车次',
  `pay_serial_number` varchar(50) DEFAULT NULL COMMENT '支付序列号',
  `final_seat_type` varchar(4) DEFAULT NULL COMMENT '最终抢到的座席类型 座席类型：9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座    F:动卧  A:高级动卧  H:包厢硬卧',
  `leak_cut_offTime` datetime DEFAULT NULL COMMENT '捡漏截止日期',
  `refund_time` datetime DEFAULT NULL,
  `ext_1` varchar(255) DEFAULT '' COMMENT '扩展字段',
  `ext_2` varchar(255) DEFAULT '' COMMENT '扩展字段2',
  `ext_3` varchar(255) DEFAULT '' COMMENT '扩展字段',
  `ext_4` varchar(255) DEFAULT '' COMMENT '扩展字段4',
  `ext_5` varchar(255) DEFAULT '' COMMENT '扩展字段5',
  PRIMARY KEY (`order_id`),
  KEY `idx_jl_create_time` (`create_time`),
  KEY `IND_jl_orderinfo_out_ticket_billno` (`out_ticket_billno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='火车票抢票信息表';

/*Table structure for table `jl_orderinfo_cp` */

DROP TABLE IF EXISTS `jl_orderinfo_cp`;

CREATE TABLE `jl_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型   1：成人票   2：儿童票   3:学生票',
  `cert_type` varchar(11) DEFAULT NULL COMMENT '''证件类型 证件类型1、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照''',
  `cert_no` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` varchar(45) DEFAULT NULL COMMENT '支付价格',
  `buy_money` varchar(45) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` varchar(50) DEFAULT NULL COMMENT '座席类型：9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座    F:动卧  A:高级动卧  H:包厢硬卧',
  `train_no` varchar(20) DEFAULT NULL COMMENT '车次',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `check_status` varchar(2) DEFAULT NULL COMMENT '12306身份核验状态 0、已通过 1、审核中 2、未通过',
  `pay_money_ext` varchar(11) DEFAULT NULL COMMENT '咱们把抢票订单传给携程出票时，携程收取咱们的单人抢票服务费（咱们给携程的服务费，单人的）',
  `buy_money_ext` varchar(11) DEFAULT NULL COMMENT '抢票时咱们收取客户的单人抢票服务费（客户给咱们的服务费，单人的）',
  `ext_1` varchar(255) DEFAULT '' COMMENT '扩展字段',
  `ext_2` varchar(255) DEFAULT '' COMMENT '扩展字段2',
  `ext_3` varchar(255) DEFAULT '' COMMENT '扩展字段',
  `ext_4` varchar(255) DEFAULT '' COMMENT '扩展字段4',
  `ext_5` varchar(255) DEFAULT '' COMMENT '扩展字段5',
  `OrderTicketPrice` varchar(20) DEFAULT NULL COMMENT '最终出票价格',
  `OrderTicketSeat` varchar(20) DEFAULT NULL COMMENT '最终坐席类型',
  `refund_status` varchar(20) DEFAULT NULL COMMENT '退款状态(开始退票,退票成功,退票失败)',
  PRIMARY KEY (`cp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `jl_orderinfo_history` */

DROP TABLE IF EXISTS `jl_orderinfo_history`;

CREATE TABLE `jl_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表ID',
  `order_id` varchar(45) DEFAULT NULL COMMENT '抢票订单号',
  `order_optlog` varchar(500) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2264 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `jl_orderinfo_notify` */

DROP TABLE IF EXISTS `jl_orderinfo_notify`;

CREATE TABLE `jl_orderinfo_notify` (
  `task_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '任务表主键',
  `order_id` varchar(45) DEFAULT NULL COMMENT '抢票订单号',
  `task_type` varchar(10) DEFAULT NULL COMMENT '任务类型  1：预定任务  2：支付任务  3：取消任务 4：向订单系统发送通知',
  `task_num` int(10) DEFAULT NULL COMMENT '任务次数',
  `task_status` varchar(10) DEFAULT NULL COMMENT '任务状态 00：开始任务 11：正在任务  22：任务成功  33：任务失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1329 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `jl_orderinfo_refund` */

DROP TABLE IF EXISTS `jl_orderinfo_refund`;

CREATE TABLE `jl_orderinfo_refund` (
  `re_id` varchar(64) NOT NULL COMMENT '抢票退款记录流水号',
  `rob_order_id` varchar(128) NOT NULL COMMENT '抢票订单号码',
  `eop_order_id` varchar(128) NOT NULL COMMENT 'eop订单号码',
  `eop_refund_seq` varchar(128) DEFAULT NULL COMMENT 'EOP退款流水 唯一',
  `refund_money` varchar(32) DEFAULT NULL COMMENT '退款金额',
  `refund_status` varchar(10) DEFAULT NULL COMMENT '退款状态(00 退款中,11 退款成功 22 退款失败)',
  `opt_time` datetime DEFAULT NULL COMMENT '操作时间',
  `opt_log` varchar(255) DEFAULT NULL COMMENT '操作日志',
  PRIMARY KEY (`re_id`),
  KEY `rob_id_index` (`rob_order_id`) USING BTREE,
  KEY `eop_seq_index` (`eop_refund_seq`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `lccc` */

DROP TABLE IF EXISTS `lccc`;

CREATE TABLE `lccc` (
  `cc` varchar(19) DEFAULT NULL,
  `lcdj` varchar(1) DEFAULT NULL,
  `kt` varchar(1) DEFAULT NULL,
  `tz` varchar(4) DEFAULT NULL,
  `pjsf` decimal(19,0) DEFAULT NULL,
  `sfz` varchar(10) DEFAULT NULL,
  `zdz` varchar(10) DEFAULT NULL,
  `zsf` decimal(19,0) DEFAULT NULL,
  `xh` int(11) DEFAULT NULL,
  `bj` int(11) DEFAULT NULL,
  `kxgl` int(11) DEFAULT NULL,
  `kxzq` int(11) DEFAULT NULL,
  `ksrq` varchar(8) DEFAULT NULL,
  `jsrq` varchar(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `login_logs` */

DROP TABLE IF EXISTS `login_logs`;

CREATE TABLE `login_logs` (
  `login_log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志表id',
  `user_id` varchar(20) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(20) DEFAULT NULL COMMENT '登录名',
  `real_name` varchar(20) DEFAULT NULL COMMENT '用户真实姓名',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `login_ip` varchar(20) DEFAULT NULL COMMENT '最后登录ip',
  PRIMARY KEY (`login_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8850 DEFAULT CHARSET=utf8;

/*Table structure for table `login_userinfo` */

DROP TABLE IF EXISTS `login_userinfo`;

CREATE TABLE `login_userinfo` (
  `user_id` varchar(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户名',
  `real_name` varchar(10) DEFAULT NULL COMMENT '用户真实姓名',
  `password` varchar(20) DEFAULT NULL COMMENT '密码',
  `email` varchar(20) DEFAULT NULL COMMENT '邮箱',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `user_IsOpen` varchar(10) DEFAULT NULL COMMENT '账号是否打开 0。启用，1.停用',
  `user_status` varchar(10) DEFAULT NULL COMMENT '用户状态 0。等待审核，1.审核通过，2.审核未通过',
  `user_level` varchar(10) DEFAULT NULL COMMENT '用户级别 0。普通用户，1.高级用户，1.1。省级负责人，2.管理员',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(20) DEFAULT NULL COMMENT '最后登录ip',
  `supervise_name` varchar(150) DEFAULT NULL COMMENT '负责区域',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mt_allchannel_notify` */

DROP TABLE IF EXISTS `mt_allchannel_notify`;

CREATE TABLE `mt_allchannel_notify` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_notify_status` varchar(2) DEFAULT NULL COMMENT '通知出票系统状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `cp_notify_num` int(11) DEFAULT '0' COMMENT '通知出票系统次数',
  `cp_notify_time` datetime DEFAULT NULL COMMENT '通知出票系统时间',
  `cp_notify_finish_time` datetime DEFAULT NULL COMMENT '通知出票系统完成时间',
  `book_notify_status` varchar(2) DEFAULT NULL COMMENT '预订结果通知状态',
  `book_notify_num` int(4) DEFAULT '0' COMMENT '预订结果通知次数',
  `book_notify_time` datetime DEFAULT NULL COMMENT '预订结果通知时间',
  `book_notify_url` varchar(100) DEFAULT NULL COMMENT '预订结果异步回调地址',
  `book_notify_finish_time` datetime DEFAULT NULL COMMENT '预订结果通知结束时间',
  `out_notify_status` varchar(2) DEFAULT NULL COMMENT '通知订单结果状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `out_notify_num` int(11) DEFAULT '0' COMMENT '通知订单结果次数',
  `out_notify_time` datetime DEFAULT NULL COMMENT '通知订单结果时间',
  `out_notify_finish_time` datetime DEFAULT NULL COMMENT '通知订单结果完成时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `channel` varchar(15) DEFAULT NULL COMMENT '渠道名称 meituan',
  `out_notify_url` varchar(100) DEFAULT NULL COMMENT '通知订单结果回调地址',
  `cp_notify_level` int(4) DEFAULT NULL COMMENT '通知出票系统优先级 num越大优先级越高',
  PRIMARY KEY (`id`),
  KEY `idx_out_notify_status` (`out_notify_status`),
  KEY `idx_out_notify_num` (`out_notify_num`),
  KEY `idx_out_notify_time` (`out_notify_time`),
  KEY `idx_mt_notify_orderId` (`order_id`) USING BTREE,
  KEY `idx_mt_notify_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13794 DEFAULT CHARSET=utf8 COMMENT='美团渠道订单通知表';

/*Table structure for table `mt_orderinfo` */

DROP TABLE IF EXISTS `mt_orderinfo`;

CREATE TABLE `mt_orderinfo` (
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '订单号',
  `order_name` varchar(20) DEFAULT NULL COMMENT '订单名',
  `order_level` varchar(2) DEFAULT NULL COMMENT '订单等级',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `order_status` varchar(10) DEFAULT '00' COMMENT '订单状态: 00下单成功 11通知出票成功 22预订成功 33出票成功 44出票失败   51撤销中 52撤销失败 88 超时订单 24 取消成功',
  `notice_status` varchar(10) DEFAULT '00' COMMENT '通知状态:00、准备通知  11、开始通知  22、通知完成  33、通知失败',
  `order_time` datetime DEFAULT NULL COMMENT '用户下单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `out_fail_reason` varchar(2) DEFAULT NULL COMMENT '出票失败原因：1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符',
  `train_no` varchar(25) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_date` date DEFAULT NULL COMMENT '乘车时间',
  `elong_seat_type` varchar(2) DEFAULT NULL COMMENT 'meituan座位类型 :1硬座 2硬卧上 3硬卧中 4硬卧下 5软座 6软卧上 7软卧中 8软卧下 9商务座 10观光座 11一等包座 12特等座 13一等座 14二等座 15高级软卧上 16高级软卧下 17无座 18一人软包 10动卧 21高级动卧 22包厢硬卧',
  `seat_type` varchar(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `passenger_reason` varchar(500) DEFAULT NULL COMMENT '乘客错误信息',
  `ext_field1` varchar(100) DEFAULT '1' COMMENT '备选1(备选坐席)',
  `ext_field2` varchar(100) DEFAULT NULL COMMENT '备选2',
  `ticket_num` int(2) DEFAULT NULL COMMENT '车票内订单数量',
  `channel` varchar(15) DEFAULT NULL COMMENT '订单渠道 meituan',
  `order_type` varchar(4) DEFAULT NULL COMMENT '订单类型  11、先预订后支付 22 先支付后预订',
  `pay_limit_time` datetime DEFAULT NULL COMMENT '支付限制时间',
  `wait_for_order` varchar(2) DEFAULT NULL COMMENT '12306异常是否继续等待出票 11：继续等待 00：不继续等待',
  `lock_callback_url` varchar(255) DEFAULT NULL COMMENT '锁票异步回调地址',
  PRIMARY KEY (`order_id`),
  KEY `IND_elong_orderinfo_create_time_order_status` (`create_time`,`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='美团火车票订单表';

/*Table structure for table `mt_orderinfo_cp` */

DROP TABLE IF EXISTS `mt_orderinfo_cp`;

CREATE TABLE `mt_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `elong_ticket_type` varchar(2) DEFAULT NULL COMMENT '美团车票类型1:成人票，2:儿童票，3:学生票，4:残军票',
  `ticket_type` varchar(2) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `elong_ids_type` varchar(2) DEFAULT NULL COMMENT '美团证件类型:1:二代身份证',
  `ids_type` varchar(11) DEFAULT NULL COMMENT '证件类型１、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `elong_seat_type` varchar(2) DEFAULT NULL COMMENT '下单坐席类型 美团座位类型 :1硬座 2硬卧上 3硬卧中 4硬卧下 5软座 6软卧上 7软卧中 8软卧下 9商务座 10观光座 11一等包座 12特等座 13一等座 14二等座 15高级软卧上 16高级软卧下 17无座 18一人软包 10动卧 21高级动卧 22包厢硬卧 ',
  `seat_type` varchar(11) DEFAULT NULL COMMENT '实际坐席类型 座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '00:无退票，11:退票中 22：拒绝退票 33：退票成功，44：退票失败',
  `refund_fail_reason` varchar(200) DEFAULT NULL COMMENT '退款失败原因/拒绝退票原因',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306订单号',
  `alter_seat_type` varchar(2) DEFAULT NULL COMMENT '改签后坐席',
  `alter_train_box` varchar(5) DEFAULT NULL COMMENT '改签后车厢',
  `alter_seat_no` varchar(10) DEFAULT NULL COMMENT '改签后座位号',
  `alter_buy_money` decimal(11,3) DEFAULT NULL COMMENT '改签后成本价格',
  `alter_train_no` varchar(10) DEFAULT NULL COMMENT '改签后车次',
  `alter_money` decimal(11,3) DEFAULT NULL COMMENT '改签差额',
  `refund_12306_money` decimal(11,3) DEFAULT NULL COMMENT '12306退款金额',
  `out_passengerid` varchar(20) DEFAULT NULL COMMENT '美团车票序号',
  PRIMARY KEY (`cp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='美团火车票订购信息表';

/*Table structure for table `mt_orderinfo_logs` */

DROP TABLE IF EXISTS `mt_orderinfo_logs`;

CREATE TABLE `mt_orderinfo_logs` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `content` varchar(500) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `opt_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `order_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61447 DEFAULT CHARSET=utf8 COMMENT='美团火车票订单日志表';

/*Table structure for table `mt_orderinfo_notify` */

DROP TABLE IF EXISTS `mt_orderinfo_notify`;

CREATE TABLE `mt_orderinfo_notify` (
  `id` int(10) DEFAULT NULL,
  `order_id` varchar(150) DEFAULT NULL,
  `cp_notify_status` varchar(6) DEFAULT NULL,
  `cp_notify_num` int(11) DEFAULT NULL,
  `cp_notify_time` datetime DEFAULT NULL,
  `cp_notify_finish_time` datetime DEFAULT NULL,
  `out_notify_status` varchar(6) DEFAULT NULL,
  `out_notify_num` int(11) DEFAULT NULL,
  `out_notify_time` datetime DEFAULT NULL,
  `out_notify_finish_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mt_orderinfo_refundstream` */

DROP TABLE IF EXISTS `mt_orderinfo_refundstream`;

CREATE TABLE `mt_orderinfo_refundstream` (
  `stream_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号',
  `refund_money` decimal(11,3) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,3) DEFAULT NULL COMMENT '实际退款金额',
  `alter_tickets_money` decimal(11,3) DEFAULT NULL COMMENT '改签差价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注/出票失败原因',
  `our_remark` varchar(200) DEFAULT '' COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refuse_reason` varchar(2) DEFAULT NULL COMMENT '拒绝退款 原因：1、已取票2、已过时间 3、来电取消',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票   05：重新机器退票06：开始机器退票   07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款  55:预退票 72：生成线下退款 73：用户提交线下退款 71：线下退款通知中 81 车站退票',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` varchar(3) DEFAULT '0' COMMENT '通知次数',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败  ',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `change_ticket_info` varchar(200) DEFAULT NULL COMMENT '改签明细',
  `detail_refund` varchar(100) DEFAULT NULL COMMENT '12306详细退款金额',
  `detail_alter_tickets` varchar(100) DEFAULT NULL COMMENT '详细改签差额的金额',
  `refund_type` varchar(2) DEFAULT NULL COMMENT '退款类型 11、退票退款 22、线下退款（人工退款）33 车站退票 44 申请退款',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志',
  `channel` varchar(20) DEFAULT NULL COMMENT '退款渠道 meituan',
  `callbackurl` varchar(200) DEFAULT NULL COMMENT '退票结果回传地址',
  `reqtoken` varchar(50) DEFAULT NULL COMMENT '针对同城的请求特征 回置值',
  `user_time` datetime DEFAULT NULL COMMENT '用户退款时间',
  PRIMARY KEY (`stream_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_refund_seq` (`refund_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=152188 DEFAULT CHARSET=utf8 COMMENT='美团退款流水表';

/*Table structure for table `mt_refundstation` */

DROP TABLE IF EXISTS `mt_refundstation`;

CREATE TABLE `mt_refundstation` (
  `station_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `order_status` varchar(20) DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8 COMMENT='手动导入订单表';

/*Table structure for table `mt_refundstation_tj` */

DROP TABLE IF EXISTS `mt_refundstation_tj`;

CREATE TABLE `mt_refundstation_tj` (
  `station_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `count` int(11) DEFAULT NULL COMMENT '总数',
  `success_num` int(11) DEFAULT NULL COMMENT '符合条件数',
  `fail_num` int(11) DEFAULT NULL COMMENT '已退款数',
  `again_num` int(11) DEFAULT NULL COMMENT '重复数据数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='手动导入订单统计表';

/*Table structure for table `mt_system_log` */

DROP TABLE IF EXISTS `mt_system_log`;

CREATE TABLE `mt_system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='美团系统设置表';

/*Table structure for table `mt_system_setting` */

DROP TABLE IF EXISTS `mt_system_setting`;

CREATE TABLE `mt_system_setting` (
  `setting_id` varchar(150) DEFAULT NULL,
  `setting_name` varchar(150) DEFAULT NULL,
  `setting_value` varchar(6000) DEFAULT NULL,
  `setting_status` varchar(6) DEFAULT NULL COMMENT '状态 0:关闭 1:启用',
  `setting_desc` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='美团系统设置表';

/*Table structure for table `mt_verify_timeinfo` */

DROP TABLE IF EXISTS `mt_verify_timeinfo`;

CREATE TABLE `mt_verify_timeinfo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `cert_no` varchar(30) DEFAULT NULL COMMENT '证件号',
  `verify_time` int(8) DEFAULT NULL COMMENT '核验时间(毫秒)',
  `create_time` datetime DEFAULT NULL COMMENT '核验插入时间',
  PRIMARY KEY (`id`),
  KEY `idx_verify_createtime` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='美团核验时间信息表';

/*Table structure for table `new_robot_system_setting` */

DROP TABLE IF EXISTS `new_robot_system_setting`;

CREATE TABLE `new_robot_system_setting` (
  `robot_id` varchar(10) NOT NULL DEFAULT '' COMMENT '主键',
  `robot_name` varchar(25) DEFAULT NULL COMMENT '名称',
  `robot_url` varchar(500) DEFAULT NULL COMMENT '机器人路径',
  `robot_channel` varchar(10) DEFAULT NULL COMMENT '分配渠道：00、19e平台；11：app客户端；22：对外商户；33：代理商',
  `robot_status` varchar(2) DEFAULT NULL COMMENT '机器人类型： 1启用 2备用',
  `robot_type` varchar(10) DEFAULT NULL COMMENT '机器人类型 1、查询 2、支付、3 。。',
  `merchant_id` varchar(20) DEFAULT NULL COMMENT '针对对外渠道 商户扩充',
  PRIMARY KEY (`robot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新机器人控制表';

/*Table structure for table `notify_status` */

DROP TABLE IF EXISTS `notify_status`;

CREATE TABLE `notify_status` (
  `code` char(4) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `order_status` */

DROP TABLE IF EXISTS `order_status`;

CREATE TABLE `order_status` (
  `code` char(4) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `orderinfo_bx_old_data` */

DROP TABLE IF EXISTS `orderinfo_bx_old_data`;

CREATE TABLE `orderinfo_bx_old_data` (
  `bx_id` varchar(50) NOT NULL COMMENT '保险号主键',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '车票ID',
  `from_name` varchar(45) DEFAULT NULL COMMENT '出发地址',
  `to_name` varchar(45) DEFAULT NULL COMMENT '到达地址',
  `ids_type` varchar(2) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户姓名',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '用户证件号',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL,
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `bx_status` int(2) DEFAULT NULL COMMENT '状态：0、未发送 1、正在发送 2、发送完成 3、正在撤销 4、撤销完成 5、发送失败 6、需要取消 7、取消失败',
  `bx_code` varchar(45) DEFAULT NULL COMMENT '保险单号',
  `bx_billno` varchar(45) DEFAULT NULL COMMENT '服务器端保险号',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '进货金额',
  `product_id` varchar(15) DEFAULT NULL COMMENT '产品ID',
  `effect_date` varchar(45) DEFAULT NULL COMMENT '生效日期',
  `train_no` varchar(45) DEFAULT NULL COMMENT '车次',
  `bx_channel` varchar(2) DEFAULT NULL COMMENT '保险公司渠道: 1、快保   2、合众',
  `order_channel` varchar(30) DEFAULT NULL COMMENT '订单渠道：19e、19e前台; inner、内嵌；app、app客户端；ext、对外商户   ',
  `merchant_id` varchar(30) DEFAULT NULL COMMENT '商户编号',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`bx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='整体保险单信息';

/*Table structure for table `orderinfo_bxfp_old_data` */

DROP TABLE IF EXISTS `orderinfo_bxfp_old_data`;

CREATE TABLE `orderinfo_bxfp_old_data` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `fp_receiver` varchar(50) DEFAULT NULL COMMENT '收件人',
  `fp_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `fp_zip_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `fp_address` varchar(300) DEFAULT NULL COMMENT '收件地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `passenger` */

DROP TABLE IF EXISTS `passenger`;

CREATE TABLE `passenger` (
  `passenger_id` int(11) NOT NULL AUTO_INCREMENT,
  `id_card_no` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `reg_12306_no` varchar(30) DEFAULT NULL COMMENT '注册12306账号',
  `refer_12306_no` varchar(30) DEFAULT NULL COMMENT '关联12306账号',
  PRIMARY KEY (`passenger_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `pay_productinfo` */

DROP TABLE IF EXISTS `pay_productinfo`;

CREATE TABLE `pay_productinfo` (
  `product_id` varchar(15) NOT NULL COMMENT '产品id',
  `type` int(2) DEFAULT NULL COMMENT '产品类别 0:自有产品 1:保险',
  `name` varchar(32) DEFAULT NULL COMMENT '产品名称',
  `status` int(2) DEFAULT NULL COMMENT '产品状态 0:未上架 1:上架',
  `province_id` varchar(10) DEFAULT NULL COMMENT '所属省份id',
  `city_id` varchar(10) DEFAULT NULL COMMENT '所属城市id',
  `sale_type` varchar(2) DEFAULT NULL COMMENT '计费方式 0：元/件 1：元/月 2：元/年',
  `sale_price` decimal(11,3) DEFAULT NULL COMMENT '售价',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `describe` varchar(800) DEFAULT NULL COMMENT '产品描述',
  `level` varchar(2) DEFAULT NULL COMMENT '级别 0：普通 1：vip',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品表';

/*Table structure for table `proxy_ip` */

DROP TABLE IF EXISTS `proxy_ip`;

CREATE TABLE `proxy_ip` (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip` varchar(16) DEFAULT NULL,
  `port` int(5) DEFAULT NULL,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT '状态 0 false  ； 1 true',
  `reason` varchar(255) DEFAULT NULL,
  `order_id` varchar(32) DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `qunar_orderinfo` */

DROP TABLE IF EXISTS `qunar_orderinfo`;

CREATE TABLE `qunar_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 11、支付成功  33、预订成功 44、出票成功  45、出票失败 99、取消成功',
  `order_time` datetime DEFAULT NULL COMMENT '用户下单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `out_fail_reason` varchar(2) DEFAULT NULL COMMENT '出票失败原因：1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符',
  `train_no` varchar(25) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `seat_type` varchar(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `qunar_seat_type` varchar(2) DEFAULT NULL COMMENT 'qunar座位类型：0、站票 1、硬座 2、软座 3、一等软座 4、二等软座 5、硬卧上 6、硬卧中 7、硬卧下 8、软卧上 9、软卧下 10、高级软卧上 11、高级软卧下',
  `ext_seat` varchar(200) DEFAULT NULL COMMENT '扩展坐席',
  `qunar_ext_seat` varchar(200) DEFAULT NULL COMMENT 'qunar扩展坐席',
  `channel` varchar(45) DEFAULT 'qunar' COMMENT '渠道id',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `order_source` varchar(10) DEFAULT NULL COMMENT 'qunar订单来源（qunar1、19旅行 qunar2、久久商旅）',
  `order_type` varchar(2) DEFAULT NULL COMMENT '订单类别：0、普通订单 1、联程订单',
  `passenger_reason` varchar(500) DEFAULT NULL COMMENT '乘客错误信息',
  `ext_field1` varchar(100) DEFAULT '1' COMMENT '去哪备选1(通知出票结果状态 1：成功 0：失败) 艺龙备选：系统备选坐席',
  `ext_field2` varchar(100) DEFAULT '1' COMMENT '去哪备选2(通知占座结果状态 1：成功 0：失败) 备选2 艺龙备选（艺龙备选坐席类型）',
  `ext_field3` varchar(100) DEFAULT '1' COMMENT '去哪备选3(通知代付结果状态 1：成功 0：失败)',
  `ticket_num` int(2) DEFAULT NULL COMMENT '订单内车票数量',
  `is_pay` varchar(2) DEFAULT '00' COMMENT '是否支付：00：已支付；11：未支付；22：代付的未支付',
  `retUrl` varchar(200) DEFAULT NULL COMMENT '向去哪发送占座结果通知的地址',
  PRIMARY KEY (`order_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_billno` (`out_ticket_billno`),
  KEY `idx_order_type` (`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='去哪儿火车票订单信息表';

/*Table structure for table `qunar_orderinfo_backup` */

DROP TABLE IF EXISTS `qunar_orderinfo_backup`;

CREATE TABLE `qunar_orderinfo_backup` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 11、支付成功  33、预订成功 44、出票成功  45、出票失败 99、取消成功',
  `order_time` datetime DEFAULT NULL COMMENT '用户下单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `out_fail_reason` varchar(2) DEFAULT NULL COMMENT '出票失败原因：1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符',
  `train_no` varchar(25) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `seat_type` varchar(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `qunar_seat_type` varchar(2) DEFAULT NULL COMMENT 'qunar座位类型：0、站票 1、硬座 2、软座 3、一等软座 4、二等软座 5、硬卧上 6、硬卧中 7、硬卧下 8、软卧上 9、软卧下 10、高级软卧上 11、高级软卧下',
  `ext_seat` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `qunar_ext_seat` varchar(45) DEFAULT NULL COMMENT 'qunar扩展坐席',
  `channel` varchar(45) DEFAULT 'qunar' COMMENT '渠道id',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `order_source` varchar(10) DEFAULT NULL COMMENT 'qunar订单来源（qunar1、19旅行 qunar2、久久商旅）',
  `order_type` varchar(2) DEFAULT NULL COMMENT '订单类别：0、普通订单 1、联程订单',
  `passenger_reason` varchar(500) DEFAULT NULL COMMENT '乘客错误信息',
  `ext_field1` varchar(100) DEFAULT '1' COMMENT '去哪备选1(通知出票结果状态 1：成功 0：失败) 艺龙备选：系统备选坐席',
  `ext_field2` varchar(100) DEFAULT '1' COMMENT '去哪备选2(通知占座结果状态 1：成功 0：失败) 备选2 艺龙备选（艺龙备选坐席类型）',
  `ext_field3` varchar(100) DEFAULT '1' COMMENT '去哪备选3(通知代付结果状态 1：成功 0：失败)',
  `ticket_num` int(2) DEFAULT NULL COMMENT '订单内车票数量',
  `travel_date` date DEFAULT NULL COMMENT '乘车日期',
  `is_pay` varchar(2) DEFAULT '00' COMMENT '是否支付：00：已支付；11：未支付；22：代付的未支付',
  `retUrl` varchar(200) DEFAULT NULL COMMENT '向去哪发送占座结果通知的地址',
  PRIMARY KEY (`order_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_billno` (`out_ticket_billno`),
  KEY `idx_order_type` (`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='去哪儿火车票订单信息表';

/*Table structure for table `qunar_orderinfo_cp` */

DROP TABLE IF EXISTS `qunar_orderinfo_cp`;

CREATE TABLE `qunar_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL DEFAULT '' COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` varchar(2) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `ids_type` varchar(2) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `qunar_certtype` varchar(2) DEFAULT NULL COMMENT 'Qunar证件类型１、身份证、C、港澳通行证、G、台湾通行证、B、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `seat_type` varchar(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `qunar_seat_type` varchar(2) DEFAULT NULL COMMENT 'qunar座位类型：0、站票 1、硬座 2、软座 3、一等软座 4、二等软座 5、硬卧上 6、硬卧中 7、硬卧下 8、软卧上 9、软卧下 10、高级软卧上 11、高级软卧下',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `out_ticket_billno` varchar(50) DEFAULT NULL COMMENT '12306单号',
  `alter_seat_type` varchar(2) DEFAULT NULL COMMENT '改签后坐席',
  `alter_train_box` varchar(5) DEFAULT NULL COMMENT '改签后车厢',
  `alter_seat_no` varchar(10) DEFAULT NULL COMMENT '改签后座位号',
  `alter_buy_money` decimal(11,3) DEFAULT NULL COMMENT '改签后成本价格',
  `alter_train_no` varchar(10) DEFAULT NULL COMMENT '改签后车次',
  `alter_money` decimal(11,3) DEFAULT NULL COMMENT '改签差额',
  `refund_12306_money` decimal(11,3) DEFAULT NULL COMMENT '12306退款金额',
  `channel_ticket_type` varchar(2) DEFAULT NULL COMMENT '去哪、艺龙 车票类型',
  `channel_ids_type` varchar(2) DEFAULT NULL COMMENT '去哪、艺龙 证件类型',
  `telephone` varchar(20) DEFAULT NULL COMMENT '用户联系方式',
  `refund_money` decimal(11,3) DEFAULT NULL,
  PRIMARY KEY (`cp_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Qunar火车票车票信息表';

/*Table structure for table `qunar_orderinfo_cp_backup` */

DROP TABLE IF EXISTS `qunar_orderinfo_cp_backup`;

CREATE TABLE `qunar_orderinfo_cp_backup` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` varchar(2) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `ids_type` varchar(2) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `qunar_certtype` varchar(2) DEFAULT NULL COMMENT 'Qunar证件类型１、身份证、C、港澳通行证、G、台湾通行证、B、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `seat_type` varchar(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `qunar_seat_type` varchar(2) DEFAULT NULL COMMENT 'qunar座位类型：0、站票 1、硬座 2、软座 3、一等软座 4、二等软座 5、硬卧上 6、硬卧中 7、硬卧下 8、软卧上 9、软卧下 10、高级软卧上 11、高级软卧下',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `out_ticket_billno` varchar(50) DEFAULT NULL COMMENT '12306单号',
  `alter_seat_type` varchar(5) DEFAULT NULL COMMENT '改签后坐席',
  `alter_train_box` varchar(10) DEFAULT NULL COMMENT '改签后车厢',
  `alter_seat_no` varchar(10) DEFAULT NULL COMMENT '改签后座位号',
  `alter_buy_money` decimal(11,3) DEFAULT NULL COMMENT '改签后成本价格',
  `alter_train_no` varchar(20) DEFAULT NULL COMMENT '改签后车次',
  `alter_money` decimal(11,3) DEFAULT NULL COMMENT '改签差额',
  `refund_12306_money` decimal(11,3) DEFAULT '0.000' COMMENT '12306退款金额',
  `refund_money` decimal(11,3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qunar_orderinfo_log` */

DROP TABLE IF EXISTS `qunar_orderinfo_log`;

CREATE TABLE `qunar_orderinfo_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `content` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `opt_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `order_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1924944 DEFAULT CHARSET=utf8 COMMENT='去哪儿火车票日志表';

/*Table structure for table `qunar_orderinfo_notify` */

DROP TABLE IF EXISTS `qunar_orderinfo_notify`;

CREATE TABLE `qunar_orderinfo_notify` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_type` varchar(2) DEFAULT NULL COMMENT '订单类别：0、普通订单 1、联程订单',
  `cp_notify_status` varchar(2) DEFAULT NULL COMMENT '通知出票系统状态：00、准备通知 11、开始通知 22、通知完成',
  `cp_notify_num` int(11) DEFAULT '0' COMMENT '通知出票系统次数',
  `cp_notify_time` datetime DEFAULT NULL COMMENT '开始通知出票系统时间',
  `cp_notify_finish_time` datetime DEFAULT NULL COMMENT '通知出票系统完成时间',
  `out_notify_status` varchar(2) DEFAULT NULL COMMENT '通知出票状态：00、准备通知 11、开始通知 22、通知完成',
  `out_notify_num` int(11) DEFAULT '0' COMMENT '通知出票次数',
  `out_notify_time` datetime DEFAULT NULL COMMENT '开始通知出票时间',
  `out_notify_finish_time` datetime DEFAULT NULL COMMENT '通知出票完成时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `book_notify_status` varchar(2) DEFAULT NULL COMMENT '通知占座状态：00、准备通知 11、开始通知 22、通知完成',
  `book_notify_num` int(11) DEFAULT '0' COMMENT '通知占座次数',
  `book_notify_time` datetime DEFAULT NULL COMMENT '开始通知占座时间',
  `book_notify_finish_time` datetime DEFAULT NULL COMMENT '通知占座完成时间',
  `pay_notify_status` varchar(2) DEFAULT NULL COMMENT '代付通知状态：00、准备通知 11、开始通知 22、通知完成',
  `pay_notify_num` int(11) DEFAULT '0' COMMENT '代付通知次数',
  `pay_notify_time` datetime DEFAULT NULL COMMENT '代付开始通知时间',
  `pay_notify_finish_time` datetime DEFAULT NULL COMMENT '代付通知完成时间',
  PRIMARY KEY (`order_id`),
  KEY `idx_cp_notify_status` (`cp_notify_status`),
  KEY `idx_cp_notify_num` (`cp_notify_num`),
  KEY `idx_cp_notify_time` (`cp_notify_time`),
  KEY `idx_out_notify_status` (`out_notify_status`),
  KEY `idx_out_notify_num` (`out_notify_num`),
  KEY `idx_out_notify_time` (`out_notify_time`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_type` (`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='去哪儿火车票订单通知表';

/*Table structure for table `qunar_orderinfo_refund` */

DROP TABLE IF EXISTS `qunar_orderinfo_refund`;

CREATE TABLE `qunar_orderinfo_refund` (
  `refund_seq` varchar(50) NOT NULL COMMENT '退款流水号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_money` decimal(11,3) DEFAULT NULL COMMENT '退款金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '用户方备注',
  `our_remark` varchar(200) DEFAULT NULL COMMENT '出票方备注',
  `refuse_reason` varchar(2) DEFAULT NULL COMMENT '拒绝退票原因：1、已取票 2、已过时间 3、来电取消',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refund_status` varchar(2) DEFAULT NULL COMMENT '退款状态：00：等待机器改签;01：重新机器改签;02：开始机器改签;03：人工改签;04：等待机器退票；05：重新机器退票；06：开始机器退票；07：人工退票；11：退票完成；22：拒绝退票；33：审核退款；44：搁置订单',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成 33通知失败',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `actual_refund_money` decimal(11,3) DEFAULT NULL COMMENT '实际退款金额',
  `alter_tickets_money` decimal(11,3) DEFAULT NULL COMMENT '改签差价',
  `order_source` varchar(10) DEFAULT NULL COMMENT '订单来源',
  `detail_refund` varchar(100) DEFAULT NULL COMMENT '12306详细退款金额',
  `detail_alter_tickets` varchar(100) DEFAULT NULL COMMENT '详细改签金额(改签差额)',
  `order_type` varchar(2) DEFAULT '0' COMMENT '去哪儿订单类型',
  `change_ticket_info` varchar(200) DEFAULT NULL COMMENT '改签明细',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `refund_type` varchar(2) DEFAULT NULL COMMENT '退款类型  艺龙退款类型：退款类型 11、退票退款 22、线下退款（人工退款） ',
  `refund_channel` varchar(2) DEFAULT NULL COMMENT '退款渠道 11 qunar 22 elong',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志',
  PRIMARY KEY (`refund_seq`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_refund_status` (`refund_status`),
  KEY `idx_notify_num` (`notify_num`),
  KEY `idx_notify_status` (`notify_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='qunar退款表';

/*Table structure for table `qunar_orderinfo_trip` */

DROP TABLE IF EXISTS `qunar_orderinfo_trip`;

CREATE TABLE `qunar_orderinfo_trip` (
  `trip_id` varchar(50) NOT NULL COMMENT '联程订单号',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `trip_seq` varchar(5) DEFAULT NULL COMMENT '联程序号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 11、支付成功  33、预订成功 44、出票成功  45、出票失败',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_fail_reason` varchar(2) DEFAULT NULL COMMENT '出票失败原因：1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票 22、配送票',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `seat_type` varchar(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他',
  `qunar_seat_type` varchar(2) DEFAULT NULL COMMENT 'qunar座位类型：0、站票 1、硬座 2、软座 3、一等软座 4、二等软座 5、硬卧上 6、硬卧中 7、硬卧下 8、软卧上 9、软卧下 10、高级软卧上 11、高级软卧下',
  `ext_seat` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `qunar_ext_seat` varchar(45) DEFAULT NULL COMMENT 'qunar扩展坐席',
  `order_source` varchar(25) DEFAULT NULL COMMENT 'qunar订单来源（qunar1、quanr2）',
  `channel` varchar(50) DEFAULT NULL COMMENT '渠道id',
  PRIMARY KEY (`trip_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_billno` (`out_ticket_billno`),
  KEY `trip_seq` (`trip_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='去哪儿火车票联程订单信息表';

/*Table structure for table `qunar_statinfo` */

DROP TABLE IF EXISTS `qunar_statinfo`;

CREATE TABLE `qunar_statinfo` (
  `stat_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '统计表id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `out_ticket_succeed` int(10) DEFAULT NULL COMMENT '出票成功的个数',
  `out_ticket_defeated` int(10) DEFAULT NULL COMMENT '出票失败的个数',
  `refund_count` int(10) DEFAULT NULL COMMENT '退款完成的个数',
  `ticket_count` int(10) DEFAULT NULL COMMENT '出售的票数',
  `order_count` int(10) DEFAULT NULL COMMENT '总订单',
  `succeed_money` decimal(11,3) DEFAULT NULL COMMENT '成功金额',
  `defeated_money` decimal(11,3) DEFAULT NULL COMMENT '失败金额',
  `succeed_odds` varchar(10) DEFAULT NULL COMMENT '成功转换率',
  `defeated_odds` varchar(10) DEFAULT NULL COMMENT '失败转换率',
  `succeed_cgl` varchar(10) DEFAULT NULL COMMENT '订单成功率',
  `succeed_sbl` varchar(10) DEFAULT NULL COMMENT '订单失败率',
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8;

/*Table structure for table `qunar_system_log` */

DROP TABLE IF EXISTS `qunar_system_log`;

CREATE TABLE `qunar_system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Table structure for table `qunar_system_setting` */

DROP TABLE IF EXISTS `qunar_system_setting`;

CREATE TABLE `qunar_system_setting` (
  `setting_id` varchar(150) DEFAULT NULL,
  `setting_name` varchar(150) DEFAULT NULL,
  `setting_value` varchar(6000) DEFAULT NULL,
  `setting_status` varchar(6) DEFAULT NULL,
  `setting_desc` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `refund_check` */

DROP TABLE IF EXISTS `refund_check`;

CREATE TABLE `refund_check` (
  `order_id` varchar(50) NOT NULL,
  `eop_order_id` varchar(50) DEFAULT NULL,
  `refund_money` decimal(11,3) DEFAULT NULL,
  `send_time` datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `refund_note` */

DROP TABLE IF EXISTS `refund_note`;

CREATE TABLE `refund_note` (
  `order_id` varchar(50) NOT NULL,
  `dealer_id` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `from_time` datetime DEFAULT NULL,
  `train_no` varchar(10) DEFAULT NULL,
  `order_name` varchar(50) DEFAULT NULL,
  `refund_total` decimal(11,3) DEFAULT NULL,
  `chajia` decimal(12,3) DEFAULT NULL,
  `user_phone` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `register12306` */

DROP TABLE IF EXISTS `register12306`;

CREATE TABLE `register12306` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `book_name` varchar(20) DEFAULT NULL,
  `book_password` varchar(20) DEFAULT NULL,
  `real_name` varchar(20) NOT NULL,
  `real_id` varchar(25) NOT NULL,
  `email` varchar(30) DEFAULT NULL,
  `email_password` varchar(20) DEFAULT NULL,
  `status` char(1) DEFAULT NULL COMMENT '0为未激活，1为已激活,2为邮箱需要激活',
  `passenger` int(11) DEFAULT '1' COMMENT '常用联系人人数',
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `request_type` */

DROP TABLE IF EXISTS `request_type`;

CREATE TABLE `request_type` (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` char(4) DEFAULT NULL,
  `title` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`auto_id`),
  KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Table structure for table `rh_history_login` */

DROP TABLE IF EXISTS `rh_history_login`;

CREATE TABLE `rh_history_login` (
  `id` int(22) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `agent_id` varchar(22) NOT NULL COMMENT '代理商id',
  `login_time` datetime NOT NULL COMMENT '登入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='打码器合作伙伴';

/*Table structure for table `rh_login_notes` */

DROP TABLE IF EXISTS `rh_login_notes`;

CREATE TABLE `rh_login_notes` (
  `id` int(22) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `login_hour` varchar(22) NOT NULL COMMENT '时区 ',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `login_num` int(22) DEFAULT NULL COMMENT '代理商登入次数',
  `login_data` date DEFAULT NULL COMMENT '日期',
  `stop_num` int(5) DEFAULT NULL COMMENT '代理商暂停次数',
  `off_num` int(5) DEFAULT NULL COMMENT '代理商注销次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='打码器合作伙伴';

/*Table structure for table `rh_partner` */

DROP TABLE IF EXISTS `rh_partner`;

CREATE TABLE `rh_partner` (
  `partner_id` varchar(22) NOT NULL COMMENT '合作伙伴ID',
  `partner_key` varchar(22) NOT NULL COMMENT '合作伙伴密钥',
  `partner_status` varchar(2) NOT NULL COMMENT '合作伙伴账号状态 00启用 11失效',
  PRIMARY KEY (`partner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='打码器合作伙伴';

/*Table structure for table `rh_picture` */

DROP TABLE IF EXISTS `rh_picture`;

CREATE TABLE `rh_picture` (
  `pic_id` varchar(45) NOT NULL COMMENT '图片ID',
  `pic_filename` varchar(100) NOT NULL COMMENT '图片保存文件名',
  `verify_code` varchar(45) DEFAULT NULL COMMENT '图片验证码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `effect_time` datetime DEFAULT NULL COMMENT '有效时间',
  `start_time` datetime DEFAULT NULL COMMENT '客户端获取码时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '操作人',
  `status` varchar(45) DEFAULT NULL COMMENT '状态：00、未输入 11、已输入',
  `channel` varchar(45) NOT NULL COMMENT '渠道ID  合作伙伴ID',
  `user_pic_status` int(11) DEFAULT '0' COMMENT '图片是否被用户占用 0 闲置图片 1正在处理中图片',
  `result_status` int(2) DEFAULT '5' COMMENT '图片验证状态：初始值为5， 0 验证失败， 1验证成功。',
  `partner_id` varchar(10) DEFAULT NULL,
  `partner_key` varchar(10) DEFAULT NULL,
  `partner_status` varchar(5) DEFAULT NULL,
  `update_status` varchar(2) DEFAULT '22' COMMENT '上传状态 11、成功 22、未上传     ',
  `shyzmid` varchar(25) DEFAULT NULL COMMENT '商户验证码id（用于反馈商户 验证码错误）',
  `back_status` varchar(2) DEFAULT '00' COMMENT '反馈商户验证结果 00默认 11再次反馈 22反馈失败 33反馈成功',
  `order_id` varchar(32) DEFAULT NULL COMMENT '订单号',
  `fail_reason` varchar(200) DEFAULT NULL COMMENT '失败原因',
  `department` varchar(50) DEFAULT NULL COMMENT '打码部门：11打码团队1、22打码团队2、33打码团队3、99打打码团队其他',
  `verify_code_coord` varchar(150) DEFAULT NULL COMMENT '图片验证码结果的坐标位置',
  PRIMARY KEY (`pic_id`),
  KEY `idx_rh_picture_effect_status` (`effect_time`,`status`,`user_pic_status`),
  KEY `IND_rh_picture_create_time` (`create_time`),
  KEY `idx_rh_picture_time_optren` (`opt_ren`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='验证码图片';

/*Table structure for table `rh_picture_2` */

DROP TABLE IF EXISTS `rh_picture_2`;

CREATE TABLE `rh_picture_2` (
  `pic_id` varchar(45) NOT NULL COMMENT '图片ID',
  `pic_filename` varchar(100) NOT NULL COMMENT '图片保存文件名',
  `verify_code` varchar(45) DEFAULT NULL COMMENT '图片验证码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `effect_time` datetime DEFAULT NULL COMMENT '有效时间',
  `start_time` datetime DEFAULT NULL COMMENT '客户端获取码时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '操作人',
  `status` varchar(45) DEFAULT NULL COMMENT '状态：00、未输入 11、已输入',
  `channel` varchar(45) NOT NULL COMMENT '渠道ID  合作伙伴ID',
  `user_pic_status` int(11) DEFAULT '0' COMMENT '图片是否被用户占用 0 闲置图片 1正在处理中图片',
  `result_status` int(2) DEFAULT '5' COMMENT '图片验证状态：初始值为5， 0 验证失败， 1验证成功。',
  `partner_id` varchar(10) DEFAULT NULL,
  `partner_key` varchar(10) DEFAULT NULL,
  `partner_status` varchar(5) DEFAULT NULL,
  `update_status` varchar(2) DEFAULT '22' COMMENT '上传状态 11、成功 22、未上传     ',
  `shyzmid` varchar(25) DEFAULT NULL COMMENT '商户验证码id（用于反馈商户 验证码错误）',
  `back_status` varchar(2) DEFAULT '00' COMMENT '反馈商户验证结果 00默认 11再次反馈 22反馈失败 33反馈成功',
  `order_id` varchar(32) DEFAULT NULL COMMENT '订单号',
  `fail_reason` varchar(200) DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`pic_id`),
  KEY `idx_effect_time` (`effect_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='验证码图片';

/*Table structure for table `rh_system` */

DROP TABLE IF EXISTS `rh_system`;

CREATE TABLE `rh_system` (
  `code_channel` varchar(2) DEFAULT NULL COMMENT '验证码 验证渠道 11、人工验证 22、联众验证 99、人工+联众验证 33、打码兔打码 44、人工+打码兔打码 ',
  `upload_num` int(3) DEFAULT NULL COMMENT '一次上传量',
  `get_code_num` int(3) DEFAULT NULL COMMENT '一次下载量',
  `upload_error_num` int(3) DEFAULT NULL COMMENT '一次上传错误量',
  `code01_weight` varchar(5) DEFAULT NULL COMMENT '打码团队01的权重',
  `code02_weight` varchar(5) DEFAULT NULL COMMENT '打码团队02的权重',
  `code03_weight` varchar(5) DEFAULT NULL COMMENT '打码团队03的权重',
  `code04_weight` varchar(5) DEFAULT NULL COMMENT '打码团队04的权重'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `rh_user` */

DROP TABLE IF EXISTS `rh_user`;

CREATE TABLE `rh_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '打码工人编号',
  `username` varchar(50) NOT NULL COMMENT '打码工人用户名',
  `pwd` varchar(22) NOT NULL COMMENT '打码工人密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `login_status` int(1) DEFAULT '0' COMMENT '登入状态： 0 离线 1 在线 3暂停',
  `code_total_number` int(11) DEFAULT '0' COMMENT '本次登入打码总个数',
  `code_error_number` int(11) DEFAULT '0' COMMENT '本次登入打码错误个数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登入时间',
  `department` varchar(50) DEFAULT NULL COMMENT '所在部门：1客服部、2运营部、3研发部、4其他 5对外 00对外02 6同程 7 机票 8 代理商 9 艺龙',
  `telephone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `user_level` varchar(20) DEFAULT NULL COMMENT '用户等级：1普通用户  2部门管理员 3公司管理员  4超级管理员',
  `logined` varchar(10) DEFAULT '0' COMMENT '用户是否登陆过：0未登陆过 1 登陆过',
  `get_code_time_limit` varchar(10) DEFAULT NULL COMMENT '用户打码器拉码时间间隔（毫秒ms）',
  `user_code_version` varchar(10) DEFAULT NULL COMMENT '用户的打码器版本号',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `robot_delete_price` */

DROP TABLE IF EXISTS `robot_delete_price`;

CREATE TABLE `robot_delete_price` (
  `xh` int(11) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '',
  `fz` varchar(10) DEFAULT '',
  `dz` varchar(10) DEFAULT '',
  `yz` float DEFAULT '0',
  `rz` float DEFAULT '0',
  `yws` float DEFAULT '0',
  `ywz` float DEFAULT '0',
  `ywx` float DEFAULT '0',
  `rws` float DEFAULT '0',
  `rwx` float DEFAULT '0',
  `rz2` float DEFAULT '0',
  `rz1` float DEFAULT '0',
  `swz` float DEFAULT '0',
  `tdz` float DEFAULT '0',
  `gws` float DEFAULT '0',
  `gwx` float DEFAULT '0',
  KEY `cc_fz_dz` (`cc`,`fz`,`dz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `robot_managerprice_log` */

DROP TABLE IF EXISTS `robot_managerprice_log`;

CREATE TABLE `robot_managerprice_log` (
  `train_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '待查询车票id',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`train_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='需要维护的票价信息表日志';

/*Table structure for table `robot_price_manager` */

DROP TABLE IF EXISTS `robot_price_manager`;

CREATE TABLE `robot_price_manager` (
  `train_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '待查询车票id',
  `from_station_no` varchar(30) DEFAULT NULL COMMENT '出发站编码',
  `to_station_no` varchar(30) DEFAULT NULL COMMENT '到达站编码',
  `seat_types` varchar(10) DEFAULT NULL COMMENT '坐席',
  `train_date` varchar(20) DEFAULT NULL COMMENT '警告时间',
  `train_no` varchar(32) DEFAULT NULL COMMENT '车次编码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `train_code` varchar(30) DEFAULT NULL COMMENT '车次',
  `from_station_name` varchar(30) DEFAULT NULL COMMENT '发车站',
  `to_station_name` varchar(30) DEFAULT NULL COMMENT '到达站',
  `status` varchar(2) DEFAULT '00' COMMENT '00:等待查询；11：重新查询；22：开始查询；33：查询结束',
  `type` varchar(10) DEFAULT NULL COMMENT 'query:查询票价；update:待更新票价 delete：删除票价',
  PRIMARY KEY (`train_id`),
  KEY `from_to_train_no` (`from_station_no`,`to_station_no`,`train_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3018328 DEFAULT CHARSET=utf8 COMMENT='需要维护的票价信息表';

/*Table structure for table `robot_system_log` */

DROP TABLE IF EXISTS `robot_system_log`;

CREATE TABLE `robot_system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `robot_name` varchar(50) DEFAULT NULL COMMENT '机器人名称',
  `robot_id` varchar(50) DEFAULT NULL COMMENT 'id',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1971 DEFAULT CHARSET=utf8;

/*Table structure for table `robot_system_setting` */

DROP TABLE IF EXISTS `robot_system_setting`;

CREATE TABLE `robot_system_setting` (
  `robot_id` varchar(50) NOT NULL COMMENT '主键',
  `robot_name` varchar(50) DEFAULT NULL COMMENT '名称',
  `robot_url` varchar(2000) DEFAULT NULL COMMENT '内容(请求url)',
  `robot_channel` varchar(50) DEFAULT NULL COMMENT '分配渠道：19e：19e平台 ext：对外商户 (qunar：去哪) elong：艺龙 app：手机端 embed：内嵌',
  `robot_desc` varchar(100) DEFAULT NULL COMMENT '说明',
  `robot_type` varchar(15) DEFAULT NULL COMMENT 'query:查询机器人',
  `robot_status` varchar(2) DEFAULT NULL COMMENT '1 启用 2停用 3 备用',
  `robot_con_timeout` varchar(10) DEFAULT NULL COMMENT '连接超时时间',
  `robot_read_timeout` varchar(10) DEFAULT NULL COMMENT '读取超时时间',
  `robot_register` varchar(2) DEFAULT NULL COMMENT '实名认证1 启用 2停用 3 备用',
  `robot_book` varchar(2) DEFAULT NULL COMMENT '预订1 启用 2停用 3 备用',
  `robot_pay` varchar(2) DEFAULT NULL COMMENT '支付1 启用 2停用 3 备用',
  `robot_check` varchar(2) DEFAULT NULL COMMENT '核验订单1 启用 2停用 3 备用',
  `robot_cancel` varchar(2) DEFAULT NULL COMMENT '取消订单1 启用 2停用 3 备用',
  `robot_endorse` varchar(2) DEFAULT NULL COMMENT '改签1 启用 2停用 3 备用',
  `robot_refund` varchar(2) DEFAULT NULL COMMENT '退票1 启用 2停用 3 备用',
  `robot_query` varchar(2) DEFAULT NULL COMMENT '余票查询1 启用 2停用 3 备用',
  `robot_enroll` varchar(2) DEFAULT NULL COMMENT '注册 1 启用 2 停用 3 备用',
  `robot_activate` varchar(2) DEFAULT NULL COMMENT '激活帐号 1启用 2停用 3备用',
  `robot_money` varchar(2) DEFAULT NULL COMMENT '票价查询1 启用 2停用 3 备用',
  `spare_thread` varchar(2) DEFAULT NULL COMMENT '剩余空闲空间',
  `opt_name` varchar(30) DEFAULT NULL COMMENT '操作人',
  `priority` varchar(2) DEFAULT NULL COMMENT '优先级',
  `robot_delete` varchar(2) DEFAULT NULL COMMENT '删除帐号联系人1 启用 2停用 3 备用',
  PRIMARY KEY (`robot_id`),
  KEY `IND_robot_system_setting_robot_type_robot_query` (`robot_type`,`robot_query`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机器人控制表';

/*Table structure for table `robot_tj_ali` */

DROP TABLE IF EXISTS `robot_tj_ali`;

CREATE TABLE `robot_tj_ali` (
  `机器人id` int(11) DEFAULT NULL,
  `机器人名称` varchar(135) DEFAULT NULL,
  `机器人状态` varchar(30) DEFAULT NULL,
  `url` varchar(450) DEFAULT NULL,
  `类型` int(11) DEFAULT NULL,
  `后台区域` varchar(60) DEFAULT NULL,
  `阿里外网IP` varchar(255) DEFAULT NULL COMMENT '公网IP\r\n',
  `阿里区域` varchar(255) DEFAULT NULL COMMENT '地域\r\n',
  `阿里到期时间` varchar(255) DEFAULT NULL COMMENT '当前到期时间\r\n',
  `阿里到期金额` varchar(255) DEFAULT NULL COMMENT '费用\r\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `robot_tj_meit` */

DROP TABLE IF EXISTS `robot_tj_meit`;

CREATE TABLE `robot_tj_meit` (
  `机器人id` int(11) DEFAULT NULL,
  `机器人名称` varchar(135) DEFAULT NULL,
  `机器人状态` varchar(30) DEFAULT NULL,
  `url` varchar(450) DEFAULT NULL,
  `类型` int(11) DEFAULT NULL,
  `后台区域` varchar(60) DEFAULT NULL,
  `美团外网IP` varchar(255) NOT NULL,
  `美团到期时间` varchar(255) NOT NULL COMMENT '到期时间',
  `美团到期金额` varchar(255) NOT NULL COMMENT '原价'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `robot_warning` */

DROP TABLE IF EXISTS `robot_warning`;

CREATE TABLE `robot_warning` (
  `warn_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '警告id',
  `telephone` varchar(50) DEFAULT NULL COMMENT '通知电话',
  `robot_name` varchar(30) DEFAULT NULL COMMENT '机器人名称',
  `warn_status` varchar(2) DEFAULT NULL COMMENT '通知状态 0、未发送， 1 正在发送， 2、发送成功， 3、发送失败',
  `create_time` datetime DEFAULT NULL COMMENT '警告时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  `send_num` int(2) DEFAULT '0' COMMENT '发送次数',
  `content` varchar(256) DEFAULT NULL COMMENT '发送内容',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  PRIMARY KEY (`warn_id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8 COMMENT='机器人警告表';

/*Table structure for table `sinfo` */

DROP TABLE IF EXISTS `sinfo`;

CREATE TABLE `sinfo` (
  `id` int(11) DEFAULT NULL,
  `stationno` int(11) DEFAULT NULL,
  `checi` varchar(19) DEFAULT NULL,
  `name` varchar(10) DEFAULT NULL,
  `arrtime` varchar(5) DEFAULT NULL,
  `starttime` varchar(5) DEFAULT NULL,
  `distance` varchar(4) DEFAULT NULL,
  `costtime` varchar(5) DEFAULT NULL,
  `czcc` varchar(5) DEFAULT NULL,
  KEY `checi_index` (`checi`),
  KEY `name_index` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `student` */

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

/*Table structure for table `t_ctrip_cc1` */

DROP TABLE IF EXISTS `t_ctrip_cc1`;

CREATE TABLE `t_ctrip_cc1` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `train_number` varchar(15) NOT NULL COMMENT '车次',
  `train_type` varchar(5) NOT NULL COMMENT '车次类型：G高铁、D动车、C城际、T特快、K快车、L临客',
  `start_station_name` varchar(30) NOT NULL COMMENT '始发站名',
  `end_station_name` varchar(30) NOT NULL COMMENT '终点站名',
  `strat_time` varchar(6) DEFAULT '' COMMENT '出发时间，格式00:00',
  `end_time` varchar(6) DEFAULT '' COMMENT '到达时间，格式00:00',
  `take_time` varchar(15) DEFAULT '' COMMENT '花费时间，格式5小时54分',
  `take_days` varchar(2) DEFAULT '0' COMMENT '花费天数',
  `pre_sale_day` varchar(3) DEFAULT '60' COMMENT '预售期',
  `departure_city_name` varchar(30) DEFAULT '' COMMENT '出发城市名',
  `arrival_city_name` varchar(30) DEFAULT '' COMMENT '到达城市名',
  `departure_date` varchar(15) DEFAULT '' COMMENT '出发日期yyyy-mm-dd',
  `dcity_code` varchar(10) DEFAULT '' COMMENT '出发城市code',
  `acity_code` varchar(10) DEFAULT '' COMMENT '到达城市code',
  `dcity_id` varchar(10) DEFAULT '' COMMENT '出发城市id',
  `acity_id` varchar(10) DEFAULT '' COMMENT '到达城市id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `midway_status` varchar(2) DEFAULT '00' COMMENT '途经站状态：00未查询途经站 11开始查询 22查询成功 33 查询失败 44查询返回空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6536 DEFAULT CHARSET=utf8;

/*Table structure for table `t_ctrip_sinfo1` */

DROP TABLE IF EXISTS `t_ctrip_sinfo1`;

CREATE TABLE `t_ctrip_sinfo1` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `stationno` int(11) NOT NULL COMMENT '车站no',
  `checi` varchar(19) NOT NULL COMMENT '车次',
  `name` varchar(10) NOT NULL COMMENT '车站名',
  `arrtime` varchar(5) NOT NULL COMMENT '到达时间',
  `starttime` varchar(5) NOT NULL COMMENT '开车时间',
  `distance` varchar(4) DEFAULT NULL COMMENT '里程',
  `costtime` varchar(5) DEFAULT NULL COMMENT '历时',
  `costday` varchar(4) DEFAULT NULL COMMENT '运行天数',
  `czcc` varchar(5) DEFAULT NULL COMMENT '车次',
  `stopovertime` varchar(5) DEFAULT NULL COMMENT '停靠时间',
  `station_code` varchar(20) DEFAULT NULL COMMENT '列车代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65447 DEFAULT CHARSET=utf8;

/*Table structure for table `t_ctrip_sinfo_bak` */

DROP TABLE IF EXISTS `t_ctrip_sinfo_bak`;

CREATE TABLE `t_ctrip_sinfo_bak` (
  `id` int(11) NOT NULL DEFAULT '0' COMMENT '自增主键',
  `stationno` int(11) NOT NULL COMMENT '车站no',
  `checi` varchar(19) NOT NULL COMMENT '车次',
  `name` varchar(10) NOT NULL COMMENT '车站名',
  `arrtime` varchar(5) NOT NULL COMMENT '到达时间',
  `starttime` varchar(5) NOT NULL COMMENT '开车时间',
  `distance` varchar(4) DEFAULT NULL COMMENT '里程',
  `costtime` varchar(5) DEFAULT NULL COMMENT '历时',
  `costday` varchar(4) DEFAULT NULL COMMENT '运行天数',
  `czcc` varchar(5) DEFAULT NULL COMMENT '车次',
  `stopovertime` varchar(5) DEFAULT NULL COMMENT '停靠时间',
  `station_code` varchar(20) DEFAULT NULL COMMENT '列车代码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_ctrip_zm1` */

DROP TABLE IF EXISTS `t_ctrip_zm1`;

CREATE TABLE `t_ctrip_zm1` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '抓取携程车站，自增id。',
  `name` varchar(20) NOT NULL COMMENT '车站名称',
  `pinyin` varchar(20) NOT NULL COMMENT '车站拼音',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `cc_status` varchar(2) DEFAULT '00' COMMENT '车次状态：00未查询 11已查询',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2560 DEFAULT CHARSET=utf8 COMMENT='携程获取的车站名称';

/*Table structure for table `t_lccc` */

DROP TABLE IF EXISTS `t_lccc`;

CREATE TABLE `t_lccc` (
  `cc` varchar(57) DEFAULT NULL,
  `lcdj` varchar(3) DEFAULT NULL,
  `kt` varchar(3) DEFAULT NULL,
  `tz` varchar(12) DEFAULT NULL,
  `pjsf` decimal(20,0) DEFAULT NULL,
  `sfz` varchar(30) DEFAULT NULL,
  `zdz` varchar(30) DEFAULT NULL,
  `zsf` decimal(20,0) DEFAULT NULL,
  `xh` int(11) DEFAULT NULL,
  `bj` int(11) DEFAULT NULL,
  `kxgl` int(11) DEFAULT NULL,
  `kxzq` int(11) DEFAULT NULL,
  `ksrq` varchar(54) DEFAULT NULL,
  `jsrq` varchar(36) DEFAULT NULL,
  `ccno` varchar(60) DEFAULT NULL,
  `status` varchar(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车次信息';

/*Table structure for table `t_lccc_2` */

DROP TABLE IF EXISTS `t_lccc_2`;

CREATE TABLE `t_lccc_2` (
  `cc` varchar(19) DEFAULT NULL,
  `lcdj` varchar(1) DEFAULT NULL,
  `kt` varchar(1) DEFAULT NULL,
  `tz` varchar(4) DEFAULT NULL,
  `pjsf` decimal(19,0) DEFAULT NULL,
  `sfz` varchar(10) DEFAULT NULL,
  `zdz` varchar(10) DEFAULT NULL,
  `zsf` decimal(19,0) DEFAULT NULL,
  `xh` int(11) DEFAULT NULL,
  `bj` int(11) DEFAULT NULL,
  `kxgl` int(11) DEFAULT NULL,
  `kxzq` int(11) DEFAULT NULL,
  `ksrq` varchar(8) DEFAULT NULL,
  `jsrq` varchar(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_query_num` */

DROP TABLE IF EXISTS `t_query_num`;

CREATE TABLE `t_query_num` (
  `query_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `merchant_id` varchar(100) DEFAULT NULL COMMENT '商户编号',
  `query_company` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `query_num` int(15) DEFAULT '0' COMMENT '当天查询次数',
  `total_num` int(15) DEFAULT '0' COMMENT '总查询次数',
  `check_left_num` int(15) DEFAULT '0' COMMENT '校验余票查询次数',
  `total_check_num` int(15) DEFAULT '0' COMMENT '校验余票查询总数',
  `query_num_error` int(10) DEFAULT '0' COMMENT '查询失败次数',
  `query_num_nodatas` int(10) DEFAULT '0' COMMENT '查询无效次数',
  `agent_id` varchar(45) DEFAULT NULL COMMENT '19e前台代理商id',
  PRIMARY KEY (`query_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='各渠道查询接口调用次数';

/*Table structure for table `t_sinfo` */

DROP TABLE IF EXISTS `t_sinfo`;

CREATE TABLE `t_sinfo` (
  `id` int(11) NOT NULL DEFAULT '0' COMMENT '自增主键',
  `stationno` int(11) NOT NULL COMMENT '车站no',
  `checi` varchar(19) NOT NULL COMMENT '车次',
  `name` varchar(10) NOT NULL COMMENT '车站名',
  `arrtime` varchar(5) NOT NULL COMMENT '到达时间',
  `starttime` varchar(5) NOT NULL COMMENT '开车时间',
  `distance` varchar(4) DEFAULT NULL COMMENT '里程',
  `costtime` varchar(5) DEFAULT NULL COMMENT '历时',
  `costday` varchar(4) DEFAULT NULL COMMENT '运行天数',
  `czcc` varchar(5) DEFAULT NULL COMMENT '车次',
  `stopovertime` varchar(5) DEFAULT NULL COMMENT '停靠时间',
  `station_code` varchar(20) DEFAULT NULL COMMENT '列车代码',
  KEY `checi` (`checi`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_sinfo_20161104` */

DROP TABLE IF EXISTS `t_sinfo_20161104`;

CREATE TABLE `t_sinfo_20161104` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `stationno` int(11) NOT NULL COMMENT '车站no',
  `checi` varchar(19) NOT NULL COMMENT '车次',
  `name` varchar(10) NOT NULL COMMENT '车站名',
  `arrtime` varchar(5) NOT NULL COMMENT '到达时间',
  `starttime` varchar(5) NOT NULL COMMENT '开车时间',
  `distance` varchar(4) DEFAULT NULL COMMENT '里程',
  `costtime` varchar(5) DEFAULT NULL COMMENT '历时',
  `costday` varchar(4) DEFAULT NULL COMMENT '运行天数',
  `czcc` varchar(5) DEFAULT NULL COMMENT '车次',
  `stopovertime` varchar(5) DEFAULT NULL COMMENT '停靠时间',
  `station_code` varchar(20) DEFAULT NULL COMMENT '列车代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64418 DEFAULT CHARSET=utf8;

/*Table structure for table `t_sinfo_20180103` */

DROP TABLE IF EXISTS `t_sinfo_20180103`;

CREATE TABLE `t_sinfo_20180103` (
  `id` int(11) NOT NULL DEFAULT '0' COMMENT '自增主键',
  `stationno` int(11) NOT NULL COMMENT '车站no',
  `checi` varchar(19) NOT NULL COMMENT '车次',
  `name` varchar(10) NOT NULL COMMENT '车站名',
  `arrtime` varchar(5) NOT NULL COMMENT '到达时间',
  `starttime` varchar(5) NOT NULL COMMENT '开车时间',
  `distance` varchar(4) DEFAULT NULL COMMENT '里程',
  `costtime` varchar(5) DEFAULT NULL COMMENT '历时',
  `costday` varchar(4) DEFAULT NULL COMMENT '运行天数',
  `czcc` varchar(5) DEFAULT NULL COMMENT '车次',
  `stopovertime` varchar(5) DEFAULT NULL COMMENT '停靠时间',
  `station_code` varchar(20) DEFAULT NULL COMMENT '列车代码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_station_match` */

DROP TABLE IF EXISTS `t_station_match`;

CREATE TABLE `t_station_match` (
  `station_id` int(10) DEFAULT NULL,
  `station_name` varchar(60) DEFAULT NULL,
  `station_key` varchar(60) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_station_match_bak` */

DROP TABLE IF EXISTS `t_station_match_bak`;

CREATE TABLE `t_station_match_bak` (
  `station_id` int(10) NOT NULL DEFAULT '0',
  `station_name` varchar(20) DEFAULT '',
  `station_key` varchar(20) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a` */

DROP TABLE IF EXISTS `t_zjpj_a`;

CREATE TABLE `t_zjpj_a` (
  `xh` int(15) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '' COMMENT '车次',
  `fz` varchar(20) DEFAULT '' COMMENT '出发站',
  `dz` varchar(20) DEFAULT '' COMMENT '到达站',
  `wz` float DEFAULT '0' COMMENT '无座',
  `yz` float DEFAULT '0' COMMENT '硬座',
  `rz` float DEFAULT '0' COMMENT '软座',
  `yws` float DEFAULT '0' COMMENT '硬卧上',
  `ywz` float DEFAULT '0' COMMENT '硬卧中',
  `ywx` float DEFAULT '0' COMMENT '硬卧下',
  `rws` float DEFAULT '0' COMMENT '软卧上',
  `rwx` float DEFAULT '0' COMMENT '软卧下',
  `rz2` float DEFAULT '0' COMMENT '二等座',
  `rz1` float DEFAULT '0' COMMENT '一等座',
  `swz` float DEFAULT '0' COMMENT '商务座',
  `tdz` float DEFAULT '0' COMMENT '特等座',
  `gws` float DEFAULT '0' COMMENT '高级软卧上',
  `gwx` float DEFAULT '0' COMMENT '高级软卧下',
  `dws` float DEFAULT '0' COMMENT '动卧上',
  `dwx` float DEFAULT '0' COMMENT '动卧下',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  KEY `NewIndex1` (`cc`),
  KEY `NewIndex4` (`fz`,`dz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a1` */

DROP TABLE IF EXISTS `t_zjpj_a1`;

CREATE TABLE `t_zjpj_a1` (
  `xh` int(11) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '',
  `fz` varchar(20) DEFAULT '',
  `dz` varchar(20) DEFAULT '',
  `wz` float DEFAULT '0',
  `yz` float DEFAULT '0',
  `rz` float DEFAULT '0',
  `yws` float DEFAULT '0',
  `ywz` float DEFAULT '0',
  `ywx` float DEFAULT '0',
  `rws` float DEFAULT '0',
  `rwx` float DEFAULT '0',
  `rz2` float DEFAULT '0',
  `rz1` float DEFAULT '0',
  `swz` float DEFAULT '0',
  `tdz` float DEFAULT '0',
  `gws` float DEFAULT '0',
  `gwx` float DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  KEY `cc` (`cc`) USING BTREE,
  KEY `fz_dz_index` (`fz`,`dz`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a111` */

DROP TABLE IF EXISTS `t_zjpj_a111`;

CREATE TABLE `t_zjpj_a111` (
  `xh` int(15) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '',
  `fz` varchar(20) DEFAULT '',
  `dz` varchar(20) DEFAULT '',
  `wz` float DEFAULT '0',
  `yz` float DEFAULT '0',
  `rz` float DEFAULT '0',
  `yws` float DEFAULT '0',
  `ywz` float DEFAULT '0',
  `ywx` float DEFAULT '0',
  `rws` float DEFAULT '0',
  `rwx` float DEFAULT '0',
  `rz2` float DEFAULT '0',
  `rz1` float DEFAULT '0',
  `swz` float DEFAULT '0',
  `tdz` float DEFAULT '0',
  `gws` float DEFAULT '0',
  `gwx` float DEFAULT '0',
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_20160830` */

DROP TABLE IF EXISTS `t_zjpj_a_20160830`;

CREATE TABLE `t_zjpj_a_20160830` (
  `xh` int(11) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '' COMMENT '车次',
  `fz` varchar(20) DEFAULT '' COMMENT '出发站',
  `dz` varchar(20) DEFAULT '' COMMENT '到达站',
  `wz` float DEFAULT '0' COMMENT '无座',
  `yz` float DEFAULT '0' COMMENT '硬座',
  `rz` float DEFAULT '0' COMMENT '软座',
  `yws` float DEFAULT '0' COMMENT '硬卧上',
  `ywz` float DEFAULT '0' COMMENT '硬卧中',
  `ywx` float DEFAULT '0' COMMENT '硬卧下',
  `rws` float DEFAULT '0' COMMENT '软卧上',
  `rwx` float DEFAULT '0' COMMENT '软卧下',
  `rz2` float DEFAULT '0' COMMENT '二等座',
  `rz1` float DEFAULT '0' COMMENT '一等座',
  `swz` float DEFAULT '0' COMMENT '商务座',
  `tdz` float DEFAULT '0' COMMENT '特等座',
  `gws` float DEFAULT '0' COMMENT '高级软卧上',
  `gwx` float DEFAULT '0' COMMENT '高级软卧下',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  KEY `cc` (`cc`) USING BTREE,
  KEY `fz_dz_index` (`fz`,`dz`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_20170210` */

DROP TABLE IF EXISTS `t_zjpj_a_20170210`;

CREATE TABLE `t_zjpj_a_20170210` (
  `xh` int(15) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '',
  `fz` varchar(20) DEFAULT '',
  `dz` varchar(20) DEFAULT '',
  `wz` float DEFAULT '0',
  `yz` float DEFAULT '0',
  `rz` float DEFAULT '0',
  `yws` float DEFAULT '0',
  `ywz` float DEFAULT '0',
  `ywx` float DEFAULT '0',
  `rws` float DEFAULT '0',
  `rwx` float DEFAULT '0',
  `rz2` float DEFAULT '0',
  `rz1` float DEFAULT '0',
  `swz` float DEFAULT '0',
  `tdz` float DEFAULT '0',
  `gws` float DEFAULT '0',
  `gwx` float DEFAULT '0',
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_20170704` */

DROP TABLE IF EXISTS `t_zjpj_a_20170704`;

CREATE TABLE `t_zjpj_a_20170704` (
  `xh` int(15) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '',
  `fz` varchar(20) DEFAULT '',
  `dz` varchar(20) DEFAULT '',
  `wz` float DEFAULT '0',
  `yz` float DEFAULT '0',
  `rz` float DEFAULT '0',
  `yws` float DEFAULT '0',
  `ywz` float DEFAULT '0',
  `ywx` float DEFAULT '0',
  `rws` float DEFAULT '0',
  `rwx` float DEFAULT '0',
  `rz2` float DEFAULT '0',
  `rz1` float DEFAULT '0',
  `swz` float DEFAULT '0',
  `tdz` float DEFAULT '0',
  `gws` float DEFAULT '0',
  `gwx` float DEFAULT '0',
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_bak` */

DROP TABLE IF EXISTS `t_zjpj_a_bak`;

CREATE TABLE `t_zjpj_a_bak` (
  `xh` int(11) NOT NULL DEFAULT '0' COMMENT 'ID',
  `cc` varchar(19) DEFAULT '' COMMENT '车次',
  `fz` varchar(10) DEFAULT '' COMMENT '发站',
  `dz` varchar(10) DEFAULT '' COMMENT '到站',
  `yz` float DEFAULT '0' COMMENT '硬座',
  `rz` float DEFAULT '0' COMMENT '软座',
  `yws` float DEFAULT '0' COMMENT '硬卧上',
  `ywz` float DEFAULT '0' COMMENT '硬卧中',
  `ywx` float DEFAULT '0' COMMENT '硬卧下',
  `rws` float DEFAULT '0' COMMENT '软卧上',
  `rwx` float DEFAULT '0' COMMENT '软卧下',
  `rz2` float DEFAULT '0' COMMENT '二等座',
  `rz1` float DEFAULT '0' COMMENT '一等座',
  `swz` float DEFAULT '0' COMMENT '商务座',
  `tdz` float DEFAULT '0' COMMENT '特等座',
  `gws` float DEFAULT '0' COMMENT '高级软卧上',
  `gwx` float DEFAULT '0' COMMENT '高级软卧下',
  `wz` float DEFAULT '0' COMMENT '无座',
  `update_time` datetime DEFAULT NULL COMMENT '操作时间',
  KEY `cc_index` (`cc`),
  KEY `fz_dz_index` (`fz`,`dz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_copy` */

DROP TABLE IF EXISTS `t_zjpj_a_copy`;

CREATE TABLE `t_zjpj_a_copy` (
  `xh` int(11) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '',
  `fz` varchar(20) DEFAULT '',
  `dz` varchar(20) DEFAULT '',
  `wz` float DEFAULT '0',
  `yz` float DEFAULT '0',
  `rz` float DEFAULT '0',
  `yws` float DEFAULT '0',
  `ywz` float DEFAULT '0',
  `ywx` float DEFAULT '0',
  `rws` float DEFAULT '0',
  `rwx` float DEFAULT '0',
  `rz2` float DEFAULT '0',
  `rz1` float DEFAULT '0',
  `swz` float DEFAULT '0',
  `tdz` float DEFAULT '0',
  `gws` float DEFAULT '0',
  `gwx` float DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  KEY `cc` (`cc`) USING BTREE,
  KEY `fz_dz_index` (`fz`,`dz`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_insert_temp` */

DROP TABLE IF EXISTS `t_zjpj_a_insert_temp`;

CREATE TABLE `t_zjpj_a_insert_temp` (
  `xh` int(15) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '',
  `fz` varchar(10) DEFAULT '',
  `dz` varchar(10) DEFAULT '',
  `wz` float DEFAULT '0',
  `yz` float DEFAULT '0',
  `rz` float DEFAULT '0',
  `yws` float DEFAULT '0',
  `ywz` float DEFAULT '0',
  `ywx` float DEFAULT '0',
  `rws` float DEFAULT '0',
  `rwx` float DEFAULT '0',
  `rz2` float DEFAULT '0',
  `rz1` float DEFAULT '0',
  `swz` float DEFAULT '0',
  `tdz` float DEFAULT '0',
  `gws` float DEFAULT '0',
  `gwx` float DEFAULT '0',
  `channel` varchar(10) DEFAULT 'alter' COMMENT 'alter:改签；query：查询'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_logs` */

DROP TABLE IF EXISTS `t_zjpj_a_logs`;

CREATE TABLE `t_zjpj_a_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `fz` char(20) DEFAULT NULL COMMENT '发站',
  `dz` char(20) DEFAULT NULL COMMENT '到站',
  `opt_name` char(50) DEFAULT NULL COMMENT '操作人',
  `before_data` char(250) DEFAULT NULL COMMENT '操作前的数据',
  `after_data` char(250) DEFAULT NULL COMMENT '操作后的数据',
  `opt_type` char(50) DEFAULT NULL COMMENT '操作类型',
  `cc` char(20) DEFAULT NULL COMMENT '车次',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_price_temp` */

DROP TABLE IF EXISTS `t_zjpj_a_price_temp`;

CREATE TABLE `t_zjpj_a_price_temp` (
  `xh` int(15) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '',
  `fz` varchar(10) DEFAULT '',
  `dz` varchar(10) DEFAULT '',
  `wz` float DEFAULT '0',
  `yz` float DEFAULT '0',
  `rz` float DEFAULT '0',
  `yws` float DEFAULT '0',
  `ywz` float DEFAULT '0',
  `ywx` float DEFAULT '0',
  `rws` float DEFAULT '0',
  `rwx` float DEFAULT '0',
  `rz2` float DEFAULT '0',
  `rz1` float DEFAULT '0',
  `swz` float DEFAULT '0',
  `tdz` float DEFAULT '0',
  `gws` float DEFAULT '0',
  `gwx` float DEFAULT '0',
  `channel` varchar(10) DEFAULT 'alter' COMMENT 'alter:改签；query：查询',
  KEY `cc_fz_dz` (`cc`,`fz`,`dz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_temp` */

DROP TABLE IF EXISTS `t_zjpj_a_temp`;

CREATE TABLE `t_zjpj_a_temp` (
  `cc` varchar(19) DEFAULT NULL,
  `fz` varchar(10) DEFAULT NULL,
  `dz` varchar(10) DEFAULT NULL,
  `yz` float DEFAULT NULL,
  `rz` float DEFAULT NULL,
  `yws` float DEFAULT NULL,
  `ywz` float DEFAULT NULL,
  `ywx` float DEFAULT NULL,
  `rws` float DEFAULT NULL,
  `rwx` float DEFAULT NULL,
  `rz2` float DEFAULT NULL,
  `rz1` float DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zjpj_a_unbook` */

DROP TABLE IF EXISTS `t_zjpj_a_unbook`;

CREATE TABLE `t_zjpj_a_unbook` (
  `xh` int(11) NOT NULL DEFAULT '0',
  `cc` varchar(19) DEFAULT '',
  `fz` varchar(10) DEFAULT '',
  `dz` varchar(10) DEFAULT '',
  `yz` float DEFAULT NULL,
  `rz` float DEFAULT NULL,
  `yws` float DEFAULT NULL,
  `ywz` float DEFAULT NULL,
  `ywx` float DEFAULT NULL,
  `rws` float DEFAULT NULL,
  `rwx` float DEFAULT NULL,
  `rz2` float DEFAULT NULL,
  `rz1` float DEFAULT NULL,
  `start_station_name` varchar(20) DEFAULT '',
  `end_station_name` varchar(20) DEFAULT '',
  `start_time` varchar(10) DEFAULT NULL,
  `arrive_time` varchar(10) DEFAULT NULL,
  `czcc` varchar(10) DEFAULT '',
  `lishi` varchar(10) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_zm` */

DROP TABLE IF EXISTS `t_zm`;

CREATE TABLE `t_zm` (
  `zm_id` int(11) NOT NULL AUTO_INCREMENT,
  `zmhz` varchar(96) DEFAULT NULL,
  `py` varchar(15) DEFAULT NULL,
  `lh` varchar(9) DEFAULT NULL,
  `tcs` int(11) DEFAULT NULL,
  `city` varchar(96) DEFAULT NULL,
  `status` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`zm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2726 DEFAULT CHARSET=utf8;

/*Table structure for table `temp` */

DROP TABLE IF EXISTS `temp`;

CREATE TABLE `temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `card_no` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=234267 DEFAULT CHARSET=utf8;

/*Table structure for table `tj_account` */

DROP TABLE IF EXISTS `tj_account`;

CREATE TABLE `tj_account` (
  `tj_id` varchar(20) NOT NULL COMMENT '主键',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `add_regist` int(11) DEFAULT NULL COMMENT '19e填写实名',
  `regist_pass` int(11) DEFAULT NULL COMMENT '通过实名',
  `regist_wait` int(11) DEFAULT NULL COMMENT '待核验',
  `regist_success` int(11) DEFAULT NULL COMMENT '已实名',
  `regist_fail` int(11) DEFAULT NULL COMMENT '信息错误',
  `regist_other` int(11) DEFAULT NULL COMMENT '其他',
  `regist_free` int(11) DEFAULT NULL COMMENT '当前注册成功未使用总数',
  `account` int(11) DEFAULT NULL COMMENT '总账号',
  `account_wait` int(11) DEFAULT NULL COMMENT '账号空闲',
  `account_stop` int(11) DEFAULT NULL COMMENT '账号停用',
  `account_land` int(11) DEFAULT NULL COMMENT '已登录',
  `account_add` int(11) DEFAULT NULL COMMENT '当天新增',
  `qunar_num` int(11) DEFAULT NULL COMMENT 'qunar总账号',
  `qunar_stop` int(11) DEFAULT NULL COMMENT 'qunar总停用',
  `qunar_can_use` int(11) DEFAULT NULL COMMENT 'qunar当前可空',
  `qunar_stop_today` int(11) DEFAULT NULL COMMENT '当日停用',
  `qunar_seal` int(11) DEFAULT NULL COMMENT '账号封用',
  `qunar_toomuch` int(11) DEFAULT NULL COMMENT '取消过多',
  `qunar_out` int(11) DEFAULT NULL COMMENT '人员上线',
  `qunar_not_real` int(11) DEFAULT NULL COMMENT '未实名',
  `qunar_shici` int(11) DEFAULT NULL COMMENT '订购10次',
  `qunar_goback` int(11) DEFAULT NULL COMMENT '用户取回',
  `tongcheng_num` int(11) DEFAULT NULL COMMENT 'tongcheng总账号',
  `tongcheng_stop` int(11) DEFAULT NULL COMMENT 'tongcheng总停用',
  `tongcheng_can_use` int(11) DEFAULT NULL COMMENT 'tc 当前可用',
  `tongcheng_stop_today` int(11) DEFAULT NULL COMMENT '当日停用',
  `tongcheng_seal` int(11) DEFAULT NULL COMMENT '账号封用',
  `tongcheng_toomuch` int(11) DEFAULT NULL COMMENT '取消过多',
  `tongcheng_out` int(11) DEFAULT NULL COMMENT '人员上线',
  `tongcheng_not_real` int(11) DEFAULT NULL COMMENT '未实名',
  `tongcheng_shici` int(11) DEFAULT NULL COMMENT '订购10次',
  `tongcheng_goback` int(11) DEFAULT NULL COMMENT '用户取回',
  `elong_num` int(11) DEFAULT NULL COMMENT 'elong总账号',
  `elong_stop` int(11) DEFAULT NULL COMMENT 'elong总停用',
  `elong_can_use` int(11) DEFAULT NULL COMMENT 'elong当前可用',
  `elong_stop_today` int(11) DEFAULT NULL COMMENT '当日停用',
  `elong_seal` int(11) DEFAULT NULL COMMENT '账号封用',
  `elong_toomuch` int(11) DEFAULT NULL COMMENT '取消过多',
  `elong_out` int(11) DEFAULT NULL COMMENT '人员上线',
  `elong_not_real` int(11) DEFAULT NULL COMMENT '未实名',
  `elong_shici` int(11) DEFAULT NULL COMMENT '订购10次',
  `elong_goback` int(11) DEFAULT NULL COMMENT '用户取回',
  `regist_all_wait` int(11) DEFAULT NULL COMMENT '待核验',
  `regist_all_hand` int(11) DEFAULT NULL COMMENT '人工注册',
  `regist_all_wait1` int(11) DEFAULT NULL,
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票账号统计表';

/*Table structure for table `tj_channel` */

DROP TABLE IF EXISTS `tj_channel`;

CREATE TABLE `tj_channel` (
  `channel_id` varchar(20) NOT NULL COMMENT '主键',
  `channel` varchar(20) DEFAULT NULL COMMENT '渠道',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `search_count` int(11) DEFAULT NULL COMMENT '查询总数',
  `search_success` int(11) DEFAULT NULL COMMENT '查询成功',
  `search_fail` int(11) DEFAULT NULL COMMENT '查询失败',
  `msg_count` int(11) DEFAULT NULL COMMENT '短信',
  `alter_count` int(11) DEFAULT NULL COMMENT '改签总数',
  `alter_success` int(11) DEFAULT NULL COMMENT '改签成功',
  `alter_fail` int(11) DEFAULT NULL COMMENT '改签失败',
  `refund_count` int(11) DEFAULT NULL COMMENT '退款总数',
  `refund_success` int(11) DEFAULT NULL COMMENT '退款成功',
  `refund_fail` int(11) DEFAULT NULL COMMENT '退款失败',
  `alter_success_cgl` varchar(10) DEFAULT NULL COMMENT '改签成功率',
  `refund_success_cgl` varchar(10) DEFAULT NULL COMMENT '退款成功率',
  PRIMARY KEY (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票渠道统计表';

/*Table structure for table `tj_dealer` */

DROP TABLE IF EXISTS `tj_dealer`;

CREATE TABLE `tj_dealer` (
  `tj_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '统计表id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `date_time` varchar(10) DEFAULT NULL COMMENT '统计月份',
  `dealer_id` varchar(50) DEFAULT NULL COMMENT '代理商id',
  `area_name` varchar(10) DEFAULT NULL COMMENT '代理商省份',
  `pay_money` varchar(10) DEFAULT NULL COMMENT '交易量（交易总金额）',
  `order_count` varchar(10) DEFAULT NULL COMMENT '出票量',
  `refund_count` varchar(10) DEFAULT NULL COMMENT '退票量',
  `refund_money` varchar(10) DEFAULT NULL COMMENT '退款总金额',
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5188 DEFAULT CHARSET=utf8;

/*Table structure for table `tj_exception` */

DROP TABLE IF EXISTS `tj_exception`;

CREATE TABLE `tj_exception` (
  `tj_id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tj_date` date DEFAULT NULL COMMENT '统计日期',
  `execption_id` varchar(10) NOT NULL COMMENT '异常ID',
  `execption_name` varchar(30) NOT NULL COMMENT '异常关键字',
  `execption_type` varchar(2) NOT NULL DEFAULT '11' COMMENT '异常类型：11预定 22支付',
  `execption_num` int(8) NOT NULL DEFAULT '0' COMMENT '异常个数',
  `opter` varchar(30) DEFAULT '' COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`tj_id`),
  KEY `inx_create_time` (`tj_date`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='异常统计表';

/*Table structure for table `tj_failorder` */

DROP TABLE IF EXISTS `tj_failorder`;

CREATE TABLE `tj_failorder` (
  `tj_id` varchar(20) NOT NULL COMMENT '主键',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `all_order` int(11) DEFAULT NULL COMMENT '总订单',
  `cgl` varchar(20) DEFAULT NULL COMMENT '总成功率',
  `zhanbi` varchar(20) DEFAULT NULL COMMENT '总占比',
  `channel` varchar(20) DEFAULT NULL COMMENT '渠道',
  `count` int(11) DEFAULT NULL COMMENT '失败总数',
  `fail_1` int(11) DEFAULT NULL COMMENT '无票',
  `fail_2` int(11) DEFAULT NULL COMMENT '实名制',
  `fail_3` int(11) DEFAULT NULL COMMENT '票价不符',
  `fail_4` int(11) DEFAULT NULL COMMENT '乘车时间异常',
  `fail_5` int(11) DEFAULT NULL COMMENT '证件错误',
  `fail_6` int(11) DEFAULT NULL COMMENT '用户要求取消订单',
  `fail_7` int(11) DEFAULT NULL COMMENT '未通过12306实名认证',
  `fail_8` int(11) DEFAULT NULL COMMENT '乘客身份信息待核验',
  `fail_9` int(11) DEFAULT NULL COMMENT '系统异常',
  `fail_11` int(11) DEFAULT NULL COMMENT '超时未支付',
  `fail_12` int(11) DEFAULT NULL COMMENT '信息冒用',
  `fail_0` int(11) DEFAULT NULL COMMENT '其他',
  `zhanbi_1` varchar(20) DEFAULT NULL COMMENT '无票占比',
  `zhanbi_2` varchar(20) DEFAULT NULL COMMENT '实名制占比',
  `zhanbi_3` varchar(20) DEFAULT NULL COMMENT '票价不符占比',
  `zhanbi_4` varchar(20) DEFAULT NULL COMMENT '乘车时间异常占比',
  `zhanbi_5` varchar(20) DEFAULT NULL COMMENT '证件错误占比',
  `zhanbi_6` varchar(20) DEFAULT NULL COMMENT '用户要求取消订单占比',
  `zhanbi_7` varchar(20) DEFAULT NULL COMMENT '未通过12306实名认证占比',
  `zhanbi_8` varchar(20) DEFAULT NULL COMMENT '乘客身份信息待核验占比',
  `zhanbi_9` varchar(20) DEFAULT NULL COMMENT '系统异常占比',
  `zhanbi_11` varchar(20) DEFAULT NULL COMMENT '超时未支付占比',
  `zhanbi_12` varchar(20) DEFAULT NULL COMMENT '信息被冒用占比',
  `zhanbi_0` varchar(20) DEFAULT NULL COMMENT '其他占比',
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出票失败订单统计表';

/*Table structure for table `tj_hc_halfhour` */

DROP TABLE IF EXISTS `tj_hc_halfhour`;

CREATE TABLE `tj_hc_halfhour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hour_stat` char(50) DEFAULT NULL COMMENT '每个时间点',
  `order_count` int(20) DEFAULT NULL COMMENT '总订单数',
  `day_stat` char(50) DEFAULT NULL COMMENT '订单日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=179 DEFAULT CHARSET=utf8;

/*Table structure for table `tj_hc_orderinfo` */

DROP TABLE IF EXISTS `tj_hc_orderinfo`;

CREATE TABLE `tj_hc_orderinfo` (
  `tj_id` varchar(20) NOT NULL DEFAULT '0' COMMENT '统计表id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `out_ticket_succeed` int(10) DEFAULT NULL COMMENT '出票成功的个数',
  `out_ticket_defeated` int(10) DEFAULT NULL COMMENT '出票失败的个数',
  `preparative_count` int(10) DEFAULT NULL COMMENT '预下单的个数',
  `pay_defeated` int(10) DEFAULT NULL COMMENT '支付失败的个数',
  `refund_count` int(10) DEFAULT NULL COMMENT '退款完成的个数',
  `ticket_count` int(10) DEFAULT NULL COMMENT '出售的票数',
  `order_count` int(10) DEFAULT NULL COMMENT '总订单',
  `succeed_money` decimal(11,1) DEFAULT NULL COMMENT '成功金额',
  `defeated_money` decimal(11,1) DEFAULT NULL COMMENT '失败金额',
  `change_money` decimal(11,1) DEFAULT NULL COMMENT '改签金额',
  `bx_count` int(10) DEFAULT NULL COMMENT '保险个数',
  `bx_countMoney` decimal(11,1) DEFAULT NULL COMMENT '保险总额',
  `succeed_odds` varchar(10) DEFAULT NULL COMMENT '成功转换率',
  `succeed_cgl` varchar(10) DEFAULT NULL COMMENT '订单成功率',
  `succeed_sbl` varchar(10) DEFAULT NULL COMMENT '订单失败率',
  `holdseat_cgl` varchar(10) DEFAULT NULL COMMENT '占座成功率',
  `holdseat_sbl` varchar(10) DEFAULT NULL COMMENT '占座失败率',
  `succeed_vip_sbl` varchar(10) DEFAULT NULL COMMENT 'VIP失败率',
  `channel` varchar(20) DEFAULT NULL COMMENT '渠道',
  `active` int(10) DEFAULT NULL COMMENT '活跃用户数',
  `vip_lose` int(5) DEFAULT NULL COMMENT 'VIP订单失败数',
  `out_ticket_XL` decimal(11,1) DEFAULT NULL COMMENT '总出票效率',
  `over_time` int(10) DEFAULT NULL COMMENT '超过10分钟订单',
  `agent_login_num` int(10) DEFAULT NULL COMMENT '代理商登陆次数',
  `svip_num` int(10) DEFAULT NULL COMMENT 'SVIP个数',
  `refund_money` decimal(11,1) DEFAULT NULL COMMENT '退款金额',
  `svip_lose` int(10) DEFAULT NULL COMMENT 'SVIP失败数',
  `pay_time` decimal(11,1) DEFAULT NULL COMMENT '支付时长（秒）',
  `out_time_order` int(10) DEFAULT NULL COMMENT '超时订单',
  `cancel_order` int(10) DEFAULT NULL COMMENT '取消订单',
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `tj_hc_ordersale` */

DROP TABLE IF EXISTS `tj_hc_ordersale`;

CREATE TABLE `tj_hc_ordersale` (
  `tj_id` varchar(20) NOT NULL DEFAULT '0' COMMENT '统计表id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间（交易时间）',
  `dealer_id` varchar(50) DEFAULT NULL COMMENT '代理商id',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '当天销售金额',
  `refund_money` decimal(11,3) DEFAULT NULL COMMENT '当天退款金额',
  `bx_money` decimal(11,3) DEFAULT NULL COMMENT '当天保险利润',
  `order_count` int(10) DEFAULT NULL COMMENT '当天订单数',
  `ticket_count` int(10) DEFAULT NULL COMMENT '当天票数',
  `month_order_count` int(10) DEFAULT NULL COMMENT '本月总订单数',
  `month_ticket_count` int(10) DEFAULT NULL COMMENT '本月总票数',
  `month_bx_money` decimal(11,3) DEFAULT NULL COMMENT '本月保险利润',
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票代理商销售明细表';

/*Table structure for table `tj_hc_outticketsbl` */

DROP TABLE IF EXISTS `tj_hc_outticketsbl`;

CREATE TABLE `tj_hc_outticketsbl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计表id',
  `day_stat` varchar(10) DEFAULT NULL COMMENT '统计日期',
  `hour_stat` varchar(10) DEFAULT NULL COMMENT '统计小时',
  `sbl` varchar(10) DEFAULT NULL COMMENT '失败率',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8;

/*Table structure for table `tj_insurance` */

DROP TABLE IF EXISTS `tj_insurance`;

CREATE TABLE `tj_insurance` (
  `tj_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '统计表id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `date_time` varchar(10) DEFAULT NULL COMMENT '统计日期',
  `click_type` varchar(50) DEFAULT NULL COMMENT '方式：11.活动点击，22，强制领取',
  `click_num` int(11) DEFAULT '0' COMMENT '点击次数',
  `isSuccess` int(11) DEFAULT '0' COMMENT '成功数',
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5212 DEFAULT CHARSET=utf8;

/*Table structure for table `tj_insurance_jipiao` */

DROP TABLE IF EXISTS `tj_insurance_jipiao`;

CREATE TABLE `tj_insurance_jipiao` (
  `tj_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '统计表id',
  `user_name` varchar(10) DEFAULT NULL COMMENT '姓名',
  `idCard` varchar(50) DEFAULT NULL COMMENT '证件号',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '票号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` varchar(50) DEFAULT NULL COMMENT '统计日期',
  `is_Need` varchar(2) DEFAULT '0' COMMENT '0.未投保 1.已投保',
  `is_Success` varchar(2) DEFAULT NULL COMMENT '00.投保失败，11。投保成功',
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5680 DEFAULT CHARSET=utf8;

/*Table structure for table `tj_match` */

DROP TABLE IF EXISTS `tj_match`;

CREATE TABLE `tj_match` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tj_date` date DEFAULT NULL COMMENT '统计日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `channel` varchar(15) DEFAULT '' COMMENT '渠道',
  `query_count` int(8) DEFAULT '0' COMMENT '该渠道总查询次数',
  `param_sum` int(8) DEFAULT '0' COMMENT '传入证件总数',
  `match_sum` int(8) DEFAULT '0' COMMENT '匹配证件总数',
  `match_ratio` varchar(8) DEFAULT '' COMMENT '匹配证件比率',
  `all_count` int(8) DEFAULT '0' COMMENT '完全匹配总查询次数',
  `all_qratio` varchar(8) DEFAULT '' COMMENT '完全匹配总查询次数比率',
  `all_sum` int(8) DEFAULT '0' COMMENT '完全匹配证件总数',
  `all_ratio` varchar(8) DEFAULT '' COMMENT '完全匹配证件比率',
  `partOk_count` int(8) DEFAULT '0' COMMENT '部分匹配（可添加剩余证件）总查询次数',
  `partOk_qratio` varchar(8) DEFAULT '' COMMENT '部分匹配（可添加剩余证件）总查询次数比率',
  `partOk_sum` int(8) DEFAULT '0' COMMENT '部分匹配（可添加剩余证件）证件总数',
  `partOk_ratio` varchar(8) DEFAULT '' COMMENT '部分匹配（可添加剩余证件）比率',
  `partNo_count` int(8) DEFAULT '0' COMMENT '部分匹配（不可添加剩余证件）总查询次数',
  `partNo_qratio` varchar(8) DEFAULT '' COMMENT '部分匹配（不可添加剩余证件）总查询次数比率',
  `partNo_sum` int(8) DEFAULT '0' COMMENT '部分匹配（不可添加剩余证件）证件总数',
  `partNo_ratio` varchar(8) DEFAULT '' COMMENT '部分匹配（不可添加剩余证件）比率',
  `unmatch_count` int(8) DEFAULT '0' COMMENT '不匹配证件总查询次数',
  `unmatch_qratio` varchar(8) DEFAULT '' COMMENT '不匹配证件总查询次数比率',
  `unmatch_sum` int(8) DEFAULT '0' COMMENT '不匹配证件证件总数',
  `unmatch_ratio` varchar(8) DEFAULT '' COMMENT '不匹配证件比率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='白名单匹配统计表';

/*Table structure for table `tj_opter` */

DROP TABLE IF EXISTS `tj_opter`;

CREATE TABLE `tj_opter` (
  `tj_id` varchar(20) NOT NULL COMMENT '统计表id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `tj_time` date DEFAULT NULL COMMENT '统计时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '统计人',
  `order_operation` int(5) DEFAULT NULL COMMENT '订单操作次数',
  `out_ticket_total` int(5) DEFAULT '0' COMMENT '出票次数',
  `refund_operation` int(5) DEFAULT NULL COMMENT '退款操作次数',
  `refund_total` int(5) DEFAULT '0' COMMENT '退款次数',
  `out_ticket_promptly` decimal(11,3) DEFAULT NULL COMMENT '出票及时率',
  `refund_promptly` decimal(11,3) DEFAULT NULL COMMENT '退款及时率',
  `refund_total_19e` int(5) DEFAULT '0' COMMENT '19e',
  `refund_total_qunar` int(5) DEFAULT '0' COMMENT 'qunar',
  `refund_total_ext` int(5) DEFAULT '0' COMMENT '商户',
  `refund_total_meituan` int(5) DEFAULT '0' COMMENT '美团',
  `refund_total_app` int(5) DEFAULT '0' COMMENT 'app',
  `refund_total_inner` int(5) DEFAULT '0' COMMENT '内嵌',
  `refund_total_elong` int(5) DEFAULT '0' COMMENT '艺龙',
  `refund_total_tongcheng` int(5) DEFAULT '0' COMMENT '同程',
  `refund_total_differ` int(5) DEFAULT '0' COMMENT '差额',
  `refund_total_failure` int(5) DEFAULT '0' COMMENT '出票失败',
  `refund_total_verify` int(5) DEFAULT '0' COMMENT '审核退款',
  `refund_total_gtgj` int(5) DEFAULT '0' COMMENT '高铁管家',
  `refund_total_ctrip` int(5) DEFAULT '0' COMMENT '携程',
  `refund_total_tuniu` int(5) DEFAULT '0' COMMENT '途牛',
  `refund_total_refuse` int(5) DEFAULT '0' COMMENT '拒绝退票总数',
  `refund_total_holdon` int(5) DEFAULT '0' COMMENT '搁置订单总数',
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `tj_outticket` */

DROP TABLE IF EXISTS `tj_outticket`;

CREATE TABLE `tj_outticket` (
  `tj_id` varchar(20) NOT NULL COMMENT '主键',
  `channel` varchar(20) DEFAULT NULL COMMENT '渠道',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `hour` varchar(2) DEFAULT NULL COMMENT '时段',
  `book_xl` int(11) DEFAULT NULL COMMENT '预订效率',
  `shoudan` int(11) DEFAULT NULL COMMENT '收单时长',
  `fenfa` int(11) DEFAULT NULL COMMENT '分发时长',
  `book` int(11) DEFAULT NULL COMMENT '预订时长',
  `notify` int(11) DEFAULT NULL COMMENT '预订通知',
  `pay_xl` int(11) DEFAULT NULL COMMENT '支付时长',
  `notify_pay` int(11) DEFAULT NULL COMMENT '支付通知',
  `outticket_xl` int(11) DEFAULT NULL COMMENT '出票效率',
  `type` varchar(2) DEFAULT NULL COMMENT '00分时11全天',
  `device_type` varchar(4) DEFAULT 'pc' COMMENT 'pc:pc统计数据;app:app统计数据',
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票出票效率统计表';

/*Table structure for table `tj_rh_picture` */

DROP TABLE IF EXISTS `tj_rh_picture`;

CREATE TABLE `tj_rh_picture` (
  `id` bigint(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `opt_ren` varchar(150) DEFAULT NULL,
  `pic_count` bigint(20) DEFAULT NULL,
  `pic_success` bigint(20) DEFAULT NULL,
  `pic_fail` bigint(20) DEFAULT NULL,
  `pic_unkonwn` bigint(20) DEFAULT NULL,
  `channel` varchar(150) DEFAULT NULL,
  `opt_name` varchar(150) DEFAULT NULL,
  `opt_time` varchar(60) DEFAULT NULL,
  `pic_notOpt` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `tj_userinfo` */

DROP TABLE IF EXISTS `tj_userinfo`;

CREATE TABLE `tj_userinfo` (
  `tj_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '统计表id',
  `area_no` varchar(10) DEFAULT NULL COMMENT '地区id',
  `area_name` varchar(20) DEFAULT NULL COMMENT '地区名称',
  `tj_time` datetime DEFAULT NULL COMMENT '统计时间',
  `user_total` int(10) DEFAULT NULL COMMENT '用户总数',
  `user_increase` int(10) DEFAULT NULL COMMENT '增长数',
  `user_rate_increase` decimal(11,4) DEFAULT NULL COMMENT '增长率',
  `active_user` int(10) DEFAULT NULL COMMENT '活跃用户数',
  `active_user_total` int(10) DEFAULT NULL COMMENT '总活跃用户数',
  `active_increase` int(10) DEFAULT NULL COMMENT '活跃用户增长数',
  `active_rate_increase` decimal(11,4) DEFAULT NULL COMMENT '活跃用户增长率',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`tj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26811 DEFAULT CHARSET=utf8;

/*Table structure for table `trade` */

DROP TABLE IF EXISTS `trade`;

CREATE TABLE `trade` (
  `order_id` varchar(30) DEFAULT NULL,
  `trade_no` varchar(30) DEFAULT NULL COMMENT '支付平台生成的流水号',
  `trade_seq` varchar(30) DEFAULT NULL COMMENT '平台生成的流水号',
  `batch_no` varchar(30) DEFAULT NULL COMMENT '支付宝支出时的批次号',
  `create_time` datetime DEFAULT NULL COMMENT '信息创建时间',
  `trade_time` datetime DEFAULT NULL COMMENT 'trade_time',
  `trade_id` varchar(30) DEFAULT NULL,
  `trade_type` varchar(2) DEFAULT NULL COMMENT '0、收入    1、支出',
  `trade_status` varchar(2) DEFAULT NULL COMMENT '00、等待买家付款  01、等待卖家收款  02、交易成功，且可对该交易做操作  03、交易成功且结束，即不可再做任何操作  04、交易关闭  10、等待退款  11、正在退款  12、发起退款成功  13、退款成功  14、退款失败  15、处理中或银行卡充退',
  `buyer_id` varchar(50) DEFAULT NULL COMMENT '支出账号',
  `buyer_name` varchar(30) DEFAULT NULL COMMENT '支出账号名',
  `seller_id` varchar(50) DEFAULT NULL COMMENT '收款账号',
  `trade_fee` varchar(6) DEFAULT NULL COMMENT '交易金额',
  `channel` varchar(10) DEFAULT NULL COMMENT '渠道，如qunar，elong, chunqiu',
  `trade_channel` varchar(10) DEFAULT NULL COMMENT '支付渠道,如alipay，cbc',
  `operate_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `operate_person` varchar(100) DEFAULT NULL COMMENT '最后操作人',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '失败原因'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `train_base_test` */

DROP TABLE IF EXISTS `train_base_test`;

CREATE TABLE `train_base_test` (
  `id` int(11) NOT NULL DEFAULT '0' COMMENT '主键',
  `train_no` varchar(25) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(25) DEFAULT NULL COMMENT '出发车站',
  `to_city` varchar(25) DEFAULT NULL COMMENT '到达车站',
  `from_time` time DEFAULT NULL COMMENT '出发时间',
  `to_time` time DEFAULT NULL COMMENT '到达时间',
  `begin_city` varchar(25) DEFAULT NULL COMMENT '始发站',
  `end_city` varchar(25) DEFAULT NULL COMMENT '终点站',
  `train_type` varchar(25) DEFAULT NULL COMMENT '列车类别',
  `time_cost` time DEFAULT NULL COMMENT '历时',
  `yz_price` varchar(50) DEFAULT NULL COMMENT '硬座票价',
  `rz_price` varchar(50) DEFAULT NULL COMMENT '软座票价',
  `yw_price` varchar(50) DEFAULT NULL COMMENT '硬卧票价(上/中/下)',
  `rw_price` varchar(50) DEFAULT NULL COMMENT '软卧票价(上/下)',
  `ydz_price` varchar(50) DEFAULT NULL COMMENT '一等座票价',
  `edz_price` varchar(50) DEFAULT NULL COMMENT '二等座票价',
  `gw_price` varchar(50) DEFAULT NULL COMMENT '高级软卧票价(上/下)',
  `swz_price` varchar(50) DEFAULT NULL COMMENT '商务座票价',
  `tdz_price` varchar(50) DEFAULT NULL COMMENT '特等座票价',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `modify_by` varchar(25) DEFAULT NULL COMMENT '修改人',
  KEY `NewIndex1` (`train_no`,`from_city`,`to_city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `train_base_testyc` */

DROP TABLE IF EXISTS `train_base_testyc`;

CREATE TABLE `train_base_testyc` (
  `id` int(11) NOT NULL DEFAULT '0' COMMENT '主键',
  `train_no` varchar(25) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(25) DEFAULT NULL COMMENT '出发车站',
  `to_city` varchar(25) DEFAULT NULL COMMENT '到达车站',
  `from_time` time DEFAULT NULL COMMENT '出发时间',
  `to_time` time DEFAULT NULL COMMENT '到达时间',
  `begin_city` varchar(25) DEFAULT NULL COMMENT '始发站',
  `end_city` varchar(25) DEFAULT NULL COMMENT '终点站',
  `train_type` varchar(25) DEFAULT NULL COMMENT '列车类别',
  `time_cost` time DEFAULT NULL COMMENT '历时',
  `yz_price` varchar(50) DEFAULT NULL COMMENT '硬座票价',
  `rz_price` varchar(50) DEFAULT NULL COMMENT '软座票价',
  `yw_price` varchar(50) DEFAULT NULL COMMENT '硬卧票价(上/中/下)',
  `rw_price` varchar(50) DEFAULT NULL COMMENT '软卧票价(上/下)',
  `ydz_price` varchar(50) DEFAULT NULL COMMENT '一等座票价',
  `edz_price` varchar(50) DEFAULT NULL COMMENT '二等座票价',
  `gw_price` varchar(50) DEFAULT NULL COMMENT '高级软卧票价(上/下)',
  `swz_price` varchar(50) DEFAULT NULL COMMENT '商务座票价',
  `tdz_price` varchar(50) DEFAULT NULL COMMENT '特等座票价',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `modify_by` varchar(25) DEFAULT NULL COMMENT '修改人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `train_compete` */

DROP TABLE IF EXISTS `train_compete`;

CREATE TABLE `train_compete` (
  `compete_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `compete_date` datetime DEFAULT NULL COMMENT '竞价日期',
  `compete_time` varchar(50) DEFAULT NULL COMMENT '竞价时段',
  `compete_channel` varchar(10) DEFAULT NULL COMMENT '竞价渠道',
  `compete_goods_1` varchar(10) DEFAULT NULL COMMENT '竞价产品1',
  `compete_goods_2` varchar(10) DEFAULT NULL COMMENT '竞价产品2',
  `compete_money_1` decimal(11,1) DEFAULT NULL COMMENT 'CDG竞价1',
  `compete_money_2` decimal(11,1) DEFAULT NULL COMMENT 'CDG竞价2',
  `compete_money_un_1` decimal(11,1) DEFAULT NULL COMMENT '非CDG竞价1',
  `compete_money_un_2` decimal(11,1) DEFAULT NULL COMMENT '非CDG竞价2',
  `compete_ranking_1` varchar(2) DEFAULT NULL COMMENT 'CDG排名1',
  `compete_ranking_2` varchar(2) DEFAULT NULL COMMENT 'CDG排名2',
  `compete_ranking_un_1` varchar(2) DEFAULT NULL COMMENT '非CDG排名1',
  `compete_ranking_un_2` varchar(2) DEFAULT NULL COMMENT '非CDG排名2',
  `compete_top` varchar(200) DEFAULT NULL COMMENT 'CDG前五名',
  `compete_top_un` varchar(200) DEFAULT NULL COMMENT '非CDG前五名',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `count_1` int(11) DEFAULT NULL COMMENT '票数1',
  `count_2` int(11) DEFAULT NULL COMMENT '票数2',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`compete_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='火车票竞价表';

/*Table structure for table `train_compete_log` */

DROP TABLE IF EXISTS `train_compete_log`;

CREATE TABLE `train_compete_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(2000) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `compete_money_1` decimal(11,1) DEFAULT NULL COMMENT '下个小时CDG竞价1',
  `compete_money_un_1` decimal(11,1) DEFAULT NULL COMMENT '下个小时非CDG竞价1',
  `compete_money_2` decimal(11,1) DEFAULT NULL COMMENT '下个小时CDG竞价2',
  `compete_money_un_2` decimal(11,1) DEFAULT NULL COMMENT '下个小时非CDG竞价2',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/*Table structure for table `train_gongdan` */

DROP TABLE IF EXISTS `train_gongdan`;

CREATE TABLE `train_gongdan` (
  `gongdan_id` varchar(50) NOT NULL COMMENT '工单id',
  `system` varchar(10) DEFAULT NULL COMMENT '所属系统',
  `question` varchar(1000) DEFAULT NULL COMMENT '问题描述',
  `answer_name` varchar(10) DEFAULT NULL COMMENT '发起人',
  `create_time` datetime DEFAULT NULL COMMENT '发起时间',
  `reply_time` datetime DEFAULT NULL COMMENT '开始时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  `reply_season` varchar(10) DEFAULT '00' COMMENT '处理结果:00未处理，11处理中，22已处理（默认00）',
  PRIMARY KEY (`gongdan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单管理';

/*Table structure for table `train_phone_plat` */

DROP TABLE IF EXISTS `train_phone_plat`;

CREATE TABLE `train_phone_plat` (
  `phone_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `telephone` varchar(13) DEFAULT NULL COMMENT '通知电话',
  `phone_name` varchar(100) DEFAULT NULL COMMENT '机器人名称',
  `phone_status` varchar(2) DEFAULT NULL COMMENT '通知状态 00、未发送， 11 正在发送， 22、发送成功， 33、发送失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  `send_num` int(2) DEFAULT '0' COMMENT '发送次数',
  `content` varchar(256) DEFAULT NULL COMMENT '发送内容',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `phone_channel` varchar(2) DEFAULT '1' COMMENT '短信渠道: 1、19e  2、鼎鑫亿动 3、企信通',
  `source_channel` varchar(20) DEFAULT NULL COMMENT '请求发送短信渠道来源',
  `fail_reason` varchar(50) DEFAULT NULL COMMENT '发送失败原因',
  `msg_type` varchar(2) DEFAULT '00' COMMENT '00：允许重复发送；11：永久禁止重复发送 22：一小时内禁止重复发送 33：当天内禁止重复发送',
  `phone_channel_ext` varchar(2) DEFAULT '00' COMMENT '短信前缀：01：高铁精灵 02 19旅行 03 酷游旅游',
  PRIMARY KEY (`phone_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='短信平台表';

/*Table structure for table `train_question_order` */

DROP TABLE IF EXISTS `train_question_order`;

CREATE TABLE `train_question_order` (
  `question_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `question_theme` varchar(50) DEFAULT NULL COMMENT '标题内容',
  `question_desc` varchar(1000) DEFAULT NULL COMMENT '问题描述',
  `question_status` varchar(2) DEFAULT NULL COMMENT '问题状态：11。新提交。22.已解决。33。问题重现',
  `question_answer` varchar(10) DEFAULT NULL COMMENT '提出人',
  `question_answer_time` datetime DEFAULT NULL COMMENT '提出时间',
  `question_solve` varchar(10) DEFAULT NULL COMMENT '解决人',
  `question_solve_time` datetime DEFAULT NULL COMMENT '解决时间',
  `question_pic` varchar(500) DEFAULT NULL COMMENT '附件',
  `question_assigner` varchar(10) DEFAULT NULL COMMENT '指定人',
  `question_order_id` varchar(50) DEFAULT NULL COMMENT '问题订单号',
  `question_seq` varchar(50) DEFAULT NULL COMMENT '流水号',
  `question_reply` varchar(500) DEFAULT NULL COMMENT '问题答复',
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 COMMENT='火车票问题订单反馈表';

/*Table structure for table `train_question_order_log` */

DROP TABLE IF EXISTS `train_question_order_log`;

CREATE TABLE `train_question_order_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(2000) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `question_seq` varchar(50) DEFAULT NULL COMMENT '问题流水号',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=231 DEFAULT CHARSET=utf8;

/*Table structure for table `train_return_optlog` */

DROP TABLE IF EXISTS `train_return_optlog`;

CREATE TABLE `train_return_optlog` (
  `return_id` varchar(2) NOT NULL DEFAULT '' COMMENT '编号',
  `return_name` varchar(1000) DEFAULT NULL COMMENT '出票日志',
  `return_value` varchar(10) DEFAULT NULL COMMENT '返回结果',
  `return_type` varchar(2) DEFAULT NULL COMMENT '处理方式 00:重发 11:失败 22：人工',
  `return_status` varchar(2) DEFAULT NULL COMMENT '是否启用0：否。1：是',
  `return_join` varchar(2) DEFAULT NULL COMMENT '是否参与0：否。1：是',
  `return_ticket` varchar(2) DEFAULT NULL COMMENT '11、出票；22、退票。',
  `return_fail_reason` varchar(2) DEFAULT NULL COMMENT '错误信息：1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验',
  `return_active` varchar(2) DEFAULT NULL COMMENT '00预订机器人；11支付机器人；22改签机器人；33退票机器人；',
  PRIMARY KEY (`return_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票出票系统返回日志表（针对所有渠道）';

/*Table structure for table `train_system_log` */

DROP TABLE IF EXISTS `train_system_log`;

CREATE TABLE `train_system_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=343 DEFAULT CHARSET=utf8;

/*Table structure for table `train_system_setting` */

DROP TABLE IF EXISTS `train_system_setting`;

CREATE TABLE `train_system_setting` (
  `setting_id` varchar(50) NOT NULL COMMENT '主键',
  `setting_name` varchar(50) DEFAULT NULL COMMENT '名称',
  `setting_value` varchar(2000) DEFAULT NULL COMMENT '内容',
  `setting_status` varchar(2) DEFAULT NULL COMMENT '状态 0:关闭 1:启用',
  `setting_desc` varchar(100) DEFAULT NULL COMMENT '说明',
  `show_name` varchar(50) DEFAULT NULL COMMENT '显示名称',
  `show_type` varchar(10) DEFAULT NULL COMMENT '单选radio 多选checkbox 文本text',
  `show_priority` varchar(10) DEFAULT NULL COMMENT '排序',
  `show_list` varchar(1000) DEFAULT NULL COMMENT '选项list',
  `show_display` varchar(2) DEFAULT '0' COMMENT '1、隐藏 0、正常 ',
  PRIMARY KEY (`setting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票系统设置表（针对所有渠道）';

/*Table structure for table `tuniu_asyn_request` */

DROP TABLE IF EXISTS `tuniu_asyn_request`;

CREATE TABLE `tuniu_asyn_request` (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `work_time` datetime DEFAULT NULL,
  `interface_code` varchar(4) NOT NULL,
  `status` varchar(1) DEFAULT '0',
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `tuniu_idempotent` */

DROP TABLE IF EXISTS `tuniu_idempotent`;

CREATE TABLE `tuniu_idempotent` (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `operator` varchar(16) DEFAULT NULL,
  `interface_type` char(4) DEFAULT NULL,
  `process_type` char(4) DEFAULT NULL,
  `condition_count` int(2) DEFAULT '0',
  `reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8;

/*Table structure for table `tuniu_mideng_notify` */

DROP TABLE IF EXISTS `tuniu_mideng_notify`;

CREATE TABLE `tuniu_mideng_notify` (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(32) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `request_type` char(4) DEFAULT NULL COMMENT '请求类型。参考REQUEST_TYPE表',
  `notify_level` int(11) DEFAULT NULL COMMENT '插入名次,从0累加',
  `status` int(1) DEFAULT '0' COMMENT '0:初始， 1:进行中， 8:失败， 9:成功',
  `reason` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`auto_id`),
  KEY `index_tuniu_mideng_notify_order_id` (`order_id`),
  KEY `index_tuniu_mideng_notify_time` (`create_time`,`notify_time`),
  KEY `index_tuniu_mideng_notify_request_type` (`request_type`),
  CONSTRAINT `fk_tuniu_mideng_nofity_request_type` FOREIGN KEY (`request_type`) REFERENCES `request_type` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Table structure for table `tuniu_notify_book` */

DROP TABLE IF EXISTS `tuniu_notify_book`;

CREATE TABLE `tuniu_notify_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_notify_status` varchar(2) DEFAULT NULL COMMENT '出票系统通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `cp_notify_num` int(2) DEFAULT '0' COMMENT '通知出票系统次数',
  `cp_notify_time` datetime DEFAULT NULL COMMENT '通知出票系统开始时间',
  `cp_notify_finish_time` datetime DEFAULT NULL COMMENT '通知出票系统结束时间',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '结果通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_num` int(2) DEFAULT '0' COMMENT '结果通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '结果通知开始时间',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '回调地址',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '结果通知结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_tuniu_notify_book_order_id` (`order_id`),
  KEY `idx_tuniu_notify_book_cp_notify_status` (`cp_notify_status`),
  KEY `idx_tuniu_notify_book_create_time` (`create_time`),
  KEY `idx_tuniu_notify_book_notify_status` (`notify_status`)
) ENGINE=InnoDB AUTO_INCREMENT=6798232 DEFAULT CHARSET=utf8 COMMENT='途牛预订通知表';

/*Table structure for table `tuniu_notify_out` */

DROP TABLE IF EXISTS `tuniu_notify_out`;

CREATE TABLE `tuniu_notify_out` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_notify_status` varchar(2) DEFAULT NULL COMMENT '出票系统通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `cp_notify_num` int(2) DEFAULT '0' COMMENT '通知出票系统次数',
  `cp_notify_time` datetime DEFAULT NULL COMMENT '通知出票系统开始时间',
  `cp_notify_finish_time` datetime DEFAULT NULL COMMENT '通知出票系统结束时间',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '结果通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_num` int(2) DEFAULT '0' COMMENT '结果通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '结果通知开始时间',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '回调地址',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '结果通知结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_tuniu_notify_out_order_id` (`order_id`),
  KEY `idx_tuniu_notify_out_order_id` (`order_id`),
  KEY `idx_tuniu_notify_out_cp_notify_status` (`cp_notify_status`),
  KEY `idx_tuniu_notify_out_create_time` (`create_time`),
  KEY `idx_tuniu_notify_out_notify_status` (`notify_status`)
) ENGINE=InnoDB AUTO_INCREMENT=4016923 DEFAULT CHARSET=utf8 COMMENT='途牛出票通知表';

/*Table structure for table `tuniu_notify_refund` */

DROP TABLE IF EXISTS `tuniu_notify_refund`;

CREATE TABLE `tuniu_notify_refund` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '车票号',
  `refund_id` int(11) DEFAULT NULL COMMENT '退款号',
  `cp_notify_status` varchar(2) DEFAULT NULL COMMENT '出票系统通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `cp_notify_num` int(2) DEFAULT '0' COMMENT '通知出票系统次数',
  `cp_notify_time` datetime DEFAULT NULL COMMENT '通知出票系统开始时间',
  `cp_notify_finish_time` datetime DEFAULT NULL COMMENT '通知出票系统结束时间',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '结果通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_num` int(2) DEFAULT '0' COMMENT '结果通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '结果通知开始时间',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '回调地址',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '结果通知结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_tuniu_notify_refund_order_id` (`order_id`),
  KEY `idx_tuniu_notify_refund_cp_notify_status` (`cp_notify_status`),
  KEY `idx_tuniu_notify_refund_create_time` (`create_time`),
  KEY `idx_tuniu_notify_refund_notify_status` (`notify_status`),
  KEY `idx_tuniu_notify_refund_refund_id` (`refund_id`),
  KEY `idx_tuniu_notify_refund_order_id_cp_id` (`order_id`,`cp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=378172 DEFAULT CHARSET=utf8 COMMENT='途牛退票通知表';

/*Table structure for table `tuniu_order_pushtimeout` */

DROP TABLE IF EXISTS `tuniu_order_pushtimeout`;

CREATE TABLE `tuniu_order_pushtimeout` (
  `push_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '途牛订单号',
  `changeId` varchar(80) DEFAULT NULL COMMENT '改签流水号(改签通知必填)',
  `status` tinyint(2) DEFAULT NULL COMMENT '超时订单类型,1、预定占位超时告警 2、出票超时告警 3、改签占位超时告警 4、改签确认超时告警 5、预定占位失败 6、出票失败 7、改签占位失败 8、改签确认失败',
  `msg` varchar(200) DEFAULT NULL COMMENT '备注信息',
  `updateTime` varchar(20) DEFAULT NULL COMMENT '状态变更时间,可选字段',
  `deal_status` varchar(4) DEFAULT NULL COMMENT '处理状态结果: 00、未处理 11、已处理 22、人工处理 33、超时告警',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`push_id`),
  KEY `order_id_idx` (`order_id`),
  KEY `create_time_idx` (`create_time`),
  KEY `status_idx` (`status`),
  KEY `deal_staus_idx` (`deal_status`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8 COMMENT='途牛推送超时订单记录表';

/*Table structure for table `tuniu_order_queue` */

DROP TABLE IF EXISTS `tuniu_order_queue`;

CREATE TABLE `tuniu_order_queue` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `queue_number` int(5) DEFAULT NULL COMMENT '排队人数',
  `wait_time` varchar(20) DEFAULT NULL COMMENT '排队时间',
  `msg` varchar(200) DEFAULT NULL COMMENT '12306完整备注信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `notify_status` int(5) DEFAULT NULL COMMENT '通知状态    1、等待通知 2、正在通知 3、通知成功 4、通知失败 5、重新通知',
  `notify_num` int(5) DEFAULT '0' COMMENT '通知次数',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `tuniu_orderinfo` */

DROP TABLE IF EXISTS `tuniu_orderinfo`;

CREATE TABLE `tuniu_orderinfo` (
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '订单号',
  `order_name` varchar(20) DEFAULT NULL COMMENT '订单名',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `order_status` varchar(10) DEFAULT '00' COMMENT '订单状态: 00下单成功 11通知出票成功 22预订成功 32、 正在出票 33出票成功 44出票失败   51撤销中 52撤销失败 88 超时订单 23、正在取消 24 取消成功',
  `notice_status` varchar(10) DEFAULT '00' COMMENT '通知状态:00、准备通知  11、开始通知  22、通知完成  33、通知失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `out_fail_reason` varchar(2) DEFAULT NULL COMMENT '出票失败原因：1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符',
  `train_no` varchar(25) DEFAULT NULL COMMENT '车次',
  `from_city_code` varchar(25) DEFAULT NULL COMMENT '出发站简码',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city_code` varchar(25) DEFAULT NULL COMMENT '到达站简码',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_date` date DEFAULT NULL COMMENT '乘车时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `has_seat` tinyint(1) DEFAULT '0' COMMENT '是否出无座票 0、可无座 1、有座',
  `ext_field1` varchar(100) DEFAULT NULL COMMENT '备选1(备选坐席)',
  `ext_field2` varchar(100) DEFAULT NULL COMMENT '备选2',
  `ticket_num` int(2) DEFAULT NULL COMMENT '订单内车票数量',
  `order_type` varchar(4) DEFAULT '1' COMMENT '订单类型  1、先预订后支付 2 先支付后预订 ',
  `pay_limit_time` datetime DEFAULT NULL COMMENT '支付限制时间',
  `contact` varchar(45) DEFAULT NULL COMMENT '联系人姓名',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系人手机',
  `user_name` varchar(50) DEFAULT NULL COMMENT '12306用户',
  `user_pwd` varchar(50) DEFAULT NULL COMMENT '12306密码',
  `isChooseSeats` tinyint(1) DEFAULT NULL COMMENT '是否选座, 1:选, 0:非选',
  `chooseSeats` varchar(50) DEFAULT NULL COMMENT '选座信息(选座个数要和乘客数量一致)',
  `insure_code` varchar(50) DEFAULT NULL COMMENT '保险编号',
  `requestid` varchar(50) DEFAULT NULL COMMENT '灾备requestId',
  PRIMARY KEY (`order_id`),
  KEY `idx_tuniu_orderinfo_create_time_order_status` (`create_time`,`order_status`),
  KEY `idx_tuniu_orderinfo_notice_status` (`notice_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='途牛火车票订单表';

/*Table structure for table `tuniu_orderinfo_cp` */

DROP TABLE IF EXISTS `tuniu_orderinfo_cp`;

CREATE TABLE `tuniu_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `insure_number` varchar(50) DEFAULT NULL COMMENT '保险单号',
  `insure_price` decimal(11,2) DEFAULT NULL COMMENT '保险价格',
  `tuniu_seat_type` varchar(2) DEFAULT NULL COMMENT '途牛座位类型 :0、棚车 1、硬座 2、软座 3、硬卧 4、软卧 5、包厢硬卧 6、高级软卧 7、一等软座 8、二等软座 9、商务座 A、高级动卧 B、混编硬座 C、混编硬卧 D、包厢软座 E、特等软座 F、动卧 G、二人软包 H、一人软包 I、一等双软 J、二等双软 K、混编软座 L、混编软卧 M、一等座 O、二等座 P、特等座 Q、观光座 S、一等包座',
  `seat_type` varchar(11) DEFAULT NULL COMMENT '实际坐席类型 座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `tuniu_ticket_type` varchar(2) DEFAULT NULL COMMENT '途牛车票类型: 1、成人票 2、儿童票 3、学生票 4、残军票',
  `ticket_type` varchar(2) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `tuniu_ids_type` varchar(2) DEFAULT NULL COMMENT '途牛证件类型: 1、二代身份证 2、一代身份证 C、港澳通行证 G、台湾通行证 B、护照 H、外国人居留证',
  `ids_type` varchar(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码',
  `telephone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '00:无退票，11:退票中 22：拒绝退票 33：退票成功，44：退票失败',
  `refund_fail_reason` varchar(200) DEFAULT NULL COMMENT '退款失败原因/拒绝退票原因',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306订单号',
  `refund_12306_money` decimal(11,3) DEFAULT NULL COMMENT '12306退款金额',
  `passenger_id` int(15) DEFAULT NULL COMMENT '途牛乘客序号',
  `reason` tinyint(2) DEFAULT NULL COMMENT '身份核验状态 0、正常 1、待审核 2、未通过',
  PRIMARY KEY (`cp_id`),
  KEY `idx_tuniu_orderinfo_cp_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='途牛火车票订购信息表';

/*Table structure for table `tuniu_orderinfo_history` */

DROP TABLE IF EXISTS `tuniu_orderinfo_history`;

CREATE TABLE `tuniu_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '退款创建时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=126098 DEFAULT CHARSET=utf8;

/*Table structure for table `tuniu_orderinfo_refund` */

DROP TABLE IF EXISTS `tuniu_orderinfo_refund`;

CREATE TABLE `tuniu_orderinfo_refund` (
  `refund_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `tuniu_refund_id` year(4) DEFAULT NULL COMMENT '途牛退票单号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `out_ticket_billno` varchar(30) DEFAULT NULL COMMENT '12306取票单号',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号',
  `refund_money` decimal(11,3) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,3) DEFAULT NULL COMMENT '实际退款金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注/出票失败原因',
  `our_remark` varchar(200) DEFAULT '' COMMENT '出票方备注',
  `refund_12306_seq` varchar(32) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `fail_reason` varchar(2) DEFAULT NULL COMMENT '拒绝退款 原因：elong:1、已取票2、已过时间 3、来电取消，tongcheng:31、已改签 32、已退票 33、已出票，只能在窗口办理退票 3、未查询到该车票 9、退票操作异常，请与客服联系 40、订单所在账号被封 41、无法在线退票，xxx地区处于旅游旺季',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '退款状态：00：等待机器改签;01：重新机器改签;02：开始机器改签;03：机器改签失败;04：等待机器退票；05：重新机器退票；06：开始机器退票；07：机器退票失败；11：退票完成；22：拒绝退票；33：审核退款；72：生成线下退款 73：用户提交线下退款 71线下退款通知中 81车站退票中',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `detail_refund` decimal(11,3) DEFAULT NULL COMMENT '12306详细退款金额',
  `detail_alter` decimal(11,3) DEFAULT NULL COMMENT '详细改签差额的金额',
  `refund_type` varchar(2) DEFAULT NULL COMMENT '退款类型 11、线上退款 22、线下退款（人工退款）33 车站退票 44 申请退款  55,改签票退票',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `callbackurl` varchar(150) DEFAULT NULL COMMENT '退票结果回调地址',
  `user_time` datetime DEFAULT NULL COMMENT '用户退款时间',
  PRIMARY KEY (`refund_id`),
  KEY `idx_tuniu_orderinfo_refund_order_id` (`order_id`),
  KEY `idx_tuniu_orderinfo_refund_cp_id` (`cp_id`),
  KEY `idx_refund_seq` (`refund_seq`),
  KEY `idx_tuniu_orderinfo_refund_option_time_refund_status` (`option_time`,`refund_status`),
  KEY `idx_tuniu_orderinfo_refund_refund_status_refund_type` (`refund_status`,`refund_type`),
  KEY `idx_tuniu_orderinfo_refund_refund_type_refund_notify` (`refund_type`,`refund_status`),
  KEY `idx_tuniu_orderinfo_refund_create_time_refund_status` (`create_time`,`refund_status`)
) ENGINE=InnoDB AUTO_INCREMENT=378173 DEFAULT CHARSET=utf8 COMMENT='途牛退款流水表';

/*Table structure for table `tuniu_orderinfo_student` */

DROP TABLE IF EXISTS `tuniu_orderinfo_student`;

CREATE TABLE `tuniu_orderinfo_student` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `province_code` varchar(10) DEFAULT NULL COMMENT '省份编号',
  `school_code` varchar(10) DEFAULT NULL COMMENT '学校代号',
  `school_name` varchar(150) DEFAULT NULL COMMENT '学校名称',
  `student_no` varchar(50) DEFAULT NULL COMMENT '学号',
  `school_system` varchar(45) DEFAULT NULL COMMENT '学制',
  `enter_year` varchar(10) DEFAULT NULL COMMENT '入学年份',
  `preference_from_station_name` varchar(150) DEFAULT NULL COMMENT '优惠区间起始地名称',
  `preference_from_station_code` varchar(45) DEFAULT NULL COMMENT '优惠区间起始地代号',
  `preference_to_station_name` varchar(150) DEFAULT NULL COMMENT '优惠区间到达地名称',
  `preference_to_station_code` varchar(45) DEFAULT NULL COMMENT '优惠区间到达地代号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`cp_id`),
  KEY `idx_tuniu_orderinfo_cp_student_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='途牛学生票附加信息表';

/*Table structure for table `tuniu_picture` */

DROP TABLE IF EXISTS `tuniu_picture`;

CREATE TABLE `tuniu_picture` (
  `pic_id` varchar(45) NOT NULL COMMENT '图片ID',
  `pic_filename` varchar(100) DEFAULT NULL COMMENT '图片保存文件名',
  `pic_base` varchar(2000) DEFAULT NULL COMMENT '图片base64编码',
  `verify_code` varchar(45) DEFAULT NULL COMMENT '图片验证码',
  `captcha_type` varchar(45) DEFAULT NULL COMMENT '验证码类型occupy 占位,change改签占位',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `effect_time` datetime DEFAULT NULL COMMENT '有效时间',
  `start_time` datetime DEFAULT NULL COMMENT '客户端获取码时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '操作人',
  `status` varchar(45) DEFAULT NULL COMMENT '状态：00、未输入 11、已输入',
  `channel` varchar(45) NOT NULL COMMENT '渠道ID  合作伙伴ID',
  `user_pic_status` int(11) DEFAULT '0' COMMENT '图片是否被用户占用 0 闲置图片 1正在处理中图片',
  `result_status` int(2) DEFAULT '5' COMMENT '图片验证状态：初始值为5， 0 验证失败， 1验证成功。',
  `partner_id` varchar(10) DEFAULT NULL,
  `partner_key` varchar(10) DEFAULT NULL,
  `partner_status` varchar(5) DEFAULT NULL,
  `update_status` varchar(2) DEFAULT '22' COMMENT '上传状态 11、成功 22、未上传     ',
  `shyzmid` varchar(25) DEFAULT NULL COMMENT '商户验证码id（用于反馈商户 验证码错误）',
  `back_status` varchar(2) DEFAULT '00' COMMENT '反馈商户验证结果 00默认 11再次反馈 22反馈失败 33反馈成功',
  `order_id` varchar(32) DEFAULT NULL COMMENT '订单号',
  `fail_reason` varchar(200) DEFAULT NULL COMMENT '失败原因',
  `department` varchar(50) DEFAULT NULL COMMENT '打码部门：11打码团队1、22打码团队2、33打码团队3、99打打码团队其他',
  `verify_code_coord` varchar(150) DEFAULT NULL COMMENT '图片验证码结果的坐标位置',
  PRIMARY KEY (`pic_id`),
  KEY `idx_rh_picture_effect_status` (`effect_time`,`status`,`user_pic_status`),
  KEY `IND_rh_picture_create_time` (`create_time`),
  KEY `idx_rh_picture_time_optren` (`opt_ren`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='验证码图片';

/*Table structure for table `tuniu_refundstation` */

DROP TABLE IF EXISTS `tuniu_refundstation`;

CREATE TABLE `tuniu_refundstation` (
  `station_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `order_status` varchar(20) DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手动导入订单表';

/*Table structure for table `tuniu_refundstation_tj` */

DROP TABLE IF EXISTS `tuniu_refundstation_tj`;

CREATE TABLE `tuniu_refundstation_tj` (
  `station_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `count` int(11) DEFAULT NULL COMMENT '总数',
  `success_num` int(11) DEFAULT NULL COMMENT '符合条件数',
  `fail_num` int(11) DEFAULT NULL COMMENT '已退款数',
  `again_num` int(11) DEFAULT NULL COMMENT '重复数据数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手动导入订单统计表';

/*Table structure for table `tuniu_urge_refund` */

DROP TABLE IF EXISTS `tuniu_urge_refund`;

CREATE TABLE `tuniu_urge_refund` (
  `urge_refund_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `out_ticket_billno` varchar(30) DEFAULT NULL COMMENT '12306出票单号',
  `urge_status` varchar(2) DEFAULT NULL COMMENT '催退款状态 11处理中 22退款成功 33 退款失败 44其他',
  `refund_money` double DEFAULT NULL COMMENT '退款金额',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '失败原因',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `opt_person` varchar(20) DEFAULT NULL COMMENT '操作人',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  PRIMARY KEY (`urge_refund_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8806 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='途牛催退款记录表';

/*Table structure for table `user_suit_info` */

DROP TABLE IF EXISTS `user_suit_info`;

CREATE TABLE `user_suit_info` (
  `gent_id` varchar(22) DEFAULT NULL,
  `suit_id` int(11) NOT NULL AUTO_INCREMENT,
  `suit_count` int(2) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`suit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

/*Table structure for table `verinfo` */

DROP TABLE IF EXISTS `verinfo`;

CREATE TABLE `verinfo` (
  `ver` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `weixin_complain` */

DROP TABLE IF EXISTS `weixin_complain`;

CREATE TABLE `weixin_complain` (
  `complain_id` varchar(50) NOT NULL COMMENT '投诉建议id',
  `cm_phone` varchar(50) DEFAULT NULL COMMENT 'cmpay手机账号',
  `question_type` varchar(2) DEFAULT NULL COMMENT '问题类别  0：订单问题 1：加盟问题 2：配送问题 3：出票问题 4：业务建议 5：其他',
  `question` varchar(1000) DEFAULT NULL COMMENT '问题或建议',
  `answer` varchar(1000) DEFAULT NULL COMMENT '答复',
  `permission` varchar(2) DEFAULT NULL COMMENT '权限 0：全部可见 1：自己可见',
  `create_time` datetime DEFAULT NULL COMMENT '提问时间',
  `reply_time` datetime DEFAULT NULL COMMENT '答复时间',
  `opt_person` varchar(10) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`complain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投诉与建议';

/*Table structure for table `weixin_menu` */

DROP TABLE IF EXISTS `weixin_menu`;

CREATE TABLE `weixin_menu` (
  `menuid` int(8) NOT NULL AUTO_INCREMENT COMMENT '菜单号',
  `menu_name` varchar(8) DEFAULT NULL COMMENT '菜单名',
  `menu_type` varchar(8) DEFAULT NULL COMMENT 'view,click',
  `key` varchar(8) DEFAULT NULL COMMENT 'click必需',
  `url` varchar(256) DEFAULT NULL COMMENT 'view必需',
  `super_button` int(8) DEFAULT NULL COMMENT '上级菜单',
  PRIMARY KEY (`menuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `weixin_noticeinfo` */

DROP TABLE IF EXISTS `weixin_noticeinfo`;

CREATE TABLE `weixin_noticeinfo` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `notice_name` varchar(45) DEFAULT NULL COMMENT '通知名称',
  `notice_status` varchar(10) DEFAULT NULL COMMENT '通知状态 00、未发布 11、已发布  22、已到期',
  `notice_content` varchar(8000) DEFAULT NULL COMMENT '通知内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pub_time` datetime DEFAULT NULL COMMENT '生效时间',
  `stop_time` datetime DEFAULT NULL COMMENT '到期时间',
  `opt_ren` varchar(45) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='通知表';

/*Table structure for table `weixin_oftentraininfo` */

DROP TABLE IF EXISTS `weixin_oftentraininfo`;

CREATE TABLE `weixin_oftentraininfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `train_code` varchar(19) DEFAULT '',
  `from_station` varchar(10) DEFAULT '',
  `arrive_station` varchar(10) DEFAULT '',
  `price` float DEFAULT '0',
  `start_station` varchar(10) DEFAULT '',
  `end_station` varchar(10) DEFAULT '',
  `station` varchar(10) DEFAULT '',
  `show_order` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

/*Table structure for table `weixin_orderinfo` */

DROP TABLE IF EXISTS `weixin_orderinfo`;

CREATE TABLE `weixin_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `ticket_pay_money` decimal(11,3) DEFAULT NULL COMMENT '票价总额',
  `bx_pay_money` decimal(11,3) DEFAULT '0.000' COMMENT '保险总额',
  `ps_pay_money` decimal(11,3) DEFAULT '0.000' COMMENT '配送总额',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 00、预下单 11、支付成功  22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、支付失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `cm_phone` varchar(50) DEFAULT NULL COMMENT '微信用户账号',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `from_city` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `to_city` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` datetime DEFAULT NULL COMMENT '出发时间',
  `to_time` datetime DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `timeout` varchar(2) DEFAULT NULL COMMENT '超时重发（1:需要重发 2:重发完成）',
  `refund_deadline_ignore` varchar(2) DEFAULT NULL COMMENT '1：无视截止时间（发车12小时后为退款截止时间）',
  `refund_total` decimal(11,3) DEFAULT NULL COMMENT '退款金额总计',
  `can_refund` varchar(2) DEFAULT NULL COMMENT '是否可以退款（默认可以退 0:不能退 1:能退）',
  `ext_seat` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `pay_no` varchar(50) DEFAULT NULL COMMENT '支付平台交易流水号',
  `request_id` varchar(50) DEFAULT NULL COMMENT '订单流水号',
  PRIMARY KEY (`order_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_billno` (`out_ticket_billno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票订单信息表';

/*Table structure for table `weixin_orderinfo_bx` */

DROP TABLE IF EXISTS `weixin_orderinfo_bx`;

CREATE TABLE `weixin_orderinfo_bx` (
  `bx_id` varchar(50) NOT NULL COMMENT '保险单号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(50) DEFAULT NULL COMMENT '车票ID',
  `from_name` varchar(45) DEFAULT NULL COMMENT '出发地址',
  `to_name` varchar(45) DEFAULT NULL COMMENT '到达地址',
  `ids_type` varchar(45) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户姓名',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '用户证件号',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL,
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `bx_status` int(11) DEFAULT NULL COMMENT '状态：0、未发送 1、正在发送 2、发送完成 3、正在撤销 4、撤销完成 5、发送失败 6、需要取消 7、取消失败',
  `bx_code` varchar(45) DEFAULT NULL COMMENT '保险单号',
  `bx_billno` varchar(45) DEFAULT NULL COMMENT '服务器端订单号',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付金额',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '进货金额',
  `product_id` varchar(15) DEFAULT NULL COMMENT '车次',
  `effect_date` varchar(45) DEFAULT NULL COMMENT '生效日期',
  `train_no` varchar(45) DEFAULT NULL COMMENT '车次',
  `bx_channel` varchar(45) DEFAULT NULL COMMENT '保险渠道: 1、快保   2、合众',
  PRIMARY KEY (`bx_id`),
  KEY `FK_Reference_weixin_idx` (`order_id`),
  CONSTRAINT `FK_Reference_weixin` FOREIGN KEY (`order_id`) REFERENCES `weixin_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保险单信息';

/*Table structure for table `weixin_orderinfo_bxfp` */

DROP TABLE IF EXISTS `weixin_orderinfo_bxfp`;

CREATE TABLE `weixin_orderinfo_bxfp` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `fp_receiver` varchar(50) DEFAULT NULL COMMENT '收件人',
  `fp_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `fp_zip_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `fp_address` varchar(300) DEFAULT NULL COMMENT '收件地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `weixin_orderinfo_cp` */

DROP TABLE IF EXISTS `weixin_orderinfo_cp`;

CREATE TABLE `weixin_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `ids_type` int(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_weixin2_idx` (`order_id`),
  CONSTRAINT `FK_Reference_weixin2` FOREIGN KEY (`order_id`) REFERENCES `weixin_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='火车票订购信息表';

/*Table structure for table `weixin_orderinfo_history` */

DROP TABLE IF EXISTS `weixin_orderinfo_history`;

CREATE TABLE `weixin_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '退款创建时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

/*Table structure for table `weixin_orderinfo_ps` */

DROP TABLE IF EXISTS `weixin_orderinfo_ps`;

CREATE TABLE `weixin_orderinfo_ps` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `ps_billno` varchar(45) DEFAULT NULL COMMENT '配送单号',
  `ps_company` varchar(45) DEFAULT NULL COMMENT '配送公司',
  `pay_money` decimal(11,3) DEFAULT NULL COMMENT '配送费用',
  `buy_money` decimal(11,3) DEFAULT NULL COMMENT '成本价格',
  `ps_status` varchar(2) DEFAULT NULL COMMENT '配送状态：00、等待配送 11、正在配送 22、配送成功',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `link_name` varchar(45) DEFAULT NULL COMMENT '联系人姓名',
  `link_phone` varchar(45) DEFAULT NULL COMMENT '联系人电话',
  `link_address` varchar(125) DEFAULT NULL COMMENT '联系人地址',
  `link_mail` varchar(45) DEFAULT NULL COMMENT '联系人邮件\n',
  PRIMARY KEY (`order_id`),
  CONSTRAINT `FK_Reference_weixin3` FOREIGN KEY (`order_id`) REFERENCES `weixin_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配送单信息';

/*Table structure for table `weixin_orderinfo_refundstream` */

DROP TABLE IF EXISTS `weixin_orderinfo_refundstream`;

CREATE TABLE `weixin_orderinfo_refundstream` (
  `stream_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `refund_type` varchar(45) DEFAULT NULL COMMENT '退款类型：1、用户退款 2、差额退款 3、出票失败退款 4、改签差额退款 5、改签单退款',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT '付款平台返回的退款交易流水号',
  `refund_money` decimal(11,3) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,3) DEFAULT NULL COMMENT '实际退款金额',
  `alter_tickets_money` decimal(11,3) DEFAULT NULL COMMENT '改签差价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_purl` varchar(100) DEFAULT NULL COMMENT '小票地址',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注/出票失败原因',
  `our_remark` varchar(200) DEFAULT NULL COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refund_status` varchar(2) DEFAULT NULL COMMENT '退款状态：00、准备退款 11、等待退款 22、开始退款  44、退款完成 55、拒绝退款 99、退款失败',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT NULL COMMENT '通知次数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `unrefund_reason` varchar(50) DEFAULT NULL COMMENT '付款平台未成功退款原因',
  PRIMARY KEY (`stream_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_refund_type` (`refund_type`),
  KEY `idx_refund_seq` (`refund_seq`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_refund_status` (`refund_status`),
  KEY `idx_plan_status` (`refund_plan_time`),
  KEY `idx_notify_num` (`notify_num`)
) ENGINE=InnoDB AUTO_INCREMENT=8668 DEFAULT CHARSET=utf8 COMMENT='退款流水表';

/*Table structure for table `weixin_productinfo` */

DROP TABLE IF EXISTS `weixin_productinfo`;

CREATE TABLE `weixin_productinfo` (
  `product_id` varchar(15) NOT NULL COMMENT '产品id',
  `type` int(2) DEFAULT NULL COMMENT '产品类别 0:自有产品 1:保险',
  `name` varchar(32) DEFAULT NULL COMMENT '产品名称',
  `status` int(2) DEFAULT NULL COMMENT '产品状态 0:未上架 1:上架',
  `province_id` varchar(10) DEFAULT NULL COMMENT '所属省份id',
  `city_id` varchar(10) DEFAULT NULL COMMENT '所属城市id',
  `sale_type` varchar(2) DEFAULT NULL COMMENT '计费方式 0：元/件 1：元/月 2：元/年',
  `sale_price` decimal(11,3) DEFAULT NULL COMMENT '售价',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `describe` varchar(800) DEFAULT NULL COMMENT '产品描述',
  `level` varchar(2) DEFAULT NULL COMMENT '级别 0：普通 1：vip',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品表';

/*Table structure for table `weixin_userinfo` */

DROP TABLE IF EXISTS `weixin_userinfo`;

CREATE TABLE `weixin_userinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `phone` varchar(11) DEFAULT NULL COMMENT '用户手机号',
  `password` varchar(15) DEFAULT NULL COMMENT '用户密码',
  `openID` varchar(30) DEFAULT NULL COMMENT '微信openID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Table structure for table `xc_orderinfo` */

DROP TABLE IF EXISTS `xc_orderinfo`;

CREATE TABLE `xc_orderinfo` (
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_name` varchar(50) DEFAULT NULL COMMENT '订单名称',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格(sum_amount)',
  `pay_money_show` decimal(11,2) DEFAULT NULL COMMENT '用户支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `ticket_pay_money` decimal(11,2) DEFAULT NULL COMMENT '票价总额',
  `bx_pay_money` decimal(11,2) DEFAULT '0.00' COMMENT '保险总额',
  `order_status` varchar(10) DEFAULT NULL COMMENT '订单状态 00、未支付 11、支付成功 12、eop发货成功 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 ',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `out_ticket_time` datetime DEFAULT NULL COMMENT '出票时间',
  `out_ticket_type` varchar(2) DEFAULT NULL COMMENT '出票方式11、电子票  22、配送票',
  `opt_time` datetime DEFAULT NULL COMMENT '最后操作时间',
  `opt_ren` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `out_ticket_billno` varchar(45) DEFAULT NULL COMMENT '12306单号',
  `train_no` varchar(10) DEFAULT NULL COMMENT '车次',
  `from_station` varchar(45) DEFAULT NULL COMMENT '出发城市',
  `arrive_station` varchar(45) DEFAULT NULL COMMENT '到达城市',
  `from_time` varchar(10) DEFAULT NULL COMMENT '出发时间',
  `arrive_time` varchar(10) DEFAULT NULL COMMENT '到达时间',
  `travel_time` datetime DEFAULT NULL COMMENT '乘车日期',
  `seat_type` int(11) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `refund_deadline_ignore` varchar(2) DEFAULT NULL COMMENT '1：无视截止时间（发车12小时后为退款截止时间）',
  `refund_total` decimal(11,2) DEFAULT NULL COMMENT '退款金额总计',
  `refund_status` varchar(20) DEFAULT 'NO_RFUND' COMMENT '退款状态：NO_RFUND:无退票 ；REFUNDING:退票中；PART_REFUND:部分退票完成；REFUND_FINISH:退款完成；REFUSE_REFUND:拒绝退款',
  `can_refund` varchar(2) DEFAULT NULL COMMENT '是否可以退款（默认可以退 0:不能退 1:能退）',
  `sms_notify` varchar(1) DEFAULT NULL COMMENT '是否短信通知  1：是；0:否',
  `ext_seat` varchar(45) DEFAULT NULL COMMENT '扩展坐席',
  `merchant_order_id` varchar(50) DEFAULT '' COMMENT '合作商户订单id',
  `order_level` varchar(10) DEFAULT '' COMMENT '订单级别：0：普通订单；1：VIP订单',
  `merchant_id` varchar(50) DEFAULT '' COMMENT '合作商户编号',
  `link_name` varchar(100) DEFAULT '' COMMENT '短信通知联系人',
  `link_phone` varchar(20) DEFAULT '' COMMENT '短信通知号码',
  `order_result_url` varchar(200) DEFAULT '' COMMENT '订单处理结果通知合作商户url',
  `order_pro1` varchar(30) DEFAULT '' COMMENT '备用字段1',
  `order_pro2` varchar(30) DEFAULT '' COMMENT '备用字段2',
  `eop_order_id` varchar(45) DEFAULT NULL COMMENT 'EOP订单号',
  `nopwd_pay_url` varchar(200) DEFAULT NULL COMMENT 'EOP提供无密支付接口地址',
  `eop_pay_number` varchar(30) DEFAULT NULL COMMENT 'EOP支付流水号',
  `eop_refund_url` varchar(200) DEFAULT NULL COMMENT '退款接口URL',
  `send_notify_url` varchar(200) DEFAULT NULL COMMENT '通知eop发货地址',
  `pay_fail_reason` varchar(200) DEFAULT NULL COMMENT '支付失败原因',
  `dealer_id` varchar(50) DEFAULT NULL COMMENT '代理商ID',
  `pay_type` varchar(2) DEFAULT NULL COMMENT '支付类型：0 钱包 1 支付宝',
  `ticket_num` int(2) DEFAULT NULL COMMENT '订单内车票数量',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='携程预订信息表';

/*Table structure for table `xc_orderinfo_cp` */

DROP TABLE IF EXISTS `xc_orderinfo_cp`;

CREATE TABLE `xc_orderinfo_cp` (
  `cp_id` varchar(50) NOT NULL COMMENT '车票ID',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `user_name` varchar(45) DEFAULT NULL COMMENT '用户名',
  `ticket_type` int(11) DEFAULT NULL COMMENT '车票类型0：成人票 1：儿童票',
  `ids_type` int(11) DEFAULT NULL COMMENT '证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 ',
  `user_ids` varchar(45) DEFAULT NULL COMMENT '证件号码\n',
  `telephone` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `pay_money` decimal(11,2) DEFAULT NULL COMMENT '支付价格',
  `buy_money` decimal(11,2) DEFAULT NULL COMMENT '成本价格',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `seat_type` int(2) DEFAULT NULL COMMENT '座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他',
  `train_box` varchar(10) DEFAULT NULL COMMENT '车厢',
  `seat_no` varchar(20) DEFAULT NULL COMMENT '座位号',
  `refund_status` varchar(2) DEFAULT '00' COMMENT '00:无退票，11:退票中 22：拒绝退票 33：退票成功，44：退票失败',
  `refund_fail_reason` varchar(200) DEFAULT NULL COMMENT '退款失败原因/拒绝退票原因',
  `alter_seat_type` varchar(2) DEFAULT NULL,
  `alter_train_box` varchar(5) DEFAULT NULL,
  `alter_seat_no` varchar(10) DEFAULT NULL,
  `alter_buy_money` decimal(11,3) DEFAULT NULL,
  `alter_train_no` varchar(10) DEFAULT NULL,
  `alter_money` decimal(11,3) DEFAULT NULL,
  `refund_12306_money` decimal(11,3) DEFAULT NULL,
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_xc_idx` (`order_id`),
  CONSTRAINT `FK_Reference_xc_idx` FOREIGN KEY (`order_id`) REFERENCES `xc_orderinfo` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='携程乘客信息表';

/*Table structure for table `xc_orderinfo_eopandpaynotify` */

DROP TABLE IF EXISTS `xc_orderinfo_eopandpaynotify`;

CREATE TABLE `xc_orderinfo_eopandpaynotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `eop_order_id` varchar(50) DEFAULT NULL COMMENT 'eop订单id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `send_notify_url` varchar(100) DEFAULT NULL COMMENT '异步通知商户地址',
  `pay_money` varchar(10) DEFAULT NULL COMMENT '支付金额',
  `pay_type` varchar(2) DEFAULT '11' COMMENT '11:代扣；22：自行扣费',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单通知eop发货和出票系统支付表';

/*Table structure for table `xc_orderinfo_history` */

DROP TABLE IF EXISTS `xc_orderinfo_history`;

CREATE TABLE `xc_orderinfo_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '历史表id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单号',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票单号',
  `order_optlog` varchar(245) DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime DEFAULT NULL COMMENT '记录时间',
  `order_time` datetime DEFAULT NULL COMMENT '退款创建时间',
  `opter` varchar(45) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=119425 DEFAULT CHARSET=utf8;

/*Table structure for table `xc_orderinfo_refundnotify` */

DROP TABLE IF EXISTS `xc_orderinfo_refundnotify`;

CREATE TABLE `xc_orderinfo_refundnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票号',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单id',
  `refund_seq` varchar(45) DEFAULT NULL COMMENT '退款流水号(trip_no)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '通知状态：00、未通知 11、准备通知 22、开始通知 33、通知完成  44、通知失败',
  `notify_url` varchar(200) DEFAULT NULL COMMENT '通知地址',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT 'eop退款流水号',
  `eop_refund_status` varchar(2) DEFAULT NULL COMMENT 'eop退款状态',
  `eop_notify_status` varchar(2) DEFAULT NULL COMMENT '通知eop退款通知状态：11、通知失败；22通知成功',
  `eop_refund_time` datetime DEFAULT NULL COMMENT 'EOP实际退款完成时间',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=414 DEFAULT CHARSET=utf8 COMMENT='退票退款通知表';

/*Table structure for table `xc_orderinfo_refundstream` */

DROP TABLE IF EXISTS `xc_orderinfo_refundstream`;

CREATE TABLE `xc_orderinfo_refundstream` (
  `stream_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水自增序号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单Id',
  `refund_type` varchar(1) DEFAULT NULL COMMENT '退款类型：1、用户线上退票 2、用户车站退票 3、改签单退款 4、差额退款 5、出票失败退款',
  `cp_id` varchar(45) DEFAULT NULL COMMENT '车票ID',
  `refund_seq` varchar(50) DEFAULT NULL COMMENT '退款流水号(trip_no)',
  `merchant_refund_seq` varchar(1000) DEFAULT NULL COMMENT '商户退款流水号(request_id)',
  `refund_money` decimal(11,2) DEFAULT NULL COMMENT '退款金额',
  `actual_refund_money` decimal(11,2) DEFAULT NULL COMMENT '实际退款金额',
  `alter_tickets_money` decimal(11,2) DEFAULT NULL COMMENT '改签差价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_purl` varchar(100) DEFAULT NULL COMMENT '小票地址',
  `user_remark` varchar(200) DEFAULT NULL COMMENT '退款发起方备注',
  `our_remark` varchar(200) DEFAULT NULL COMMENT '出票方备注',
  `refund_plan_time` datetime DEFAULT NULL COMMENT '计划退款时间',
  `refund_12306_seq` varchar(300) DEFAULT NULL COMMENT '12306退款流水单号',
  `verify_time` datetime DEFAULT NULL COMMENT '退款审核时间',
  `refund_status` varchar(2) DEFAULT NULL COMMENT '退款状态：00、等待退票 11、同意退票 22、拒绝退票 33、退票完成 44、退票失败  ',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `refund_percent` varchar(5) DEFAULT NULL COMMENT '手续费百分比',
  `channel` varchar(45) DEFAULT NULL COMMENT '渠道(merchant_id 合作商户编号)',
  `eop_refund_status` varchar(2) DEFAULT NULL COMMENT '平台退款状态：11、退款失败；22:、退款成功',
  `eop_refund_money` decimal(11,2) DEFAULT NULL COMMENT '平台实际退款金额',
  `eop_refund_seq` varchar(50) DEFAULT NULL COMMENT '平台退款流水号',
  `eop_refund_time` date DEFAULT NULL COMMENT '平台实际退款完成时间',
  `change_ticket_info` varchar(200) DEFAULT NULL COMMENT '改签明细',
  `return_optlog` varchar(2) DEFAULT NULL COMMENT '返回日志',
  PRIMARY KEY (`stream_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `index_xc_refundstream_refund_status` (`refund_status`),
  KEY `index_xc_refundstream_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=687 DEFAULT CHARSET=utf8 COMMENT='携程退款表';

/*Table structure for table `xc_orderinfo_returnnotify` */

DROP TABLE IF EXISTS `xc_orderinfo_returnnotify`;

CREATE TABLE `xc_orderinfo_returnnotify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `merchant_order_id` varchar(50) DEFAULT NULL COMMENT '商户订单id',
  `order_id` varchar(45) DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、准备通知 11、开始通知 22、通知完成',
  `notify_time` datetime DEFAULT NULL COMMENT '通知开始时间',
  `notify_num` int(3) DEFAULT '0' COMMENT '通知次数',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '通知完成时间',
  `refund_type` varchar(10) DEFAULT NULL COMMENT '退款类型: 001、差额退款；002：出票失败退款',
  `refund_amount` varchar(10) DEFAULT '0' COMMENT '退款金额',
  `fail_reason` varchar(100) DEFAULT NULL COMMENT '出票失败原因',
  PRIMARY KEY (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1801 DEFAULT CHARSET=utf8 COMMENT='订单处理结果通知表';

/*Table structure for table `xc_refundstation` */

DROP TABLE IF EXISTS `xc_refundstation`;

CREATE TABLE `xc_refundstation` (
  `station_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单号',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  `order_status` varchar(20) DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8 COMMENT='手动导入订单表';

/*Table structure for table `xc_refundstation_tj` */

DROP TABLE IF EXISTS `xc_refundstation_tj`;

CREATE TABLE `xc_refundstation_tj` (
  `station_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `create_time` datetime DEFAULT NULL COMMENT '日期',
  `refund_seq` varchar(20) NOT NULL COMMENT '流水号',
  `count` int(11) DEFAULT NULL COMMENT '总数',
  `success_num` int(11) DEFAULT NULL COMMENT '符合条件数',
  `fail_num` int(11) DEFAULT NULL COMMENT '已退款数',
  `again_num` int(11) DEFAULT NULL COMMENT '重复数据数',
  `opt_person` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`station_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='手动导入订单统计表';

/*Table structure for table `yop_account_open` */

DROP TABLE IF EXISTS `yop_account_open`;

CREATE TABLE `yop_account_open` (
  `customer_no` varchar(45) DEFAULT NULL COMMENT '商户编号',
  `order_id` varchar(45) DEFAULT NULL COMMENT '19e订单号',
  `request_id` varchar(70) DEFAULT NULL COMMENT '开户请求号 length<=64',
  `open_status` varchar(10) DEFAULT NULL COMMENT '开户状态: 00,未开户 11,开户查询 22,开户成功 33,开户失败',
  `card_no` varchar(45) DEFAULT NULL COMMENT '卡号',
  `bank_account_no` varchar(45) DEFAULT NULL COMMENT '二类账户号',
  `phone` varchar(45) DEFAULT NULL COMMENT '手机号',
  `id_card_type` varchar(11) DEFAULT NULL COMMENT '证件类型: 0:身份证 ,1:护照证 ,2:港澳通行证 ,3:军官证',
  `id_card_no` varchar(45) DEFAULT NULL COMMENT '证件号码',
  `holder_name` varchar(45) DEFAULT NULL COMMENT '姓名',
  `is_authenticated` tinyint(1) DEFAULT NULL COMMENT '是否已鉴权:0：未鉴权 1：已鉴权',
  `ret_msg` varchar(300) DEFAULT NULL COMMENT '开户返回描述信息',
  `ret_msg_query` varchar(300) DEFAULT NULL COMMENT '开户查询消息提示',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  UNIQUE KEY `request_id` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='易宝银行二类帐户开通';

/*Table structure for table `yop_account_recharge` */

DROP TABLE IF EXISTS `yop_account_recharge`;

CREATE TABLE `yop_account_recharge` (
  `customer_no` varchar(45) DEFAULT NULL COMMENT '商户编号',
  `request_id` varchar(70) DEFAULT NULL COMMENT '商户充值请求号 length<=64',
  `recharge_status` varchar(10) DEFAULT NULL COMMENT '充值状态: 00,未充值  11,充值查询 22,充值成功 33,充值失败',
  `amount` decimal(11,2) DEFAULT NULL COMMENT '充值金额',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '默认： CNY',
  `timeout_express` int(11) DEFAULT NULL COMMENT '充值有效期,单位：分钟，最小 1 分钟，最大 24 小时,默认:1440 分钟',
  `biz_request_id` varchar(20) DEFAULT NULL COMMENT '商户业务流水号，业务查询用(12306 订单号)',
  `bank_account_no` varchar(45) DEFAULT NULL COMMENT '二类账户号',
  `memo` varchar(45) DEFAULT NULL COMMENT '自定义对账字段',
  `goods_name` varchar(45) DEFAULT NULL COMMENT '商品名称 建议传:火车票 最长 20 个汉字',
  `goods_cat` varchar(45) DEFAULT NULL COMMENT '商品类别',
  `goods_des` varchar(45) DEFAULT NULL COMMENT '商品描述',
  `good_ext_info` varchar(1000) DEFAULT NULL COMMENT '商品扩展信息(火车车次+出发时间)json 格式',
  `industry_ext_info` varchar(1000) DEFAULT NULL COMMENT '行业扩展： json 格式',
  `customer_operator` varchar(45) DEFAULT NULL COMMENT '操作员账户[商户]',
  `external_id` varchar(45) DEFAULT NULL COMMENT '唯一流水号(请求成功返回的)',
  `ret_msg` varchar(300) DEFAULT NULL COMMENT '请求充值返回的消息提示',
  `ret_msg_query` varchar(300) DEFAULT NULL COMMENT '充值查询返回的消息提示',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='易宝银行二类帐户充值';

/*Table structure for table `yop_account_refund` */

DROP TABLE IF EXISTS `yop_account_refund`;

CREATE TABLE `yop_account_refund` (
  `customer_no` varchar(45) DEFAULT NULL COMMENT '商户编号',
  `request_id` varchar(45) DEFAULT NULL COMMENT '商户充值请求号，业务查询用',
  `refund_request_id` varchar(45) DEFAULT NULL COMMENT '商户退回请求号，业务查询用',
  `refund_status` varchar(10) DEFAULT NULL COMMENT '退回状态: 00,未退回 11,退回查询 22,退回成功 33,退回失败',
  `refund_amount` decimal(11,3) DEFAULT NULL COMMENT '退回金额,以元为单位，精确到分。如： 100.00',
  `memo` varchar(45) DEFAULT NULL COMMENT '自定义对账字段',
  `external_id` varchar(45) DEFAULT NULL COMMENT '唯一流水号',
  `bank_account_no` varchar(45) DEFAULT NULL COMMENT '二类账户号',
  `ret_msg` varchar(300) DEFAULT NULL COMMENT '请求退回返回的消息提示',
  `ret_msg_query` varchar(300) DEFAULT NULL COMMENT '查询退回返回的消息提示',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='易宝银行二类帐户退款';

/*Table structure for table `yop_open_notify` */

DROP TABLE IF EXISTS `yop_open_notify`;

CREATE TABLE `yop_open_notify` (
  `notify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
  `customer_no` varchar(45) DEFAULT NULL COMMENT '商户编号',
  `request_id` varchar(45) DEFAULT NULL COMMENT '请求号',
  `notify_status` varchar(2) DEFAULT '00' COMMENT '通知状态：00、未通知 11、准备通知 22、开始通知 33、通知完成 44、通知失败',
  `notify_num` int(2) DEFAULT '0' COMMENT '结果通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '结果通知开始时间',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '回调地址',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '结果通知结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_type` varchar(2) DEFAULT NULL COMMENT '通知类型: 1、请求开户推送 2、查询开户推送 3、开户结果回调出票系统',
  `version_` int(11) DEFAULT '0' COMMENT '版本号',
  UNIQUE KEY `notify_id` (`notify_id`)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8 COMMENT='开户通知表';

/*Table structure for table `yop_recharge_notify` */

DROP TABLE IF EXISTS `yop_recharge_notify`;

CREATE TABLE `yop_recharge_notify` (
  `customer_no` varchar(45) DEFAULT NULL COMMENT '商户编号',
  `request_id` varchar(45) DEFAULT NULL COMMENT '请求号',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '结果通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_num` int(2) DEFAULT NULL COMMENT '结果通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '结果通知开始时间',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '回调地址',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '结果通知结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_type` varchar(2) DEFAULT NULL COMMENT '通知类型：1,充值请求推送 2,充值查询推送 3,充值结果回调出票系统'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='帐户充值通知表';

/*Table structure for table `yop_refund_notify` */

DROP TABLE IF EXISTS `yop_refund_notify`;

CREATE TABLE `yop_refund_notify` (
  `customer_no` varchar(45) DEFAULT NULL COMMENT '商户编号',
  `request_id` varchar(45) DEFAULT NULL COMMENT '请求号',
  `notify_status` varchar(2) DEFAULT NULL COMMENT '结果通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败',
  `notify_num` int(2) DEFAULT NULL COMMENT '结果通知次数',
  `notify_time` datetime DEFAULT NULL COMMENT '结果通知开始时间',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '回调地址',
  `notify_finish_time` datetime DEFAULT NULL COMMENT '结果通知结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `notify_type` varchar(2) DEFAULT NULL COMMENT '通知类型: 1,退回请求推送 2,退回查询推送 '
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='帐户退款通知表';

/*Table structure for table `zjpj_a` */

DROP TABLE IF EXISTS `zjpj_a`;

CREATE TABLE `zjpj_a` (
  `xh` int(11) DEFAULT NULL,
  `cc` varchar(19) DEFAULT NULL,
  `fz` varchar(10) DEFAULT NULL,
  `dz` varchar(10) DEFAULT NULL,
  `yz` float DEFAULT NULL,
  `rz` float DEFAULT NULL,
  `yws` float DEFAULT NULL,
  `ywz` float DEFAULT NULL,
  `ywx` float DEFAULT NULL,
  `rws` float DEFAULT NULL,
  `rwx` float DEFAULT NULL,
  `rz2` float DEFAULT NULL,
  `rz1` float DEFAULT NULL,
  KEY `cc_index` (`cc`),
  KEY `fz` (`fz`,`dz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `zm` */

DROP TABLE IF EXISTS `zm`;

CREATE TABLE `zm` (
  `zmhz` varchar(10) DEFAULT NULL,
  `py` varchar(5) DEFAULT NULL,
  `lh` varchar(3) DEFAULT NULL,
  `tcs` int(11) DEFAULT NULL,
  `qpy` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `zm_test` */

DROP TABLE IF EXISTS `zm_test`;

CREATE TABLE `zm_test` (
  `zmhz` varchar(10) DEFAULT NULL,
  `py` varchar(5) DEFAULT NULL,
  `lh` varchar(3) DEFAULT NULL,
  `tcs` int(11) DEFAULT NULL,
  `qpy` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* Procedure structure for procedure `demo` */

/*!50003 DROP PROCEDURE IF EXISTS  `demo` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`hcpiao`@`%` PROCEDURE `demo`()
BEGIN
	insert into student (name) values ('event');  
	
END */$$
DELIMITER ;

/* Procedure structure for procedure `getAccForVerifyRand` */

/*!50003 DROP PROCEDURE IF EXISTS  `getAccForVerifyRand` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`hcp`@`%` PROCEDURE `getAccForVerifyRand`(
	in p_channel varchar(45), 
	out p_accId varchar(25), 
	out p_accUsername varchar(25), 
	out p_accPassword varchar(25)
)
BEGIN
  select 
	CONVERT(acc_id, CHAR),acc_username,acc_password  
  into 
	p_accId,p_accUsername,p_accPassword
  from
    cp_accountinfo 
 
  where acc_status = '33' 
    and channel = p_channel 
    AND is_alive = 1 
  order by rand() 
 
  limit 1 for update ;
    END */$$
DELIMITER ;

/* Procedure structure for procedure `jdSelectCard` */

/*!50003 DROP PROCEDURE IF EXISTS  `jdSelectCard` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`hcpiao`@`%` PROCEDURE `jdSelectCard`()
BEGIN
	/**
	 *创建于2017-03-03
	 *用于定时任务查询预付卡,对预付卡余额进行更新
	 *taokai
	 */
	DECLARE notfound INT DEFAULT 0; 
	DECLARE temp INT;
	DECLARE temp_update_status INT; 
	
	/**
	 *查询预付卡,状态不为22(停用),以操作时间排序
	 */
	DECLARE cur CURSOR FOR SELECT card_id FROM jd_card WHERE card_id <> 1 AND update_status <> (SELECT update_status FROM jd_card WHERE card_id = 1) AND card_status <> '22' ORDER BY option_time DESC LIMIT 0,1 ; 
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET notfound = 1;
	OPEN cur;
	FETCH cur INTO temp;
	/*
	 *如果不存在,则更新查询标识的核验状态,重新查询；否则返回该条数据
	 */
	SELECT update_status INTO temp_update_status FROM jd_card WHERE card_id = 1;
	IF notfound = 1 THEN
		IF temp_update_status = 0 THEN
			SET temp_update_status:=1;  
			UPDATE jd_card SET update_status = 1 WHERE card_id = 1;
		ELSE
			SET temp_update_status:=0;  
			UPDATE jd_card SET update_status = 0 WHERE card_id = 1;
		END IF;
		SELECT card_no AS cardNo, card_pwd AS cardPwd  FROM jd_card WHERE card_id <> 1 AND update_status <> temp_update_status AND card_status <> '22' ORDER BY option_time DESC LIMIT 0,1;
	ELSE
		SELECT card_no AS cardNo, card_pwd AS cardPwd FROM jd_card WHERE card_id = temp;
	END IF;
	UPDATE jd_card SET update_status = temp_update_status WHERE card_id = temp;
	CLOSE cur;
END */$$
DELIMITER ;

/* Procedure structure for procedure `releaseProxyIp` */

/*!50003 DROP PROCEDURE IF EXISTS  `releaseProxyIp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`hcpiao`@`%` PROCEDURE `releaseProxyIp`()
BEGIN
	/**
	 *创建于2017-03-03
	 *用于释放代理IP的使用状态,状态使用中,并且使用时间小于当前时间-10分钟。这种状况的代理IP进行释放
	 *taokai
	 *每5秒执行一次
	 */
	#call demo;
	DECLARE notfound INT DEFAULT 0; 
	DECLARE temp INT;
	DECLARE temp_use_time DATETIME;
	DECLARE cur CURSOR FOR SELECT auto_id FROM proxy_ip WHERE auto_id <> 1 AND  STATUS = 0 AND
		(jd_use_time < DATE_SUB(NOW(),INTERVAL 10 MINUTE) AND jd_status <> 0) 
		OR (tn_use_time < DATE_SUB(NOW(),INTERVAL 10 MINUTE) AND tn_status <> 0)
		or (l2306_use_time < DATE_SUB(NOW(),INTERVAL 10 MINUTE) AND tn_status <> 0);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET notfound = 1;
	OPEN cur;
	FETCH cur INTO temp;
	IF notfound = 0 THEN
		#查询到数据
		#京东使用
		SELECT jd_use_time INTO temp_use_time FROM proxy_ip WHERE auto_id = temp;
		IF temp_use_time < DATE_SUB(NOW(),INTERVAL 10 MINUTE) THEN 
			UPDATE proxy_ip SET jd_status = 0 WHERE auto_id = temp;
		END IF;
		
		#途牛使用
		SELECT tn_use_time INTO temp_use_time FROM proxy_ip WHERE auto_id = temp;
		IF temp_use_time < DATE_SUB(NOW(),INTERVAL 10 MINUTE) THEN 
			UPDATE proxy_ip SET tn_status = 0 WHERE auto_id = temp;
		END IF;
		
		#12306
		SELECT l2306_use_time INTO temp_use_time FROM proxy_ip WHERE auto_id = temp;
		IF temp_use_time < DATE_SUB(NOW(),INTERVAL 10 MINUTE) THEN 
			UPDATE proxy_ip SET l2306_status = 0 WHERE auto_id = temp;
		END IF;
	END IF;
END */$$
DELIMITER ;

/* Procedure structure for procedure `selectProxyIp` */

/*!50003 DROP PROCEDURE IF EXISTS  `selectProxyIp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`hcpiao`@`%` PROCEDURE `selectProxyIp`()
BEGIN
	/**
	 *创建于2017-03-03
	 *用于定时任务查询代理IP,核验代理IP是否可用
	 *taokai
	 */
	DECLARE notfound INT DEFAULT 0; 
	DECLARE temp INT;
	DECLARE temp_check_status INT; 
	
	/*
	 *定义游标,根据查询标识查询出状态0(可用)的代理IP
	 * 0初始化状态, 1可用, 8人工备用, 9停用
	 */
	DECLARE cur CURSOR FOR SELECT auto_id FROM proxy_ip WHERE auto_id <> 1 AND STATUS <= 0 AND check_status <> (SELECT check_status FROM proxy_ip WHERE auto_id = 1) LIMIT 0,1 ; 
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET notfound = 1;
	
	OPEN cur;
	FETCH cur INTO temp;
	
	/*
	 *如果不存在,则更新查询标识的核验状态,重新查询；否则返回该条数据
	 */
	IF notfound = 1 THEN
		SELECT check_status INTO temp_check_status FROM proxy_ip WHERE auto_id = 1;
		IF temp_check_status = 0 THEN
			SET temp_check_status:=1;  
			UPDATE proxy_ip SET check_status = 1 WHERE auto_id = 1;
		ELSE
			SET temp_check_status:=0;  
			UPDATE proxy_ip SET check_status = 0 WHERE auto_id = 1;
		END IF;
		SELECT ip, PORT, username, PASSWORD  FROM proxy_ip WHERE auto_id <> 1 AND STATUS <= 0 AND check_status <> temp_check_status  LIMIT 0,1 ; 
	ELSE
	   SELECT ip, PORT, username, password  FROM proxy_ip WHERE auto_id = temp; 
	END IF;
	UPDATE proxy_ip SET check_status = temp_check_status WHERE auto_id = temp;
	CLOSE cur;
END */$$
DELIMITER ;

/* Procedure structure for procedure `updateOrderStatusAndSendMessage` */

/*!50003 DROP PROCEDURE IF EXISTS  `updateOrderStatusAndSendMessage` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`hcp`@`%` PROCEDURE `updateOrderStatusAndSendMessage`()
BEGIN
	DECLARE _order_id VARCHAR(50) DEFAULT '';
	LOOP
	SELECT order_id INTO _order_id FROM cp_orderinfo WHERE order_status = '88';
 
	UPDATE cp_orderinfo SET order_status = '99' WHERE order_id=_order_id;
	UPDATE cp_orderinfo_notify SET notify_num = 0 AND notify_status = 1 WHERE order_id=_order_id; 
	END LOOP;
    END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
