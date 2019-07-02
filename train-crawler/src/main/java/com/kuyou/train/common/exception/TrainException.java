package com.kuyou.train.common.exception;

import com.kuyou.train.common.enums.KeyWordEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * TrainException
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Setter
@Getter
public class TrainException extends IOException {


    private String message;


    public TrainException(String message) {
        super(message);
        this.message = message;
    }

    public TrainException(KeyWordEnum keyWordEnum, String... info) {
        this.message = keyWordEnum.format(info);
    }

    public TrainException(KeyWordEnum keyWordEnum) {
        this.message = keyWordEnum.getKeyword();
    }



}
