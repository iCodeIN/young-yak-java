package app.controller.api;

import app.bot.DefaultBotImpl;
import app.bot.IBot;
import app.controller.BotReply;
import app.controller.IBotController;
import app.entities.DialogChunk;
import app.entities.DialogChunkRepository;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerInputImpl;
import app.skill.impl.aiml.AIMLSkill;
import app.skill.impl.game.blackjack.BlackJackSkill;
import app.skill.impl.game.tictactoe.TicTacToeSkill;
import app.skill.impl.internet.NYTimes.NYTimesSkill;
import app.skill.impl.internet.cocktail.CocktailSkill;
import app.skill.impl.internet.duckduckgo.DuckDuckGoSkill;
import app.skill.impl.internet.imdb.IMDBSkill;
import app.skill.impl.internet.soundcloud.SoundCloudSkill;
import app.skill.impl.internet.unsplash.UnsplashSkill;
import app.skill.impl.internet.weather.WeatherSkill;
import app.skill.impl.internet.xkcd.XkcdSkill;
import app.skill.impl.math.MathSkill;
import app.skill.impl.game.hangman.HangmanSkill;
import app.skill.impl.meta.stats.BotStatisticsSkill;
import app.skill.impl.meta.typo.TypoCorrectionSkill;
import app.skill.impl.meta.word2vec.FAQSkill;
import app.skill.impl.meta.word2vec.SemanticMatchSkill;
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
@RequestMapping("api")
public class ApiBotController implements IBotController {

    @Autowired
    private DialogChunkRepository dialogChunkRepository;

    private DefaultBotImpl bot;

    public ApiBotController() {
        bot = new DefaultBotImpl();

        bot.addSkill(new AIMLSkill(bot))
                .addSkill(new TimeSkill())
                .addSkill(new MathSkill())

                // games
                .addSkill(new BlackJackSkill())
                .addSkill(new HangmanSkill())
                .addSkill(new TicTacToeSkill())

                // meta
                .addSkill(new BotStatisticsSkill(this))

                // internet
                .addSkill(new CocktailSkill())
                .addSkill(new IMDBSkill())
                .addSkill(new UnsplashSkill())
                .addSkill(new WeatherSkill())
                .addSkill(new NYTimesSkill())
                .addSkill(new SoundCloudSkill())
                .addSkill(new XkcdSkill())

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

    public Optional<IHandlerResponse> getResponse(String input, String userID){
        Optional<IHandlerResponse> tmpA = getBot().respond(new HandlerInputImpl(input, userID));

        // store
        storeDialogChunk(input,
                tmpA.isPresent() ? tmpA.get().getContent().toString() : "",
                tmpA.isPresent() ? tmpA.get().getInvokedSkills() : new String[]{},
                userID);

        return tmpA;
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

    @CrossOrigin()
    @RequestMapping("/respond")
    public BotReply respond(@RequestParam(value = "text", defaultValue = "") String text, @RequestParam(value = "userid", defaultValue = "") String userID) {
        // get response
        Optional<IHandlerResponse> tmpA = getResponse(text, userID);
        // return
        return new BotReply(userID, tmpA.isPresent() ? tmpA.get().getContent().toString() : "");
    }

}
