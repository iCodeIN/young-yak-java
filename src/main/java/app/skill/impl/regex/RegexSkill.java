package app.skill.impl.regex;

import app.skill.DefaultSkillImpl;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * This ISkill represents a generic ISkill that matches regular expressions as input, and randomly outputs a reply.
 */
public class RegexSkill extends DefaultSkillImpl {

    /**
     * @param patterns
     * @param replies
     */
    public RegexSkill(Iterable<Pattern> patterns, Iterable<String> replies) {
        addRequestHandler(new RegexRequestHandler(patterns, replies));
    }

    /**
     * @param patterns
     * @param replies
     */
    public RegexSkill(Pattern[] patterns, String[] replies) {
        addRequestHandler(new RegexRequestHandler(Arrays.asList(patterns), Arrays.asList(replies)));
    }

}
