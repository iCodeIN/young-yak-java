package app.controller.api;

import app.entities.DialogChunk;
import app.entities.DialogChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class ApiDataController {

    @Autowired
    private DialogChunkRepository dialogChunkRepository;

    public ApiDataController(){}

    @CrossOrigin()
    @RequestMapping("/history")
    public List<DialogChunk> getHistory(){
        List<DialogChunk> dialogChunkList = new ArrayList<>();
        for(DialogChunk dialogChunk : dialogChunkRepository.findAll()){
            dialogChunkList.add(dialogChunk);
        }
        return dialogChunkList;
    }

}
