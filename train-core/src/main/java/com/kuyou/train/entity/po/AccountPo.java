package com.kuyou.train.entity.po;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChangeCpPo
 *
 * @author taokai3
 * @date 2018/10/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPo {

    private Integer accountId;

    private String username;

    private String password;
    private String channel;

    private Integer contactsNum;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}