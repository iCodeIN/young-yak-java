package app.skill.impl.meta.stats;

import app.skill.DefaultSkillImpl;
import app.web.BotController;

public class BotStatisticsSkill extends DefaultSkillImpl {

    public BotStatisticsSkill(BotController botController) {
        addRequestHandler(new CountUsersRequestHandler(botController));
        addRequestHandler(new ListSkillsRequestHandler(botController));
        addRequestHandler(new CountLogsRequestHandler(botController));
        addRequestHandler(new CountPersonalLogsRequestHandler(botController));
        addRequestHandler(new CountSkillInvocationRequestHandler(botController));
    }
}
