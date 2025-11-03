package com.shms.deployrabbitmq.ui;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.shms.deployrabbitmq.Event.ReceiveMessageEvent;
import com.shms.deployrabbitmq.pojo.ChatMessage;
import com.shms.deployrabbitmq.pojo.Result;
import com.shms.deployrabbitmq.pojo.User;

import com.shms.deployrabbitmq.service.EventBusManager;
import com.shms.deployrabbitmq.service.ChatService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MainFrame extends JFrame {
    private final String currentUser;
    private final EventBusManager eventBusManager;
    private final ChatService chatService;
    private final DefaultListModel<String> userListModel;
    private final Map<String, ChatWindow> chatWindows = new HashMap<>(); // 管理已打开的聊天窗口

//    private final ChatMessageListener chatMessageListener;

    private boolean isLoggedOut = false; // 新增标志位

    public MainFrame(String currentUser, EventBusManager eventBusManager, ChatService chatService) {
        this.currentUser = currentUser;
        this.eventBusManager = eventBusManager;
        this.chatService = chatService;
        this.userListModel = new DefaultListModel<>();
//        this.chatMessageListener = chatMessageListener;

        // 初始化用户列表(默认包含自己和all)
        initUserList();

        // 注册事件
        eventBusManager.register(this);

        initUI();
        setTitle("主界面 - " + currentUser);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 500);
        setLocationRelativeTo(null);
    }

    private void initUserList() {
        userListModel.addElement("all"); // 群聊
        userListModel.addElement(currentUser); // 自己
        // 主动获取当前在线用户（补充MQ未收到的历史状态）
        Result res = chatService.getOnlineUsers();
        if (res != null && res.getCode() == 1) {
            Object data = res.getData();
            if (data != null) {
                try {
                    // 用Jackson将data转换为List<String>
                    //jszhe
                    ObjectMapper mapper = new ObjectMapper();
                    List<String> onlineUsers = mapper.convertValue(
                            data,
                            new TypeReference<List<String>>() {} // 明确目标类型
                    );
                    // 遍历添加在线用户
                    for (String user : onlineUsers) {
                        if (!user.equals(currentUser) && !userListModel.contains(user)) {
                            userListModel.addElement(user);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    log.error("在线用户列表类型转换失败", e);
                    JOptionPane.showMessageDialog(this, "获取在线用户失败");
                }
            }
        } else {
            log.info("没有在线用户");
        }
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // 顶部显示当前用户
        // 顶部：当前用户信息 + 登出按钮
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel("当前账号: " + currentUser, SwingConstants.CENTER);
        panel.add(userLabel, BorderLayout.NORTH);
        JButton logoutBtn = new JButton("登出");
        logoutBtn.addActionListener(e -> doLogout()); // 绑定登出事件
        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH); // 添加到顶部
        // 中间显示在线用户列表
        JList<String> userList = new JList<>(userListModel);
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 双击打开聊天窗口
                    String targetUser = userList.getSelectedValue();
                    openChatWindow(targetUser);
                }
            }
        });
        panel.add(new JScrollPane(userList), BorderLayout.CENTER);

        add(panel);
    }

    // 打开聊天窗口(已存在则激活，不存在则创建)
    private void openChatWindow(String targetUser) {
        if (targetUser == null) return;

        ChatWindow chatWindow = chatWindows.get(targetUser);
        if (chatWindow == null) {
            chatWindow = new ChatWindow(currentUser, targetUser, eventBusManager, chatService);
            chatWindows.put(targetUser, chatWindow);
            // 窗口关闭时从map移除
            chatWindow.addWindowListener(() -> chatWindows.remove(targetUser));
        }
        chatWindow.setVisible(true);
        chatWindow.toFront(); // 置顶
    }

//    //订阅
@Subscribe
public void handleChatMessage(ReceiveMessageEvent event) {
        ChatMessage msg = event.getMessage();
        log.info("开始处理{}",msg);
    SwingUtilities.invokeLater(() -> {
        switch (msg.getType()) {
            case "status": // 用户状态
                if ("online".equals(msg.getContent()) && !userListModel.contains(msg.getSender())) {
                    userListModel.addElement(msg.getSender());
                } else if ("offline".equals(msg.getContent())) {
                    userListModel.removeElement(msg.getSender());
                }
                break;

            case "text": // 私聊
                if (msg.getReceiver().equals(currentUser)) {
                    openChatWindow(msg.getSender());
                    ChatWindow chatWindow = chatWindows.get(msg.getSender());
                    if (chatWindow != null) chatWindow.handleMessage(msg);
                }
                else if(msg.getReceiver().equals("all"))
                {
                    openChatWindow("all");
                    ChatWindow chatWindow = chatWindows.get("all");
                    if (chatWindow != null) chatWindow.handleMessage(msg);
                }
                break;

//            case "group": // 群聊
//                if (msg.getReceiver().equals("all") || msg.getReceiver().equals(currentUser)) {
//                    openChatWindow(msg.getSender());
//                    ChatWindow chatWindow = chatWindows.get(msg.getSender());
//                    if (chatWindow != null) chatWindow.handleMessage(msg);
//                }
//                break;

            default:
                System.out.println("未知消息类型: " + msg.getType());
        }
    });
}



        private void doLogout() {
            // 若已登出，直接返回，避免重复执行
            if (isLoggedOut) {
                return;
            }
            try {
                // 1. 标记为已登出
                isLoggedOut = true;

                // 2. 发送下线状态
//                chatService.sendOfflineStatus(currentUser, eventBusManager);
                User user = new User();
                user.setUsername(currentUser);
                chatService.logout(user);
                // 3. 停止消息监听器
//                chatMessageListener.stopAllListeners();

                // 4. 注销事件总线（仅执行一次）
                eventBusManager.unregister(this);

                // 5. 关闭所有聊天窗口
                for (ChatWindow window : chatWindows.values()) {
                    window.dispose();
                }
                chatWindows.clear();

                // 6. 关闭当前主窗口，打开登录窗口
                this.dispose(); // 调用dispose，但此时isLoggedOut为true，不会再次触发doLogout
                LoginFrame loginFrame = new LoginFrame(eventBusManager, chatService);
                loginFrame.setVisible(true);

                log.info("用户 {} 已登出", currentUser);
            } catch (Exception e) {
                log.error("登出失败", e);
                JOptionPane.showMessageDialog(this, "登出异常: " + e.getMessage());
            }
        }

        // 修正dispose方法，避免再次调用doLogout
        @Override
        public void dispose() {
            // 仅在未登出时执行兜底逻辑（正常登出后不会触发）
            if (!isLoggedOut) {
                doLogout(); // 若用户直接关闭窗口，触发登出
            } else {
                super.dispose(); // 已登出，直接关闭窗口
            }
        }
