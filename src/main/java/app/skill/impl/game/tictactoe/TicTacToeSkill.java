package app.skill.impl.game.tictactoe;

import app.skill.DefaultSkillImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicTacToeSkill extends DefaultSkillImpl {

    private static Menace playerB = MenaceTrainer.train(10000)[1];
    private static Map<String, List<Long>> GAMES_IN_PROGRESS = new HashMap<>();

    public TicTacToeSkill(){
        addRequestHandler(new StartGameRequestHandler());
        addRequestHandler(new PlaceStoneRequestHandler());
    }

    public static void startGame(String userID) {
        // setup game
        List<Long> game = new ArrayList<>();
        game.add(Menace.boardToHash(new int[3][3]));
        GAMES_IN_PROGRESS.put(userID, game);
    }

    public static int[][] getGame(String userID){
        List<Long> boards = GAMES_IN_PROGRESS.get(userID);
        if(boards == null)
            return null;
        if(boards.isEmpty())
            return null;
        return Menace.hashToBoard(boards.get(boards.size()-1));
    }

    public static void setGame(String userID, int[][] game){
        GAMES_IN_PROGRESS.get(userID).add(Menace.boardToHash(game));
    }

    public static Menace getPlayerB(){return playerB;}

    public static void userWon(String userID){
        List<Long> boards = GAMES_IN_PROGRESS.get(userID);
        playerB.markLoss(boards);
        GAMES_IN_PROGRESS.remove(userID);
    }

    public static void userLost(String userID){
        List<Long> boards = GAMES_IN_PROGRESS.get(userID);
        playerB.markWin(boards);
        GAMES_IN_PROGRESS.remove(userID);
    }

    public static String boardToString(int[][] board){
        return  "<code>.---.---.---.\n"
                + "| "  + (board[0][0] == 0 ? "A" : board[0][0] == 1 ? "X" : "O")
                + " | " + (board[0][1] == 0 ? "B" : board[0][1] == 1 ? "X" : "O")
                + " | " + (board[0][2] == 0 ? "C" : board[0][2] == 1 ? "X" : "O")+" |\n" +
                ".---.---.---.\n"
                + "| "  + (board[1][0] == 0 ? "D" : board[1][0] == 1 ? "x" : "O")
                + " | " + (board[1][1] == 0 ? "E" : board[1][1] == 1 ? "x" : "O")
                + " | " + (board[1][2] == 0 ? "F" : board[1][2] == 1 ? "x" : "O")+" |\n" +
                ".---.---.---.\n"
                + "| "  + (board[2][0] == 0 ? "G" : board[2][0] == 1 ? "X" : "O")
                + " | " + (board[2][1] == 0 ? "H" : board[2][1] == 1 ? "X" : "O")
                + " | " + (board[2][2] == 0 ? "I" : board[2][2] == 1 ? "X" : "O")+" |\n" +
                ".---.---.---.</code>";
    }

}
