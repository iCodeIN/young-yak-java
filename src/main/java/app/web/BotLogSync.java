package app.web;

import app.entities.DialogChunk;
import app.entities.DialogChunkRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class regularly syncs the current logs to file and vice versa.<br>
 * This mechanism ensures minimal traffic.
 */
@Component
public class BotLogSync {

    @Autowired
    private DialogChunkRepository dialogChunkRepository;

    private int lastLogCount;
    private boolean isSaveEnabled = false;

    @PostConstruct
    private void setup(){
        // add hook
        addShutdownHook();

        // read repository
        try { read(); } catch (FileNotFoundException e) { }

        // setup timer
        isSaveEnabled = true;
        lastLogCount = 0;
        new Thread(){
            public void run(){
                while(isSaveEnabled){
                    // sleep
                    try { Thread.sleep(60 * 60 * 1000); } catch (InterruptedException e) { }
                    // save
                    try { save(); } catch (IOException e) { }
                }
            }
        }.start();
    }

    private void addShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
               preDestroy();
            }
        });
    }

    @PreDestroy
    private void preDestroy() {
        // disable save
        isSaveEnabled = false;
        // write here any instructions that should be executed
        try { save(); } catch (IOException e) { }
    }

    private void save() throws IOException {
        if(dialogChunkRepository != null && lastLogCount == dialogChunkRepository.count())
            return;

        File outFile = new File(System.getProperty("user.home"),"alice.db.json");
        FileWriter fileWriter = new FileWriter(outFile);

        List<JSONObject> tmp = new ArrayList<>();
        for(DialogChunk dc : dialogChunkRepository.findAll()) {
            tmp.add(new JSONObject()
                    .put("input", dc.getInput())
                    .put("output", dc.getOutput())
                    .put("userid", dc.getUserID())
                    .put("timestamp", dc.getTimestamp()));
        }
        lastLogCount = tmp.size();

        fileWriter.write(new JSONArray(tmp).toString(3).toString());
        fileWriter.close();
    }

    private void read() throws FileNotFoundException {
        File inFile = new File(System.getProperty("user.home"),"alice.db.json");
        if(!inFile.exists())
            return;

        String txt = "";
        Scanner scanner = new Scanner(inFile);
        while (scanner.hasNextLine())
            txt += scanner.nextLine();
        scanner.close();

        JSONArray object = new JSONArray(txt);
        for (int i=0;i<object.length();i++) {
            JSONObject dialogChunk = object.getJSONObject(i);
            dialogChunkRepository.save(new DialogChunk(dialogChunk.getString("input"),
                    dialogChunk.getString("output"),
                    dialogChunk.has("userid") ? dialogChunk.getString("userid") : "",
                    dialogChunk.getLong("timestamp")));
        }
    }
}
