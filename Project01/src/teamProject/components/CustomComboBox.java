package teamProject.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 사용자 정의 콤보박스 컴포넌트입니다.
 * 힌트 텍스트와 최대 입력 길이 설정이 가능합니다.
 * 숫자만 입력 허용 기능이 포함되어 있습니다.
 */
public class CustomComboBox<E> extends JComboBox<E> {

    private static final long serialVersionUID = 1L;

    // 텍스트와 콤보박스 사이의 여백
    private static final int PADDING = 10;

    private String hint;
    private boolean isHint = true;
    private int maxLength = Integer.MAX_VALUE; // 기본 최대 길이 설정

    public CustomComboBox(E[] items, String hint) {
        super(items);
        this.hint = hint;
        customize();
    }

    public CustomComboBox() {
        super();
        customize();
    }

    private void customize() {
        // 폰트 설정
        this.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

        // 배경색 및 텍스트 색상 설정
        this.setBackground(Color.WHITE);

        // 경계 설정
        this.setBorder(new LineBorder(new Color(240, 240, 240), 1));

        // 값 수정 방지 및 기본값(힌트 텍스트) 설정
        this.setEditable(true);

        // 커스터마이즈된 에디터 설정
        this.setEditor(new CustomComboBoxEditor());

        // 콤보박스에서 항목이 선택될 때 힌트 텍스트 제거
        this.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (getSelectedItem() != null && getSelectedItem().equals(hint)) {
                        // 힌트가 선택되면 선택을 리셋
                        setSelectedIndex(-1);
                    } else {
                        isHint = false;
                    }
                }
            }
        });
    }

    private void setHintText(String hint) {
        JTextField editor = (JTextField) this.getEditor().getEditorComponent();
        if (editor != null) {
            editor.setText(hint);
            editor.setForeground(Color.GRAY);

            // 최대 길이 및 숫자만 허용하도록 설정
            PlainDocument doc = (PlainDocument) editor.getDocument();
            doc.setDocumentFilter(new NumericLengthDocumentFilter(maxLength));

            // 포커스 리스너 추가
            editor.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (editor.getText().equals(hint)) {
                        editor.setText("");
                        editor.setForeground(Color.BLACK);
                        isHint = false;
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (editor.getText().isEmpty()) {
                        editor.setText(hint);
                        editor.setForeground(Color.GRAY);
                        isHint = true;
                    }
                }
            });

            // 힌트 텍스트가 있는 경우, 사용자 입력 방지
            editor.addActionListener(e -> {
                if (isHint) {
                    editor.setText(hint);
                }
            });
        }
    }

    @Override
    public void setSelectedIndex(int index) {
        JTextField editor = (JTextField) this.getEditor().getEditorComponent();
        if (editor != null && index == -1 && editor.getText().equals(hint)) {
            // 힌트가 선택될 때의 동작
            return;
        }
        super.setSelectedIndex(index);
        isHint = false;
    }

    @Override
    public void setModel(ComboBoxModel<E> model) {
        super.setModel(model);
        // 모델이 설정된 후에 힌트 텍스트를 적용
        SwingUtilities.invokeLater(() -> setHintText(hint));
    }

    /**
     * 최대 입력 길이를 설정합니다.
     * @param length 최대 길이
     */
    public void setMaxLength(int length) {
        this.maxLength = length;
        setHintText(this.hint); // 길이 제한을 다시 설정
    }

    /**
     * 숫자와 최대 길이를 제한하는 DocumentFilter 클래스입니다.
     */
    private class NumericLengthDocumentFilter extends DocumentFilter {
        private final int maxLength;

        NumericLengthDocumentFilter(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            String filteredString = string.replaceAll("[^0-9]", ""); // 숫자만 허용
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + filteredString + currentText.substring(offset);
            if (newText.length() <= maxLength) {
                super.insertString(fb, offset, filteredString, attr);
            } else {
                Toolkit.getDefaultToolkit().beep(); // 입력을 제한할 때 소리 알림
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            String filteredText = text.replaceAll("[^0-9]", ""); // 숫자만 허용
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + filteredText + currentText.substring(offset + length);
            if (newText.length() <= maxLength) {
                super.replace(fb, offset, length, filteredText, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep(); // 입력을 제한할 때 소리 알림
            }
        }
    }

    /**
     * 콤보박스의 입력칸을 커스터마이즈하는 에디터입니다.
     */
    private class CustomComboBoxEditor extends BasicComboBoxEditor {
        @Override
        protected JTextField createEditorComponent() {
            JTextField editor = super.createEditorComponent();
            // 텍스트 필드의 여백을 설정합니다.
            editor.setBorder(new EmptyBorder(0, PADDING, 0, PADDING)); // 상하 여백은 0, 좌우 여백은 PADDING으로 설정
            return editor;
        }
    }
}
