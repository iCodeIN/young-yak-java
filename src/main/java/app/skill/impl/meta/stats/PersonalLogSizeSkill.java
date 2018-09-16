package app.skill.impl.meta.stats;

import app.entities.DialogChunk;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexSkill;
import app.web.BotController;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * This ISkill deals with diagnostic inquiries about the number of logs being stored pertaining to the current user.
 */
public class PersonalLogSizeSkill extends RegexSkill {

    private static Pattern[] PATTERNS = {
            Pattern.compile("HOW MANY LOG ENTRIES DO YOU HAVE ABOUT ME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY LOG ENTRIES DO YOU HAVE CONCERNING ME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY LOG ENTRIES DO YOU HAVE ON ME", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOWMANY LOG ENTRIES DO YOU HAVE ABOUT ME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY LOG ENTRIES DO YOU HAVE CONCERNING ME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY LOG ENTRIES DO YOU HAVE ON ME", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOW MANY LOGS DO YOU HAVE ABOUT ME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY LOGS DO YOU HAVE CONCERNING ME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY LOGS DO YOU HAVE ON ME", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOWMANY LOGS DO YOU HAVE ABOUT ME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY LOGS DO YOU HAVE CONCERNING ME", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY LOGS DO YOU HAVE ON ME", Pattern.CASE_INSENSITIVE)
    };

    private static String[] ZERO_REPLIES = {
            "I don't have any logs about you.",
            "I don't have any logs concerning you.",
            "I don't have any logs on you.",
            "I didn't find any logs about you.",
            "I didn't find any logs concerning you.",
            "I didn't find any logs on you."
                                            };

    private static String[] ONE_REPLIES = {
            "I only have one log entry about you.",
            "I only have one log entry concerning you.",
            "I only have one log entry on you."
    };

    private static String[] MORE_THAN_ONE_REPLIES = {
            "I currently have about %d log entries about you.",
            "I currently have about %d log entries concerning you.",
            "I currently have about %d log entries on you.",

            "I currently have about %d logs about you.",
            "I currently have about %d logs concerning you.",
            "I currently have about %d logs on you.",

            "I currently have %d log entries about you.",
            "I currently have %d log entries concerning you.",
            "I currently have %d log entries on you.",

            "I currently have %d logs about you.",
            "I currently have %d logs concerning you.",
            "I currently have %d logs on you.",

            "I have about %d log entries about you.",
            "I have about %d log entries concerning you.",
            "I have about %d log entries on you.",

            "I have about %d logs about you.",
            "I have about %d logs concerning you.",
            "I have about %d logs on you.",

            "I have %d log entries about you.",
            "I have %d log entries concerning you.",
            "I have %d log entries on you.",

            "I have %d logs about you.",
            "I have %d logs concerning you.",
            "I have %d logs on you.",

            "According to the database, there should be %d log entries about you.",
            "According to the database, there should be %d log entries concerning you.",
            "According to the database, there should be %d log entries on you.",

            "According to the database, there should be %d logs about you.",
            "According to the database, there should be %d logs concerning you.",
            "According to the database, there should be %d logs on you."

    };

    private BotController botController;
    private static Random RANDOM = new Random(System.currentTimeMillis());

    public PersonalLogSizeSkill(BotController botController) {
        super(PATTERNS, MORE_THAN_ONE_REPLIES);
        this.botController = botController;
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        int N = 0;
        for(DialogChunk dc : botController.getDialogChunkRepository().findAll()) {
            if (dc.getUserID().equals(input.getUserID())) {
                N++;
            }
        }
        String txt = null;
        if(N == 0){
            txt = ZERO_REPLIES[RANDOM.nextInt(ZERO_REPLIES.length)];
        }
        else if(N == 1){
            txt = ONE_REPLIES[RANDOM.nextInt(ONE_REPLIES.length)];
        }
        else{
            txt = MORE_THAN_ONE_REPLIES[RANDOM.nextInt(MORE_THAN_ONE_REPLIES.length)];
            txt = String.format(txt, N);
        }
        return new HandlerResponseImpl(txt);
    }
}
