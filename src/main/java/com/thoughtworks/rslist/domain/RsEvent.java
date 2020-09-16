package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

public class RsEvent {
    private String eventName;
    private String keyWords;
    @Valid
    private User user;

    public RsEvent(){

    }

    public RsEvent(String eventName, String keyWords, @Valid User user) {
        this.eventName = eventName;
        this.keyWords = keyWords;
        this.user = user;
    }
    @JsonIgnore
    public User getUser() {
        return user;
    }
    @JsonProperty
    public void setUser(@Valid User user) {
        this.user = user;
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
