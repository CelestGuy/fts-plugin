package fr.celestgames.fts.events.minigames;

import fr.celestgames.fts.minigames.Minigame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinigameEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Minigame minigame;

    public MinigameEvent(Minigame minigame) {
        this.minigame = minigame;
    }

    public Minigame getMinigame() {
        return minigame;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
