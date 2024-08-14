package teamProject.components;

import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.AbstractDocument;

public class ResidentNumberFilter extends DocumentFilter {
    private int maxLength;

    public ResidentNumberFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;
        StringBuilder currentText = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        currentText.insert(offset, string);

        if (currentText.length() <= maxLength) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) return;
        StringBuilder currentText = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        currentText.replace(offset, offset + length, text);

        if (currentText.length() <= maxLength) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }
}
