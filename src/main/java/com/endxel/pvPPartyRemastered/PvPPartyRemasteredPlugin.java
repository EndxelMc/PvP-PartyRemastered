package com.endxel.pvPPartyRemastered;

import org.bukkit.plugin.java.JavaPlugin;

public class PvPPartyRemasteredPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled :D");

        registerCommands();
    }

    private void registerCommands() {
        CommandService.registerCommands(this);
    }

    @Override
    public void onDisable() {

    }
}
