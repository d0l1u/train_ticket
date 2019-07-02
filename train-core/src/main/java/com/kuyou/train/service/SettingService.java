package com.kuyou.train.service;

import com.kuyou.train.common.enums.SettingEnum;
import com.kuyou.train.entity.po.SettingPo;

/**
 * SettingService
 *
 * @author taokai3
 * @date 2018/12/26
 */
public interface SettingService {

    /**
     * 根据name查询
     *
     * @param settingEnum
     * @return
     */
    SettingPo selectByName(SettingEnum settingEnum);
}
