package com.clean.architecture.infrastructure.user.repository;

import com.clean.architecture.domain.user.entity.User;
import com.clean.architecture.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCoreRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserCustomRepository userCustomRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email can not be empty");
        }

        User foundUser = userCustomRepository.findByEmail(email);
        return Optional.ofNullable(foundUser);
    }

}
