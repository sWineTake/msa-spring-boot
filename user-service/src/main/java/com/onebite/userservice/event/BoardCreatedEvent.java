package com.onebite.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreatedEvent {

    private Long userId;

    public static BoardCreatedEvent fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, BoardCreatedEvent.class);
        } catch (Exception e){
            throw new RuntimeException("JSON 파싱 실패");
        }
    }

}
