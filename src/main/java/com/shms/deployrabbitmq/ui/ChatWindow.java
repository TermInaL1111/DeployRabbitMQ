package com.shms.deployrabbitmq.ui;


import com.shms.deployrabbitmq.Event.SendMessageEvent;
import com.shms.deployrabbitmq.pojo.ChatMessage;
import com.shms.deployrabbitmq.service.EventBusManager;
import com.shms.deployrabbitmq.service.ChatService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.UUID;

@Slf4j
public class ChatWindow extends JFrame {
    private final String sender; // 当前用户
    private final String currentUser;
    private final String receiver; // 聊天对象
    private final EventBusManager eventBusManager;
    private final ChatService chatService;

    // UI组件
    private JTextPane chatArea;
    private JTextField messageField;
    private JTextField fileField;
    private File selectedFile;

    public ChatWindow(String sender, String receiver, EventBusManager eventBusManager, ChatService chatService) {
        this.sender = sender;
        this.receiver = receiver;
        currentUser = sender;
        this.eventBusManager = eventBusManager;
        this.chatService = chatService;

        eventBusManager.register(this); // 注册事件
        initUI();
        setTitle("聊天 - " + receiver);
        setSize(600, 500);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 聊天记录区域
        chatArea = new JTextPane();
        chatArea.setContentType("text/html");
        chatArea.setEditable(false);
        mainPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        initChatArea();
        // 底部输入区域
        JPanel inputPanel = new JPanel(new BorderLayout());

        // 消息输入
        JPanel msgPanel = new JPanel(new BorderLayout(5, 5));
        messageField = new JTextField();
        JButton sendMsgBtn = new JButton("发送消息");
        sendMsgBtn.addActionListener(e -> sendMessage());
        msgPanel.add(messageField, BorderLayout.CENTER);
        msgPanel.add(sendMsgBtn, BorderLayout.EAST);

        // 文件发送
        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        fileField = new JTextField();
        fileField.setEditable(false);
        JButton selectFileBtn = new JButton("选择文件");
        selectFileBtn.addActionListener(e -> selectFile());
        JButton sendFileBtn = new JButton("发送文件");
        sendFileBtn.addActionListener(e -> sendFile());
        filePanel.add(fileField, BorderLayout.CENTER);
        filePanel.add(selectFileBtn, BorderLayout.EAST);
        filePanel.add(sendFileBtn, BorderLayout.WEST);

        // 组装底部面板
        inputPanel.add(msgPanel, BorderLayout.NORTH);
        inputPanel.add(filePanel, BorderLayout.SOUTH);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // 发送文本消息
    private void sendMessage() {
        String content = messageField.getText().trim();
        if (content.isEmpty()) return;

        // 构建消息
        ChatMessage msg = new ChatMessage();
        msg.setMessageId(UUID.randomUUID().toString());
        msg.setType("text");
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(content);
        msg.setTimestamp(System.currentTimeMillis());
        // 发布发送事件
        eventBusManager.post(new SendMessageEvent(msg));

        // 本地显示
        appendMessage(sender, content);
        messageField.setText("");
    }

    // 选择文件
    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fileField.setText(selectedFile.getName());
        }
    }

    // 发送文件
    private void sendFile() {
        if (selectedFile == null || !selectedFile.exists()) {
            JOptionPane.showMessageDialog(this, "请选择有效的文件");
            return;
        }

        try {
            // 先上传文件到服务器
            String fileUrl = chatService.uploadFile(selectedFile);
            if (fileUrl == null) {
                appendSystemMessage("文件上传失败");
                return;
            }

            // 发送文件消息
            ChatMessage msg = new ChatMessage();
            msg.setMessageId(UUID.randomUUID().toString());
            msg.setType("text");
            msg.setSender(sender);
            msg.setReceiver(receiver);
            msg.setContent(selectedFile.getName());
            msg.setFileUrl(fileUrl);
            msg.setTimestamp(System.currentTimeMillis());

            eventBusManager.post(new SendMessageEvent(msg));

            appendSystemMessage("文件已发送: " + selectedFile.getName());
            fileField.setText("");
            selectedFile = null;
        } catch (Exception e) {
            log.error("文件发送失败", e);
            appendSystemMessage("文件发送失败");
        }
    }

