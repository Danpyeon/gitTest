package teamProject.join;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class MemberDAO {
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;  

        // 데이터베이스 연결
        public void joinAccess() {
            if (conn == null) {
                try {
                    String url = "jdbc:mysql://localhost:3306/chatP?serverTimezone=UTC";
                    String user = "root";
                    String pw = "1234";
                    conn = DriverManager.getConnection(url, user, pw);
                    if (conn != null) {
                        System.out.println("Database connected successfully.");
                    } else {
                        System.out.println("Failed to connect to the database.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 회원 가입 처리
        public int memberInsert(String user_id, String password, String name, String nickname, String birthdayStr, String gender, String email, String phone_number) {
            joinAccess();
            int result = 0;

            try {
                String sql = "INSERT INTO user_table (user_id, password, name, nickname, birthday, gender, email, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user_id);
                pstmt.setString(2, password);
                pstmt.setString(3, name);
                pstmt.setString(4, nickname);
                pstmt.setString(5, birthdayStr); // VARCHAR 형식으로 저장
                pstmt.setString(6, gender);
                pstmt.setString(7, email);
                pstmt.setString(8, phone_number);
                result = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                result = -1;
            } finally {
                closeResources();
            }
            return result;
        }
        
        // 로그인 유효성 검사를 위한 메서드
        public boolean validateLogin(String username, String password) {
            joinAccess();
            if (conn == null) {
                System.err.println("Database connection is not established.");
                return false;
            }

            String query = "SELECT * FROM user_table WHERE user_id = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                return rs.next(); // 아이디와 비밀번호가 일치하는 사용자가 있으면 true 반환
            } catch (SQLException e) {
                // 예외 메시지와 스택 트레이스를 출력하여 디버깅에 도움을 줌
                System.err.println("SQL error during login validation:");
                e.printStackTrace();
            }
            return false;
        }
        
     // 사용자의 이름을 가져오는 메서드
        public String getUserName(String userId) {
            String sql = "SELECT name FROM user_table WHERE user_id = ?";
            String userName = null;
            try {
                //conn = // 데이터베이스 연결 로직;
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userId);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    userName = rs.getString("name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // 자원 해제 로직
            }
            return userName;
        }
        
        // 연결 해제를 위한 메서드
        public void closeConnection() {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    conn = null;  // 연결을 닫은 후 conn을 null로 설정하여 재사용 시 새로운 연결을 열도록 함
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    // 아이디 찾기 (이메일 또는 전화번호로)
    public UserInfo findUserId(String name, String residentNumberFront, String residentNumberBackFirst, String contactInfo, boolean isEmail) {
        joinAccess();
        UserInfo userInfo = null;

        // 주민번호 앞자리에서 생년월일을 파싱합니다.
        String birthday = residentNumberFront.substring(0, 6); // 예: "900101"

        // 주민번호 뒷자리 첫 자리에서 성별을 파싱합니다.
        String gender = getGenderFromResidentNumber(residentNumberBackFirst);

        try {
            String sql;
            if (isEmail) {
                sql = "SELECT user_id, signup_date FROM user_table WHERE name = ? AND DATE_FORMAT(birthday, '%y%m%d') = ? AND gender = ? AND email = ?";
            } else {
                sql = "SELECT user_id, signup_date FROM user_table WHERE name = ? AND DATE_FORMAT(birthday, '%y%m%d') = ? AND gender = ? AND phone_number = ?";
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, birthday);
            pstmt.setString(3, gender);
            pstmt.setString(4, contactInfo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String userId = rs.getString("user_id");
                String signupDate = rs.getString("signup_date");
                userInfo = new UserInfo(userId, signupDate);
            } else {
                JOptionPane.showMessageDialog(null, "정보와 일치하는 사용자 아이디가 없습니다.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "정보와 일치하는 사용자 아이디가 없습니다.");
        } finally {
            closeResources();
        }
        return userInfo;
    }

    // UserInfo 클래스를 정의하여 아이디와 가입일 정보를 담을 수 있게 합니다.
    public class UserInfo {
        private String userId;
        private String signupDate;

        public UserInfo(String userId, String signupDate) {
            this.userId = userId;
            this.signupDate = signupDate;
        }

        public String getUserId() {
            return userId;
        }

        public String getSignupDate() {
            return signupDate;
        }
    }

    public boolean verifyUser(String userId, String name, String residentNumberFront, String residentNumberBackFirst, String contactInfo, boolean isEmail) {
        joinAccess(); // 연결 확인
        if (conn == null) {
            throw new IllegalStateException("Database connection is not established.");
        }

        String sql;
        String birthday = residentNumberFront.substring(0, 6);
        String gender = getGenderFromResidentNumber(residentNumberBackFirst);
   
        if (isEmail) {
            sql = "SELECT user_id FROM user_table WHERE user_id = ? AND name = ? AND DATE_FORMAT(birthday, '%y%m%d') = ? AND gender = ? AND email = ?";
        } else {
            sql = "SELECT user_id FROM user_table WHERE user_id = ? AND name = ? AND DATE_FORMAT(birthday, '%y%m%d') = ? AND gender = ? AND phone_number = ?";
        }

        

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, name);
            pstmt.setString(3, birthday);
            pstmt.setString(4, gender);
            pstmt.setString(5, contactInfo);
            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // 일치하는 사용자가 있으면 true, 없으면 false
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    public boolean resetPassword(String userId, String newPassword) {
        joinAccess(); // 연결 확인
        if (conn == null) {
            throw new IllegalStateException("Database connection is not established.");
        }

        String sql = "UPDATE user_table SET password = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    private String getGenderFromResidentNumber(String residentNumberBackFirst) {
        char genderCode = residentNumberBackFirst.charAt(0);
        if (genderCode == '1' || genderCode == '3') {
            return "남자";
        } else if (genderCode == '2' || genderCode == '4') {
            return "여자";
        } else {
            throw new IllegalArgumentException("유효하지 않은 성별 코드입니다.");
        }
    }

    // 프로필 업데이트 메서드
    public int updateProfile(String user_id, String nickname, String profile_image, String status_message) {
        joinAccess();
        int result = 0;
        try {
            String sql = "UPDATE user_table SET nickname = ?, profile_image = ?, status_message = ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nickname);
            pstmt.setString(2, profile_image);
            pstmt.setString(3, status_message);
            pstmt.setString(4, user_id);
            result = pstmt.executeUpdate();
            System.out.println("Execution result: " + result);
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        } finally {
            closeResources();
        }
        return result;
    }

    public int memberSelect(String userId) {
        joinAccess();
        int resultCnt = 0;
        try {
            String sql = "SELECT COUNT(*) FROM user_table WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                resultCnt = rs.getInt(1);  // 결과로 얻은 카운트 값을 resultCnt에 저장
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return resultCnt;
    }

    // 리소스 정리 메서드
    private void closeResources() {
        try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
        try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
        try { if (conn != null && !conn.isClosed()) conn.close(); } catch (Exception e) { e.printStackTrace(); }
    }

 
}
