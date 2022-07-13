package fr.celestgames.fts.enumerations;

import fr.celestgames.fts.minigames.HideNSeek;
import fr.celestgames.fts.minigames.Minigame;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public enum GameList {
    HIDE_N_SEEK(HideNSeek.class);

    private final Class<? extends Minigame> clazz;

    GameList(Class<? extends Minigame> clazz) {
        this.clazz = clazz;
    }

    public static List<Class<? extends Minigame>> getMinigames() {
        List<Class<? extends Minigame>> minigames = new ArrayList<>();
        for (GameList game : GameList.values()) {
            minigames.add(game.clazz);
        }
        return minigames;
    }

    public static Minigame getMinigame(GameList game) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return game.clazz.getConstructor().newInstance();
    }
}
