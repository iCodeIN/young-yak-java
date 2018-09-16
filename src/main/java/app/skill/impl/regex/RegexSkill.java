package app.skill.impl.regex;

import app.skill.DefaultSkillImpl;

import java.util.Arrays;
import java.util.regex.Pattern;

public class RegexSkill extends DefaultSkillImpl {

    public RegexSkill(Iterable<Pattern> patterns, Iterable<String> replies) {
        addRequestHandler(new RegexRequestHandler(patterns, replies));
    }

    public RegexSkill(Pattern[] patterns, String[] replies) {
        addRequestHandler(new RegexRequestHandler(Arrays.asList(patterns), Arrays.asList(replies)));
    }

}
