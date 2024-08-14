package teamProject_report;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatRoomUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    public ChatRoomUI() {
        setTitle("채팅방");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("전송");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String message = messageField.getText();
        // 메시지 전송 로직 구현 (DAO 사용 등)
        // chatArea.append("사용자명: " + message + "\n"); // 예시로 추가

        messageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChatRoomUI chatRoomUI = new ChatRoomUI();
                chatRoomUI.setVisible(true);
            }
        });
    }
}