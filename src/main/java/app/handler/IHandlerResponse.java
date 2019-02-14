package app.handler;

/**
 * This interface represents  an output to the system.<br>
 * It keeps track of the user input and the user ID
 */
public interface IHandlerResponse {

    /**
     * Get the status of the response
     * <br> The status indicates how the reply was obtained
     *
     * @return
     */
    Status getStatus();

    /**
     * Get the output
     *
     * @return the output
     */
    Object getContent();

    /**
     * Get the content type
     */
    String getContentType();

    /**
     * Get the skills (names) invoked in generating this output
     * @return
     */
    String[] getInvokedSkills();
}
