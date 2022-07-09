package fr.celestgames.fts.events.party;

import fr.celestgames.fts.server.party.Party;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartyEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Party party;

    public PartyEvent(Party party) {
        this.party = party;
    }

    public Party getParty() {
        return party;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
