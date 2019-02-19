package app.skill.impl.internet.xkcd;

import app.skill.DefaultSkillImpl;

public class XkcdSkill extends DefaultSkillImpl {

    public XkcdSkill(){
        addRequestHandler(new XkcdByNumberRequestHandler());
        /*
        addRequestHandler(new XkcdByKeywordRequestHandler());
        addRequestHandler(new XkcdByTitleRequestHandler());
        */
    }
}
