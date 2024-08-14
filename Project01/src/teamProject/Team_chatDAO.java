package teamProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Team_chatDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/chatP?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    // JDBC 드라이버를 로드하는 생성자
    public Team_chatDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC 드라이버 로드 성공");
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로드 실패: " + e.getMessage());
        }
    }

    // 데이터베이스 연결을 반환하는 메소드
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 모든 채팅방 이름을 조회하여 반환하는 메소드
    public List<String> getChatRooms() {
        List<String> roomNames = new ArrayList<>();
        String query = "SELECT room_name FROM chat_rooms WHERE deleted_at IS NULL";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                roomNames.add(rs.getString("room_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomNames;
    }

    // 새로운 채팅방을 생성하는 메소드
    public void createChatRoom(String roomName, String creatorId, String password) {
        String query = "INSERT INTO chat_rooms (room_name, user_id, room_password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, roomName);
            pstmt.setString(2, creatorId);
            pstmt.setString(3, password.isEmpty() ? null : password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // 방 이름으로 방 ID 조회
    public int getRoomIdByName(String roomName) {
        int roomId = -1;
        String query = "SELECT id FROM chat_rooms WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, roomName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                roomId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomId;
    }

 // 채팅방 ID로 메시지 조회 (삭제되지 않은 메시지만)
    public List<String> getMessagesByRoomId1(int roomId) {
        List<String> messages = new ArrayList<>();
        String query = "SELECT content FROM chat_messages WHERE room_id = ? AND deleted_at IS NULL";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                messages.add(rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // 채팅 메시지를 데이터베이스에 저장하는 메소드
    public void saveChatMessage(int roomId, String userId, String message) {
        String query = "INSERT INTO chat_messages (room_id, user_id, content, created_at) VALUES (?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, roomId);
            pstmt.setString(2, userId);
            pstmt.setString(3, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 방 ID로 채팅 메시지를 조회하여 반환하는 메소드
    public List<String> getMessagesByRoomId(int roomId) {
        List<String> messages = new ArrayList<>();
        String query = "SELECT u.nickname, c.content " +
                       "FROM chat_messages c " +
                       "JOIN user_table u ON c.user_id = u.user_id " +
                       "WHERE c.room_id = ? " +
                       "ORDER BY c.created_at ASC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userName = rs.getString("nickname");
                String content = rs.getString("content");
                messages.add(userName + ": " + content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // 채팅방 이름으로 방의 마지막 메시지를 가져오는 메소드
    public String getLastMessage(String roomName) {
        String query = "SELECT u.nickname, c.content " +
                       "FROM chat_messages c " +
                       "JOIN user_table u ON c.user_id = u.user_id " +
                       "WHERE c.room_id = (SELECT room_id FROM chat_rooms WHERE room_name = ?) " +
                       "ORDER BY c.created_at DESC LIMIT 1";
        String lastMessage = "최근 메시지 없음";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, roomName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String userName = rs.getString("nickname");
                    String content = rs.getString("content");
                    lastMessage = userName + ": " + content;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastMessage;
    }

    // 채팅방 이름으로 방의 비밀번호를 조회하는 메소드
    public String getRoomPassword(String roomName) {
        String query = "SELECT room_password FROM chat_rooms WHERE room_name = ?";
        String password = null;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, roomName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                password = rs.getString("room_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }

    // 채팅방을 삭제하는 메소드
    public boolean deleteChatRoom(String roomName, String userId) {
        boolean isDeleted = false;
        String checkCreatorQuery = "SELECT user_id FROM chat_rooms WHERE room_name = ?";
        String updateRoomQuery = "UPDATE chat_rooms SET deleted_at = NOW() WHERE room_name = ? AND deleted_at IS NULL";
        try (Connection conn = getConnection();
             PreparedStatement checkPstmt = conn.prepareStatement(checkCreatorQuery);
             PreparedStatement updatePstmt = conn.prepareStatement(updateRoomQuery)) {
            checkPstmt.setString(1, roomName);
            ResultSet rs = checkPstmt.executeQuery();
            if (rs.next()) {
                String creatorId = rs.getString("user_id");
                if (creatorId.equals(userId)) {
                    updatePstmt.setString(1, roomName);
                    int rowsAffected = updatePstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        isDeleted = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    // 채팅 메시지에 반응을 추가하는 메소드
    public void addReactionToMessage(String messageId, String reaction) {
        String query = "UPDATE chat_messages SET content = CONCAT(content, ' (반응: ', ?, ')') WHERE message_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, reaction); // reaction을 첫 번째 ? 자리로 설정
            pstmt.setString(2, messageId); // messageId를 두 번째 ? 자리로 설정
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 메시지 삭제
    public void deleteMessage(String message) {
        String query = "UPDATE chat_messages SET deleted_at = NOW() WHERE content = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 메시지 ID를 콘텐츠에서 추출하는 메소드 (추가 필요)
    public String getMessageIdFromContent(String messageContent) {
        // 메시지 내용을 기준으로 ID를 반환하는 로직 추가
        // 예: "user:message_id:content" 형식일 경우, message_id를 추출하여 반환
        return "someMessageId"; // 실제 구현 필요
    }
}
