package fr.celestgames.fts.server;

import fr.celestgames.fts.FTSMain;
import fr.celestgames.fts.enumerations.PartyType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartyManager {
    private final FTSMain plugin;
    private final HashMap<String, Party> parties;
    private final HashMap<Player, ArrayList<String>> partyRequests;

    public PartyManager(FTSMain plugin) {
        this.plugin = plugin;
        this.parties = new HashMap<>();
        this.partyRequests = new HashMap<>();
    }

    public void createParty(String partyName, Player leader) {
        if (parties.containsKey(partyName)) {
            leader.sendMessage("§cCe nom de party est déjà utilisé.");
        } else {
            Party party = new Party(partyName, leader);
            parties.put(partyName, party);
        }
    }

    public void removeParty(String partyName) {
        parties.remove(partyName);
    }

    public void addInvitation(Player invited, Party party) throws Exception {
        if (party.getMembers().contains(invited)) {
            throw new Exception("Ce joueur est déjà dans cette party.");
        } else {
            for (Party p : parties.values()) {
                if (p.getMembers().contains(invited)) {
                    throw new Exception("Ce joueur est déjà dans une party.");
                }
            }
        }

        String partyName = party.getName();
        if (partyRequests.get(invited) != null) {
            if (partyRequests.get(invited).contains(partyName)) {
                throw new Exception("Ce joueur a déjà une invitation en attente.");
            } else {
                partyRequests.get(invited).add(partyName);
            }
        } else {
            partyRequests.put(invited, new ArrayList<>());
            partyRequests.get(invited).add(partyName);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            invited.sendMessage("§cVotre invitation à la party §a§l" + partyName + "§r§c expire dans §n§630§r§c secondes.");
        }, 20L * 30L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            invited.sendMessage("§cVotre invitation à la party §a§l" + partyName + "§r§c expire dans §n§615§r§c secondes.");
        }, 20L * 45L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            removeInvitation(invited, partyName);
            invited.sendMessage("§cVotre invitation à la party §a§l" + partyName + "§r§c a expiré.");
        }, 20L * 60L);
    }

    public void removeInvitation(Player invited, String partyName) {
        if (partyRequests.get(invited) != null) {
            partyRequests.get(invited).remove(partyName);
        }
    }

    public List<String> getPublicParties() {
        List<String> publicParties = new ArrayList<>();
        for (Party party : parties.values()) {
            if (party.getType() == PartyType.PUBLIC) {
                publicParties.add(party.getName());
            }
        }
        return publicParties;
    }

    public List<String> getPrivateParties() {
        List<String> publicParties = new ArrayList<>();
        for (Party party : parties.values()) {
            if (party.getType() == PartyType.PRIVATE) {
                publicParties.add(party.getName() + " (privée)");
            } else if (party.getType() == PartyType.FRIENDS) {
                publicParties.add(party.getName() + " (amis seulement)");
            }
        }
        return publicParties;
    }

    public List<String> getPartyRequests(Player player) {
        if (partyRequests.get(player) == null) {
            return new ArrayList<>();
        } else {
            return partyRequests.get(player);
        }
    }

    public Party getPlayerParty(String playerName) {
        for (Party party : parties.values()) {
            if (party.getMembers().contains(plugin.getServer().getPlayer(playerName))) {
                return party;
            }
        }
        return null;
    }

    public Party getParty(String partyName) {
        return parties.get(partyName);
    }
}
