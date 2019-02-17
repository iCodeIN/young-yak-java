package app.skill.impl.game.hangman;

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
            Pattern.compile("PLAY HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY HANGMAN", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY A ROUND OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY A ROUND OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY A ROUND OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY A ROUND OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY A ROUND OF HANGMAN", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY A GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY A GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY A GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY A GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY A GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY ANOTHER GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY ANOTHER GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY ANOTHER GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY ANOTHER GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY ANOTHER GAME OF HANGMAN", Pattern.CASE_INSENSITIVE),
    };

    private static String[] REPLIES = {
            "Starting up a new hangman.\n%s",
            "New hangman started.\n%s",
            "Good luck!\n%s",
            "New hangman started. Good luck!\n%s"
    };

    public StartGameRequestHandler() {
        super(Arrays.asList(START_GAME_PATTERNS));
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {

        String txt = REPLIES[RANDOM.nextInt(REPLIES.length)];
        String userID = input.getUserID();

        HangmanSkill.startGame(userID);
        txt = String.format(txt, HangmanSkill.blanks(userID));
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }

}
