package app.skill.impl.time;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * This IRequestHandler deals with  request for the current day of the week.
 */
public class DayOfWeekHandler extends RegexRequestHandler {

    private static Pattern[] PATTERNS = new Pattern[]{
            Pattern.compile("WHAT DAY IS IT", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE DAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS TODAYS DAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE DAY TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT DAY IS IT TODAY", Pattern.CASE_INSENSITIVE),

            Pattern.compile("IS IT MONDAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT TUESDAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT WEDNESDAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT THURSDAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT FRIDAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT SATURDAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT SUNDAY", Pattern.CASE_INSENSITIVE),

            Pattern.compile("IS IT MONDAY TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT TUESDAY TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT WEDNESDAY TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT THURSDAY TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT FRIDAY TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT SATURDAY TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("IS IT SUNDAY TODAY", Pattern.CASE_INSENSITIVE),

    };
    private String[] daysOfTheWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public DayOfWeekHandler() {
        super(Arrays.asList(PATTERNS));
    }


    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        return Optional.of(new HandlerResponseImpl(
                "Today is " + daysOfTheWeek[new Date().getDay()] + ".",
                new String[]{this.getClass().getName()}
        ));
    }
}
