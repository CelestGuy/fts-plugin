package fr.celestgames.fts.listeners;

import fr.celestgames.fts.FTSPlugin;
import fr.celestgames.fts.minigames.HideNSeek;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static org.bukkit.Bukkit.getPluginManager;

public class HnSListener implements Listener {
    private final HideNSeek hnsGame;

    public HnSListener(HideNSeek hnsGame) {
        this.hnsGame = hnsGame;
        getPluginManager().registerEvents(this, FTSPlugin.getInstance());
    }

    @EventHandler
    private void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && event.getEntity() instanceof Player victim) {
            String playerName = player.getName();
            String victimName = victim.getName();

            if (hnsGame.isSeeker(playerName) && hnsGame.isHider(victimName)) {
                player.sendMessage("Vous avez touché §b§l" + victimName + "§r !");
                victim.sendMessage("Vous avez été touché par §a§l" + playerName + "§r ! Vous êtes maintenant un §c§lSeeker§r !");
                hnsGame.removeHider(victimName);
                hnsGame.addSeeker(victimName);
            }
        }
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
    }
}
