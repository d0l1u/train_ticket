package com.kuyou.train.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HthySyncDto
 *
 * @author taokai3
 * @date 2018/11/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HthySyncDto {

    private boolean success;
    private String message;

}
