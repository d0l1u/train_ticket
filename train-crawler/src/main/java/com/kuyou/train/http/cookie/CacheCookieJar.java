package com.kuyou.train.http.cookie;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kuyou.train.entity.dto.CookieDto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CacheCookieJar
 *
 * @author liujia33
 * @date 2018/9/28
 */
@Slf4j
public class CacheCookieJar implements CookieJar {

    private static final ThreadLocal<List<Cookie>> COOKIE_CACHE = new ThreadLocal<>();


    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookieList = COOKIE_CACHE.get();
        if (cookieList == null || cookieList.isEmpty()) {
            return new ArrayList<Cookie>();
        }
        return cookieList;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies.isEmpty()) {
            return;
        }

        //将原有的cookie组装成一个map
        Map<String, Cookie> map = new HashMap<>();
        List<Cookie> cookieList = COOKIE_CACHE.get();
        if (cookieList != null && !cookieList.isEmpty()) {
            map = cookieList.stream().collect(Collectors
                    .toMap(cookie -> String.format("%s#%s#%s", cookie.domain(), cookie.name(), cookie.path()),
                            Function.identity()));
        }

        //判断响应cookie是否已经存在，如果存在，进行覆盖
        Map<String, Cookie> finalMap = map;
        cookies.forEach(cookie -> finalMap
                .put(String.format("%s#%s#%s", cookie.domain(), cookie.name(), cookie.path()), cookie));
        cookies = Lists.newArrayList(finalMap.values().iterator());
        COOKIE_CACHE.set(cookies);
    }


    public static void main(String[] args) {
        List<Cookie> cookieList = Lists
                .newArrayList(new Cookie.Builder().domain("12306").path("/112").name("name").value("aa").build());
        Map<String, Cookie> map = cookieList.stream().collect(Collectors
                .toMap(cookie -> String.format("%s#%s#%s", cookie.domain(), cookie.name(), cookie.path()),
                        Function.identity()));
        System.err.println(map.entrySet().iterator().next().getKey());
        System.err.println(map);
    }

    /**
     * 设置cookie
     *
     * @param cookieStr
     */
    public void setCookie(String cookieStr) {
        //[{"domain":"", "name":"", "path":"", "value":""}]
        List<Cookie> cookieList = JSON.parseArray(cookieStr, CookieDto.class).stream()
                .map(cookie -> new Cookie.Builder().domain(cookie.getDomain()).name(cookie.getName())
                        .path(cookie.getPath()).value(cookie.getValue()).build()).collect(Collectors.toList());
        COOKIE_CACHE.set(cookieList);
    }

    /**
     * 获取cookie
     */
    public String getCookie() {
        List<CookieDto> cookieList = COOKIE_CACHE.get().stream()
                .map(cookie -> CookieDto.builder().domain(cookie.domain()).name(cookie.name()).path(cookie.path())
                        .value(cookie.value()).build()).collect(Collectors.toList());
        return JSON.toJSONString(cookieList);
    }

    /**
     * 清除cookie
     */
    public void clearCookie() {
        COOKIE_CACHE.remove();
    }

}