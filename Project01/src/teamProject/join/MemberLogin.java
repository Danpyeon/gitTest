package teamProject.join;

import teamProject.components.TitleBar;
import teamProject.ChatClient;
import teamProject.MultiServer;
import teamProject.components.HintRoundedPasswordField;
import teamProject.components.HintRoundedTextField;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MemberLogin extends JFrame implements ActionListener {

    private JTextField userTextField; // ID 입력창
    private JPasswordField passwordField; // 비밀번호 입력창
    private JButton loginButton; // 로그인 버튼
    private JLabel joinLabel; // 회원가입 텍스트
    private JLabel findIdLabel; // 아이디 찾기 텍스트
    private JLabel findPasswordLabel; // 비밀번호 찾기 텍스트
    private JLabel minimizeLabel; // 내림 버튼
    private JLabel closeLabel; // 닫기 버튼

    private Point initialClick; // 창 이동을 위한 포인트

    private static final String PLACEHOLDER_PASSWORD = "비밀번호를 입력하세요";

    public MemberLogin() {
        setTitle("로그인 화면");
        setSize(350, 600); // 창의 크기 설정
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // 기본 타이틀바를 제거합니다.
        setLayout(new BorderLayout());

        // 타이틀바 패널
        TitleBar titleBarPanel = new TitleBar(this);
        
        add(titleBarPanel, BorderLayout.NORTH);

        // 전체 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE); // 배경색 설정
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 간 여백 설정
        gbc.anchor = GridBagConstraints.CENTER;

        // 로고 또는 타이틀
        JLabel titleLabel = new JLabel("MyApp");
        titleLabel.setFont(new Font("돋움", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        // 입력 필드 패널 생성
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null); // 절대 레이아웃
        inputPanel.setOpaque(false); // 배경색 제거
        inputPanel.setPreferredSize(new Dimension(250, 80)); // 패널 크기 조정 (여유 공간 축소)

        // ID 입력
        userTextField = HintRoundedTextField.create("아이디를 입력하세요");
        userTextField.setBounds(0, 0, 250, 40); // 위치와 크기 설정
        inputPanel.add(userTextField);

        // 비밀번호 입력
        passwordField = HintRoundedPasswordField.create(PLACEHOLDER_PASSWORD);
        passwordField.setBounds(0, 40, 250, 40); // 위치와 크기 설정 (간격을 줄임)
        inputPanel.add(passwordField);

        // 입력 필드 패널에 추가
        gbc.gridy = 1;
        mainPanel.add(inputPanel, gbc);

        // 로그인 버튼
        loginButton = new JButton("로그인");
        loginButton.setFont(new Font("돋움", Font.BOLD, 16));
        loginButton.setBackground(Color.decode("#90B494")); // 버튼 배경색
        loginButton.setForeground(Color.BLACK); // 글자색을 검정색으로 설정
        loginButton.setPreferredSize(new Dimension(250, 35)); // 필드와 동일한 너비로 조정
        loginButton.setBorder(BorderFactory.createLineBorder(Color.decode("#B0B0B0"))); // 버튼 테두리 색상: 약간 진한 회색
        loginButton.setFocusPainted(false); // 버튼 포커스 효과 제거
        loginButton.setOpaque(true); // 배경색 적용
        loginButton.setEnabled(false); // 초기에는 버튼을 비활성화
        loginButton.addActionListener(this);
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 10, 10); // 버튼과 필드 간 간격을 좁힘
        mainPanel.add(loginButton, gbc);

        // 링크 패널
        JPanel linkPanel = new JPanel();
        linkPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 중앙 정렬 및 간격 설정
        linkPanel.setOpaque(false);

        joinLabel = new JLabel("<html><u>회원가입</u></html>");
        joinLabel.setFont(new Font("돋움", Font.PLAIN, 14));
        joinLabel.setForeground(Color.GRAY); // 텍스트 색상 설정
        joinLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        joinLabel.setOpaque(false); // 배경색 제거
        joinLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 회원가입 처리
                dispose();
                new JoinPage().setVisible(true);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                joinLabel.setForeground(Color.GRAY); // 색상 변경
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                joinLabel.setForeground(Color.GRAY); // 색상 변경
            }
        });

        findIdLabel = new JLabel("<html><u>아이디 찾기</u></html>");
        findIdLabel.setFont(new Font("돋움", Font.PLAIN, 14));
        findIdLabel.setForeground(Color.GRAY); // 텍스트 색상 설정
        findIdLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        findIdLabel.setOpaque(false); // 배경색 제거
        findIdLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 아이디 찾기 처리
                new FindIDPassword().setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                findIdLabel.setForeground(Color.GRAY); // 색상 변경
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                findIdLabel.setForeground(Color.GRAY); // 색상 변경
            }
        });

        findPasswordLabel = new JLabel("<html><u>비밀번호 찾기</u></html>");
        findPasswordLabel.setFont(new Font("돋움", Font.PLAIN, 14));
        findPasswordLabel.setForeground(Color.GRAY); // 텍스트 색상 설정
        findPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        findPasswordLabel.setOpaque(false); // 배경색 제거
        findPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 비밀번호 찾기 처리
                new FindIDPassword("passwordPanel").setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                findPasswordLabel.setForeground(Color.GRAY); // 색상 변경
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                findPasswordLabel.setForeground(Color.GRAY); // 색상 변경
            }
        });

        linkPanel.add(joinLabel);
        linkPanel.add(findIdLabel);
        linkPanel.add(findPasswordLabel);

        gbc.gridy = 3;
        mainPanel.add(linkPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // 입력 필드에 키 리스너 추가
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateInputs();
            }
        };
        userTextField.addKeyListener(keyAdapter);
        passwordField.addKeyListener(keyAdapter);

        setVisible(true);
    }

    private Border createCustomBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1), // 외부 테두리
            new EmptyBorder(10, 10, 10, 10) // 내부 여백
        );
    }

    private void validateInputs() {
        String username = userTextField.getText();
        String password = new String(passwordField.getPassword());
        if (username.length() >= 6 && password.length() >= 8 && !password.equals(PLACEHOLDER_PASSWORD)) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 로그인 버튼 클릭 시 동작
        String username = userTextField.getText();
        String password = new String(passwordField.getPassword());

        MemberDAO dao = new MemberDAO(); // DAO 객체 생성
        boolean loginSuccess = dao.validateLogin(username, password);

        if (loginSuccess) {
            String userName = dao.getUserName(username);
            JOptionPane.showMessageDialog(this, userName + "님의 방문을 환영합니다!");

            // 서버를 시작하는 부분
            try {
                // 서버 프로세스를 시작합니다.
                ProcessBuilder pb = new ProcessBuilder("java", "-cp", System.getProperty("java.class.path"), "teamProject.MultiServer");
                pb.inheritIO(); // 서버 프로세스의 입출력을 현재 프로세스와 공유
                pb.start();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "서버를 시작하는 데 오류가 발생했습니다.");
                return;
            }

            // ChatClient를 열고
            new ChatClient(username).setVisible(true);
            dispose(); // 로그인 창 닫기
        } else {
            JOptionPane.showMessageDialog(this, "아이디 혹은 비밀번호가 일치하지 않습니다.");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberLogin());
    }
}
