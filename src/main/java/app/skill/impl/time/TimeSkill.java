package app.skill.impl.time;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.Status;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexSkill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * This IRequestHandler deals with  request for the current time.
 */
public class TimeSkill extends RegexSkill {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:MM:ss");

    public TimeSkill() {
        super(new Pattern[]{
                Pattern.compile("WHAT TIME IS IT", Pattern.CASE_INSENSITIVE),
                Pattern.compile("WHAT IS THE TIME", Pattern.CASE_INSENSITIVE),
                Pattern.compile("WHAT IS TODAYS TIME", Pattern.CASE_INSENSITIVE),
                Pattern.compile("WHAT IS THE TIME TODAY", Pattern.CASE_INSENSITIVE),

        }, new String[]{});
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        return new HandlerResponseImpl("It is " + dateFormat.format(new Date()) + ".", new String[]{this.getClass().getName()});
    }
}
