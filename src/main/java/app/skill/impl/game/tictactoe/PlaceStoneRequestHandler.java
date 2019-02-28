package app.skill.impl.game.tictactoe;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceStoneRequestHandler extends RegexRequestHandler {

    private static Pattern[] PLACE_STONE = {
            Pattern.compile("PLAY ON ([A-I])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("PLACE ON ([A-I])", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY ON ([A-I]) PLEASE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("PLACE ON ([A-I]) PLEASE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("([A-I])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([A-I]) PLEASE", Pattern.CASE_INSENSITIVE)
    };

    private static Random RANDOM = new Random(System.currentTimeMillis());

    private String[] NO_SUCH_GAME = {
            "We aren't playing Tic Tac Toe.",
            "We aren't playing Tic Tac Toe at the moment.",
            "We're not playing Tic Tac Toe.",
            "We're not playing Tic Tac Toe at the moment."
    };

    private String[] INVALID_MOVE = {
            "That's not a valid move.",
            "That's not a valid move I'm afraid.",
            "I'm afraid that's not a valid move.",
            "Invalid move.",
            "Invalid move, please try again."
    };

    private String[] YOU_WIN = {
            "You win.",
            "You win this time.",
            "Congratulations, you won.",
            "Congrats, you won.",
            "Good job! You won.",
            "You won!"
    };

    private String[] I_WIN = {
        "I win!",
        "Point for me. I win!",
        "I win.",
        "I've won."
    };

    private String[] DRAW = {
        "Draw.",
            "This game ends in a draw.",
            "No more spaces left on the board."
    };

    public PlaceStoneRequestHandler() {
        super(Arrays.asList(PLACE_STONE));
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        // check whether there is a game in progress
        int[][] g = TicTacToeSkill.getGame(input.getUserID());
        if (g == null)
            return Optional.empty();
            // return Optional.of(new HandlerResponseImpl(NO_SUCH_GAME[RANDOM.nextInt(NO_SUCH_GAME.length)], new String[]{this.getClass().getName()}));

        // check whether the move is valid
        int[] pos = null;
        for(Pattern pattern : PLACE_STONE){
            Matcher mat = pattern.matcher(input.getContent().toString());
            if(mat.matches()){
                char letter = mat.group(1).charAt(0);
                if(letter == 'A') pos = new int[]{0,0};
                if(letter == 'B') pos = new int[]{0,1};
                if(letter == 'C') pos = new int[]{0,2};

                if(letter == 'D') pos = new int[]{1,0};
                if(letter == 'E') pos = new int[]{1,1};
                if(letter == 'F') pos = new int[]{1,2};

                if(letter == 'G') pos = new int[]{2,0};
                if(letter == 'H') pos = new int[]{2,1};
                if(letter == 'I') pos = new int[]{2,2};
                break;
            }
        }
        if(pos == null)
            return Optional.of(new HandlerResponseImpl(INVALID_MOVE[RANDOM.nextInt(INVALID_MOVE.length)], new String[]{this.getClass().getName()}));

        // check validity
        boolean posValid = false;
        for(int[] empty : Menace.empty(g)){
            if(empty[0] == pos[0] && empty[1] == pos[1]) {
                posValid = true;
                break;
            }
        }
        if(!posValid)
            return Optional.of(new HandlerResponseImpl(INVALID_MOVE[RANDOM.nextInt(INVALID_MOVE.length)], new String[]{this.getClass().getName()}));

        // check win/loss
        g[pos[0]][pos[1]] = 1;
        if(Menace.winner(g) == 1) {
            TicTacToeSkill.userWon(input.getUserID());
            return Optional.of(new HandlerResponseImpl(YOU_WIN[RANDOM.nextInt(YOU_WIN.length)], new String[]{this.getClass().getName()}));
        }
        if(Menace.winner(g) == -1) {
            TicTacToeSkill.userLost(input.getUserID());
            return Optional.of(new HandlerResponseImpl(I_WIN[RANDOM.nextInt(I_WIN.length)], new String[]{this.getClass().getName()}));
        }
        // check board full
        if(Menace.empty(g).isEmpty())
            return Optional.of(new HandlerResponseImpl(DRAW[RANDOM.nextInt(DRAW.length)], new String[]{this.getClass().getName()}));

        // play
        pos = TicTacToeSkill.getPlayerB().getNextPosition(g);
        g[pos[0]][pos[1]] = -1;
        TicTacToeSkill.setGame(input.getUserID(), g);

        // check win/loss
        if(Menace.winner(g) == -1) {
            TicTacToeSkill.userLost(input.getUserID());
            return Optional.of(new HandlerResponseImpl(I_WIN[RANDOM.nextInt(I_WIN.length)], new String[]{this.getClass().getName()}));
        }

        // default
        return Optional.of(new HandlerResponseImpl(TicTacToeSkill.boardToString(g), new String[]{this.getClass().getName()}));
    }

}
