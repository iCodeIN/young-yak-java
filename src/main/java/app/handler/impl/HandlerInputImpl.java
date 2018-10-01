package app.handler.impl;

import app.handler.IHandlerInput;

/**
 * This class represents  an input to the system.<br>
 * It keeps track of the user input and the user ID
 */
public class HandlerInputImpl implements IHandlerInput {

    private String txt;
    private String userID;

    public HandlerInputImpl(String txt, String userID) {
        this.txt = txt;
        this.userID = userID;
    }

    @Override
    public Object getContent() {
        return txt;
    }

    @Override
    public String getUserID() {
        return userID;
    }

}
