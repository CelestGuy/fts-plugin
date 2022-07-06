package fr.celestgames.fts.listeners;

import fr.celestgames.fts.events.party.*;
import fr.celestgames.fts.server.Party;
import fr.celestgames.fts.server.PartyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PartyListener implements Listener {
    private final PartyManager partyManager;
    public PartyListener(PartyManager partyManager) {
        this.partyManager = partyManager;
    }

    @EventHandler
    public void onPlayerJoinParty(PlayerJoinPartyEvent event) {
        Party party = event.getParty();
        Player player = event.getPlayer();
        player.sendMessage("Vous avez rejoint la party §a§l" + party.getName() + "§r.");

        if (partyManager.getPartyRequests(player) != null) {
            partyManager.getPartyRequests(player).remove(party.getName());
        }

        for (Player member : party.getMembers()) {
            if (!member.equals(player)) {
                member.sendMessage("§b§l" + player.getName() + "§r a §arejoint§r la party.");
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveParty(PlayerLeavePartyEvent event) {
        Party party = event.getParty();
        Player player = event.getPlayer();
        player.sendMessage("Vous avez quitté la party §a§l"+ party.getName() +"§r.");

        if (party.getMembers().size() > 0) {
            for (Player member : party.getMembers()) {
                member.sendMessage("§b§l" + player.getName() + "§r a §cquitté§r la party.");
            }
        } else {
            partyManager.removeParty(party.getName());
        }
    }

    @EventHandler
    public void onLeaderChange(LeaderChange event) {
        Player newLeader = event.getNewLeader();
        Party party = event.getParty();

        for (Player member : party.getMembers()) {
            if (member.equals(newLeader)) {
                member.sendMessage("Vous êtes le nouveau leader de la party §a§l" + party.getName() + "§r.");
            } else {
                member.sendMessage("Le nouveau leader de la party §a§l" + party.getName() + "§r est §b§l" + newLeader.getName() + "§r.");
            }
        }
    }

    @EventHandler
    public void onLeaderTeleport(LeaderTeleport event) {
        Party party = event.getParty();
        Player leader = party.getLeader();

        for (Player member : party.getMembers()) {
            if (member != leader) {
                member.teleport(leader);
                member.sendMessage("Vous avez été téléporté vers §b§l" + party.getLeader().getName() + "§r.");
            }
        }
    }
}
