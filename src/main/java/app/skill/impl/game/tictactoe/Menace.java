package app.skill.impl.game.tictactoe;

import java.util.*;
import java.util.logging.Logger;

public class Menace {

    class HashableArrayWrapper {
        int[] array;

        HashableArrayWrapper(int[] array){ this.array = array; }

        @Override
        public int hashCode() {
            return array[0] * 97 + array[1] * 23;
        }

        public boolean equals(Object object) {
            if (object instanceof HashableArrayWrapper) {
                int[] arrayB = ((HashableArrayWrapper) object).array;
                return array[0] == arrayB[0] && array[1] == arrayB[1];
            }
            return false;
        }
    }

    private static Random RANDOM = new Random(System.currentTimeMillis());
    private Map<Long, Map<HashableArrayWrapper, Integer>> memory = new HashMap<>();
    private static Logger logger = Logger.getLogger(Menace.class.getSimpleName());

    public int[] getNextInvariantPosition(int[][] board){
        // check base
        long h = boardToHash(board);
        if(memory.containsKey(h))
            return getNextPosition(board);

        // check invariants
        int[][] rotatedBoard = board;
        for(int i=0;i<4;i++){
            rotatedBoard = rotateBoardClockwise(rotatedBoard);
            h = boardToHash(rotatedBoard);
            if(memory.containsKey(h)){
                // decide best response
                int[] response = getNextPosition(rotatedBoard);

                // rotate coordinates
                int[] rotatedResponse = response;
                for (int j = 0; j <= i; j++) { rotatedResponse = rotatePositionCounterClockwise(rotatedResponse); }

                // return
                return rotatedResponse;
            }
        }

        // default
        return getNextPosition(board);
    }

    private int[] rotatePositionCounterClockwise(int[] pos){
        if(pos[0] == 0 && pos[1] == 0) return new int[]{2,0};
        if(pos[0] == 0 && pos[1] == 1) return new int[]{1,0};
        if(pos[0] == 0 && pos[1] == 2) return new int[]{0,0};

        if(pos[0] == 1 && pos[1] == 0) return new int[]{2,1};
        if(pos[0] == 1 && pos[1] == 1) return new int[]{1,1};
        if(pos[0] == 1 && pos[1] == 2) return new int[]{0,1};

        if(pos[0] == 2 && pos[1] == 0) return new int[]{2,2};
        if(pos[0] == 2 && pos[1] == 1) return new int[]{1,2};
        if(pos[0] == 2 && pos[1] == 2) return new int[]{0,2};

        return null;
    }

    private int[][] rotateBoardClockwise(int[][] board){
        // 1 2 3       7 4 1
        // 4 5 6   =>  8 5 2
        // 7 8 9       9 6 3
        return new int[][]{ new int[]{board[2][0], board[1][0], board[0][0]},
                            new int[]{board[2][1], board[1][1], board[0][1]},
                            new int[]{board[2][2], board[1][2], board[0][2]}};
    }

    private int[] getNextPosition(int[][] board){
        long h = boardToHash(board);
        // known entry
        if(memory.containsKey(h)){
            int n = 0;
            for(Map.Entry<HashableArrayWrapper, Integer> en : memory.get(h).entrySet()){
                n += en.getValue();
            }
            if(n == 0)
                return null;
            int[] selected = null;
            while(selected == null) {
                for (Map.Entry<HashableArrayWrapper, Integer> en : memory.get(h).entrySet()){
                    double p = (double) en.getValue() / (double) n;
                    if(sometimes(p)){
                        selected = en.getKey().array;
                        break;
                    }
                }
            }
            return selected;
        }
        // unknown entry
        else{
            // populate memory
            memory.put(h, new HashMap<>());
            int k = (int) Math.max(Math.floor(empty(board).size() / 2), 1);
            for(int[] opt : empty(board)){
                memory.get(h).put(new HashableArrayWrapper(opt), k);
            }
            // recurse
            return getNextPosition(board);
        }
    }

    public static int winner(int[][] board){
        for (int i = 0; i < 3; i++) {
            if(board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != 0)
                return board[i][0];
            if(board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != 0)
                return board[0][i];
        }
        if(board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != 0)
            return board[0][0];
        if(board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != 0)
            return board[0][2];
        return 0;
    }

