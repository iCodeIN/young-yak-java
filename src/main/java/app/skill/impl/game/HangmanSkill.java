package app.skill.impl.game;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.DefaultSkillImpl;
import app.skill.ISkill;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HangmanSkill extends DefaultSkillImpl {

    static class Game{
        String word;
        String guesses;
        int badGuesses;
    }

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

    public HangmanSkill(){
        addRequestHandler(new StartGameHandler());
        addRequestHandler(new MakeGuessHandler());
    }

    public static Game getGame(String userID){
        return GAMES_IN_PROGRESS.get(userID);
    }

    public static void startGame(String userID){
        // setup game
        Game g = new Game();
        g.guesses = "";
        g.word = DICTIONARY[RANDOM.nextInt(DICTIONARY.length)].toUpperCase();
        GAMES_IN_PROGRESS.put(userID, g);
    }

    public static void guess(String userID, String letter){
        Game g = GAMES_IN_PROGRESS.get(userID);
        g.guesses += letter;
        g.badGuesses += g.word.contains(letter) ? 0 : 1;
        GAMES_IN_PROGRESS.put(userID, g);
    }

    public static String blanks(String userID){
        Game g = GAMES_IN_PROGRESS.get(userID);

        // create blanks
        String blanks = "";
        for(int i=0;i<g.word.length();i++) {
            if(g.guesses.isEmpty())
                blanks += "_ ";
            else if(g.guesses.contains(g.word.substring(i,i+1)))
                blanks += g.word.substring(i, i+1) + " ";
            else
                blanks += "_ ";
        }
        blanks = blanks.substring(0, blanks.length() - 1);

        // return
        return blanks;
    }

}
