package app.skill.impl.game.hangman;

import app.handler.IHandlerInput;
import app.skill.DefaultSkillImpl;

import java.util.HashMap;
import java.util.Map;

public class HangmanSkill extends DefaultSkillImpl {

    private static Map<String, Game> GAMES_IN_PROGRESS = new HashMap<>();

    public HangmanSkill() {
        addRequestHandler(new StartGameRequestHandler());
        addRequestHandler(new MakeGuessRequestHandler());
    }

    public static Game getGame(String userID) {
        return GAMES_IN_PROGRESS.get(userID);
    }

    public static Game startGame(String userID) {
        // setup hangman
        Game g = new Game();
        GAMES_IN_PROGRESS.put(userID, g);
        return g;
    }

    public static void guess(String userID, String letter) {
        GAMES_IN_PROGRESS.get(userID).guess(letter);
    }

    public static void stopGame(String userID) {
        GAMES_IN_PROGRESS.remove(userID);
    }

    @Override
    public boolean canHandle(IHandlerInput input) {
        String userID = input.getUserID();
        boolean ch = super.canHandle(input);
        if (!ch && GAMES_IN_PROGRESS.containsKey(userID))
            GAMES_IN_PROGRESS.remove(userID);

        return ch;
    }

}
