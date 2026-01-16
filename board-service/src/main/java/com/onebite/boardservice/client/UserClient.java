package com.onebite.boardservice.client;

import com.onebite.boardservice.dto.UserResponseDto;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@Value("${client.user-service.url}") String url) {
        this.restClient = RestClient.builder().baseUrl(url).build();
    }

    public Optional<UserResponseDto> fetchUser(Long userId) {

        try {

            UserResponseDto userResponseDto = this.restClient.get()
                .uri("/users/{userId}", userId)
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

}
