package fr.celestgames.fts.enumerations;

import java.util.ArrayList;
import java.util.List;

public enum PartyType {
    PRIVATE, PUBLIC, FRIENDS;

    public static List<String> getTypes() {
        List<String> types = new ArrayList<>();
        for (PartyType type : PartyType.values()) {
            types.add(type.name());
        }
        return types;
    }

    public static PartyType getType(String type) {
        if (type.equals("private")) {
            return PRIVATE;
        } else if (type.equals("public")) {
            return PUBLIC;
        } else if (type.equals("friends")) {
            return FRIENDS;
        }

        return PRIVATE;
    }

    @Override
    public String toString() {
        if (this == PRIVATE) {
            return "private";
        } else if (this == PUBLIC) {
            return "public";
        } else if (this == FRIENDS) {
            return "friends";
        }
        return "null";
    }
}
