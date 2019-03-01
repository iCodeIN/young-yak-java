package app.skill.impl.game.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class MenaceTrainer {

    public static void main(String[] args) {
        MenaceTrainer.train(100);
    }

    public static Menace[] train(int numberOfIterations) {

        int[][] board = new int[3][3];

        Menace playerA = new Menace();
        Menace playerB = new Menace();

        for (int i = 0; i < numberOfIterations; i++) {
            // play until winner
            int turn = 1;
            int winner = 0;
            List<Long> boards = new ArrayList<>();
            while(Menace.winner(board) == 0 && !Menace.empty(board).isEmpty()) {
                int pos[] = null;
                if(turn == 1) {
                    pos = playerA.getNextPosition(board);
                }else{
                    pos = playerB.getNextPosition(board);
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
            }
            else if(winner == 1){
                    playerA.learnFromWin(boards, true);
                    playerB.learnFromLoss(boards, false);
            }else{
                    playerA.learnFromLoss(boards, true);
                    playerB.learnFromWin(boards, false);
            }


            // start again
            boards.clear();
            board = new int[3][3];
        }
        // return
        return new Menace[]{playerA, playerB};
    }
}
