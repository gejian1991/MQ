package com.cn.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {
    public final static String QUEUE_NAME = "testQueue";

    public final static String EXCHANGE_NAME = "exchange";

    public static Connection getConnection() throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("test");
        factory.setPassword("test");
        factory.setVirtualHost("testhost");
        return factory.newConnection();
    }
}
