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
import com.intellij.util.ui.JBUI;
import com.tfx.mod.SudokuMod;
import com.tfx.utils.SudokuUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author tianfx
 * @date 2024/11/26 16:39
 */
public class MainUI implements ToolWindowFactory, DumbAware {

    private Project project;

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

        JButton create = new JButton("重新生成");
        create.setLocation(5,5);
        create.setSize(120,26);

        JButton result = new JButton("显示结果");
        result.setLocation(130,5);
        result.setSize(120,26);

        JPanel area = new JBPanel(null);
        area.setLocation(5,40);
        area.setSize(360,360);
        JBLabel msg = new JBLabel("正在加载中，请稍后....");
        msg.setLocation(100,150);
        area.add(msg);
        setAreas(area);
        setAuxiliaryLines(area);

        create.addActionListener(getCreateSudokuPanel(area,msg));
        result.addActionListener(setResultSudoku(area));

        /* 添加 组件 到 内容面板 */
        panel.add(create);
        panel.add(result);
        panel.add(area);

        // 创建一个JScrollPane，并将JPanel作为参数传入

        return new JBScrollPane(panel);
    }

    private void setAuxiliaryLines(JPanel area) {
        JBColor jbColor = JBColor.BLUE;
        
        JPanel panel1 = new JPanel();
        panel1.setBorder(BorderFactory.createMatteBorder(0,0,1,1,jbColor));
        panel1.setLocation(0,0);
        panel1.setSize(120,120);
        area.add(panel1);
        JPanel panel2 = new JPanel();
        panel2.setBorder(BorderFactory.createMatteBorder(0,1,1,1, jbColor));
        panel2.setLocation(120,0);
        panel2.setSize(120,120);
        area.add(panel2);
        JPanel panel3 = new JPanel();
        panel3.setBorder(BorderFactory.createMatteBorder(0,1,1,0, jbColor));
        panel3.setLocation(240,0);
        panel3.setSize(120,120);
        area.add(panel3);

        JPanel panel4 = new JPanel();
        panel4.setBorder(BorderFactory.createMatteBorder(1,0,1,1, jbColor));
        panel4.setLocation(0,120);
        panel4.setSize(120,120);
        area.add(panel4);
        JPanel panel5 = new JPanel();
        panel5.setBorder(BorderFactory.createMatteBorder(1,1,1,1, jbColor));
        panel5.setLocation(120,120);
        panel5.setSize(120,120);
        area.add(panel5);
        JPanel panel6 = new JPanel();
        panel6.setBorder(BorderFactory.createMatteBorder(1,1,1,0, jbColor));
        panel6.setLocation(240,120);
        panel6.setSize(120,120);
        area.add(panel6);
        
        JPanel panel7 = new JPanel();
        panel7.setBorder(BorderFactory.createMatteBorder(1,0,0,1, jbColor));
        panel7.setLocation(0,240);
        panel7.setSize(120,120);
        area.add(panel7);
        JPanel panel8 = new JPanel();
        panel8.setBorder(BorderFactory.createMatteBorder(1,1,0,1, jbColor));
        panel8.setLocation(120,240);
        panel8.setSize(120,120);
        area.add(panel8);
        JPanel panel9 = new JPanel();
        panel9.setBorder(BorderFactory.createMatteBorder(1,1,0,0, jbColor));
        panel9.setLocation(240,240);
        panel9.setSize(120,120);
        area.add(panel9);

        area.setBorder(JBUI.Borders.customLine(jbColor,1,1,1,1));
    }

    private ActionListener setResultSudoku(JPanel area) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 1; i <= 81; i++) {
                    JButton button = ((JButton)area.getComponent(i));
                    String trueVal = String.valueOf(MyPersistentStateComponent.getInstance().getState().getResult()[i-1]);
                    if (button.getText() != null && button.getText().length() > 0 && !button.getText().equals(trueVal)){
                        button.setBackground(JBColor.RED);
                    }
                    button.setText(trueVal);
                }
            }
        };
    }

    private void setAreas(JPanel area) {
        int x = 0;
        int y = 0;
        for (int i = 1; i <= 81; i++) {
            JButton button = new JButton("");
            button.setLocation(x,y);
            button.setSize(38,38);
            if (i%9 == 0) {
                x = 0;y+=40;
            }else{
                x += 40;
            }
            int numIndex = i-1;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 获取按钮的位置，设置数字键盘显示在按钮下方
                    Point location = button.getLocationOnScreen();
                    getJBPopupKeyboard(button, numIndex).showInScreenCoordinates(button, new Point(location.x, location.y + button.getHeight()+25));
                }
            });
            area.add(button);
        }
    }

    private ActionListener getCreateSudokuPanel(JPanel area,JBLabel msg) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msg.setSize(240,26);
                area.repaint();
                new Thread(getSudokuRun(area,msg)).start();
            }
        };
    }
    
    private Runnable getSudokuRun(JPanel area,JBLabel msg){
        return ()->{
            SudokuMod sudokuMod = new SudokuMod();
            int[] result = new int[81];
            sudokuMod.setSudoku(SudokuUtil.create(result));
            sudokuMod.setResult(result);
            SudokuUtil.printArr(result);
            MyPersistentStateComponent.getInstance().loadState(sudokuMod);
            setVal(area);
            msg.setSize(0,0);
            area.repaint();
        };
    }
    
    private void setVal(JPanel area){
        int[] sudoku = MyPersistentStateComponent.getInstance().getState().getSudoku();
        for (int i = 1; i <= 81; i++) {
            JButton component = (JButton)area.getComponent(i);
            component.setText("");
            component.setBackground(null);
            if (sudoku[i-1] == 0){
                continue;
            }
            component.setText(String.valueOf(sudoku[i-1]));
        }
    }
    
    public JBPopup getJBPopupKeyboard(JButton textButton,int numIndex){
        // 创建数字键盘的面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 3, 5, 5));

        // 创建数字按钮
        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            int finalI = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textButton.setText(String.valueOf(finalI));
                }
            });
            panel.add(button);
        }
        
        //添加提示按钮
        JButton tips = new JButton("提示");
        tips.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MyPersistentStateComponent.getInstance().getState() != null && MyPersistentStateComponent.getInstance().getState().getResult() != null) {
                    textButton.setText(String.valueOf(MyPersistentStateComponent.getInstance().getState().getResult()[numIndex]));
                }
            }
        });
        panel.add(tips);

        // 添加0按钮
        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textButton.setText("0");
            }
        });
        panel.add(zeroButton);

        // 添加关闭按钮
        JButton closeButton = new JButton("关闭");
        panel.add(closeButton);
        JBPopup numberKeyboard = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, null)
                .setTitle("数字键盘")
                .setMovable(true)
                .setResizable(false)
                .setLocateByContent(true)
                .createPopup();

        for (int i = 0; i < panel.getComponentCount(); i++) {
            ((JButton)panel.getComponent(i)).addActionListener(closeNumberKeyboard(numberKeyboard));
        }
        return numberKeyboard;
    }
    
    public ActionListener closeNumberKeyboard(JBPopup numberKeyboard){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberKeyboard.dispose();
            }
        };
    }

}