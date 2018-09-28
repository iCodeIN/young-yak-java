package app.entities;

import javax.persistence.*;

/**
 * Entity used for logging <br>
 * Logging enables ISkill implementations to correct spelling mistakes
 */
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

    private String[] invokedSkills;

    private String userID;

    private long timestamp;

    public DialogChunk(){}

    /**
     * Constructor
     * @param input the user input
     * @param output the user output
     * @param invokedSkills the skills invoked to generate this output
     * @param userID the user ID
     * @param timestamp the time at which the input was given to the system
     */
    public DialogChunk(String input, String output, String[] invokedSkills, String userID, long timestamp){
        this.input = input;
        this.output = output;
        this.invokedSkills = invokedSkills;
        this.userID = userID;
        this.timestamp = timestamp;
    }

    /**
     * Get the input of this DialogChunk
     * @return the input of this DialogChunk
     */
    public String getInput(){ return input; }

    /**
     * Get the output of this DialogChunk
     * @return the output of this DialogChunk
     */
    public String getOutput(){ return output; }

    /**
     * Get the user ID of this DialogChunk
     * @return the user ID of this DialogChunk
     */
    public String getUserID(){ return userID; }

    /**
     * Get the timestamp of this DialogChunk
     * @return the timestamp of this DialogChunk
     */
    public long getTimestamp(){ return timestamp; }

    public String[] getInvokedSkills(){ return invokedSkills; }

}