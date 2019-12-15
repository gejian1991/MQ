package com.cn.util;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;


@Component
public class RabbitmqUtil {



    //两个不同的消费者监听同一队列
    //containerFactory的值默认beanName
    @RabbitListener(queues = "testQueue",containerFactory = "simpleRabbitListenerContainerFactory")
    public void get(Message message, Channel channel) throws Exception{
        System.out.println(new String(message.getBody(),"UTF-8"));
        //下单成功
        if(placeAnOrder()){
            //第二个boolean参数指定是不是批量处理的
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);//非批量确认
        //下单失败
        }else{
            //消息退回队列 ，前两个参数 和上面的意义一样， 最后一个参数 就是这条消息是返回到原队列 还是这条消息作废 就是不退回了。
            //批量退回，一般不用单条退回
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            //单条退回
            //channel.basicReject();
        }

        //JSON.parse(new String(message.getBody(),);
        System.out.println("消费者1退回");
    }

    public boolean placeAnOrder(){
        return false;
    }

    //监听相同队列，默认以轮询方式接收，会有乱码
   /* @RabbitListener(queues = "testQueue")
    public  void  get1(String message) throws UnsupportedEncodingException {
        System.out.println(message);
        System.out.println("消费者2");
    }*/

    @RabbitListener(queues = "deadQueue",containerFactory = "simpleRabbitListenerContainerFactory")
    public  void  get2(Message message,Channel channel) throws Exception {
        System.out.println(message);
        channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        System.out.println("deadQueue拒绝 拒绝了消息");
    }
    @RabbitListener(queues = "deadQueue1",containerFactory = "simpleRabbitListenerContainerFactory")
    public  void  get3(Message message,Channel channel) throws Exception {
        System.out.println(message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        System.out.println("deadQueue1接收了死信交换机消息");
    }
}
