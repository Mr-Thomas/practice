package rabbitmq.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rabbitmq.config.RabbitmqConfig;

/**
 * Created by Nancy on 2019/7/4 9:44
 */
@Slf4j
@Component
public class ReceiveHandler {

    //channel 原始方法的  通道
    //接受消息
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void send_email(String msg, Message message, Channel channel){
        log.info("接收消息-msg: {}",msg);
        log.info("接收消息-message: {}",message.getBody());
        log.info("接收消息-channel: {}",channel);
    }
}
