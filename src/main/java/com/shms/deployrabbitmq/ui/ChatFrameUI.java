//package com.shms.deployrabbitmq.ui;
//
//import com.google.common.eventbus.Subscribe;
//import com.shms.deployrabbitmq.Event.*;
//import com.shms.deployrabbitmq.pojo.ChatMessage;
//import com.shms.deployrabbitmq.service.EventBusManager;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import jakarta.annotation.PostConstruct;
//import javax.swing.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.File;
//
//@Slf4j
//@Component
//public class ChatFrameUI extends JFrame {
//
//    @Autowired
//    private EventBusManager eventBusManager;
//
//    private String username;
//    private String password;
//    private DefaultListModel<String> model = new DefaultListModel<>();
//    private File file;
//    private String historyFile = "D:/History.xml";
//
//    // Swing 组件
//    private JTextArea chatArea = new JTextArea();
//    private JTextField messageField = new JTextField();
//    private JList<String> userList = new JList<>(model);
//    private JButton sendButton = new JButton("发送");
//
//    @PostConstruct
//    public void init() {
//        setTitle("事件总线版 jMessenger");
//        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // 注册事件监听
//        eventBusManager.register(this);
//
//        // UI 布局
//        JScrollPane scrollPane = new JScrollPane(chatArea);
//        add(scrollPane, "Center");
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.add(messageField);
//        bottomPanel.add(sendButton);
//        add(bottomPanel, "South");
//
//        sendButton.addActionListener(e -> onSendButtonClick());
//
//        // 窗口关闭事件：发送离线状态
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                ChatMessage msg = new ChatMessage();
//                msg.setType("status");
//                msg.setSender(username);
//                msg.setReceiver("all");
//                msg.setContent("OFFLINE");
//                msg.setTimestamp(System.currentTimeMillis());
//                eventBusManager.post(new UserStatusEvent(msg));
//            }
//        });
//
//        setVisible(true);
//    }
//
//    // 用户登录（可从登录界面或输入框触发）
//    public void login(String username, String password) {
//        this.username = username;
//        this.password = password;
//
//        ChatMessage online = new ChatMessage();
//        online.setType("status");
//        online.setSender(username);
//        online.setReceiver("all");
//        online.setContent("ONLINE");
//        online.setTimestamp(System.currentTimeMillis());
//
//        eventBusManager.post(new UserStatusEvent(online));
//    }
//
//    // 点击发送消息按钮
//    private void onSendButtonClick() {
//        String content = messageField.getText().trim();
//        String target = userList.getSelectedValue();
//
//        if (content.isEmpty() || target == null) return;
//
//        ChatMessage msg = new ChatMessage();
//        msg.setType("message");
//        msg.setSender(username);
//        msg.setReceiver(target);
//        msg.setContent(content);
//        msg.setTimestamp(System.currentTimeMillis());
//
//        // 发布事件，由 ChatSendListener 负责发送 MQ
//        eventBusManager.post(new SendMessageEvent(msg));
//
//        chatArea.append("[" + username + " > " + target + "] : " + content + "\n");
//        messageField.setText("");
//    }
//
//    // 收到私信事件（来自 MQ → ChatMessageListener → EventBus）
//    @Subscribe
//    public void handlePrivateMessage(PrivateMessageEvent event) {
//        ChatMessage msg = event.getMessage();
//        chatArea.append("[" + msg.getSender() + " > Me] : " + msg.getContent() + "\n");
//    }
//
//    // 收到用户上下线事件
//    @Subscribe
//    public void handleUserStatus(UserStatusEvent event) {
//        ChatMessage msg = event.getStatusMessage();
//        if ("ONLINE".equals(msg.getContent())) {
//            if (!model.contains(msg.getSender())) model.addElement(msg.getSender());
//        } else if ("OFFLINE".equals(msg.getContent())) {
//            model.removeElement(msg.getSender());
//        }
//    }
//}
