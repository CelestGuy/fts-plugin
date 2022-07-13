package fr.celestgames.fts.server;

import fr.celestgames.fts.exceptions.MinigameException;
import fr.celestgames.fts.enumerations.GameList;
import fr.celestgames.fts.exceptions.PartyException;
import fr.celestgames.fts.minigames.Minigame;
import fr.celestgames.fts.server.party.Party;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomManager {
    private static RoomManager instance;
    private final HashMap<String, Minigame> rooms;

    private RoomManager() throws MinigameException {
        this.rooms = new HashMap<>();
        List<Class<? extends Minigame>> minigames = GameList.getMinigames();

        try {
            for (Class<? extends Minigame> minigame : minigames) {
                Minigame game = minigame.getConstructor().newInstance();
                rooms.put(game.ID, game);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new MinigameException("Erreur lors de la création des minigames.");
        }
    }

    public static RoomManager getInstance() throws MinigameException {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public void joinRoom(String id, Player player) throws MinigameException, PartyException {
        if (this.rooms.containsKey(id)) {
            String playerName = player.getName();
            Party playerParty = PartyManager.getInstance().getPlayerParty(playerName);
            if (playerParty != null) {
                if (playerParty.getLeader().getName().equals(playerName)) {
                    this.rooms.get(id).addParty(playerParty);
                } else {
                    throw new PartyException(PartyException.PARTY_NOT_LEADER);
                }
            } else {
                this.rooms.get(id).addPlayer(playerName);
            }
        } else {
            throw new MinigameException(MinigameException.ROOM_NOT_FOUND);
        }
    }

    public void leaveRoom(String id, Player player) throws MinigameException, PartyException {
        if (this.rooms.containsKey(id)) {
            String playerName = player.getName();
            Party playerParty = PartyManager.getInstance().getPlayerParty(playerName);
            if (playerParty != null) {
                if (playerParty.getLeader().getName().equals(playerName)) {
                    this.rooms.get(id).removeParty(playerParty);
                } else {
                    throw new PartyException(PartyException.PARTY_NOT_LEADER);
                }
            } else {
                this.rooms.get(id).removePlayer(playerName);
            }
        } else {
            throw new MinigameException(MinigameException.ROOM_NOT_FOUND);
        }
    }

    public Minigame getPlayerRoom(String playerName) {
        for (Minigame room : this.rooms.values()) {
            if (room.getPlayers().contains(playerName)) {
                return room;
            }
        }
        return null;
    }

    public Minigame getPlayerRoom(Player player) {
        return getPlayerRoom(player.getName());
    }

    public Minigame getPartyRoom(Party party) {
        for (Minigame room : this.rooms.values()) {
            for (Player player : party.getMembers()) {
                if (room.getPlayers().contains(player.getName())) {
                    return room;
                }
            }
        }
        return null;
    }

    public List<String> getRoomList() {
        List<String> list = new ArrayList<>();

        for (Minigame room : this.rooms.values()) {
            list.add(room.NAME + "(" + room.ID + ")");
        }

        return list;
    }
}
