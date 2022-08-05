package com.example.help_m5.chat;

public class ChatItem {
    private String myMessage;
    private String myDateTime;

    private String oppoMessage;
    private String oppoDateTime;

    public ChatItem(String myMessage, String myDateTime, String oppoMessage, String oppoDateTime) {
        this.myMessage = myMessage;
        this.myDateTime = myDateTime;
        this.oppoMessage = oppoMessage;
        this.oppoDateTime = oppoDateTime;
    }

    public String getMyMessage() {
        return myMessage;
    }

    public String getMyDateTime() {
        return myDateTime;
    }

    public String getOppoMessage() {
        return oppoMessage;
    }

    public String getOppoDateTime() {
        return oppoDateTime;
    }

}
