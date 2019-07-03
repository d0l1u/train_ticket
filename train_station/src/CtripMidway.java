
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.l9e.transaction.dao.CtripDao;
import com.l9e.transaction.vo.TrainCtrip;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpRequest;
/**
 * 查询携程的途经站信息
 * t_sinfo表
 * @author guona
 *
 */
public class CtripMidway {
	private Logger logger = Logger.getLogger(CtripMidway.class);
	public static String QUERY_MIDWAY_RUL = "http://trains.ctrip.com/TrainBooking/Ajax/SearchListHandler.ashx";

	private CtripDao ctripDao = new CtripDao();
	
	public static void main(String[] args) {
		//System.getProperties().setProperty("http.proxyHost", "192.168.65.126");
		//System.getProperties().setProperty("http.proxyPort", "3128");
		
		new CtripMidway().updateMidwayOfLccc();
	}
	
	public void updateMidwayOfLccc() {
		String logPre = "[ctrip途经站]";
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//2016-01-07
		String query_date = DateUtil.dateAddDays(today, "15");
		//查询需要更新途经站的所有车次信息
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("midway_status", "00");//途经站状态：00未查询途经站 11开始查询 22查询成功 33 查询失败 44查询返回空
		List<TrainCtrip> tarinList = ctripDao.queryTrainList(paramMap);
		logger.info(logPre+"查询需要更新途经站的所有车次总数:"+tarinList.size());
		
		if(tarinList.size()>0){
			TrainCtrip updateTrain = null;
			for(TrainCtrip train : tarinList){
				updateTrain = new TrainCtrip();
				updateTrain.setMidway_status("11");//11开始查询
				updateTrain.setTrain_number(train.getTrain_number());
				ctripDao.updateCtrip(updateTrain);
				logger.info(logPre+train.getTrain_number()+"更新midway_status为11开始查询");
				

				try {
					//向ctrip发起请求，查询车次信息
					//{"trainNum":"K2665","departure":"临汾","arrive":"苏州","date":"2016-06-13"}
					JSONObject json = new JSONObject();
					json.put("trainNum", train.getTrain_number());
					json.put("departure", URLEncoder.encode(train.getStart_station_name(), "utf-8"));
					json.put("arrive", URLEncoder.encode(train.getEnd_station_name(), "utf-8"));
					json.put("date", train.getDeparture_date());
//					Map<String, String> param = new HashMap<String, String>();
//					param.put("Action", "getIntermediateStopList");
//					param.put("value", URLEncoder.encode(json.toString(), "utf-8"));
//					logger.info(logPre+"paramMap:"+param);
//					
//					String result = HttpClientUtils.sendPostRequest(QUERY_MIDWAY_RUL, param, "utf-8");
					
//					String sendParams=UrlFormatUtil.CreateUrl("", param, "utf-8");
//					logger.info(logPre+"sendParams---------------------------"+sendParams);
//					String result = HttpUtil.sendByPost(QUERY_MIDWAY_RUL, sendParams, "utf-8");
					
					logger.info(logPre+"请求json：" + json.toString());
					/**
					 * encodeURI方法是正确的，只是需要使用两次encodeURI方法，例如encodeURI(encodeURI("中文"));
					 * 第一次是把中文编码成%xy的格式，第二次是对%xy中的%进行编码,%编码成%25。
					 * 整个传参过程大体应该是：
					 * 提交页面使用encodeURI(encodeURI("中文"))编码,把最后的编码结果%25xy传递给处理页面的过程中，
					 * 浏览器获取URL地址（注意openModelDialog方法，浏览器获取不到参数编码）后解码成%xy，
					 * 然后把%xy传递给处理页面,处理页面使用URLDecoder.decode(request.getParameter("参数名"),"UTF-8");完成解码
					 */
					String result = HttpRequest.sendGet(QUERY_MIDWAY_RUL, "Action=getIntermediateStopList&value="+URLEncoder.encode(json.toString(), "utf-8"),30000,null);
					logger.info(logPre+"请求通知接口返回：" + result);
					
					
					//解析结果-----html
					if(StringUtils.isNotEmpty(result) && result!=null && !"".equals(result)){
						result = result.replaceAll("\n", "").replaceAll("<em class='control'></em>", "");
						result = result.substring(1, result.length()-1);
						logger.info(logPre+"--------"+result);
						StringBuilder sbuilder = new StringBuilder();
						sbuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><body>")
								.append(result).append("</body>");
						
						Document doc = DocumentHelper.parseText(sbuilder.toString()); // 将字符串转为XML
						Element root = doc.getRootElement();
						//System.out.println("根节点：" + root.getName()); // 拿到根节点的名称
						
						Map<String, String> map = null;
			            Iterator dlRoot = root.elementIterator("dl"); // 获取根节点下的子节点head
			            // 遍历dl节点
			            while (dlRoot.hasNext()) {
			            	map = new HashMap<String, String>();
			            	map.put("checi", train.getTrain_number());
			            	map.put("czcc", train.getTrain_number());
			            	
			                Element recordEle = (Element) dlRoot.next();
			                
			                Iterator dtRoot = recordEle.elementIterator("dt"); // 获取子节点dt下的子节点script
			                // 遍历Header节点下的Response节点
			                while (dtRoot.hasNext()) {
			                    Element itemEle = (Element) dtRoot.next();
			                    String xuhao = itemEle.elementTextTrim("i"); // 拿到head下的子节点script下的字节点username的值
			                    map.put("stationno", xuhao);
			                }
			                
			                Iterator ddRoot = recordEle.elementIterator("dd"); // 获取子节点head下的子节点script
			                // 遍历Header节点下的Response节点
			                int num = 1;
			                while (ddRoot.hasNext()) {
			                    Element itemEle = (Element) ddRoot.next();
			                    String nr = itemEle.getTextTrim(); // 拿到head下的子节点script下的字节点username的值
			                    if(num==1){
			                    	map.put("name", nr);
			                    }else if(num==2){
			                    	map.put("arrtime", nr);
			                    }else if(num==3){
			                    	map.put("starttime", nr);
			                    }else if(num==4){
			                    	map.put("stopovertime", nr);
			                    }
			                    num++;
			                }
			                
			                //logger.info(logPre+"map:"+map);
			                int count = ctripDao.queryMidWayIsExist(map);
		            		if(count>0){
		            			int flag = ctripDao.updateMidWay(map);
		            			logger.info(logPre+"更新"+flag+"条数据"+map);
		            		}else{
		            			ctripDao.addMidWay(map);
		            			logger.info(logPre+"新增1条数据"+map);
		            		}
			            }
			            updateTrain = new TrainCtrip();
						updateTrain.setMidway_status("22");//途经站状态：00未查询途经站 11开始查询 22查询成功 33 查询失败 44查询返回空
						updateTrain.setTrain_number(train.getTrain_number());
						ctripDao.updateCtrip(updateTrain);
						logger.info(logPre+train.getTrain_number()+"更新midway_status为22查询成功");
					}else{
						updateTrain = new TrainCtrip();
						updateTrain.setMidway_status("44");//途经站状态：00未查询途经站 11开始查询 22查询成功 33 查询失败 44查询返回空
						updateTrain.setTrain_number(train.getTrain_number());
						ctripDao.updateCtrip(updateTrain);
						logger.info(logPre+train.getTrain_number()+"更新midway_status为44查询返回空");
					}
						
				} catch (Exception e) {
					e.printStackTrace();
					updateTrain = new TrainCtrip();
					updateTrain.setMidway_status("33");//途经站状态：00未查询途经站 11开始查询 22查询成功 33 查询失败 44查询返回空
					updateTrain.setTrain_number(train.getTrain_number());
					ctripDao.updateCtrip(updateTrain);
					logger.info(logPre+train.getTrain_number()+"更新midway_status为33 查询失败");
					
					logger.info(logPre+"exception, e="+e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
}
