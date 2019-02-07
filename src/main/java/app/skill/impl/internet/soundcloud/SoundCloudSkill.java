package app.skill.impl.internet.soundcloud;

import app.skill.DefaultSkillImpl;

public class SoundCloudSkill extends DefaultSkillImpl {

    public SoundCloudSkill(){
        addRequestHandler(new SoundCloudRequestHandler());
    }
}
