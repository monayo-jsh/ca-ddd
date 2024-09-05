package com.clean.architecture.infrastructure.user.persistence.mapper;

import com.clean.architecture.infrastructure.user.persistence.dto.UserStatusUpdateRequest;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE // 매핑 대상이 없는 경우 무시하고 매핑
)
public interface UserUpdateRequestMapper {

    // Entity -> Update Request
    UserStatusUpdateRequest toStatusUpdateRequest(UserEntity user);

}
