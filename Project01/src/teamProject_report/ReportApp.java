package teamProject_report;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportApp extends JFrame {
    private JComboBox<String> reasonComboBox;
    private JTextArea additionalReasonTextArea;
    private JButton submitButton;
    private ReportDAO reportDAO;
    private String reporterId;  // 현재 사용자 ID
    private String reportedId;  // 신고 대상 사용자 ID
    private String chatRoomId;  // 현재 채팅방 ID

    public ReportApp(String reporterId, String reportedId, String chatRoomId) {
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.chatRoomId = chatRoomId;
        reportDAO = new ReportDAO();

        setTitle("신고하기");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 수정: 창 닫기 동작 변경
        setLocationRelativeTo(null);

        // 신고 유형 선택 드롭다운 메뉴
        JLabel reasonLabel = new JLabel("신고 유형:");
        String[] reasons = {"욕설", "비하발언", "혐오발언", "기타"};
        reasonComboBox = new JComboBox<>(reasons);

        // 신고 이유 입력 필드
        JLabel additionalReasonLabel = new JLabel("신고 사유:");
        additionalReasonTextArea = new JTextArea(5, 30);
        additionalReasonTextArea.setLineWrap(true);
        additionalReasonTextArea.setWrapStyleWord(true);

        // 신고 버튼
        submitButton = new JButton("제출");

        // 신고 버튼 이벤트 리스너
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedReason = (String) reasonComboBox.getSelectedItem();
                String reportType = selectedReason;
                String reportReason = additionalReasonTextArea.getText().trim();

                // 데이터베이스에 데이터 저장
                int result = reportDAO.report(chatRoomId, reporterId, reportedId, reportType, reportReason);

                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "신고가 접수되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "신고 접수에 실패했습니다.");
                }

                // 신고 창 닫기
                dispose();
            }
        });

        // UI 구성
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(reasonLabel);
        topPanel.add(reasonComboBox);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.add(additionalReasonLabel);
        centerPanel.add(new JScrollPane(additionalReasonTextArea));
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ReportApp("currentUser", "selectedUser", "1").setVisible(true);
            }
        });
    }
}
