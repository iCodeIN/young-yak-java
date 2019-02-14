package app;

import app.controller.telegram.TelegramBotController;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerInputImpl;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;
import java.util.Optional;

public class TelegramApplication {

    private static TelegramBotController botController = new TelegramBotController();

    private static void reply(TelegramBot bot, Message message){
        Optional<IHandlerResponse> response = botController.getBot().respond(new HandlerInputImpl(message.text(), message.from().id()+""));
        if(response.isPresent()) {
            new Thread() {
                public void run() {
                    bot.execute(new SendMessage(message.chat().id(), response.get().getContent().toString()));
                }
            }.start();
        }
    }

    public static void main(String[] args) {

        // Create your bot passing the token received from @BotFather
        TelegramBot bot = new TelegramBot("741085051:AAFV1TKYCo-Ov8GMlaTyFhuChLDCr36M5GE");

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

}
