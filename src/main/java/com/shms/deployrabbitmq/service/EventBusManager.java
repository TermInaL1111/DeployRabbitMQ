package com.shms.deployrabbitmq.service;

import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

//事件总线管理器
@Component
public class EventBusManager {

    private final EventBus eventBus = new EventBus();

    public void register(Object listener) {
        eventBus.register(listener);
    }

    public void unregister(Object listener) {
        eventBus.unregister(listener);
    }

    public void post(Object event) {
        eventBus.post(event);
    }
}
