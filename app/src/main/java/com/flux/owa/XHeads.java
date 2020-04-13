package com.flux.owa;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class XHeads extends RealmObject {
    @PrimaryKey private String locale;
    private String name;
    private String agent;
    private String details;
    private boolean notification;


    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setLink(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getLocale() {
        return locale;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
}
