package com.kuyou.train.entity.resp;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * HthyTicketInfoResp
 *
 * @author taokai3
 * @date 2018/12/13
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HthyTicketInfoResp {

    private boolean success;
    private int code;
    private String searchkey;
    private String msg;
    private List<HthyTicketDetailResp> data;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
