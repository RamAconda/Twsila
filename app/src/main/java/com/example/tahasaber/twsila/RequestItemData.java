package com.example.tahasaber.twsila;

/**
 * Created by TahaSaber on 6/12/2017.
 */

public class RequestItemData {

    private String requesterName;
    private String postBody;
    private int status;

    public RequestItemData(String requesterName, String postBody){

        this.requesterName = requesterName;
        this.postBody = postBody;
        status = -1; // this variable expresses about request status 1- accepted or 2- rejected

    }

    public void chancgeStatus(int status){
        this.status = status;
    }

    public String getRequesterName(){
        return requesterName;
    }

    public String getPostBody(){
        return postBody;
    }

    public int getStatus(){
        return status;
    }



}
