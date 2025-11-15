package com.endxel.pvPPartyRemastered;

import org.bukkit.plugin.java.JavaPlugin;

public class PvPPartyRemasteredPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled :D");

        // Initialize GameManager with plugin instance
        PluginRegistry.initialize(this);

        registerCommands();
    }

    private void registerCommands() {
        CommandService.registerCommands(this);
    }

    @Override
    public void onDisable() {

    }
}
