package app.skill.impl.eval.math;

import app.skill.DefaultSkillImpl;

public class MathSkill extends DefaultSkillImpl {

    public MathSkill(){
        addRequestHandler(new MathRequestHandler());
    }

}
