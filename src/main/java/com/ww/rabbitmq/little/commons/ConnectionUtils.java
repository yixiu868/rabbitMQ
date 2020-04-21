package com.ww.rabbitmq.little.commons;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtils {

    public static Connection getConnection() throws IOException, TimeoutException {
        // 定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务地址
        factory.setHost("127.0.0.1");
        // 设置端口
        factory.setPort(5672);
        // 设置账户信息、用户名、密码、vhost
        factory.setVirtualHost("ww");
        factory.setUsername("guest");
        factory.setPassword("guest");
        
        // 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
