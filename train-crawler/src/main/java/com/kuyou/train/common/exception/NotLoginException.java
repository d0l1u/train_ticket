package com.kuyou.train.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * LoginException：未登录异常
 *
 * @author taokai3
 * @date 2018/11/27
 */
@Setter
@Getter
public class NotLoginException extends TrainException {
    public NotLoginException(String message) {
        super(message);
    }
}
