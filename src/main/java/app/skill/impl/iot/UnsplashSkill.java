package app.skill.impl.iot;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This ISkill deals with picture/image inquiries.
 */
public class UnsplashSkill extends AbstractLookupSkill {

    private static Random RANDOM = new Random(System.currentTimeMillis());
    private static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

    private static Pattern[] PATTERNS = {

            // 1st variant

            Pattern.compile("FIND AN IMAGE OF A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND A PICTURE OF A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND A PHOTO OF A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("FIND AN IMAGE OF AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND A PICTURE OF AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND A PHOTO OF AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("FIND AN IMAGE OF ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND A PICTURE OF ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FIND A PHOTO OF ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            // 2nd variant

            Pattern.compile("CAN YOU FIND AN IMAGE OF A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("CAN YOU FIND A PICTURE OF A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("CAN YOU FIND A PHOTO OF A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("CAN YOU FIND AN IMAGE OF AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("CAN YOU FIND A PICTURE OF AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("CAN YOU FIND A PHOTO OF AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("CAN YOU FIND AN IMAGE OF ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("CAN YOU FIND A PICTURE OF ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("CAN YOU FIND A PHOTO OF ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            // 3rd variant

            Pattern.compile("DO YOU HAVE AN IMAGE OF A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DO YOU HAVE A PICTURE OF A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DO YOU HAVE A PHOTO OF A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("DO YOU HAVE AN IMAGE OF AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DO YOU HAVE A PICTURE OF AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DO YOU HAVE A PHOTO OF AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("DO YOU HAVE AN IMAGE OF ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DO YOU HAVE A PICTURE OF ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("DO YOU HAVE A PHOTO OF ([A-Z ]+)", Pattern.CASE_INSENSITIVE)
    };

    public UnsplashSkill() {
        super(PATTERNS);
    }

    public String lookup(String q) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://unsplash.com/search/photos/" + URLEncoder.encode(q));

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

        Document doc = Jsoup.parse(result.toString());

        Map<String, Integer> cssClassCount = new HashMap<>();
        for (Element imgElement : doc.getElementsByTag("img")) {
            String url = imgElement.attr("src");
            if (url.contains("profile"))
                continue;
            if(url.isEmpty())
                continue;
            for (String className : imgElement.classNames()) {
                if (cssClassCount.containsKey(className))
                    cssClassCount.put(className, cssClassCount.get(className) + 1);
                else
                    cssClassCount.put(className, 1);
            }
        }
        Map.Entry<String, Integer> topEntry = null;
        for (Map.Entry<String, Integer> entry : cssClassCount.entrySet()) {
            if (topEntry == null || topEntry.getValue() < entry.getValue())
                topEntry = entry;
        }

        List<String> imgURLs = new ArrayList<>();
        for (Element imgElement : doc.getElementsByTag("img")) {
            if(imgElement.attr("src").isEmpty())
                continue;
            if (!imgElement.classNames().contains(topEntry.getKey()))
                continue;
            imgURLs.add(imgElement.attr("src"));
        }

        int N = RANDOM.nextInt(java.lang.Math.min(5, imgURLs.size()));
        return String.format("<img src='%s' width='300px' height='300px' />", imgURLs.get(N));
    }

}
