package app.entities;

import javax.persistence.*;

@Entity
public class DialogChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length=2048)
    @Lob
    private String input;

    @Column(length=2048)
    @Lob
    private String output;

    private String userID;

    private long timestamp;

    public DialogChunk(){}

    public DialogChunk(String input, String output, String userID, long timestamp){
        this.input = input;
        this.output = output;
        this.userID = userID;
        this.timestamp = timestamp;
    }

    public String getInput(){ return input; }

    public String getOutput(){ return output; }

    public String getUserID(){ return userID; }

    public long getTimestamp(){ return timestamp; }

}