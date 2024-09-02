package com.clean.architecture.infrastructure.user.persistence.mapper;

import com.clean.architecture.domain.user.model.User;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-02T14:32:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public User toDomain(UserEntity source) {
        if ( source == null ) {
            return null;
        }

        User user = new User();

        user.setId( source.getId() );
        user.setUsername( source.getUsername() );
        user.setPassword( source.getPassword() );
        user.setEmail( source.getEmail() );
        user.setPhoneNumber( source.getPhoneNumber() );
        user.setStatus( source.getStatus() );
        user.setStatusChangedAt( source.getStatusChangedAt() );

        return user;
    }

    @Override
    public UserEntity toEntity(User source) {
        if ( source == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( source.getId() );
        userEntity.username( source.getUsername() );
        userEntity.password( source.getPassword() );
        userEntity.email( source.getEmail() );
        userEntity.phoneNumber( source.getPhoneNumber() );
        userEntity.status( source.getStatus() );
        userEntity.statusChangedAt( source.getStatusChangedAt() );

        return userEntity.build();
    }
}
