package com.ad.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @JsonProperty(value = "login")
    private String login;

    private User(String login) {
        this();

        this.login = login;
    }

    public static User of(String login) {
        return new User(login);
    }

    public static User unauthorizedUser() {
        return new User("Unauthorized");
    }
}
