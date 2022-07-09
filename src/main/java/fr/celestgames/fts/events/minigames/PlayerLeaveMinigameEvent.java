package fr.celestgames.fts.events.minigames;

import fr.celestgames.fts.minigames.Minigame;
import org.bukkit.entity.Player;

public class PlayerLeaveMinigameEvent extends MinigameEvent {
    private final Player player;

    public PlayerLeaveMinigameEvent(Minigame minigame, Player player) {
        super(minigame);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
