package fr.celestgames.fts.events.party;

import fr.celestgames.fts.server.Party;
import org.bukkit.entity.Player;

public class LeaderTeleport extends PartyEvent {
    private final Player player;

    public LeaderTeleport(Party party, Player player) {
        super(party);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
