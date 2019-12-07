package com.techbatu.timeline;

public class AnswerModel extends BlogPostId {
    private String answer,image_Uir,prn,timestamp,username;

    public AnswerModel(String answer, String image_Uir, String prn, String timestamp, String username) {
        this.answer = answer;
        this.image_Uir = image_Uir;
        this.prn = prn;
        this.timestamp = timestamp;
        this.username = username;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImage_Uir() {
        return image_Uir;
    }

    public void setImage_Uir(String image_Uir) {
        this.image_Uir = image_Uir;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AnswerModel() {
    }
}
