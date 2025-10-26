//package com.shms.deployrabbitmq.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.shms.deployrabbitmq.ChatMessageHandler;
//import com.shms.deployrabbitmq.pojo.ChatMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.swing.*;
//
//import java.nio.charset.StandardCharsets;
//
//@Service
//@Slf4j
//public class ChatListenerManager {
//
//    @Autowired
//    private ConnectionFactory connectionFactory; // Spring 的 RabbitMQ 连接工厂
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//
//    private SimpleMessageListenerContainer container;
//
//    // 登录成功后调用，动态创建队列并注册监听
//// 客户端 ChatListenerManager
//    public void registerUserQueueListener(String userId, ChatMessageHandler handler) {
//        String privateQueue = "queue_chat_user_" + userId;
//        String groupQueue = "queue_chat_all_" + userId;
//        String statusQueue = "queue_user_status_" + userId;
//
//        container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(privateQueue, groupQueue, statusQueue);
//
//        container.setMessageListener(message -> {
//            try {
//                byte[] body = message.getBody();
//                log.info("收到消息（JSON格式）：{}", new String(body, StandardCharsets.UTF_8)); // 打印JSON字符串
//
//                // 关键：用Jackson反序列化JSON为ChatMessage
//                ChatMessage chatMessage = objectMapper.readValue(body, ChatMessage.class);
//
//
//
//                    SwingUtilities.invokeLater(() -> {
//                      if ("status".equals(chatMessage.getType())) {
//                          log.info("Received user status: " + chatMessage);
//                        handler.onUserStatus(chatMessage);
//                    }
//                      else  if ("all".equals(chatMessage.getReceiver())) {
//                          log.info("Received group message: " + chatMessage);
//                        handler.onGroupMessage(chatMessage);
//                    } else if (userId.equals(chatMessage.getReceiver())) {
//                          log.info("Received private message: " + chatMessage);
//                        handler.onPrivateMessage(chatMessage);
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//
//        container.start();
//    }
//
//
//
//    // 登出或关闭时停止监听
//    public void stopListener() {
//        if(container != null) {
//            container.stop();
//        }
//    }
//
////    private ChatMessage deserialize(byte[] body) {
////        try (ByteArrayInputStream bais = new ByteArrayInputStream(body);
////             ObjectInputStream ois = new ObjectInputStream(bais)) {
////            return (ChatMessage) ois.readObject();
////        } catch (Exception e) {
////            e.printStackTrace();
////            return null;
////        }
////    }
//}
