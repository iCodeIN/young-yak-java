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
 * This IRequestHandler deals with  request for the current date.
 */
public class DateSkill extends RegexSkill {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public DateSkill() {
        super(new Pattern[]{
                Pattern.compile("WHAT DATE IS IT", Pattern.CASE_INSENSITIVE),
                Pattern.compile("WHAT IS THE DATE", Pattern.CASE_INSENSITIVE),
                Pattern.compile("WHAT IS TODAYS DATE", Pattern.CASE_INSENSITIVE),
                Pattern.compile("WHAT IS THE DATE TODAY", Pattern.CASE_INSENSITIVE),

        }, new String[]{});
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        return new HandlerResponseImpl("Today is " + dateFormat.format(new Date()) + ".", new String[]{this.getClass().getName()});
    }

}
