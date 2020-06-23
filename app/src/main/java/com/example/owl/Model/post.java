package com.example.owl.Model;

import com.google.firebase.database.ServerValue;

public class post {
   private String name,title,description,userid,userphoto,picture;
    private Object timestamp;
    private String postkey;

    public post(String name,String title, String description, String userid, String userphoto, String picture) {
        this.title = title;
        this.name = name;
        this.description = description;
        this.userid = userid;
        this.userphoto = userphoto;
        this.picture = picture;
        this.timestamp = ServerValue.TIMESTAMP;
    }
    public post()
    {

    }

    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
