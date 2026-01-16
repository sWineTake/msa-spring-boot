package com.onebite.userservice.service;

import com.onebite.userservice.domain.User;
import com.onebite.userservice.dto.SignUpRequestDto;
import com.onebite.userservice.domain.UserRepository;
import com.onebite.userservice.dto.UserResponseDto;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

}
