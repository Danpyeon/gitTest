package teamProject;

import java.io.Serializable;

public class DataPost implements Serializable {
    private String command;
    private String chat; // 채팅 메시지
    private String roomName; // 방 이름

    public DataPost() {
    }

    public DataPost(String command, String roomName) {
        this.command = command;
        this.roomName = roomName;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
