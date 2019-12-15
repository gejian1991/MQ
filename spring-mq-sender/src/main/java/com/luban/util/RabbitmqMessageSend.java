package com.luban.util;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@Component
public class RabbitmqMessageSend {

    @Autowired
    RabbitTemplate rabbitTemplate;

    //有多个Callback，使用不同的calback
    @PostConstruct
    /*public void init(){
        rabbitTemplate.setConfirmCallback();
    }*/

    public void  sendMessage(){
        CorrelationData correlationData = new CorrelationData("订单ID");
        //direct.key
        Map<String,Object> map = new HashMap<>();
        map.put("name","123");
        map.put("password","123456");
        rabbitTemplate.convertAndSend("", "deadQueue", map,correlationData);
    }

}
