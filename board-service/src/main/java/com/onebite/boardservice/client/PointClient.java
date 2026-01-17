package com.onebite.boardservice.client;

import com.onebite.boardservice.dto.DeductPointsRequestDto;
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

    public void deductPoints(Long userId, Integer amount) {

        DeductPointsRequestDto dto = new DeductPointsRequestDto(userId, amount);

        this.restClient.post().uri("/point/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();

    }

}
