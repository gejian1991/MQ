package com;

import com.luban.config.RabbitmqConfig;
import com.luban.util.RabbitmqMessageSend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@SpringBootApplication
public class AppMqSend {

    public static void main(String[] args) {
        SpringApplication.run(AppMqSend.class);

        /**
         * 模拟发送失败
         */
       /* AnnotationConfigApplicationContext
                annotationConfigApplicationContext=new AnnotationConfigApplicationContext(RabbitmqConfig.class);
        RabbitmqMessageSend bean = annotationConfigApplicationContext.getBean(RabbitmqMessageSend.class);
        bean.sendMessage();
        annotationConfigApplicationContext.close();*/
    }
}
