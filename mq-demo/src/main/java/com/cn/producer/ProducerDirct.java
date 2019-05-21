package com.cn.producer;

import com.cn.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class ProducerDirct {

    public static void main(String[] argv) throws Exception {
        Connection  connection=ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //队列允许持久化，队列可以其他channel调用，没有任何消费者消费时自动删除队列，队列配置（消息寿命，容纳消息数量等信息）
        //channel.queueDeclare(ConnectionUtil.QUEUE_NAME, true, false, false, null);
        channel.exchangeDelete("exchangeTest");
        channel.exchangeDeclare("exchangeTest", BuiltinExchangeType.DIRECT);
        channel.queueBind("queue1","exchangeTest","info.user");
        channel.queueBind("queue2","exchangeTest","error.user");
        channel.queueBind("queue3","exchangeTest","debug.user");
        String message = "Hello World!";
        //默认路由，队列名，消息配置先为null
        //只有队列一可以消费
        channel.basicPublish("exchangeTest", "info.user", null, message.getBytes());
        channel.close();
        System.out.println(" Sent '" + message + "'");
    }
}
