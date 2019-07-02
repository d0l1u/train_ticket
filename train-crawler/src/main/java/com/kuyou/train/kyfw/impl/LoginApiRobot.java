package com.kuyou.train.kyfw.impl;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.common.Constant;
import com.kuyou.train.common.code.Message;
import com.kuyou.train.common.enums.KeyEnum;
import com.kuyou.train.common.exception.NotLoginException;
import com.kuyou.train.common.exception.RetryException;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.util.CaptchaUtil;
import com.kuyou.train.common.util.RegexUtil;
import com.kuyou.train.entity.kyfw.*;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.http.cookie.CacheCookieJar;
import com.kuyou.train.kyfw.api.CaptchaApi;
import com.kuyou.train.kyfw.api.LoginApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * LoginService
 *
 * @author taokai3
 * @date 2018/11/11
 */
@Slf4j
@Service
public class LoginApiRobot {


    @Resource
    private JedisClient cookieJedisClient;

    @Resource
    private JedisClient switchJedisClient;

    @Resource
    private LoginApi loginApi;

    @Resource
    private CaptchaApiRobot captchaApiRobot;

    @Value("${login.max}")
    private int loginMax;


    /**
     * 检查登录状态
     *
     * @param username
     * @param password
     */
    public void checkLoginStatus(String username, String password) throws IOException {
        //从redis中获取cookie
        CacheCookieJar cacheCookieJar = new CacheCookieJar();
        String key = KeyEnum.PC_COOKIE.format(username);
        String cookieStr = "";
        try {
            cookieStr = cookieJedisClient.get(key);
            //log.info("Redis缓存中的cookie, key:{}, cookies:{}", key, cookieStr);
        } catch (Exception e) {
            log.info("获取Redis缓存cookie异常:{}", e.getClass().getSimpleName());
        }
        //判断缓存是存在cookie
        if (StringUtils.isBlank(cookieStr)) {
            log.info("{}尚未登录，进行12306登录", username);
            login12306(username, password);
            return;
        }

        //将cookie设置到本地线程，并判断是否登录成功
        cacheCookieJar.setCookie(cookieStr);

        //判断cookie是否有效
        if (!initQueryUserInfoApi()) {
            log.info("cookie已经失效，重新登录12306");
            login12306(username, password);
        }
    }

    private boolean initQueryUserInfoApi() throws TrainException {
        try {
            UserInfoData data = loginApi.initQueryUserInfoApi().execute().body().getData();
            log.info("initQueryUserInfoApi data:{}", data);
            String userStatus = data.getUserDTO().getUser_status();
            log.info("userStatus:{}", userStatus);
            if ("1".equals(userStatus)) {
                return true;
            }

            //错误信息
            String notice = data.getNotice1();
            if (StringUtils.isBlank(notice)) {
                notice = data.getNotice();
            }
            if ("已通过".equals(notice)) {
                return true;
            } else {
                throw new TrainException(notice);
            }
        } catch (NotLoginException e) {
			return  false;
		} catch (TrainException e) {
			throw e;
		} catch (Exception e) {
            log.info("initQueryUserInfoApi Exception", e);
        }
        return false;
    }

