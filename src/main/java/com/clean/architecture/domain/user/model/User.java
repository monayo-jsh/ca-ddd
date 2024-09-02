package com.clean.architecture.domain.user.model;

import com.clean.architecture.infrastructure.user.persistence.entity.UserStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter @Setter
public class User {

    @Comment("사용자 고유키")
    private Long id;

    @Comment("사용자 이름")
    private String username;

    @Comment("사용자 패스워드(암호화)")
    private String password;

    @Comment("사용자 이메일")
    private String email;

    @Comment("사용자 휴대전화번호")
    private String phoneNumber;

    @Comment("사용자 상태")
    private UserStatus status;

    @Comment("사용자 상태 변경일시")
    private LocalDateTime statusChangedAt;

    public User() {}

    public User(Long userId) {
        // 테스트 코드를 위한 생성자
        this.id = userId;
    }

    private User(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static User createNew(String username, String password, String email, String phoneNumber) {
        User user = new User(username, password, email, phoneNumber);
        user.markAsActive();
        return user;
    }

    public void markAsActive() {
        changeStatus(UserStatus.ACTIVE);
    }

    public void markAsDeleted() {
        changeStatus(UserStatus.DELETED);
    }

    public void changeStatus(UserStatus userStatus) {
        this.status = userStatus;
        this.statusChangedAt = now();
    }

    private LocalDateTime now() {
        return LocalDateTime.now();
    }
}
