package app.bot;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.Status;
import app.handler.impl.HandlerInputImpl;
import app.handler.impl.HandlerResponseImpl;
import app.skill.ISkill;

import java.util.*;

/**
 * Default implementation of IBot interface
 */
public class DefaultBotImpl implements IBot {

    List<ISkill> skillList = new ArrayList<>();

    /**
     * Add an ISkill to this IBot
     *
     * @param skill the ISkill to be added
     * @return this object
     */
    public DefaultBotImpl addSkill(ISkill skill) {
        skillList.add(skill);
        return this;
    }

    /**
     * Removes an ISkill from this IBot
     *
     * @param skill the ISkill to be removed
     * @return this object
     */
    public DefaultBotImpl removeSkill(ISkill skill) {
        skillList.remove(skill);
        return this;
    }

    /**
     * Get all the ISkills registered for this IBot
     *
     * @return a collection of ISkill objects registered to this IBot
     */
    public Collection<ISkill> getSkills() {
        return skillList;
    }

    @Override
    public Optional<IHandlerResponse> respond(IHandlerInput input) {
        /* Just to ensure we are not going to deep in the recursion
         * we explictly count the number of StackTraceElements that match the current method
         */
        int d = 0;
        for (StackTraceElement el : Thread.currentThread().getStackTrace()) {
            if (el.getClassName().equals(DefaultBotImpl.class.getName()) && el.getMethodName().equals("respond"))
                d++;
        }
        // recursion
        return respond(input, 16 - d);
    }

    public Optional<IHandlerResponse> respond(IHandlerInput input, int maxDepth) {

        List<ResponseStackElement> tmp = new ArrayList<>();
        tmp.add(new ResponseStackElement(input.getContent().toString(), ""));

        IHandlerResponse response = respond(tmp, input.getUserID(), maxDepth);

        return response == null ? Optional.empty() : Optional.ofNullable(response);
    }

    /**
     * Recursive method that iteratively looks through the graph of all applicable reductions
     *
     * @param stk      the stack of reductions so far
     * @param userID   the userID that invoked the response
     * @param maxDepth the maximal depth to look for through the graph
     * @return
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
                stk.add(new ResponseStackElement(response.getContent().toString(), skill.getClass().getName()));
                IHandlerResponse finalResponse = respond(stk, userID, maxDepth);
                stk.remove(stk.size() - 1);
                if (finalResponse != null) {
                    List<String> invokedSkills = new ArrayList<>();
                    invokedSkills.addAll(Arrays.asList(response.getInvokedSkills()));
                    invokedSkills.addAll(Arrays.asList(finalResponse.getInvokedSkills()));
                    return new HandlerResponseImpl(finalResponse.getContent().toString(), invokedSkills.toArray(new String[]{}));
                }
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
