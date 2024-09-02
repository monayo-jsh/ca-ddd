package com.clean.architecture.infrastructure.user.persistence.mapper;

import com.clean.architecture.infrastructure.user.persistence.dto.UserStatusUpdateRequest;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-02T14:32:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class UserStatusUpdateRequestMapperImpl implements UserStatusUpdateRequestMapper {

    @Override
    public UserStatusUpdateRequest toRequest(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest();

        userStatusUpdateRequest.setId( userEntity.getId() );
        userStatusUpdateRequest.setStatus( userEntity.getStatus() );
        userStatusUpdateRequest.setStatusChangedAt( userEntity.getStatusChangedAt() );

        return userStatusUpdateRequest;
    }
}
