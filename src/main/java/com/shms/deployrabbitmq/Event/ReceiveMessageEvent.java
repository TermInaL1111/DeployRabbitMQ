package com.shms.deployrabbitmq.Event;

import com.shms.deployrabbitmq.pojo.ChatMessage;

public class ReceiveMessageEvent {

        private final ChatMessage message;

        public ReceiveMessageEvent(ChatMessage message) {
            this.message = message;
        }

        public ChatMessage getMessage() {
            return message;
        }
    }
