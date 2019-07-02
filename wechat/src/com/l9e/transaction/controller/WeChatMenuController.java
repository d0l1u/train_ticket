package com.l9e.transaction.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.WeChatErrorCode;
import com.l9e.weixin.pojo.AccessToken;
import com.l9e.weixin.pojo.Button;
import com.l9e.weixin.pojo.CommonButton;
import com.l9e.weixin.pojo.ComplexButton;
import com.l9e.weixin.pojo.Menu;
import com.l9e.weixin.pojo.ViewButton;
import com.l9e.weixin.util.WeChatUtil;

@Controller
@RequestMapping("/weChatMenu")
public class WeChatMenuController {
	private static final Logger logger = Logger
			.getLogger(WeChatMenuController.class);
	@Value("#{propertiesReader[app_download_url]}")
	private String app_download_url;

	@RequestMapping("/createMenu.jhtml")
	public void createMenu(HttpServletRequest request,
			HttpServletResponse response) {

		// 调用接口获取access_token
		AccessToken at = WeChatUtil.getAccessToken(WeChatUtil.APPID,
				WeChatUtil.SECRET);
		if (null != at) {
			// 调用接口创建菜单
			logger.info("the access_token of the url is " + at.getToken());
			int result = WeChatUtil.createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result) {
				logger.info("菜单创建成功！");
			} else {
				logger.info("菜单创建失败，错误原因："
						+ WeChatErrorCode.getWeChatErrorCodeMap().get(result)
						+ ",错误码：" + result);
			}
		}
	}

	@RequestMapping("/updateMenu.jhtml")
	public void updateMenu(HttpServletRequest request,
			HttpServletResponse response) {
		// 调用接口获取access_token
		AccessToken at = WeChatUtil.getAccessToken(WeChatUtil.APPID,
				WeChatUtil.SECRET);

		if (null != at) {
			// 调用接口创建菜单
			logger.info("the access_token of the url is " + at.getToken());
			int deleteResult = WeChatUtil.deleteMenu(at.getToken());
			if (0 == deleteResult) {
				logger.info("菜单删除成功！");
			}
			int result = WeChatUtil.createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result) {
				logger.info("菜单创建成功！");
			} else {
				logger.info("菜单创建失败，错误原因："
						+ WeChatErrorCode.getWeChatErrorCodeMap().get(result)
						+ ",错误码：" + result);
			}
		}
	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	private Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("车票预订");
		btn11.setType("click");
		btn11.setKey("11");

		CommonButton btn12 = new CommonButton();
		btn12.setName("订单查询");
		btn12.setType("click");
		btn12.setKey("12");

		CommonButton btn13 = new CommonButton();
		btn13.setName("联系我们");
		btn13.setType("click");
		btn13.setKey("13");

		ViewButton btn14 = new ViewButton();
		btn14.setName("下载客户端");
		btn14.setType("view");
		btn14.setUrl(app_download_url);

		CommonButton btn15 = new CommonButton();
		btn15.setName("重置密码");
		btn15.setType("click");
		btn15.setKey("15");

		/*
		 * CommonButton btn21 = new CommonButton(); btn21.setName("修改密码");
		 * btn21.setType("view"); btn21.setKey("21");
		 */

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("更多");
		mainBtn1.setSub_button(new Button[] { btn13, btn14, btn15 });

		/**
		 * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { btn11, btn12, mainBtn1 });

		return menu;
	}

}
