package teamProject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class ChatClient extends JFrame {
    private JButton createRoomButton; // 방 생성 버튼
    private JButton deleteRoomButton; // 방 삭제 버튼
    private JButton profileButton; // 프로필 버튼
    private JButton chatRoomsButton; // 채팅방 버튼
    private JPanel roomPanel; // 방 목록을 표시하는 JPanel
    private Team_chatDAO chatDAO; // 데이터베이스 처리를 담당하는 DAO 객체
    private String currentUser; // 현재 사용자 ID
    private RoomPanel selectedRoomPanel; // 현재 선택된 방 패널

    public ChatClient(String currentUser) {
        this.currentUser = currentUser; // 현재 사용자 ID 설정
        setTitle("Multi Chat Client - " + currentUser); // 프레임 타이틀 설정
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 닫기 버튼 동작 설정
        setSize(400, 600); // 프레임 크기 설정
        setLocationRelativeTo(null); // 화면 중앙에 프레임 표시

        chatDAO = new Team_chatDAO(); // DAO 객체 초기화

        initGUI(); // GUI 초기화 메서드 호출

        setVisible(true); // 프레임을 보이도록 설정
    }

    private void initGUI() {
        // Layout 설정
        setLayout(new BorderLayout());

        // 방 목록 설정 (JPanel)
        roomPanel = new JPanel();
        roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.Y_AXIS));

        // 방 목록이 많을 경우 스크롤이 가능하도록 JScrollPane 추가
        JScrollPane roomScrollPane = new JScrollPane(roomPanel);
        roomScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 항상 세로 스크롤바 표시
        roomScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 가로 스크롤바는 표시하지 않음
        roomScrollPane.setPreferredSize(new Dimension(150, 0)); // 크기 조정
        add(roomScrollPane, BorderLayout.CENTER); // 방 목록을 프레임의 중앙에 추가하여 가로로 꽉 차게 설정

        // 방 생성 버튼 설정 (JButton)
        createRoomButton = new JButton("방 생성");
        createRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRoom(); // 방 생성 버튼 클릭 시 createRoom 메서드 호출
            }
        });

        // 방 삭제 버튼 설정 (JButton)
        deleteRoomButton = new JButton("방 삭제");
        deleteRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoom(); // 방 삭제 버튼 클릭 시 deleteRoom 메서드 호출
            }
        });

        // 버튼 패널 설정 (FlowLayout을 사용하여 버튼 간 간격 추가)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0)); // 버튼 간 가로 간격 30픽셀
        buttonPanel.add(createRoomButton);
        buttonPanel.add(deleteRoomButton); // 방 삭제 버튼 추가
        add(buttonPanel, BorderLayout.NORTH); // 버튼 패널을 프레임의 상단에 추가

        // 사용자 정보 패널 추가 (프레임 하단에)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0)); // 버튼 간 간격 30픽셀
        bottomPanel.setPreferredSize(new Dimension(0, 40)); // 세로 크기 40 설정

        // 유저 프로필 버튼 (예시로 JButton 사용)
        profileButton = new JButton("프로필");
        profileButton.setPreferredSize(new Dimension(80, 30)); // 버튼 크기 설정
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 사용자 프로필 버튼 클릭 시 동작 (추가적인 클래스나 기능으로 이동)
                JOptionPane.showMessageDialog(ChatClient.this, "프로필 버튼 클릭됨: " + currentUser);
                // 예를 들어, 아래와 같이 새로운 클래스 호출
                // new UserProfile(currentUser);
            }
        });
        bottomPanel.add(profileButton);

        // 채팅방 버튼 추가
        chatRoomsButton = new JButton("채팅방");
        chatRoomsButton.setPreferredSize(new Dimension(80, 30)); // 버튼 크기 설정
        chatRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 채팅방 버튼 클릭 시 동작 (예: 채팅방 목록 창 열기)
                JOptionPane.showMessageDialog(ChatClient.this, "채팅방 버튼 클릭됨");
                // 예를 들어, 아래와 같이 새로운 클래스 호출
                // new ChatRoomsWindow(currentUser);
            }
        });
        bottomPanel.add(chatRoomsButton);

        add(bottomPanel, BorderLayout.SOUTH); // 사용자 정보 패널을 프레임의 하단에 추가

        // 방 목록 업데이트
        updateRoomList();
    }

    private void createRoom() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField roomNameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        panel.add(new JLabel("방 이름 :"));
        panel.add(roomNameField);
        panel.add(new JLabel("비밀번호 설정 :"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "방 생성", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String roomName = roomNameField.getText();
            String password = new String(passwordField.getPassword()).trim();

            if (roomName != null && !roomName.trim().isEmpty()) {
                chatDAO.createChatRoom(roomName, currentUser, password); // DAO를 통해 방 생성
                JOptionPane.showMessageDialog(null, roomName + " 방이 생성 되었습니다.", "방 생성 완료", JOptionPane.INFORMATION_MESSAGE);
                updateRoomList(); // 방 목록 업데이트
            }
        }
    }

    private void deleteRoom() {
        if (selectedRoomPanel != null) {
            String selectedRoom = selectedRoomPanel.getRoomName();
            int confirm = JOptionPane.showConfirmDialog(this, "정말로 " + selectedRoom + " 방을 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean isDeleted = chatDAO.deleteChatRoom(selectedRoom, currentUser);
                if (isDeleted) {
                    JOptionPane.showMessageDialog(null, selectedRoom + " 방이 삭제 되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
                    updateRoomList(); // 방 목록 업데이트
                } else {
                    JOptionPane.showMessageDialog(this, "방 삭제에 실패했습니다. 권한이 없거나 방이 존재하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 방을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateRoomList() {
        List<String> roomNames = chatDAO.getChatRooms(); // DAO를 통해 방 목록 가져오기
        roomPanel.removeAll(); // 방 목록 패널 초기화
        selectedRoomPanel = null; // 선택된 방 초기화
        for (String room : roomNames) {
            RoomPanel panel = new RoomPanel(room);
            panel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // 더블 클릭 시
                        openChatRoom(room); // 채팅방 열기 메서드 호출
                    } else if (e.getClickCount() == 1) { // 싱글 클릭 시
                        setSelectedRoomPanel(panel); // 선택된 방 업데이트
                    }
                }
            });
            roomPanel.add(panel); // 방 목록 패널에 방 패널 추가
        }
        roomPanel.revalidate(); // 레이아웃 갱신
        roomPanel.repaint(); // 패널 다시 그리기
    }

    private void setSelectedRoomPanel(RoomPanel panel) {
        if (selectedRoomPanel != null) {
            selectedRoomPanel.setSelected(false); // 이전에 선택된 패널 비활성화
        }
        selectedRoomPanel = panel;
        selectedRoomPanel.setSelected(true); // 새로 선택된 패널 활성화
    }

    private void openChatRoom(String roomName) {
        // 방의 패스워드를 가져오는 DAO 메서드 추가 필요
        String roomPassword = chatDAO.getRoomPassword(roomName);

        if (roomPassword != null && !roomPassword.isEmpty()) {
            // 패스워드를 입력받기 위한 패널
            JPanel panel = new JPanel(new GridLayout(1, 2));
            JPasswordField passwordField = new JPasswordField();
            panel.add(new JLabel("패스워드 입력:"));
            panel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, panel, "방에 입장하기 위한 패스워드", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String enteredPassword = new String(passwordField.getPassword()).trim();
                if (enteredPassword.equals(roomPassword)) {
                    // 패스워드가 맞으면 채팅방 열기 
                    SwingUtilities.invokeLater(() -> new MultiChatClient(roomName, currentUser));
                } else {
                    JOptionPane.showMessageDialog(this, "패스워드가 올바르지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            // 패스워드가 없으면 바로 채팅방 열기
            SwingUtilities.invokeLater(() -> new MultiChatClient(roomName, currentUser));
        }
    }

    // 방 목록의 각 방을 나타내는 패널 클래스
    private class RoomPanel extends JPanel {
        private String roomName;
        private boolean selected;
        private JLabel lastMessageLabel; // 마지막 채팅 내용을 표시하는 레이블

        public RoomPanel(String roomName) {
            this.roomName = roomName;
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.GRAY));

            // 제목 레이블
            JLabel roomLabel = new JLabel(roomName);
            roomLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
            add(roomLabel, BorderLayout.NORTH);

            // 마지막 채팅 내용을 표시할 레이블
            lastMessageLabel = new JLabel("최근 메시지 없음");
            lastMessageLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            add(lastMessageLabel, BorderLayout.CENTER);

            setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // 세로 크기를 60으로 설정

            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    setSelected(true);
                }
            });

            // 방의 마지막 채팅 내용을 업데이트하는 메서드 호출
            updateLastMessage();
        }

        private void updateLastMessage() {
            // DAO에서 마지막 채팅 내용을 가져오는 메서드 호출
            String lastMessage = chatDAO.getLastMessage(roomName);
            lastMessageLabel.setText(lastMessage != null ? lastMessage : "최근 메시지 없음");
        }

        public String getRoomName() {
            return roomName;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            setBackground(selected ? Color.LIGHT_GRAY : Color.WHITE);	
        }
    }

    public static void main(String[] args) {	
        SwingUtilities.invokeLater(() -> new ChatClient("사용자1")); // 프로그램 실행
    }
}
