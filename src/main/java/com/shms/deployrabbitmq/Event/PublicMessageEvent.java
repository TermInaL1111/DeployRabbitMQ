package com.shms.deployrabbitmq.Event;

import com.shms.deployrabbitmq.pojo.ChatMessage;

public class PublicMessageEvent {
    private final ChatMessage message;

    public PublicMessageEvent(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
