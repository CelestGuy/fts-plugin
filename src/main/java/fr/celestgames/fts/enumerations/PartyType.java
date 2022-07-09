package fr.celestgames.fts.enumerations;

public enum PartyType {
    PRIVATE, PUBLIC;

    @Override
    public String toString() {
        return switch(this) {
            case PRIVATE -> "private";
            case PUBLIC -> "public";
        };
    }
}
