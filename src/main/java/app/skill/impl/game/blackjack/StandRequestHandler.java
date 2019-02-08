package app.skill.impl.game.blackjack;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

public class StandRequestHandler extends RegexRequestHandler {

    private static Pattern[] HIT_ME_PATTERNS = {
            Pattern.compile("STAND", Pattern.CASE_INSENSITIVE),
            Pattern.compile("STAND PLEASE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("I WANT TO STAND", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WANT TO STAND PLEASE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("I WOULD LIKE TO STAND", Pattern.CASE_INSENSITIVE),
            Pattern.compile("I WOULD LIKE TO STAND PLEASE", Pattern.CASE_INSENSITIVE)
    };

    private static Random RANDOM = new Random(System.currentTimeMillis());

    private String[] NO_SUCH_GAME = {
            "We aren't playing Blackjack.",
            "We aren't playing Blackjack at the moment.",
            "We're not playing Blackjack.",
            "We're not playing Blackjack at the moment."
    };

    private String[] NOT_POSSIBLE_TO_STAND = {
            "You're not allowed to stop before reaching 17.",
            "You are not allowed to stop before reaching 17.",
            "You need to have at least 17 before you can stop.",
            "You need to have at least 17."
    };

    private String[] STAND_ACCEPTED = {
            "Ok.",
            "Ok, you've chosen to stand.",
            "Duly noted.",
    };

    public StandRequestHandler(){
        super(Arrays.asList(HIT_ME_PATTERNS));
    }

    public Optional<IHandlerResponse> handle(IHandlerInput input){
        // check whether there is a game in progress
        BlackJackSkill.Game g = BlackJackSkill.getGame(input.getUserID());
        if(g == null)
            return Optional.of(new HandlerResponseImpl(NO_SUCH_GAME[RANDOM.nextInt(NO_SUCH_GAME.length)], new String[]{this.getClass().getName()}));

        int maxValue = 0;
        for(int v : g.valueForPlayer())
            maxValue = java.lang.Math.max(v, maxValue);

        // not possible to stand
        if(maxValue < 17)
            return Optional.of(new HandlerResponseImpl(STAND_ACCEPTED[RANDOM.nextInt(STAND_ACCEPTED.length)], new String[]{this.getClass().getName()}));

        // default
        return Optional.of(new HandlerResponseImpl(NOT_POSSIBLE_TO_STAND[RANDOM.nextInt(NOT_POSSIBLE_TO_STAND.length)], new String[]{this.getClass().getName()}));
    }
}
