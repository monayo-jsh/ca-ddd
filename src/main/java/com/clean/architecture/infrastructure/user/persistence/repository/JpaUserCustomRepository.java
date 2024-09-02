package com.clean.architecture.infrastructure.user.persistence.repository;

import static com.clean.architecture.infrastructure.user.persistence.entity.QUserEntity.userEntity;

import com.clean.architecture.infrastructure.user.persistence.dto.UserStatusUpdateRequest;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaUserCustomRepository {

    private final JPAQueryFactory queryFactory;

    public UserEntity findByEmail(String email) {
        return queryFactory.select(userEntity)
                           .from(userEntity)
                           .where(userEntity.email.eq(email))
                           .fetchOne();
    }

    public void updateStatus(UserStatusUpdateRequest user) {
        queryFactory.update(userEntity)
                    .set(userEntity.status, user.getStatus())
                    .set(userEntity.statusChangedAt, user.getStatusChangedAt())
                    .where(userEntity.id.eq(user.getId()))
                    .execute();
    }
}
