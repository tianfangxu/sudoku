package com.tfx.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tianfx
 * @date 2024/11/26 16:19
 */
public class SudokuUtil {
    
    public static Integer[] create(int level){
        Integer[][] crate;
        while (true){
            crate = crateCommon(level);
            if (crate == null){
                continue;
            }
            List<Integer[][]> source = new ArrayList<>();
            sudokuTest(crate,0,0,source);
            if (source.size()  == 1 ){
                break;
            }
        }
        return transf(crate);
    }
    
    public static Integer[] getResult(Integer[][] source){
        List<Integer[][]> results = new ArrayList<>();
        sudokuTest(source,0,0,results);
        if (results.size() == 0){
            return null;
        }else{
            return transf(results.get(0));
        }
    }

    public static Integer[][] crateCommon(int level){
        Integer[][] target = createEmpty99();
        for (int i = 0; i < level; i++) {
            int x = (int) (Math.random() * 81);
            int r = x / 9;
            int c = x % 9;
            if (target[r][c] > 0) {
                i--;
            }else{
                List<Integer> vals = getVals(target, r, c);
                if (vals.size() == 0 ){
                    i--;
                }else{
                    target[r][c] = vals.get((int) (Math.random() * vals.size()));
                }
            }
        }

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (target[r][c] > 0) {
                    continue;
                }else{
                    List<Integer> vals = getVals(target, r, c);
                    if (vals.size() > 5){
                        target[r][c] = vals.get((int) (Math.random() * vals.size()));
                    }else if (vals.size() == 0){
                        return null;
                    }
                }
            }
        }
        return target;
    }

    public static void sudokuTest(Integer[][] target,int rowIndex,int colIndex,List<Integer[][]> source){
        if (rowIndex == 9){
            source.add(target);
            return;
        }
        Integer val = target[rowIndex][colIndex];
        if (val > 0){
            sudokuTest(target,getNextRowIndex(rowIndex,colIndex),getNextColIndex(colIndex),source);
        }else{
            List<Integer> vals = getVals(target, rowIndex, colIndex);
            if (vals.size() == 0){
                return;
            }
            for (Integer v : vals) {
                Integer[][] copy = copy(target);
                copy[rowIndex][colIndex] = v;
                sudokuTest(copy,getNextRowIndex(rowIndex,colIndex),getNextColIndex(colIndex),source);
                if (source.size() > 1){
                    return;
                }
            }
        }
    }

    public static Integer[][] copy(Integer[][] resouce){
        Integer[][] target = createEmpty99();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                target[i][j] = resouce[i][j];
            }
        }
        return target;
    }

    public static int getNextRowIndex(int rowIndex,int colIndex){
        return colIndex == 8 ? rowIndex+1:rowIndex;
    }

    public static int getNextColIndex(int colIndex){
        return colIndex == 8 ? 0:colIndex+1;
    }

    static List<Integer> min = Arrays.asList(0,1,2);
    static List<Integer> mid = Arrays.asList(3,4,5);
    static List<Integer> max = Arrays.asList(6,7,8);
    public static List<Integer> getVals(Integer [][] target,int rowIndex,int colIndex){
        List<Integer> vals = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (target[rowIndex][i]>0) {
                vals.add(target[rowIndex][i]);
            }
            if (target[i][colIndex]>0){
                vals.add(target[i][colIndex]);
            }
        }
        List<Integer> row = min.contains(rowIndex)?min:(mid.contains(rowIndex)?mid:max);
        List<Integer> col = min.contains(colIndex)?min:(mid.contains(colIndex)?mid:max);
        for (Integer r : row) {
            for (Integer c : col) {
                if (target[r][c]>0){
                    vals.add(target[r][c]);
                }
            }
        }
        List<Integer> elseVal = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            if (!vals.contains(i)) {
                elseVal.add(i);
            }
        }
        return elseVal;
    }

    private static void printArr(int[][] target) {
        for (int i = 0; i < target.length; i++) {
            for (int i1 = 0; i1 < target[i].length; i1++) {
                System.out.print(""+ target[i][i1]+" ");
                if (i1==2||i1 == 5){
                    System.out.print("  ");
                }
            }
            if (i==2||i == 5){
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("----------------------");
    }
    
    public static Integer[][] transf(Integer[] source){
        Integer[][] target = createEmpty99();
        for (int i = 0; i < 81; i++) {
            target[i/9][i%9] = source[i];
        }
        return target;
    }
    
    public static Integer[] transf(Integer[][] source){
        Integer[] target = createEmpty81();
        int c = 0;
        for (Integer[] ints : source) {
            for (Integer i : ints) {
                target[c++] = i;
            }
        }
        return target;
    }
    
    public static Integer[] createEmpty81(){
        Integer[] target = new Integer[81];
        Arrays.fill(target, 0);
        return target;
    }
    
    public static Integer[][] createEmpty99(){
        Integer[][] target = new Integer[9][9];
        for (int i = 0; i < target.length; i++) {
            Arrays.fill(target[i], 0);
        }
        return target;
    }
}
