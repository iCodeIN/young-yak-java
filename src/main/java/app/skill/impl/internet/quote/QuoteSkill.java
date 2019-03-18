package app.skill.impl.internet.quote;

import app.skill.DefaultSkillImpl;

public class QuoteSkill extends DefaultSkillImpl {

    public QuoteSkill(){
        addRequestHandler(new QuoteOfDayRequestHandler());
    }

}
