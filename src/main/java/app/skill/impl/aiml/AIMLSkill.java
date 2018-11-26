package app.skill.impl.aiml;

import app.bot.IBot;
import app.skill.DefaultSkillImpl;

/**
 * This ISkill deals with AIML.<br>
 * AIML, or Artificial Intelligence Markup Language, is an XML dialect for creating natural language software agents.<br>
 * The XML dialect called AIML was developed by Richard Wallace and a worldwide free software community between 1995 and 2002. <br>
 * AIML formed the basis for what was initially a highly extended Eliza called "A.L.I.C.E.." ("Artificial Linguistic Internet Computer Entity"), <br>
 * which won the annual Loebner Prize Competition in Artificial Intelligence three times, and was also the Chatterbox Challenge Champion in 2004.
 */
public class AIMLSkill extends DefaultSkillImpl {

    private final IBot bot;

    public AIMLSkill(IBot bot) {
        this.bot = bot;
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/root.xml")));
        /*
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
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/bad_robot.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/punctuation.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/salutations.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/swearwords.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/teenspeak.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/time.xml")));
        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/trivia.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/what_happened.xml")));

        addRequestHandler(new AIMLRequestHandler(bot, AIMLSkill.class.getClassLoader().getResourceAsStream("aiml/x11colors.xml")));
        */
    }

}
