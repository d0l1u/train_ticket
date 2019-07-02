package com.kuyou.train.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kuyou.train.common.Constant;
import com.kuyou.train.common.enums.*;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.util.BigDecimalUtil;
import com.kuyou.train.common.util.TaskExecutorEnumUtil;
import com.kuyou.train.common.util.TrainTimeUtil;
import com.kuyou.train.entity.dto.*;
import com.kuyou.train.entity.kyfw.PassengerData;
import com.kuyou.train.entity.req.BookReq;
import com.kuyou.train.entity.req.BookTicketReq;
import com.kuyou.train.entity.req.PassengerReq;
import com.kuyou.train.entity.resp.BookResp;
import com.kuyou.train.entity.resp.BookTicketResp;
import com.kuyou.train.http.cookie.CacheCookieJar;
import com.kuyou.train.kyfw.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BookService
 *
 * @author taokai3
 * @date 2018/12/1
 */
@Slf4j
@Service
public class BookService {
	@Resource
	private JedisClient orderJedisClient;

	@Resource
    private LoginApiRobot loginApiRobot;

    @Resource
    private QueryOrderApiRobot queryOrderApiRobot;

    @Resource
    private LeftTicketApiRobot leftTicketApiRobot;

    @Resource
    private PassengerApiRobot passengerApiRobot;

    @Resource
    private RequestOrderApiRobot requestOrderApiRobot;

    @Resource
    private ConfirmApiRobot confirmApiRobot;

    @Resource
    private OrderApiRobot orderApiRobot;

    @Resource
    private JedisClient flagJedisClient;

    private static final int SLEEP = 2000;
    private static final int ADD_MAX = 3;

