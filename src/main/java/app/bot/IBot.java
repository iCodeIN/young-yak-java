package app.bot;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;

import java.util.Optional;

public interface IBot {

    Optional<IHandlerResponse> respond(IHandlerInput input);
}
