package com.l9e.transaction.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.Picture;
import com.l9e.util.CreateIDUtil;



@Controller
public class UploadPictureController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(UploadPictureController.class);
	@Resource
	RobotCodeService robotService;
	
	@RequestMapping("/upload.do")
	@ResponseBody
	public void upload(HttpServletRequest request,HttpServletResponse response){
		logger.info(" upload picture start");
		Picture picture = new Picture();
		//String picAddress = new ConfigUtil().getValue("pic.address");
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> list;
//		String picId = "";
		try {
			list = upload.parseRequest(request);
			for (FileItem item : list) {
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					if ("channel".equals(name)) {
						picture.setChannel(value);
					} else if ("effectTime".equals(name)) {
						picture.setEffect_time(value);
					}
				} else {
					String filename = item.getName();
					//filename = filename.substring(filename.lastIndexOf("\\") + 1);
					String suffix = filename.substring(filename.lastIndexOf("."));//文件后缀名
					String picId = CreateIDUtil.createID(CreateIDUtil.HEAD_PICTURE);
					picture.setPic_id(picId);
					String filePath = request.getSession().getServletContext().getRealPath("/");  
					//String picFilename = "/upload/" + filename;
					String picFilename = "/upload/" + picId + suffix;
					picture.setPic_filename(picFilename);
					logger.info(new Date().getTime()+" upload picture picFilename "+picFilename);
					// 获得流，读取数据写入文件
					InputStream in = item.getInputStream();
					FileOutputStream fos = new FileOutputStream(filePath+picFilename);
					int len;
					byte[] buffer = new byte[1024];
					while ((len = in.read(buffer)) > 0)
						fos.write(buffer, 0, len);
					fos.close();
					in.close();
				}
			}
		} catch (Exception e) {
			logger.error("fileupload error" + e.getMessage());
			writeN2Response(response, "");
			return;
		}
		robotService.savePicture(picture);
		writeN2Response(response, picture.getPic_id());
	}
}