//    // 处理私人消息
//    @Subscribe
//    public void onPrivateMessage(PrivateMessageEvent event) {
//        ChatMessage msg = event.getMessage();
//        // 只处理发给当前聊天对象的消息
//        if (msg.getReceiver().equals(sender) && msg.getSender().equals(receiver)) {
//            handleMessage(msg);
//        }
//    }

    // 处理群聊消息
//    @Subscribe
//    public void onPublicMessage(PublicMessageEvent event) {
//        ChatMessage msg = event.getMessage();
//        // 群聊窗口才处理
//        if ("all".equals(receiver) && msg.getReceiver().equals("all")) {
//            handleMessage(msg);
//        }
//    }

    // 统一处理消息显示
    void handleMessage(ChatMessage msg) {
        SwingUtilities.invokeLater(() -> {
            if (msg.getFileUrl()!=null) {
                appendFileMessage(msg.getSender(), msg.getContent(), msg.getFileUrl());
            } else {
                appendMessage(msg.getSender(), msg.getContent());
            }
        });
    }
    // 统一使用HTML格式，确保排版一致
    private void appendMessage(String sender, String content) {
        log.info(sender);
        try {
            // 强制转换为HTML文档（确保类型正确）
            HTMLDocument doc = (HTMLDocument) chatArea.getDocument();
            HTMLEditorKit kit = (HTMLEditorKit) chatArea.getEditorKit();

            // 转义HTML特殊字符
            String escapedContent = content.replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("&", "&amp;");

            // 构建HTML片段
            String html = String.format(
                    "<div style='margin:5px 0;'><span style='color:#2c3e50; font-weight:bold;'>%s</span>: <span style='color:#34495e;'>%s</span></div>",
                    sender, escapedContent
            );

            // 直接插入到文档末尾（无需手动处理闭合标签）
            kit.insertHTML(doc, doc.getLength(), html, 0, 0, null);
            chatArea.setCaretPosition(doc.getLength()); // 滚动到底部
        } catch (Exception e) {
            log.error("消息渲染失败", e);
        }
    }

    // 初始化聊天区域时也使用HTMLEditorKit
    private void initChatArea() {
        chatArea.setContentType("text/html");
        chatArea.setEditorKit(new HTMLEditorKit());
        // 初始化空白HTML文档
        HTMLDocument doc = (HTMLDocument) chatArea.getDocument();
        doc.setBase(null); // 避免路径问题
        try {
            ((HTMLEditorKit) chatArea.getEditorKit()).insertHTML(
                    doc, 0,
                    "<html><body style='font-family:微软雅黑, sans-serif; font-size:14px; padding:10px;'></body></html>",
                    0, 0, null
            );
        } catch (Exception e) {
            log.error("初始化聊天区域失败", e);
        }
    }


    // 系统消息单独样式
    private void appendSystemMessage(String msg) {
        StyledDocument doc = chatArea.getStyledDocument();
        String html = String.format("<div style='margin:5px 0; color:#e74c3c; font-style:italic;'>[系统] %s</div>", msg);
        try {
            String currentText = chatArea.getText().replaceAll("</body></html>", "");
            chatArea.setText(currentText + html + "</body></html>");
            chatArea.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            log.error("系统消息排版失败", e);
        }
    }


    // 显示文件消息(带下载按钮)

    private void appendFileMessage(String sender, String fileName, String fileUrl) {
        StyledDocument doc = chatArea.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), sender + ": " + fileName + " ", null);

            // 添加下载按钮
            JButton downloadBtn = new JButton("下载");
            downloadBtn.addActionListener(e -> downloadFile(fileUrl, fileName));
            chatArea.insertComponent(downloadBtn);
            doc.insertString(doc.getLength(), "\n", null);
            chatArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            log.error("文件消息显示失败", e);
        }
    }

    // 下载文件
