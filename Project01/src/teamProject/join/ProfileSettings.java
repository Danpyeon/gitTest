package teamProject.join;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import teamProject.components.TitleBar;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ProfileSettings extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProfileSettings frame = new ProfileSettings();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ProfileSettings() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setUndecorated(true); // 타이틀 바 제거
        contentPane = new JPanel();
        contentPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(220, 220, 220)));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // TitleBar 추가
        TitleBar titleBar = new TitleBar(this);
        titleBar.setBounds(0, 0, 800, 30);
        contentPane.add(titleBar);

        // contentPane 배경 설정
        contentPane.setBackground(Color.WHITE);

        // 프로필 정보 패널
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(null);
        profilePanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBounds(20, 60, 360, 120);
        contentPane.add(profilePanel);

        JLabel profileLabel = new JLabel("프로필");
        profileLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        profileLabel.setBounds(20, 20, 100, 30);
        profilePanel.add(profileLabel);

        JLabel profileInfoLabel = new JLabel("고객과 대니ㅤㅤ에게 보이는 정보를 설정하세요.");
        profileInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        profileInfoLabel.setBounds(20, 50, 320, 30);
        profilePanel.add(profileInfoLabel);

        // 연락처 정보 패널
        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(null);
        contactPanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        contactPanel.setBackground(Color.WHITE);
        contactPanel.setBounds(20, 200, 360, 120);
        contentPane.add(contactPanel);

        JLabel contactLabel = new JLabel("연락처");
        contactLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        contactLabel.setBounds(20, 20, 100, 30);
        contactPanel.add(contactLabel);

        JLabel contactInfoLabel = new JLabel("연락처와 이메일을 설정하세요.");
        contactInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        contactInfoLabel.setBounds(20, 50, 320, 30);
        contactPanel.add(contactInfoLabel);

        // 사용자 지정 정보 패널
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(null);
        userInfoPanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.setBounds(20, 340, 360, 120);
        contentPane.add(userInfoPanel);

        JLabel userInfoLabel = new JLabel("사용자 지정 정보");
        userInfoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        userInfoLabel.setBounds(20, 20, 200, 30);
        userInfoPanel.add(userInfoLabel);

        JLabel userInfoDescriptionLabel = new JLabel("기본 직책 외에 추가 정보를 입력하세요.");
        userInfoDescriptionLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        userInfoDescriptionLabel.setBounds(20, 20, 320, 30);
        userInfoPanel.add(userInfoDescriptionLabel);

        // 고객에게 공개 패널
        JPanel publicPanel = new JPanel();
        publicPanel.setLayout(null);
        publicPanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        publicPanel.setBackground(Color.WHITE);
        publicPanel.setBounds(20, 480, 360, 120);
        contentPane.add(publicPanel);

        JLabel publicLabel = new JLabel("고객에게 공개");
        publicLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        publicLabel.setBounds(20, 20, 100, 30);
        publicPanel.add(publicLabel);

        JLabel publicInfoLabel = new JLabel("고객과 공유할 정보를 입력하세요.");
        publicInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        publicInfoLabel.setBounds(20, 50, 320, 30);
        publicPanel.add(publicInfoLabel);

        // 오른쪽 사용자 정보 패널
        JPanel userInfoRightPanel = new JPanel();
        userInfoRightPanel.setLayout(null);
        userInfoRightPanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        userInfoRightPanel.setBackground(Color.WHITE);
        userInfoRightPanel.setBounds(400, 60, 360, 540);
        contentPane.add(userInfoRightPanel);

        JLabel userImageLabel = new JLabel(new ImageIcon("path_to_image")); // 사용자 이미지 경로 설정
        userImageLabel.setBounds(120, 20, 120, 120);
        userInfoRightPanel.add(userImageLabel);

        JLabel userNameLabel = new JLabel("코니");
        userNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        userNameLabel.setBounds(160, 160, 200, 30);
        userInfoRightPanel.add(userNameLabel);

        JLabel userRoleLabel = new JLabel("CX Manager");
        userRoleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        userRoleLabel.setBounds(160, 200, 200, 30);
        userInfoRightPanel.add(userRoleLabel);

        JButton messageButton = new JButton("메시지");
        messageButton.setBounds(140, 240, 80, 30);
        userInfoRightPanel.add(messageButton);

        // 연락처 정보
        JLabel phoneLabel = new JLabel(new ImageIcon("path_to_phone_icon")); // 전화 아이콘 경로 설정
        phoneLabel.setText("010-1234-1234");
        phoneLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        phoneLabel.setBounds(140, 300, 200, 30);
        userInfoRightPanel.add(phoneLabel);

        JLabel emailLabel = new JLabel(new ImageIcon("path_to_email_icon")); // 이메일 아이콘 경로 설정
        emailLabel.setText("conie@channel.io");
        emailLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        emailLabel.setBounds(140, 340, 200, 30);
        userInfoRightPanel.add(emailLabel);

        JLabel kakaotalkLabel = new JLabel(new ImageIcon("path_to_kakaotalk_icon")); // 카카오톡 아이콘 경로 설정
        kakaotalkLabel.setText("연락 가능");
        kakaotalkLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        kakaotalkLabel.setBounds(140, 380, 200, 30);
        userInfoRightPanel.add(kakaotalkLabel);
    }
}
