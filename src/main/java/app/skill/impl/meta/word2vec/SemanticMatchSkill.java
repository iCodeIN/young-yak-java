package app.skill.impl.meta.word2vec;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.skill.ISkill;

public class SemanticMatchSkill implements ISkill {

    @Override
    public boolean canHandle(IHandlerInput input) {
        return false;
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        return null;
    }
}