//    @Subscribe
//    public void handleWsEvent(WsEvent event) {
//        SwingUtilities.invokeLater(() -> {
//            switch (event.getType()) {
//                case "open":
//                    statusLabel.setText("✅ 已连接服务器");
//                    connectBtn.setEnabled(false);
//                    break;
//
//                case "close":
//                    statusLabel.setText("🔴 连接关闭: " + event.getPayload());
//                    connectBtn.setEnabled(true);
//                    // 可以弹窗提示或尝试重连
//                    int option = JOptionPane.showConfirmDialog(this,
//                            "连接已断开，是否重连？", "连接断开", JOptionPane.YES_NO_OPTION);
//                    if (option == JOptionPane.YES_OPTION) {
//                        reconnectWebSocket();
//                    }
//                    break;
//
//                case "error":
//                    statusLabel.setText("⚠ WebSocket 出错: " + event.getPayload());
//                    // 记录日志
//                    log.error("WebSocket 错误: {}", event.getPayload());
//                    break;
//
//                default:
//                    System.out.println("未知 WsEvent 类型: " + event.getType());
//            }
//        });
//    }



//    // 处理用户状态事件(更新在线列表)
//    @Subscribe
//    public void onUserStatus(UserStatusEvent event) {
//        ChatMessage msg = event.getMessage();
//        SwingUtilities.invokeLater(() -> { // UI操作需在EDT线程
//            if ("online".equals(msg.getContent()) && !userListModel.contains(msg.getSender())) {
//                userListModel.addElement(msg.getSender());
//            } else if ("offline".equals(msg.getContent())) {
//                userListModel.removeElement(msg.getSender());
//            }
//        });
//    }
//    @Subscribe
//    public void onPrivateMessage(PrivateMessageEvent event) {
//        ChatMessage msg = event.getMessage();
//        // 只处理发给当前用户的消息
//        if (msg.getReceiver().equals(currentUser)) {
//            String sender = msg.getSender();
//            SwingUtilities.invokeLater(() -> {
//                // 自动打开聊天窗口（如果未打开）
//                openChatWindow(sender);
//                // 将消息推送到对应窗口
//                ChatWindow chatWindow = chatWindows.get(sender);
//                if (chatWindow != null) {
//                    chatWindow.handleMessage(msg);
//                }
//            });
//        }
//    }
//    @Subscribe
//    public void onPublicMessage(PublicMessageEvent event) {
//        ChatMessage msg = event.getMessage();
//        // 群聊窗口才处理
//        if (msg.getReceiver().equals(currentUser)) {
//            String sender = msg.getSender();
//            SwingUtilities.invokeLater(() -> {
//                // 自动打开聊天窗口（如果未打开）
//                openChatWindow(sender);
//                // 将消息推送到对应窗口
//                ChatWindow chatWindow = chatWindows.get(sender);
//                if (chatWindow != null) {
//                    chatWindow.handleMessage(msg);
//                }
//            });
//        }
//    }
    // 其他变量...


//    private void doLogout() {
//        try {
//            // 1. 发送下线状态
//            chatService.sendOfflineStatus(currentUser, eventBusManager);
//
//            // 2. 停止消息监听器（关键：避免继续接收消息）
//            chatMessageListener.stopAllListeners(); // 需要在ChatMessageListener中实现停止方法
//
//            // 3. 注销事件总线
//            eventBusManager.unregister(this);
//
//            // 4. 关闭所有打开的聊天窗口
//            for (ChatWindow window : chatWindows.values()) {
//                window.dispose();
//            }
//            chatWindows.clear();
//
//            // 5. 关闭当前主窗口，打开登录窗口
//            this.dispose();
//            LoginFrame loginFrame = new LoginFrame(eventBusManager, chatService, chatMessageListener);
//            loginFrame.setVisible(true);
//
//            log.info("用户 {} 已登出", currentUser);
//        } catch (Exception e) {
//            log.error("登出失败", e);
//            JOptionPane.showMessageDialog(this, "登出异常: " + e.getMessage());
//        }
//    }

}