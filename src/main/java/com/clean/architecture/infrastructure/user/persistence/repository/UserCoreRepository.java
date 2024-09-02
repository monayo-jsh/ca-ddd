package com.clean.architecture.infrastructure.user.persistence.repository;

import com.clean.architecture.infrastructure.user.persistence.dto.UserStatusUpdateRequest;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCoreRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserCustomRepository jpaUserCustomRepository;

    @Override
    public UserEntity save(UserEntity userEntity) {
        return jpaUserRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email can not be empty");
        }

        UserEntity foundUserEntity = jpaUserCustomRepository.findByEmail(email);
        return Optional.ofNullable(foundUserEntity);
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId can not be null");
        }

        return jpaUserRepository.findById(userId);
    }

    public void updateStatus(UserStatusUpdateRequest user) {
        jpaUserCustomRepository.updateStatus(user);
    }
}
