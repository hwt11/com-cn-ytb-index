package com.cn.red.point.o2c.rabbitmq.comsumer;

import com.alibaba.fastjson.JSONObject;
import com.cn.red.point.o2c.service.IndexService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

/**
 * 监听器.
 *
 * @author dax.
 * @version v1.0
 * @since 2018 /2/24 9:36
 */
@Component
public class Receiver {
    private static final Logger log= LoggerFactory.getLogger(Receiver.class);

    @Autowired
    private IndexService indexService;
    /**
     * FANOUT广播队列监听一.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = {"FANOUT_QUEUE_A"})
    public void on(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("FANOUT_QUEUE_A "+new String(message.getBody()));
    }

    /**
     * FANOUT广播队列监听二.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception   这里异常需要处理
     */
    @RabbitListener(queues = {"FANOUT_QUEUE_B"})
    public void t(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("FANOUT_QUEUE_B "+new String(message.getBody()));
    }

    /**
     * DIRECT模式.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = {"DIRECT_QUEUE"})
    public void message(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("DIRECT "+new String (message.getBody()));
    }

    /**
     * 监听替补队列 来验证死信.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = {"REDIRECT_QUEUE"})
    public void redirect(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("dead message 2 hours 后 消费消息 {}",new String (message.getBody()));
        Map map = JSONObject.parseObject(new String(message.getBody()),Map.class);
        indexService.cancelOrder(map);
    }
}
