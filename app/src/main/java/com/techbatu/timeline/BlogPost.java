package com.techbatu.timeline;


import java.util.Date;

public class BlogPost extends BlogPostId {

    public String username, image_Uir, description, prn, timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public BlogPost() {

    }

    public BlogPost(String username, String image_Uir, String description, String prn,String timestamp) {

        this.username = username;
        this.image_Uir = image_Uir;
        this.description = description;
        this.prn = prn;
        this.timestamp = timestamp;
    }



    public String getImage_Uir() {
        return image_Uir;
    }

    public void setImage_Uir(String image_Uir) {
        this.image_Uir = image_Uir;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }
}


