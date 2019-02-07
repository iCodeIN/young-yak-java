package app.skill.impl.internet.imdb;

import app.skill.DefaultSkillImpl;
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
 * This ISkill deals with movie inquiries.
 */
public class IMDBSkill extends DefaultSkillImpl {

    public IMDBSkill(){
        addRequestHandler(new IMDBRequestHandler());
    }

}
