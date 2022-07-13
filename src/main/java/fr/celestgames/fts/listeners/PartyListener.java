package fr.celestgames.fts.listeners;

import fr.celestgames.fts.events.party.*;
import fr.celestgames.fts.exceptions.MinigameException;
import fr.celestgames.fts.minigames.Minigame;
import fr.celestgames.fts.server.RoomManager;
import fr.celestgames.fts.server.party.Party;
import fr.celestgames.fts.server.PartyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PartyListener implements Listener {
    @EventHandler
    public void onPlayerJoinParty(PlayerJoinPartyEvent event) throws MinigameException {
        Party party = event.getParty();
        Player player = event.getPlayer();
        Minigame partyRoom = RoomManager.getInstance().getPartyRoom(party);
        player.sendMessage("Vous avez rejoint la party §a§l" + party.getName() + "§r.");

        if (PartyManager.getInstance().getPartyRequests(player) != null) {
            PartyManager.getInstance().getPartyRequests(player).remove(party.getName());
        }

        for (Player member : party.getMembers()) {
            if (!member.equals(player)) {
                member.sendMessage("§b§l" + player.getName() + "§r a §arejoint§r la party.");
            }
        }

        if (partyRoom != null && !partyRoom.hasPlayer(player.getName())) {
            partyRoom.addPlayer(player.getName());
        }
    }

    @EventHandler
    public void onPlayerLeaveParty(PlayerLeavePartyEvent event) throws MinigameException {
        Party party = event.getParty();
        Player player = event.getPlayer();
        Minigame partyRoom = RoomManager.getInstance().getPartyRoom(party);
        player.sendMessage("Vous avez quitté la party §a§l"+ party.getName() +"§r.");

        if (party.getMembers().size() > 0) {
            for (Player member : party.getMembers()) {
                member.sendMessage("§b§l" + player.getName() + "§r a §cquitté§r la party.");
            }
        } else {
            PartyManager.getInstance().removeParty(party.getName());
        }

        if (partyRoom != null && !partyRoom.hasPlayer(player.getName())) {
            partyRoom.removePlayer(player.getName());
        }
    }

    @EventHandler
    public void onLeaderChange(LeaderChangeEvent event) {
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
}
