package com.clean.architecture.infrastructure.user.repository;

import com.clean.architecture.domain.user.entity.QUser;
import com.clean.architecture.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    public User findByEmail(String email) {
        QUser user = QUser.user;

        return queryFactory.select(user)
                           .from(user)
                           .where(user.email.eq(email))
                           .fetchOne();
    }

}
