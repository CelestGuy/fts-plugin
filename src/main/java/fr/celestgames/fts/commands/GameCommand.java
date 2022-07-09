package fr.celestgames.fts.commands;

import fr.celestgames.fts.enumerations.GameType;
import fr.celestgames.fts.exceptions.MinigameException;
import fr.celestgames.fts.minigames.Minigame;
import fr.celestgames.fts.server.GameManager;
import fr.celestgames.fts.server.PartyManager;
import fr.celestgames.fts.server.party.Party;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameCommand extends PluginCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player player) {
            Party party = PartyManager.getInstance().getPlayerParty(player.getName());
            Minigame minigame = GameManager.getInstance().getPlayerMinigame(player);

            if (args.length > 0) {
                switch (args[0]) {
                    case "join" -> {
                        joinGame(args, player, party, minigame);
                    }
                    case "start" -> {
                        if (player.isOp()) {
                            if (args.length == 1) {
                                try {
                                    GameManager.getInstance().getPlayerMinigame(player).start();
                                } catch (MinigameException e) {
                                    player.sendMessage("§cAttention : " + e.getMessage());
                                }
                            } else {
                                commandSender.sendMessage("/game start");
                            }
                        } else {
                            commandSender.sendMessage("§cVous n'avez pas les permissions pour cette commande.");
                        }
                    }
                    case "list" -> {
                        if (args.length == 1) {
                            if (minigame != null) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Joueurs dans le ").append(minigame.getType().getName()).append(" :\n");
                                for (Player member : minigame.getPlayers()) {
                                    sb.append("   - §b").append(member.getName());
                                    sb.append("§r,\n");
                                }
                                sb.append("\n").append("§c").append(minigame.getPlayers().size()).append("/").append(minigame.getMaxPlayers()).append("§r joueurs");
                                player.sendMessage(sb.toString());
                            } else {
                                if (player.isOp()) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Liste des salons de mini-jeux :\n");
                                    for (String s : GameManager.getInstance().getGameList()) {
                                        sb.append("   - §3").append(s);
                                    }
                                    player.sendMessage(sb.toString());
                                } else {
                                    player.sendMessage("§cVous n'êtes pas dans un mini-jeu.");
                                }
                            }
                        } else {
                            commandSender.sendMessage("/game list");
                        }
                    }
                    case "leave" -> {
                        if (args.length == 1) {
                            if (minigame != null) {
                                minigame.removePlayer(player);
                            } else {
                                player.sendMessage("§cVous n'êtes pas dans un mini-jeu.");
                            }
                        } else {
                            commandSender.sendMessage("/game leave <name>");
                        }
                    }
                    case "map" -> {
                        if (args.length == 1) {
                            if (minigame != null) {
                                player.sendMessage("§cVous êtes dans le mini-jeu " + minigame.getType().getName());
                                player.sendMessage("§cVous êtes sur la map " + minigame.getMapName());
                            } else {
                                player.sendMessage("§cVous n'êtes pas dans un mini-jeu.");
                            }
                        } else if (args.length == 2) {
                            if (minigame != null) {
                                try {
                                    if (args[1].equals("random")) {
                                        minigame.setRandomMap();
                                    } else {
                                        minigame.setMapName(args[1]);
                                        player.sendMessage("Vous avez changé la map du mini-jeu " + minigame.getType() + " à " + args[1]);
                                    }
                                } catch (MinigameException e) {
                                    player.sendMessage("§c" + e.getMessage());
                                }
                            } else {
                                player.sendMessage("§cVous n'êtes pas dans un mini-jeu.");
                            }
                        } else {
                            commandSender.sendMessage("/game map");
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player player) {
            Minigame minigame = GameManager.getInstance().getPlayerMinigame(player);
            Party party = PartyManager.getInstance().getPlayerParty(player.getName());

            if (minigame == null) {
                if (args.length == 1) {
                    return List.of("join", "list");
                } else if (args.length == 2) {
                    if ("join".equals(args[0])) {
                        ArrayList<String> minigames = new ArrayList<>();

                        for (GameType gameType : GameType.values()) {
                            minigames.add(gameType.getName());
                            minigames.add(gameType.getAlias());
                        }

                        if (player.isOp()) {
                            minigames.addAll(PartyManager.getInstance().getPrivateParties());
                        }
                        return minigames;
                    }
                }
            } else {
                if (args.length == 1) {
                    if (player.isOp() || (party != null && party.getLeader().equals(player))) {
                        return List.of("start", "leave", "map", "list");
                    } else {
                        return List.of("leave", "list");
                    }
                } else if (args.length == 2) {
                    if (player.isOp() || (party != null && party.getLeader().equals(player))) {
                        if ("map".equals(args[0])) {
                            List<String> maps = minigame.getMaps();
                            maps.add("random");
                            return maps;
                        }
                    }
                }
            }
        }
        return List.of();
    }

    public void joinGame(String[] args, Player player, Party party, Minigame game) {
        if (args.length > 1 && game == null) {
            try {
                if (GameType.getGame(args[1]) != null) {
                    if (party != null) {
                        GameManager.getInstance().joinGame(GameType.getGame(args[1]), party);
                    } else {
                        GameManager.getInstance().joinGame(GameType.getGame(args[1]), player);
                    }
                } else {
                    player.sendMessage("§cCe mini-jeu n'existe pas.");
                }
            } catch (MinigameException e) {
                player.sendMessage("§c" + e.getMessage());
            }
        } else if (game != null) {
            player.sendMessage("§cVous êtes déjà dans un mini-jeu.");
        } else {
            player.sendMessage("/game join <type>");
        }
    }
}
