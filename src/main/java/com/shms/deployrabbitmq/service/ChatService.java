package com.shms.deployrabbitmq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.shms.deployrabbitmq.Event.SendMessageEvent;
import com.shms.deployrabbitmq.pojo.ChatMessage;
import com.shms.deployrabbitmq.pojo.Result;
import com.shms.deployrabbitmq.pojo.User;
import lombok.Setter;
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

    private  ChatWebSocketClient webSocketClient;
    private final EventBusManager eventBus;

    public ChatService(EventBusManager eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this); // 注册事件总线监听器
    }
    /** 登录成功后初始化 WebSocket 客户端 */
    public void initWebSocketForUser(String username) throws Exception {
        if (webSocketClient != null && !webSocketClient.isClosed()) {
            webSocketClient.close();
        }
        webSocketClient = new ChatWebSocketClient(username, eventBus);
        webSocketClient.connect(); // 异步连接
    }
    /** 发送消息给服务器（WebSocket） */
    public void sendMessage(ChatMessage msg) {
            webSocketClient.sendChatMessage(msg);
            log.info("发送消息: {}", msg);
    }

    /** EventBus 监听发送事件 */
    @Subscribe
    public void handleSendMessageEvent(SendMessageEvent event) {
        ChatMessage msg = event.getMessage();
        sendMessage(msg); // 直接调用发送
    }


    @Value("${serverurl}")
    private String serverurl;
    private final RestTemplate restTemplate = new RestTemplate();


    // 测试服务器连接
    public Result testConnection(User user) {
        return restTemplate.postForObject(serverurl + "test", user, Result.class);
    }


    // 登录
    public Result login(User user) {
        return restTemplate.postForObject(serverurl + "login", user, Result.class);
    }

    // 注册
    public Result register(User user) {
        return restTemplate.postForObject(serverurl + "register", user, Result.class);
    }

    //logout
    public void logout(User user) {
        restTemplate.postForObject(serverurl + "logout", user, Result.class);
    }

    // 上传文件
    public String uploadFile(File file) {
        try {
            String Url = serverurl + "upload";
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));

            Result result = restTemplate.postForObject(Url, body, Result.class);
            return result.getCode() == 1 ? result.getData().toString() : null;
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
}



