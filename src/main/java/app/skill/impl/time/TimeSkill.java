package app.skill.impl.time;

import app.skill.DefaultSkillImpl;

public class TimeSkill extends DefaultSkillImpl {

    public TimeSkill() {
        addRequestHandler(new DateHandler());
        addRequestHandler(new DayOfWeekHandler());
        addRequestHandler(new TimeOfDayHandler());
    }
}
