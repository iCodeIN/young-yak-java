package app.skill.impl.game.blackjack;

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
            Pattern.compile("PLAY BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY BLACKJACK", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY A ROUND OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY A ROUND OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY A ROUND OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY A ROUND OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY A ROUND OF BLACKJACK", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY A GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY A GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY A GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY A GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY A GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),

            Pattern.compile("PLAY ANOTHER GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO PLAY ANOTHER GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANNA PLAY ANOTHER GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LETS PLAY ANOTHER GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LET'S PLAY ANOTHER GAME OF BLACKJACK", Pattern.CASE_INSENSITIVE),
    };

    private static String[] REPLIES = {
            "Starting up a new game of blackjack.\n%s",
            "New blackjack game started.\n%s",
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
        BlackJackSkill.startGame(userID);

        // get game
        Game g = BlackJackSkill.getGame(userID);

        String cards = "";
        for (int i = 0; i < g.cards.length; i++) {
            if(g.selected[i] == 1)
                cards += (cards.isEmpty() ? "" : " and " ) + g.cards[i];
        }
        txt = String.format(txt, cards);

        // return
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }
}
