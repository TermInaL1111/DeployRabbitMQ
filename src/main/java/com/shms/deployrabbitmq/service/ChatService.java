package com.shms.deployrabbitmq.service;

import com.shms.deployrabbitmq.pojo.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@Slf4j
public class ChatService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendPrivateMessage(ChatMessage msg) {
        // 发送到私信交换机
        rabbitTemplate.convertAndSend("chat_topic_exchange", "chat.private." + msg.getReceiver(), msg);
        log.info("已发送消息给 {}: {}", msg.getReceiver(), msg.getContent());
    }
    public void ackMessage(String messageId) {
        log.info("ACK 消息: {}", messageId);
        // TODO: 可更新数据库状态
    }
}



//@Service
//public class ChatService {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    // 群发消息
//    public void sendToAll(ChatMessage msg) {
//        rabbitTemplate.convertAndSend("chat_fanout_exchange", msg);
//    }
//
//    // 私发消息
//    public void sendToUser(ChatMessage msg) {
//        // 路由键规则：chat.user.用户ID
//        String routeKey = "chat.user." + msg.getReceiver();
//        rabbitTemplate.convertAndSend("chat_topic_exchange", routeKey, msg);
//    }
//
//    // 群发 上线 / 下线   目前
//    public void sendUserStatus(ChatMessage msg) {
//
//        rabbitTemplate.convertAndSend("status_fanout_exchange", msg);
//    }
//
//
//    // 发送文件（文件转为字节数组）
//    public void sendFile(String sender, String receiver, MultipartFile file) throws IOException {
//        ChatMessage msg = new ChatMessage();
//        msg.setType("file");
//        msg.setSender(sender);
//        msg.setReceiver(receiver);
//        msg.setContent(file.getOriginalFilename());
//        msg.setFileData(file.getBytes());
//        msg.setTimestamp(System.currentTimeMillis());
//        sendToUser(msg);
//    }
//
//}
