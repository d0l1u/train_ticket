package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CookieDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CookieDto {
    //[{"domain":"", "name":"", "path":"", "value":""}]

    @JSONField(ordinal = 1)
    private String domain;

    @JSONField(ordinal = 2)
    private String name;

    @JSONField(ordinal = 3)
    private String path;

    @JSONField(ordinal = 4)
    private String value;
}
