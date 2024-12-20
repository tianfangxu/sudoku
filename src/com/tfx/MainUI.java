package com.tfx;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.tfx.mod.SudokuMod;
import com.tfx.mod.TButton;
import com.tfx.utils.SudokuUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

/**
 * @author tianfx
 * @date 2024/11/26 16:39
 */
public class MainUI implements ToolWindowFactory, DumbAware {

    private Project project;
    private JSlider jSlider;
    private JPanel area;
    
    private volatile boolean falg = false;

    public static void main(String[] args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent jComponent = new MainUI().getSudoku();

        jf.setContentPane(jComponent);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        try {
            this.project = project;
            JComponent jComponent = getSudoku();
            Content content = toolWindow.getContentManager().getFactory().createContent(jComponent, "Sudoku", false);
            toolWindow.getContentManager().addContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private JComponent getSudoku() {
        JPanel panel = new JBPanel(null);
        panel.setPreferredSize(new Dimension(390,400));

        JButton create = new JButton("Start");
        create.setLocation(5,5);
        create.setSize(120,26);

        JButton result = new JButton("Check Result");
        result.setLocation(130,5);
        result.setSize(120,26);

        JBLabel model = new JBLabel("Level：");
        model.setLocation(10,36);
        model.setSize(50,26);
        JBLabel field1 = new JBLabel("seay");
        field1.setLocation(60,36);
        field1.setSize(32,26);
        this.jSlider = getLevelPanel(92, 36);
        JBLabel field2 = new JBLabel("hard");
        field2.setLocation(252,36);
        field2.setSize(32,26);

        area = new JBPanel(null);
        area.setLocation(5,80);
        area.setSize(360,360);
        JBLabel msg = new JBLabel("loading Please Wait....");
        msg.setLocation(130,150);
        area.add(msg);
        setAreas(area);
        setAuxiliaryLines(area);

        create.addActionListener(getCreateSudokuPanel(area,msg));
        result.addActionListener(setResultSudoku(area,msg));

        /* 添加 组件 到 内容面板 */
        panel.add(create);
        panel.add(result);
        panel.add(area);
        panel.add(model);
        panel.add(jSlider);
        panel.add(field1);
        panel.add(field2);

        // 创建一个JScrollPane，并将JPanel作为参数传入

        return new JBScrollPane(panel);
    }
    
    public JSlider getLevelPanel(int x,int y){
        JSlider jSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
        jSlider.setLocation(x,y);
        jSlider.setSize(160,26);
        jSlider.setMajorTickSpacing(2);
        jSlider.setSnapToTicks(true);
        return jSlider;
    }

    private void setAuxiliaryLines(JPanel area) {
        JBColor jbColor = JBColor.GRAY;
        
        JPanel panel2 = new JPanel();
        panel2.setBorder(BorderFactory.createMatteBorder(0,2,2,2, jbColor));
        panel2.setLocation(120,0);
        panel2.setSize(122,119);
        area.add(panel2);

        JPanel panel4 = new JPanel();
        panel4.setBorder(BorderFactory.createMatteBorder(2,0,2,2, jbColor));
        panel4.setLocation(0,117);
        panel4.setSize(122,122);
        area.add(panel4);
        JPanel panel6 = new JPanel();
        panel6.setBorder(BorderFactory.createMatteBorder(2,2,2,0, jbColor));
        panel6.setLocation(240,117);
        panel6.setSize(122,122);
        area.add(panel6);
        JPanel panel8 = new JPanel();
        panel8.setBorder(BorderFactory.createMatteBorder(2,2,0,2, jbColor));
        panel8.setLocation(120,237);
        panel8.setSize(122,122);
        area.add(panel8);
    }

    private ActionListener setResultSudoku(JPanel area,JBLabel msg) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 1; i <= 81; i++) {
                    JButton button = ((JButton)area.getComponent(i));
                    String trueVal = String.valueOf(MyPersistentStateComponent.getInstance().getState().getResult()[i-1]);
                    if (button.getText() != null && button.getText().length() > 0 && !button.getText().equals(trueVal)){
                        button.setBackground(JBColor.RED);
                    }else{
                        button.setBackground(null);
                    }
                    button.setText(trueVal);
                }
                area.repaint();
                msg.setForeground(JBColor.GREEN);
                msg.setText("Perfect Completion！！！");
                msg.setSize(240,26);
            }
        };
    }

    private void setAreas(JPanel area) {
        int x = 3;
        int y = 0;
        List<TButton> buttonList = new ArrayList<>();
        for (int i = 1; i <= 81; i++) {
            TButton button = new TButton("");
            button.setLocation(x,y);
            button.setSize(38,38);
            if (i%9 == 0) {
                x = 0;y+=39;
            }else{
                x += 39;
            }
            
            if (i%3 == 0){
                x+=3;
            }
            if (i%27 == 0){
                y+=3;
            }
            buttonList.add(button);
            area.add(button);
        }
        SudokuMod sudokuMod = MyPersistentStateComponent.getInstance().getState();
        if (sudokuMod != null && sudokuMod.getSudoku() != null) {
            setVal(area);
        }
        for (int numIndex = 0; numIndex < buttonList.size(); numIndex++) {
            TButton button = buttonList.get(numIndex);
            int finalNumIndex = numIndex;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (button.isEdit()) {
                        // 获取按钮的位置，设置数字键盘显示在按钮下方
                        Point location = button.getLocationOnScreen();
                        getJBPopupKeyboard(button, finalNumIndex).showInScreenCoordinates(button, new Point(location.x, location.y + button.getHeight() + 25));
                    }
                    setHighlight(button.getText());
                }
            });
        }
    }
    
    public void setHighlight(String text){
        for (int i = 1; i <= 81; i++) {
            TButton tButton = (TButton)area.getComponent(i);
            if (text != null && text.length() == 1 && Objects.equals(tButton.getText(),text)){
                tButton.setBackground(JBColor.ORANGE);
            }else{
                tButton.setBackground(null);
            }
        }
        area.repaint();
    }

    private ActionListener getCreateSudokuPanel(JPanel area,JBLabel msg) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (falg){
                    return;
                }
                falg = true;
                msg.setSize(240,26);
                msg.setForeground(null);
                msg.setText("loading Please Wait....");
                area.repaint();
                new Thread(getSudokuRun(area,msg)).start();
            }
        };
    }
    
    private Runnable getSudokuRun(JPanel area,JBLabel msg){
        return ()->{
            try {
                MyPersistentStateComponent stateComponent = MyPersistentStateComponent.getInstance();
                Integer[] sudoku = SudokuUtil.create(40 - this.jSlider.getValue());
                Integer[] result = SudokuUtil.getResult(SudokuUtil.transf(sudoku));
                Integer[] handle = SudokuUtil.createEmpty81();
                stateComponent.setSudoku(sudoku);
                stateComponent.setResult(result);
                stateComponent.setHandle(handle);
                setVal(area);
                msg.setSize(0,0);
                area.repaint();
            }finally {
                falg = false;
            }
        };
    }
    
    private void setVal(JPanel area){
        SudokuMod state = MyPersistentStateComponent.getInstance().getState();
        Integer[] sudoku = state.getSudoku();
        Integer[] handle = state.getHandle();
        for (int i = 1; i <= 81; i++) {
            TButton component = (TButton)area.getComponent(i);
            component.setText("");
            component.setBackground(null);
            component.setEdit(true);
            if (sudoku[i-1] == 0){
                if (handle != null && handle[i-1] > 0){
                    component.setText(String.valueOf(handle[i-1]));
                }
                continue;
            }
            component.setText(String.valueOf(sudoku[i-1]));
            component.setEdit(false);
        }
    }
    
    private Integer[] getVal(JPanel area){
        Integer[] sudoku = new Integer[81];
        for (int i = 1; i <= 81; i++) {
            JButton component = (JButton)area.getComponent(i);
            String text = component.getText();
            if (text == null || text.length() != 1){
                sudoku[i-1] = 0;
            }else{
                sudoku[i-1] = Integer.parseInt(text);
            }
        }
        return sudoku;
    }
    
    public int getIndex(JPanel area,TButton textButton){
        for (int i = 1; i <= 81; i++) {
            if (area.getComponent(i) == textButton) {
                return i-1;
            }
        }
        return 0;
    }
    
    public JBPopup getJBPopupKeyboard(TButton textButton,int numIndex){
        // 创建数字键盘的面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 3, 5, 5));

        List<JButton> nums = new ArrayList<>();
        // 创建数字按钮
        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            panel.add(button);
            nums.add(button);
        }
        
        //添加提示按钮
