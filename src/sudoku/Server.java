/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudoku;

import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 *
 * @author inoca
 */
public class Server extends UnicastRemoteObject implements SudokuInterface {
    
    private static ArrayList<Sudoku> games = new ArrayList();
    
    private Map<PlayerInterface, Player> players = new HashMap<>();
    
    private static Sudoku selected;
    private static Boolean started = false;
    
    Server() throws RemoteException {
        super();
    }
    
    //lista de jogadores com o nome pelo menos
    public static void main(String[] args) {
        int[][] solved1 = {{5, 3, 4, 6, 7, 8, 9, 1, 2},
          {6, 7, 2, 1, 9, 5, 3, 4, 8},
          {1, 9, 8, 3, 4, 2, 5, 6, 7},
          {8, 5, 9, 7, 6, 1, 4, 2, 3},
          {4, 2, 6, 8, 5, 3, 7, 9, 1},
          {7, 1, 3, 9, 2, 4, 8, 5, 6},
          {9, 6, 1, 5, 3, 7, 2, 8, 4},
          {2, 8, 7, 4, 1, 9, 6, 3, 5},
          {3, 4, 5, 2, 8, 6, 1, 7, 9}};
        
        int[][] unsolved1 = {{5, 0, 4, 6, 7, 8, 9, 1, 2},
          {6, 7, 2, 1, 9, 5, 3, 0, 8},
          {1, 9, 8, 3, 4, 2, 5, 6, 7},
          {8, 0, 9, 7, 6, 1, 4, 2, 3},
          {4, 2, 6, 8, 5, 3, 7, 9, 1},
          {7, 1, 3, 9, 2, 4, 8, 5, 6},
          {9, 6, 1, 5, 3, 7, 2, 8, 4},
          {2, 8, 7, 4, 1, 9, 6, 3, 5},
          {3, 4, 5, 2, 8, 6, 1, 7, 9}};
        
        int[][] unsolved2 = {{5, 0, 4, 6, 7, 8, 9, 1, 2},
          {6, 7, 2, 1, 9, 5, 3, 4, 8},
          {1, 9, 8, 3, 4, 2, 5, 6, 7},
          {8, 5, 9, 7, 6, 1, 4, 2, 3},
          {4, 2, 6, 8, 5, 3, 7, 9, 1},
          {7, 1, 3, 9, 2, 4, 8, 5, 6},
          {9, 6, 1, 5, 3, 7, 2, 8, 4},
          {2, 8, 7, 4, 1, 9, 6, 3, 5},
          {3, 4, 5, 2, 8, 6, 1, 7, 9}};
        
        Sudoku game1 = new Sudoku(solved1, unsolved1);
        Sudoku game2 = new Sudoku(solved1, unsolved2);
        
        games.add(game1);
        games.add(game2);
        
        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            Server serv = new Server();
            reg.bind("sudoku", serv);

            System.out.println("Servidor RMI iniciado.\nIP: " + InetAddress.getLocalHost().getHostAddress());
            
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        
        selectGame();
    }
    
    private static void selectGame() {
        selected = games.get(new Random().nextInt(games.size()));
    }

    @Override
    public Boolean login(PlayerInterface player, String username) throws RemoteException {
        for (Player p : players.values()) {
            if (p.getName().equalsIgnoreCase(username)) {
                return false;
            }
        }
        
        Player p = new Player(username);
        players.put(player, p);
        
        notifyPlayerJoined(username);
        
        if(started) {
           p.setStatus(true);
           player.startGame(selected.getValues());
           
        }

        return true;
    }

    private void notifyPlayerJoined(String username) throws RemoteException {
       for (PlayerInterface i : players.keySet()) {
           i.playerJoined(username);
        }
    }

    @Override
    public void logout(PlayerInterface player) throws RemoteException {
        players.remove(player);
    }

    @Override
    public Boolean move(Move move, PlayerInterface player) throws RemoteException {
        System.out.println("entrou");
        
        int line = move.getLine();
        int col = move.getColumn();
        int val = move.getValue();
        
        Player p = players.get(player);

        if(p.getPlay(line, col) == val) {
            System.out.println("yolo");
            return selected.getCorrectValues()[line][col] == val;
        } 
        
        if(selected.getCorrectValues()[line][col] == val) {
            p.incrementScore();
            String score = p.getScore() + "/" + selected.getScore();
                
            for (PlayerInterface i : players.keySet()) {
                i.playerMove(p.getName(), score);
            }
            
            if(p.getScore() == selected.getScore()) {
                selectGame();
                for(PlayerInterface i : players.keySet()) {
                    i.gameEnd(p.getName());
                    players.get(i).status = false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void playerReady(PlayerInterface player) throws RemoteException {
        Player p = players.get(player);
        p.setStatus(true);
        
        for (Player pl : players.values()) {
            if(!pl.status) {
                return;
            }
        }
        
        for(PlayerInterface pl : players.keySet()) {
            started = true;
            pl.startGame(selected.getValues());
        }
    }

    @Override
    public Boolean getGameStatus() throws RemoteException {
        return started;
    }
    
}
