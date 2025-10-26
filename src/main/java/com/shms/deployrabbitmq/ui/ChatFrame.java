package com.shms.deployrabbitmq.ui;

import com.google.common.eventbus.Subscribe;
import com.shms.deployrabbitmq.Event.*;
import com.shms.deployrabbitmq.pojo.ChatMessage;
import com.shms.deployrabbitmq.pojo.Result;
import com.shms.deployrabbitmq.pojo.User;
import com.shms.deployrabbitmq.DeployRabbitMqApplication;
import com.shms.deployrabbitmq.History;
import com.shms.deployrabbitmq.service.ChatMessageListener;
import com.shms.deployrabbitmq.service.ChatService;
import com.shms.deployrabbitmq.service.EventBusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;


@Slf4j
@Component
public class ChatFrame extends JFrame {

//    @Autowired
    private EventBusManager eventBusManager;

   // @Autowired
    private ChatService chatService;

   // @Autowired
    private ChatMessageListener chatMessageListener;

    private RestTemplate restTemplate = new RestTemplate();
    private String url = "http://localhost:8090/user";

    // UI Components
    public JButton jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7, jButton8;
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7;
    public JList jList1;
    public JPasswordField jPasswordField1;
    private JScrollPane jScrollPane1, jScrollPane2;
    private JSeparator jSeparator1, jSeparator2;
    public JTextArea jTextArea1;
    public JTextField jTextField1, jTextField2, jTextField3, jTextField4, jTextField5, jTextField6;

    public DefaultListModel model;
    public File file;
    public String historyFile = "D:/History.xml";
    public HistoryFrame historyFrame;
    public History hist;

    private String username;
    private String password;

