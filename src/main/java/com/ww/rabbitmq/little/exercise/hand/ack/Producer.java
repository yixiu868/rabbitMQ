package com.ww.rabbitmq.little.exercise.hand.ack;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.ww.rabbitmq.little.commons.ConnectionUtils;

/**
 * Description: 消费者限流，对应生产者
 * @author: xiaohua
 * @Date: 2020年4月19日下午11:26:32
 * @Version: 1.0
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();            
        Channel channel = connection.createChannel();
        
        String exchangeName = "test_qos_exchange";
        String routingKey = "qos.save";
        
        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ QOS Message " + i;
            System.out.println("开始第" + (i+1) + "次发送消息");
            channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
        }
    }
}
