package fr.celestgames.fts.server.party;

import fr.celestgames.fts.enumerations.PartyType;
import fr.celestgames.fts.events.party.LeaderChangeEvent;
import fr.celestgames.fts.events.party.PlayerJoinPartyEvent;
import fr.celestgames.fts.events.party.PlayerLeavePartyEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getPluginManager;

public class Party {
    private final ArrayList<Player> members;
    private final String name;
    private Player leader;
    private PartyType type;

    public Party(String name, Player leader) {
        this.name = name;
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader);
        this.type = PartyType.PRIVATE;
        leader.sendMessage("Vous avez créé la party §a§l" + name + "§r. Invitez vos amis pour jouer à un mini-jeu.");
    }

    public void addMember(Player member) {
        if (!members.contains(member)) {
            members.add(member);
            getPluginManager().callEvent(new PlayerJoinPartyEvent(this, member));
        } else {
            member.sendMessage("§cVous êtes déjà dans cette party !");
        }
    }

    public void removeMember(Player member) {
        if (members.contains(member)) {
            members.remove(member);
            if (leader.equals(member) && members.size() > 0) {
                setLeader(members.get(0));
                getPluginManager().callEvent(new LeaderChangeEvent(this, leader));
            }
            getPluginManager().callEvent(new PlayerLeavePartyEvent(this, member));
        }
    }

    public String getName() {
        return name;
    }
    public Player getLeader() {
        return leader;
    }
    public ArrayList<Player> getMembers() {
        return members;
    }
    public PartyType getType() {
        return type;
    }
    public void setType(PartyType type) {
        this.type = type;
    }
    public void setLeader(Player leader) {
        this.leader = leader;
    }
}
