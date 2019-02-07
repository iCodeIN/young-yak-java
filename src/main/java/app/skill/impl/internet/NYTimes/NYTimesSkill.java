package app.skill.impl.internet.NYTimes;

import app.skill.DefaultSkillImpl;

public class NYTimesSkill extends DefaultSkillImpl {

    public NYTimesSkill(){
        addRequestHandler(new NYTimesRequestHandler());
    }
}
