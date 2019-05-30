package app.skill.impl.internet.duckduckgo;

import app.skill.impl.internet.InternetLookupRequestHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * This ISkill redirects incoming input to the duck duck go search engine.<br>
 * Questions must match:<br>
 * <ul>
 * <li>WHO IS [A-Z]+</li>
 * <li>WHO WAS [A-Z]+</li>
 * <li>WHAT IS [A-Z]+</li>
 * <li>WHAT WAS [A-Z]+</li>
 * </ul>
 */
public class DuckDuckGoRequestHandler extends InternetLookupRequestHandler {

    private static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

    private static Pattern[] PATTERNS = {
            Pattern.compile("WHO IS ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHO WAS ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT WAS ([A-Z ]+)", Pattern.CASE_INSENSITIVE)
    };

    public DuckDuckGoRequestHandler() {
        super(PATTERNS);
    }

    public String lookup(String q) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://api.duckduckgo.com/?q=" + URLEncoder.encode(q) + "+&format=json&pretty=1");

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

        JSONObject object = new JSONObject(result.toString());
        if (object.has("Abstract") && !object.getString("Abstract").isEmpty())
            return object.getString("Abstract");
        if(object.has("RelatedTopics"))
            return object.getJSONArray("RelatedTopics").getJSONObject(0).getString("Text");
        // default
        return "";
    }

}
