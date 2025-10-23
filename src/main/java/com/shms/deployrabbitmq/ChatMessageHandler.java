package com.shms.deployrabbitmq;

import com.shms.deployrabbitmq.pojo.ChatMessage;

public interface ChatMessageHandler {
    void onPrivateMessage(ChatMessage msg);
    void onGroupMessage(ChatMessage msg);
    void onUserStatus(ChatMessage msg);
}
