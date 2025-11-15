package com.endxel.pvPPartyRemastered;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final PvPPartyRemasteredPlugin plugin;
    private final List<Player> players;
    private final Map<Player, Location> previousLocations;
    private GameState state;
    private Location gameLocation;
    private Location lobbyLocation;
    private int countdownTaskId = -1;

    public enum GameState {
        WAITING,
        COUNTDOWN,
        ACTIVE,
        ENDING
    }

    public Game(PvPPartyRemasteredPlugin plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.previousLocations = new HashMap<>();
        this.state = GameState.WAITING;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setGameLocation(Location location) {
        this.gameLocation = location;
    }

    public void setLobbyLocation(Location location) {
        this.lobbyLocation = location;
    }

    public void start() {
        if (state != GameState.WAITING) {
            plugin.getLogger().warning("Cannot start game - game is not in WAITING state!");
            return;
        }

        if (players.isEmpty()) {
            plugin.getLogger().warning("Cannot start game - no players!");
            return;
        }

        if (gameLocation == null) {
            plugin.getLogger().warning("Cannot start game - game location not set!");
            return;
        }

        state = GameState.COUNTDOWN;

        // Save player locations before teleporting
        for (Player player : players) {
            previousLocations.put(player, player.getLocation().clone());
        }

        // Teleport all players to game location
        for (Player player : players) {
            player.teleport(gameLocation);
            player.sendMessage("§a§lGame Starting!");
        }

        // Start countdown
        startCountdown(5);
    }

    private void startCountdown(int seconds) {
        new BukkitRunnable() {
            int timeLeft = seconds;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    // Countdown finished, end the game
                    cancel();
                    endGame();
                    return;
                }

                // Broadcast countdown to all players
                for (Player player : players) {
                    player.sendMessage("§e§lGame ends in: §c" + timeLeft + "s");
                    player.sendTitle("§c" + timeLeft, "§7Game ending soon...", 0, 20, 10);
                }

                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every second (20 ticks)
    }

    public void endGame() {
        if (state == GameState.ENDING) {
            return;
        }

        state = GameState.ENDING;

        // Teleport all players back to lobby
        for (Player player : players) {
            Location returnLocation = lobbyLocation != null ? lobbyLocation : previousLocations.get(player);

            if (returnLocation != null) {
                player.teleport(returnLocation);
            }

            player.sendMessage("§c§lGame Ended!");
            player.sendTitle("§c§lGAME OVER", "§7Returning to lobby...", 10, 40, 20);
        }

        // Clear data
        players.clear();
        previousLocations.clear();

        // Notify GameManager that this game has ended
        GameManager.onGameEnd(this);
    }

    public void forceEnd() {
        if (countdownTaskId != -1) {
            Bukkit.getScheduler().cancelTask(countdownTaskId);
            countdownTaskId = -1;
        }
        endGame();
    }
}
