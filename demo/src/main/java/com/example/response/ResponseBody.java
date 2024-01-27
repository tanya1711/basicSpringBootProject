package com.example.response;


public class ResponseBody {

    private String url;
    private Integer statusCode;
    private String header;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return this.header;
    }


}
