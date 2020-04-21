package com.ww.rabbitmq.little.exercise.reback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.ww.rabbitmq.little.commons.ConnectionUtils;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        
        String exchange = "test_ack_exchange";
        String routingKey = "ack.save";
        
        for (int i = 0; i < 5; i++) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("num", i);
            
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .deliveryMode(2) // 投递模式：持久化
                    .contentEncoding("UTF-8")
                    .headers(headers)
                    .build();
            
            String msg = "Hello RabbitMQ ACK Message " + i;
            channel.basicPublish(exchange, routingKey, properties, msg.getBytes());
        }
    }
}
