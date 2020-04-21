package com.ww.rabbitmq.little.exercise.hand.ack;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * Description: 自定义消费者，扩展自DefaultConsumer
 * @author: xiaohua
 * @Date: 2020年4月19日下午11:31:56
 * @Version: 1.0
 */
public class MyConsumer extends DefaultConsumer {

    private Channel channel;
    
    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
            throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);
        System.out.println("消费消息了……");
        System.out.println("consumerTag: " + consumerTag);
        System.out.println("envelope: " + envelope);
        System.out.println("消费的消息: " + new String(body));
        
        // 手动签收，false不批量签收
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}
