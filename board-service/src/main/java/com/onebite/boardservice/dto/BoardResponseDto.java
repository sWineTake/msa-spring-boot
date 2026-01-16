package com.onebite.boardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {

    private Long boardId;
    private String title;
    private String content;
    private UserResponseDto user;

}
