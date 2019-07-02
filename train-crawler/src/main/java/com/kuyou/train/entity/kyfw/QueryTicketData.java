package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * QueryTicketData
 *
 * @author taokai3
 * @date 2018/11/15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryTicketData {

    private String flag;
    private JSONObject map;
    private List<String> result;

    @Override

    public String toString() {
        return JSON.toJSONString(this);
    }


}
