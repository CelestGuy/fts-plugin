package fr.celestgames.fts;

import fr.celestgames.fts.server.CommandManager;
import fr.celestgames.fts.listeners.PartyListener;
import fr.celestgames.fts.listeners.PlayerListener;
import fr.celestgames.fts.server.PartyManager;
import fr.celestgames.fts.utIls.YmlFile;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.celestgames.fts.utIls.FileUtil.*;

public class FTSMain extends JavaPlugin {
    private final PartyManager partyManager;

    private YmlFile config;
    private YmlFile maps;

    public FTSMain() {
        partyManager = new PartyManager(this);
    }

    @Override
    public void onLoad() {
        Bukkit.getLogger().warning("[" + this.getName() + "] Vérification des fichiers importants pour le serveur...");
        if (!fileExists("plugins/FTS/maps.yml")) {
            YmlFile ymlFile = new YmlFile();
            ymlFile.addValue("lobby_map", "lobby");
            ymlFile.addValue("hide_n_seek_maps", "hlm");
            ymlFile.addValue("hide_n_seek_maps", "manoir");

            writeFile("plugins/FTS/maps.yml", ymlFile.toString());
            maps = ymlFile;
        } else {
            Bukkit.getLogger().info("[" + this.getName() + "] Fichier maps.yml trouvé. Lecture...");
            maps = new YmlFile("plugins/FTS/maps.yml");
            Bukkit.getLogger().info("[" + this.getName() + "] Fichier maps.yml:" + maps.toString());
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[" + this.getName() + "] Chargement des commandes...");
        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommands();

        Bukkit.getLogger().info("[" + this.getName() + "] Chargement des listeners...");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new PartyListener(partyManager), this);

        Bukkit.getLogger().info("[" + this.getName() + "] Chargement terminé.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[" + this.getName() + "] Désactivé !");
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public YmlFile getConfigFile() {
        return config;
    }

    public YmlFile getMapsFile() {
        return maps;
    }
}
