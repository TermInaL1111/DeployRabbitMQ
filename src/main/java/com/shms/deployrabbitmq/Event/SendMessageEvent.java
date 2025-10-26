package com.shms.deployrabbitmq.Event;

import com.shms.deployrabbitmq.pojo.ChatMessage;

// 客户端发消息事件
public class SendMessageEvent {
    private final ChatMessage message;

    public SendMessageEvent(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
