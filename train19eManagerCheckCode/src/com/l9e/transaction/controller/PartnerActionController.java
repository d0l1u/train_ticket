package com.l9e.transaction.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqz.code.JudgePicType;
import com.cqz.code.RuoKuaiUtil;
import com.cqz.code.YunsuUtil;
import com.cqz.tc.TcCodeUtil;
import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.mq.MqService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.thread.PartnerActionThread;
import com.l9e.transaction.vo.Picture;
import com.l9e.transaction.vo.ReturnError;
import com.l9e.transaction.vo.ReturnSuccess;
import com.l9e.transaction.vo.TcCodeReturn;
import com.l9e.transaction.vo.WeightCategory;
import com.l9e.util.Base64Util;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;


//与机器人交互
@Controller
@RequestMapping("/partner")
public class PartnerActionController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(PartnerActionController.class);
	@Resource
	RobotCodeService robotService;
	
	@Resource
	private MqService mqServic;
	
	@Value("#{propertiesReader[sys_root]}")
	private String sys_root;
	
	@Value("#{propertiesReader[pic_root]}")
	private String pic_root;
	
	//收码、存码、返回结果
	@RequestMapping("/sendCode.do")
	@ResponseBody
	public void sendCode(HttpServletRequest request,HttpServletResponse response){
		logger.info("【机器人上传图片】IP:"+request.getRemoteAddr()+", TIME:"+DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
		//接收上传的验证码图片、有效时间、获取图片的渠道（合作伙伴ID）等数据保存到数据库中，自动生成pic_ID并返回
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Picture picture = new Picture();
		//String picAddress = new ConfigUtil().getValue("pic.address");
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> list;
//		String time="";
		BufferedOutputStream bw = null;
		BufferedInputStream bis = null;
		String qunar_play_code = robotService.querySystemValue("qunar_play_code");
//		String tc_play_code = robotService.querySystemValue("tc_play_code");
//		String code_type_channel = robotService.querySystemValue("code_type_channel");
		
		//除了qunar打码以外，按照权重值来分配打码start~~~~
		String tongcheng_code_weight = robotService.querySystemValue("tongcheng_code_weight");//同程打码权重值
		String code_19e_weight = robotService.querySystemValue("19e_code_weight");//19e打码权重值
		String code_rk_weight = robotService.querySystemValue("code_book_rk_weight");//若快打码权重值
		String code_ys_weight = robotService.querySystemValue("code_book_ys_weight");//云速打码权重值
		//设置打码权重值
		List<WeightCategory> categorysChannel = new ArrayList<WeightCategory>();//放各个权重的集合
		WeightCategory wcchannel1 = new WeightCategory("tongcheng", Integer.parseInt(tongcheng_code_weight));//tongcheng打码
		WeightCategory wcchannel2 = new WeightCategory("19e", Integer.parseInt(code_19e_weight));//19e打码
		WeightCategory wcchannel3 = new WeightCategory("ruokuai", Integer.parseInt(code_rk_weight));//若快打码
		WeightCategory wcchannel4 = new WeightCategory("yunsu", Integer.parseInt(code_ys_weight));//yunsu打码
		categorysChannel.add(wcchannel1);
		categorysChannel.add(wcchannel2);
		categorysChannel.add(wcchannel3);
		categorysChannel.add(wcchannel4);
//		logger.info("-------------权重list--------"+categorys.size());
		int count = Integer.parseInt(tongcheng_code_weight)
					+ Integer.parseInt(code_19e_weight)
					+ Integer.parseInt(code_rk_weight)
					+ Integer.parseInt(code_ys_weight);
		Integer nchannel = TrainConsts.random.nextInt(count); // n in [0, weightSum)
		Integer mchannel = 0;
		for (WeightCategory wc : categorysChannel) {
			if (mchannel <= nchannel && nchannel < mchannel + wc.getWeight()) {
				picture.setChannel(wc.getCategory());
				logger.info("-------------权重channel--------"+wc.getCategory());
				break;
			}
			mchannel += wc.getWeight();
		}
		//除了qunar打码以外，按照权重值来分配打码end~~~~
		
		try {
			list = upload.parseRequest(request);
			for (FileItem item : list) {
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					logger.info("机器人上传--------------------------"+name+"++"+value);
					if ("order_id".equals(name)) {
						picture.setOrder_id(value);
						//TODO 上线需更换
						String channel = "19e";
//						String channel = robotService.queryOrderInfoChannel(value);
						if("qunar".equals(channel) && !"11".equals(qunar_play_code)){
							picture.setChannel(channel);
						}
						
					//}else if ("sign".equals(name)) {
					}else if ("span_time".equals(name)) {
						picture.setSpan_time(Picture.INIT_TIME_SPAN);//设置默认时间
						Date date=new Date();
						picture.setCreate_time(DateUtil.dateToString(date,DateUtil.DATE_FMT3));
					}
				} else {
					String filename = item.getName();
					if(filename==null||filename.equals("")){
						System.out.println("非法参数");
						ReturnError returnError=new ReturnError();
						returnError.setRet(ReturnError.FAIL);
						returnError.setErr_code(ReturnError.ERRCODE4);
						returnError.setErr_msg(ReturnError.ERRMSG4);
						writeN2Response(response,JSONObject.fromObject(returnError).toString());
						return;
					}
					String suffix = filename.substring(filename.lastIndexOf(".")).toUpperCase();//文件后缀名
					if(!suffix.equals(".PNG")&&!suffix.equals(".GIF")&&!suffix.equals(".BMP")&&!suffix.equals(".JPG")&&!suffix.equals(".JPEG")){
						//非法的图片格式
						ReturnError returnError=new ReturnError();
						returnError.setRet(ReturnError.FAIL);
						returnError.setErr_code(ReturnError.ERRCODE7);
						returnError.setErr_msg(ReturnError.ERRMSG7);
						writeN2Response(response,JSONObject.fromObject(returnError).toString());
						return;
					}
					String picId = CreateIDUtil.createID(CreateIDUtil.HEAD_PICTURE);
					picture.setPic_id(picId);
//					String filePath = pic_root; 
					String cal = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
					String cal_HH = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT5);
					String picFilename = "/upload/" +cal+"/"+cal_HH+"/"+picId + suffix;
					//图片上传的文件夹日期：/upload/2015-03-17
					File file_new = new File(pic_root+"/upload/"+cal);
					file_new.mkdir();
					//图片上传的文件夹小时：/upload/2015-03-17/2015-03-17-10
					File file = new File(pic_root+"/upload/"+cal+"/"+cal_HH);
					file.mkdir();
					picture.setPic_filename(picFilename);
//					logger.info(new Date().getTime()+" upload picture picFilename "+picFilename);
					// 获得流，读取数据写入文件
					if(item.getSize()>5000){
						bis = new BufferedInputStream(item.getInputStream());
						bw = new BufferedOutputStream(new FileOutputStream(pic_root+picFilename));
						byte[] buffer = new byte[1024];
						while(bis.read(buffer) != -1) {
							bw.write(buffer);
			            }
						logger.info("【机器人保存图片文件成功】pic_id:"+picture.getPic_id()+", TIME:"+DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3)+",IP:"+request.getRemoteAddr());
					}else{
						//图片传输失败
						logger.error("fileupload error");
						ReturnError returnError=new ReturnError();
						returnError.setRet(ReturnError.FAIL);
						returnError.setErr_code(ReturnError.ERRCODE13);
						returnError.setErr_msg(ReturnError.ERRMSG13);
						writeN2Response(response,JSONObject.fromObject(returnError).toString());
						return;
					}
				}
			}
			
			if("19e".equals(picture.getChannel())){//19e的验证码放入
				//系统设置--查看当前打码权重开关
				String code_weight_switch = robotService.querySystemValue("code_weight_switch");//打码权重开关：1开启 0关闭
				if("0".equals(code_weight_switch)){
					//picture.setDepartment(wc.getCategory());//5:打码团队1    00:打码团队2   4:打码团队3    99:打码团队其他
					robotService.savePicture(picture);
					//打码数据放入队列start
					mqServic.sendMqMsg(TrainConsts.ROBOT_CODENO, picture.getPic_id());
					logger.info("【关闭权重】upload picture from partner："+picture.getPic_filename()+" save to database！");
				}else{
					// 验证码根据权重分配打码部门---start
					//5:打码团队1    00:打码团队2   4:打码团队3    99:打码团队其他
					//1：客服部、2：运营部、3：研发部、4：其他部门、5：对外部门、00：对外部门02、6：同程部门、7：机票部门、8：代理商部门、9：艺龙部门
					//----------
					Map<String,String> sysInfo=robotService.querySysInfo();
//					logger.info("+=====+++"+sysInfo);
					List<WeightCategory> categorys = new ArrayList<WeightCategory>();//放各个权重的集合
					WeightCategory wc1 = new WeightCategory("5", Integer.parseInt(sysInfo.get("code01_weight")));//打码团队01      code01_weight
					WeightCategory wc2 = new WeightCategory("00", Integer.parseInt(sysInfo.get("code02_weight")));//打码团队02      code02_weight
					WeightCategory wc3 = new WeightCategory("4", Integer.parseInt(sysInfo.get("code03_weight")));//打码团队03      code03_weight
					WeightCategory wc4 = new WeightCategory("99", Integer.parseInt(sysInfo.get("code04_weight")));//打码团队--其他        code04_weight
					categorys.add(wc1);
					categorys.add(wc2);
					categorys.add(wc3);
					categorys.add(wc4);
					logger.info("-------------权重list--------"+categorys.size());
					/*	Integer weightSum = 0;
					for (WeightCategory wc : categorys) {
						weightSum += wc.getWeight();
					}
					//logger.info("-------"+weightSum);
					if (weightSum <= 0) {
						logger.info("[权重分配]Error: weightSum=" + weightSum.toString());
						writeN2Response(response,"false");
					}*/
					Integer n = TrainConsts.random.nextInt(100); // n in [0, weightSum)
					Integer m = 0;
					for (WeightCategory wc : categorys) {
						if (m <= n && n < m + wc.getWeight()) {
							picture.setDepartment(wc.getCategory());//5:打码团队1    00:打码团队2   4:打码团队3    99:打码团队其他
							robotService.savePicture(picture);
							//打码数据放入队列start
							logger.info("当前获取到的权重值："+wc.getCategory());
							if("5".equals(picture.getDepartment())){//5:打码团队1    00:打码团队2   4:打码团队3    99:打码团队其他
								mqServic.sendMqMsg(TrainConsts.ROBOT_CODE, picture.getPic_id());
							}else if("00".equals(picture.getDepartment())){
								mqServic.sendMqMsg(TrainConsts.ROBOT_CODE02, picture.getPic_id());
							}else if("4".equals(picture.getDepartment())){
								mqServic.sendMqMsg(TrainConsts.ROBOT_CODE03, picture.getPic_id());
							}else{
								mqServic.sendMqMsg(TrainConsts.ROBOT_CODE04, picture.getPic_id());
							}
//							logger.info(picture.getPic_id() + " This Random Category is " + wc.getCategory());
							logger.info("upload picture from partner："+picture.getPic_filename()+" save to database！");
							break;
						}
						m += wc.getWeight();
					}
					// 验证码根据权重分配打码部门---end
				}
			}else{
				picture.setDepartment(picture.getChannel());
				robotService.savePicture(picture);
				logger.info("["+picture.getChannel()+"]upload picture from partner："+picture.getPic_filename()+" save to database！");
			}
		}catch (Exception e) {
			e.printStackTrace();
			//图片上传异常
			logger.error("fileupload error" + e.getMessage());
			ReturnError returnError=new ReturnError();
			returnError.setRet(ReturnError.FAIL);
			returnError.setErr_code(ReturnError.ERRCODE2);
			returnError.setErr_msg(ReturnError.ERRMSG2);
			writeN2Response(response,JSONObject.fromObject(returnError).toString());
			return;
		}finally{
			try{
				if(bw!=null){
					bw.flush();
					bw.close();
				}
				if(bis!=null){
					bis.close();
				}
			}catch(Exception ef){
				logger.error("清理图片缓存失败！",ef);
			}
		}
		
		//验证成功  发送成功信息
		ReturnSuccess returnSuccess=new ReturnSuccess();
		returnSuccess.setPic_id(picture.getPic_id());
		returnSuccess.setRet(ReturnSuccess.SUCCESS);
		//凑够256个字节flush给机器人
		String returnJson = JSONObject.fromObject(returnSuccess).toString();
		logger.info("returnJson"+returnJson);
		writeN2Response(response,returnJson);
		
		//解决app上传验证码为空的问题
		PartnerActionThread partnerActionThread = new PartnerActionThread(picture,pic_root,robotService);
		Thread thread = new Thread(partnerActionThread); 
		thread.start();
		
