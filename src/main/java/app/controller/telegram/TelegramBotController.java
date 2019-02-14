package app.controller.telegram;

import app.bot.DefaultBotImpl;
import app.bot.IBot;
import app.controller.IBotController;
import app.entities.DialogChunkRepository;
import app.skill.impl.aiml.AIMLSkill;
import app.skill.impl.game.blackjack.BlackJackSkill;
import app.skill.impl.game.hangman.HangmanSkill;
import app.skill.impl.internet.NYTimes.NYTimesSkill;
import app.skill.impl.internet.cocktail.CocktailSkill;
import app.skill.impl.internet.duckduckgo.DuckDuckGoSkill;
import app.skill.impl.internet.imdb.IMDBSkill;
import app.skill.impl.internet.soundcloud.SoundCloudSkill;
import app.skill.impl.internet.unsplash.UnsplashSkill;
import app.skill.impl.internet.weather.WeatherSkill;
import app.skill.impl.math.MathSkill;
import app.skill.impl.meta.stats.BotStatisticsSkill;
import app.skill.impl.meta.typo.TypoCorrectionSkill;
import app.skill.impl.meta.word2vec.FAQSkill;
import app.skill.impl.meta.word2vec.SemanticMatchSkill;
import app.skill.impl.time.TimeSkill;
import org.springframework.beans.factory.annotation.Autowired;

public class TelegramBotController implements IBotController {

    @Autowired
    private DialogChunkRepository dialogChunkRepository;

    private DefaultBotImpl bot;

    public TelegramBotController() {

        bot = new DefaultBotImpl();

        bot.addSkill(new AIMLSkill(bot))
                .addSkill(new TimeSkill())
                .addSkill(new MathSkill())

                // games
                .addSkill(new BlackJackSkill())
                .addSkill(new HangmanSkill())

                // meta
                .addSkill(new BotStatisticsSkill(this))

                // internet
                .addSkill(new CocktailSkill())
                .addSkill(new IMDBSkill())
                .addSkill(new UnsplashSkill())
                .addSkill(new WeatherSkill())
                .addSkill(new NYTimesSkill())
                .addSkill(new SoundCloudSkill())

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

}
