/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sudoku;

import java.rmi.*;

/**
 *
 * @author inoca
 */
public interface PlayerInterface extends Remote{
    
   public void playerJoined(String username) throws RemoteException;
   public void playerLeft(String username) throws RemoteException;
   
   public void startGame(int[][] values) throws RemoteException;
   public void playerMove(String username, String score) throws RemoteException;
   
   public void gameEnd(String username) throws RemoteException;
}
