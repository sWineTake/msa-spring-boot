package com.onebite.boardservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@Value("${client.user-service.url}") String url) {
        this.restClient = RestClient.builder().baseUrl(url).build();
    }

    /*
    카프카로 Users 테이블을 Board서비스에서 관리하게되면서 따로 조회하는 로직을 사용하지않게됨 기록용으로 주석 처리

    public Optional<UserResponseDto> fetchUser(Long userId) {

        try {

            UserResponseDto userResponseDto = this.restClient.get()
                .uri("/internal/users/{userId}", userId)
                .retrieve()
                .body(UserResponseDto.class);

            return Optional.of(userResponseDto);

        } catch (RestClientException e) {
            // 로깅 코드 추가 예정
            return Optional.empty();
        }

    }

    public List<UserResponseDto> fetchUsersByIds(List<Long> ids) {

        try {

            return this.restClient.get()
                    .uri(uriBuilder ->
                        uriBuilder.path("/users").queryParam("userIds", ids).build()
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

        } catch (RestClientException e) {
            // 로깅 코드 추가 예정
            return Collections.emptyList();
        }

    }

    public void addActivityScore(Long userId, int score) {

        AddActivityScoreRequestDto dto = new AddActivityScoreRequestDto(userId, score);

        this.restClient.post()
                .uri("/users/activity-score/add", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();

    }*/

}
