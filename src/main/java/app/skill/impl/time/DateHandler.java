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
 * This IRequestHandler deals with  request for the current date.
 */
public class DateHandler extends RegexRequestHandler {

    private static Pattern[] PATTERNS = new Pattern[]{
            Pattern.compile("WHAT DATE IS IT", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE DATE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS TODAYS DATE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE DATE TODAY", Pattern.CASE_INSENSITIVE)
    };
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public DateHandler() {
        super(Arrays.asList(PATTERNS));
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        return Optional.of(new HandlerResponseImpl("Today is " + dateFormat.format(new Date()) + ".", new String[]{this.getClass().getName()}));
    }

}
