package app.skill.impl.internet.duckduckgo;

import app.skill.DefaultSkillImpl;

public class DuckDuckGoSkill extends DefaultSkillImpl {

    public DuckDuckGoSkill(){
        addRequestHandler(new DuckDuckGoRequestHandler());
    }
}
