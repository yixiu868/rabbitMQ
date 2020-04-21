package com.ww.rabbitmq.little.exercise.amqp.component;

import java.util.Map;

public class MessageDelegate {

    /**
     * 适配器使用方式1
     * 指定defaultListenerMethod
     */
    public void handleMessage(byte[] messageBody) {
        System.out.println("默认方法, 消费消息内容:" + new String(messageBody));
    }
    
    public void wwMsgProcess(String messageBody) {
        System.out.println("字节数组方法, 消息内容:" + messageBody);
    }
    
    
    /**
     * 适配器使用方式2
     * 队列名称和方法名称 进行一一匹配
     */
    public void method1(String messageBody) {
        System.out.println("method1收到消息内容: " + new String(messageBody));
    }
    
    public void method2(String messageBody) {
        System.out.println("method2收到消息内容: " + new String(messageBody));
    }
    
    /**
     * json对应Map对象
     * @param messageBody
     */
    /*public void consumerMessage(Map messageBody) {
        System.out.println("map方法, 消息内容: " + messageBody);
    }*/
    
    public void consumerMessage(Order order) {
        System.out.println("order对象, 消息内容, id: " + order.getId() + 
                ", name: " + order.getName() +
                ", content: " + order.getContent());
    }
}
