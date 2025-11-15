package com.endxel.pvPPartyRemastered;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class GameManager {
    private static Game activeGame;
    private static Location defaultGameLocation = null;
    private static Location defaultLobbyLocation = null;

    public static void createGame(){
        if(activeGame != null){
            LoggerService.warn("cant create game, active game exists!");
            return;
        }

        activeGame = new Game();
    }

    public static void addPlayerToGame(Game g, Player p){
        g.addPlayer(p);
    }

    public static void startGame() {
        // TODO: fix logic
        if (activeGame != null && activeGame.getState() != GameState.FINISHED) {
            LoggerService.warn("A game is already active!");
            return;
        }

        // Add all online players to the game
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
            activeGame.addPlayer(player);
            player.sendMessage("§6You have been added to the game!");
        }

        if (onlinePlayers.isEmpty()) {
            LoggerService.warn("No players online to start the game!");
            activeGame = null;
            return;
        }

        // Set locations (use defaults or get from config)
        if (defaultGameLocation != null) {
            activeGame.setGameLocation(defaultGameLocation);
        } else {
            // Use world spawn as default game location if not set
            activeGame.setGameLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
        }

        if (defaultLobbyLocation != null) {
            activeGame.setLobbyLocation(defaultLobbyLocation);
        }

        // Start the game
        activeGame.start();
        LoggerService.info("Game started with " + onlinePlayers.size() + " players!");
    }

    public static void endGame() {
        if (activeGame == null) {
            LoggerService.warn("No active game to end!");
            return;
        }

        activeGame.endGame();
    }

    public static void onGameEnd(Game game) {
        if (activeGame == game) {
            activeGame = null;
            LoggerService.info("Game ended and cleared.");
        }
    }

    public static Game getActiveGame() {
        return activeGame;
    }

    public static void setDefaultGameLocation(Location location) {
        defaultGameLocation = location;
    }

    public static void setDefaultLobbyLocation(Location location) {
        defaultLobbyLocation = location;
    }

    public static Location getDefaultGameLocation() {
        return defaultGameLocation;
    }

    public static Location getDefaultLobbyLocation() {
        return defaultLobbyLocation;
    }
}