package app.skill.impl.internet.weather;

import app.skill.DefaultSkillImpl;

public class WeatherSkill extends DefaultSkillImpl {

    public WeatherSkill(){
        addRequestHandler(new WeatherRequestHandler());
    }
}
