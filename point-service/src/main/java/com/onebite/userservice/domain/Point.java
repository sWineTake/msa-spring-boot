package com.onebite.userservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Integer pointId;

    private Long userId;

    private int amount;

    public static Point of(Long userId, Integer amount) {
        Point point = new Point();
        point.userId = userId;
        point.amount = amount;
        return point;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void deductAmount(int amount) {
        this.amount -= amount;
    }

}
