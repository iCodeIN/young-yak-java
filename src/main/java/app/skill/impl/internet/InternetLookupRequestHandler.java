package app.skill.impl.internet;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.impl.regex.RegexRequestHandler;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This ISkill matches a collection of regular expressions, extracts an attribute
 * and then calls the abstract function lookup with the first matched group
 */
public abstract class InternetLookupRequestHandler extends RegexRequestHandler {

    private Pattern[] PATTERNS;

    public InternetLookupRequestHandler(Pattern[] patterns) {
        super(Arrays.asList(patterns));
        PATTERNS = patterns;
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        // find keyword
        String txt = input.getContent().toString();
        String keyword = "";
        for (Pattern p : PATTERNS) {
            Matcher m = p.matcher(txt);
            if (m.matches()) {
                if (m.groupCount() > 0)
                    keyword = m.group(1);
                break;
            }
        }

        // lookup
        try {
            String reply = lookup(keyword);
            return (reply == null || reply.isEmpty())
                    ? Optional.empty()
                    : Optional.of(new HandlerResponseImpl(reply, new String[]{this.getClass().getName()}));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * This method is called to generate the output.
     * The text returned in this method is wrapped in a HandlerResponseImpl object before being returned.
     *
     * @param q the first group captured in the regular expression that was matched
     * @return
     * @throws IOException
     * @throws Exception
     */
    public abstract String lookup(String q) throws IOException, Exception;

}
