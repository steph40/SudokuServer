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

    private synchronized void notifyPlayerJoined(String username) throws RemoteException {
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
    public synchronized Boolean getGameStatus() throws RemoteException {
        return started;
    }

}
