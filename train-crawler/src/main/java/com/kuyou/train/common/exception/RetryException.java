package com.kuyou.train.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * RetryException：重试异常
 *
 * @author taokai3
 * @date 2018/11/30
 */
@Setter
@Getter
public class RetryException extends TrainException {
    public RetryException(String message) {
        super(message);
    }
}
