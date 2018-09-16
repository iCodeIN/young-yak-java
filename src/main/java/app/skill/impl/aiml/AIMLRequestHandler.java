package app.skill.impl.aiml;

import app.bot.IBot;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.IRequestHandler;
import app.handler.Status;
import app.handler.impl.HandlerInputImpl;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This IRequestHandler deals with AIML.<br>
 * AIML, or Artificial Intelligence Markup Language, is an XML dialect for creating natural language software agents.<br>
 * The XML dialect called AIML was developed by Richard Wallace and a worldwide free software community between 1995 and 2002. <br>
 * AIML formed the basis for what was initially a highly extended Eliza called "A.L.I.C.E.." ("Artificial Linguistic Internet Computer Entity"), <br>
 * which won the annual Loebner Prize Competition in Artificial Intelligence three times, and was also the Chatterbox Challenge Champion in 2004.
 */
public class AIMLRequestHandler implements IRequestHandler {

    private static Random RANDOM = new Random(System.currentTimeMillis());
    private final IBot bot;
    private List<Element> categories = new ArrayList<>();

    public AIMLRequestHandler(IBot bot, InputStream aimlInputStream) {
        this.bot = bot;
        try {
            readAIML(aimlInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void readAIML(InputStream is) throws JDOMException, IOException {
        Element root = new SAXBuilder().build(is).getRootElement();
        for (Element categoryElement : root.getChildren()) {
            if (categoryElement.getName().equals("category"))
                categories.add(categoryElement);
        }
    }

    @Override
    public boolean canHandle(IHandlerInput input) {
        for (Element e : categories)
            if (canHandle(input, e))
                return true;
        return false;
    }

    private boolean canHandle(IHandlerInput input, Element element) {
        String txt = input.getContent().toString();

        // must have pattern element
        Element patternElement = child(element, "pattern");
        if (patternElement == null)
            return false;

        // must have matching pattern element
        try {
            Pattern pattern = Pattern.compile(patternElement.getText(), Pattern.CASE_INSENSITIVE);
            if (!pattern.matcher(txt).matches())
                return false;
        }catch (PatternSyntaxException ex){ return false; }

        // must output something
        Element templateElement = child(element, "template");
        if (templateElement == null)
            return false;

        // default
        return true;
    }

    private Element child(Element e, String name) {
        for (Element c : e.getChildren())
            if (c.getName().equals(name))
                return c;
        return null;
    }

    @Override
    public Optional<IHandlerResponse> handle(IHandlerInput input) {
        for (Element e : categories) {
            if (canHandle(input, e))
                return handle(input, e);
        }
        return Optional.empty();
    }

    private Optional<IHandlerResponse> handle(IHandlerInput input, Element element) {
        String txt = input.getContent().toString();

        Pattern pattern = Pattern.compile(child(element, "pattern").getText(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        matcher.matches();

        String response = buildResponse(matcher, element, input.getUserID());
        return response.isEmpty() ? Optional.empty() : Optional.ofNullable(new IHandlerResponse() {
            @Override
            public Status getStatus() {
                return Status.STATUS_200_OK;
            }

            @Override
            public Object getContent() {
                return response;
            }
        });

    }

    private String buildResponse(Matcher matcher, Element root, String userID) {
        // category
        if (root.getName().equals("category"))
            return buildResponse(matcher, child(root, "template"), userID);

        // <li> element without children
        if (root.getName().equals("li") && root.getChildren().isEmpty()) {
            return replaceGroups(matcher, root.getText());
        }

        // <template> element without children
        if (root.getName().equals("template") && root.getChildren().isEmpty()) {
            return replaceGroups(matcher, root.getText());
        }

        // <redirect>
        if (root.getName().equals("redirect") && root.getChildren().isEmpty()) {
            IHandlerInput tmpA = new HandlerInputImpl(replaceGroups(matcher, root.getText()), userID);
            Optional<IHandlerResponse> tmpB = bot.respond(tmpA);
            return tmpB.isPresent() ? tmpB.get().getContent().toString() : "";
        }

        // template
        if (root.getName().equals("template") || root.getName().equals("li")) {
            String out = "";
            for (Element c : root.getChildren())
                out += buildResponse(matcher, c, userID);
            return out;
        }

        // random
        if (root.getName().equals("random")) {
            List<Element> cs = root.getChildren();
            return buildResponse(matcher, cs.get(RANDOM.nextInt(cs.size())), userID);
        }

        // default
        return "";
    }

    private String replaceGroups(Matcher matcher, String s) {
        String out = s;
        for (int i = 0; i <= matcher.groupCount(); i++) {
            String key = "\\" + i;
            String val = matcher.group(i);
            while (out.contains(key))
                out = out.replace(key, val);
        }
        return out;
    }

}