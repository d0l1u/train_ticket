package com.kuyou.train.service.impl;

import com.kuyou.train.common.enums.SettingEnum;
import com.kuyou.train.dao.SettingMapper;
import com.kuyou.train.entity.po.SettingPo;
import com.kuyou.train.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * SettingServiceImpl
 *
 * @author taokai3
 * @date 2018/12/26
 */
@Slf4j
@Service
public class SettingServiceImpl implements SettingService {

    @Resource
    private SettingMapper settingMapper;

    @Override
    public SettingPo selectByName(SettingEnum settingEnum) {
        return settingMapper.selectByName(settingEnum.getName());
    }
}
