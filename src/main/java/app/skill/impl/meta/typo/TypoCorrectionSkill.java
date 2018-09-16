package app.skill.impl.meta.typo;

import app.entities.DialogChunk;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.Status;
import app.skill.ISkill;
import app.web.BotController;

public class TypoCorrectionSkill implements ISkill {

    private final BotController botController;
    private BKTree<DialogChunk> bkTree;

    public TypoCorrectionSkill(BotController botController){
        this.botController = botController;
    }

    private void initBKTree(){
        bkTree = new BKTree<DialogChunk>(new BKTree.Metric<DialogChunk>() {
            @Override
            public int distance(DialogChunk obj0, DialogChunk obj1) {
                String s0 = obj0.getInput();
                String s1 = obj1.getInput();
                return java.lang.Math.min(Levenshtein.distance(s0, s1, false), 5);
            }
        });
        for(DialogChunk dc : botController.getDialogChunkRepository().findAll()){
            if(dc.getOutput().isEmpty())
                continue;
            bkTree.add(dc);
        }
    }

    @Override
    public boolean canHandle(IHandlerInput input) {
        if(bkTree == null)
            initBKTree();
        DialogChunk dc = new DialogChunk(input.getContent().toString(), "", "", 0);
        return bkTree.contains(dc, 2);
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        DialogChunk synomymChunk = null;
        for(DialogChunk dc : bkTree.get(new DialogChunk(input.getContent().toString(), "", "", 0), 2)){
            if(dc.getInput().equalsIgnoreCase(input.getContent().toString()))
                continue;
            if(synomymChunk == null || synomymChunk.getTimestamp() > dc.getTimestamp())
                synomymChunk = dc;
        }
        if(synomymChunk == null)
            return null;
        String synonymText = synomymChunk.getInput();
        return new IHandlerResponse() {
            @Override
            public Status getStatus() { return Status.STATUS_303_SEE_OTHER; }
            @Override
            public Object getContent() { return synonymText; }
        };
    }

}
