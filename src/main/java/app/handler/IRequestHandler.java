package app.handler;

import java.util.Optional;

/**
 * Interface for request handlers<br>
 * A request handler receives an incoming IHandlerInput first through its canHandle method.<br>
 * If the IRequestHandler returns true on canHandle, it is asked to handle the IHandlerInput and provice an IHandlerResponse.
 */
public interface IRequestHandler {

    /**
     * Determines whether this IRequestHandler can (or should) handle the incoming IHandlerInput
     *
     * @param input the given IHandlerInput
     * @return true iff the IRequestHandler can (and should) handle the input
     */
    boolean canHandle(IHandlerInput input);

    /**
     * Determines the IHandlerResponse for the incoming IHandlerInput
     *
     * @param input the given IHandlerInput
     * @return the output
     */
    Optional<IHandlerResponse> handle(IHandlerInput input);

}
