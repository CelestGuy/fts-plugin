package fr.celestgames.fts.events.party;

import fr.celestgames.fts.server.party.Party;
import org.bukkit.entity.Player;

public class PlayerLeavePartyEvent extends PartyEvent {
    private final Player player;

    public PlayerLeavePartyEvent(Party party, Player player) {
        super(party);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
