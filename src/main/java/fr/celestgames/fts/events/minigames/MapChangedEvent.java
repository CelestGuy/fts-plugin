package fr.celestgames.fts.events.minigames;

import fr.celestgames.fts.minigames.Minigame;

public class MapChangedEvent extends MinigameEvent {
    private final String mapID;

    public MapChangedEvent(Minigame minigame, String mapID) {
        super(minigame);
        this.mapID = mapID;
    }

    public String getMapID() {
        return mapID;
    }
}