    public BookResp book(BookReq bookReq) throws IOException {
        String orderId = bookReq.getOrderId();
        String username = bookReq.getUsername();
        String password = bookReq.getPassword();
        List<BookTicketReq> tickets = bookReq.getTickets();
        log.info(" orderId:{}, username:{}, password:{}", orderId, username, password);

        //判断是同时存在学生票和成人票
        TicketTypeEnum ticketTypeEnum = TicketTypeEnum.STUDENT;
        for (BookTicketReq ticket : tickets) {
            PassengerTypeEnum typeEnum = PassengerTypeEnum.getByKyfw(ticket.getTicketType());
            if (!PassengerTypeEnum.XUE_SHENG.equals(typeEnum)) {
                ticketTypeEnum = TicketTypeEnum.ADULT;
                break;
            }
        }

        //查询余票
        String trainCode = bookReq.getTrainCode();
        String fromStationCode = bookReq.getFromStationCode();
        String toStationCode = bookReq.getToStationCode();
        Date fromDate = bookReq.getFromDate();
        TrainTicketDto leftTicketDto = leftTicketApiRobot.queryTicket4Order(true, trainCode, fromDate, fromStationCode, toStationCode, ticketTypeEnum);
        leftTicketDto.setFromStationName(bookReq.getFromStationName());
        leftTicketDto.setToStationName(bookReq.getToStationName());

        //判断code是否相同
        if (!fromStationCode.equals(leftTicketDto.getFromStationCode()) || !toStationCode.equals(leftTicketDto.getToStationCode())) {
            throw new TrainException("三字码有误");
        }

        //将酷游无座，转成车次支持的无座坐席
        String seatType = tickets.get(0).getSeatType();
        log.info("本次占位坐席:{}", seatType);
        if (SeatTypeEnum.WU_ZUO.getKyfw().equals(seatType)) {
            //将无座 还原成对应的坐席
            seatType = leftTicketDto.getWuZuoSeat().getKyfw();
        }

        //缓存出发时间到本地
        TrainTimeUtil.set(leftTicketDto.getTrainTime());

        //登录
        loginApiRobot.checkLoginStatus(username, password);

        //查询未完成订单
        String trainInfo = bookReq.trainInfo();
        List<String> ticketInfo = bookReq.ticketInfo();
        NoCompleteOrderDto orderDto = queryOrderApiRobot.queryNoComplete("", trainInfo, ticketInfo, Lists.newArrayList(OrderStatusEnum.WAIT_PAY));
        if (orderDto != null) {
            if (OrderStatusEnum.WAIT_ING.equals(orderDto.getStatusEnum())) {
                //取消排队订单
                try {
                    orderApiRobot.cancelQueueOrder(true);
                } catch (Exception e) {
                    log.info("取消排队订单异常", e);
                }
            } else {
                //解析未完成订单结果
                return parseResponse(orderId, bookReq, orderDto);
            }
        }

        String key = String.format("passenger_%s_%s", orderId, username);
        String flag = "";
        try {
            flag = flagJedisClient.get(key);
        } catch (Exception e) {
            log.error("获取缓存中乘客处理标识异常", e);
        }

        //添加联系人 or 删除联系人
        log.info("联系人处理标识{}:{}", key, flag);
        if (StringUtils.isBlank(flag) || !flag.equals(Constant.Y)) {
            passengerCrud(tickets, username, password);
			//添加成功，进行缓存标识
			try {
				flagJedisClient.setex(key,20 * 60, Constant.Y );
			} catch (Exception e) {
				log.error("缓存乘客处理标识异常", e);
			}
        }

        //申请下单
        requestOrderApiRobot.submitOrderRequest(fromDate, fromDate, leftTicketDto, true);

        //填单初始页面
        ConfirmDto confirmDto = confirmApiRobot.initXc(true);

        //填单
        StringBuffer passengerTicketStrBf = new StringBuffer();
        StringBuffer oldPassengerStrBf = new StringBuffer();
        String finalChangeSeatType = seatType;
        tickets.forEach(ticket -> {
            passengerTicketStrBf.append(ticket.getPassengerTicketStr(finalChangeSeatType));
            oldPassengerStrBf.append(ticket.getOldPassengerStr());
        });
        String passengerTicketStr = passengerTicketStrBf.toString();
        passengerTicketStr = passengerTicketStr.substring(0, passengerTicketStr.length() - 1);
        String oldPassengerStr = oldPassengerStrBf.toString();


        String isAsync = confirmDto.getIsAsync();
        log.info("下单模式:{}", isAsync);
        if ("1".equals(isAsync)) {
            confirmApiRobot.confirmOrder1(fromDate, seatType, tickets.size(),bookReq.getChooseSeats(),
                    confirmDto, passengerTicketStr, oldPassengerStr, true);
        } else {
            confirmApiRobot.confirmOrder0(seatType, bookReq.getChooseSeats(), confirmDto, passengerTicketStr,
                    oldPassengerStr, true);
        }

        //反查获取订单信息
        orderDto = queryOrderApiRobot.queryNoCompleteBack("", trainInfo, ticketInfo, OrderStatusEnum.WAIT_PAY);
        //判断订单结果
        String statusCode = orderDto.getStatus();
        if (OrderStatusEnum.BOOK_SEAT_FAIL.getCode().equals(statusCode)) {
            throw new TrainException(orderDto.getMessage());
        }

        //解析订单
        return parseResponse(orderId, bookReq, orderDto);
    }

