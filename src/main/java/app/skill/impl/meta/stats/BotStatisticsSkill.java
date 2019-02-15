package app.skill.impl.meta.stats;

import app.controller.IBotController;
import app.skill.DefaultSkillImpl;

public class BotStatisticsSkill extends DefaultSkillImpl {

    public BotStatisticsSkill(IBotController botController) {
        addRequestHandler(new CountUsersRequestHandler(botController));
        addRequestHandler(new ListSkillsRequestHandler(botController));
        addRequestHandler(new CountLogsRequestHandler(botController));
        addRequestHandler(new CountPersonalLogsRequestHandler(botController));
        addRequestHandler(new CountSkillInvocationRequestHandler(botController));
    }
}
