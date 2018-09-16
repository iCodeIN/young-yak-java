package app.skill.impl.time;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.Status;
import app.skill.impl.regex.RegexSkill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

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
        return new IHandlerResponse() {
            @Override
            public Status getStatus() {
                return Status.STATUS_200_OK;
            }

            @Override
            public Object getContent() {
                return "It is " + dateFormat.format(new Date()) + ".";
            }
        };
    }
}
