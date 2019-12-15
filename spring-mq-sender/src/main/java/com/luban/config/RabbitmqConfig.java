package com.luban.config;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
@ComponentScan("com")
public class RabbitmqConfig {


    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("192.168.91.143",5672);
        //我这里直接在构造方法传入了
        //        connectionFactory.setHost();
        //        connectionFactory.setPort();
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("my_vhost");
        //开启消息确认机制，失败回调也需要设置
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    /**
     * 死信交换机配置
     * @return
     */
    @Bean
    public Queue queue(){
        Map<String,Object> map=new HashMap<>();
        map.put("x-dead-letter-exchange","deadExchange");
        map.put("x-dead-letter-routing-key","test.key");
        Queue queue = new Queue("deadQueue",true,false,false,map);
        return queue;
    }
    @Bean
    public Queue queue1(){
        Queue queue = new Queue("deadQueue1",true,false,false,null);
        return queue;
    }
    @Bean
    public DirectExchange deadExchange(){
        DirectExchange directExchange = new DirectExchange("deadExchange");
        return directExchange;
    }
    @Bean
    public Binding binding(){
        return  BindingBuilder.bind( queue1()).to(deadExchange()).with("test.key");
    }


    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
        TomcatServletWebServerFactory tomcatServletWebServerFactory
                =new TomcatServletWebServerFactory(8081);
        return tomcatServletWebServerFactory;
    }
    //非单例ConfirmCallback
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //发送确认，发送成功
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println(ack);    //消息是否成功
                System.out.println(cause);  //失败原因
                System.out.println(correlationData);    //类似于业务id，就是new CorrelationData("订单ID")传入的参数
            }
        });

        //开启失败回调
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                //message : 发送的消息 "hello"+ 发送消息的配置(如消息过期时间等。。)
                System.out.println(message);
                //状态码
                System.out.println(replyCode);
                //失败信息
                System.out.println(replyText);
                System.out.println(exchange);
                System.out.println(routingKey);
            }
        });

        //还可以设置消息转换器
        /*rabbitTemplate.setMessageConverter(new MessageConverter() {
            //发送消息转换
            @Override
            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
                Message message = new Message(JSON.toJSONBytes(o),messageProperties);
                System.out.println("消息转换器");
                return message;
            }

            //接收消息转换
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                System.out.println("发送消息转换");
                return null;
            }
        });



        return rabbitTemplate;*/

         //避免乱码
        rabbitTemplate.setMessageConverter(new MessageConverter() {
            @Override
            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
                //避免乱码
                messageProperties.setContentType("text/xml");
                messageProperties.setContentEncoding("UTF-8");
                 Message message = new Message(JSON.toJSONBytes(o),messageProperties);
                System.out.println("调用了消息解析器");
                return message;
            }

            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                return null;
            }
        });
        return  rabbitTemplate;

    }
    /**
     * 设置备用交换机
     */
    @Bean
    public DirectExchange defaultExchange() {
        Map<String, Object> map = new HashMap<>();
        //声明备用交换机，已经存在的
        map.put("alternate-exchange","name");
        //如果交换机directExchangeTest2失败，将会调用备用交换机name
        return  new DirectExchange("directExchangeTest2",false,false,map);
    }
}
