package app.skill.impl.internet.unsplash;

import app.skill.DefaultSkillImpl;

public class UnsplashSkill extends DefaultSkillImpl {

    public UnsplashSkill(){
        addRequestHandler(new UnsplashRequestHandler());
    }
}
