package app.skill.impl.game.blackjack;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import java.util.*;
import java.util.regex.Pattern;

public class HitMeRequestHandler extends RegexRequestHandler {

    private static Pattern[] HIT_ME_PATTERNS = {
            Pattern.compile("HIT ME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HIT ME PLEASE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("CARD", Pattern.CASE_INSENSITIVE),
            Pattern.compile("CARD PLEASE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("ONE MORE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ONE MORE PLEASE", Pattern.CASE_INSENSITIVE)
    };

    private static Random RANDOM = new Random(System.currentTimeMillis());

    private String[] NO_SUCH_GAME = {
            "We aren't playing Blackjack.",
            "We aren't playing Blackjack at the moment.",
            "We're not playing Blackjack.",
            "We're not playing Blackjack at the moment."
    };

    private String[] GAME_LOST = {
            "You have %s, you lose.",
            "You have %s, you lost.",
            "You lost. You have %s.",
            "I'm afraid you lost, you have %s."
    };

    private String[] CARD_STATUS = {
            "You now have %s, for a value of %s"
    };

    public HitMeRequestHandler(){
        super(Arrays.asList(HIT_ME_PATTERNS));
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        // check whether there is a game in progress
        BlackJackSkill.Game g = BlackJackSkill.getGame(input.getUserID());
        if(g == null)
            return Optional.of(new HandlerResponseImpl(NO_SUCH_GAME[RANDOM.nextInt(NO_SUCH_GAME.length)], new String[]{this.getClass().getName()}));

        // draw card
        BlackJackSkill.drawCardForPlayer(input.getUserID());

        // get game
        g = BlackJackSkill.getGame(input.getUserID());

        // display cards
        String cards = "";
        for (int i = 0; i < g.cards.length; i++) {
            if(g.selected[i] == 1)
                cards += (cards.isEmpty() ? "" : ", " ) + g.cards[i];
        }

        if(g.valueForPlayer().isEmpty()){
            String txt = String.format(GAME_LOST[RANDOM.nextInt(GAME_LOST.length)], cards);
            return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
        }


        // calculate value
        String value = "";
        for (int v : g.valueForPlayer()) {
            value += (value.isEmpty() ? "" : " or ") + v;
        }

        String txt = String.format(CARD_STATUS[RANDOM.nextInt(CARD_STATUS.length)], cards, value);

        // return answer
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }

}
