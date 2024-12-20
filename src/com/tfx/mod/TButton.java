package com.tfx.mod;

import javax.swing.*;

/**
 * @author tianfx
 * @date 2024/11/27 17:08
 */
public class TButton extends JButton {

    /**
     * Creates a button with no set text or icon.
     */
    public TButton() {
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon the Icon image to display on the button
     */
    public TButton(Icon icon) {
        super(icon);
    }

    /**
     * Creates a button with text.
     *
     * @param text the text of the button
     */
    public TButton(String text) {
        super(text);
    }

    /**
     * Creates a button where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     * @since 1.3
     */
    public TButton(Action a) {
        super(a);
    }

    /**
     * Creates a button with initial text and an icon.
     *
     * @param text the text of the button
     * @param icon the Icon image to display on the button
     */
    public TButton(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * 标记
     */
    private boolean mark = false;

    /**
     * 编辑
     */
    private boolean edit = true;

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}
