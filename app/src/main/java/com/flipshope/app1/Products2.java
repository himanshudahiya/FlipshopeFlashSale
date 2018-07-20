package com.flipshope.app1;

public class Products2 {
    private String productName;
    private String productImageURL;
    private String productPrice;
    private String productURL;
    private String pid;
    private String saledate;
    private String emid;
    private String time;
    private String cookie;
    private int isAdded = 0;
    public Products2(){}

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Products2(String productName, String productImageURL, String productPrice, String productURL, String pid, String saledate, String emid, String time, String cookie, int isAdded) {
        this.productName = productName;
        this.productImageURL = productImageURL;
        this.productPrice = productPrice;
        this.productURL = productURL;
        this.pid = pid;
        this.saledate = saledate;
        this.emid = emid;
        this.time = time;
        this.cookie = cookie;
        this.isAdded = isAdded;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public int getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(int isAdded) {
        this.isAdded = isAdded;
    }

    public void setSaledate(String saledate) {
        this.saledate = saledate;
    }

    public void setEmid(String emid) {
        this.emid = emid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public String getSaledate() {
        return saledate;
    }

    public String getEmid() {
        return emid;
    }

    public String getTime() {
        return time;
    }


    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
