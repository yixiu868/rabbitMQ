package com.ww.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.rabbitmq.little.exercise.amqp.component.Order;

@SpringBootTest
class RabbitMqApplicationTests {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	public void testSendMessage() {
	    // 1、创建消息
	    MessageProperties messageProperties = new MessageProperties();
	    messageProperties.getHeaders().put("desc", "信息描述...");
	    messageProperties.getHeaders().put("type", "自定义消息类型");
	    
	    Message message = new Message("RabbitMQ testSendMessage".getBytes(), messageProperties);
	    rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, new MessagePostProcessor() {
            
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().getHeaders().put("desc", "修改描述");
                message.getMessageProperties().getHeaders().put("type", "修改消息类型");
                return message;
            }
        });
	}
	
	@Test
	public void testSendMessage2() {
	    MessageProperties messageProperties = new MessageProperties();
	    messageProperties.setContentType("text/plain");
	    Message message = new Message("m1q消息".getBytes(), messageProperties);
	    rabbitTemplate.send("topic001", "spring.abc", message);
	    rabbitTemplate.convertAndSend("topic001", "spring.amqp", "hello object message send!!!");
	    rabbitTemplate.convertAndSend("topic002", "rabbit.abc", "hello object message send!");
	}
	
	@Test
	public void testSendJsonMessage() throws JsonProcessingException {
	    Order order = new Order();
	    order.setId("001");
	    order.setName("消息订单");
	    order.setContent("描述信息");
	    ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(order);
	    System.out.println("Order json信息: " + json);
	    
	    MessageProperties messageProperties = new MessageProperties();
	    messageProperties.getHeaders().put("__TypeId__", "com.ww.rabbitmq.little.exercise.amqp.component.Order");
	    messageProperties.setContentType("application/json");
	    Message message = new Message(json.getBytes(), messageProperties);
	    
	    rabbitTemplate.send("topic001", "spring.order", message);
	}
}
