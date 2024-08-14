package teamProject.join;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;

import teamProject.components.CustomComboBox;
import teamProject.components.HintRoundedPasswordField;
import teamProject.components.HintRoundedTextField;
import teamProject.components.ResidentNumberFilter;
import teamProject.components.StyledButton;
import teamProject.components.TitleBar;


public class JoinPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private HintRoundedTextField idField;
    private HintRoundedPasswordField passwordField;
    private HintRoundedPasswordField confirmPasswordField;
    private HintRoundedTextField emailField;
    private HintRoundedTextField phoneField1;
    private HintRoundedTextField phoneField2;
    private HintRoundedTextField phoneField3;
    private HintRoundedTextField nameField;
    private HintRoundedTextField nicknameField;
    private CustomComboBox<String>  yearComboBox;
    private CustomComboBox<String>  monthComboBox;
    private CustomComboBox<String>  dayComboBox;
    private JButton maleButton;
    private JButton femaleButton;
    private String selectedGender = "";
    private HintRoundedTextField domainField;
    private JComboBox<String> domainComboBox;
    private boolean isIdChecked = false;  // 중복 검사 결과 저장

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JoinPage frame = new JoinPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public JoinPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 480, 800);
        setUndecorated(true);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        setContentPane(contentPane);

        TitleBar titleBar = new TitleBar(this);
        titleBar.setBounds(0, 0, 480, 30);
        contentPane.add(titleBar);

        JLabel titleLabel = new JLabel("회원가입");
        titleLabel.setBounds(60, 50, 146, 35);
        titleLabel.setFont(new Font("굴림", Font.BOLD, 30));
        contentPane.add(titleLabel);

        // 아이디 필드와 버튼 추가
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(new Font("굴림", Font.BOLD, 15));
        idLabel.setBounds(60, 100, 307, 18);
        contentPane.add(idLabel);
        
        JButton checkIdButton = StyledButton.create("중복확인");
        checkIdButton.setBounds(325, 120, 100, 35);
        contentPane.add(checkIdButton);
        
        idField = new HintRoundedTextField("아이디 입력 (6~20자)");
        idField.setBounds(60, 120, 260, 35);
        ((AbstractDocument) idField.getDocument()).setDocumentFilter(new ResidentNumberFilter(20));
        
        contentPane.add(idField);
        
        

        // 비밀번호 필드 추가
        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setFont(new Font("굴림", Font.BOLD, 15));
        passwordLabel.setBounds(60, 160, 307, 18);
        contentPane.add(passwordLabel);

        passwordField = new HintRoundedPasswordField("비밀번호 입력 (문자, 숫자, 특수문자를 포함 8~20자)");
        passwordField.setBounds(60, 180, 365, 35);
        ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new ResidentNumberFilter(20));
        contentPane.add(passwordField);

        // 비밀번호 확인 필드 추가
        JLabel confirmPasswordLabel = new JLabel("비밀번호 확인");
        confirmPasswordLabel.setFont(new Font("굴림", Font.BOLD, 15));
        confirmPasswordLabel.setBounds(60, 220, 307, 18);
        contentPane.add(confirmPasswordLabel);

        confirmPasswordField = new HintRoundedPasswordField("비밀번호 재입력");
        confirmPasswordField.setBounds(60, 240, 365, 35);
        ((AbstractDocument) confirmPasswordField.getDocument()).setDocumentFilter(new ResidentNumberFilter(20));
        contentPane.add(confirmPasswordField);

        // 이름 필드 추가
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setFont(new Font("굴림", Font.BOLD, 15));
        nameLabel.setBounds(60, 280, 307, 18);
        contentPane.add(nameLabel);

        nameField = new HintRoundedTextField("이름을 입력해주세요");
        nameField.setBounds(60, 300, 365, 35);
        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(new ResidentNumberFilter(50));
        contentPane.add(nameField);

        // 닉네임 필드 추가
        JLabel nicknameLabel = new JLabel("닉네임");
        nicknameLabel.setFont(new Font("굴림", Font.BOLD, 15));
        nicknameLabel.setBounds(60, 340, 307, 18);
        contentPane.add(nicknameLabel);

        nicknameField = new HintRoundedTextField("닉네임을 입력해주세요");
        nicknameField.setBounds(60, 360, 365, 35);
        ((AbstractDocument) nicknameField.getDocument()).setDocumentFilter(new ResidentNumberFilter(8));
        contentPane.add(nicknameField);

        // 생년월일 필드 추가
        JLabel birthdayLabel = new JLabel("생년월일");
        birthdayLabel.setFont(new Font("굴림", Font.BOLD, 15));
        birthdayLabel.setBounds(60, 400, 307, 18);
        contentPane.add(birthdayLabel);

        // 생년월일 콤보박스
        yearComboBox = new CustomComboBox<>(getYearOptions(), "년도");
        yearComboBox.setBounds(60, 420, 115, 35);
        yearComboBox.setMaxLength(4); 
        contentPane.add(yearComboBox);

        monthComboBox = new CustomComboBox<>(getMonthOptions(), "월");
        monthComboBox.setBounds(216, 420, 85, 35);
        monthComboBox.setMaxLength(2);
        contentPane.add(monthComboBox);

        dayComboBox = new CustomComboBox<>(getDayOptions(1900, 1), "일");
        dayComboBox.setBounds(340, 420, 85, 35);
        dayComboBox.setMaxLength(2);
        contentPane.add(dayComboBox);
        yearComboBox.addActionListener(e -> updateDayOptions());
        monthComboBox.addActionListener(e -> updateDayOptions());

        // 성별 선택 버튼 추가
        JLabel genderLabel = new JLabel("성별");
        genderLabel.setFont(new Font("굴림", Font.BOLD, 15));
        genderLabel.setBounds(60, 460, 307, 18);
        contentPane.add(genderLabel);

        maleButton = new JButton("남자");
        maleButton.setBounds(60, 480, 182, 35);
        maleButton.setBackground(Color.WHITE);
        contentPane.add(maleButton);

        femaleButton = new JButton("여자");
        femaleButton.setBounds(243, 480, 182, 35);
        femaleButton.setBackground(Color.white);
        contentPane.add(femaleButton);

        maleButton.addActionListener(e -> {
            selectedGender = "남자";
            maleButton.setBackground(Color.LIGHT_GRAY);
            femaleButton.setBackground(null);
        });

        femaleButton.addActionListener(e -> {
            selectedGender = "여자";
            femaleButton.setBackground(Color.LIGHT_GRAY);
            maleButton.setBackground(null);
        });

        // 이메일 필드 추가
        JLabel emailLabel = new JLabel("이메일");
        emailLabel.setFont(new Font("굴림", Font.BOLD, 15));
        
        emailLabel.setBounds(60, 520, 307, 18);
        
        contentPane.add(emailLabel);

        emailField = new HintRoundedTextField("이메일 주소");
        emailField.setBounds(60, 540, 130, 35);
        ((AbstractDocument) emailField.getDocument()).setDocumentFilter(new ResidentNumberFilter(20));
        contentPane.add(emailField);

        JLabel atLabel = new JLabel("@");
        atLabel.setFont(new Font("굴림", Font.BOLD, 15));
        atLabel.setBounds(195, 540, 20, 35);
        contentPane.add(atLabel);

        domainField = new HintRoundedTextField("직접입력");
        domainField.setBounds(220, 540, 100, 35);
        ((AbstractDocument) domainField.getDocument()).setDocumentFilter(new ResidentNumberFilter(20));
        contentPane.add(domainField);

        domainComboBox = new JComboBox<>(new String[]{"직접입력", "gmail.com", "naver.com", "daum.net"});
        domainComboBox.setBounds(325, 540, 100, 35);
        contentPane.add(domainComboBox);

        domainComboBox.addActionListener(e -> {
            String selectedDomain = (String) domainComboBox.getSelectedItem();
            if ("직접입력".equals(selectedDomain)) {
                domainField.setText(""); // 직접 입력 모드로 전환 시 필드 초기화
                domainField.setVisible(true);
                domainField.setEditable(true); // 필드를 수정 가능하게 설정
            } else {
                domainField.setText(selectedDomain);
                domainField.setEditable(false); // 필드를 수정 불가능하게 설정
                // 포커스 및 마우스 리스너를 제거하여 호버 및 포커스 기능 비활성화

            }
        });
        

        // 전화번호 필드 추가
        JLabel phoneLabel = new JLabel("전화번호");
        phoneLabel.setFont(new Font("굴림", Font.BOLD, 15));
        phoneLabel.setBounds(60, 580, 307, 18);
        contentPane.add(phoneLabel);

        phoneField1 = new HintRoundedTextField("010");
        phoneField1.setBounds(60, 600, 100, 35);
        ((AbstractDocument) phoneField1.getDocument()).setDocumentFilter(new ResidentNumberFilter(3));
        contentPane.add(phoneField1);

        JLabel phoneDash1 = new JLabel("-");
        phoneDash1.setFont(new Font("굴림", Font.BOLD, 15));
        phoneDash1.setBounds(160, 600, 10, 35);
        contentPane.add(phoneDash1);

        phoneField2 = new HintRoundedTextField("1234");
        phoneField2.setBounds(170, 600, 100, 35);
        ((AbstractDocument) phoneField2.getDocument()).setDocumentFilter(new ResidentNumberFilter(4));
        contentPane.add(phoneField2);

        JLabel phoneDash2 = new JLabel("-");
        phoneDash2.setFont(new Font("굴림", Font.BOLD, 15));
        phoneDash2.setBounds(280, 600, 10, 35);
        contentPane.add(phoneDash2);

        phoneField3 = new HintRoundedTextField("5678");
        phoneField3.setBounds(290, 600, 100, 35);
        ((AbstractDocument) phoneField3.getDocument()).setDocumentFilter(new ResidentNumberFilter(4));
        contentPane.add(phoneField3);
       
        // 가입 버튼 추가
        JButton signUpButton = StyledButton.create("회원가입");
        signUpButton.setBounds(60, 650, 365, 35);
        contentPane.add(signUpButton);

     // 중복 확인 버튼 이벤트 처리
        checkIdButton.addActionListener(e -> {
            String userId = idField.getText();

            // 아이디 유효성 검사
            if (!Pattern.matches("^[a-zA-Z0-9]{6,20}$", userId)) {
                JOptionPane.showMessageDialog(this, "아이디는 6~20자의 알파벳 대문자, 소문자, 숫자만 허용됩니다.");
                return;
            }

            if (userId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "아이디를 입력하세요.");
                return;
            }

            MemberDAO memberDAO = new MemberDAO();
            int count = memberDAO.memberSelect(userId);
            if (count > 0) {
                JOptionPane.showMessageDialog(null, "아이디가 이미 존재합니다.");
                isIdChecked = false;
            } else {
                JOptionPane.showMessageDialog(null, "아이디 사용 가능.");
                isIdChecked = true;
            }
        });

        // 회원가입 버튼 이벤트 처리
        signUpButton.addActionListener(e -> {
            if (!isIdChecked) {
                JOptionPane.showMessageDialog(this, "아이디 중복 확인을 먼저 해주세요.");
                return;
            }

            if (validateInputs()) {
                // 유효성 검사를 통과하면 데이터베이스에 저장
                String user_id = idField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String name = nameField.getText();
                String nickname = nicknameField.getText();
                String birthday = yearComboBox.getSelectedItem() + "-" + monthComboBox.getSelectedItem() + "-" + dayComboBox.getSelectedItem();
                String gender = selectedGender;
                String email = emailField.getText().toLowerCase() + "@" + (domainField.isVisible() ? domainField.getText().toLowerCase() : ((String) domainComboBox.getSelectedItem()).toLowerCase());
                String phone_number = phoneField1.getText() + "-" + phoneField2.getText() + "-" + phoneField3.getText();

   

                MemberDAO memberDAO = new MemberDAO();
                int result = memberDAO.memberInsert(user_id, password, name, nickname, birthday, gender, email, phone_number);

                if (result > 0) {
                    JOptionPane.showMessageDialog(JoinPage.this, "회원가입이 완료되었습니다.");
                    dispose();
                    new MemberLogin().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(JoinPage.this, "회원가입에 실패했습니다.");
                }
            }
        });
    }

    private void updateDayOptions() {
    	 if (yearComboBox == null || monthComboBox == null || dayComboBox == null) {
    	    
    	        return;
    	    }
        String selectedYearStr = (String) yearComboBox.getSelectedItem();
        String selectedMonthStr = (String) monthComboBox.getSelectedItem();

        if (selectedYearStr != null && !selectedYearStr.isEmpty() && selectedMonthStr != null && !selectedMonthStr.isEmpty()) {
            int selectedYear = Integer.parseInt(selectedYearStr);
            int selectedMonth = Integer.parseInt(selectedMonthStr);
            dayComboBox.setModel(new DefaultComboBoxModel<>(getDayOptions(selectedYear, selectedMonth)));
        }
    }

    private static String[] getYearOptions() {
        int startYear = Year.now().getValue() - 100;
        int endYear = Year.now().getValue(); // 현재 연도까지 포함
        String[] years = new String[endYear - startYear + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = String.valueOf(startYear + i);
        }
        return years;
    }

    private static String[] getMonthOptions() {
        String[] months = new String[12];
        for (int i = 1; i <= 12; i++) {
            months[i - 1] = String.format("%02d", i);
        }
        return months;
    }

    private static String[] getDayOptions(int year, int month) {
        int daysInMonth;
        switch (month) {
            case 4: case 6: case 9: case 11:
                daysInMonth = 30;
                break;
            case 2:
                daysInMonth = isLeapYear(year) ? 29 : 28;
                break;
            default:
                daysInMonth = 31;
                break;
        }
        String[] days = new String[daysInMonth];
        for (int i = 1; i <= daysInMonth; i++) {
            days[i - 1] = String.format("%02d", i);
        }
        return days;
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // 입력 검증
    private boolean validateInputs() {
        if (idField.getText().isEmpty() ||
            passwordField.getPassword().length == 0 ||
            confirmPasswordField.getPassword().length == 0 ||
            nameField.getText().isEmpty() ||
            nicknameField.getText().isEmpty() ||
            yearComboBox.getSelectedItem() == null ||
            monthComboBox.getSelectedItem() == null ||
            dayComboBox.getSelectedItem() == null ||
            selectedGender.isEmpty() ||
            emailField.getText().isEmpty() ||
            phoneField1.getText().isEmpty() ||
            phoneField2.getText().isEmpty() ||
            phoneField3.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "모든 필드를 채워주세요.");
            return false;
        }

        // 아이디 유효성 검사
        String id = idField.getText();
        if (!Pattern.matches("^[a-zA-Z0-9]{6,20}$", id)) {
            JOptionPane.showMessageDialog(this, "아이디는 6~20자의 알파벳 대문자, 소문자, 숫자만 허용됩니다.");
            return false;
        }

        // 비밀번호 유효성 검사
        
            String password = new String(passwordField.getPassword());
            System.out.println("비밀번호 확인: " + password);  // 비밀번호 출력 (디버깅 용도)

            String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\[\\]{}|;:,.<>?]).{8,20}$";
            if (!Pattern.matches(passwordPattern, password)) {
                JOptionPane.showMessageDialog(this, "비밀번호는 8~20자이며, 최소 1개의 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
                return false;
            }
       
        // 비밀번호 확인
        String confirmPassword = new String(confirmPasswordField.getPassword());
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
            return false;
        }

        // 닉네임 유효성 검사
        String nickname = nicknameField.getText();
        if (!Pattern.matches("^[a-zA-Z0-9가-힣]{2,8}$", nickname)) {
            JOptionPane.showMessageDialog(this, "닉네임은 2~8자의 한글, 알파벳 대문자, 소문자, 숫자만 허용됩니다.");
            return false;
        }

        // 이름 유효성 검사
        String name = nameField.getText();
        if (!Pattern.matches("^[a-zA-Z가-힣]+$", name)) {
            JOptionPane.showMessageDialog(this, "이름은 한글, 알파벳 대문자, 소문자만 허용됩니다.");
            return false;
        }
     

        // 이메일 유효성 검사
        String email = emailField.getText().toLowerCase();
        String domain = domainField.isVisible() ? domainField.getText().toLowerCase() : ((String) domainComboBox.getSelectedItem()).toLowerCase();
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email + "@" + domain)) {
            JOptionPane.showMessageDialog(this, "이메일 형식이 올바르지 않습니다.");
            return false;
        }

        // 전화번호 유효성 검사
        String phoneNumber = phoneField1.getText() + phoneField2.getText() + phoneField3.getText();
        if (!Pattern.matches("^010\\d{8}$", phoneNumber)) {
            JOptionPane.showMessageDialog(this, "전화번호 형식이 올바르지 않습니다.");
            return false;
        }

        // 생년월일 유효성 검사
        int year = Integer.parseInt((String) yearComboBox.getSelectedItem());
        int month = Integer.parseInt((String) monthComboBox.getSelectedItem());
        int day = Integer.parseInt((String) dayComboBox.getSelectedItem());

        if (!isValidDate(year, month, day)) {
            JOptionPane.showMessageDialog(this, "유효하지 않은 생년월일입니다.", "경고", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 성별 유효성 검사
        if (selectedGender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "성별을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // 날짜 유효성 검사 메서드
    private boolean isValidDate(int year, int month, int day) {
    	int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int minYear = currentYear - 100;

        // 연도 범위 체크
        if (year < minYear || year > currentYear) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }

        int daysInMonth;
        switch (month) {
            case 2:
                // 윤년 체크
                daysInMonth = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
                break;
            case 4: case 6: case 9: case 11:
                daysInMonth = 30;
                break;
            default:
                daysInMonth = 31;
                break;
        }

        return day >= 1 && day <= daysInMonth;
    }
    
}
