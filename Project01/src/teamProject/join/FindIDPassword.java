package teamProject.join;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;

import teamProject.components.HintRoundedPasswordField;
import teamProject.components.HintRoundedTextField;
import teamProject.components.ResidentNumberFilter;
import teamProject.components.StyledButton;
import teamProject.components.TitleBar;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;

public class FindIDPassword extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HintRoundedTextField idNameField;
    private HintRoundedTextField idResidentNumberFrontField;
    private HintRoundedTextField idResidentNumberBackField;
    private HintRoundedTextField passwordIdField;
    private HintRoundedTextField passwordNameField;
    private HintRoundedTextField passwordResidentNumberFrontField;
    private HintRoundedTextField passwordResidentNumberBackField;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel idFindOptionsPanel;
    private JPanel passwordFindOptionsPanel;
    private JPanel cardPanel2;
    private CardLayout cardLayout2;
    private JPanel cardPanel3;
    private CardLayout cardLayout3;

    private Color labelDefaultTextColor = new Color(220, 220, 220); // 기본 글씨 색상
    private Color labelHoverTextColor = new Color(200, 200, 200); // 호버 시 글씨 색상
    private Color labelClickTextColor = new Color(0x90B494); // 클릭 시 글씨 색상
    private Color labelDefaultBorderColor = new Color(220, 220, 220); // 기본 경계선 색상
    private Color labelClickBorderColor = new Color(0x90B494); // 클릭 시 경계선 색상
    private Color labelHoverBorderColor = new Color(200, 200, 200); // 호버 시 경계선 색상
    private Color frameBackgroundColor = Color.WHITE; // 프레임 전체 배경색

    private JLabel idLabel;
    private JLabel passwordLabel;

    private MemberDAO memberDAO;
    private String userIdForPasswordReset;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FindIDPassword frame = new FindIDPassword();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 기본 생성자
    public FindIDPassword() {
        this("idPanel"); // 기본으로 아이디 찾기 패널을 표시
    }

    // 패널 이름을 받아 초기 패널을 설정하는 생성자
    public FindIDPassword(String initialPanel) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 600);
        setUndecorated(true);

        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(frameBackgroundColor); // 프레임 배경색 설정
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // TitleBar 추가
        TitleBar titleBar = new TitleBar(this);
        titleBar.setBounds(0, 0, 500, 30);
        contentPane.add(titleBar);

        idLabel = createClickableLabel("아이디 찾기");
        idLabel.setBounds(1, 90, 249, 40);
        contentPane.add(idLabel);

        passwordLabel = createClickableLabel("비밀번호 찾기");
        passwordLabel.setBounds(250, 90, 249, 40);
        contentPane.add(passwordLabel);

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        cardPanel.setBounds(50, 150, 400, 400);
        contentPane.add(cardPanel);

        // 아이디 찾기 패널
        JPanel idPanel = new JPanel();
        idPanel.setLayout(null);
        idPanel.setBackground(frameBackgroundColor); // 아이디 패널 배경색 설정

        JLabel idNameLabel = new JLabel("이름");
        idNameLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        idNameLabel.setBounds(20, 30, 100, 30);
        
        idPanel.add(idNameLabel);

        idNameField = new HintRoundedTextField("이름을 입력하세요");
        idNameField.setBounds(20, 60, 360, 35);  // 높이를 35로 변경
        ((AbstractDocument) idNameField.getDocument()).setDocumentFilter(new ResidentNumberFilter(50));
        idPanel.add(idNameField);

        JLabel idResidentNumberLabel = new JLabel("주민번호");
        idResidentNumberLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        idResidentNumberLabel.setBounds(20, 100, 100, 30);
        idPanel.add(idResidentNumberLabel);

        idResidentNumberFrontField = new HintRoundedTextField("앞자리 6자리");
        idResidentNumberFrontField.setBounds(20, 130, 185, 35);  // 높이를 35로 변경
        ((AbstractDocument) idResidentNumberFrontField.getDocument()).setDocumentFilter(new ResidentNumberFilter(6));
        idPanel.add(idResidentNumberFrontField);

        JLabel idResidentNumberSeparator = new JLabel("-");
        idResidentNumberSeparator.setFont(new Font("맑은고딕", Font.BOLD, 16));
        idResidentNumberSeparator.setBounds(215, 129, 10, 35);
        idPanel.add(idResidentNumberSeparator);

        idResidentNumberBackField = new HintRoundedTextField("●");
        idResidentNumberBackField.setBounds(230, 130, 30, 35);  // 높이를 35로 변경
        ((AbstractDocument) idResidentNumberBackField.getDocument()).setDocumentFilter(new ResidentNumberFilter(1));
        idPanel.add(idResidentNumberBackField);

        JLabel idResidentNumberDots = new JLabel("● ● ● ● ● ●");
        idResidentNumberDots.setFont(new Font("맑은고딕", Font.BOLD, 16));
        idResidentNumberDots.setBounds(265, 130, 116, 35);
        idPanel.add(idResidentNumberDots);

        // 아이디 찾기 옵션 패널
        idFindOptionsPanel = new JPanel();
        idFindOptionsPanel.setBounds(20, 165, 360, 40);
        idFindOptionsPanel.setBackground(frameBackgroundColor);
        idFindOptionsPanel.setLayout(new GridLayout(1, 2)); // 가로로 배치

        JRadioButton phoneButton = new JRadioButton("전화번호로 찾기");
        phoneButton.setBackground(frameBackgroundColor);
        phoneButton.setFont(new Font("맑은고딕", Font.BOLD, 14));
        phoneButton.setSelected(true); // 기본 선택
        phoneButton.addActionListener(e -> showPhonePanel());
        idFindOptionsPanel.add(phoneButton);

        JRadioButton emailButton = new JRadioButton("이메일로 찾기");
        emailButton.setBackground(frameBackgroundColor);
        emailButton.setFont(new Font("맑은고딕", Font.BOLD, 14));
        emailButton.addActionListener(e -> showEmailPanel());
        idFindOptionsPanel.add(emailButton);

        ButtonGroup findOptionsGroup = new ButtonGroup();
        findOptionsGroup.add(phoneButton);
        findOptionsGroup.add(emailButton);

        idPanel.add(idFindOptionsPanel);

        cardPanel2 = new JPanel();
        cardLayout2 = new CardLayout();
        cardPanel2.setLayout(cardLayout2);
        cardPanel2.setBounds(0, 205, 400, 70);
        idPanel.add(cardPanel2);

        // 전화번호 입력 패널
        JPanel phoneInputPanel = new JPanel();
        phoneInputPanel.setLayout(null);
        phoneInputPanel.setBackground(Color.WHITE);
        cardPanel2.add(phoneInputPanel, "phonePanel");

        JLabel phoneLabel = new JLabel("전화번호");
        phoneLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        phoneLabel.setBounds(20, 0, 100, 30);
        phoneInputPanel.add(phoneLabel);

        // 앞자리
        HintRoundedTextField phoneFrontField = new HintRoundedTextField("010");
        phoneFrontField.setBounds(20, 30, 60, 35);  // 높이를 35로 변경
        ((AbstractDocument) phoneFrontField.getDocument()).setDocumentFilter(new ResidentNumberFilter(3));
        phoneInputPanel.add(phoneFrontField);

        // 중간 구분선
        JLabel phoneMiddleSeparator = new JLabel("-");
        phoneMiddleSeparator.setFont(new Font("맑은고딕", Font.BOLD, 16));
        phoneMiddleSeparator.setBounds(85, 30, 10, 35);
        phoneInputPanel.add(phoneMiddleSeparator);

        // 중간 자리
        HintRoundedTextField phoneMiddleField = new HintRoundedTextField("0000");
        phoneMiddleField.setBounds(100, 30, 80, 35);  // 높이를 35로 변경
        ((AbstractDocument) phoneMiddleField.getDocument()).setDocumentFilter(new ResidentNumberFilter(4));
        phoneInputPanel.add(phoneMiddleField);

        // 끝자리 구분선
        JLabel phoneEndSeparator = new JLabel("-");
        phoneEndSeparator.setFont(new Font("맑은고딕", Font.BOLD, 16));
        phoneEndSeparator.setBounds(185, 30, 10, 35);
        phoneInputPanel.add(phoneEndSeparator);

        // 끝 자리
        HintRoundedTextField phoneEndField = new HintRoundedTextField("0000");
        phoneEndField.setBounds(200, 30, 80, 35);  // 높이를 35로 변경
        ((AbstractDocument) phoneEndField.getDocument()).setDocumentFilter(new ResidentNumberFilter(4));
        phoneInputPanel.add(phoneEndField);

        // 이메일 입력 패널
        JPanel emailInputPanel = new JPanel();
        emailInputPanel.setLayout(null);
        emailInputPanel.setBackground(Color.WHITE);
        cardPanel2.add(emailInputPanel, "emailPanel");

        JLabel emailLabel = new JLabel("이메일");
        emailLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        emailLabel.setBounds(20, 0, 100, 30);
        emailInputPanel.add(emailLabel);

        // 이메일 입력 필드
        HintRoundedTextField emailField = new HintRoundedTextField("이메일 주소");
        emailField.setBounds(20, 30, 130, 35);  // 높이를 35로 변경
        ((AbstractDocument) emailField.getDocument()).setDocumentFilter(new ResidentNumberFilter(20));
        emailInputPanel.add(emailField);

        // @ 라벨
        JLabel atLabel = new JLabel("@");
        atLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        atLabel.setBounds(155, 30, 20, 35);
        emailInputPanel.add(atLabel);

        // 도메인 입력 필드
        HintRoundedTextField domainField = new HintRoundedTextField("직접 입력");
        domainField.setBounds(175, 30, 100, 35);  // 높이를 35로 변경
        ((AbstractDocument) domainField.getDocument()).setDocumentFilter(new ResidentNumberFilter(10));
        emailInputPanel.add(domainField);

        // 도메인 선택 콤보박스
        
        String[] domains = {"직접입력", "naver.com", "gmail.com", "yahoo.com", "daum.net"};
        JComboBox<String> domainComboBox = new JComboBox<>(domains);
        domainComboBox.setBounds(280, 30, 100, 35);  // 높이를 35로 변경
        emailInputPanel.add(domainComboBox);

        // 콤보박스와 직접 입력 필드 조정
        domainComboBox.addActionListener(e -> {
        	String selectedDomain = (String) domainComboBox.getSelectedItem();
            if (selectedDomain != null) {
                if (selectedDomain.equals("직접입력")) {
                    domainField.setText("");
                    domainField.setEnabled(true);
                  
                } else {
                    domainField.setText(selectedDomain);
                    domainField.setEnabled(false);
                    
                }
            }
        });

        cardLayout2.show(cardPanel2, "phonePanel");
        memberDAO = new MemberDAO(); // MemberDAO 인스턴스 생성
        StyledButton findIdButton = StyledButton.create("아이디 찾기");
        findIdButton.setBounds(20, 295, 360, 35);  // 높이를 35로 변경
        
        // 비밀번호 필드 추가
        JPanel findPassword = new JPanel();
        findPassword.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(240, 240, 240)));
        findPassword.setBackground(new Color(255, 255, 255));
        cardPanel.add(findPassword, "findPassword");
        findPassword.setLayout(null);
        
        JLabel newpasswordLabel = new JLabel("새 비밀번호");
        newpasswordLabel.setFont(new Font("굴림", Font.BOLD, 15));
        newpasswordLabel.setBounds(12, 132, 307, 18);
        findPassword.add(newpasswordLabel);

        HintRoundedPasswordField passwordField = new HintRoundedPasswordField("비밀번호 입력 (문자, 숫자, 특수문자를 포함 8~20자)");
        passwordField.setBounds(12, 152, 365, 35);
        
        findPassword.add(passwordField);
        JLabel passwordLabel2 = new JLabel("비밀번호 재설정");
        passwordLabel2.setBackground(new Color(220, 220, 220));
        passwordLabel2.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        passwordLabel2.setBounds(12, 37, 365, 40);

        passwordLabel2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240,240,240)));
        findPassword.add(passwordLabel2);
        
        StyledButton resetPasswordButton = StyledButton.create("재설정하기");
        resetPasswordButton.setBounds(12, 275, 365, 40);
        findPassword.add(resetPasswordButton);
        updateLabelAppearance(idLabel, false);
        
        JLabel newPasswordLabel2 = new JLabel("비밀번호 확인");
        newPasswordLabel2.setFont(new Font("굴림", Font.BOLD, 15));
        newPasswordLabel2.setBounds(12, 192, 307, 18);
        findPassword.add(newPasswordLabel2);

        HintRoundedPasswordField confirmPasswordField = new HintRoundedPasswordField("비밀번호 재입력");
        confirmPasswordField.setBounds(12, 212, 365, 35);
        
        findPassword.add(confirmPasswordField);

        JPanel findID = new JPanel();
        findID.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(240, 240, 240)));
        findID.setBackground(new Color(255, 255, 255));
        cardPanel.add(findID, "findID");
        findID.setLayout(null);

        JLabel label = new JLabel("입력하신 정보와 일치한 아이디입니다");
        label.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        label.setBounds(98, 60, 205, 25);
        findID.add(label);

        JPanel panel = new JPanel();
        panel.setBounds(50, 120, 300, 150);
        findID.add(panel);
        panel.setLayout(null);

        JLabel joinID = new JLabel("아이디 : ");
        joinID.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        joinID.setBounds(50, 50, 250, 15);
        panel.add(joinID);

        JLabel joindate = new JLabel("가입일 : ");
        joindate.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        joindate.setBounds(50, 85, 250, 15);
        panel.add(joindate);

        StyledButton loginButton = StyledButton.create("로그인");
        loginButton.setBounds(50, 305, 135, 40);
        findID.add(loginButton);

        StyledButton resetPasswordFromIDButton = StyledButton.create("비밀번호 재설정");
        resetPasswordFromIDButton.setBounds(215, 305, 135, 40);
        findID.add(resetPasswordFromIDButton);
        idPanel.add(findIdButton);
        findIdButton.addActionListener(e -> {
            String name = idNameField.getText();
            String residentNumberFront = idResidentNumberFrontField.getText();
            String residentNumberBackFirst = idResidentNumberBackField.getText();
            // 전화번호와 이메일 입력 필드로부터 정보를 가져옵니다.
            String phoneFront = phoneFrontField.getText();
            String phoneMiddle = phoneMiddleField.getText();
            String phoneEnd = phoneEndField.getText();
            String email = emailField.getText();
            String domain = domainField.getText();

            // 연락처 정보를 완성합니다.
            String contactInfo;
            boolean isEmail;
            if (email.isEmpty() && domain.isEmpty()) {
                contactInfo = email + "@" + domain;
                isEmail = true;
            } else {
                contactInfo = phoneFront+"-"+ phoneMiddle+"-"+phoneEnd;
                isEmail = false;
            }

            MemberDAO.UserInfo userInfo = memberDAO.findUserId(name, residentNumberFront, residentNumberBackFirst, contactInfo, isEmail);

            if (userInfo != null) {
                joinID.setText("아이디 : " + userInfo.getUserId());
                joindate.setText("가입일 : " + userInfo.getSignupDate());
                cardLayout.show(cardPanel, "findID");
            }
        });

        // 비밀번호 찾기 패널
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(null);
        passwordPanel.setBackground(frameBackgroundColor); // 비밀번호 패널 배경색 설정

        JLabel passwordIdLabel = new JLabel("아이디");
        passwordIdLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        passwordIdLabel.setBounds(20, 30, 100, 30);
        passwordPanel.add(passwordIdLabel);

        passwordIdField = new HintRoundedTextField("아이디를 입력하세요");
        passwordIdField.setBounds(20, 60, 360, 35);  // 높이를 35로 변경
        ((AbstractDocument) passwordIdField.getDocument()).setDocumentFilter(new ResidentNumberFilter(20));
        passwordPanel.add(passwordIdField);

        JLabel passwordNameLabel = new JLabel("이름");
        passwordNameLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        passwordNameLabel.setBounds(20, 100, 100, 30);
        passwordPanel.add(passwordNameLabel);

        passwordNameField = new HintRoundedTextField("이름을 입력하세요");
        passwordNameField.setBounds(20, 130, 360, 35);  // 높이를 35로 변경
        ((AbstractDocument) passwordNameField.getDocument()).setDocumentFilter(new ResidentNumberFilter(50));
        passwordPanel.add(passwordNameField);

        JLabel passwordResidentNumberLabel = new JLabel("주민번호");
        passwordResidentNumberLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        passwordResidentNumberLabel.setBounds(20, 170, 100, 30);
        passwordPanel.add(passwordResidentNumberLabel);

        passwordResidentNumberFrontField = new HintRoundedTextField("앞자리 6자리");
        passwordResidentNumberFrontField.setBounds(20, 200, 185, 35);  // 높이를 35로 변경
        ((AbstractDocument) passwordResidentNumberFrontField.getDocument()).setDocumentFilter(new ResidentNumberFilter(6));
        passwordPanel.add(passwordResidentNumberFrontField);

        JLabel passwordResidentNumberSeparator = new JLabel("-");
        passwordResidentNumberSeparator.setFont(new Font("맑은고딕", Font.BOLD, 16));
        passwordResidentNumberSeparator.setBounds(215, 199, 10, 35);
        passwordPanel.add(passwordResidentNumberSeparator);

        passwordResidentNumberBackField = new HintRoundedTextField("●");
        passwordResidentNumberBackField.setBounds(230, 200, 30, 35);  // 높이를 35로 변경
        ((AbstractDocument) passwordResidentNumberBackField.getDocument()).setDocumentFilter(new ResidentNumberFilter(1));
        passwordPanel.add(passwordResidentNumberBackField);

        JLabel passwordResidentNumberDots = new JLabel("● ● ● ● ● ●");
        passwordResidentNumberDots.setFont(new Font("맑은고딕", Font.BOLD, 16));
        passwordResidentNumberDots.setBounds(265, 200, 116, 35);
        passwordPanel.add(passwordResidentNumberDots);

        // 비밀번호 찾기 옵션 패널
        passwordFindOptionsPanel = new JPanel();
        passwordFindOptionsPanel.setBounds(20, 235, 360, 40);
        passwordFindOptionsPanel.setBackground(frameBackgroundColor);
        passwordFindOptionsPanel.setLayout(new GridLayout(1, 2)); // 가로로 배치

        JRadioButton phonePasswordButton = new JRadioButton("전화번호로 찾기");
        phonePasswordButton.setBackground(frameBackgroundColor);
        phonePasswordButton.setFont(new Font("맑은고딕", Font.BOLD, 14));
        phonePasswordButton.setSelected(true); // 기본 선택
        phonePasswordButton.addActionListener(e -> showPhonePasswordPanel());
        passwordFindOptionsPanel.add(phonePasswordButton);

        JRadioButton emailPasswordButton = new JRadioButton("이메일로 찾기");
        emailPasswordButton.setBackground(frameBackgroundColor);
        emailPasswordButton.setFont(new Font("맑은고딕", Font.BOLD, 14));
        emailPasswordButton.addActionListener(e -> showEmailPasswordPanel());
        passwordFindOptionsPanel.add(emailPasswordButton);

        ButtonGroup findPasswordOptionsGroup = new ButtonGroup();
        findPasswordOptionsGroup.add(phonePasswordButton);
        findPasswordOptionsGroup.add(emailPasswordButton);

        passwordPanel.add(passwordFindOptionsPanel);

        cardPanel3 = new JPanel();
        cardLayout3 = new CardLayout();
        cardPanel3.setLayout(cardLayout3);
        cardPanel3.setBounds(0, 275, 400, 70);
        passwordPanel.add(cardPanel3);

        // 비밀번호 찾기 - 전화번호 입력 패널
        JPanel phonePasswordInputPanel = new JPanel();
        phonePasswordInputPanel.setLayout(null);
        phonePasswordInputPanel.setBackground(Color.WHITE);
        cardPanel3.add(phonePasswordInputPanel, "phonePasswordPanel");

        JLabel phonePasswordLabel = new JLabel("전화번호");
        phonePasswordLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        phonePasswordLabel.setBounds(20, 0, 100, 30);
        phonePasswordInputPanel.add(phonePasswordLabel);

        // 앞자리
        HintRoundedTextField phonePasswordFrontField = new HintRoundedTextField("010");
        phonePasswordFrontField.setBounds(20, 30, 60, 35);  // 높이를 35로 변경
        ((AbstractDocument) phonePasswordFrontField.getDocument()).setDocumentFilter(new ResidentNumberFilter(3));
        phonePasswordInputPanel.add(phonePasswordFrontField);

        // 중간 구분선
        JLabel phonePasswordMiddleSeparator = new JLabel("-");
        phonePasswordMiddleSeparator.setFont(new Font("맑은고딕", Font.BOLD, 16));
        phonePasswordMiddleSeparator.setBounds(85, 30, 10, 35);
        phonePasswordInputPanel.add(phonePasswordMiddleSeparator);

        // 중간 자리
        HintRoundedTextField phonePasswordMiddleField = new HintRoundedTextField("0000");
        phonePasswordMiddleField.setBounds(100, 30, 80, 35);  // 높이를 35로 변경
        ((AbstractDocument) phonePasswordMiddleField.getDocument()).setDocumentFilter(new ResidentNumberFilter(4));
        phonePasswordInputPanel.add(phonePasswordMiddleField);

        // 끝자리 구분선
        JLabel phonePasswordEndSeparator = new JLabel("-");
        phonePasswordEndSeparator.setFont(new Font("맑은고딕", Font.BOLD, 16));
        phonePasswordEndSeparator.setBounds(185, 30, 10, 35);
        phonePasswordInputPanel.add(phonePasswordEndSeparator);

        // 끝 자리
        HintRoundedTextField phonePasswordEndField = new HintRoundedTextField("0000");
        phonePasswordEndField.setBounds(200, 30, 80, 35);  // 높이를 35로 변경
        ((AbstractDocument) phonePasswordEndField.getDocument()).setDocumentFilter(new ResidentNumberFilter(4));
        phonePasswordInputPanel.add(phonePasswordEndField);

        // 비밀번호 찾기 - 이메일 입력 패널
        JPanel emailPasswordInputPanel = new JPanel();
        emailPasswordInputPanel.setLayout(null);
        emailPasswordInputPanel.setBackground(Color.WHITE);
        cardPanel3.add(emailPasswordInputPanel, "emailPasswordPanel");

        // 이메일 레이블
        JLabel emailPasswordLabel = new JLabel("이메일");
        emailPasswordLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        emailPasswordLabel.setBounds(20, 0, 100, 30);
        emailPasswordInputPanel.add(emailPasswordLabel);

        // 이메일 입력 필드
        HintRoundedTextField emailPasswordField = new HintRoundedTextField("이메일 주소");
        emailPasswordField.setBounds(20, 30, 130, 35);  // 높이를 35로 변경
        ((AbstractDocument) emailPasswordField.getDocument()).setDocumentFilter(new ResidentNumberFilter(10));
        emailPasswordInputPanel.add(emailPasswordField);

        // @ 라벨
        JLabel atPasswordLabel = new JLabel("@");
        atPasswordLabel.setFont(new Font("맑은고딕", Font.BOLD, 16));
        atPasswordLabel.setBounds(155, 30, 20, 35);
        emailPasswordInputPanel.add(atPasswordLabel);

        // 도메인 입력 필드
        HintRoundedTextField domainPasswordField = new HintRoundedTextField("직접 입력");
        domainPasswordField.setBounds(175, 30, 100, 35);  // 높이를 35로 변경
        ((AbstractDocument) domainPasswordField.getDocument()).setDocumentFilter(new ResidentNumberFilter(10));
        emailPasswordInputPanel.add(domainPasswordField);

        // 도메인 선택 콤보박스
        String[] domainsStrings = {"직접입력", "naver.com", "gmail.com", "yahoo.com", "daum.net"};
        JComboBox<String> domainPasswordComboBox = new JComboBox<>(domainsStrings);
        domainPasswordComboBox.setBounds(280, 30, 100, 35);  // 높이를 35로 변경
        emailPasswordInputPanel.add(domainPasswordComboBox);

        // 콤보박스와 직접 입력 필드 조정
        domainPasswordComboBox.addActionListener(e -> {
            String selectedDomain = (String) domainPasswordComboBox.getSelectedItem();
            if (selectedDomain != null) {
                if (selectedDomain.equals("직접입력")) {
                    domainPasswordField.setText("");
                    domainPasswordField.setEnabled(true);
                    domainPasswordField.setForeground(Color.BLACK); // 기본 폰트 색상
                } else {
                    domainPasswordField.setText(selectedDomain);
                    domainPasswordField.setEnabled(false);
                    domainPasswordField.setForeground(Color.BLACK); // 선택된 도메인에 대한 폰트 색상 설정
                }
            }
        });

        cardLayout3.show(cardPanel3, "phonePasswordPanel");

        StyledButton findPasswordButton = StyledButton.create("비밀번호 찾기");
        findPasswordButton.setBounds(20, 360, 360, 35);  // 높이를 35로 변경
        findPasswordButton.addActionListener(e -> handleFindPassword());
        passwordPanel.add(findPasswordButton);

        findPasswordButton.addActionListener(e -> {
            String userId = passwordIdField.getText();
            String name = passwordNameField.getText();
            String residentNumberFront = passwordResidentNumberFrontField.getText();
            String residentNumberBackFirst = passwordResidentNumberBackField.getText();
            String phoneFront = phonePasswordFrontField.getText();
            String phoneMiddle = phonePasswordMiddleField.getText();
            String phoneEnd = phonePasswordEndField.getText();
            String email = emailPasswordField.getText();
            String domain = domainPasswordField.getText();

            // 연락처 정보를 완성합니다.
            String contactInfo;
            boolean isEmail;
            if (email.isEmpty() && domain.isEmpty()) {
                contactInfo = email + "@" + domain;
                isEmail = true;
            } else {
                contactInfo = phoneFront + "-" + phoneMiddle + "-" + phoneEnd;
                isEmail = false;
            }

            boolean userExists = memberDAO.verifyUser(userId, name, residentNumberFront, residentNumberBackFirst, contactInfo, isEmail);
            
            if (userExists) {
                userIdForPasswordReset = userId;
                cardLayout.show(cardPanel, "findPassword");
            } else {
                JOptionPane.showMessageDialog(null, "정보와 일치하는 사용자 비밀번호가 없습니다.");
            }
        });

        cardPanel.add(idPanel, "idPanel");
        cardPanel.add(passwordPanel, "passwordPanel");

     // 기본으로 아이디 찾기 패널을 보여주도록 설정
        cardLayout.show(cardPanel, initialPanel);

        // 초기 라벨 상태 설정
        updateLabelAppearance(idLabel, "idPanel".equals(initialPanel));
        updateLabelAppearance(passwordLabel, "passwordPanel".equals(initialPanel));

     
        JLabel lblNewLabel = new JLabel("아이디 찾기 / 비밀번호 재설정");
        lblNewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        lblNewLabel.setBounds(50, 30, 400, 40);
        contentPane.add(lblNewLabel);

        idLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "idPanel");
                updateLabelAppearance(idLabel, true);
                updateLabelAppearance(passwordLabel, false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 포커스가 없는 경우에만 호버 색상과 테두리 적용
                if (!idLabel.getForeground().equals(labelClickTextColor)) {
                    idLabel.setForeground(labelHoverTextColor);
                    idLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, labelHoverBorderColor));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 포커스가 없는 경우에만 기본 색상과 테두리 적용
                if (!idLabel.getForeground().equals(labelClickTextColor)) {
                    idLabel.setForeground(labelDefaultTextColor);
                    idLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, labelDefaultBorderColor));
                }
            }
        });

        passwordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "passwordPanel");
                updateLabelAppearance(passwordLabel, true);
                updateLabelAppearance(idLabel, false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 포커스가 없는 경우에만 호버 색상과 테두리 적용
                if (!passwordLabel.getForeground().equals(labelClickTextColor)) {
                    passwordLabel.setForeground(labelHoverTextColor);
                    passwordLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, labelHoverBorderColor));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 포커스가 없는 경우에만 기본 색상과 테두리 적용
                if (!passwordLabel.getForeground().equals(labelClickTextColor)) {
                    passwordLabel.setForeground(labelDefaultTextColor);
                    passwordLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, labelDefaultBorderColor));
                }
            }
        });

        idLabel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateLabelAppearance(idLabel, true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateLabelAppearance(idLabel, false);
            }
        });

        passwordLabel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateLabelAppearance(passwordLabel, true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateLabelAppearance(passwordLabel, false);
            }
        });

        // 확인 버튼 이벤트 추가
        loginButton.addActionListener(e -> {
            // JoinPage로 이동
            new MemberLogin().setVisible(true);
            dispose(); // 현재 프레임 닫기
        });

        // 비밀번호 재설정 버튼 이벤트 추가
        resetPasswordFromIDButton.addActionListener(e -> {
            // 비밀번호 찾기 패널로 이동
        	cardLayout.show(cardPanel, "findPassword");
        	updateLabelAppearance(idLabel, false);
        	updateLabelAppearance(passwordLabel, true);
            
        });

        resetPasswordButton.addActionListener(e -> {
            String newPassword = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
                return;
            }

            if (!Pattern.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{}|;:,.<>?]).{8,20}$", newPassword)) {
                JOptionPane.showMessageDialog(null, "비밀번호는 8~20자이며, 최소 1개의 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
                return;
            }

            boolean result = memberDAO.resetPassword(userIdForPasswordReset, newPassword);
            if (result) {
                JOptionPane.showMessageDialog(null, "비밀번호가 성공적으로 재설정되었습니다.");
                new MemberLogin().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "비밀번호 재설정에 실패했습니다.");
            }
        });
    }

    private JLabel createClickableLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("맑은고딕", Font.BOLD, 16));
        label.setForeground(labelDefaultTextColor);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(frameBackgroundColor);

        // 기본 테두리 설정
        Border border = BorderFactory.createMatteBorder(0, 0, 3, 0, labelDefaultBorderColor);
        label.setBorder(border);

        return label;
    }

    private void updateLabelAppearance(JLabel label, boolean isSelected) {
        if (isSelected) {
            label.setForeground(labelClickTextColor);
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, labelClickTextColor));
        } else {
            label.setForeground(labelDefaultTextColor);
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, labelDefaultBorderColor));
        }
    }

    private void showPhonePanel() {
        cardLayout2.show(cardPanel2, "phonePanel");
    }

    private void showEmailPanel() {
        cardLayout2.show(cardPanel2, "emailPanel");
    }

    private void showPhonePasswordPanel() {
        cardLayout3.show(cardPanel3, "phonePasswordPanel");
    }

    private void showEmailPasswordPanel() {
        cardLayout3.show(cardPanel3, "emailPasswordPanel");
    }

    private void handleFindId() {
        // 아이디 찾기 로직 처리
    }

    private void handleFindPassword() {
        // 비밀번호 찾기 로직 처리
    }
}
