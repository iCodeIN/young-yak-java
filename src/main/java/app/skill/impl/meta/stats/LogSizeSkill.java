package app.skill.impl.meta.stats;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexSkill;
import app.web.BotController;

import java.util.regex.Pattern;

/**
 * This ISkill deals with diagnostic inquiries about the number of logs being stored.
 */
public class LogSizeSkill extends RegexSkill {

    private static Pattern[] PATTERNS = {
            Pattern.compile("HOW MANY LOG ENTRIES DO YOU HAVE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY LOG ENTRIES DO YOU HAVE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOW MANY LOGS DO YOU HAVE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY LOGS DO YOU HAVE", Pattern.CASE_INSENSITIVE)
    };

    private static String[] REPLIES = {
                                        "I currently have about %d log entries.",
                                        "I currently have about %d logs.",

                                        "I currently have %d log entries.",
                                        "I currently have %d logs.",

                                        "I have about %d log entries.",
                                        "I have about %d logs.",

                                        "I have %d log entries.",
                                        "I have %d logs.",

                                        "According to the database, there should be %d log entries.",
                                        "According to the database, there should be %d logs."
                                        };

    private BotController botController;

    public LogSizeSkill(BotController botController) {
        super(PATTERNS, REPLIES);
        this.botController = botController;
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        String txt = super.invoke(input).getContent().toString();
        int N = (int) botController.getDialogChunkRepository().count();
        return new HandlerResponseImpl(String.format(txt, N), new String[]{this.getClass().getName()});
    }
}
