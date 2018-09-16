package app.bot;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.Status;
import app.handler.impl.HandlerInputImpl;
import app.skill.ISkill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DefaultBotImpl implements IBot {

    List<ISkill> skillList = new ArrayList<>();


    public DefaultBotImpl addSkill(ISkill skill) {
        skillList.add(skill);
        return this;
    }

    public DefaultBotImpl removeSkill(ISkill skill) {
        skillList.remove(skill);
        return this;
    }

    public Collection<ISkill> getSkills() {
        return skillList;
    }

    @Override
    public Optional<IHandlerResponse> respond(IHandlerInput input) {

        List<ResponseStackElement> tmp = new ArrayList<>();
        tmp.add(new ResponseStackElement(input.getContent().toString(), ""));

        IHandlerResponse response = respond(tmp, input.getUserID(), 32);

        return response == null ? Optional.empty() : Optional.ofNullable(response);
    }

    /*
     * recursive algorithm ahead
     */

    private IHandlerResponse respond(List<ResponseStackElement> stk, String userID, int maxDepth) {
        // response is taking too long to build
        if (stk.size() >= maxDepth) {
            return null;
        }

        // circular logic
        for (int i = 0; i < stk.size(); i++) {
            for (int j = i + 1; j < stk.size(); j++) {
                if (stk.get(i).equals(stk.get(j))) {
                    return null;
                }
            }
        }

        ResponseStackElement last = stk.get(stk.size() - 1);
        IHandlerInput input = new HandlerInputImpl(last.txt, userID);

        // check which skills can handle this input
        List<ISkill> skillsCapableOfHandlingInput = new ArrayList<>();
        for (ISkill skill : skillList) {
            if (skill.canHandle(input)) {
                skillsCapableOfHandlingInput.add(skill);
            }
        }

        // no skills capable of handling this input
        if (skillsCapableOfHandlingInput.isEmpty()) {
            return null;
        }

        for (ISkill skill : skillsCapableOfHandlingInput) {

            // produce output with this app.skill
            IHandlerResponse response = skill.invoke(input);
            if (response == null)
                continue;

            // final (non-redirecting) output
            if (response.getStatus() == Status.STATUS_200_OK)
                return response;

            // recursion
            if (response.getStatus() == Status.STATUS_303_SEE_OTHER) {
                ResponseStackElement next = new ResponseStackElement(response.getContent().toString(), skill.getClass().getName());
                stk.add(next);
                response = respond(stk, userID, maxDepth);
                stk.remove(stk.size() - 1);
                if (response != null)
                    return response;
            }
        }

        // default
        return null;
    }

    class ResponseStackElement {
        String txt;
        String skillName;

        public ResponseStackElement(String txt, String skillName) {
            this.txt = txt;
            this.skillName = skillName;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ResponseStackElement) {
                ResponseStackElement other = (ResponseStackElement) o;
                return other.txt.equals(txt) && other.skillName.equals(skillName);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int h = 0;
            h += (txt.hashCode() * 97) % 997;
            h += (skillName.hashCode() * 97) % 997;
            return h;
        }
    }
}
