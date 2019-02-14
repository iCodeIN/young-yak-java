package app.controller;

import app.bot.IBot;
import app.entities.DialogChunkRepository;

public interface IBotController {

     IBot getBot();

     DialogChunkRepository getDialogChunkRepository();

}
