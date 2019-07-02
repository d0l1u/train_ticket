package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginData
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginData {

    private String result_message;
    private Integer result_code;

    private String uamtk;
    private String apptk;
    private String newapptk;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
