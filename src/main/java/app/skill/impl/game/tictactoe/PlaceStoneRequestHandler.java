package app.skill.impl.game.tictactoe;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

public class PlaceStoneRequestHandler extends RegexRequestHandler {

    private static Pattern[] HIT_ME_PATTERNS = {
            Pattern.compile("PLAY ON [A-I]", Pattern.CASE_INSENSITIVE),
            Pattern.compile("PLACE ON [A-I]", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY ON [A-I] PLEASE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("PLACE ON [A-I] PLEASE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("[A-I]", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[A-I] PLEASE", Pattern.CASE_INSENSITIVE)
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

    public PlaceStoneRequestHandler() {
        super(Arrays.asList(HIT_ME_PATTERNS));
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        // check whether there is a game in progress
        int[][] g = TicTacToeSkill.getGame(input.getUserID());
        if (g == null)
            return Optional.of(new HandlerResponseImpl(NO_SUCH_GAME[RANDOM.nextInt(NO_SUCH_GAME.length)], new String[]{this.getClass().getName()}));

        // check whether the move is valid
        return Optional.empty();
    }

}
