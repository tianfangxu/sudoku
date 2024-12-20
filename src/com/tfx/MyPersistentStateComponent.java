package com.tfx;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.tfx.mod.SudokuMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author tianfx
 * @date 2024/11/26 16:54
 */
@State(
        name = "MyPluginPersistentState",
        storages = {@Storage("sudokuTfx.xml")} 
)
public class MyPersistentStateComponent implements PersistentStateComponent<SudokuMod> {
    
    static  MyPersistentStateComponent component;

    SudokuMod sudokuMod = new SudokuMod();
    
    @Override
    public @Nullable SudokuMod getState() {
        return sudokuMod;
    }

    @Override
    public void loadState(@NotNull SudokuMod sudokuMod) {
        this.sudokuMod = sudokuMod;
    }

    public static MyPersistentStateComponent getInstance() {
        try {
            return ApplicationManager.getApplication().getService(MyPersistentStateComponent.class);
        }catch (Exception e){
            return component==null?(component = new MyPersistentStateComponent()):component;
        }
    }
    
    public void setSudoku(Integer[] sudoku) {
        sudokuMod.setSudoku(sudoku);
    }

    public void setResult(Integer[] result) {
        sudokuMod.setResult(result);
    }
    
    public void setHandle(Integer[] handle) {
        sudokuMod.setHandle(handle);
    }
}
