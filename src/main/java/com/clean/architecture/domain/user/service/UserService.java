package com.clean.architecture.domain.user.service;

import com.clean.architecture.domain.user.model.User;
import com.clean.architecture.domain.user.repository.UserRepository;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.infrastructure.user.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                                              .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDomain(userEntity);
    }

    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.toDomain(savedUserEntity);
    }

    public void validateEmailUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
    }

}
