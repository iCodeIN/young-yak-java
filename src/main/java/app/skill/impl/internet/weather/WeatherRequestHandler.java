package app.skill.impl.internet.weather;

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
 * This ISkill deals with weather inquiries.
 */
public class WeatherRequestHandler extends InternetLookupRequestHandler {

    private static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

    private static Pattern[] PATTERNS = {
            Pattern.compile("WHAT IS THE WEATHER IN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE WEATHER LIKE IN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("WHAT IS THE CURRENT WEATHER IN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE CURRENT WEATHER LIKE IN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("WHAT IS TODAYS WEATHER IN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS TODAYS WEATHER LIKE IN ([A-Z ]+)", Pattern.CASE_INSENSITIVE)
    };
    private String API_KEY = "fa9cb4a1bbe7769a5acd1e6a1ee00f27";

    public WeatherRequestHandler() {
        super(PATTERNS);
    }

    @Override
    public String lookup(String q) throws Exception {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(q) + "&appid=" + API_KEY + "&mode=json";

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

        JSONObject object = new JSONObject(result.toString());

        double avgTemp = object.getJSONObject("main").getDouble("temp");
        double minTemp = object.getJSONObject("main").getDouble("temp_min");
        double maxTemp = object.getJSONObject("main").getDouble("temp_max");
        int humidity = object.getJSONObject("main").getInt("humidity");
        int pressure = object.getJSONObject("main").getInt("pressure");
        //String description = object.getJSONObject("weather").getString("description");

        return "<pre>\uD83C\uDF21️\tavg  : " + centigrade(avgTemp) + "\n"
                + "  \tmin  : " + centigrade(minTemp) + "\n"
                + "  \tmax  : " + centigrade(maxTemp) + "\n\n"
                + "\uD83C\uDF27️\t(%)  : " + humidity + "\n\n"
                + "\uD83C\uDF2A️\t(mb) : " + pressure
                + "</pre>";

    }

    private double centigrade(double kelvin) {
        return java.lang.Math.round((kelvin - 273.15) * 10.0) / 10.0;
    }
}
