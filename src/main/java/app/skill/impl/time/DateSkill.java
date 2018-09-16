package app.skill.impl.time;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.Status;
import app.skill.impl.regex.RegexSkill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

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
        return new IHandlerResponse() {
            @Override
            public Status getStatus() {
                return Status.STATUS_200_OK;
            }

            @Override
            public Object getContent() {
                return "Today is " + dateFormat.format(new Date()) + ".";
            }
        };
    }

}
