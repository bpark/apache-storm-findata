package com.github.bpark.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("id_str")
    private String id;

    @JsonProperty("followers_count")
    private Integer followersCount;

    @JsonProperty("friends_count")
    private Integer friendsCount;

    @JsonProperty("created_at")
    private String createdAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", followersCount=" + followersCount +
                ", friendsCount=" + friendsCount +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
