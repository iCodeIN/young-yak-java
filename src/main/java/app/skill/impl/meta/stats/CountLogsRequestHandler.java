package app.skill.impl.meta.stats;

import app.controller.IBotController;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

public class CountLogsRequestHandler extends RegexRequestHandler {

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

    private IBotController botController;

    public CountLogsRequestHandler(IBotController botController){
        super(Arrays.asList(PATTERNS));
        this.botController = botController;
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        long size = botController.getDialogChunkRepository().count();
        String txt = String.format(REPLIES[RANDOM.nextInt(REPLIES.length)], size);
        return Optional.of(new HandlerResponseImpl(txt, new String[]{this.getClass().getName()}));
    }
}
