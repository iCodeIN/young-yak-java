package app.skill.impl.meta.stats;

import app.controller.IBotController;
import app.entities.DialogChunk;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;
import app.controller.web.BotController;

import java.util.*;
import java.util.regex.Pattern;

/**
 * This ISkill deals with diagnostic inquiries about the number of users.
 */
public class CountUsersRequestHandler extends RegexRequestHandler {

    private static Pattern[] PATTERNS = {
            Pattern.compile("HOWMANY USERS ARE THERE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY USERS ARE THERE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY PEOPLE ARE THERE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY PEOPLE ARE THERE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOWMANY USERS HAVE YOU TALKED TO", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY USERS HAVE YOU TALKED TO", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY PEOPLE HAVE YOU TALKED TO", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY PEOPLE HAVE YOU TALKED TO", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOWMANY USERS DO YOU TALK TO", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY USERS DO YOU TALK TO", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY PEOPLE DO YOU TALK TO", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY PEOPLE DO YOU TALK TO", Pattern.CASE_INSENSITIVE)
    };

    private static Random RANDOM = new Random(System.currentTimeMillis());

    private static String[] REPLIES = {
            "I've talked to %d people.",
            "I have talked to %d people.",
            "I've talked to %d users.",
            "I have talked to %d users."
    };

    private final IBotController botController;

    public CountUsersRequestHandler(IBotController botController) {
        super(Arrays.asList(PATTERNS));
        this.botController = botController;
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        String txt = REPLIES[RANDOM.nextInt(REPLIES.length)];
        Set<String> userIDs = new HashSet<>();
        for (DialogChunk dc : botController.getDialogChunkRepository().findAll()) {
            userIDs.add(dc.getUserID());
        }
        int N = userIDs.size();
        return Optional.of(new HandlerResponseImpl(String.format(txt, N), new String[]{this.getClass().getName()}));
    }
}
