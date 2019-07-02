package com.l9e.common;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {

	// 联通优势错误代码
	private static final Map<String, String> payErrorCodeMap = new HashMap<String, String>();
	// 联通优势订单支付状态
	private static final Map<String, String> upayOrderStatusMap = new HashMap<String, String>();
	// 联通优势订单退款状态
	private static final Map<String, String> upayRefundStatusMap = new HashMap<String, String>();
	static {
		payErrorCodeMap.put("0000", "处理成功");
		payErrorCodeMap.put("00060999", "系统忙，请稍后再试。");
		payErrorCodeMap.put("00060700", "数据校验未通过");
		payErrorCodeMap.put("00020000", "数据库错误");
		payErrorCodeMap.put("00060009", "商户没有注册");
		payErrorCodeMap.put("00060027", "请求参数不完整");
		payErrorCodeMap.put("00060059", "用户媒介不合法");
		payErrorCodeMap.put("00060066", "产品结算账户已开通");
		payErrorCodeMap.put("00060067", "商户内部账户已开通");
		payErrorCodeMap.put("00060488", "卡已锁定");
		payErrorCodeMap.put("00060490", "银行卡日限额超限");
		payErrorCodeMap.put("00060491", "银行卡月限额超限");
		payErrorCodeMap.put("00060492", "银行卡已超过日交易限制次数");
		payErrorCodeMap.put("00060493", "银行卡月交易次数超限");
		payErrorCodeMap.put("00060494", "手机号次限额超限");
		payErrorCodeMap.put("00060495", "手机号日限额超限");
		payErrorCodeMap.put("00060496", "手机号月限额超限");
		payErrorCodeMap.put("00060497", "手机号日交易次数超限");
		payErrorCodeMap.put("00060498", "手机号月交易次数超限");
		payErrorCodeMap.put("00060499", "银行卡次限额超限");
		payErrorCodeMap.put("00060500", "银行卡号信息不存在");
		payErrorCodeMap.put("00060501", "请求参数不足");
		payErrorCodeMap.put("00060502", "交易金额小于规定的最小金额");
		payErrorCodeMap.put("00060503", "交易金额大于规定的最大金额");
		payErrorCodeMap.put("00060544", "超过信用卡的支付限额");
		payErrorCodeMap.put("00060710", "商户签名验签失败");
		payErrorCodeMap.put("00060711", "商户未开通");
		payErrorCodeMap.put("00060720", "用户手机号错误");
		payErrorCodeMap.put("00060721", "用户未开通该银行");
		payErrorCodeMap.put("00060722", "请重新选择支付银行");
		payErrorCodeMap.put("00060723", "支付密码错误请重新输入");
		payErrorCodeMap.put("00060724", "支付密码错误次数超过3次，支付失败。");
		payErrorCodeMap.put("00060730", "用户余额不足请充值");
		payErrorCodeMap.put("00060740", "生成支付订单失败");
		payErrorCodeMap.put("00060750", "支付失败");
		payErrorCodeMap.put("00060751", "支付超时被冲正");
		payErrorCodeMap.put("00060760", "支付订单不存在");
		payErrorCodeMap.put("00060761", "订单正在支付中请稍后");
		payErrorCodeMap.put("00060762", "订单已过期请重新下单");
		payErrorCodeMap.put("00060763", "订单已关闭");
		payErrorCodeMap.put("00060764", "订单未支付，请继续支付");
		payErrorCodeMap.put("00060765", "支付失败请重新选择支付方式");
		payErrorCodeMap.put("00060766", "不允许退款");
		payErrorCodeMap.put("00060767", "退款请求处理失败");
		payErrorCodeMap.put("00060768", "退款金额与支付金额不一致");
		payErrorCodeMap.put("00060769", "不允许撤销");
		payErrorCodeMap.put("00060770", "撤销金额与支付金额不一致");
		payErrorCodeMap.put("00060771", "订单已撤销");
		payErrorCodeMap.put("00060772", "交易不存在");
		payErrorCodeMap.put("00060780", "订单已支付");
		payErrorCodeMap.put("00060781", "交易已成功");
		payErrorCodeMap.put("00060782", "获取验证码超过限制次数");
		payErrorCodeMap.put("00060792", "验证码错误");
		payErrorCodeMap.put("00060793", "验证码校验超过错误次数");
		payErrorCodeMap.put("00060794", "获取验证码失败");
		payErrorCodeMap.put("00060854", "记账失败");
		payErrorCodeMap.put("00060869", "商户账户未开通");
		payErrorCodeMap.put("00060875", "卡bin校验失败，系统不支持该卡");
		payErrorCodeMap.put("00060975", "鉴权失败，请求支付金额大于单次支付金额");
		payErrorCodeMap.put("00070004", "银行卡号无效");
		payErrorCodeMap.put("00070015", "银行卡密码错误");
		payErrorCodeMap.put("00080530", "信用卡已过期");
		payErrorCodeMap.put("00080531", "手机号或cvv2错误");
		payErrorCodeMap.put("00080532", "手机号错误");
		payErrorCodeMap.put("00080533", "CVV2错误");
		payErrorCodeMap.put("00009999", "未知错误");
		payErrorCodeMap.put("00060004", "尊敬的用户,您的手机和银行帐户绑定记录不存在!");
		payErrorCodeMap.put("00060008", "用户手机号码无效");
		payErrorCodeMap.put("00060022", "您输入的证件号码或用户姓名有误");
		payErrorCodeMap.put("00060072", "协议不存在");
		payErrorCodeMap.put("00060124", "业务协议依赖的支付协议关系不存在");
		payErrorCodeMap.put("00060305", "银行卡号信息不存在");
		payErrorCodeMap.put("00060306", "银行卡已锁定");
		payErrorCodeMap.put("00060322", "商户产品次限额不符");
		payErrorCodeMap.put("00060424", "鉴权传入参数格式错误");
		payErrorCodeMap.put("00060535", "余额不足");
		payErrorCodeMap.put("00060550", "尊敬的用户，交易异常，支付失败。请稍后再试。");
		payErrorCodeMap.put("00060553", "支付请求重复");
		payErrorCodeMap.put("00060594", "银行支付失败");
		payErrorCodeMap.put("00060604", "查询请求数据错误");
		payErrorCodeMap.put("00060633", "无扩展订单记录");
		payErrorCodeMap.put("00060725", "支付鉴权异常");
		payErrorCodeMap.put("00060774", "累计退款金额大于支付金额");
		payErrorCodeMap.put("00060778", "退款记录不存在");
		payErrorCodeMap.put("00060861", "财务处理中");
		payErrorCodeMap.put("00060874", "该时间段交易暂停");
		payErrorCodeMap.put("00060876", "重复下单：分账类型不匹配");
		payErrorCodeMap.put("00060884", "订单状态不正确,不允许退款");
		payErrorCodeMap.put("00060886", "预授权撤销已成功");
		payErrorCodeMap.put("00060892", "预授权申请,完成,撤销订单号不能相同");
		payErrorCodeMap.put("00060911", "重复支付时支付失败");
		payErrorCodeMap.put("00060920", "不支持当日退费");
		payErrorCodeMap.put("00060927", "预授权完成处理中");
		payErrorCodeMap.put("00060928", "预授权撤销处理中");
		payErrorCodeMap.put("00060929", "预授权申请处理中");
		payErrorCodeMap.put("00061003", "没收卡，请联系收单行");
		payErrorCodeMap.put("00061005", "超出卡取款金额限制");
		payErrorCodeMap.put("00061007", "不允许持卡人交易");
		payErrorCodeMap.put("00061009", "银行返回交易失败");
		payErrorCodeMap.put("00061010", "受卡方呼受理方安全保密部门");
		payErrorCodeMap.put("00061014", "银行卡密码错误");
		payErrorCodeMap.put("00080508", "订单源地址与配置不匹配");
		payErrorCodeMap.put("00080534", "银行卡号与证件号码不符");
		payErrorCodeMap.put("00080537", "银行账户余额不足");
		payErrorCodeMap.put("00080540", "去银行支付超时");
		payErrorCodeMap.put("00080541", "发卡行拒付");
		payErrorCodeMap.put("00080543", "银行密码输入次数超限");
		payErrorCodeMap.put("00080550", "无可用的支付通道");
		payErrorCodeMap.put("00080557", "银行返回失败");
		payErrorCodeMap.put("00080569", "通信失败");
		payErrorCodeMap.put("00080572", "注册用户失败");
		payErrorCodeMap.put("00080605", "银行返回的支付结果是中间状态");
		payErrorCodeMap.put("00080700", "数据校验不通过");
		payErrorCodeMap.put("00080705", "验证码错误");
		payErrorCodeMap.put("00080706", "验证码一分钟获取多次");
		payErrorCodeMap.put("00080707", "同一订单号请求验证码次数超过最大次数");
		payErrorCodeMap.put("00080709", "验证码错误次数超过最大次数");
		payErrorCodeMap.put("00080710", "请求手机号和上次获取验证码手机号不一致");
		payErrorCodeMap.put("00080711", "验证码已失效");
		payErrorCodeMap.put("00080728", "交易冲正成功");
		payErrorCodeMap.put("00080732", "系统不支持该类卡支付");
		payErrorCodeMap.put("00080748", "订单金额超过风控最大次限额");
		payErrorCodeMap.put("00090006", "尊敬的用户，您查找的原始交易失败或交易不存在。");
		payErrorCodeMap.put("00090010", "尊敬的用户，该交易无法正常进行，请与发卡行联系。");
		payErrorCodeMap.put("00090013", "尊敬的用户，银行返回交易失败。");
		payErrorCodeMap.put("00090019", "连续多次输入错误密码");
		payErrorCodeMap.put("00090020", "尊敬的用户，您的银行卡已过期或输入的有效期错误");
		payErrorCodeMap.put("00090021", "尊敬的用户，交易异常，支付失败。请稍后再试");
		payErrorCodeMap.put("00131023", "商户付款配置不存在");
		payErrorCodeMap.put("00131053", "获取费率失败");
		payErrorCodeMap.put("00160022", "订单金额大于交易最大限额");
		payErrorCodeMap.put("00160023", "新用户金额超过次限额");
		payErrorCodeMap.put("00160024", "新用户超过日限额");
		payErrorCodeMap.put("00160026", "新用户超过日次数");
		payErrorCodeMap.put("00160028", "银行新用户金额超过次限额");
		payErrorCodeMap.put("00180008", "支付结果不明");
		payErrorCodeMap.put("00180011", "非支付环节调用外部服务超时");
		payErrorCodeMap.put("00200004", "支付要素与下发短信时上送的信息不一致,请重新获取短信验证码");
		payErrorCodeMap.put("00200005", "没有找到相关短信验证码");
		payErrorCodeMap.put("00200006", "验证码错误");
		payErrorCodeMap.put("00200008", "验证码一分钟内获取多次");
		payErrorCodeMap.put("00200009", "请求验证码次数超过最大次数");
		payErrorCodeMap.put("00200010", "验证码错误次数超过最大次数");
		payErrorCodeMap.put("00200012", "验证码已失效");
		payErrorCodeMap.put("00200013", "商户未开通银行");
		payErrorCodeMap.put("00200014", "返回给商户的，支付结果不明");
		payErrorCodeMap.put("00200025", "实名认证失败，卡信息有误");
		payErrorCodeMap.put("00200026", "信用卡已过有效期");
		payErrorCodeMap.put("00200027", "商户产品对应的银行未开通");
		payErrorCodeMap.put("00200029", "请求业务系统通讯异常");

		// 初始化upayOrderStatusMap
		upayOrderStatusMap.put("WAIT_BUYER_PAY", "交易创建，等待买家付款");
		upayOrderStatusMap.put("TRADE_SUCCESS", "交易成功，不能再次进行交易");
		upayOrderStatusMap.put("TRADE_CLOSED",
				"交易关闭, 在指定时间段内未支付时关闭的交易(交易关闭，商户支付已经过期的订单后，订单状态才会改变为交易关闭)");
		upayOrderStatusMap.put("TRADE_CANCEL", "交易撤销");
		upayOrderStatusMap.put("TRADE_FAIL", "交易失败");

		// 初始化upayRefundStatusMap
		upayRefundStatusMap.put("REFUND_SUCCESS", "退费成功");
		upayRefundStatusMap.put("REFUND_FAIL", "退费失败");
		upayRefundStatusMap.put("REFUND_PROCESS", "退费处理中");
		upayRefundStatusMap.put("REFUND_UNKNOWN", "退费结果未知");
	}

	public static Map<String, String> getPayErrorCode() {
		return payErrorCodeMap;
	}

	public static Map<String, String> getUpayOrderStatusMap() {
		return upayOrderStatusMap;
	}

	public static Map<String, String> getUpayRefundStatusMap() {
		return upayRefundStatusMap;
	}
}
