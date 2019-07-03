package com.soservice.mina.encoder;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 解码器
 * 
 * @author zhangyou
 * 
 */
public class DataEncoderEx extends ProtocolEncoderAdapter {
	/**
	 * 具体实现
	 */
	public void encode(IoSession session, Object message,
	ProtocolEncoderOutput out) throws Exception {
		if (!"".equals(message)) {
			IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
			String strOut = message.toString();
			//加入包头 用4个字节的int值代表包体信息长度 用来处理粘包断包等问题
			buf.putInt(strOut.getBytes(Charset.forName("utf-8")).length);
			buf.putString(strOut, Charset.forName("utf-8").newEncoder());
			buf.flip();
			out.write(buf);
		}
	}
}
