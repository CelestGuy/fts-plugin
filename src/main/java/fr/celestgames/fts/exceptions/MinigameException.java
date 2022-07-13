package fr.celestgames.fts.exceptions;

public class MinigameException extends Exception {
    public static final String PLAYER_ALREADY_IN_ROOM = "Le joueur est déjà dans un salon.";
    public static final String PLAYER_ALREADY_MOD = "Le joueur est déjà un modérateur.";
    public static final String PLAYER_NOT_IN_ROOM = "Le joueur n'est pas dans un salon.";
    public static final String PLAYER_NOT_MOD = "Le joueur n'est pas un modérateur.";
    public static final String PLAYER_NOT_FOUND = "Le joueur n'existe pas.";
    public static final String ROOM_FULL = "Le salon est plein.";
    public static final String ROOM_FULL_FOR_PARTY = "Le salon est plein pour cette party.";
    public static final String ROOM_NOT_FOUND = "Le salon n'existe pas.";
    public static final String ROOM_ALREADY_EXISTS = "Un salon portant ce nom existe déjà.";
    public static final String MAP_NOT_FOUND = "La map n'existe pas.";
    public static final String NO_SPAWN_FOUND = "Aucun spawn n'a été trouvé.";
    public static final String MINIGAME_NOT_FOUND = "Le minigame n'existe pas.";
    public static final String LOBBY_NOT_FOUND = "Le lobby n'existe pas.";

    public static final String UNKNOWN_ERROR = "Une erreur inconnue est survenue.";

    public MinigameException(String message) {
        super(message);
    }
}
