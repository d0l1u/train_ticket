package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.DataMaintainService;
import com.l9e.transaction.vo.DataMaintainVo;
import com.l9e.transaction.vo.DataTrainMaintainVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.util.DateUtil;

@Controller
@RequestMapping("/data")
public class DataController extends BaseController implements Runnable{
	private Logger logger=Logger.getLogger(this.getClass());     
	@Resource
	private DataMaintainService dataMaintain;
	
	@RequestMapping("/dataMaintain.do")
	public String dataMaintain(){
		return "data/dataMaintain";
	}
	@RequestMapping("/checkThePwd.do")
	public void checkThePwd(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String pwd = request.getParameter("pwd");
		String real_pwd = "hcp_yanfa_"+DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(pwd.equals(real_pwd)){
			logger.info(loginUserVo.getReal_name()+"输入数据维护操作成功！");
			out.write("true");
		}else{
			logger.info(loginUserVo.getReal_name()+"输入数据维护操作失败！");
			out.write("false");
		}
	}
	//线下整合票价表（价格）
	@RequestMapping("/updateNewDataPrice.do")
	public String updateNewDataPrice(){
		boolean result = false;
		List<DataMaintainVo> dataMainList = null;
		DataMaintainVo dataMainOld = null;
		int index = 250000;
		int updateIndex = 0;
		int insertIndex = 0;
		while(true){
			Map<String, Integer> paramMap = new HashMap<String, Integer>();
			paramMap.put("before", index);
			paramMap.put("after", 5000);
			try {
				//新表批量查询出5000数据
				dataMainList = dataMaintain.getNewDataMaintain(paramMap);
				if(dataMainList.size()==0){
					System.out.println("票价表价格整合结束！");
					appendNewDataPrice();
					break;
				}
				for (DataMaintainVo dataMainNew : dataMainList){
					if(dataMainNew!=null){
						Map<String, String> paramData = new HashMap<String, String>();
						paramData.put("cc", dataMainNew.getCc());
						paramData.put("fz", dataMainNew.getFz());
						paramData.put("dz", dataMainNew.getDz());
						//旧表中匹配该车次数据
						dataMainOld = dataMaintain.getOldDataMaintain(paramData);
						
						if(dataMainOld!=null){
							if(dataMainNew.getYz()>dataMainOld.getYz()){
								dataMainOld.setYz(dataMainNew.getYz());
								result = true;
							}
							if(dataMainNew.getRz()>dataMainOld.getRz()){
								dataMainOld.setRz(dataMainNew.getRz());
								result = true;
							}
							if(dataMainNew.getYws()>dataMainOld.getYws()){
								dataMainOld.setYws(dataMainNew.getYws());
								result = true;
							}
							if(dataMainNew.getYwz()>dataMainOld.getYwz()){
								dataMainOld.setYwz(dataMainNew.getYwz());
								result = true;
							}
							if(dataMainNew.getYwx()>dataMainOld.getYwx()){
								dataMainOld.setYwx(dataMainNew.getYwx());
								result = true;
							}
							if(dataMainNew.getRws()>dataMainOld.getRws()){
								dataMainOld.setRws(dataMainNew.getRws());
								result = true;
							}
							if(dataMainNew.getRwx()>dataMainOld.getRwx()){
								dataMainOld.setRwx(dataMainNew.getRwx());
								result = true;
							}
							if(dataMainNew.getRz1()>dataMainOld.getRz1()){
								dataMainOld.setRz1(dataMainNew.getRz1());
								result = true;
							}
							if(dataMainNew.getRz2()>dataMainOld.getRz2()){
								dataMainOld.setRz2(dataMainNew.getRz2());
								result = true;
							}
						}else{
//							dataMaintain.insertDataMaintain(dataMainNew);
							dataMainNew.setType("insert");
							dataMaintain.insertDataMaintainTemp(dataMainNew);
							insertIndex++;
						}
						if(result){
							dataMainNew.setType("update");
							dataMaintain.insertDataMaintainUpdateTemp(dataMainNew);
//							dataMaintain.updateDataMaintain(dataMainOld);
							updateIndex++;
							result = false;
						}
					}
//					Thread.sleep(1000);
				}
				index += 5000;
				dataMainList = null;
				dataMainOld = null;
				System.out.println("-------------------休息一分钟！-------------------");
				logger.info("-----票价已经扫描了"+index+"条数据！-----");
				logger.info("-----需要插入"+insertIndex+"条新数据！-----");
				logger.info("-----需要更新"+updateIndex+"条数据！-----");
				Thread.sleep(30*1000);
			} catch (Exception e) {
				logger.error("捕获需要整合新票价表数据异常！",e);
			}
		}
		return null;
	}
	//线下整合票价表（站名、时间）
	@RequestMapping("/appendNewDataPrice.do")
	public String appendNewDataPrice(){
		List<DataTrainMaintainVo> dataMainList = null;
		int index = 0;
		int updateIndex = 0;
		while(true){
			Map<String, Integer> paramMap = new HashMap<String, Integer>();
			paramMap.put("before", index);
			paramMap.put("after", 5000);
			Map<String, String> paramData = null;
			Map<String,String> resultData = null;
			try {
				//从整合完成的旧票价表中批量查出5000条数据进行新增字段数据整合
				dataMainList = dataMaintain.getDataTrainMaintain(paramMap);
				if(dataMainList.size()==0){
					System.out.println("车次表信息整合到票价表结束！");
					break;
				}
				boolean wea_add = false;
				for (DataTrainMaintainVo dataTrainMain : dataMainList){
					String checi = dataTrainMain.getCc();
					if(dataTrainMain!=null){
						paramData = new HashMap<String, String>();
						paramData.put("cc", checi);
						paramData.put("fz", dataTrainMain.getFz());
						paramData.put("dz", dataTrainMain.getDz());
					}
					//需要更新数据
					resultData = new HashMap<String, String>();
					resultData = dataMaintain.queryCheciStartEndStationName(checi);
					if(resultData!=null){
						if(resultData.get("start_station_name")!=null && !resultData.get("start_station_name").equals(dataTrainMain.getStart_station_name())){
							wea_add = true;
							dataTrainMain.setStart_station_name(resultData.get("start_station_name"));
						}
						if(resultData.get("end_station_name")!=null && !resultData.get("end_station_name").equals(dataTrainMain.getEnd_station_name())){
							wea_add = true;
							dataTrainMain.setEnd_station_name(resultData.get("end_station_name"));
						}
					}
					
					resultData = dataMaintain.queryCheciStartEndStationTime(paramData);
					if(resultData!=null){
						String start_time = resultData.get("start_time");
						String arrive_time = resultData.get("arrive_time");
						String czcc = resultData.get("czcc");
						if(start_time !=null && !start_time.equals(dataTrainMain.getStart_time())){
							wea_add = true;
							dataTrainMain.setStart_time(start_time);
						}
						if(arrive_time !=null && !arrive_time.equals(dataTrainMain.getArrive_time())){
							wea_add = true;
							dataTrainMain.setArrive_time(arrive_time);
						}
						if(czcc !=null && !czcc.equals(dataTrainMain.getCzcc())){
							wea_add = true;
							dataTrainMain.setCzcc(czcc);
						}
						String lishi = String.valueOf(diffMinute(start_time,arrive_time,resultData.get("startcost"),resultData.get("arrivecost")));
						if(!lishi.equals(dataTrainMain.getLishi())){
							wea_add = true;
							dataTrainMain.setLishi(lishi);
						}
					}
					if(wea_add){
						dataMaintain.addDataTrainMaintain(dataTrainMain);
						updateIndex++;
						wea_add = false;
					}
				}
				index += 5000;
				dataMainList = null;
				System.out.println("-------------------休息10秒！-------------------");
				logger.info("-----票价表已经扫描了"+index+"条数据！-----");
				logger.info("-----票价表需要更新"+updateIndex+"条数据！-----");
				Thread.sleep(10*1000);
			} catch (Exception e) {
				logger.error("车次表信息整合到票价表异常!",e);
			}
		}
		return null;
	}
	
