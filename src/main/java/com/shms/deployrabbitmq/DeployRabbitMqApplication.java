package com.shms.deployrabbitmq;


import com.shms.deployrabbitmq.service.ChatMessageListener;
import com.shms.deployrabbitmq.service.ChatService;
import com.shms.deployrabbitmq.service.EventBusManager;
import com.shms.deployrabbitmq.ui.ChatFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

//@SpringBootApplication
//public class DeployRabbitMqApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(DeployRabbitMqApplication.class, args);
//    }
//
//}
@SpringBootApplication
public class DeployRabbitMqApplication implements CommandLineRunner {

//    @Autowired
//    private ChatListenerManager listenerManager;

    @Autowired
    private ChatService chatService;
    @Autowired
    private EventBusManager eventBusManager;
     @Autowired
    private ChatMessageListener chatMessageListener;

    public static void main(String[] args) {
        // 关闭 headless 模式，允许 Swing 窗口显示
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(DeployRabbitMqApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // 设置系统 LookAndFeel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // 启动 Swing UI
        SwingUtilities.invokeLater(() -> {
            ChatFrame chatFrame = new ChatFrame(eventBusManager,chatService,chatMessageListener);
            chatFrame.setVisible(true);
        });
    }
}
