package app.skill.impl.game.hangman;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakeGuessRequestHandler extends RegexRequestHandler {

    private static Pattern[] MAKE_GUESS_PATTERNS = {
            Pattern.compile("([A-Z])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("THE ([A-Z])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("THE LETTER ([A-Z])", Pattern.CASE_INSENSITIVE),

            Pattern.compile("I GUESS ([A-Z])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I GUESS THE ([A-Z])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I GUESS THE LETTER ([A-Z])", Pattern.CASE_INSENSITIVE)
    };

    private static Random RANDOM = new Random(System.currentTimeMillis());

    private String[] LETTER_TRIED_REPLIES = {
            "You already tried that letter.",
            "I'm afraid you already tried that letter.",
            "You already tried that.",
            "I'm afraid you already tried that.",
            "You already tried that letter. Try another one."
    };

    private String[] GOOD_GUESS_REPLIES = {
            "Great guess!\n%s",
            "Good job!\n%s",
            "Well done!\n%s",
            "Great guess.\n%s",
            "Good job.\n%s",
            "Well done.\n%s"
    };

    private String[] WORD_COMPLETED_REPLIES = {
            "You guessed it!",
            "You did it!",
            "Well done!",
            "Great guess. You finished the word!"
    };

    private String[] BAD_GUESS_REPLIES = {
            "I'm afraid that letter isn't correct.\n%s",
            "Incorrect.\n%s",
            "Nope.\n%s"
    };

    private String[] GAME_OVER_REPLIES = {
            "You lose.",
            "You lose :-(",
            "Nope. You lose.",
            "I'm afraid you lost."
    };

    private String[] NO_SUCH_GAME = {
            "We aren't playing Hangman.",
            "We aren't playing Hangman at the moment.",
            "We're not playing Hangman.",
            "We're not playing Hangman at the moment."
    };

    public MakeGuessRequestHandler() {
        super(Arrays.asList(MAKE_GUESS_PATTERNS));
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        // check whether there is a game in progress
        HangmanSkill.Game g = HangmanSkill.getGame(input.getUserID());
        if(g == null)
            return Optional.empty();
            // return Optional.of(new HandlerResponseImpl(NO_SUCH_GAME[RANDOM.nextInt(NO_SUCH_GAME.length)], new String[]{this.getClass().getName()}));

        String txt = input.getContent().toString();
        for (Pattern p : MAKE_GUESS_PATTERNS) {
            Matcher m = p.matcher(txt);
            if (m.matches()) {
                return handleGuess(input.getUserID(), m.group(1).toUpperCase());
            }
        }
        return Optional.empty();
    }

    private Optional<IHandlerResponse> handleGuess(String userID, String guess) {
        HangmanSkill.Game g = HangmanSkill.getGame(userID);
        if (g.guesses.contains(guess))
            return handleLetterTried();

        HangmanSkill.guess(userID, guess);

        boolean goodGuess = g.word.contains(guess);
        if (goodGuess) {
            boolean wordCompleted = Arrays.asList(g.guesses.split("")).containsAll(Arrays.asList(g.word.split("")));
            if (wordCompleted) {
                HangmanSkill.stopGame(userID);
                return handleWordCompleted();
            }
            return handleGoodGuess(userID);
        } else {
            boolean gameOver = (g.badGuesses >= 10);
            if (gameOver) {
                HangmanSkill.stopGame(userID);
                return handleGameOver();
            }
            return handleBadGuess(userID);
        }
    }

    private Optional<IHandlerResponse> handleLetterTried() {
        String txt = LETTER_TRIED_REPLIES[RANDOM.nextInt(LETTER_TRIED_REPLIES.length)];
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }

    private Optional<IHandlerResponse> handleWordCompleted() {
        String txt = WORD_COMPLETED_REPLIES[RANDOM.nextInt(WORD_COMPLETED_REPLIES.length)];
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }

    private Optional<IHandlerResponse> handleGoodGuess(String userID) {
        String txt = GOOD_GUESS_REPLIES[RANDOM.nextInt(GOOD_GUESS_REPLIES.length)];
        txt = String.format(txt, HangmanSkill.blanks(userID));
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }

    private Optional<IHandlerResponse> handleGameOver() {
        String txt = GAME_OVER_REPLIES[RANDOM.nextInt(GAME_OVER_REPLIES.length)];
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }

    private Optional<IHandlerResponse> handleBadGuess(String userID) {
        String txt = BAD_GUESS_REPLIES[RANDOM.nextInt(BAD_GUESS_REPLIES.length)];
        txt = String.format(txt, HangmanSkill.blanks(userID));
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }

}
