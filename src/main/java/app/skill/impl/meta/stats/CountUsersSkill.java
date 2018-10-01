package app.skill.impl.meta.stats;

import app.entities.DialogChunk;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexSkill;
import app.web.BotController;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * This ISkill deals with diagnostic inquiries about the number of users.
 */
public class CountUsersSkill extends RegexSkill {

    private static Pattern[] PATTERNS = {
            Pattern.compile("HOWMANY USERS ARE THERE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY USERS ARE THERE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOWMANY PEOPLE HAVE YOU TALKED TO", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY PEOPLE HAVE YOU TALKED TO", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOWMANY PEOPLE DO YOU TALK TO", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY PEOPLE DO YOU TALK TO", Pattern.CASE_INSENSITIVE)
    };

    private static String[] REPLIES = {
            "I've talked to %d people.",
            "I have talked to %d people.",
            "I've talked to %d users.",
            "I have talked to %d users."
    };

    private final BotController botController;

    public CountUsersSkill(BotController botController) {
        super(PATTERNS, REPLIES);
        this.botController = botController;
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        String txt = super.invoke(input).getContent().toString();
        Set<String> userIDs = new HashSet<>();
        for (DialogChunk dc : botController.getDialogChunkRepository().findAll()) {
            userIDs.add(dc.getUserID());
        }
        int N = userIDs.size();
        return new HandlerResponseImpl(String.format(txt, N), new String[]{this.getClass().getName()});
    }
}
