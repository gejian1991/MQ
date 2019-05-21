package com.cn.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfig {


    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost",5672);
        //我这里直接在构造方法传入了
        //        connectionFactory.setHost();
        //        connectionFactory.setPort();
        connectionFactory.setUsername("test");
        connectionFactory.setPassword("test");
        connectionFactory.setVirtualHost("testhost");
        //是否开启消息确认机制    
        //connectionFactory.setPublisherConfirms(true)
        return connectionFactory;
    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
        TomcatServletWebServerFactory tomcatServletWebServerFactory =new TomcatServletWebServerFactory(8080);
        return tomcatServletWebServerFactory;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return  rabbitTemplate;
    }

    @Bean
    public DirectExchange defaultExchange() {
        //可以设置不同类型交换机
        return new DirectExchange ("directExchange");
    }

    @Bean
    public Queue queue() {
        //名字  是否持久化    
        return new Queue("testQueue", true);
    }

    @Bean
    public Binding binding() {
        //绑定一个队列  to: 绑定到哪个交换机上面 with：绑定的路由建（routingKey）    
        return BindingBuilder.bind(queue()).to(defaultExchange()).with("direct.key");
    }
/*  //现在以注解的方式声明队列，这种方法不方便
    @Bean public SimpleMessageListenerContainer  simpleMessageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        //这个connectionFactory就是我们自己配置的连接工厂直接注入进来    
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        //设置监听
        simpleMessageListenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {

            }

        });
        //simpleMessageListenerContainer.addQueueNames();
        //这边设置消息确认方式由自动确认变为手动确认    
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
         return simpleMessageListenerContainer;
    }*/


    //用这种方式完成手动确认，用注解的方式声明消费者
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory =
        new SimpleRabbitListenerContainerFactory();
        //这个connectionFactory就是我们自己配置的连接工厂直接注入进来    
        simpleRabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
        //这边设置消息确认方式由自动确认变为手动确认
        simpleRabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置消息预取数量
        //simpleRabbitListenerContainerFactory.setPrefetchCount(10);
        return simpleRabbitListenerContainerFactory;
    }











    //消息发送失败进行入库等操作
//  @Component
//    public class myCllBack implements  RabbitTemplate.ConfirmCallback{
//
//
//        @Autowired
//        XXXmapper
//
//        @Override
//        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//
//        }
//    }

}
