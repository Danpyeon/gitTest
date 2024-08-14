package artgame;

import java.io.Serializable;

public class DataPost2 implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private String chat;
    private String img;

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
