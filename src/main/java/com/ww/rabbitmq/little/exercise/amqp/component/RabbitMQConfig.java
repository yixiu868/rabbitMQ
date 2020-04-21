package com.ww.rabbitmq.little.exercise.amqp.component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan({ "com.ww.rabbitmq" })
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("ww");
        return connectionFactory;
    }
    
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }
    
    @Bean
    public RabbitTemplate RabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }
    
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // 添加多个队列进行监听
        container.setQueues(queue001(), queue002(), queue003());
        // 当前消费者数量
        container.setConcurrentConsumers(1);
        // 最大消费者数量
        container.setMaxConcurrentConsumers(5);
        // 设置重回队列,一般设置false
        container.setDefaultRequeueRejected(false);
        // 设置自动签收机制
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        // 设置listner外露
        container.setExposeListenerChannel(true);
        // 消费端标签生成策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            
            @Override
            public String createConsumerTag(String queue) {
                // 每个消费端都有自己独立的标签
                return queue + "_" + UUID.randomUUID().toString();
            }
        });
        // 消息监听
        /*container.setMessageListener(new ChannelAwareMessageListener() {
            
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String msg = new String(message.getBody());
                System.out.println("消费者消费:" + msg);
            }
        });*/
        
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        // adapter.setDefaultListenerMethod("wwMsgProcess");
        // adapter.setMessageConverter(new TextMessageConverter());
        // Map<String, String> queueOrTagToMethodName = new HashMap<String, String>();
        // queueOrTagToMethodName.put("queue001", "method1");
        // queueOrTagToMethodName.put("queue002", "method2");
        // adapter.setQueueOrTagToMethodName(queueOrTagToMethodName); // 队列名称和方法名称 进行一一匹配
        
        adapter.setDefaultListenerMethod("consumerMessage");
        // 重点, 加入json格式的转换器, json对应Map对象
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        // 需要将javaTypeMapper放入到Jackson2JsonMessageConverter对象中
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        
        
        
        container.setMessageListener(adapter);
        
        return container;
    }
    
    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }
    
    @Bean
    public Queue queue001() {
        return new Queue("queue001", true);
    }
    
    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }
    
    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002", true, false);
    }
    
    @Bean
    public Queue queue002() {
        return new Queue("queue002", true);
    }
    
    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }
    
    @Bean
    public Queue queue003() {
        return new Queue("queue003", true);
    }
    
    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }
}
