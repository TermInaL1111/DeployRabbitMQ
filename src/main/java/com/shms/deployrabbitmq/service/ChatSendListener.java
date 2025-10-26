package com.shms.deployrabbitmq.service;

import com.google.common.eventbus.Subscribe;
import com.shms.deployrabbitmq.Event.SendMessageEvent;
import com.shms.deployrabbitmq.pojo.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.Objects;

//监听事件总线中的 SendMessageEvent，将消息发送到 RabbitMQ。
@Component
@Slf4j
public class ChatSendListener {

    @Autowired
    private EventBusManager eventBusManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 注册到事件总线
    @PostConstruct
    public void init() {
        eventBusManager.register(this);
    }

    // 监听 UI 发布的发送事件
    @Subscribe
    public void handleSendMessage(SendMessageEvent event) {
        ChatMessage msg = event.getMessage();

        // 路由键规则: chat.user.<receiverId>
        String routingKey = "chat.user." + msg.getReceiver();

        //    System.out.println("准备发送消息到 MQ: " + routingKey + " 内容: " + msg.getContent());
        int type = 0;
        if (Objects.equals(msg.getType(), "status")) type = 3;
        else{
            if(Objects.equals(msg.getSender(), "all") || Objects.equals(msg.getReceiver(), "all")) type = 2;
            else type=1;
        }

        // 发送到 Topic 交换机
        switch (type) {
            case 1:
                // String routingKey = "chat.user." + msg.getReceiver();
                System.out.println("发送私信到 MQ: " + routingKey);
                rabbitTemplate.convertAndSend("chat_topic_exchange", routingKey, msg);
                break;

            case 2: // 群发消息
                System.out.println("发送公共消息到 MQ");
                rabbitTemplate.convertAndSend("chat_fanout_exchange", "", msg);
                break;

            case 3: // 用户状态
                System.out.println("发送状态消息到 MQ");
                rabbitTemplate.convertAndSend("status_fanout_exchange", "", msg);
                break;

            default:
                log.warn("未知消息类型: {}", msg.getType());
        }
    }

}
