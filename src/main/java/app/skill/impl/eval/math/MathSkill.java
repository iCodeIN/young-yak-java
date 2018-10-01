package app.skill.impl.eval.math;

import app.skill.DefaultSkillImpl;

/**
 * This ISkill deals with math problems
 */
public class MathSkill extends DefaultSkillImpl {

    public MathSkill() {
        addRequestHandler(new MathRequestHandler());
    }

}
