package app.handler.impl;

import app.handler.IHandlerResponse;
import app.handler.Status;

/**
 * This class represents  an output to the system.<br>
 * It keeps track of the user input and the user ID
 */
public class HandlerResponseImpl implements IHandlerResponse {

    private String text;
    private String contentType;
    private String[] invokedSkills;
    private Status status;

    public HandlerResponseImpl(String text, String[] invokedSkills) {
        this.text = text;
        this.contentType = "text";
        this.invokedSkills = invokedSkills;
        this.status = Status.STATUS_200_OK;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Object getContent() {
        return text;
    }

    @Override
    public String getContentType(){return contentType;}
    public HandlerResponseImpl setContentType(String contentType){this.contentType=contentType;return this;}

    @Override
    public String[] getInvokedSkills() {
        return invokedSkills;
    }
}
