package fr.celestgames.fts.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Bienvenue sur le serveur " + event.getPlayer().getName() + " !");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        teleportPlayerToLobby(player);
        removePlayerFromTeam(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerKickEvent event) {
        Player player = event.getPlayer();
        teleportPlayerToLobby(player);
        removePlayerFromTeam(player);
    }

    private void teleportPlayerToLobby(Player player) {
        World lobby = player.getServer().getWorld("lobby");
        if (lobby != null) {
            player.teleport(lobby.getSpawnLocation());
        }
    }

    private void removePlayerFromTeam(Player p) {
        p.getScoreboard().getTeams().forEach(team -> {
            team.removeEntry(p.getName());
        });
    }
}
