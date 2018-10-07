package app.skill.impl.meta.stats;

import app.entities.DialogChunk;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;
import app.web.BotController;

import java.util.*;
import java.util.regex.Pattern;

public class CountSkillInvocationHandler extends RegexRequestHandler {

    private static Pattern[] PATTERNS = {
            Pattern.compile("HOW MANY TIMES WAS EACH SKILL USED", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY TIMES IS EACH SKILL USED", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOWMANY TIMES WAS EACH SKILL USED", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY TIMES IS EACH SKILL USED", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOW MANY TIMES WAS EACH SKILL INVOKED", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW MANY TIMES IS EACH SKILL INVOKED", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOWMANY TIMES WAS EACH SKILL INVOKED", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOWMANY TIMES IS EACH SKILL INVOKED", Pattern.CASE_INSENSITIVE),
    };

    private static Random RANDOM = new Random(System.currentTimeMillis());

    private BotController botController;

    public CountSkillInvocationHandler(BotController botController) {
        super(Arrays.asList(PATTERNS));
        this.botController = botController;
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        Map<String, Integer> skillInvocationCount = new HashMap<>();
        for (DialogChunk dc : botController.getDialogChunkRepository().findAll()) {
            for (String skillName : dc.getInvokedSkills()) {
                if (skillInvocationCount.containsKey(skillName))
                    skillInvocationCount.put(skillName, skillInvocationCount.get(skillName) + 1);
                else
                    skillInvocationCount.put(skillName, 1);
            }
        }
        String out = "<table>";
        for (Map.Entry<String, Integer> en : skillInvocationCount.entrySet()) {
            out += "<tr><td>" + en.getKey() + "</td><td>" + en.getValue() + "</td></tr>";
        }
        out += "</table>";
        return Optional.of(new HandlerResponseImpl(out, new String[]{this.getClass().getName()}));
    }

}