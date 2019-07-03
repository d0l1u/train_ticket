package com.l9e.transaction.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.FileService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.CommStr;
import com.l9e.util.PageUtil;

@Controller
@RequestMapping("/file")
public class FileController extends BaseController {
	private static final Logger logger = Logger.getLogger(FileController.class);

	@Resource
	private FileService fileService;
	
	/**
	 * 将数据库中保存的文件信息展现，可供下载
	 */
	@RequestMapping("/queryFileList.do")
	public String queryFileList(HttpServletResponse response,
			HttpServletRequest request) {
		logger.info("执行queryFileList.do");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		int pageCount = fileService.queryFileCount(paramMap);// 总条数
		// 分页
		PageVo page = PageUtil.getInstance().paging(request, 20, pageCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数
		List<Map<String, Object>> fileList = fileService
				.queryFileList(paramMap);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("fileList", fileList);
		request.setAttribute("isShowList", 1);
		// return "redirect:/hcStat/queryHcStatPage.do";
		return "fileupDown/fileList";
	}
	
	/**
	 * 将上传的文件信息插入到数据库
	 */
	@RequestMapping("/insertFile.do")
	public String insertFile(HttpServletResponse response,
			HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String username = loginUserVo.getReal_name();
		String filename = this.getParam(request, "filename");
		String filepath = this.getParam(request, "filepath");
		String bill_time = this.getParam(request, "bill_time");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("filename", filename);
		paramMap.put("filepath", filepath);
		paramMap.put("bill_time", bill_time);
		paramMap.put("opt_name", username);
		fileService.insertFile(paramMap);
		logger.info(username+"将文件信息插入到数据库,filename:"+filename+",filepath:"+filepath+",bill_time:"+bill_time);
		return "redirect:/file/queryFileList.do";
	}
	
	/**
	 * 跳转到文件上传页面
	 */
	@RequestMapping("/uploadFile.do")
	public String uploadFile(HttpServletResponse response,
			HttpServletRequest request) {
		String bill_time = this.getParam(request, "bill_time");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 前一天时间
		Date nowDate = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String bill_time1 = df.format(nowDate);// 前一天时间
		if (bill_time.equals("")) {
			request.setAttribute("bill_time", bill_time1);
		} else {
			request.setAttribute("bill_time", bill_time);
		}
		return "fileupDown/fileup";
	}
	
	/**
	 * 文件下载
	 */
	@RequestMapping("/fileDown.do")
	public void fileDown(HttpServletRequest request,
			HttpServletResponse response) {
		String id = this.getParam(request, "id");
		String dirPath = fileService.queryFilepath(id);
		logger.info("执行文件下载"+dirPath);
		// ************取得文件的路径和文件名***************//
		//String dirPath = request.getParameter("filepath");
		dirPath = CommStr.javaEncoder(dirPath);
		System.out.println(dirPath);
		//String fileName = request.getParameter("filename");
		String fileName = dirPath.substring(dirPath.lastIndexOf(File.separator) + 1);
		// ************判断文件是否存在********************//
		File file = new File(dirPath);
		if (!file.exists()) {
			System.out.println("文件下载失败：文件或路径错误");
			//return false;
		}
		long fileLength = file.length();
		String length = String.valueOf(fileLength);
		try {
			fileName = URLEncoder.encode(fileName,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 设置返回文件的类型和头信息，application/octet-stream:文件类型的通用格式//
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename="
				+ fileName);
		response.setHeader("Content_Length", length);
		FileInputStream input = null;
		ServletOutputStream output = null;
		try {
			// **************产生输入流和输出流*************//
			input = new FileInputStream(file);
			output = response.getOutputStream();
			byte[] block = new byte[1024];
			int len = 0;
			// **************开始下载文件*****************//
			while ((len = input.read(block)) != -1) {
				output.write(block, 0, len);
			}
			//return true;
			
		} catch (IOException e) {
			System.out.println("文件下载失败：" + e.getMessage());
			//return false;
		} finally {
			// *************关闭文件流****************//
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.flush();
					output.close();
				}
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	/**
	 * 将选中的文件信息删除
	 */
	@RequestMapping("/fileDelete.do")
	public String fileDelete(HttpServletResponse response,HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String username = loginUserVo.getReal_name();
		String id = this.getParam(request, "id");
		String dirPath = fileService.queryFilepath(id);
		File file = new File(dirPath);
		if(file.exists()){
			file.delete();
		}
		fileService.deleteFile(id);
		logger.info(username+"将文件信息删除,路径是:"+dirPath);
		return "redirect:/file/queryFileList.do";
	}

}
