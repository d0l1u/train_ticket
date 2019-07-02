package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * LoginConfData
 *
 * @author taokai3
 * @date 2018/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginConfData {

    private boolean isstudentDate;
    private String is_login_passCode;
    private String is_sweep_login;
    private String psr_qr_code_result;
    private String login_url;
    private List<String> studentDate;
    private int stu_control;
    private String is_uam_login;
    private String is_login;
    private int other_control;
    private String name;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
