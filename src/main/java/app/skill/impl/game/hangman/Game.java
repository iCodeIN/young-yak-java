package app.skill.impl.game.hangman;

import java.util.Arrays;
import java.util.Random;

public class Game {

    String word;
    String guesses;
    private int badGuesses;

    private static Random RANDOM = new Random(System.currentTimeMillis());
    private static String[] DICTIONARY = {
            "Awkward", "Abyss", "Bagpipes", "Banjo", "Bayou", "Bungler",
            "Croquet", "Crypt", "Dwarves","Embezzle", "Fervid", "Fishhook",
            "Fjord", "Gazebo", "Glyph","Gypsy", "Haiku", "Haphazard",
            "Hyphen", "Ivory", "Jazzy", "Jiffy", "Jinx",
            "Jukebox", "Kayak", "Kiosk", "Klutz", "Memento",
            "Mystify", "Numbskull", "Ostracize", "Oxygen", "Pajama",
            "Phlegm", "Pixel", "Polka", "Quad", "Quip",
            "Rhythmic", "Rogue", "Sphinx", "Squawk", "Swivel",
            "Toady", "Twelfth", "Unzip", "Waxy", "Wildebeest",
            "Yacht", "Zealous", "Zigzag", "Zippy", "Zombie"};

    public Game(){
        this.word = DICTIONARY[RANDOM.nextInt(DICTIONARY.length)];
        guesses = "";
        badGuesses = 0;
    }

    public void guess(String letter) {
        guesses += letter;
        badGuesses += word.contains(letter) ? 0 : 1;
    }

    public boolean isWordCompletelyGuessed(){
        return Arrays.asList(guesses.split("")).containsAll(Arrays.asList(word.split("")));
    }

    public boolean isGameOver(){
        return badGuesses >= 10;
    }

    public String blanks() {

        // create blanks
        String blanks = "";
        for (int i = 0; i < word.length(); i++) {
            if (guesses.isEmpty())
                blanks += "_ ";
            else if (guesses.contains(word.substring(i, i + 1)))
                blanks += word.substring(i, i + 1) + " ";
            else
                blanks += "_ ";
        }
        blanks = blanks.substring(0, blanks.length() - 1);

        // return
        return blanks;
    }

}