//        JButton tips = new JButton("answer");
//        tips.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (MyPersistentStateComponent.getInstance().getState() != null && MyPersistentStateComponent.getInstance().getState().getResult() != null) {
//                    textButton.setText(String.valueOf(MyPersistentStateComponent.getInstance().getState().getResult()[numIndex]));
//                    textButton.setBackground(null);
//                    textButton.setFont(null);
//                    textButton.setMark(false);
//                }
//            }
//        });
//        panel.add(tips);

        JButton tips = new JButton("tips");
        tips.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MyPersistentStateComponent.getInstance().getState() != null && MyPersistentStateComponent.getInstance().getState().getResult() != null) {
                    Integer[] ints = getVal(area);
                    int index = getIndex(area, textButton);
                    List<Integer> vals = SudokuUtil.getVals(SudokuUtil.transf(ints), index / 9, index % 9);
                    for (int i = 0; i < nums.size(); i++) {
                        if (!vals.contains(i+1)){
                            nums.get(i).setEnabled(false);
                        }
                    }
                }
            }
        });
        panel.add(tips);

        // 添加清空按钮
        JButton clear = new JButton("reset");
        panel.add(clear);

        // 添加标记按钮
        String markText = textButton.isMark()?"unmark":"mark";
        JButton mark = new JButton(markText);
        mark.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textButton.isMark()){
                    textButton.setBackground(null);
                    textButton.setFont(null);
                    if (textButton.getText()==null || removeHtml(textButton.getText()).length() != 1){
                        textButton.setText("");
                    }
                    textButton.setMark(false);
                    mark.setText("mark");
                }else{
                    Font font = new Font(null,0,10);
                    textButton.setFont(font);
                    textButton.setBackground(JBColor.YELLOW);
                    textButton.setText(htmlText(textButton.getText(),null));
                    textButton.setMark(true);
                    mark.setText("unmark");
                }
            }
        });
        panel.add(mark);
        JBPopup numberKeyboard = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, null)
                .setTitle("numberKeyboard")
                .setMovable(true)
                .setResizable(false)
                .setLocateByContent(true)
                .createPopup();

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textButton.setBackground(null);
                textButton.setFont(null);
                textButton.setText("");
                textButton.setMark(false);
                mark.setText("mark");
            }
        });

        for (int i = 0; i < nums.size(); i++) {
            int finalI = i+1;
            nums.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = String.valueOf(finalI);
                    if (textButton.isMark()) {
                        textButton.setText(htmlText(textButton.getText(), text));
                    }else{
                        textButton.setText(text);
                        textButton.setBackground(null);
                        textButton.setFont(null);
                        numberKeyboard.dispose();
                        MyPersistentStateComponent stateComponent = MyPersistentStateComponent.getInstance();
                        stateComponent.setHandle(getVal(area));
                        setHighlight(text);
                    }
                }
            });
        }

        return numberKeyboard;
    }
    
    public String htmlText(String text,String add){
        String replace = removeHtml(text);
        if (replace.length() == 4){
            replace += "<br>";
        }
        if (add != null){
            replace+=add;
        }
        return "<html>"+replace+"</html>";
    }

    @NotNull
    private String removeHtml(String text) {
        String replace = "";
        if (text != null && text.length() > 0) {
            replace = text.replace("<html>", "").replace("</html>", "");
        }
        return replace;
    }

}
