package com.train.robot.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class DemoController extends  BaseController{




	@RequestMapping("/exception")
	@ResponseBody
	public void exception(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("1111111111111111111");


		printJson(response, "success");
	}
}