    @Autowired
    public ChatFrame(EventBusManager eventBusManager, ChatService chatService,ChatMessageListener chatMessageListener) {
        this.eventBusManager = eventBusManager;
        this.chatService = chatService;
        this.chatMessageListener = chatMessageListener;
        initComponents();
        this.setTitle("jMessenger");
        model.addElement("all");
        jList1.setSelectedIndex(0);
        jTextField6.setEditable(false);

        // 注册事件总线监听
        eventBusManager.register(this);

        // 窗口关闭发送下线状态
        this.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {
                if (username != null && !username.isEmpty()) {
                    ChatMessage msg = new ChatMessage();
                    msg.setType("status");
                    msg.setSender(username);
                    msg.setReceiver("all");
                    msg.setContent("OFFLINE");
                    msg.setTimestamp(System.currentTimeMillis());
                    eventBusManager.post(new SendMessageEvent(msg));
                }
            }
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });

        hist = new History(historyFile);
    }
    public boolean isWin32(){
        return System.getProperty("os.name").startsWith("Windows");
    }
        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jTextField5 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Host Address : ");

        jTextField1.setText("localhost");

        jLabel2.setText("Host Port : ");

        jTextField2.setText("8090");

        jButton1.setText("Connect");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField3.setText("Anurag");
       // jTextField3.setEnabled(false);

        jLabel3.setText("Password :");

        jLabel4.setText("Username :");

        jButton3.setText("SignUp");
      //  jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPasswordField1.setText("password");
     //   jPasswordField1.setEnabled(false);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jList1.setModel((model = new DefaultListModel()));
        jScrollPane2.setViewportView(jList1);

        jLabel5.setText("Message : ");

        jButton4.setText("Send Message ");
      //  jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton2.setText("Login");
      //  jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setText("...");
      //  jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Send");
      //  jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel6.setText("File :");

        jLabel7.setText("History File :");

        jButton7.setText("...");
       // jButton7.setEnabled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Show");
     //   jButton8.setEnabled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator2)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel7))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jTextField3)
                                                                        .addComponent(jTextField1))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jLabel2)
                                                                        .addComponent(jLabel3))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jTextField2)
                                                                        .addComponent(jPasswordField1)))
                                                        .addComponent(jTextField6))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jScrollPane1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField4)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4)
                                        .addComponent(jButton3)
                                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton7)
                                        .addComponent(jButton8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton4)
                                        .addComponent(jLabel5)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel6)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       String serverAddr = jTextField1.getText();
       int port = Integer.parseInt(jTextField2.getText());

        if(!serverAddr.isEmpty() && !jTextField2.getText().isEmpty()){
            try{
//                client = new SocketClient(this);
//                clientThread = new Thread(client);
//                clientThread.start();
//                client.send(new Message("test", "testUser", "testContent", "SERVER"));

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                Result result = restTemplate.postForObject(url+"/test", user, Result.class);
                log.info(result.toString());
                jTextArea1.append("[Application > Me] : Server is found\n");

            }
            catch(Exception ex){
                jTextArea1.append("[Application > Me] : Server not found\n");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        username = jTextField3.getText();
        password = jPasswordField1.getText();

        if(!username.isEmpty() && !password.isEmpty()){


            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            Result result = restTemplate.postForObject(url + "/login", user, Result.class);
            // 登录成功，启动消息监听
            chatMessageListener.startPrivateMessageListener(username);
            chatMessageListener.startPublicMessageListener(username);
            chatMessageListener.startUserStatusListener(username);
            // 发送上线状态
            ChatMessage msg = new ChatMessage();
            msg.setType("status");
            msg.setSender(username);
            msg.setReceiver("all");
            msg.setContent("online");
            msg.setTimestamp(System.currentTimeMillis());
            eventBusManager.post(new SendMessageEvent(msg));

            log.info(result.toString());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String msg = jTextField4.getText();
        String target = jList1.getSelectedValue().toString();

        if(!msg.isEmpty() && !target.isEmpty()){
            jTextField4.setText("");


            ChatMessage message = new ChatMessage();
            message.setType("message");
            message.setContent(msg);
            message.setSender(username);
            message.setReceiver(target);
            message.setTimestamp(System.currentTimeMillis());
            // 使用事件总线发送
            eventBusManager.post(new SendMessageEvent(message));
            jTextArea1.append("[" + username + " > " + target + "] : " + msg + "\n");

            // client.send(new Message("message", username, msg, target));
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        username = jTextField3.getText();
        password = jPasswordField1.getText();

        if(!username.isEmpty() && !password.isEmpty()){
            //  client.send(new Message("signup", username, password, "SERVER"));

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            Result result = restTemplate.postForObject(url + "/register", user, Result.class);
            log.info(result.toString());

            //http 11111111111
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showDialog(this, "Select File");
        file = fileChooser.getSelectedFile();

        if(file != null){
            if(!file.getName().isEmpty()){
                jButton6.setEnabled(true); String str;

                if(jTextField5.getText().length() > 30){
                    String t = file.getPath();
                    str = t.substring(0, 20) + " [...] " + t.substring(t.length() - 20, t.length());
                }
                else{
                    str = file.getPath();
                }
                jTextField5.setText(str);
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        long size = file.length();
        if(size < 120 * 1024 * 1024){
            log.info("skip");
            //     client.send(new Message("upload_req", username, file.getName(), jList1.getSelectedValue().toString()));
        }
        else{
            jTextArea1.append("[Application > Me] : File is size too large\n");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        JFileChooser jf = new JFileChooser();
        jf.showDialog(this, "Select File");

        if(!jf.getSelectedFile().getPath().isEmpty()){
            historyFile = jf.getSelectedFile().getPath();
            if(this.isWin32()){
                historyFile = historyFile.replace("/", "\\");
            }
            jTextField6.setText(historyFile);
            jTextField6.setEditable(false);
            jButton7.setEnabled(false);
            jButton8.setEnabled(true);
            hist = new History(historyFile);

            historyFrame = new HistoryFrame(hist);
            historyFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            historyFrame.setVisible(false);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        historyFrame.setLocation(this.getLocation());
        historyFrame.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed
    private void showHistory() {
        if (historyFrame != null) {
            historyFrame.setLocation(this.getLocation());
            historyFrame.setVisible(true);
        }
    }

    // 事件总线回调
    //私人消息
    @Subscribe
    public void onPrivateMessage(PrivateMessageEvent event) {
        ChatMessage msg = event.getMessage();
//        if (msg.getSender().equals(username)) {
//            jTextArea1.append("[Me > " + msg.getReceiver() + "] : " + msg.getContent() + "\n");
//        } else {
            jTextArea1.append("[" + msg.getSender() + " > Me] : " + msg.getContent() + "\n");
    //    }

    }

    @Subscribe
    public void onUserStatus(UserStatusEvent event) {
        ChatMessage msg = event.getMessage();
        if ("online".equals(msg.getContent())) {
            if (!model.contains(msg.getSender())) {
                model.addElement(msg.getSender());
            }
        } else if ("offline".equals(msg.getContent())) {
            model.removeElement(msg.getSender());
        }
    }
    @Subscribe
    public void onPublicMessage(PublicMessageEvent event) {
        ChatMessage msg = event.getMessage();
    //    if (msg.getSender().equals(username)) {
      //      jTextArea1.append("[Me > " + "all" + "] : " + msg.getContent() + "\n");
       // } else {
            jTextArea1.append("[" + msg.getSender() + " > Me] : " + msg.getContent() + "\n");
        //}

    }


}



//import com.shms.deployrabbitmq.ChatMessageHandler;
//import com.shms.deployrabbitmq.DeployRabbitMqApplication;
//import com.shms.deployrabbitmq.History;
//import com.shms.deployrabbitmq.pojo.ChatMessage;
//import com.shms.deployrabbitmq.pojo.Result;
//import com.shms.deployrabbitmq.pojo.User;
//import com.shms.deployrabbitmq.service.ChatListenerManager;
//
//import com.shms.deployrabbitmq.service.ChatService;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//
//import org.springframework.web.client.RestTemplate;
//
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.io.File;
//import javax.swing.*;
//
////import oracle.jrockit.jfr.JFR;
//
////观察者模式  实现 chat接口
//
//@Slf4j
//public class ChatFrame extends javax.swing.JFrame implements ChatMessageHandler {
//
//
//    private ChatListenerManager listenerManager;
//
//    private ChatService chatService; // 发送消息用
//
//
//
//
//    RestTemplate restTemplate = new RestTemplate();
//
//    String url = "http://localhost:8090/user";
//            //"http://8.137.54.50/:8090/user";
//
//    @Override
//    public void onPrivateMessage(ChatMessage msg) {
//        jTextArea1.append("[" + msg.getSender() + " > Me] : " + msg.getContent() + "\n");
//        // 可同步写入历史
//    }
//
//    @Override
//    public void onGroupMessage(ChatMessage msg) {
//        jTextArea1.append("[" + msg.getSender() + " > All] : " + msg.getContent() + "\n");
//        // 可同步写入历史
//    }
//
//    @Override
//    public void onUserStatus(ChatMessage msg) {
//        // 用户上线/下线更新列表
//        if (msg.getContent().equals("ONLINE")) {
//            if (!model.contains(msg.getSender())) {
//                model.addElement(msg.getSender());
//            }
//        } else if (msg.getContent().equals("OFFLINE")) {
//            model.removeElement(msg.getSender());
//        }
//    }
//
////    public SocketClient client;
//    public int port;
//    public String serverAddr, username, password;
//    public Thread clientThread;
//    public DefaultListModel model;
//    public File file;
//    public String historyFile = "D:/History.xml";
//    public HistoryFrame historyFrame;
//    public History hist;
//
//    public ChatFrame(
//    ChatListenerManager listenerManager, ChatService chatService) {
//        this.listenerManager = listenerManager;
//        this.chatService = chatService;
//        initComponents();
//        this.setTitle("jMessenger");
//        model.addElement("all");
//        jList1.setSelectedIndex(0);
//
//        jTextField6.setEditable(false);
//
//
//
//
//        this.addWindowListener(new WindowListener() {
//
//            @Override public void windowOpened(WindowEvent e) {}
//            @Override public void windowClosing(WindowEvent e) {
////                try{
//                    ChatMessage msg = new ChatMessage();
//                    msg.setType("status");
//                    msg.setSender(username);
//                    msg.setReceiver("all");
//                    msg.setContent("OFFLINE");
//                    msg.setTimestamp(System.currentTimeMillis());
//                    chatService.sendUserStatus( msg);
////                }catch(Exception ex){}
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
//


//
//    @SuppressWarnings("unchecked")
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
//        jTextArea1 = new javax.swing.JTextArea();
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
//        jTextArea1.setColumns(20);
//        jTextArea1.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
//        jTextArea1.setRows(5);
//        jScrollPane1.setViewportView(jTextArea1);
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
//    public static void main(String[] args) {
//        // 1️⃣ 设置系统 Look & Feel
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception ex) {
//            System.out.println("Look & Feel exception");
//        }
//
//        // 2️⃣ 启动 Spring 容器
//        ConfigurableApplicationContext context = SpringApplication.run(DeployRabbitMqApplication.class, args);
//
//        // 3️⃣ 从 Spring 获取服务 Bean
//        ChatListenerManager listenerManager = context.getBean(ChatListenerManager.class);
//        ChatService chatService = context.getBean(ChatService.class);
//
//        // 4️⃣ 启动 Swing GUI，并注入 Spring Bean
//        SwingUtilities.invokeLater(() -> {
//            ChatFrame chatFrame = new ChatFrame(listenerManager, chatService);
//            chatFrame.setVisible(true);
//        });
//    }
//
//    // Variables declaration - do not modify//GEN-BEGIN:variables
//    public javax.swing.JButton jButton1;
//    public javax.swing.JButton jButton2;
//    public javax.swing.JButton jButton3;
//    public javax.swing.JButton jButton4;
//    public javax.swing.JButton jButton5;
//    public javax.swing.JButton jButton6;
//    public javax.swing.JButton jButton7;
//    public javax.swing.JButton jButton8;
//    private javax.swing.JLabel jLabel1;
//    private javax.swing.JLabel jLabel2;
//    private javax.swing.JLabel jLabel3;
//    private javax.swing.JLabel jLabel4;
//    private javax.swing.JLabel jLabel5;
//    private javax.swing.JLabel jLabel6;
//    private javax.swing.JLabel jLabel7;
//    public javax.swing.JList jList1;
//    public javax.swing.JPasswordField jPasswordField1;
//    private javax.swing.JScrollPane jScrollPane1;
//    private javax.swing.JScrollPane jScrollPane2;
//    private javax.swing.JSeparator jSeparator1;
//    private javax.swing.JSeparator jSeparator2;
//    public javax.swing.JTextArea jTextArea1;
//    public javax.swing.JTextField jTextField1;
//    public javax.swing.JTextField jTextField2;
//    public javax.swing.JTextField jTextField3;
//    public javax.swing.JTextField jTextField4;
//    public javax.swing.JTextField jTextField5;
//    public javax.swing.JTextField jTextField6;
//    // End of variables declaration//GEN-END:variables
//}
