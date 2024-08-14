package teamProject;

import javax.swing.*;
import teamProject_report.ReportApp;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class MultiChatClient extends JFrame {
    private JList<String> chatList; // 채팅 메시지를 표시할 리스트
    private DefaultListModel<String> chatListModel; // 채팅 리스트 모델
    private JList<String> userList; // 사용자 목록을 표시할 리스트
    private DefaultListModel<String> userListModel; // 사용자 리스트 모델
    private Socket socket; // 서버와의 소켓 연결
    private ObjectOutputStream oos; // 서버로 데이터를 보내기 위한 스트림
    private ObjectInputStream ois; // 서버로부터 데이터를 받기 위한 스트림
    private String roomName; // 현재 채팅방의 이름
    private String currentUser; // 현재 사용자의 이름
    private int roomId; // 현재 채팅방의 ID

    // 이모지 목록
    private static final String[] REACTIONS = {"👍", "❤️", "😂", "😮", "😢", "😡"};

    public MultiChatClient(String roomName, String currentUser) {
        super("Network Drawing Client - " + roomName);
        this.roomName = roomName;
        this.currentUser = currentUser;

        // roomName으로 roomId를 조회하여 설정
        Team_chatDAO dao = new Team_chatDAO();
        this.roomId = dao.getRoomIdByName(roomName);  // roomName으로 roomId를 가져옴

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 시 종료
        setSize(800, 600); // 창 크기 설정
        setLayout(new BorderLayout()); // 레이아웃 설정

        initializeComponents(); // UI 컴포넌트 초기화
        loadChatHistory(); // 채팅 기록 불러오기
        setupNetworking(); // 네트워킹 설정
        setVisible(true); // 창 표시
    }

    private void initializeComponents() {
        // 채팅 리스트 모델과 채팅 리스트 초기화
        chatListModel = new DefaultListModel<>();
        chatList = new JList<>(chatListModel);
        chatList.setBorder(BorderFactory.createTitledBorder("Chat")); // 채팅 리스트에 제목 추가

        JScrollPane chatScrollPane = new JScrollPane(chatList);
        chatScrollPane.setPreferredSize(new Dimension(560, 600)); // 채팅 스크롤 패널 크기 설정

        // 사용자 리스트 모델과 사용자 리스트 초기화
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBorder(BorderFactory.createTitledBorder("Users")); // 사용자 리스트에 제목 추가

        JScrollPane userScrollPane = new JScrollPane(userList);

        // 사용자 목록에서 우클릭 시 표시될 컨텍스트 메뉴 초기화
        JPopupMenu userPopupMenu = new JPopupMenu();
        JMenuItem viewProfileMenuItem = new JMenuItem("프로필");
        JMenuItem sendWhisperMenuItem = new JMenuItem("귓속말");
        JMenuItem reportMenuItem = new JMenuItem("신고하기");

        userPopupMenu.add(viewProfileMenuItem);
        userPopupMenu.add(sendWhisperMenuItem);
        userPopupMenu.add(reportMenuItem);

        // 사용자 목록에서 우클릭 이벤트 처리
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && userList.locationToIndex(e.getPoint()) != -1) {
                    // 로그 추가
                    System.out.println("Right-click detected on userList");

                    // 선택된 사용자의 이름을 가져옴
                    userList.setSelectedIndex(userList.locationToIndex(e.getPoint()));
                    String selectedUser = userList.getSelectedValue();

                    // 팝업 메뉴를 마우스 위치에 표시
                    userPopupMenu.show(userList, e.getX(), e.getY());

                    // 프로필 보기 메뉴 아이템 클릭 시 이벤트 처리
                    viewProfileMenuItem.addActionListener(ae -> {
                        JOptionPane.showMessageDialog(null, selectedUser + "의 프로필을 봅니다.");
                        // 프로필 조회 로직 추가
                        viewUserProfile(selectedUser);
                    });

                    // 귓속말 보내기 메뉴 아이템 클릭 시 이벤트 처리
                    sendWhisperMenuItem.addActionListener(ae -> {
                        String message = JOptionPane.showInputDialog("귓속말 메시지를 입력하세요:");
                        if (message != null && !message.trim().isEmpty()) {
                            System.out.println("귓속말 to " + selectedUser + ": " + message);
                            // 귓속말 전송 로직 추가
                            sendWhisper(selectedUser, message);
                        }
                    });

                    // 신고하기 메뉴 아이템 클릭 시 이벤트 처리
                    reportMenuItem.addActionListener(ae -> {
                        int confirm = JOptionPane.showConfirmDialog(null, selectedUser + "을(를) 신고하시겠습니까?", "신고하기", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // ReportApp 생성자를 호출하여 신고 창을 띄움, roomId를 추가로 전달
                            new ReportApp(currentUser, selectedUser, Integer.toString(roomId)).setVisible(true);
                        }
                    });
                }
            }
        });

        // 채팅 메시지 우클릭 메뉴 설정
        JPopupMenu chatPopupMenu = new JPopupMenu();
        JMenuItem addReactionMenuItem = new JMenuItem("반응 추가");
        JMenuItem deleteMessageMenuItem = new JMenuItem("메시지 삭제");

        chatPopupMenu.add(addReactionMenuItem);
        chatPopupMenu.add(deleteMessageMenuItem);

        chatList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && chatList.locationToIndex(e.getPoint()) != -1) {
                    // 로그 추가
                    System.out.println("Right-click detected on chatList");

                    // 선택된 메시지를 가져옴
                    int index = chatList.locationToIndex(e.getPoint());
                    String selectedMessage = chatList.getModel().getElementAt(index);
                    chatList.setSelectedIndex(index);

                    // 팝업 메뉴를 마우스 위치에 표시
                    chatPopupMenu.show(chatList, e.getX(), e.getY());

                    // 반응 추가 메뉴 아이템 클릭 시 이벤트 처리
                    addReactionMenuItem.addActionListener(ae -> {
                        // 이모지 선택 대화상자 표시
                        String reaction = (String) JOptionPane.showInputDialog(
                                MultiChatClient.this,
                                "반응을 선택하세요:",
                                "반응 추가",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                REACTIONS,
                                REACTIONS[0]
                        );
                        if (reaction != null) {
                            System.out.println("Add reaction to message: " + selectedMessage);
                            // 반응 추가 로직 추가
                            addReactionToMessage(selectedMessage, reaction);
                        }
                    });

                    // 메시지 삭제 메뉴 아이템 클릭 시 이벤트 처리
                    deleteMessageMenuItem.addActionListener(ae -> {
                        int confirm = JOptionPane.showConfirmDialog(null, "정말로 이 메시지를 삭제하시겠습니까?", "메시지 삭제", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            System.out.println("Delete message: " + selectedMessage);
                            // 메시지 삭제 로직 추가
                            deleteMessage(selectedMessage);
                        }
                    });
                }
            }
        });

        // 나가기 버튼과 초대 버튼 초기화
        JButton leaveButton = new JButton("나가기");
        JButton inviteButton = new JButton("초대하기");

        Dimension buttonSize = new Dimension(100, 30);
        leaveButton.setPreferredSize(buttonSize);
        inviteButton.setPreferredSize(buttonSize);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(leaveButton);
        buttonPanel.add(inviteButton);

        // 오른쪽 패널 초기화
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(userScrollPane, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(240, 600));

        // 채팅 입력 필드와 보내기 버튼 초기화
        JTextField chatField = new JTextField();
        JButton sendButton = new JButton("전송하기");

        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.add(chatField, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);

        add(chatScrollPane, BorderLayout.CENTER); // 채팅 리스트 추가
        add(rightPanel, BorderLayout.EAST); // 오른쪽 패널 추가
        add(chatInputPanel, BorderLayout.SOUTH); // 채팅 입력 패널 추가

        // 메시지 보내기 액션 리스너 설정
        ActionListener sendAction = e -> {
            String text = chatField.getText();
            if (!text.trim().isEmpty()) {
                System.out.println("chat: " + currentUser + " : " + text);
                try {
                    DataPost dp = new DataPost();
                    dp.setChat("chat: " + text);
                    oos.writeObject(dp);
                    oos.flush();

                    // 메시지를 DB에 저장
                    Team_chatDAO dao = new Team_chatDAO();
                    dao.saveChatMessage(roomId, currentUser, text);

                } catch (IOException e1) {
                    System.out.println(e1.getMessage());
                }
                chatField.setText("");
            }
        };

        chatField.addActionListener(sendAction);
        sendButton.addActionListener(sendAction);

        // 나가기 버튼 액션 리스너 설정
        leaveButton.addActionListener(e -> {
            try {
                oos.close();
                ois.close();
                //socket.close();
            } catch (IOException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
            returnToChatClient();
        });

        // 초대 버튼 액션 리스너 설정
        inviteButton.addActionListener(e -> {
            String email = JOptionPane.showInputDialog(this, "초대하고 싶은 친구의 아이디를 입력해주세요.");
            if (email != null && !email.trim().isEmpty()) {
                System.out.println("Invite email: " + email);
                // 초대 기능 메소드 호출
                inviteUser(email);
            }
        });
    }

    // 사용자 프로필 조회 메소드
    private void viewUserProfile(String userName) {
        // 프로필 조회 로직 추가
        JOptionPane.showMessageDialog(this, userName + "의 프로필을 표시합니다.");
    }

    // 귓속말 전송 메소드
    private void sendWhisper(String userName, String message) {
        try {
            DataPost dp = new DataPost();
            dp.setChat("whisper:" + userName + ":" + message);
            oos.writeObject(dp);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Error sending whisper: " + e.getMessage());
        }
    }

    // 초대 기능 메소드
    private void inviteUser(String email) {
        // 초대 로직 추가
        System.out.println("User invited: " + email);
    }

 // 반응 추가 메소드
    private void addReactionToMessage(String message, String reaction) {
        // DAO를 통해 메시지에 반응 추가
        Team_chatDAO dao = new Team_chatDAO();
        dao.addReactionToMessage(message, reaction);

        // UI 업데이트
        String updatedMessage = message + " (반응: " + reaction + ")";
        int index = chatListModel.indexOf(message);
        if (index != -1) {
            chatListModel.set(index, updatedMessage);
        }
    }

 // 메시지 삭제 메소드
    private void deleteMessage(String message) {
        // DAO를 통해 메시지 삭제
        Team_chatDAO dao = new Team_chatDAO();
        dao.deleteMessage(message);

        // UI 업데이트
        chatListModel.removeElement(message);
    }

 // 채팅 기록을 불러와서 리스트 모델에 추가
    private void loadChatHistory() {
        Team_chatDAO dao = new Team_chatDAO();
        List<String> messages = dao.getMessagesByRoomId(roomId);
        for (String message : messages) {
            chatListModel.addElement(message);
        }
    }

    private void setupNetworking() {
        try {
            socket = new Socket("localhost", 5000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            // 클라이언트의 이름을 서버로 전송 (예: user:username)
            DataPost userDataPost = new DataPost();
            userDataPost.setChat("user:" + currentUser);
            oos.writeObject(userDataPost);
            oos.flush();

            Thread listenerThread = new Thread(this::listenForMessages);
            listenerThread.start();
        } catch (IOException e) {
            System.out.println("Unable to connect to the server.");
            e.printStackTrace();
        }
    }

    // 서버로부터 메시지를 수신하는 메소드
    private void listenForMessages() {
        try {
            ois = new ObjectInputStream(socket.getInputStream()); // 입력 스트림 초기화
            DataPost receivedDataPost;

            while ((receivedDataPost = (DataPost) ois.readObject()) != null) {
                if (receivedDataPost.getChat().startsWith("chat:")) {
                    // 채팅 메시지 처리
                    String chatMessage = receivedDataPost.getChat().substring(5);
                    SwingUtilities.invokeLater(() -> chatListModel.addElement(chatMessage)); // UI 스레드에서 채팅 메시지 추가
                } else if (receivedDataPost.getChat().startsWith("user:")) {
                    // 새로운 사용자 접속 처리
                    String userName = receivedDataPost.getChat().substring(5);
                    SwingUtilities.invokeLater(() -> {
                        if (!userListModel.contains(userName)) { // 중복 방지
                            userListModel.addElement(userName);
                        }
                    });
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from the server: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }

    // 채팅 클라이언트로 돌아가는 메소드
    private void returnToChatClient() {
        SwingUtilities.invokeLater(() -> {
            dispose(); // 현재 창 닫기
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MultiChatClient("Test Room", "Test User")); // 프로그램 실행
    }
}
