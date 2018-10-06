package app.skill.impl.meta.stats;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;
import app.web.BotController;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * This ISkill deals with diagnostic inquiries about the number of logs being stored.
 */
public class CountLogsHandler extends RegexRequestHandler {

    private static Pattern[] PATTERNS = {
            Pattern.compile("HOW MANY LOG ENTRIES DO YOU HAVE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY LOG ENTRIES DO YOU HAVE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOW MANY LOGS DO YOU HAVE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY LOGS DO YOU HAVE", Pattern.CASE_INSENSITIVE)
    };

    private static Random RANDOM = new Random(System.currentTimeMillis());

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

    public CountLogsHandler(BotController botController) {
        super(Arrays.asList(PATTERNS));
        this.botController = botController;
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        String txt = REPLIES[RANDOM.nextInt(REPLIES.length)];
        int N = (int) botController.getDialogChunkRepository().count();
        return Optional.of(new HandlerResponseImpl(String.format(txt, N), new String[]{this.getClass().getName()}));
    }
}
