/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sudoku;

import java.rmi.*;


/**
 *
 * @author Ines e Stéphane
 */
public interface SudokuInterface extends Remote {
    
    Boolean login(PlayerInterface player, String username) throws RemoteException;
    void logout(PlayerInterface player) throws RemoteException;
    
    void playerReady(PlayerInterface player) throws RemoteException; 
    Boolean move(Move move, PlayerInterface player) throws RemoteException;
    Boolean getGameStatus() throws RemoteException;
}
