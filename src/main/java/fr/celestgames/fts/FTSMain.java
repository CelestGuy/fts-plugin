package fr.celestgames.fts;

import fr.celestgames.fts.enumerations.GameType;
import fr.celestgames.fts.listeners.MinigameListener;
import fr.celestgames.fts.minigames.HideNSeek;
import fr.celestgames.fts.minigames.Minigame;
import fr.celestgames.fts.server.CommandManager;
import fr.celestgames.fts.listeners.PartyListener;
import fr.celestgames.fts.listeners.PlayerListener;
import fr.celestgames.fts.server.GameManager;
import fr.celestgames.fts.server.PartyManager;
import fr.celestgames.fts.utIls.YmlFile;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

import static fr.celestgames.fts.utIls.FileUtil.*;

public class FTSMain extends JavaPlugin {
    private YmlFile config;
    private YmlFile maps;

    private static FTSMain instance;

    public FTSMain() {
        instance = this;
    }

    @Override
    public void onLoad() {
        Bukkit.getLogger().warning("[" + this.getName() + "] Vérification des fichiers importants pour le serveur...");
        if (!fileExists("plugins/FTS/maps.yml")) {
            YmlFile ymlFile = new YmlFile();
            ymlFile.addValue("lobby", "lobby");

            for (GameType gameType : GameType.values()) {
                ymlFile.addValue(gameType.getName(), "");
            }

            writeFile("plugins/FTS/maps.yml", ymlFile.toString());
            maps = ymlFile;
        } else {
            Bukkit.getLogger().info("[" + this.getName() + "] Fichier maps.yml trouvé. Lecture...");
            maps = new YmlFile("plugins/FTS/maps.yml");
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[" + this.getName() + "] Chargement des commandes...");
        for (String minigame : maps.getOptions().keySet()) {
            for (String map : maps.getValues(minigame)) {
                WorldCreator worldCreator = new WorldCreator(map);
                Bukkit.getServer().createWorld(worldCreator);
            }
        }

        Bukkit.getLogger().info("[" + this.getName() + "] Chargement des commandes...");
        CommandManager commandManager = new CommandManager();
        commandManager.registerCommands();

        Bukkit.getLogger().info("[" + this.getName() + "] Chargement des listeners...");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new PartyListener(), this);
        getServer().getPluginManager().registerEvents(new MinigameListener(), this);

        Bukkit.getLogger().info("[" + this.getName() + "] Chargement terminé.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[" + this.getName() + "] Désactivé !");
    }

    public YmlFile getConfigFile() {
        return config;
    }

    public YmlFile getMapsFile() {
        return maps;
    }

    public static FTSMain getInstance() {
        return instance;
    }
}
