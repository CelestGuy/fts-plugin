package fr.celestgames.fts.minigames;

import fr.celestgames.fts.FTSPlugin;
import fr.celestgames.fts.exceptions.MinigameException;
import fr.celestgames.fts.listeners.HnSListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static fr.celestgames.fts.utIls.TimerFormater.formatTime;
import static org.bukkit.Bukkit.*;

public class HideNSeek extends Minigame {
    private final int DEFAULT_HIDE_TIME = 60;
    private final int DEFAULT_SEEK_TIME = 600;

    private Location seekerSpawn;
    private HnSListener listener;

    private int hideTime;
    private int seekTime;

    private HashSet<String> hiders;
    private HashSet<String> seekers;
    private String firstSeeker;

    public HideNSeek() throws MinigameException {
        super("Hide & Seek", "hns", 4, 16);
        hideTime = DEFAULT_HIDE_TIME;
        seekTime = DEFAULT_SEEK_TIME;
        seekers = new HashSet<>();
        hiders = new HashSet<>();
    }

    public void setCurrentMap(String map) throws MinigameException {
        if (!mapIDs.contains(map)) {
            throw new MinigameException(MinigameException.MAP_NOT_FOUND);
        } else {
            World world = getWorld(map);
            if (world == null) {
                throw new MinigameException(MinigameException.MAP_NOT_FOUND);
            } else {
                ArmorStand ass = world.getEntitiesByClass(ArmorStand.class).stream().filter(armorStand -> armorStand.getName().equals("seeker_spawn")).findFirst().orElse(null);

                if (ass == null) {
                    throw new MinigameException(MinigameException.NO_SPAWN_FOUND);
                } else {
                    seekerSpawn = ass.getLocation();
                }

                currentMapID = map;
                currentWorld = getWorld(map);
                randomMap = false;
            }
        }
    }

    public void addSeeker(String seeker) {
        seekers.add(seeker);
    }

    public void removeHider(String hider) {
        hiders.remove(hider);
    }

    public boolean isSeeker(String player) {
        return seekers.contains(player);
    }

    public boolean isHider(String player) {
        return hiders.contains(player);
    }

    @Override
    protected void init() throws MinigameException {
        if (seekerSpawn == null) {
            throw new MinigameException(MinigameException.NO_SPAWN_FOUND);
        } else {
            if (super.randomMap) {
                String tempMap = mapIDs.toArray()[new Random().nextInt(mapIDs.size())].toString();
                World tempWorld = getWorld(tempMap);
                if (tempWorld == null) {
                    throw new MinigameException(MinigameException.MAP_NOT_FOUND);
                } else {
                    currentMapID = tempMap;
                    currentWorld = tempWorld;
                }
            }

            int random = new Random().nextInt(super.players.size());
            Location seekerSpawnLocation = seekerSpawn;
            Location hiderSpawnLocation = currentWorld.getSpawnLocation();

            for (String playerName : super.players) {
                Player player = FTSPlugin.getInstance().getServer().getPlayer(playerName);
                if (player == null) {
                    throw new MinigameException(MinigameException.PLAYER_NOT_FOUND);
                } else {
                    player.setGameMode(GameMode.ADVENTURE);
                    if (random == 0 && firstSeeker == null) {
                        firstSeeker = playerName;
                        seekers.add(playerName);
                        player.teleport(seekerSpawnLocation);
                    } else if (firstSeeker != null && firstSeeker.equals(playerName)) {
                        seekers.add(playerName);
                        player.teleport(seekerSpawnLocation);
                    } else {
                        hiders.add(playerName);
                        player.teleport(hiderSpawnLocation);
                    }
                    random--;
                }
            }

            listener = new HnSListener(this);
        }
    }

