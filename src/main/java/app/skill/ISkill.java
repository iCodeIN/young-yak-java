package app.skill;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;

public interface ISkill {

    boolean canHandle(IHandlerInput input);

    IHandlerResponse invoke(IHandlerInput input);

}
