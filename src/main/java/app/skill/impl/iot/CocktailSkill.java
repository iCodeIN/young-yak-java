package app.skill.impl.iot;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CocktailSkill extends AbstractLookupSkill {

    private static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

    private static Pattern[] PATTERNS = {
            Pattern.compile("HOW DO YOU MAKE A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW DO YOU MAKE AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("HOW DO YOU MIX A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("HOW DO YOU MIX AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE),

            Pattern.compile("WHAT IS THE RECIPE FOR A ([A-Z ]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHAT IS THE RECIPE FOR AN ([A-Z ]+)", Pattern.CASE_INSENSITIVE)
    };

    public CocktailSkill(){
        super(PATTERNS);
    }

    @Override
    public String lookup(String q) throws IOException, Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + URLEncoder.encode(q));

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

        JSONArray object = new JSONObject(result.toString()).getJSONArray("drinks");
        if(object.isEmpty())
            return null;

        JSONObject drinkObject = object.getJSONObject(0);

        List<String[]> ingredients = new ArrayList<>();
        for(int i=0;i<20;i++){
            String t0 = drinkObject.has("strIngredient" + i) ? drinkObject.getString("strIngredient" + i) : "";
            String t1 = drinkObject.has("strMeasure" + i) ? drinkObject.getString("strMeasure" + i) : "";
            if(t0.isEmpty() || t1.isEmpty())
                continue;
            ingredients.add(new String[]{t0, t1});
        }

        String out = "<ul>";
        for (int i = 0; i < ingredients.size(); i++)
            out += ("<li>" + ingredients.get(i)[0] + " (" + ingredients.get(i)[1].trim() + ")</li>");
        out += "</ul>";
        out += drinkObject.getString("strInstructions");
        return out;

    }
}
