package com.mall.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: rabbitmq配置类
 **/
@Configuration
public class RabbitmqConfig {

    /**
    * 死信交换机
    * @return
    **/
    @Bean
    public DirectExchange orderDelayExchange() {
        return new DirectExchange("order.delay_exchange");
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("x-dead-letter-exchange", "order.receive_exchange");
        map.put("x-dead-letter-routing-key", "order.receive_key");
        return new Queue("order.delay_queue", true, false, false, map);
    }

    /**
     * 给死信队列绑定交换机
     * @return
     */
    @Bean
    public Binding orderDelayBinding(){
        return BindingBuilder.bind(orderDelayQueue()).to(orderDelayExchange()).with("order.delay_key");
    }

    /**
     * 死信接收交换机
     * @return
     */
    @Bean
    public DirectExchange orderReceiveExchange(){
        return new DirectExchange("order.receive_exchange");
    }

    /**
     * 死信接收队列(消费队列)
     * @return
     */
    @Bean
    public Queue orderReceiveQueue(){
        return new Queue("order.receive_queue");
    }

    /**
     * 死信接收交换机绑定消费队列
     * @return
     */
    @Bean
    public Binding orderReceiveBinding(){
        return BindingBuilder.bind(orderReceiveQueue()).to(orderReceiveExchange()).with("order.receive_key");
    }

    /**
     * rabbitmq操作模板配置
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    /**
     * rabbitmq工厂监听配置
     * @param connectionFactory
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

}
