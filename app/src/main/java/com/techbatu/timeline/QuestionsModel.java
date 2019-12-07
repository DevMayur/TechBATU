package com.techbatu.timeline;

public class QuestionsModel extends BlogPostId{

    private String image_Uir,question,username,prn,timestamp;

    public QuestionsModel(String image_Uir, String question, String username, String prn, String timestamp) {
        this.image_Uir = image_Uir;
        this.question = question;
        this.username = username;
        this.prn = prn;
        this.timestamp = timestamp;
    }

    public String getImage_Uir() {
        return image_Uir;
    }

    public void setImage_Uir(String image_Uir) {
        this.image_Uir = image_Uir;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public QuestionsModel() {
    }
}
