package fr.celestgames.fts.minigames;

import fr.celestgames.fts.FTSMain;
import fr.celestgames.fts.enumerations.GameType;
import fr.celestgames.fts.events.minigames.PlayerJoinMinigameEvent;
import fr.celestgames.fts.events.minigames.PlayerLeaveMinigameEvent;
import fr.celestgames.fts.events.party.PlayerLeavePartyEvent;
import fr.celestgames.fts.exceptions.MinigameException;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.bukkit.Bukkit.*;

public abstract class Minigame {
    private final int showMapTask;
    protected final GameType type;
    protected final int MIN_PLAYERS;
    protected final int MAX_PLAYERS;
    protected final ArrayList<Player> players = new ArrayList<>();
    protected final ArrayList<Player> spectators = new ArrayList<>();
    protected ArrayList<String> mapNames;
    protected boolean started;
    protected boolean randomMap;
    protected String currentMap;
    protected World world;

    public Minigame(GameType type, int minPlayers, int maxPlayers) throws MinigameException {
        if (minPlayers < 1) {
            throw new MinigameException("minPlayers doit être supérieur à 0.");
        } else if (maxPlayers < minPlayers) {
            throw new MinigameException("maxPlayers doit être supérieur à minPlayers.");
        }

        if (maxPlayers > 32) {
            throw new MinigameException("maxPlayers doit être inférieur à 16.");
        }

        this.type = type;
        this.MIN_PLAYERS = minPlayers;
        this.MAX_PLAYERS = maxPlayers;

        this.randomMap = true;

        mapNames = FTSMain.getInstance().getMapsFile().getValues(type.getName());
        if (mapNames.size() == 0) {
            throw new MinigameException("Aucune map n'a été trouvée dans le fichier de configuration");
        }

        this.currentMap = mapNames.get(new Random().nextInt(mapNames.size()));
        this.world = getWorld(currentMap);

        this.showMapTask = getScheduler().scheduleSyncRepeatingTask(FTSMain.getInstance(), () -> {
            for (Player player : players) {
                if (!started) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Map sélectionnée: §e" + currentMap));
                }
            }
            for (Player p : spectators) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Map sélectionnée: §e" + currentMap));
            }
        }, 0, 20);
    }

    public abstract void start() throws MinigameException;

    public abstract void update();

    public abstract void end();

    public void addPlayer(Player player) throws MinigameException {
        if (!players.contains(player)) {
            if (started) {
                if (players.size() + spectators.size() >= MAX_PLAYERS) {
                    throw new MinigameException("Le mini-jeu est plein.");
                } else {
                    spectators.add(player);
                }
            } else if (players.size() < MAX_PLAYERS) {
                players.add(player);
            }
        } else {
            throw new MinigameException("Le joueur " + player.getName() + " est déjà dans le mini-jeu.");
        }
        getPluginManager().callEvent(new PlayerJoinMinigameEvent(this, player));
    }

    public void removePlayer(Player player) {
        if (players.contains(player)) {
            players.remove(player);
        } else spectators.remove(player);

        getPluginManager().callEvent(new PlayerLeaveMinigameEvent(this, player));
    }

    public void addSpectator(Player player) throws MinigameException {
        if (!spectators.contains(player)) {
            if (players.size() + spectators.size() >= MAX_PLAYERS) {
                throw new MinigameException("Le mini-jeu est plein.");
            } else {
                spectators.add(player);
            }
        } else {
            throw new MinigameException("Le joueur " + player.getName() + " est déjà dans le mini-jeu.");
        }
    }

    public void removeSpectator(Player player) {
        spectators.remove(player);
    }

    public void setMapName(String currentMap) throws MinigameException {
        if (mapNames.contains(currentMap)) {
            this.currentMap = currentMap;
            this.world = getWorld(currentMap);
            this.randomMap = false;
        } else {
            throw new MinigameException("La map " + currentMap + " n'a pas été trouvée dans le fichier de configuration");
        }
    }

    public void setRandomMap() {
        this.currentMap = mapNames.get(new Random().nextInt(mapNames.size()));
        this.randomMap = true;
    }

    public String getMapName() {
        return currentMap;
    }

    public GameType getType() {
        return type;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public int getMinPlayers() {
        return MIN_PLAYERS;
    }

    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    public boolean isEmpty() {
        return players.size() == 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isRandomMap() {
        return randomMap;
    }

    public List<String> getMaps() {
        return mapNames;
    }

    public Location getMinigameLobby() {
        World lobby = getWorld("lobby");
        if (lobby != null) {
            for (ArmorStand stand : lobby.getEntitiesByClass(ArmorStand.class)) {
                if (stand.getCustomName() != null && stand.getCustomName().equals(getType().getAlias() + "_lobby")) {
                    return stand.getLocation();
                }
            }
            return lobby.getSpawnLocation();
        } else {
            return null;
        }
    }
}
