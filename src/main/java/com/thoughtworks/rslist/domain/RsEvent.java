package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Builder
public class RsEvent {
    @NotNull
    private String eventName;
//    @NotNull
    private String keyWords;
//    @NotNull
    private int userId;

    public RsEvent() {
    }

    public RsEvent(@NotNull String eventName, @NotNull String keyWords, @NotNull int userId) {
        this.eventName = eventName;
        this.keyWords = keyWords;
        this.userId = userId;
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

//    @JsonIgnore
    public int getUserId() {
        return userId;
    }
//    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
