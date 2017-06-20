package com.example.tahasaber.twsila;

/**
 * Created by mohamed on 20/06/17.
 */

public class MessageDataClass {
    public String msg_body;
    public String msg_publisher;

    public MessageDataClass(){}

    public MessageDataClass(String msg_body,String msg_publisher){
        this.msg_body=msg_body;
        this.msg_publisher=msg_publisher;
    }

}
