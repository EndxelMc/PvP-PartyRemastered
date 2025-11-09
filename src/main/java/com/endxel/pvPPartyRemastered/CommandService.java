package com.endxel.pvPPartyRemastered;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandService {
    public static void registerCommands(JavaPlugin plugin) {
        // "/start" -> GameService.startGame()
        PluginCommand gamestartCommand =  plugin.getCommand("gamestart");
        if (gamestartCommand != null){
             gamestartCommand.setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
                    GameManager.startGame();

                    return true;
                }
            });
        }
    }
}