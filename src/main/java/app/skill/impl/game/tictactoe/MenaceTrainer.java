package app.skill.impl.game.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class MenaceTrainer {

    public static void main(String[] args) {
        MenaceTrainer.train(0.9);
    }

    private static String printBoard(int[][] board){
        String[] stones = {"X","O"};
        return  ""
                + (board[0][0] == 0 ? "." : board[0][0] == 1 ? stones[0] : stones[1])
                + (board[0][1] == 0 ? "." : board[0][1] == 1 ? stones[0] : stones[1])
                + (board[0][2] == 0 ? "." : board[0][2] == 1 ? stones[0] : stones[1]) + "\n"
                + (board[1][0] == 0 ? "." : board[1][0] == 1 ? stones[0] : stones[1])
                + (board[1][1] == 0 ? "." : board[1][1] == 1 ? stones[0] : stones[1])
                + (board[1][2] == 0 ? "." : board[1][2] == 1 ? stones[0] : stones[1]) + "\n"
                + (board[2][0] == 0 ? "." : board[2][0] == 1 ? stones[0] : stones[1])
                + (board[2][1] == 0 ? "." : board[2][1] == 1 ? stones[0] : stones[1])
                + (board[2][2] == 0 ? "." : board[2][2] == 1 ? stones[0] : stones[1]);
    }

    public static Menace[] train(double desiredDrawPercentage) {

        int[][] board = new int[3][3];

        Menace playerA = new Menace();
        Menace playerB = new Menace();

        double numberOfDraws = 0;
        double drawPercentage = 0;
        int i = 0;
        while(drawPercentage < desiredDrawPercentage) {
            // bookkeeping
            i++;
            drawPercentage = (double) numberOfDraws / (double) i;

            // play until winner
            int turn = 1;
            int winner = 0;
            List<Long> boards = new ArrayList<>();
            while(Menace.winner(board) == 0 && !Menace.empty(board).isEmpty()) {
     //           System.out.println(printBoard(board) + "\n");
                int pos[] = null;
                if(turn == 1) {
                    pos = playerA.getNextInvariantPosition(board);
                }else{
                    pos = playerB.getNextInvariantPosition(board);
                }
                if(pos == null && Menace.winner(board) == 0){
                    winner = (turn * -1);
                    break;
                }
                board[pos[0]][pos[1]] = turn;
                boards.add(Menace.boardToHash(board));
                turn *= -1;
            }

            // handle win/loss
            winner = winner == 0 ? Menace.winner(board) : winner;
            if(winner == 0){
                playerA.learnFromDraw(boards);
                playerB.learnFromDraw(boards);
                numberOfDraws++;
            }
            else if(winner == 1){
                    playerA.learnFromWin(boards, true);
                    playerB.learnFromLoss(boards, false);
            }else{
                    playerA.learnFromLoss(boards, true);
                    playerB.learnFromWin(boards, false);
            }

            // logging
            //System.out.println("Iteration: " + i + ", NumberOfDraws: " + numberOfDraws + ", drawPercentage: " + drawPercentage);

            // start again
            boards.clear();
            board = new int[3][3];
        }
        // return
        return new Menace[]{playerA, playerB};
    }
}
