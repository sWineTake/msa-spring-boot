package com.onebite.boardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserRequestDto {

    private Long userId;
    private String name;

    public static SaveUserRequestDto fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, SaveUserRequestDto.class);
        } catch (Exception e){
            throw new RuntimeException("JSON 파싱 실패");
        }
    }
}
