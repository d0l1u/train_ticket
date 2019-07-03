package test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.common.TrainConsts;
import com.l9e.controller.CtripDataInerface;
import com.l9e.entity.CtripResultData;
import com.l9e.entity.CtripSeatData;
import com.l9e.entity.CtripZhanZhanData;
import com.l9e.entity.OuterSoukdNewData;
import com.l9e.entity.TrainNewData;

public class DealJsonTest {

	private static final Logger logger = Logger.getLogger(CtripDataInerface.class);

	public static void main(String[] args) {

		String jsonStr = "{\"ResponseStatus\":{\"Timestamp\":\"\\/Date(1516010321396+0800)\\/\",\"Ack\":\"Success\",\"Errors\":[],\"Extension\":[{\"Id\":\"CLOGGING_TRACE_ID\",\"Value\":\"6733867434787000563\"},{\"Id\":\"RootMessageId\",\"Value\":\"921812-0a1c7f29-421113-7025847\"}]},\"Trains\":[{\"StartTime\":\"12:21\",\"ArriveTime\":\"05:01\",\"FromStationName\":\"北京西\",\"ToStationName\":\"九江\",\"FromStationTypeName\":\"起点\",\"ToStationTypeName\":\"途经\",\"TrainNo\":\"K1453\",\"Train12306No\":\"24000K14530K\",\"DurationMinutes\":1000,\"DayDiff\":1,\"ControlDay\":29,\"SaleTime\":\"08:00\",\"ToTelcode\":\"JJG\",\"FromTelcode\":\"BXP\",\"CanWebBuy\":\"Y\",\"Seats\":[{\"SeatName\":\"硬座\",\"Price\":163.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧上\",\"Price\":280.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧中\",\"Price\":289.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧下\",\"Price\":299.5,\"TicketLeft\":99},{\"SeatName\":\"软卧上\",\"Price\":438.5,\"TicketLeft\":1},{\"SeatName\":\"软卧下\",\"Price\":456.5,\"TicketLeft\":1},{\"SeatName\":\"无座\",\"Price\":163.5,\"TicketLeft\":99}],\"IsSupportCard\":\"0\"},{\"StartTime\":\"16:02\",\"ArriveTime\":\"03:09\",\"FromStationName\":\"北京西\",\"ToStationName\":\"九江\",\"FromStationTypeName\":\"起点\",\"ToStationTypeName\":\"途经\",\"TrainNo\":\"Z307\",\"Train12306No\":\"240000Z30706\",\"DurationMinutes\":667,\"DayDiff\":1,\"ControlDay\":29,\"SaleTime\":\"08:00\",\"ToTelcode\":\"JJG\",\"FromTelcode\":\"BXP\",\"CanWebBuy\":\"Y\",\"Seats\":[{\"SeatName\":\"硬座\",\"Price\":163.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧上\",\"Price\":280.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧中\",\"Price\":289.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧下\",\"Price\":299.5,\"TicketLeft\":0},{\"SeatName\":\"软卧上\",\"Price\":438.5,\"TicketLeft\":0},{\"SeatName\":\"软卧下\",\"Price\":456.5,\"TicketLeft\":0},{\"SeatName\":\"无座\",\"Price\":163.5,\"TicketLeft\":99}],\"IsSupportCard\":\"0\"},{\"StartTime\":\"16:53\",\"ArriveTime\":\"09:26\",\"FromStationName\":\"北京西\",\"ToStationName\":\"九江\",\"FromStationTypeName\":\"起点\",\"ToStationTypeName\":\"途经\",\"TrainNo\":\"K571\",\"Train12306No\":\"240000K57113\",\"DurationMinutes\":993,\"DayDiff\":1,\"ControlDay\":29,\"SaleTime\":\"08:00\",\"ToTelcode\":\"JJG\",\"FromTelcode\":\"BXP\",\"CanWebBuy\":\"Y\",\"Seats\":[{\"SeatName\":\"硬座\",\"Price\":163.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧上\",\"Price\":280.5,\"TicketLeft\":8},{\"SeatName\":\"硬卧中\",\"Price\":289.5,\"TicketLeft\":8},{\"SeatName\":\"硬卧下\",\"Price\":299.5,\"TicketLeft\":8},{\"SeatName\":\"软卧上\",\"Price\":438.5,\"TicketLeft\":0},{\"SeatName\":\"软卧下\",\"Price\":456.5,\"TicketLeft\":0},{\"SeatName\":\"无座\",\"Price\":163.5,\"TicketLeft\":99}],\"IsSupportCard\":\"0\"},{\"StartTime\":\"18:50\",\"ArriveTime\":\"06:15\",\"FromStationName\":\"北京\",\"ToStationName\":\"九江\",\"FromStationTypeName\":\"起点\",\"ToStationTypeName\":\"途经\",\"TrainNo\":\"Z71\",\"Train12306No\":\"2400000Z7109\",\"DurationMinutes\":685,\"DayDiff\":1,\"ControlDay\":29,\"SaleTime\":\"10:00\",\"ToTelcode\":\"JJG\",\"FromTelcode\":\"BJP\",\"CanWebBuy\":\"Y\",\"Seats\":[{\"SeatName\":\"硬座\",\"Price\":163.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧上\",\"Price\":280.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧中\",\"Price\":289.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧下\",\"Price\":299.5,\"TicketLeft\":0},{\"SeatName\":\"软卧上\",\"Price\":438.5,\"TicketLeft\":0},{\"SeatName\":\"软卧下\",\"Price\":456.5,\"TicketLeft\":0},{\"SeatName\":\"无座\",\"Price\":163.5,\"TicketLeft\":0}],\"IsSupportCard\":\"0\"},{\"StartTime\":\"19:13\",\"ArriveTime\":\"06:22\",\"FromStationName\":\"北京西\",\"ToStationName\":\"九江\",\"FromStationTypeName\":\"起点\",\"ToStationTypeName\":\"途经\",\"TrainNo\":\"Z133\",\"Train12306No\":\"240000Z1330K\",\"DurationMinutes\":669,\"DayDiff\":1,\"ControlDay\":29,\"SaleTime\":\"08:00\",\"ToTelcode\":\"JJG\",\"FromTelcode\":\"BXP\",\"CanWebBuy\":\"Y\",\"Seats\":[{\"SeatName\":\"硬座\",\"Price\":163.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧上\",\"Price\":280.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧中\",\"Price\":289.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧下\",\"Price\":299.5,\"TicketLeft\":0},{\"SeatName\":\"软卧上\",\"Price\":438.5,\"TicketLeft\":0},{\"SeatName\":\"软卧下\",\"Price\":456.5,\"TicketLeft\":0},{\"SeatName\":\"高级软卧上\",\"Price\":806.5,\"TicketLeft\":0},{\"SeatName\":\"高级软卧下\",\"Price\":840.5,\"TicketLeft\":0},{\"SeatName\":\"无座\",\"Price\":163.5,\"TicketLeft\":99}],\"IsSupportCard\":\"0\"},{\"StartTime\":\"19:47\",\"ArriveTime\":\"06:41\",\"FromStationName\":\"北京西\",\"ToStationName\":\"九江\",\"FromStationTypeName\":\"起点\",\"ToStationTypeName\":\"途经\",\"TrainNo\":\"Z65\",\"Train12306No\":\"2400000Z650K\",\"DurationMinutes\":654,\"DayDiff\":1,\"ControlDay\":29,\"SaleTime\":\"08:00\",\"ToTelcode\":\"JJG\",\"FromTelcode\":\"BXP\",\"CanWebBuy\":\"Y\",\"Seats\":[{\"SeatName\":\"硬座\",\"Price\":163.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧上\",\"Price\":280.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧中\",\"Price\":289.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧下\",\"Price\":299.5,\"TicketLeft\":0},{\"SeatName\":\"软卧上\",\"Price\":438.5,\"TicketLeft\":1},{\"SeatName\":\"软卧下\",\"Price\":456.5,\"TicketLeft\":1},{\"SeatName\":\"高级软卧上\",\"Price\":806.5,\"TicketLeft\":0},{\"SeatName\":\"高级软卧下\",\"Price\":840.5,\"TicketLeft\":0},{\"SeatName\":\"无座\",\"Price\":163.5,\"TicketLeft\":99}],\"IsSupportCard\":\"0\"},{\"StartTime\":\"19:59\",\"ArriveTime\":\"06:52\",\"FromStationName\":\"北京西\",\"ToStationName\":\"九江\",\"FromStationTypeName\":\"起点\",\"ToStationTypeName\":\"途经\",\"TrainNo\":\"Z67\",\"Train12306No\":\"2400000Z670M\",\"DurationMinutes\":653,\"DayDiff\":1,\"ControlDay\":29,\"SaleTime\":\"08:00\",\"ToTelcode\":\"JJG\",\"FromTelcode\":\"BXP\",\"CanWebBuy\":\"Y\",\"Seats\":[{\"SeatName\":\"硬座\",\"Price\":163.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧上\",\"Price\":280.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧中\",\"Price\":289.5,\"TicketLeft\":0},{\"SeatName\":\"硬卧下\",\"Price\":299.5,\"TicketLeft\":0},{\"SeatName\":\"软卧上\",\"Price\":438.5,\"TicketLeft\":0},{\"SeatName\":\"软卧下\",\"Price\":456.5,\"TicketLeft\":0},{\"SeatName\":\"高级软卧上\",\"Price\":806.5,\"TicketLeft\":0},{\"SeatName\":\"高级软卧下\",\"Price\":840.5,\"TicketLeft\":0},{\"SeatName\":\"无座\",\"Price\":163.5,\"TicketLeft\":99}],\"IsSupportCard\":\"0\"},{\"StartTime\":\"22:16\",\"ArriveTime\":\"11:24\",\"FromStationName\":\"北京\",\"ToStationName\":\"九江\",\"FromStationTypeName\":\"起点\",\"ToStationTypeName\":\"途经\",\"TrainNo\":\"T91\",\"Train12306No\":\"2400000T910A\",\"DurationMinutes\":788,\"DayDiff\":1,\"ControlDay\":29,\"SaleTime\":\"10:00\",\"ToTelcode\":\"JJG\",\"FromTelcode\":\"BJP\",\"CanWebBuy\":\"Y\",\"Seats\":[{\"SeatName\":\"软座\",\"Price\":258.5,\"TicketLeft\":99},{\"SeatName\":\"软卧上\",\"Price\":438.5,\"TicketLeft\":99},{\"SeatName\":\"软卧下\",\"Price\":456.5,\"TicketLeft\":99}],\"IsSupportCard\":\"0\"},{\"StartTime\":\"23:16\",\"ArriveTime\":\"14:22\",\"FromStationName\":\"北京西\",\"ToStationName\":\"九江\",\"FromStationTypeName\":\"起点\",\"ToStationTypeName\":\"途经\",\"TrainNo\":\"K105\",\"Train12306No\":\"240000K1051L\",\"DurationMinutes\":906,\"DayDiff\":1,\"ControlDay\":29,\"SaleTime\":\"08:00\",\"ToTelcode\":\"JJG\",\"FromTelcode\":\"BXP\",\"CanWebBuy\":\"Y\",\"Seats\":[{\"SeatName\":\"硬座\",\"Price\":163.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧上\",\"Price\":280.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧中\",\"Price\":289.5,\"TicketLeft\":99},{\"SeatName\":\"硬卧下\",\"Price\":299.5,\"TicketLeft\":99},{\"SeatName\":\"软卧上\",\"Price\":438.5,\"TicketLeft\":4},{\"SeatName\":\"软卧下\",\"Price\":456.5,\"TicketLeft\":4},{\"SeatName\":\"无座\",\"Price\":163.5,\"TicketLeft\":99}],\"IsSupportCard\":\"0\"}],\"TicketLeftTime\":\"\\/Date(1516009377391+0800)\\/\",\"IsRealTicket\":true,\"TimeStamp\":\"2018-01-15 17:42:57\"}";

		JSONObject resultObj = JSON.parseObject(jsonStr);
		String Ack = resultObj.getJSONObject("ResponseStatus").getString("Ack");
		String TicketLeftTime = resultObj.getString("TicketLeftTime");
		boolean IsRealTicket = resultObj.getBoolean("IsRealTicket");
		String TimeStamp = resultObj.getString("TimeStamp");
		String Message = resultObj.getString("Message");
		JSONArray Trains = resultObj.getJSONArray("Trains");

		CtripResultData crd = new CtripResultData();
		crd.setIsRealTicket(IsRealTicket);
		crd.setMessage(Message);
		crd.setResponseStatus(Ack);
		crd.setTimeStamp(TimeStamp);
		crd.setTicketLeftTime(TicketLeftTime);

		if (Trains.size() == 0) {

		}

		List<CtripZhanZhanData> zzlist = new ArrayList<CtripZhanZhanData>();
		for (int i = 0; i < Trains.size(); i++) {

			JSONObject train = Trains.getJSONObject(i); // 车次
			CtripZhanZhanData czzd = new CtripZhanZhanData();
			czzd.setStartTime(train.getString("StartTime"));
			czzd.setArriveTime(train.getString("ArriveTime"));
			czzd.setFromStationName(train.getString("FromStationName"));
			czzd.setToStationName(train.getString("ToStationName"));
			czzd.setFromStationTypeName(train.getString("FromStationTypeName"));
			czzd.setToStationTypeName(train.getString("ToStationTypeName"));
			czzd.setTrainNo(train.getString("TrainNo"));
			czzd.setTrain12306No(train.getString("Train12306No"));
			czzd.setDurationMinutes(train.getInteger("DurationMinutes"));
			czzd.setDayDiff(train.getInteger("DayDiff"));
			czzd.setControlDay(train.getInteger("ControlDay"));
			czzd.setSaleTime(train.getString("SaleTime"));
			czzd.setToTelcode(train.getString("ToTelcode"));
			czzd.setFromTelcode(train.getString("FromTelcode"));
			czzd.setCanWebBuy(train.getString("CanWebBuy"));
			czzd.setIsSupportCard(train.getString("IsSupportCard"));

			JSONArray seatArr = train.getJSONArray("Seats");// 车次的坐席信息
			List<CtripSeatData> seatlist = new ArrayList<CtripSeatData>();
			for (int j = 0; j < seatArr.size(); j++) {
				CtripSeatData csd = new CtripSeatData();
				JSONObject seat = seatArr.getJSONObject(j);
				csd.setSeatName(seat.getString("SeatName"));
				csd.setPrice(seat.getDoubleValue("Price"));
				csd.setTicketLeft(seat.getIntValue("TicketLeft"));
				seatlist.add(csd);
			}

			czzd.setSeats(seatlist);

			zzlist.add(czzd);
		}
		crd.setTrains(zzlist);
		//********************************************************//
		List<CtripZhanZhanData> zzlistNew = crd.getTrains();
		List<TrainNewData> dataList = new ArrayList<TrainNewData>();
		for (CtripZhanZhanData czzd : zzlistNew) {

			TrainNewData trainNewdata = new TrainNewData();
			trainNewdata.setStation_train_code(czzd.getTrainNo());
			trainNewdata.setTrain_no(czzd.getTrain12306No());
			trainNewdata.setFrom_station_name(czzd.getFromStationName());
			trainNewdata.setTo_station_name(czzd.getToStationName());
			trainNewdata.setFrom_station_telecode(czzd.getFromTelcode());
			trainNewdata.setTo_station_telecode(czzd.getToTelcode());
			trainNewdata.setStart_time(czzd.getStartTime());
			trainNewdata.setArrive_time(czzd.getArriveTime());
			trainNewdata.setIs_support_card(czzd.getIsSupportCard());
			trainNewdata.setCanWebBuy(czzd.getCanWebBuy());
			trainNewdata.setControl_day(String.valueOf(czzd.getControlDay()));
			trainNewdata.setDay_difference(String.valueOf(czzd.getDayDiff()));
			trainNewdata.setSale_time(czzd.getSaleTime());
			trainNewdata.setLishiValue(String.valueOf(czzd.getDurationMinutes()));
			String lishi = "";
			try {
				int lishiValue = Integer.valueOf(czzd.getDurationMinutes());
				int hour=lishiValue / 60;
				int min=lishiValue % 60;
				lishi=(hour<10?("0"+hour):hour)+":"+(min<10?("0"+min):min);
			} catch (Exception e) {
				logger.info(e);
			}
			trainNewdata.setLishi(String.valueOf(lishi));
			trainNewdata.initPrice();
			trainNewdata.initYupiao();

			List<CtripSeatData> seatlist = czzd.getSeats();// 车次坐席

			for (CtripSeatData csd : seatlist) {
				double seat_price = csd.getPrice();
				String seat_name = csd.getSeatName();
				int seat_yupiao = csd.getTicketLeft();

				if ("一等座".equals(seat_name)) {
					trainNewdata.setZy(Double.toString(seat_price));
					trainNewdata.setZy_num(String.valueOf(seat_yupiao));
				} else if ("二等座".equals(seat_name)) {
					trainNewdata.setZe(Double.toString(seat_price));
					trainNewdata.setZe_num(String.valueOf(seat_yupiao));
				} else if ("商务座".equals(seat_name)) {
					trainNewdata.setSwz(Double.toString(seat_price));
					trainNewdata.setSwz_num(String.valueOf(seat_yupiao));
				} else if ("特等座".equals(seat_name)) {
					trainNewdata.setTdz(Double.toString(seat_price));
					trainNewdata.setTdz_num(String.valueOf(seat_yupiao));
					trainNewdata.setTz_num(String.valueOf(seat_yupiao));
				} else if ("高级软卧下".equals(seat_name)) {
					trainNewdata.setGr_num(String.valueOf(seat_yupiao));
					trainNewdata.setGg_num(String.valueOf(seat_yupiao));
					trainNewdata.setGwx(Double.toString(seat_price));
				}else if ("高级软卧上".equals(seat_name)) {
					trainNewdata.setGr_num(String.valueOf(seat_yupiao));
					trainNewdata.setGg_num(String.valueOf(seat_yupiao));
					trainNewdata.setGws(Double.toString(seat_price));
				} else if ("软卧下".equals(seat_name)) {
					trainNewdata.setRw_num(String.valueOf(seat_yupiao));
					trainNewdata.setRwx(Double.toString(seat_price));
				} else if ("软卧上".equals(seat_name)) {
					trainNewdata.setRw_num(String.valueOf(seat_yupiao));
					trainNewdata.setRws(Double.toString(seat_price));
				} else if ("动卧下".equals(seat_name)) {
					trainNewdata.setDw_num(String.valueOf(seat_yupiao));
					trainNewdata.setDwx(Double.toString(seat_price));
				} else if ("动卧上".equals(seat_name)) {
					trainNewdata.setDw_num(String.valueOf(seat_yupiao));
					trainNewdata.setDws(Double.toString(seat_price));
				} else if ("硬卧下".equals(seat_name)) {
					trainNewdata.setYwx(Double.toString(seat_price));
					trainNewdata.setYw_num(String.valueOf(seat_yupiao));
				}else if ("硬卧中".equals(seat_name)) {
					trainNewdata.setYwz(Double.toString(seat_price));
					trainNewdata.setYw_num(String.valueOf(seat_yupiao));
				}else if ("硬卧上".equals(seat_name)) {
					trainNewdata.setYws(Double.toString(seat_price));
					trainNewdata.setYw_num(String.valueOf(seat_yupiao));
				} else if ("软座".equals(seat_name)) {
					trainNewdata.setRz(Double.toString(seat_price));
					trainNewdata.setRz_num(String.valueOf(seat_yupiao));
				} else if ("硬座".equals(seat_name)) {
					trainNewdata.setYz(Double.toString(seat_price));
					trainNewdata.setYz_num(String.valueOf(seat_yupiao));
				}else if ("无座".equals(seat_name)) {
					trainNewdata.setWz_num(String.valueOf(seat_yupiao));
				//	logger.info("【"+seat_name+"】"+",不是坐席类型,[二等座,硬座]有无座");
				} else {
					logger.info(seat_name + "该坐席没有匹配到");
				}		
			}
			// 票价和余票数设置完成
			dataList.add(trainNewdata);	
		}
		//**********************************************************//
		if (dataList.size() == 0) {
			//return TrainConsts.NO_DATAS; // 没有查到车次
		}

		OuterSoukdNewData oskd = new OuterSoukdNewData();
		oskd.setErrInfo("");
		oskd.setSdate("");
		oskd.setStype("");
		oskd.setCode("000");
		oskd.setDatajson(dataList);
		String returnStr = JSON.toJSONString(oskd);
	
		System.out.println(returnStr);
		
	}

}
