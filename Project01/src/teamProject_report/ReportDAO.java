package teamProject_report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReportDAO {
    private Connection conn;
    private PreparedStatement pstmt;

    public void joinAcces() {
        try {
            String url = "jdbc:mysql://localhost:3306/chatP?serverTimezone=UTC";
            String user = "root";
            String pw = "1234";
            conn = DriverManager.getConnection(url, user, pw);

            if (conn != null) {
                System.out.println("연결 성공");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int report(String chat_room_id, String reporter_id, String reported_id, String report_type, String report_reason) {
        joinAcces();
        int result = 0;
        try {
            String sql = "INSERT INTO reports1 (chat_room_id, reporter_id, reported_id, report_type, report_reason) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, chat_room_id);
            pstmt.setString(2, reporter_id);
            pstmt.setString(3, reported_id);
            pstmt.setString(4, report_type);
            pstmt.setString(5, report_reason);

            result = pstmt.executeUpdate();
            System.out.println("실행결과: " + result);
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // Test the ReportDAO with dummy data
        ReportDAO rep = new ReportDAO();
        int result = rep.report("1", "currentUser", "selectedUser", "욕설", "테스트용 이유");
        if (result > 0) {
            System.out.println("신고가 성공적으로 접수되었습니다.");
        } else {
            System.out.println("신고 접수에 실패했습니다.");
        }
    }
}
