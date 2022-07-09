package fr.celestgames.fts.enumerations;

import fr.celestgames.fts.exceptions.MinigameException;
import fr.celestgames.fts.minigames.HideNSeek;
import fr.celestgames.fts.minigames.Minigame;

public enum GameType {
    HIDE_N_SEEK,
    FLYING_BATTLE,
    MINEKART,
    DE_A_COUDRE,
    HEMBOUK_TAG;

    public String toString() {
        return switch (this) {
            case HIDE_N_SEEK -> "Hide & Seek";
            case FLYING_BATTLE -> "Flying Battle";
            case MINEKART -> "Minekart";
            case DE_A_COUDRE -> "De A Coudre";
            case HEMBOUK_TAG -> "Hembouk Tag";
        };
    }

    public String getName() {
        return switch (this) {
            case HIDE_N_SEEK -> "hide_n_seek";
            case FLYING_BATTLE -> "flying_battle";
            case MINEKART -> "minekart";
            case DE_A_COUDRE -> "de_a_coudre";
            case HEMBOUK_TAG -> "hembouk_tag";
        };
    }

    public String getAlias() {
        return switch (this) {
            case HIDE_N_SEEK -> "hns";
            case FLYING_BATTLE -> "fb";
            case MINEKART -> "mk";
            case DE_A_COUDRE -> "dac";
            case HEMBOUK_TAG -> "ht";
        };
    }
    
    public Minigame getGame() throws MinigameException {
        return switch (this) {
            case HIDE_N_SEEK -> new HideNSeek();
            case FLYING_BATTLE -> null;
            case MINEKART -> null;
            case DE_A_COUDRE -> null;
            case HEMBOUK_TAG -> null;
        };
    }

    public static GameType getGame(String name) {
        return switch (name) {
            case "hide_n_seek", "hns" -> HIDE_N_SEEK;
            case "flying_battle", "fb" -> FLYING_BATTLE;
            case "minekart", "mk" -> MINEKART;
            case "de_a_coudre", "dac" -> DE_A_COUDRE;
            case "hembouk_tag", "ht" -> HEMBOUK_TAG;
            default -> null;
        };
    }
}
