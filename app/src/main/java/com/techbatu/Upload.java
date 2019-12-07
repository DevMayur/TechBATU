package com.techbatu;

public class Upload {

    private String name,iv_uri;


    public Upload() {

    }

    public Upload(String name, String iv_uri) {
        if(name.trim().equals("")){
            name = "no name";
        }

        this.name = name;
        this.iv_uri = iv_uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIv_uri() {
        return iv_uri;
    }

    public void setIv_uri(String iv_uri) {
        this.iv_uri = iv_uri;
    }
}
