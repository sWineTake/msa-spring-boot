package com.onebite.userservice.controller;

import com.onebite.userservice.dto.AddPointRequestDto;
import com.onebite.userservice.dto.DeductPointRequestDto;
import com.onebite.userservice.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/point")
@RequiredArgsConstructor
public class PointInternalController {

    private final PointService pointService;

    @PostMapping("/add")
    public ResponseEntity<Void> addPoint(@RequestBody AddPointRequestDto point) {
        pointService.addPoints(point);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deduct")
    public ResponseEntity<Void> deductPoint(@RequestBody DeductPointRequestDto dto) {
        pointService.deductPoints(dto);
        return ResponseEntity.noContent().build();
    }

}
