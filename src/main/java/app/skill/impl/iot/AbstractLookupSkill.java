package app.skill.impl.iot;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexSkill;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractLookupSkill extends RegexSkill {

    private Pattern[] PATTERNS;

    public AbstractLookupSkill(Pattern[] patterns) {
        super(patterns, new String[]{});
        PATTERNS = patterns;
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        // find keyword
        String txt = input.getContent().toString();
        String keyword = "";
        for (Pattern p : PATTERNS) {
            Matcher m = p.matcher(txt);
            if (m.matches()) {
                if(m.groupCount() > 0)
                    keyword = m.group(1);
                break;
            }
        }

        // lookup
        try {
            String reply = lookup(keyword);
            return (reply == null || reply.isEmpty()) ? null : new HandlerResponseImpl(reply);
        } catch (Exception e) {
            return null;
        }
    }

    public abstract String lookup(String q) throws IOException, Exception;

}
