package com.kuyou.train.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @ClassName: CaptchaUtil
 * @Description: 验证码工具类
 * @author: taokai
 * @date: 2017年7月19日 下午7:06:34
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
@Slf4j
public class CaptchaUtil {

    /**
     * 上传验证码路径
     */
    private static final String KYFW_SEND_URL = "http://219.238.151.222:18010/partner/sendCode.do";
    private static final String ENGLISH_SEND_URL = "http://219.238.151.236:18000/partner/sendCode.do";
    /**
     * 获取打码结果路径
     */
    private static final String KYFW_GET_URL = "http://219.238.151.222:18010/partner/requestResult.do";
    private static final String ENGLISH_GET_URL = "http://219.238.151.236:18000/partner/requestResult.do";

    /**
     * 获取验证码
     *
     * @param filePath
     * @param fileName
     * @return String
     * @author: taoka
     * @date: 2018年1月3日 下午4:52:18
     */
    public String getImageCode(String filePath, String fileName, boolean isKyfw) {
        return getImageCode(image2byte(filePath), fileName, isKyfw);
    }


    public String getKyfwImageCode(byte[] bytes, String fileName) {
        return getImageCode(bytes, fileName, true);
    }


    /**
     * 获取验证码
     *
     * @param bytes
     * @param fileName
     * @param isKyfw
     * @return
     */
    public String getImageCode(byte[] bytes, String fileName, boolean isKyfw) {
        long begin = System.currentTimeMillis();
        // 上传图片至服务器
        String uploadResult = uploadImage(bytes, fileName, isKyfw);
        log.info("验证码图片上传结果:{}", uploadResult);

        JSONObject json = JSONObject.parseObject(uploadResult);
        String picId = json.getString("pic_id");
        log.info("PIC-ID:{}", picId);

        if (StringUtils.isBlank(picId)) {
            return "";
        }

        // 获取打码结果
        int count = 1;
        String result = "";
        while (count < 11 && StringUtils.isBlank(result)) {
            result = getResult(picId, isKyfw);
            log.info("获取打码结果-{}-, result:{}", count, result);
            count++;
        }
        BigDecimal runTime = new BigDecimal(Long.valueOf(System.currentTimeMillis() - begin));
        runTime = runTime.divide(BigDecimalUtil.divideTime(1000));
        log.info("打码耗时:{}", runTime);
        return result;
    }

    /**
     * 图片转数组
     *
     * @param path
     * @return
     */
    private byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取打码结果
     *
     * @param picId
     * @param isKyfw
     * @return
     */
    private String getResult(String picId, boolean isKyfw) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        String temp_url = "";
        String result = "";
        if (isKyfw) {
            temp_url = KYFW_GET_URL + "?pic_id=" + picId;
        } else {
            temp_url = ENGLISH_GET_URL + "?pic_id=" + picId;
        }
        BufferedReader in = null;
        try {
            URL url = new URL(temp_url);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            StringBuffer sb = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }
            JSONObject verify_code_obj = JSON.parseObject(sb.toString());
            result = verify_code_obj.getString("verify_code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 上传验证码
     *
     * @param bytes
     * @param fileName
     * @param isKyfw
     * @return
     */
    private String uploadImage(byte[] bytes, String fileName, boolean isKyfw) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------";
        try {
            URL url = null;
            if (isKyfw) {
                url = new URL(KYFW_SEND_URL);
            } else {
                url = new URL(ENGLISH_SEND_URL);
            }

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = new StringBuffer();
            if (isKyfw) {
                sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"pic_id\"\r\n\r\n").append("pic1");
            } else {
                sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"order_id\"\r\n\r\n").append("order_id");
                sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"pic_id\"\r\n\r\n").append("pic1");
            }

            out.write(sb.toString().getBytes());

            sb = new StringBuffer();
            sb.append("\r\n--").append(BOUNDARY).append("\r\n")
                    .append("Content-Disposition: form-data; name=\"userfile\"; filename=\"" + fileName + "\"\r\n")
                    .append("Content-Type:image/png\r\n\r\n");
            out.write(sb.toString().getBytes());

            out.write(bytes);

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

}
