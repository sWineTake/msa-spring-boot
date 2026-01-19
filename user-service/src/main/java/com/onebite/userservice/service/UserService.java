package com.onebite.userservice.service;

import com.onebite.userservice.client.PointClient;
import com.onebite.userservice.domain.User;
import com.onebite.userservice.domain.UserRepository;
import com.onebite.userservice.dto.AddActivityScoreRequestDto;
import com.onebite.userservice.dto.SignUpRequestDto;
import com.onebite.userservice.dto.UserResponseDto;
import com.onebite.userservice.event.UserSignedUpEvent;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointClient pointClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        User user = User.of(
            signUpRequestDto.getEmail(),
            signUpRequestDto.getName(),
            signUpRequestDto.getPassword()
        );

        User saveUser = this.userRepository.save(user);

        // 회원가입 완료 후 포인트 1000점 적립
        pointClient.addPoints(saveUser.getUserId(), 1000);

        // 회원가입 완료 이벤트 발행
        UserSignedUpEvent userSignedUpEvent = new UserSignedUpEvent(user.getUserId(), user.getName());
        this.kafkaTemplate.send("user.signed-up", toJsonString(userSignedUpEvent));

    }

    private String toJsonString(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    public UserResponseDto selectUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserResponseDto(
            user.getUserId(),
            user.getEmail(),
            user.getName()
        );
    }

    public List<UserResponseDto> getUsersByIds(List<Long> ids) {

        List<User> users = userRepository.findAllById(ids);

        return users.stream()
            .map(user ->
                new UserResponseDto(user.getUserId(), user.getEmail(), user.getName())
            ).toList();

    }

    @Transactional
    public void addActivityScore(AddActivityScoreRequestDto dto) {

        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾지 못하였습니다"));

        user.addActivityScore(dto.getScore());

        userRepository.save(user);

    }

}