    public static List<int[]> empty(int[][] board){
        List<int[]> empty = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[i][j] == 0)
                    empty.add(new int[]{i,j});
            }
        }
        return empty;
    }

    private static boolean sometimes(double p){
        double p2 = (double) RANDOM.nextInt(1000) / 1000.0;
        return p2 < p;
    }

    public static long boardToHash(int[][] board){
        long hash = 1;
        int[] primesA = {2, 7,17, 29,41,53, 67,79, 97};
        int[] primesB = {3,11,19, 31,43,59, 71,83,101};
        int[] primesC = {5,13,23, 37,47,61, 73,89,103};
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[i][j] == 1)
                    hash *= primesA[k];
                else if(board[i][j] == 0)
                    hash *= primesB[k];
                else
                    hash *= primesC[k];
                k++;
            }
        }
        return hash;
    }

    public static int[][] hashToBoard(long h){
        int[][] board = new int[3][3];
        int[] primesA = {2, 7,17, 29,41,53, 67,79, 97};
        int[] primesB = {3,11,19, 31,43,59, 71,83,101};
        int[] primesC = {5,13,23, 37,47,61, 73,89,103};
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(h % primesA[k] == 0)
                    board[i][j] = 1;
                if(h % primesB[k] == 0)
                    board[i][j] = 0;
                if(h % primesC[k] == 0)
                    board[i][j] =-1;
                k++;
            }
        }
        return board;
    }

    public void learnFromWin(List<Long> boards, boolean menaceBegins){
        logger.info("Learning from win.");
        if(!boards.get(0).equals(29349960207117L))
            boards.add(0, 29349960207117L);
        for (int i = 0; i < boards.size() - 1; i++) {
            if(menaceBegins && i % 2 == 1)
                continue;
            if(!menaceBegins && i % 2 == 0)
                continue;
            long hA = boards.get(i);
            long hB = boards.get(i + 1);
            if(!memory.containsKey(hA))
                continue;

            HashableArrayWrapper move = new HashableArrayWrapper(diff(hA, hB));
            if(memory.get(hA).containsKey(move))
                memory.get(hA).put(move, memory.get(hA).get(move) + 3);
            else
                memory.get(hA).put(move, 3);
        }
    }

    public void learnFromDraw(List<Long> boards){
        logger.info("Learning from draw.");
        if(!boards.get(0).equals(29349960207117L))
            boards.add(0, 29349960207117L);
        for (int i = 0; i < boards.size() - 1; i++) {
            long hA = boards.get(i);
            long hB = boards.get(i + 1);
            if(!memory.containsKey(hA))
                continue;

            HashableArrayWrapper move = new HashableArrayWrapper(diff(hA, hB));
            if(memory.get(hA).containsKey(move))
                memory.get(hA).put(move, memory.get(hA).get(move) + 1);
            else
                memory.get(hA).put(move, 1);
        }
    }

    public void learnFromLoss(List<Long> boards, boolean menaceBegins){
        logger.info("Learning from loss.");
        if(!boards.get(0).equals(29349960207117L))
            boards.add(0, 29349960207117L);
        for (int i = 0; i < boards.size() - 1; i++) {
            if(menaceBegins && i % 2 == 1)
                continue;
            if(!menaceBegins && i % 2 == 0)
                continue;
            long hA = boards.get(i);
            long hB = boards.get(i + 1);
            if(!memory.containsKey(hA))
                continue;

            HashableArrayWrapper move = new HashableArrayWrapper(diff(hA, hB));
            if(hA == 29349960207117L){
                int N = 0;
                for(Integer f : memory.get(hA).values())
                    N += f;
                if(N != 1){
                    memory.get(hA).put(move, memory.get(hA).get(move) - 1);
                }
            }
            if(memory.get(hA).containsKey(move)) {
                memory.get(hA).put(move, memory.get(hA).get(move) - 1);
            }
        }
    }

    private int[] diff(long boardA, long boardB){
        int[][] bA = hashToBoard(boardA);
        int[][] bB = hashToBoard(boardB);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(bA[i][j] != bB[i][j])
                    return new int[]{i, j};
            }
        }
        return null;
    }
}