package com.example.tahasaber.twsila;

/**
 * Created by mohamed on 20/06/17.
 */

public class MessageDataClass {
    private String msgBody;
    private String msgPublisher;

    public MessageDataClass() {
    }

    public MessageDataClass(String msgBody, String msgPublisher) {
        this.msgBody = msgBody;
        this.msgPublisher = msgPublisher;

    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public void setMsgPublisher(String msgPublisher) {
        this.msgPublisher = msgPublisher;
    }


    public String getMsgBody() {
        return msgBody;
    }

    public String getMsgPublisher() {
        return msgPublisher;
    }

}
