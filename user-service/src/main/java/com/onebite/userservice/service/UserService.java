package com.onebite.userservice.service;

import com.onebite.userservice.client.PointClient;
import com.onebite.userservice.domain.User;
import com.onebite.userservice.domain.UserRepository;
import com.onebite.userservice.dto.AddActivityScoreRequestDto;
import com.onebite.userservice.dto.LoginRequestDto;
import com.onebite.userservice.dto.LoginResponseDto;
import com.onebite.userservice.dto.SignUpRequestDto;
import com.onebite.userservice.dto.UserResponseDto;
import com.onebite.userservice.event.UserSignedUpEvent;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointClient pointClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${jwt.secret}")
    private String jwtSecret;

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

    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 스프링 시큐리티를 사용하지않아 이와같이 작업...원래는 시큐리티 라이브리에서 패스워드를 체크하는 방식으로 사용해야함..
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지않습니다.");
        }

        // jwt 생성
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .subject(user.getUserId().toString())
                .signWith(secretKey)
                .compact();

        return new LoginResponseDto(token);
    }
}
