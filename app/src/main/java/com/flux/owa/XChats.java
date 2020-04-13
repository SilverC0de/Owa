package com.flux.owa;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class XChats extends RealmObject{

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }


    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    @PrimaryKey private long stamp;
    private String locale;
    private String message;
    private String type;
    private String agent;
}
