package app.controller;

/**
 * This class is turned into JSON and presented as output of the API.
 */
public class BotReply {

    private String id;
    private String txt;

    /**
     * Constructor
     *
     * @param id  the user ID
     * @param txt the output text
     */
    public BotReply(String id, String txt) {
        this.id = id;
        this.txt = txt;
    }

    /**
     * Get the user ID
     *
     * @return the user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get the output text
     *
     * @return the output text
     */
    public String getText() {
        return txt;
    }

}
