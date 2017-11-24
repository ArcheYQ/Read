package com.example.administrator.read.bean;

/**
 * Created by Administrator on 2017/11/24.
 */

public class PhotoCategory {

    private String name;

    private String url;

    public PhotoCategory(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}