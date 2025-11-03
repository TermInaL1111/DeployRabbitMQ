package com.shms.deployrabbitmq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shms.deployrabbitmq.Event.ReceiveMessageEvent;
import com.shms.deployrabbitmq.Event.WsEvent;
import com.shms.deployrabbitmq.pojo.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

@Slf4j
public class ChatWebSocketClient extends WebSocketClient {

    private final EventBusManager eventBusManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatWebSocketClient(String username, EventBusManager eventBusManager) throws Exception {
        super(new URI("ws://8.137.54.50:8090/ws/chat?username=" + username));
        this.eventBusManager = eventBusManager;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("✅ WebSocket连接成功");
        eventBusManager.post(new WsEvent("open", "连接成功"));
    }

    @Override
    public void onMessage(String message) {
        try {
            // 解析 JSON 为 ChatMessage
            log.info("收到websocket{}",message);
            ChatMessage chatMsg = objectMapper.readValue(message, ChatMessage.class);
            eventBusManager.post( new ReceiveMessageEvent(chatMsg));  // 直接把 ChatMessage 发到事件总线
        } catch (Exception e) {
            System.err.println("⚠ 消息解析失败: " + e.getMessage());
            // 如果不是 ChatMessage，可以当作普通文本事件发出
            eventBusManager.post(new WsEvent("message", message));
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println(" 连接关闭: " + reason);
        eventBusManager.post(new WsEvent("close", reason));
    }

    @Override
    public void onError(Exception ex) {
        System.out.println(" 出错: " + ex.getMessage());
        eventBusManager.post(new WsEvent("error", ex.getMessage()));
    }

    public void sendText(String msg) {
        send(msg);
    }

    // 发送 ChatMessage 对象
    public void sendChatMessage(ChatMessage chatMessage) {
        try {
            String json = objectMapper.writeValueAsString(chatMessage);
            log.info("发送websocket{}",json);
            send(json);
        } catch (Exception e) {
            System.err.println(" ChatMessage 发送失败: " + e.getMessage());
        }
    }
}
