/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudoku;

import java.io.Serializable;

/**
 *
 * @author Ines e Stéphane
 */
public class Move implements Serializable {
    private int line;
    private int column;
    private int value;

    public Move(int line, int column, int value) {
        this.line = line;
        this.column = column;
        this.value = value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getValue() {
        return value;
    }
}
