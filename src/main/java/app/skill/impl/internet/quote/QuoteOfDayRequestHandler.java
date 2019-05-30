package app.skill.impl.internet.quote;

import app.skill.impl.internet.InternetLookupRequestHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class QuoteOfDayRequestHandler extends InternetLookupRequestHandler {

    private static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

    private static Pattern[] PATTERNS = {
            Pattern.compile("WHAT IS THE QUOTE OF TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE QUOTE FOR TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE QUOTE OF THE DAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE QUOTE FOR THE DAY", Pattern.CASE_INSENSITIVE),

            Pattern.compile("WHAT IS MY QUOTE OF TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS MY QUOTE FOR TODAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS MY QUOTE OF THE DAY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS MY QUOTE FOR THE DAY", Pattern.CASE_INSENSITIVE),
    };

    public QuoteOfDayRequestHandler() {
        super(PATTERNS);
    }

    @Override
    public String lookup(String q) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://quotes.rest/qod");

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Accept","application/json");
        HttpResponse response = client.execute(request);

        // build result
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        JSONObject object = new JSONObject(result.toString());

        // null checks
        if(!object.has("contents"))
            return null;
        if(!object.getJSONObject("contents").has("quotes"))
            return null;

        // get quote object
        JSONObject quoteObject = object.getJSONObject("contents").getJSONArray("quotes").getJSONObject(0);

        // return
        return quoteObject.getString("quote") + " - " + quoteObject.getString("author");
    }
}
