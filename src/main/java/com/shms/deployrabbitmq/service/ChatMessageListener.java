package com.shms.deployrabbitmq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shms.deployrabbitmq.Event.PrivateMessageEvent;
import com.shms.deployrabbitmq.Event.PublicMessageEvent;
import com.shms.deployrabbitmq.Event.UserStatusEvent;
import com.shms.deployrabbitmq.pojo.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ChatMessageListener {

    @Autowired
    private EventBusManager eventBusManager;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private ChatService chatService; // 用于发送 ACK

//    @Autowired
//    private DynamicQueueUtil dynamicQueueUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SimpleMessageListenerContainer privateContainer;
    private SimpleMessageListenerContainer publicContainer;
    private SimpleMessageListenerContainer statusContainer;

    /**
     * 启动私信监听（用户登录后，监听自己队列）
     */
    public void startPrivateMessageListener(String userId) {
        privateContainer = new SimpleMessageListenerContainer();
        privateContainer.setConnectionFactory(connectionFactory);

        String privateQueue = "queue_chat_user_" + userId; // 动态队列
        privateContainer.setQueueNames(privateQueue);

        privateContainer.setMessageListener(message -> {
            try {
                ChatMessage msg = objectMapper.readValue(message.getBody(), ChatMessage.class);
                log.info("收到私信: {}", msg);

                // 发布事件给 UI
                eventBusManager.post(new PrivateMessageEvent(msg));

                // 自动 ACK
                chatService.ackMessage(msg.getMessageId());
            } catch (Exception e) {
                log.error("处理私信失败", e);
            }
        });

        privateContainer.start();
        log.info("私信监听启动完成, userId={}", userId);
    }

    /**
     * 启动群发消息监听（用户登录后，监听自己绑定的广播队列）
     */
    public void startPublicMessageListener(String userId) {
        publicContainer = new SimpleMessageListenerContainer();
        publicContainer.setConnectionFactory(connectionFactory);

        String publicQueue = "queue_chat_all_" + userId; // 动态队列
        publicContainer.setQueueNames(publicQueue);

        publicContainer.setMessageListener(message -> {
            try {
                ChatMessage msg = objectMapper.readValue(message.getBody(), ChatMessage.class);
                log.info("收到公共消息: {}", msg);

                // 发布事件给 UI
                eventBusManager.post(new PublicMessageEvent(msg));
            } catch (Exception e) {
                log.error("处理公共消息失败", e);
            }
        });

        publicContainer.start();
        log.info("公共消息监听启动完成, userId={}", userId);
    }

    /**
     * 启动用户状态监听（动态队列）
     */
    public void startUserStatusListener(String userId) {
        statusContainer = new SimpleMessageListenerContainer();
        statusContainer.setConnectionFactory(connectionFactory);

        String statusQueue = "queue_user_status_" + userId; // 动态队列
        statusContainer.setQueueNames(statusQueue);

        statusContainer.setMessageListener(message -> handleUserStatusMessage(message));

        statusContainer.start();
        log.info("用户状态监听启动完成, userId={}", userId);
    }

    private void handleUserStatusMessage(org.springframework.amqp.core.Message message) {
        try {
            ChatMessage msg = objectMapper.readValue(message.getBody(), ChatMessage.class);
            log.info("收到用户状态: {}", msg);

            // 发布事件给 UI
            eventBusManager.post(new UserStatusEvent(msg));
        } catch (Exception e) {
            log.error("处理用户状态消息失败", e);
        }
    }

    /**
     * 停止所有监听
     */
    public void stopAllListeners() {
        if (privateContainer != null) privateContainer.stop();
        if (publicContainer != null) publicContainer.stop();
        if (statusContainer != null) statusContainer.stop();
        log.info("所有消息监听已停止");
    }
}



//@Component
//@Slf4j
//public class ChatMessageListener {
//
//    @Autowired
//    private EventBusManager eventBusManager;
//
//    @Autowired
//    private ConnectionFactory connectionFactory;
//
//    @Autowired
//    private ChatService chatService; // 用于发送 ACK
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    private SimpleMessageListenerContainer privateContainer;
//    private SimpleMessageListenerContainer statusContainer;
//    private SimpleMessageListenerContainer publicContainer;
//
//    /**
//     * 启动私信监听（原 queue_chat_private）
//     */
//    public void startPrivateMessageListener() {
//        privateContainer = new SimpleMessageListenerContainer();
//        privateContainer.setConnectionFactory(connectionFactory);
//        privateContainer.setQueueNames("queue_chat_private");
//
//        privateContainer.setMessageListener(message -> {
//            try {
//                ChatMessage msg = objectMapper.readValue(message.getBody(), ChatMessage.class);
//                log.info("收到私信: {}", msg);
//
//                // 发布事件给 UI
//                eventBusManager.post(new PrivateMessageEvent(msg));
//
//                // 自动 ACK
//                chatService.ackMessage(msg.getMessageId());
//            } catch (Exception e) {
//                log.error("处理私信失败", e);
//            }
//        });
//
//        privateContainer.start();
//        log.info("私信监听启动完成");
//    }
//    /**
//     * 公共消息监听（广播给所有用户）
//     */
//    private void startPublicMessageListener() {
//        publicContainer = new SimpleMessageListenerContainer();
//        publicContainer.setConnectionFactory(connectionFactory);
//        publicContainer.setQueueNames("queue_chat_public");
//
//        publicContainer.setMessageListener(message -> {
//            try {
//                ChatMessage msg = objectMapper.readValue(message.getBody(), ChatMessage.class);
//                log.info("收到公共消息: {}", msg);
//
//                eventBusManager.post(new PublicMessageEvent(msg));
//            } catch (Exception e) {
//                log.error("处理公共消息失败", e);
//            }
//        });
//
//        publicContainer.start();
//        log.info("公共消息监听启动完成");
//    }
//
//    /**
//     * 启动用户状态监听（动态队列）
//     * 每个用户只监听自己相关的队列
//     *
//     * @param userId 当前登录用户
//     */
//    public void startUserStatusListener(String userId) {
//        statusContainer = new SimpleMessageListenerContainer();
//        statusContainer.setConnectionFactory(connectionFactory);
//
//        // 只监听自己的状态队列
//        String queue = "queue_user_status_" + userId;
//        statusContainer.setQueueNames(queue);
//
//        statusContainer.setMessageListener(message -> handleUserStatusMessage(message));
//
//        statusContainer.start();
//        log.info("用户状态监听启动完成, userId={}", userId);
//    }
//
//    /**
//     * 处理用户状态消息
//     */
//    private void handleUserStatusMessage(org.springframework.amqp.core.Message message) {
//        try {
//            ChatMessage msg = objectMapper.readValue(message.getBody(), ChatMessage.class);
//            log.info("收到用户状态: {}", msg);
//
//            // 发布事件给 UI
//            eventBusManager.post(new UserStatusEvent(msg));
//        } catch (Exception e) {
//            log.error("处理用户状态消息失败", e);
//        }
//    }
//
//    /**
//     * 停止所有监听
//     */
//    public void stopAllListeners() {
//        if (privateContainer != null) privateContainer.stop();
//        if (publicContainer != null) publicContainer.stop();
//        if (statusContainer != null) statusContainer.stop();
//        log.info("所有消息监听已停止");
//    }
//}