    @Override
    protected boolean loop() {
        String hidersMessage;
        String seekersMessage;
        if (hideTime > 0) {
            if (hideTime < 15) {
                hidersMessage = "§c§lAttention !§r il ne vous reste plus que " + formatTime(hideTime) + " pour vous cacher !";
                seekersMessage = "Vous allez passer à l'action dans " + formatTime(hideTime) + " !";
            } else {
                hidersMessage = "Vous avez " + formatTime(hideTime) + " pour vous cacher !";
                seekersMessage = "Il ne reste plus que " + formatTime(hideTime) + " aux §3§lHiders§r pour se cacher !";
            }

            for (String playerName : players) {
                Player player = getPlayer(playerName);
                if (player != null) {
                    if (hiders.contains(playerName)) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(hidersMessage));
                    } else {
                        if (hideTime == 0) {
                            player.teleport(currentWorld.getSpawnLocation());
                        }
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(seekersMessage));
                    }
                }
            }

            hideTime--;
            if (hideTime == 0) {
                for (String playerName : seekers) {
                    Player player = getPlayer(playerName);
                    if (player != null) {
                        player.teleport(currentWorld.getSpawnLocation());
                    }
                }
            }
        } else if (seekTime > 0) {
            if (seekTime < 60) {
                seekersMessage = "§c§lAttention !§r il ne vous reste plus que " + formatTime(seekTime) + " pour trouver les §3§lHiders§r !";
                hidersMessage = "Il ne reste plus que " + formatTime(seekTime) + ". Tenez bon !";
            } else {
                hidersMessage = "Il reste " + formatTime(seekTime) + ". Restez bien caché !";
                seekersMessage = "Il vous reste " + formatTime(seekTime) + " pour trouver les §3§lHiders§r !";
            }

            for (String playerName : players) {
                Player player = getPlayer(playerName);
                if (player != null) {
                    if (hiders.contains(playerName)) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(hidersMessage));
                    } else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(seekersMessage));
                    }
                }
            }

            seekTime--;

            return hiders.isEmpty() || seekTime == 0;
        }

        return false;
    }

    @Override
    public void unset() {
        if (seekTime > 0 && hiders.isEmpty()) {
            for (String playerName : seekers) {
                Player player = getPlayer(playerName);
                if (player != null) {
                    player.sendMessage("L'équipe des §cSeekers§r de §3§l" + firstSeeker + "§r a gagné !");
                }
            }
        } else {
            for (String playerName : seekers) {
                Player player = getPlayer(playerName);
                if (player != null) {
                    player.sendMessage("L'équipe des §3Hiders§r a gagné !");
                }
            }
        }

        hiders = new HashSet<>();
        seekers = new HashSet<>();
        listener.destroy();
        hideTime = DEFAULT_HIDE_TIME;
        seekTime = DEFAULT_SEEK_TIME;
        firstSeeker = null;

        for (String playerName : players) {
            Player player = getPlayer(playerName);
            if (player != null) {
                player.teleport(lobbySpawn);
            }
        }
    }

    @Override
    public List<String> getConfigArgs(String[] args) {
        if (args.length > 1) {
            return List.of("hideTime", "seekTime", "seeker", "seekerSpawn");
        }
        return List.of();
    }

    @Override
    public boolean configCommand(Player sender, String[] args) {
        if (!isStarted()) {
            if (args != null && args.length > 2) {
                switch (args[1]) {
                    case "hideTime" -> {
                        int time = Integer.parseInt(args[2]);
                        if (time > 15) {
                            hideTime = time;
                        } else {
                            hideTime = 15;
                            sender.sendMessage("§cLa durée doit être supérieure à 15 secondes.");
                        }
                    }
                    case "seekTime" -> {
                        int time = Integer.parseInt(args[2]);
                        if (time > 60) {
                            seekTime = time;
                        } else {
                            seekTime = 60;
                            sender.sendMessage("§cLa durée doit être supérieure à 60 secondes.");
                        }
                    }
                    case "seeker" -> {
                        String playerName = args[2];
                        if (getPlayer(playerName) != null) {
                            firstSeeker = playerName;
                            sender.sendMessage("§aLe joueur §e" + playerName + "§a sera le prochain §3§lSeeker§a !");
                        } else {
                            sender.sendMessage("§cLe joueur §e" + playerName + "§c n'existe pas !");
                        }
                    }
                    case "seekerSpawn" -> {

                    }
                }
            } else {
                sender.sendMessage("§c/game config <hideTime | seekTime | seeker> <valeur>");
            }
        } else {
            sender.sendMessage("§cVous ne pouvez pas changer les paramètres d'une partie en cours.");
        }
        return true;
    }
}
