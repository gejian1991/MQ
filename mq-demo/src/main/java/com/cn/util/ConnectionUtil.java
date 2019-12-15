package com.cn.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {
    public final static String QUEUE_NAME = "testQueue";

    public final static String EXCHANGE_NAME = "exchange";

    public static Connection getConnection() throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.91.143");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_vhost");
        return factory.newConnection();
    }
}