    /**
     * 订单解析
     *
     * @param orderId
     * @param bookReq
     * @param orderDto
     * @return
     */
    private BookResp parseResponse(String orderId, BookReq bookReq, NoCompleteOrderDto orderDto) throws IOException {
        //获取出发到达时间
        TrainTimeDto timeDto = TrainTimeUtil.get();
        log.info("本次缓存出发到站时间:{}", timeDto);
        Date fromTime = orderDto.getFromTime();
        Date toTime = null;
        if(timeDto != null ){
            fromTime = timeDto.getFromTime();
            toTime = timeDto.getToTime();
        }

        BookResp.BookRespBuilder bookRespBuilder = BookResp.builder();
        bookRespBuilder.success(true).trainCode(orderDto.getTrainCode()).fromDate(orderDto.getFromDate());
        bookRespBuilder.totalPrice(BigDecimalUtil.dividePrice(orderDto.getTotalPrice())).payLimitTime(orderDto.getPayLimitTime());
        bookRespBuilder.fromTime(fromTime).toTime(toTime).sequence(orderDto.getSequence());
        bookRespBuilder.fromStationCode(orderDto.getFromStationCode()).fromStationName(orderDto.getFromStationName());
        bookRespBuilder.toStationCode(orderDto.getToStationCode()).toStationName(orderDto.getToStationName());

        Map<String, List<OrderTicketDto>> map = orderDto.getTickets().stream().collect(Collectors.groupingBy(OrderTicketDto::getKey));
        final boolean[] isWuZuo = {false};
        final String[] noExist = {""};
        List<BookTicketResp> ticketResps = bookReq.getTickets().stream().map(req -> {
            String key = req.getKey();
            log.info("key:{}, map:{}",key, map);
            List<OrderTicketDto> ticketDtos = map.get(key);
            if(CollectionUtils.isEmpty(ticketDtos)){
                noExist[0] = key;
                return null;
            }
            OrderTicketDto dto = ticketDtos.remove(0);
            String coachName = dto.getCoachName();
            String seatName = dto.getSeatName();
            if(coachName.contains("无") || seatName.contains("无")){
                isWuZuo[0] = true;
            }
            return BookTicketResp.builder().cpId(req.getCpId()).name(req.getName()).subSequence(dto.getSubSequence())
                    .price(BigDecimalUtil.dividePrice(dto.getPrice())).coachNo(dto.getCoachNo()).coachName(coachName)
                    .seatNo(dto.getSeatNo()).seatName(seatName).build();
        }).collect(Collectors.toList());

        if(StringUtils.isNotBlank(noExist[0])){
            throw new TrainException("反查订单失败, 出现不存在的乘客:" + noExist[0]);
        }

		boolean acceptNoSeat = bookReq.isAcceptNoSeat();
		if(!acceptNoSeat && isWuZuo[0]){
			log.info("用户不接受无座，转取消");
			orderApiRobot.cancelOrder(orderDto.getSequence());
			throw new TrainException("订到无座票, 用户不接受无座");
		}

		return  bookRespBuilder.orderId(orderId).tickets(ticketResps).build();
    }

