package com.shms.deployrabbitmq;

import com.shms.deployrabbitmq.service.ChatListenerManager;
import com.shms.deployrabbitmq.service.ChatService;
import com.shms.deployrabbitmq.ui.ChatFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

    @Autowired
    private ChatListenerManager listenerManager;

    @Autowired
    private ChatService chatService;

    public static void main(String[] args) {
        // 关闭 headless 模式，允许 Swing 窗口显示
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(DeployRabbitMqApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // 启动 Swing UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            ChatFrame frame = new ChatFrame(listenerManager, chatService);
            frame.setVisible(true);
        });
    }
}
