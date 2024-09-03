package com.clean.architecture.application.user;

import com.clean.architecture.application.user.mapper.UserUseCaseMapper;
import com.clean.architecture.application.user.model.CreateUserCommand;
import com.clean.architecture.application.user.model.UpdateUserCommand;
import com.clean.architecture.domain.user.model.User;
import com.clean.architecture.domain.user.service.UserService;
import com.clean.architecture.presentation.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserService userService;
    private final UserUseCaseMapper userUseCaseMapper;

    @Transactional
    public UserResponse createUser(CreateUserCommand command) {
        // 1. check email
        userService.validateEmailUnique(command.email());

        // 2. save
        User user = User.createNew(command.username(),
                                   command.password(),
                                   command.email(),
                                   command.phoneNumber());

        User saveUser = userService.save(user);
        return userUseCaseMapper.toResponse(saveUser);
    }

    @Transactional
    public UserResponse updateUser(UpdateUserCommand command) {
        // 1. get User
        User exsistUser = userService.getUserById(command.id()); // throw NotFound Exception.

        // 2. update fields
        exsistUser.change(command.username(),
                          command.phoneNumber());

        // 3. update
        User updateUser = userService.save(exsistUser);
        return userUseCaseMapper.toResponse(updateUser);
    }
}
