package com.tfx.mod;

import com.tfx.utils.SudokuUtil;

import java.io.Serializable;

/**
 * @author tianfx
 * @date 2024/11/26 16:55
 */
public class SudokuMod implements Serializable {
    
    public Integer[] sudoku = SudokuUtil.createEmpty81();

    public Integer[] result = SudokuUtil.createEmpty81();

    public Integer[] handle = SudokuUtil.createEmpty81();

    public Integer[] getSudoku() {
        return sudoku;
    }

    public void setSudoku(Integer[] sudoku) {
        this.sudoku = sudoku;
    }

    public Integer[] getResult() {
        return result;
    }

    public void setResult(Integer[] result) {
        this.result = result;
    }

    public Integer[] getHandle() {
        return handle;
    }

    public void setHandle(Integer[] handle) {
        this.handle = handle;
    }
}
