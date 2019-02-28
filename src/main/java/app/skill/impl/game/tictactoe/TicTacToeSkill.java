package app.skill.impl.game.tictactoe;

import app.skill.DefaultSkillImpl;

import java.util.HashMap;
import java.util.Map;

public class TicTacToeSkill extends DefaultSkillImpl {

    private static Map<String, int[][]> GAMES_IN_PROGRESS = new HashMap<>();

    public TicTacToeSkill(){
        addRequestHandler(new StartGameRequestHandler());
        addRequestHandler(new PlaceStoneRequestHandler());
    }

    public static void startGame(String userID) {
        // setup game
        GAMES_IN_PROGRESS.put(userID, new int[3][3]);
    }

    public static int[][] getGame(String userID){
        return GAMES_IN_PROGRESS.get(userID);
    }

    public static String boardToString(int[][] board){
        return  ".---.---.---.\n"
                + "| " + (board[0][0] == 0 ? "A" : board[0][0] == 1 ? "x" : "0")
                + " | " + (board[0][1] == 0 ? "B" : board[0][1] == 1 ? "x" : "0")
                + " | " + (board[0][2] == 0 ? "C" : board[0][2] == 1 ? "x" : "0")+" |\n" +
                ".---.---.---."
                + "| " + (board[1][0] == 0 ? "D" : board[1][0] == 1 ? "x" : "0")
                + " | " + (board[1][1] == 0 ? "E" : board[1][1] == 1 ? "x" : "0")
                + " | " + (board[1][2] == 0 ? "F" : board[1][2] == 1 ? "x" : "0")+" |\n" +
                ".---.---.---."
                + "| " + (board[2][0] == 0 ? "G" : board[2][0] == 1 ? "x" : "0")
                + " | " + (board[2][1] == 0 ? "H" : board[2][1] == 1 ? "x" : "0")
                + " | " + (board[2][2] == 0 ? "I" : board[2][2] == 1 ? "x" : "0")+" |\n" +
                ".---.---.---.";
    }

}
