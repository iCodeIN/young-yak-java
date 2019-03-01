package app.skill.impl.game.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Game {

    enum LookAndFeel {
        PLANETS,
        FLOWERS,
        ANIMALS,
        SPORTS,
        FRUIT
    }

    private List<Long> boardHashes = new ArrayList<>();
    private LookAndFeel lookAndFeel;

    public Game(){
        lookAndFeel = LookAndFeel.values()[(int) (System.currentTimeMillis() % LookAndFeel.values().length)];
        addBoard(new int[3][3]);
    }

    public List<Long> getBoardHashes(){return boardHashes;}
    public List<int[][]> getBoards(){
        List<int[][]> boards = new ArrayList<>();
        for(long l : getBoardHashes())
            boards.add(Menace.hashToBoard(l));
        return boards;
    }

    public long getLastBoardHash(){return boardHashes.get(boardHashes.size()-1);}
    public int[][] getLastBoard(){return Menace.hashToBoard(getLastBoardHash());}

    public void addBoard(int[][] board){ this.boardHashes.add(Menace.boardToHash(board)); }
    public void addBoard(long boardHash){this.boardHashes.add(boardHash);}

    public void placeStone(int x, int y){
        int[][] lastBoard = getLastBoard();

        // count occupied squares
        int occupied = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(lastBoard[i][j] != 0)
                    occupied++;
            }
        }

        // determine turn
        int turn = 0;
        turn = (occupied % 2 == 0) ? 1 : -1;

        // place stone
        lastBoard[x][y] = turn;

        // store
        addBoard(lastBoard);
    }

    public LookAndFeel getLookAndFeel(){ return lookAndFeel; }

    public String lastBoardToString(){
        int[][] board = Menace.hashToBoard(boardHashes.get(boardHashes.size()-1));
        String[] stones;
        if(lookAndFeel == LookAndFeel.PLANETS)
            stones = new String[]{"\uD83C\uDF0E","\uD83C\uDF15"};
        else if(lookAndFeel == LookAndFeel.ANIMALS)
            stones = new String[]{"\uD83E\uDD8A", "\uD83D\uDC36"};
        else if(lookAndFeel == LookAndFeel.FLOWERS)
            stones = new String[]{"\uD83C\uDF3A", "\uD83C\uDF40"};
        else if(lookAndFeel == LookAndFeel.SPORTS)
            stones = new String[]{"\uD83C\uDFC0","\uD83C\uDFD0"};
        else
            stones = new String[]{"\uD83C\uDF4E","\uD83C\uDF4F"};

        return  "<pre>"
                + (board[0][0] == 0 ? " A " : board[0][0] == 1 ? stones[0] : stones[1])
                + (board[0][1] == 0 ? " B " : board[0][1] == 1 ? stones[0] : stones[1])
                + (board[0][2] == 0 ? " C " : board[0][2] == 1 ? stones[0] : stones[1]) + "\n"
                + (board[1][0] == 0 ? " D " : board[1][0] == 1 ? stones[0] : stones[1])
                + (board[1][1] == 0 ? " E " : board[1][1] == 1 ? stones[0] : stones[1])
                + (board[1][2] == 0 ? " F " : board[1][2] == 1 ? stones[0] : stones[1]) + "\n"
                + (board[2][0] == 0 ? " G " : board[2][0] == 1 ? stones[0] : stones[1])
                + (board[2][1] == 0 ? " H " : board[2][1] == 1 ? stones[0] : stones[1])
                + (board[2][2] == 0 ? " I " : board[2][2] == 1 ? stones[0] : stones[1]) + "\n" +
                "</pre>";
    }
}
