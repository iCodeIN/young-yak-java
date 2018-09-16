package app.skill.impl.aiml;

import app.bot.IBot;
import app.skill.DefaultSkillImpl;

public class AIMLSkill extends DefaultSkillImpl {

    private final IBot bot;

    public AIMLSkill(IBot bot) {
        this.bot = bot;
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/abbreviations.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/animals_with_letter.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/british_to_us.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/coinflip.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/countries.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/cup_of.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/day.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/diceroll.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/famous.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/identity.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/insults.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/is_prime.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/jokes.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/math.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/nerdy.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/numbers.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/polite.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/punctuation.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/salutations.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/swearwords.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/teenspeak.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/time.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/trivia.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/what_happened.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/x11colors.xml")));
    }

}
