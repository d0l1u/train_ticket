package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * loginAysnData
 *
 * @author taokai3
 * @date 2019/1/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginAysnData {

	private String loginCheck;
	private String otherMsg;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
