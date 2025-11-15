package com.endxel.pvPPartyRemastered;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginRegistry {
    static private JavaPlugin plugin;

    static void initialize(JavaPlugin plugin){
        if (PluginRegistry.plugin == null){
            PluginRegistry.plugin = plugin;
        }
    }
    static JavaPlugin getPlugin(){
        return plugin;
    }
}