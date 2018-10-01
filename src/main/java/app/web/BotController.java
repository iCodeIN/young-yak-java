package app.web;

import app.bot.DefaultBotImpl;
import app.bot.IBot;
import app.entities.DialogChunk;
import app.entities.DialogChunkRepository;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerInputImpl;
import app.skill.impl.aiml.AIMLSkill;
import app.skill.impl.eval.math.MathSkill;
import app.skill.impl.iot.*;
import app.skill.impl.meta.stats.CountUsersSkill;
import app.skill.impl.meta.stats.ListSkillsSkill;
import app.skill.impl.meta.stats.LogSizeSkill;
import app.skill.impl.meta.stats.PersonalLogSizeSkill;
import app.skill.impl.meta.typo.TypoCorrectionSkill;
import app.skill.impl.meta.word2vec.FAQSkill;
import app.skill.impl.meta.word2vec.SemanticMatchSkill;
import app.skill.impl.time.DateSkill;
import app.skill.impl.time.DaySkill;
import app.skill.impl.time.TimeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST controller that provides the main API for interacting with the chatbot.
 */
@RestController
public class BotController {

    @Autowired
    private DialogChunkRepository dialogChunkRepository;

    private DefaultBotImpl bot;

    public BotController() {
        bot = new DefaultBotImpl();

        bot.addSkill(new AIMLSkill(bot))
                .addSkill(new DateSkill())
                .addSkill(new DaySkill())
                .addSkill(new TimeSkill())
                .addSkill(new MathSkill())

                // meta
                .addSkill(new ListSkillsSkill(this))
                .addSkill(new LogSizeSkill(this))
                .addSkill(new PersonalLogSizeSkill(this))
                .addSkill(new CountUsersSkill(this))

                // iot
                .addSkill(new CocktailSkill())
                .addSkill(new OMDBSkill())
                .addSkill(new UnsplashSkill())
                .addSkill(new WeatherSkill())
                .addSkill(new NewYorkTimesSkill())

                // generic
                .addSkill(new DuckDuckGoSkill())

                // input mods
                .addSkill(new TypoCorrectionSkill(this))
                .addSkill(new SemanticMatchSkill(this))
                .addSkill(new FAQSkill(this.getClass().getClassLoader().getResourceAsStream("faq/faq.xml")));
    }

    public IBot getBot() {
        return bot;
    }

    public DialogChunkRepository getDialogChunkRepository() {
        return dialogChunkRepository;
    }

    @CrossOrigin()
    @RequestMapping("/respond")
    public BotReply respond(@RequestParam(value = "text", defaultValue = "") String text, @RequestParam(value = "userid", defaultValue = "") String userID) {
        Optional<IHandlerResponse> tmpA = bot.respond(new HandlerInputImpl(text, userID));

        // store
        storeDialogChunk(text,
                tmpA.isPresent() ? tmpA.get().getContent().toString() : "",
                tmpA.isPresent() ? tmpA.get().getInvokedSkills() : new String[]{},
                userID);

        // return
        return new BotReply(userID,
                tmpA.isPresent() ? tmpA.get().getContent().toString() : ""
        );
    }

    private void storeDialogChunk(String in, String out, String[] invokedSkills, String user) {
        try {
            DialogChunk dialogChunk = new DialogChunk(
                    in.substring(0, java.lang.Math.min(in.length(), 2048)),
                    out.substring(0, java.lang.Math.min(out.length(), 2048)),
                    invokedSkills,
                    user,
                    System.currentTimeMillis());
            dialogChunkRepository.save(dialogChunk);
        } catch (Throwable ex) {
        }
    }

}
