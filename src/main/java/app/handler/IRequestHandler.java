package app.handler;

import java.util.Optional;

public interface IRequestHandler {

    boolean canHandle(IHandlerInput input);

    Optional<IHandlerResponse> handle(IHandlerInput input);

}
