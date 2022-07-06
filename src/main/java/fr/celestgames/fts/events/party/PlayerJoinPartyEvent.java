package fr.celestgames.fts.events.party;

import fr.celestgames.fts.server.Party;
import org.bukkit.entity.Player;

public class PlayerJoinPartyEvent extends PartyEvent {
    private final Player player;

    public PlayerJoinPartyEvent(Party party, Player player) {
        super(party);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