//		if("tongcheng".equals(picture.getChannel())){//同程打码
//			logger.info("[tongcheng]getTcCode："+picture.getPic_id()+" success！");
//			getTcCode(picture);
//		}else if("ruokuai".equals(picture.getChannel())){//若快打码
//			getRkCode(picture);
//		}else if("yunsu".equals(picture.getChannel())){//云速打码
//			getYsCode(picture);
//		}
	}
	
	
	//请求打码结果
	@RequestMapping("/requestResult.do")
	@ResponseBody
	public void requestResult(HttpServletRequest request,HttpServletResponse response){
		Pattern pattern = Pattern.compile("^[0-9]*");
	
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	String pic_id = request.getParameter("pic_id");
		if(pic_id==null){
			//logger.info("----------pic_id"+pic_id);
			writeN2Response(response,null);
		}
		Picture picture = new Picture();
//    
    	logger.info("机器人请求图片结果："+pic_id);
    	
    	//查询系统设置的验证码超时时间  add by wangsf
		int codeTimeOut=0;
		String codeTimeOutStr=robotService.queryVerifiCodeTimeOut("verifiCode_timeOut");
		logger.info("返回验证码超时的时间为："+codeTimeOutStr);
		codeTimeOut=Integer.parseInt(codeTimeOutStr); //返回验证码的超时时间
		
    	ReturnSuccess returnSuccess=new ReturnSuccess();
		try{
			picture=robotService.getPicture(pic_id);
			returnSuccess.setPic_id(pic_id);
			returnSuccess.setRet(ReturnSuccess.SUCCESS);
			
			String verify_code=picture.getVerify_code()==null?"":picture.getVerify_code().trim();
			String verify_code_coord=picture.getVerify_code_coord()==null?"":picture.getVerify_code_coord().trim();
			if("".equals(verify_code) && "".equals(verify_code_coord) && Integer.parseInt(picture.getMistiming()) >= codeTimeOut){//距离现在codeTimeOut秒中还没有打码结果
				verify_code = "2";//随机设置一个验证码值返回给机器人
				verify_code_coord = JudgePicType.getCode(verify_code);
				logger.info(pic_id+"随机验证码：verify_code="+verify_code+"/verify_code_coord="+verify_code_coord);
			}
//			Matcher matcher = pattern.matcher(verify_code);
//			if(matcher.matches()){
//				verify_code="".equals(verify_code)?"":this.getCode(verify_code);
//			}else{
//				verify_code="".equals(verify_code)?"":verify_code;
//			}
			
//			logger.info("pic_id："+pic_id+"处理以后的code值为:"+verify_code);
			returnSuccess.setVerify_code(verify_code_coord);
			logger.info("robot request result："+JSONObject.fromObject(returnSuccess));
			///
			writeN2Response(response,JSONObject.fromObject(returnSuccess).toString());
		}catch(Exception e){
			logger.error("获取打码结果异常！", e);
			writeN2Response(response,null);
		}
	}
	
	//机器人输入打码值结果返回
	@RequestMapping("/feedBack.do")
	@ResponseBody
	public void feedBack(HttpServletRequest request,HttpServletResponse response){
		//Pattern pattern = Pattern.compile("^[0-9]*");
		
		//接受pic_ID 和合作伙伴ID
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Picture picture = new Picture();
		String partner_id= request.getParameter("partner_id");
		String pic_id= request.getParameter("pic_id");
		String sign= request.getParameter("sign");
		String result_status= request.getParameter("result_status");
		logger.info(" robot feedBack pic_id ：" + pic_id + ",result:" +result_status+",partner_id:" +partner_id);
		try{
			picture=robotService.getPicture(pic_id);
			if(picture.getChannel()==null||"".equals(picture.getChannel())){
				//pic_id错误
				logger.error("noPicture dberror");
				ReturnError returnError=new ReturnError();
				returnError.setRet(ReturnError.FAIL);
				returnError.setErr_code(ReturnError.ERRCODE12);
				returnError.setErr_msg(ReturnError.ERRMSG12);
				writeN2Response(response,JSONObject.fromObject(returnError).toString());
				return;
			}
			Map<String,String> status_map = new HashMap<String,String>();
			status_map.put("result_status", result_status);
			status_map.put("pic_id", pic_id);
			robotService.updateResultStatusById(status_map);
			
			//打码成功，则移动成功图片并重命名 
			//重命名规则：源文件名+verify_code			
			if("1".equals(result_status)){
				Picture picInfo = robotService.getPicture(pic_id);
				//正则匹配纯数字打码结果
				if(picInfo.getVerify_code_coord()!=null && StringUtils.isNotEmpty(picInfo.getVerify_code_coord())){
//					Matcher matcher = pattern.matcher(picInfo.getVerify_code());
//					if(matcher.matches()){
						File oldFile = new File(sys_root+picInfo.getPic_filename());
						String cal = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
						String cal_HH = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT5);
						String newPath = sys_root+"/wait_cut_map/"+cal+"/"+cal_HH+"/";
						
						File newFile = new File(newPath);
						if(!newFile.exists()){
							newFile.mkdirs();
						}
						
						String old_name = oldFile.getName();
						String new_name = old_name.substring(0, old_name.indexOf("."))+"_"+picInfo.getVerify_code_coord()+old_name.substring(old_name.indexOf("."),old_name.length());
						
						File newF = new File(newPath+new_name);
						boolean success = oldFile.renameTo(newF);
						if(success){
							logger.info("image move reame success:"+picInfo.getPic_id());
						}else{
							logger.info("image move reame failure:"+picInfo.getPic_id());
						}
//					}else{
//						logger.info("play code result is not right"+picInfo.getPic_id());
//					}
				}
			}
			//System.out.println(picId);
			//验证成功  发送成功信息
			ReturnSuccess returnSuccess=new ReturnSuccess();
			returnSuccess.setRet(ReturnSuccess.SUCCESS);
//			logger.info("返回打码结果："+JSONObject.fromObject(returnSuccess));
			writeN2Response(response,JSONObject.fromObject(returnSuccess).toString());
		}catch (Exception e) {
			ReturnError returnError=new ReturnError();
			returnError.setRet(ReturnError.FAIL);
			returnError.setErr_code(ReturnError.ERRCODE2);
			returnError.setErr_msg(ReturnError.ERRMSG2);
			logger.error("feedBack error！",e);
			writeN2Response(response,JSONObject.fromObject(returnError).toString());
			return;
		} 
	}
	
	
}
