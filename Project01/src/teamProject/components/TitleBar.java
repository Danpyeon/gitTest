package teamProject.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TitleBar extends JPanel {

    private final JFrame frame;
    private Point initialClick;

    public TitleBar(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(220,220,220)));

        JLabel minimizeLabel = new JLabel(" - ");
        minimizeLabel.setFont(new Font("굴림", Font.BOLD, 16));
        minimizeLabel.setForeground(Color.BLACK);
        minimizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        minimizeLabel.setPreferredSize(new Dimension(30, 30));
        minimizeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minimizeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setState(JFrame.ICONIFIED);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                minimizeLabel.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                minimizeLabel.setForeground(Color.GRAY);
            }
        });

        JLabel closeLabel = new JLabel(" × ");
        closeLabel.setFont(new Font("굴림", Font.BOLD, 16));
        closeLabel.setForeground(Color.BLACK);
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        closeLabel.setPreferredSize(new Dimension(30, 30));
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeLabel.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeLabel.setForeground(Color.GRAY);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(minimizeLabel);
        buttonPanel.add(closeLabel);

        add(buttonPanel, BorderLayout.EAST);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                frame.setLocation(x - initialClick.x, y - initialClick.y);
            }
        });
    }
}
