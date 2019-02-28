package app.skill.impl.game.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class MenaceTrainer {

    public static void main(String[] args) {

        int[][] board = new int[3][3];

        Menace playerA = new Menace();
        Menace playerB = new Menace();

        for (int i = 0; i < 10000; i++) {
            // play until winner
            int turn = 1;
            List<Long> boards = new ArrayList<>();
            while(Menace.winner(board) == 0 && !Menace.empty(board).isEmpty()) {
                int pos[] = null;
                if(turn == 1) {
                    pos = playerA.getNextPosition(board);
                }else{
                    pos = playerB.getNextPosition(board);
                }
                board[pos[0]][pos[1]] = turn;
                boards.add(Menace.boardToHash(board));
                turn *= -1;
            }

            // handle win/loss
            turn = Menace.winner(board);
            if(turn != 0){
                if(turn == 1){
                    playerA.markWin(boards);
                    playerB.markLoss(boards);
                }else{
                    playerA.markLoss(boards);
                    playerB.markWin(boards);
                }
            }

            // start again
            boards.clear();
            board = new int[3][3];
        }

    }
}
