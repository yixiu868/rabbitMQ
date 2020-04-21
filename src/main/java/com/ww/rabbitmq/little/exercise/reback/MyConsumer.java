package com.ww.rabbitmq.little.exercise.reback;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * Description: 消费端手工ACK和NACK重回队列
 * @author: xiaohua
 * @Date: 2020年4月20日上午12:11:26
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
        System.out.println("-------------------消费者开始消费了-------------------");
        System.out.println("body: " + new String(body));
        
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (0 == Integer.parseInt(properties.getHeaders().get("num").toString())) {
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        } else {
            channel.basicAck(envelope.getDeliveryTag(), false);
        }
    }
}
