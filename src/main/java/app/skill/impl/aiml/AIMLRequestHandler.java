package app.skill.impl.aiml;

import app.bot.IBot;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.IRequestHandler;
import app.handler.impl.HandlerInputImpl;
import app.handler.impl.HandlerResponseImpl;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class AIMLRequestHandler implements IRequestHandler {

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

    /**
     * Reads the given inputstream and parses the XML as AIML
     *
     * @param is the inputstream representing the AIML document
     * @throws JDOMException
     * @throws IOException
     */
    private void readAIML(InputStream is) throws JDOMException, IOException {
        Element root = new SAXBuilder().build(is).getRootElement();
        for (Element categoryElement : root.getChildren()) {
            // category
            if (categoryElement.getName().equals("category")) {
                categories.add(categoryElement);
            }
            // import
            else if(categoryElement.getName().equals("import")){
                String url = categoryElement.getAttributeValue("src");
                try {
                    readAIML(new URI(url).toURL().openStream());
                    System.out.println(String.format("Loading external AIML file : %s", url));
                } catch (URISyntaxException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public boolean canHandle(IHandlerInput input) {
        for (Element e : categories)
            if (canHandle(input, e))
                return true;
        return false;
    }

    /**
     * Checks whether a given XML Element can process the given IHandlerInput
     *
     * @param input   the IHandlerInput
     * @param element the XML Element
     * @return true if the XML Element represents an AIML pattern that matches the IHandlerInput
     */
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
        } catch (PatternSyntaxException ex) {
            return false;
        }

        // must output something
        Element templateElement = child(element, "template");
        return templateElement != null;
    }

    /**
     * This function fetches the first Element of an XML Element with given name
     *
     * @param e    parent Element
     * @param name name of the child Element to fetch
     * @return
     */
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

        List<String> invokedSkills = new ArrayList<>();
        invokedSkills.add(this.getClass().getName());

        // build response
        String response = buildResponse(matcher, element, invokedSkills, input.getUserID());

        return response == null ? Optional.empty() : Optional.ofNullable(new HandlerResponseImpl(response, invokedSkills.toArray(new String[]{})));

    }

    private String buildResponse(Matcher matcher, Element root, List<String> invokedSkills, String userID) {
        // category
        if (root.getName().equals("category"))
            return buildResponse(matcher, child(root, "template"), invokedSkills, userID);

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
            if (tmpB.isPresent()) {
                IHandlerResponse tmpC = tmpB.get();
                invokedSkills.addAll(Arrays.asList(tmpC.getInvokedSkills()));
                return tmpC.getContent().toString();
            }
            return "";
        }

        // template
        if (root.getName().equals("template") || root.getName().equals("li")) {
            String out = "";
            for (Element c : root.getChildren())
                out += buildResponse(matcher, c, invokedSkills, userID);
            return out;
        }

        // random
        if (root.getName().equals("random")) {
            List<Element> cs = root.getChildren();
            return buildResponse(matcher, cs.get(RANDOM.nextInt(cs.size())), invokedSkills, userID);
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