/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudoku;

import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Ines e Stéphane
 */
public class Server extends UnicastRemoteObject implements SudokuInterface {

    private static ArrayList<Sudoku> games = new ArrayList();

    private Map<PlayerInterface, Player> players = new HashMap<>();
    LocalDateTime horaAtual;
    private static Sudoku selected;
    private static Boolean started = false;

    Server() throws RemoteException {
        super();
    }

    //lista de jogadores com o nome pelo menos
    public static void main(String[] args) {
        int[][] solved1 = {{6, 5, 9, 3, 1, 4, 2, 8, 7},
        {1, 8, 7, 6, 5, 2, 4, 3, 9},
        {2, 3, 4, 8, 9, 7, 5, 1, 6},
        {4, 2, 6, 1, 3, 5, 9, 7, 8},
        {8, 7, 1, 9, 4, 6, 3, 5, 2},
        {5, 9, 3, 2, 7, 8, 6, 4, 1},
        {3, 1, 2, 5, 8, 9, 7, 6, 4},
        {7, 6, 5, 4, 2, 1, 8, 9, 3},
        {9, 4, 8, 7, 6, 3, 1, 2, 5}};

        int[][] unsolved1 = {{6, 5, 9, 0, 1, 0, 2, 8, 0},
        {1, 0, 0, 0, 5, 0, 0, 3, 0},
        {2, 0, 0, 8, 0, 0, 0, 1, 0},
        {0, 0, 0, 1, 3, 5, 0, 7, 0},
        {8, 0, 0, 9, 0, 0, 0, 0, 2},
        {0, 0, 3, 0, 7, 8, 6, 4, 0},
        {3, 0, 2, 0, 0, 9, 0, 0, 4},
        {0, 0, 0, 0, 0, 1, 8, 0, 0},
        {0, 0, 8, 7, 6, 0, 0, 0, 0}};

        int[][] solved2 = {{7, 3, 6, 8, 1, 4, 9, 2, 5},
        {5, 4, 9, 6, 3, 2, 1, 7, 8},
        {2, 1, 8, 9, 5, 7, 3, 6, 4},
        {6, 5, 4, 2, 8, 1, 7, 9, 3},
        {9, 2, 3, 7, 4, 6, 8, 5, 1},
        {1, 8, 7, 3, 9, 5, 2, 4, 6},
        {8, 7, 5, 4, 2, 3, 6, 1, 9},
        {3, 6, 1, 5, 7, 9, 4, 8, 2},
        {4, 9, 2, 1, 6, 8, 5, 3, 7}};
        
        int[][] unsolved2 = {{7, 3, 6, 8, 1, 4, 0, 0, 5},
        {0, 0, 9, 6, 0, 2, 0, 7, 8},
        {2, 1, 0, 9, 0, 0, 3, 6, 0},
        {6, 0, 4, 2, 0, 1, 7, 9, 0},
        {0, 2, 0, 7, 0, 0, 0, 5, 1},
        {0, 8, 7, 0, 9, 5, 2, 0, 6},
        {8, 0, 5, 0, 2, 3, 6, 1, 9},
        {0, 0, 0, 5, 0, 0, 4, 0, 2},
        {4, 9, 2, 1, 0, 8, 5, 0, 0}};
        
        int[][] solved3 = {{3, 5, 1, 4, 9, 8, 7, 2, 6},
        {2, 4, 9, 7, 6, 5, 1, 3, 8},
        {6, 7, 8, 1, 3, 2, 9, 5, 4},
        {8, 9, 4, 5, 2, 3, 6, 1, 7},
        {1, 3, 2, 6, 8, 7, 5, 4, 9},
        {5, 6, 7, 9, 1, 4, 2, 8, 3},
        {7, 1, 3, 2, 4, 6, 8, 9, 5},
        {4, 2, 6, 8, 5, 9, 3, 7, 1},
        {9, 8, 5, 3, 7, 1, 4, 6, 2}};
        
        int[][] unsolved3 = {{3, 5, 1, 0, 0, 8, 7, 0, 6},
        {0, 4, 0, 7, 0, 0, 1, 0, 0},
        {0, 7, 0, 0, 0, 0, 9, 5, 4},
        {8, 0, 4, 0, 2, 0, 6, 0, 0},
        {0, 3, 2, 0, 0, 0, 0, 0, 0},
        {0, 6, 7, 0, 1, 0, 0, 0, 3},
        {7, 1, 0, 0, 4, 6, 0, 0, 5},
        {0, 0, 6, 0, 5, 9, 0, 7, 0},
        {9, 8, 0, 3, 0, 0, 4, 0, 2}};
        

        Sudoku game1 = new Sudoku(solved1, unsolved1);
        Sudoku game2 = new Sudoku(solved2, unsolved2);
        Sudoku game3 = new Sudoku(solved3, unsolved3);

        games.add(game1);
        games.add(game2);
        games.add(game3);

        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            Server serv = new Server();
            reg.bind("sudoku", serv);

            System.out.println("Servidor RMI iniciado.\nIP: " + InetAddress.getLocalHost().getHostAddress());

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }

    }

    @Override
    public synchronized Boolean login(PlayerInterface player, String username) throws RemoteException {
        for (Player p : players.values()) {
            if (p.getName().equalsIgnoreCase(username)) {
                return false;
            }
        }

        Player p = new Player(username);
        players.put(player, p);

        notifyPlayerJoined(username);

        if (started) {
            p.setStatus(true);
            player.startGame(selected.getValues(), horaAtual);

        }

        return true;
    }

    private void notifyPlayerJoined(String username) throws RemoteException {
        for (PlayerInterface i : players.keySet()) {
            i.playerJoined(username);
        }
    }

    @Override
    public synchronized void logout(PlayerInterface player) throws RemoteException {
        String username = players.get(player).getName();
        players.remove(player);
        
        if(players.isEmpty()) {
            started = false;
        } else {
            for (PlayerInterface i : players.keySet()) {
                i.playerLeft(username);
            }
            
            for (Player pl : players.values()) {
            if (!pl.status) {
                return;
            }
        }
        
        newGame();
        }
        
    }

    @Override
    public synchronized Boolean move(Move move, PlayerInterface player) throws RemoteException {
        int line = move.getLine();
        int col = move.getColumn();
        int val = move.getValue();

        Player p = players.get(player);

        if (p.getPlay(line, col) == val) {
            return selected.getCorrectValues()[line][col] == val;
        }

        if (selected.getCorrectValues()[line][col] == val) {
            p.incrementScore();
            String score = p.getScore() + "/" + selected.getScore();

            for (PlayerInterface i : players.keySet()) {
                i.playerMove(p.getName(), score);
            }

            if (p.getScore() == selected.getScore()) {
                started = false;
                for (PlayerInterface i : players.keySet()) {
                    players.get(i).resetGame();
                    i.gameEnd(p.getName());
                }

            }

            return true;
        }
        return false;
    }

    @Override
    public synchronized void playerReady(PlayerInterface player) throws RemoteException {
        Player p = players.get(player);
        p.setStatus(true);

        for (Player pl : players.values()) {
            if (!pl.status) {
                return;
            }
        }
        
        newGame();
        
    }
    
    private void newGame() throws RemoteException{
        selected = games.get(new Random().nextInt(games.size()));
        started = true;
        horaAtual = LocalDateTime.now();
        for (PlayerInterface pl : players.keySet()) {
            pl.startGame(selected.getValues(), horaAtual);
        }
    }

    @Override
    public Boolean getGameStatus() throws RemoteException {
        return started;
    }

}
