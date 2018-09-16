package app.web;

public class BotReply {
    private String id;
    private String txt;

    public BotReply(String id, String txt) {
        this.id = id;
        this.txt = txt;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return txt;
    }

}
