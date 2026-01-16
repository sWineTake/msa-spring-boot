package com.onebite.userservice.client;

import com.onebite.userservice.dto.AddPointRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PointClient {

    private final RestClient restClient;
    public PointClient(@Value("${client.point-service.url}") String url) {
        this.restClient = RestClient.builder().baseUrl(url).build();
    }

    public void addPoints(Long userId, Integer amount) {
        AddPointRequestDto addPointRequestDto =
                new AddPointRequestDto(userId, amount);

        this.restClient.post()
            .uri("/point/add")
            .contentType(MediaType.APPLICATION_JSON)
            .body(addPointRequestDto)
            .retrieve()
            .toBodilessEntity();
    }



}