    /**
     * 登录12306
     *
     * @param username
     * @param password
     */
    public void login12306(String username, String password) throws IOException {

        //otn_login_conf
        LoginConfData data = loginApi.login_conf().execute().body().getData();
        log.info("login conf data:{}", data);

        String isLoginPassCode = data.getIs_login_passCode();
        String isUamLogin = data.getIs_uam_login();
        log.info("登录是否需要打码:{}", isLoginPassCode);
        log.info("登录是否需要验证:{}", isUamLogin);

        //判断是否开启，登录无验证码模式
        try {
            String loginSwitch = switchJedisClient.get(KeyEnum.LOGIN_SWITCH.getValue());
            if("1".equals(loginSwitch)){
                log.info("开启无验证码登录模式");
                isLoginPassCode = Constant.N;
                isUamLogin = Constant.N;
            }
        }catch (Exception e){
            log.info("获取登录模式开关异常");
        }

        int index = 0;
        IOException exception = null;
        while (index++ < loginMax) {
            exception = null;
            try {
                if(Constant.Y.equals(isLoginPassCode) && Constant.Y.equals(isUamLogin)){
                    //YY
                    //验证码
                    String answer = captchaApiRobot.crackCaptcha4Login();
                    //登录
                    webLogin(username, password, answer);
                    String appTk = webAuthUamTk();
                    uamAuthClient(appTk);
                } else if(Constant.Y.equals(isLoginPassCode) && Constant.N.equals(isUamLogin)){
                    String answer = captchaApiRobot.crackCaptcha4LoginNew();
                    loginAysnSuggest(username, password, answer);
                } else if(Constant.N.equals(isLoginPassCode) && Constant.N.equals(isUamLogin)){
                    //NN
                    loginAysnSuggest(username, password, "");
                } else {
                    webLogin(username, password, "");
                    String appTk = webAuthUamTk();
                    uamAuthClient(appTk);
                }
                //我的中心
                initQueryUserInfoApi();

                //缓存cookie
                try {
                    String setEx = cookieJedisClient.setex(KeyEnum.PC_COOKIE.format(username), 60 * 20, new CacheCookieJar().getCookie());
                    log.info("{}登录cookie缓存结果:{}", username, setEx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            } catch (RetryException e) {
                exception = e;
                log.info("login12306 -{}-登录重试, message:{}", index, e.getMessage());
            }
        }

        //走到这里的一定是重发超过3次的
        if (exception == null) {
            throw new TrainException("登录失败, 登录重发3次");
        } else {
            throw exception;
        }
    }

    private void loginAysnSuggest(String username, String password, String answer) throws IOException {
        KyfwCommonResponse<LoginAysnData> body ;
        if (StringUtils.isBlank(answer)) {
            body = loginApi.loginAysnSuggest(username, password).execute().body();
        } else {
            body = loginApi.loginAysnSuggest(username, password, answer).execute().body();
        }
		log.info("loginAysnSuggest body:{}", body);
		LoginAysnData data = body.getData();
		List<String> messages = body.getMessages();
		if (data == null) {
			if (CollectionUtils.isNotEmpty(messages)) {
				throw new TrainException("loginAysnSuggest登录失败:" + messages.get(0));
			} else {
				throw new TrainException("loginAysnSuggest登录失败 data is null");
			}
		}
		if (!Constant.Y.equals(data.getLoginCheck())) {
			String otherMsg = data.getOtherMsg();
			if (StringUtils.isBlank(otherMsg)) {
				if (CollectionUtils.isNotEmpty(messages)) {
					otherMsg = "loginAysnSuggest登录失败:" + messages.get(0);
				} else {
					otherMsg = "loginAysnSuggest登录失败 data is empty";
				}
			}
			throw new TrainException(otherMsg);
		}
    }

    private void getLoginBanner() throws IOException {
        loginApi.getLoginBanner().execute().body();
    }

    /**
     * 用户身份验证管理，验证客户端
     * @param appTk
     * @throws IOException
     */
    private void uamAuthClient(String appTk) throws IOException {
        LoginData data = loginApi.uamauthclient(appTk).execute().body();
        log.info("uamauthclient data:{}", data);
        Integer result_code = data.getResult_code();
        if (!new Integer(0).equals(result_code)) {
            throw new TrainException(data.getResult_message());
        }
    }

    /**
     * 用户身份验证管理，验证token
     * @return
     * @throws IOException
     */
    private String webAuthUamTk() throws IOException {
        LoginData data = loginApi.web_auth_uamtk("otn").execute().body();
        log.info("web_auth_uamtk data:{}", data);
        Integer result_code = data.getResult_code();
        if (!new Integer(0).equals(result_code)) {
            throw new TrainException(data.getResult_message());
        }
        return data.getNewapptk();
    }

    /**
     * 登录
     * @param username
     * @param password
     * @param answer
     * @throws IOException
     */
    private void webLogin(String username, String password, String answer) throws IOException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("appid", "otn");
        map.put("answer", answer);
        LoginData data = loginApi.web_login(map).execute().body();
        log.info("web_login data:{}", data);
        Integer result_code = data.getResult_code();
        if (!new Integer(0).equals(result_code)) {
            throw new TrainException(data.getResult_message());
        }
    }
}
