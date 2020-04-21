package com.ww.rabbitmq.little.exercise.hand.ack;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.ww.rabbitmq.little.commons.ConnectionUtils;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        
        String exchangeName = "test_qos_exchange";
        String queueName = "test_qos_queue";
        String routingKey = "qos.#";
        
        channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        
        // 限流方式：
        // 1、设置autoAck为false；
        // 2、设置为1，表示一条一条数据处理
        channel.basicQos(0, 1, false);
        channel.basicConsume(queueName, false, new MyConsumer(channel));
    }
}
