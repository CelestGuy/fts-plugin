package fr.celestgames.fts.server;

import fr.celestgames.fts.enumerations.GameType;
import fr.celestgames.fts.exceptions.MinigameException;
import fr.celestgames.fts.minigames.Minigame;
import fr.celestgames.fts.server.party.Party;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    public HashMap<String, Minigame> minigames;

    private GameManager() {
        this.minigames = new HashMap<>();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void createGame(String id, GameType type) throws MinigameException {
        if (this.minigames.containsKey(id)) {
            throw new MinigameException("Un mini-jeu avec l'id §6§l" + id + "§r§c existe déjà.");
        } else {
            Minigame minigame = type.getGame();
            minigames.put(id, minigame);
        }
    }

    public void resetGame(String id) throws MinigameException {
        Minigame minigame = this.minigames.get(id);
        if (minigame != null) {
            String map = minigame.getMapName();
            boolean random = minigame.isRandomMap();
            GameType type = minigame.getType();

            this.minigames.remove(id);

            minigame = type.getGame();

            if (random) {
                minigame.setMapName(map);
            }

            minigames.put(id, minigame);
        }
    }

    public void removeGame(String id) {
        this.minigames.remove(id);
    }

    public void joinGame(GameType type, Player player) throws MinigameException {
        int count = 0;
        for (Minigame minigame : minigames.values()) {
            if (minigame.getType() == type && !minigame.isFull() && !minigame.isStarted()) {
                minigame.addPlayer(player);
                return;
            } else if (minigame.getType() == type && !minigame.isFull() && minigame.isStarted()) {
                minigame.addSpectator(player);
                return;
            } else {
                count++;
            }
        }
        String id = type.getName() + "." + (count + 1);
        player.sendMessage(id);
        createGame(id, type);
        minigames.get(id).addPlayer(player);
    }

    public void joinGame(GameType type, Party party) throws MinigameException {
        int count = 0;
        for (Minigame minigame : minigames.values()) {
            if (minigame.getType() == type && (minigame.getPlayerCount() + party.getMembers().size() <= minigame.getMaxPlayers()) && !minigame.isStarted()) {
                for (Player player : party.getMembers()) {
                    minigame.addPlayer(player);
                }
                return;
            } else if (minigame.getType() == type && (minigame.getPlayerCount() + party.getMembers().size() <= minigame.getMaxPlayers()) && minigame.isStarted()) {
                for (Player player : party.getMembers()) {
                    minigame.addSpectator(player);
                }
                return;
            } else {
                count++;
            }
        }
        String id = type.getName() + "." + (count + 1);
        createGame(id, type);
        for (Player player : party.getMembers()) {
            minigames.get(id).addPlayer(player);
        }
    }

    public List<String> getGameList() {
        return new ArrayList<>(minigames.keySet());
    }

    public List<String> getGameTypeIDs(GameType type) {
        List<String> list = new ArrayList<>();
        for (String id : minigames.keySet()) {
            if (minigames.get(id).getType() == type) {
                list.add(id);
            }
        }
        return list;
    }

    public Minigame getGame(String id) {
        return minigames.get(id);
    }

    public Minigame getPlayerMinigame(Player player) {
        for (Minigame minigame : minigames.values()) {
            if (minigame.getPlayers().contains(player)) {
                return minigame;
            }
        }
        return null;
    }
}
