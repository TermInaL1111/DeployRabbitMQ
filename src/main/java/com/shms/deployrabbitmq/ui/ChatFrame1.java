//package com.shms.deployrabbitmq.ui;
//
//
//import com.google.common.eventbus.Subscribe;
//import com.shms.deployrabbitmq.Event.*;
//import com.shms.deployrabbitmq.pojo.ChatMessage;
//import com.shms.deployrabbitmq.pojo.Result;
//import com.shms.deployrabbitmq.pojo.User;
//import com.shms.deployrabbitmq.service.ChatMessageListener;
//import com.shms.deployrabbitmq.service.ChatService;
//import com.shms.deployrabbitmq.service.EventBusManager;
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import javax.swing.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@Slf4j
//@Component
//public class ChatFrame1 extends JFrame {
//
//    @Autowired
//    private EventBusManager eventBusManager;
//    @Autowired
//    private ChatService chatService;
//    @Autowired
//    private ChatMessageListener chatMessageListener;
//
//    private RestTemplate restTemplate = new RestTemplate();
//    private String url = "http://localhost:8090/user";
//
//    private JList<String> userList;
//    private DefaultListModel<String> model;
//    private JTextField usernameField, passwordField;
//
//    private String username, password;
//
//    // 存储每个用户的聊天窗口
//    private Map<String, ChatWindow> chatWindows = new HashMap<>();
//
//    public ChatFrame1() {
//        initComponents();
//      //  eventBusManager.register(this);
//    }
//
//    @PostConstruct
//    public void initAfterAutowired() {
//        // 在依赖注入完成后再注册事件总线
//        eventBusManager.register(this);
//        log.info("ChatFrame1 注册到 EventBus 成功");
//    }
//    private void initComponents() {
//        setTitle("jMessenger");
//        setSize(300, 400);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLayout(null);
//
//        JLabel userLabel = new JLabel("Username:");
//        userLabel.setBounds(10, 10, 80, 25);
//        add(userLabel);
//
//        usernameField = new JTextField();
//        usernameField.setBounds(100, 10, 160, 25);
//        add(usernameField);
//
//        JLabel passLabel = new JLabel("Password:");
//        passLabel.setBounds(10, 40, 80, 25);
//        add(passLabel);
//
//        passwordField = new JTextField();
//        passwordField.setBounds(100, 40, 160, 25);
//        add(passwordField);
//
//        JButton loginButton = new JButton("Login");
//        loginButton.setBounds(10, 70, 120, 25);
//        loginButton.addActionListener(e -> login());
//        add(loginButton);
//
//        JButton registerButton = new JButton("Register");
//        registerButton.setBounds(140, 70, 120, 25);
//        registerButton.addActionListener(e -> register());
//        add(registerButton);
//
//        model = new DefaultListModel<>();
//        userList = new JList<>(model);
//        JScrollPane scrollPane = new JScrollPane(userList);
//        scrollPane.setBounds(10, 110, 250, 240);
//        add(scrollPane);
//
//        // 点击用户列表打开聊天窗口
//        userList.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if(userList.getSelectedValue() != null){
//                    openChatWindow(userList.getSelectedValue());
//                }
//            }
//        });
//    }
//
//    private void login() {
//        username = usernameField.getText();
//        password = passwordField.getText();
//        if(username.isEmpty() || password.isEmpty()) return;
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        try {
//            Result result = restTemplate.postForObject(url + "/login", user, Result.class);
//            if(result == null || !result.getCode().equals(1)){
//                JOptionPane.showMessageDialog(this, "登录失败: " + (result != null ? result.getMsg() : "未知错误"), "错误", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            JOptionPane.showMessageDialog(this, "登录成功!");
//            // 启动消息监听
//            chatMessageListener.startPrivateMessageListener(username);
//            chatMessageListener.startPublicMessageListener(username);
//            chatMessageListener.startUserStatusListener(username);
//
//        } catch(Exception e){
//            JOptionPane.showMessageDialog(this, "服务器连接失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void register() {
//        username = usernameField.getText();
//        password = passwordField.getText();
//        if(username.isEmpty() || password.isEmpty()) return;
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        try {
//            Result result = restTemplate.postForObject(url + "/register", user, Result.class);
//            if(result == null || !result.getCode().equals(1)){
//                JOptionPane.showMessageDialog(this, "注册失败: " + (result != null ? result.getMsg() : "未知错误"), "错误", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            JOptionPane.showMessageDialog(this, "注册成功!");
//        } catch(Exception e){
//            JOptionPane.showMessageDialog(this, "注册失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void openChatWindow(String targetUser){
//        chatWindows.computeIfAbsent(targetUser, t -> {
//            ChatWindow w = new ChatWindow(t, msg -> sendPrivateMessage(t, String.valueOf(msg)));
//            w.setVisible(true);
//            return w;
//        });
//    }
//
//    private void sendPrivateMessage(String target, String msgText){
//        if(msgText.isEmpty()) return;
//
//        ChatMessage msg = new ChatMessage();
//        msg.setSender(username);
//        msg.setReceiver(target);
//        msg.setContent(msgText);
//        msg.setMessageId(UUID.randomUUID().toString());
//        msg.setType("message");
//        msg.setTimestamp(System.currentTimeMillis());
//
//        eventBusManager.post(new SendMessageEvent(msg));
//
//        chatWindows.get(target).appendMessage("[" + username + " > " + target + "] : " + msgText);
//    }
//
//    @Subscribe
//    public void onPrivateMessage(PrivateMessageEvent event){
//        ChatMessage msg = event.getMessage();
//        if(msg.getSender().equals(username)) return;
//
//        ChatWindow w = chatWindows.computeIfAbsent(msg.getSender(), t -> {
//            ChatWindow win = new ChatWindow(t, m -> sendPrivateMessage(t, String.valueOf(m)));
//            win.setVisible(true);
//            return win;
//        });
//
//        if("file".equals(msg.getType())){
//            w.appendMessage("[" + msg.getSender() + " > Me] : [File] " + msg.getContent());
//        } else {
//            w.appendMessage("[" + msg.getSender() + " > Me] : " + msg.getContent());
//        }
//    }
//
//    @Subscribe
//    public void onUserStatus(UserStatusEvent event){
//        ChatMessage msg = event.getMessage();
//        SwingUtilities.invokeLater(() -> {
//            if("online".equals(msg.getContent()) && !model.contains(msg.getSender())){
//                model.addElement(msg.getSender());
//            } else if("offline".equals(msg.getContent())){
//                model.removeElement(msg.getSender());
//            }
//        });
//    }
//
//}
