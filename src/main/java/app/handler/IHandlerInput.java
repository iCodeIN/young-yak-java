package app.handler;

/**
 * This interface represents  an input to the system.<br>
 * It keeps track of the user input and the user ID
 */
public interface IHandlerInput {

    /**
     * Gets the user ID of the input
     *
     * @return the user ID
     */
    String getUserID();

    /**
     * Gets the user input
     *
     * @return the user input
     */
    Object getContent();

}
