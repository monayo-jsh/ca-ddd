package com.clean.architecture.domain.user.repository;

import com.clean.architecture.domain.user.entity.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);
    Optional<User> findByEmail(String email);

}
