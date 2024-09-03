package com.clean.architecture.application.user.mapper;

import com.clean.architecture.application.user.model.CreateUserCommand;
import com.clean.architecture.application.user.model.UpdateUserCommand;
import com.clean.architecture.domain.user.model.User;
import com.clean.architecture.presentation.user.dto.UserRequest;
import com.clean.architecture.presentation.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserUseCaseMapper {

    // to Create Command
    CreateUserCommand toCreateCommand(UserRequest request);

    // to Update Command
    UpdateUserCommand toUpdateCommand(Long id, UserRequest request);

    // to Response
    UserResponse toResponse(User user);

}
