package com.tfx.mod;

import java.io.Serializable;

/**
 * @author tianfx
 * @date 2024/11/26 16:55
 */
public class SudokuMod implements Serializable {
    
    public int[] sudoku;

    public int[] result;

    public int[] handle;

    public int[] getSudoku() {
        return sudoku;
    }

    public void setSudoku(int[] sudoku) {
        this.sudoku = sudoku;
    }

    public int[] getResult() {
        return result;
    }

    public void setResult(int[] result) {
        this.result = result;
    }

    public int[] getHandle() {
        return handle;
    }

    public void setHandle(int[] handle) {
        this.handle = handle;
    }
}
