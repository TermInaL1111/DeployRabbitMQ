package com.shms.deployrabbitmq.ui;


import com.shms.deployrabbitmq.pojo.Result;
import com.shms.deployrabbitmq.pojo.User;
import com.shms.deployrabbitmq.service.EventBusManager;
import com.shms.deployrabbitmq.service.ChatMessageListener;
import com.shms.deployrabbitmq.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
@Component
public class LoginFrame extends JFrame {
    private final EventBusManager eventBusManager;
    private final ChatService chatService;

    private final ChatMessageListener chatMessageListener;

    // UI组件
    private JTextField hostField;
    private JTextField portField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton connectBtn;
    private JButton loginBtn;
    private JButton registerBtn;

    @Value("${serverurl}")
    private String serverUrl;

    @Autowired
    public LoginFrame(EventBusManager eventBusManager, ChatService chatService, ChatMessageListener chatMessageListener) {
        this.eventBusManager = eventBusManager;
        this.chatService = chatService;
        this.chatMessageListener = chatMessageListener;
        initUI();
        setTitle("登录 - jMessenger");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

//        // 主机地址
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        panel.add(new JLabel("主机地址:"), gbc);
//        gbc.gridx = 1;
//        hostField = new JTextField("localhost", 20);
//        panel.add(hostField, gbc);
//
//        // 端口
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        panel.add(new JLabel("端口:"), gbc);
//        gbc.gridx = 1;
//        portField = new JTextField("8090", 20);
//        panel.add(portField, gbc);

        // 连接按钮
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        connectBtn = new JButton("连接服务器");
        connectBtn.addActionListener(e -> testConnection());
        panel.add(connectBtn, gbc);
        gbc.gridheight = 1;

        // 用户名
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("用户名:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField("Anurag", 20);
        panel.add(usernameField, gbc);

        // 密码
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("密码:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField("password", 20);
        panel.add(passwordField, gbc);

        // 登录/注册按钮
        gbc.gridx = 0;
        gbc.gridy = 4;
        loginBtn = new JButton("登录");
        loginBtn.addActionListener(e -> doLogin());
        panel.add(loginBtn, gbc);

        gbc.gridx = 1;
        registerBtn = new JButton("注册");
        registerBtn.addActionListener(e -> doRegister());
        panel.add(registerBtn, gbc);

        add(panel);
        pack();
    }

    // 测试服务器连接
    private void testConnection() {
      //  String host = hostField.getText();
     //   String port = portField.getText();
    ///    serverUrl = "http://" + host + ":" + port + "/user";

        try {
            User testUser = new User();
            Result result = chatService.testConnection(serverUrl, testUser);
            JOptionPane.showMessageDialog(this, "服务器连接成功");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "服务器连接失败: " + e.getMessage());
        }
    }

    // 登录处理
    private void doLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名/密码不能为空");
            return;
        }

        try {
            // 调用登录接口
            Result result = chatService.login(serverUrl, new User(username, password));
            if (result.getCode() == 1 ) {
                // 启动消息监听
                chatMessageListener.startPrivateMessageListener(username);
                chatMessageListener.startPublicMessageListener(username);
                chatMessageListener.startUserStatusListener(username);

                // 发送上线状态
                chatService.sendOnlineStatus(username, eventBusManager);

                // 打开主界面
                MainFrame mainFrame = new MainFrame(username, eventBusManager, chatService,chatMessageListener);
                mainFrame.setVisible(true);
                this.dispose(); // 关闭登录窗口
            } else {
                JOptionPane.showMessageDialog(this, "登录失败: " + result.getMsg());
            }
        } catch (Exception e) {
            log.error("登录失败", e);
            JOptionPane.showMessageDialog(this, "登录异常: " + e.getMessage());
        }
    }

    // 注册处理
    private void doRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名/密码不能为空");
            return;
        }

        try {
            Result result = chatService.register(serverUrl, new User(username, password));
            JOptionPane.showMessageDialog(this, result.getMsg());
        } catch (Exception e) {
            log.error("注册失败", e);
            JOptionPane.showMessageDialog(this, "注册异常: " + e.getMessage());
        }
    }
}