	//整合线上票价表价格
	@RequestMapping("/conformOfficialPriceTable.do")
	public String conformOfficialPriceTable(HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		logger.info("登录人："+loginUserVo.getReal_name()+"，执行整合线上票价表操作！");
		List<DataMaintainVo> dataMainList = null;
		int index = 0;
		int insertIndex = 0;
		int updateIndex = 0;
		try{
			while(true){
				Map<String, Integer> paramMap = new HashMap<String, Integer>();
				paramMap.put("before", index);
				paramMap.put("after", 5000);
				//批量查询100条需要插入到线上的数据
				dataMainList = dataMaintain.findInsertOfficialPriceData(paramMap);
				if(dataMainList.size()==0){
					dataMainList = null;
					logger.info("插入数据结束！");
					break;
				}
				for(DataMaintainVo data:dataMainList){
					dataMaintain.tempDataInsertNewPrice(data);
					insertIndex++;
				}
				index += 5000;
				dataMainList = null;
				System.out.println("-------------------休息10秒！-------------------");
				logger.info("-----补丁表临时表已经扫描了"+index+"条需要插入的数据！-----");
				logger.info("-----票价表已经插入了"+insertIndex+"条新数据！-----");
				Thread.sleep(10*1000);
			}
			index = 0;
			while(true){
				Map<String, Integer> paramMap = new HashMap<String, Integer>();
				paramMap.put("before", index);
				paramMap.put("after", 5000);
				//批量查询100条需要更新到线上的数据
				dataMainList = dataMaintain.findUpdateOfficialPriceData(paramMap);
				if(dataMainList.size()==0){
					//删除临时表中数据
					dataMaintain.deleteOfficialPriceDataTemp();
					logger.info("更新数据结束！");
					break;
				}
				for(DataMaintainVo data:dataMainList){
					dataMaintain.tempDataUpdateNewPrice(data);
					updateIndex++;
				}
				index += 5000;
				dataMainList = null;
				System.out.println("-------------------休息10秒！-------------------");
				logger.info("-----补丁表临时表已经扫描了"+index+"条需要更新的数据！-----");
				logger.info("-----票价表需要更新"+updateIndex+"条数据！-----");
				Thread.sleep(10*1000);
			}
		}catch(Exception e){
			logger.error("整合线上票价表数据异常！",e);
		}
		return null;
	}

