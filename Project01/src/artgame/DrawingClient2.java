package artgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class DrawingClient2 extends JFrame {
    private JList<String> chatList; // 채팅 메시지를 표시할 리스트
    private DefaultListModel<String> chatListModel; // chatList의 모델
    private JList<String> userList; // 접속 중인 사용자 목록을 표시할 리스트
    private DefaultListModel<String> userListModel; // userList의 모델
    private Socket socket; // 서버와의 연결을 위한 소켓
    private ObjectOutputStream oos; // 직렬화된 객체를 보내기 위한 출력 스트림
    private ObjectInputStream ois; // 직렬화된 객체를 받기 위한 입력 스트림

    public DrawingClient2() {
        super("Network Drawing Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // 프레임 크기 설정
        setLayout(new BorderLayout());

        initializeComponents(); // 컴포넌트를 초기화
        setupNetworking(); // 네트워킹 설정
        setVisible(true); // 프레임을 화면에 표시
    }

    private void initializeComponents() {
        // 채팅 리스트 초기화 및 모델 설정
        chatListModel = new DefaultListModel<>();
        chatList = new JList<>(chatListModel);
        chatList.setBorder(BorderFactory.createTitledBorder("Chat"));

        // 채팅 리스트를 포함할 스크롤 패널 생성
        JScrollPane chatScrollPane = new JScrollPane(chatList);
        chatScrollPane.setPreferredSize(new Dimension(560, 600)); // 프레임 크기의 70%

        // 사용자 리스트 초기화 및 모델 설정
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBorder(BorderFactory.createTitledBorder("Users"));

        // 사용자 리스트를 포함할 스크롤 패널 생성
        JScrollPane userScrollPane = new JScrollPane(userList);

        // 나가기 버튼과 초대 버튼 생성
        JButton leaveButton = new JButton("Leave");
        JButton inviteButton = new JButton("Invite");

        // 버튼의 크기를 작게 설정
        Dimension buttonSize = new Dimension(100, 30);
        leaveButton.setPreferredSize(buttonSize);
        inviteButton.setPreferredSize(buttonSize);

        // 나가기 버튼과 초대 버튼을 포함할 패널 생성
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 가로로 배치
        buttonPanel.add(leaveButton);
        buttonPanel.add(inviteButton);

        // 사용자 리스트와 버튼 패널을 포함할 우측 패널 생성
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(userScrollPane, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(240, 600)); // 프레임 크기의 30%

        // 채팅 입력 필드와 버튼
        JTextField chatField = new JTextField();
        JButton sendButton = new JButton("Send");

        // 채팅 입력 패널 생성
        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.add(chatField, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);

        // 좌측에 채팅 내역 추가
        add(chatScrollPane, BorderLayout.CENTER);

        // 우측에 사용자 리스트와 버튼 패널 추가
        add(rightPanel, BorderLayout.EAST);

        // 하단에 채팅 입력 패널 추가
        add(chatInputPanel, BorderLayout.SOUTH);

        // Enter 키나 Send 버튼을 눌렀을 때 메시지 전송
        ActionListener sendAction = e -> {
            String text = chatField.getText();
            if (!text.trim().isEmpty()) {
                System.out.println("chat: " + text); // 콘솔에 텍스트 출력
                try {
                    DataPost dp = new DataPost();
                    dp.setChat("chat: " + text);
                    oos.writeObject(dp); // 메시지를 직렬화하여 서버로 전송
                    oos.flush();
                } catch (IOException e1) {
                    System.out.println(e1.getMessage());
                }
                chatField.setText(""); // 메시지 전송 후 텍스트 필드 클리어
            }
        };

        chatField.addActionListener(sendAction);
        sendButton.addActionListener(sendAction);

        // 나가기 버튼 액션 리스너
        leaveButton.addActionListener(e -> {
            // 서버 연결을 종료하고 프로그램을 종료
            try {
                oos.close();
                ois.close();
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
            System.exit(0);
        });

        // 초대 버튼 액션 리스너
        inviteButton.addActionListener(e -> {
            // 친구를 초대하는 기능 구현 (예: 다이얼로그를 통해 이메일 입력 받기)
            String email = JOptionPane.showInputDialog(this, "Enter email to invite:");
            if (email != null && !email.trim().isEmpty()) {
                System.out.println("Invite email: " + email); // 초대 이메일을 콘솔에 출력
                // 실제 초대 기능 구현
            }
        });
    }

    private void setupNetworking() {
        try {
            socket = new Socket("localhost", 5000); // 서버에 연결
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            // 서버로부터 메시지를 듣는 스레드 시작
            Thread listenerThread = new Thread(this::listenForMessages);
            listenerThread.start();
        } catch (IOException e) {
            System.out.println("Unable to connect to the server.");
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            // 객체를 읽어서 적절한 타입으로 캐스팅
            ois = new ObjectInputStream(socket.getInputStream());
            DataPost receivedDataPost;

            while ((receivedDataPost = (DataPost) ois.readObject()) != null) {
                if (receivedDataPost.getChat().startsWith("chat:")) {
                    String chatMessage = receivedDataPost.getChat().substring(5);
                    SwingUtilities.invokeLater(() -> chatListModel.addElement(chatMessage));
                } else if (receivedDataPost.getChat().startsWith("user:")) {
                    String userName = receivedDataPost.getChat().substring(5);
                    SwingUtilities.invokeLater(() -> userListModel.addElement(userName));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from the server: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawingClient2::new); // 프로그램 실행
    }
}
