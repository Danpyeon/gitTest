package teamProject.components;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 힌트 텍스트와 둥근 모서리를 지원하는 JTextField입니다.
 * 기본적인 스타일과 이벤트 리스너를 설정하고, 힌트 텍스트를 지원합니다.
 */
public class HintRoundedTextField extends JTextField {

    private static final long serialVersionUID = 1L;

    // 기본 배경색
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(240, 240, 240);
    // 기본 테두리 색상 (배경색과 동일하게 설정)
    private static final Color DEFAULT_BORDER_COLOR = DEFAULT_BACKGROUND_COLOR;
    // 포커스 상태일 때의 배경색
    private static final Color FOCUS_BACKGROUND_COLOR = Color.WHITE;
    // 포커스 상태일 때의 테두리 색상
    private static final Color FOCUS_BORDER_COLOR = Color.BLACK;
    // 마우스가 입력 필드 위에 있을 때의 배경색
    private static final Color HOVER_BACKGROUND_COLOR = new Color(220, 220, 220);
    // 마우스가 입력 필드 위에 있을 때의 테두리 색상
    private static final Color HOVER_BORDER_COLOR = new Color(220, 220, 220);

    // 테두리의 둥근 정도 (모서리의 반경)
    private static final int BORDER_RADIUS = 7;
    // 텍스트와 테두리 사이의 여백
    private static final int PADDING = 10;

    private String hint;
    private boolean showingHint;

    /**
     * 힌트 텍스트를 설정하여 생성합니다.
     * @param hint 힌트 텍스트
     */
    public HintRoundedTextField(String hint) {
        this.hint = hint;
        this.showingHint = true;
        setText(hint);
        setForeground(Color.GRAY);
        setFont(new Font("맑은고딕", Font.PLAIN, 13));
        setBackground(DEFAULT_BACKGROUND_COLOR);
        setBorder(new RoundedBorder(DEFAULT_BORDER_COLOR));

        // 포커스 이벤트 리스너 추가
        addFocusListener(new HintFocusListener());

        // 마우스 이벤트 리스너 추가
        addMouseListener(new RoundedTextFieldMouseListener());
    }

    private class HintFocusListener extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            if (showingHint) {
                setText("");
                setForeground(Color.BLACK);
                showingHint = false;
            }
            setBackground(FOCUS_BACKGROUND_COLOR);
            setBorder(new RoundedBorder(FOCUS_BORDER_COLOR));
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (getText().isEmpty()) {
                setText(hint);
                setForeground(Color.GRAY);
                showingHint = true;
            }
            setBackground(DEFAULT_BACKGROUND_COLOR);
            setBorder(new RoundedBorder(DEFAULT_BORDER_COLOR));
        }
    }

    private class RoundedTextFieldMouseListener extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (!hasFocus()) {
                setBackground(HOVER_BACKGROUND_COLOR);
                setBorder(new RoundedBorder(HOVER_BORDER_COLOR));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!hasFocus()) {
                setBackground(DEFAULT_BACKGROUND_COLOR);
                setBorder(new RoundedBorder(DEFAULT_BORDER_COLOR));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getBorderColor());
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
    }

    private Color getBorderColor() {
        if (getBorder() instanceof RoundedBorder) {
            return ((RoundedBorder) getBorder()).borderColor;
        }
        return DEFAULT_BORDER_COLOR;
    }

    /**
     * HintRoundedTextField 인스턴스를 생성하는 정적 메서드.
     * @param hint 힌트 텍스트
     * @return 새로운 HintRoundedTextField 인스턴스
     */
    public static HintRoundedTextField create(String hint) {
        return new HintRoundedTextField(hint);
    }

    /**
     * 둥근 테두리와 여백을 설정하는 내장 클래스입니다.
     */
    private class RoundedBorder extends AbstractBorder {
        private final Color borderColor;

        RoundedBorder(Color borderColor) {
            this.borderColor = borderColor;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, PADDING, 0, 0);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x, y, width - 1, height - 1, BORDER_RADIUS, BORDER_RADIUS);
        }
    }
}
