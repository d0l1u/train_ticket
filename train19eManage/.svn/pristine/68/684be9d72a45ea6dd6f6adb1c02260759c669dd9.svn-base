package com.l9e.picUpload;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.l9e.util.CommStr;
import com.l9e.util.FileUtil;
/**
 * 图片上传
 * @author zhangjc
 *
 */
@SuppressWarnings("serial")
public class PicUploadServlet extends HttpServlet {
	//@Resource
	//private FileService fileService;
	
	@SuppressWarnings("unchecked")
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,   -1);
		String yesterday = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
		System.out.println(yesterday);

		// 1 创建一个工厂类的实例，该实例为解析器提供了缺省的配置
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 2 创建一个解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// 3 使用解析器解析
		String fileName="";String filepath = "";String path="";
		String question_order_id="";
		String question_theme="";
		String question_assigner="";
		String question_desc="";
		String question_pic="";
		try {
			List<FileItem> items = sfu.parseRequest(request);
			// 4 遍历items集合
			for (int i = 0; i < items.size(); i++) {
				FileItem item = items.get(i);
				if (item.isFormField()) {
					// 普通表单域:账单时间
					if(i==0)
					question_order_id = item.getString();
					if(i==1)
					question_theme = item.getString();
					if(i==2)
					question_assigner = item.getString();
					if(i==3)
					question_desc = item.getString();
				} else {
					// 文件上传表单域
					ServletContext sctx = getServletContext();
					path = sctx.getRealPath("upPicture");
					// 获得文件名
					fileName = item.getName();
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1,fileName.length());
					System.out.println("图片文件名:" + fileName);
					File file = new File(path + "//" + fileName);
					
					System.out.println("path:" + path + "//" + fileName);
					filepath = path + "//" + fileName;
					question_pic+=filepath+"@";
					item.write(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		StringBuilder builder = new StringBuilder();
		question_pic = URLEncoder.encode(question_pic,"utf-8");
		fileName = URLEncoder.encode(fileName,"utf-8");
		question_order_id = URLEncoder.encode(question_order_id,"utf-8");
		question_theme = URLEncoder.encode(question_theme,"utf-8");
		question_assigner = URLEncoder.encode(question_assigner,"utf-8");
		question_desc = URLEncoder.encode(question_desc,"utf-8");
		builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
		builder.append("window.location.href=\"");
				builder.append("/questionOrder/turntoAddQuestionPage.do?question_pic="+question_pic+
						"&fileName="+CommStr.jsEncoder(fileName)+
						"&question_order_id="+CommStr.jsEncoder(question_order_id)+
						"&question_theme="+CommStr.jsEncoder(question_theme)+
						"&question_assigner="+CommStr.jsEncoder(question_assigner)+
						"&question_desc="+CommStr.jsEncoder(question_desc));
		builder.append("\"</script>");
		System.out.println(builder);
		out.print(builder);
		out.close();
	}

	public static void main(String[] args) {
		String path="d:\\";
		String fileName="111.jpg";
		String www="";
		String ddd="";
		FileUtil.createFile(path,fileName, www, ddd);
	}
}
