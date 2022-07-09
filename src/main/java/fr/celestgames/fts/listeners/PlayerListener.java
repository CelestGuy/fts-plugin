package fr.celestgames.fts.listeners;

import fr.celestgames.fts.FTSMain;
import fr.celestgames.fts.minigames.Minigame;
import fr.celestgames.fts.server.GameManager;
import fr.celestgames.fts.server.PartyManager;
import fr.celestgames.fts.server.party.Party;
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
        clearPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerKickEvent event) {
        clearPlayer(event.getPlayer());
    }

    private void clearPlayer(Player player) {
        //player.getInventory().clear();
        teleportPlayerToLobby(player);
        removePlayerFromTeam(player);
        removePlayerFromParty(player);
        removePlayerFromMinigame(player);
        PartyManager.getInstance().removeInvitations(player);
        for (Party p : PartyManager.getInstance().getParties()) {
            p.removeMember(player);
        }
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

    private void removePlayerFromParty(Player p) {
        Party pa = PartyManager.getInstance().getParty(p.getName());
        if (pa != null) {
            pa.removeMember(p);
        }
    }

    private void removePlayerFromMinigame(Player p) {
        Minigame g = GameManager.getInstance().getPlayerMinigame(p);
        if (g != null) {
            g.removePlayer(p);
        }
    }
}
