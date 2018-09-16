package app.bot;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;

import java.util.Optional;

/**
 * Bot interface
 */
public interface IBot {

    /**
     * Optionally provides an IHandlerResponse as a response to an IHandlerInput
     * @param input the given IHandlerInput
     * @return an optional IHandlerResponse
     */
    Optional<IHandlerResponse> respond(IHandlerInput input);
}
