package com.onebite.userservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String name;
    private String password;
    private int activityScore = 0;

    public static User of(String email, String name, String password) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.password = password;

        return user;
    }

    public void addActivityScore(int score) {
        this.activityScore += score;
    }

}
