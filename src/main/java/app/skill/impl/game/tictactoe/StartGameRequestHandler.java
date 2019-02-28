package app.skill.impl.game.tictactoe;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

public class StartGameRequestHandler extends RegexRequestHandler {

    private static Random RANDOM = new Random(System.currentTimeMillis());

    private static Pattern[] START_GAME_PATTERNS = {
            Pattern.compile("PLAY TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY TIC TAC TOE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY A ROUND OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY A ROUND OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY A ROUND OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY A ROUND OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY A ROUND OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY A GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY A GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY A GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY A GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY A GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY ANOTHER GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY ANOTHER GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY ANOTHER GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY ANOTHER GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY ANOTHER GAME OF TIC TAC TOE", Pattern.CASE_INSENSITIVE),
    };

    private static String[] REPLIES = {
            "Starting up a new game of Tic Tac Toe.\n%s",
            "New Tic Tac Toe game started.\n%s",
            "Good luck!\n%s",
            "New game started. Good luck!\n%s"
    };


    public StartGameRequestHandler() {
        super(Arrays.asList(START_GAME_PATTERNS));
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {

        String txt = REPLIES[RANDOM.nextInt(REPLIES.length)];
        String userID = input.getUserID();

        // init game
        TicTacToeSkill.startGame(userID);

        txt = String.format(txt, TicTacToeSkill.boardToString(TicTacToeSkill.getGame(userID)));

        // return
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }
}
