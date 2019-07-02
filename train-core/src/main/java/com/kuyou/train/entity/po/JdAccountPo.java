package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * JdAccountPo
 *
 * @author taokai3
 * @date 2018/12/5
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JdAccountPo {

    private Long accountId;
    private String username;
    private String password;
    private Integer status;
    private Integer type;
    private Boolean yn;
    private Date created;
    private Date modified;
}
