package com.onebite.userservice.service;

import com.onebite.userservice.domain.Point;
import com.onebite.userservice.domain.PointRepository;
import com.onebite.userservice.dto.AddPointRequestDto;
import com.onebite.userservice.dto.DeductPointRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public void addPoints(AddPointRequestDto dto) {
        Point userPoint = pointRepository.findByUserId(dto.getUserId()).orElseGet(() -> Point.of(dto.getUserId(), 0));

        userPoint.addAmount(dto.getAmount());

        pointRepository.save(userPoint);
    }

    @Transactional
    public void deductPoints(DeductPointRequestDto dto) {
        Point userPoint = pointRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("user id not found"));

        userPoint.deductAmount(dto.getAmount());

        pointRepository.save(userPoint);
    }


}
