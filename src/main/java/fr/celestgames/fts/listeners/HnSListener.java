package fr.celestgames.fts.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.Team;

public class HnSListener implements Listener {
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && event.getEntity() instanceof Player victim) {
            Team seekers = player.getScoreboard().getTeam("seekers");
            Team hiders = player.getScoreboard().getTeam("hiders");
            if (seekers != null && hiders != null) {
                if (seekers.hasEntry(player.getName()) && hiders.hasEntry(victim.getName())) {
                    player.sendMessage("Vous avez touché §a§l" + victim.getName() + "§r !");
                    victim.sendMessage("Vous avez été touché par §a§l" + player.getName() + "§r ! Vous êtes maintenant un §c§lSeeker§r !");
                    hiders.removeEntry(victim.getName());
                    seekers.addEntry(victim.getName());
                }
            }
        }
    }
}
