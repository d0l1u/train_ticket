package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuyou.train.entity.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * InitMyData
 *
 * @author taokai3
 * @date 2018/11/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoData {


    private String userTypeName;
    private String picFlag;
    private String canUpload;
    private String userPassword;
    private String notice1;
    private String isMobileCheck;
    private boolean canEdit;
    private String country_name;
    private UserDto userDTO;

    @JSONField(format = "yyyy-MM-dd")
    private Date bornDateString;
    private String notice;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
