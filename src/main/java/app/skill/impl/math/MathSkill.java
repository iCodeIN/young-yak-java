package app.skill.impl.math;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.DefaultSkillImpl;
import app.skill.ISkill;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathSkill extends DefaultSkillImpl {

    public MathSkill(){
        addRequestHandler(new MathSkillRequestHandler());
    }

}
