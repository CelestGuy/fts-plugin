package fr.celestgames.fts.events.party;

import fr.celestgames.fts.server.Party;
import org.bukkit.entity.Player;

public class LeaderChange extends PartyEvent {
    private final Player newLeader;

    public LeaderChange(Party party, Player newLeader) {
        super(party);
        this.newLeader = newLeader;
    }

    public Player getNewLeader() {
        return newLeader;
    }
}
