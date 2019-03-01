package app.skill.impl.game.blackjack;

import app.handler.IHandlerInput;
import app.skill.DefaultSkillImpl;

import java.util.*;

public class BlackJackSkill extends DefaultSkillImpl {

    private static Map<String, Game> GAMES_IN_PROGRESS = new HashMap<>();

    public BlackJackSkill(){
        addRequestHandler(new StartGameRequestHandler());
        addRequestHandler(new HitMeRequestHandler());
        addRequestHandler(new StandRequestHandler());
    }

    public static void startGame(String userID) {
        GAMES_IN_PROGRESS.put(userID, new Game());
    }

    public static Game getGame(String userID){
        return GAMES_IN_PROGRESS.get(userID);
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
