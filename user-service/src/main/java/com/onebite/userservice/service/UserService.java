package com.onebite.userservice.service;

import com.onebite.userservice.domain.User;
import com.onebite.userservice.dto.SignUpRequestDto;
import com.onebite.userservice.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {

        User user = new User(
            signUpRequestDto.getEmail(),
            signUpRequestDto.getName(),
            signUpRequestDto.getPassword()
        );

        this.userRepository.save(user);

    }
}
