import app.entities.DialogChunk;
import app.entities.DialogChunkRepository;
import app.web.BotController;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

@RunWith(SpringRunner.class)
public class TestCoverage {

    @Test
    public void testCoverage() throws FileNotFoundException {
        File root = new File("/home/joris/Downloads/srt");

        int N = 0;
        int M = 0;
        for(File srtFile : SRTFile.getFiles(root)){
            for(SRTFile.SRTEntry en : new SRTFile(srtFile).getEntries()){
                N++;
                String x = en.getText();
                String y = getReponse(x);
                M += (y.isEmpty() ? 0 : 1);
            }
            System.out.println(String.format("Responded to %d out of %d inputs.", M, N));
        }
    }

    public String getReponse(String in){
        String out = "";
        try {
            String urlTxt = "http://localhost:8080/respond?text=" + URLEncoder.encode(in);
            // System.out.println(urlTxt);
            URL url = new URL(urlTxt);
            Scanner scanner = new Scanner(url.openStream());
            String jsonOut = "";
            while(scanner.hasNextLine()){
                jsonOut += scanner.nextLine();
            }
            scanner.close();
            out =new JSONObject(jsonOut).getString("text");
        } catch (IOException e) { e.printStackTrace();}
        return out;
    }
}
