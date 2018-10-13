package app.skill.impl.game;

import app.handler.IHandlerInput;
import app.skill.DefaultSkillImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HangmanSkill extends DefaultSkillImpl {

    private static Map<String, Game> GAMES_IN_PROGRESS = new HashMap<>();
    private static Random RANDOM = new Random(System.currentTimeMillis());
    private static String[] DICTIONARY = {
            "Awkward", "Bagpipes", "Banjo", "Bungler",
            "Croquet", "Crypt", "Dwarves", "Fervid", "Fishhook",
            "Fjord", "Gazebo", "Gypsy", "Haiku", "Haphazard",
            "Hyphen", "Ivory", "Jazzy", "Jiffy", "Jinx",
            "Jukebox", "Kayak", "Kiosk", "Klutz", "Memento",
            "Mystify", "Numbskull", "Ostracize", "Oxygen", "Pajama",
            "Phlegm", "Pixel", "Polka", "Quad", "Quip",
            "Rhythmic", "Rogue", "Sphinx", "Squawk", "Swivel",
            "Toady", "Twelfth", "Unzip", "Waxy", "Wildebeest",
            "Yacht", "Zealous", "Zigzag", "Zippy", "Zombie"};

    public HangmanSkill() {
        addRequestHandler(new StartGameHandler());
        addRequestHandler(new MakeGuessHandler());
    }

    public static Game getGame(String userID) {
        return GAMES_IN_PROGRESS.get(userID);
    }

    public static void startGame(String userID) {
        // setup game
        Game g = new Game();
        g.guesses = "";
        g.word = DICTIONARY[RANDOM.nextInt(DICTIONARY.length)].toUpperCase();
        GAMES_IN_PROGRESS.put(userID, g);
    }

    public static void guess(String userID, String letter) {
        Game g = GAMES_IN_PROGRESS.get(userID);
        g.guesses += letter;
        g.badGuesses += g.word.contains(letter) ? 0 : 1;
        GAMES_IN_PROGRESS.put(userID, g);
    }

    public static String blanks(String userID) {
        Game g = GAMES_IN_PROGRESS.get(userID);

        // create blanks
        String blanks = "";
        for (int i = 0; i < g.word.length(); i++) {
            if (g.guesses.isEmpty())
                blanks += "_ ";
            else if (g.guesses.contains(g.word.substring(i, i + 1)))
                blanks += g.word.substring(i, i + 1) + " ";
            else
                blanks += "_ ";
        }
        blanks = blanks.substring(0, blanks.length() - 1);

        // return
        return blanks;
    }

    public static void stopGame(String userID) {
        GAMES_IN_PROGRESS.remove(userID);
    }

    public boolean canHandle(IHandlerInput input) {
        String userID = input.getUserID();

        boolean ch = super.canHandle(input);
        if (!ch && GAMES_IN_PROGRESS.containsKey(userID))
            GAMES_IN_PROGRESS.remove(userID);

        return ch;
    }

    static class Game {
        String word;
        String guesses;
        int badGuesses;
    }


}
