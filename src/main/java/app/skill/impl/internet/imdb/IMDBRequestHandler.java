package app.skill.impl.internet.imdb;

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

public class IMDBRequestHandler extends InternetLookupRequestHandler {

    private static String API_KEY = "caee14d8ab9e074ae59f4e85e83662fa";
    private static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

    private static Pattern[] PATTERNS = {

            Pattern.compile("THE MOVIE ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LOOKUP THE MOVIE ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LOOK UP MOVIE ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("SEARCH THE MOVIE ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND THE MOVIE ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("DO YOU KNOW THE MOVIE ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DO YOU KNOW THE ([A-Z ]+) MOVIE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HAVE YOU HEARD OF THE MOVIE ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HAVE YOU HEARD OF THE ([A-Z ]+) MOVIE", Pattern.CASE_INSENSITIVE),

            Pattern.compile("TELL ME ABOUT THE MOVIE ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("TELL ME ABOUT THE ([A-Z ]+) MOVIE", Pattern.CASE_INSENSITIVE)
    };

    public IMDBRequestHandler() {
        super(PATTERNS);
    }

    public String lookup(String q) throws Exception {
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + URLEncoder.encode(q);

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

        JSONObject object = new JSONObject(result.toString()).getJSONArray("results").getJSONObject(0);

        String plot = object.get("overview").toString();
        String year = object.getString("release_date");
        String title = object.getString("original_title");

        return "<table>" +
                "   <tr>" +
                "       <td>Title</td>" +
                "       <td>" + title + "</td>" +
                "   </tr>" +

                "   <tr>" +
                "       <td>Year</td>" +
                "       <td>" + year + "</td>" +
                "   </tr>" +

                "   <tr>" +
                "       <td>Plot</td>" +
                "       <td>" + plot + "</td>" +
                "   </tr>" +
                "</table>";
    }

}