//    private void downloadFile(String fileUrl, String fileName) {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setSelectedFile(new File(fileName));
//        int result = fileChooser.showSaveDialog(this);
//        if (result != JFileChooser.APPROVE_OPTION) return;
//
//        File saveFile = fileChooser.getSelectedFile();
//        new Thread(() -> {
//            try {
//                chatService.downloadFile(new URI(fileUrl), saveFile);
//                SwingUtilities.invokeLater(() -> appendSystemMessage("文件已保存: " + saveFile.getAbsolutePath()));
//            } catch (Exception e) {
//                log.error("文件下载失败", e);
//                SwingUtilities.invokeLater(() -> appendSystemMessage("文件下载失败"));
//            }
//        }).start();
//    }

    private void downloadFile(String fileUrl, String fileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(fileName));
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File saveFile = fileChooser.getSelectedFile();
        new Thread(() -> {
            try {
                chatService.downloadFile(new URI(fileUrl), saveFile);
                // 下载成功弹窗
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "文件已保存: " + saveFile.getAbsolutePath(),
                            "下载成功",
                            JOptionPane.INFORMATION_MESSAGE);
                    appendSystemMessage("文件已保存: " + saveFile.getAbsolutePath());
                });
            } catch (Exception e) {
                log.error("文件下载失败", e);
                // 下载失败弹窗
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "文件下载失败: " + e.getMessage(),
                            "下载失败",
                            JOptionPane.ERROR_MESSAGE);
                    appendSystemMessage("文件下载失败");
                });
            }
        }).start();
    }


//    private void appendFileMessage(String sender, String fileName, String fileUrl) {
//        StyledDocument doc = chatArea.getStyledDocument();
//        try {
//            // 插入文件名称文本
//            doc.insertString(doc.getLength(), sender + ": " + fileName + " ", null);
//
//            // 创建下载按钮（持久存在）
//            JButton downloadBtn = new JButton("下载");
//            downloadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//
//            // 防止重复点击
//            downloadBtn.addActionListener(e -> {
//                downloadBtn.setEnabled(false);
//                downloadBtn.setText("下载中...");
//                new Thread(() -> {
//                    try {
//                        downloadFile(fileUrl, fileName);
//                        SwingUtilities.invokeLater(() -> {
//                            downloadBtn.setText("已下载");
//                            JOptionPane.showMessageDialog(
//                                    chatArea,
//                                    "文件已成功保存到本地！",
//                                    "下载完成",
//                                    JOptionPane.INFORMATION_MESSAGE
//                            );
//                        });
//                    } catch (Exception ex) {
//                        SwingUtilities.invokeLater(() -> {
//                            downloadBtn.setEnabled(true);
//                            downloadBtn.setText("下载");
//                            JOptionPane.showMessageDialog(
//                                    chatArea,
//                                    "文件下载失败：" + ex.getMessage(),
//                                    "错误",
//                                    JOptionPane.ERROR_MESSAGE
//                            );
//                        });
//                    }
//                }).start();
//            });
//
//            // 插入按钮（不会被自动移除）
//            chatArea.insertComponent(downloadBtn);
//            doc.insertString(doc.getLength(), "\n", null);
//            chatArea.setCaretPosition(doc.getLength());
//        } catch (BadLocationException e) {
//            log.error("文件消息显示失败", e);
//        }
//    }

    // 下载文件方法（保持原样，但不再内部弹窗）
//    private void downloadFile(String fileUrl, String fileName) throws Exception {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setSelectedFile(new File(fileName));
//        int result = fileChooser.showSaveDialog(this);
//        if (result != JFileChooser.APPROVE_OPTION) return;
//
//        File saveFile = fileChooser.getSelectedFile();
//        chatService.downloadFile(new URI(fileUrl), saveFile);
//    }

    // 添加窗口关闭监听器
    public void addWindowListener(Runnable onClose) {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
              //  eventBusManager.unregister(ChatWindow.this); // 注销事件
                onClose.run();
            }
        });
    }
}