package rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rabbitmq.config.RabbitmqConfig;

/**
 * Created by Nancy on 2019/7/4 9:20
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class producer_topics {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //使用rabbitTemplate发送消息

    /**
     * 参数
     * 1.交换机
     * 2.routingKey
     * 3.消息
     */
    @Test
    public void testSendEmail(){
        String message="send email message to user";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM,"inform.email",message);
    }
}
