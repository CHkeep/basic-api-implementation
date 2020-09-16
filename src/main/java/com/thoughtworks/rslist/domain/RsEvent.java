package com.thoughtworks.rslist.domain;

public class RsEvent {
    private String eventName;
    private  String keyWords;

    public RsEvent(){

    }

    public RsEvent(String eventName, String keyWords) {
        this.eventName = eventName;
        this.keyWords = keyWords;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
}
