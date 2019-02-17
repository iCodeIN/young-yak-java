package app.controller.telegram;

import app.controller.web.WebBotController;

import app.handler.IHandlerResponse;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("telegram")
public class TelegramBotController extends WebBotController {

    private static Random RANDOM = new Random(System.currentTimeMillis());

    public TelegramBotController() {
        // Create your bot passing the token received from @BotFather
        TelegramBot bot = new TelegramBot(System.getProperty("TELEGRAM_ID"));
        //TelegramBot bot = new TelegramBot("745707375:AAFO1KkYsuOFvR79o1jdTGZMwZP_1yoOnTg");

        // Register for updates
        bot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> list) {
                for(Update u : list){
                    reply(bot, u.message());
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }

    private long realisticDelayInMs(String text){
        int numberOfWords = text.toUpperCase().split("[^A-Z]").length + 1;              // rough count of the number of words
        double numberOfMinutes = numberOfWords / 80.0;                                      // slightly faster than average typing speed
        return (long) (numberOfMinutes * 60 * 1000) + RANDOM.nextInt(1000 * 2);
    }

    private void reply(TelegramBot bot, Message message){
        String userID = message.from().firstName() + " " + message.from().lastName() + "[" + message.from().id()+"]";

        // call super, ensuring this data gets stored
        Optional<IHandlerResponse> response = getResponse(message.text(), userID);

        if(response.isPresent()) {
            new Thread() {
                public void run() {
                    // extract text for response
                    String txt = response.get().getContent().toString();

                    // sleep
                    try { Thread.sleep(realisticDelayInMs(txt)); } catch (InterruptedException e) { e.printStackTrace(); }

                    // images
                    BaseRequest sendMessage;
                    if(txt.matches("<img src=.*>")) {
                        String imgSrc = Jsoup.parse(txt).getElementsByTag("img").get(0).attr("src");
                        sendMessage = new SendPhoto(message.chat().id(), imgSrc);
                    }
                    // regular text
                    else {
                        sendMessage = new SendMessage(message.chat().id(), txt).parseMode(ParseMode.HTML);
                    }
                    bot.execute(sendMessage);
                }
            }.start();
        }
    }

}
