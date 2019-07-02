package com.kuyou.train.http.converter;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;

/**
 * StringResponseBodyConverter
 *
 * @author taokai3
 * @date 2018/9/25
 */
public class StringResponseBodyConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            return value.string();
        } finally {
            value.close();
        }
    }
}
