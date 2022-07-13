package fr.celestgames.fts.listeners;

import fr.celestgames.fts.events.minigames.MapChangedEvent;
import fr.celestgames.fts.events.minigames.PlayerJoinMinigameEvent;
import fr.celestgames.fts.events.minigames.PlayerLeaveMinigameEvent;
import fr.celestgames.fts.minigames.Minigame;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static org.bukkit.Bukkit.getPlayer;

public class MinigameListener implements Listener {
    @EventHandler
    public void onPlayerJoinMinigame(PlayerJoinMinigameEvent event) {
        Player player = event.getPlayer();
        Minigame minigame = event.getMinigame();

        if (minigame != null && player != null) {
            for (String memberName : minigame.getPlayers()) {
                Player member = getPlayer(memberName);
                if (member != null && !member.equals(player)) {
                    member.sendMessage("§b§l" + player.getName() + "§r a §arejoint§r le salon.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveMinigame(PlayerLeaveMinigameEvent event) {
        Player player = event.getPlayer();
        Minigame minigame = event.getMinigame();

        if (minigame != null && player != null) {
            for (String memberName : minigame.getPlayers()) {
                Player member = getPlayer(memberName);
                if (member != null) {
                    member.sendMessage("§b§l" + player.getName() + "§r a §cquitté§r le salon.");
                }
            }
        }
    }

    @EventHandler
    public void onMapChangedEvent(MapChangedEvent event) {
        Minigame minigame = event.getMinigame();
        String mapID = event.getMapID();

        if (minigame != null && mapID != null) {
            for (String player : minigame.getPlayers()) {
                Player p = getPlayer(player);
                if (p != null) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("La prochaine map sera: §c" + mapID + "§r."));
                }
            }
        }
    }
}
