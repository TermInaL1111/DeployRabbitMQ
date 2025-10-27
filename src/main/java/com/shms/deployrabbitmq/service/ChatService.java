package com.shms.deployrabbitmq.service;

import com.shms.deployrabbitmq.Event.SendMessageEvent;
import com.shms.deployrabbitmq.pojo.ChatMessage;
import com.shms.deployrabbitmq.pojo.Result;
import com.shms.deployrabbitmq.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


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
    //mq
    // 发送上线状态
    public void sendOnlineStatus(String username, EventBusManager eventBus) {
        ChatMessage msg = createStatusMessage(username, "online");
        eventBus.post(new SendMessageEvent(msg));
    }

    // 发送下线状态
    public void sendOfflineStatus(String username, EventBusManager eventBus) {
        ChatMessage msg = createStatusMessage(username, "offline");
        eventBus.post(new SendMessageEvent(msg));
    }

    // 创建状态消息
    private ChatMessage createStatusMessage(String username, String status) {
        ChatMessage msg = new ChatMessage();
        msg.setMessageId(UUID.randomUUID().toString());
        msg.setType("status");
        msg.setSender(username);
        msg.setReceiver("all");
        msg.setContent(status);
        msg.setTimestamp(System.currentTimeMillis());
        return msg;
    }
    @Value("${serverurl}")
    private String serverurl;
    private final RestTemplate restTemplate = new RestTemplate();

    // 测试服务器连接
    public Result testConnection(String serverUrl, User user) {
        return restTemplate.postForObject(serverurl + "test", user, Result.class);
    }

    // 登录
    public Result login(String serverUrl, User user) {
        return restTemplate.postForObject(serverurl + "login", user, Result.class);
    }

    // 注册
    public Result register(String serverUrl, User user) {
        return restTemplate.postForObject(serverurl + "register", user, Result.class);
    }
    //logout
    public void logout(User  user){
        restTemplate.postForObject(serverurl+"logout",user,Result.class);
    }

    // 上传文件
    public String uploadFile(File file) {
        try {
            String Url = serverurl+ "upload"; // 实际应从配置获取
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));

            Result result = restTemplate.postForObject(Url, body, Result.class);
            return result.getCode()==1 ? result.getData().toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    // 下载文件
    public void downloadFile(URI fileUrl, File saveFile) throws Exception {
        ResponseEntity<Resource> response = restTemplate.exchange(
                fileUrl, HttpMethod.GET, null, Resource.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try (InputStream in = response.getBody().getInputStream()) {
                Files.copy(in, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
    // 获取当前在线用户列表
    public Result getOnlineUsers() {
        return restTemplate.getForObject(serverurl + "online", Result.class);
    }
    //此时RestTemplate无法识别data是List<String>，会默认将其解析为List<LinkedHashMap>
    // //（因为 JSON 数组中的字符串会被错误映射），导致(List<String>) res.getData()转换失败。
    // 在ChatService中添加方法
//    public Result getOnlineUsers() {
//        RestTemplate restTemplate = new RestTemplate();
//        // 用ParameterizedTypeReference指定Result<List<String>>类型
//        ResponseEntity response = restTemplate.exchange(
//                serverurl + "/online",  // 后端获取在线用户的接口地址
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<Result<List<String>>>() {}
//        );
//        return response.getBody();
//    }


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
