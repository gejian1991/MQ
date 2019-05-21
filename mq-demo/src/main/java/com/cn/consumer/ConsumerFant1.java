package com.cn.consumer;

import com.cn.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class ConsumerFant1 {
    public static void main(String[] args) throws Exception{
        getMessage();
    }


    public static void getMessage()throws Exception{
        Connection connection= ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("queue1", false, false, false, null);

        //消费消息
        DefaultConsumer deliverCallback = new DefaultConsumer(channel) {
            //envelope，可以拿消息标识路由        properties,消息配置      body消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "UTF-8"));

                System.out.println("消息消费成功");
                //消息确认          DeliveryTag消息的标识，multiple是否批量确认
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //开始消费
        channel.basicConsume("queue1",  deliverCallback);
    }
}
