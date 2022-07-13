package fr.celestgames.fts.exceptions;

public class PartyException extends Exception {
    public static final String PARTY_ALREADY_EXISTS = "Une party portant ce nom existe déjà.";
    public static final String PARTY_NOT_FOUND = "La party n'existe pas.";
    public static final String PARTY_NOT_EMPTY = "La party n'est pas vide.";
    public static final String PARTY_NOT_INVITED = "Vous n'avez pas été invité à cette party.";
    public static final String PARTY_NOT_LEADER = "Vous n'êtes pas le leader de cette party.";
    public static final String PLAYER_NOT_IN_PARTY = "Vous n'êtes pas dans cette party.";
    public PartyException(String message) {
        super(message);
    }
}
