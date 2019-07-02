package com.kuyou.train.service.impl;

import com.kuyou.train.dao.JdAccountMapper;
import com.kuyou.train.entity.po.JdAccountPo;
import com.kuyou.train.service.JdAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * JdAccountServiceImpl
 *
 * @author taokai3
 * @date 2018/12/5
 */
@Slf4j
@Service
public class JdAccountServiceImpl implements JdAccountService {

    @Resource
    private JdAccountMapper jdAccountMapper;

    @Override
    public int selectCountType(Integer type) {
        return jdAccountMapper.selectCountType(type);
    }


    @Override
    public int insertBatch(List<JdAccountPo> jdAccountPos) {
        return jdAccountMapper.insertBatch(jdAccountPos, jdAccountPos.get(0));
    }

    @Override
    public int updateById(JdAccountPo jdAccountPo, Long accountId) {
        return jdAccountMapper.updateById(jdAccountPo, accountId);
    }
}
