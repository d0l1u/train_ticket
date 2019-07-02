package com.kuyou.train.entity.kyfw.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * KyfwCommonResponse
 *
 * @author taokai3
 * @date 2018/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KyfwCommonResponse<T> extends AbstractKyfwResponse<T> {

    private List<String> messages;
    private JSONObject validateMessages;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
