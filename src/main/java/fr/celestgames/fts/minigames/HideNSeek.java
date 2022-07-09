package fr.celestgames.fts.minigames;

import fr.celestgames.fts.FTSMain;
import fr.celestgames.fts.enumerations.GameType;
import fr.celestgames.fts.exceptions.MinigameException;
import fr.celestgames.fts.listeners.HnSListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Team;

import java.util.Random;

import static fr.celestgames.fts.utIls.FileUtil.writeFile;
import static fr.celestgames.fts.utIls.TimerFormater.formatTime;
import static org.bukkit.Bukkit.*;

public class HideNSeek extends Minigame {
    private HnSListener listener;
    private final int defaultHideTime;
    private final int defaultSeekTime;

    private int hideTime;
    private int seekTime;

    private int task;
    private Player seeker;

    public HideNSeek() throws MinigameException {
        super(GameType.HIDE_N_SEEK, 4, 16);
        this.defaultHideTime = 60;
        this.defaultSeekTime = 600;
        this.hideTime = defaultHideTime;
        this.seekTime = defaultSeekTime;
    }

    @Override
    public void start() throws MinigameException {
        ArmorStand seekerSpawn = null;

        if (world == null) {
            throw new MinigameException("La map n'a pas été trouvée");
        } else {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof ArmorStand) {
                    if (entity.getName().equals("seeker_spawn")) {
                        seekerSpawn = (ArmorStand) entity;
                    }
                }
            }

            if (seekerSpawn == null) {
                throw new MinigameException("Aucun spawn pour le seeker n'a été trouvé");
            }
        }

        listener = new HnSListener();
        createHidersTeam();
        createSeekersTeam();

        this.seekTime = defaultSeekTime;
        this.hideTime = defaultHideTime;

        getServer().getPluginManager().registerEvents(listener, FTSMain.getInstance());
        seeker = players.get(new Random().nextInt(players.size()));
        seeker.getScoreboard().getTeam("seekers").addEntry(seeker.getName());
        seeker.teleport(seekerSpawn.getLocation());

        for (Player player : players) {
            player.setGameMode(GameMode.ADVENTURE);
            if (player != seeker) {
                player.getScoreboard().getTeam("hiders").addEntry(player.getName());
                player.teleport(world.getSpawnLocation());
            }
        }
        for (Player player : spectators) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(world.getSpawnLocation());
        }
        started = true;
        update();
    }

    @Override
    public void update() {
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(FTSMain.getInstance(), () -> {
            String hidersMessage;
            String seekersMessage;
            if (hideTime > 0) {
                hideTime--;

                if (hideTime < 15) {
                    hidersMessage = "§c§lAttention !§r il ne vous reste plus que " + formatTime(hideTime) + " pour vous cacher !";
                    seekersMessage = "Vous allez passer à l'action dans " + formatTime(hideTime) + " !";
                } else {
                    hidersMessage = "Vous avez " + formatTime(hideTime) + " pour vous cacher !";
                    seekersMessage = "Il ne reste plus que " + formatTime(hideTime) + " aux §3§lHiders§r pour se cacher !";
                }

                for (Player player : players) {
                    if (player.getScoreboard().getTeam("hiders").getEntries().contains(player.getName())) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(hidersMessage));
                    } else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(seekersMessage));
                    }
                }

                if (hideTime == 0) {
                    seeker.teleport(world.getSpawnLocation());
                }
            } else if (seekTime > 0) {
                seekTime--;

                if (seekTime < 60) {
                    seekersMessage = "§c§lAttention !§r il ne vous reste plus que " + formatTime(seekTime) + " pour trouver les §3§lHiders§r !";
                    hidersMessage = "Il ne reste plus que " + formatTime(seekTime) + ". Tenez bon !";
                } else {
                    hidersMessage = "Il reste " + formatTime(seekTime) + ". Restez bien caché !";
                    seekersMessage = "Il vous reste " + formatTime(seekTime) + " pour trouver les §3§lHiders§r !";
                }

                for (Player player : players) {
                    if (player.getScoreboard().getTeam("hiders").getEntries().contains(player.getName())) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(hidersMessage));
                    } else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(seekersMessage));
                    }
                }

                if (seekTime == 0 || getScoreboardManager().getMainScoreboard().getTeam("hiders").getEntries().size() == 0) {
                    end();
                }
            }
        }, 0, 20);
    }

    @Override
    public void end() {
        getScheduler().cancelTask(task);
        HandlerList.unregisterAll(listener);
        World lobby = getServer().getWorld("lobby");
        started = false;

        for (Player player : super.players) {
            if (lobby != null) {
                Location lobbySpawn = getMinigameLobby();
                if (lobbySpawn != null) {
                    player.teleport(lobbySpawn);
                } else {
                    player.teleport(lobby.getSpawnLocation());
                }
                player.getScoreboard().getTeam("hiders").removeEntry(player.getName());
                player.getScoreboard().getTeam("seekers").removeEntry(player.getName());
                if (seekTime == 0) {
                    player.sendMessage("Les §b§lHiders§r ont gagné !");
                } else {
                    player.sendMessage("Les §c§lSeekers§r ont gagné !");
                }
            } else {
                player.teleport(getServer().getWorlds().get(0).getSpawnLocation());
            }
        }
    }

    public int getHideTime() {
        return hideTime;
    }

    public int getSeekTime() {
        return seekTime;
    }

    public void setHideTime(int hideTime) {
        if (hideTime < 0) {
            this.hideTime = (hideTime * -1);
        } else {
            this.hideTime = hideTime;
        }
    }

    public void setSeekTime(int seekTime) {
        if (seekTime < 0) {
            this.seekTime = (seekTime * -1);
        } else {
            this.seekTime = seekTime;
        }
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    private void createHidersTeam() {
        if (getScoreboardManager() != null) {
            Team hiders = getScoreboardManager().getMainScoreboard().getTeam("hiders");
            if (hiders != null) {
                return;
            }
            hiders = getScoreboardManager().getMainScoreboard().registerNewTeam("hiders");
            hiders.setPrefix("§b[Hider] ");
            hiders.setSuffix("");
            hiders.setAllowFriendlyFire(false);
            hiders.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            hiders.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.NEVER);
        }
    }

    private void createSeekersTeam() {
        if (getScoreboardManager() != null) {
            Team seekers = getScoreboardManager().getMainScoreboard().getTeam("seekers");
            if (seekers != null) {
                return;
            }
            seekers = getScoreboardManager().getMainScoreboard().registerNewTeam("seekers");
            seekers.setPrefix("§c[Seeker] ");
            seekers.setSuffix("");
            seekers.setAllowFriendlyFire(false);
            seekers.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            seekers.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.NEVER);
        }
    }
}