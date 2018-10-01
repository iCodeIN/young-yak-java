package app.skill.impl.meta.stats;

import app.bot.DefaultBotImpl;
import app.bot.IBot;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.ISkill;
import app.skill.impl.regex.RegexSkill;
import app.web.BotController;

import java.util.regex.Pattern;

/**
 * This ISkill deals with diagnostic inquiries about the number of skills.
 */
public class ListSkillsSkill extends RegexSkill {

    private static Pattern[] PATTERNS = {
            Pattern.compile("WHAT SKILLS DO YOU HAVE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHICH SKILLS DO YOU HAVE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("WHAT SKILLS ARE INSTALLED", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHICH SKILLS ARE INSTALLED", Pattern.CASE_INSENSITIVE)
    };
    private final BotController botController;

    public ListSkillsSkill(BotController bot) {
        super(PATTERNS, new String[]{});
        this.botController = bot;
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        String out = "<ul>";
        for (ISkill s : ((DefaultBotImpl) botController.getBot()).getSkills())
            out += ("<li>" + s.getClass().getSimpleName() + "</li>");
        out += "</ul>";
        return new HandlerResponseImpl(out, new String[]{this.getClass().getName()});
    }
}
