package com.SoftTech.SelfParkingLot_RestApi.security;

import java.util.Date;

public class TokenQueue {

    private String data=null;
    private String userName=null;
    private Date initialDate=new Date(System.currentTimeMillis());
    public TokenQueue next=null;
    public TokenQueue prev=null;

    public TokenQueue() {
    }

    public TokenQueue(String data,String userName) {
        this.data = data;
        this.userName = userName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
