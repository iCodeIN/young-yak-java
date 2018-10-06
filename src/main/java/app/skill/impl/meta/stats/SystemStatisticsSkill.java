package app.skill.impl.meta.stats;

import app.skill.DefaultSkillImpl;
import app.web.BotController;

public class SystemStatisticsSkill extends DefaultSkillImpl {

    public SystemStatisticsSkill(BotController botController) {
        addRequestHandler(new CountUsersHandler(botController));
        addRequestHandler(new ListSkillsHandler(botController));
        addRequestHandler(new CountLogsHandler(botController));
        addRequestHandler(new CountPersonalLogsHandler(botController));
        addRequestHandler(new CountSkillInvocationHandler(botController));
    }
}
