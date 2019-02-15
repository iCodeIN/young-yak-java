package app.handler.impl;

import app.handler.IHandlerResponse;
import app.handler.Status;

/**
 * This class represents  an output to the system.<br>
 * It keeps track of the user input and the user ID
 */
public class HandlerResponseImpl implements IHandlerResponse {

    private String text;
    private ContentType contentType;
    private String[] invokedSkills;
    private Status status;

    public HandlerResponseImpl(String text, String[] invokedSkills) {
        this.text = text;
        this.contentType = ContentType.TEXT;
        this.invokedSkills = invokedSkills;
        this.status = Status.STATUS_200_OK;
    }

    @Override
    public Status getStatus() {
        return status;
    }
    public HandlerResponseImpl setStatus(Status status){this.status = status; return this;}

    @Override
    public Object getContent() {
        return text;
    }

    @Override
    public ContentType getContentType(){return contentType;}
    public HandlerResponseImpl setContentType(ContentType contentType){this.contentType=contentType;return this;}

    @Override
    public String[] getInvokedSkills() {
        return invokedSkills;
    }
}
