package com.kuyou.train.kyfw.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.kuyou.train.common.Constant;
import com.kuyou.train.common.code.Message;
import com.kuyou.train.common.exception.RetryException;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.util.CaptchaUtil;
import com.kuyou.train.common.util.RegexUtil;
import com.kuyou.train.entity.kyfw.CaptchaCheckData;
import com.kuyou.train.entity.kyfw.CaptchaData;
import com.kuyou.train.kyfw.api.CaptchaApi;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * PassCodeApiRobot
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Slf4j
@Service
public class CaptchaApiRobot {

    private static final Pattern JQUERY_PATTERN = Pattern.compile("\\(([^\\)]+)\\)");

    @Value("${captcha.max}")
    private int captchaMax;

    @Value("${captcha.sleep}")
    private int captchaSleep;

    @Resource
    private CaptchaApi captchaApi;


    /**
     * 破解验证码：登录
     *
     * @throws IOException
     */
    public String crackCaptcha4Login() throws IOException {
        int index = 0;
        TrainException exception = null;
        while (index++ < captchaMax) {
            try {
                if (index != 1) {
                    try {
                        Thread.sleep(captchaSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                LinkedHashMap<String, Object> imageMap = new LinkedHashMap<>();
                LinkedHashMap<String, Object> checkMap = new LinkedHashMap<>();
                String millis = String.valueOf(System.currentTimeMillis());
                imageMap.put("login_site", "E");
                imageMap.put("module", "login");
                imageMap.put("rand", "sjrand");
                imageMap.put(millis, "");
                imageMap.put("callback", String.format("jQuery%s", Long.valueOf(millis) + 10));
                imageMap.put("_", Long.valueOf(millis) + 11);
                Call<String> call = captchaApi.captcha_image4Login(imageMap);

                //正则，获取()中的json
                String body = call.execute().body();
                String dataStr = RegexUtil.matcher(body, JQUERY_PATTERN);
                //log.info("captcha-image data:{}", dataStr);
                CaptchaData data = JSON.parseObject(dataStr, CaptchaData.class);
                String resultCode = data.getResult_code();
                String resultMessage = data.getResult_message();
                if (!"0".equals(resultCode)) {
                    log.info("获取验证码失败:{}", resultMessage);
                    throw new RetryException(resultMessage);
                }

                byte[] bytes = new BASE64Decoder().decodeBuffer(data.getImage());
                if (bytes.length < 10000) {
                    throw new RetryException("验证码过小");
                }

                //获取打码结果
                String answer = new CaptchaUtil().getKyfwImageCode(bytes, System.currentTimeMillis() + ".png");
                if (StringUtils.isBlank(answer)) {
                    throw new RetryException("打码接口无打码结果");
                }

                //校验打码结果
                checkMap.put("callback", String.format("jQuery%s", Long.valueOf(millis) + 10));
                checkMap.put("answer", answer);
                checkMap.put("rand", "sjrand");
                checkMap.put("login_site", "E");
                checkMap.put("_", Long.valueOf(millis) + 12);
                call = captchaApi.captcha_check4Login(checkMap);

                //正则，获取()中的json
                body = call.execute().body();
                dataStr = RegexUtil.matcher(body, JQUERY_PATTERN);
                log.info("captcha-check data:{}", dataStr);
                data = JSON.parseObject(dataStr, CaptchaData.class);
                resultCode = data.getResult_code();
                if (!"4".equals(resultCode)) {
                    throw new RetryException(data.getResult_message());
                }
                return answer;
            } catch (TrainException e) {
                //发生异常进行重试
                exception = e;
                log.info("{}, 进行重试", e.getMessage());
            }
        }

        //到这里来的一定是，重发超过三次
        if (exception == null) {
            exception = new TrainException("登录时破解验证码失败");
        }
        throw exception;
    }


    /**
     * 下单破解验证码
     * @param token
     * @param isBook
     * @return
     * @throws IOException
     */
    public String crackCaptcha4Order(String ifShowPassCode, String token, boolean isBook) throws IOException {
        if (Constant.N.equals(ifShowPassCode)) {
           return "";
        }
        log.info("下单需要验证码");
        int index = 0;
        TrainException exception = null;
        while (index++ < captchaMax) {
            try {
                if (index != 1) {
                    try {
                        Thread.sleep(captchaSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                LinkedHashMap<String, Object> imageMap = Maps.newLinkedHashMap();
                LinkedHashMap<String, Object> checkMap = Maps.newLinkedHashMap();
                String millis = String.valueOf(System.currentTimeMillis());
                imageMap.put("module", "passenger");
                imageMap.put("rand", "randp");
                imageMap.put(String.valueOf(new Random().nextDouble()), "");
                byte[] bytes = captchaApi.captcha_image4Order(imageMap,isBook ? Constant.INIT_DC : Constant.INIT_GC)
                        .execute().body().bytes();
                if (bytes.length < 1000) {
                    throw new RetryException(String.format(Message.CAPTCHA_FAIL, "验证码过小"));
                }

                //获取打码结果
                String answer = new CaptchaUtil().getKyfwImageCode(bytes, System.currentTimeMillis() + ".png");
                if (StringUtils.isBlank(answer)) {
                    throw new RetryException(String.format(Message.CAPTCHA_FAIL, "打码服务器返回结果为空"));
                }

                //校验打码结果
                checkMap.put("randCode", answer);
                checkMap.put("rand", "randp");
                checkMap.put("_json_att", "");
                checkMap.put("REPEAT_SUBMIT_TOKEN", token);
                CaptchaCheckData data = captchaApi.captcha_check4Order(checkMap, isBook ? Constant.INIT_DC : Constant.INIT_GC)
                        .execute().body().getData();
                log.info("下单验证码check data:{}", data);

                if (!"1".equals(data.getResult())) {
                    throw new RetryException("下单验证码校验失败");
                }
                return answer;
            } catch (TrainException e) {
                //发生异常进行重试
                exception = e;
                log.info("{}, 进行重试", e.getMessage());
            }
        }

        //到这里来的一定是，重发超过三次
        if (exception == null) {
            exception = new TrainException("下单验证码失败");
        }
        throw exception;
    }

	public String crackCaptcha4LoginNew() throws IOException {
        int index = 0;
        TrainException exception = null;
        while (index++ < captchaMax) {
            try {
                if (index != 1) {
                    try {
                        Thread.sleep(captchaSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                byte[] bytes = getPassCodeNew(index);
                if (bytes.length < 1000) {
                    throw new RetryException("验证码过小");
                }
                //获取打码结果
                String answer = new CaptchaUtil().getKyfwImageCode(bytes, System.currentTimeMillis() + ".png");
                if (StringUtils.isBlank(answer)) {
                    throw new RetryException("打码接口无打码结果");
                }
                //校验打码结果
               checkRandCodeAnsyn(answer);
                return answer;
            } catch (TrainException e) {
                //发生异常进行重试
                exception = e;
                log.info("{}, 进行重试", e.getMessage());
            }
        }

        //到这里来的一定是，重发超过三次
        if (exception == null) {
            exception = new TrainException("登录时破解验证码失败");
        }
        throw exception;
	}

    private void checkRandCodeAnsyn(String answer) throws IOException {
        LinkedHashMap<String, Object> checkMap = new LinkedHashMap<>();
        checkMap.put("randCode", answer);
        checkMap.put("rand", "sjrand");
        checkMap.put("login_site", "E");
		CaptchaCheckData data = captchaApi.checkRandCodeAnsyn(checkMap).execute().body().getData();
		if (!"1".equals(data.getResult())) {
			throw new TrainException("登录验证码校验失败");
		}
	}

    private byte[] getPassCodeNew(int index) throws TrainException {
        int getIndex = 0;
        int sleep = 1500;
        do {
            try {
                getIndex++;
                log.info("第 {}-{} 次获取验证码图片", index, getIndex);
                String url = String.format("/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&%s",
                        System.currentTimeMillis());
                byte[] bytes = captchaApi.getPassCodeNew(url).execute().body().bytes();
                log.info("验证码字节length:{}", bytes.length);
                if (bytes.length < 1000) {
                    Thread.sleep(sleep);
                    sleep = sleep + 500;
                    continue;
                }
                return bytes;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (getIndex < 3);

        //这里表明，获取验证验证码失败
        throw new TrainException("登录时破解验证码失败");
    }
}
