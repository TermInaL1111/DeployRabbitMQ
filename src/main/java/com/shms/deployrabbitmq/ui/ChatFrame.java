//package com.shms.deployrabbitmq.ui;
//
//import com.google.common.eventbus.Subscribe;
//import com.shms.deployrabbitmq.Event.*;
//import com.shms.deployrabbitmq.pojo.ChatMessage;
//import com.shms.deployrabbitmq.pojo.Result;
//import com.shms.deployrabbitmq.pojo.User;
//import com.shms.deployrabbitmq.DeployRabbitMqApplication;
//import com.shms.deployrabbitmq.History;
//import com.shms.deployrabbitmq.service.ChatMessageListener;
//import com.shms.deployrabbitmq.service.ChatService;
//import com.shms.deployrabbitmq.service.EventBusManager;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import javax.swing.*;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.StyledDocument;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.io.File;
//import java.io.InputStream;
//import java.net.URI;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.Objects;
//import java.util.UUID;
//
//
//@Slf4j
//@Component
//public class ChatFrame extends JFrame {
//
////    @Autowired
//    private EventBusManager eventBusManager;
//
//   // @Autowired
//    private ChatService chatService;
//
//   // @Autowired
//    private ChatMessageListener chatMessageListener;
//
//    private RestTemplate restTemplate = new RestTemplate();
//    private String url = "http://localhost:8090/user";
//
//    // UI Components
//    public JButton jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7, jButton8;
//    private JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7;
//    public JList jList1;
//    public JPasswordField jPasswordField1;
//    private JScrollPane jScrollPane1, jScrollPane2;
//    private JSeparator jSeparator1, jSeparator2;
//
//
//    // 替换原来的jTextArea1
//    private JTextPane jTextArea1 ;
//   // public JTextArea jTextArea1;
//    public JTextField jTextField1, jTextField2, jTextField3, jTextField4, jTextField5, jTextField6;
//
//    public DefaultListModel model;
//    public File file;
//    public String historyFile = "D:/History.xml";
//    public HistoryFrame historyFrame;
//    public History hist;
//
//    private String username;
//    private String password;
//
//    @Autowired
//    public ChatFrame(EventBusManager eventBusManager, ChatService chatService,ChatMessageListener chatMessageListener) {
//        this.eventBusManager = eventBusManager;
//        this.chatService = chatService;
//        this.chatMessageListener = chatMessageListener;
//        initComponents();
//        this.setTitle("jMessenger");
//        model.addElement("all");
//        jList1.setSelectedIndex(0);
//        jTextField6.setEditable(false);
//
//        // 注册事件总线监听
//        eventBusManager.register(this);
//
//        // 窗口关闭发送下线状态
//        this.addWindowListener(new WindowListener() {
//            @Override public void windowOpened(WindowEvent e) {}
//            @Override public void windowClosing(WindowEvent e) {
//                if (username != null && !username.isEmpty()) {
//                    ChatMessage msg = new ChatMessage();
//                    msg.setType("status");
//                    msg.setSender(username);
//                    msg.setReceiver("all");
//                    msg.setContent("OFFLINE");
//                    msg.setTimestamp(System.currentTimeMillis());
//                    eventBusManager.post(new SendMessageEvent(msg));
//                }
//            }
//            @Override public void windowClosed(WindowEvent e) {}
//            @Override public void windowIconified(WindowEvent e) {}
//            @Override public void windowDeiconified(WindowEvent e) {}
//            @Override public void windowActivated(WindowEvent e) {}
//            @Override public void windowDeactivated(WindowEvent e) {}
//        });
//
//        hist = new History(historyFile);
//    }
//    public boolean isWin32(){
//        return System.getProperty("os.name").startsWith("Windows");
//    }
//        @SuppressWarnings("unchecked")
//    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
//    private void initComponents() {
//
//        jLabel1 = new javax.swing.JLabel();
//        jTextField1 = new javax.swing.JTextField();
//        jLabel2 = new javax.swing.JLabel();
//        jTextField2 = new javax.swing.JTextField();
//        jButton1 = new javax.swing.JButton();
//        jTextField3 = new javax.swing.JTextField();
//        jLabel3 = new javax.swing.JLabel();
//        jLabel4 = new javax.swing.JLabel();
//        jButton3 = new javax.swing.JButton();
//        jPasswordField1 = new javax.swing.JPasswordField();
//        jSeparator1 = new javax.swing.JSeparator();
//        jScrollPane1 = new javax.swing.JScrollPane();
//
//            jTextArea1 = new JTextPane();
//      //  jTextArea1 = new javax.swing.JTextArea();
//        jScrollPane2 = new javax.swing.JScrollPane();
//        jList1 = new javax.swing.JList();
//        jLabel5 = new javax.swing.JLabel();
//        jTextField4 = new javax.swing.JTextField();
//        jButton4 = new javax.swing.JButton();
//        jButton2 = new javax.swing.JButton();
//        jSeparator2 = new javax.swing.JSeparator();
//        jTextField5 = new javax.swing.JTextField();
//        jButton5 = new javax.swing.JButton();
//        jButton6 = new javax.swing.JButton();
//        jLabel6 = new javax.swing.JLabel();
//        jLabel7 = new javax.swing.JLabel();
//        jTextField6 = new javax.swing.JTextField();
//        jButton7 = new javax.swing.JButton();
//        jButton8 = new javax.swing.JButton();
//
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//
//        jLabel1.setText("Host Address : ");
//
//        jTextField1.setText("localhost");
//
//        jLabel2.setText("Host Port : ");
//
//        jTextField2.setText("8090");
//
//        jButton1.setText("Connect");
//        jButton1.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton1ActionPerformed(evt);
//            }
//        });
//
//        jTextField3.setText("Anurag");
//       // jTextField3.setEnabled(false);
//
//        jLabel3.setText("Password :");
//
//        jLabel4.setText("Username :");
//
//        jButton3.setText("SignUp");
//      //  jButton3.setEnabled(false);
//        jButton3.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton3ActionPerformed(evt);
//            }
//        });
//
//        jPasswordField1.setText("password");
//     //   jPasswordField1.setEnabled(false);
//
////        jTextArea1.setColumns(20);
////        jTextArea1.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
////        jTextArea1.setRows(5);
////        jScrollPane1.setViewportView(jTextArea1);
//            // 在initComponents方法中，替换原来的jTextArea1相关代码
//            jTextArea1.setContentType("text/html"); // 支持HTML格式（可选，方便样式控制）
//            jTextArea1.setEditable(false); // 聊天记录不可编辑
//            jScrollPane1.setViewportView(jTextArea1); // 滚动面板关联JTextPane
//
//        jList1.setModel((model = new DefaultListModel()));
//        jScrollPane2.setViewportView(jList1);
//
//        jLabel5.setText("Message : ");
//
//        jButton4.setText("Send Message ");
//      //  jButton4.setEnabled(false);
//        jButton4.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton4ActionPerformed(evt);
//            }
//        });
//
//        jButton2.setText("Login");
//      //  jButton2.setEnabled(false);
//        jButton2.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton2ActionPerformed(evt);
//            }
//        });
//
//        jButton5.setText("...");
//      //  jButton5.setEnabled(false);
//        jButton5.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton5ActionPerformed(evt);
//            }
//        });
//
//        jButton6.setText("Send");
//      //  jButton6.setEnabled(false);
//        jButton6.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton6ActionPerformed(evt);
//            }
//        });
//
//        jLabel6.setText("File :");
//
//        jLabel7.setText("History File :");
//
//        jButton7.setText("...");
//       // jButton7.setEnabled(false);
//        jButton7.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton7ActionPerformed(evt);
//            }
//        });
//
//        jButton8.setText("Show");
//     //   jButton8.setEnabled(false);
//        jButton8.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton8ActionPerformed(evt);
//            }
//        });
//
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
//        getContentPane().setLayout(layout);
//        layout.setHorizontalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(layout.createSequentialGroup()
//                                .addContainerGap()
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                        .addComponent(jSeparator2)
//                                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
//                                                        .addComponent(jLabel1)
//                                                        .addComponent(jLabel4)
//                                                        .addComponent(jLabel7))
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                                        .addGroup(layout.createSequentialGroup()
//                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                                                        .addComponent(jTextField3)
//                                                                        .addComponent(jTextField1))
//                                                                .addGap(18, 18, 18)
//                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
//                                                                        .addComponent(jLabel2)
//                                                                        .addComponent(jLabel3))
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                                                        .addComponent(jTextField2)
//                                                                        .addComponent(jPasswordField1)))
//                                                        .addComponent(jTextField6))
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
//                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
//                                                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                                                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)))))
//                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
//                                                .addComponent(jScrollPane1)
//                                                .addGap(18, 18, 18)
//                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addGap(23, 23, 23)
//                                                .addComponent(jLabel6)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                .addGap(18, 18, 18)
//                                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
//                                                .addComponent(jLabel5)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addComponent(jTextField4)
//                                                .addGap(18, 18, 18)
//                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
//                                .addContainerGap())
//        );
//        layout.setVerticalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(layout.createSequentialGroup()
//                                .addContainerGap()
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                        .addComponent(jLabel1)
//                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jLabel2)
//                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jButton1))
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jLabel3)
//                                        .addComponent(jLabel4)
//                                        .addComponent(jButton3)
//                                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jButton2))
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                        .addComponent(jLabel7)
//                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jButton7)
//                                        .addComponent(jButton8))
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                        .addComponent(jScrollPane1)
//                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                        .addComponent(jButton4)
//                                        .addComponent(jLabel5)
//                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
//                                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                        .addComponent(jLabel6)
//                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                .addContainerGap())
//        );
//
//        pack();
//    }// </editor-fold>//GEN-END:initComponents
//
//
//    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//       String serverAddr = jTextField1.getText();
//       int port = Integer.parseInt(jTextField2.getText());
//
//        if(!serverAddr.isEmpty() && !jTextField2.getText().isEmpty()){
//            try{
//                User user = new User();
//                user.setUsername(username);
//                user.setPassword(password);
//                Result result = restTemplate.postForObject(url+"/test", user, Result.class);
//                log.info(result.toString());
//
//                appendToChat("[Application > Me] : Server is found\n");
//
//
//            }
//            catch(Exception ex){
//                    appendToChat("[Application > Me] : Server not found\n");
//            }
//        }
//    }//GEN-LAST:event_jButton1ActionPerformed
//
//    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//        username = jTextField3.getText();
//        password = jPasswordField1.getText();
//
//        if(!username.isEmpty() && !password.isEmpty()){
//
//
//            User user = new User();
//            user.setUsername(username);
//            user.setPassword(password);
//            Result result = restTemplate.postForObject(url + "/login", user, Result.class);
//            // 登录成功，启动消息监听
//            chatMessageListener.startPrivateMessageListener(username);
//            chatMessageListener.startPublicMessageListener(username);
//            chatMessageListener.startUserStatusListener(username);
//            // 发送上线状态
//            ChatMessage msg = new ChatMessage();
//            msg.setTimestamp(System.currentTimeMillis());
//            msg.setMessageId(UUID.randomUUID().toString()); // 唯一消息ID
//            msg.setType("status");
//            msg.setSender(username);
//            msg.setReceiver("all");
//            msg.setContent("online");
//            msg.setTimestamp(System.currentTimeMillis());
//            eventBusManager.post(new SendMessageEvent(msg));
//
//
//            log.info(result.toString());
//        }
//    }//GEN-LAST:event_jButton2ActionPerformed
//
//    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
//        String msg = jTextField4.getText();
//        String target = jList1.getSelectedValue().toString();
//
//        if(!msg.isEmpty() && !target.isEmpty()){
//            jTextField4.setText("");
//
//
//            ChatMessage message = new ChatMessage();
//            message.setTimestamp(System.currentTimeMillis());
//            message.setMessageId(UUID.randomUUID().toString()); // 唯一消息ID
//            message.setType("message");
//            message.setContent(msg);
//            message.setSender(username);
//            message.setReceiver(target);
//            message.setTimestamp(System.currentTimeMillis());
//            // 使用事件总线发送
//            eventBusManager.post(new SendMessageEvent(message));
//
//            appendToChat("[" + username + " > " + target + "] : " + msg + "\n");
//        }
//    }//GEN-LAST:event_jButton4ActionPerformed
//
//    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
//        username = jTextField3.getText();
//        password = jPasswordField1.getText();
//
//        if(!username.isEmpty() && !password.isEmpty()){
//            User user = new User();
//            user.setUsername(username);
//            user.setPassword(password);
//            Result result = restTemplate.postForObject(url + "/register", user, Result.class);
//            log.info(result.toString());
//
//        }
//    }//GEN-LAST:event_jButton3ActionPerformed
//
//    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.showDialog(this, "Select File");
//        file = fileChooser.getSelectedFile();
//
//        if(file != null){
//            if(!file.getName().isEmpty()){
//                jButton6.setEnabled(true); String str;
//
//                if(jTextField5.getText().length() > 30){
//                    String t = file.getPath();
//                    str = t.substring(0, 20) + " [...] " + t.substring(t.length() - 20, t.length());
//                }
//                else{
//                    str = file.getPath();
//                }
//                jTextField5.setText(str);
//            }
//        }
//    }//GEN-LAST:event_jButton5ActionPerformed
//
//    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
//        long size = file.length();
//        if(size < 120 * 1024 * 1024){
//          //  log.info("skip");
//            //     client.send(new Message("upload_req", username, file.getName(), jList1.getSelectedValue().toString()));
//            try {
//                // 上传文件到服务器
//                String serverUrl = url+"/upload";
//                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//                body.add("file", new FileSystemResource(file));
//
//                RestTemplate restTemplate = new RestTemplate();
//                Result result = restTemplate.postForObject(serverUrl, body, Result.class);
//                if(result == null || result.getCode().equals(0)){
//                    appendToChat("[Application > Me] : Upload failed\n");
//                    return;
//                }
//
//                String fileUrl = result.getData().toString();
//
//                // 构建文件消息
//                ChatMessage msg = new ChatMessage();
//                msg.setTimestamp(System.currentTimeMillis());
//                msg.setMessageId(UUID.randomUUID().toString()); // 唯一消息ID
//                msg.setType("file");
//                msg.setSender(username);
//                msg.setReceiver(jList1.getSelectedValue().toString());
//
//                msg.setContent(file.getName());
//                msg.setFileUrl(fileUrl);
//                msg.setTimestamp(System.currentTimeMillis());
//
//                // 发布事件到事件总线 → 最终通过 MQ 发送
//                eventBusManager.post(new SendMessageEvent(msg));
//
//                appendToChat("[Application > Me] : File sent: " + file.getName() + "\n");
//
//            } catch (Exception e) {
//                log.error("文件发送失败", e);
//                appendToChat("[Application > Me] : File send error\n");
//            }
//
//        }
//        else{
//            appendToChat("[Application > Me] : File is size too large\n");
//        }
//    }//GEN-LAST:event_jButton6ActionPerformed
//
//    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
//        JFileChooser jf = new JFileChooser();
//        jf.showDialog(this, "Select File");
//
//        if(!jf.getSelectedFile().getPath().isEmpty()){
//            historyFile = jf.getSelectedFile().getPath();
//            if(this.isWin32()){
//                historyFile = historyFile.replace("/", "\\");
//            }
//            jTextField6.setText(historyFile);
//            jTextField6.setEditable(false);
//            jButton7.setEnabled(false);
//            jButton8.setEnabled(true);
//            hist = new History(historyFile);
//
//            historyFrame = new HistoryFrame(hist);
//            historyFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//            historyFrame.setVisible(false);
//        }
//    }//GEN-LAST:event_jButton7ActionPerformed
//
//    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
//        historyFrame.setLocation(this.getLocation());
//        historyFrame.setVisible(true);
//    }//GEN-LAST:event_jButton8ActionPerformed
//    private void showHistory() {
//        if (historyFrame != null) {
//            historyFrame.setLocation(this.getLocation());
//            historyFrame.setVisible(true);
//        }
//    }
//
//    // 事件总线回调
//    //私人消息
//    @Subscribe
//    public void onPrivateMessage(PrivateMessageEvent event) {
//        ChatMessage msg = event.getMessage();
//        StyledDocument doc = jTextArea1.getStyledDocument(); // 获取文档对象
//
//            if ("file".equals(msg.getType())) {
//                // 显示文件名，并提供下载按钮
//                JButton downloadButton = new JButton(msg.getContent());
//                downloadButton.addActionListener(e -> {
//                    // 使用 fileUrl 下载文件
//                    downloadFile(msg.getFileUrl(), msg.getContent());
//                });
//                appendToChat("[" + msg.getSender() + " > Me] : ");
//            } else {
//                appendToChat("[" + msg.getSender() + " > Me] : " + msg.getContent() + "\n");
//            }
//    }
//
//    private void downloadFile(String fileUrl, String defaultFileName) {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setSelectedFile(new File(defaultFileName));
//        int option = fileChooser.showSaveDialog(this);
//        if (option != JFileChooser.APPROVE_OPTION) {
//            // 下载取消消息（直接在当前线程，因为showSaveDialog在EDT中）
//            appendToChat("[Application > Me] : Download canceled\n");
//            return;
//        }
//        File saveFile = fileChooser.getSelectedFile();
//
//        new Thread(() -> {
//            try {
//                RestTemplate restTemplate = new RestTemplate();
//                URI uri = new URI(fileUrl);
//                ResponseEntity<Resource> response = restTemplate.exchange(
//                        uri, HttpMethod.GET, null, Resource.class);
//
//                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//                    try (InputStream in = response.getBody().getInputStream()) {
//                        Files.copy(in, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                    }
//                    // 下载成功消息（在EDT中执行）
//                    SwingUtilities.invokeLater(() ->
//                            appendToChat("[Application > Me] : File downloaded: " + saveFile.getAbsolutePath() + "\n")
//                    );
//                } else {
//                    // 服务器非成功状态消息
//                    SwingUtilities.invokeLater(() ->
//                            appendToChat("[Application > Me] : Server returned " + response.getStatusCode() + "\n")
//                    );
//                }
//
//            } catch (Exception e) {
//                log.error("下载失败", e);
//                // 下载错误消息
//                SwingUtilities.invokeLater(() ->
//                        appendToChat("[Application > Me] : File download error\n")
//                );
//            }
//        }).start();
//    }
//
//    // 封装一个通用的聊天记录添加方法，避免重复代码
//    private void appendToChat(String text) {
//        StyledDocument doc = jTextArea1.getStyledDocument();
//        try {
//            doc.insertString(doc.getLength(), text, null); // 在文档末尾插入文本
//            jTextArea1.setCaretPosition(doc.getLength()); // 自动滚动到底部
//        } catch (BadLocationException e) {
//            e.printStackTrace();
//        }
//    }
//    @Subscribe
//    public void onUserStatus(UserStatusEvent event) {
//        ChatMessage msg = event.getMessage();
//
//        if ("online".equals(msg.getContent())) {
//            if (!model.contains(msg.getSender())) {
//                model.addElement(msg.getSender());
//            }
//        } else if ("offline".equals(msg.getContent())) {
//            model.removeElement(msg.getSender());
//        }
//    }
//    @Subscribe
//    public void onPublicMessage(PublicMessageEvent event) {
//        ChatMessage msg = event.getMessage();
//            if(Objects.equals(msg.getSender(), username))return;
//            appendToChat("[" + msg.getSender() + " > Me] : " + msg.getContent() + "\n");
//    }
//}
