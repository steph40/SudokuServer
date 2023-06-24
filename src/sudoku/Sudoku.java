/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudoku;

/**
 *
 * @author Ines e Stéphane
 */
public class Sudoku {
    private int[][] values = new int[9][9];
    private int[][] correct = new int[9][9];
    
    int score = 0;
    
    Sudoku(int[][] correct, int values[][]) {
        this.correct = correct;
        this.values = values;
        calculateScore(); 
    }
    
    private void calculateScore() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(values[i][j] == 0) {
                    score++;
                }
            }
        }
    }
    
    public int[][] getValues() {
        return values;
    }
    
    public int[][] getCorrectValues() {
        return correct;
    }
    
    public int getScore() {
        return score;
    }
    
}
