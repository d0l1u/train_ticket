package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * PassengerPo
 *
 * @author taokai3
 * @date 2018/12/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerPo {

    private Integer id;
    private String name;
    private String cardNo;
    private String cardType;
    private String username;
    private String password;
    private Integer accountId;
    private String accountStatus;
    private Integer contactsNum;
    private Date deleteTime;
    private String isUserSelf;
    private Date lastUseTime;
}
