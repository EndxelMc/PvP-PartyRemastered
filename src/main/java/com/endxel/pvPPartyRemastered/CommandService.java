package com.endxel.pvPPartyRemastered;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandService {
    public static void registerCommands(JavaPlugin plugin) {
        // "/gamestart" -> Start the game
        PluginCommand gamestartCommand = plugin.getCommand("gamestart");
        if (gamestartCommand != null) {
            gamestartCommand.setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
                    GameManager.startGame();
                    commandSender.sendMessage("§aGame start initiated!");
                    return true;
                }
            });
        }

        // "/gameend" -> End the game
        PluginCommand gameendCommand = plugin.getCommand("gameend");
        if (gameendCommand != null) {
            gameendCommand.setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
                    GameManager.endGame();
                    commandSender.sendMessage("§cGame ended!");
                    return true;
                }
            });
        }

        // "/setgamelocation" -> Set game spawn location
        PluginCommand setGameLocationCommand = plugin.getCommand("setgamelocation");
        if (setGameLocationCommand != null) {
            setGameLocationCommand.setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
                    if (!(commandSender instanceof Player)) {
                        commandSender.sendMessage("§cThis command can only be used by players!");
                        return true;
                    }

                    Player player = (Player) commandSender;
                    GameManager.setDefaultGameLocation(player.getLocation());
                    commandSender.sendMessage("§aGame location set to your current position!");
                    return true;
                }
            });
        }

        // "/setlobbylocation" -> Set lobby return location
        PluginCommand setLobbyLocationCommand = plugin.getCommand("setlobbylocation");
        if (setLobbyLocationCommand != null) {
            setLobbyLocationCommand.setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
                    if (!(commandSender instanceof Player)) {
                        commandSender.sendMessage("§cThis command can only be used by players!");
                        return true;
                    }

                    Player player = (Player) commandSender;
                    GameManager.setDefaultLobbyLocation(player.getLocation());
                    commandSender.sendMessage("§aLobby location set to your current position!");
                    return true;
                }
            });
        }
    }
}