	//整合线上票价表站名、时间
	@RequestMapping("/conformOfficialNameTimeTable.do")
	public String conformOfficialNameTimeTable(HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		logger.info("登录人："+loginUserVo.getReal_name()+"，执行整合线上票价表操作！");
		List<DataTrainMaintainVo> dataMainList = null;
		int index = 0;
		int updateIndex = 0;
		try{
			while(true){
				Map<String, Integer> paramMap = new HashMap<String, Integer>();
				paramMap.put("before", index);
				paramMap.put("after", 5000);
				//批量查询100条需要更新到线上的数据
				dataMainList = dataMaintain.findUpdateOfficialPriceNameTimeData(paramMap);
				if(dataMainList.size()==0){
					//删除临时表中数据
					dataMaintain.deleteOfficialPriceNameTimeDataTemp();
					logger.info("更新数据结束！");
					break;
				}
				for(DataTrainMaintainVo data:dataMainList){
					dataMaintain.tempDataUpdateNewPriceNameTime(data);
					updateIndex++;
				}
				index += 5000;
				dataMainList = null;
				System.out.println("-------------------休息10秒！-------------------");
				logger.info("-----补丁表临时表已经扫描了"+index+"条需要更新的数据！-----");
				logger.info("-----票价表已经更新"+updateIndex+"条数据！-----");
				Thread.sleep(10*1000);
			}
		}catch(Exception e){
			logger.error("整合线上票价表（站名，时间）数据异常！",e);
		}
		return null;
	}
	
	public int diffMinute(String start_time,String arrive_time, String startcost, String arrivecost){
		int DAY = 24*60;
		int sta = getDiffDay(startcost);
		int arr = getDiffDay(arrivecost);
		int datResult = DateUtil.minuteDiffInt(arrive_time,start_time);
		if(arrive_time.compareTo(start_time)<0){
			return arr*DAY - sta*DAY - datResult;
		}else{
			return arr*DAY - sta*DAY + datResult;
		}
	}
	
	public int getDiffDay(String str){
		return "当日".equals(str) ? 1: "第2日".equals(str) ? 2:"第3日".equals(str) ? 3:4;
	}
	
