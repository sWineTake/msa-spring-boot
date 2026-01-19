package com.onebite.userservice.consumer;

import com.onebite.userservice.dto.AddActivityScoreRequestDto;
import com.onebite.userservice.event.BoardCreatedEvent;
import com.onebite.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@RequiredArgsConstructor
public class BoardCreatedEventConsumer {

    private final UserService userService;

    @KafkaListener(
        topics = "board.created",
        groupId = "user-service"
    )
    public void consume(String message) {
        System.out.println("======== kafka 메세지 수신 ========");
        BoardCreatedEvent event = BoardCreatedEvent.fromJson(message);

        // 게시글 작성 시 활동 점수 10점 추가
        AddActivityScoreRequestDto dto = new AddActivityScoreRequestDto(event.getUserId(), 10);

        userService.addActivityScore(dto);

    }

}
