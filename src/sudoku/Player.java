/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudoku;

/**
 *
 * @author Ines e Stéphane
 */
public class Player {
    
    String name;
    int score;
    Boolean status;
    int[][] plays = new int[9][9];
    
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.status = false;
    }
    
    public void resetGame() {
        score = 0;
        status = false;
        plays = new int[9][9];
    }
    
    public String getName() {
        return name;
    }
    
    public int getScore() {
        return score;
    }
    
    public void incrementScore() {
        score++;
    }
    
    public void reduceScore() {
        score--;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    public void addPlay(int line, int col, int val) {
        plays[line][col] = val;
    }
    
    public int getPlay(int line, int col) {
        return plays[line][col];
    }
}
