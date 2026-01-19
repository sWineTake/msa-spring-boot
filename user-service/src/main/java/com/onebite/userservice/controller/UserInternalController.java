package com.onebite.userservice.controller;

import com.onebite.userservice.dto.AddActivityScoreRequestDto;
import com.onebite.userservice.dto.SignUpRequestDto;
import com.onebite.userservice.dto.UserResponseDto;
import com.onebite.userservice.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
public class UserInternalController {

    private final UserService userService;

    private UserInternalController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("userId") Long userId) {
        UserResponseDto userResponseDto = userService.selectUserById(userId);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsersByIds(@RequestParam List<Long> userIds) {
        List<UserResponseDto> usersByIds = userService.getUsersByIds(userIds);
        return ResponseEntity.ok().body(usersByIds);
    }

    @PostMapping("/activity-score/add")
    public ResponseEntity<Void> addActivityScore(@RequestBody AddActivityScoreRequestDto dto) {
        userService.addActivityScore(dto);
        return ResponseEntity.noContent().build();
    }

}
