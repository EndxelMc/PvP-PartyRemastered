package com.endxel.pvPPartyRemastered;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class PvPPartyRemastered extends JavaPlugin {

    private static PvPPartyRemastered instance;
    private GameManager gameManager;
    private WorldManager worldManager;
    private KitManager kitManager;
    private StatsManager statsManager;
    private FileConfiguration config;
    private FileConfiguration kitsConfig;
    private FileConfiguration statsConfig;

    @Override
    public void onEnable() {
        instance = this;

        // Load configurations
        loadConfigs();

        // Initialize managers
        this.gameManager = new GameManager(this);
        this.worldManager = new WorldManager(this);
        this.kitManager = new KitManager(this);
        this.statsManager = new StatsManager(this);

        // Register commands
        registerCommands();

        // Register events
        getServer().getPluginManager().registerEvents(new GameListener(this), this);

        getLogger().info("PvPParty Remastered v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        if (gameManager != null) {
            gameManager.endGame();
        }
        getLogger().info("PvPParty Remastered disabled!");
    }

    private void loadConfigs() {
        saveDefaultConfig();
        config = getConfig();

        // Load kits configuration
        File kitsFile = new File(getDataFolder(), "kits.yml");
        if (!kitsFile.exists()) {
            saveResource("kits.yml", false);
        }
        kitsConfig = YamlConfiguration.loadConfiguration(kitsFile);

        // Load stats configuration
        File statsFile = new File(getDataFolder(), "stats.yml");
        if (!statsFile.exists()) {
            saveResource("stats.yml", false);
        }
        statsConfig = YamlConfiguration.loadConfiguration(statsFile);
    }

    private void registerCommands() {
        // Register all commands from plugin.yml
        getCommand("spectate").setExecutor(new SpectateCommand(this));
        getCommand("unqueue").setExecutor(new QueueCommands(this));
        getCommand("requeue").setExecutor(new QueueCommands(this));
        getCommand("vote").setExecutor(new VoteCommand(this));
        getCommand("pvpparty").setExecutor(new AdminCommands(this));
        getCommand("gamestart").setExecutor(new AdminCommands(this));
        getCommand("pvpstats").setExecutor(new StatsCommand(this));
        getCommand("savemap").setExecutor(new AdminCommands(this));
        getCommand("createkit").setExecutor(new KitCommands(this));
        getCommand("removekit").setExecutor(new KitCommands(this));
        getCommand("listkits").setExecutor(new KitCommands(this));
        getCommand("pause").setExecutor(new AdminCommands(this));
    }

    public static PvPPartyRemastered getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public FileConfiguration getKitsConfig() {
        return kitsConfig;
    }

    public FileConfiguration getStatsConfig() {
        return statsConfig;
    }

    public void saveKitsConfig() {
        try {
            kitsConfig.save(new File(getDataFolder(), "kits.yml"));
        } catch (Exception e) {
            getLogger().severe("Could not save kits.yml: " + e.getMessage());
        }
    }

    public void saveStatsConfig() {
        try {
            statsConfig.save(new File(getDataFolder(), "stats.yml"));
        } catch (Exception e) {
            getLogger().severe("Could not save stats.yml: " + e.getMessage());
        }
    }
}