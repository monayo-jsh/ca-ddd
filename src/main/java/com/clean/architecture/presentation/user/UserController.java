package com.clean.architecture.presentation.user;

import com.clean.architecture.application.user.UserUseCase;
import com.clean.architecture.application.user.mapper.UserUseCaseMapper;
import com.clean.architecture.application.user.model.CreateUserCommand;
import com.clean.architecture.application.user.model.UpdateUserCommand;
import com.clean.architecture.presentation.user.dto.UserRequest;
import com.clean.architecture.presentation.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserUseCaseMapper userUseCaseMapper;

    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        CreateUserCommand command = userUseCaseMapper.toCreateCommand(request);
        UserResponse user = userUseCase.createUser(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        UpdateUserCommand command = userUseCaseMapper.toUpdateCommand(id, request);
        UserResponse user = userUseCase.updateUser(command);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(user);
    }

}
