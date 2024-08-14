package teamProject.components;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 힌트 텍스트와 둥근 모서리를 지원하는 JPasswordField입니다.
 * 기본적인 스타일과 이벤트 리스너를 설정하고, 힌트 텍스트를 지원합니다.
 */
public class HintRoundedPasswordField extends JPasswordField {

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
    public HintRoundedPasswordField(String hint) {
        this.hint = hint;
        this.showingHint = true; // 힌트가 기본적으로 보이도록 설정
        setForeground(Color.GRAY);
        setFont(new Font("맑은고딕", Font.PLAIN, 13));
        setBackground(DEFAULT_BACKGROUND_COLOR);
        setBorder(new RoundedBorder(DEFAULT_BORDER_COLOR));
        setEchoChar('●');

        // 포커스 이벤트 리스너 추가
        addFocusListener(new HintFocusListener());

        // 키 이벤트 리스너 추가
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateHintVisibility();
            }
        });

        // 마우스 이벤트 리스너 추가
        addMouseListener(new RoundedTextFieldMouseListener());
    }

    private void updateHintVisibility() {
        // 비밀번호가 입력되었는지 여부에 따라 힌트 표시 여부를 결정
        showingHint = getPassword().length == 0 && !hasFocus();
        setForeground(showingHint ? Color.GRAY : Color.BLACK);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 기본 배경을 그리기
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);

        // 기본적인 JPasswordField의 렌더링을 호출
        super.paintComponent(g);

        // 힌트 텍스트를 그리기
        if (showingHint) {
            g2.setColor(Color.GRAY);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textX = PADDING;
            int textY = getHeight() / 2 + fm.getAscent() / 2 - 2;
            g2.drawString(hint, textX, textY);
        }
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

    private class HintFocusListener extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            // 포커스가 얻어졌을 때 힌트를 숨기기
            showingHint = false;
            setForeground(Color.BLACK);
            setBackground(FOCUS_BACKGROUND_COLOR);
            setBorder(new RoundedBorder(FOCUS_BORDER_COLOR));
            repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {
            // 포커스를 잃었을 때 힌트 상태를 업데이트
            updateHintVisibility();
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

    /**
     * HintRoundedPasswordField 인스턴스를 생성하는 정적 메서드.
     * @param hint 힌트 텍스트
     * @return 새로운 HintRoundedPasswordField 인스턴스
     */
    public static HintRoundedPasswordField create(String hint) {
        return new HintRoundedPasswordField(hint);
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
