package app.skill.impl.iot;

import app.skill.impl.iot.AbstractLookupSkill;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Random;
import java.util.regex.Pattern;

public class SoundCloudSkill extends AbstractLookupSkill {

    private static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36";

    private static Pattern[] PATTERNS = {
            Pattern.compile("THE SONG ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LOOKUP THE SONG ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LOOK UP SONG ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("SEARCH THE SONG ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND THE SONG ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("DO YOU KNOW THE SONG ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DO YOU KNOW THE ([A-Z ]+) SONG", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HAVE YOU HEARD OF THE SONG ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HAVE YOU HEARD OF THE ([A-Z ]+) SONG", Pattern.CASE_INSENSITIVE),

            Pattern.compile("THE ARTIST ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LOOKUP THE ARTIST ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("LOOK UP ARTIST ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("SEARCH THE ARTIST ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND THE ARTIST ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("DO YOU KNOW THE ARTIST ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HAVE YOU HEARD OF THE ARTIST ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
    };

    private static Random RANDOM = new Random(System.currentTimeMillis());

    private static String[] REPLIES = {
            "I found this on SoundCloud<br><a href='%s'>&#9654;</a>",
            "I may have found something on SoundCloud<br><a href='%s'>&#9654;</a>",
            "Found this on SoundCloud<br><a href='%s'>&#9654;</a>",
            "I got this from SoundCloud<br><a href='%s'>&#9654;</a>",
    };

    public SoundCloudSkill() {
        super(PATTERNS);
    }

    public String lookup(String q) throws IOException {
        String url = "https://soundcloud.com/search?q=" + URLEncoder.encode(q);

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

        String link = "";
        Document doc = Jsoup.parse(result.toString());
        for(Element e : doc.getElementsByTag("body").get(0).getElementsByTag("a")){
           String href = e.hasAttr("href") ? e.attr("href") : "";
           if(href.matches("/search.*"))
               continue;
           if(href.matches("/[a-zA-Z- ]+/[a-zA-Z- ]+")){
               link = "https://soundcloud.com" + href;
               break;
           }
        }

        if(!link.isEmpty()){
            return String.format(REPLIES[RANDOM.nextInt(REPLIES.length)], link);
        }

        // return
        return null;
    }
}
