package com.flux.owa;

public class XLocale {

    XLocale(String i, String image, String name, String hint, String city, String agent, String agent_name, int price) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.hint = hint;
        this.city = city;
        this.agent = agent; //mail
        this.agent_name = agent_name;
        this.i = i;
    }



    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getHint() {
        return hint;
    }

    public int getPrice() {
        return price;
    }

    public String getCity() {
        return city;
    }

    public String getAgent() {
        return agent;
    }

    public String getAgent_name() {
        return agent_name;
    }


    private String image;
    private String name;
    private int price;
    private String hint;
    private String city;
    private String agent;
    private String agent_name;
    private String i;

    public String getI() {
        return i;
    }
}
