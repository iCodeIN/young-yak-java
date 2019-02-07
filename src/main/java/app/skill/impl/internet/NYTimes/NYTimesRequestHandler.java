package app.skill.impl.internet.NYTimes;

import app.skill.impl.internet.InternetLookupRequestHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This ISkill deals with current news inquiries.
 */
public class NYTimesRequestHandler extends InternetLookupRequestHandler {

    private static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

    private static Pattern[] PATTERNS = {
            Pattern.compile("WHAT IS ON THE NEWS", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS ON THE NEWS TODAY", Pattern.CASE_INSENSITIVE),

            Pattern.compile("WHAT IS IN THE NEWS", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS IN THE NEWS TODAY", Pattern.CASE_INSENSITIVE),

            Pattern.compile("WHAT IS IN THE PAPERS", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS IN THE PAPERS TODAY", Pattern.CASE_INSENSITIVE)
    };

    public NYTimesRequestHandler() {
        super(PATTERNS);
    }

    @Override
    public String lookup(String q) throws Exception {
        String url = "http://rss.nytimes.com/services/xml/rss/nyt/World.xml";

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        // build result
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        List<String> headlines = new ArrayList<>();
        Element rootElement = new SAXBuilder().build(new ByteArrayInputStream(result.toString().getBytes())).getRootElement();
        for (Element channelElement : rootElement.getChildren()) {
            if (!channelElement.getName().equals("channel"))
                continue;
            for (Element itemElement : channelElement.getChildren()) {
                if (!itemElement.getName().equals("item"))
                    continue;
                Element titleElement = child(itemElement, "title");
                if (titleElement != null)
                    headlines.add(titleElement.getText());
            }
        }

        String out = "<ul>";
        for (int i = 0; i < java.lang.Math.min(10, headlines.size()); i++)
            out += ("<li>" + headlines.get(i) + "</li>");
        out += "</ul>";
        return out;
    }

    private Element child(Element e, String name) {
        for (Element c : e.getChildren()) {
            if (c.getName().equals(name))
                return c;
        }
        return null;
    }
}