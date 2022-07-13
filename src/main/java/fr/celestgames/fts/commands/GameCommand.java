package fr.celestgames.fts.commands;

import fr.celestgames.fts.exceptions.MinigameException;
import fr.celestgames.fts.exceptions.PartyException;
import fr.celestgames.fts.minigames.Minigame;
import fr.celestgames.fts.server.RoomManager;
import fr.celestgames.fts.server.PartyManager;
import fr.celestgames.fts.server.party.Party;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameCommand extends PluginCommand {
    private Minigame minigame;
    private Player player;
    private String[] args;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player p) {
            this.player = p;
            try {
                this.minigame = RoomManager.getInstance().getPlayerRoom(player);
            } catch (MinigameException e) {
                player.sendMessage("§c" + e.getMessage());
            }
            this.args = args;

            if (args.length > 0 && minigame != null) {
                switch (args[0]) {
                    case "join" -> joinRoom();
                    case "start" -> startGame();
                    case "stop" -> stopGame();
                    case "moderator" -> moderator();
                    case "list" -> listPlayers();
                    case "config" -> config();
                    case "leave" -> {
                        if (args.length == 1) {
                            try {
                                minigame.removePlayer(player.getName());
                            } catch (MinigameException e) {
                                player.sendMessage("§c" + e.getMessage());
                            }
                        } else {
                            player.sendMessage("/game leave");
                        }
                    }
                    case "map" -> {
                        if (args.length == 1) {
                            player.sendMessage("Vous êtes dans le mini-jeu " + minigame.NAME + ". \nsur la map : " + minigame.getCurrentMap());
                        } else if (args.length == 2) {
                            if (player.isOp() || minigame.getModerators().contains(player.getName())) {
                                try {
                                    if (args[1].equals("random")) {
                                        minigame.setRandomMap();
                                    } else {
                                        minigame.setCurrentMap(args[1]);
                                    }
                                } catch (MinigameException e) {
                                    player.sendMessage("§c" + e.getMessage());
                                }
                            }
                        } else {
                            player.sendMessage("/game map <map|random>");
                        }
                    }
                }
            } else if (args.length > 0) {
                switch (args[0]) {
                    case "join" -> joinRoom();
                    case "list" -> listRooms();
                    default -> player.sendMessage("§cVous n'êtes pas dans un mini-jeu.");
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender player, Command command, String label, String[] args) {
        if (player instanceof Player p) {
            try {
                minigame = RoomManager.getInstance().getPlayerRoom(p);
            } catch (MinigameException e) {
                player.sendMessage("§c" + e.getMessage());
            }
            Party party = PartyManager.getInstance().getPlayerParty(player.getName());

            if (minigame == null) {
                if (args.length == 1) {
                    return List.of("join", "list");
                } else if (args.length == 2) {
                    if (args[0].equals("join")) {
                        ArrayList<String> minigames = new ArrayList<>();
                        if (party == null || party.getLeader().equals(player)) {
                            try {
                                minigames.addAll(RoomManager.getInstance().getRoomList());
                            } catch (MinigameException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            player.sendMessage("§cVous n'êtes pas le leader de votre party.");
                        }
                        return minigames;
                    }
                }
            } else {
                if (args.length == 1) {
                    if (player.isOp() || (minigame.getModerators().contains(player.getName()) && !minigame.isStarted())) {
                        return List.of("start", "moderator", "leave", "map", "list", "config", "spawn");
                    } else if (player.isOp() || (minigame.getModerators().contains(player.getName()) && minigame.isStarted())) {
                        return List.of("stop", "moderator", "leave", "list", "config");
                    } else {
                        return List.of("leave", "list");
                    }
                } else if (args.length == 2) {
                    if (player.isOp() || minigame.getModerators().contains(player.getName())) {
                        if (args[0].equals("map")) {
                            List<String> maps = new ArrayList<>(minigame.getMapIDs());
                            maps.add("random");
                            return maps;
                        }
                        if (args[0].equals("moderator")) {
                            return List.of("add", "remove");
                        }
                        if (args[0].equals("config")) {
                            return minigame.getConfigArgs(args);
                        }
                    }
                } else if (args.length == 3) {
                    if (player.isOp() || minigame.getModerators().contains(player.getName())) {
                        if (args[0].equals("moderator")) {
                            if (args[1].equals("add")) {
                                ArrayList<String> players = new ArrayList<>();
                                for (String p2 : minigame.getPlayers()) {
                                    if (!minigame.getModerators().contains(p2)) {
                                        players.add(p2);
                                    }
                                }
                                return players;
                            } else if (args[1].equals("remove")) {
                                ArrayList<String> players = new ArrayList<>();
                                for (String p2 : minigame.getPlayers()) {
                                    if (minigame.getModerators().contains(p2)) {
                                        players.add(p2);
                                    }
                                }
                                return players;
                            }
                        }
                    }
                }
            }
        }
        return List.of();
    }

    private void joinRoom() {
        if (args.length > 1) {
            try {
                RoomManager.getInstance().joinRoom(args[1], player);
            } catch (MinigameException | PartyException e) {
                player.sendMessage("§c" + e.getMessage());
            }
        } else {
            player.sendMessage("/game join <type>");
        }
    }

    private void startGame() {
        if (player.isOp() || minigame.getModerators().contains(player.getName())) {
            if (args.length == 1) {
                try {
                    minigame.start();
                } catch (MinigameException e) {
                    player.sendMessage("§c" + e.getMessage());
                }
            } else {
                player.sendMessage("/game start");
            }
        } else {
            player.sendMessage("§cVous n'avez pas les permissions pour cette commande.");
        }
    }

    private void stopGame() {
        if (player.isOp() || minigame.getModerators().contains(player.getName())) {
            if (args.length == 1) {
                minigame.stop();
            } else {
                player.sendMessage("/game stop");
            }
        } else {
            player.sendMessage("§cVous n'avez pas les permissions pour cette commande.");
        }
    }

    private void moderator() {
        if (player.isOp() || minigame.getModerators().contains(player.getName())) {
            if (args.length == 3) {
                try {
                    if (args[1].equals("add")) {
                        minigame.addModerator(args[2]);
                    } else if (args[1].equals("remove")) {
                        minigame.removeModerator(args[2]);
                    }
                } catch (MinigameException e) {
                    player.sendMessage("§c" + e.getMessage());
                }
            } else {
                player.sendMessage("/game moderator <add|remove> <player>");
            }
        } else {
            player.sendMessage("§cVous n'avez pas les permissions pour cette commande.");
        }
    }

    public void listPlayers() {
        if (args.length == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Joueurs dans le salon de ").append(minigame.NAME).append(" :\n");
            for (String member : minigame.getPlayers()) {
                sb.append("   - §b").append(member);
                if (minigame.getModerators().contains(member)) {
                    sb.append(" §c(Modérateur)");
                }
                sb.append("§r,\n");
            }
            sb.append("\n").append("§c").append(minigame.getPlayers().size()).append("/").append(minigame.MAX_PLAYERS).append("§r joueurs");
            player.sendMessage(sb.toString());
        } else {
            player.sendMessage("/game list");
        }
    }

    public void listRooms() {

    }

    public void config() {
        if (player.isOp() || minigame.getModerators().contains(player.getName())) {
            minigame.configCommand(player, args);
        }
    }
}
