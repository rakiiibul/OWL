package com.example.owl.Model;

import com.google.firebase.database.ServerValue;

public class Coment {
   private String content,username,uid,userimage;
    private Object timestamp;

    public Coment(String content, String username, String uid, String userimage) {
        this.content = content;
        this.username = username;
        this.uid = uid;
        this.userimage = userimage;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public Coment(){

}
}
