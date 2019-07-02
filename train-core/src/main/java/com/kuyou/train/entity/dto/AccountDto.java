package com.kuyou.train.entity.dto;

import com.kuyou.train.entity.po.PassengerPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AccountDto
 *
 * @author taokai3
 * @date 2018/11/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private String username;
    private String password;
    private List<PassengerPo> passengers;
}
