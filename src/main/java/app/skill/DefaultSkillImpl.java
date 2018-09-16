package app.skill;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.IRequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultSkillImpl implements ISkill {

    private List<IRequestHandler> requestHandlerList = new ArrayList<>();

    public DefaultSkillImpl addRequestHandler(IRequestHandler handler) {
        this.requestHandlerList.add(handler);
        return this;
    }

    public DefaultSkillImpl removeRequestHandler(IRequestHandler handler) {
        this.requestHandlerList.remove(handler);
        return this;
    }

    @Override
    public boolean canHandle(IHandlerInput input) {
        for (IRequestHandler handler : requestHandlerList)
            if (handler.canHandle(input))
                return true;
        return false;
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        for (IRequestHandler handler : requestHandlerList) {
            if (handler.canHandle(input)) {
                Optional<IHandlerResponse> response = handler.handle(input);
                if (response.isPresent())
                    return response.get();
            }
        }
        return null;
    }
}
