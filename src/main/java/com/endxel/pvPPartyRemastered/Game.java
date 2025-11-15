package com.endxel.pvPPartyRemastered;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.endxel.pvPPartyRemastered.LoggerService.*;

public class Game {

    private final List<Player> players;
    private final Map<Player, Location> previousLocations;
    private GameState state;
    private Location gameLocation;
    private Location lobbyLocation;
    private int countdownTaskId = -1;

    private int countdownSecondsLeft = -1; // -1: symvoliko (den exei ksekinhsei countdown)

    public Game() {
        this.players = new ArrayList<>();
        this.previousLocations = new HashMap<>();
        this.state = GameState.WAITING;
    }

    public void addPlayer(Player player) {
        if (this.state != GameState.WAITING ) {
            warn("Game not in WAITING state!");
        }
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
            warn("Cannot start game - game is not in WAITING state!");
            return;
        }

        if (players.isEmpty()) {
            warn("Cannot start game - no players!");
            return;
        }

        if (gameLocation == null) {
            warn("Cannot start game - game location not set!");
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
        this.countdownSecondsLeft = seconds;
        new BukkitRunnable() {


            @Override
            public void run() {
                if (countdownSecondsLeft <= 0) {
                    // Countdown finished, end the game
                    cancel();
                    endGame();
                    return;
                }

                // Broadcast countdown to all players
                for (Player player : players) {
                    player.sendMessage("§e§lGame ends in: §c" + countdownSecondsLeft + "s");
                    player.sendTitle("§c" + countdownSecondsLeft, "§7Game ending soon...", 0, 20, 10);
                }

                countdownSecondsLeft--;
            }
        }.runTaskTimer(PluginRegistry.getPlugin(), 0L, 20L); // Run every second (20 ticks)
    }

    public void endGame() {
        if (state == GameState.FINISHED) {
            return;
        }

        state = GameState.FINISHED;

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
