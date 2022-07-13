package fr.celestgames.fts.minigames;

import fr.celestgames.fts.FTSPlugin;
import fr.celestgames.fts.events.minigames.MapChangedEvent;
import fr.celestgames.fts.events.minigames.PlayerJoinMinigameEvent;
import fr.celestgames.fts.events.minigames.PlayerLeaveMinigameEvent;
import fr.celestgames.fts.events.party.PlayerJoinPartyEvent;
import fr.celestgames.fts.exceptions.MinigameException;
import fr.celestgames.fts.server.party.Party;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.bukkit.Bukkit.*;

public abstract class Minigame {
    public final String NAME;
    public final String ID;
    public final int MIN_PLAYERS;
    public final int MAX_PLAYERS;
    protected final Location lobbySpawn;
    protected boolean started;
    protected boolean randomMap;
    protected int startTask;
    protected int loopTask;
    protected HashSet<String> players;
    protected HashSet<String> spectators;
    protected HashSet<String> moderators;
    protected HashSet<String> mapIDs;
    protected String currentMapID;
    protected World currentWorld;

    public Minigame(String name, String id, int minPlayers, int maxPlayers) throws MinigameException {
        this.NAME = name;
        this.ID = id;
        this.MIN_PLAYERS = minPlayers;
        this.MAX_PLAYERS = maxPlayers;
        this.players = new HashSet<>();
        this.spectators = new HashSet<>();
        this.moderators = new HashSet<>();
        this.mapIDs = new HashSet<>();
        this.started = false;
        this.randomMap = true;

        this.mapIDs = FTSPlugin.getInstance().getMapsFile().getValues(id);
        if (mapIDs == null || mapIDs.size() == 0) {
            throw new MinigameException(MinigameException.MAP_NOT_FOUND);
        } else {
            this.currentMapID = mapIDs.iterator().next();
            this.currentWorld = getWorld(currentMapID);

            if (this.currentWorld == null) {
                throw new MinigameException(MinigameException.MAP_NOT_FOUND);
            }
        }

        World lobbyWorld = getWorld("lobby");
        if (lobbyWorld == null) {
            throw new MinigameException(MinigameException.LOBBY_NOT_FOUND);
        } else {
            ArmorStand ass = lobbyWorld.getEntitiesByClass(ArmorStand.class)
                    .stream().filter(armorStand -> armorStand.getName().equals("seeker_spawn")).findFirst().orElse(null);

            if (ass == null) {
                lobbySpawn = lobbyWorld.getSpawnLocation();
            } else {
                lobbySpawn = ass.getLocation();
            }
        }

        setRandomMap();
    }

    public boolean isStarted() {
        return this.started;
    }

    public HashSet<String> getPlayers() {
        return players;
    }

    public HashSet<String> getSpectators() {
        return spectators;
    }

    public HashSet<String> getModerators() {
        return moderators;
    }

    public boolean isFull() {
        return (players.size() + spectators.size()) >= MAX_PLAYERS;
    }

    public boolean isEmpty() {
        return players.size() == 0 && spectators.size() == 0;
    }

    public HashSet<String> getMapIDs() {
        return mapIDs;
    }

    public boolean isRandomMap() {
        return this.randomMap;
    }

    public String getCurrentMap() {
        return currentMapID;
    }

    public boolean hasPlayer(String player) {
        return players.contains(player) || spectators.contains(player);
    }

    public void setCurrentMap(String map) throws MinigameException {
        if (!mapIDs.contains(map)) {
            throw new MinigameException(MinigameException.MAP_NOT_FOUND);
        } else {
            if (getWorld(map) == null) {
                throw new MinigameException(MinigameException.MAP_NOT_FOUND);
            } else {
                this.currentMapID = map;
                this.currentWorld = getWorld(map);
                this.randomMap = false;
            }
        }

        getPluginManager().callEvent(new MapChangedEvent(this, map));
    }

    public void setRandomMap() throws MinigameException {
        int random = new Random().nextInt(mapIDs.size());

        for (String map : mapIDs) {
            if (random == 0) {
                this.randomMap = true;
                setCurrentMap(map);
                break;
            }
            random--;
        }
    }

    public void addPlayer(String playerName) throws MinigameException {
        Player player = getPlayer(playerName);
        if (player == null) {
            throw new MinigameException(MinigameException.PLAYER_NOT_FOUND);
        } else {
            if (players.size() >= MAX_PLAYERS) {
                this.spectators = new HashSet<>();
                throw new MinigameException(MinigameException.ROOM_FULL);
            } else if (players.contains(playerName) || spectators.contains(playerName)) {
                throw new MinigameException(MinigameException.PLAYER_ALREADY_IN_ROOM);
            } else if (!started) {
                players.add(playerName);
            } else {
                spectators.add(playerName);
            }

            if (player.isOp()) {
                moderators.add(playerName);
            }

            getPluginManager().callEvent(new PlayerJoinMinigameEvent(this, player));
        }
    }

    public void removePlayer(String playerName) throws MinigameException {
        Player player = getPlayer(playerName);
        if (player == null) {
            throw new MinigameException(MinigameException.PLAYER_NOT_FOUND);
        } else {
            if (!players.contains(playerName) && !spectators.contains(playerName)) {
                throw new MinigameException(MinigameException.PLAYER_NOT_IN_ROOM);
            } else if (players.contains(playerName)) {
                players.remove(playerName);
            } else {
                spectators.remove(playerName);
            }
            moderators.remove(playerName);
        }

        getPluginManager().callEvent(new PlayerLeaveMinigameEvent(this, player));
    }

    public void addParty(Party party) throws MinigameException {
        if ((players.size() + spectators.size() + party.getMembers().size()) >= MAX_PLAYERS) {
            throw new MinigameException(MinigameException.ROOM_FULL_FOR_PARTY);
        } else {
            for (Player player : party.getMembers()) {
                addPlayer(player.getName());
            }
        }
    }

    public void removeParty(Party party) throws MinigameException {
        for (Player player : party.getMembers()) {
            removePlayer(player.getName());
        }
    }

    public void addModerator(String playerName) throws MinigameException {
        if (getServer().getPlayer(playerName) == null) {
            throw new MinigameException(MinigameException.PLAYER_NOT_FOUND);
        } else {
            if (!players.contains(playerName) && !spectators.contains(playerName)) {
                throw new MinigameException(MinigameException.PLAYER_NOT_IN_ROOM);
            } else if (moderators.contains(playerName)) {
                throw new MinigameException(MinigameException.PLAYER_ALREADY_MOD);
            } else {
                moderators.add(playerName);
            }
        }
    }

    public void removeModerator(String playerName) throws MinigameException {
        if (getServer().getPlayer(playerName) == null) {
            throw new MinigameException(MinigameException.PLAYER_NOT_FOUND);
        } else {
            if (!moderators.contains(playerName)) {
                throw new MinigameException(MinigameException.PLAYER_NOT_MOD);
            } else {
                moderators.remove(playerName);
            }
        }
    }

    public void start() throws MinigameException {
        init();
        this.started = true;
        this.loopTask = getScheduler().scheduleSyncRepeatingTask(FTSPlugin.getInstance(), () -> {
            if (loop()) {
                stop();
            }
        }, 0L, 20L);
    }

    public void stop() {
        this.started = false;
        getScheduler().cancelTask(loopTask);
        unset();
    }

    protected abstract boolean loop();

    protected abstract void init() throws MinigameException;

    protected abstract void unset();

    public abstract List<String> getConfigArgs(String[] args);

    public abstract boolean configCommand(Player sender, String[] args);
}