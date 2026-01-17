package com.onebite.boardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddActivityScoreRequestDto {

    private Long userId;
    private int score;

}
