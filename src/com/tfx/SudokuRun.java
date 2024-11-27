package com.tfx;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author tianfx
 * @date 2024/11/26 16:39
 */
public class SudokuRun extends AnAction {
    
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        if (anActionEvent.getProject() != null) {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(anActionEvent.getProject());
            ToolWindow toolWindow = toolWindowManager.getToolWindow("Sudoku");
            if (toolWindow != null) {
                toolWindow.show(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
                if (toolWindow.getContentManager().getContentCount() < 1) {
                    MainUI mainUi = new MainUI();
                    mainUi.createToolWindowContent(anActionEvent.getProject(), toolWindow);
                }
            }
        }
    }
}
