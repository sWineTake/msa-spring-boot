package com.onebite.userservice.controller;

import com.onebite.userservice.dto.SignUpRequestDto;
import com.onebite.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp (
        @RequestBody SignUpRequestDto signUpRequestDto
    ) {
        userService.signUp(signUpRequestDto);
        return ResponseEntity.noContent().build();
    }

}
