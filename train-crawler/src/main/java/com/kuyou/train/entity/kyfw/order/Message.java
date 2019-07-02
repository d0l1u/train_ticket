package com.kuyou.train.entity.kyfw.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Message
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private String message;
    private String code;
}
