package fr.celestgames.fts.server;

import fr.celestgames.fts.FTSMain;
import fr.celestgames.fts.enumerations.PartyType;
import fr.celestgames.fts.exceptions.PartyException;
import fr.celestgames.fts.server.party.Party;
import fr.celestgames.fts.server.party.PartyInvitation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PartyManager {
    private static PartyManager instance;
    private final HashSet<Party> parties;
    private final HashMap<Player, ArrayList<PartyInvitation>> partyRequests;

    private PartyManager() {
        this.parties = new HashSet<>();
        this.partyRequests = new HashMap<>();
    }

    public static PartyManager getInstance() {
        if (instance == null) {
            instance = new PartyManager();
        }
        return instance;
    }

    public void createParty(String partyName, Player leader) throws PartyException {
        for (Party party : this.parties) {
            if (party.getName().equals(partyName)) {
                throw new PartyException("Une party portant ce nom existe déjà.");
            }
        }
        Party party = new Party(partyName, leader);
        parties.add(party);
    }

    public void removeParty(String partyName) {
        this.parties.removeIf(party -> party.getName().equals(partyName));
    }

    public void addInvitation(Player inviter, Player invited, Party party) throws PartyException {
        if (party.getMembers().contains(invited)) {
            throw new PartyException("Ce joueur est déjà dans cette party.");
        } else {
            for (Party p : this.parties) {
                if (p.getMembers().contains(invited)) {
                    throw new PartyException("Ce joueur est déjà dans une party.");
                }
            }
        }

        String partyName = party.getName();
        PartyInvitation invitation = new PartyInvitation(inviter, invited, partyName);
        if (partyRequests.get(invited) != null) {
            if (partyRequests.get(invited).contains(invitation)) {
                invitation.destroy();
                throw new PartyException("Ce joueur a déjà une invitation en attente.");
            } else {
                partyRequests.get(invited).add(invitation);
            }
        } else {
            partyRequests.put(invited, new ArrayList<>());
            partyRequests.get(invited).add(invitation);
        }
    }

    public void removeInvitation(Player invited, String partyName) {
        if (partyRequests.get(invited) != null) {
            for (PartyInvitation invitation : partyRequests.get(invited)) {
                if (invitation.getPartyName().equals(partyName)) {
                    invitation.destroy();
                    partyRequests.get(invited).remove(invitation);
                    return;
                }
            }
        }
    }

    public void removeInvitations(Player invited) {
        if (partyRequests.get(invited) != null) {
            for (PartyInvitation invitation : partyRequests.get(invited)) {
                invitation.destroy();
                partyRequests.get(invited).remove(invitation);
            }
        }
    }

    public List<String> getPublicParties() {
        List<String> publicParties = new ArrayList<>();
        for (Party party : parties) {
            if (party.getType() == PartyType.PUBLIC) {
                publicParties.add(party.getName());
            }
        }
        return publicParties;
    }

    public List<String> getPrivateParties() {
        List<String> privateParties = new ArrayList<>();
        for (Party party : parties) {
            if (party.getType() == PartyType.PRIVATE) {
                privateParties.add(party.getName());
            }
        }
        return privateParties;
    }

    public List<String> getPartyRequests(Player player) {
        List<String> requests = new ArrayList<>();

        if (partyRequests.get(player) != null) {
            for (PartyInvitation invitation : partyRequests.get(player)) {
                requests.add(invitation.getPartyName());
            }
        }

        return requests;
    }

    public Party getPlayerParty(String playerName) {
        for (Party party : parties) {
            if (party.getMembers().contains(Bukkit.getPlayer(playerName))) {
                return party;
            }
        }
        return null;
    }

    public Party getParty(String partyName) {
        for (Party party : parties) {
            if (party.getName().equals(partyName)) {
                return party;
            }
        }
        return null;
    }

    public HashSet<Party> getParties() {
        return parties;
    }
}
