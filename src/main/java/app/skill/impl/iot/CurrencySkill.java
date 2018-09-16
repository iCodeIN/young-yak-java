package app.skill.impl.iot;

import app.skill.DefaultSkillImpl;

import java.util.regex.Pattern;

public class CurrencySkill extends DefaultSkillImpl {

    private static Pattern[] PATTERNS = {
            Pattern.compile("WHO IS ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHO WAS ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT WAS ([A-Z ]+)", Pattern.CASE_INSENSITIVE)
    };
}