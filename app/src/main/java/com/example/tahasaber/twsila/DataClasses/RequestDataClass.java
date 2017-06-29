package com.example.tahasaber.twsila.DataClasses;

/**
 * Created by mohamed on 12/06/17.
 */

public class RequestDataClass {

    public String postId;
    public String requesterId;
    public String requesterName;
    public String postBody;

    public RequestDataClass() {
    }


    public RequestDataClass(String postId, String requesterId,String requesterName,String postBody){
        this.postId=postId;
        this.requesterId=requesterId;
        this.postBody=postBody;
        this.requesterName=requesterName;
    }

}
