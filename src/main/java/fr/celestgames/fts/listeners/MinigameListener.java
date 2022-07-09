package fr.celestgames.fts.listeners;

import fr.celestgames.fts.events.minigames.PlayerJoinMinigameEvent;
import fr.celestgames.fts.events.minigames.PlayerLeaveMinigameEvent;
import fr.celestgames.fts.minigames.Minigame;
import fr.celestgames.fts.server.GameManager;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static org.bukkit.Bukkit.getServer;

public class MinigameListener implements Listener {

    @EventHandler
    public void onPlayerJoinMinigame(PlayerJoinMinigameEvent event) {
        Player player = event.getPlayer();
        Minigame minigame = event.getMinigame();

        if (minigame != null && player != null) {
            World lobby = getServer().getWorld("lobby");
            if (lobby != null) {
                ArmorStand spawn;
                for (ArmorStand armorStand : lobby.getEntitiesByClass(ArmorStand.class)) {
                    if (armorStand.getCustomName() != null && armorStand.getCustomName().equals(minigame.getType().getAlias() + "_lobby")) {
                        spawn = armorStand;
                        player.teleport(spawn.getLocation());
                        break;
                    }
                }
            }
            event.getPlayer().sendMessage("Vous avez rejoint le mini-jeu §a§l" + minigame.getType().toString() + "§r.");
        }
    }

    @EventHandler
    public void onPlayerLeaveMinigame(PlayerLeaveMinigameEvent event) {
        Player player = event.getPlayer();
        Minigame minigame = event.getMinigame();

        if (minigame != null && player != null) {
            World lobby = getServer().getWorld("lobby");
            if (lobby != null) {
                player.teleport(lobby.getSpawnLocation());
            }

            event.getPlayer().sendMessage("Vous avez quitté le mini-jeu §a§l" + minigame.getType().toString() + "§r.");

            if (minigame.getPlayers().size() < minigame.getMinPlayers() && minigame.isStarted()) {
                minigame.end();
            }

            for (String id : GameManager.getInstance().getGameList()) {
                Minigame game = GameManager.getInstance().getGame(id);
                if (game != null && game.getPlayers().size() == 0) {
                    GameManager.getInstance().removeGame(id);
                }
            }

            for (Player p : minigame.getPlayers()) {
                p.sendMessage("§c§l" + player.getName() + "§r a quitté le mini-jeu §a§l" + minigame.getType().toString() + "§r.");
            }
        }
    }
}
