package app.skill.impl.meta.stats;

import app.bot.DefaultBotImpl;
import app.controller.IBotController;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.ISkill;
import app.skill.impl.regex.RegexRequestHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * This ISkill deals with diagnostic inquiries about the number of skills.
 */
public class ListSkillsRequestHandler extends RegexRequestHandler {

    private static Pattern[] PATTERNS = {
            Pattern.compile("WHAT SKILLS DO YOU HAVE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHICH SKILLS DO YOU HAVE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("WHAT SKILLS ARE INSTALLED", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHICH SKILLS ARE INSTALLED", Pattern.CASE_INSENSITIVE)
    };
    private final IBotController botController;

    public ListSkillsRequestHandler(IBotController bot) {
        super(Arrays.asList(PATTERNS));
        this.botController = bot;
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        String out = "<ul>";
        for (ISkill s : ((DefaultBotImpl) botController.getBot()).getSkills())
            out += ("<li>" + s.getClass().getSimpleName() + "</li>");
        out += "</ul>";
        return Optional.of(new HandlerResponseImpl(out, new String[]{this.getClass().getName()}));
    }
}
