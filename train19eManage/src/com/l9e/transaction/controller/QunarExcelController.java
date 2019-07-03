package com.l9e.transaction.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
@Controller
@RequestMapping("/qunarExcel")
public class QunarExcelController extends BaseController {

	public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
		String filename = "chepiao.xls";
		String filepath = request.getSession().getServletContext().getRealPath("/excel") + filename;
		Workbook workbook = null;
		WritableWorkbook book = null;
		try {
			workbook = Workbook.getWorkbook(new File(filepath));
			book = Workbook.createWorkbook(new File(filepath));
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
