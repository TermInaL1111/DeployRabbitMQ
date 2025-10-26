package com.shms.deployrabbitmq.Event;

import com.shms.deployrabbitmq.pojo.ChatMessage;

// 用户状态事件（上线/下线）
public class UserStatusEvent {
    private final ChatMessage statusMessage;

    public UserStatusEvent(ChatMessage statusMessage) {
        this.statusMessage = statusMessage;
    }

    public ChatMessage getMessage() {
        return statusMessage;
    }
}