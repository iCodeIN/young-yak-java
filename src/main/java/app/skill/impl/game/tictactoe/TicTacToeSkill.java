package app.skill.impl.game.tictactoe;

import app.skill.DefaultSkillImpl;

import java.util.HashMap;
import java.util.Map;

public class TicTacToeSkill extends DefaultSkillImpl {

    private static Menace playerB = new Menace();
    private static Map<String, Game> GAMES_IN_PROGRESS = new HashMap<>();

    public TicTacToeSkill(){
        addRequestHandler(new StartGameRequestHandler());
        addRequestHandler(new PlaceStoneRequestHandler());
    }

    public static void startGame(String userID) {
        // setup game
        GAMES_IN_PROGRESS.put(userID, new Game());
    }

    public static Game getGame(String userID){ return GAMES_IN_PROGRESS.get(userID); }

    public static Menace getPlayerB(){return playerB;}

    public static void userWon(String userID){
        playerB.learnFromLoss(GAMES_IN_PROGRESS.get(userID).getBoardHashes(), false);
        GAMES_IN_PROGRESS.remove(userID);
    }

    public static void userLost(String userID){
        playerB.learnFromWin(GAMES_IN_PROGRESS.get(userID).getBoardHashes(), false);
        GAMES_IN_PROGRESS.remove(userID);
    }

}
