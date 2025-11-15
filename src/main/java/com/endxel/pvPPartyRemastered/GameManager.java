package com.endxel.pvPPartyRemastered;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class GameManager {
    private static PvPPartyRemasteredPlugin plugin;
    private static Game activeGame = null;
    private static Location defaultGameLocation = null;
    private static Location defaultLobbyLocation = null;

    public static void initialize(PvPPartyRemasteredPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static void startGame() {
        if (activeGame != null && activeGame.getState() != Game.GameState.ENDING) {
            plugin.getLogger().warning("A game is already active!");
            return;
        }

        // Create new game
        activeGame = new Game(plugin);

        // Add all online players to the game
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
            activeGame.addPlayer(player);
            player.sendMessage("§6You have been added to the game!");
        }

        if (onlinePlayers.isEmpty()) {
            plugin.getLogger().warning("No players online to start the game!");
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
        plugin.getLogger().info("Game started with " + onlinePlayers.size() + " players!");
    }

    public static void endGame() {
        if (activeGame == null) {
            plugin.getLogger().warning("No active game to end!");
            return;
        }

        activeGame.endGame();
    }

    public static void onGameEnd(Game game) {
        if (activeGame == game) {
            activeGame = null;
            plugin.getLogger().info("Game ended and cleared.");
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