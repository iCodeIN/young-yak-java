package app.skill.impl.internet.xkcd;

import app.skill.impl.internet.InternetLookupRequestHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class XkcdByNumberRequestHandler  extends InternetLookupRequestHandler {

    private static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

    private static Pattern[] PATTERNS = {

            Pattern.compile("FIND XKCD ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND XKCD NUMBER ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND XKCD NR ([0-9]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("FETCH XKCD ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FETCH XKCD NUMBER ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FETCH XKCD NR ([0-9]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("SHOW XKCD ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("SHOW XKCD NUMBER ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("SHOW XKCD NR ([0-9]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("DISPLAY XKCD ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DISPLAY XKCD NUMBER ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DISPLAY XKCD NR ([0-9]+)", Pattern.CASE_INSENSITIVE),
            
            Pattern.compile("XKCD ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("XKCD NUMBER ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("XKCD NR ([0-9]+)", Pattern.CASE_INSENSITIVE),

    };

    public XkcdByNumberRequestHandler() {
        super(PATTERNS);
    }

    public String lookup(String q) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://xkcd.com/" + q + "/info.0.json");

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

        JSONObject doc = new JSONObject(result.toString());
        return String.format("<img src='%s' width='300px' height='300px' />", doc.getString("img"));
    }
}
