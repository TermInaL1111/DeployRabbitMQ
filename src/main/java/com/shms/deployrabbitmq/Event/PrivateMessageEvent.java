package com.shms.deployrabbitmq.Event;

import com.shms.deployrabbitmq.pojo.ChatMessage;

// 私信事件
public class PrivateMessageEvent {
    private final ChatMessage message;

    public PrivateMessageEvent(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }
}