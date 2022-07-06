package fr.celestgames.fts.server;

import fr.celestgames.fts.FTSMain;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class FriendsManager implements Serializable {
    private final FTSMain plugin;
    private final HashMap<Player, ArrayList<Player>> friendShips;
    private final HashMap<Player, ArrayList<Player>> friendRequests;

    public FriendsManager(FTSMain plugin, HashMap<Player, ArrayList<Player>> friendRequests) {
        this.plugin = plugin;
        this.friendRequests = friendRequests;
        this.friendShips = new HashMap<>();
    }

    public void addFriend(Player player, Player friend) {
        if (!friendShips.containsKey(player)) {
            friendShips.put(player, new ArrayList<>());
        }
        if (!friendShips.containsKey(friend)) {
            friendShips.put(friend, new ArrayList<>());
        }
        if (friendShips.get(player).contains(friend)) {
            player.sendMessage("§cCe joueur est déjà dans votre liste d'amis.");
        } else {
            friendRequests.get(friend).remove(player);
            friendRequests.get(player).remove(friend);
            friendShips.get(player).add(friend);
            friendShips.get(friend).add(player);

            player.sendMessage("§b§l" + friend.getName() + " est désormais votre ami.");
            friend.sendMessage("§b§l" + player.getName() + " est désormais votre ami.");
        }
    }

    public void removeFriend(Player player, Player friend) {
        if (friendShips.containsKey(player)) {
            friendShips.get(player).remove(friend);
        }
    }

    public boolean isFriend(Player player, Player friend) {
        if (friendShips.containsKey(player)) {
            return friendShips.get(player).contains(friend);
        }
        return false;
    }

    public ArrayList<Player> getFriends(Player player) {
        if (friendShips.containsKey(player)) {
            return friendShips.get(player);
        }
        return new ArrayList<>();
    }
}
