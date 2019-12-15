package com.cn.consumer;

import com.cn.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Date;

public class Consumer {
    public static int i=0;
    public static void main(String[] args) throws Exception{
        getMessage();
    }
    public static void getMessage()throws Exception{
        Date date = new Date();
        Connection connection= ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //channel.queueDeclare(ConnectionUtil.QUEUE_NAME, false, false, false, null);
        //消费消息
        DefaultConsumer deliverCallback = new DefaultConsumer(channel) {
            //envelope，可以拿消息标识路由        properties,消息配置      body消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                /*System.out.println(new String(body, "UTF-8"));
                System.out.println("消息消费成功");*/

                i++;
                //消息确认          DeliveryTag消息的唯一标识，multiple是否批量确认
               // channel.basicAck(envelope.getDeliveryTag(),false);
                channel.basicAck(envelope.getDeliveryTag(),false);
                /*if(i%50==0||i==1000){
                    channel.basicAck(envelope.getDeliveryTag(),true);
                    System.out.println("耗时"+(new Date().getTime()-date.getTime()));
                }*/
            }
        };
        //开启消息预取，必须手动确认
        channel.basicQos(50);
        //开始消费
        channel.basicConsume(ConnectionUtil.QUEUE_NAME,  false,deliverCallback);
    }
}
