package com.onebite.boardservice.service;

import com.onebite.boardservice.domain.User;
import com.onebite.boardservice.domain.UserRepository;
import com.onebite.boardservice.dto.SaveUserRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(SaveUserRequestDto dto) {
        User user = User.of(dto.getUserId(), dto.getName());
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}
