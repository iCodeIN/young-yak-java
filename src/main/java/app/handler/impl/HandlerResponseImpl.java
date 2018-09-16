package app.handler.impl;

import app.handler.IHandlerResponse;
import app.handler.Status;

public class HandlerResponseImpl implements IHandlerResponse {

    private String text;
    private Status status;

    public HandlerResponseImpl(String text) {
        this.text = text;
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
}
