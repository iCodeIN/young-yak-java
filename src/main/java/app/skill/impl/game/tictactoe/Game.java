package app.skill.impl.game.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Game {

    enum LookAndFeel {
        PLANETS,
        FLOWERS,
        ANIMALS,
        SPORTS,
        FRUIT,
        MOO,
        BIRDS
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
        else if(lookAndFeel == LookAndFeel.FRUIT)
            stones = new String[]{"\uD83C\uDF4E","\uD83C\uDF4F"};
        else if(lookAndFeel == LookAndFeel.MOO)
            stones = new String[]{"\uD83D\uDC04","\uD83D\uDCA9"};
        else
            stones = new String[]{"\uD83E\uDD9A","\uD83E\uDDA2"};

        // build grid
        String[][] tmpA = {{"A","B","C"},{"D","E","F"},{"G","H","I"}};
        String[][] tmpB = new String[3][3];
        int[] colWidth = {1,1,1};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[i][j] == 0) {
                    tmpB[i][j] = tmpA[i][j];
                }
                if(board[i][j] == 1) {
                    tmpB[i][j] = stones[0];
                    colWidth[j] = 2;
                }
                if(board[i][j] == -1) {
                    tmpB[i][j] = stones[1];
                    colWidth[j] = 2;
                }
            }
        }

        // build final string
        String out = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String c = tmpB[i][j];
                out += spaces(colWidth[j] - c.length()) + c + "\t";
            }
            out += "\n";
        }

        // return
        return "<pre>" + out + "</pre>";
    }

    private String spaces(int l){
        String s = "";
        while(s.length()  < l)
            s += " ";
        return s;
    }
}
