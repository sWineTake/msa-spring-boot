package com.onebite.boardservice.consumer;

import com.onebite.boardservice.dto.SaveUserRequestDto;
import com.onebite.boardservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@RequiredArgsConstructor
public class UserCreatedEventConsumer {

    private final UserService userService;

    @KafkaListener(
            topics = "user.signed-up",
            groupId = "user-service"
    )
    public void consume(String message) {
        System.out.println("======== kafka 메세지 수신 ========");

        userService.save(SaveUserRequestDto.fromJson(message));
    }

}
