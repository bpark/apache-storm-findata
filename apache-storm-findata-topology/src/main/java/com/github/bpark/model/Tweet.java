package com.github.bpark.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("id_str")
    private String id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("user")
    private User user;


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "createdAt='" + createdAt + '\'' +
                ", id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", user=" + user +
                '}';
    }
}
