package fr.celestgames.fts.minigames;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public abstract class MiniGame {
    protected final Plugin plugin;
    protected ArrayList<Player> players;
    protected String name;

    protected World world;

    protected final int MIN_PLAYERS;
    protected final int MAX_PLAYERS;

    public MiniGame(Plugin plugin, String name, int minPlayers, int maxPlayers) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.name = name;
        this.MIN_PLAYERS = minPlayers;
        this.MAX_PLAYERS = maxPlayers;
    }

    public abstract void start();

    public abstract void update();

    public abstract void end();

    public abstract void addPlayer(Player player);

    public abstract void removePlayer(Player player);

    public String getName() {
        return name;
    }
}
