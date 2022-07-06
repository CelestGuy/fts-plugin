package fr.celestgames.fts.minigames;

public enum MiniGames {
    HIDE_N_SEEK("hide_n_seek"),
    FLYING_BATTLE("flying_battle");

    private final String id;

    MiniGames(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }
}
