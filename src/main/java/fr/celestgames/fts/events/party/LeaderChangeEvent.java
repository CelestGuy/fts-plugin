package fr.celestgames.fts.events.party;

import fr.celestgames.fts.server.party.Party;
import org.bukkit.entity.Player;

public class LeaderChangeEvent extends PartyEvent {
    private final Player newLeader;

    public LeaderChangeEvent(Party party, Player newLeader) {
        super(party);
        this.newLeader = newLeader;
    }

    public Player getNewLeader() {
        return newLeader;
    }
}
