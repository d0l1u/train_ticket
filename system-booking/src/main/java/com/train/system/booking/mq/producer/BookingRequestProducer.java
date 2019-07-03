package com.train.system.booking.mq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @ClassName: BookingProducer
 * @Description: 将订单数据推送到队列中
 * @author: taoka
 * @date: 2018年6月14日 上午11:25:56
 * @Copyright: 2018 www.19e.cn Inc. All rights reserved.
 */
public class BookingRequestProducer extends DefaultMQProducer {

    private Logger logger = LoggerFactory.getLogger(BookingRequestProducer.class);

    private static final String TOPIC = "TRAIN_BOOKING";
    private static final String TAG = "BOOKING_PC";

    public SendStatus send(String body) throws MQClientException, RemotingException, MQBrokerException, InterruptedException, UnsupportedEncodingException {

        String keys = String.valueOf(System.nanoTime());
        logger.info("Topic: {}, Tag: {}, Keys:{}, Body: {}", TOPIC, TAG, keys, body);
        Message msg = new Message();
        msg.setTopic(TOPIC);
        msg.setTags(TAG);
        msg.setBody(body.getBytes("UTF-8"));
        msg.setKeys(keys);
        SendResult sendResult = this.send(msg);
        String msgId = sendResult.getMsgId();
        SendStatus status = sendResult.getSendStatus();
        logger.info("Send Status: {}, MessageId: {}", status, msgId);
        return status;
    }
}
