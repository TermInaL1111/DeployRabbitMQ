package com.shms.deployrabbitmq.Event;

public class WsEvent {
    private final String type;   // open, message, close, error
    private final String payload;

    public WsEvent(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() { return type; }
    public String getPayload() { return payload; }

    @Override
    public String toString() {
        return "[" + type + "] " + payload;
    }
}

