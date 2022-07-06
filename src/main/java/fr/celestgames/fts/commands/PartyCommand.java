package fr.celestgames.fts.commands;

import fr.celestgames.fts.FTSMain;
import fr.celestgames.fts.server.Party;
import fr.celestgames.fts.enumerations.PartyType;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PartyCommand extends PluginCommand {
    public PartyCommand(FTSMain plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            switch (args[0]) {
                case "create" -> createParty(player, args);
                case "leave" -> leaveParty(player);
                case "join" -> joinParty(player, args);
                case "list" -> listParties(player);
                case "invite" -> invitePlayer(player, args);
                case "accept" -> acceptInvitation(player, args);
                case "deny" -> denyInvitation(player, args);
                case "kick" -> kickPlayer(player, args);
                case "visibility" -> setPartyVisibility(player, args);
                default -> {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player player) {
            Party party = plugin.getPartyManager().getPlayerParty(player.getName());

            if (party == null) {
                if (args.length == 1) {
                    return List.of("create", "join", "list", "accept", "deny");
                } else if (args.length == 2) {
                    switch (args[0]) {
                        case "create" -> {
                            return List.of("nom");
                        }
                        case "join" -> {
                            ArrayList<String> parties = new ArrayList<>();
                            parties.addAll(plugin.getPartyManager().getPublicParties());
                            parties.addAll(plugin.getPartyManager().getPartyRequests(player));
                            if (player.isOp()) {
                                parties.addAll(plugin.getPartyManager().getPrivateParties());
                            }
                            return parties;
                        }
                        case "invite" -> {
                            return null;
                        }
                        case "accept", "deny" -> {
                            if (plugin.getPartyManager().getPartyRequests(player) != null) {
                                return plugin.getPartyManager().getPartyRequests(player);
                            } else {
                                return List.of();
                            }
                        }
                    }
                }
            } else {
                if (args.length == 1) {
                    if (party.getLeader().equals(player)) {
                        return List.of("invite", "kick", "leave", "list", "visibility");
                    } else {
                        return List.of("leave", "list");
                    }
                } else if (args.length == 2) {
                    if (party.getLeader().equals(player)) {
                        switch (args[0]) {
                            case "visibility" -> {
                                return List.of("public", "private", "friends");
                            }
                            case "kick" -> {
                                List<String> players = new ArrayList<>();
                                for (Player p : plugin.getPartyManager().getPlayerParty(player.getName()).getMembers()) {
                                    if (!p.equals(player)) {
                                        players.add(p.getName());
                                    }
                                }
                                return players;
                            }
                            case "invite" -> {
                                return null;
                            }
                        }
                    }
                }
            }
        }
        return List.of();
    }

    private void listParties(Player player) {
        StringBuilder sb = new StringBuilder();
        Party party = plugin.getPartyManager().getPlayerParty(player.getName());

        if (party == null) {
            sb.append("§6Parties publiques:§r\n");
            for (String p : plugin.getPartyManager().getPublicParties()) {
                sb.append("   - §a").append(p).append("§r\n");
            }

            if (player.isOp()) {
                sb.append("\n").append("§6Parties privées:§r\n");
                for (String p : plugin.getPartyManager().getPrivateParties()) {
                    sb.append("   - §7").append(p).append("§r\n");
                }
            }
        } else {
            sb.append("Joueurs dans la party §a§l").append(party.getName()).append("§r :\n");
            for (Player member : party.getMembers()) {
                sb.append("   - §b").append(member.getName());
                if (member == party.getLeader()) {
                    sb.append(" §r§d§l(leader)");
                }
                sb.append("§r,\n");
            }
        }
        player.sendMessage(sb.toString());
    }

    private void joinParty(Player player, String[] args) {
        Party playerParty = plugin.getPartyManager().getPlayerParty(player.getName());
        if (playerParty != null) {
            player.sendMessage("§cVous êtes déjà dans une party.");
        } else if (args.length >= 2) {
            Party party = plugin.getPartyManager().getParty(args[1]);

            if (party != null) {
                if (party.getType() == PartyType.PUBLIC || player.isOp() || plugin.getPartyManager().getPartyRequests(player).contains(args[1])) {
                    party.addMember(player);
                } else if (party.getType() == PartyType.PRIVATE) {
                    player.sendMessage("§cCette party est privée.");
                } else if (party.getType() == PartyType.FRIENDS) {
                    player.sendMessage("§cCette party n'est qu'avec des amis ou sur invitation.");
                }
            } else {
                player.sendMessage("§cCette party n'existe pas.");
            }
        } else {
            player.sendMessage("§c/party join <name>");
        }
    }

    private void leaveParty(Player player) {
        plugin.getPartyManager().getPlayerParty(player.getName()).removeMember(player);
    }

    private void createParty(Player player, String[] args) {
        Party playerParty = plugin.getPartyManager().getPlayerParty(player.getName());
        if (playerParty != null) {
            player.sendMessage("§cVous êtes déjà dans une party.");
        } else if (args.length >= 2) {
            plugin.getPartyManager().createParty(args[1], player);
        } else {
            player.sendMessage("§c/party create <name>");
        }
    }

    private void invitePlayer(Player sender, String[] args) {
        if (args.length >= 2) {
            Party party = plugin.getPartyManager().getPlayerParty(sender.getName());
            Player invited = plugin.getServer().getPlayer(args[1]);

            if (party != null) {
                if (party.getLeader().equals(sender)) {
                    if (invited != null) {
                        try {
                            plugin.getPartyManager().addInvitation(invited, party);
                            invited.sendMessage("§b§l" + sender.getName() + "§r vous a invité à rejoindre la party §a§l" + party.getName() + "§r. Vous avez 60 secondes pour accepter.");
                            sender.sendMessage("§b§l" + invited.getName() + "§r a été invité à rejoindre la party §a§l" + party.getName() + "§r.");
                        } catch (Exception e) {
                            sender.sendMessage("§c" + e.getMessage());
                        }
                    } else {
                        sender.sendMessage("§b§l" + args[1] + "§r§c n'est pas connecté ou n'existe pas.");
                    }
                } else {
                    sender.sendMessage("§cVous n'êtes pas le leader de la party.");
                }
            } else {
                sender.sendMessage("§cVous n'êtes pas dans une party.");
            }
        } else {
            sender.sendMessage("§c/party invite <player>");
        }
    }

    private void denyInvitation(Player player, String[] args) {
        Party playerParty = plugin.getPartyManager().getPlayerParty(player.getName());
        if (playerParty != null) {
            player.sendMessage("§cVous êtes déjà dans une party.");
        } else {
            List<String> partyRequests = plugin.getPartyManager().getPartyRequests(player);

            if (partyRequests == null || partyRequests.size() == 0) {
                player.sendMessage("§cVous n'avez aucune invitation en attente.");
            } else {
                if (args.length >= 2) {
                    String partyName = args[1];
                    if (partyRequests.contains(partyName)) {
                        plugin.getPartyManager().removeInvitation(player, partyName);
                        player.sendMessage("§cVous avez refusé l'invitation pour rejoindre la party §a§l" + partyName + "§r.");
                    } else {
                        player.sendMessage("§cVous n'avez pas d'invitation pour rejoindre la party §a§l" + partyName + "§r.");
                    }
                } else {
                    String partyName = partyRequests.get(0);
                    plugin.getPartyManager().removeInvitation(player, partyName);
                    player.sendMessage("§cVous avez refusé l'invitation pour rejoindre la party §a§l" + partyName + "§r.");
                }
            }
        }
    }

    private void acceptInvitation(Player player, String[] args) {
        Party playerParty = plugin.getPartyManager().getPlayerParty(player.getName());
        if (playerParty != null) {
            player.sendMessage("§cVous êtes déjà dans une party.");
        } else {
            List<String> partyRequests = plugin.getPartyManager().getPartyRequests(player);

            if (partyRequests == null || partyRequests.size() == 0) {
                player.sendMessage("§cVous n'avez aucune invitation en attente.");
            } else {
                if (args.length >= 2) {
                    String partyName = args[1];
                    if (partyRequests.contains(partyName)) {
                        plugin.getPartyManager().removeInvitation(player, partyName);
                        plugin.getPartyManager().getParty(partyName).addMember(player);
                    } else {
                        player.sendMessage("§cVous n'avez pas d'invitation pour rejoindre la party §a§l" + partyName + "§r.");
                    }
                } else {
                    String partyName = partyRequests.get(0);
                    plugin.getPartyManager().getParty(partyName).addMember(player);
                    plugin.getPartyManager().removeInvitation(player, partyName);
                }
            }
        }
    }

    private void setPartyVisibility(Player player, String[] args) {
        if (args.length >= 2) {
            Party party = plugin.getPartyManager().getPlayerParty(player.getName());

            if (party != null) {
                PartyType type = party.getType();
                if (party.getLeader().equals(player)) {
                    if (type.toString().equals(args[1])) {
                        player.sendMessage("Votre party est déjà sur le mode §d" + type + "§r.");
                    } else {
                        switch (args[1]) {
                            case "public" -> {
                                party.setType(PartyType.PUBLIC);
                                player.sendMessage("Votre party est maintenant §apublique§r.");
                            }
                            case "private" -> {
                                party.setType(PartyType.PRIVATE);
                                player.sendMessage("Votre party est maintenant §cprivée§r.");
                            }
                            case "friends" -> {
                                party.setType(PartyType.FRIENDS);
                                player.sendMessage("Votre party n'est maintenant §avisible§r que par invitation ou pour vos amis.");
                            }
                            default -> player.sendMessage("§c/party visibility <public|private|friends>");
                        }
                    }
                } else {
                    player.sendMessage("§cVous n'êtes pas le leader de la party.");
                }
            } else {
                player.sendMessage("§cVous n'êtes pas dans une party.");
            }
        } else {
            player.sendMessage("§c/party visibility <public|private|friends>");
        }
    }

    private void kickPlayer(Player player, String[] args) {
        if (args.length >= 2) {
            Party party = plugin.getPartyManager().getPlayerParty(player.getName());

            if (party != null) {
                if (party.getLeader().equals(player)) {
                    Player kicked = plugin.getServer().getPlayer(args[1]);

                    if (kicked != null) {
                        if (party.getMembers().contains(kicked)) {
                            party.removeMember(kicked);
                            kicked.sendMessage("§cVous avez été kick de la party §a§l" + party.getName() + "§r.");
                            player.sendMessage("§b§l" + kicked.getName() + "§r a été kick de la party §a§l" + party.getName() + "§r.");
                        } else {
                            player.sendMessage("§c" + kicked.getName() + " n'est pas dans la party.");
                        }
                    } else {
                        player.sendMessage("§c" + args[1] + " n'est pas connecté ou n'existe pas.");
                    }
                } else {
                    player.sendMessage("§cVous n'êtes pas le leader de la party.");
                }
            } else {
                player.sendMessage("§cVous n'êtes pas dans une party.");
            }
        } else {
            player.sendMessage("§c/party kick <player>");
        }
    }
}