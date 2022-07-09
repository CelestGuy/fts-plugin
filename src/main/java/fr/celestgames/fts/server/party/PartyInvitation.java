package fr.celestgames.fts.server.party;

import fr.celestgames.fts.FTSMain;
import fr.celestgames.fts.server.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class PartyInvitation {
    private final Player inviter;
    private final Player invited;
    private final String party;
    private final long invitationTimestamp;

    private final BukkitTask firstWarning;
    private final BukkitTask secondWarning;
    private final BukkitTask deletingTask;


    public PartyInvitation(Player inviter, Player invited, String partyName) {
        this.inviter = inviter;
        this.invited = invited;
        this.party = partyName;
        this.invitationTimestamp = System.currentTimeMillis();

        FTSMain plugin = FTSMain.getInstance();

        this.firstWarning = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            invited.sendMessage("§cVotre invitation à la party §a§l" + partyName + "§r§c expire dans §n§630§r§c secondes.");
        }, 20L * 30L);

        this.secondWarning = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            invited.sendMessage("§cVotre invitation à la party §a§l" + partyName + "§r§c expire dans §n§615§r§c secondes.");
        }, 20L * 45L);

        this.deletingTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            PartyManager.getInstance().removeInvitation(invited, partyName);
            invited.sendMessage("§cVotre invitation à la party §a§l" + partyName + "§r§c a expiré.");
        }, 20L * 60L);
    }

    public void destroy() {
        this.firstWarning.cancel();
        this.secondWarning.cancel();
        this.deletingTask.cancel();
    }

    public Player getInviter() {
        return inviter;
    }

    public Player getInvited() {
        return invited;
    }

    public String getPartyName() {
        return party;
    }

    public long getInvitationTimestamp() {
        return invitationTimestamp;
    }

    public boolean equals(Object o) {
        if (o instanceof PartyInvitation pi) {
            return pi.getInviter().equals(inviter) && pi.getInvited().equals(invited) && pi.getPartyName().equals(party);
        }
        return false;
    }
}
