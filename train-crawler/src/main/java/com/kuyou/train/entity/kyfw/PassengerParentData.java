package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.*;

import java.util.List;

/**
 * PassengerKyfwData
 *
 * @author liujia33
 * @date 2018/9/29
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerParentData {

    List<PassengerData> datas;

    Boolean flag;

    Integer pageTotal;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
