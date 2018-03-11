package com.l9e.fileUpload;

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
/**
 * 文件上传
 * @author guona
 *
 */
@SuppressWarnings("serial")
public class FileUploadServlet extends HttpServlet {
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
		String bill_time = "";String fileName="";String filepath = "";String path="";
		try {
			// 解析之后，会将表单中的数据转换成一个个FileItem对象，一个表单域中的数据对应于一个FileItem对象
			List<FileItem> items = sfu.parseRequest(request);
			// 4 遍历items集合
			for (int i = 0; i < items.size(); i++) {
				FileItem item = items.get(i);
				// 读表单域中的数据时，要区分表单域的类型
				if (item.isFormField()) {
					// 普通表单域:账单时间
					bill_time = item.getString();
					//System.out.println("bill_time:" + bill_time);
					//filevo.setBill_time(bill_time);
				} else {
					// 文件上传表单域
					ServletContext sctx = getServletContext();
					path = sctx.getRealPath("upload");
					// 获得文件名
					//fileName = CommStr.jsEncoder(item.getName());
					fileName = item.getName();
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1,fileName.length());
					System.out.println("fileName文件名:" + fileName);
					File file = new File(path + "//" + fileName);
					System.out.println("path:" + path + "//" + fileName);
					filepath = path + "//" + fileName;
					item.write(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		StringBuilder builder = new StringBuilder();
		filepath = URLEncoder.encode(filepath,"utf-8");
		fileName = URLEncoder.encode(fileName,"utf-8");
		builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
		builder.append("window.location.href=\"");
		if(bill_time.equals("")||bill_time==""||bill_time==null){
			builder.append("/mail/addExcelMail.do?file="+CommStr.jsEncoder(filepath));
		}else{
			builder.append("/file/insertFile.do?bill_time="+bill_time+"&filename="+CommStr.jsEncoder(fileName)
					+"&filepath="+CommStr.jsEncoder(filepath));
		}
		
		builder.append("\"</script>");
		//System.out.println("hhhhhhhhhhhhhhhhh: "+CommStr.jsEncoder(filepath));
		System.out.println(builder);
		out.print(builder);
		out.close();
	}
	public static void main(String[] args) {
		String filename = "D/:\\jar\\commons-fileupload.jar";
		filename = filename.substring(filename.lastIndexOf(File.separator) + 1);
		System.out.println("fileName:" + filename);
	}
}
