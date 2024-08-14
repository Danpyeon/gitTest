package teamProject.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {

    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0x90B494); // 평소 색상
    private static final Color HOVER_BACKGROUND_COLOR = new Color(0x718F94); // 호버 시 색상

    private StyledButton(String text) {
        super(text);
        setBackground(DEFAULT_BACKGROUND_COLOR);
        setBorder(new LineBorder(DEFAULT_BACKGROUND_COLOR));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_BACKGROUND_COLOR);
                setBorder(new LineBorder(HOVER_BACKGROUND_COLOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(DEFAULT_BACKGROUND_COLOR);
                setBorder(new LineBorder(DEFAULT_BACKGROUND_COLOR));
            }
        });
    }

    public static StyledButton create(String text) {
        return new StyledButton(text);
    }
}
