package app.skill.impl.time;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * This IRequestHandler deals with  request for the current time.
 */
public class TimeOfDayHandler extends RegexRequestHandler {

    private static Pattern[] PATTERNS = new Pattern[]{
            Pattern.compile("WHAT TIME IS IT", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE TIME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS TODAYS TIME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE TIME TODAY", Pattern.CASE_INSENSITIVE),

    };
    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:MM:ss");

    public TimeOfDayHandler() {
        super(Arrays.asList(PATTERNS));
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        return Optional.of(new HandlerResponseImpl("It is " + dateFormat.format(new Date()) + ".", new String[]{this.getClass().getName()}));
    }
}