    /**
     * 预订占位，乘客操作
     * @param passengers
     * @param username
	 * @param password
	 * @throws IOException
     */
	private void passengerCrud(List<? extends PassengerReq> passengers, String username, String password) throws IOException {
		try {
			//将重复的乘客过滤
			List<? extends PassengerReq> distinctList = passengers.stream().distinct().collect(Collectors.toList());

			//查询全部联系人
			List<PassengerData> allPassengers = passengerApiRobot.queryPassenger("");
			int allSize = allPassengers.size();
			log.info("账号内共有常用联系人 {} 个", allSize);

			//删除状态，不可以订票的乘客
			allPassengers = deleteExceptionPassenger(allPassengers);

			//定义需要操作的乘客列表
			List<PassengerDto> addList = Lists.newArrayList();
			List<PassengerDto> editList = Lists.newArrayList();
			List<PassengerDto> editUserList = Lists.newArrayList();
			//进行对比，并进行分类
			classifyPassenger(allPassengers, distinctList, addList, editList, editUserList);

			//判断添加的乘客
			allSize = allPassengers.size();
			int addSize = addList.size();
			log.info("allSize:{}, addSize:{}", allSize, addSize);
			if (15 - allSize < addSize) {
				//联系人坑位不足
				throw new TrainException("账号内可添加常用旅客坑位不足");
			}

			//添加联系人
			for (PassengerDto add : addList) {
				log.info("添加乘客:{}", add.getName());
				passengerApiRobot.addPassenger(add);
			}

			//修改联系人
			for (PassengerDto edit : editList) {
				log.info("修改乘客:{}", edit.getName());
				passengerApiRobot.editPassenger(edit);
			}

			//修改账号所属人
			for (PassengerDto edit : editUserList) {
				log.info("修改账号所属人:{}", edit.getName());
				passengerApiRobot.editUser(edit);
			}

			if (CollectionUtils.isNotEmpty(addList)) {
				try {
					Thread.sleep(SLEEP);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//再次查询全部联系人，判断乘客是否核验通过，上述代码添加过一次，所以这里的索引从1开始
			int index = 1;
			while (index < ADD_MAX && CollectionUtils.isNotEmpty(addList)) {
				log.info("第 {} 次判断乘客核验状态", index);
				if (index != 1) {
					//添加乘客
					for (PassengerDto add : addList) {
						log.info("第 {} 次，重新添加乘客:{}", index, add.getName());
						passengerApiRobot.addPassenger(add);
					}
					try {
						Thread.sleep(SLEEP);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				index++;
				//查询全部常用联系人
				Map<String, List<PassengerData>> kyfwMap = passengerApiRobot.queryAllPassenger4Map();
				for (int i = 0; i < addList.size(); i++) {
					PassengerDto passengerReq = addList.get(i);
					String cardNo = passengerReq.getCardNo();
					String name = passengerReq.getName();
					List<PassengerData> kyfwDataList = kyfwMap.get(cardNo);
					if (CollectionUtils.isEmpty(kyfwDataList)) {
						continue;
					}
					PassengerData kfywPassenger = kyfwDataList.get(0);
					String totalTimes = kfywPassenger.getTotalTimes();
					CardTypeEnum cardTypeEnum = CardTypeEnum.getByKyfw(kfywPassenger.getPassengerIdTypeCode());

					//判断是否可以订票
					if (cardTypeEnum.canBuyTicket(totalTimes)) {
						log.info("乘客name:{} 核验通过", name);
						addList.remove(i--);
					} else {
						log.info("乘客name:{} 核验未通过, cardType:{}, totalTimes:{}", name, cardTypeEnum, totalTimes);
					}
				}
				//addList中剩下的元素，即可视为‘不可订票’的状态，进行删除，重新添加
				if (CollectionUtils.isNotEmpty(addList)) {
					passengerApiRobot.deletePassenger4Try(addList);
				}
			}

			//判断addList是否为空，如果为空，则证明全部核验成功，否则核验失败
			if (CollectionUtils.isNotEmpty(addList)) {
				throw new TrainException(String.format("%s 核验状态不可购票", addList.get(0).getName()));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			//上传白名单
			String cookie = new CacheCookieJar().getCookie();
			ThreadPoolTaskExecutor executor = TaskExecutorEnumUtil.INSTANCE.init();
			executor.execute(new Thread() {
				@Override
				public void run() {
					try {
						new CacheCookieJar().setCookie(cookie);
						Map<String, ? extends List<? extends PassengerReq>> pMap = passengers.stream().collect(
								Collectors.groupingBy(p -> {
									return p.getCardNo().toUpperCase().trim();
								}));

						JSONObject json = new JSONObject(true);
						json.put("username", username);
						json.put("password", password);
						List<PassengerData> passengerDataList = passengerApiRobot.queryPassenger("");
						List<JSONObject> passengers = passengerDataList.stream().map(data -> {
							String name = data.getPassengerName();
							String cardType = data.getPassengerIdTypeCode();
							String cardNo = data.getPassengerIdNo();
							String isUserSelf = data.getIsUserSelf();
							Date deleteTime = data.getDeleteTime();
							JSONObject pjson = new JSONObject(true);
							pjson.put("name", name);
							pjson.put("cardType", cardType);
							pjson.put("cardNo", cardNo);
							pjson.put("isUserSelf", isUserSelf);
							pjson.put("deleteTime", deleteTime);

							List<? extends PassengerReq> list = pMap.get(cardNo);
							if(CollectionUtils.isNotEmpty(list)){
								pjson.put("lastUseTime", new Date());
							}
							return pjson;
						}).collect(Collectors.toList());
						json.put("passengers", passengers);

						//白名单数据缓存队列中
						int index = 0;
						while (index < 3){
							try {
								orderJedisClient.lpush(KeyEnum.WHITE_lIST.getValue(), json.toJSONString());
								return;
							}catch (Exception e){
								log.info("上传白名单异常", e);
							}
							Thread.sleep(3000);
						}
					} catch (Exception e) {
						log.info("上传乘客信息异常", e);
					}finally {
						new CacheCookieJar().clearCookie();
					}
				}
			});
		}
	}

    /**
     * 将常用旅客进行分类
     *
     * @param kyfwPassengerList     ：账号内已经存在的乘客
     * @param distinctPassengerList ：本次占位去除重复后的乘客
     * @param addList               ：需要进行添加的乘客
     * @param editList              ：需要进行修改的乘客
     * @param editUserList          ：需要进行修改账号所属人的乘客
     */
    private Map<String, List<PassengerData>> classifyPassenger(List<PassengerData> kyfwPassengerList,
            List<? extends PassengerReq> distinctPassengerList, List<PassengerDto> addList, List<PassengerDto> editList,
            List<PassengerDto> editUserList) {
        //将账号内的list转成map
        Map<String, List<PassengerData>> kyfwMap = kyfwPassengerList.stream()
                .collect(Collectors.groupingBy(PassengerData::getPassengerIdNo));
        //对比乘客是否在账号内
        distinctPassengerList.forEach(passengerReq -> {
            String name = passengerReq.getName();
            String cardNo = passengerReq.getCardNo().toUpperCase().replaceAll("\\s", "");
            String passengerTypeReq = passengerReq.getTicketType();
            List<PassengerData> passengerDataList = kyfwMap.get(cardNo);
            if (CollectionUtils.isEmpty(passengerDataList)) {
                //乘客不存在。进行添加
                log.info("乘客:{} 不存在账号内，需要添加", name);
                PassengerDto.Builder builder = PassengerDto.builder();
                //基本信息
                builder.name(name).passengerType(passengerTypeReq).cardNo(cardNo).cardType(passengerReq.getCardType());
                //特殊证件参数
                builder.validDateEnd(passengerReq.getValidDateEnd()).bornDate(passengerReq.getBornDate())
                        .countryCode(passengerReq.getCountryCode()).birthDate(passengerReq.getBornDate());
                //学生票信息
                builder.provinceCode(passengerReq.getProvinceCode()).schoolName(passengerReq.getSchoolName())
                        .schoolCode(passengerReq.getSchoolCode()).studentNo(passengerReq.getStudentNo())
                        .system(passengerReq.getSystem()).enterYear(passengerReq.getEnterYear())
                        .stuFromStationName(passengerReq.getStuFromStationName())
                        .stuFromStationCode(passengerReq.getStuFromStationCode())
                        .stuToStationName(passengerReq.getStuToStationName())
                        .stuToStationCode(passengerReq.getStuToStationCode());
                addList.add(builder.build());
                return;
            }

            //存在乘客，判断是否需要进行修改信息
            PassengerData kfywPassenger = passengerDataList.get(0);
            String passengerTypeKyfw = kfywPassenger.getPassengerType();
            if (passengerTypeKyfw.equals(passengerTypeReq)) {
                log.info("乘客:{} 信息一致，不需要修改", name);
                return;
            }
            //如果入参为成人票，则可以不用修改
            if (PassengerTypeEnum.CHE_NGREN.getKyfw().equals(passengerTypeReq)) {
                log.info("乘客:{} 预订成人票，不需要修改", name);
                return;
            }
            //如果入参为‘儿童票’，并且原乘客也是‘成人’，则可以不用修改
            if (PassengerTypeEnum.ER_TONG.getKyfw().equals(passengerTypeReq) &&
                    PassengerTypeEnum.CHE_NGREN.getKyfw().equals(passengerTypeKyfw)) {
                log.info("乘客:{} 为‘儿童’，原类型为‘成人’，不需要修改");
                return;
            }
            PassengerDto.Builder builder = PassengerDto.builder();
            //基本信息
            builder.name(name).passengerType(passengerTypeReq).cardNo(cardNo).cardType(passengerReq.getCardType());
            //特殊证件参数
            builder.validDateEnd(passengerReq.getValidDateEnd()).bornDate(passengerReq.getBornDate())
                    .countryCode(passengerReq.getCountryCode()).birthDate(passengerReq.getBornDate());
            //学生票信息
            builder.provinceCode(passengerReq.getProvinceCode()).schoolName(passengerReq.getSchoolName())
                    .schoolCode(passengerReq.getSchoolCode()).studentNo(passengerReq.getStudentNo())
                    .system(passengerReq.getSystem()).enterYear(passengerReq.getEnterYear())
                    .stuFromStationName(passengerReq.getStuFromStationName())
                    .stuFromStationCode(passengerReq.getStuFromStationCode())
                    .stuToStationName(passengerReq.getStuToStationName())
                    .stuToStationCode(passengerReq.getStuToStationCode());
            //设置原先乘客信息
            builder.oldName(kfywPassenger.getPassengerName()).oldCardType(kfywPassenger.getPassengerIdTypeCode())
                    .oldCardNo(kfywPassenger.getPassengerIdNo());

            //判断是修改常用联系人还是修改账号所属人
            if (kfywPassenger.getIsUserSelf().equals(Constant.Y)) {
                log.info("需要修改账号所属人:{}", name);
                editUserList.add(builder.build());
            } else {
                log.info("需要修改常用旅客:{}", name);
                editList.add(builder.build());
            }
        });
        return kyfwMap;
    }

    /**
     * 过滤不可订票的乘客，并删除该乘客
     *
     * @param passengerDataList
     */
    private List<PassengerData> deleteExceptionPassenger(List<PassengerData> passengerDataList)throws IOException {
        List<PassengerDelDto> deleteDtos = Lists.newArrayList();
        for (int i = 0; i < passengerDataList.size(); i++) {
            PassengerData data = passengerDataList.get(i);
            String cardType = data.getPassengerIdTypeCode();
            CardTypeEnum cardTypeEnum = CardTypeEnum.getByKyfw(cardType);
            String name = data.getPassengerName();
            String cardNo = data.getPassengerIdNo();
            String totalTimes = data.getTotalTimes();
            if (CardTypeEnum.TWO_OPEN.contains(cardTypeEnum)) {
                //中国居住证（中国居民居住证，港澳台居民居住证，外国人永久居住证）
                if (!CardTypeEnum.TWO_OPEN_STATUS_LIST.contains(totalTimes)) {
                    log.info("现有乘客:{}, {}, {}-{}, 不可订票, 进行删除", name, totalTimes, cardType, cardTypeEnum.getTitle());
                    deleteDtos.add(PassengerDelDto.builder().name(name).cardNo(cardNo).cardType(cardType).build());
                    passengerDataList.remove(i--);
                }
            } else {
                //其他（港澳通行证，台湾通行证，护照）
                if (!CardTypeEnum.OTHER_OPEN_STATUS_LIST.contains(totalTimes)) {
                    log.info("现有乘客:{}, {}, {}-{}, 不可订票, 进行删除", name, totalTimes, cardType, cardTypeEnum.getTitle());
                    deleteDtos.add(PassengerDelDto.builder().name(name).cardNo(cardNo).cardType(cardType).build());
                    passengerDataList.remove(i--);
                }
            }
        }
        if (CollectionUtils.isEmpty(deleteDtos)) {
            return passengerDataList;
        }

        //批量删除联系人
        try {
            log.info("本次删除常用联系人:{}", JSON.toJSONString(deleteDtos));
            passengerApiRobot.deletePassenger(deleteDtos);
        } catch (Exception e) {
            log.info("删除不可订票乘客异常，重新查询账号内的全部乘客", e);
            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e1) {
            }
            return passengerApiRobot.queryPassenger("");
        }
        return passengerDataList;
    }
}