	/**
	//线下整合票价表（站名、时间）
	@RequestMapping("/appendSinfoInPrice.do")
	public String appendSinfoInPrice(){
		List<DataMaintainVo> dataMainList = null;
		int index = 0;
		while(true){
			Map<String, Integer> paramMap = new HashMap<String, Integer>();
			paramMap.put("before", index);
			paramMap.put("after", 5000);
			Map<String, String> paramData = null;
			try {
				//从整合完成的旧票价表中批量查出5000条数据进行新增字段数据整合
				dataMainList = dataMaintain.queryOldDataMaintainList(paramMap);
				if(dataMainList.size()==0){
					System.out.println("车次表信息初次整合到票价表结束！");
					break;
				}
				boolean updateData = false;
				for (DataMaintainVo dataTrainMain : dataMainList){
					String checi = dataTrainMain.getCc();
					if(dataTrainMain!=null){
						paramData = new HashMap<String, String>();
						paramData.put("cc", checi);
						paramData.put("fz", dataTrainMain.getFz());
						paramData.put("dz", dataTrainMain.getDz());
					}
					//需要更新数据
					Map<String,String> resultDataName = new HashMap<String, String>();
					
					Map<String,String> resultDataTime = new HashMap<String, String>();
					
					resultDataName = dataMaintain.queryCheciStartEndStationName(checi);
					resultDataTime = dataMaintain.queryCheciStartEndStationTime(paramData);
					
					NewPriceAppendData data = new NewPriceAppendData();
					data.setCc(checi);
					data.setFz(dataTrainMain.getFz());
					data.setDz(dataTrainMain.getDz());
					if(resultDataName!=null){
						data.setStart_station_name(resultDataName.get("start_station_name"));
						data.setEnd_station_name(resultDataName.get("end_station_name"));
						updateData = true;
					}
					if(resultDataTime!=null){
						data.setArrive_time(resultDataTime.get("arrive_time"));
						data.setStart_time(resultDataTime.get("start_time"));
						data.setCzcc(resultDataTime.get("czcc"));
						int lishi = diffMinute(resultDataTime.get("start_time"),resultDataTime.get("arrive_time"),resultDataTime.get("startcost"),resultDataTime.get("arrivecost"));
						data.setLishi(String.valueOf(lishi));
						updateData = true;
					}
					if(updateData){
						dataMaintain.appendNewPriceDataMaintain(data);
						updateData = false;
					}
					
				}
				index += 5000;
				dataMainList = null;
				System.out.println("-------------------休息10秒！-------------------");
				logger.info("-----票价表已经扫描了"+index+"条数据！-----");
				Thread.sleep(10*1000);
			} catch (Exception e) {
				logger.error("车次表信息初次整合到票价表结束!",e);
			}
		}
		return null;
	}
	*/
	/**
	@RequestMapping("/updateTrainData.do")
	public String updateTrainData(){
		boolean result = false;
		List<TrainMaintainVo> trainMainList = null;
		TrainMaintainVo trainMainOld = null;
		int index = 0;
		int updateIndex = 0;
		int insertIndex = 0;
		while(true){
			Map<String, Integer> paramMap = new HashMap<String, Integer>();
			paramMap.put("before", index);
			paramMap.put("after", 5000);
			try {
				//新表依次查询出一条数据
				trainMainList = dataMaintain.queryNewDataMaintain(paramMap);
				if(trainMainList.size()==0){
					break;
				}
				for (TrainMaintainVo trainMainNew : trainMainList){
					if(trainMainNew!=null){
						Map<String, String> paramTrain = new HashMap<String, String>();
						paramTrain.put("cc", trainMainNew.getCc());
						paramTrain.put("fz", trainMainNew.getFz());
						paramTrain.put("dz", trainMainNew.getDz());
						//旧表中匹配该车次数据
						trainMainOld = dataMaintain.getOldTrainMaintain(paramTrain);
						
						if(trainMainOld!=null){
							if(trainMainNew.getYz()>trainMainOld.getYz()){
								trainMainOld.setYz(trainMainNew.getYz());
								result = true;
							}
							if(trainMainNew.getRz()>trainMainOld.getRz()){
								trainMainOld.setRz(trainMainNew.getRz());
								result = true;
							}
							if(trainMainNew.getYws()>trainMainOld.getYws()){
								trainMainOld.setYws(trainMainNew.getYws());
								result = true;
							}
							if(trainMainNew.getYwz()>trainMainOld.getYwz()){
								trainMainOld.setYwz(trainMainNew.getYwz());
								result = true;
							}
							if(trainMainNew.getYwx()>trainMainOld.getYwx()){
								trainMainOld.setYwx(trainMainNew.getYwx());
								result = true;
							}
							if(trainMainNew.getRws()>trainMainOld.getRws()){
								trainMainOld.setRws(trainMainNew.getRws());
								result = true;
							}
							if(trainMainNew.getRwx()>trainMainOld.getRwx()){
								trainMainOld.setRwx(trainMainNew.getRwx());
								result = true;
							}
							if(trainMainNew.getRz1()>trainMainOld.getRz1()){
								trainMainOld.setRz1(trainMainNew.getRz1());
								result = true;
							}
							if(trainMainNew.getRz2()>trainMainOld.getRz2()){
								trainMainOld.setRz2(trainMainNew.getRz2());
								result = true;
							}
						}else{
							dataMaintain.insertTrainMaintain(trainMainNew);
							insertIndex++;
						}
						if(result){
							dataMaintain.updateTrainMaintain(trainMainOld);
							updateIndex++;
							result = false;
						}
					}
//					Thread.sleep(1000);
				}
				index += 5000;
				trainMainList = null;
				trainMainOld = null;
				System.out.println("-------------------休息一分钟！-------------------");
				System.out.println("-----已经扫描了"+index+"条数据！-----");
				System.out.println("-----插入了"+insertIndex+"条新数据！-----");
				System.out.println("-----更新了"+updateIndex+"条数据！-----");
				Thread.sleep(10*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	*/
	@Override
	public void run() {
		
	}
}
