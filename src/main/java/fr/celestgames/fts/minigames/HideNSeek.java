package fr.celestgames.fts.minigames;

import fr.celestgames.fts.listeners.PlayerHitListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class HideNSeek extends MiniGame {
    private PlayerHitListener listener;

    public HideNSeek(Plugin plugin, String mapName) {
        super(plugin, mapName, 4, 16);
    }

    public HideNSeek(Plugin plugin) {
        super(plugin, "hide_n_seek", 4, 16);
    }

    @Override
    public void start() {
        listener = new PlayerHitListener();
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void update() {

    }

    @Override
    public void end() {
        HandlerList.unregisterAll(listener);
    }

    @Override
    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);
    }
}