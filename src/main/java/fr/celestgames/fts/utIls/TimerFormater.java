package fr.celestgames.fts.utIls;

public class TimerFormater {
    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secondsLeft = seconds % 60;

        if (minutes == 0) {
            if (secondsLeft == 1) {
                return "1 seconde";
            }
            return secondsLeft + " secondes";
        } else {
            if (minutes == 1) {
                return minutes + ":" + (secondsLeft < 10 ? "0" + secondsLeft : secondsLeft) + " minute";
            }
            return minutes + ":" + (secondsLeft < 10 ? "0" + secondsLeft : secondsLeft) + " minutes";
        }
    }
}
