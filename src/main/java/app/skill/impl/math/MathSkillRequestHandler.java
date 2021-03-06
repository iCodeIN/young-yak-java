package app.skill.impl.math;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.IRequestHandler;
import app.handler.impl.HandlerResponseImpl;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathSkillRequestHandler implements IRequestHandler {

    private static Pattern MATH_EXPRESSION = Pattern.compile("[0-9\\+\\-\\*\\/ ]+");

    @Override
    public boolean canHandle(IHandlerInput input) {
        String txt = input.getContent().toString();
        Matcher matcher = MATH_EXPRESSION.matcher(txt);
        return matcher.matches();
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        // input
        String txt = input.getContent().toString();

        // script engine
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

        // try to evaluate
        try {
            String out = scriptEngine.eval(txt).toString();
            return Optional.of(new HandlerResponseImpl(out, new String[]{this.getClass().getName()}));
        } catch (ScriptException e) {
            return null;
        }
    }
}
